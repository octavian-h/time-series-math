package ro.hasna.ts.math.representation;

import org.apache.commons.math3.util.FastMath;

/**
 * Implements the Discrete Chebyshev Transformation.
 *
 * @since 1.0
 */
public class DiscreteChebyshevTransform implements GenericTransformer<double[], double[]> {

    @Override
    public double[] transform(double[] v) {
        int n = v.length - 1;
        double[] result = new double[n + 1];
        for (int i = 0; i <= n; i++) {
            double sum = 0;
            for (int j = 0; j <= n; j++) {
                if (j == 0 || j == n) {
                    sum += v[j] * FastMath.cos(i * j * FastMath.PI / n) / 2;
                } else {
                    sum += v[j] * FastMath.cos(i * j * FastMath.PI / n);
                }
            }
            if (i == 0 || i == n) {
                result[i] = sum / n;
            } else {
                result[i] = sum * 2 / n;
            }
        }
        return result;
    }
}
