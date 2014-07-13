package com.chiefs.chategvedro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionService extends Service {
    private ArrayList<Interlocutor> interlocutors;
    private boolean serverLoop;
    private ServerSocket serverSocket;

    private boolean clientLoop;
    private Socket clientSocket;
    private InputStream clientInputStream;
    private OutputStream clientOutputStream;

    public ConnectionService() {
        serverLoop = false;
    }

    class LocalBinder extends Binder {
        ConnectionService getService() {
            return ConnectionService.this;
        }
    }

    IBinder binder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void stopServer() throws IOException {
        if (serverLoop) {
            serverLoop = false;
            for (Interlocutor interlocutor : interlocutors) {
                interlocutor.getSocket().close();
            }
        }
    }

    public void startAsServer(int port) throws IOException {
        if (!serverLoop) {
            serverSocket = new ServerSocket(port);
            interlocutors = new ArrayList<Interlocutor>();
            serverLoop = true;

            while (serverLoop) {
                Socket tempsocket = serverSocket.accept();
                interlocutors.add(new Interlocutor(tempsocket,"John"));
            }
        }
    }

    public void sendMessage (String message) throws IOException {
        if (serverLoop) {
            for (Interlocutor interlocutor : interlocutors) {
                interlocutor.getOutputStream().write(message.getBytes());
            }
        }
        else if (clientLoop) {
            clientOutputStream.write(message.getBytes());
        }
        else {
            //TODO: ke?
        }
    }
}
