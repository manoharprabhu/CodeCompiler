package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;
import org.apache.commons.exec.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.file.FileSystem;
import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
public class MessageReceiverService {
    Logger logger = Logger.getLogger(MessageReceiverService.class.getName());
    private ProgramRepository programRepository;

    @Autowired
    public MessageReceiverService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public void receiveMessage(String message) throws IOException {
        logger.info("Received message ID " + message + " from the producer");
        ProgramEntity programEntity = programRepository.findByQueueId(message);
        if(programEntity == null) {
            return;
        }
        AbstractProgramExecutor executor = ProgramExecutorFactory.getCProgramExecutor(message, programEntity, programRepository);
        executor.executeProgram();
    }
}
