package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Shot {

  private int shotNumber;

  private String shotType;

  private int batterId;

  private int bowlerId;

  private int runs;
  
  private int inningNumber;

  private int overNumber;

  private int ballNumber;

public Shot(int shotNumber, String shotType, int batterId, int bowlerId, int runs, int inningNumber, int overNumber,
		int ballNumber) {
	super();
	this.shotNumber = shotNumber;
	this.shotType = shotType;
	this.batterId = batterId;
	this.bowlerId = bowlerId;
	this.runs = runs;
	this.inningNumber = inningNumber;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
}

public Shot() {
	super();
	// TODO Auto-generated constructor stub
}

public int getShotNumber() {
	return shotNumber;
}

public void setShotNumber(int shotNumber) {
	this.shotNumber = shotNumber;
}

public String getShotType() {
	return shotType;
}

public void setShotType(String shotType) {
	this.shotType = shotType;
}

public int getBatterId() {
	return batterId;
}

public void setBatterId(int batterId) {
	this.batterId = batterId;
}

public int getBowlerId() {
	return bowlerId;
}

public void setBowlerId(int bowlerId) {
	this.bowlerId = bowlerId;
}

public int getRuns() {
	return runs;
}

public void setRuns(int runs) {
	this.runs = runs;
}

public int getInningNumber() {
	return inningNumber;
}

public void setInningNumber(int inningNumber) {
	this.inningNumber = inningNumber;
}

public int getOverNumber() {
	return overNumber;
}

public void setOverNumber(int overNumber) {
	this.overNumber = overNumber;
}

public int getBallNumber() {
	return ballNumber;
}

public void setBallNumber(int ballNumber) {
	this.ballNumber = ballNumber;
}

@Override
public String toString() {
	return "Shot [shotNumber=" + shotNumber + ", shotType=" + shotType + ", batterId=" + batterId + ", bowlerId="
			+ bowlerId + ", runs=" + runs + ", inningNumber=" + inningNumber + ", overNumber=" + overNumber
			+ ", ballNumber=" + ballNumber + "]";
}
 
}