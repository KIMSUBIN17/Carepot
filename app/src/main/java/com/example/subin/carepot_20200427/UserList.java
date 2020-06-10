package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UserList extends AppCompatActivity {

    int version = 1;
    ListView listView;

    SQLiteDatabase database;
    DatabaseOpenHelper helper;

    EditText user_edit_id;
    EditText user_edit_phoneNum;
    EditText guard_edit_name;
    EditText guard_edit_phoneNum;
    TextView user_edit_caution;

    String sql;
    Cursor cursor;

    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        btn_add = (Button) findViewById(R.id.btn_add);
        listView = (ListView) findViewById(R.id.user_list);

        helper = new DatabaseOpenHelper(UserList.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase();

        cursor = helper.getUser();
        if(cursor != null){
            String[] columns = {"_id", "user_phoneNum", "guard_name", "guard_phoneNum", "user_caution"};
            int [] reslds = {R.id.text_user_name,R.id.text_user_phoneNum,R.id.text_guard_name,R.id.text_guard_phoneNum,R.id.text_user_caution};

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.list_item,cursor,columns,reslds,0);
            listView.setAdapter(adapter);
        }



        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Signup_user.class);
                startActivity(intent);

                Toast toast = Toast.makeText(UserList.this, "사용자 회원가입 화면으로 이동", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        if(listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}