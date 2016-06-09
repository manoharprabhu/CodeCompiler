package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;
import org.apache.commons.exec.*;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class CProgramExecutorImpl implements IProgramExecutor {
    private String message;
    private ProgramEntity programEntity;
    private ProgramRepository programRepository;
    private Logger logger = Logger.getLogger(CProgramExecutorImpl.class.getName());

    public CProgramExecutorImpl(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        this.message = message;
        this.programEntity = programEntity;
        this.programRepository = programRepository;
    }
    @Override
    public void executeProgram() throws IOException {
        DefaultExecutor defaultExecutor = new DefaultExecutor();

        //Create a temp directory of name queueID
        CommandLine makeDirectory = new CommandLine("mkdir");
        makeDirectory.addArgument(message);
        defaultExecutor.execute(makeDirectory);


        //write the program and input file into this
        PrintWriter programWriter = new PrintWriter(message + File.separator + "program");
        programWriter.write(programEntity.getProgram());
        programWriter.close();

        // Set program status to PROGRAM_IS_GETTING_PROCESSED = 2;
        programEntity.setProgramStatus(2);
        programRepository.save(programEntity);

        //Compile the program
        CommandLine compiler = CommandLine.parse("gcc -x c " + message + File.separator + "program" + " -o "+ message + File.separator + "a.out");
        try {
            defaultExecutor.execute(compiler);
        } catch (ExecuteException e) {
            logger.info("Compilation was UNSUCCESSFUL");
            e.printStackTrace();
            // Update database status code to PROGRAM_COMPILATION_ERROR = 3;
            programEntity.setProgramStatus(3);
            programEntity.setErrorMessage("Compilation error");
            programRepository.save(programEntity);
            return;
        }

        //If compilation succeeds, run the binary and pipe the input into it
        CommandLine executorCommand = CommandLine.parse(message + File.separator + "a.out");
        ByteArrayInputStream input =
                new ByteArrayInputStream(programEntity.getInput().getBytes("ISO-8859-1"));
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
            programEntity.setProgramStatus(6);
            programEntity.setOutput(output.toString());
            programRepository.save(programEntity);
        } catch(ExecuteException e) {
            logger.info("Non-zero exit value. The program crashed \\ timedout");
            e.printStackTrace();
            if (watchdog.killedProcess()) {
                // Update database status code to PROGRAM_EXECUTION_TIMEOUT = 4;
                programEntity.setProgramStatus(4);
                programEntity.setErrorMessage("Program did not complete execution in time");
            } else {
                // Update database status code to PROGRAM_RUNTIME_ERROR = 5;
                programEntity.setProgramStatus(5);
                programEntity.setErrorMessage("Non-zero exit status code. Make sure your program returns 0");
            }
            programRepository.save(programEntity);
            return;
        }
    }
}
