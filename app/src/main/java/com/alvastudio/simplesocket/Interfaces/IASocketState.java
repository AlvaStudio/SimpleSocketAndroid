package com.alvastudio.simplesocket.Interfaces;

public interface IASocketState {
    void setSender();
    void connectionReady();
    void connectionError();
    void disconnect();
}
