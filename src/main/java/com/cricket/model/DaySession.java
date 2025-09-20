package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaySession {

  private int dayNumber;

  private int sessionNumber;

  private String isCurrentSession;

  private int totalMaidens;
  
  private int totalRuns;

  private int totalBalls;

  private int totalWickets;

  private int totalFours;

  private int totalSixes;

  private long totalSeconds;

public DaySession() {
	super();
}

public DaySession(int dayNumber, int sessionNumber) {
	super();
	this.dayNumber = dayNumber;
	this.sessionNumber = sessionNumber;
}

public DaySession(int dayNumber, int sessionNumber, String isCurrentSession) {
	super();
	this.dayNumber = dayNumber;
	this.sessionNumber = sessionNumber;
	this.isCurrentSession = isCurrentSession;
}

public int getTotalMaidens() {
	return totalMaidens;
}

public void setTotalMaidens(int totalMaidens) {
	this.totalMaidens = totalMaidens;
}

public String getIsCurrentSession() {
	return isCurrentSession;
}

public void setIsCurrentSession(String isCurrentSession) {
	this.isCurrentSession = isCurrentSession;
}

public int getDayNumber() {
	return dayNumber;
}

public void setDayNumber(int dayNumber) {
	this.dayNumber = dayNumber;
}

public int getSessionNumber() {
	return sessionNumber;
}

public void setSessionNumber(int sessionNumber) {
	this.sessionNumber = sessionNumber;
}

public int getTotalRuns() {
	return totalRuns;
}

public void setTotalRuns(int totalRuns) {
	if(totalRuns < 0) {
		this.totalRuns = 0;
	}
	else {
		this.totalRuns = totalRuns;
	}
}

public int getTotalBalls() {
	return totalBalls;
}

public void setTotalBalls(int totalBalls) {
	if(totalBalls < 0) {
		this.totalBalls = 0;
	}
	else {
		this.totalBalls = totalBalls;
	}
}

public int getTotalWickets() {
	return totalWickets;
}

public void setTotalWickets(int totalWickets) {
	if(totalWickets < 0) {
		this.totalWickets = 0;
	}
	else {
		this.totalWickets = totalWickets;
	}
}

public int getTotalFours() {
	return totalFours;
}

public void setTotalFours(int totalFours) {
	if(totalFours < 0) {
		this.totalFours = 0;
	}
	else {
		this.totalFours = totalFours;
	}
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	if(totalSixes < 0) {
		this.totalSixes = 0;
	}
	else {
		this.totalSixes = totalSixes;
	}
}

public long getTotalSeconds() {
	return totalSeconds;
}

public void setTotalSeconds(long totalSeconds) {
	if(totalSeconds < 0) {
		this.totalSeconds = 0;
	}
	else {
		this.totalSeconds = totalSeconds;
	}
}
 
}