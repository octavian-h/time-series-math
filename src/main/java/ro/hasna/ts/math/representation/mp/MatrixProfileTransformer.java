package ro.hasna.ts.math.representation.mp;

import ro.hasna.ts.math.representation.GenericTransformer;
import ro.hasna.ts.math.type.MatrixProfile;

public interface MatrixProfileTransformer extends GenericTransformer<double[], MatrixProfile> {
    MatrixProfile transform(double[] a, double[] b);
}
