package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="speeds")
@XmlAccessorType(XmlAccessType.FIELD)
public class Speed {

  @XmlElement(name = "speedNumber")
  private int speedNumber;

  @XmlElement(name = "speedValue")
  private String speedValue;

  @XmlElement(name = "speedExtra")
  private String speedExtra;
  
  @XmlElement(name = "overNumber")
  private int overNumber;

  @XmlElement(name = "ballNumber")
  private int ballNumber;

  @XmlTransient
  private long speedFileModifiedTime;
  
public Speed() {
	super();
}

public Speed(long speedFileModifiedTime) {
	super();
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