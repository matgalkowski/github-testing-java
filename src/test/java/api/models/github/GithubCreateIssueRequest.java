package api.models.github;

import com.fasterxml.jackson.annotation.JsonInclude;

import api.models.ApiRequest;

/**
 * Request model for creating an issue
 * 
 * @param title     (required) title of the issue
 * @param body      body of the issue
 * @param assignees assignees of the issue
 * @param milestone milestone of the issue
 * @param labels    labels of the issue
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubCreateIssueRequest extends ApiRequest {
    public String title;
    public String body;
    public String[] assignees;
    public Integer milestone;
    public String[] labels;

    public GithubCreateIssueRequest(String title) {
        this.title = title;
    }

    public GithubCreateIssueRequest(String title, String body, String[] assignees, Integer milestone, String[] labels) {
        this.title = title;
        this.body = body;
        this.assignees = assignees;
        this.milestone = milestone;
        this.labels = labels;
    }
}
