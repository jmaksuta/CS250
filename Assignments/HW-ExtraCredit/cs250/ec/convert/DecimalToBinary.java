package cs250.ec.convert;

import java.security.InvalidParameterException;

import cs250.ec.Common;
import cs250.ec.Common.ArgType;

public class DecimalToBinary {

    public static void main(String[] args) {
        try {
            if (args.length != 1) {
                throw new InvalidParameterException("The parameter is invalid. Expected length is 1.");
            }
            double toConvert = Common.decimalStringToDouble(args[0]);
            DecimalToBinary decimalToBinary = new DecimalToBinary();
            // String outputStr = decimalToBinary.convertDecimalToBinaryString(toConvert);
            double output = decimalToBinary.convertDecimalToBinary(toConvert);

            String msg = String.format("%f -> %f", toConvert, output);
            System.out.println(msg);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public DecimalToBinary() {
        super();
    }

    private String convertDecimalToBinaryString(double doubleToConvert) {
        String base2ValueStr = "";
        double base = 2.0;
        int startIndex = (int) Math.pow(doubleToConvert, 1.0 / base);
        int index = startIndex;
        
        while (!isEqualTo(doubleToConvert, 0.0)) {
            double power = Math.pow(2.0, index);
            if (doubleToConvert >= power) {
                doubleToConvert -= power;
                base2ValueStr += "1";
            } else {
                base2ValueStr += "0";
            }
            if (index == 0) {
                base2ValueStr += ".";
            }
            index--;
        }

        return base2ValueStr;
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
                base2Value += Math.pow(10.0, (double)index);
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
