package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Assignment implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String resourceLink;
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
    private boolean completed;
    private String score;
    private int flag;
    private int attemptsAllowed;


    public Assignment() {
    }

    public Assignment(String title, String description, String resourceLink, int dayStart, int monthStart, int yearStart, int hourStart, int minuteStart, int dayEnd, int monthEnd, int yearEnd, int hourEnd, int minuteEnd) {
        this.title = title;
        this.description = description;
        this.resourceLink = resourceLink;
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
    }

    protected Assignment(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        resourceLink = in.readString();
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
        completed = in.readByte() != 0;
        score = in.readString();
        flag = in.readInt();
        attemptsAllowed = in.readInt();
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

    public String getResourceLink() {
        return resourceLink;
    }

    public void setResourceLink(String resourceLink) {
        this.resourceLink = resourceLink;
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

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getAttemptsAllowed() {
        return attemptsAllowed;
    }

    public void setAttemptsAllowed(int attemptsAllowed) {
        this.attemptsAllowed = attemptsAllowed;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", resourceLink='" + resourceLink + '\'' +
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
                ", completed=" + completed +
                ", score='" + score + '\'' +
                ", flag=" + flag +
                ", attemptsAllowed=" + attemptsAllowed +
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
        dest.writeString(resourceLink);
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
        dest.writeByte((byte) (completed ? 1 : 0));
        dest.writeString(score);
        dest.writeInt(flag);
        dest.writeInt(attemptsAllowed);
    }
}