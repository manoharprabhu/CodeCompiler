package com.codecompiler;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class ProgramStatusResponse {
    private final int programStatus;
    private final String output;
    private final String errorMessage;
    private final Date submittedDate;

    public ProgramStatusResponse(int programStatus, String output, String errorMessage, Date submittedDate) {
        this.programStatus = programStatus;
        this.output = output;
        this.errorMessage = errorMessage;
        this.submittedDate = submittedDate;
    }

    public int getProgramStatus() {
        return programStatus;
    }

    public String getOutput() {
        return output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }
}
