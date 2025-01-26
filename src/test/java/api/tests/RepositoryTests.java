package api.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import api.clients.GithubApiClient;
import api.models.github.GithubRepository;
import api.models.github.requests.GithubCreateRepoRequest;
import api.utils.DataUtils;
import org.apache.commons.lang3.Range;
import org.testng.annotations.*;

public class RepositoryTests {
  private final GithubApiClient githubApiClient = new GithubApiClient();
  private String repoName;

  @BeforeMethod(alwaysRun = true)
  public void beforeMethod() {
    repoName = DataUtils.generateRandomString(Range.between(5, 15));
  }

  @Test(groups = {"repo"})
  public void shouldCreateRepository() {
    githubApiClient
        .createRepository(GithubCreateRepoRequest.builder().name(repoName).build())
        .then()
        .statusCode(201);

    GithubRepository repository =
        githubApiClient
            .getRepository(repoName)
            .then()
            .statusCode(200)
            .extract()
            .as(GithubRepository.class);

    assertThat(repository.getName(), equalTo(repoName));
  }

  @AfterMethod(alwaysRun = true)
  public void afterMethod() {
    githubApiClient.deleteRepository(repoName).then().statusCode(204);
  }
}
