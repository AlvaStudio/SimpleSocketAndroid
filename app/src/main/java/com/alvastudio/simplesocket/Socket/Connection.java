package com.alvastudio.simplesocket.Socket;

public class Connection {
    private static Connection instance;
    private ASocket aSocket;

    public Connection() {
    }

    public static synchronized Connection getInstance() {
        if (instance == null) {
            instance = new Connection();

        }
        return Connection.instance;
    }

    public void initConnection() {
        this.aSocket = new ASocket();
        aSocket.execute();
    }

    public void stopConnection() {
        aSocket.onCancelled();
    }

    public ASocket getASocket() {
        return aSocket;
    }
}
