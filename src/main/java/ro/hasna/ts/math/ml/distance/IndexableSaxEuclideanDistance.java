package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.IndexableSymbolicAggregateApproximation;
import ro.hasna.ts.math.type.SaxPair;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two vectors using the iSAX representation.
 *
 * @since 1.0
 */
public class IndexableSaxEuclideanDistance extends AbstractTimeSeriesDistance implements DistanceMeasure, GenericDistanceMeasure<SaxPair[]> {
    private final IndexableSymbolicAggregateApproximation isax;

    public IndexableSaxEuclideanDistance(IndexableSymbolicAggregateApproximation isax) {
        this.isax = isax;
    }

    @Override
    public double compute(double[] a, double[] b) {
        SaxPair[] symbolsA = isax.transform(a);
        SaxPair[] symbolsB = isax.transform(b);
        int n = a.length;

        return compute(symbolsA, symbolsB, n, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(SaxPair[] a, SaxPair[] b) {
        return compute(a, b, initialVectorLength, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(SaxPair[] a, SaxPair[] b, double cutOffValue) {
        return compute(a, b, initialVectorLength, cutOffValue);
    }

    /**
     * Compute the distance between two iSAX representations.
     * The alphabet sizes must be powers of two.
     *
     * @param symbolsA the first iSAX representation
     * @param symbolsB the second representation
     * @param n        the length of the initial vectors
     * @param cutoff   if the distance being calculated is above this value stop computing the remaining distance
     * @return the distance between the two representations
     */
    public double compute(SaxPair[] symbolsA, SaxPair[] symbolsB, int n, double cutoff) {
        double sum = 0.0;
        int w = symbolsA.length;
        double transformedCutoff = cutoff * cutoff * w / n;

        for (int i = 0; i < w; i++) {
            double[] boundsA = getBounds(symbolsA[i]);
            double[] boundsB = getBounds(symbolsB[i]);
            double diff = 0.0;

            if (boundsA[1] != Double.POSITIVE_INFINITY && boundsB[0] != Double.NEGATIVE_INFINITY && boundsA[1] < boundsB[0]) {
                diff = boundsA[1] - boundsB[0];
            } else if (boundsA[0] != Double.NEGATIVE_INFINITY && boundsB[1] != Double.POSITIVE_INFINITY && boundsA[0] > boundsB[1]) {
                diff = boundsA[0] - boundsB[1];
            }

            sum += diff * diff;
            if (sum > transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }
        }

        return FastMath.sqrt(n * sum / w);
    }

    private double[] getBounds(SaxPair saxPair) {
        double[] bounds = {Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY};

        double[] breakpoints = isax.getDistributionDivider().getBreakpoints(saxPair.getAlphabetSize());

        int symbol = saxPair.getSymbol();
        if (symbol == 0) {
            bounds[1] = breakpoints[0];
        } else if (symbol == breakpoints.length) {
            bounds[0] = breakpoints[symbol - 1];
        } else {
            bounds[0] = breakpoints[symbol - 1];
            bounds[1] = breakpoints[symbol];
        }

        return bounds;
    }
}
