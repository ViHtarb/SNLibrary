package com.snl.facebook;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;
import com.snl.core.SocialPerson;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class FacebookPerson implements SocialPerson {

    public static final Creator<FacebookPerson> CREATOR = new Creator<FacebookPerson>() {
        public FacebookPerson createFromParcel(Parcel in) {
            return new FacebookPerson(in);
        }

        public FacebookPerson[] newArray(int size) {
            return new FacebookPerson[size];
        }
    };

    @SerializedName("id")
    private String mId; // Id of social person from chosen social network.

    @SerializedName("name")
    private String mName; // Name of social person from social network.

    @SerializedName("link")
    private String mProfileURL; // Profile URL of social person from social network.

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

    private FacebookPerson(Parcel in) {
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
    }

    @Override
    public String toString() {
        return "FacebookPerson{" +
                "id='" + mId + '\'' +
                ", name='" + mName + '\'' +
                ", avatarURL='" + getAvatarURL() + '\'' +
                ", profileURL='" + mProfileURL + '\'' +
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
        return "http://graph.facebook.com/" + mId + "/picture?type=large";
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
}
