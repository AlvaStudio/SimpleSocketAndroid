package com.alvastudio.simplesocket.Socket;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class SocketListener extends Thread {
    private BufferedReader bufferedReader;

    public SocketListener() {

    }

    public void setBufferedReader(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            try {
                if (Connection.getInstance().getASocket().getSocket() != null) {
                    String incomingString = bufferedReader.readLine();

                    if (incomingString != null) {
                        Log.d("SocketListener", "----------------");
                        Log.d("SocketListener", "incoming " + incomingString);
                    } else {
                        Log.d("SocketListener", "----------------");
                        Log.d("SocketListener", "incoming NULL");
                    }
                } else {
                    Log.d("SocketListener", "Socket is NULL");
                }
            } catch (IOException e) {
                try {
                    join();
                } catch (InterruptedException InterruptedException) {
                    InterruptedException.printStackTrace();
                }
            }
        }
    }
}
