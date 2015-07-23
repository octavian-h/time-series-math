package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link org.apache.commons.math3.ml.distance.EuclideanDistance} that
 * implements {@link GenericDistanceMeasure}.
 *
 * @since 1.0
 */
public class EuclideanDistanceAdapter implements GenericDistanceMeasure<double[]> {

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double sum = 0.0;
        double transformedCutoff = cutOffValue * cutOffValue;
        for (int i = 0; i < a.length; i++) {
            double aux = a[i] - b[i];
            sum += aux * aux;
            if (sum > transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return FastMath.sqrt(sum);
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }
}
