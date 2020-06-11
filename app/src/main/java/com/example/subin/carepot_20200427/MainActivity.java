package com.example.subin.carepot_20200427;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;

    TextView user_name;
    TextView user_phoneNum;
    TextView user_address;
    TextView guard_phoneNum;
    TextView user_caution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = (TextView)findViewById(R.id.text_name);
        user_phoneNum = (TextView)findViewById(R.id.text_phoneNum);
        user_address = (TextView)findViewById(R.id.text_address);
        guard_phoneNum = (TextView)findViewById(R.id.text_guard_phoneNum);
        user_caution = (TextView)findViewById(R.id.text_caution);


        helper = new DatabaseOpenHelper(MainActivity.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        user_name.setText(helper.get_Username());
        user_phoneNum.setText(helper.get_UserphoneNum());
        user_address.setText(helper.get_Useraddress());
        user_caution.setText(helper.get_Usercaution());
        guard_phoneNum.setText(helper.get_GuardphoneNum());

    }
}

