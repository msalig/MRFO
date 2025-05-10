import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MRFOOptimizerTest {

    /**
     * Test that the MRFO algorithm minimizes the Sphere function.
     * The Sphere function is defined as f(x) = sum(x_i^2), whose minimum is 0 at (0,...,0).
     */
    @Test
    public void testSphereFunctionMinimum() {
        // Parameters for the optimizer
        int populationSize = 30;
        int dimensions = 2;         // Testing in 2D space
        int maxIterations = 200;      // Sufficient iterations to converge (depending on randomness)
        double searchSpaceMin = -10.0;
        double searchSpaceMax = 10.0;

        // Create and initialize the optimizer
        IObjectiveFunction objectiveFunction = new AckleyFunction();
        MRFOOptimizer optimizer = new MRFOOptimizer(populationSize, dimensions, maxIterations, searchSpaceMin, searchSpaceMax, objectiveFunction);
        optimizer.initialize();

        // Run iterations until termination condition is met
        while (!optimizer.termination()) {
            optimizer.iterate();
        }

        double bestFitness = optimizer.getBestFitness();
        System.out.println("Best fitness after optimization: " + bestFitness);

        // The sphere function has a known minimum of 0. We test that the found value is close enough.
        double toleranceFitness = 1e-3;
        assertTrue(bestFitness < toleranceFitness,
                "Expected fitness < " + toleranceFitness + " but got " + bestFitness);

        double[] bestSolution = optimizer.getBestSolution();
        // Check that each coordinate is close to 0.
        double toleranceCoordinate = 1e-2;
        for (double x : bestSolution) {
            assertTrue(Math.abs(x) < toleranceCoordinate,
                    "Expected coordinate close to 0, but found " + x);
        }
    }

    /**
     * Test that the optimizer correctly terminates after the maximum number of iterations.
     */
    @Test
    public void testTermination() {
        int populationSize = 30;
        int dimensions = 2;
        int maxIterations = 50;
        IObjectiveFunction objectiveFunction = new AckleyFunction();
        MRFOOptimizer optimizer = new MRFOOptimizer(populationSize, dimensions, maxIterations, -10.0, 10.0, objectiveFunction);
        optimizer.initialize();

        // Run iterations until termination
        while (!optimizer.termination()) {
            optimizer.iterate();
        }

        // Verify that termination signals true and that the iteration counter equals maxIterations.
        assertTrue(optimizer.termination(), "Optimizer did not terminate as expected.");
        assertEquals(maxIterations, optimizer.getCurrentIteration(),
                "Expected current iteration to be " + maxIterations + " but was " + optimizer.getCurrentIteration());
    }
}
