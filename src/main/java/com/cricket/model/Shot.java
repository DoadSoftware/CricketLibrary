package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Shot {

  private int shotNumber;

  private String shotType;

  private String boundaryHeight;

  private int sixDistance;
  
  private int batterId;

  private int bowlerId;

  private int runs;
  
  private int inningNumber;

  private int overNumber;

  private int ballNumber;
 
public Shot() {
	super();
}

public Shot(int shotNumber, String shotType, String boundaryHeight, int sixDistance, int batterId, int bowlerId,
		int runs, int inningNumber, int overNumber, int ballNumber) {
	super();
	this.shotNumber = shotNumber;
	this.shotType = shotType;
	this.boundaryHeight = boundaryHeight;
	this.sixDistance = sixDistance;
	this.batterId = batterId;
	this.bowlerId = bowlerId;
	this.runs = runs;
	this.inningNumber = inningNumber;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
}

public String getBoundaryHeight() {
	return boundaryHeight;
}

public void setBoundaryHeight(String boundaryHeight) {
	this.boundaryHeight = boundaryHeight;
}

public int getSixDistance() {
	return sixDistance;
}

public void setSixDistance(int sixDistance) {
	this.sixDistance = sixDistance;
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

}