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

  @XmlElement(name = "totalFours")
  private int totalFours;

  @XmlElement(name = "totalSixes")
  private int totalSixes;
  
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

public Partnership(int partnershipNumber, int firstBatterNo, int secondBatterNo, int firstBatterRuns,
		int secondBatterRuns, int firstBatterBalls, int secondBatterBalls, int totalRuns, int totalBalls,
		int totalFours, int totalSixes, Player firstPlayer, Player secondPlayer) {
	super();
	this.partnershipNumber = partnershipNumber;
	this.firstBatterNo = firstBatterNo;
	this.secondBatterNo = secondBatterNo;
	this.firstBatterRuns = firstBatterRuns;
	this.secondBatterRuns = secondBatterRuns;
	this.firstBatterBalls = firstBatterBalls;
	this.secondBatterBalls = secondBatterBalls;
	this.totalRuns = totalRuns;
	this.totalBalls = totalBalls;
	this.totalFours = totalFours;
	this.totalSixes = totalSixes;
	this.firstPlayer = firstPlayer;
	this.secondPlayer = secondPlayer;
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

public int getTotalFours() {
	return totalFours;
}

public void setTotalFours(int totalFours) {
	this.totalFours = totalFours;
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	this.totalSixes = totalSixes;
}

@Override
public int compareTo(Partnership part) {
	return (int) (this.getPartnershipNumber()-part.getPartnershipNumber());
}
@Override
public Partnership clone()
{
    try {
        return (Partnership) super.clone();
    } catch (CloneNotSupportedException e) {
        return new Partnership(partnershipNumber, firstBatterNo, secondBatterNo, 
        		firstBatterRuns, secondBatterRuns, firstBatterBalls, secondBatterBalls, 
        		totalRuns, totalBalls, totalFours, totalSixes, firstPlayer, secondPlayer); 
    }	
}

}