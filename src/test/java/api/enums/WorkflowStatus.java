package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing the status of a workflow run in Github actions */
@AllArgsConstructor
@Getter
public enum WorkflowStatus {
  COMPLETED("completed"),
  QUEUED("queued"),
  IN_PROGRESS("in_progress");

  private final String value;
}
