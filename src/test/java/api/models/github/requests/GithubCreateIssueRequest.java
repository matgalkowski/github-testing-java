package api.models.github.requests;

import api.models.ApiRequest;
import api.models.github.GithubIssue;
import lombok.experimental.SuperBuilder;

/** Request model for creating an issue */
@SuperBuilder
public class GithubCreateIssueRequest extends GithubIssue implements ApiRequest {}
