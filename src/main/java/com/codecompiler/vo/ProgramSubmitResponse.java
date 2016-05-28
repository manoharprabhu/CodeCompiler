package com.codecompiler.vo;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class ProgramSubmitResponse {
    private final String programId;

    public ProgramSubmitResponse(String programId) {
        this.programId = programId;
    }

    public String getProgramId() {
        return programId;
    }

}
