package com.cricket.model.json;

public class BestStats{

  private int playerId;
  
  private int bestEquation;
  
  private Team opponentTeam;

  private int runs;
  private int wickets;
  private int balls;
  private boolean not_out;
  
  private Player player;

public BestStats() {
	super();
}

public BestStats(int playerId, int bestEquation, int balls, Team opponentTeam, Player player) {
	super();
	this.playerId = playerId;
	this.bestEquation = bestEquation;
	this.balls = balls;
	this.opponentTeam = opponentTeam;
	this.player = player;
}

public boolean isNot_out() {
	return not_out;
}

public void setNot_out(boolean not_out) {
	this.not_out = not_out;
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

public int getBalls() {
	return balls;
}

public void setBalls(int balls) {
	this.balls = balls;
}

public int getBestEquation() {
	return bestEquation;
}

public void setBestEquation(int bestEquation) {
	this.bestEquation = bestEquation;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public Team getOpponentTeam() {
	return opponentTeam;
}

public void setOpponentTeam(Team opponentTeam) {
	this.opponentTeam = opponentTeam;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

@Override
public String toString() {
	return "BestStats [playerId=" + playerId + ", bestEquation=" + bestEquation + ", opponentTeam=" + opponentTeam
			+ ", runs=" + runs + ", wickets=" + wickets + ", balls=" + balls + ", not_out=" + not_out + ", player="
			+ player + "]";
}

}