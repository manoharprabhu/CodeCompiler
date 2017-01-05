package com.dockerconsumercompiler.services;

import org.apache.commons.exec.DefaultExecutor;
import org.springframework.stereotype.Component;

import com.codecompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */

@Component
public class ProgramExecutorFactory {

    public AbstractProgramExecutor getCProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository, CommandExecutor commandExecutor) {
        commandExecutor.setDefaultExecutor(new DefaultExecutor());
        return new CProgramExecutor(message, programEntity, programRepository, commandExecutor);
    }
    
    public AbstractProgramExecutor getJSProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository, CommandExecutor commandExecutor) {
        commandExecutor.setDefaultExecutor(new DefaultExecutor());
        return new JavascriptExecutor(message, programEntity, programRepository, commandExecutor);
    }
}
