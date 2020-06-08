package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AssignmentResponse implements Parcelable {
    private String id;
    private String uId;
    private String assignmentId;
    private String documentUri;
    public static final Creator<AssignmentResponse> CREATOR = new Creator<AssignmentResponse>() {
        @Override
        public AssignmentResponse createFromParcel(Parcel in) {
            return new AssignmentResponse(in);
        }

        @Override
        public AssignmentResponse[] newArray(int size) {
            return new AssignmentResponse[size];
        }
    };

    public AssignmentResponse() {
    }

    private long timestamp;

    public AssignmentResponse(String id, String uId, String assignmentId, String documentUri, long timestamp) {
        this.id = id;
        this.uId = uId;
        this.assignmentId = assignmentId;
        this.documentUri = documentUri;
        this.timestamp = timestamp;
    }

    protected AssignmentResponse(Parcel in) {
        id = in.readString();
        uId = in.readString();
        assignmentId = in.readString();
        documentUri = in.readString();
        timestamp = in.readLong();
    }

    public static Creator<AssignmentResponse> getCREATOR() {

        return CREATOR;
    }

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

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getDocumentUri() {
        return documentUri;
    }

    public void setDocumentUri(String documentUri) {
        this.documentUri = documentUri;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AssignmentResponse{" +
                "id='" + id + '\'' +
                ", uId='" + uId + '\'' +
                ", assignmentId='" + assignmentId + '\'' +
                ", documentUri='" + documentUri + '\'' +
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
        dest.writeString(uId);
        dest.writeString(assignmentId);
        dest.writeString(documentUri);
        dest.writeLong(timestamp);
    }
}
