package api.models.github;

import api.models.ApiRequest;

/**
 * Request model for creating a pull request
 * 
 * @param title title of the pull request
 * @param body  body of the pull request
 * @param head  branch name to merge into the base branch
 * @param base  branch name of the base branch
 */
public class GithubCreatePrRequest extends ApiRequest {
    public String title;
    public String body;
    public String head;
    public String base;

    public GithubCreatePrRequest(String title, String body, String head, String base) {
        this.title = title;
        this.body = body;
        this.head = head;
        this.base = base;
    }
}
