/*
 * Copyright 2019 The Android Open Source Project
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

package androidx.work;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The information required when a {@link ListenableWorker} runs in the context of a foreground
 * service.
 */
public final class ForegroundInfo {

    private final int mNotificationType;
    private final Notification mNotification;

    /**
     * Creates an instance of {@link ForegroundInfo} with a {@link Notification}.
     * <p>
     * On API 29 and above, you should specify a {@code notificationType} by using the
     * {@link #ForegroundInfo(Notification, int)} constructor; otherwise, a default {@code
     * notificationType} of {@code 0} will be used.
     *
     * @param notification The {@link Notification} to show when the Worker is running in the
     *                     context of a foreground {@link android.app.Service}
     */
    public ForegroundInfo(@NonNull Notification notification) {
        this(notification, 0);
    }

    /**
     * Creates an instance of {@link ForegroundInfo} with a {@link Notification} and a
     * notification type.
     *
     * Fore more information look at {@code android.app.Service#startForeground(int,
     * Notification, int)}.
     *
     * @param notification     The {@link Notification}
     * @param notificationType The foreground {@link android.app.Service} type
     */
    public ForegroundInfo(@NonNull Notification notification, int notificationType) {
        mNotification = notification;
        mNotificationType = notificationType;
    }

    /**
     * @return The Foreground service notification type
     */
    public int getNotificationType() {
        return mNotificationType;
    }

    /**
     * @return The user visible {@link Notification}
     */
    @NonNull
    public Notification getNotification() {
        return mNotification;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ForegroundInfo that = (ForegroundInfo) other;

        if (mNotificationType != that.mNotificationType) return false;
        return mNotification != null ? mNotification.equals(that.mNotification)
                : that.mNotification == null;
    }

    @Override
    public int hashCode() {
        int result = mNotificationType;
        result = 31 * result + (mNotification != null ? mNotification.hashCode() : 0);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ForegroundInfo{");
        sb.append("mNotificationType=").append(mNotificationType);
        sb.append(", mNotification=").append(mNotification);
        sb.append('}');
        return sb.toString();
    }
}
