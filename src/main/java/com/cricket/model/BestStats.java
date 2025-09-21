package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BestStats implements Cloneable{

  private int playerId;
  private int bestEquation;
  private Team opponentTeam;
  private Ground whichVenue;
  private String matchNumber;
  private int runs;
  private int fours;
  private int sixes;
  private int wickets;
  private int balls;
  private String status;
  private boolean not_out;
  
  private int teamId;
  private int matches;
  private int challengeRuns;
  
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

public BestStats(int playerId, Player player, Team opponentTeam, String matchNumber, int runs, int wickets) {
	super();
	this.playerId = playerId;
	this.player = player;
	this.opponentTeam = opponentTeam;
	this.matchNumber = matchNumber;
	this.runs = runs;
	this.wickets = wickets;
}

public BestStats(int teamId, int matches, int challengeRuns) {
	super();
	this.teamId = teamId;
	this.matches = matches;
	this.challengeRuns = challengeRuns;
}

public BestStats(int playerId, int runs, int balls, int fours, int sixes, Player player) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.player = player;
	this.balls = balls;
}

public int getBatsmanStrikeRateSortData() {
	int temp = 0;
	if(this.getBalls() >= 1) {
		temp = (100*this.getRuns())/this.getBalls();
	}
	if(temp > 32000) {
		return 0 ;
	}else {
		return temp;
	}
}

public int getBowlerEconomySortData() {
	int temp = 0;
	if(this.getBalls()>0) {
		temp = (1000*this.getRuns())/this.getBalls();
	}
	if(temp > 32000) {
		return 32000;
	}else {
		return 20000-temp;
	}
}

public int getTeamId() {
	return teamId;
}

public void setTeamId(int teamId) {
	this.teamId = teamId;
}

public int getMatches() {
	return matches;
}

public void setMatches(int matches) {
	this.matches = matches;
}

public int getChallengeRuns() {
	return challengeRuns;
}

public void setChallengeRuns(int challengeRuns) {
	if(challengeRuns < 0) {
		this.challengeRuns = 0;
	}
	else {
		this.challengeRuns = challengeRuns;
	}
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
	if(runs < 0) {
		this.runs = 0;
	}
	else {
		this.runs = runs;
	}
}

public int getFours() {
	return fours;
}

public void setFours(int fours) {
	if(fours < 0) {
		this.fours = 0;
	}
	else {
		this.fours = fours;
	}
}

public int getSixes() {
	return sixes;
}

public void setSixes(int sixes) {
	if(sixes < 0) {
		this.sixes = 0;
	}
	else {
		this.sixes = sixes;
	}
}

public int getWickets() {
	return wickets;
}

public void setWickets(int wickets) {
	if(wickets < 0) {
		this.wickets = 0;
	}
	else {
		this.wickets = wickets;
	}
}

public int getBalls() {
	return balls;
}

public void setBalls(int balls) {
	if(balls < 0) {
		this.balls = 0;
	}
	else {
		this.balls = balls;
	}
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

@Override
public BestStats clone() throws CloneNotSupportedException {
	BestStats clone = null;
    try
    {
        clone = (BestStats) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

}