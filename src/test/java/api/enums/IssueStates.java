package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing the state of a pull request */
@AllArgsConstructor
@Getter
public enum IssueStates {
  OPEN("open"),
  CLOSED("closed");

  private final String value;
}
