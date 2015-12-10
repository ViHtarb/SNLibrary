package com.snl.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class SocialPerson implements Parcelable {

    public static final Parcelable.Creator<SocialPerson> CREATOR
            = new Parcelable.Creator<SocialPerson>() {
        public SocialPerson createFromParcel(Parcel in) {
            return new SocialPerson(in);
        }

        public SocialPerson[] newArray(int size) {
            return new SocialPerson[size];
        }
    };

    /*** Id of social person from chosen social network.*/
    protected String mId;
    /*** Name of social person from social network.*/
    protected String mName;
    /*** Profile URL of social person from social network.*/
    protected String mProfileURL;
    /*** Profile picture url of social person from social network.*/
    protected String mAvatarURL;
    /*** Email of social person from social network if exist.*/
    protected String mEmail;

    public SocialPerson() {

    }

    private SocialPerson(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mAvatarURL = in.readString();
        mProfileURL = in.readString();
        mEmail = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mAvatarURL);
        dest.writeString(mProfileURL);
        dest.writeString(mEmail);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SocialPerson that = (SocialPerson) o;

        return !(mAvatarURL != null ? !mAvatarURL.equals(that.mAvatarURL) : that.mAvatarURL != null) &&
                !(mEmail != null ? !mEmail.equals(that.mEmail) : that.mEmail != null) &&
                !(mId != null ? !mId.equals(that.mId) : that.mId != null) &&
                !(mName != null ? !mName.equals(that.mName) : that.mName != null) &&
                !(mProfileURL != null ? !mProfileURL.equals(that.mProfileURL) : that.mProfileURL != null);
    }

    @Override
    public int hashCode() {
        int result = mId != null ? mId.hashCode() : 0;
        result = 31 * result + (mName != null ? mName.hashCode() : 0);
        result = 31 * result + (mProfileURL != null ? mProfileURL.hashCode() : 0);
        result = 31 * result + (mEmail != null ? mEmail.hashCode() : 0);
        result = 31 * result + (mAvatarURL != null ? mAvatarURL.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SocialPerson{" +
                "id='" + mId + '\'' +
                ", name='" + mName + '\'' +
                ", avatarURL='" + mAvatarURL + '\'' +
                ", profileURL='" + mProfileURL + '\'' +
                ", email='" + mEmail + '\'' +
                '}';
    }

    public void setId(String id) {
        mId = id;
    }
    public String getId() {
        return mId;
    }

    public void setName(String name) {
        mName = name;
    }
    public String getName() {
        return mName;
    }

    public void setProfileURL(String profileURL) {
        mProfileURL = profileURL;
    }
    public String getProfileURL() {
        return mProfileURL;
    }

    public void setAvatarURL(String avatarURL) {
        mAvatarURL = avatarURL;
    }
    public String getAvatarURL() {
        return mAvatarURL;
    }

    public void setEmail(String email) {
        mEmail = email;
    }
    public String getEmail() {
        return mEmail;
    }
}