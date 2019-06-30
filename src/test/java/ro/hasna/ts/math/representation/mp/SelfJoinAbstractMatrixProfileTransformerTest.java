package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;

/**
 * @since 0.17
 */
public class SelfJoinAbstractMatrixProfileTransformerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_withSmallWindow() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new SelfJoinAbstractMatrixProfileTransformer(3) {
            private static final long serialVersionUID = 7691413957352355633L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        };
    }

    @Test
    public void constructor_withSmallExclusionZonePercentage() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (10)");

        new SelfJoinAbstractMatrixProfileTransformer(4, 0.1, false) {
            private static final long serialVersionUID = -932685352427204784L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        };
    }

    @Test
    public void transform_withSmallInput() throws Exception {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (5)");

        new SelfJoinAbstractMatrixProfileTransformer(4) {
            private static final long serialVersionUID = -2772945411191715308L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        }.transform(new double[4]);
    }
}