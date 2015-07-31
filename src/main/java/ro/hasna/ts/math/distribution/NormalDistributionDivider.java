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
import org.apache.commons.math3.exception.MathInternalError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Implements the equal areas of probability for Unit Normal Distribution.
 *
 * @since 1.0
 */
public class NormalDistributionDivider extends AbstractDistributionDivider {
    public static final int PRE_COMPUTED_BREAKPOINTS = 256;
    private static final String RESOURCE_NAME = "normal-distribution-breakpoints.csv";

    /**
     * Creates a new instance of this class with caching enable.
     */
    public NormalDistributionDivider() {
        this(true);
    }

    /**
     * Creates a new instance of this class
     *
     * @param cachingEnabled flag to enable or not the caching
     */
    public NormalDistributionDivider(boolean cachingEnabled) {
        super(cachingEnabled, PRE_COMPUTED_BREAKPOINTS);
        try {
            readBreakpoints();
        } catch (IOException e) {
            throw new MathInternalError(e);
        }
    }

    @Override
    protected double[] computeBreakpoints(int areas) {
        NormalDistribution normalDistribution = new NormalDistribution();
        int len = areas - 1;
        double[] result = new double[len];
        double searchArea = 1.0 / areas;
        for (int i = 0; i < len; i++) {
            result[i] = normalDistribution.inverseCumulativeProbability(searchArea * (i + 1));
        }

        return result;
    }

    // Utility functions
    private void readBreakpoints() throws IOException {
        InputStream stream = getClass().getResourceAsStream(RESOURCE_NAME);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, Charset.forName("utf-8")))) {
            String read = br.readLine();

            int n = 1;
            while (read != null) {
                String[] v = read.split(";");
                double[] list = new double[n];
                for (int i = 0; i < v.length; i++) {
                    list[i] = Double.parseDouble(v[i]);
                }
                saveBreakpoints(list);

                read = br.readLine();
                n++;
            }
        }
    }
}
