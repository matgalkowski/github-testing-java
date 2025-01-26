package api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Enum representing HTTP methods */
@AllArgsConstructor
@Getter
public enum HttpMethods {
  GET("GET"),
  POST("POST"),
  PUT("PUT"),
  DELETE("DELETE"),
  PATCH("PATCH"),
  OPTIONS("OPTIONS"),
  HEAD("HEAD");

  private final String value;
}
