public class MRFOConfig {

    int populationSize;
    int dimensions;
    int maxIterations;
    double searchSpaceMin;
    double searchSpaceMax;
    boolean enableLogging;

    public MRFOConfig(int populationSize,
                      int dimensions,
                      int maxIterations,
                      double searchSpaceMin,
                      double searchSpaceMax,
                      boolean enableLogging) {
        this.populationSize = populationSize;
        this.dimensions = dimensions;
        this.maxIterations = maxIterations;
        this.searchSpaceMin = searchSpaceMin;
        this.searchSpaceMax = searchSpaceMax;
        this.enableLogging = enableLogging;
    }

    public MRFOConfig(Builder builder) {
        this.populationSize = builder.populationSize;
        this.dimensions = builder.dimensions;
        this.maxIterations = builder.maxIterations;
        this.searchSpaceMin = builder.searchSpaceMin;
        this.searchSpaceMax = builder.searchSpaceMax;
        this.enableLogging = builder.enableLogging;
    }

    public static class Builder {

        int populationSize = -1;
        int dimensions = -1;
        int maxIterations = -1;
        double searchSpaceMin = Double.NaN;
        double searchSpaceMax = Double.NaN;
        boolean enableLogging = false;

        public Builder setPopulationSize(int populationSize) {
            this.populationSize = populationSize;
            return this;
        }

        public Builder setDimensions(int dimensions) {
            this.dimensions = dimensions;
            return this;
        }

        public Builder setMaxIterations(int maxIterations) {
            this.maxIterations = maxIterations;
            return this;
        }

        public Builder setSearchSpaceMin(double searchSpaceMin) {
            this.searchSpaceMin = searchSpaceMin;
            return this;
        }

        public Builder setSearchSpaceMax(double searchSpaceMax) {
            this.searchSpaceMax = searchSpaceMax;
            return this;
        }

        public Builder setEnableLogging(Boolean enableLogging) {
            this.enableLogging = enableLogging;
            return this;
        }

        public MRFOConfig build() {
            if (populationSize == -1 || dimensions == -1 || maxIterations == -1 || Double.isNaN(searchSpaceMin) || Double.isNaN(searchSpaceMax)) {
                throw new IllegalArgumentException("All arguments must be set");
            }

            return new MRFOConfig(this);
        }
    }
}
