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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.representation.PiecewiseLinearAggregateApproximation;
import ro.hasna.ts.math.type.MeanSlopePair;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Random;


public class PlaaEuclideanDistanceTest {
    private PlaaEuclideanDistance distance;
    private PiecewiseLinearAggregateApproximation plaa;

    @Before
    public void setUp() throws Exception {
        plaa = new PiecewiseLinearAggregateApproximation(8);
        distance = new PlaaEuclideanDistance();
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
        plaa = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance, plaa)
                .testTriangleInequality();
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

        ZNormalizer normalizer = new ZNormalizer();
        double result = distance.compute(plaa.transform(normalizer.normalize(a)), plaa.transform(normalizer.normalize(b)));

        Assert.assertEquals(0, result, 0.1);
    }

    @Test
    public void testOverflow() throws Exception {
        MeanSlopePair[] a = new MeanSlopePair[4];
        a[0] = new MeanSlopePair(0, 8);
        a[1] = new MeanSlopePair(4, 8);
        a[2] = new MeanSlopePair(5, 8);
        a[3] = new MeanSlopePair(6, 8);

        MeanSlopePair[] b = new MeanSlopePair[4];
        b[0] = new MeanSlopePair(7, 8);
        b[1] = new MeanSlopePair(2, 4);
        b[2] = new MeanSlopePair(5, 8);
        b[3] = new MeanSlopePair(1, 2);

        double result = distance.compute(a, b, 3);

        Assert.assertEquals(Double.POSITIVE_INFINITY, result, TimeSeriesPrecision.EPSILON);
    }
}