package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Quiz implements Parcelable {
    private int id;
    private String title;
    private String description;
    private HashMap<String, Object> timeStamp;
    private ArrayList<MultipleChoiceQuestion> questions;
    private ArrayList<User> attemptedBy;
    private HashMap<String, String> results;

    public Quiz() {
    }

    protected Quiz(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        questions = in.createTypedArrayList(MultipleChoiceQuestion.CREATOR);
        attemptedBy = in.createTypedArrayList(User.CREATOR);
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

    public HashMap<String, String> getResults() {
        return results;
    }

    public void setResults(HashMap<String, String> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", timeStamp=" + timeStamp +
                ", questions=" + questions +
                ", attemptedBy=" + attemptedBy +
                ", results=" + results +
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
        dest.writeTypedList(questions);
        dest.writeTypedList(attemptedBy);
    }
}



