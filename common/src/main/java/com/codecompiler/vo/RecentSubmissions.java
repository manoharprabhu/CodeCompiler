package com.codecompiler.vo;

import java.util.List;

public class RecentSubmissions {
	private List<Submission> submissions = null;
	private int nextPage = 0;
	private int prevPage = 0;
	private int rowSize = 0;

	public RecentSubmissions(List<Submission> submissions, int prevPage, int nextPage, int rowSize) {
		this.submissions = submissions;
		this.prevPage = prevPage;
		this.nextPage = nextPage;
		this.rowSize = rowSize;
	}

	public List<Submission> getSubmissions() {
		return submissions;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getRowSize() {
		return rowSize;
	}
}
