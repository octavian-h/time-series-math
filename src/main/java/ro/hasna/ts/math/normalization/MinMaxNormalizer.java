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
package ro.hasna.ts.math.normalization;

import org.apache.commons.math3.exception.NumberIsTooLargeException;

/**
 * Implements the min-max normalization algorithm that maps the original data to a new interval.
 *
 * @since 0.12
 */
public class MinMaxNormalizer implements Normalizer {
    private static final long serialVersionUID = 2822371561555309746L;
    private final double min;
    private final double max;

    /**
     * Default constructor for normalizing data between 0 and 1.
     */
    public MinMaxNormalizer() {
        this(0.0, 1.0);
    }

    /**
     * Creates a new instance of this class with the given minimum and maximum values.
     *
     * @param min the new minimum value
     * @param max the new maximum value
     * @throws NumberIsTooLargeException if min is bigger than max
     */
    public MinMaxNormalizer(double min, double max) {
        if (min > max) {
            throw new NumberIsTooLargeException(min, max, true);
        }

        this.min = min;
        this.max = max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] normalize(double[] values) {
        double[] result = new double[values.length];

        double initialMin = values[0];
        double initialMax = values[0];
        for (int i = 1; i < values.length; i++) {
            if (initialMax < values[i]) {
                initialMax = values[i];
            }
            if (initialMin > values[i]) {
                initialMin = values[i];
            }
        }

        double aux = (max - min) / (initialMax - initialMin);
        for (int i = 0; i < values.length; i++) {
            result[i] = (values[i] - initialMin) * aux + min;
        }

        return result;
    }
}
