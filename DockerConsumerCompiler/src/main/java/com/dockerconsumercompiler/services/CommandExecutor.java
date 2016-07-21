package com.dockerconsumercompiler.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.springframework.stereotype.Component;

@Component
public class CommandExecutor {
	private DefaultExecutor defaultExecutor;
	private Logger logger = Logger.getLogger(CommandExecutor.class.getName());

	public boolean isGccInstalled() {
		try {
			if (defaultExecutor.execute(new CommandLine("gcc").addArgument("-v")) != 0) {
				logger.info("GCC is not installed");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean isNodeInstalled() {
		try {
			if (defaultExecutor.execute(new CommandLine("node").addArgument("-v")) != 0) {
				logger.info("node is not installed");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
				logger.info("Error while creating the temp directory");
				e.printStackTrace();
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
			logger.info("Unable to open the program file");
			e.printStackTrace();
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
			logger.info("Compilation was UNSUCCESSFUL");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			logger.info("Unable to execute the command");
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void setDefaultExecutor(DefaultExecutor defaultExecutor) {
		this.defaultExecutor = defaultExecutor;
	}
}
