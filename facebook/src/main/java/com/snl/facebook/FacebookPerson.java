package com.snl.facebook;

import android.os.Parcel;
import android.os.Parcelable;

import com.snl.core.SocialPerson;

/**
 * Created by Viнt@rь on 28.11.2015
 */
public class FacebookPerson extends SocialPerson implements Parcelable {

    public static final Creator<FacebookPerson> CREATOR = new Creator<FacebookPerson>() {
        public FacebookPerson createFromParcel(Parcel in) {
            return new FacebookPerson(in);
        }

        public FacebookPerson[] newArray(int size) {
            return new FacebookPerson[size];
        }
    };

    /*** First mName of social person*/
    private String mFirstName;
    /*** Middle name of social person*/
    private String mMiddleName;
    /*** Last name of social person*/
    private String mLastName;
    /*** Sex of social person*/
    private String mGender;
    /*** mBirthday of social person in the format MM/DD/YYYY*/
    private String mBirthday;
    /*** mCity of social person from user*/
    private String mCity;
    /*** Check if user is mVerified*/
    private String mVerified;

    public FacebookPerson() {

    }

    private FacebookPerson(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
        mGender = in.readString();
        mBirthday = in.readString();
        mCity = in.readString();
        mVerified = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mGender);
        dest.writeString(mBirthday);
        dest.writeString(mCity);
        dest.writeString(mVerified);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof FacebookPerson)) return false;

        FacebookPerson that = (FacebookPerson) o;

        return !(mFirstName != null ? !mFirstName.equals(that.mFirstName) : that.mFirstName != null) &&
                !(mLastName != null ? !mLastName.equals(that.mLastName) : that.mLastName != null) &&
                !(mGender != null ? !mGender.equals(that.mGender) : that.mGender != null) &&
                !(mBirthday != null ? !mBirthday.equals(that.mBirthday) : that.mBirthday != null) &&
                !(mCity != null ? !mCity.equals(that.mCity) : that.mCity != null) &&
                !(mVerified != null ? !mVerified.equals(that.mVerified) : that.mVerified != null);
    }

    @Override
    public int hashCode() {
        int result = mFirstName != null ? mFirstName.hashCode() : 0;
        result = 31 * result + (mLastName != null ? mLastName.hashCode() : 0);
        result = 31 * result + (mGender != null ? mGender.hashCode() : 0);
        result = 31 * result + (mBirthday != null ? mBirthday.hashCode() : 0);
        result = 31 * result + (mCity != null ? mCity.hashCode() : 0);
        result = 31 * result + (mVerified != null ? mVerified.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "FacebookPerson{" +
                "id='" + mId + '\'' +
                ", name='" + mName + '\'' +
                ", avatarURL='" + mAvatarURL + '\'' +
                ", profileURL='" + mProfileURL + '\'' +
                ", email='" + mEmail + '\'' +
                ", firstName='" + mFirstName + '\'' +
                ", middleName='" + mMiddleName + '\'' +
                ", lastName='" + mLastName + '\'' +
                ", gender='" + mGender + '\'' +
                ", birthday='" + mBirthday + '\'' +
                ", city='" + mCity + '\'' +
                ", verified='" + mVerified + '\'' +
                '}';
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }
    public String getFirstName() {
        return mFirstName;
    }

    public void setMiddleName(String middleName) {
        mMiddleName = middleName;
    }
    public String getMiddleName() {
        return mMiddleName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }
    public String getLastName() {
        return mLastName;
    }

    public void setGender(String gender) {
        mGender = gender;
    }
    public String getGender() {
        return mGender;
    }

    public void setBirthday(String birthday) {
        mBirthday = birthday;
    }
    public String getBirthday() {
        return mBirthday;
    }

    public void setCity(String city) {
        mCity = city;
    }
    public String getCity() {
        return mCity;
    }

    public void setVerified(String verified) {
        mVerified = verified;
    }
    public String getVerified() {
        return mVerified;
    }
}
