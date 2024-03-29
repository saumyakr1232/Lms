package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.labstechnology.project1.Utils;

public class Event implements Parcelable {
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
    private boolean format24Hr;
    private int flag;

    public Event() {
    }

    public Event(String title, String description, int dayStart, int monthStart, int yearStart, int hourStart, int minuteStart, int dayEnd, int monthEnd, int yearEnd, int hourEnd, int minuteEnd, boolean format24Hr, int flag) {
        this.id = Utils.getEventId();
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
        this.format24Hr = format24Hr;
        this.flag = flag;
    }

    protected Event(Parcel in) {
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
        format24Hr = in.readByte() != 0;
        flag = in.readInt();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isFormat24Hr() {
        return format24Hr;
    }

    public void setFormat24Hr(boolean format24Hr) {
        this.format24Hr = format24Hr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Event{" +
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
                ", format24Hr=" + format24Hr +
                ", flag=" + flag +
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
        dest.writeByte((byte) (format24Hr ? 1 : 0));
        dest.writeInt(flag);
    }
}
