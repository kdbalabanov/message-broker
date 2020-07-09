package main.java.com.company.messagebroker.service;

import main.java.com.company.messagebroker.core.MessageBroker;
import main.java.com.company.messagebroker.core.MessageConsumer;
import main.java.com.company.messagebroker.core.MessagePublisher;
import main.java.com.company.messagebroker.utils.MessageType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MessageBrokerService {
    private static MessageBrokerService instance;
    private MessageBroker messageBroker;
    private ExecutorService executorService;
    private static final int numTasks = 3;
    private static final long numMessagesToProduce = 1000;

    public static MessageBrokerService getInstance() {
        if (instance == null) {
            instance = new MessageBrokerService();
        }
        return instance;
    }

    public void run() throws InterruptedException {
        init();

        for (int i = 1; i <= numTasks; i++) {
            MessageType messageType = MessageType.getRandom();
            executorService.execute(new MessagePublisher(i, messageType, numMessagesToProduce, messageBroker));
            executorService.execute(new MessageConsumer(i, messageType, messageBroker));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        System.out.println("Published messages: " + messageBroker.consumedMessagesCount());
        System.out.println("Consumed messages: " + messageBroker.consumedMessagesCount());
        System.out.println("Unconsumed messages: " + messageBroker.unConsumedMessagesCount());
    }

    private void init() throws InterruptedException {
        messageBroker = new MessageBroker();
        int numCores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numCores);
    }
}
