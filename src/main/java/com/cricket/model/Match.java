package com.cricket.model;

import java.util.List;

public class Match {

  private String matchResult;

  private String matchStatus;
	
  private String current_speed;
	
  private long matchTotalSeconds;
  
  private String bowlerRunning;

  private String ballRelease;
	
  private String matchFileName;
	
  private List<Inning> inning;

  private List<DaySession> daysSessions;
  
  private List<Shot> shots;

  private List<Wagon> wagons;
  
  private MatchClock clock;

public String getMatchResult() {
	return matchResult;
}

public void setMatchResult(String matchResult) {
	this.matchResult = matchResult;
}

public String getMatchStatus() {
	return matchStatus;
}

public void setMatchStatus(String matchStatus) {
	this.matchStatus = matchStatus;
}

public String getCurrent_speed() {
	return current_speed;
}

public void setCurrent_speed(String current_speed) {
	this.current_speed = current_speed;
}

public long getMatchTotalSeconds() {
	return matchTotalSeconds;
}

public void setMatchTotalSeconds(long matchTotalSeconds) {
	this.matchTotalSeconds = matchTotalSeconds;
}

public String getBowlerRunning() {
	return bowlerRunning;
}

public void setBowlerRunning(String bowlerRunning) {
	this.bowlerRunning = bowlerRunning;
}

public String getBallRelease() {
	return ballRelease;
}

public void setBallRelease(String ballRelease) {
	this.ballRelease = ballRelease;
}

public String getMatchFileName() {
	return matchFileName;
}

public void setMatchFileName(String matchFileName) {
	this.matchFileName = matchFileName;
}

public List<Inning> getInning() {
	return inning;
}

public void setInning(List<Inning> inning) {
	this.inning = inning;
}

public List<DaySession> getDaysSessions() {
	return daysSessions;
}

public void setDaysSessions(List<DaySession> daysSessions) {
	this.daysSessions = daysSessions;
}

public List<Shot> getShots() {
	return shots;
}

public void setShots(List<Shot> shots) {
	this.shots = shots;
}

public List<Wagon> getWagons() {
	return wagons;
}

public void setWagons(List<Wagon> wagons) {
	this.wagons = wagons;
}

public MatchClock getClock() {
	return clock;
}

public void setClock(MatchClock clock) {
	this.clock = clock;
}

@Override
public String toString() {
	return "Match [matchResult=" + matchResult + ", matchStatus=" + matchStatus + ", current_speed=" + current_speed
			+ ", matchTotalSeconds=" + matchTotalSeconds + ", bowlerRunning=" + bowlerRunning + ", ballRelease="
			+ ballRelease + ", matchFileName=" + matchFileName + ", inning=" + inning + ", daysSessions=" + daysSessions
			+ ", shots=" + shots + ", wagons=" + wagons + ", clock=" + clock + "]";
}
  
}