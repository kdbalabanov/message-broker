package main.java.com.company.messagebroker;

import main.java.com.company.messagebroker.service.MessageBrokerService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageBrokerApplication {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

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
