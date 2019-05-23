package com.alvastudio.simplesocket;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alvastudio.simplesocket.Interfaces.IASocketState;
import com.alvastudio.simplesocket.Interfaces.ICommandSocketSender;
import com.alvastudio.simplesocket.Socket.Connection;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IASocketState {

    private ICommandSocketSender mSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPerm();
        initButton();

    }

    private void initButton() {



        Button connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(this);

        Button disconnectBtn = findViewById(R.id.disconnectBtn);
        disconnectBtn.setOnClickListener(this);

        Button startRecordBtn = findViewById(R.id.startRecordBtn);
        startRecordBtn.setOnClickListener(this);




        Button stopRecordBtn = findViewById(R.id.stopRecordBtn);
        stopRecordBtn.setOnClickListener(this);

    }
    private void checkPerm() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.connectBtn:
                Connection.getInstance().initConnection(MainActivity.this);
                break;
            case R.id.disconnectBtn:
                Connection.getInstance().stopConnection();
                break;
            case R.id.startRecordBtn:

                EditText editText = findViewById(R.id.editText);
                String userID = editText.getText().toString();

                if (userID.length() != 0){
                    mSender.callUserByID(userID);
                    //mThread = new RecordMP3();
                    //mThread.start();
                } else {
                    Toast.makeText(this,"Введите ID пользователя",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.stopRecordBtn:

        }
    }

    @Override
    public void setSender() {
        this.mSender = Connection.getInstance().getSocketSender();
    }

    @Override
    public void connectionReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Подключено",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void connectionError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Ошибка подключения или сервер недоступен",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,"Отключено",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
