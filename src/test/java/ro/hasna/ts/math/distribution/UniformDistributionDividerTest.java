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
package ro.hasna.ts.math.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 1.0
 */
public class UniformDistributionDividerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private UniformDistributionDivider divider;

    @Before
    public void setUp() throws Exception {
        divider = new UniformDistributionDivider(-1, 1);
    }

    @After
    public void tearDown() throws Exception {
        divider = null;
    }

    @Test
    public void testConstructor() throws Exception {
        thrown.expect(NumberIsTooLargeException.class);
        thrown.expectMessage("4 is larger than the maximum (3)");

        new UniformDistributionDivider(4, 3);
    }

    @Test
    public void testGetBreakpointsWithException() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("1 is smaller than the minimum (2)");

        divider.getBreakpoints(1);
    }

    @Test
    public void testGetBreakpoints1() throws Exception {
        double[] expected = {-1.0 / 3, 1.0 / 3};
        double[] v = divider.getBreakpoints(3);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetBreakpoints2() throws Exception {
        double[] expected = {-1.0 / 2, 0, 1.0 / 2};
        double[] v = divider.getBreakpoints(4);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }
}