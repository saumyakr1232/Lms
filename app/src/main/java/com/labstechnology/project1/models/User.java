package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String enrollmentNo;
    private String firstName;
    private String LastName;
    private String emailId;
    private String mobileNo;
    private String uId;
    private String dateOfBirth;
    private String gender;
    private String profileImage;

    public User() {
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    protected User(Parcel in) {
        enrollmentNo = in.readString();
        firstName = in.readString();
        LastName = in.readString();
        emailId = in.readString();
        mobileNo = in.readString();
        uId = in.readString();
        dateOfBirth = in.readString();
        gender = in.readString();
        profileImage = in.readString();
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public String toString() {
        return "User{" +
                "enrollmentNo='" + enrollmentNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", uId='" + uId + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(enrollmentNo);
        dest.writeString(firstName);
        dest.writeString(LastName);
        dest.writeString(emailId);
        dest.writeString(mobileNo);
        dest.writeString(uId);
        dest.writeString(dateOfBirth);
        dest.writeString(gender);
        dest.writeString(profileImage);
    }
}

