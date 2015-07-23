package ro.hasna.ts.math.representation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import ro.hasna.ts.math.distribution.DistributionDivider;
import ro.hasna.ts.math.distribution.NormalDistributionDivider;
import ro.hasna.ts.math.normalization.Normalizer;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.type.SaxPair;

/**
 * Implements the Indexable Symbolic Aggregate Approximation (iSAX) algorithm.
 * <p>
 * Reference:
 * Camerra A., Palpanas T., Shieh J., Keogh E. (2010)
 * <i>iSAX 2.0: Indexing and mining one billion time series</i>
 * </p>
 *
 * @since 1.0
 */
public class IndexableSymbolicAggregateApproximation implements GenericTransformer<double[], SaxPair[]> {
    private final PiecewiseAggregateApproximation paa;
    private final Normalizer normalizer;
    private final DistributionDivider distributionDivider;
    private final int[] alphabetSizes;

    /**
     * Creates a new instance of this class with a given number of segments and
     * the size of the alphabet.
     *
     * @param segments      the number of segments
     * @param alphabetSizes the size of the alphabet used to discretise the values for every segment
     */
    public IndexableSymbolicAggregateApproximation(int segments, int[] alphabetSizes) {
        this(new PiecewiseAggregateApproximation(segments), new ZNormalizer(), alphabetSizes, new NormalDistributionDivider());
    }


    /**
     * Creates a new instance of this class with a given PAA and normalizer algorithm and
     * the breakpoints used to divide the distribution of the values.
     *
     * @param paa                 the Piecewise Aggregate Approximation algorithm
     * @param normalizer          the normalizer (it can be null if the values were normalized)
     * @param alphabetSizes       the size of the alphabet used to discretise the values for every segment
     * @param distributionDivider the divider that gets the list of breakpoints (values that divide a
     *                            distribution in equal areas of probability)
     * @throws DimensionMismatchException if the number of segments is different than the length of alphabetSizes
     */
    public IndexableSymbolicAggregateApproximation(PiecewiseAggregateApproximation paa, Normalizer normalizer,
                                                   int[] alphabetSizes, DistributionDivider distributionDivider) {
        if (paa.getSegments() != alphabetSizes.length) {
            throw new DimensionMismatchException(alphabetSizes.length, paa.getSegments());
        }

        this.paa = paa;
        this.normalizer = normalizer;
        this.alphabetSizes = alphabetSizes;
        this.distributionDivider = distributionDivider;
    }

    /**
     * Transform a given sequence of values using the iSAX algorithm.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public SaxPair[] transform(double[] values) {
        double[] copy = paa.transform(values);

        // NOTE: mathematically the order of PAA and normalisation doesn't matter, the result is the same,
        // but comparing the speed, running PAA and then normalisation is faster than in the reverse order
        if (normalizer != null) {
            copy = normalizer.normalize(copy);
        }

        int n = 0;
        int segments = copy.length;
        SaxPair[] result = new SaxPair[segments];
        for (int i = 0; i < segments; i++) {
            double item = copy[i];
            int alphabetSize = alphabetSizes[i];
            double[] breakpoints = distributionDivider.getBreakpoints(alphabetSize);
            boolean found = false;
            for (int j = 0; j < breakpoints.length && !found; j++) {
                double breakpoint = breakpoints[j];
                if (breakpoint > item) {
                    result[n] = new SaxPair(j, alphabetSize);
                    found = true;
                }
            }
            if (!found) {
                result[n] = new SaxPair(breakpoints.length, alphabetSize);
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

    public DistributionDivider getDistributionDivider() {
        return distributionDivider;
    }

    public int[] getAlphabetSizes() {
        return alphabetSizes;
    }
}
