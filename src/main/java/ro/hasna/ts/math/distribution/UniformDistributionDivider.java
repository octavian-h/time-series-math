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
package ro.hasna.ts.math.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/**
 * Implements the equal areas of probability for Uniform Distribution.
 *
 * @since 1.0
 */
public class UniformDistributionDivider implements DistributionDivider {
    private static final long serialVersionUID = 3307519249593191140L;
    private final double lower;
    private final double upper;

    /**
     * Creates a new instance of this class.
     *
     * @param lower the minimum value from the distribution
     * @param upper the maximum value from the distribution
     * @throws NumberIsTooLargeException if lower is bigger than upper
     */
    public UniformDistributionDivider(double lower, double upper) {
        if (lower > upper) {
            throw new NumberIsTooLargeException(lower, upper, true);
        }

        this.lower = lower;
        this.upper = upper;
    }

    @Override
    public double[] getBreakpoints(int areas) {
        if (areas < 2) {
            throw new NumberIsTooSmallException(areas, 2, true);
        }

        int len = areas - 1;
        double[] result = new double[len];
        double intervalSize = (upper - lower) / areas;
        for (int i = 0; i < len; i++) {
            result[i] = lower + (i + 1) * intervalSize;
        }

        return result;
    }
}
