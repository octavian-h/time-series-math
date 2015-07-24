package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link ChebyshevDistance} that implements {@link GenericDistanceMeasure}.
 *
 * @since 1.0
 */
public class ChebyshevDistanceAdapter extends ChebyshevDistance implements GenericDistanceMeasure<double[]> {

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double max = 0;
        for (int i = 0; i < a.length; i++) {
            max = FastMath.max(max, FastMath.abs(a[i] - b[i]));
            if (max > cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return max;
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }
}
