package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;


public class Assignment implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String resourceUrl;
    private String deadLineDate;
    private String deadLineTime;
    private HashMap<String, Object> timestamp;
    private ArrayList<User> attemptedBy;
    private ArrayList<AssignmentResponse> responses;

    public Assignment() {
    }

    protected Assignment(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        deadLineDate = in.readString();
        deadLineTime = in.readString();
        attemptedBy = in.createTypedArrayList(User.CREATOR);
        responses = in.createTypedArrayList(AssignmentResponse.CREATOR);
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadLineDate() {
        return deadLineDate;
    }

    public void setDeadLineDate(String deadLineDate) {
        this.deadLineDate = deadLineDate;
    }

    public String getDeadLineTime() {
        return deadLineTime;
    }

    public void setDeadLineTime(String deadLineTime) {
        this.deadLineTime = deadLineTime;
    }

    public HashMap<String, Object> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(HashMap<String, Object> timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<User> getAttemptedBy() {
        return attemptedBy;
    }

    public void setAttemptedBy(ArrayList<User> attemptedBy) {
        this.attemptedBy = attemptedBy;
    }

    public ArrayList<AssignmentResponse> getResponses() {
        return responses;
    }

    public void setResponses(ArrayList<AssignmentResponse> responses) {
        this.responses = responses;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadLineDate='" + deadLineDate + '\'' +
                ", deadLineTime='" + deadLineTime + '\'' +
                ", timestamp=" + timestamp +
                ", attemptedBy=" + attemptedBy +
                ", responses=" + responses +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(deadLineDate);
        dest.writeString(deadLineTime);
        dest.writeTypedList(attemptedBy);
        dest.writeTypedList(responses);
    }
}