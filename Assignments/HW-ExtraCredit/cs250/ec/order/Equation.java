package cs250.ec.order;

import java.security.InvalidParameterException;

public class Equation {

    private int x;
    private int y;

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new InvalidParameterException("Expects 2 parameters");
            }
            Equation equation = new Equation(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            System.out.printf("%.5f\n", equation.perform());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Equation(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public double perform() {
        double numerator = Math.log10(Math.abs(x * y)) - Math.pow(x, 4);
        double denominator = Math.sqrt(Math.pow(x * y, 2)) + Math.pow(y, 3) * x;
        return numerator / denominator;
    }

}
