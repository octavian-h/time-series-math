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

public class ComplexityInvariantDistanceTest {

    private ComplexityInvariantDistance distance;

    @Before
    public void setUp() {
        distance = new ComplexityInvariantDistance(new EuclideanDistanceAdapter());
    }

    @After
    public void tearDown() {
        distance = null;
    }

    @Test
    public void testConstructor1() {
        EuclideanDistanceAdapter ed = new EuclideanDistanceAdapter();
        ComplexityInvariantDistance cid = new ComplexityInvariantDistance(ed);

        Assert.assertEquals(ed, cid.getBaseDistance());
    }

    @Test
    public void testEquality() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .testEquality();
    }

    @Test
    public void testOverflow() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .withVectorLength(100)
                .withOffset(2)
                .withCutOffValue(20)
                .testOverflowAdditive();
    }

    @Test
    public void testResult() throws Exception {
        new DistanceTester().withGenericDistanceMeasure(distance)
                .withVectorLength(100)
                .withOffset(2)
                .withCutOffValue(21)
                .withExpectedResult(20)
                .testResultAdditive();
    }

    @Test
    public void testResultSmall() {
        double[] a = {2, 3, 4, 3, 2}; // CF = sqrt(1+1+1+1)=2
        double[] b = {0, 1, 2, 1, 0}; // CF = sqrt(1+1+1+1)=2
        double[] c = {2, 4, 2, 4, 2}; // CF = sqrt(4+4+4+4)=4

        double r1 = distance.compute(a, b); // ED = sqrt(20)
        double r2 = distance.compute(a, c); // ED = sqrt(6)

        Assert.assertEquals(20, r1 * r1, TimeSeriesPrecision.EPSILON);
        Assert.assertEquals(24, r2 * r2, TimeSeriesPrecision.EPSILON);
    }
}