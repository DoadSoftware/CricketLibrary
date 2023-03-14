package com.cricket.model.json;

public class OverByOverData
{
  private int InningNumber;
  private int OverNumber;
  private int OverTotalRuns;
  private int OverTotalWickets;
  private boolean WasOverPowerplay;
  
public OverByOverData() {
	super();
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
public boolean getWasOverPowerplay() {
	return WasOverPowerplay;
}
public void setWasOverPowerplay(boolean wasOverPowerplay) {
	WasOverPowerplay = wasOverPowerplay;
}
@Override
public String toString() {
	return "OverByOverData [InningNumber=" + InningNumber + ", OverNumber=" + OverNumber + ", OverTotalRuns="
			+ OverTotalRuns + ", OverTotalWickets=" + OverTotalWickets + ", WasOverPowerplay=" + WasOverPowerplay + "]";
}

}