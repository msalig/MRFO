import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        MRFOConfig config = parseCLIArgs(args);

        IObjectiveFunction objectiveFunction = new AckleyFunction();
        MRFOOptimizer optimizer = new MRFOOptimizer(config, objectiveFunction);
        optimizer.initialize();

        while (!optimizer.termination()) {
            optimizer.iterate();
        }

        logger.info("Optimization complete!");
        logger.info("Best fitness: {}", optimizer.getBestFitness());
        logger.info("Best solution: {}", Arrays.toString(optimizer.getBestSolution()));
    }

    private static MRFOConfig parseConfigFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, MRFOConfig.class);
    }

    private static MRFOConfig parseCLIArgs(String[] args) throws IOException {

        MRFOConfig.Builder configBuilder = new MRFOConfig.Builder();

        if(args.length == 0) {
            printHelp();
        }

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "help", "-h", "--help" -> printHelp();
                case "-f", "--file" -> {
                    return parseConfigFile(args[++i]);
                }
                case "-N", "--populationSize" -> configBuilder.setPopulationSize(Integer.parseInt(args[++i]));
                case "-d", "--dimensions" -> configBuilder.setDimensions(Integer.parseInt(args[++i]));
                case "-L", "--searchSpaceMin" -> configBuilder.setSearchSpaceMin(Double.parseDouble(args[++i]));
                case "-U", "--searchSpaceMax" -> configBuilder.setSearchSpaceMax(Double.parseDouble(args[++i]));
                case "-T", "--maxIterations" -> configBuilder.setMaxIterations(Integer.parseInt(args[++i]));
                case "-v", "--verbose" -> {
                    configBuilder.setEnableLogging(true);
                    ++i;
                }
                default -> {
                    logger.error("Unknown command: {}", arg);
                    System.exit(1);
                }
            }
        }

        return configBuilder.build();
    }

    private static void printHelp() {
        System.out.println("""
                Manta Ray Foraging Optimization (MRFO)
                
                MRFO is a bio-inspired metaheuristic algorithm that mimics the chain,
                somersault, and cyclone foraging behaviors of manta rays to balance exploration and exploitation in
                global optimization problems.
                
                USAGE
                  $ mrfo -f parameters.json
                  or
                  $ mrfo -N 30 -d 2 -T 200 -L -10.0 -U 10.0 --verbose
                
                OPTIONS
                  -d, --dimensions <int>            filter by space
                  -h, --help                        shows this help message
                  -L, --searchSpaceMin <double>     the search spaces lower bound
                  -N, --populationSize <int>        the population size
                  -T, --maxIterations <int>         the maximum number of iterations
                  -U, --searchSpaceMax <double>     the search spaces upper bound
                  -v, --verbose                     enables logging of each iteration, defaults to false
                
                COMMANDS
                  help     shows this help message
                """);
        System.exit(0);
    }
}
