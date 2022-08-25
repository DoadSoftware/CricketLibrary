package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="partnership")
@XmlAccessorType(XmlAccessType.FIELD)
public class Partnership implements Comparable<Partnership>
{

  @XmlElement(name = "partnershipNumber")
  private int partnershipNumber;
  
  @XmlElement(name = "firstBatterNo")
  private int firstBatterNo;

  @XmlElement(name = "secondBatterNo")
  private int secondBatterNo;
  
  @XmlElement(name = "firstBatterRuns")
  private int firstBatterRuns;

  @XmlElement(name = "secondBatterRuns")
  private int secondBatterRuns;

  @XmlElement(name = "firstBatterBalls")
  private int firstBatterBalls;

  @XmlElement(name = "secondBatterBalls")
  private int secondBatterBalls;
  
  @XmlElement(name = "totalRuns")
  private int totalRuns;
  
  @XmlElement(name = "totalBalls")
  private int totalBalls;
  
  @XmlTransient
  private Player firstPlayer;
  
  @XmlTransient
  private Player secondPlayer;
  
public Partnership(int partnershipNumber, int firstBatterNo, int secondBatterNo) {
	super();
	this.partnershipNumber = partnershipNumber;
	this.firstBatterNo = firstBatterNo;
	this.secondBatterNo = secondBatterNo;
}

public Partnership() {
	super();
}

public Player getFirstPlayer() {
	return firstPlayer;
}

public void setFirstPlayer(Player firstPlayer) {
	this.firstPlayer = firstPlayer;
}

public Player getSecondPlayer() {
	return secondPlayer;
}

public void setSecondPlayer(Player secondPlayer) {
	this.secondPlayer = secondPlayer;
}

public int getPartnershipNumber() {
	return partnershipNumber;
}

public void setPartnershipNumber(int partnershipNumber) {
	this.partnershipNumber = partnershipNumber;
}

public int getFirstBatterNo() {
	return firstBatterNo;
}

public void setFirstBatterNo(int firstBatterNo) {
	this.firstBatterNo = firstBatterNo;
}

public int getSecondBatterNo() {
	return secondBatterNo;
}

public void setSecondBatterNo(int secondBatterNo) {
	this.secondBatterNo = secondBatterNo;
}

public int getFirstBatterRuns() {
	return firstBatterRuns;
}

public void setFirstBatterRuns(int firstBatterRuns) {
	this.firstBatterRuns = firstBatterRuns;
}

public int getSecondBatterRuns() {
	return secondBatterRuns;
}

public void setSecondBatterRuns(int secondBatterRuns) {
	this.secondBatterRuns = secondBatterRuns;
}

public int getTotalRuns() {
	return totalRuns;
}

public void setTotalRuns(int totalRuns) {
	this.totalRuns = totalRuns;
}

public int getTotalBalls() {
	return totalBalls;
}

public void setTotalBalls(int totalBalls) {
	this.totalBalls = totalBalls;
}

public int getFirstBatterBalls() {
	return firstBatterBalls;
}

public void setFirstBatterBalls(int firstBatterBalls) {
	this.firstBatterBalls = firstBatterBalls;
}

public int getSecondBatterBalls() {
	return secondBatterBalls;
}

public void setSecondBatterBalls(int secondBatterBalls) {
	this.secondBatterBalls = secondBatterBalls;
}

@Override
public int compareTo(Partnership part) {
	return (int) (this.getPartnershipNumber()-part.getPartnershipNumber());
}
  
}