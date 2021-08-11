package com.example.sockettesting;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Message {
    ConstraintLayout layout;
    KeyboardHandler keyboardHandler;
    TextView sendButton;
    TextView editTextInput;
    TextView messageTextView;
    SocketThread socketThread;
    String messageText = "";
    View.OnClickListener sendButtonClick = new View.OnClickListener(){
        @Override
        public void onClick(View v) { sendButtonEvent(); }
    };
    View.OnClickListener layoutClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) { layoutEvent(view); }
    };

    public Message(ConstraintLayout layout, MainActivity mainActivity) {
        keyboardHandler = new KeyboardHandler(mainActivity);
        this.layout = layout;
        // Set layout click listener to close keyboard
        layout.setOnClickListener(layoutClick);
        // View for Message Text
        messageTextView = layout.findViewById(R.id.text_view);
        // Edit Text Box
        editTextInput = layout.findViewById(R.id.edit_box);
        // Send button
        sendButton = layout.findViewById(R.id.send_button);
        sendButton.setOnClickListener(sendButtonClick);
        // Starting Socket Thread
        socketThread = new SocketThread();
        socketThread.start();
    }

    public void sendButtonEvent() {
        // Get text entered and send
        CharSequence charSequence = editTextInput.getText();
        String textMessage = charSequence.toString();
        socketThread.sendMessage(MessageData.MESSAGE, textMessage);
        editTextInput.setText("");
    }

    public void layoutEvent(View view) {
        keyboardHandler.outsideKeyboardClickUtil(view);
    }

    public void updateMessageText(String message){
        messageText = messageText + message;
        messageText = messageText + "\n";
        messageTextView.setText(messageText);
    }


}
