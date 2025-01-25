package api.models.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Model for a repository
 * It does not contain all the fields returned by the API, just those needed in
 * tests
 * 
 * @param id   id of the repository
 * @param name name of the repository
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Repository {
    public String id;
    public String name;
}
