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

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.*;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

public class NormalDistributionDividerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private NormalDistributionDivider divider;

    @Before
    public void setUp() {
        divider = new NormalDistributionDivider();
    }

    @After
    public void tearDown() {
        divider = null;
    }

    @Test
    public void testGetBreakpointsWithException() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("1 is smaller than the minimum (2)");

        divider.getBreakpoints(1);
    }

    @Test
    public void testGetBreakpoints1() {
        double[] expected = {-0.4307272992954576, 0.4307272992954576};
        double[] v = divider.getBreakpoints(3);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetBreakpoints2() {
        double[] expected = {-0.6744897501960816, 0, 0.6744897501960816};
        double[] v = divider.getBreakpoints(4);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }
}