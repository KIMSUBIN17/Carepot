package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Signup_user extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText user_edit_id;
    EditText user_edit_phoneNum;
    EditText guard_edit_name;
    EditText guard_edit_phoneNum;
    TextView user_edit_caution;
    TextView user_edit_address;
    TextView user_time_morning;
    TextView user_time_afternoon;
    TextView user_time_night;

    Button btnFinish;
    //Button btnSearch;

    String sql;
    Cursor cursor;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user);

        user_edit_id = (EditText) findViewById(R.id.user_edit_id);
        user_edit_phoneNum = (EditText) findViewById(R.id.user_edit_phoneNum);
        guard_edit_name = (EditText) findViewById(R.id.guard_edit_name);
        guard_edit_phoneNum = (EditText) findViewById(R.id.guard_edit_phoneNum);
        user_edit_caution = (EditText) findViewById(R.id.user_edit_caution);
        user_edit_address = (TextView) findViewById(R.id.user_edit_address);
        user_time_morning = (TextView) findViewById(R.id.user_time_morning);
        user_time_afternoon = (TextView) findViewById(R.id.user_time_afternoon);
        user_time_night = (TextView) findViewById(R.id.user_time_night);

        btnFinish = (Button) findViewById(R.id.user_btnFinish);

        helper = new DatabaseOpenHelper(Signup_user.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

          btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String id = user_edit_id.getText().toString();
                String user_phoneNum = user_edit_phoneNum.getText().toString();
                String guard_name = guard_edit_name.getText().toString();
                String guard_phoneNum = guard_edit_phoneNum.getText().toString();
                String user_caution = user_edit_caution.getText().toString();
                String user_address = user_edit_address.getText().toString();
                String user_morning = user_time_morning.getText().toString();
                String user_afternoon = user_time_morning.getText().toString();
                String user_night = user_time_morning.getText().toString();

                if(id.length() == 0 || user_phoneNum.length() == 0 || guard_name.length() == 0 || guard_phoneNum.length() == 0 ||
                        user_caution.length() == 0 || user_address.length() == 0 || user_morning.length() == 0 || user_afternoon.length() == 0 || user_night.length() == 0) {
                    //아이디와 비밀번호, 비밀번호확인, 이름, 전화번호, 주소는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(Signup_user
                            .this, "내용을 모두 작성해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }



                // DB 접근할때 속성값에 무조건! _id 필요 (지우지마세요!)
                sql = "SELECT _id FROM "+ helper.TABLE_USERS + " WHERE _id = '" + id + "'";
                cursor = database.rawQuery(sql, null); //select 실행

                if(cursor.getCount() != 0){ //테이블에 똑같은 id 내용이 있는지 확인
                    //존재하는 아이디입니다.
                    Toast toast = Toast.makeText(Signup_user.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    user_edit_id.setText("");
                }

                else{
                    helper.insert_user(database, id, user_phoneNum, guard_name, guard_phoneNum, user_caution, user_address, user_morning, user_afternoon, user_night);
                    Toast toast = Toast.makeText(Signup_user.this, "가입이 완료되었습니다. 로그인을 해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), UserList.class);
                    startActivity(intent);
                    finish();
                }

            }
        });



    }
}