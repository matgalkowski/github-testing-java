package api.tests;

import static org.hamcrest.Matchers.equalTo;

import api.clients.GithubApiClient;
import api.enums.IssueState;
import api.models.github.requests.GithubCreateIssueRequest;
import org.testng.annotations.*;

public class IssueTests {
  private final GithubApiClient githubApiClient = new GithubApiClient();
  private int issueNumber;

  @Test(groups = {"issue"})
  public void shouldCreateIssue() {
    GithubCreateIssueRequest createIssueRequest =
        GithubCreateIssueRequest.builder().title("Bug report").build();
    issueNumber =
        githubApiClient
            .createIssue(createIssueRequest)
            .then()
            .statusCode(201)
            .extract()
            .jsonPath()
            .getInt("number");

    githubApiClient
        .getIssue(issueNumber)
        .then()
        .statusCode(200)
        .body("state", equalTo(IssueState.OPEN.getValue()));
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    githubApiClient.updateIssueState(issueNumber, IssueState.CLOSED).then().statusCode(200);
  }
}
