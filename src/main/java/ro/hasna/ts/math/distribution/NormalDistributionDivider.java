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

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/**
 * Implements the equal areas of probability for Unit Normal Distribution.
 *
 * @since 1.0
 */
public class NormalDistributionDivider implements DistributionDivider {
    private static final long serialVersionUID = -909800668897655203L;

    @Override
    public double[] getBreakpoints(int areas) {
        if (areas < 2) {
            throw new NumberIsTooSmallException(areas, 2, true);
        }

        NormalDistribution normalDistribution = new NormalDistribution();
        int len = areas - 1;
        double[] result = new double[len];
        double searchArea = 1.0 / areas;
        for (int i = 0; i < len; i++) {
            result[i] = normalDistribution.inverseCumulativeProbability(searchArea * (i + 1));
        }

        return result;
    }
}
