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

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.type.FullMatrixProfile;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

public class MatrixProfileTransformerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_withSmallWindow() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new MatrixProfileTransformer(0);
    }

    @Test
    public void transform_withSmallInput() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new MatrixProfileTransformer(4).transform(new double[5], new double[3]);
    }

    @Test
    public void fullJoinTransform_withSmallInput() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new MatrixProfileTransformer(4).fullJoinTransform(new double[5], new double[3]);
    }

    @Test
    public void transform_withoutNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, 0, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = {1.4142135623730951, 46.05431575867782, 3.1622776601683795, 3.3166247903554};
        int[] expectedIp = {0, 0, 6, 7};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = {0.5257667760397134, 0.0970386144176177, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIp = {1, 1, 6, 7};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void fullJoinTransform_withoutNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, 0, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v2);
        double[] expectedMpLeft = {1.4142135623730951, 46.05431575867782, 3.1622776601683795, 3.3166247903554};
        int[] expectedIpLeft = {0, 0, 6, 7};
        double[] expectedMpRight = {1.4142135623730951, 70.01428425685718, 83.773504164503, 85.28188553262645, 120.21231218140677, 69.0724257573165, 3.1622776601683795, 3.3166247903554};
        int[] expectedIpRight = {0, 1, 2, 3, 3, 1, 2, 3};

        Assert.assertArrayEquals(expectedMpLeft, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpLeft, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMpRight, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpRight, transform.getRightMatrixProfile().getIndexProfile());
    }

    @Test
    public void fullJoinTransform_withNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v2);
        double[] expectedMpLeft = {0.5257667760397134, 0.0970386144176177, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIpLeft = {1, 1, 6, 7};
        double[] expectedMpRight = {0.9190116821894448, 0.0970386144176177, 0.25693055542614596, 0.7563820833584827, 3.0538959257478884, 2.3369758598299764, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIpRight = {0, 1, 2, 3, 3, 1, 2, 3};

        Assert.assertArrayEquals(expectedMpLeft, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpLeft, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMpRight, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpRight, transform.getRightMatrixProfile().getIndexProfile());
    }

    @Test
    public void fullJoinTransform_sameArray_withoutNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, 0.25, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v);
        double[] expectedMp = {1.4142135623730951, 101.00495037373169, 125.93252161375949, 135.41787178950938, 84.63450832845902, 69.03622237637282, 1.4142135623730951, 14.177446878757825};
        int[] expectedIp = {6, 7, 1, 7, 5, 6, 0, 6};

        Assert.assertArrayEquals(expectedMp, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMp, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getRightMatrixProfile().getIndexProfile());
    }

    @Test
    public void fullJoinTransform_sameArray_withNormalization() {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, 0.25, true);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v);
        double[] expectedMp = {0.8348847624659255, 0.28817656745352077, 1.4755693976274564, 2.8934001724310083, 1.1962169014456976, 1.1962169014456976, 0.3938867756711901, 0.28817656745352077};
        int[] expectedIp = {6, 7, 0, 4, 5, 4, 7, 1};

        Assert.assertArrayEquals(expectedMp, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMp, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getRightMatrixProfile().getIndexProfile());
    }
}