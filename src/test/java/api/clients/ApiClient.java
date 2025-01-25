package api.clients;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.*;

import api.enums.HttpMethod;
import api.models.ApiRequest;
import api.utils.EnvUtils;

/**
 * Abstract base class that should be inherited by all API clients
 * Contains basic methods used for communicating with external api
 * 
 */
public abstract class ApiClient {
    private String baseUrl;
    private String apiKey;
    private String debugModeEnabled;

    public ApiClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.debugModeEnabled = EnvUtils.getEnv("DEBUG_MODE", "false");
    }

    /**
     * Sends a request to the specified path with the specified method and returns
     * the response.
     * 
     * @param reqSpec the RestAssured request specification
     * @param method  the method of the request
     * @param path    the path of the request
     * @return the response of the request
     */
    public Response sendRequest(RequestSpecification reqSpec, HttpMethod method, String path) {
        reqSpec.baseUri(this.baseUrl);
        if (this.debugModeEnabled.equals("true")) {
            reqSpec.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        }
        reqSpec.header("Authorization", "Bearer " + this.apiKey);
        return reqSpec.request(method.name(), path);
    }

    /**
     * Sends a request to the specified path with the specified method and returns
     * the response.
     * 
     * @param method the method of the request
     * @param path   the path of the request
     * @return the response of the request
     */
    public Response sendRequest(HttpMethod method, String path) {
        RequestSpecification reqSpec = given();
        return this.sendRequest(reqSpec, method, path);
    };

    /**
     * Sends a request to the specified path with the specified method and body and
     * returns the response.
     * 
     * @param method the method of the request
     * @param path   the path of the request
     * @param body   the body of the request
     * @return the response of the request
     */
    public <T extends ApiRequest> Response sendRequest(HttpMethod method, String path, T body) {
        RequestSpecification reqSpec = given().body(body);
        return this.sendRequest(reqSpec, method, path);
    };
}
