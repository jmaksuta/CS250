package cs250.ec;

import java.util.ArrayList;

public class Common {

    public static String[] convertArguments(String[] args, ArgType argType, boolean pad, boolean prefix) {
        ArrayList<String> result = new ArrayList<>();
        for (String arg : args) {
            result.add(convertArgument(arg, argType, pad, prefix));
        }
        return result.toArray(new String[] {});
    }

    public static String convertArgument(String arg, ArgType argType, boolean pad, boolean prefix) {
        String result = "ERROR";
        ArgType inArgType = identifyArg(arg);
        if (inArgType == argType) {
            if (!prefix && (inArgType == ArgType.BINARY || inArgType == ArgType.HEXADECIMAL)) {
                result = stripPrefix(arg);
            } else {
                result = arg;
            }
        } else {
            switch (inArgType) {
                case BINARY:
                    result = fromBinary(arg, argType, pad, prefix);
                    break;
                case DECIMAL:
                    result = fromDecimal(arg, argType, pad, prefix);
                    break;
                case HEXADECIMAL:
                    result = fromHexadecimal(arg, argType, pad, prefix);
                    break;
                default:
                    break;
            }
        }
        return result;
    }

    private static String fromBinary(String arg, ArgType argType, boolean pad, boolean prefix) {
        String result = "";
        double binaryValue = binaryStringToDouble(arg);
        switch (argType) {
            case BINARY:
                result = doubletoBinaryString(binaryValue, pad, prefix);
                break;
            case DECIMAL:
                result = doubleToDecimalString(binaryValue, pad, prefix);
                break;
            case HEXADECIMAL:
                result = doubleToHexadecimalString(binaryValue, pad, prefix);
                break;
            default:
                break;
        }
        return result;
    }

    private static String fromDecimal(String arg, ArgType argType, boolean pad, boolean prefix) {
        String result = "";
        double decimalValue = decimalStringToDouble(arg);
        switch (argType) {
            case BINARY:
                result = doubletoBinaryString(decimalValue, pad, prefix);
                break;
            case DECIMAL:
                result = doubleToDecimalString(decimalValue, pad, prefix);
                break;
            case HEXADECIMAL:
                result = doubleToHexadecimalString(decimalValue, pad, prefix);
                break;
            default:
                break;
        }
        return result;
    }

    private static String fromHexadecimal(String arg, ArgType argType, boolean pad, boolean prefix) {
        String result = "";
        double hexadecimalValue = hexadecimalStringToDouble(arg);
        switch (argType) {
            case BINARY:
                result = doubletoBinaryString(hexadecimalValue, pad, prefix);
                break;
            case DECIMAL:
                result = doubleToDecimalString(hexadecimalValue, pad, prefix);
                break;
            case HEXADECIMAL:
                result = doubleToHexadecimalString(hexadecimalValue, pad, prefix);
                break;
            default:
                break;
        }
        return result;
    }

    private static double binaryStringToDouble(String value) {
        double result = 0.0;
        String binaryValue = stripPrefix(value);
        for (int n = binaryValue.length() - 1; n > -1; n--) {
            int orderOfMaginitude = (binaryValue.length() - (n + 1));
            char c = binaryValue.charAt(n);
            int bit = c - 48;
            result += (bit * Math.pow(2.0, orderOfMaginitude));
        }
        return result;
    }

    private static double hexadecimalStringToDouble(String value) {
        double result = 0.0;
        String hexadecimalValue = stripPrefix(value).toUpperCase();
        for (int n = hexadecimalValue.length() - 1; n > -1; n--) {
            int orderOfMaginitude = (hexadecimalValue.length() - (n + 1));
            char c = hexadecimalValue.charAt(n);
            int hex = 0;
            if (c >= 48 && c <= 57) {
                hex = (int) c - 48;
            } else if (c >= 65 && c <= 70) {
                hex = ((int) c - 65) + 10;
            }

            result += (hex * Math.pow(16.0, orderOfMaginitude));
        }
        return result;
    }

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

    private static String doubletoBinaryString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 2;
        while (workValue > 0) {
            double remainder = mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%d%s", remainders.get(n), result);
        }
        result = format(result, ArgType.BINARY, pad, prefix);
        return result;
    }

    public static String doubleToDecimalString(double value) {
        return doubleToDecimalString(value, false, false);
    }

    private static String doubleToDecimalString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 10;
        while (workValue > 0) {
            double remainder = mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%s%s", remainders.get(n), result);
        }
        result = format(result, ArgType.DECIMAL, pad, prefix);
        return result;
    }

    private static String doubleToHexadecimalString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 16;
        while (workValue > 0) {
            double remainder = mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%s%s", intToHexChar(remainders.get(n)), result);
        }
        result = format(result, ArgType.HEXADECIMAL, pad, prefix);
        return result;
    }

    private static String intToHexChar(int value) {
        String result = "";
        if (value >= 0 && value <= 9) {
            result = new String(new char[] { (char) (value + 48) });
        } else if (value >= 10 && value <= 16) {
            result = new String(new char[] { (char) ((value - 10) + 65) });
        }
        return result;
    }

    public static String intToString(int value) {
        return new String(new char[] { (char) (value + 48) });
    }

    public enum ArgType {
        UNKNOWN, BINARY, DECIMAL, HEXADECIMAL;

        @Override
        public String toString() {
            String result = "";
            switch (this) {
                case BINARY:
                    result = "Binary";
                    break;
                case HEXADECIMAL:
                    result = "Hexadecimal";
                    break;
                case DECIMAL:
                    result = "Decimal";
                    break;
                default:
                    result = "Unknown";
                    break;
            }
            return result;
        }
    }

    public enum BASE {
        BINARY(2), DECIMAL(10), HEXADECIMAL(16);

        int value = 0;

        BASE(int value) {
            this.value = value;
        }

        public int toInt() {
            return this.value;
        }

        @Override
        public String toString() {
            return String.format("%d", this.value);
        }
    }

    public static ArgType identifyArg(String arg) {
        ArgType result = ArgType.UNKNOWN;
        if (arg.startsWith("0b")) {
            result = ArgType.BINARY;
        } else if (arg.startsWith("0x")) {
            result = ArgType.HEXADECIMAL;
        } else {
            result = ArgType.DECIMAL;
        }
        return result;
    }

    public static String format(String toFormat, ArgType argType, boolean pad, boolean addPrefix) {
        String result = toFormat;
        int toPad = 0;
        String ZERO = "0";
        switch (argType) {
            case BINARY:
                if (pad) {
                    toPad = (int) mod(result.length(), 4);
                    result = leftPad(result, ZERO, toPad);
                }
                if (addPrefix) {
                    result = String.format("0b%s", result);
                }
                break;
            case HEXADECIMAL:
                if (pad) {
                    toPad = (int) mod(result.length(), 2);
                    result = leftPad(result, ZERO, toPad);
                }
                if (addPrefix) {
                    result = String.format("0x%s", result);
                }
                break;
            case DECIMAL:
            default:
                // do nothing.
                break;
        }
        return result;
    }

    public static double mod(int dividend, int divisor) {
        return mod((double) dividend, divisor);
    }

    public static double mod(double dividend, int divisor) {
        double result = 0;
        if (divisor != 0) {
            result = dividend - (Math.floor(dividend / divisor) * divisor);
        }
        return result;
    }

    public static String stripPrefix(String value) {
        String result = "";
        if (value.length() > 2) {
            result = value.substring(2);
        }
        return result;
    }

    public static String leftPad(String valueToPad, String padString, int number) {
        StringBuilder builder = new StringBuilder();
        builder.append(valueToPad);
        if (number > 0) {
            for (int n = 0; n < number; n++) {
                builder.insert(0, padString);
            }
        }
        return builder.toString();
    }

    public static String rightPad(String valueToPad, String padString, int number) {
        StringBuilder builder = new StringBuilder();
        builder.append(valueToPad);
        if (number > 0) {
            for (int n = 0; n < number; n++) {
                builder.append(padString);
            }
        }
        return builder.toString();
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