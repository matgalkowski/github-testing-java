package api.tests;

import static org.hamcrest.Matchers.*;

import api.clients.GithubApiClient;
import api.enums.IssueStates;
import api.models.github.requests.GithubCreateBranchRequest;
import api.models.github.requests.GithubCreateCommitRequest;
import api.models.github.requests.GithubCreatePrRequest;
import api.models.github.requests.GithubUpdateReferenceRequest;
import api.utils.DataUtils;
import org.apache.commons.lang3.Range;
import org.testng.annotations.*;

public class PullRequestTests {

  public static final String BASE_BRANCH_NAME = "main";
  private final GithubApiClient githubApiClient = new GithubApiClient();
  private String newBranchName;
  private int newPrNumber;

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    newBranchName = DataUtils.generateRandomString(Range.between(5, 15));
    String fileName = DataUtils.generateRandomString(Range.between(5, 15)) + ".txt";

    // Get latest commit in the repo
    String latestCommitSha =
        githubApiClient
            .getBranch(BASE_BRANCH_NAME)
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getString("commit.sha");

    // Create branch
    GithubCreateBranchRequest createBranchRequest =
        GithubCreateBranchRequest.builder()
            .ref("refs/heads/" + newBranchName)
            .sha(latestCommitSha)
            .build();
    String branchRef =
        githubApiClient
            .createBranch(createBranchRequest)
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getString("ref");

    // Create git tree
    String treeSha =
        githubApiClient
            .createTreeForFile(fileName, "Hello, world!", latestCommitSha)
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getString("sha");

    // Create commit
    GithubCreateCommitRequest createCommitRequest =
        GithubCreateCommitRequest.builder()
            .message("New commit")
            .tree(treeSha)
            .parent(latestCommitSha)
            .build();
    String newCommitSha =
        githubApiClient
            .createCommit(createCommitRequest)
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getString("sha");

    // Update branch head to the new commit
    githubApiClient
        .updateBranchRef(
            branchRef, GithubUpdateReferenceRequest.builder().sha(newCommitSha).build())
        .then()
        .statusCode(200);
  }

  @Test(groups = {"pr"})
  public void shouldCreatePullRequest() {
    GithubCreatePrRequest createPrDto =
        GithubCreatePrRequest.builder()
            .title("Awesome PullRequest")
            .body("Just testing")
            .head(newBranchName)
            .base(BASE_BRANCH_NAME)
            .build();
    newPrNumber =
        githubApiClient
            .createPullRequest(createPrDto)
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getInt("number");

    githubApiClient
        .getPullRequest(newPrNumber)
        .then()
        .statusCode(200)
        .body("state", equalTo(IssueStates.OPEN.getValue()));
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    githubApiClient.updatePullRequestState(newPrNumber, IssueStates.CLOSED).then().statusCode(200);
    githubApiClient.deleteBranch(newBranchName).then().statusCode(204);
  }
}
