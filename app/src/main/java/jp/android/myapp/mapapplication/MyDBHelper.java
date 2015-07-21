package jp.android.myapp.mapapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KodateY on 2015/07/12.
 */
public class MyDBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "myDatabase.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_NAME = "myData";

    static final String ID = "_id";
    static final String REG_NAME = "regName";
    static final String REG_ADDRESS = "regAddress";
    static final String REG_PARKING = "regParking";
    static final String REG_OPENING = "regOpening";
    static final String REG_DATE = "regDate";

    public MyDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY autoincrement," +
                REG_NAME + " TEXT," +
                REG_ADDRESS + " TEXT," +
                REG_OPENING + " TEXT," +
                REG_PARKING + " TEXT," +
                REG_DATE + " TEXT);";
        db.execSQL(query);
    }

    public void onUpgrade(SQLiteDatabase db, int newVersion, int oldVersion){
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

}
