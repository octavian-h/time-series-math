package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

public class SelfJoinMatrixProfileTransformerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_withSmallWindow() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new SelfJoinMatrixProfileTransformer(3);
    }

    @Test
    public void constructor_withSmallExclusionZonePercentage() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (10)");

        new SelfJoinMatrixProfileTransformer(4, 0.1, false);
    }

    @Test
    public void transform_withSmallInput() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (5)");

        new SelfJoinMatrixProfileTransformer(4).transform(new double[4]);
    }

    @Test
    public void transform_withoutNormalization() throws Exception {
        SelfJoinMatrixProfileTransformer transformer = new SelfJoinMatrixProfileTransformer(4, 0.25, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = new double[]{1.4142135623730951, 101.00495037373169, 125.93252161375949, 135.41787178950938, 84.63450832845902, 69.03622237637282, 1.4142135623730951, 14.177446878757825};
        int[] expectedIp = new int[]{6, 7, 1, 7, 5, 6, 0, 6};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() throws Exception {
        SelfJoinMatrixProfileTransformer transformer = new SelfJoinMatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = new double[]{0.8348847624659255, 0.28817656745352077, 1.4755693976274564, 2.8934001724310083, 1.1962169014456976, 1.1962169014456976, 0.3938867756711901, 0.28817656745352077};
        int[] expectedIp = new int[]{6, 7, 0, 4, 5, 4, 7, 1};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }
}