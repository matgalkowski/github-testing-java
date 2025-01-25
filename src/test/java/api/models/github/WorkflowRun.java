package api.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Model for a workflow run
 * It does not contain all the fields returned by the API, just those needed in
 * tests
 * 
 * @param id        id of the run
 * @param status    status of the run
 * @param runNumber number of the run displayed in Github
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkflowRun {
    public String id;
    public String status;
    public int runNumber;
}
