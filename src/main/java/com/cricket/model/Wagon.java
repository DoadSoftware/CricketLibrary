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

  @XmlElement(name = "wagonXCoOrd")
  private String wagonXCoOrd;

  @XmlElement(name = "wagonYCoOrd")
  private String wagonYCoOrd;
  
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

public Wagon(int wagonNumber, String wagonXCoOrd, String wagonYCoOrd, int batterId, int bowlerId, int runs,
		int inningNumber, int overNumber, int ballNumber) {
	super();
	this.wagonNumber = wagonNumber;
	this.wagonXCoOrd = wagonXCoOrd;
	this.wagonYCoOrd = wagonYCoOrd;
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

public String getWagonXCoOrd() {
	return wagonXCoOrd;
}

public void setWagonXCoOrd(String wagonXCoOrd) {
	this.wagonXCoOrd = wagonXCoOrd;
}

public String getWagonYCoOrd() {
	return wagonYCoOrd;
}

public void setWagonYCoOrd(String wagonYCoOrd) {
	this.wagonYCoOrd = wagonYCoOrd;
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