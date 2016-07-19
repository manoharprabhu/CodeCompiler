package com.codecompiler.vo;

import java.util.Date;

public class Submission {

	private String queueId;
	private Date submittedTime;
	private int status;

	public Submission(String queueId, Date submittedTime, int status) {
		this.queueId = queueId;
		this.submittedTime = submittedTime;
		this.status = status;
	}

	public String getQueueId() {
		return queueId;
	}

	public Date getSubmittedTime() {
		return submittedTime;
	}

	public int getStatus() {
		return status;
	}
}