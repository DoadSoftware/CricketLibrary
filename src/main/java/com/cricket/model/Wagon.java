package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Wagon {

  private int wagonNumber;

  private int wagonSector;

  private int wagonXCord;

  private int wagonYCord;
  
  private String boundaryHeight;

  private int sixDistance;
  
  private int batterId;

  private int bowlerId;

  private int runs;
  
  private int inningNumber;

  private int overNumber;

  private int ballNumber;

public Wagon() {
	super();
}

public Wagon(int wagonNumber, int wagonSector, int wagonXCord, int wagonYCord, String boundaryHeight, int sixDistance,
		int batterId, int bowlerId, int runs, int inningNumber, int overNumber, int ballNumber) {
	super();
	this.wagonNumber = wagonNumber;
	this.wagonSector = wagonSector;
	this.wagonXCord = wagonXCord;
	this.wagonYCord = wagonYCord;
	this.boundaryHeight = boundaryHeight;
	this.sixDistance = sixDistance;
	this.batterId = batterId;
	this.bowlerId = bowlerId;
	this.runs = runs;
	this.inningNumber = inningNumber;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
}

public int getWagonNumber() {
	return wagonNumber;
}

public void setWagonNumber(int wagonNumber) {
	this.wagonNumber = wagonNumber;
}

public int getWagonSector() {
	return wagonSector;
}

public void setWagonSector(int wagonSector) {
	this.wagonSector = wagonSector;
}

public int getWagonXCord() {
	return wagonXCord;
}

public void setWagonXCord(int wagonXCord) {
	this.wagonXCord = wagonXCord;
}

public int getWagonYCord() {
	return wagonYCord;
}

public void setWagonYCord(int wagonYCord) {
	this.wagonYCord = wagonYCord;
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
	return "Wagon [wagonNumber=" + wagonNumber + ", wagonSector=" + wagonSector + ", wagonXCord=" + wagonXCord
			+ ", wagonYCord=" + wagonYCord + ", boundaryHeight=" + boundaryHeight + ", sixDistance=" + sixDistance
			+ ", batterId=" + batterId + ", bowlerId=" + bowlerId + ", runs=" + runs + ", inningNumber=" + inningNumber
			+ ", overNumber=" + overNumber + ", ballNumber=" + ballNumber + "]";
}

}