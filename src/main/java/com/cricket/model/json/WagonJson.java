package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "wagon", namespace = "wagons")
public class WagonJson {

  @JsonProperty(value = "wagonNumber")
  private int wagonNumber;

  @JsonProperty(value = "wagonSector")
  private String wagonSector;

  @JsonProperty(value = "wagonXCord")
  private int wagonXCord;

  @JsonProperty(value = "wagonYCord")
  private int wagonYCord;
  
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

public WagonJson() {
	super();
}

public WagonJson(int wagonXCord, int wagonYCord) {
	super();
	this.wagonXCord = wagonXCord;
	this.wagonYCord = wagonYCord;
}

public WagonJson(int wagonNumber, String wagonSector, int wagonXCord, int wagonYCord, int batterId, int bowlerId,
		int runs, int inningNumber, int overNumber, int ballNumber) {
	super();
	this.wagonNumber = wagonNumber;
	this.wagonSector = wagonSector;
	this.wagonXCord = wagonXCord;
	this.wagonYCord = wagonYCord;
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

public String getWagonSector() {
	return wagonSector;
}

public void setWagonSector(String wagonSector) {
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
			+ ", wagonYCord=" + wagonYCord + ", batterId=" + batterId + ", bowlerId=" + bowlerId + ", runs=" + runs
			+ ", inningNumber=" + inningNumber + ", overNumber=" + overNumber + ", ballNumber=" + ballNumber + "]";
}

}