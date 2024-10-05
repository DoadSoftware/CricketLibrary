package com.WTV;

public class Wtv_Txt {

  private String playerName;
  private int runs;
  private int ballsFaced;
  private int fours;
  private int sixes;
  private int wickets;
  private int runsConceded;
  private int ballsBowled;
  
  
public Wtv_Txt(String playerName, int runs, int ballsFaced, int fours, int sixes, int wickets, int runsConceded,
		int ballsBowled) {
	super();
	this.playerName = playerName;
	this.runs = runs;
	this.ballsFaced = ballsFaced;
	this.fours = fours;
	this.sixes = sixes;
	this.wickets = wickets;
	this.runsConceded = runsConceded;
	this.ballsBowled = ballsBowled;
}

public Wtv_Txt() {
	super();
}

public String getPlayerName() {
	return playerName;
}


public void setPlayerName(String playerName) {
	this.playerName = playerName;
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
}
