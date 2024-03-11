package cs250.hw3;

public class Main {

    public static final int MIN_PORT_NUMBER = 1025;
    public static final int MAX_PORT_NUMBER = 65535;

    public static final int MIN_UNSIGNED_INTEGER = 0;
    public static final int MAX_UNSIGNED_INTEGER = 2147483647;

    public static void validateCommandLineArgs(String[] args, IMain mainInterface) throws Exception {
        mainInterface.validateCommandLineArgs();
    }

}
