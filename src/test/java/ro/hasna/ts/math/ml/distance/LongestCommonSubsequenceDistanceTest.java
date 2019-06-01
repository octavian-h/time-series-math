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

import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Locale;
import java.util.Scanner;

/**
 * @since 1.0
 */
public class LongestCommonSubsequenceDistanceTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private LongestCommonSubsequenceDistance distance;

    @Before
    public void setUp() throws Exception {
        distance = new LongestCommonSubsequenceDistance(0.25, 0.05);
    }

    @After
    public void tearDown() throws Exception {
        distance = null;
    }

    @Test
    public void testConstructor1() throws Exception {
        thrown.expect(OutOfRangeException.class);
        thrown.expectMessage("2 out of [0, 1] range");

        new LongestCommonSubsequenceDistance(0.1, 2);
    }

    @Test
    public void testConstructor2() throws Exception {
        thrown.expect(OutOfRangeException.class);
        thrown.expectMessage("-0.5 out of [0, 1] range");

        new LongestCommonSubsequenceDistance(0.1, -0.5);
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
                .withCutOffValue(0.96875)
                .testOverflowSquare();
    }

    @Test
    public void testResult() throws Exception {
        double[] a = {2, 2, 3, 3, 4, 2, 3, 1, 3, 4, 3, 2, 3, 2, 3, 3, 1, 1, 3, 2};
        double[] b = {3, 4, 2, 3, 4, 4, 2, 3, 3, 1, 1, 4, 3, 2, 2, 3, 4, 4, 3, 1};

        double result = distance.compute(a, b);

        // 1 - |{2,3,4,2,3, 3,3,2,3,3}| / 20
        Assert.assertEquals(0.5, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResult2() throws Exception {
        double[] a = {2, 2, 3, 3, 4, 2, 3, 1, 3, 4, 3, 2, 3, 2, 3, 3, 1, 1, 3, 2};
        double[] b = {3, 4, 2, 3, 4, 4, 2, 3, 3, 1, 1, 4, 3, 2, 2, 3, 4, 4, 3, 1};

        LongestCommonSubsequenceDistance tmp = new LongestCommonSubsequenceDistance(0.25, 1);
        double result = tmp.compute(a, b);

        // 1 - |{2,3,4,2,3,1, 4,3,2,2,3,3,1}| / 20
        Assert.assertEquals(0.35, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResultSmall() throws Exception {
        double[] a = {3};
        double[] b = {2};

        double result = distance.compute(a, b);

        Assert.assertEquals(1, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testResultLarge() throws Exception {
        LongestCommonSubsequenceDistance lcss = new LongestCommonSubsequenceDistance(0.1, 0.05);
        int m = 128;
        ZNormalizer normalizer = new ZNormalizer();

        Scanner dataScanner = new Scanner(getClass().getResourceAsStream("data-100k.txt")).useLocale(Locale.ENGLISH);
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
            double d = lcss.compute(normalizedCopy, query, min);
//            duration += System.currentTimeMillis() - start;

            if (d < min) {
                min = d;
                posMin = n;
            }

            data = copy;
            n++;
        }

//        System.out.println("duration=" + duration);

        Assert.assertEquals(93728, posMin);
        Assert.assertEquals(0.5625, min, TimeSeriesPrecision.EPSILON);
    }
}