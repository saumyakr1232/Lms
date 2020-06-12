package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class UpcomingEvents implements Parcelable {
    public static final Creator<UpcomingEvents> CREATOR = new Creator<UpcomingEvents>() {
        @Override
        public UpcomingEvents createFromParcel(Parcel in) {
            return new UpcomingEvents(in);
        }

        @Override
        public UpcomingEvents[] newArray(int size) {
            return new UpcomingEvents[size];
        }
    };
    private String eventId;
    private String title;
    //    private long deadlineMills;
    private String type;

    public UpcomingEvents() {
    }

    public UpcomingEvents(String eventId, String title, String type) {
        this.eventId = eventId;
        this.title = title;
        this.type = type;
//        this.deadlineMills = deadlineMills;
    }

    protected UpcomingEvents(Parcel in) {
        eventId = in.readString();
        title = in.readString();
        type = in.readString();
//        deadlineMills = in.readLong();
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //    public long getDeadlineMills() {
//        return deadlineMills;
//    }
//
//    public void setDeadlineMills(long deadlineMills) {
//        this.deadlineMills = deadlineMills;
//    }
//
    @Override
    public String toString() {
        return "UpcomingEvents{" +
                "eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
//                ", deadlineMills=" + deadlineMills +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(title);
        dest.writeString(type);
//        dest.writeLong(deadlineMills);
    }
}
