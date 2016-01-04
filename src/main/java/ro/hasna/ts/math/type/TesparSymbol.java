/**
 * Copyright (C) 2016-2015 Octavian Hasna
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
package ro.hasna.ts.math.type;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.util.Precision;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class TesparSymbol {
    private final int duration;
    private final int shape;
    private final double amplitude;

    /**
     * Create a TESPAR symbol.
     *
     * @param duration  the number of samples between two successive real zeros (one epoch)
     * @param shape     the number of local minimums (for a positive epoch) or
     *                  the number of local maximums (for a negative epoch)
     * @param amplitude the amplitude of the epoch
     */
    public TesparSymbol(int duration, int shape, double amplitude) {
        this.duration = duration;
        this.shape = shape;
        if (amplitude < 0) {
            throw new NotPositiveException(amplitude);
        }
        this.amplitude = amplitude;
    }

    public int getDuration() {
        return duration;
    }

    public int getShape() {
        return shape;
    }

    public double getAmplitude() {
        return amplitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TesparSymbol)) {
            return false;
        } else {
            TesparSymbol symbol = (TesparSymbol) o;
            return duration == symbol.getDuration() && shape == symbol.getShape() &&
                    Precision.equals(amplitude, symbol.getAmplitude(), TimeSeriesPrecision.EPSILON);
        }
    }

    @Override
    public String toString() {
        return "[" + duration + ", " + shape + ", " + amplitude + "]";
    }
}
