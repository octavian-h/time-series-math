package ro.hasna.ts.math.distribution;

/**
 * Interface for distributions with breakpoints.
 *
 * @since 1.0
 */
public interface DistributionDivider {
    /**
     * Get the breakpoints for dividing the distribution in equal areas of probability.
     * NOTE: the breakpoints are in ascending order.
     *
     * @param areas the number of areas
     * @return the list of breakpoints
     */
    double[] getBreakpoints(int areas);
}
