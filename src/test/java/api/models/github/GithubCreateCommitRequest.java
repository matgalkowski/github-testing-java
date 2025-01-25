package api.models.github;

import java.util.List;

import api.models.ApiRequest;

/**
 * Request model for creating a commit
 * 
 * @param message the commit message
 * @param tree    the SHA of the tree
 * @param parents the SHAs of the parents
 */
public class GithubCreateCommitRequest extends ApiRequest {
    public String message;
    public String tree;
    public List<String> parents;

    public GithubCreateCommitRequest(String message, String tree, List<String> parents) {
        this.message = message;
        this.tree = tree;
        this.parents = parents;
    }
}