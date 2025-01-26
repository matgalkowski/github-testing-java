package api.tests;

import static org.awaitility.Awaitility.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import api.clients.GithubApiClient;
import api.enums.WorkflowStatuses;
import api.models.github.GithubWorkflowRun;
import api.models.github.requests.GithubTriggerWorkflowRequest;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import org.testng.annotations.*;

public class WorkflowTests {
  public static final String BASE_BRANCH_NAME = "main";
  public static final String TEST_WORKFLOW_FILE_NAME = "sample-workflow.yml";
  private final GithubApiClient githubApiClient = new GithubApiClient();
  private int initialRunsCount;

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    initialRunsCount = getRunsCount();
  }

  @Test(groups = {"workflow"})
  public void shouldTriggerWorkflow() {
    GithubTriggerWorkflowRequest triggerWorkflowRequest =
        GithubTriggerWorkflowRequest.builder().ref(BASE_BRANCH_NAME).build();
    githubApiClient
        .triggerWorkflow(triggerWorkflowRequest, TEST_WORKFLOW_FILE_NAME)
        .then()
        .statusCode(204);

    await()
        .atMost(Duration.ofSeconds(10))
        .pollInterval(Duration.ofSeconds(1))
        .untilAsserted(() -> assertThat(getRunsCount(), equalTo(initialRunsCount + 1)));
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    List<GithubWorkflowRun> workflowRuns =
        githubApiClient
            .getWorkflowRuns(TEST_WORKFLOW_FILE_NAME)
            .then()
            .extract()
            .jsonPath()
            .getList("workflow_runs", GithubWorkflowRun.class);
    GithubWorkflowRun latestRun = getLatestRun(workflowRuns);

    await()
        .atMost(Duration.ofSeconds(60))
        .pollDelay(Duration.ofSeconds(5))
        .pollInterval(Duration.ofSeconds(3))
        .untilAsserted(
            () ->
                assertThat(
                    getRunStatus(latestRun.getId()), equalTo(WorkflowStatuses.COMPLETED.getValue())));

    githubApiClient.deleteWorkflowRun(latestRun.getId()).then().statusCode(204);
  }

  private int getRunsCount() {
    return githubApiClient
        .getWorkflowRuns(TEST_WORKFLOW_FILE_NAME)
        .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList("workflow_runs")
        .size();
  }

  private String getRunStatus(String runId) {
    return githubApiClient.getWorkflowRun(runId).as(GithubWorkflowRun.class).getStatus();
  }

  private GithubWorkflowRun getLatestRun(List<GithubWorkflowRun> workflowRuns) {
    return workflowRuns.stream()
        .max(Comparator.comparingInt(GithubWorkflowRun::getRunNumber))
        .orElseThrow(() -> new RuntimeException("No workflow runs found"));
  }
}
