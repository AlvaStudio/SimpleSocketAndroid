package com.alvastudio.simplesocket.Socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ASocket extends AsyncTask<String, Void, Void> {

    private Socket socket;
    private SocketListener socketListener;
    private SocketSender socketSender;
    private boolean isOpen = false;

    public ASocket() {
        Log.d("ASocket", "init");
        socketListener = new SocketListener();
    }

    @Override
    protected Void doInBackground(String... strings) {
        connectionOpen();
        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        connectionClose();

    }

    private void connectionClose() {
        Log.d("ASocket", "Disconnected");
        try {
            if (isOpen) {
                socketSender.sendMessage("Exit");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectionOpen() {
        try {
            InetAddress inetAddress = InetAddress.getByName(ConstServerAPI.ADDRESS);
            socket = new Socket(inetAddress, ConstServerAPI.PORT);

            if (socketListener != null) {
                socketListener.setBufferedReader(socket.getInputStream());
            }

            socketSender = new SocketSender(socket.getOutputStream());
            socketSender.start();
            socketSender.sendMessage("Hello...");

            isOpen = true;

        } catch (IOException e) {
            Log.d("ASocket", "Not connected");
            cancel(true);
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public SocketListener getSocketListener() {
        return socketListener;
    }

    public SocketSender getSocketSender() {
        return socketSender;
    }
}
