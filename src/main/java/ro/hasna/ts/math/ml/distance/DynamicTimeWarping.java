package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.util.FastMath;

/**
 * Calculates the distance between two vectors using Dynamic Time Warping.
 * <p>
 * Reference:
 * Wikipedia https://en.wikipedia.org/wiki/Dynamic_time_warping
 * </p>
 *
 * @since 1.0
 */
public class DynamicTimeWarping implements GenericDistanceMeasure<double[]> {
    private int radius;

    public DynamicTimeWarping(int radius) {
        this.radius = radius;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        //TODO compute lower bounds
        return computeFullDtw(a, b, cutOffValue);
    }

    protected double computeFullDtw(double[] a, double[] b, double cutOffValue) {
        int n = a.length;
        int m = b.length;
        double[][] d = new double[n + 1][m + 1];

        //TODO use only two arrays
        int w = FastMath.max(radius, FastMath.abs(n - m));
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                d[i][j] = Double.POSITIVE_INFINITY;
            }
        }

        d[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            int start = FastMath.max(1, i - w);
            int end = FastMath.min(m, i + w);
            for (int j = start; j <= end; j++) {
                double aux = FastMath.abs(a[i - 1] - b[j - 1]);
                d[i][j] = aux + FastMath.min(d[i - 1][j], FastMath.min(d[i][j - 1], d[i - 1][j - 1]));
                if (d[i][j] > cutOffValue) {
                    return Double.POSITIVE_INFINITY;
                }
            }
        }

        return d[n][m];
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }
}
