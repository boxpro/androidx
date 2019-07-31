/*
 * Copyright 2018 The Android Open Source Project
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

package androidx.media;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;
import static androidx.media.AudioAttributesCompat.INVALID_STREAM_TYPE;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;

import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.versionedparcelable.ParcelField;
import androidx.versionedparcelable.VersionedParcelize;

/** @hide */
@VersionedParcelize(jetifyAs = "android.support.v4.media.AudioAttributesImplApi21")
@RestrictTo(LIBRARY)
@RequiresApi(21)
public class AudioAttributesImplApi21 implements AudioAttributesImpl {
    private static final String TAG = "AudioAttributesCompat21";

    @ParcelField(1)
    AudioAttributes mAudioAttributes;

    @ParcelField(2)
    int mLegacyStreamType = INVALID_STREAM_TYPE;

    /**
     * Used for VersionedParcelable
     */
    AudioAttributesImplApi21() {
    }

    AudioAttributesImplApi21(AudioAttributes audioAttributes) {
        this(audioAttributes, INVALID_STREAM_TYPE);
    }

    AudioAttributesImplApi21(AudioAttributes audioAttributes, int explicitLegacyStream) {
        mAudioAttributes = audioAttributes;
        mLegacyStreamType = explicitLegacyStream;
    }

    @Override
    public Object getAudioAttributes() {
        return mAudioAttributes;
    }

    @Override
    @SuppressLint("NewApi")
    public int getVolumeControlStream() {
        // TODO: address the framework change ag/4995785.
        return AudioAttributesCompat.toVolumeStreamType(true, getFlags(), getUsage());
    }

    @Override
    public int getLegacyStreamType() {
        if (mLegacyStreamType != INVALID_STREAM_TYPE) {
            return mLegacyStreamType;
        }
        return AudioAttributesCompat.toVolumeStreamType(false, getFlags(), getUsage());
    }

    @Override
    public int getRawLegacyStreamType() {
        return mLegacyStreamType;
    }

    @Override
    public int getContentType() {
        return mAudioAttributes.getContentType();
    }

    @Override
    public @AudioAttributesCompat.AttributeUsage int getUsage() {
        return mAudioAttributes.getUsage();
    }

    @Override
    public int getFlags() {
        return mAudioAttributes.getFlags();
    }

    @Override
    public int hashCode() {
        return mAudioAttributes.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AudioAttributesImplApi21)) {
            return false;
        }
        final AudioAttributesImplApi21 that = (AudioAttributesImplApi21) o;
        return mAudioAttributes.equals(that.mAudioAttributes);
    }

    @Override
    public String toString() {
        return "AudioAttributesCompat: audioattributes=" + mAudioAttributes;
    }

    static class Builder implements AudioAttributesImpl.Builder {
        final AudioAttributes.Builder mFwkBuilder;

        Builder() {
            mFwkBuilder = new AudioAttributes.Builder();
        }

        Builder(Object aa) {
            mFwkBuilder = new AudioAttributes.Builder((AudioAttributes) aa);
        }

        @Override
        public AudioAttributesImpl build() {
            return new AudioAttributesImplApi21(mFwkBuilder.build());
        }

        @Override
        public Builder setUsage(int usage) {
            if (usage == AudioAttributes.USAGE_ASSISTANT) {
                // TODO: shouldn't we keep the origin usage?
                usage = AudioAttributes.USAGE_ASSISTANCE_NAVIGATION_GUIDANCE;
            }
            mFwkBuilder.setUsage(usage);
            return this;
        }

        @Override
        public Builder setContentType(int contentType) {
            mFwkBuilder.setContentType(contentType);
            return this;
        }

        @Override
        public Builder setFlags(int flags) {
            mFwkBuilder.setFlags(flags);
            return this;
        }

        @Override
        public Builder setLegacyStreamType(int streamType) {
            mFwkBuilder.setLegacyStreamType(streamType);
            return this;
        }
    }
}
