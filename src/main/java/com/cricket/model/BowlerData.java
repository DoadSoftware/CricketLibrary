package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlerData {

  private int playerId;
  
  private int runs;
  
  private int fours;
  
  private int sixes;
   
  private Player player;

public BowlerData(int playerId, int runs, int fours, int sixes, Player player) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.player = player;
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

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
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

@Override
public String toString() {
	return "BowlerData [playerId=" + playerId + ", runs=" + runs + ", player=" + player + "]";
}

}