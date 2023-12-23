package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlerData {

  private int playerId;
  
  private int runs;
   
  private Player player;

  
  
public BowlerData(int playerId, int runs, Player player) {
	super();
	this.playerId = playerId;
	this.runs = runs;
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

@Override
public String toString() {
	return "BowlerData [playerId=" + playerId + ", runs=" + runs + ", player=" + player + "]";
}

}