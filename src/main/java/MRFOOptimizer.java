import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;

import static java.lang.Math.*;

public class MRFOOptimizer implements IOptimizer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int populationSize;
    private final int dimensions;
    private final int maxIterations;
    private final double searchSpaceMin;
    private final double searchSpaceMax;

    private int currentIteration = 0;
    private double[][] population;  // Positions of each individual (Manta Ray)
    private double[] bestSolution;  // Best solution found so far
    private double bestFitness = Double.POSITIVE_INFINITY;

    private final Random rand;
    private final IObjectiveFunction objectiveFunction;
    private final boolean isLoggingEnabled;

    public double[] getBestSolution() {
        return bestSolution;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    /**
     * Constructor to initialize the optimizer with its parameters.
     *
     * @param config            The configuration for the optimizer
     * @param objectiveFunction The evaluation function
     */
    public MRFOOptimizer(MRFOConfig config, IObjectiveFunction objectiveFunction) {
        this.populationSize = config.populationSize;
        this.dimensions = config.dimensions;
        this.maxIterations = config.maxIterations;
        this.searchSpaceMin = config.searchSpaceMin;
        this.searchSpaceMax = config.searchSpaceMax;
        this.rand = new Random(1412478894212413894L);
        this.objectiveFunction = objectiveFunction;
        this.isLoggingEnabled = config.enableLogging;
    }

    /**
     * Initializes the population with random positions within the search space.
     * Also evaluates the initial population to determine the best solution.
     */
    @Override
    public void initialize() {
        population = new double[populationSize][dimensions];

        // Randomly initialize each individual in the population
        for (int i = 0; i < populationSize; i++) {
            for (int d = 0; d < dimensions; d++) {
                population[i][d] = generateRandomPosition();
            }
        }

        // Determine the best solution in the initial population
        bestSolution = new double[dimensions];
        for (int i = 0; i < populationSize; i++) {
            double fitness = objectiveFunction.evaluate(population[i]);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                System.arraycopy(population[i], 0, bestSolution, 0, dimensions);
            }
        }

        currentIteration = 0;
    }

    /**
     * Executes one iteration of the MRFO algorithm.
     * Implements the chain foraging (exploration and exploitation) update rule.
     */
    @Override
    public void iterate() {
        // Compute somersault factor; decreases with iterations to refine search.
        double somersaultFactor = 2.0 - ((double) currentIteration / maxIterations) * 2.0;

        // Update each individual's position using the MRFO update rule.
        for (int i = 0; i < populationSize; i++) {
            double r = rand.nextDouble();
            for (int d = 0; d < dimensions; d++) {
                double newPos;

                if (r < 0.5) {
                    newPos = cycloneForaging(i, d, r);
                } else {
                    newPos = chainForaging(i, d);
                }

                // Ensure the new position remains within bounds.
                newPos = Math.max(searchSpaceMin, Math.min(newPos, searchSpaceMax));
                population[i][d] = newPos;
            }

            evaluateFitness(i);

            for (int d = 0; d < dimensions; d++) {
                double newPos = somersaultForaging(somersaultFactor, i, d);

                // Ensure the new position remains within bounds.
                newPos = Math.max(searchSpaceMin, Math.min(newPos, searchSpaceMax));
                population[i][d] = newPos;
            }

            evaluateFitness(i);
        }

        if (isLoggingEnabled) {
            logIteration();
        }

        currentIteration++;
    }

    /**
     * Termination condition: returns true when the maximum number of iterations is reached.
     *
     * @return true if the algorithm should terminate, false otherwise.
     */
    @Override
    public boolean termination() {
        return currentIteration >= maxIterations;
    }

    /**
     * @param i the individual
     * @param d the dimension
     * @return the new chain foraging position
     */
    private double chainForaging(int i, int d) {
        double r = rand.nextDouble();
        double alpha = 2 * r * sqrt(abs(log(r)));

        if (i == 0) {
            return population[i][d] + r * (bestSolution[d] - population[i][d]) + alpha * (bestSolution[d] - population[i][d]);
        } else {
            return population[i][d] + r * (population[i - 1][d] - population[i][d]) + alpha * (bestSolution[d] - population[i][d]);
        }
    }

    /**
     * @param i the individual
     * @param d the dimension
     * @param r a random number between 0 and 1
     * @return the new cyclone position
     */
    private double cycloneForaging(int i, int d, double r) {
        double newBestSolution = bestSolution[d];
        double r1 = rand.nextDouble();
        double beta = 2 * exp((r1 * (maxIterations - currentIteration + 1)) / maxIterations) * sin(2 * PI * r1);

        if ((double) currentIteration / maxIterations < r) {
            newBestSolution = generateRandomPosition();
        }

        if (i == 0) {
            return newBestSolution + r * (bestSolution[d] - population[i][d]) + beta * (bestSolution[d] - population[i][d]);
        } else {
            return newBestSolution + r * (population[i - 1][d] - population[i][d]) + beta * (bestSolution[d] - population[i][d]);
        }
    }

    /**
     * @param s the somersault factor
     * @param i the individual
     * @param d the dimension
     * @return the new somersault position
     */
    private double somersaultForaging(double s, int i, int d) {
        return population[i][d] + s * (rand.nextDouble() * bestSolution[d] - rand.nextDouble() * population[i][d]);
    }

    /**
     * Generates a random position
     *
     * @return a random position
     */
    private double generateRandomPosition() {
        return searchSpaceMin + (searchSpaceMax - searchSpaceMin) * rand.nextDouble();
    }

    /**
     * @param i the individual
     */
    private void evaluateFitness(int i) {
        // Evaluate the updated population and update the best found solution.
        double fitness = objectiveFunction.evaluate(population[i]);
        if (fitness < bestFitness) {
            bestFitness = fitness;
            System.arraycopy(population[i], 0, bestSolution, 0, dimensions);
        }
    }

    private void logIteration() {
        logger.info("Iteration: {}, global best solution: {}, fitness: {}", currentIteration, Arrays.toString(bestSolution), bestFitness);
    }
}
