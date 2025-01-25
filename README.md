# Testing Java Project

Project created for testing GitHub API

## How to Run Tests
1. Make sure you have Maven installed. 
2. The build target expects Java 23 to be installed as well but earlier versions should do just fine. In that case an error will be displayed - it can be solved by changing the source and target in pom.xml.
3. Add the api key to the `.env` file located in the project root: `GITHUB_API_KEY={KEY}`. I've provided a test api key for you in the email for the sake of simplifying the verification of this assignment.
4. Navigate to the project directory.
5. Run the tests using the command: `mvn test`.

## Additional features
### Debug mode
Debug mode is disabled by default. It can be enabled by setting the `DEBUG_MODE` key in the `.env` file to `true`. As a result, all requests and responses will be logged to the console.
