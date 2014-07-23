package com.chiefs.chategvedro;

import java.util.ArrayList;

/**
 * Created by yolo on 23.07.14.
 */
public class Fifo {
    private ArrayList<String> bufferedMessages;

    public Fifo () {
        bufferedMessages = new ArrayList<String>();
    }

    public void add (String message) {
        bufferedMessages.add(message);
    }

    public String getLast () {
        String elem = String.valueOf(bufferedMessages.get(bufferedMessages.size() - 1));
        bufferedMessages.remove(bufferedMessages.size() - 1);
        return elem;
    }
}
