package com.codecompiler.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class RecentSubmissionsTest {
	
	@Test
	public void testRecentSubmissionsFields() {
		Date date = new Date();
		Submission submission = new Submission("queueId", date, 6);
		Assert.assertEquals("queueId", submission.getQueueId());
		Assert.assertEquals(6, submission.getStatus());
		Assert.assertEquals(date, submission.getSubmittedTime());
		List<Submission> submissions = new ArrayList<Submission>();
		submissions.add(submission);
		RecentSubmissions recentSubmissions = new RecentSubmissions(submissions, -1, -1, 1);
		Assert.assertEquals(-1, recentSubmissions.getNextPage());
		Assert.assertEquals(-1, recentSubmissions.getPrevPage());
		Assert.assertEquals(1, recentSubmissions.getRowSize());
		Assert.assertEquals(submissions, recentSubmissions.getSubmissions());
	}

}
