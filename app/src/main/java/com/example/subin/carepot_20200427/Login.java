package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    EditText idEditText;
    EditText pwEditText;
    Button btnLogin;
    Button btnJoin;

    String sql;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        idEditText = (EditText) findViewById(R.id.idEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);

        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        helper = new DatabaseOpenHelper(Login.this, DatabaseOpenHelper.TABLE_MANAGERS, null, version);

        database = helper.getWritableDatabase();

        // login ) 로그인 버튼 눌렀을 때
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                sql = "SELECT id FROM "+ helper.TABLE_MANAGERS + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null);

                if(id.length() == 0 || pw.length() == 0) {
                    //아이디와 비밀번호는 필수 입력사항입니다.
                    Toast toast = Toast.makeText(Login.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                // 아이디 확인
                if(cursor.getCount() != 1){ //해당되는 테이블의 행 갯수 가져오기
                    Toast toast = Toast.makeText(Login.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                sql = "SELECT pw FROM "+ helper.TABLE_MANAGERS + " WHERE id = '" + id + "'";
                cursor = database.rawQuery(sql, null);

                cursor.moveToNext(); // 다음 행으로 이동

                // 비밀번호 확인
                if(!pw.equals(cursor.getString(0))){
                    Toast toast = Toast.makeText(Login.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }
                cursor.close();

                // 사용자목록화면으로 이동
                Intent manager_intent = new Intent(getApplicationContext(), UserList.class);
                startActivity(manager_intent);
                finish();

                // 관리자로그인성공
                Toast toast = Toast.makeText(Login.this, "관리자로그인성공", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        // join) 회원가입 버튼 눌렀을 때
        btnJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Signup_master.class);
                startActivity(intent);
                //finish();

                Toast toast = Toast.makeText(Login.this, "관리자 회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                toast.show();

            }
        });

    }
}