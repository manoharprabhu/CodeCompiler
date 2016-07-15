package com.codecompiler.controllers;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.codecompiler.services.CodeCompilerService;
import com.codecompiler.vo.ProgramStatusResponse;
import com.codecompiler.vo.ProgramSubmitResponse;
import com.codecompiler.vo.Response;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CodeCompilerControllerTest {

    @Mock
    private CodeCompilerService codeCompilerService;

    @InjectMocks
    private CodeCompilerController codeCompilerController = new CodeCompilerController();

    @Test
    public void testSubmitProgram() {
        Mockito.when(codeCompilerService.submitProgram("program", "input", 2 ,"c")).thenReturn(mockSubmitResponse());
	    Response<ProgramSubmitResponse> response = codeCompilerController.submitProgram("program", "input", 2, "c");
        Mockito.verify(codeCompilerService, Mockito.times(1)).submitProgram("program", "input", 2 ,"c");
        Assert.assertEquals("queueId", response.getData().getQueueId());	
    }

    @Test
    public void testCheckProgramStatus() {
    	Mockito.when(codeCompilerService.checkProgramStatus("queueId")).thenReturn(mockStatusResponse());
    	Response<ProgramStatusResponse> response = codeCompilerController.checkProgramStatus("queueId");
        Mockito.verify(codeCompilerService, Mockito.times(1)).checkProgramStatus("queueId");
        Assert.assertEquals(ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, response.getData().getProgramStatus());
    }
    
    private Response<ProgramSubmitResponse> mockSubmitResponse() {
    	ProgramSubmitResponse programSubmitResponse = new ProgramSubmitResponse("queueId");
    	Response<ProgramSubmitResponse> response = new Response<ProgramSubmitResponse>(programSubmitResponse);
    	return response;
    }
    
    private Response<ProgramStatusResponse> mockStatusResponse() {
    	Date date = new Date();
    	ProgramStatusResponse programStatusResponse = new ProgramStatusResponse(ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, "output", null, date);
    	Response<ProgramStatusResponse> response = new Response<ProgramStatusResponse>(programStatusResponse);
    	return response;
    }
}
