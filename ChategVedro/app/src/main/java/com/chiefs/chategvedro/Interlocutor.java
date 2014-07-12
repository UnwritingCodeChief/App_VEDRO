package com.chiefs.chategvedro;

import android.content.Intent;

import java.net.Socket;

/**
 * Created by yolo on 12.07.14.
 */
public class Interlocutor {
    private Socket socket;
    private String name;

    public Interlocutor(Socket socket, String name) {
        this.socket = socket;
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }
}
