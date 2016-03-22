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
import ro.hasna.ts.math.representation.SymbolicAggregateApproximation;

/**
 * Calculates the L<sub>2</sub> (Euclidean) distance between two vectors using the SAX representation.
 *
 * @since 1.0
 */
public class SaxEuclideanDistance implements GenericDistanceMeasure<int[]> {
    private static final long serialVersionUID = -2580188567848020545L;
    private final SymbolicAggregateApproximation sax;

    public SaxEuclideanDistance(SymbolicAggregateApproximation sax) {
        this.sax = sax;
    }

    @Override
    public double compute(int[] a, int[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(int[] a, int[] b, double cutoff) {
        double[] breakpoints = sax.getBreakpoints();

        double sum = 0.0;
        int w = a.length;
        double transformedCutoff = cutoff * cutoff;

        for (int i = 0; i < w; i++) {
            int sa = a[i];
            int sb = b[i];
            if (FastMath.abs(sa - sb) > 1) {
                double aux;
                if (sa > sb) {
                    aux = breakpoints[sa - 1] - breakpoints[sb];
                } else {
                    aux = breakpoints[sb - 1] - breakpoints[sa];
                }
                sum += aux * aux;

                if (sum >= transformedCutoff) {
                    return Double.POSITIVE_INFINITY;
                }
            }
        }

        return FastMath.sqrt(sum);
    }
}
