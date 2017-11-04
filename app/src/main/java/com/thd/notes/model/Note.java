package com.thd.notes.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Tran Hai Dang on 10/31/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class Note implements Parcelable {
    public static final String COLOR_WHITE = "#FFFFFF";
    public static final String COLOR_ORANGE = "#FFA726";
    public static final String COLOR_GREEN= "#66BB6A";
    public static final String COLOR_PINK = "#F06292";
    public static final int ALARM_TRUE = 1;
    public static final int ALARM_FALSE = 2;
    private String noteTitle;
    private String noteContent;
    private String date;
    private String time;
    private String color;
    private String dateAlarm;
    private String timeAlarm;
    private int alarm;
    private int id;
    private List<String> imageList = new ArrayList<>();

    public Note() {
        this.color = COLOR_WHITE;
        this.alarm = ALARM_FALSE;
        this.dateAlarm = "";
        this.timeAlarm = "";
    }

    public Note(String noteTitle, String noteContent, String date, String time)  {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.date = date;
        this.time = time;
        this.color = COLOR_WHITE;
        this.alarm = 0;
    }

    protected Note(Parcel in) {
        noteTitle = in.readString();
        noteContent = in.readString();
        date = in.readString();
        time = in.readString();
        color = in.readString();
        alarm = in.readInt();
        dateAlarm = in.readString();
        timeAlarm = in.readString();
        in.readStringList(imageList);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public String getDateAlarm() {
        return dateAlarm;
    }

    public void setDateAlarm(String dateAlarm) {
        this.dateAlarm = dateAlarm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeAlarm() {
        return timeAlarm;
    }

    public void setTimeAlarm(String timeAlarm) {
        this.timeAlarm = timeAlarm;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public String getBrief() {
        if(this.noteContent.length()<=15) return this.noteContent;
        return noteContent.substring(0,15)+"...";
    }
    public String getStoreString() {
        StringBuilder builder = new StringBuilder();
        for(String s : imageList) {
            builder.append(s).append("|");
        }
        if (builder.length()!=0) builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
    public void decode(String decode) {
        StringTokenizer st = new StringTokenizer(decode,"|");
        ArrayList<String> t = new ArrayList<>();
        while (st.hasMoreTokens()) {
            t.add(st.nextToken());
        }
        imageList.addAll(t);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteTitle);
        dest.writeString(noteContent);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(color);
        dest.writeInt(alarm);
        dest.writeString(dateAlarm);
        dest.writeString(timeAlarm);
        dest.writeStringList(imageList);
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

    @Override
    public String toString() {
        return "Note{" +
                "noteTitle='" + noteTitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", color='" + color + '\'' +
                ", dateAlarm='" + dateAlarm + '\'' +
                ", timeAlarm='" + timeAlarm + '\'' +
                ", alarm=" + alarm +
                ", id=" + id +
                ", imageList=" + imageList +
                '}';
    }
}
