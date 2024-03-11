package cs250.hw1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import cs250.hw1.Common.BASE;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
public class Bitwise {

    private static final int ASCII_ONE = 49;
    @SuppressWarnings("unused")
    private static final int ASCII_ZERO = 48;

    public static double leftShift(double value, int times, Common.ArgType argType) {
        double result = value;
        switch (argType) {
            case BINARY:
                result = value * Math.pow(BASE.BINARY.toInt(), times);
                break;
            case DECIMAL:
                result = value * Math.pow(BASE.DECIMAL.toInt(), times);
                break;
            case HEXADECIMAL:
                result = value * Math.pow(BASE.HEXADECIMAL.toInt(), times);
                break;
            default:
                break;
        }
        return result;
    }

    public static double rightShift(double value, int times, Common.ArgType argType) {
        double result = value;
        switch (argType) {
            case BINARY:
                result = Math.floor(value / Math.pow(BASE.BINARY.toInt(), times));
                break;
            case DECIMAL:
                result = Math.floor(value / Math.pow(BASE.DECIMAL.toInt(), times));
                break;
            case HEXADECIMAL:
                result = Math.floor(value / Math.pow(BASE.HEXADECIMAL.toInt(), times));
                break;
            default:
                break;
        }
        return result;
    }

    public static String leftShift(String binaryString, int times) {
        StringBuilder builder = new StringBuilder(binaryString);
        for (int n = 0; n < times; n++) {
            builder.append("0");
        }
        return builder.toString();
    }

    public static String rightShift(String binaryString, int times) {
        StringBuilder builder = new StringBuilder(binaryString);
        for (int n = 0; n < times; n++) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static String[] onesCompliment(String[] binaryArgs) {
        ArrayList<String> result = new ArrayList<>();
        for (String binary : binaryArgs) {
            result.add(onesCompliment(binary));
        }
        return result.toArray(new String[] {});
    }

    public static String onesCompliment(String binaryArg) {
        StringBuilder builder = new StringBuilder();
        if (!binaryArg.isEmpty()) {
            for (int n = binaryArg.length() - 1; n >= 0; n--) {
                boolean isTrue = binaryArg.charAt(n) == ASCII_ONE;
                int out = (isTrue) ? 0 : 1;

                builder.insert(0, out);
            }
        }
        return builder.toString();
    }

    public static String[] twosCompliment(String[] binaryArgs) {
        ArrayList<String> result = new ArrayList<>();
        for (String binary : binaryArgs) {
            result.add(twosCompliment(binary));
        }
        return result.toArray(new String[] {});
    }

    public static String twosCompliment(String binaryArg) {
        StringBuilder builder = new StringBuilder();
        binaryArg = onesCompliment(binaryArg);
        builder.append(addValue(binaryArg, 1));
        return builder.toString();
    }

    private static String addValue(String binaryString, int value) {
        return addValue(binaryString, binaryString.length() - 1, value);
    }

    private static String addValue(String binaryString, int index, int value) {
        StringBuilder builder = new StringBuilder(binaryString);
        if (index >= 0 && index < binaryString.length()) {
            boolean isTrue = binaryString.charAt(index) == ASCII_ONE;
            int out = (isTrue) ? 1 : 0;
            out += value;
            if (out > 1) {
                out = 0;
                builder = new StringBuilder(addValue(binaryString, index - 1, value));
            }
            builder.replace(index, index + 1, Convert.intToString(out));

        }
        return builder.toString();
    }

    public static String xOr(String[] binaryArgs) {
        binaryArgs = clone(binaryArgs);

        int maxLen = maxLength(binaryArgs);

        binaryArgs = normalize(binaryArgs, maxLen);

        Stack<String> stack = loadStack(binaryArgs);

        String result = "";
        do {
            String value1 = stack.pop();
            String value2 = stack.pop();

            result = xOr(value1, value2);
            if (!stack.isEmpty()) {
                stack.push(result);
            }

        } while (!stack.isEmpty());

        return result;

    }

    public static String xOr(String normalBinary1, String normalBinary2) {
        StringBuilder builder = new StringBuilder();

        for (int n = normalBinary1.length() - 1; n >= 0; n--) {
            int out = 0;
            boolean value1 = normalBinary1.charAt(n) == ASCII_ONE;
            boolean value2 = normalBinary2.charAt(n) == ASCII_ONE;
            if ((value1 || value2) && !(value1 && value2)) {
                out = 1;
            }
            builder.insert(0, out);
        }

        return builder.toString();
    }

    public static String and(String[] binaryArgs) {
        binaryArgs = clone(binaryArgs);

        int maxLen = maxLength(binaryArgs);

        binaryArgs = normalize(binaryArgs, maxLen);

        Stack<String> stack = loadStack(binaryArgs);

        String result = "";
        do {
            String value1 = stack.pop();
            String value2 = stack.pop();

            result = and(value1, value2);
            if (!stack.isEmpty()) {
                stack.push(result);
            }

        } while (!stack.isEmpty());

        return result;

    }

    public static String and(String normalBinary1, String normalBinary2) {
        StringBuilder builder = new StringBuilder();

        for (int n = normalBinary1.length() - 1; n >= 0; n--) {
            int out = 0;
            boolean value1 = normalBinary1.charAt(n) == ASCII_ONE;
            boolean value2 = normalBinary2.charAt(n) == ASCII_ONE;
            if (value1 && value2) {
                out = 1;
            }
            builder.insert(0, out);
        }

        return builder.toString();
    }

    public static String or(String[] binaryArgs) {
        binaryArgs = clone(binaryArgs);

        int maxLen = maxLength(binaryArgs);

        binaryArgs = normalize(binaryArgs, maxLen);

        Stack<String> stack = loadStack(binaryArgs);

        String result = "";
        do {
            String value1 = stack.pop();
            String value2 = stack.pop();

            result = or(value1, value2);
            if (!stack.isEmpty()) {
                stack.push(result);
            }

        } while (!stack.isEmpty());

        return result;

    }

    public static String or(String normalBinary1, String normalBinary2) {
        StringBuilder builder = new StringBuilder();

        for (int n = normalBinary1.length() - 1; n >= 0; n--) {
            int out = 0;
            boolean value1 = normalBinary1.charAt(n) == ASCII_ONE;
            boolean value2 = normalBinary2.charAt(n) == ASCII_ONE;
            if (value1 || value2) {
                out = 1;
            }
            builder.insert(0, out);
        }

        return builder.toString();
    }

    public static String[] clone(String[] array) {
        return Arrays.copyOf(array, array.length);
    }

    public static Stack<String> loadStack(String[] binaryArgs) {
        Stack<String> stack = new Stack<>();

        if (binaryArgs.length > 0) {
            for (int n = binaryArgs.length - 1; n >= 0; n--) {
                stack.push(binaryArgs[n]);
            }
        }

        return stack;
    }

    public static int maxLength(String[] binaryArgs) {
        int maxLength = 0;
        for (String arg : binaryArgs) {
            if (arg.length() > maxLength) {
                maxLength = arg.length();
            }
        }
        return maxLength;
    }

    public static String[] normalize(String[] binaryArgs, int length) {
        if (binaryArgs != null && binaryArgs.length > 0) {
            for (int n = 0; n < binaryArgs.length; n++) {
                if (binaryArgs[n].length() < length) {
                    binaryArgs[n] = Common.leftPad(binaryArgs[n], "0", length - binaryArgs[n].length());
                }
            }
        }
        return binaryArgs;
    }

}
