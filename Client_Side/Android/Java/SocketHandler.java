package com.example.sockettesting;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

public class SocketHandler extends Handler {

    SocketMessaging socketMessaging;

    public SocketHandler(SocketMessaging socketMessaging) {
        // Keep same instance of socket
        this.socketMessaging = socketMessaging;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        // Socket thread messages will end up here
        // Check what to do with message
        MessageData messageData = (MessageData) msg.obj;
        String msgType = messageData.getMsgType();
        if (msgType.equals("start")){
            // Start the socket off the main thread
            socketMessaging.socket();
        } else if (msgType.equals("message")){
            // Send a message through the socket thread
            socketMessaging.send(messageData.getMessage());
        }
    }

}
