package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Quiz implements Parcelable {
    private String id;
    private String title;
    private String description;
    private String deadLineDate;
    private String deadLineTime;
    private String timeLimit;
    private HashMap<String, Object> timeStamp;
    private ArrayList<MultipleChoiceQuestion> questions;
    private ArrayList<User> attemptedBy;
    private ArrayList<User> attempting;
    private ArrayList<QuizScore> scores;

    public Quiz() {
    }

    protected Quiz(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        deadLineDate = in.readString();
        deadLineTime = in.readString();
        timeLimit = in.readString();
        questions = in.createTypedArrayList(MultipleChoiceQuestion.CREATOR);
        attemptedBy = in.createTypedArrayList(User.CREATOR);
        attempting = in.createTypedArrayList(User.CREATOR);
        scores = in.createTypedArrayList(QuizScore.CREATOR);
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public HashMap<String, Object> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(HashMap<String, Object> timeStamp) {
        this.timeStamp = timeStamp;
    }

    public ArrayList<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<MultipleChoiceQuestion> questions) {
        this.questions = questions;
    }

    public ArrayList<User> getAttemptedBy() {
        return attemptedBy;
    }

    public void setAttemptedBy(ArrayList<User> attemptedBy) {
        this.attemptedBy = attemptedBy;
    }

    public ArrayList<User> getAttempting() {
        return attempting;
    }

    public void setAttempting(ArrayList<User> attempting) {
        this.attempting = attempting;
    }

    public ArrayList<QuizScore> getScores() {
        return scores;
    }

    public void setScores(ArrayList<QuizScore> scores) {
        this.scores = scores;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadLineDate='" + deadLineDate + '\'' +
                ", deadLineTime='" + deadLineTime + '\'' +
                ", timeLimit='" + timeLimit + '\'' +
                ", timeStamp=" + timeStamp +
                ", questions=" + questions +
                ", attemptedBy=" + attemptedBy +
                ", attempting=" + attempting +
                ", scores=" + scores +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(deadLineDate);
        dest.writeString(deadLineTime);
        dest.writeString(timeLimit);
        dest.writeTypedList(questions);
        dest.writeTypedList(attemptedBy);
        dest.writeTypedList(attempting);
        dest.writeTypedList(scores);
    }
}



