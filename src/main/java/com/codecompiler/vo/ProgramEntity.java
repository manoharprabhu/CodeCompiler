package com.codecompiler.vo;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/29/2016.
 */

public class ProgramEntity {

    @Id
    private String Id;

    private String queueId;
    private String program;
    private String input;
    private int executionTimeLimit;
    private Date queuedTime;

    private int programStatus;
    private String errorMessage;
    private String output;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQueueId() {
        return queueId;
    }

    public void setQueueId(String queueId) {
        this.queueId = queueId;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public int getExecutionTimeLimit() {
        return executionTimeLimit;
    }

    public void setExecutionTimeLimit(int executionTimeLimit) {
        this.executionTimeLimit = executionTimeLimit;
    }

    public int getProgramStatus() {
        return programStatus;
    }

    public void setProgramStatus(int programStatus) {
        this.programStatus = programStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Date getQueuedTime() {
        return queuedTime;
    }

    public void setQueuedTime(Date queuedTime) {
        this.queuedTime = queuedTime;
    }
}
