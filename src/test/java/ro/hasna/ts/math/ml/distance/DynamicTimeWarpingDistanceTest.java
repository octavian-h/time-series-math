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
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Locale;
import java.util.Scanner;

/**
 * @since 1.0
 */
public class DynamicTimeWarpingDistanceTest {
    private DynamicTimeWarpingDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new DynamicTimeWarpingDistance();
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testEquality();
    }

    @Test
    public void testOverflow() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .withVectorLength(128)
                .withCutOffValue(2)
                .testOverflowSquare();
    }

    @Test
    public void testResult() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .withVectorLength(128)
                .withCutOffValue(3)
                .withExpectedResult(2.0840142540417594)
                .testResultSquare();
    }

    @Test
    public void testResultSmall() throws Exception {
        double a[] = {3,2,1};
        double b[] = {2,1,0};

        double result = distance.compute(a, b);

        Assert.assertEquals(0, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult2() throws Exception {
        DynamicTimeWarpingDistance dtw = new DynamicTimeWarpingDistance(0.05, null);
        int m = 128;
        ZNormalizer normalizer = new ZNormalizer();

        Scanner dataScanner = new Scanner(getClass().getResourceAsStream("data-1m.txt")).useLocale(Locale.ENGLISH);
        double[] data = new double[m];
        double[] copy = new double[m];
        for (int i = 0; i < m && dataScanner.hasNextDouble(); i++) {
            data[i] = dataScanner.nextDouble();
        }

        Scanner queryScanner = new Scanner(getClass().getResourceAsStream("query-128.txt")).useLocale(Locale.ENGLISH);
        double[] query = new double[m];
        for (int i = 0; i < m && queryScanner.hasNextDouble(); i++) {
            query[i] = queryScanner.nextDouble();
        }
        query = normalizer.normalize(query);

        double min = Double.POSITIVE_INFINITY;
        int posMin = 0;
        int n = 0;
//        long duration = 0;
        while (dataScanner.hasNextDouble()) {
            System.arraycopy(data, 1, copy, 0, m - 1);
            copy[m - 1] = dataScanner.nextDouble();
            double[] normalizedCopy = normalizer.normalize(copy);

//            long start = System.currentTimeMillis();
            double d = dtw.compute(normalizedCopy, query, min);
//            duration += System.currentTimeMillis() - start;

            if (d < min) {
                min = d;
                posMin = n;
            }

            data = copy;
            n++;
        }

//        System.out.println("duration=" + duration);

        Assert.assertEquals(756561, posMin);
        Assert.assertEquals(3.775620905705185, min, TimeSeriesPrecision.EPSILON);
    }
}