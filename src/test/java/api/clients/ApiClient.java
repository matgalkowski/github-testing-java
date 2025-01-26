package api.clients;

import static io.restassured.RestAssured.*;

import api.models.ApiRequest;
import api.utils.EnvUtils;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Abstract base class that should be inherited by all API clients. Contains basic methods used for
 * communicating with external api
 */
public abstract class ApiClient {
  private final String baseUrl;
  private final String apiKey;
  private final String debugModeEnabled;

  public ApiClient(String baseUrl, String apiKey) {
    this.baseUrl = baseUrl;
    this.apiKey = apiKey;
    this.debugModeEnabled = EnvUtils.getEnv("DEBUG_MODE", "false");
  }

  /**
   * Sends a request to the specified path with the specified method and returns the response.
   *
   * @param reqSpec the RestAssured request specification
   * @param method the method of the request
   * @param path the path of the request
   * @return the response of the request
   */
  public Response sendRequest(RequestSpecification reqSpec, Method method, String path) {
    reqSpec.baseUri(this.baseUrl);
    if (this.debugModeEnabled.equals("true")) {
      reqSpec.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    } else {
      reqSpec.filters(new ErrorLoggingFilter());
    }
    reqSpec.header("Authorization", "Bearer " + this.apiKey);
    return reqSpec.request(method, path);
  }

  /**
   * Sends a request to the specified path with the specified method and returns the response.
   *
   * @param method the method of the request
   * @param path the path of the request
   * @return the response of the request
   */
  public Response sendRequest(Method method, String path) {
    RequestSpecification reqSpec = given();
    return this.sendRequest(reqSpec, method, path);
  }

  /**
   * Sends a request to the specified path with the specified method and body and returns the
   * response.
   *
   * @param method the method of the request
   * @param path the path of the request
   * @param body the body of the request
   * @return the response of the request
   */
  public <T extends ApiRequest> Response sendRequest(Method method, String path, T body) {
    RequestSpecification reqSpec = given().body(body);
    return this.sendRequest(reqSpec, method, path);
  }
}
