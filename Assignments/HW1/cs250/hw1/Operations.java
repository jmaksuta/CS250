package cs250.hw1;

import java.util.ArrayList;
import cs250.hw1.Common.ArgType;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
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
                ArgType argType = Common.identifyArg(arg);
                builder.append(String.format("%s=%s\n", arg, argType));
            }

            print(builder.toString());
        }

        private void task3() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 3\n");
            boolean allArgsValid = true;
            for (String arg : args) {
                ArgType argType = Common.identifyArg(arg);
                boolean isValid = Validate.validate(arg, argType);
                builder.append(String.format("%s=%s\n", arg, isValid));
                if (!isValid) {
                    allArgsValid = false;
                }
            }

            print(builder.toString());
            if (!allArgsValid) {
                throw new IllegalArgumentException("Invalid arguments.");
            }
        }

        private void task4() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 4\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(arg, ArgType.BINARY, false, true);
                String decimal = Convert.convertArgument(arg, ArgType.DECIMAL, false, true);
                String hexadecimal = Convert.convertArgument(arg, ArgType.HEXADECIMAL, false, true);
                builder.append(String.format("Start=%s,Binary=%s,Decimal=%s,Hexadecimal=%s\n", arg, binary, decimal,
                        hexadecimal));
            }

            print(builder.toString());
        }

        private void task5() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 5\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(new StringBuilder(arg).toString(), ArgType.BINARY, false,
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
                String binary = Convert.convertArgument(new StringBuilder(arg).toString(), ArgType.BINARY, false,
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
                list.add(Convert.convertArgument(arg, ArgType.BINARY, false, false));
            }

            String[] arr = list.toArray(new String[] {});

            String or = Bitwise.or(arr);
            String and = Bitwise.and(arr);
            String xOr = Bitwise.xOr(arr);

            builder.append(String.format("%s|%s|%s=%s\n", arr[0], arr[1], arr[2], or));
            builder.append(String.format("%s&%s&%s=%s\n", arr[0], arr[1], arr[2], and));
            builder.append(String.format("%s^%s^%s=%s\n", arr[0], arr[1], arr[2], xOr));

            print(builder.toString());
        }

        private void task8() {
            StringBuilder builder = new StringBuilder();
            builder.append("Task 8\n");
            for (String arg : args) {
                String binary = Convert.convertArgument(arg, ArgType.BINARY, false, false);
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

}