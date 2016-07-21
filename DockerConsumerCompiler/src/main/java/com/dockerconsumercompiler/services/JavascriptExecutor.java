package com.dockerconsumercompiler.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;

public class JavascriptExecutor extends AbstractProgramExecutor {

	private String message;
	private ProgramEntity programEntity;
	private ProgramRepository programRepository;
	private CommandExecutor commandExecutor;
	private static final String PROGRAM_NAME = "program.js";
	private Logger logger = Logger.getLogger(JavascriptExecutor.class.getName());

	public JavascriptExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository,
			CommandExecutor commandExecutor) {
		this.message = message;
		this.programEntity = programEntity;
		this.programRepository = programRepository;
		this.commandExecutor = commandExecutor;
	}

	@Override
	public boolean preCompile() {

		// Make sure node is installed
		if (!commandExecutor.isNodeInstalled()) {
			return false;
		}

		if (!commandExecutor.createTempDirectoryIfNotExists(message)) {
			return false;
		}

		// write the program and input file into this
		if (!commandExecutor.writeCProgramToTempDirectory(message, PROGRAM_NAME, programEntity.getProgram())) {
			return false;
		}

		// Set program status to PROGRAM_IS_GETTING_PROCESSED = 2;
		this.updateProgramEntity(programEntity, null, null, ProgramStatusResponse.PROGRAM_IS_GETTING_PROCESSED,
				programRepository);
		return true;
	}

	@Override
	public boolean compile() {
		// No compilation phase in javascript
		return true;
	}

	@Override
	public boolean runProgram() {
		CommandLine executorCommand = CommandLine.parse("node " + message + File.separator + "program.js");
		ByteArrayInputStream input = null;
		try {
			input = new ByteArrayInputStream(programEntity.getInput().getBytes("ISO-8859-1"));
		} catch (UnsupportedEncodingException e) {
			logger.info("Unable to pipe input into the program");
			e.printStackTrace();
			return false;
		}
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		DefaultExecutor timedExecutor = new DefaultExecutor();
		timedExecutor.setExitValue(0);
		ExecuteWatchdog watchdog = new ExecuteWatchdog(
				programEntity.getExecutionTimeLimit() > 0 ? (programEntity.getExecutionTimeLimit() * 1000) : 2000);
		timedExecutor.setWatchdog(watchdog);
		try {
			timedExecutor.setStreamHandler(new PumpStreamHandler(output, null, input));
			timedExecutor.execute(executorCommand);
			this.updateProgramEntity(programEntity, null, output.toString(),
					ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, programRepository);
		} catch (ExecuteException e) {
			logger.info("Non-zero exit value. The program crashed \\ timedout");
			e.printStackTrace();
			if (watchdog.killedProcess()) {
				// Update database status code to PROGRAM_EXECUTION_TIMEOUT = 4;
				this.updateProgramEntity(programEntity, Messages.DID_NOT_EXECUTE_IN_TIME, null,
						ProgramStatusResponse.PROGRAM_EXECUTION_TIMEOUT, programRepository);
			} else {
				// Update database status code to PROGRAM_RUNTIME_ERROR = 5;
				this.updateProgramEntity(programEntity, Messages.NON_ZERO_EXIT_STATUS, null,
						ProgramStatusResponse.PROGRAM_RUNTIME_ERROR, programRepository);
			}
			return false;
		} catch (IOException e) {
			logger.info("Unable to execute the command");
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
