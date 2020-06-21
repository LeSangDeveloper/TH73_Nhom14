package android.example.demolistviewplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WorksDataSource {
    private SQLiteDatabase db;
    private WorksSQLiteHelper dbHelper;
    private String[] allColumns = {WorksSQLiteHelper.COLUMN_ID, WorksSQLiteHelper.COLUMN_TITLE, WorksSQLiteHelper.COLUMN_DATE, WorksSQLiteHelper.COLUMN_TIME, WorksSQLiteHelper.COLUMN_LOCATION, WorksSQLiteHelper.COLUMN_NOTIFICATION};

    public WorksDataSource(Context context) {
        dbHelper = new WorksSQLiteHelper(context);
    }

    public void open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
    }

    public void close()
    {
        dbHelper.close();
    }

    public Work createWork(String title, String date, String time, String location, String notification)
    {
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(WorksSQLiteHelper.COLUMN_TITLE, title);
        contentvalues.put(WorksSQLiteHelper.COLUMN_DATE, date);
        contentvalues.put(WorksSQLiteHelper.COLUMN_TIME, time);
        contentvalues.put(WorksSQLiteHelper.COLUMN_LOCATION, location);
        contentvalues.put(WorksSQLiteHelper.COLUMN_NOTIFICATION, notification);
        long InsertedId = db.insert(WorksSQLiteHelper.TABLE_WORK, null, contentvalues);
        Cursor cursor = db.query(WorksSQLiteHelper.TABLE_WORK, allColumns, WorksSQLiteHelper.COLUMN_ID + " = " + InsertedId, null, null, null, null);
        cursor.moveToFirst();
        Work work = cursorToWork(cursor);
        cursor.close();
        return work;
    }

    public void deleteWork(Work w)
    {
        long id = w.getId();
        db.delete(WorksSQLiteHelper.TABLE_WORK, WorksSQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public Work saveWork(Work w)
    {
        long id = w.getId();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(WorksSQLiteHelper.COLUMN_TITLE, w.getTitle());
        contentvalues.put(WorksSQLiteHelper.COLUMN_DATE, w.getDate());
        contentvalues.put(WorksSQLiteHelper.COLUMN_TIME, w.getTime());
        contentvalues.put(WorksSQLiteHelper.COLUMN_LOCATION, w.getLocation());
        contentvalues.put(WorksSQLiteHelper.COLUMN_NOTIFICATION, w.getNotification());
        db.update(WorksSQLiteHelper.TABLE_WORK,contentvalues ,WorksSQLiteHelper.COLUMN_ID + " = " + id, null);

        return w;
    }

    public List<Work> getAllWork() {
        List<Work> works = new ArrayList<Work>();

        Cursor cursor = db.query(WorksSQLiteHelper.TABLE_WORK, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            Work work = cursorToWork(cursor);
            works.add(work);
            cursor.moveToNext();
        }
        cursor.close();
        return works;
    }

    private Work cursorToWork(Cursor cursor)
    {
        Work work = new Work();
        work.setId(cursor.getLong(0));
        work.setTitle(cursor.getString(1));
        work.setDate(cursor.getString(2));
        work.setTime(cursor.getString(3));
        work.setLocation(cursor.getString(4));
        work.setNotification(cursor.getString(5));
        return work;
    }

}
