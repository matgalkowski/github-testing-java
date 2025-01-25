package api.tests;

import java.util.List;

import org.apache.commons.lang3.Range;
import org.testng.annotations.*;

import api.clients.GithubApiClient;
import api.enums.IssueState;
import api.models.github.GithubCreateBranchRequest;
import api.models.github.GithubCreateCommitRequest;
import api.models.github.GithubCreatePrRequest;
import api.models.github.GithubUpdateReferenceRequest;
import api.utils.DataUtils;
import api.utils.TestData;

import static org.hamcrest.Matchers.*;

public class PullRequestTests {

        private GithubApiClient githubApiClient = new GithubApiClient();
        private String newBranchName;
        private String latestCommitSha;
        private String fileName;
        private int newPrNumber;

        @BeforeMethod(alwaysRun = true)
        public void beforeMethod() {
                newBranchName = DataUtils.generateRandomString(Range.between(5, 15));
                fileName = DataUtils.generateRandomString(Range.between(5, 15)) + ".txt";

                // Get latest commit in the repo
                latestCommitSha = githubApiClient.getBranch(TestData.BASE_BRANCH_NAME)
                                .then().statusCode(200)
                                .extract().jsonPath().getString("commit.sha");

                // Create branch
                GithubCreateBranchRequest createBranchRequest = new GithubCreateBranchRequest(newBranchName,
                                latestCommitSha);
                String branchRef = githubApiClient
                                .createBranch(createBranchRequest)
                                .then().statusCode(201)
                                .extract().jsonPath().getString("ref");

                // Create git tree
                String treeSha = githubApiClient.createTreeForFile(fileName, "Hello, world!", latestCommitSha)
                                .then().statusCode(201)
                                .extract().jsonPath().getString("sha");

                // Create commit
                GithubCreateCommitRequest createCommitRequest = new GithubCreateCommitRequest("New commit", treeSha,
                                List.of(latestCommitSha));
                String newCommitSha = githubApiClient
                                .createCommit(createCommitRequest)
                                .then().statusCode(201)
                                .extract().jsonPath().getString("sha");

                // Update branch head to the new commit
                githubApiClient.updateBranchRef(branchRef, new GithubUpdateReferenceRequest(newCommitSha, false))
                                .then().statusCode(200);
        }

        @Test(groups = { "pr" })
        public void shouldCreatePullRequest() {
                GithubCreatePrRequest createPrDto = new GithubCreatePrRequest("Awesome PullRequest", "Just testing",
                                newBranchName, TestData.BASE_BRANCH_NAME);
                newPrNumber = githubApiClient.createPullRequest(createPrDto)
                                .then().statusCode(201)
                                .extract().jsonPath().getInt("number");

                githubApiClient.getPullRequest(newPrNumber)
                                .then().statusCode(200)
                                .body("state", equalTo(IssueState.open.name()));
        }

        @AfterMethod(alwaysRun = true)
        public void afterMethod() {
                githubApiClient.updatePullRequestState(newPrNumber, IssueState.closed)
                                .then().statusCode(200);
                githubApiClient.deleteBranch(newBranchName)
                                .then().statusCode(204);
        }
}
