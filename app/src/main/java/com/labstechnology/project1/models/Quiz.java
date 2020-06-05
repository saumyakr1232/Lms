package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz implements Parcelable {
    private int id;
    private String title;
    private String description;
    private int dayStart;
    private int monthStart;
    private int yearStart;
    private int hourStart;
    private int minuteStart;
    private int dayEnd;
    private int monthEnd;
    private int yearEnd;
    private int hourEnd;
    private int minuteEnd;
    private ArrayList<MultipleChoiceQuestion> questions;
    private ArrayList<User> attemptedBy;

    public Quiz() {
    }

    public Quiz(String title, String description, int dayStart, int monthStart, int yearStart, int hourStart, int minuteStart, int dayEnd, int monthEnd, int yearEnd, int hourEnd, int minuteEnd, ArrayList<MultipleChoiceQuestion> questions) {
        this.title = title;
        this.description = description;
        this.dayStart = dayStart;
        this.monthStart = monthStart;
        this.yearStart = yearStart;
        this.hourStart = hourStart;
        this.minuteStart = minuteStart;
        this.dayEnd = dayEnd;
        this.monthEnd = monthEnd;
        this.yearEnd = yearEnd;
        this.hourEnd = hourEnd;
        this.minuteEnd = minuteEnd;
        this.questions = questions;
    }

    protected Quiz(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        dayStart = in.readInt();
        monthStart = in.readInt();
        yearStart = in.readInt();
        hourStart = in.readInt();
        minuteStart = in.readInt();
        dayEnd = in.readInt();
        monthEnd = in.readInt();
        yearEnd = in.readInt();
        hourEnd = in.readInt();
        minuteEnd = in.readInt();
        questions = in.createTypedArrayList(MultipleChoiceQuestion.CREATOR);
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

    public ArrayList<User> getAttemptedBy() {
        return attemptedBy;
    }

    public void setAttemptedBy(ArrayList<User> attemptedBy) {
        this.attemptedBy = attemptedBy;
    }


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

    public int getDayStart() {
        return dayStart;
    }

    public void setDayStart(int dayStart) {
        this.dayStart = dayStart;
    }

    public int getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(int monthStart) {
        this.monthStart = monthStart;
    }

    public int getYearStart() {
        return yearStart;
    }

    public void setYearStart(int yearStart) {
        this.yearStart = yearStart;
    }

    public int getHourStart() {
        return hourStart;
    }

    public void setHourStart(int hourStart) {
        this.hourStart = hourStart;
    }

    public int getMinuteStart() {
        return minuteStart;
    }

    public void setMinuteStart(int minuteStart) {
        this.minuteStart = minuteStart;
    }

    public int getDayEnd() {
        return dayEnd;
    }

    public void setDayEnd(int dayEnd) {
        this.dayEnd = dayEnd;
    }

    public int getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(int monthEnd) {
        this.monthEnd = monthEnd;
    }

    public int getYearEnd() {
        return yearEnd;
    }

    public void setYearEnd(int yearEnd) {
        this.yearEnd = yearEnd;
    }

    public int getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(int hourEnd) {
        this.hourEnd = hourEnd;
    }

    public int getMinuteEnd() {
        return minuteEnd;
    }

    public void setMinuteEnd(int minuteEnd) {
        this.minuteEnd = minuteEnd;
    }

    public ArrayList<MultipleChoiceQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<MultipleChoiceQuestion> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dayStart=" + dayStart +
                ", monthStart=" + monthStart +
                ", yearStart=" + yearStart +
                ", hourStart=" + hourStart +
                ", minuteStart=" + minuteStart +
                ", dayEnd=" + dayEnd +
                ", monthEnd=" + monthEnd +
                ", yearEnd=" + yearEnd +
                ", hourEnd=" + hourEnd +
                ", minuteEnd=" + minuteEnd +
                ", questions=" + questions +
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
        dest.writeInt(dayStart);
        dest.writeInt(monthStart);
        dest.writeInt(yearStart);
        dest.writeInt(hourStart);
        dest.writeInt(minuteStart);
        dest.writeInt(dayEnd);
        dest.writeInt(monthEnd);
        dest.writeInt(yearEnd);
        dest.writeInt(hourEnd);
        dest.writeInt(minuteEnd);
        dest.writeTypedList(questions);
    }


}



