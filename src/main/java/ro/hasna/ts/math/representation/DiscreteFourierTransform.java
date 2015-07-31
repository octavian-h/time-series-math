/**
 * Copyright (C) 2015 Octavian Hasna
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

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Implements the Discrete Fourier Transformation using {@link FastFourierTransformer}.
 *
 * @since 1.0
 */
public class DiscreteFourierTransform implements GenericTransformer<double[], double[]> {
    private final FastFourierTransformer fourierTransformer;

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
    public double[] transform(double[] values) {
        // pad the input array with zeros so as to have a length == 2^k
        int initialLength = values.length;
        int powerOfTwo = Integer.highestOneBit(initialLength);
        if (initialLength != powerOfTwo) {
            powerOfTwo = powerOfTwo << 1;
        }
        double[] copy = new double[powerOfTwo];
        System.arraycopy(values, 0, copy, 0, initialLength);

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
