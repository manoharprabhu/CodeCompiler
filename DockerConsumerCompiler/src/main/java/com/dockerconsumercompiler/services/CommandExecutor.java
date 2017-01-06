package com.dockerconsumercompiler.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
	private DefaultExecutor defaultExecutor;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public boolean isGccInstalled() {
		try {
			if (defaultExecutor.execute(new CommandLine("gcc").addArgument("-v")) != 0) {
				logger.error("GCC is not installed");
				return false;
			}
		} catch (IOException e) {
			logger.error("Error while checking GCC version", e);
			return false;
		}
		return true;
	}

	public boolean isNodeInstalled() {
		try {
			if (defaultExecutor.execute(new CommandLine("node").addArgument("-v")) != 0) {
				logger.error("node is not installed");
				return false;
			}
		} catch (IOException e) {
			logger.error("Error while checking nodejs version", e);
			return false;
		}
		return true;
	}

	public boolean createTempDirectoryIfNotExists(String queueId) {
		if (!Files.exists(Paths.get(queueId))) {
			// Create a temp directory of name queueID
			CommandLine makeDirectory = new CommandLine("mkdir");
			makeDirectory.addArgument(queueId);
			try {
				defaultExecutor.execute(makeDirectory);
			} catch (IOException e) {
				logger.error("Error while creating the temp directory", e);
				return false;
			}
		}
		return true;
	}

	public boolean writeCProgramToTempDirectory(String queueId, String programName, String programCode) {
		PrintWriter programWriter = null;
		try {
			programWriter = new PrintWriter(queueId + File.separator + programName);
		} catch (FileNotFoundException e) {
			logger.error("Unable to open the program file", e);
			return false;
		}
		programWriter.write(programCode);
		programWriter.close();
		return true;
	}

	public boolean compileCProgramAndGenerateBinary(String queueId, String programName) {
		CommandLine compiler = CommandLine.parse(
				"gcc -x c " + queueId + File.separator + programName + " -o " + queueId + File.separator + "a.out");
		try {
			defaultExecutor.execute(compiler);
		} catch (ExecuteException e) {
			logger.error("Compilation was UNSUCCESSFUL", e);
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			logger.error("Unable to execute the command", e);
			return false;
		}

		return true;
	}

	public void setDefaultExecutor(DefaultExecutor defaultExecutor) {
		this.defaultExecutor = defaultExecutor;
	}
}
