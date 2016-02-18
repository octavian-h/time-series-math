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
package ro.hasna.ts.math.filter;

import org.apache.commons.math3.exception.OutOfRangeException;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * Implements the exponential moving average filter (exponential smoothing).
 *
 * @since 1.0
 */
public class ExponentialMovingAverageFilter implements Filter {
    private static final long serialVersionUID = -5033372522209156302L;
    private final double smoothingFactor;

    public ExponentialMovingAverageFilter(double smoothingFactor) {
        if (smoothingFactor <= 0 || smoothingFactor >= 1) {
            throw new OutOfRangeException(LocalizableMessages.OUT_OF_RANGE_BOTH, smoothingFactor, 0, 1);
        }

        this.smoothingFactor = smoothingFactor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] filter(double[] values) {
        int length = values.length;
        double[] result = new double[length];
        result[0] = values[0] * smoothingFactor;
        double k = 1 - smoothingFactor;
        for (int i = 1; i < length; i++) {
            result[i] = values[i] * smoothingFactor + result[i - 1] * k;
        }
        return result;
    }
}
