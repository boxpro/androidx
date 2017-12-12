/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.arch.background.workmanager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import android.arch.background.workmanager.worker.TestWorker;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PeriodicWorkTest extends WorkManagerTest {

    @Rule
    public ExpectedException mThrown = ExpectedException.none();

    @Test
    @SmallTest
    public void testBuild_backoffAndIdleMode_throwsIllegalArgumentException() {
        mThrown.expect(IllegalArgumentException.class);
        PeriodicWork.newBuilder(TestWorker.class, Constants.MIN_PERIODIC_INTERVAL_MILLIS)
                .withBackoffCriteria(Constants.BACKOFF_POLICY_EXPONENTIAL, 20000)
                .withConstraints(new Constraints.Builder()
                        .setRequiresDeviceIdle(true)
                        .build())
                .build();
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_onlyIntervalDuration_inRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS + 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(), is(testInterval));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(), is(testInterval));
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_onlyIntervalDuration_outOfRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS - 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(),
                is(Constants.MIN_PERIODIC_INTERVAL_MILLIS));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(),
                is(Constants.MIN_PERIODIC_INTERVAL_MILLIS));
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_intervalAndFlexDurations_inRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS + 123L;
        long testFlex = Constants.MIN_PERIODIC_FLEX_MILLIS + 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval, testFlex).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(), is(testInterval));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(), is(testFlex));
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_intervalAndFlexDurations_outOfRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS - 123L;
        long testFlex = Constants.MIN_PERIODIC_FLEX_MILLIS - 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval, testFlex).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(),
                is(Constants.MIN_PERIODIC_INTERVAL_MILLIS));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(),
                is(Constants.MIN_PERIODIC_FLEX_MILLIS));
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_intervalInRange_flexOutOfRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS + 123L;
        long testFlex = Constants.MIN_PERIODIC_FLEX_MILLIS - 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval, testFlex).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(), is(testInterval));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(),
                is(Constants.MIN_PERIODIC_FLEX_MILLIS));
    }

    @Test
    @SmallTest
    public void testBuild_setPeriodic_intervalOutOfRange_flexInRange() {
        long testInterval = Constants.MIN_PERIODIC_INTERVAL_MILLIS - 123L;
        long testFlex = Constants.MIN_PERIODIC_FLEX_MILLIS + 123L;
        PeriodicWork periodicWork =
                PeriodicWork.newBuilder(TestWorker.class, testInterval, testFlex).build();
        assertThat(getWorkSpec(periodicWork).getIntervalDuration(),
                is(Constants.MIN_PERIODIC_INTERVAL_MILLIS));
        assertThat(getWorkSpec(periodicWork).getFlexDuration(), is(testFlex));
    }
}
