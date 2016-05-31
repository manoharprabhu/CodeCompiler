package com.codecompiler.vo;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
public class ResponseTest {
    @Test
    public void testProgramStatusResponseFields() {
        Date date = new Date();
        ProgramStatusResponse programStatusResponse = new ProgramStatusResponse(0, "output", "error", date);
        Assert.assertEquals(ProgramStatusResponse.PROGRAM_NOT_FOUND, programStatusResponse.getProgramStatus());
        Assert.assertEquals("output", programStatusResponse.getOutput());
        Assert.assertEquals("error", programStatusResponse.getErrorMessage());
        Assert.assertEquals(date, programStatusResponse.getSubmittedDate());
    }

    @Test
    public void testProgramSubmitResponseFields() {
        ProgramSubmitResponse programSubmitResponse = new ProgramSubmitResponse("id");
        Assert.assertEquals("id", programSubmitResponse.getQueueId());
    }

    @Test
    public void testResponseFields() {
        Date date = new Date();
        Response<ProgramStatusResponse> programStatusResponseResponse = new Response<ProgramStatusResponse>(new ProgramStatusResponse(0, "output", "error", date));
        Assert.assertEquals(ProgramStatusResponse.PROGRAM_NOT_FOUND, programStatusResponseResponse.getData().getProgramStatus());
        Assert.assertEquals("output", programStatusResponseResponse.getData().getOutput());
        Assert.assertEquals("error", programStatusResponseResponse.getData().getErrorMessage());
        Assert.assertEquals(date, programStatusResponseResponse.getData().getSubmittedDate());
    }

    @Test
    public void testEmptyResponse() {
        Response response = Response.createEmptyResponse();
        Assert.assertNull(response.getData());
    }
}
