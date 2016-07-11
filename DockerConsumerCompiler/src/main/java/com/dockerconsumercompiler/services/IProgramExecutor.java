package com.dockerconsumercompiler.services;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by Manohar Prabhu on 6/9/2016.
 */
public abstract class IProgramExecutor {
	private Logger logger = Logger.getLogger(IProgramExecutor.class.getName());
	
	abstract public boolean preCompile();
	
	abstract public boolean compile();
	
	abstract public boolean runProgram();
	
    public void executeProgram() throws IOException {
    	if(preCompile()) {
    		logger.info("Program precompilation stage successful");
    		if(compile()) {
    			logger.info("Program compilation stage successful");
    			if(runProgram()) {
    				logger.info("Program successfully run");
    			} else {
    				logger.info("Program did not run successfully");
    			}
    		} else {
    			logger.info("Program compilation failed");
    		}
    	} else {
    		logger.info("Program precompilation failed");
    	}
    }
}
