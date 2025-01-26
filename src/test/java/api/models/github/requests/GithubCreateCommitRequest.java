package api.models.github.requests;

import api.models.ApiRequest;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/** Request model for creating a commit */
@Builder
@Getter
public class GithubCreateCommitRequest implements ApiRequest {
  private String message;
  private String tree;
  @Singular private List<String> parents;
}
