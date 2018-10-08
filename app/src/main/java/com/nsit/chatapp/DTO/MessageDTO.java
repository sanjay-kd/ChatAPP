package com.nsit.chatapp.DTO;

import java.util.HashMap;

public class MessageDTO {

    private String username;
    private String message;
    private String timeStamp;
    private String from;
    private String to;

//    public MessageDTO(String username, String message, String timeStamp) {
//        this.username = username;
//        this.message = message;
//        this.timeStamp = timeStamp;
//    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
