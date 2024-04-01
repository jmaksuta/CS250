package cs250.hw3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static void writeToConsole(String message) {
        System.out.print(message);
    }

    public static void writeLineToConsole(String message) {
        System.out.println(message);
    }

    public static boolean isInteger(String value) {
        boolean result = false;
        try {
            Integer.parseInt(value);
            result = true;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static int toInteger(String value) {
        Integer result = 0;
        try {
            result = Integer.parseInt(value);

        } catch (Exception e) {
            // do nothing.
        }
        return result;
    }

    public static boolean isValidRange(Integer value, Integer lowerLimit, Integer upperLimit) {
        boolean result = true;
        try {
            if (value != null) {
                if (lowerLimit != null && value < lowerLimit) {
                    throw new Exception();
                }
                if (upperLimit != null && value > upperLimit) {
                    throw new Exception();
                }

            } else {
                throw new Exception();
            }

        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static boolean isValidHost(String value) {
        boolean result = false;
        try {
            String ipv4 = "[0-9]{1,3}[\\.][0-9]{1,3}[\\.][0-9]{1,3}[\\.][0-9]{1,3}";
            String ipv6 = "[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}:[ 0-9A-Fa-f]{0,4}";
            String domainName = "[a-zA-Z0-9-.:\\/]*";

            boolean isIpv4 = matchesRegEx(value, ipv4);

            boolean isIPv6 = matchesRegEx(value, ipv6);

            boolean isDomainName = matchesRegEx(value, domainName);

            result = (isIpv4 || isIPv6 || isDomainName);

        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    private static boolean matchesRegEx(String value, String regEx) {
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static byte[] listToArray(ArrayList<Byte> list) {
        byte[] result = new byte[list.size()];
        for (int n = 0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }

    /**
     * Converts an int to a big endian encoded byte array.
     * 
     * @param value
     * @return
     * @apiNote
     *          ByteBuffer usage from
     *          https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html
     */
    public static byte[] intToByteArray(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value);
        return buffer.array();
    }

    public static byte[] append(byte[] destination, byte source) {
        return append(destination, new byte[] { source });
    }

    /**
     * Appends a byte array to another byte array.
     * 
     * @param destination
     * @param source
     * @return
     * @apiNote
     *          System.arraycopy usage from
     *          https://docs.oracle.com/javase/8/docs/api/java/lang/System.html#arraycopy-java.lang.Object-int-java.lang.Object-int-int-
     */
    public static byte[] append(byte[] destination, byte[] source) {
        byte[] result = new byte[destination.length + source.length];
        System.arraycopy(destination, 0, result, 0, destination.length);
        System.arraycopy(source, 0, result, destination.length, source.length);
        return result;
    }

    public static byte[] subbyte(byte[] bytes, int startIndex, int length) {
        byte[] result = new byte[length];
        System.arraycopy(bytes, startIndex, result, 0, length);
        return result;
    }

    public static int toInteger(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.BIG_ENDIAN);
        return buffer.getInt();
    }

}
