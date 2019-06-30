package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @since 0.17
 */
public abstract class AbstractMatrixProfileTransformer implements Serializable {
    private static final long serialVersionUID = -4758909899130005950L;

    protected final int window;
    protected final double exclusionZonePercentage;
    protected final boolean useNormalization;

    /**
     * Creates a new instance of this class with normalization enabled and an exclusion zone of 25%.
     *
     * @param window the length of the window
     * @throws NumberIsTooSmallException if window is lower than 4
     */
    public AbstractMatrixProfileTransformer(int window) {
        this(window, 0.25, true);
    }

    /**
     * @param window                  the length of the window
     * @param exclusionZonePercentage percentage of window that should be excluded at distance profile computing
     * @param useNormalization        flag to use Z-Normalization
     * @throws NumberIsTooSmallException if window is lower than 1
     */
    public AbstractMatrixProfileTransformer(int window, double exclusionZonePercentage, boolean useNormalization) {
        this.useNormalization = useNormalization;
        if (window < 1) {
            throw new NumberIsTooSmallException(window, 1, true);
        }

        this.window = window;
        this.exclusionZonePercentage = exclusionZonePercentage;
    }

    /**
     * When this is finished second will contain statistics for last sliding window
     */
    protected void computeFirstNormalizedDistanceProfile(double[] a, BothWaySummaryStatistics first, double[] b, BothWaySummaryStatistics second, int skip, int nb, double[] productSums, double[] distanceProfile) {
        if (window > Math.log(b.length)) {
            // O(b.length * log(b.length))
            computeFirstNormalizedDistanceProfileWithFft(a, first, b, second, skip, nb, productSums, distanceProfile);
        } else {
            // O(b.length * window)
            computeFirstNormalizedDistanceProfileWithProductSums(a, first, b, second, skip, nb, productSums, distanceProfile);
        }
    }

    private void computeFirstNormalizedDistanceProfileWithFft(double[] a, BothWaySummaryStatistics first, double[] b, BothWaySummaryStatistics second, int skip, int nb, double[] productSums, double[] distanceProfile) {
        Complex[] transform = computeConvolutionUsingFft(a, 0, b);
        for (int j = skip; j < nb; j++) {
            if (j > skip) {
                second.addValue(b[j + window - 1]);
                second.removeValue(b[j - 1]);
            }

            productSums[j] = transform[j + window - 1].abs();
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, second);
        }
    }

    protected Complex[] computeConvolutionUsingFft(double[] a, int indexA, double[] b) {
        FastFourierTransformer fft = new FastFourierTransformer(DftNormalization.STANDARD);
        int powerOfTwo = Integer.highestOneBit(b.length);
        double[] paddedB = b;
        if (b.length > powerOfTwo) {
            powerOfTwo = powerOfTwo << 1;
            paddedB = new double[powerOfTwo];
            System.arraycopy(b, 0, paddedB, 0, b.length);
        }
        Complex[] transformB = fft.transform(paddedB, TransformType.FORWARD);
        double[] reverseA = new double[transformB.length];
        for (int i = 0; i < window; i++) {
            reverseA[i] = a[indexA + window - 1 - i];
        }
        Complex[] transformA = fft.transform(reverseA, TransformType.FORWARD);
        for (int i = 0; i < transformB.length; i++) {
            transformA[i] = transformA[i].multiply(transformB[i]);
        }
        return fft.transform(transformA, TransformType.INVERSE);
    }

    private void computeFirstNormalizedDistanceProfileWithProductSums(double[] a, BothWaySummaryStatistics first, double[] b, BothWaySummaryStatistics second, int skip, int nb, double[] productSums, double[] distanceProfile) {
        for (int j = skip; j < nb; j++) {
            if (j > skip) {
                second.addValue(b[j + window - 1]);
                second.removeValue(b[j - 1]);
            }
            double productSum = 0;
            for (int k = 0; k < window; k++) {
                productSum += a[k] * b[k + j];
            }
            productSums[j] = productSum;
            distanceProfile[j] = computeNormalizedDistance(productSums[j], first, second);
        }
    }

    protected double computeNormalizedDistance(double productSum, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        return 2.0 * window * (1 - (productSum - window * first.getMean() * second.getMean()) / (window * first.getStandardDeviation() * second.getStandardDeviation()));
    }

    protected void computeFirstDistanceProfileWithProductSums(double[] a, double[] b, int skip, int nb, double[] distanceProfile) {
        for (int j = skip; j < nb; j++) {
            double distance = 0;
            for (int k = 0; k < window; k++) {
                distance += (a[k] - b[k + j]) * (a[k] - b[k + j]);
            }
            distanceProfile[j] = distance;
        }
    }

    protected boolean inExclusionZone(int indexA, int indexB, int skip) {
        return Math.abs(indexA - indexB) < skip;
    }

    protected int[] generateRandomIndices(int n) {
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        Random random = ThreadLocalRandom.current();
        for (int i = 0; i < n; i++) {
            int k = random.nextInt(n);
            int aux = indices[i];
            indices[i] = indices[k];
            indices[k] = aux;
        }

        return indices;
    }
}
