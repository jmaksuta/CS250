package cs250.ec.convert;

/**
 * @author John Maksuta
 * @since 2024-04-19
 * @see Course: CS250-801 Spring 2024,
 *      Instructor: Professor Pallickara,
 *      Assignment: Extra Credit Assignment
 */
public class Common {

    public static double decimalStringToDouble(String value) {
        double result = 0.0;
        String decimalValue = value;
        if (!value.contains(".")) {
            value = value + ".";
        }
        int decimalIndex = value.indexOf(".");
        int orderOfMaginitude = decimalIndex;

        for (int n = 0; n < value.length(); n++) {
            char c = decimalValue.charAt(n);
            int dec = 0;
            if (c >= 48 && c <= 57) {
                dec = (int) c - 48;
                orderOfMaginitude--;
                result += (dec * Math.pow(10.0, orderOfMaginitude));
            }
        }
        result = Math.round(result * 1000000) / 1000000.0;
        return result;
    }

    public static String getSignificantNumber(double input) {
        return getSignificantNumber(String.format("%f", input));
    }

    public static String getSignificantNumber(String input) {
        StringBuilder builder = new StringBuilder();
        if (!input.isEmpty()) {
            for (int n = 0; n < input.length(); n++) {
                char current = input.charAt(n);
                if (isValid(current, n)) {
                    builder.append(current);
                }
            }
        }
        return stripTrailingZeros(builder.toString());
    }

    private static boolean isValid(char inChar, int inIndex) {
        return (isLeadingSign(inChar, inIndex) || isDecimalPoint(inChar) || isNumber(inChar));
    }

    private static boolean isNumber(char inChar) {
        return (inChar >= 48 && inChar <= 57);
    }

    public static boolean isSign(char inChar) {
        return (inChar == 43 || inChar == 45);
    }

    private static boolean isLeadingSign(char inChar, int currentIndex) {
        return (isSign(inChar) && currentIndex == 0);
    }

    private static boolean isDecimalPoint(char inChar) {
        return (inChar == 46);
    }

    private static String stripTrailingZeros(String input) {
        StringBuilder builder = new StringBuilder();
        if (!input.isEmpty()) {
            boolean isSignificant = false;
            for (int n = input.length() - 1; n >= 0; n--) {
                if (!isSignificant && isNonZeroNumber(input.charAt(n))) {
                    isSignificant = true;
                }
                if (isSignificant) {
                    builder.insert(0, input.charAt(n));
                }
            }
        }
        return builder.toString();
    }

    private static boolean isNonZeroNumber(char inChar) {
        return (inChar >= 49 && inChar <= 57);
    }

}