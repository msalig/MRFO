import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MRFOOptimizer implements IOptimizer {

    private final Logger logger = Logger.getLogger(MRFOOptimizer.class.getName());

    // Algorithm parameters
    private final int populationSize;
    private final int dimensions;
    private final int maxIterations;
    private final double searchSpaceMin;
    private final double searchSpaceMax;

    // State variables
    private int currentIteration = 0;
    private double[][] population;  // Positions of each individual (Manta Ray)
    private double[] bestSolution;  // Best solution found so far
    private double bestFitness = Double.POSITIVE_INFINITY;

    private final Random rand;
    private final IObjectiveFunction objectiveFunction;

    /**
     * Constructor to initialize the optimizer with its parameters.
     *
     * @param populationSize  Number of manta rays (solutions)
     * @param dimensions      Dimensionality of the search space
     * @param maxIterations   Maximum number of iterations
     * @param searchSpaceMin  Lower bound of the search space
     * @param searchSpaceMax  Upper bound of the search space
     * @param objectiveFunction The evaluation function
     */
    public MRFOOptimizer(int populationSize, int dimensions, int maxIterations,
                         double searchSpaceMin, double searchSpaceMax, IObjectiveFunction objectiveFunction) {
        this.populationSize = populationSize;
        this.dimensions = dimensions;
        this.maxIterations = maxIterations;
        this.searchSpaceMin = searchSpaceMin;
        this.searchSpaceMax = searchSpaceMax;
        this.rand = new Random(1412478894212413894L);
        this.objectiveFunction = objectiveFunction;
    }

    /**
     * Initializes the population with random positions within the search space.
     * Also evaluates the initial population to determine the best solution.
     */
    @Override
    public void initialize() {
        population = new double[populationSize][dimensions];
        // Randomly initialize each individual in the population.
        for (int i = 0; i < populationSize; i++) {
            for (int d = 0; d < dimensions; d++) {
                population[i][d] = searchSpaceMin + (searchSpaceMax - searchSpaceMin) * rand.nextDouble();
            }
        }
        // Determine the best solution in the initial population.
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
            double A = 2.0 * somersaultFactor * r - somersaultFactor;
            double C = 2.0 * r;

            for (int d = 0; d < dimensions; d++) {
                // Chain Foraging update equation:
                // X_new = bestSolution[d] - A * |C * bestSolution[d] - X_current|
                double newPos = bestSolution[d] - A * Math.abs(C * bestSolution[d] - population[i][d]);
                // Ensure the new position remains within bounds.
                newPos = Math.max(searchSpaceMin, Math.min(newPos, searchSpaceMax));
                population[i][d] = newPos;
            }
        }

        // Evaluate the updated population and update the best found solution.
        for (int i = 0; i < populationSize; i++) {
            double fitness = objectiveFunction.evaluate(population[i]);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                System.arraycopy(population[i], 0, bestSolution, 0, dimensions);
            }
        }

        logger.log(Level.INFO, "Iteration: {0}", currentIteration);
        logger.log(Level.INFO, "Global-Best-Solution: {0}", Arrays.toString(bestSolution));
        logger.log(Level.INFO, "Fitness: {0}", bestFitness);
        //logger.log(Level.INFO, "Auswahlentscheidungen: {0}", );
        //logger.log(Level.INFO, "Paramteränderungen: {0}", );
        logger.log(Level.INFO, "");

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

    public double[] getBestSolution() {
        return bestSolution;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }
}
