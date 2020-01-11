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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ro.hasna.ts.math.normalization.ZNormalizer;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

public class AdaptiveDistributionDividerTest {
    private AdaptiveDistributionDivider divider;

    @Before
    public void setUp() throws Exception {
        double[][] trainingSet = {
                {60.7, 96.05, 130.45, 151.3, 160.45, 162.55, 160.3, 138.8, 111.85, 79.6},
                {59.5, 93.85, 128.9, 150.9, 160.55, 162.6, 160.45, 140.1, 112.6, 81.25},
                {63.7, 99.4, 131.55, 150.9, 160.85, 163.8, 159.55, 135.2, 109.4, 78.65},
                {62.6, 96.7, 129.85, 150.35, 161.05, 163.75, 160.5, 137.15, 110.95, 80.6},
                {59.35, 94, 128.1, 150, 160.3, 163.7, 160.55, 139.15, 111, 83.05},
                {58.35, 92.1, 127.3, 149.4, 160, 162.8, 160.95, 139.9, 112.6, 83.5}
        };

        ZNormalizer normalizer = new ZNormalizer();
        for (int i = 0; i < trainingSet.length; i++) {
            trainingSet[i] = normalizer.normalize(trainingSet[i]);
        }

        divider = new AdaptiveDistributionDivider(trainingSet, TimeSeriesPrecision.EPSILON);
    }

    @After
    public void tearDown() throws Exception {
        divider = null;
    }

    @Test
    public void testGetBreakpoints1() throws Exception {
        double[] expected = {-0.6468634085322369, 0.5037245662070147};
        double[] v = divider.getBreakpoints(3);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }

    @Test
    public void testGetBreakpoints2() throws Exception {
        double[] expected = {-1.097189452816095, -0.18696880941804792, 0.6120418964169175};
        double[] v = divider.getBreakpoints(4);

        Assert.assertArrayEquals(expected, v, TimeSeriesPrecision.EPSILON);
    }
}