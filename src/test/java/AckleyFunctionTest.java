import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AckleyFunctionTest {

    @Test
    public void testGlobalMinimum() {
        double[] minimumPoint = {0.0, 0.0}; // Global minimum
        double expectedValue = 0.0;

        AckleyFunction ackleyFunction = new AckleyFunction();
        double computedValue = ackleyFunction.evaluate(minimumPoint);

        assertEquals(expectedValue, computedValue, 1e-6,
                "Ackley function should return ~0 at global minimum");
    }
}
