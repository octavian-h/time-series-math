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

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.representation.SymbolicAggregateApproximation;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Random;

/**
 * @since 1.0
 */
public class SaxEuclideanDistanceTest {
    private SaxEuclideanDistance distance;
    private SymbolicAggregateApproximation sax;

    @Before
    public void setUp() throws Exception {
        sax = new SymbolicAggregateApproximation(8, 16);
        distance = new SaxEuclideanDistance(sax);
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
        sax = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withDistanceMeasure(new DistanceMeasure() {
            private static final long serialVersionUID = -8270417993000076772L;

            @Override
            public double compute(double[] a, double[] b) throws DimensionMismatchException {
                return distance.compute(sax.transform(a), sax.transform(b));
            }
        }).testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        int n = 128;
        double[] a = new double[n];
        double[] b = new double[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            a[i] = i;
            b[i] = 100 + i + random.nextDouble();
        }

        double result = distance.compute(sax.transform(a), sax.transform(b));

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testOverflow() throws Exception {
        int[] a = {0, 4, 5, 6};
        int[] b = {7, 2, 5, 1};

        double result = distance.compute(a, b, 1);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }
}