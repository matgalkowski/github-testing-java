package api.models.github.requests;

import api.models.ApiRequest;
import api.models.github.GithubPr;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/** Request model for updating a pull request */
@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GithubUpdatePrRequest extends GithubPr implements ApiRequest {
    private String state;
}
