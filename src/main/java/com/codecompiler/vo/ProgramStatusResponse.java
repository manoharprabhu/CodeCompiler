package com.codecompiler.vo;

import java.util.Date;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class ProgramStatusResponse {
    private final int programStatus;
    private final String output;
    private final String errorMessage;
    private final Date submittedDate;

    public static final int PROGRAM_NOT_FOUND = 0;
    public static final int PROGRAM_IN_QUEUE_FOR_COMPILATION = 1;
    public static final int PROGRAM_IS_GETTING_PROCESSED = 2;
    public static final int PROGRAM_COMPILATION_ERROR = 3;
    public static final int PROGRAM_EXECUTION_TIMEOUT = 4;
    public static final int PROGRAM_RUNTIME_ERROR = 5;
    public static final int PROGRAM_RAN_SUCCESSFULLY = 6;

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
