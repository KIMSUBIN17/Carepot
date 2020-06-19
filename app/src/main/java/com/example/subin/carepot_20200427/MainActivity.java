package com.example.subin.carepot_20200427;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    static final int REQUEST_ENABLE_BT = 10;
    int mPairedDeviceCount = 0;
    Set<BluetoothDevice> mDevices;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice mRemoteDevice;
    BluetoothSocket mSocket = null;
    OutputStream mOutputStream = null;
    InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter = '\n';
    Thread mWorkerThread = null;
    byte[] readBuffer;
    int readBufferPosition;
    TextView text_count;
    TextView text_morning;
    TextView text_afternoon;
    TextView text_night;

    @Override
    protected void onDestroy() { //어플리케이션이 종료될때  호출되는 함수
        try{
            mWorkerThread.interrupt(); // 데이터 수신 쓰레드 종료
            mInputStream.close();
            mOutputStream.close();
            mSocket.close();
        }catch(Exception e){}
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user_name = (TextView)findViewById(R.id.text_name);
        user_phoneNum = (TextView)findViewById(R.id.text_phoneNum);
        user_address = (TextView)findViewById(R.id.text_address);
        guard_phoneNum = (TextView)findViewById(R.id.text_guard_phoneNum);
        user_caution = (TextView)findViewById(R.id.text_caution);
        text_count = (TextView) findViewById(R.id.text_count);
        text_morning = (TextView) findViewById(R.id.text_morning);
        text_afternoon = (TextView) findViewById(R.id.text_afternoon);
        text_night = (TextView) findViewById(R.id.text_night);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_clear = (Button) findViewById(R.id.btn_clear);

        helper = new DatabaseOpenHelper(MainActivity.this, DatabaseOpenHelper.TABLE_USERS, null, version);
        database = helper.getWritableDatabase(); //읽기,쓰기 모드로 DB오픈

        user_name.setText(helper.get_Username());
        user_phoneNum.setText(helper.get_UserphoneNum());
        user_address.setText(helper.get_Useraddress());
        user_caution.setText(helper.get_Usercaution());
        guard_phoneNum.setText(helper.get_GuardphoneNum());


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
        btn_clear.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                sendData("0");
                text_morning.setTextColor(Color.BLACK);
                text_afternoon.setTextColor(Color.BLACK);
                text_night.setTextColor(Color.BLACK);
                text_count.setText("");
            }
        });
        checkBluetooth();

    }

    BluetoothDevice getDeviceFromBondedList(String name){ //해당 블루투스 장치 객체를 페어링 된 장치 목록에서 찾아내기
        BluetoothDevice selectedDevice = null;
        for (BluetoothDevice device : mDevices)
        {
            if(name.equals(device.getName()))
            {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    void sendData(String msg){  //데이터 전송
        msg += mStrDelimiter; // 문자열 종료 표시
        try{
            mOutputStream.write(msg.getBytes()); // 문자열 전송0
        }catch(Exception e){
            // 문자열 전송 도중 오류가 발생한 경우
            Toast.makeText(getApplicationContext(),"데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /*
    void receive(String msg){
        msg += mStrDelimiter; // 문자열 종료 표시
        try{
            mInputStream.read(msg.getBytes());
        }catch(Exception e){
            // 문자열 전송 도중 오류가 발생한 경우
            Toast.makeText(getApplicationContext(),"데이터 수신 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
*/


    void connectToSelectedDevice(String selectedDeviceName){ //소켓
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
        try{
            // 소켓 생성
            mSocket = mRemoteDevice.createRfcommSocketToServiceRecord(uuid);
            // RFCOMM 채널을 통한 연결
            mSocket.connect();

            // 데이터 송수신을 위한 스트림 얻기
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();

            // 데이터 수신 준비
            beginListenForData();
        }catch(Exception e){
            // 블루투스 연결 중 오류 발생
            Toast.makeText(getApplicationContext(),"블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            finish(); // 어플리케이션 종료
        }
    }

    void beginListenForData(){ //데이터 수신
        final Handler handler = new Handler();
        readBufferPosition = 0; // 버퍼 내 수신 문자 저장 위치
        readBuffer = new byte[1024]; // 수신 버퍼


        // 문자열 수신 쓰레드
        mWorkerThread = new Thread(new Runnable(){
            public void run(){
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        int bytesAvailable = mInputStream.available(); // 수신 데이터 확인
                        if(bytesAvailable > 0){         // 데이터가 수신된 경우
                            byte[] packetBytes = new byte[bytesAvailable];
                            mInputStream.read(packetBytes);
                            for(int i = 0; i < bytesAvailable; i++){
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter){
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0,encodedBytes, 0, encodedBytes.length);
                                    final String read_buffer = new String(encodedBytes, "utf-8");
                                    readBufferPosition = 0;
                                    handler.post(new Runnable(){
                                        @SuppressLint("SetTextI18n")
                                        public void run(){
                                            // 수신된 문자열 데이터에 대한 처리 작업
                                            //
                                            for(int i = 0 ; i < read_buffer.length() ; i++){
                                                char[] value = read_buffer.toCharArray();

                                                if (value[i] == '1' || value[i] == '2' || value[i] == '3'){
                                                    text_count.setText(null);
                                                    text_count.setText(text_count.getText().toString()+ read_buffer + mStrDelimiter);
                                                }
                                                else if (value[i] == 'q')
                                                {
                                                    text_morning.setTextColor(Color.RED);
                                                }
                                                else if (value[i] == 'w')
                                                {
                                                    text_afternoon.setTextColor(Color.RED);
                                                }
                                                else  if (value[i] == 'e')
                                                {
                                                    text_night.setTextColor(Color.RED);
                                                }
                                                else
                                                {
                                                    Log.e("value","다른 값이 들어왔음");
                                                }
                                            }

                                        }
                                    });
                                }
                                else{
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex){
                        // 데이터 수신 중 오류 발생
                        Toast.makeText(getApplicationContext(),"데이터 수신 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });
        mWorkerThread.start();
    }

    void selectDevice(){ //AkertDialog 이용하여 페어링 된 장치목록 보여주기
        mDevices = mBluetoothAdapter.getBondedDevices();
        mPairedDeviceCount = mDevices.size();
        if(mPairedDeviceCount == 0){
            // 페어링 된 장치가 없는 경우
            Toast.makeText(getApplicationContext(),"페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            finish();// 어플리케이션 종료
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("블루투스 장치 선택");
        // 페어링 된 블루투스 장치의 이름 목록 작성

        List<String> listItems = new ArrayList<String>();
        for (BluetoothDevice device : mDevices) {
            listItems.add(device.getName());
        }
        listItems.add("취소");// 취소 항목 추가

        final CharSequence[] items =listItems.toArray(new CharSequence[listItems.size()]);

        builder.setItems(items, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int item){
                if(item == mPairedDeviceCount){
                    // 연결할 장치를 선택하지 않고 ‘취소’를 누른 경우
                    Toast.makeText(getApplicationContext(),"연결할 장치를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    // 연결할 장치를 선택한 경우
                    // 선택한 장치와 연결을 시도함
                    connectToSelectedDevice(items[item].toString());
                }
            }
        });
        builder.setCancelable(false); // 뒤로 가기 버튼 사용 금지
        AlertDialog alert = builder.create();
        alert.show();
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    void checkBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            // 장치가 블루투스를 지원하지 않는 경우
            Toast.makeText(getApplicationContext(),"기기가 블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
            finish();// 어플리케이션 종료
        }else {
            // 장치가 블루투스를 지원하는 경우
            if (!mBluetoothAdapter.isEnabled()) {
                // 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청
                Toast.makeText(getApplicationContext(),"현재 블루투스가 비활성 상태입니다.", Toast.LENGTH_LONG).show();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }else selectDevice();
            // 블루투스를 지원하며 활성 상태인 경우
            // 페어링 된 기기 목록을 보여주고 연결할 장치를 선택
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    // 블루투스가 활성 상태로 변경됨
                    selectDevice();
                }else if(resultCode == RESULT_CANCELED){
                    // 블루투스가 비활성 상태임
                    Toast.makeText(getApplicationContext(),"블루투스를 사용할 수 없어 프로그램을 종료합니다.",Toast.LENGTH_LONG).show();
                    finish();// 어플리케이션 종료
                }

                break;

        }

        super.onActivityResult(requestCode, resultCode, data);

    }





    }



