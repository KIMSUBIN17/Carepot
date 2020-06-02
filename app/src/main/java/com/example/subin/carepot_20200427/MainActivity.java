package com.example.subin.carepot_20200427;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int version = 1;
    DatabaseOpenHelper helper;
    SQLiteDatabase database;

    String sql;
    Cursor cursor;

    TextView username;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        username = (TextView)findViewById(R.id.user_name);

        helper = new DatabaseOpenHelper(MainActivity.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        //username.setText(helper.get_Username());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }
}

