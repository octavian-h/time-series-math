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

import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.TransformType;

/**
 * Implements the Discrete Cosine Transformation using {@link FastCosineTransformer}.
 *
 * @since 1.0
 */
public class DiscreteCosineTransform implements GenericTransformer<double[], double[]> {
    private static final long serialVersionUID = -100136722639862251L;
    private final FastCosineTransformer cosineTransformer;

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
    public double[] transform(double[] values) {
        // pad the input array with zeros so as to have a length == 2^k + 1
        int initialLength = values.length;
        int powerOfTwo = Integer.highestOneBit(initialLength);
        int requiredLength = powerOfTwo + 1;
        if (initialLength != requiredLength && initialLength != powerOfTwo) {
            requiredLength = (powerOfTwo << 1) + 1;
        }

        double[] copy = new double[requiredLength];
        System.arraycopy(values, 0, copy, 0, initialLength);

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
