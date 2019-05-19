package com.alvastudio.simplesocket.Socket;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

class SocketSender extends Thread {

    private PrintWriter printWriter;
    private ArrayList<String> messageQueue = new ArrayList<>();

    public SocketSender(OutputStream outputStream) {
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);
    }

    public synchronized void sendMessage(String message) {
        if (Connection.getInstance().getASocket().getSocket() != null) {
            messageQueue.add(message);
            notify();
        }
    }

    private synchronized String getNextMessageFromQueue() {
        String message = null;
        try {
            while (messageQueue.size() == 0)
                wait();

            message = messageQueue.get(0);
            messageQueue.remove(0);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return message;
    }

    private void sendMessageToClient(String message) {
        Log.d("Send message", message);
        printWriter.println(message);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {

            String message = getNextMessageFromQueue();
            if (message != null) {
                sendMessageToClient(message);
            }
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }


}
