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
import ro.hasna.ts.math.util.TimeSeriesPrecision;


public class RealSequenceEditDistanceTest {
    private RealSequenceEditDistance distance;

    @Before
    public void setUp() {
        distance = new RealSequenceEditDistance();
    }

    @After
    public void tearDown() {
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
    public void testResult() {
        double[] a = {3, 2, 5, 7, 4, 8, 10, 7};
        double[] b = {2, 5, 4, 7, 3, 10, 8, 6};

        double result = distance.compute(a, b);

        // 3-d, 4-i, 4-3-r, 8-d, 8-i, 7-6-r
        Assert.assertEquals(6, result, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testDifferentLengths() {
        double[] a = {1, 2, 3, 4};
        double[] b = {1, 100, 101, 2, 4};

        double result = distance.compute(a, b);

        // 100-i, 101-i, 3-d
        Assert.assertEquals(3, result, TimeSeriesPrecision.EPSILON);
    }
}