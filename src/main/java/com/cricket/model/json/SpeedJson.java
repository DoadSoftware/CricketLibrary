package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "speed", namespace = "speeds")
public class SpeedJson {

  @JsonProperty(value = "speedNumber")
  private int speedNumber;

  @JsonProperty(value = "speedValue")
  private String speedValue;

  @JsonProperty(value = "speedExtra")
  private String speedExtra;
  
  @JsonProperty(value = "overNumber")
  private int overNumber;

  @JsonProperty(value = "ballNumber")
  private int ballNumber;
 
public SpeedJson() {
	super();
}

public SpeedJson(int speedNumber, String speedValue, String speedExtra, int overNumber, int ballNumber) {
	super();
	this.speedNumber = speedNumber;
	this.speedValue = speedValue;
	this.speedExtra = speedExtra;
	this.overNumber = overNumber;
	this.ballNumber = ballNumber;
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

}