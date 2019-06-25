package ro.hasna.ts.math.type;

import lombok.Data;
import org.apache.commons.math3.exception.DimensionMismatchException;

@Data
public class MatrixProfile implements Cloneable {
    private final double[] profile;
    private final int[] indexProfile;

    public MatrixProfile(double[] profile, int[] indexProfile) {
        if (profile.length != indexProfile.length) {
            throw new DimensionMismatchException(profile.length, indexProfile.length);
        }

        this.profile = profile;
        this.indexProfile = indexProfile;
    }

    public MatrixProfile(int size) {
        profile = new double[size];
        indexProfile = new int[size];
        for (int j = 0; j < size; j++) {
            profile[j] = Double.POSITIVE_INFINITY;
            indexProfile[j] = -1;
        }
    }

    @Override
    public MatrixProfile clone() {
        int size = profile.length;
        MatrixProfile copy = new MatrixProfile(size);
        for (int i = 0; i < size; i++) {
            copy.profile[i] = profile[i];
            copy.indexProfile[i] = indexProfile[i];
        }
        return copy;
    }
}
