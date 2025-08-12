package com.cricket.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InningStats
{
	private List<Integer> ballsPerHour;
	
	private int timeSinceLastRun;
	
	private int timeSinceLastRunOffBat;
	
	private int timeSinceLastBoundary;
	
	public List<Integer> getBallsPerHour() {
		return ballsPerHour;
	}

	public void setBallsPerHour(List<Integer> ballsPerHour) {
		this.ballsPerHour = ballsPerHour;
	}

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
