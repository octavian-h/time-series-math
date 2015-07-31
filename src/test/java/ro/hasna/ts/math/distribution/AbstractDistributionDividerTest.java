/**
 * Copyright (C) 2015 Octavian Hasna
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

/**
 * @since 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractDistributionDividerTest {

    @Mock
    private Map<Integer, double[]> breakpointsMap;

    @InjectMocks
    private AbstractDistributionDivider divider = new AbstractDistributionDivider() {
        @Override
        protected double[] computeBreakpoints(int areas) {
            return new double[0];
        }
    };

    @Test
    public void testGetBreakpointsWithCaching() throws Exception {
        divider.setCachingEnabled(true);
        double[] v = divider.getBreakpoints(1000);

        Mockito.verify(breakpointsMap).put(1000, v);
    }

    @Test
    public void testGetBreakpointsWithoutCaching() throws Exception {
        divider.setCachingEnabled(false);
        double[] v = divider.getBreakpoints(1000);

        Mockito.verify(breakpointsMap, Mockito.never()).put(1000, v);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSmallNumberOfAreas() throws Exception {
        divider.getBreakpoints(1);
    }

    @Test
    public void testSaveBreakpoints() throws Exception {
        double[] v = {1, 2, 3};
        divider.saveBreakpoints(v);

        Mockito.verify(breakpointsMap).put(4, v);
    }

    @Test
    public void testIsCachingEnabled1() throws Exception {
        divider = new AbstractDistributionDivider() {
            @Override
            protected double[] computeBreakpoints(int areas) {
                return new double[0];
            }
        };

        Assert.assertTrue(divider.isCachingEnabled());
    }

    @Test
    public void testIsCachingEnabled2() throws Exception {
        divider = new AbstractDistributionDivider(false) {
            @Override
            protected double[] computeBreakpoints(int areas) {
                return new double[0];
            }
        };

        Assert.assertFalse(divider.isCachingEnabled());
    }
}