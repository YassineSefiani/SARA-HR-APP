package com.noideastudios.hrms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "candidates.db";
    private static final String TABLE_CANDIDATES = "candidatetable";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_POSITION = "position";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_PHOTO = "photo";
    private static final String COLUMN_RESUME = "resume";

    DBhandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                "CREATE TABLE " + TABLE_CANDIDATES + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + COLUMN_NAME + " VARCHAR(30), "
                        + COLUMN_PHONE + " VARCHAR(10), "
                        + COLUMN_POSITION + " VARCHAR(30), "
                        + COLUMN_STATUS + " VARCHAR(15), "
                        + COLUMN_PHOTO + " VARCHAR(50), "
                        + COLUMN_RESUME + " VARCHAR(50));";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CANDIDATES);
        onCreate(sqLiteDatabase);
    }

    void addCandidate(Candidate candidate) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, candidate.getName());
        contentValues.put(COLUMN_PHONE, candidate.getPhone());
        contentValues.put(COLUMN_POSITION, candidate.getPosition());
        contentValues.put(COLUMN_STATUS, candidate.getStatus());
        contentValues.put(COLUMN_PHOTO, candidate.getPhotoURI());
        contentValues.put(COLUMN_RESUME, candidate.getResumeURI());
        sqLiteDatabase.insert(TABLE_CANDIDATES, null, contentValues);
        sqLiteDatabase.close();
    }

    Candidate returnCandidate(int employee_id) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CANDIDATES
                + " WHERE " + COLUMN_ID + " = " + employee_id;

        Candidate candidate = new Candidate();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null) {
            cursor.moveToFirst();
            candidate.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            candidate.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
            candidate.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
            candidate.setPosition(cursor.getString(cursor.getColumnIndex(COLUMN_POSITION)));
            candidate.setPhotoURI(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)));
            candidate.setResumeURI(cursor.getString(cursor.getColumnIndex(COLUMN_RESUME)));
            cursor.close();
        }
        return candidate;
    }

    ArrayList<Candidate> returnCandidates(int number) {

        String query = "";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        switch (number) {
            case 0:
                query = "SELECT * FROM " + TABLE_CANDIDATES + ";";
                break;
            case 1:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " WHERE " + COLUMN_STATUS + " = 'Selected'";
                break;
            case 100:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " ORDER BY " + COLUMN_STATUS + ";";
                break;
            case 200:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " ORDER BY " + COLUMN_NAME + ";";
                break;
            case 201:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " WHERE " + COLUMN_STATUS + " = 'Selected' "
                        + " ORDER BY " + COLUMN_NAME + ";";
                break;
            case 300:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " ORDER BY " + COLUMN_POSITION + ";";
                break;
            case 301:
                query = "SELECT * FROM " + TABLE_CANDIDATES
                        + " WHERE " + COLUMN_STATUS + " = 'Selected' "
                        + " ORDER BY " + COLUMN_POSITION + ";";
                break;
        }

        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Candidate candidate = new Candidate();
                candidate.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                candidate.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                candidate.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                candidate.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                candidate.setPosition(cursor.getString(cursor.getColumnIndex(COLUMN_POSITION)));
                candidate.setPhotoURI(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)));
                candidate.setResumeURI(cursor.getString(cursor.getColumnIndex(COLUMN_RESUME)));
                candidateArrayList.add(candidate);
                cursor.moveToNext();
            }
            cursor.close();
        }

        sqLiteDatabase.close();
        return candidateArrayList;
    }

    void updateCandidate(int id, String name, String phone, String position, String status, String photoURI, String resumeUri) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "UPDATE " + TABLE_CANDIDATES + " SET "
                + COLUMN_NAME + " = '" + name + "', "
                + COLUMN_PHONE + " = '" + phone + "', "
                + COLUMN_POSITION + " = '" + position + "', "
                + COLUMN_STATUS + " = '" + status + "', "
                + COLUMN_PHOTO + " = '" + photoURI + "',"
                + COLUMN_RESUME + " = '" + resumeUri + "'"
                + " WHERE " + COLUMN_ID + " = " + id;
        sqLiteDatabase.execSQL(query);
    }

    void deleteAll() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_CANDIDATES, null, null);
    }

    void deleteSelected() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CANDIDATES + " WHERE "
                + COLUMN_STATUS + " = 'Selected' ;";
        sqLiteDatabase.execSQL(query);
    }

    ArrayList<Candidate> returnQuery(String search) {
        String query = "SELECT * FROM " + TABLE_CANDIDATES
                + " WHERE " + COLUMN_NAME + " LIKE '%" + search.toLowerCase() + "%';";
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ArrayList<Candidate> candidateArrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Candidate candidate = new Candidate();
                candidate.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                candidate.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                candidate.setPhone(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
                candidate.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS)));
                candidate.setPosition(cursor.getString(cursor.getColumnIndex(COLUMN_POSITION)));
                candidate.setPhotoURI(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)));
                candidate.setResumeURI(cursor.getString(cursor.getColumnIndex(COLUMN_RESUME)));
                candidateArrayList.add(candidate);
                cursor.moveToNext();
            }
            cursor.close();
        }

        sqLiteDatabase.close();
        return candidateArrayList;
    }
}
