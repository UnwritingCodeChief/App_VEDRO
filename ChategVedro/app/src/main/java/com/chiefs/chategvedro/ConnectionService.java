package com.chiefs.chategvedro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionService extends Service {
    private ArrayList<Interlocutor> interlocutors;
    private boolean working;
    private ServerSocket serverSocket;

    public ConnectionService() {
        working = false;
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
        working = false;
        for (Interlocutor interlocutor: interlocutors) {
            interlocutor.getSocket().close();
        }
    }

    public void startAsServer(int port) throws IOException {
        if (!working) {
            working = true;
            serverSocket = new ServerSocket(port);
            while (working) {
                Socket tempsocket = serverSocket.accept();
                interlocutors.add(new Interlocutor(tempsocket,"John"));
            }
        }
    }
}
