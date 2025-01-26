# Testing Java Project

Project created for testing GitHub API

## How to Run Tests
1. Make sure you have Maven installed. 
2. The build target expects Java 23 to be installed as well but earlier versions should do just fine. In that case an error will be displayed - it can be solved by changing the source and target in pom.xml.
3. Add the api key to the `.env` file located in the project root: `TEST_GITHUB_TOKEN={KEY}`. <br> I've provided a test token for you in the email for the sake of simplifying the verification of this assignment.
4. Navigate to the project directory.
5. Run the tests using the command: `mvn test`. <br>
   Note: You can also run one of the test groups using maven profiles by running `mvn test -P {GROUP_NAME}` <br>
   Groups available: `pr`, `workflow`, `issue`, `repo`


## Additional features
### Debug mode
Debug mode is disabled by default. It can be enabled by setting the `DEBUG_MODE` key in the `.env` file to `true`. <br> As a result, all requests and responses will be logged to the console.

### CI
GitHub Actions workflow is configured to run all tests. See [GH Action runs](https://github.com/matgalkowski/github-testing-java/actions) <br>
The most recent run executed the final implementation of tests.