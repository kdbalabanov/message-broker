package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;
import main.java.com.company.messagebroker.utils.TerminationMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * The MessageBroker acts as a thread-safe buffer between MessagePublisher and MessageConsumer
 */
public class MessageBroker implements IMessageBroker {
    private int publishedMessagesCount;
    private int consumedMessagesCount;
    // Use of ReentrantLock in order to make consuming and producing of messages thread-safe
    private final ReentrantLock reentrantLock;
    // Messages are stored in an EnumMap where the MessageType is the key and
    private final Map<MessageType, List> messages;
    // Thread Conditions (one per MessageType) that ensure that MessageConsumer will wait when there are no available messages for its MessageType
    private final Map<MessageType, Condition> lockConditions;

    /**
     * Constructor for MessageBroker
     */
    public MessageBroker() {
        reentrantLock = new ReentrantLock(true);
        lockConditions = new EnumMap<>(MessageType.class);
        messages = new EnumMap<>(MessageType.class);

        for (MessageType messageType : MessageType.values()) {
            messages.put(messageType, new ArrayList<>());
            lockConditions.put(messageType, reentrantLock.newCondition());
        }
    }

    /**
     * Method used by MessagePublisher to publish a Message of specific MessageType
     *
     * Once the MethodPublisher is done publishing its messages, it will publish additional Message
     * with a String pay load of TerminationMessage.TERMINATE
     *
     * This TerminationMessage will be inserted at the beginning of the queue of messages so that
     * it gets processed last
     *
     * This will be consumed by a MessageConsumer which in turn will recognise this as a signal to terminate
     *
     * Every time a Message of a specific MessageType is published, a signal is sent to the MessageConsumer/s
     * waiting for a Message of of that specific MessageType to be available
     *
     * @param messageType the MessageType
     * @param message the Message
     * @param <T> the Message pay load of type T
     */
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

    /**
     * Method used by MessageConsumer to consume a Message of specific MessageType
     *
     * If there are no messages for the corresponding MessageType, then the MessageConsumer will wait
     * until it receives a signal that a Message of the corresponding MessageType is added
     *
     * Consumed messages are removed from the end of the queue
     *
     * @param messageType the MessageType of the Message to be consumed
     * @param <T> the type of the payload of the consumed Message
     * @return the Message payload of type T
     * @throws InterruptedException
     */
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

    public synchronized int publishedMessagesCount() {
        return publishedMessagesCount;
    }

    public synchronized int consumedMessagesCount() {
        return consumedMessagesCount;
    }

    public synchronized int unConsumedMessagesCount() {
        int unconsumedMessagesCount = 0;

        for (MessageType messageType : MessageType.values()) {
            unconsumedMessagesCount += messages.get(messageType).size();
        }

        return unconsumedMessagesCount;
    }

}
