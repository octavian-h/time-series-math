package ro.hasna.ts.math.representation;

/**
 * Implements the Haar Wavelet Transformation.
 * <p>
 * Reference:
 * Wikipedia https://en.wikipedia.org/wiki/Discrete_wavelet_transform
 * </p>
 *
 * @since 1.0
 */
public class DiscreteHaarWaveletTransform implements GenericTransformer<double[], double[]> {

    @Override
    public double[] transform(double[] values) {
        double[] copy = new double[values.length];
        System.arraycopy(values, 0, copy, 0, values.length);

        double[] output = new double[copy.length];
        for (int length = copy.length >> 1; length > 0; length >>= 1) {
            for (int i = 0; i < length; ++i) {
                double sum = copy[i * 2] + copy[i * 2 + 1];
                double difference = copy[i * 2] - copy[i * 2 + 1];
                output[i] = sum;
                output[length + i] = difference;
            }

            if (length > 1) {
                //Swap arrays to do next iteration
                System.arraycopy(output, 0, copy, 0, length << 1);
            }
        }
        return output;
    }
}
