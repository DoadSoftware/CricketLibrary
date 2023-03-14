package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "shot", namespace = "shots")
public class ShotJson {

  @JsonProperty(value = "shotNumber")
  private int shotNumber;

  @JsonProperty(value = "shotType")
  private String shotType;

  @JsonProperty(value = "boundaryHeight")
  private String boundaryHeight;

  @JsonProperty(value = "sixDistance")
  private int sixDistance;
  
  @JsonProperty(value = "batterId")
  private int batterId;

  @JsonProperty(value = "bowlerId")
  private int bowlerId;

  @JsonProperty(value = "runs")
  private int runs;
  
  @JsonProperty(value = "inningNumber")
  private int inningNumber;

  @JsonProperty(value = "overNumber")
  private int overNumber;

  @JsonProperty(value = "ballNumber")
  private int ballNumber;
 
public ShotJson() {
	super();
}

public ShotJson(int shotNumber, String shotType, String boundaryHeight, int sixDistance, int batterId, int bowlerId,
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