package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadToHeadPlayer implements Cloneable {

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
  private String MatchFileName;

  private Team Team;
  private Team OpponentTeam;
  
  private String venue;
  
  private String inningStarted;
  private String dismissed;
  
  private int tapeBall_balls;
  private int tapeBall_runs;
  private int tapeBall_wickets;
  private int tapeBall_dotsBall;
  
  private boolean isTeamVsTeam = false;

public HeadToHeadPlayer(int playerId, int runs, int ballsFaced, int batdots, int ones, int twos, int threes, int fours,
		int sixes, int wickets, int runsConceded, int ballsBowled, int maidens, int balldots, String matchFileName,
		com.cricket.model.Team team, com.cricket.model.Team opponentTeam, String inningStarted, String dismissed, String venue) {
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
	this.MatchFileName = matchFileName;
	this.Team = team;
	this.OpponentTeam = opponentTeam;
	this.inningStarted = inningStarted;
	this.dismissed = dismissed;
	this.venue = venue;
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
	if(runs < 0) {
		this.runs = 0;
	}
	else {
		this.runs = runs;
	}
}

public int getBallsFaced() {
	return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
	if(ballsFaced < 0) {
		this.ballsFaced = 0;
	}
	else {
		this.ballsFaced = ballsFaced;
	}
}

public int getBatdots() {
	return batdots;
}

public void setBatdots(int batdots) {
	if(batdots < 0) {
		this.batdots = 0;
	}
	else {
		this.batdots = batdots;
	}
}

public int getBalldots() {
	return balldots;
}

public void setBalldots(int balldots) {
	if(balldots < 0) {
		this.balldots = 0;
	}
	else {
		this.balldots = balldots;
	}
}

public int getOnes() {
	return ones;
}

public void setOnes(int ones) {
	if(ones < 0) {
		this.ones = 0;
	}
	else {
		this.ones = ones;
	}
}

public int getTwos() {
	return twos;
}

public void setTwos(int twos) {
	if(twos < 0) {
		this.twos = 0;
	}
	else {
		this.twos = twos;
	}
}

public int getThrees() {
	return threes;
}

public void setThrees(int threes) {
	if(threes < 0) {
		this.threes = 0;
	}
	else {
		this.threes = threes;
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

public int getRunsConceded() {
	return runsConceded;
}

public void setRunsConceded(int runsConceded) {
	if(runsConceded < 0) {
		this.runsConceded = 0;
	}
	else {
		this.runsConceded = runsConceded;
	}
}

public int getBallsBowled() {
	return ballsBowled;
}

public void setBallsBowled(int ballsBowled) {
	if(ballsBowled < 0) {
		this.ballsBowled = 0;
	}
	else {
		this.ballsBowled = ballsBowled;
	}
}

public int getMaidens() {
	return maidens;
}

public void setMaidens(int maidens) {
	if(maidens < 0) {
		this.maidens = 0;
	}
	else {
		this.maidens = maidens;
	}
}

public String getMatchFileName() {
	return MatchFileName;
}

public void setMatchFileName(String matchFileName) {
	MatchFileName = matchFileName;
}

public Team getTeam() {
	return Team;
}

public void setTeam(Team team) {
	Team = team;
}

public Team getOpponentTeam() {
	return OpponentTeam;
}

public void setOpponentTeam(Team opponentTeam) {
	OpponentTeam = opponentTeam;
}

public String getInningStarted() {
	return inningStarted;
}

public void setInningStarted(String inningStarted) {
	this.inningStarted = inningStarted;
}

public String getDismissed() {
	return dismissed;
}

public void setDismissed(String dismissed) {
	this.dismissed = dismissed;
}

public String getVenue() {
	return venue;
}

public void setVenue(String venue) {
	this.venue = venue;
}

public int getTapeBall_balls() {
	return tapeBall_balls;
}

public void setTapeBall_balls(int tapeBall_balls) {
	if(tapeBall_balls < 0) {
		this.tapeBall_balls = 0;
	}
	else {
		this.tapeBall_balls = tapeBall_balls;
	}
}

public int getTapeBall_runs() {
	return tapeBall_runs;
}

public void setTapeBall_runs(int tapeBall_runs) {
	if(tapeBall_runs < 0) {
		this.tapeBall_runs = 0;
	}
	else {
		this.tapeBall_runs = tapeBall_runs;
	}
}

public int getTapeBall_wickets() {
	return tapeBall_wickets;
}

public void setTapeBall_wickets(int tapeBall_wickets) {
	if(tapeBall_wickets < 0) {
		this.tapeBall_wickets = 0;
	}
	else {
		this.tapeBall_wickets = tapeBall_wickets;
	}
}

public int getTapeBall_dotsBall() {
	return tapeBall_dotsBall;
}

public void setTapeBall_dotsBall(int tapeBall_dotsBall) {
	if(tapeBall_dotsBall < 0) {
		this.tapeBall_dotsBall = 0;
	}
	else {
		this.tapeBall_dotsBall = tapeBall_dotsBall;
	}
}

public boolean isTeamVsTeam() {
	return isTeamVsTeam;
}

public void setTeamVsTeam(boolean isTeamVsTeam) {
	this.isTeamVsTeam = isTeamVsTeam;
}

@Override
public HeadToHeadPlayer clone() throws CloneNotSupportedException {
    HeadToHeadPlayer clone = null;
    try
    {
        clone = (HeadToHeadPlayer) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

@Override
public String toString() {
	return "HeadToHead [playerId=" + playerId + ", tapeBall_balls=" + tapeBall_balls + ", tapeBall_runs=" + tapeBall_runs
			+ ", tapeBall_wickets=" + tapeBall_wickets + ", tapeBall_dotsBall=" + tapeBall_dotsBall + "]";
}

}