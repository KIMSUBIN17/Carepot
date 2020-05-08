package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText edit_id;
    EditText edit_pw;
    EditText edit_passSign;
    EditText edit_name;
    EditText edit_phoneNum;

    Button btnFinish;


    String sql;
    Cursor cursor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_master);

        edit_id = (EditText) findViewById(R.id.user_edit_id);
        edit_pw = (EditText) findViewById(R.id.master_edit_pw);
        edit_passSign = (EditText) findViewById(R.id.master_edit_passSign);
        edit_name = (EditText) findViewById(R.id.master_edit_name);
        edit_phoneNum = (EditText) findViewById(R.id.master_edit_phoneNum);

        btnFinish = (Button) findViewById(R.id.master_btnFinish);

        helper = new DatabaseOpenHelper(SignupActivity.this, DatabaseOpenHelper.tableName, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        btnFinish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String id = edit_id.getText().toString();
                String pw = edit_pw.getText().toString();
                String passSign = edit_passSign.getText().toString();
                String name = edit_name.getText().toString();
                String phoneNum = edit_phoneNum.getText().toString();

                if(id.length() == 0 || pw.length() == 0 || passSign.length() == 0 || name.length() == 0 || phoneNum.length() == 0) {
                    //아이디와 비밀번호, 비밀번호확인, 이름, 전화번호는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(SignupActivity
                            .this, "내용을 모두 작성해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                sql = "SELECT id FROM "+ helper.tableName + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null); //select 실행

                if(cursor.getCount() != 0){ //테이블에 똑같은 id 내용이 있는지 확인
                    //존재하는 아이디입니다.
                    Toast toast = Toast.makeText(SignupActivity.this, "존재하는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    edit_id.setText("");
                }

                // 비밀번호 같은지 문자열 비교
                else if(!edit_pw.getText().toString().equals(edit_passSign.getText().toString())){
                    Toast toast = Toast.makeText(SignupActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    edit_pw.setText("");
                    edit_passSign.setText("");
                    return;
                }

                else{
                    helper.insertUser(database,id,pw);
                    Toast toast = Toast.makeText(SignupActivity.this, "가입이 완료되었습니다. 로그인을 해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
