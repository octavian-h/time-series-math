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
package ro.hasna.ts.math.exception;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.Localizable;

/**
 * @since 1.0
 */
public class ArrayLengthIsTooSmallException extends NumberIsTooSmallException {
    /**
     * Construct the exception.
     *
     * @param wrong          Value that is smaller than the minimum.
     * @param min            Minimum.
     * @param boundIsAllowed Whether {@code min} is included in the allowed range.
     */
    public ArrayLengthIsTooSmallException(Number wrong, Number min, boolean boundIsAllowed) {
        super(wrong, min, boundIsAllowed);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific       Specific context pattern.
     * @param wrong          Value that is smaller than the minimum.
     * @param min            Minimum.
     * @param boundIsAllowed Whether {@code min} is included in the allowed range.
     */
    public ArrayLengthIsTooSmallException(Localizable specific, Number wrong, Number min, boolean boundIsAllowed) {
        super(specific, wrong, min, boundIsAllowed);
    }
}
