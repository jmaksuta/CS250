package cs250.hw1;

/**
 * @author John Maksuta
 * @since 01/27/2024
 */
public class TestOperations {

    @SuppressWarnings("unused")
    private void testMod() {
        try {
            for (int operand = 0; operand < 100; operand++) {
                for (int modulus = 1; modulus < 100; modulus++) {
                    double expected = operand % modulus;
                    double actual = Common.mod(operand, modulus);

                    if (expected != actual) {
                        throw new Exception(
                                String.format("operand=%d, modulus=%d, expected %f, actual %f", operand, modulus,
                                        expected, actual));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
