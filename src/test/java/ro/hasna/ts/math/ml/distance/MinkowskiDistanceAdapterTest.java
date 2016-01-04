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
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.ml.distance.util.DistanceTester;

/**
 * @since 1.0
 */
public abstract class MinkowskiDistanceAdapterTest {
    protected GenericDistanceMeasure<double[]> distance;
    protected int vectorLength = 100;
    protected double offset;
    protected double overflowCutOffValue;
    protected double normalCutOffValue;
    protected double expectedResult;
    private DistanceTester distanceTester;

    @Before
    public void setUp() throws Exception {
        distanceTester = new DistanceTester().withGenericDistanceMeasure(distance);
    }

    @After
    public void tearDown() throws Exception {
        distanceTester = null;
    }

    @Test
    public void testTriangleInequality() throws Exception {
        distanceTester.testTriangleInequality();
    }

    @Test
    public void testEquality() throws Exception {
        distanceTester.testEquality();
    }

    @Test
    public void testOverflow() throws Exception {
        distanceTester.withVectorLength(vectorLength)
                .withOffset(offset)
                .withCutOffValue(overflowCutOffValue)
                .testOverflowAdditive();
    }

    @Test
    public void testResult() throws Exception {
        distanceTester.withVectorLength(vectorLength)
                .withOffset(offset)
                .withCutOffValue(normalCutOffValue)
                .withExpectedResult(expectedResult)
                .testResultAdditive();
    }
}