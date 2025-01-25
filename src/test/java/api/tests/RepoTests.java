package api.tests;

import org.apache.commons.lang3.Range;
import org.testng.annotations.*;

import api.clients.GithubApiClient;
import api.models.github.Repository;
import api.utils.DataUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RepoTests {
    private GithubApiClient githubApiClient = new GithubApiClient();
    private String repoName;

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod() {
        repoName = DataUtils.generateRandomString(Range.between(5, 15));
    }

    @Test(groups = { "repo" })
    public void shouldCreateRepository() {
        githubApiClient.createRepository(repoName)
                .then().statusCode(201);

        Repository repository = githubApiClient.getRepository(repoName)
                .then().statusCode(200)
                .extract().as(Repository.class);

        assertThat(repository.name, equalTo(repoName));

    }

    @AfterMethod(alwaysRun = true)
    public void afterMethod() {
        githubApiClient.deleteRepository(repoName)
                .then().statusCode(204);
    }

}
