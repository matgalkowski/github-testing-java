package api.models.github.requests;

import api.models.ApiRequest;
import api.models.github.GithubPr;
import lombok.experimental.SuperBuilder;

/** Request model for creating a pull request */
@SuperBuilder
public class GithubCreatePrRequest extends GithubPr implements ApiRequest {}
