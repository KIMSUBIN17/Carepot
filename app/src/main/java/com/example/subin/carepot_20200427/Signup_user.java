package com.example.subin.carepot_20200427;

import android.content.Intent;
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
    EditText user_edit_pw;
    EditText user_edit_passSign;
    EditText user_edit_name;
    EditText user_edit_phoneNum;
    TextView user_text_address;

    Button btnFinish;
    Button btnSearch;

    String sql;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_user);

        user_edit_id = (EditText) findViewById(R.id.user_edit_id);
        user_edit_name = (EditText) findViewById(R.id.user_edit_name);
        user_edit_phoneNum = (EditText) findViewById(R.id.user_edit_phoneNum);
        user_text_address = (TextView) findViewById(R.id.user_text_address);

        btnFinish = (Button) findViewById(R.id.user_btnFinish);
        btnSearch = (Button) findViewById(R.id.user_btnSearch);

        helper = new DatabaseOpenHelper(Signup_user.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        Intent address_intent = getIntent();
        String address = address_intent.getStringExtra("address_value");
        System.out.println(address);
        user_text_address.setText(address);

        btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String id = user_edit_id.getText().toString();
                String pw = user_edit_pw.getText().toString();
                String passSign = user_edit_passSign.getText().toString();
                String name = user_edit_name.getText().toString();
                String phoneNum = user_edit_phoneNum.getText().toString();
                String address = user_text_address.getText().toString();

                if(id.length() == 0 || pw.length() == 0 || passSign.length() == 0 || name.length() == 0 || phoneNum.length() == 0 || address.length() == 0) {
                    //아이디와 비밀번호, 비밀번호확인, 이름, 전화번호, 주소는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(Signup_user
                            .this, "내용을 모두 작성해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                sql = "SELECT _id FROM "+ helper.TABLE_USERS + " WHERE _id = '" + id + "'";
                cursor = database.rawQuery(sql, null); //select 실행

                if(cursor.getCount() != 0){ //테이블에 똑같은 id 내용이 있는지 확인
                    //존재하는 아이디입니다.
                    Toast toast = Toast.makeText(Signup_user.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    user_edit_id.setText("");
                }

                // 비밀번호, 비밀번호확인 문자열 비교
                else if(!user_edit_pw.getText().toString().equals(user_edit_passSign.getText().toString())){
                    Toast toast = Toast.makeText(Signup_user.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    user_edit_pw.setText("");
                    user_edit_passSign.setText("");
                    return;
                }

                else{
                    helper.insertUser_user(database,id,pw,passSign,name,phoneNum,address);
                    Toast toast = Toast.makeText(Signup_user.this, "가입이 완료되었습니다. 로그인을 해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(), UserList.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapSearch.class);
                startActivity(intent);
                finish();
            }
        });
    }
}