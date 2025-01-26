package api.models.github.requests;

import api.models.ApiRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

/** Request model for creating a repository Only name is required */
@Builder
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubCreateRepoRequest implements ApiRequest {
  private String name;
  private String description;
  private String homepage;

  @JsonProperty("private")
  private boolean privateRepo;

  private boolean isTemplate;
}
