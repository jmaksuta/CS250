package cs250.hw1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Operations {

    public static void main(String[] args) {
        try {

            Main main = new Main(args);
            main.run();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static class Main {

        private String[] args;

        public Main() {
            super();
            this.args = new String[] {};
        }

        public Main(String[] args) {
            this();
            this.args = args;
        }

        public void run() throws Exception {
            task1();
            task2();
            task3();
            task4();
            task5();
            task6();
            task7();
            task8();
        }

        private void task1() throws Exception {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 1\n");
            if (args.length != 3) {
                throw new Exception("Incorrect number of arguments have been provided. Program Terminating!\n");
            }
            builder.append("Correct number of arguments given.\n");

            print(builder.toString());
        }

        private void task2() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 2\n");
            for (String arg : args) {
                Common.ArgType argType = Common.identifyArg(arg);
                builder.append(String.format("%s=%s\n", arg, argType));
            }

            print(builder.toString());
        }

        private void task3() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 3\n");
            for (String arg : args) {
                Common.ArgType argType = Common.identifyArg(arg);
                boolean isValid = Validate.validate(arg, argType);
                builder.append(String.format("%s=%s\n", arg, isValid));
            }

            print(builder.toString());
        }

        private void task4() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 4\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(arg, Common.ArgType.BINARY, false, true);
                String decimal = Convert.convertArgument(arg, Common.ArgType.DECIMAL, false, true);
                String hexadecimal = Convert.convertArgument(arg, Common.ArgType.HEXADECIMAL, false, true);
                builder.append(String.format("Start=%s,Binary=%s,Decimal=%s,Hexadecimal=%s\n", arg, binary, decimal,
                        hexadecimal));
            }

            print(builder.toString());
        }

        private void task5() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 5\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(new StringBuilder(arg).toString(), Common.ArgType.BINARY, false,
                        false);
                String onesCompliment = Bitwise.onesCompliment(binary);

                builder.append(String.format("%s=%s=>%s\n", arg, binary, onesCompliment));
            }

            print(builder.toString());
        }

        private void task6() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 6\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(new StringBuilder(arg).toString(), Common.ArgType.BINARY, false,
                        false);
                String twosCompliment = Bitwise.twosCompliment(binary);

                builder.append(String.format("%s=%s=>%s\n", arg, binary, twosCompliment));
            }

            print(builder.toString());
        }

        private void task7() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 7\n");

            ArrayList<String> list = new ArrayList<>();
            for (String arg : args) {
                list.add(Convert.convertArgument(arg, Common.ArgType.BINARY, false, false));
            }

            String[] arr = list.toArray(new String[] {});

            String or = Bitwise.or(arr);
            String and = Bitwise.and(arr);
            String xOr = Bitwise.xOr(arr);

            builder.append(String.format("%s|%s|%s=%s\n", arr[0], arr[1], arr[2], or));
            builder.append(String.format("%s&%s&%s=%s\n", arr[1], arr[1], arr[2], and));
            builder.append(String.format("%s^%s^%s=%s\n", arr[2], arr[1], arr[2], xOr));

            print(builder.toString());
        }

        private void task8() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 8\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(arg, Common.ArgType.BINARY, false, false);
                String lShift = Bitwise.leftShift(binary, 2);
                String rShift = Bitwise.rightShift(binary, 2);

                builder.append(String.format("%s<<2=%s,%s>>2=%s\n", binary, lShift, binary, rShift));
            }

            print(builder.toString());
        }

        private void print(String toPrint) {
            System.out.println(toPrint);
        }

    }

    private static class Bitwise {

        private static final int ASCII_ONE = 49;
        @SuppressWarnings("unused")
        private static final int ASCII_ZERO = 48;

        @SuppressWarnings("unused")
        public static double leftShift(double value, int times, Common.ArgType argType) {
            double result = value;
            switch (argType) {
                case BINARY:
                    result = value * Math.pow(Common.BASE.BINARY.toInt(), times);
                    break;
                case DECIMAL:
                    result = value * Math.pow(Common.BASE.DECIMAL.toInt(), times);
                    break;
                case HEXADECIMAL:
                    result = value * Math.pow(Common.BASE.HEXADECIMAL.toInt(), times);
                    break;
                default:
                    break;
            }
            return result;
        }

        @SuppressWarnings("unused")
        public static double rightShift(double value, int times, Common.ArgType argType) {
            double result = value;
            switch (argType) {
                case BINARY:
                    result = Math.floor(value / Math.pow(Common.BASE.BINARY.toInt(), times));
                    break;
                case DECIMAL:
                    result = Math.floor(value / Math.pow(Common.BASE.DECIMAL.toInt(), times));
                    break;
                case HEXADECIMAL:
                    result = Math.floor(value / Math.pow(Common.BASE.HEXADECIMAL.toInt(), times));
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

        @SuppressWarnings("unused")
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

        @SuppressWarnings("unused")
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

    private static class Common {

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
                        toPad = result.length() % 4;
                        result = Common.leftPad(result, ZERO, toPad);
                    }
                    if (addPrefix) {
                        result = String.format("0b%s", result);
                    }
                    break;
                case HEXADECIMAL:
                    if (pad) {
                        toPad = result.length() % 2;
                        result = Common.leftPad(result, ZERO, toPad);
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

        @SuppressWarnings("unused")
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

    }

    private static class Convert {

        @SuppressWarnings("unused")
        public static String[] convertArguments(String[] args, Common.ArgType argType, boolean pad, boolean prefix) {
            ArrayList<String> result = new ArrayList<>();
            for (String arg : args) {
                result.add(convertArgument(arg, argType, pad, prefix));
            }
            return result.toArray(new String[] {});
        }

        public static String convertArgument(String arg, Common.ArgType argType, boolean pad, boolean prefix) {
            String result = "ERROR";
            Common.ArgType inArgType = Common.identifyArg(arg);
            if (inArgType == argType) {
                if (!prefix && (inArgType == Common.ArgType.BINARY || inArgType == Common.ArgType.HEXADECIMAL)) {
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

        private static String fromBinary(String arg, Common.ArgType argType, boolean pad, boolean prefix) {
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

        private static String fromDecimal(String arg, Common.ArgType argType, boolean pad, boolean prefix) {
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

        private static String fromHexadecimal(String arg, Common.ArgType argType, boolean pad, boolean prefix) {
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
                double remainder = workValue % baseValue;
                remainders.add((int) remainder);
                workValue = Math.floor(workValue / baseValue);
            }
            for (int n = 0; n < remainders.size(); n++) {
                result = String.format("%d%s", remainders.get(n), result);
            }
            result = Common.format(result, Common.ArgType.BINARY, pad, prefix);
            return result;
        }

        private static String doubleToDecimalString(double value, boolean pad, boolean prefix) {
            String result = "";
            ArrayList<Integer> remainders = new ArrayList<>();
            double workValue = value;
            int baseValue = 10;
            while (workValue > 0) {
                double remainder = workValue % baseValue;
                remainders.add((int) remainder);
                workValue = Math.floor(workValue / baseValue);
            }
            for (int n = 0; n < remainders.size(); n++) {
                result = String.format("%s%s", remainders.get(n), result);
            }
            result = Common.format(result, Common.ArgType.DECIMAL, pad, prefix);
            return result;
        }

        private static String doubleToHexadecimalString(double value, boolean pad, boolean prefix) {
            String result = "";
            ArrayList<Integer> remainders = new ArrayList<>();
            double workValue = value;
            int baseValue = 16;
            while (workValue > 0) {
                double remainder = workValue % baseValue;
                remainders.add((int) remainder);
                workValue = Math.floor(workValue / baseValue);
            }
            for (int n = 0; n < remainders.size(); n++) {
                result = String.format("%s%s", intToHexChar(remainders.get(n)), result);
            }
            result = Common.format(result, Common.ArgType.HEXADECIMAL, pad, prefix);
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

    private static class Validate {

        public static boolean validate(String value, Common.ArgType argType) {
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

}