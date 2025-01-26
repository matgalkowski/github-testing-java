package api.models.github.requests;

import api.models.ApiRequest;
import lombok.Builder;
import lombok.Getter;

/** Request model for updating a reference */
@Builder
@Getter
public class GithubUpdateReferenceRequest implements ApiRequest {
  private String sha;
  @Builder.Default private boolean force = false;
}
