package ro.hasna.ts.math.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class MatrixProfile {
    private final double[] profile;
    private final int[] indexProfile;

    public MatrixProfile(double[] profile, int[] indexProfile) {
        this.profile = profile;
        this.indexProfile = indexProfile;
    }
}
