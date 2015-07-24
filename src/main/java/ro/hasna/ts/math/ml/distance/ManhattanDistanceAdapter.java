package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link ManhattanDistance} that implements {@link GenericDistanceMeasure}.
 *
 * @since 1.0
 */
public class ManhattanDistanceAdapter extends ManhattanDistance implements GenericDistanceMeasure<double[]> {

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += FastMath.abs(a[i] - b[i]);
            if (sum > cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return sum;
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }
}
