package main.java.com.company.messagebroker.core;

import main.java.com.company.messagebroker.utils.MessageType;

/**
 * A simple message broker that enables publishers and consumers to communicate in a
 thread safe manner
 *
 * All implementations of this interface should be thread safe
 *
 */
public interface MessageBroker {
    /**
     * Enables to publish a message to the broker
     *
     * @param messageType the business type of message
     * @param message the message
     * @param <T> data type of the message (String, Ingteger ...)
     */
    <T> void publishMessage(MessageType messageType, T message);
    /**
     * Enables to retrieve the published message count
     *
     * @return the current count of messages that were published during the lifetime
    of the broker
     *
     */
    int publishedMessagesCount();
    /**
     * Enables to retrieve the consumed message count
     *
     * @return the current count of messages that were consumed during the lifetime of
    the broker
     */
    int consumedMessagesCount();
    /**
     * Enables to retrieve the un-consumed message count
     *
     * @return the current count of un-consumed messages
     */
    int unConsumedMessages();
    /**
     * Enables to retrieve the latest message published for the given {@MessageType}
     *
     * @param messageType the latest message to get for the given messageType
     * @param <T> the message data type
     * @return the latest message in the broker for that message type
     */
    <T> T consume(MessageType messageType);
}
