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
    public void constructor_withSmallWindow() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("0 is smaller than the minimum (1)");

        new MatrixProfileTransformer(0);
    }

    @Test
    public void transform_withSmallInput() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new MatrixProfileTransformer(4).transform(new double[5], new double[3]);
    }

    @Test
    public void fullJoinTransform_withSmallInput() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new MatrixProfileTransformer(4).fullJoinTransform(new double[5], new double[3]);
    }

    @Test
    public void transform_withoutNormalization() throws Exception {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = new double[]{1.4142135623730951, 46.05431575867782, 3.1622776601683795, 3.3166247903554};
        int[] expectedIp = new int[]{0, 0, 6, 7};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void transform_withNormalization() throws Exception {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        MatrixProfile transform = transformer.transform(v, v2);
        double[] expectedMp = new double[]{0.5257667760397134, 0.0970386144176177, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIp = new int[]{1, 1, 6, 7};

        Assert.assertArrayEquals(expectedMp, transform.getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIp, transform.getIndexProfile());
    }

    @Test
    public void fullJoinTransform_withoutNormalization() throws Exception {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4, false);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v2);
        double[] expectedMpLeft = new double[]{1.4142135623730951, 46.05431575867782, 3.1622776601683795, 3.3166247903554};
        int[] expectedIpLeft = new int[]{0, 0, 6, 7};
        double[] expectedMpRight = new double[]{1.4142135623730951, 70.01428425685718, 83.773504164503, 85.28188553262645, 120.21231218140677, 69.0724257573165, 3.1622776601683795, 3.3166247903554};
        int[] expectedIpRight = new int[]{0, 1, 2, 3, 3, 1, 2, 3};

        Assert.assertArrayEquals(expectedMpLeft, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpLeft, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMpRight, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpRight, transform.getRightMatrixProfile().getIndexProfile());
    }

    @Test
    public void fullJoinTransform_withNormalization() throws Exception {
        MatrixProfileTransformer transformer = new MatrixProfileTransformer(4);
        double[] v = {1, 2, 3, 4, 120, 71, 2, 2, 49, 25, 19};
        double[] v2 = {1, 2, 2, 5, 50, 25, 18};

        FullMatrixProfile transform = transformer.fullJoinTransform(v, v2);
        double[] expectedMpLeft = new double[]{0.5257667760397134, 0.0970386144176177, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIpLeft = new int[]{1, 1, 6, 7};
        double[] expectedMpRight = new double[]{0.9190116821894448, 0.0970386144176177, 0.25693055542614596, 0.7563820833584827, 3.0538959257478884, 2.3369758598299764, 0.12392968870054807, 0.16907943342270723};
        int[] expectedIpRight = new int[]{0, 1, 2, 3, 3, 1, 2, 3};

        Assert.assertArrayEquals(expectedMpLeft, transform.getLeftMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpLeft, transform.getLeftMatrixProfile().getIndexProfile());
        Assert.assertArrayEquals(expectedMpRight, transform.getRightMatrixProfile().getProfile(), TimeSeriesPrecision.EPSILON);
        Assert.assertArrayEquals(expectedIpRight, transform.getRightMatrixProfile().getIndexProfile());
    }
}