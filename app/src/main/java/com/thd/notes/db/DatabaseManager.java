package com.thd.notes.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.thd.notes.model.Note;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tran Hai Dang on 11/3/2017.
 * Email : tranhaidang2320@gmail.com
 */

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "noteManager.db";
    private static final String NOTE_COLUMN_ID = "ID";
    private static final String NOTE_TABLE_NAME = "NOTE";
    private static final String NOTE_COLUMN_TITLE = "TITLE";
    private static final String NOTE_COLUMN_CONTENT = "CONTENT";
    private static final String NOTE_COLUMN_DATE = "DATE";
    private static final String NOTE_COLUMN_TIME = "TIME";
    private static final String NOTE_COLUMN_COLOR = "COLOR";
    private static final String NOTE_COLUMN_ALARM = "ALARM";
    private static final String NOTE_COLUMN_IMAGES = "IMAGE";
    private static final String NOTE_COLUMN_ADATE = "ADATE";
    private static final String NOTE_COLUMN_ATIME = "ATIME";
    private static final String SQL_CREATE_ENTRY = "CREATE TABLE "+NOTE_TABLE_NAME+"( "
            + NOTE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NOTE_COLUMN_TITLE + " TEXT, "
            + NOTE_COLUMN_CONTENT + " TEXT, "
            + NOTE_COLUMN_DATE + " TEXT, "
            + NOTE_COLUMN_TIME + " TEXT, "
            + NOTE_COLUMN_COLOR + " TEXT, "
            + NOTE_COLUMN_IMAGES + " TEXT, "
            + NOTE_COLUMN_ADATE + " TEXT, "
            + NOTE_COLUMN_ATIME + " TEXT, "
            +NOTE_COLUMN_ALARM + " INTEGER)";

    public DatabaseManager(Context context) {
        super(context,DB_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS NOTE");
        onCreate(db);
    }
    public boolean insert(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE,note.getNoteTitle());
        contentValues.put(NOTE_COLUMN_CONTENT,note.getNoteContent());
        contentValues.put(NOTE_COLUMN_DATE,note.getDate());
        contentValues.put(NOTE_COLUMN_TIME,note.getTime());
        contentValues.put(NOTE_COLUMN_ALARM,note.getAlarm());
        contentValues.put(NOTE_COLUMN_COLOR,note.getColor());
        contentValues.put(NOTE_COLUMN_IMAGES,note.getStoreString());
        contentValues.put(NOTE_COLUMN_ADATE,note.getDateAlarm());
        contentValues.put(NOTE_COLUMN_ATIME,note.getTimeAlarm());
        db.insert(NOTE_TABLE_NAME,null,contentValues);
        return true;
    }
    public ArrayList<Note> getAllNote() {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor res = database.rawQuery("select * from " + NOTE_TABLE_NAME,null);
        while (res.moveToNext()) {
            int id = res.getInt(res.getColumnIndex(NOTE_COLUMN_ID));
            String title = res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE));
            String content = res.getString(res.getColumnIndex(NOTE_COLUMN_CONTENT));
            String color = res.getString(res.getColumnIndex(NOTE_COLUMN_COLOR));
            String date = res.getString(res.getColumnIndex(NOTE_COLUMN_DATE));
            String time = res.getString(res.getColumnIndex(NOTE_COLUMN_TIME));
            String images = res.getString(res.getColumnIndex(NOTE_COLUMN_IMAGES));
            int alarm = res.getInt(res.getColumnIndex(NOTE_COLUMN_ALARM));
            String aDate = res.getString(res.getColumnIndex(NOTE_COLUMN_ADATE));
            String aTime = res.getString(res.getColumnIndex(NOTE_COLUMN_ATIME));
            Note note = new Note(title,content,date,time);
            note.setId(id);
            note.setAlarm(alarm);
            note.setColor(color);
            note.decode(images);
            note.setDateAlarm(aDate);
            note.setTimeAlarm(aTime);
            notes.add(note);
        }
        Collections.reverse(notes);
        res.close();
        return notes;
    }
    public void delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NOTE_TABLE_NAME,NOTE_COLUMN_ID+ " = "+ id,null);
    }
    public void update(Note note) {
        Log.d("update to",note.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put(NOTE_COLUMN_TITLE,note.getNoteTitle());
        contentValues.put(NOTE_COLUMN_CONTENT,note.getNoteContent());
        contentValues.put(NOTE_COLUMN_DATE,note.getDate());
        contentValues.put(NOTE_COLUMN_TIME,note.getTime());
        contentValues.put(NOTE_COLUMN_ALARM,note.getAlarm());
        contentValues.put(NOTE_COLUMN_COLOR,note.getColor());
        contentValues.put(NOTE_COLUMN_IMAGES,note.getStoreString());
        contentValues.put(NOTE_COLUMN_ADATE,note.getDateAlarm());
        contentValues.put(NOTE_COLUMN_ATIME,note.getTimeAlarm());
        db.update(NOTE_TABLE_NAME,contentValues,NOTE_COLUMN_ID+" = "+note.getId(),null);
    }
}
