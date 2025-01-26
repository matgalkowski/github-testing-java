package api.utils;

import io.github.cdimascio.dotenv.Dotenv;
import java.util.Objects;

public class EnvUtils {
  /**
   * Returns the value of the variable in .env file using the given key. If the value is not set,
   * throws an IllegalArgumentException exception.
   *
   * @param key the key of the environment variable
   * @return the value of the environment variable
   */
  public static String getEnv(String key) {
    String value = Dotenv.load().get(key);
    return Objects.requireNonNull(value, "Environment variable " + key + " must be provided");
  }

  /**
   * Returns the value of the variable in .env file using the given key. If the value is not set,
   * returns the default value.
   *
   * @param key the key of the environment variable
   * @param defaultValue the default value to be returned if the variable is not set
   * @return the value of the environment variable
   */
  public static String getEnv(String key, String defaultValue) {
    String value = Dotenv.load().get(key);
    return value == null ? defaultValue : value;
  }
}
