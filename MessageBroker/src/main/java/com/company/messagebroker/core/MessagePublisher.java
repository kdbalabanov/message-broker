package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

import java.util.Random;

/**
 * MessagePublisher publishes messages to MessageBroker
 * Implements Runnable so it is run as a task
 */
public class MessagePublisher implements Runnable {
    private final IMessageBroker MESSAGE_BROKER;
    private int publisherId;
    private MessageType messageTypeToPublish;
    private long numOfMessagesToPublish;

    /**
     * Constructor for MessagePublisher
     * @param publisherId The id of the MessagePublisher
     * @param messageTypeToPublish The MessageType that is to be published by the MessagePublisher
     * @param numOfMessagesToPublish The number of messages that the MessagePublisher is supposed to publish
     * @param messageBroker The MessageBroker that acts as a buffer between the MessagePublisher and MessageConsumer
     */
    public MessagePublisher(int publisherId, MessageType messageTypeToPublish, long numOfMessagesToPublish, IMessageBroker messageBroker) {
        this.publisherId = publisherId;
        this.messageTypeToPublish = messageTypeToPublish;
        this.numOfMessagesToPublish = numOfMessagesToPublish;
        this.MESSAGE_BROKER = messageBroker;
    }

    /**
     * The run method of the MessagePublisher task which publishes to the MessageBroker
     * The MessagePublisher will publish a TerminationMessage when done publishing the set number of messages
     * The TerminationMessage is a signal for the consumer to terminate
     */
    public void run() {
        System.out.println("Starting Publisher with id: " + publisherId + " on Thread: " + Thread.currentThread().getName());
        for (int i = 0; i < numOfMessagesToPublish; i++) {
            MESSAGE_BROKER.publishMessage(messageTypeToPublish, generateRandomMessagePayload(20));
        }
        MESSAGE_BROKER.publishMessage(messageTypeToPublish, TerminationMessage.TERMINATE.toString());
        System.out.println("Terminating Publisher with id: " + publisherId + " on Thread: " + Thread.currentThread().getName());
    }

    /**
     * Generate random string message payload
     * @param n length of payload
     * @return
     */
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
