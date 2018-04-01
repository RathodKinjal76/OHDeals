package com.example.kinjal.ohdeals;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "RegisteredUserRecordManager";

    private static final String TABLE_USER = "user";
    private static final String FULL_NAME = "fullname";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String PINCODE = "pincode";
    private static final String MOBILE_NO = "mobileno";
    private static final String EMAIL = "email";
    private static final String MEMBERSHIPID = "membershipid";
    private static final String BIRTHDATE = "birthdate";
    private static final String PROMOCODE = "promocode";
    public static String RegisterStatus = "";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + "(" + FULL_NAME + " TEXT," + ADDRESS + " TEXT," +
                CITY + " TEXT," + PINCODE + " TEXT," + MOBILE_NO + " TEXT," + EMAIL + " TEXT," + MEMBERSHIPID + " TEXT PRIMARY KEY,"
                + BIRTHDATE + " TEXT," + PROMOCODE + " TEXT );";
        db.execSQL(CREATE_TABLE_USER);
        System.out.println("Table User Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
    }

    //Insert login details
    public void registerUser(String FullName, String Address, String City, String Pincode, String MobileNo, String Email,
                             String MembershipId, String BirthDate, String PromoCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO " + TABLE_USER + " ( " + FULL_NAME + " , " + ADDRESS + " , " + CITY + " , " + PINCODE + " , " +
                MOBILE_NO + " , " + EMAIL + " , " + MEMBERSHIPID + " , " + BIRTHDATE + " , " + PROMOCODE + " ) " + " VALUES " +
                " ( '" + FullName + "' , '" + Address + "' , '" + City + "' , '" + Pincode + "' , '" + MobileNo +
                "' , '" + Email + "' , '" + MembershipId + "' , '" + BirthDate + "' , '" + PromoCode + "' ) ");
        RegisterStatus = "success";
        Log.e("RegisterStatus", "User Registration Successful !!");
        db.close();// Closing database connection
    }
}