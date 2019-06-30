package ro.hasna.ts.math.representation.mp;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @since 0.17
 */
public class ScrimpTransformerTest {

    @Ignore("not implemented")
    @Test
    public void transform_withoutNormalization() {
        ScrimpTransformer transformer = new ScrimpTransformer(4, 0.25, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = {1.4142135623730951, 101.00495037373169, 125.93252161375949, 135.41787178950938, 84.63450832845902, 69.03622237637282, 1.4142135623730951, 14.177446878757825};
        int[] expectedIp = {6, 7, 1, 7, 5, 6, 0, 6};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() throws Exception {
        ScrimpTransformer transformer = new ScrimpTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = {0.8348847624659255, 0.28817656745352077, 1.4755693976274564, 2.8934001724310083, 1.1962169014456976, 1.1962169014456976, 0.3938867756711901, 0.28817656745352077};
        int[] expectedIp = {6, 7, 0, 4, 5, 4, 7, 1};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }
}