package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
    //String sql;

    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        btn_add = (Button) findViewById(R.id.btn_add);
        listView = (ListView) findViewById(R.id.user_list);

        //user_RecyclerView = this.findViewById(R.id.recyclerView);
        //user_RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        helper = new DatabaseOpenHelper(UserList.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase();

        //sql =  "SELECT * FROM "+ helper.TABLE_USERS;
        //cursor = database.rawQuery(sql, null);

        cursor = helper.getUser();
        if(cursor != null){
            //startManagingCursor(cursor);
            String[] columns = {"_id", "pw", "passSign", "name", "phoneNum", "address"};
            int [] reslds = {R.id.text01,R.id.text02,R.id.text03,R.id.text04,R.id.text05,R.id.text06};

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

    }
}