package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class ProgramExecutorFactory {
    private ProgramExecutorFactory(){}

    public static IProgramExecutor getCProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        return new CProgramExecutorImpl(message, programEntity, programRepository);
    }
}
