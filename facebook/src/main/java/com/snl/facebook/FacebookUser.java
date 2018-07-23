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

package com.snl.facebook;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.snl.core.SocialUser;

import java.io.Serializable;

/**
 * Facebook detailed {@link SocialUser} implementation
 */
public class FacebookUser implements SocialUser {

    private static final Creator<FacebookUser> CREATOR = new Creator<FacebookUser>() {
        public FacebookUser createFromParcel(Parcel in) {
            return new FacebookUser(in);
        }

        public FacebookUser[] newArray(int size) {
            return new FacebookUser[size];
        }
    };

    @SerializedName(value = "id", alternate = "uid")
    private String mId; // Id of social person from chosen social network.

    @SerializedName(value = "name", alternate = "text")
    private String mName; // Name of social person from social network.

    @SerializedName("link")
    private String mProfileURL; // Profile URL of social person from social network.

    @SerializedName("photo")
    private String mAvatarURL; // Avatar URL of social person from social network.

    @SerializedName("email")
    private String mEmail; // Email of social person from social network if exist.

    @SerializedName("first_name")
    private String mFirstName; // First mName of social person

    @SerializedName("middle_name")
    private String mMiddleName; // Middle name of social person

    @SerializedName("last_name")
    private String mLastName; // Last name of social person

    @SerializedName("gender")
    private String mGender; // Sex of social person

    @SerializedName("birthday")
    private String mBirthday; // birthday of social person in the format MM/DD/YYYY

    @SerializedName("verified")
    private String isVerified; // Check if user is verified

    @SerializedName("picture")
    private Avatar mAvatar; // avatar data from /me/invitable_friends graph request

    FacebookUser(Parcel in) { // TODO make it private when FacebookPerson will be removed
        mId = in.readString();
        mName = in.readString();
        mProfileURL = in.readString();
        mEmail = in.readString();
        mFirstName = in.readString();
        mMiddleName = in.readString();
        mLastName = in.readString();
        mGender = in.readString();
        mBirthday = in.readString();
        isVerified = in.readString();
        mAvatar = (Avatar) in.readSerializable();
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
        dest.writeString(mMiddleName);
        dest.writeString(mLastName);
        dest.writeString(mGender);
        dest.writeString(mBirthday);
        dest.writeString(isVerified);
        dest.writeSerializable(mAvatar);
    }

    @Override
    public String toString() {
        return "FacebookUser{" +
                "id='" + mId + '\'' +
                ", name='" + mName + '\'' +
                ", profileURL='" + mProfileURL + '\'' +
                ", avatarURL='" + getAvatarURL() + '\'' +
                ", email='" + mEmail + '\'' +
                ", firstName='" + mFirstName + '\'' +
                ", middleName='" + mMiddleName + '\'' +
                ", lastName='" + mLastName + '\'' +
                ", gender='" + mGender + '\'' +
                ", birthday='" + mBirthday + '\'' +
                ", verified='" + isVerified + '\'' +
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
        return mId != null ? "http://graph.facebook.com/" + mId + "/picture?type=large" : mAvatar != null ? mAvatar.getURL() : mAvatarURL;
    }

    @Override
    public String getEmail() {
        return mEmail;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public String getMiddleName() {
        return mMiddleName;
    }

    public String getLastName() {
        return mLastName;
    }

    public String getGender() {
        return mGender;
    }

    public String getBirthday() {
        return mBirthday;
    }

    public String isVerified() {
        return isVerified;
    }

    private static final class Avatar implements Serializable {

        @SerializedName("data")
        private Data mData;

        public String getURL() {
            return mData.mURL;
        }

        private static final class Data {

            @SerializedName("url")
            private String mURL;
        }
    }
}
