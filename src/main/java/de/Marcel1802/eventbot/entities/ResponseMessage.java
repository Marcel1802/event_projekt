package de.Marcel1802.eventbot.entities;

public class ResponseMessage {
    private String Message;

    public ResponseMessage(String msg){
        this.Message = msg;
    }

    public String getMessage() {
        return this.Message;
    }
}
