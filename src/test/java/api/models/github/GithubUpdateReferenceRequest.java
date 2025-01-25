package api.models.github;

import api.models.ApiRequest;

/**
 * Request model for updating a reference
 * 
 * @param sha   the SHA of the commit that will be used to update the reference
 * @param force whether to force the update
 */
public class GithubUpdateReferenceRequest extends ApiRequest {
    public String sha;
    public boolean force;

    public GithubUpdateReferenceRequest(String sha, boolean force) {
        this.sha = sha;
        this.force = force;
    }
}