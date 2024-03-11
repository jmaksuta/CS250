package cs250.hw1;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
public class Common {

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
                    toPad = (int) Common.mod(result.length(), 4);
                    result = Common.leftPad(result, ZERO, toPad);
                }
                if (addPrefix) {
                    result = String.format("0b%s", result);
                }
                break;
            case HEXADECIMAL:
                if (pad) {
                    toPad = (int) Common.mod(result.length(), 2);
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

}
