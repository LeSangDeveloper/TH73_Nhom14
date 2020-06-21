package android.example.demolistviewplan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WorksSQLiteHelper extends SQLiteOpenHelper {

        public static final String TABLE_WORK = "work";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_NOTIFICATION = "notification";

        private static final String DATABASE_NAME = "work.db";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "create table " + TABLE_WORK + " ( " + COLUMN_ID + " integer primary key autoincrement, " +
                COLUMN_TITLE + " text not null, " + COLUMN_DATE + " text not null, " + COLUMN_TIME +" text not null, " + COLUMN_LOCATION + " text not null, " + COLUMN_NOTIFICATION + " text not null);";

        public WorksSQLiteHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database)
        {
            database.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
        {
            Log.w(WorksSQLiteHelper.class.getName(), "Ugrading database form old version " + oldVersion + " to " + newVersion);
            database.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        }
}