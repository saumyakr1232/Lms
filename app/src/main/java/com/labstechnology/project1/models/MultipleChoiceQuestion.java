package com.labstechnology.project1.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.labstechnology.project1.Utils;

import java.util.ArrayList;

public class MultipleChoiceQuestion implements Parcelable {
    private int id;
    private String question;
    private ArrayList<String> options;
    private String hint;
    private int flag;

    public MultipleChoiceQuestion() {
    }

    public MultipleChoiceQuestion(String question, ArrayList<String> options, String hint) {
        this.id = Utils.getQuestionId();
        this.question = question;
        this.options = options;
        this.hint = hint;
    }

    protected MultipleChoiceQuestion(Parcel in) {
        id = in.readInt();
        question = in.readString();
        options = in.createStringArrayList();
        hint = in.readString();
        flag = in.readInt();
    }

    public static final Creator<MultipleChoiceQuestion> CREATOR = new Creator<MultipleChoiceQuestion>() {
        @Override
        public MultipleChoiceQuestion createFromParcel(Parcel in) {
            return new MultipleChoiceQuestion(in);
        }

        @Override
        public MultipleChoiceQuestion[] newArray(int size) {
            return new MultipleChoiceQuestion[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestion{" +
                "id=" + id +
                ", question='" + question + '\'' +
                ", options=" + options +
                ", hint='" + hint + '\'' +
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
        dest.writeString(question);
        dest.writeStringList(options);
        dest.writeString(hint);
        dest.writeInt(flag);
    }
}
