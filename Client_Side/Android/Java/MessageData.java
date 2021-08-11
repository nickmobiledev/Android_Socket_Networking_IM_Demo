package com.example.sockettesting;

public class MessageData {
    public static final String START = "start";
    public static final String MESSAGE = "message";
    String msgType;
    String message;

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
