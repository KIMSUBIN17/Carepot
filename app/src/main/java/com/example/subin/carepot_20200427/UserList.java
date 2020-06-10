package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class UserList extends AppCompatActivity {

    int version = 1;
    ListView listView;

    SQLiteDatabase database;
    DatabaseOpenHelper helper;

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
            int [] reslds = {R.id.text01,R.id.text02,R.id.text03,R.id.text04,R.id.text05};

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