package test.java.com.company.mesagebroker.service;

import main.java.com.company.messagebroker.core.*;
import main.java.com.company.messagebroker.utils.MessageType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import static org.junit.Assert.*;

public class MessageBrokerServiceTest {
    private MessagePublisher messagePublisher;
    private MessageConsumer messageConsumer;
    private MessageBroker messageBroker;

    @Before
    public void setUp() {
        messageBroker = new MessageBroker();
        messagePublisher = new MessagePublisher(21, MessageType.BUY_ORDER,24, messageBroker);
        messageConsumer = new MessageConsumer(43, MessageType.SELL_ORDER, messageBroker);
    }

    @After
    public void cleanUp() {
        messageBroker.clearMessageBroker();
        messageBroker = null;
        messagePublisher = null;
        messageConsumer = null;
    }

    @Test
    public void testMessageProducerInitialization() {
        assertEquals(21, messagePublisher.getPublisherId());
        assertEquals(MessageType.BUY_ORDER, messagePublisher.getMessageTypeToPublish());
        assertEquals(24, messagePublisher.getNumOfMessagesToPublish());

        messagePublisher.setPublisherId(121);
        assertEquals(121, messagePublisher.getPublisherId());

        messagePublisher.setNumOfMessagesToPublish(67892);
        assertEquals(67892, messagePublisher.getNumOfMessagesToPublish());

        messagePublisher.setMessageTypeToPublish(MessageType.ORDER_CONFIRMED);
        assertEquals(MessageType.ORDER_CONFIRMED, messagePublisher.getMessageTypeToPublish());
    }

    @Test
    public void testMessageConsumerInitialization() {
        assertEquals(43, messageConsumer.getConsumerId());
        assertEquals(MessageType.SELL_ORDER, messageConsumer.getMessageTypeToConsume());

        messageConsumer.setConsumerId(81);
        assertEquals(81, messageConsumer.getConsumerId());

        messageConsumer.setMessageTypeToConsume(MessageType.ORDER_CONFIRMED);
        assertEquals(MessageType.ORDER_CONFIRMED, messageConsumer.getMessageTypeToConsume());
    }

    @Test
    public void testMessageInitilization() {
        String messagePayload = "MyMessagePayload";
        Message message = new Message(MessageType.BUY_ORDER, messagePayload);
        assertEquals(MessageType.BUY_ORDER, message.getMessageType());
        assertEquals(messagePayload, message.getMessagePayload());
    }

    @Test
    public void testMessageProducerGenerateRandomMessagePayload() {
        int messagePayloadLength = 20;
        assertEquals(messagePayloadLength, messagePublisher.generateRandomMessagePayload(messagePayloadLength).length());
    }

    @Test
    public void testMessageBrokerPublish() {
        String messageDataOfPublishedMessage = "MyMessage";
        messageBroker.publishMessage(MessageType.BUY_ORDER, messageDataOfPublishedMessage);
        assertEquals(1, messageBroker.unConsumedMessagesCount());
        assertEquals(1, messageBroker.publishedMessagesCount());
        assertEquals(0, messageBroker.consumedMessagesCount());
    }

    @Test
    public void testMessageBrokerConsume() throws InterruptedException {
        String messageDataOfPublishedMessage = "MyMessage";
        messageBroker.publishMessage(MessageType.BUY_ORDER, messageDataOfPublishedMessage);

        String messageDataOfConsumedMessage = messageBroker.consumeMessage(MessageType.BUY_ORDER);
        assertEquals(messageDataOfPublishedMessage, messageDataOfConsumedMessage);
        assertEquals(0, messageBroker.unConsumedMessagesCount());
        assertEquals(1, messageBroker.publishedMessagesCount());
        assertEquals(1, messageBroker.consumedMessagesCount());
    }

}
