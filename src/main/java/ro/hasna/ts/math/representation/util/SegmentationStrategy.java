/**
 * Copyright (C) 2015 Octavian Hasna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
     * The sequence is divided in fractional partitions.
     * The value from one edge of a segment can contribute in a given fraction to both segments.
     */
    FRACTIONAL_PARTITION
}
