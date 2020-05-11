package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper_m;
    DatabaseOpenHelper helper_u;
    SQLiteDatabase database_m;
    SQLiteDatabase database_u;

    CheckBox userCheck;
    CheckBox managerCheck;

    EditText idEditText;
    EditText pwEditText;
    Button btnLogin;
    Button btnJoin;

    String sql_m;
    String sql_u;
    Cursor cursor_m;
    Cursor cursor_u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userCheck = (CheckBox) findViewById(R.id.userCheck);
        managerCheck = (CheckBox) findViewById(R.id.managerCheck);

        idEditText = (EditText) findViewById(R.id.idEditText);
        pwEditText = (EditText) findViewById(R.id.pwEditText);

        btnJoin = (Button) findViewById(R.id.btnJoin);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        helper_m = new DatabaseOpenHelper(Login.this, DatabaseOpenHelper.TABLE_MANAGERS, null, version);
        helper_u = new DatabaseOpenHelper(Login.this, DatabaseOpenHelper.TABLE_USERS, null, version);

        database_m = helper_m.getWritableDatabase();
        database_u = helper_u.getWritableDatabase();


        // login ) 체크박스에 따라 로그인 버튼 눌렀을 때
        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String pw = pwEditText.getText().toString();

                sql_m = "SELECT id FROM "+ helper_m.TABLE_MANAGERS + " WHERE id = '" + id + "'";
                sql_u = "SELECT id FROM "+ helper_u.TABLE_USERS + " WHERE id = '" + id + "'";
                cursor_m = database_m.rawQuery(sql_m, null);
                cursor_u = database_u.rawQuery(sql_u, null);

                if(id.length() == 0 || pw.length() == 0) {
                    //아이디와 비밀번호는 필수 입력사항입니다.

                    Log.e("show","흐엥!");

                    Toast toast = Toast.makeText(Login.this, "아이디와 비밀번호는 필수 입력사항입니다.", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                // 관리자/사용자 중복 체크할때
                if(managerCheck.isChecked() && userCheck.isChecked()){
                    Toast toast = Toast.makeText(Login.this, "체크박스를 하나만 선택해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }

                // 사용자 체크하고 로그인
                else if(userCheck.isChecked()){

                    if(cursor_u.getCount() != 1){ //해당되는 테이블의 행 갯수 가져오기
                        //아이디가 틀렸습니다.
                        Toast toast = Toast.makeText(Login.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    sql_u = "SELECT pw FROM "+ helper_u.TABLE_USERS + " WHERE id = '" + id + "'";
                    cursor_u = database_u.rawQuery(sql_u, null);

                    cursor_u.moveToNext();

                    if(!pw.equals(cursor_u.getString(0))){
                        //비밀번호가 틀렸습니다.
                        Toast toast = Toast.makeText(Login.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    cursor_u.close();

                    // 메인페이지로 화면이동
                    Intent user_intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(user_intent);
                    finish();

                    //사용자 로그인성공
                    Toast toast = Toast.makeText(Login.this, "사용자로그인성공", Toast.LENGTH_SHORT);
                    toast.show();
                }

                // 관리자 체크하고 로그인
                else if(managerCheck.isChecked()){

                    // 아이디 확인
                    if(cursor_m.getCount() != 1){ //해당되는 테이블의 행 갯수 가져오기
                        Toast toast = Toast.makeText(Login.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }

                    sql_m = "SELECT pw FROM "+ helper_m.TABLE_MANAGERS + " WHERE id = '" + id + "'";
                    cursor_m = database_m.rawQuery(sql_m, null);

                    cursor_m.moveToNext(); // 다음 행으로 이동

                    // 비밀번호 확인
                    if(!pw.equals(cursor_m.getString(0))){
                        Toast toast = Toast.makeText(Login.this, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    cursor_m.close();

                    // 사용자목록화면으로 이동
                    Intent manager_intent = new Intent(getApplicationContext(), UserList.class);
                    startActivity(manager_intent);
                    finish();

                    // 관리자로그인성공
                    Toast toast = Toast.makeText(Login.this, "관리자로그인성공", Toast.LENGTH_SHORT);
                    toast.show();
                }

                // 체크안함
                else{
                    Toast toast = Toast.makeText(Login.this, "체크박스를 체크해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


        // join) 체크박스에 따른 회원가입 버튼 눌렀을 때
        btnJoin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                if(managerCheck.isChecked() && userCheck.isChecked()) {
                    Toast toast = Toast.makeText(Login.this, "체크박스를 하나만 선택해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(managerCheck.isChecked()){
                    Intent intent = new Intent(getApplicationContext(), Signup_master.class);
                    startActivity(intent);
                    //finish();

                    Toast toast = Toast.makeText(Login.this, "관리자 회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else if(userCheck.isChecked()){
                    Intent intent = new Intent(getApplicationContext(), Signup_user.class);
                    startActivity(intent);
                    //finish();

                    Toast toast = Toast.makeText(Login.this, "사용자 회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else{
                    Toast toast = Toast.makeText(Login.this, "체크박스를 체크해주세요.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}
