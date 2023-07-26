package com.cricket.model;

public class FallOfWicket implements Comparable<FallOfWicket>
{

  private int fowNumber;
  
  private int fowPlayerID;

  private int fowRuns;
  
  private int fowOvers;

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

@Override
public String toString() {
	return "FallOfWicket [fowNumber=" + fowNumber + ", fowPlayerID=" + fowPlayerID + ", fowRuns=" + fowRuns
			+ ", fowOvers=" + fowOvers + ", fowBalls=" + fowBalls + "]";
}

}