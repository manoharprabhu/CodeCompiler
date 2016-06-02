package com.codecompiler.services;

import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
public class MessageReceiverService {
    Logger logger = Logger.getLogger(MessageReceiverService.class.getName());

    public void receiveMessage(String message) {
        logger.info("Received message ID " + message + " from the producer");
    }
}
