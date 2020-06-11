package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    Button btn_back;
    Button btn_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = (TextView)findViewById(R.id.text_name);
        user_phoneNum = (TextView)findViewById(R.id.text_phoneNum);
        user_address = (TextView)findViewById(R.id.text_address);
        guard_phoneNum = (TextView)findViewById(R.id.text_guard_phoneNum);
        user_caution = (TextView)findViewById(R.id.text_user_caution);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_clear = (Button) findViewById(R.id.btn_clear);


        //사용자 목록으로 돌아가는 버튼
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UserList.class);
                startActivity(intent);
                //finish();

                //Toast toast = Toast.makeText(MainActivity.this, "사용자 목록으로 이동", Toast.LENGTH_SHORT);
                //toast.show();

            }
        });

        //버튼누른횟수 데이터 초기화 버튼


        helper = new DatabaseOpenHelper(MainActivity.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        user_name.setText(helper.get_Username());
        user_phoneNum.setText(helper.get_UserphoneNum());
        //user_address.setText(helper.get_Useraddress());

    }
}

