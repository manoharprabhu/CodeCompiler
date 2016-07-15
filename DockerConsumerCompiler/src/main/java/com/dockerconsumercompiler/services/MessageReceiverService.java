package com.dockerconsumercompiler.services;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import com.codecompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
public class MessageReceiverService {
    private Logger logger = Logger.getLogger(MessageReceiverService.class.getName());
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
        AbstractProgramExecutor executor = null;
        if("c".equals(programEntity.getLanguage())) {
        	executor = ProgramExecutorFactory.getCProgramExecutor(message, programEntity, programRepository);
        } else if("js".equals(programEntity.getLanguage())) {
        	executor = ProgramExecutorFactory.getJSProgramExecutor(message, programEntity, programRepository);
        }
        executor.executeProgram();
    }
}
