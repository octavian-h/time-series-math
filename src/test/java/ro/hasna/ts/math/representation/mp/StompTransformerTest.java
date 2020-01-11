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
package ro.hasna.ts.math.representation.mp;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.ml.distance.LongestCommonSubsequenceDistance;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

import java.util.Locale;
import java.util.Scanner;

/**
 * @since 0.17
 */
public class StompTransformerTest {

    @Test
    public void transform_withoutNormalization() {
        StompTransformer transformer = new StompTransformer(4, 0.25, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = {1.4142135623730951, 101.00495037373169, 125.93252161375949, 135.41787178950938, 84.63450832845902, 69.03622237637282, 1.4142135623730951, 14.177446878757825};
        int[] expectedIp = {6, 7, 1, 7, 5, 6, 0, 6};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() throws Exception {
        StompTransformer transformer = new StompTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = {0.8348847624659255, 0.28817656745352077, 1.4755693976274564, 2.8934001724310083, 1.1962169014456976, 1.1962169014456976, 0.3938867756711901, 0.28817656745352077};
        int[] expectedIp = {6, 7, 0, 4, 5, 4, 7, 1};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_large() {
        int m = 300;
        int window = 8;

        Scanner dataScanner = new Scanner(LongestCommonSubsequenceDistance.class.getResourceAsStream("data-100k.txt")).useLocale(Locale.ENGLISH);
        double[] data = new double[m];
        for (int i = 0; i < m && dataScanner.hasNextDouble(); i++) {
            data[i] = dataScanner.nextDouble();
        }

        StompTransformer stompTransformer = new StompTransformer(window);
        MatrixProfile mp1 = stompTransformer.transform(data);
        StampTransformer stampTransformer = new StampTransformer(window);
        MatrixProfile mp2 = stampTransformer.transform(data);
        ScrimpTransformer scrimpTransformer = new ScrimpTransformer(window);
        MatrixProfile mp3 = scrimpTransformer.transform(data);

        Assert.assertArrayEquals(mp1.getProfile(), mp2.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(mp1.getProfile(), mp3.getProfile(), TimeSeriesPrecision.EPSILON);
    }
}