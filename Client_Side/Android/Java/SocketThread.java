package com.example.sockettesting;

import android.os.Looper;
import android.os.Message;

public class SocketThread extends Thread{

    SocketHandler handler;
    SocketMessaging socketMessaging = new SocketMessaging();
    public SocketHandler getHandler() {
        return handler;
    }
    public void setHandler(SocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new SocketHandler(socketMessaging);
        setHandler(handler);
        sendMessage(MessageData.START, null);
        Looper.loop();
    }

    public void sendMessage(String msgType, String message){
        // Main thread to socket thread message
        MessageData data = new MessageData();
        data.setMsgType(msgType);
        data.setMessage(message);
        Message msg = Message.obtain();
        msg.obj = data;
        getHandler().sendMessage(msg);
    }
}
