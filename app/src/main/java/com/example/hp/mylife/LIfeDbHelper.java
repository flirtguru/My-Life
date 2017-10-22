package com.example.hp.mylife;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hp on 14-Oct-17.
 */

public class LIfeDbHelper extends SQLiteOpenHelper {



    private static final String DATABASE_NAME="incidents.db";
    private static final int DATABASE_VERSION=1;

    public LIfeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_TABLE= "CREATE TABLE"+ LifeContracts.LifeEntry.TABLE_NAME+"("+
        LifeContracts.LifeEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
        LifeContracts.LifeEntry.COLUMN_GENRE +" INTEGER NOT NULL," +
                LifeContracts.LifeEntry.COLUMN_SUBJECT +" TEXT," +
                LifeContracts.LifeEntry.COLUMN_DESCRIPTION +" TEXT);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//no use
    }
}




