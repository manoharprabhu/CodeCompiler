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
	private ProgramExecutorFactory programExecutorFactory;
	private CommandExecutor commandExecutor;

	@Autowired
	public MessageReceiverService(ProgramRepository programRepository, ProgramExecutorFactory programExecutorFactory,
			CommandExecutor commandExecutor) {
		this.programRepository = programRepository;
		this.programExecutorFactory = programExecutorFactory;
		this.commandExecutor = commandExecutor;
	}

	public void receiveMessage(String message) throws IOException {
		logger.info("Received message ID " + message + " from the producer");
		ProgramEntity programEntity = programRepository.findByQueueId(message);
		if (programEntity == null) {
			logger.info("Program corresponding to the given queue ID does not exist.");
			return;
		}
		AbstractProgramExecutor executor = null;
		if ("c".equals(programEntity.getLanguage())) {
			logger.info("Selected C executor");
			executor = programExecutorFactory.getCProgramExecutor(message, programEntity, programRepository,
					commandExecutor);
		} else if ("js".equals(programEntity.getLanguage())) {
			logger.info("Selected JS executor");
			executor = programExecutorFactory.getJSProgramExecutor(message, programEntity, programRepository,
					commandExecutor);
		}
		executor.executeProgram();
	}
}
