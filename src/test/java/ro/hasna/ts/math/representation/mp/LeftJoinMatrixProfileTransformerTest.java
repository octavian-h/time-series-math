package ro.hasna.ts.math.representation.mp;

import org.junit.Assert;
import org.junit.Test;
import ro.hasna.ts.math.type.MatrixProfile;
import ro.hasna.ts.math.util.TimeSeriesPrecision;

/**
 * @author Octavian
 * @since 01.06.2019
 */
public class LeftJoinMatrixProfileTransformerTest {

    @Test
    public void transform_withoutNormalization() throws Exception {
        LeftJoinMatrixProfileTransformer transformer = new LeftJoinMatrixProfileTransformer(4, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 30, 15};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = new double[]{2.0, 2121.0, 2895.0, 3170.0};
        int[] expectedIp = new int[]{0, 0, 0, 0};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() throws Exception {
        LeftJoinMatrixProfileTransformer transformer = new LeftJoinMatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 3, 5, 19};
        double[] v2 = {1, 2, 2, 5, 50, 30, 15};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = new double[]{0, 0, 0, 0, 0, 0, 0, 0};
        int[] expectedIp = new int[]{4, 4, 4, 4, 0, 0, 0, 0};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }


}