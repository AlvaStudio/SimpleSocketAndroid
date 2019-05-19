package com.alvastudio.simplesocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alvastudio.simplesocket.Socket.Connection;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Connection.getInstance().initConnection();
    }
}
