package main.java.com.company.messagebroker.service;

import main.java.com.company.messagebroker.core.IMessageBroker;
import main.java.com.company.messagebroker.core.MessageBroker;
import main.java.com.company.messagebroker.core.MessageConsumer;
import main.java.com.company.messagebroker.core.MessagePublisher;
import main.java.com.company.messagebroker.utils.MessageType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The MessageBrokerService glues everything together and runs it
 * It is a Singleton - initialised only once
 *
 * Each MessagePublisher/MessageConsumer is treated as a Task
 *
 * Tasks are executed by a FixedThreadPool, the size of which is determined by the number of logical CPU cores
 * This limits the excessive creation of threads which can be expensive
 */
public class MessageBrokerService {
    private static MessageBrokerService instance;
    private final IMessageBroker messageBroker;
    private final ExecutorService executorService;
    private static final int numTasks = 150;
    private static final long numMessagesToProduce = 1000;
    private static final int timeOutMinutes = 1;

    /**
     * Private constructor for MessageBrokerService
     */
    private MessageBrokerService() {
        messageBroker = new MessageBroker();
        int numCores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numCores);
    }

    /**
     * Initialise only one instance of MessageBrokerService
     * @return the instance
     */
    public static synchronized MessageBrokerService getInstance() {
        if (instance == null) {
            instance = new MessageBrokerService();
        }
        return instance;
    }

    /**
     * Executes the specified number of tasks using a thread pool of fixed size which is determined by the
     * number of logical CPU cores
     *
     * The following assumptions are made based on the requirements:
     * 1. There is an equal number of MessagePublishers and MessageConsumers
     * 2. For each MessagePublisher publishing messages of a specifc MessageType, there is a matching
     * MessageConsumer that consumes messages of that MessageType
     * 3. There is no limit on the amount of messages that can be stored in the buffer/MessageBroker
     *
     * These assumptions have led to the current implementation where graceful termination of MessageConumer/s
     * is achieved by the MessagePublisher/s sending a TerminationMessage once the target of number of messages
     * to publish is reached
     *
     * @throws Exception
     */
    public void run() throws Exception {
        for (int i = 1; i <= numTasks; i++) {
            MessageType messageType = MessageType.getRandom();
            executorService.execute(new MessagePublisher(i, messageType, numMessagesToProduce, messageBroker));
            executorService.execute(new MessageConsumer(i, messageType, messageBroker));
        }

        executorService.shutdown();
        // Force a timeout if the application can not finish the tasks for longer than the specified time (in minutes)
        executorService.awaitTermination(timeOutMinutes, TimeUnit.MINUTES);

        System.out.println("Published messages: " + messageBroker.consumedMessagesCount());
        System.out.println("Consumed messages: " + messageBroker.consumedMessagesCount());
        System.out.println("Unconsumed messages: " + messageBroker.unConsumedMessagesCount());
    }

}
