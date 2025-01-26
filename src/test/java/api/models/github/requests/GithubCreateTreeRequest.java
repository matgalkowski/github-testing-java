package api.models.github.requests;

import api.models.ApiRequest;
import api.models.github.GithubTree;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

/** Request model for creating a tree */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@Getter
public class GithubCreateTreeRequest implements ApiRequest {
  private String baseTree;

  @JsonProperty("tree")
  @Singular
  private List<GithubTree> trees;
}
