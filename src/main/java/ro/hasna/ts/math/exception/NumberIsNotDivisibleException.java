/*
 * Copyright 2015 Octavian Hasna
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

import org.apache.commons.math3.exception.MathIllegalNumberException;
import org.apache.commons.math3.exception.util.Localizable;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * Exception to be thrown when a number is not divisible with a given factor.
 *
 * @since 0.3
 */
public class NumberIsNotDivisibleException extends MathIllegalNumberException {
    private static final long serialVersionUID = -3573144648031073903L;
    /**
     * The factor for the number.
     */
    private final Integer factor;

    /**
     * Construct the exception.
     *
     * @param wrong  Value that is not divisible with the factor.
     * @param factor The factor.
     */
    public NumberIsNotDivisibleException(Number wrong,
                                         Integer factor) {
        this(LocalizableMessages.NUMBER_NOT_DIVISIBLE_WITH, wrong, factor);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Specific context pattern.
     * @param wrong    Value that is not divisible with the factor.
     * @param factor   The factor.
     */
    public NumberIsNotDivisibleException(Localizable specific,
                                         Number wrong,
                                         Integer factor) {
        super(specific, wrong, factor);
        this.factor = factor;
    }

    /**
     * @return the factor
     */
    public Integer getFactor() {
        return factor;
    }
}
