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
package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.ManhattanDistance;
import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link ManhattanDistance} that implements {@link GenericDistanceMeasure}.
 *
 * @since 0.7
 */
public class ManhattanDistanceAdapter extends ManhattanDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = -8048423591199782828L;

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += FastMath.abs(a[i] - b[i]);
            if (sum >= cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return sum;
    }
}
