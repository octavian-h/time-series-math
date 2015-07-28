package ro.hasna.ts.math.representation;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.FastMath;

/**
 * Implements the Discrete Chebyshev Transformation using {@link FastFourierTransformer}.
 *
 * @since 1.0
 */
public class DiscreteChebyshevTransform implements GenericTransformer<double[], double[]> {
    private final FastFourierTransformer fourierTransformer;

    public DiscreteChebyshevTransform() {
        this(new FastFourierTransformer(DftNormalization.STANDARD));
    }

    public DiscreteChebyshevTransform(FastFourierTransformer fourierTransformer) {
        this.fourierTransformer = fourierTransformer;
    }

    @Override
    public double[] transform(double[] values) {
        int length = values.length;
        if (length < 3) {
            int n = length - 1;
            double[] result = new double[length];
            for (int i = 0; i <= n; i++) {
                double sum = 0;
                for (int j = 0; j <= n; j++) {
                    if (j == 0 || j == n) {
                        sum += values[j] * FastMath.cos(i * j * FastMath.PI / n) / 2;
                    } else {
                        sum += values[j] * FastMath.cos(i * j * FastMath.PI / n);
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

        int end = length * 2 - 2;

        // pad the input array with zeros so as to have a length == 2^k-1
        int powerOfTwo = Integer.highestOneBit(end);
        if (end != powerOfTwo) {
            powerOfTwo = powerOfTwo << 1;
        }
        double[] copy = new double[powerOfTwo];
        System.arraycopy(values, 0, copy, 0, length);
        // copy 1..n-1 in reverse order at the back of the array
        for (int i = 1; i < length - 1; i++) {
            copy[end - i] = values[i];
        }

        Complex[] complexes = fourierTransformer.transform(copy, TransformType.FORWARD);
        double[] result = new double[length];
        result[0] = complexes[0].getReal() / powerOfTwo;
        for (int i = 1; i < length && i < complexes.length; i++) {
            result[i] = 2 * complexes[i].getReal() / powerOfTwo;
        }
        result[length - 1] /= 2;
        return result;
    }
}
