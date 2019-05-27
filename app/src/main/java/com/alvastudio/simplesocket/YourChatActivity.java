package com.alvastudio.simplesocket;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alvastudio.simplesocket.Interfaces.IASocketState;
import com.alvastudio.simplesocket.Interfaces.ISocketListener;
import com.alvastudio.simplesocket.Socket.Connection;

public abstract class YourChatActivity extends AppCompatActivity implements View.OnClickListener,
                                                                                    IASocketState,
                                                                                    ISocketListener {


    public void setListener(){
        Connection.getInstance().getASocket().getSocketListener().setListener(this);
    }

    @Override
    public void callUserNotFound(final int userID) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(YourChatActivity.this,"Пользователь "+userID+" не найден",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void setSender() {

    }

    @Override
    public void connectionReady() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(YourChatActivity.this,"Подключено",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void connectionError() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(YourChatActivity.this,"Ошибка подключения или сервер недоступен",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void disconnect() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(YourChatActivity.this,"Отключено",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
