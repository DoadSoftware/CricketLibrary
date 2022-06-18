package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="partnership")
@XmlAccessorType(XmlAccessType.FIELD)
public class FallOfWicket implements Comparable<FallOfWicket>
{

  @XmlElement(name = "fowNumber")
  private int fowNumber;
  
  @XmlElement(name = "fowPlayerID")
  private int fowPlayerID;

  @XmlElement(name = "fowRuns")
  private int fowRuns;
  
  @XmlElement(name = "fowOvers")
  private int fowOvers;

  @XmlElement(name = "fowBalls")
  private int fowBalls;

public FallOfWicket() {
	super();
}

public FallOfWicket(int fowNumber, int fowPlayerID, int fowRuns, int fowOvers, int fowBalls) {
	super();
	this.fowNumber = fowNumber;
	this.fowPlayerID = fowPlayerID;
	this.fowRuns = fowRuns;
	this.fowOvers = fowOvers;
	this.fowBalls = fowBalls;
}

public int getFowNumber() {
	return fowNumber;
}

public void setFowNumber(int fowNumber) {
	this.fowNumber = fowNumber;
}

public int getFowPlayerID() {
	return fowPlayerID;
}

public void setFowPlayerID(int fowPlayerID) {
	this.fowPlayerID = fowPlayerID;
}

public int getFowRuns() {
	return fowRuns;
}

public void setFowRuns(int fowRuns) {
	this.fowRuns = fowRuns;
}

public int getFowOvers() {
	return fowOvers;
}

public void setFowOvers(int fowOvers) {
	this.fowOvers = fowOvers;
}

public int getFowBalls() {
	return fowBalls;
}

public void setFowBalls(int fowBalls) {
	this.fowBalls = fowBalls;
}

@Override
public int compareTo(FallOfWicket fow) {
	return (int) (this.getFowNumber()-fow.getFowNumber());
}

}