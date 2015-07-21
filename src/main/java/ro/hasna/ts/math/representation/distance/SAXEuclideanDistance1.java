package ro.hasna.ts.math.representation.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.SymbolicAggregateApproximation;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two points using the SAX representation.
 *
 * @since 1.0
 */
public class SAXEuclideanDistance1 implements DistanceMeasure {
    private final SymbolicAggregateApproximation sax;

    public SAXEuclideanDistance1(SymbolicAggregateApproximation sax) {
        this.sax = sax;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double compute(double[] a, double[] b) {
        int[] symbolsA = sax.transformToIntArray(a);
        int[] symbolsB = sax.transformToIntArray(b);
        int n = a.length;

        return compute(symbolsA, symbolsB, n);
    }

    /**
     * Compute the distance between two SAX representations.
     *
     * @param symbolsA the first SAX representation
     * @param symbolsB the second representation
     * @param n        the length of the initial vectors
     * @return the distance between the two representations
     */
    public double compute(int[] symbolsA, int[] symbolsB, int n) {
        double[] breakpoints = sax.getBreakpoints();

        double sum = 0.0;
        int w = symbolsA.length;
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
            }
        }

        return FastMath.sqrt(n * sum / w);
    }
}
