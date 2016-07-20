package com.dockerconsumercompiler.services;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.codecompiler.vo.ProgramEntity;

/**
 * Created by Manohar Prabhu on 7/20/2016.
 */
public class ProgramExecutorFactoryTest {
	
	ProgramExecutorFactory programExecutorFactory = new ProgramExecutorFactory();
	@Test
	public void testGetCProgramExecutor() {
		Assert.assertEquals(CProgramExecutor.class, programExecutorFactory.getCProgramExecutor("queueId", Mockito.mock(ProgramEntity.class), Mockito.mock(ProgramRepository.class)).getClass());
	}
	
	@Test
	public void testGetJSProgramExecutor() {
		Assert.assertEquals(JavascriptExecutor.class, programExecutorFactory.getJSProgramExecutor("queueId", Mockito.mock(ProgramEntity.class), Mockito.mock(ProgramRepository.class)).getClass());
	}
}
