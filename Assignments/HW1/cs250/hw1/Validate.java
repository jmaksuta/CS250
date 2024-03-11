package cs250.hw1;

import cs250.hw1.Common.ArgType;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
public class Validate {

    public static boolean validate(String value, ArgType argType) {
        boolean isValid = true;
        try {
            switch (argType) {
                case BINARY:
                    validateBinary(value);
                    break;
                case HEXADECIMAL:
                    validateHexadecimal(value);
                    break;
                case DECIMAL:
                    validateDecimal(value);
                    break;
                default:
                    isValid = false;
                    break;
            }
        } catch (Exception e) {
            isValid = false;
        }
        return isValid;
    }

    private static void validateBinary(String arg) throws Exception {
        if (arg.length() > 2) {
            String binaryPart = Common.stripPrefix(arg);
            for (int n = 0; n < binaryPart.length(); n++) {
                if (!(binaryPart.charAt(n) == (char) 48 || binaryPart.charAt(n) == (char) 49)) {
                    throw new Exception("The binary value is invalid");
                }
            }
        }
    }

    private static void validateDecimal(String arg) throws Exception {
        if (arg.length() > 2) {
            String decimalPart = Common.stripPrefix(arg);
            for (int n = 0; n < decimalPart.length(); n++) {
                if (!(decimalPart.charAt(n) >= (char) 48 && decimalPart.charAt(n) <= (char) 57)) {
                    throw new Exception("The decimal value is invalid.");
                }
            }
        }
    }

    private static void validateHexadecimal(String arg) throws Exception {
        if (arg.length() > 2) {
            String decimalPart = Common.stripPrefix(arg).toUpperCase();
            for (int n = 0; n < decimalPart.length(); n++) {
                boolean isValidNumber = (decimalPart.charAt(n) >= (char) 48 && decimalPart.charAt(n) <= (char) 57);
                boolean isValidAlpha = (decimalPart.charAt(n) >= (char) 65 && decimalPart.charAt(n) <= (char) 70);
                if (!(isValidAlpha || isValidNumber)) {
                    throw new Exception("The hexadecimal value is invalid.");
                }
            }
        }
    }

}
