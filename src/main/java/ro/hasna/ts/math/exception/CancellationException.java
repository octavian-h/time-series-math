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

import org.apache.commons.math3.exception.MathIllegalStateException;
import ro.hasna.ts.math.exception.util.LocalizableMessages;

/**
 * Exception to be thrown when an async task is canceled.
 *
 * @since 0.17
 */
public class CancellationException extends MathIllegalStateException {
    private static final long serialVersionUID = 8488577710717564366L;

    public CancellationException() {
        super(LocalizableMessages.OPERATION_WAS_CANCELLED);
    }
}
