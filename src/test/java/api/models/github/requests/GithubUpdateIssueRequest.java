package api.models.github.requests;

import api.models.ApiRequest;
import api.models.github.GithubIssue;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/** Request model for updating an issue */
@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubUpdateIssueRequest extends GithubIssue implements ApiRequest {
  private String state;
}
