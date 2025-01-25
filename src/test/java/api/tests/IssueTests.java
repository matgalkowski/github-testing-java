package api.tests;

import static org.hamcrest.Matchers.equalTo;

import org.testng.annotations.*;

import api.clients.GithubApiClient;
import api.enums.IssueState;
import api.models.github.GithubCreateIssueRequest;

public class IssueTests {
    private GithubApiClient githubApiClient = new GithubApiClient();
    private int issueNumber;

    @Test(groups = { "issue" })
    public void shouldCreateIssue() {
        GithubCreateIssueRequest createIssueRequest = new GithubCreateIssueRequest("Bug report");
        issueNumber = githubApiClient.createIssue(createIssueRequest)
                .then().statusCode(201)
                .extract().jsonPath().getInt("number");

        githubApiClient.getIssue(issueNumber)
                .then().statusCode(200)
                .body("state", equalTo(IssueState.open.name()));
    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        githubApiClient.updateIssueState(issueNumber, IssueState.closed)
                .then().statusCode(200);
    }
}
