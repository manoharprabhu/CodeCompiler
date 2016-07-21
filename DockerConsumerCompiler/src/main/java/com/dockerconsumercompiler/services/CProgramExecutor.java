package com.dockerconsumercompiler.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;

import com.codecompiler.vo.ProgramEntity;
import com.codecompiler.vo.ProgramStatusResponse;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class CProgramExecutor extends AbstractProgramExecutor {
    private String message;
    private ProgramEntity programEntity;
    private ProgramRepository programRepository;
    private CommandExecutor commandExecutor;
    private final String PROGRAM_NAME = "program";
    private Logger logger = Logger.getLogger(CProgramExecutor.class.getName());

    public CProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository, CommandExecutor commandExecutor) {
        this.message = message;
        this.programEntity = programEntity;
        this.programRepository = programRepository;
        this.commandExecutor = commandExecutor;
    }
	@Override
	public boolean preCompile() {

        // Make sure GCC is installed for compiling C program
        try {
            if(!commandExecutor.isGccInstalled()) {
            	return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if(!commandExecutor.createTempDirectoryIfNotExists(message)) {
        	return false;
        }
        //write the program and input file into this
        if(!commandExecutor.writeCProgramToTempDirectory(message, PROGRAM_NAME, programEntity.getProgram())) {
        	return false;
        }
        // Set program status to PROGRAM_IS_GETTING_PROCESSED = 2;
        this.updateProgramEntity(programEntity, null, null, ProgramStatusResponse.PROGRAM_IS_GETTING_PROCESSED, programRepository);
        return true;
	}
	
	@Override
	public boolean compile() {
		//Compile the program
        if(!commandExecutor.compileCProgramAndGenerateBinary(message, PROGRAM_NAME)) {
        	//Update database status code to PROGRAM_COMPILATION_ERROR = 3;
            this.updateProgramEntity(programEntity, Messages.COMPILATION_ERROR, null, ProgramStatusResponse.PROGRAM_COMPILATION_ERROR, programRepository);
            return false;
        } else {
        	return true;
        }
	}
	
	@Override
	public boolean runProgram() {
		//If compilation succeeds, run the binary and pipe the input into it
        CommandLine executorCommand = CommandLine.parse(message + File.separator + "a.out");
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
        ExecuteWatchdog watchdog = new ExecuteWatchdog(programEntity.getExecutionTimeLimit() > 0
                ? (programEntity.getExecutionTimeLimit()*1000)
                : 2000);
        timedExecutor.setWatchdog(watchdog);
        try {
            timedExecutor.setStreamHandler(new PumpStreamHandler(output, null, input));
            timedExecutor.execute(executorCommand);
            this.updateProgramEntity(programEntity, null, output.toString(), ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, programRepository);
        } catch(ExecuteException e) {
            logger.info("Non-zero exit value. The program crashed \\ timedout");
            e.printStackTrace();
            if (watchdog.killedProcess()) {
                // Update database status code to PROGRAM_EXECUTION_TIMEOUT = 4;
            	this.updateProgramEntity(programEntity, Messages.DID_NOT_EXECUTE_IN_TIME, null, ProgramStatusResponse.PROGRAM_EXECUTION_TIMEOUT, programRepository);
            } else {
                // Update database status code to PROGRAM_RUNTIME_ERROR = 5;
            	this.updateProgramEntity(programEntity, Messages.NON_ZERO_EXIT_STATUS, null, ProgramStatusResponse.PROGRAM_RUNTIME_ERROR, programRepository);
            }
            return false;
        } catch (IOException e) {
            logger.info("Program did not accept input");
            this.updateProgramEntity(programEntity, null, output.toString(), ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, programRepository);
            return true;
        }
        return true;
	}
	
}
