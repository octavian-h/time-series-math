/**
 * Copyright (C) 2016-2015 Octavian Hasna
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

import org.apache.commons.math3.exception.util.Localizable;

/**
 * @since 1.0
 */
public class ArrayLengthIsNotDivisibleException extends NumberIsNotDivisibleException {
    private static final long serialVersionUID = 1652407890465175618L;

    /**
     * Construct the exception.
     *
     * @param wrong  Value that is not divisible with the factor.
     * @param factor The factor.
     */
    public ArrayLengthIsNotDivisibleException(Number wrong, Integer factor) {
        super(wrong, factor);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Specific context pattern.
     * @param wrong    Value that is not divisible with the factor.
     * @param factor   The factor.
     */
    public ArrayLengthIsNotDivisibleException(Localizable specific, Number wrong, Integer factor) {
        super(specific, wrong, factor);
    }
}
