
public class AckleyFunction implements IObjectiveFunction {

    private int a = 20;
    private double b = 0.2;
    private double c = 2 * Math.PI;

    public AckleyFunction() {}

    public AckleyFunction(int a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Computes the Ackley function for a given point.
     *
     * @param x the point in d-dimensional space
     * @return the Ackley function value at point x
     */
    @Override
    public double evaluate(double[] x) {
        int d = x.length;
        double sumSquares = 0.0;
        double sumCos = 0.0;

        for (double v : x) {
            sumSquares += v * v;
            sumCos += Math.cos(c * v);
        }

        double term1 = -a * Math.exp(-b * Math.sqrt(sumSquares / d));
        double term2 = -Math.exp(sumCos / d);

        return term1 + term2 + a + Math.E;
    }
}
