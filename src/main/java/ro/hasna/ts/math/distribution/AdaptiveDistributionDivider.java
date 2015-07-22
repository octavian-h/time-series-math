package ro.hasna.ts.math.distribution;

/**
 * Implements the adaptive areas of probability for a given base distribution divider.
 * <p>
 * Reference:
 * Pham N.D., Quang L.L., Tran K.D. (2010)
 * <i>Two Novel Adaptive Symbolic Representations for Similarity Search in Time Series Databases</i>
 * </p>
 *
 * @since 1.0
 */
public class AdaptiveDistributionDivider extends AbstractDistributionDivider {
    private final double[][] trainingSet;
    private final DistributionDivider baseDivider;
    private final double threshold;

    /**
     * Creates a new instance of this class with caching enabled and starting from the Normal Distribution breakpoints.
     *
     * @param trainingSet the set of normalized PAA segments for improving the breakpoints
     * @param threshold   the threshold used for terminating the algorithm
     */
    public AdaptiveDistributionDivider(double[][] trainingSet, double threshold) {
        this(true, trainingSet, threshold, new NormalDistributionDivider());
    }

    /**
     * Creates a new instance of this class.
     *
     * @param cachingEnabled flag to enable or not the caching
     * @param trainingSet    the set of normalized PAA segments for improving the breakpoints
     * @param threshold      the threshold used for terminating the algorithm
     * @param baseDivider    the base distribution divider used for initial breakpoints
     */
    public AdaptiveDistributionDivider(boolean cachingEnabled, double[][] trainingSet, double threshold, DistributionDivider baseDivider) {
        super(cachingEnabled);
        this.trainingSet = trainingSet;
        this.threshold = threshold;
        this.baseDivider = baseDivider;
    }

    @Override
    protected double[] computeBreakpoints(int areas) {
        double[] breakpoints = baseDivider.getBreakpoints(areas);
        boolean stop = false;
        double oldDelta = Double.MAX_VALUE;

        while (!stop) {
            double[] sum = new double[breakpoints.length + 1];
            int[] count = new int[breakpoints.length + 1];

            for (int i = 0; i < trainingSet.length; i++) {
                for (int j = 0; j < trainingSet[i].length; j++) {
                    boolean found = false;
                    for (int k = 0; k < breakpoints.length && !found; k++) {
                        double breakpoint = breakpoints[k];
                        if (breakpoint > trainingSet[i][j]) {
                            sum[k] += trainingSet[i][j];
                            count[k]++;
                            found = true;
                        }
                    }
                    if (!found) {
                        sum[breakpoints.length] += trainingSet[i][j];
                        count[breakpoints.length]++;
                    }
                }
            }
            double[] r = new double[breakpoints.length + 1];
            for (int i = 0; i < breakpoints.length + 1; i++) {
                r[i] = sum[i] / count[i];
            }
            for (int i = 0; i < breakpoints.length; i++) {
                breakpoints[i] = (r[i] + r[i + 1]) / 2;
            }

            double delta = 0.0;
            for (int i = 0; i < trainingSet.length; i++) {
                for (int j = 0; j < trainingSet[i].length; j++) {
                    boolean found = false;
                    for (int k = 0; k < breakpoints.length && !found; k++) {
                        double breakpoint = breakpoints[k];
                        if (breakpoint > trainingSet[i][j]) {
                            double aux = trainingSet[i][j] - r[k];
                            delta += aux * aux;
                            found = true;
                        }
                    }
                    if (!found) {
                        double aux = trainingSet[i][j] - r[breakpoints.length];
                        delta += aux * aux;
                    }
                }
            }

            if ((oldDelta - delta) / oldDelta >= threshold) {
                oldDelta = delta;
            } else {
                stop = true;
            }
        }

        return breakpoints;
    }
}
