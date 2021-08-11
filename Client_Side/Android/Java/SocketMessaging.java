package com.example.sockettesting;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class SocketMessaging {
    // Android cannot use ip '127.0.0.1' for local host
    public final String SERVER_IP = "192.xxx.x.x";
    public final int SERVER_PORT = 59117;
    boolean listening = true;
    SocketAddress socketAddress;
    Socket socket;
    String receivedMsg;

    public void socket() {
        connectSocket(SERVER_IP, SERVER_PORT);
        // Start listening for data from server
        listener();
        // Send preliminary message
        send("USERNAME :android");
    }

    public void handleMsg(String msg) {
        // Send network data back to main thread
        MessageData data = new MessageData();
        data.setMessage(msg);
        MainHandler.sendMessage(data);
    }

    public void connectSocket(String ip, int port) {
        try {
            // Make connection
            InetAddress serverAddr = InetAddress.getByName(ip);
            socket = new Socket(serverAddr.getHostAddress(), port);
            socketAddress = socket.getRemoteSocketAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listener() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (listening) {
                    // Listening for data
                    receivedMsg = getData();
                    if (receivedMsg != null) {
                        handleMsg(receivedMsg);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    public String getData() {
        try {
            // Get Data
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            return serverDataToString(inputStream);
        } catch (IOException io) {
            io.printStackTrace();
            return null;
        }
    }

    public void send(String message) {
        try {
            // Send Data
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String serverDataToString(InputStream inputStream) throws IOException {
        if (inputStream.available() == 0) { return null; }
        ArrayList<Integer> byteArray = new ArrayList<>();
        int nRead;
        // Get utf-8 ints from socket
        while (inputStream.available() > 0) {
            nRead = inputStream.read();
            byteArray.add(nRead);
        }
        // Convert ArrayList of Integers to Array of Ints
        int[] intArray = new int[byteArray.size()];
        for (int j=0; j<byteArray.size(); j++){
            intArray[j] = byteArray.get(j);
        }
        // Convert Array of Ints to Array of Bytes
        byte[] byteMsg = new byte[byteArray.size()];
        for (int j=0; j<intArray.length; j++){
            byteMsg[j] = (byte) intArray[j];
        }
        // Create string from Byte Array
        String message = new String(byteMsg, "UTF-8");
        return message;
    }

    public void closeSocket() {
        listening = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
