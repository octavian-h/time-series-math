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

import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.type.MeanSlopePair;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two vectors using the PLAA representation.
 *
 * @since 0.7
 */
public class PlaaEuclideanDistance implements GenericDistanceMeasure<MeanSlopePair[]> {
    private static final long serialVersionUID = -2987558975696360228L;

    @Override
    public double compute(MeanSlopePair[] a, MeanSlopePair[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(MeanSlopePair[] a, MeanSlopePair[] b, double cutoff) {
        double sum1 = 0.0;
        double sum2 = 0.0;
        int w = a.length;
        double k = 1.0 / w;
        double transformedCutoff = cutoff * cutoff;

        for (int i = 0; i < w; i++) {
            double aux = a[i].getMean() - b[i].getMean();
            sum1 += aux * aux;

            aux = a[i].getSlope() - b[i].getSlope();
            sum2 += aux * aux;

            double currentDistance = sum1 * k + sum2 * (k * k - 1) / 18;
            if (currentDistance >= transformedCutoff) {
                return Double.POSITIVE_INFINITY;
            }
        }

        sum1 = sum1 * k;
        sum2 = sum2 * (k * k - 1) / 18;

        return FastMath.sqrt(sum1 + sum2);
    }
}
