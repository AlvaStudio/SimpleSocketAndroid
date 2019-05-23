package com.alvastudio.simplesocket.Socket;

import com.alvastudio.simplesocket.Interfaces.IASocketState;

public class Connection {
    private static Connection instance;
    private ASocket aSocket;
    private SocketSender mSocketSender;

    public Connection() {
    }

    public static synchronized Connection getInstance() {
        if (instance == null) {
            instance = new Connection();

        }
        return Connection.instance;
    }

    public void initConnection(IASocketState connectionState) {
        if (aSocket == null){
            this.aSocket = new ASocket(connectionState);
            aSocket.execute();
        } else{
            stopConnection();
        }
    }

    public void stopConnection() {
       if (aSocket != null){
           aSocket.onCancelled();
           aSocket = null;
       }
    }

    public ASocket getASocket() {
        return aSocket;
    }

    public SocketSender getSocketSender() {
        return mSocketSender;
    }

    public void setSocketSender(SocketSender socketSender) {
        mSocketSender = socketSender;
    }
}
