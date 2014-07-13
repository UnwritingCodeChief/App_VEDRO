package com.chiefs.chategvedro;

import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by yolo on 12.07.14.
 */
public class Interlocutor {
    private Socket socket;
    private String name;
    private InputStream inputStream;
    private OutputStream outputStream;

    public Interlocutor(Socket socket, String name) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.name = name;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }
}
