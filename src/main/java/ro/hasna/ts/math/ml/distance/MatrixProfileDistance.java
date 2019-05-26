package ro.hasna.ts.math.ml.distance;

import ro.hasna.ts.math.representation.mp.MatrixProfileTransformer;
import ro.hasna.ts.math.type.MatrixProfile;

/**
 * Calculates the k<sup>th</sup> smallest value in the matrix profile of AB and BA.
 */
public class MatrixProfileDistance implements GenericDistanceMeasure<double[]> {
    private final MatrixProfileTransformer matrixProfileTransformer;
    private final double k;

    public MatrixProfileDistance(MatrixProfileTransformer matrixProfileTransformer) {
        this(matrixProfileTransformer, 5.0);
    }

    public MatrixProfileDistance(MatrixProfileTransformer matrixProfileTransformer, double k) {
        this.matrixProfileTransformer = matrixProfileTransformer;
        this.k = k;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        MatrixProfile mpAB = matrixProfileTransformer.transform(a, b);
        double[] mpABProfile = mpAB.getProfile();
        MatrixProfile mpBA = matrixProfileTransformer.transform(b, a);
        double[] mpBAProfile = mpBA.getProfile();
        int n = mpABProfile.length + mpBAProfile.length;
        int position = (int) (k * n);
        double[] v = new double[n];
        System.arraycopy(mpABProfile, 0, v, 0, mpABProfile.length);
        System.arraycopy(mpBAProfile, 0, v, mpABProfile.length, mpBAProfile.length);
        return quickSelect(v, 0, n, position);
    }

    private double quickSelect(double[] v, int left, int right, int k) {
        //TODO
        return 0;
    }
}
