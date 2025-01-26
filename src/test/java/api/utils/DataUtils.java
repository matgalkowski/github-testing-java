package api.utils;

import java.util.Random;
import org.apache.commons.lang3.Range;

public class DataUtils {

  /**
   * Generates a random string of letters of length within provided range.
   *
   * @param lengthRange the range of lengths for the string
   * @return the random string
   */
  public static String generateRandomString(Range<Integer> lengthRange) {
    String characters = "abcdefghijklmnopqrstuvwxyz";
    Random random = new Random();

    // Randomly select a length between minLength and maxLength
    int length =
        lengthRange.getMinimum()
            + random.nextInt(lengthRange.getMaximum() - lengthRange.getMinimum() + 1);
    StringBuilder stringBuilder = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      int index = random.nextInt(characters.length());
      stringBuilder.append(characters.charAt(index));
    }

    return stringBuilder.toString();
  }
}
