package ro.hasna.ts.math.representation;

import ro.hasna.ts.math.distribution.NormalDistributionDivider;
import ro.hasna.ts.math.normalization.Normalizer;
import ro.hasna.ts.math.normalization.ZNormalizer;

/**
 * Implements the Symbolic Aggregate Approximation (SAX) algorithm.
 * <p>
 * Reference:
 * Jessica L., Keogh E., Lonardi S., Chiu B. (2003)
 * <i>A symbolic representation of time series, with implications for streaming algorithms</i>
 * </p>
 *
 * @since 1.0
 */
public class SymbolicAggregateApproximation implements GenericTransformer<double[], int[]> {
    private final PiecewiseAggregateApproximation paa;
    private final Normalizer normalizer;
    private final double[] breakpoints;

    /**
     * Creates a new instance of this class with a given number of segments and
     * the size of the alphabet.
     *
     * @param segments     the number of segments
     * @param alphabetSize the size of the alphabet used to discretise the values
     */
    public SymbolicAggregateApproximation(int segments, int alphabetSize) {
        this(new PiecewiseAggregateApproximation(segments), new ZNormalizer(), new NormalDistributionDivider().getBreakpoints(alphabetSize));
    }

    /**
     * Creates a new instance of this class with a given number of segments and
     * the breakpoints used to divide the distribution of the values.
     *
     * @param segments    the number of segments
     * @param breakpoints the list of values that divide the distribution of the values
     *                    in equal areas of probability
     */
    public SymbolicAggregateApproximation(int segments, double[] breakpoints) {
        this(new PiecewiseAggregateApproximation(segments), new ZNormalizer(), breakpoints);
    }

    /**
     * Creates a new instance of this class with a given PAA and normalizer algorithm and
     * the breakpoints used to divide the distribution of the values.
     *
     * @param paa         the Piecewise Aggregate Approximation algorithm
     * @param normalizer  the normalizer (it can be null if the values were normalized)
     * @param breakpoints the list of values that divide the distribution of the values
     *                    in equal areas of probability
     */
    public SymbolicAggregateApproximation(PiecewiseAggregateApproximation paa, Normalizer normalizer, double[] breakpoints) {
        this.paa = paa;
        this.normalizer = normalizer;
        this.breakpoints = breakpoints;
    }

    /**
     * Transform a given sequence of values using the SAX algorithm.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public int[] transform(double[] values) {
        double[] copy = paa.transform(values);

        // NOTE: mathematically the order of PAA and normalisation doesn't matter, the result is the same,
        // but comparing the speed, running PAA and then normalisation is faster than in the reverse order
        if (normalizer != null) {
            copy = normalizer.normalize(copy);
        }

        int n = 0;
        int[] result = new int[copy.length];
        for (double item : copy) {
            boolean found = false;
            for (int i = 0; i < breakpoints.length && !found; i++) {
                double breakpoint = breakpoints[i];
                if (breakpoint > item) {
                    result[n] = i;
                    found = true;
                }
            }
            if (!found) {
                result[n] = breakpoints.length;
            }
            n++;
        }

        return result;
    }

    public PiecewiseAggregateApproximation getPaa() {
        return paa;
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public double[] getBreakpoints() {
        return breakpoints;
    }

    public int getAlphabetSize() {
        return breakpoints.length + 1;
    }
}
