package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

import java.util.Random;

public class MessagePublisher implements Runnable {
    private MessageBroker messageBroker;
    private int publisherId;
    private MessageType messageTypeToPublish;
    private long numOfMessagesToPublish;

    public MessagePublisher(int publisherId, MessageType messageTypeToPublish, long numOfMessagesToPublish, MessageBroker messageBroker) {
        this.publisherId = publisherId;
        this.messageTypeToPublish = messageTypeToPublish;
        this.numOfMessagesToPublish = numOfMessagesToPublish;
        this.messageBroker = messageBroker;
    }

    public void run() {
        System.out.println("Starting Publisher with id: " + publisherId);
        for (int i = 0; i < numOfMessagesToPublish; i++) {
            messageBroker.publishMessage(messageTypeToPublish, generateRandomMessagePayload(20));
        }
        messageBroker.publishMessage(messageTypeToPublish, TerminationMessage.TERMINATE.toString());
        System.out.println("Terminating Publisher with id: " + publisherId);
    }

    public String generateRandomMessagePayload(int n) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        StringBuilder stringBuilder = new StringBuilder(n);
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            char c = chars[random.nextInt(chars.length)];
            stringBuilder.append(c);
        }

        return stringBuilder.toString();
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public MessageType getMessageTypeToPublish() {
        return messageTypeToPublish;
    }

    public void setMessageTypeToPublish(MessageType messageTypeToPublish) {
        this.messageTypeToPublish = messageTypeToPublish;
    }

    public long getNumOfMessagesToPublish() {
        return numOfMessagesToPublish;
    }

    public void setNumOfMessagesToPublish(long numOfMessagesToPublish) {
        this.numOfMessagesToPublish = numOfMessagesToPublish;
    }
}
