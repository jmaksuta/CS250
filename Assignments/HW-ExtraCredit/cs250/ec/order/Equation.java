package cs250.ec.order;

/**
 * @author John Maksuta
 * @since 2024-04-19
 * @see Course: CS250-801 Spring 2024,
 *      Instructor: Professor Pallickara,
 *      Assignment: Extra Credit Assignment
 */
public class Equation {

    private int x;
    private int y;

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new Exception("The parameter is invalid. Expected length is 2.");
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
