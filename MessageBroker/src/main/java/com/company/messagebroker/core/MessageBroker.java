package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MessageBroker implements IMessageBroker {
    private int publishedMessagesCount;
    private int consumedMessagesCount;
    private ReentrantLock reentrantLock;
    private Map<MessageType, List> messages;
    private Map<MessageType, Condition> lockConditions;

    public MessageBroker() {
        reentrantLock = new ReentrantLock(true);
        lockConditions = new EnumMap<>(MessageType.class);
        messages = new EnumMap<>(MessageType.class);

        for (MessageType messageType : MessageType.values()) {
            messages.put(messageType, new ArrayList<>());
            lockConditions.put(messageType, reentrantLock.newCondition());
        }
    }

    public <T> void publishMessage(MessageType messageType, T message) {
        reentrantLock.lock();
        try {
            if (!message.equals(TerminationMessage.TERMINATE.toString())) {
                messages.get(messageType).add(new Message<T>(messageType, message));
                publishedMessagesCount++;
            } else {
                messages.get(messageType).add(0, new Message<T>(messageType, message));
            }

            lockConditions.get(messageType).signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    public <T> T consumeMessage(MessageType messageType) throws InterruptedException {
        reentrantLock.lock();
        try {
            List<Message> messagesForMessageType = messages.get(messageType);

            while (messagesForMessageType.isEmpty()) {
                lockConditions.get(messageType).await();
            }

            int consumedMessageIndex = messagesForMessageType.size() - 1;
            T messageData = (T) messagesForMessageType.get(consumedMessageIndex).getMessagePayload();
            messagesForMessageType.remove(consumedMessageIndex);

            if (!messageData.equals(TerminationMessage.TERMINATE.toString())) {
                consumedMessagesCount++;
            }

            return messageData;
        } finally {
            reentrantLock.unlock();
        }
    }

    public int publishedMessagesCount() {
        return publishedMessagesCount;
    }

    public int consumedMessagesCount() {
        return consumedMessagesCount;
    }

    public int unConsumedMessagesCount() {
        int unconsumedMessagesCount = 0;

        for (MessageType messageType : MessageType.values()) {
            unconsumedMessagesCount += messages.get(messageType).size();
        }

        return unconsumedMessagesCount;
    }

    public void clearMessageBroker() {
        messages.clear();
        lockConditions.clear();
    }

    public void displayStatus() {

    }

}
