package ro.hasna.ts.math.ml.distance;

import org.apache.commons.math3.util.Pair;
import ro.hasna.ts.math.representation.mp.LeftJoinMatrixProfileTransformer;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Calculates the k<sup>th</sup> smallest value in the full join matrix profile (Mab and Mba).
 * <p>
 * Reference:
 * Gharghabi S., Imani S., Bagnall A., Darvishzadeh A., Keogh E. (2018)
 * <i>An Ultra-Fast Time Series Distance Measure to allow Data Mining in more Complex Real-World Deployments</i>
 * </p>
 */
public class MatrixProfileDistance implements GenericDistanceMeasure<double[]> {
    private static final long serialVersionUID = -2290780320746907899L;
    private final LeftJoinMatrixProfileTransformer matrixProfileTransformer;
    private final double k;

    public MatrixProfileDistance(LeftJoinMatrixProfileTransformer matrixProfileTransformer) {
        this(matrixProfileTransformer, 5.0);
    }

    public MatrixProfileDistance(LeftJoinMatrixProfileTransformer matrixProfileTransformer, double k) {
        this.matrixProfileTransformer = matrixProfileTransformer;
        this.k = k;
    }

    @Override
    public double compute(double[] a, double[] b) {
        return compute(a, b, Double.POSITIVE_INFINITY);
    }

    @Override
    public double compute(double[] a, double[] b, double cutOffValue) {
        //TODO use a full join instead of two left-joins
        PriorityQueue<Double> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        updateMaxHeap(a, b, maxHeap);
        updateMaxHeap(b, a, maxHeap);
        return maxHeap.peek();
    }

    private void updateMaxHeap(double[] a, double[] b, PriorityQueue<Double> maxHeap) {
        MatrixProfile mp = matrixProfileTransformer.transform(new Pair<>(a, b));
        double[] profile = mp.getProfile();
        for (double v : profile) {
            if (maxHeap.size() < k) {
                maxHeap.add(v);
            } else if (v < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.add(v);
            }
        }
    }
}
