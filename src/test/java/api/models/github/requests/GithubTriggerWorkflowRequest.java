package api.models.github.requests;

import api.models.ApiRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

/** Request model for triggering a workflow */
@Builder
@Getter
public class GithubTriggerWorkflowRequest implements ApiRequest {
  private String ref;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Object inputs;
}
