package api.models.github.requests;

import api.models.ApiRequest;
import lombok.Builder;
import lombok.Getter;

/** Request model for creating a branch */
@Builder
@Getter
public class GithubCreateBranchRequest implements ApiRequest {
  private String ref;
  private String sha;
}
