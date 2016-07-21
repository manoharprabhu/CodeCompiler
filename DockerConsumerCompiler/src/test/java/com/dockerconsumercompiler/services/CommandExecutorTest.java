package com.dockerconsumercompiler.services;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class CommandExecutorTest {

	@Test
	public void testGccInstalled() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenReturn(0);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertTrue(commandExecutor.isGccInstalled());
	}

	@Test
	public void testGccNotInstalled() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenReturn(128);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertFalse(commandExecutor.isGccInstalled());
	}

	@Test
	public void testNodeInstalled() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenReturn(0);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertTrue(commandExecutor.isNodeInstalled());
	}

	@Test
	public void testNodeNotInstalled() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenReturn(128);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertFalse(commandExecutor.isNodeInstalled());
	}

	@Test
	public void testCompileCProgram() {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertTrue(commandExecutor.compileCProgramAndGenerateBinary("queueId", "program"));
	}

	@Test
	public void testCompileWrongCProgram() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenThrow(ExecuteException.class);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertFalse(commandExecutor.compileCProgramAndGenerateBinary("queueId", "program"));
	}

	@Test
	public void testCompileCProgramNoAccess() throws ExecuteException, IOException {
		DefaultExecutor defaultExecutor = Mockito.mock(DefaultExecutor.class);
		Mockito.when(defaultExecutor.execute(Mockito.any(CommandLine.class))).thenThrow(IOException.class);
		CommandExecutor commandExecutor = new CommandExecutor();
		commandExecutor.setDefaultExecutor(defaultExecutor);
		Assert.assertFalse(commandExecutor.compileCProgramAndGenerateBinary("queueId", "program"));
	}

}
