package api.models.github;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import api.models.ApiRequest;

/**
 * Request model for creating a tree
 * 
 * @param baseTree SHA of the base tree
 * @param tree     tree items
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubCreateTreeRequest extends ApiRequest {
    public String baseTree;
    public List<GithubTreeItem> tree;

    public GithubCreateTreeRequest(String baseTree, List<GithubTreeItem> tree) {
        this.baseTree = baseTree;
        this.tree = tree;
    }

    /**
     * Model for creating a tree item
     * 
     * @param path    File referenced in the tree
     * @param mode    File mode
     * @param type    File type
     * @param content File content
     */
    public static class GithubTreeItem {
        public String path;
        public String mode;
        public String type;
        public String content;

        public GithubTreeItem(String path, String mode, String type, String content) {
            this.path = path;
            this.mode = mode;
            this.type = type;
            this.content = content;
        }
    }
}
