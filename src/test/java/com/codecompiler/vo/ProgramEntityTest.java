package com.codecompiler.vo;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/31/2016.
 */
public class ProgramEntityTest {
    @Test
    public void testProgramEntityFields() {
        ProgramEntity programEntity = new ProgramEntity();
        Date date = new Date();
        programEntity.setId("ID");
        programEntity.setProgramStatus(0);
        programEntity.setProgram("program");
        programEntity.setOutput("output");
        programEntity.setErrorMessage("error");
        programEntity.setExecutionTimeLimit(1);
        programEntity.setInput("input");
        programEntity.setQueuedTime(date);
        programEntity.setQueueId("qid");

        Assert.assertEquals("ID", programEntity.getId());
        Assert.assertEquals(0, programEntity.getProgramStatus());
        Assert.assertEquals("program", programEntity.getProgram());
        Assert.assertEquals("output", programEntity.getOutput());
        Assert.assertEquals("error", programEntity.getErrorMessage());
        Assert.assertEquals(1, programEntity.getExecutionTimeLimit());
        Assert.assertEquals("input", programEntity.getInput());
        Assert.assertEquals(date, programEntity.getQueuedTime());
        Assert.assertEquals("qid", programEntity.getQueueId());

    }
}
