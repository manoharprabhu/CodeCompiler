package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

        DefaultExecutor defaultExecutor = new DefaultExecutor();

        //Create a temp directory of name queueID
        CommandLine makeDirectory = new CommandLine("mkdir");
        makeDirectory.addArgument(message);
        defaultExecutor.execute(makeDirectory);


        //write the program and input file into this
        PrintWriter programWriter = new PrintWriter(message + File.separator + "program");
        programWriter.write(programEntity.getProgram());
        programWriter.close();
        PrintWriter inputWriter = new PrintWriter(message + File.separator + "input");
        inputWriter.write(programEntity.getInput());
        inputWriter.close();

        //Compile the program

        //If compilation succeeds, run the binary and pipe the input into it

        //Collect output into a file

        //Kill the process after T seconds if the process is still running

        //Update the database with the output after execution
    }
}
