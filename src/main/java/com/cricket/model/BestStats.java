package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BestStats{

  private int playerId;
  private int bestEquation;
  private Team opponentTeam;
  private Ground whichVenue;
  private String matchNumber;
  private int runs;
  private int wickets;
  private int balls;
  private String status;
  private boolean not_out;
  
  @JsonIgnore
  private Player player;

public BestStats() {
	super();
}

public BestStats(int playerId, int bestEquation, int balls, Team opponentTeam, Ground whichVenue, String matchNumber, Player player ,String status) {
	super();
	this.playerId = playerId;
	this.bestEquation = bestEquation;
	this.opponentTeam = opponentTeam;
	this.whichVenue = whichVenue;
	this.matchNumber = matchNumber;
	this.balls = balls;
	this.status = status;
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

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public String getMatchNumber() {
	return matchNumber;
}

public void setMatchNumber(String matchNumber) {
	this.matchNumber = matchNumber;
}

public Ground getWhichVenue() {
	return whichVenue;
}

public void setWhichVenue(Ground whichVenue) {
	this.whichVenue = whichVenue;
}

@Override
public String toString() {
	return "BestStats [playerId=" + playerId + ", bestEquation=" + bestEquation + ", opponentTeam=" + opponentTeam
			+ ", matchNumber=" + matchNumber + ", runs=" + runs + ", wickets=" + wickets + ", balls=" + balls
			+ ", status=" + status + ", not_out=" + not_out + ", player=" + player + "]";
}

}