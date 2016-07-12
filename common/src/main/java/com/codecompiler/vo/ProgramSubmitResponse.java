package com.codecompiler.vo;

/**
 * Created by Manohar Prabhu on 5/28/2016.
 */
public class ProgramSubmitResponse {
    private final String queueId;

    public ProgramSubmitResponse(String queueId) {
        this.queueId = queueId;
    }

    public String getQueueId() {
        return queueId;
    }

}
