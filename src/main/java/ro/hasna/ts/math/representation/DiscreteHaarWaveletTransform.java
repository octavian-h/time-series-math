/*
 * Copyright 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.hasna.ts.math.representation;

/**
 * Implements the Haar Wavelet Transformation.
 * <p>
 * Reference:
 * Wikipedia https://en.wikipedia.org/wiki/Discrete_wavelet_transform
 * </p>
 *
 * @since 0.8
 */
public class DiscreteHaarWaveletTransform implements GenericTransformer<double[], double[]> {
    private static final long serialVersionUID = -7596019215266231225L;

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
