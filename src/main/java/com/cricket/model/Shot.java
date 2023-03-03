package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="shots")
@XmlAccessorType(XmlAccessType.FIELD)
public class Shot {

  @XmlElement(name = "shotNumber")
  private int shotNumber;

  @XmlElement(name = "shotType")
  private String shotType;

  @XmlElement(name = "boundaryHeight")
  private String boundaryHeight;

  @XmlElement(name = "sixDistance")
  private int sixDistance;
  
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