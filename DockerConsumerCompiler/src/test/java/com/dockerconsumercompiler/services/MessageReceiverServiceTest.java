package com.dockerconsumercompiler.services;

import java.io.IOException;
import java.util.Date;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.codecompiler.vo.ProgramEntity;

public class MessageReceiverServiceTest {
	
	private static ProgramRepository programRepository;
	private static ProgramExecutorFactory programExecutorFactory;
	
	@BeforeClass
	public static void init() {
		 programRepository = Mockito.mock(ProgramRepository.class);
		 programExecutorFactory = Mockito.mock(ProgramExecutorFactory.class);
	}
	
	@Test
	public void testReceiveMessageForCProgram() throws IOException {
		AbstractProgramExecutor abstractProgramExecutor = Mockito.mock(AbstractProgramExecutor.class);
		Mockito.when(programRepository.findByQueueId("queueId")).thenReturn(mockProgramEntity("c"));
		Mockito.when(programExecutorFactory.getCProgramExecutor(Mockito.anyString(), Mockito.any(ProgramEntity.class), Mockito.any(ProgramRepository.class))).thenReturn(abstractProgramExecutor);
		MessageReceiverService messageReceiverService = new MessageReceiverService(programRepository, programExecutorFactory);
		messageReceiverService.receiveMessage("queueId");
		Mockito.verify(abstractProgramExecutor, Mockito.times(1)).executeProgram();
		Assert.assertTrue(true);
	}
	
	@Test
	public void testReceiveMessageForJSProgram() throws IOException {
		AbstractProgramExecutor abstractProgramExecutor = Mockito.mock(AbstractProgramExecutor.class);
		Mockito.when(programRepository.findByQueueId("queueId")).thenReturn(mockProgramEntity("js"));
		Mockito.when(programExecutorFactory.getJSProgramExecutor(Mockito.anyString(), Mockito.any(ProgramEntity.class), Mockito.any(ProgramRepository.class))).thenReturn(abstractProgramExecutor);
		MessageReceiverService messageReceiverService = new MessageReceiverService(programRepository, programExecutorFactory);
		messageReceiverService.receiveMessage("queueId");
		Mockito.verify(abstractProgramExecutor, Mockito.times(1)).executeProgram();
		Assert.assertTrue(true);
	}
	
	@Test
	public void testReceiveMessageForUnknownProgram() throws IOException {
		AbstractProgramExecutor abstractProgramExecutor = Mockito.mock(AbstractProgramExecutor.class);
		MessageReceiverService messageReceiverService = new MessageReceiverService(programRepository, programExecutorFactory);
		messageReceiverService.receiveMessage("queueId");
		Mockito.verify(abstractProgramExecutor, Mockito.times(0)).executeProgram();
		Assert.assertTrue(true);
	}
	
	private ProgramEntity mockProgramEntity(String language) {
		ProgramEntity programEntity = new ProgramEntity();
		programEntity.setId("id");
		programEntity.setInput("");
		programEntity.setLanguage(language);
		programEntity.setExecutionTimeLimit(1);
		programEntity.setErrorMessage(null);
		programEntity.setOutput(null);
		programEntity.setProgram("program");
		programEntity.setProgramStatus(6);
		programEntity.setQueuedTime(new Date());
		programEntity.setQueueId("queueId");
		return programEntity;
	}

}
