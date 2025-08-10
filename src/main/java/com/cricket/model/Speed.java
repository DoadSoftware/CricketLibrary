package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Speed {

  private int speedNumber;

  private String speedValue;

  private String speedExtra;
  
  private int overNumber;

  private int ballNumber;

  @JsonIgnore
  private long speedFileModifiedTime;
  
public Speed() {
	super();
}

public Speed(long speedFileModifiedTime) {
	super();
	this.speedFileModifiedTime = speedFileModifiedTime;
}

public Speed(String speedValue, long speedFileModifiedTime) {
	super();
	this.speedValue = speedValue;
	this.speedFileModifiedTime = speedFileModifiedTime;
}

public Speed(int speedNumber, String speedValue, String speedExtra, int overNumber, int ballNumber) {
	super();
	this.speedNumber = speedNumber;
	this.speedValue = speedValue;
	this.speedExtra = speedExtra;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
}

public long getSpeedFileModifiedTime() {
	return speedFileModifiedTime;
}

public void setSpeedFileModifiedTime(long speedFileModifiedTime) {
	this.speedFileModifiedTime = speedFileModifiedTime;
}

public String getSpeedExtra() {
	return speedExtra;
}

public void setSpeedExtra(String speedExtra) {
	this.speedExtra = speedExtra;
}

public int getSpeedNumber() {
	return speedNumber;
}

public void setSpeedNumber(int speedNumber) {
	this.speedNumber = speedNumber;
}

public String getSpeedValue() {
	return speedValue;
}

public void setSpeedValue(String speedValue) {
	this.speedValue = speedValue;
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
	return "Speed [speedNumber=" + speedNumber + ", speedValue=" + speedValue + ", speedExtra=" + speedExtra
			+ ", overNumber=" + overNumber + ", ballNumber=" + ballNumber + ", speedFileModifiedTime="
			+ speedFileModifiedTime + "]";
}

}