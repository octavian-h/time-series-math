package ro.hasna.ts.math.distribution;

/**
 * Implements the equal areas of probability for Uniform Distribution.
 *
 * @since 1.0
 */
public class UniformDistributionDivider implements DistributionDivider {
    private final double lower;
    private final double upper;

    /**
     * Creates a new instance of this class.
     *
     * @param lower the minimum value from the distribution
     * @param upper the maximum value from the distribution
     */
    public UniformDistributionDivider(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getBreakpoints(int areas) {
        int len = areas - 1;
        double[] result = new double[len];
        double intervalSize = (upper - lower) / areas;
        for (int i = 0; i < len; i++) {
            result[i] = lower + (i + 1) * intervalSize;
        }

        return result;
    }
}
