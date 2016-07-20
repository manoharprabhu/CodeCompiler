package com.dockerconsumercompiler.services;

import org.junit.Assert;
import org.junit.Test;

public class MessageTest {
	
	@Test
	public void verifyMessages() {
		Assert.assertEquals("Compilation error", Messages.COMPILATION_ERROR);
		Assert.assertEquals("Program did not complete execution in time", Messages.DID_NOT_EXECUTE_IN_TIME);
		Assert.assertEquals("Non-zero exit status code. Make sure your program returns 0", Messages.NON_ZERO_EXIT_STATUS);
	}

}
