package com.alvastudio.simplesocket.Socket;

import android.util.Log;

import com.alvastudio.simplesocket.Interfaces.ICommandSocketSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SocketSender extends Thread implements ICommandSocketSender {

    private PrintWriter printWriter;
    private ArrayList<String> messageQueue = new ArrayList<>();

    SocketSender(OutputStream outputStream) {
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


    /*
     * Вот тут будем писать методы которые отправляют команды серверу
     * */


    @Override
    public void callUserByID(String id) {
        JSONObject js = new JSONObject();
        try {
            js.put("userID",id);
            sendMessage(js.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
