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
package ro.hasna.ts.math.ml.distance;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.representation.IndexableSymbolicAggregateApproximation;
import ro.hasna.ts.math.type.SaxPair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Random;

/**
 * @since 1.0
 */
public class IndexableSaxEuclideanDistanceTest {
    private IndexableSaxEuclideanDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new IndexableSaxEuclideanDistance(new IndexableSymbolicAggregateApproximation(8, new int[]{2, 2, 2, 4, 2, 2, 2, 2}));
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withDistanceMeasure(distance)
                .testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        int n = 128;
        double a[] = new double[n];
        double b[] = new double[n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (i >= 48 && i < 64) {
                a[i] = 10 + i;
                b[i] = 300 + i + random.nextDouble();
            } else {
                a[i] = i;
                b[i] = 100 + i + random.nextDouble();
            }
        }

        double result = distance.compute(a, b);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testEquality2() throws Exception {
        SaxPair[] a = new SaxPair[4];
        a[0] = new SaxPair(0, 8);
        a[1] = new SaxPair(4, 8);
        a[2] = new SaxPair(5, 8);
        a[3] = new SaxPair(6, 8);

        SaxPair[] b = new SaxPair[4];
        b[0] = new SaxPair(0, 8);
        b[1] = new SaxPair(2, 4);
        b[2] = new SaxPair(5, 8);
        b[3] = new SaxPair(1, 2);

        double result = distance.compute(a, b, 128, Double.POSITIVE_INFINITY);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testOverflow() throws Exception {
        SaxPair[] a = new SaxPair[4];
        a[0] = new SaxPair(0, 8);
        a[1] = new SaxPair(4, 8);
        a[2] = new SaxPair(5, 8);
        a[3] = new SaxPair(6, 8);

        SaxPair[] b = new SaxPair[4];
        b[0] = new SaxPair(7, 8);
        b[1] = new SaxPair(2, 4);
        b[2] = new SaxPair(5, 8);
        b[3] = new SaxPair(1, 2);

        double result = distance.compute(a, b, 128, 3);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }
}