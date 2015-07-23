package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.SymbolicAggregateApproximation;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two vectors using the SAX representation.
 *
 * @since 1.0
 */
public class SaxEuclideanDistance extends AbstractTimeSeriesDistance implements DistanceMeasure, GenericDistanceMeasure<int[]> {
    private final SymbolicAggregateApproximation sax;

    public SaxEuclideanDistance(SymbolicAggregateApproximation sax) {
        this.sax = sax;
    }

    @Override
    public double compute(double[] a, double[] b) {
        int[] symbolsA = sax.transform(a);
        int[] symbolsB = sax.transform(b);
        int n = a.length;

        return compute(symbolsA, symbolsB, n, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(int[] a, int[] b) {
        return compute(a, b, initialVectorLength, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(int[] a, int[] b, double cutoff) {
        return compute(a, b, initialVectorLength, Double.POSITIVE_INFINITY);
    }

    /**
     * Compute the distance between two SAX representations.
     *
     * @param symbolsA the first SAX representation
     * @param symbolsB the second representation
     * @param n        the length of the initial vectors
     * @param cutoff   if the distance being calculated is above this value stop computing the remaining distance
     * @return the distance between the two representations
     */
    public double compute(int[] symbolsA, int[] symbolsB, int n, double cutoff) {
        double[] breakpoints = sax.getBreakpoints();

        double sum = 0.0;
        int w = symbolsA.length;
        double transformedCutoff = cutoff * cutoff * w / n;

        for (int i = 0; i < w; i++) {
            int sa = symbolsA[i];
            int sb = symbolsB[i];
            if (FastMath.abs(sa - sb) > 1) {
                double aux;
                if (sa > sb) {
                    aux = breakpoints[sa - 1] - breakpoints[sb];
                } else {
                    aux = breakpoints[sb - 1] - breakpoints[sa];
                }
                sum += aux * aux;

                if (sum > transformedCutoff) {
                    return FastMath.sqrt(n * sum / w);
                }
            }
        }

        return FastMath.sqrt(n * sum / w);
    }
}
