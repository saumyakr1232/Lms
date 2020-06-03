package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.labstechnology.project1.Utils;

public class AssignmentScore implements Parcelable {
    private int id;
    private String uId;
    private int assignmentId;
    private double score;
    private double outOf;

    public AssignmentScore(String uId, int assignmentId, double score, double outOf) {
        this.id = Utils.getAssignmentId();
        this.uId = uId;
        this.assignmentId = assignmentId;
        this.score = score;
        this.outOf = outOf;
    }

    public AssignmentScore() {
    }


    protected AssignmentScore(Parcel in) {
        id = in.readInt();
        uId = in.readString();
        assignmentId = in.readInt();
        score = in.readDouble();
        outOf = in.readDouble();
    }

    public static final Creator<AssignmentScore> CREATOR = new Creator<AssignmentScore>() {
        @Override
        public AssignmentScore createFromParcel(Parcel in) {
            return new AssignmentScore(in);
        }

        @Override
        public AssignmentScore[] newArray(int size) {
            return new AssignmentScore[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getOutOf() {
        return outOf;
    }

    public void setOutOf(double outOf) {
        this.outOf = outOf;
    }

    @Override
    public String toString() {
        return "AssignmentScore{" +
                "id=" + id +
                ", uId='" + uId + '\'' +
                ", assignmentId=" + assignmentId +
                ", score=" + score +
                ", outOf=" + outOf +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uId);
        dest.writeInt(assignmentId);
        dest.writeDouble(score);
        dest.writeDouble(outOf);
    }
}
