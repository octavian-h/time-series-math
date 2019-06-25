package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import ro.hasna.ts.math.stat.BothWaySummaryStatistics;

/**
 * @author Octavian
 * @since 25.06.2019
 */
class MatrixProfileUtil {
    private MatrixProfileUtil() {
    }

    static Complex[] computeConvolutionUsingFft(double[] a, double[] b, int window) {
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
            reverseA[i] = a[window - 1 - i];
        }
        Complex[] transformA = fft.transform(reverseA, TransformType.FORWARD);
        for (int i = 0; i < transformB.length; i++) {
            transformA[i] = transformA[i].multiply(transformB[i]);
        }
        return fft.transform(transformA, TransformType.INVERSE);
    }

    static void computeFirstNormalizedDistanceProfileWithProductSums(double[] a, double[] b, int skip, int nb, int window, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
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
            distanceProfile[j] = computeNormalizedDistance(window, productSums[j], first, second);
        }
    }

    static double computeNormalizedDistance(int window, double productSum, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        return 2.0 * window * (1 - (productSum - window * first.getMean() * second.getMean()) / (window * first.getStandardDeviation() * second.getStandardDeviation()));
    }

    static void computeFirstNormalizedDistanceProfileWithFft(double[] a, double[] b, int skip, int nb, int window, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        Complex[] transform = computeConvolutionUsingFft(a, b, window);
        for (int j = skip; j < nb; j++) {
            if (j > skip) {
                second.addValue(b[j + window - 1]);
                second.removeValue(b[j - 1]);
            }

            productSums[j] = transform[j + window - 1].abs();
            distanceProfile[j] = computeNormalizedDistance(window, productSums[j], first, second);
        }
    }

    /**
     * When this is finished second will contain statistics for last sliding window
     */
    static void computeFirstNormalizedDistanceProfile(double[] a, double[] b, int skip, int nb, int window, double[] distanceProfile, double[] productSums, BothWaySummaryStatistics first, BothWaySummaryStatistics second) {
        if (window > Math.log(b.length)) {
            // O(b.length * log(b.length))
            computeFirstNormalizedDistanceProfileWithFft(a, b, skip, nb, window, distanceProfile, productSums, first, second);
        } else {
            // O(b.length * window)
            computeFirstNormalizedDistanceProfileWithProductSums(a, b, skip, nb, window, distanceProfile, productSums, first, second);
        }
    }

    static void computeFirstDistanceProfile(double[] a, double[] b, int skip, int nb, int window, double[] distanceProfile) {
        for (int j = skip; j < nb; j++) {
            double distance = 0;
            for (int k = 0; k < window; k++) {
                distance += (a[k] - b[k + j]) * (a[k] - b[k + j]);
            }
            distanceProfile[j] = distance;
        }
    }
}
