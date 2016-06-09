package com.dockerconsumercompiler.services;

import com.dockerconsumercompiler.vo.ProgramEntity;

import java.io.IOException;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public interface IProgramExecutor {
    public void executeProgram() throws IOException;
}
