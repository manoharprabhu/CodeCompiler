package com.dockerconsumercompiler.services;

import com.codecompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class ProgramExecutorFactory {
    private ProgramExecutorFactory(){}

    public static AbstractProgramExecutor getCProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        return new CProgramExecutor(message, programEntity, programRepository);
    }
    
    public static AbstractProgramExecutor getJSProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        return new JavascriptExecutor(message, programEntity, programRepository);
    }
}
