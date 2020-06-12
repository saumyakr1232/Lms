package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class QuizScore implements Parcelable {
    private String id;
    private String uId;
    private String quizId;
    private double score;
    private double outOf;
    private ArrayList<HashMap<String, MultipleChoiceQuestion>> result;

    public QuizScore() {
    }

    protected QuizScore(Parcel in) {
        id = in.readString();
        uId = in.readString();
        quizId = in.readString();
        score = in.readDouble();
        outOf = in.readDouble();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getQuizId() {
        return quizId;
    }

    public void setQuizId(String quizId) {
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

    public ArrayList<HashMap<String, MultipleChoiceQuestion>> getResult() {
        return result;
    }

    public void setResult(ArrayList<HashMap<String, MultipleChoiceQuestion>> result) {
        this.result = result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uId);
        dest.writeString(quizId);
        dest.writeDouble(score);
        dest.writeDouble(outOf);
    }

    @Override
    public String toString() {
        return "QuizScore{" +
                "id='" + id + '\'' +
                ", uId='" + uId + '\'' +
                ", quizId='" + quizId + '\'' +
                ", score=" + score +
                ", outOf=" + outOf +
                ", result=" + result +
                '}';
    }
}
