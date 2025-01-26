package api.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Model for a repository It does not contain all the fields returned by the API, just those needed
 * in tests
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepository {
  private String id;
  private String name;
}
