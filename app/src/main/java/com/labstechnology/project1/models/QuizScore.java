package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.labstechnology.project1.Utils;

public class QuizScore implements Parcelable {
    private int id;
    private String uId;
    private int quizId;
    private double score;
    private double outOf;

    public QuizScore(String uId, int quizId, double score, double outOf) {
        this.id = Utils.getQuizId();
        this.uId = uId;
        this.quizId = quizId;
        this.score = score;
        this.outOf = outOf;
    }

    public QuizScore() {
    }

    protected QuizScore(Parcel in) {
        id = in.readInt();
        uId = in.readString();
        quizId = in.readInt();
        score = in.readDouble();
        outOf = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(uId);
        dest.writeInt(quizId);
        dest.writeDouble(score);
        dest.writeDouble(outOf);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<QuizScore> CREATOR = new Creator<QuizScore>() {
        @Override
        public QuizScore createFromParcel(Parcel in) {
            return new QuizScore(in);
        }

        @Override
        public QuizScore[] newArray(int size) {
            return new QuizScore[size];
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

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
        return "QuizScore{" +
                "id=" + id +
                ", uId='" + uId + '\'' +
                ", quizId=" + quizId +
                ", score=" + score +
                ", outOf=" + outOf +
                '}';
    }


}
