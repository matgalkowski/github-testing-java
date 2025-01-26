package api.clients;

import api.enums.IssueState;
import api.enums.TreeMode;
import api.enums.TreeType;
import api.models.github.*;
import api.models.github.requests.*;
import api.utils.EnvUtils;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Client for communicating with GitHub API
 *
 * @see <a href="https://docs.github.com/en/rest">GitHub REST API Documentation</a>
 */
public class GithubApiClient extends ApiClient {
  private final String repoOwner;
  private final String repoName;

  public GithubApiClient() {
    super(EnvUtils.getEnv("GITHUB_BASE_URL"), EnvUtils.getEnv("TEST_GITHUB_TOKEN"));
    this.repoOwner = EnvUtils.getEnv("GITHUB_REPO_OWNER");
    this.repoName = EnvUtils.getEnv("GITHUB_REPO_NAME");
  }

  @Override
  public Response sendRequest(RequestSpecification reqSpec, Method method, String path) {
    reqSpec.header("Accept", "application/vnd.github+json");
    reqSpec.header("X-Github-Api-Version", "2022-11-28");
    return super.sendRequest(reqSpec, method, path);
  }

  /**
   * Creates a repository
   *
   * @param request body
   * @return the response of the request
   */
  public Response createRepository(GithubCreateRepoRequest request) {
    String fullPath = "/user/repos";
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Deletes a repository
   *
   * @param repoName the name of the repository
   * @return the response of the request
   */
  public Response deleteRepository(String repoName) {
    String fullPath = "/repos/%s/%s".formatted(this.repoOwner, repoName);
    return sendRequest(Method.DELETE, fullPath);
  }

  /**
   * Gets a repository
   *
   * @param repoName the name of the repository
   * @return the response of the request
   */
  public Response getRepository(String repoName) {
    String fullPath = "/repos/%s/%s".formatted(this.repoOwner, repoName);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Creates a pull request
   *
   * @param request GithubCreatePrRequest
   * @return the response of the request
   */
  public Response createPullRequest(GithubCreatePrRequest request) {
    String fullPath = "/repos/%s/%s/pulls".formatted(this.repoOwner, this.repoName);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Gets a pull request
   *
   * @param prNumber the number of the pull request
   * @return the response of the request
   */
  public Response getPullRequest(int prNumber) {
    String fullPath = "/repos/%s/%s/pulls/%s".formatted(this.repoOwner, this.repoName, prNumber);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Updates the state of a pull request
   *
   * @param prNumber the number of the pull request
   * @param state new state of the pull request
   * @return the response of the request
   */
  public Response updatePullRequestState(int prNumber, IssueState state) {
    String fullPath = "/repos/%s/%s/pulls/%s".formatted(this.repoOwner, this.repoName, prNumber);
    GithubUpdatePrRequest body = GithubUpdatePrRequest.builder().state(state.getValue()).build();
    return sendRequest(Method.PATCH, fullPath, body);
  }

  /**
   * Creates a branch
   *
   * @param request GithubCreateBranchRequest
   * @return the response of the request
   */
  public Response createBranch(GithubCreateBranchRequest request) {
    String fullPath = "/repos/%s/%s/git/refs".formatted(this.repoOwner, this.repoName);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Gets a branch
   *
   * @param branchName the name of the branch
   * @return the response of the request
   */
  public Response getBranch(String branchName) {
    String fullPath =
        "/repos/%s/%s/branches/%s".formatted(this.repoOwner, this.repoName, branchName);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Deletes a branch
   *
   * @param branchName the name of the branch
   * @return the response of the request
   */
  public Response deleteBranch(String branchName) {
    String fullPath =
        "/repos/%s/%s/git/refs/heads/%s".formatted(this.repoOwner, this.repoName, branchName);
    return sendRequest(Method.DELETE, fullPath);
  }

  /**
   * Updates a branch reference
   *
   * @param branchRef the reference of the branch
   * @param request GithubUpdateReferenceRequest
   * @return the response of the request
   */
  public Response updateBranchRef(String branchRef, GithubUpdateReferenceRequest request) {
    String fullPath = "/repos/%s/%s/git/%s".formatted(this.repoOwner, this.repoName, branchRef);
    return sendRequest(Method.PATCH, fullPath, request);
  }

  /**
   * Creates a tree
   *
   * @param request GithubCreateTreeRequest
   * @return the response of the request
   */
  public Response createTree(GithubCreateTreeRequest request) {
    String fullPath = "/repos/%s/%s/git/trees".formatted(this.repoOwner, this.repoName);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Creates a git tree for a file
   *
   * @param filePath the path of the file
   * @param content the content of the file
   * @param latestCommitSha the SHA of the latest commit
   * @return the response of the request
   */
  public Response createTreeForFile(String filePath, String content, String latestCommitSha) {
    GithubTree treeItem =
        GithubTree.builder()
            .path(filePath)
            .mode(TreeMode.NORMAL_BLOB.getValue())
            .type(TreeType.BLOB.getValue())
            .content(content)
            .build();
    GithubCreateTreeRequest createTreeRequest =
        GithubCreateTreeRequest.builder().baseTree(latestCommitSha).tree(treeItem).build();
    return createTree(createTreeRequest);
  }

  /**
   * Creates a commit
   *
   * @param request GithubCreateCommitRequest
   * @return the response of the request
   */
  public Response createCommit(GithubCreateCommitRequest request) {
    String fullPath = "/repos/%s/%s/git/commits".formatted(this.repoOwner, this.repoName);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Triggers a workflow
   *
   * @param request the request body
   * @param workflowId the id of the workflow (.yml file name)
   * @return the response of the request
   */
  public Response triggerWorkflow(GithubTriggerWorkflowRequest request, String workflowId) {
    String fullPath =
        "/repos/%s/%s/actions/workflows/%s/dispatches"
            .formatted(this.repoOwner, this.repoName, workflowId);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Gets the runs of a workflow
   *
   * @param workflowId the id of the workflow (.yml file name)
   * @return the response of the request
   */
  public Response getWorkflowRuns(String workflowId) {
    String fullPath =
        "/repos/%s/%s/actions/workflows/%s/runs"
            .formatted(this.repoOwner, this.repoName, workflowId);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Gets a workflow run
   *
   * @param runId the id of the run
   * @return the response of the request
   */
  public Response getWorkflowRun(String runId) {
    String fullPath =
        "/repos/%s/%s/actions/runs/%s".formatted(this.repoOwner, this.repoName, runId);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Deletes a workflow run
   *
   * @param runId the id of the run
   * @return the response of the request
   */
  public Response deleteWorkflowRun(String runId) {
    String fullPath =
        "/repos/%s/%s/actions/runs/%s".formatted(this.repoOwner, this.repoName, runId);
    return sendRequest(Method.DELETE, fullPath);
  }

  /**
   * Creates an issue
   *
   * @param request the request body
   * @return the response of the request
   */
  public Response createIssue(GithubCreateIssueRequest request) {
    String fullPath = "/repos/%s/%s/issues".formatted(this.repoOwner, this.repoName);
    return sendRequest(Method.POST, fullPath, request);
  }

  /**
   * Gets an issue
   *
   * @param issueNumber the number of the issue
   * @return the response of the request
   */
  public Response getIssue(int issueNumber) {
    String fullPath =
        "/repos/%s/%s/issues/%s".formatted(this.repoOwner, this.repoName, issueNumber);
    return sendRequest(Method.GET, fullPath);
  }

  /**
   * Updates the state of an issue
   *
   * @param issueNumber the number of the issue
   * @param state new state of the issue
   * @return the response of the request
   */
  public Response updateIssueState(int issueNumber, IssueState state) {
    String fullPath =
        "/repos/%s/%s/issues/%s".formatted(this.repoOwner, this.repoName, issueNumber);
    GithubUpdateIssueRequest body =
        GithubUpdateIssueRequest.builder().state(state.getValue()).build();
    return sendRequest(Method.PATCH, fullPath, body);
  }
}
