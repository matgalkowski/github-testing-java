package api.models.github;

import lombok.Builder;
import lombok.Getter;

/** Model for creating a tree item */
@Builder
@Getter
public class GithubTree {
  private String path;
  private String mode;
  private String type;
  private String content;
}
