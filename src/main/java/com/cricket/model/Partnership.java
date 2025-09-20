package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Partnership implements Comparable<Partnership>
{
  private int partnershipNumber;
  
  private int firstBatterNo;

  private int secondBatterNo;
  
  private int firstBatterRuns;

  private int secondBatterRuns;

  private int firstBatterBalls;

  private int secondBatterBalls;
  
  private int totalRuns;
  
  private int totalBalls;

  private int totalFours;

  private int totalSixes;

  private int totalNines;
  
  @JsonIgnore
  private Player firstPlayer;
  
  @JsonIgnore
  private Player secondPlayer;
  
public Partnership(int partnershipNumber, int firstBatterNo, int secondBatterNo) {
	super();
	this.partnershipNumber = partnershipNumber;
	this.firstBatterNo = firstBatterNo;
	this.secondBatterNo = secondBatterNo;
}

public Partnership(int partnershipNumber, int firstBatterNo, int secondBatterNo, int firstBatterRuns,
		int secondBatterRuns, int firstBatterBalls, int secondBatterBalls, int totalRuns, int totalBalls,
		int totalFours, int totalSixes) {
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
}

public Partnership() {
	super();
}

public int getTotalNines() {
	return totalNines;
}

public void setTotalNines(int totalNines) {
	if(totalNines < 0) {
		this.totalNines = 0;
	}
	else {
		this.totalNines = totalNines;
	}
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
	if(totalRuns < 0) {
		this.totalRuns = 0;
	}
	else {
		this.totalRuns = totalRuns;
	}
}

public int getTotalBalls() {
	return totalBalls;
}

public void setTotalBalls(int totalBalls) {
	if(totalBalls < 0) {
		this.totalBalls = 0;
	}
	else {
		this.totalBalls = totalBalls;
	}
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
	if(totalFours < 0) {
		this.totalFours = 0;
	}
	else {
		this.totalFours = totalFours;
	}
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	if(totalSixes < 0) {
		this.totalSixes = 0;
	}
	else {
		this.totalSixes = totalSixes;
	}
}

@Override
public int compareTo(Partnership part) {
	return (int) (this.getPartnershipNumber()-part.getPartnershipNumber());
}

@Override
public String toString() {
	return "Partnership [partnershipNumber=" + partnershipNumber + ", firstBatterNo=" + firstBatterNo
			+ ", secondBatterNo=" + secondBatterNo + ", firstBatterRuns=" + firstBatterRuns + ", secondBatterRuns="
			+ secondBatterRuns + ", firstBatterBalls=" + firstBatterBalls + ", secondBatterBalls=" + secondBatterBalls
			+ ", totalRuns=" + totalRuns + ", totalBalls=" + totalBalls + ", totalFours=" + totalFours + ", totalSixes="
			+ totalSixes + ", totalNines=" + totalNines + ", firstPlayer=" + firstPlayer + ", secondPlayer="
			+ secondPlayer + "]";
}
  
}