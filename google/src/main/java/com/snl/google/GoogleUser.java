/*
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2018. Viнt@rь
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.snl.google;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.snl.core.SocialUser;

/**
 * Google {@link SocialUser} implementation
 */
public class GoogleUser implements SocialUser {

    private static final Creator<GoogleUser> CREATOR = new Creator<GoogleUser>() {
        public GoogleUser createFromParcel(Parcel in) {
            return new GoogleUser(in);
        }

        public GoogleUser[] newArray(int size) {
            return new GoogleUser[size];
        }
    };

    private String mId; // Id of social person from chosen social network.
    private String mName; // Name of social person from social network.
    private String mProfileURL; // Profile URL of social person from social network.
    private String mAvatarURL; // Avatar URL of social person from social network.
    private String mEmail; // Email of social person from social network if exist.
    private String mFirstName; // First mName of social person
    private String mLastName; // Last name of social person

    GoogleUser(@NonNull GoogleSignInAccount account) {
        mId = account.getId();
        mName = account.getDisplayName();
        mProfileURL = "https://plus.google.com/u/0/" + account.getId();
        if (account.getPhotoUrl() != null) {
            mAvatarURL = account.getPhotoUrl().toString();
        }
        mEmail = account.getEmail();
        mFirstName = account.getGivenName();
        mLastName = account.getFamilyName();
    }

    private GoogleUser(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mProfileURL = in.readString();
        mEmail = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mProfileURL);
        dest.writeString(mEmail);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
    }

    @Override
    public String toString() {
        return "GoogleUser{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", profileURL='" + getProfileURL() + '\'' +
                ", avatarURL='" + getAvatarURL() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                '}';
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getProfileURL() {
        return mProfileURL;
    }

    @Override
    public String getAvatarURL() {
        return mAvatarURL;
    }

    @Override
    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getLastName() {
        return mLastName;
    }

}