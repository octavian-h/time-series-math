package ro.hasna.ts.math.representation.mp;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @author ohasna
 * @since 22.05.2019
 */
public class SelfJoinMatrixProfileTransformerTest {

    @Test
    public void transform_withoutNormalization() throws Exception {
        SelfJoinMatrixProfileTransformer transformer = new SelfJoinMatrixProfileTransformer(4, 0.25, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};

        MatrixProfile transform = transformer.transform(v);
        double[] expectedMp = new double[]{2.0, 10202.0, 15859.0, 18338.0, 7163.0, 4766.0, 2.0, 201.0};
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