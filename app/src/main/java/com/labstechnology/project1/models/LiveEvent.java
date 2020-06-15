package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class LiveEvent implements Parcelable {
    public static final Creator<LiveEvent> CREATOR = new Creator<LiveEvent>() {
        @Override
        public LiveEvent createFromParcel(Parcel in) {
            return new LiveEvent(in);
        }

        @Override
        public LiveEvent[] newArray(int size) {
            return new LiveEvent[size];
        }
    };
    private String id;
    private String title;
    private String description;
    private String url;
    private HashMap<String, Object> timestamp;

    public LiveEvent() {
    }

    public LiveEvent(String id, String title, String description, String url, HashMap<String, Object> timestamp) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.url = url;
        this.timestamp = timestamp;
    }

    protected LiveEvent(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();
        url = in.readString();
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HashMap<String, Object> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(HashMap<String, Object> timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "LiveEvent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", timestamp=" + timestamp +
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
        dest.writeString(url);
    }
}
