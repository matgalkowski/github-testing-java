package api.models.github.requests;

import api.models.ApiRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/** Request model for updating an issue */
@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubUpdateIssueRequest implements ApiRequest {
  private String state;
}
