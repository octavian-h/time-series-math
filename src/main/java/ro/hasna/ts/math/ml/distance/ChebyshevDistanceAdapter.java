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
package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.ml.distance.ChebyshevDistance;
import org.apache.commons.math3.util.FastMath;

/**
 * Adapter class for {@link ChebyshevDistance} that implements {@link GenericDistanceMeasure}.
 *
 * @since 1.0
 */
public class ChebyshevDistanceAdapter extends ChebyshevDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = 3282698054494240303L;

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        double max = 0;
        for (int i = 0; i < a.length; i++) {
            max = FastMath.max(max, FastMath.abs(a[i] - b[i]));
            if (max >= cutOffValue) {
                return Double.POSITIVE_INFINITY;
            }
        }
        return max;
    }

    @Override
    public double compute(double[] a, double[] b, int n, double cutOffValue) {
        return compute(a, b, cutOffValue);
    }
}
