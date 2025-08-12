package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadToHeadTeam implements Cloneable {

  private int teamRuns;
  private int teamWickets;
  private int teamBalls;
  private int totalmatches;
  private String MatchFileName;
  private int P1_Run;
  private int P2_Run;
  private int P3_Run;
  private int P1_Wicket;
  private int P2_Wicket;
  private int P3_Wicket;

  private Team Team;
  private Team OpponentTeam;
  
  private String venue;
  
  
public HeadToHeadTeam() {
	super();
	// TODO Auto-generated constructor stub
}

public HeadToHeadTeam(int teamRuns, int teamWickets, int teamBalls, String matchFileName, com.cricket.model.Team team,
		com.cricket.model.Team opponentTeam, String venue) {
	super();
	this.teamRuns = teamRuns;
	this.teamWickets = teamWickets;
	this.teamBalls = teamBalls;
	MatchFileName = matchFileName;
	Team = team;
	OpponentTeam = opponentTeam;
	this.venue = venue;
}

public int getTotalmatches() {
	return totalmatches;
}

public void setTotalmatches(int totalmatches) {
	this.totalmatches = totalmatches;
}

public int getTeamRuns() {
	return teamRuns;
}


public void setTeamRuns(int teamRuns) {
	this.teamRuns = teamRuns;
}


public int getTeamWickets() {
	return teamWickets;
}


public void setTeamWickets(int teamWickets) {
	this.teamWickets = teamWickets;
}


public int getTeamBalls() {
	return teamBalls;
}


public void setTeamBalls(int teamBalls) {
	this.teamBalls = teamBalls;
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


public String getVenue() {
	return venue;
}


public void setVenue(String venue) {
	this.venue = venue;
}

public int getP1_Run() {
	return P1_Run;
}

public void setP1_Run(int p1_Run) {
	P1_Run = p1_Run;
}

public int getP2_Run() {
	return P2_Run;
}

public void setP2_Run(int p2_Run) {
	P2_Run = p2_Run;
}

public int getP3_Run() {
	return P3_Run;
}

public void setP3_Run(int p3_Run) {
	P3_Run = p3_Run;
}

public int getP1_Wicket() {
	return P1_Wicket;
}

public void setP1_Wicket(int p1_Wicket) {
	P1_Wicket = p1_Wicket;
}

public int getP2_Wicket() {
	return P2_Wicket;
}

public void setP2_Wicket(int p2_Wicket) {
	P2_Wicket = p2_Wicket;
}

public int getP3_Wicket() {
	return P3_Wicket;
}

public void setP3_Wicket(int p3_Wicket) {
	P3_Wicket = p3_Wicket;
}

@Override
public HeadToHeadTeam clone() throws CloneNotSupportedException {
    HeadToHeadTeam clone = null;
    try
    {
        clone = (HeadToHeadTeam) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

@Override
public String toString() {
	return "HeadToHeadTeam [teamRuns=" + teamRuns + ", teamWickets=" + teamWickets + ", teamBalls=" + teamBalls
			+ ", totalmatches=" + totalmatches + ", MatchFileName=" + MatchFileName + ", P1_Run=" + P1_Run + ", P2_Run="
			+ P2_Run + ", P3_Run=" + P3_Run + ", P1_Wicket=" + P1_Wicket + ", P2_Wicket=" + P2_Wicket + ", P3_Wicket="
			+ P3_Wicket + ", Team=" + Team + ", OpponentTeam=" + OpponentTeam + ", venue=" + venue + "]";
}

}