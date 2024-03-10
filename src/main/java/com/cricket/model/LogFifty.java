package com.cricket.model;

public class LogFifty implements Cloneable{

  private int playerId;
  private Player player;
  private String opponentTeam;
  private String matchNumber;
  private int runs;
  private int wickets;
  
  private int teamId;
  private int matches;
  private int challengeRuns;
  
public LogFifty() {
	super();
}

public LogFifty(int playerId, Player player, String opponentTeam, String matchNumber, int runs, int wickets) {
	super();
	this.playerId = playerId;
	this.player = player;
	this.opponentTeam = opponentTeam;
	this.matchNumber = matchNumber;
	this.runs = runs;
	this.wickets = wickets;
}

public LogFifty(int teamId, int matches, int challengeRuns) {
	super();
	this.teamId = teamId;
	this.matches = matches;
	this.challengeRuns = challengeRuns;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
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

public int getTeamId() {
	return teamId;
}

public void setTeamId(int teamId) {
	this.teamId = teamId;
}

public int getChallengeRuns() {
	return challengeRuns;
}

public void setChallengeRuns(int challengeRuns) {
	this.challengeRuns = challengeRuns;
}

public int getMatches() {
	return matches;
}

public void setMatches(int matches) {
	this.matches = matches;
}

@Override
public LogFifty clone() throws CloneNotSupportedException {
	LogFifty clone = null;
    try
    {
        clone = (LogFifty) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

}