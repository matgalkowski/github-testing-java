package api.tests;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import org.testng.annotations.*;

import api.clients.GithubApiClient;
import api.enums.WorkflowStatus;
import api.models.github.GithubTriggerWorkflowRequest;
import api.models.github.WorkflowRun;
import api.utils.TestData;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.awaitility.Awaitility.*;

public class WorkflowTests {
        private GithubApiClient githubApiClient = new GithubApiClient();
        private int initialRunsCount;

        @BeforeMethod(alwaysRun = true)
        public void beforeMethod() {
                initialRunsCount = getRunsCount();
        }

        @Test(groups = { "workflow" })
        public void shouldTriggerWorkflow() {
                GithubTriggerWorkflowRequest triggerWorkflowRequest = new GithubTriggerWorkflowRequest(
                                TestData.BASE_BRANCH_NAME);
                githubApiClient.triggerWorkflow(triggerWorkflowRequest, TestData.TEST_WORKFLOW_FILE_NAME)
                                .then().statusCode(204);

                await().atMost(Duration.ofSeconds(10))
                                .pollInterval(Duration.ofSeconds(1))
                                .untilAsserted(() -> assertThat(getRunsCount(), equalTo(initialRunsCount + 1)));
        }

        @AfterMethod(alwaysRun = true)
        public void afterMethod() {
                List<WorkflowRun> workflowRuns = githubApiClient.getWorkflowRuns(TestData.TEST_WORKFLOW_FILE_NAME)
                                .then().extract().jsonPath().getList("workflow_runs", WorkflowRun.class);
                WorkflowRun latestRun = getLatestRun(workflowRuns);

                await().atMost(Duration.ofSeconds(60))
                                .pollDelay(Duration.ofSeconds(5))
                                .pollInterval(Duration.ofSeconds(3))
                                .untilAsserted(() -> assertThat(
                                                getRunStatus(latestRun.id), equalTo(WorkflowStatus.completed)));

                githubApiClient.deleteWorkflowRun(latestRun.id)
                                .then().statusCode(204);
        }

        private int getRunsCount() {
                return githubApiClient.getWorkflowRuns(TestData.TEST_WORKFLOW_FILE_NAME)
                                .then().statusCode(200)
                                .extract().jsonPath().getList("workflow_runs").size();
        }

        private WorkflowStatus getRunStatus(String runId) {
                String status = githubApiClient.getWorkflowRun(runId).as(WorkflowRun.class).status;
                return WorkflowStatus.valueOf(status);
        }

        private WorkflowRun getLatestRun(List<WorkflowRun> workflowRuns) {
                return workflowRuns.stream().max(Comparator.comparingInt(run -> run.runNumber))
                                .orElseThrow(() -> new RuntimeException("No workflow runs found"));
        }
}
