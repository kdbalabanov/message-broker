package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;

/**
 * The Message that is exchanged between MessagePublisher and MessageConsumer through MessageBroker
 * @param <T> The type of the message payload
 */
public class Message<T> {
    private MessageType messageType;
    private T messagePayload;

    /**
     * Constructor for Message
     * @param messageType The message type
     * @param messagePayload The message payload
     */
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
