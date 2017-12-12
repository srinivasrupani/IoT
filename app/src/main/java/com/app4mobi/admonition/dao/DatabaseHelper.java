package com.app4mobi.admonition.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app4mobi.admonition.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Srinivas Rupani on 10/14/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase myDataBase;
    private String TAG = "Database";
    private final Context mContext;
    private static final String DATABASE_NAME = "admonition.db";
    public static String CREATE_TABLE = Constants.CREATE_TABLE;
    public static String USER_TABLE = Constants.TBL_USER;
    public static String ECG_TABLE = Constants.TBL_ECG;
    public static String DOC_TABLE = Constants.TBL_DOCS;
    private String KEY_ID = "ID";
    private String KEY_NAME = "NAME";
    private String KEY_EMAIL = "EMAIL";
    private String KEY_MOBILE = "MOBILE";
    private String KEY_PATIENT_ID = "PATIENT_ID";
    private String KEY_TEST_NAME = "TEST_NAME";
    private String KEY_DEVICE_ID = "DEVICE_ID";
    private String KEY_PATIENT_NAME = "PATIENT_NAME";
    private String KEY_ECG_INFO = "ECG_INFO";
    private String KEY_DOC_SOURCE = "SOURCE";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUserTbl = CREATE_TABLE + USER_TABLE + " (" + KEY_ID + Constants.INT_PRIMARY_KEY + KEY_NAME
            + " TEXT," + "" + KEY_EMAIL + " TEXT," + "" + KEY_MOBILE + " TEXT)";
        db.execSQL(createUserTbl);
        Log.e(TAG, USER_TABLE + "==>Table Created");

        String createECGTbl = CREATE_TABLE + ECG_TABLE + " (" + KEY_ID + Constants.INT_PRIMARY_KEY + KEY_TEST_NAME + " TEXT,"
            + KEY_DEVICE_ID + " TEXT," + KEY_PATIENT_ID + " TEXT," + KEY_PATIENT_NAME + " TEXT," + KEY_ECG_INFO + " TEXT)";
        db.execSQL(createECGTbl);
        Log.e(TAG, ECG_TABLE + "==>Table Created");

        String createDocTbl = CREATE_TABLE + DOC_TABLE + " (" + KEY_ID + Constants.INT_PRIMARY_KEY + KEY_DOC_SOURCE + " TEXT)";
        db.execSQL(createDocTbl);
        Log.e(TAG, DOC_TABLE + "==>Table Created");

        myDataBase = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constants.DROP_TBL + USER_TABLE);
        db.execSQL(Constants.DROP_TBL + ECG_TABLE);
        db.execSQL(Constants.DROP_TBL + DOC_TABLE);

        onCreate(db);
    }

    //add your public methods for insert, get, delete and update data in database.
    public boolean insertCommonTable(String tableName, HashMap<String, String> listOfKeyVal) {
        myDataBase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //TODO-Iterating
        for (Map.Entry<String, String> entry : listOfKeyVal.entrySet()) {
            contentValues.put(entry.getKey(), entry.getValue());
            Log.e("" + entry.getKey(), "" + entry.getValue());
        }
        myDataBase.insert(tableName, null, contentValues);
        Log.e(TAG, "Table Inserted ==> " + tableName);
        myDataBase.close();
        return true;
    }

    public void getSpecificRecord(String tableName, int id) {
        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.query(USER_TABLE, new String[]{KEY_ID,
            KEY_NAME, KEY_EMAIL, KEY_MOBILE}, KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        //TODO : Get All the Details of Specific User
    }

    public int getTotalRecordCount(String tableName) {
        myDataBase = this.getReadableDatabase();
        String recordCountQuery = Constants.SELECT_STAR + tableName;
        Cursor cursor = myDataBase.rawQuery(recordCountQuery, null);
        return cursor.getCount();
    }

/*
    public List<PendingECG> getPendingECGInfo() {
        List<PendingECG> pendingECGList = new ArrayList<PendingECG>();
        // Select All Query
        String selectQuery = Constants.SELECT_STAR + ECG_TABLE;

        myDataBase = this.getReadableDatabase();
        Cursor cursor = myDataBase.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PendingECG pendingECG = new PendingECG();
                pendingECG.setId(cursor.getInt(0));
                pendingECG.setHmTestName(cursor.getString(1));
                pendingECG.setDeviceId(cursor.getString(2));
                pendingECG.setPatientID(Long.parseLong(cursor.getString(3)));
                pendingECG.setPatientName(cursor.getString(4));
                pendingECG.setFilePath(cursor.getString(5));
                // Adding pendingECG to list
                pendingECGList.add(pendingECG);
            } while (cursor.moveToNext());
        }
        // return pendingECGList
        return pendingECGList;
    }
*/

    public void deleteRow(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + ECG_TABLE + " WHERE " + KEY_ECG_INFO + "='" + value + "'");
        db.close();
    }

}
