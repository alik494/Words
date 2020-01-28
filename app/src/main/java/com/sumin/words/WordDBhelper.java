package com.sumin.words;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class WordDBhelper extends SQLiteOpenHelper {

    private static final String DB_NAME  = "words.db";
    private static final int DB_VERSION=1;

    public WordDBhelper(Context context) {
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WordsEntry.CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(WordsEntry.DROP_COMMAND);
        onCreate(db);
    }



    public static final class WordsEntry implements BaseColumns{
        public static final String TABLE_NAME="words" ;
        public static final String COLUMN_ENG = "eng";
        public static final String COLUMN_RUS = "rus";
        public static final String COLUMN_HEALTH = "health";

        public static final String TYPE_TEXT = "TEXT";
        public static final String TYPE_INTEGER = "INTEGER";

        public static final String CREATE_COMMAND = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(" + _ID + " " + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ENG + " " + TYPE_TEXT + ", " +
                COLUMN_RUS +" " + TYPE_TEXT + ", " +
                COLUMN_HEALTH +" " + TYPE_INTEGER  + ")";

        public static final String DROP_COMMAND = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
