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
package ro.hasna.ts.math.util;

import org.apache.commons.math3.util.FastMath;

/**
 * Methods and constants used for comparisons.
 *
 * @see org.apache.commons.math3.util.Precision
 * @since 0.5
 */
public class TimeSeriesPrecision {
    public static final double EPSILON = FastMath.pow(2, -30);

    /**
     * Private constructor.
     */
    private TimeSeriesPrecision() {
    }
}
