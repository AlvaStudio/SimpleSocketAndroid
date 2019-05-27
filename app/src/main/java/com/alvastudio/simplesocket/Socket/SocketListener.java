package com.alvastudio.simplesocket.Socket;

import android.util.Log;

import com.alvastudio.simplesocket.YourChatActivity;
import com.alvastudio.simplesocket.Interfaces.ICommandSocketSender;
import com.alvastudio.simplesocket.Utils.Utils;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SocketListener extends Thread {
    private BufferedReader bufferedReader;
    private ICommandSocketSender mSender;
    private YourChatActivity mListener;

    public void setListener(YourChatActivity listener) {
        this.mListener = listener;
    }



    void setBufferedReader(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        this.mSender = Connection.getInstance().getSocketSender();
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

                        JSONObject js;
                        if ((js= Utils.getJSONInString(incomingString) )!= null) {
                            if (js.containsKey("result")) {
                                if (js.containsKey("callUserNotFound")) {
                                    callUserNotFound(js);
                                }
                            }
                        }

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

    private void callUserNotFound(JSONObject js) {
        int userID = Integer.parseInt(String.valueOf(js.get("userID")));
        mListener.callUserNotFound(userID);
    }


}
