package com.alvastudio.simplesocket;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alvastudio.simplesocket.Socket.ASocket;
import com.alvastudio.simplesocket.Socket.Connection;
import com.alvastudio.simplesocket.Socket.SocketSender;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPerm();
        initButton();

    }

    private void initButton() {
        EditText editText = findViewById(R.id.editText);

        Button connectBtn = findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(this);

        Button disconnectBtn = findViewById(R.id.disconnectBtn);
        disconnectBtn.setOnClickListener(this);

        Button startRecordBtn = findViewById(R.id.startRecordBtn);
        startRecordBtn.setOnClickListener(this);
        startRecordBtn.setTag(editText.getTag().toString());


        Button stopRecordBtn = findViewById(R.id.stopRecordBtn);
        stopRecordBtn.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        if (mThread != null){
            mThread.interrupt();
        }
        super.onPause();
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
                Connection.getInstance().initConnection();
                break;
            case R.id.disconnectBtn:
                Connection.getInstance().stopConnection();
                break;
            case R.id.startRecordBtn:
                int userID = (int)v.getTag();


                JSONObject js = new JSONObject();
                try {
                    js.put("userID",userID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //FIXME необходимо отправить сообещение серверу о том что я хочу позвонить кому-то
                Connection.getInstance().getASocket().getSocketSender().sendMessage(js.toString());

                //mThread = new RecordMP3();
                //mThread.start();
                break;
            case R.id.stopRecordBtn:

        }
    }
}
