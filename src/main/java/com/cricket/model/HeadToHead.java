package com.cricket.model;

public class HeadToHead implements Cloneable {

  private int playerId;
  private int runs;
  private int ballsFaced;
  private int batdots;
  private int ones;
  private int twos;
  private int threes;
  private int fours;
  private int sixes;
  private int wickets;
  private int runsConceded;
  private int ballsBowled;
  private int maidens;
  private int balldots;
  


public HeadToHead(int playerId, int runs, int ballsFaced, int batdots, int ones, int twos, int threes, int fours,
		int sixes, int wickets, int runsConceded, int ballsBowled, int maidens, int balldots) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.ballsFaced = ballsFaced;
	this.batdots = batdots;
	this.ones = ones;
	this.twos = twos;
	this.threes = threes;
	this.fours = fours;
	this.sixes = sixes;
	this.wickets = wickets;
	this.runsConceded = runsConceded;
	this.ballsBowled = ballsBowled;
	this.maidens = maidens;
	this.balldots = balldots;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public int getRuns() {
	return runs;
}

public void setRuns(int runs) {
	this.runs = runs;
}

public int getBallsFaced() {
	return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
	this.ballsFaced = ballsFaced;
}

public int getBatdots() {
	return batdots;
}

public void setBatdots(int batdots) {
	this.batdots = batdots;
}

public int getBalldots() {
	return balldots;
}

public void setBalldots(int balldots) {
	this.balldots = balldots;
}

public int getOnes() {
	return ones;
}

public void setOnes(int ones) {
	this.ones = ones;
}

public int getTwos() {
	return twos;
}

public void setTwos(int twos) {
	this.twos = twos;
}

public int getThrees() {
	return threes;
}

public void setThrees(int threes) {
	this.threes = threes;
}

public int getFours() {
	return fours;
}

public void setFours(int fours) {
	this.fours = fours;
}

public int getSixes() {
	return sixes;
}

public void setSixes(int sixes) {
	this.sixes = sixes;
}

public int getWickets() {
	return wickets;
}

public void setWickets(int wickets) {
	this.wickets = wickets;
}

public int getRunsConceded() {
	return runsConceded;
}

public void setRunsConceded(int runsConceded) {
	this.runsConceded = runsConceded;
}

public int getBallsBowled() {
	return ballsBowled;
}

public void setBallsBowled(int ballsBowled) {
	this.ballsBowled = ballsBowled;
}

public int getMaidens() {
	return maidens;
}

public void setMaidens(int maidens) {
	this.maidens = maidens;
}

@Override
public HeadToHead clone() throws CloneNotSupportedException {
    HeadToHead clone = null;
    try
    {
        clone = (HeadToHead) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

}