package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class User implements Parcelable {

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
    private String enrollmentNo;
    private String firstName;
    private String LastName;
    private String emailId;
    private String mobileNo;
    private String uId;
    private String dateOfBirth;
    private String gender;
    private String profileImage;
    private HashMap<String, String> attemptedQuizzes;

    public User() {
    }

    private HashMap<String, String> attemptedAssignments;//assignment.id to response.id

    protected User(Parcel in) {
        uId = in.readString();
        enrollmentNo = in.readString();
        firstName = in.readString();
        LastName = in.readString();
        emailId = in.readString();
        mobileNo = in.readString();
        profileImage = in.readString();
        dateOfBirth = in.readString();
        gender = in.readString();
    }

    public static Creator<User> getCREATOR() {
        return CREATOR;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    public HashMap<String, String> getAttemptedQuizzes() {
        return attemptedQuizzes;
    }

    public void setAttemptedQuizzes(HashMap<String, String> attemptedQuizzes) {
        this.attemptedQuizzes = attemptedQuizzes;
    }

    public HashMap<String, String> getAttemptedAssignments() {
        return attemptedAssignments;
    }

    public void setAttemptedAssignments(HashMap<String, String> attemptedAssignments) {
        this.attemptedAssignments = attemptedAssignments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uId);
        dest.writeString(enrollmentNo);
        dest.writeString(firstName);
        dest.writeString(LastName);
        dest.writeString(emailId);
        dest.writeString(mobileNo);
        dest.writeString(profileImage);
        dest.writeString(dateOfBirth);
        dest.writeString(gender);
    }

    @Override
    public String toString() {
        return "User{" +
                "uId='" + uId + '\'' +
                ", enrollmentNo='" + enrollmentNo + '\'' +
                ", firstName='" + firstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", emailId='" + emailId + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", attemptedQuizzes=" + attemptedQuizzes +
                ", attemptedAssignments=" + attemptedAssignments +
                '}';
    }
}

