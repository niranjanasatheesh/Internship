package attrqa.framework.helper;

/**
 * utility class to cater to string validations
 *
 *
 * @author  Suraj Bhattathiri
 * @version 1.0
 * @since   2019-05-22
 */
public class ValidationsHelper {

  public static boolean compareStringsWithWildcards(String sObservedValue, String sExpectedValue) {

    boolean multipleFlag = false;

    int firstIndex = sExpectedValue.indexOf('*');
    if (firstIndex >= 0) {
      if (sExpectedValue.indexOf('*', firstIndex + 1) > firstIndex) {
        multipleFlag = true;
      }
    }

    char cLastCharOfExpectedValue = sExpectedValue.charAt(sExpectedValue.length() - 1);
    char cFirstCharOfExpectedValue = sExpectedValue.charAt(0);

    if (!multipleFlag) {
      // sampleString*
      if (cLastCharOfExpectedValue == '*') {
        if(sObservedValue.length()<(sExpectedValue.length()-1)){
          return false;
        }
        else {
          String sExpectedValueToConsider = sExpectedValue
              .substring(0, sExpectedValue.length() - 1);
          String sObservedValueToConsider = sObservedValue
              .substring(0, sExpectedValue.length() - 1);
          if (sObservedValueToConsider.equalsIgnoreCase(sExpectedValueToConsider)) {
            return true;
          } else {
            return false;
          }
        }
      }
      // *sampleString
      else if (cFirstCharOfExpectedValue == '*') {
        String sExpectedValueToConsider = sExpectedValue.substring(1);
        String sObservedValueToConsider = sObservedValue
            .substring(sObservedValue.length() - sExpectedValue.length() + 1);
        if (sObservedValueToConsider.equalsIgnoreCase(sExpectedValueToConsider)) {
          return true;
        } else {
          return false;
        }
      }
      // sample*String
      else {
        String sExpectedValueFirstPart = sExpectedValue.substring(0, firstIndex);
        String sExpectedValueSecondPart = sExpectedValue.substring(firstIndex + 1);
        String sObservedValueFirstPartToConsider = sObservedValue
            .substring(0, sExpectedValueFirstPart.length());
        String sObservedValueSecondPartToConsider = sObservedValue
            .substring(sObservedValue.length() - sExpectedValueSecondPart.length());
        if (sObservedValueFirstPartToConsider.equalsIgnoreCase(sExpectedValueFirstPart)
            && sObservedValueSecondPartToConsider.equalsIgnoreCase(sExpectedValueSecondPart)) {
          return true;
        } else {
          return false;
        }
      }
    }
    // sample*String*
    else {
      String sExpectedValueToConsider = sExpectedValue.substring(1, sExpectedValue.length() - 1);
      String sObservedValueToConsider = sObservedValue;
      if (sObservedValueToConsider.contains(sExpectedValueToConsider)) {
        return true;
      } else {
        return false;
      }
    }
  }

}
