package com.alvastudio.simplesocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alvastudio.simplesocket.Socket.Connection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connectBtn = (Button)findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection.getInstance().initConnection();
            }
        });

        Button diconnectBtn = (Button)findViewById(R.id.disconnectBtn);
        diconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection.getInstance().stopConnection();
            }
        });

        Button startRecordBtn = (Button)findViewById(R.id.startRecordBtn);
        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button stopRecordBtn = (Button)findViewById(R.id.stopRecordBtn);
        startRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
