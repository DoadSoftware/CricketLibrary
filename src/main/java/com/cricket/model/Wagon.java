package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="wagons")
@XmlAccessorType(XmlAccessType.FIELD)
public class Wagon {

  @XmlElement(name = "wagonNumber")
  private int wagonNumber;

  @XmlElement(name = "wagonSector")
  private String wagonSector;

  @XmlElement(name = "wagonXCord")
  private int wagonXCord;

  @XmlElement(name = "wagonYCord")
  private int wagonYCord;
  
  @XmlElement(name = "batterId")
  private int batterId;

  @XmlElement(name = "bowlerId")
  private int bowlerId;

  @XmlElement(name = "runs")
  private int runs;
  
  @XmlElement(name = "inningNumber")
  private int inningNumber;

  @XmlElement(name = "overNumber")
  private int overNumber;

  @XmlElement(name = "ballNumber")
  private int ballNumber;

public Wagon() {
	super();
}

public Wagon(int wagonXCord, int wagonYCord) {
	super();
	this.wagonXCord = wagonXCord;
	this.wagonYCord = wagonYCord;
}

public Wagon(int wagonNumber, String wagonSector, int wagonXCord, int wagonYCord, int batterId, int bowlerId,
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