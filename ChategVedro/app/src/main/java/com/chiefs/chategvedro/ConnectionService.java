package com.chiefs.chategvedro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
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

    private Fifo fifo;

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

    public String getLastMessage () throws InterruptedException {
        //TODO: Something to show messages on tha screen SWAGACIOUS SYNCHRONIZATION
        synchronized (fifo) {
            fifo.wait();
            return fifo.getLast();
        }
    }

    public void startAsServer(int port) throws IOException {
        if (!serverLoop) {
            serverSocket = new ServerSocket(port);
            interlocutors = new ArrayList<Interlocutor>();
            fifo = new Fifo();
            serverLoop = true;

            Thread acceptingSocketThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (serverLoop) {
                        Socket tempsocket = null;
                        try {
                            tempsocket = serverSocket.accept();
                            interlocutors.add(new Interlocutor(tempsocket,"John")); //LOL UNSAFE
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d("ServerLoop", "AcceptingSocketThread",e);
                        }
                    }
                }
            });

            Thread acceptingMessageThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while(serverLoop) {
                        //TODO: ну тут короч нужно принимать сообщения от каждого из клиентов,
                        //TODO: но я не знаю как
                    }
                }
            });
            acceptingSocketThread.start();
            acceptingMessageThread.start();
        }
    }

    public void sendMessage (String message) throws IOException {
        if (serverLoop) {
            for (Interlocutor interlocutor : interlocutors) {
                interlocutor.getOutputStream().write(message.getBytes());
            }
            //TODO: something for server to see
        }
        else if (clientLoop) {
            clientOutputStream.write(message.getBytes());
            //TODO: что-то для отображения
        }
        else {
            //TODO: ke?
        }
    }

    public void startAsClient (InetAddress addr, int port) throws IOException {
        clientSocket = new Socket(addr,port);
        clientInputStream = clientSocket.getInputStream();
        clientOutputStream = clientSocket.getOutputStream();
        fifo = new Fifo();
        Thread acceptingMessageThread = new Thread(new Runnable() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientInputStream));

            @Override
            public void run() {
                int charsRead;
                char[] buffer = new char[256]; //lol
                while (clientLoop) {
                    try {
                        charsRead = reader.read(buffer);
                        String message = new String(buffer,0,charsRead);
                        fifo.add(message);
                    } catch (IOException e) {
                        Log.d("ClientLoop", "AcceptingMessagesThread",e);
                    }
                }
            }
        });
        clientLoop = true;
        acceptingMessageThread.start();
    }

    public ArrayList<Interlocutor> getInterlocutors() {
        return interlocutors;
    }
}
