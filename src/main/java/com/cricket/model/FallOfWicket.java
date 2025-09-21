package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FallOfWicket implements Comparable<FallOfWicket>
{
  private int fowNumber;
  
  private int fowPlayerID;

  private int fowRuns;
  
  private int fowOvers;

  private int fowBalls;
  
  private String fowDateTime;
  
public FallOfWicket() {
	super();
}

public FallOfWicket(int fowNumber, int fowPlayerID, int fowRuns, int fowOvers, int fowBalls, String fowDateTime) {
	super();
	this.fowNumber = fowNumber;
	this.fowPlayerID = fowPlayerID;
	this.fowRuns = fowRuns;
	this.fowOvers = fowOvers;
	this.fowBalls = fowBalls;
	this.fowDateTime = fowDateTime;
}

public String getFowDateTime() {
	return fowDateTime;
}

public void setFowDateTime(String fowDateTime) {
	this.fowDateTime = fowDateTime;
}

public int getFowNumber() {
	return fowNumber;
}

public void setFowNumber(int fowNumber) {
	if(fowNumber < 0) {
		this.fowNumber = 0;
	}
	else {
		this.fowNumber = fowNumber;
	}
}

public int getFowPlayerID() {
	return fowPlayerID;
}

public void setFowPlayerID(int fowPlayerID) {
	if(fowPlayerID < 0) {
		this.fowPlayerID = 0;
	}
	else {
		this.fowPlayerID = fowPlayerID;
	}
}

public int getFowRuns() {
	return fowRuns;
}

public void setFowRuns(int fowRuns) {
	if(fowRuns < 0) {
		this.fowRuns = 0;
	}
	else {
		this.fowRuns = fowRuns;
	}
}

public int getFowOvers() {
	return fowOvers;
}

public void setFowOvers(int fowOvers) {
	if(fowOvers < 0) {
		this.fowOvers = 0;
	}
	else {
		this.fowOvers = fowOvers;
	}
}

public int getFowBalls() {
	return fowBalls;
}

public void setFowBalls(int fowBalls) {
	if(fowBalls < 0) {
		this.fowBalls = 0;
	}
	else {
		this.fowBalls = fowBalls;
	}
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