package com.dockerconsumercompiler.services;

import java.io.IOException;
import java.util.logging.Logger;

import com.codecompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public abstract class AbstractProgramExecutor implements Runnable {
	private Logger logger = Logger.getLogger(AbstractProgramExecutor.class.getName());

	abstract public boolean preCompile();

	abstract public boolean compile();

	abstract public boolean runProgram();

	public Thread executeProgram() throws IOException {
		Thread thread = new Thread(this);
		thread.start();
		return thread;
	}

	@Override
	public void run() {
		if (preCompile()) {
			logger.info("Program precompilation stage successful");
			if (compile()) {
				logger.info("Program compilation stage successful");
				if (runProgram()) {
					logger.info("Program successfully run");
				} else {
					logger.info("Program did not run successfully");
				}
			} else {
				logger.info("Program compilation failed");
			}
		} else {
			logger.info("Program precompilation failed");
		}
	}
	
	public void updateProgramEntity(ProgramEntity programEntity, String errorMessage, String output, int programStatus, ProgramRepository programRepository) {
		programEntity.setProgramStatus(programStatus);
		programEntity.setOutput(output);
		programEntity.setErrorMessage(errorMessage);
		programRepository.save(programEntity);
	}
}
