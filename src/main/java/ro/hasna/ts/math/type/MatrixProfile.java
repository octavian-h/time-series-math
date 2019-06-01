package ro.hasna.ts.math.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.math3.exception.DimensionMismatchException;

@Getter
@EqualsAndHashCode
@ToString
public class MatrixProfile {
    private double[] profile;
    private int[] indexProfile;

    public MatrixProfile(double[] profile, int[] indexProfile) {
        if (profile.length != indexProfile.length) {
            throw new DimensionMismatchException(profile.length, indexProfile.length);
        }

        this.profile = profile;
        this.indexProfile = indexProfile;
    }

    public MatrixProfile(int n) {
        profile = new double[n];
        indexProfile = new int[n];
        for (int j = 0; j < n; j++) {
            profile[j] = Double.POSITIVE_INFINITY;
            indexProfile[j] = -1;
        }
    }
}
