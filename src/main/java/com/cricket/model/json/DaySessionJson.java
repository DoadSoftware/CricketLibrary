package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="daySession", namespace = "daySessions")
public class DaySessionJson {

  @JsonProperty(value ="dayNumber")
  private int dayNumber;

  @JsonProperty(value ="sessionNumber")
  private int sessionNumber;

  @JsonProperty(value ="isCurrentSession")
  private String isCurrentSession;
  
  @JsonProperty(value ="totalRuns")
  private int totalRuns;

  @JsonProperty(value ="totalBalls")
  private int totalBalls;

  @JsonProperty(value ="totalWickets")
  private int totalWickets;

  @JsonProperty(value ="totalFours")
  private int totalFours;

  @JsonProperty(value ="totalSixes")
  private int totalSixes;

  @JsonProperty(value ="totalSeconds")
  private long totalSeconds;

public DaySessionJson() {
	super();
}

public DaySessionJson(int dayNumber, int sessionNumber, String isCurrentSession) {
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