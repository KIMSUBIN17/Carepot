package com.example.subin.carepot_20200427;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

//import androidx.annotation.Nullable;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public static final String tableName = "managers";

    public DatabaseOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("tag","생성 db가 없을때만 최초로 실행함");
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + tableName + "(id text, pw text, name text, phoneNum text)";
        try {
            db.execSQL(sql);
        }catch (SQLException e){
            Log.i("tag","테이블 생성 중 오류 발생");
        }
    }

    public void insertUser(SQLiteDatabase db, String id, String pw, String name, String phoneNum) {
        Log.i("tag", "회원가입을 했을때 실행함");
        db.beginTransaction();
        try {
            String sql = "INSERT INTO " + tableName + "(id, pw, name, phoneNum)" + "values(' + id + ', ' + pw + ', ' + name + ', ' + phoneNum + ')";
            db.execSQL(sql); // select를 제외한 모든 SQL문장 실행
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
