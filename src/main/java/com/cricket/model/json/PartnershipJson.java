package com.cricket.model.json;

import com.cricket.model.Player;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "partnership", namespace = "partnerships")
public class PartnershipJson implements Comparable<PartnershipJson>
{

  @JsonProperty(value = "partnershipNumber")
  private int partnershipNumber;
  
  @JsonProperty(value = "firstBatterNo")
  private int firstBatterNo;

  @JsonProperty(value = "secondBatterNo")
  private int secondBatterNo;
  
  @JsonProperty(value = "firstBatterRuns")
  private int firstBatterRuns;

  @JsonProperty(value = "secondBatterRuns")
  private int secondBatterRuns;

  @JsonProperty(value = "firstBatterBalls")
  private int firstBatterBalls;

  @JsonProperty(value = "secondBatterBalls")
  private int secondBatterBalls;
  
  @JsonProperty(value = "totalRuns")
  private int totalRuns;
  
  @JsonProperty(value = "totalBalls")
  private int totalBalls;

  @JsonProperty(value = "totalFours")
  private int totalFours;

  @JsonProperty(value = "totalSixes")
  private int totalSixes;
  
  @JsonIgnoreProperties
  private Player firstPlayer;
  
  @JsonIgnoreProperties
  private Player secondPlayer;
  
public PartnershipJson(int partnershipNumber, int firstBatterNo, int secondBatterNo) {
	super();
	this.partnershipNumber = partnershipNumber;
	this.firstBatterNo = firstBatterNo;
	this.secondBatterNo = secondBatterNo;
}

public PartnershipJson(int partnershipNumber, int firstBatterNo, int secondBatterNo, int firstBatterRuns,
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

public PartnershipJson() {
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
public int compareTo(PartnershipJson part) {
	return (int) (this.getPartnershipNumber()-part.getPartnershipNumber());
}
  
}