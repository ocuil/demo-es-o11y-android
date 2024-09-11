package minimal.todo.com.todo.database;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import minimal.todo.com.todo.database.model.Noted;

import static minimal.todo.com.todo.database.model.Noted.TABLE_NAMED;


/**
 * Created by Michal Štrba on 01/08/18.
 */

public class DatabaseHelperb extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notess_db";
    public DatabaseHelperb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Noted.CREATE_TABLED);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMED);
        onCreate(db);
    }

    public long insertNoted(String noted, String timed, String timestamped) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Noted.COLUMN_NOTE, noted);
        values.put(Noted.COLUMN_TIME, timed);
        values.put(Noted.COLUMN_TIMESTAMP, timestamped);
        long idb = db.insert(TABLE_NAMED, null, values);
        db.close();
        return idb;
    }
    public Noted getNoteb(long idb) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAMED,
                new String[]{Noted.COLUMN_ID, Noted.COLUMN_NOTE, Noted.COLUMN_TIME, Noted.COLUMN_TIMESTAMP},
                Noted.COLUMN_ID + "=?",
                new String[]{String.valueOf(idb)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Noted noted = new Noted(
                cursor.getInt(cursor.getColumnIndex(Noted.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_TIMESTAMP)));
        cursor.close();
        return noted;
    }
    public Noted getTimeb(long idb) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAMED,
                new String[]{Noted.COLUMN_ID, Noted.COLUMN_NOTE, Noted.COLUMN_TIME, Noted.COLUMN_TIMESTAMP},
                Noted.COLUMN_ID + "=?",
                new String[]{String.valueOf(idb)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Noted noted = new Noted(
                cursor.getInt(cursor.getColumnIndex(Noted.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_NOTE)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_TIME)),
                cursor.getString(cursor.getColumnIndex(Noted.COLUMN_TIMESTAMP)));
        cursor.close();
        return noted;
    }

    public List<Noted> getAllDakar() {
        List<Noted> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + Noted.TABLE_NAMED + " ORDER BY " +
                Noted.COLUMN_TIMESTAMP + " DESC ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursory = db.rawQuery(selectQuery, null);
        if (cursory.moveToFirst()) {
            do {
                Noted noted = new Noted();
                noted.setIdb(cursory.getInt(cursory.getColumnIndex(Noted.COLUMN_ID)));
                noted.setNoted(cursory.getString(cursory.getColumnIndex(Noted.COLUMN_NOTE)));
                noted.setTimed(cursory.getString(cursory.getColumnIndex(Noted.COLUMN_TIME)));
                noted.setTimestamped(cursory.getString(cursory.getColumnIndex(Noted.COLUMN_TIMESTAMP)));
                notes.add(noted);
            } while (cursory.moveToNext());
        }
        db.close();
        return notes;
    }
    public int getNotesCountb() {
        String countQuery = "SELECT  * FROM " + TABLE_NAMED;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
    public int updateNoteb(Noted noted) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Noted.COLUMN_NOTE, noted.getNoted());
        values.put(Noted.COLUMN_TIME, noted.getTimed());
        values.put(Noted.COLUMN_TIMESTAMP, noted.getTimestamped());
        return db.update(TABLE_NAMED, values, Noted.COLUMN_ID + " = ?",
                new String[]{String.valueOf(noted.getIdb())});
    }
    public void deleteNoted(Noted noted) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAMED, Noted.COLUMN_ID + " = ?",
                new String[]{String.valueOf(noted.getIdb())});
        db.close();
    }
    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAMED;
        db.execSQL(clearDBQuery);
        db.close();
    }
}
