package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TapeBall{

  private String player;
  private String opponentTeam;
  private String matchNumber;
  private int runs;
  private int wickets;
  
public TapeBall() {
	super();
}

public TapeBall(String player, String opponentTeam, String matchNumber, int runs, int wickets) {
	super();
	this.player = player;
	this.opponentTeam = opponentTeam;
	this.matchNumber = matchNumber;
	this.runs = runs;
	this.wickets = wickets;
}

public String getPlayer() {
	return player;
}

public void setPlayer(String player) {
	this.player = player;
}

public String getOpponentTeam() {
	return opponentTeam;
}

public void setOpponentTeam(String opponentTeam) {
	this.opponentTeam = opponentTeam;
}

public String getMatchNumber() {
	return matchNumber;
}

public void setMatchNumber(String matchNumber) {
	this.matchNumber = matchNumber;
}

public int getRuns() {
	return runs;
}

public void setRuns(int runs) {
	this.runs = runs;
}

public int getWickets() {
	return wickets;
}

public void setWickets(int wickets) {
	this.wickets = wickets;
}

@Override
public String toString() {
	return "TapeBall [player=" + player + ", opponentTeam=" + opponentTeam + ", matchNumber=" + matchNumber + ", runs="
			+ runs + ", wickets=" + wickets + "]";
}

}