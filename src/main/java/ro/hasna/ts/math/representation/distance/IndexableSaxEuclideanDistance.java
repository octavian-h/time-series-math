package ro.hasna.ts.math.representation.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.IndexableSymbolicAggregateApproximation;
import ro.hasna.ts.math.type.SaxPair;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two points using the iSAX representation.
 *
 * @since 1.0
 */
public class IndexableSaxEuclideanDistance implements DistanceMeasure {
    private final IndexableSymbolicAggregateApproximation isax;

    public IndexableSaxEuclideanDistance(IndexableSymbolicAggregateApproximation isax) {
        this.isax = isax;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double compute(double[] a, double[] b) {
        SaxPair[] symbolsA = isax.transformToSaxPairArray(a);
        SaxPair[] symbolsB = isax.transformToSaxPairArray(b);
        int n = a.length;

        return compute(symbolsA, symbolsB, n);
    }

    /**
     * Compute the distance between two iSAX representations.
     * The alphabet sizes must be powers of two.
     *
     * @param symbolsA the first iSAX representation
     * @param symbolsB the second representation
     * @param n        the length of the initial vectors
     * @return the distance between the two representations
     */
    public double compute(SaxPair[] symbolsA, SaxPair[] symbolsB, int n) {
        double sum = 0.0;
        int w = symbolsA.length;

        for (int i = 0; i < w; i++) {
            SaxPair spA = symbolsA[i];
            SaxPair spB = symbolsB[i];
            int sa = spA.getSymbol();
            int sb = spB.getSymbol();
            int alphabetSizeA = spA.getAlphabetSize();
            int alphabetSizeB = spB.getAlphabetSize();
            int alphabetSize;

            if (alphabetSizeA == alphabetSizeB) {
                alphabetSize = alphabetSizeA;
            } else {
                if (alphabetSizeA > alphabetSizeB) {
                    alphabetSize = alphabetSizeB;
                    sa = sa / (alphabetSizeA / alphabetSizeB);
                } else {
                    alphabetSize = alphabetSizeB;
                    sb = sb / (alphabetSizeB / alphabetSizeA);
                }
            }

            double[] breakpoints = isax.getDistributionDivider().getBreakpoints(alphabetSize);

            if (FastMath.abs(sa - sb) > 1) {
                double aux;
                if (sa > sb) {
                    aux = breakpoints[sa - 1] - breakpoints[sb];
                } else {
                    aux = breakpoints[sb - 1] - breakpoints[sa];
                }
                sum += aux * aux;
            }
        }

        return FastMath.sqrt(n * sum / w);
    }
}
