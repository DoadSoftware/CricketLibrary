package com.cricket.model;

public class DaySession {

  private int dayNumber;

  private int sessionNumber;

  private String isCurrentSession;
  
  private int totalRuns;

  private int totalBalls;

  private int totalWickets;

  private int totalFours;

  private int totalSixes;

  private long totalSeconds;

public DaySession() {
	super();
}

public DaySession(int dayNumber, int sessionNumber, String isCurrentSession) {
	super();
	this.dayNumber = dayNumber;
	this.sessionNumber = sessionNumber;
	this.isCurrentSession = isCurrentSession;
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
	this.totalRuns = totalRuns;
}

public int getTotalBalls() {
	return totalBalls;
}

public void setTotalBalls(int totalBalls) {
	this.totalBalls = totalBalls;
}

public int getTotalWickets() {
	return totalWickets;
}

public void setTotalWickets(int totalWickets) {
	this.totalWickets = totalWickets;
}

public int getTotalFours() {
	return totalFours;
}

public void setTotalFours(int totalFours) {
	this.totalFours = totalFours;
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	this.totalSixes = totalSixes;
}

public long getTotalSeconds() {
	return totalSeconds;
}

public void setTotalSeconds(long totalSeconds) {
	this.totalSeconds = totalSeconds;
}
 
}