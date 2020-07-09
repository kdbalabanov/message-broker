package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;

public class Message<T> {
    private MessageType messageType;
    private T messagePayload;

    public Message(MessageType messageType, T messagePayload) {
        this.messageType = messageType;
        this.messagePayload = messagePayload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public T getMessagePayload() {
        return messagePayload;
    }

    public void setMessagePayload(T messagePayload) {
        this.messagePayload = messagePayload;
    }
}
