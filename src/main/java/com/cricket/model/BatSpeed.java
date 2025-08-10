package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatSpeed {

  @JsonProperty(value = "Power")
  private Integer batPower;

  @JsonProperty(value = "Speed")
  private float batSpeed;

  @JsonProperty(value = "Impact")
  private Integer batImpact;
 
  @JsonProperty(value = "Twist")
  private Integer batTwist;
  
  @JsonProperty(value = "Over")
  private Integer overNumber;

  @JsonProperty(value = "Ball")
  private Integer ballNumber;

  @JsonIgnore
  private long batSpeedFileModifiedTime;

public BatSpeed() {
	super();
}

public BatSpeed(long batSpeedFileModifiedTime) {
	super();
	this.batSpeedFileModifiedTime = batSpeedFileModifiedTime;
}

public BatSpeed(Integer batPower, Integer batSpeed, Integer batImpact, Integer batTwist, Integer overNumber, Integer ballNumber) {
	super();
	this.batPower = batPower;
	this.batSpeed = batSpeed;
	this.batImpact = batImpact;
	this.batTwist = batTwist;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
}

public Integer getBatPower() {
	return batPower;
}

public void setBatPower(Integer batPower) {
	this.batPower = batPower;
}

public float getBatSpeed() {
	return batSpeed;
}

public void setBatSpeed(float batSpeed) {
	this.batSpeed = batSpeed;
}

public Integer getBatImpact() {
	return batImpact;
}

public void setBatImpact(Integer batImpact) {
	this.batImpact = batImpact;
}

public Integer getBatTwist() {
	return batTwist;
}

public void setBatTwist(Integer batTwist) {
	this.batTwist = batTwist;
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

public long getBatSpeedFileModifiedTime() {
	return batSpeedFileModifiedTime;
}

public void setBatSpeedFileModifiedTime(long batSpeedFileModifiedTime) {
	this.batSpeedFileModifiedTime = batSpeedFileModifiedTime;
}

@Override
public String toString() {
	return "BatSpeed [batPower=" + batPower + ", batSpeed=" + batSpeed + ", batImpact=" + batImpact + ", batTwist="
			+ batTwist + ", overNumber=" + overNumber + ", ballNumber=" + ballNumber + ", batSpeedFileModifiedTime="
			+ batSpeedFileModifiedTime + "]";
}
  
}