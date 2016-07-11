package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public class ProgramExecutorFactory {
    private ProgramExecutorFactory(){}

    public static AbstractProgramExecutor getCProgramExecutor(String message, ProgramEntity programEntity, ProgramRepository programRepository) {
        return new CProgramExecutor(message, programEntity, programRepository);
    }
}
