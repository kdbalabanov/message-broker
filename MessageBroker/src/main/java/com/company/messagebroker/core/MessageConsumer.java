package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

public class MessageConsumer implements Runnable {
    private MessageBroker messageBroker;
    int consumerId;

    MessageType messageTypeToConsume;

    public MessageConsumer(int consumerId, MessageType messageTypeToConsume, MessageBroker messageBroker) {
        this.consumerId = consumerId;
        this.messageTypeToConsume = messageTypeToConsume;
        this.messageBroker = messageBroker;
    }

    public void run() {
        try{
            System.out.println("Starting Consumer with id: " + consumerId);
            while (true) {
                if (TerminationMessage.TERMINATE.toString().equals(messageBroker.consumeMessage(messageTypeToConsume))) {
                    break;
                }
            }
            System.out.println("Terminating Consumer with id: " + consumerId);
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
