import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        if (args.length != 0) {

        }

        IObjectiveFunction objectiveFunction = new AckleyFunction();

        // Create an instance of MRFOOptimizer with 30 individuals,
        // 5 dimensions, maximum 100 iterations, and search space [-10, 10].
        MRFOOptimizer optimizer = new MRFOOptimizer(30, 5, 100, -10.0, 10.0, objectiveFunction);
        optimizer.initialize();

        // Run the optimization until termination.
        while (!optimizer.termination()) {
            optimizer.iterate();
        }

        // Print out the best solution and its fitness.
        logger.log(Level.INFO, "Optimization complete!");
        logger.log(Level.INFO, "Best fitness: {0}", optimizer.getBestFitness());
        logger.log(Level.INFO, "Best solution: ");
        for (double x : optimizer.getBestSolution()) {
            logger.log(Level.INFO, "{0}", x);
        }
    }
}
