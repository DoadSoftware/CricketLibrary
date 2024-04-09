package com.cricket.model;

public class InningStats
{
	private int timeSinceLastRun;
	
	private int timeSinceLastRunOffBat;
	
	private int timeSinceLastBoundary;
	
	public int getTimeSinceLastRun() {
		return timeSinceLastRun;
	}

	public void setTimeSinceLastRun(int timeSinceLastRun) {
		this.timeSinceLastRun = timeSinceLastRun;
	}

	public int getTimeSinceLastRunOffBat() {
		return timeSinceLastRunOffBat;
	}

	public void setTimeSinceLastRunOffBat(int timeSinceLastRunOffBat) {
		this.timeSinceLastRunOffBat = timeSinceLastRunOffBat;
	}

	public int getTimeSinceLastBoundary() {
		return timeSinceLastBoundary;
	}

	public void setTimeSinceLastBoundary(int timeSinceLastBoundary) {
		this.timeSinceLastBoundary = timeSinceLastBoundary;
	}

}
