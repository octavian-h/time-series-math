package ro.hasna.ts.math.representation.util;

/**
 * Strategies use by segmentation algorithms.
 *
 * @since 1.0
 */
public enum SegmentationStrategy {
    /**
     * The length of the sequence must be divisible with the number of segments.
     * Otherwise it will throw a {@code NumberIsNotDivisibleException}
     */
    STRICT,
    /**
     * If the length of the sequence is not divisible with the number of segments,
     * then the remaining values are ignored.
     */
    IGNORE_REMAINING,
    /**
     * The sequence is padded with the mean of the values from the last segment
     * so as the length of the sequence to be divisible with the number of segments.
     */
    MEAN_PADDING,
    /**
     * The sequence is divided in fractional partitions.
     * The value from one edge of a segment can contribute in a given fraction to both segments.
     */
    FRACTIONAL_PARTITION
}
