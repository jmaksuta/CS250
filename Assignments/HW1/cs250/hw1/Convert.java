package cs250.hw1;

import java.util.ArrayList;
import cs250.hw1.Common.ArgType;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
public class Convert {

    public static String[] convertArguments(String[] args, ArgType argType, boolean pad, boolean prefix) {
        ArrayList<String> result = new ArrayList<>();
        for (String arg : args) {
            result.add(convertArgument(arg, argType, pad, prefix));
        }
        return result.toArray(new String[] {});
    }

    public static String convertArgument(String arg, ArgType argType, boolean pad, boolean prefix) {
        String result = "ERROR";
        ArgType inArgType = Common.identifyArg(arg);
        if (inArgType == argType) {
            if (!prefix && (inArgType == ArgType.BINARY || inArgType == ArgType.HEXADECIMAL)) {
                result = Common.stripPrefix(arg);
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
        String binaryValue = Common.stripPrefix(value);
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
        String hexadecimalValue = Common.stripPrefix(value).toUpperCase();
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

    private static double decimalStringToDouble(String value) {
        double result = 0.0;
        String decimalValue = value;
        for (int n = decimalValue.length() - 1; n > -1; n--) {
            int orderOfMaginitude = (decimalValue.length() - (n + 1));
            char c = decimalValue.charAt(n);
            int dec = 0;
            if (c >= 48 && c <= 57) {
                dec = (int) c - 48;
            }

            result += (dec * Math.pow(10.0, orderOfMaginitude));
        }
        return result;
    }

    private static String doubletoBinaryString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 2;
        while (workValue > 0) {
            double remainder = Common.mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%d%s", remainders.get(n), result);
        }
        result = Common.format(result, ArgType.BINARY, pad, prefix);
        return result;
    }

    private static String doubleToDecimalString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 10;
        while (workValue > 0) {
            double remainder = Common.mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%s%s", remainders.get(n), result);
        }
        result = Common.format(result, ArgType.DECIMAL, pad, prefix);
        return result;
    }

    private static String doubleToHexadecimalString(double value, boolean pad, boolean prefix) {
        String result = "";
        ArrayList<Integer> remainders = new ArrayList<>();
        double workValue = value;
        int baseValue = 16;
        while (workValue > 0) {
            double remainder = Common.mod(workValue, baseValue);
            remainders.add((int) remainder);
            workValue = Math.floor(workValue / baseValue);
        }
        for (int n = 0; n < remainders.size(); n++) {
            result = String.format("%s%s", intToHexChar(remainders.get(n)), result);
        }
        result = Common.format(result, ArgType.HEXADECIMAL, pad, prefix);
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

}
