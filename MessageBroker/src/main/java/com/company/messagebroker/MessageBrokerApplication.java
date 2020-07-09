package main.java.com.company.messagebroker;

import main.java.com.company.messagebroker.service.MessageBrokerService;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main starting point of the Application
 */
public class MessageBrokerApplication {
    private static final Logger LOGGER = Logger.getLogger(MessageBrokerApplication.class.getName());

    public static void main(String[] args) {
        try {
            MessageBrokerService.getInstance().run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            System.exit(-1);
        }

        System.exit(0);
    }
}
