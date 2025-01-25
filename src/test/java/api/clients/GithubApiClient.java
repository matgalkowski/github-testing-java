package api.clients;

import static io.restassured.RestAssured.given;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

import api.enums.HttpMethod;
import api.enums.IssueState;
import api.enums.TreeFileTypes;
import api.models.github.GithubCreateBranchRequest;
import api.models.github.GithubCreateCommitRequest;
import api.models.github.GithubCreateIssueRequest;
import api.models.github.GithubCreatePrRequest;
import api.models.github.GithubCreateTreeRequest;
import api.models.github.GithubUpdateReferenceRequest;
import api.models.github.GithubCreateTreeRequest.GithubTreeItem;
import api.models.github.GithubTriggerWorkflowRequest;
import api.utils.EnvUtils;
import api.utils.TestData;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Client for communicating with GitHub API
 * 
 * @see <a href="https://docs.github.com/en/rest">GitHub REST API
 *      Documentation</a>
 */
public class GithubApiClient extends ApiClient {
    private String repoOwner;
    private String repoName;

    public GithubApiClient() {
        super(EnvUtils.getEnv("GITHUB_BASE_URL"), EnvUtils.getEnv("GITHUB_TOKEN"));
        this.repoOwner = EnvUtils.getEnv("GITHUB_REPO_OWNER");
        this.repoName = EnvUtils.getEnv("GITHUB_REPO_NAME");
    }

    @Override
    public Response sendRequest(RequestSpecification reqSpec, HttpMethod method, String path) {
        reqSpec.header("Accept", "application/vnd.github+json");
        reqSpec.header("X-Github-Api-Version", "2022-11-28");
        return super.sendRequest(reqSpec, method, path);
    }

    /**
     * Creates a repository
     * 
     * @param repoName the name of the repository
     * @return the response of the request
     */
    public Response createRepository(String repoName) {
        String fullPath = "/user/repos";

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("name", repoName);

        RequestSpecification reqSpec = given().body(jsonBody.toString());
        return sendRequest(reqSpec, HttpMethod.POST, fullPath);
    }

    /**
     * Deletes a repository
     * 
     * @param repoName the name of the repository
     * @return the response of the request
     */
    public Response deleteRepository(String repoName) {
        String fullPath = "/repos/%s/%s".formatted(this.repoOwner, repoName);
        return sendRequest(HttpMethod.DELETE, fullPath);
    }

    /**
     * Get repository
     * 
     * @param repoName the name of the repository
     * @return the response of the request
     */
    public Response getRepository(String repoName) {
        String fullPath = "/repos/%s/%s".formatted(this.repoOwner, repoName);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Creates a pull request
     * 
     * @param createPrDto GithubCreatePrRequest
     * @return the response of the request
     */
    public Response createPullRequest(GithubCreatePrRequest createPrDto) {
        String fullPath = "/repos/%s/%s/pulls".formatted(this.repoOwner, this.repoName);
        return sendRequest(HttpMethod.POST, fullPath, createPrDto);
    }

    /**
     * Gets a pull request
     * 
     * @param prNumber the number of the pull request
     * @return the response of the request
     */
    public Response getPullRequest(int prNumber) {
        String fullPath = "/repos/%s/%s/pulls/%s".formatted(this.repoOwner, this.repoName, prNumber);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Updates the state of a pull request
     * 
     * @param prNumber the number of the pull request
     * @param state    new state of the pull request
     * @return the response of the request
     */
    public Response updatePullRequestState(int prNumber, IssueState state) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("state", state.name());
        String fullPath = "/repos/%s/%s/pulls/%s".formatted(this.repoOwner, this.repoName, prNumber);
        RequestSpecification reqSpec = given().body(jsonBody.toString());
        return sendRequest(reqSpec, HttpMethod.PATCH, fullPath);
    }

    /**
     * Creates a branch
     * 
     * @param createBranchRequest GithubCreateBranchRequest
     * @return the response of the request
     */
    public Response createBranch(GithubCreateBranchRequest createBranchRequest) {
        String fullPath = "/repos/%s/%s/git/refs".formatted(this.repoOwner, this.repoName);
        return sendRequest(HttpMethod.POST, fullPath, createBranchRequest);
    }

    /**
     * Gets a branch
     * 
     * @param branchName the name of the branch
     * @return the response of the request
     */
    public Response getBranch(String branchName) {
        String fullPath = "/repos/%s/%s/branches/%s".formatted(this.repoOwner, this.repoName, branchName);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Deletes a branch
     * 
     * @param branchName the name of the branch
     * @return the response of the request
     */
    public Response deleteBranch(String branchName) {
        String fullPath = "/repos/%s/%s/git/refs/heads/%s".formatted(this.repoOwner, this.repoName, branchName);
        return sendRequest(HttpMethod.DELETE, fullPath);
    }

    /**
     * Updates a branch reference
     * 
     * @param branchRef              the reference of the branch
     * @param updateReferenceRequest GithubUpdateReferenceRequest
     * @return the response of the request
     */
    public Response updateBranchRef(String branchRef, GithubUpdateReferenceRequest updateReferenceRequest) {
        String fullPath = "/repos/%s/%s/git/%s".formatted(this.repoOwner, this.repoName, branchRef);
        return sendRequest(HttpMethod.PATCH, fullPath, updateReferenceRequest);
    }

    /**
     * Creates a tree
     * 
     * @param createTreeRequest GithubCreateTreeRequest
     * @return the response of the request
     */
    public Response createTree(GithubCreateTreeRequest createTreeRequest) {
        String fullPath = "/repos/%s/%s/git/trees".formatted(this.repoOwner, this.repoName);
        return sendRequest(HttpMethod.POST, fullPath, createTreeRequest);
    }

    /**
     * Creates a git tree for a file
     * 
     * @param filePath        the path of the file
     * @param content         the content of the file
     * @param latestCommitSha the SHA of the latest commit
     * @return the response of the request
     */
    public Response createTreeForFile(String filePath, String content, String latestCommitSha) {
        GithubTreeItem treeItem = new GithubTreeItem(filePath, TestData.FILE_BLOB_MODE_FOR_TREE,
                TreeFileTypes.blob.name(), content);
        List<GithubTreeItem> tree = Arrays.asList(treeItem);
        GithubCreateTreeRequest createTreeRequest = new GithubCreateTreeRequest(latestCommitSha, tree);
        return createTree(createTreeRequest);
    }

    /**
     * Creates a commit
     * 
     * @param createCommitRequest GithubCreateCommitRequest
     * @return the response of the request
     */
    public Response createCommit(GithubCreateCommitRequest createCommitRequest) {
        String fullPath = "/repos/%s/%s/git/commits".formatted(this.repoOwner, this.repoName);
        return sendRequest(HttpMethod.POST, fullPath, createCommitRequest);
    }

    /**
     * Triggers a workflow
     * 
     * @param request    the request body
     * @param workflowId the id of the workflow (.yml file name)
     * @return the response of the request
     */
    public Response triggerWorkflow(GithubTriggerWorkflowRequest request, String workflowId) {
        String fullPath = "/repos/%s/%s/actions/workflows/%s/dispatches".formatted(this.repoOwner, this.repoName,
                workflowId);
        return sendRequest(HttpMethod.POST, fullPath, request);
    }

    /**
     * Gets the runs of a workflow
     * 
     * @param workflowId the id of the workflow (.yml file name)
     * @return the response of the request
     */
    public Response getWorkflowRuns(String workflowId) {
        String fullPath = "/repos/%s/%s/actions/workflows/%s/runs".formatted(this.repoOwner, this.repoName, workflowId);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Gets a workflow run
     * 
     * @param runId the id of the run
     * @return the response of the request
     */
    public Response getWorkflowRun(String runId) {
        String fullPath = "/repos/%s/%s/actions/runs/%s".formatted(this.repoOwner, this.repoName, runId);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Deletes a workflow run
     * 
     * @param runId the id of the run
     * @return the response of the request
     */
    public Response deleteWorkflowRun(String runId) {
        String fullPath = "/repos/%s/%s/actions/runs/%s".formatted(this.repoOwner, this.repoName, runId);
        return sendRequest(HttpMethod.DELETE, fullPath);
    }

    /**
     * Creates an issue
     * 
     * @param createIssueRequest the request body
     * @return the response of the request
     */
    public Response createIssue(GithubCreateIssueRequest createIssueRequest) {
        String fullPath = "/repos/%s/%s/issues".formatted(this.repoOwner, this.repoName);
        return sendRequest(HttpMethod.POST, fullPath, createIssueRequest);
    }

    /**
     * Gets an issue
     * 
     * @param issueNumber the number of the issue
     * @return the response of the request
     */
    public Response getIssue(int issueNumber) {
        String fullPath = "/repos/%s/%s/issues/%s".formatted(this.repoOwner, this.repoName, issueNumber);
        return sendRequest(HttpMethod.GET, fullPath);
    }

    /**
     * Updates the state of an issue
     * 
     * @param issueNumber the number of the issue
     * @param state       new state of the issue
     * @return the response of the request
     */
    public Response updateIssueState(int issueNumber, IssueState state) {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("state", state.name());
        String fullPath = "/repos/%s/%s/issues/%s".formatted(this.repoOwner, this.repoName, issueNumber);
        RequestSpecification reqSpec = given().body(jsonBody.toString());
        return sendRequest(reqSpec, HttpMethod.PATCH, fullPath);
    }
}
