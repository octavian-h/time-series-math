package ro.hasna.ts.math.representation;

import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Implements the Discrete Cosine Transformation using {@link FastCosineTransformer}.
 *
 * @since 1.0
 */
public class DiscreteCosineTransform {
    private FastCosineTransformer cosineTransformer;

    public DiscreteCosineTransform() {
        this(new FastCosineTransformer(DctNormalization.STANDARD_DCT_I));
    }

    public DiscreteCosineTransform(FastCosineTransformer cosineTransformer) {
        this.cosineTransformer = cosineTransformer;
    }

    /**
     * Transform a given sequence of values into Fourier coefficients using {@link FastCosineTransformer}.
     * The sequence is padded with zeros if it hasn't the right length.
     *
     * @param values the sequence of values
     * @return the result of the transformation
     */
    public double[] transformToDoubleArray(double[] values) {
        // pad the input array with zeros so as to have a length == 2^k + 1
        int initialLength = values.length;
        int powerOfTwo = Integer.highestOneBit(initialLength);
        int requiredLength = powerOfTwo + 1;
        if (initialLength != requiredLength) {
            if (initialLength != powerOfTwo) {
                requiredLength = (powerOfTwo << 1) + 1;
            }
        }
        double[] copy = new double[requiredLength];
        System.arraycopy(values, 0, copy, 0, initialLength);
        for (int i = initialLength; i < requiredLength; i++) {
            copy[i] = 0;
        }

        // run FCT (=> DCT-I)
        double[] transform = cosineTransformer.transform(copy, TransformType.FORWARD);

        // keep only the most important coefficients
        int outputLength = (powerOfTwo >> 1) + 1;
        double[] result = new double[outputLength];
        for (int i = 0; i < outputLength && i < transform.length; i++) {
            result[i] = transform[i];
        }
        return result;
    }
}
