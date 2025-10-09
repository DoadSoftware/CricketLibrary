package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OverByOverData
{
  private int InningNumber;
  private int OverNumber;
  private int OverTotalRuns;
  private int OverTotalWickets;
  private boolean WasOverPowerplay;
  private String outBatsman,notWicketCount,overtype,ChallengeOverRuns;
  
public OverByOverData() {
	super();
	this.outBatsman = "";
	this.notWicketCount = "";
}
public int getOverNumber() {
	return OverNumber;
}
public OverByOverData(int inningNumber, int overNumber, int overTotalRuns, int overTotalWickets,
		boolean wasOverPowerplay) {
	super();
	InningNumber = inningNumber;
	OverNumber = overNumber;
	OverTotalRuns = overTotalRuns;
	OverTotalWickets = overTotalWickets;
	WasOverPowerplay = wasOverPowerplay;
}
public int getInningNumber() {
	return InningNumber;
}
public void setInningNumber(int inningNumber) {
	InningNumber = inningNumber;
}
public void setOverNumber(int overNumber) {
	OverNumber = overNumber;
}
public int getOverTotalRuns() {
	return OverTotalRuns;
}
public void setOverTotalRuns(int overTotalRuns) {
	OverTotalRuns = overTotalRuns;
}
public int getOverTotalWickets() {
	return OverTotalWickets;
}
public void setOverTotalWickets(int overTotalWickets) {
	OverTotalWickets = overTotalWickets;
}
public OverByOverData(int inningNumber, int overNumber, int overTotalRuns, int overTotalWickets,
		boolean wasOverPowerplay, String outBatsman, String notWicketCount) {
	super();
	InningNumber = inningNumber;
	OverNumber = overNumber;
	OverTotalRuns = overTotalRuns;
	OverTotalWickets = overTotalWickets;
	WasOverPowerplay = wasOverPowerplay;
	this.outBatsman = outBatsman;
	this.notWicketCount = notWicketCount;
}
public boolean getWasOverPowerplay() {
	return WasOverPowerplay;
}
public void setWasOverPowerplay(boolean wasOverPowerplay) {
	WasOverPowerplay = wasOverPowerplay;
}
public String getOutBatsman() {
	return outBatsman;
}
public void setOutBatsman(String outBatsman) {
	this.outBatsman = outBatsman;
}
public String getNotWicketCount() {
	return notWicketCount;
}
public void setNotWicketCount(String notWicketCount) {
	this.notWicketCount = notWicketCount;
}
public String getOvertype() {
	return overtype;
}
public void setOvertype(String overtype) {
	this.overtype = overtype;
}
public String getChallengeOverRuns() {
	return ChallengeOverRuns;
}
public void setChallengeOverRuns(String challengeOverRuns) {
	ChallengeOverRuns = challengeOverRuns;
}
@Override
public String toString() {
	return "OverByOverData [InningNumber=" + InningNumber + ", OverNumber=" + OverNumber + ", OverTotalRuns="
			+ OverTotalRuns + ", OverTotalWickets=" + OverTotalWickets + ", WasOverPowerplay=" + WasOverPowerplay
			+ ", outBatsman=" + outBatsman + ", notWicketCount=" + notWicketCount + "]";
}

}