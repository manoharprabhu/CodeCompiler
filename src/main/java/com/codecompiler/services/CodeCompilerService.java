package com.codecompiler.services;

import com.codecompiler.configuration.RabbitMQConfiguration;
import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.Response;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
@Service
public class CodeCompilerService {

    Logger logger = Logger.getLogger(Logger.class.toString());
    private ProgramRepository programRepository;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public CodeCompilerService(ProgramRepository programRepository, RabbitTemplate rabbitTemplate) {
        this.programRepository = programRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Response<ProgramSubmitResponse> submitProgram(String program, String input, int timeout, String language) {
        if(!isLanguageValid(language))
            return new Response<ProgramSubmitResponse>(null);
        ProgramEntity programEntity = new ProgramEntity();
        ProgramSubmitResponse response = null;
        String uniqueId = generateUniqueID();
        programEntity.setProgram(program);
        programEntity.setInput(input);
        programEntity.setExecutionTimeLimit(timeout);
        programEntity.setQueuedTime(new Date());
        programEntity.setQueueId(uniqueId);
        programEntity.setLanguage(language);
        try {
            programRepository.save(programEntity);
            rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, uniqueId);
            response = new ProgramSubmitResponse(uniqueId);
        } catch(DataAccessResourceFailureException e) {
            logger.info("Database seems to be down: " + e.getLocalizedMessage());
        } catch (AmqpException e) {
            logger.info("Messaging queue seems to be down: " + e.getLocalizedMessage());
        }
        return new Response<ProgramSubmitResponse>(response);
    }

    public Response<ProgramStatusResponse> checkProgramStatus(String queueId) {
        ProgramEntity programEntity = null;
        try {
            programEntity = programRepository.findByQueueId(queueId);
        } catch(DataAccessResourceFailureException e) {
            logger.info("Database seems to be down: " + e.getLocalizedMessage());
        }
        ProgramStatusResponse response;
        if(programEntity == null) {
            response = new ProgramStatusResponse(ProgramStatusResponse.PROGRAM_NOT_FOUND, null, "Program not found", null);
        } else {
            response = new ProgramStatusResponse(programEntity.getProgramStatus(), programEntity.getOutput(), programEntity.getErrorMessage(), programEntity.getQueuedTime());
        }
        return new Response<ProgramStatusResponse>(response);
    }

    public static boolean isLanguageValid(String language) {
        return "c".equals(language);
    }

    public String generateUniqueID() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString();
    }

}
