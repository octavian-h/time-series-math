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
package ro.hasna.ts.math.representation.mp;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ro.hasna.ts.math.type.MatrixProfile;

import java.util.function.Predicate;

public class SelfJoinAbstractMatrixProfileTransformerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_withSmallWindow() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("3 is smaller than the minimum (4)");

        new SelfJoinAbstractMatrixProfileTransformer(3) {
            private static final long serialVersionUID = 7691413957352355633L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        };
    }

    @Test
    public void constructor_withSmallExclusionZonePercentage() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (10)");

        new SelfJoinAbstractMatrixProfileTransformer(4, 0.1, false) {
            private static final long serialVersionUID = -932685352427204784L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        };
    }

    @Test
    public void transform_withSmallInput() {
        thrown.expect(NumberIsTooSmallException.class);
        thrown.expectMessage("4 is smaller than the minimum (5)");

        new SelfJoinAbstractMatrixProfileTransformer(4) {
            private static final long serialVersionUID = -2772945411191715308L;

            @Override
            protected MatrixProfile computeNormalizedMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }

            @Override
            protected MatrixProfile computeMatrixProfile(double[] input, int skip, Predicate<MatrixProfile> callback) {
                return null;
            }
        }.transform(new double[4]);
    }
}