package com.example.shaunchap.notr;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shaunchap on 12/11/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Notes.db";
    private static final String TABLE_NAME = "notes_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "DATE";
    private static final String COL_3 = "TITLE";
    private static final String COL_4 = "CONTENT";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, DATE LONG, TITLE TEXT, CONTENT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(long date, String title, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, date);
        contentValues.put(COL_3, title);
        contentValues.put(COL_4, content);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        }else{
            return true;
        }
    }
}

// RESOURCES: Giving credit, for where I found some code that helped me.

// Saving txt files: https://www.youtube.com/watch?v=BnYruBLqdmM
// Launching camera intent: https://www.youtube.com/watch?v=je9bdkdNQqg
// SQLite DB Insertion: https://www.youtube.com/watch?v=T0ClYrJukPA
// Saving Notes as objects: https://github.com/EMBEDONIX/
// https://stackoverflow.com/questions/3674933/find-out-if-android-device-is-portrait-or-landscape-for-normal-usage
// https://stackoverflow.com/questions/10539268/making-two-linearlayouts-have-50-of-the-screen-each-without-using-layout-weight/10539489
// https://developer.android.com/training/sharing/shareaction.html
// https://developer.android.com/guide/topics/media/camera.html
