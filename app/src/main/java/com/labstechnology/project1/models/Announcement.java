package com.labstechnology.project1.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.labstechnology.project1.Utils;

public class Announcement implements Parcelable {
    private int id;
    private String title;
    private String description;
    private String ResourceLink;
    private int dayMade;
    private int monthMade;
    private int yearMade;
    private int minuteMade;
    private int hourMade;


    public Announcement() {
    }

    public Announcement(String title, String description, String resourceLink, int dayMade, int monthMade, int yearMade, int minuteMade, int hourMade) {
        this.id = Utils.getAnnouncementId();
        this.title = title;
        this.description = description;
        ResourceLink = resourceLink;
        this.dayMade = dayMade;
        this.monthMade = monthMade;
        this.yearMade = yearMade;
        this.minuteMade = minuteMade;
        this.hourMade = hourMade;
    }

    protected Announcement(Parcel in) {
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        ResourceLink = in.readString();
        dayMade = in.readInt();
        monthMade = in.readInt();
        yearMade = in.readInt();
        minuteMade = in.readInt();
        hourMade = in.readInt();
    }

    public static final Creator<Announcement> CREATOR = new Creator<Announcement>() {
        @Override
        public Announcement createFromParcel(Parcel in) {
            return new Announcement(in);
        }

        @Override
        public Announcement[] newArray(int size) {
            return new Announcement[size];
        }
    };

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
        return ResourceLink;
    }

    public void setResourceLink(String resourceLink) {
        ResourceLink = resourceLink;
    }

    public int getDayMade() {
        return dayMade;
    }

    public void setDayMade(int dayMade) {
        this.dayMade = dayMade;
    }

    public int getMonthMade() {
        return monthMade;
    }

    public void setMonthMade(int monthMade) {
        this.monthMade = monthMade;
    }

    public int getYearMade() {
        return yearMade;
    }

    public void setYearMade(int yearMade) {
        this.yearMade = yearMade;
    }

    public int getMinuteMade() {
        return minuteMade;
    }

    public void setMinuteMade(int minuteMade) {
        this.minuteMade = minuteMade;
    }

    public int getHourMade() {
        return hourMade;
    }

    public void setHourMade(int hourMade) {
        this.hourMade = hourMade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        dest.writeString(ResourceLink);
        dest.writeInt(dayMade);
        dest.writeInt(monthMade);
        dest.writeInt(yearMade);
        dest.writeInt(minuteMade);
        dest.writeInt(hourMade);
    }

    @Override
    public String toString() {
        return "Announcement{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ResourceLink='" + ResourceLink + '\'' +
                ", dayMade=" + dayMade +
                ", monthMade=" + monthMade +
                ", yearMade=" + yearMade +
                ", minuteMade=" + minuteMade +
                ", hourMade=" + hourMade +
                '}';
    }
}
