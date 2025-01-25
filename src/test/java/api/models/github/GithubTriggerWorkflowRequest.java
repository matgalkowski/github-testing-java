package api.models.github;

import com.fasterxml.jackson.annotation.JsonInclude;

import api.models.ApiRequest;

/**
 * Request model for triggering a workflow
 * 
 * @param ref    (required) branch ref from which the workflow will be triggered
 * @param inputs the inputs for the workflow (if any)
 */
public class GithubTriggerWorkflowRequest extends ApiRequest {
    public String ref;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object inputs;

    public GithubTriggerWorkflowRequest(String ref) {
        this.ref = ref;
    }

    public GithubTriggerWorkflowRequest(String ref, Object inputs) {
        this.ref = ref;
        this.inputs = inputs;
    }
}
