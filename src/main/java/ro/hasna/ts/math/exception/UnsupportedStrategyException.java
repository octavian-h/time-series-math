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

import org.apache.commons.math3.exception.MathUnsupportedOperationException;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * Exception to be thrown when a strategy is invalid for a given algorithm.
 *
 * @since 1.0
 */
public class UnsupportedStrategyException extends MathUnsupportedOperationException {
    private static final long serialVersionUID = 5160643698191149943L;
    private final String strategy;
    private final String algorithm;

    /**
     * Default constructor.
     */
    public UnsupportedStrategyException(String strategy, String algorithm) {
        super(LocalizableMessages.UNSUPPORTED_STRATEGY, strategy, algorithm);
        this.strategy = strategy;
        this.algorithm = algorithm;
    }

    public String getStrategy() {
        return strategy;
    }

    public String getAlgorithm() {
        return algorithm;
    }
}
