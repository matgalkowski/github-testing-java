package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing the file mode in a git tree */
@AllArgsConstructor
@Getter
public enum TreeModes {
  NORMAL_BLOB("100644"),
  EXECUTABLE_BLOB("100755"),
  SUBDIRECTORY("040000"),
  COMMIT("160000"),
  SYMLINK("120000");

  private final String value;
}
