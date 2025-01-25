package api.models.github;

import api.models.ApiRequest;

/**
 * Request model for creating a branch
 * 
 * @param ref branch name
 * @param sha the SHA of the commit
 */
public class GithubCreateBranchRequest extends ApiRequest {
    public String ref;
    public String sha;

    public GithubCreateBranchRequest(String ref, String sha) {
        this.ref = "refs/heads/" + ref;
        this.sha = sha;
    }
}
