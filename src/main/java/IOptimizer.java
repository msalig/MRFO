public interface IOptimizer {
    void initialize();
    void iterate();
    boolean termination();
}
