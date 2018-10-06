package com.nsit.chatapp.DTO;

import java.util.HashMap;

public class MessageDTO {

    private String username;
    private String message;

    public MessageDTO(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
