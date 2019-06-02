package ro.hasna.ts.math.type;

import lombok.Data;

@Data
public class FullMatrixProfile {
    private final MatrixProfile leftMatrixProfile;
    private final MatrixProfile rightMatrixProfile;

    public FullMatrixProfile(MatrixProfile leftMatrixProfile, MatrixProfile rightMatrixProfile) {
        this.leftMatrixProfile = leftMatrixProfile;
        this.rightMatrixProfile = rightMatrixProfile;
    }

    public FullMatrixProfile(int leftSize, int rightSize) {
        this(new MatrixProfile(leftSize), new MatrixProfile(rightSize));
    }
}
