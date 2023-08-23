package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "fallOfWicket", namespace = "fallOfWickets")
public class FallOfWicketJson implements Comparable<FallOfWicketJson>
{

  @JsonProperty(value ="fowNumber")
  private int fowNumber;
  
  @JsonProperty(value ="fowPlayerID")
  private int fowPlayerID;

  @JsonProperty(value ="fowRuns")
  private int fowRuns;
  
  @JsonProperty(value ="fowOvers")
  private int fowOvers;

  @JsonProperty(value ="fowBalls")
  private int fowBalls;

public FallOfWicketJson() {
	super();
}

public FallOfWicketJson(int fowNumber, int fowPlayerID, int fowRuns, int fowOvers, int fowBalls) {
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
public int compareTo(FallOfWicketJson fow) {
	return (int) (this.getFowNumber()-fow.getFowNumber());
}

}