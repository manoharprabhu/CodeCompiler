package com.codecompiler.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.codecompiler.vo.RecentSubmissions;
import com.codecompiler.vo.Response;
import com.codecompiler.vo.Submission;

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
    
    @Test
    public void testPing() {
    	Assert.assertEquals(codeCompilerController.ping(), "pong");
    }
    
    @Test
    public void testRecent() {
    	Mockito.when(codeCompilerService.getRecentSubmissions(0, 5)).thenReturn(mockRecentSubmissions());
    	Response<RecentSubmissions> response = codeCompilerController.getRecentSubmissions(0, 5);
    	Assert.assertEquals(response.getData().getSubmissions().size(), 2);
    	Assert.assertEquals(response.getData().getSubmissions().get(0).getQueueId(), "queueID1");
    	Assert.assertEquals(response.getData().getSubmissions().get(1).getQueueId(), "queueID2");
    }
    
    private Response<ProgramSubmitResponse> mockSubmitResponse() {
    	ProgramSubmitResponse programSubmitResponse = new ProgramSubmitResponse("queueId");
    	Response<ProgramSubmitResponse> response = new Response<ProgramSubmitResponse>(programSubmitResponse);
    	return response;
    }
    
    private Response<RecentSubmissions> mockRecentSubmissions() {
    	Submission submission1 = new Submission("queueID1", new Date(), 6);
    	Submission submission2= new Submission("queueID2", new Date(), 6);
    	List<Submission> submissions = new ArrayList<Submission>();
    	submissions.add(submission1);
    	submissions.add(submission2);
    	RecentSubmissions recentSubmissions = new RecentSubmissions(submissions, -1, 1, 5);
    	return new Response<RecentSubmissions>(recentSubmissions);
    }
    
    private Response<ProgramStatusResponse> mockStatusResponse() {
    	Date date = new Date();
    	ProgramStatusResponse programStatusResponse = new ProgramStatusResponse(ProgramStatusResponse.PROGRAM_RAN_SUCCESSFULLY, "output", null, date);
    	Response<ProgramStatusResponse> response = new Response<ProgramStatusResponse>(programStatusResponse);
    	return response;
    }
}
