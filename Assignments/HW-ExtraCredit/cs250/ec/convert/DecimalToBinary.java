package cs250.ec.convert;

/**
 * @author John Maksuta
 * @since 2024-04-19
 * @see Course: CS250-801 Spring 2024,
 *      Instructor: Professor Pallickara,
 *      Assignment: Extra Credit Assignment
 */
public class DecimalToBinary {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new Exception("The parameter is invalid. Expected length is 1.");
            }
            double toConvert = Common.decimalStringToDouble(args[0]);
            DecimalToBinary decimalToBinary = new DecimalToBinary();

            double output = decimalToBinary.convertDecimalToBinary(toConvert);
            String inputStr = Common.getSignificantNumber(toConvert);
            String outputStr = Common.getSignificantNumber(output);

            String msg = String.format("%s -> %s", inputStr, outputStr);
            System.out.println(msg);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public DecimalToBinary() {
        super();
    }

    private double convertDecimalToBinary(double doubleToConvert) {
        double base2Value = 0.0;
        double base = 2.0;
        int startIndex = (int) Math.pow(doubleToConvert, 1.0 / base);
        int index = startIndex;

        while (!isEqualTo(doubleToConvert, 0.0)) {
            double power = Math.pow(2.0, index);
            if (doubleToConvert >= power) {
                doubleToConvert -= power;
                base2Value += Math.pow(10.0, (double) index);
            }
            index--;
        }

        return base2Value;
    }

    private boolean isEqualTo(double valueA, double valueB) {
        boolean result = false;
        Double a = (Double) valueA;
        Double b = (Double) valueB;

        result = a.compareTo(b) == 0;
        return result;
    }

}
