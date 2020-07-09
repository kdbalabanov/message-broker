package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

/**
 * The MessageConsumer consumes messages from the MessageBroker
 * Implements Runnable so it is run as a task
 */
public class MessageConsumer implements Runnable {
    private final IMessageBroker messageBroker;
    int consumerId;
    MessageType messageTypeToConsume;

    /**
     * Constructor for MessageConsumer
     * @param consumerId The id of the MessageConsumer
     * @param messageTypeToConsume The MessageType that is to be consumer by the MessageConsumer
     * @param messageBroker The MessageBroker that acts as a buffer between the MessagePublisher and MessageConsumer
     */
    public MessageConsumer(int consumerId, MessageType messageTypeToConsume, IMessageBroker messageBroker) {
        this.consumerId = consumerId;
        this.messageTypeToConsume = messageTypeToConsume;
        this.messageBroker = messageBroker;
    }

    /**
     * The run method of the MessageConsumer task which consumes from the MessageBroker
     * The MessageConsumer will terminate upon the consumption of a TerminationMessage
     */
    public void run() {
        try{
            System.out.println("Starting Consumer with id: " + consumerId + " on Thread: " + Thread.currentThread().getName());
            while (true) {
                if (TerminationMessage.TERMINATE.toString().equals(messageBroker.consumeMessage(messageTypeToConsume))) {
                    break;
                }
            }
            Thread.currentThread().interrupt();
            System.out.println("Terminating Consumer with id: " + consumerId + " on Thread: " + Thread.currentThread().getName());
        } catch(InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public int getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(int consumerId) {
        this.consumerId = consumerId;
    }

    public MessageType getMessageTypeToConsume() {
        return messageTypeToConsume;
    }

    public void setMessageTypeToConsume(MessageType messageTypeToConsume) {
        this.messageTypeToConsume = messageTypeToConsume;
    }

}
