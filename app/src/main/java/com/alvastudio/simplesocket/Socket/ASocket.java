package com.alvastudio.simplesocket.Socket;

import android.os.AsyncTask;
import android.util.Log;

import com.alvastudio.simplesocket.Config.ConstServerAPI;
import com.alvastudio.simplesocket.Interfaces.IASocketState;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ASocket extends AsyncTask<String, Void, Void> {

    private Socket socket;
    private SocketListener socketListener;
    private SocketSender socketSender;
    private boolean isOpen = false;
    private IASocketState mConnectionState;

    public ASocket(IASocketState connectionState) {
        Log.d("ASocket", "init");
        socketListener = new SocketListener();
        this.mConnectionState = connectionState;
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
            if (socket != null){
                if (socket.isClosed()){
                    mConnectionState.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mConnectionState.connectionError();
        }
    }

    private void connectionOpen() {
        try {
            InetAddress inetAddress = InetAddress.getByName(ConstServerAPI.ADDRESS);
            socket = new Socket(inetAddress, ConstServerAPI.PORT);
            socket.setSoTimeout(1000);

            if (socketListener != null) {
                socketListener.setBufferedReader(socket.getInputStream());
                socketListener.start();
            }

            socketSender = new SocketSender(socket.getOutputStream());
            socketSender.start();

            Connection.getInstance().setSocketSender(socketSender);
            mConnectionState.setSender();


            socketSender.sendMessage("Hello...");


            if (socket.isConnected()){
                mConnectionState.connectionReady();
            }

            isOpen = true;

        } catch (IOException e) {
            Log.d("ASocket", "Not connected");
            cancel(true);
            mConnectionState.connectionError();
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
