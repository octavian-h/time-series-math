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
package ro.hasna.ts.math.normalization;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Implements the ZNormalizer algorithm that use mean and standard deviation for data normalization.
 * This algorithm is also called Standard Score, Z-Values, Z-Scores, Normal Scores and Standardized Variables.
 *
 * @since 1.0
 */
public class ZNormalizer implements Normalizer {
    private final Mean mean;
    private final StandardDeviation standardDeviation;

    /**
     * Creates a new instance of this class.
     */
    public ZNormalizer() {
        this(new Mean(), new StandardDeviation(false));
    }

    /**
     * Creates a new instance of this class with the given mean and standard deviation algorithms.
     *
     * @param mean              the mean
     * @param standardDeviation the standard deviation
     */
    public ZNormalizer(final Mean mean, final StandardDeviation standardDeviation) {
        this.mean = mean;
        this.standardDeviation = standardDeviation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] normalize(double[] values) {
        double m = mean.evaluate(values);
        double sd = standardDeviation.evaluate(values, m);

        int length = values.length;
        double[] normalizedValues = new double[length];
        for (int i = 0; i < length; i++) {
            normalizedValues[i] = (values[i] - m) / sd;
        }
        return normalizedValues;
    }
}
