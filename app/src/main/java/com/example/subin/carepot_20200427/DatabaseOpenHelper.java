package com.example.subin.carepot_20200427;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

//import androidx.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_MANAGERS = "managers";
    public static final String TABLE_USERS = "users";

    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("tag","생성 db가 없을때만 최초로 실행함");
        createTable_manager(db);
        createTable_user(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createTable_manager(SQLiteDatabase db) {
        String sql_m = "CREATE TABLE " + TABLE_MANAGERS + "(id text, pw text, passSign text, name text, phoneNum text)";
        try {
            db.execSQL(sql_m);
        }catch (SQLException e){
            Log.i("tag","테이블 생성 중 오류 발생");
        }
    }

    public void createTable_user(SQLiteDatabase db) {
        String sql_u = "CREATE TABLE " + TABLE_USERS + "(id text, pw text, passSign text, name text, phoneNum text, address text)";
        try {
            db.execSQL(sql_u);
        }catch (SQLException e){
            Log.i("tag","테이블 생성 중 오류 발생");
        }
    }

    public void insertUser_manager(SQLiteDatabase db, String id, String pw, String passSign, String name, String phoneNum) {
        Log.i("tag", "회원가입을 했을때 실행함");
        db.beginTransaction();
        try {
            String sql_m = "INSERT INTO " + TABLE_MANAGERS + "(id, pw, passSign, name, phoneNum)"
                    + "values('" + id + "', '" + pw + "', '" + passSign + "', '" + name + "', '" + phoneNum + "')";
            db.execSQL(sql_m); // select를 제외한 모든 SQL문장 실행
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void insertUser_user(SQLiteDatabase db, String id, String pw, String passSign, String name, String phoneNum, String address) {
        Log.i("tag", "회원가입을 했을때 실행함");
        db.beginTransaction();
        try {
            String sql_u = "INSERT INTO " + TABLE_USERS + "(id, pw, passSign, name, phoneNum, address)"
                    + "values('" + id + "', '" + pw + "', '" + passSign + "', '" + name + "', '" + phoneNum + "', '" + address + "')";
            db.execSQL(sql_u); // select를 제외한 모든 SQL문장 실행
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public String getUser(){
        SQLiteDatabase db = getReadableDatabase();
        String user_name =  "";

        Cursor cursor = db.rawQuery("SELECT name FROM " + TABLE_USERS,null);
        cursor.moveToFirst();
            user_name = cursor.getString(0);
            return  user_name;
    }

}
