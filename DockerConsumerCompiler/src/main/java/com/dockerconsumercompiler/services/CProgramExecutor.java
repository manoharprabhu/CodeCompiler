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

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class CProgramExecutor extends AbstractProgramExecutor {
    private String message;
    private ProgramEntity programEntity;
    private ProgramRepository programRepository;
    private Logger logger = Logger.getLogger(CProgramExecutor.class.getName());

    public CProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        this.message = message;
        this.programEntity = programEntity;
        this.programRepository = programRepository;
    }
	@Override
	public boolean preCompile() {
		DefaultExecutor defaultExecutor = new DefaultExecutor();

        // Make sure GCC is installed for compiling C program
        try {
            if(defaultExecutor.execute(new CommandLine("gcc").addArgument("-v")) != 0) {
                logger.info("GCC is not installed");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if(!Files.exists(Paths.get(message))) {
            //Create a temp directory of name queueID
            CommandLine makeDirectory = new CommandLine("mkdir");
            makeDirectory.addArgument(message);
            try {
                defaultExecutor.execute(makeDirectory);
            } catch (IOException e) {
                logger.info("Error while running the command");
                e.printStackTrace();
                return false;
            }
        }

        //write the program and input file into this
        PrintWriter programWriter = null;
        try {
            programWriter = new PrintWriter(message + File.separator + "program");
        } catch (FileNotFoundException e) {
            logger.info("Unable to open the program file");
            e.printStackTrace();
            return false;
        }
        programWriter.write(programEntity.getProgram());
        programWriter.close();

        // Set program status to PROGRAM_IS_GETTING_PROCESSED = 2;
        this.updateProgramEntity(programEntity, null, null, ProgramStatusResponse.PROGRAM_IS_GETTING_PROCESSED);
        return true;
	}
	
	@Override
	public boolean compile() {
		DefaultExecutor defaultExecutor = new DefaultExecutor();

		//Compile the program
        CommandLine compiler = CommandLine.parse("gcc -x c " + message + File.separator + "program" + " -o "+ message + File.separator + "a.out");
        try {
            defaultExecutor.execute(compiler);
        } catch (ExecuteException e) {
            logger.info("Compilation was UNSUCCESSFUL");
            e.printStackTrace();
            // Update database status code to PROGRAM_COMPILATION_ERROR = 3;
            this.updateProgramEntity(programEntity, Messages.COMPILATION_ERROR, null, ProgramStatusResponse.PROGRAM_COMPILATION_ERROR);
            return false;
        } catch (IOException e) {
            logger.info("Unable to execute the command");
            e.printStackTrace();
            return false;
        }
        
        return true;
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
            this.updateProgramEntity(programEntity, null, output.toString(), ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY);
        } catch(ExecuteException e) {
            logger.info("Non-zero exit value. The program crashed \\ timedout");
            e.printStackTrace();
            if (watchdog.killedProcess()) {
                // Update database status code to PROGRAM_EXECUTION_TIMEOUT = 4;
            	this.updateProgramEntity(programEntity, Messages.DID_NOT_EXECUTE_IN_TIME, null, ProgramStatusResponse.PROGRAM_EXECUTION_TIMEOUT);
            } else {
                // Update database status code to PROGRAM_RUNTIME_ERROR = 5;
            	this.updateProgramEntity(programEntity, Messages.NON_ZERO_EXIT_STATUS, null, ProgramStatusResponse.PROGRAM_RUNTIME_ERROR);
            }
            return false;
        } catch (IOException e) {
            logger.info("Program did not accept input");
            this.updateProgramEntity(programEntity, null, output.toString(), ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY);
            return true;
        }
        return true;
	}
	
	public void updateProgramEntity(ProgramEntity programEntity, String errorMessage, String output, int programStatus) {
		programEntity.setProgramStatus(programStatus);
		programEntity.setOutput(output);
		programEntity.setErrorMessage(errorMessage);
		programRepository.save(programEntity);
	}
}
