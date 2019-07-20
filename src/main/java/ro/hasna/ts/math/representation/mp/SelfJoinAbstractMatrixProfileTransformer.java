package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import ro.hasna.ts.math.exception.ArrayLengthIsTooSmallException;
import ro.hasna.ts.math.exception.CancellationException;
import ro.hasna.ts.math.representation.GenericTransformer;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;

public abstract class SelfJoinAbstractMatrixProfileTransformer extends AbstractMatrixProfileTransformer implements GenericTransformer<double[], MatrixProfile> {
    private static final long serialVersionUID = 4273395812927663256L;

    /**
     * Creates a new instance of this class with normalization enabled and an exclusion zone of 25%.
     *
     * @param window the length of the window
     * @throws NumberIsTooSmallException if window is lower than 4
     */
    public SelfJoinAbstractMatrixProfileTransformer(int window) {
        this(window, 0.25, true);
    }

    /**
     * @param window                  the length of the window
     * @param exclusionZonePercentage percentage of window that should be excluded at distance profile computing
     * @param useNormalization        flag to use Z-Normalization
     * @throws NumberIsTooSmallException if window or window * exclusionZonePercentage is lower than 1
     */
    public SelfJoinAbstractMatrixProfileTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
        super(window, exclusionZonePercentage, useNormalization);

        int skip = (int) (window * exclusionZonePercentage);
        if (skip < 1) {
            int minWindow = (int) Math.ceil(1 / exclusionZonePercentage);
            throw new NumberIsTooSmallException(window, minWindow, true);
        }
    }

    @Override
    public MatrixProfile transform(double[] input) {
        int len = input.length;
        int skip = (int) (window * exclusionZonePercentage);
        if (len < window + skip) {
            throw new ArrayLengthIsTooSmallException(len, window + skip, true);
        }

        if (useNormalization) {
            return computeNormalizedMatrixProfile(input, skip, null);
        }
        return computeMatrixProfile(input, skip, null);
    }

    public void transform(double[] input, Predicate<MatrixProfile> callback) {
        int len = input.length;
        int skip = (int) (window * exclusionZonePercentage);
        if (len < window + skip) {
            throw new ArrayLengthIsTooSmallException(len, window + skip, true);
        }

        if (useNormalization) {
            computeNormalizedMatrixProfile(input, skip, callback);
        } else {
            computeMatrixProfile(input, skip, callback);
        }
    }

    protected void updateMatrixProfileWithSqrt(MatrixProfile mp) {
        double[] profile = mp.getProfile();
        for (int i = 0; i < profile.length; i++) {
            profile[i] = FastMath.sqrt(profile[i]);
        }
    }

    protected void updateMatrixProfileFromDistanceProfile(double[] distanceProfile, int i, int skip, int n, MatrixProfile mp, Predicate<MatrixProfile> callback) {
        for (int j = i + skip; j < n; j++) {
            // update horizontal line from upper triangle
            if (mp.getProfile()[j] > distanceProfile[j]) {
                mp.getProfile()[j] = distanceProfile[j];
                mp.getIndexProfile()[j] = i;
            }
            // update vertical line i from matrix profile
            if (mp.getProfile()[i] > distanceProfile[j]) {
                mp.getProfile()[i] = distanceProfile[j];
                mp.getIndexProfile()[i] = j;
            }
        }

        executeCallback(callback, mp);
    }

    protected void executeCallback(Predicate<MatrixProfile> callback, MatrixProfile mp) {
        if (callback == null) {
            return;
        }

        MatrixProfile clone = mp.clone();
        updateMatrixProfileWithSqrt(clone);
        if (!callback.test(clone)) {
            throw new CancellationException();
        }
    }

    protected abstract MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback);

    protected abstract MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback);
}
