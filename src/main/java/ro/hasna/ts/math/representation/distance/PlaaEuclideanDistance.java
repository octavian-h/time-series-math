package ro.hasna.ts.math.representation.distance;

import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.representation.PiecewiseLinearAggregateApproximation;
import ro.hasna.ts.math.type.MeanSlopePair;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two points using the PLAA representation.
 *
 * @since 1.0
 */
public class PlaaEuclideanDistance implements DistanceMeasure {
    private final PiecewiseLinearAggregateApproximation plaa;

    public PlaaEuclideanDistance(PiecewiseLinearAggregateApproximation plaa) {
        this.plaa = plaa;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double compute(double[] a, double[] b) {
        MeanSlopePair[] mspA = plaa.transformToMeanSlopePairArray(a);
        MeanSlopePair[] mspB = plaa.transformToMeanSlopePairArray(b);
        int n = a.length;

        return compute(mspA, mspB, n);
    }

    /**
     * Compute the distance between two PLAA representations.
     *
     * @param a the first PLAA representation
     * @param b the second representation
     * @param n the length of the initial vectors
     * @return the distance between the two representations
     */
    public double compute(MeanSlopePair[] a, MeanSlopePair[] b, int n) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        int w = a.length;
        for (int i = 0; i < w; i++) {
            double aux = a[i].getMean() - b[i].getMean();
            sum1 += aux * aux;

            aux = a[i].getSlope() - b[i].getSlope();
            sum2 += aux * aux;
        }

        double k = n * 1.0 / w;
        sum1 = sum1 * k;
        sum2 = sum2 * (k * k - 1) / 18;

        return FastMath.sqrt(sum1 + sum2);
    }
}
