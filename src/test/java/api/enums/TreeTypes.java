package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing the type of file in a git tree */
@AllArgsConstructor
@Getter
public enum TreeTypes {
  BLOB("blob"),
  TREE("tree"),
  COMMIT("commit");

  private final String value;
}
