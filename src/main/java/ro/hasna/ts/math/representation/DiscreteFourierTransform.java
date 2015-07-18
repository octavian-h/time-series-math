package ro.hasna.ts.math.representation;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Implements the Discrete Fourier Transformation using {@link FastFourierTransformer}.
 *
 * @since 1.0
 */
public class DiscreteFourierTransform {
    private FastFourierTransformer fourierTransformer;

    public DiscreteFourierTransform() {
        this(new FastFourierTransformer(DftNormalization.STANDARD));
    }

    public DiscreteFourierTransform(FastFourierTransformer fourierTransformer) {
        this.fourierTransformer = fourierTransformer;
    }

    /**
     * Transform a given sequence of values into Fourier coefficients using {@link FastFourierTransformer}.
     * The sequence is padded with zeros if it hasn't the right length.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public double[] transformToDoubleArray(double[] values) {
        // pad the input array with zeros so as to have a length == 2^k
        int initialLength = values.length;
        int powerOfTwo = Integer.highestOneBit(initialLength);
        if (initialLength != powerOfTwo) {
            powerOfTwo = powerOfTwo << 1;
        }
        double[] copy = new double[powerOfTwo];
        System.arraycopy(values, 0, copy, 0, initialLength);
        for (int i = initialLength; i < powerOfTwo; i++) {
            copy[i] = 0;
        }

        // run FFT
        Complex[] complexes = fourierTransformer.transform(copy, TransformType.FORWARD);

        // keep only the most important coefficients
        int outputLength = (powerOfTwo >> 1) + 1;
        double[] result = new double[outputLength];
        double k = 2.0 / initialLength;
        result[0] = complexes[0].divide(initialLength).abs();
        for (int i = 1; i < outputLength && i < complexes.length; i++) {
            // multiply the values with 2/N
            result[i] = complexes[i].multiply(k).abs();
        }
        return result;
    }
}
