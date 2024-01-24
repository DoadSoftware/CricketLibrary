package com.cricket.model;

import java.util.List;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Tournament implements Cloneable {

  private int playerId;
  
  private int matches;
  
  private int innings;
  
  private int not_out;
  
  private int runs;
  
  private int fours;
  
  private int sixes;
  
  private int fifty;
  
  private int hundreds;
  
  private int wickets;
  
  private int runsConceded;
  
  private int ballsBowled;
  
  private int threeWicketHaul;
  
  private int fiveWicketHaul;
  
  private int ballsFaced;
  
  private int dots;
  
  private String notOut;
  
  private int runs_against_pace;
  private int balls_against_pace;
  
  private int runs_against_spin;
  private int balls_against_spin;
  
  @JsonIgnore
  private Player player;
  
  private int tournament_fours;
  
  private int tournament_sixes;
  
  private List<BestStats> batsman_best_Stats;

  private List<BestStats> bowler_best_Stats;

public Tournament() {
	super();
}



public Tournament(int playerId, int runs, int fours, int sixes, int innings, int not_out, int fifty, int hundreds, int wickets, int runsConceded,
		int ballsBowled, int ballsFaced, int dots, int threeWicketHaul, int fiveWicketHaul, String notOut, int runs_against_pace, int balls_against_pace,
		int runs_against_spin, int balls_against_spin, Player player, List<BestStats> batsman_best_Stats,
		List<BestStats> bowler_best_Stats) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.innings = innings;
	this.not_out = not_out;
	this.fifty = fifty;
	this.hundreds = hundreds;
	this.wickets = wickets;
	this.runsConceded = runsConceded;
	this.ballsBowled = ballsBowled;
	this.ballsFaced = ballsFaced;
	this.dots = dots;
	this.threeWicketHaul = threeWicketHaul;
	this.fiveWicketHaul = fiveWicketHaul;
	this.notOut = notOut;
	this.runs_against_pace = runs_against_pace;
	this.balls_against_pace = balls_against_pace;
	this.runs_against_spin = runs_against_spin;
	this.balls_against_spin = balls_against_spin;
	this.player = player;
	this.batsman_best_Stats = batsman_best_Stats;
	this.bowler_best_Stats = bowler_best_Stats;
}

public int getBatsmanScoreSortData() {
	int sortData = this.getRuns();
	if(this.getNotOut() != null && this.getNotOut().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
		sortData = sortData + 1;
	}
	return 1000 * sortData + 1000 - this.getBallsFaced();
}

public int getBatsmanStrikeRateSortData() {
	int temp = 0;
	if(this.getBallsFaced() >= 1) {
		temp = (100*this.getRuns())/this.getBallsFaced();
	}
	if(temp > 32000) {
		return 0 ;
	}else {
		return temp;
	}
}

public int getBowlerFigureSortData() {	
	return 1000 * this.getWickets() - this.getRunsConceded();
}

public int getBowlerEconomySortData() {
	int temp = 0;
	if(this.getBallsBowled()>0) {
		temp = (1000*this.getRunsConceded())/this.getBallsBowled();
	}
	if(temp > 32000) {
		return 32000;
	}else {
		return 20000-temp;
	}
}
public int getBowlerStrikeRateSortData() {
	int temp = 0;
	if(this.getBallsBowled() >= 1 && this.getWickets() >= 1) {
		temp = (100*this.getBallsBowled())/this.getWickets();
	}
	if(temp > 32000) {
		return 0 ;
	}else {
		return temp;
	}
}

public int getBatsmanFoursSortData() {
	return 1000 * this.getFours() - this.getMatches();
}

public int getBatsmanSixesSortData() {
	return 1000 * this.getSixes() - this.getMatches();
}

public List<BestStats> getBatsman_best_Stats() {
	return batsman_best_Stats;
}

public void setBatsman_best_Stats(List<BestStats> batsman_best_Stats) {
	this.batsman_best_Stats = batsman_best_Stats;
}

public List<BestStats> getBowler_best_Stats() {
	return bowler_best_Stats;
}

public void setBowler_best_Stats(List<BestStats> bowler_best_Stats) {
	this.bowler_best_Stats = bowler_best_Stats;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public int getMatches() {
	return matches;
}

public void setMatches(int matches) {
	this.matches = matches;
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

public int getBallsFaced() {
	return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
	this.ballsFaced = ballsFaced;
}

public String getNotOut() {
	return notOut;
}

public void setNotOut(String notOut) {
	this.notOut = notOut;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

public int getDots() {
	return dots;
}

public void setDots(int dots) {
	this.dots = dots;
}

public int getTournament_fours() {
	return tournament_fours;
}

public void setTournament_fours(int tournament_fours) {
	this.tournament_fours = tournament_fours;
}

public int getTournament_sixes() {
	return tournament_sixes;
}

public void setTournament_sixes(int tournament_sixes) {
	this.tournament_sixes = tournament_sixes;
}

public int getRuns_against_pace() {
	return runs_against_pace;
}

public void setRuns_against_pace(int runs_against_pace) {
	this.runs_against_pace = runs_against_pace;
}

public int getBalls_against_pace() {
	return balls_against_pace;
}

public void setBalls_against_pace(int balls_against_pace) {
	this.balls_against_pace = balls_against_pace;
}

public int getRuns_against_spin() {
	return runs_against_spin;
}

public void setRuns_against_spin(int runs_against_spin) {
	this.runs_against_spin = runs_against_spin;
}

public int getBalls_against_spin() {
	return balls_against_spin;
}

public void setBalls_against_spin(int balls_against_spin) {
	this.balls_against_spin = balls_against_spin;
}

public int getFifty() {
	return fifty;
}

public void setFifty(int fifty) {
	this.fifty = fifty;
}

public int getHundreds() {
	return hundreds;
}

public void setHundreds(int hundreds) {
	this.hundreds = hundreds;
}

public int getInnings() {
	return innings;
}

public void setInnings(int innings) {
	this.innings = innings;
}

public int getNot_out() {
	return not_out;
}

public void setNot_out(int not_out) {
	this.not_out = not_out;
}

public int getThreeWicketHaul() {
	return threeWicketHaul;
}

public void setThreeWicketHaul(int threeWicketHaul) {
	this.threeWicketHaul = threeWicketHaul;
}

public int getFiveWicketHaul() {
	return fiveWicketHaul;
}

public void setFiveWicketHaul(int fiveWicketHaul) {
	this.fiveWicketHaul = fiveWicketHaul;
}

@Override
public String toString() {
	return "Tournament [playerId=" + playerId + ", matches=" + matches + ", runs=" + runs + ", fours=" + fours
			+ ", sixes=" + sixes + ", wickets=" + wickets + ", runsConceded=" + runsConceded + ", ballsBowled="
			+ ballsBowled + ", ballsFaced=" + ballsFaced + ", dots=" + dots + ", notOut=" + notOut + ", player="
			+ player + ", batsman_best_Stats=" + batsman_best_Stats + ", bowler_best_Stats=" + bowler_best_Stats + "]";
}

@Override
public Tournament clone() throws CloneNotSupportedException {
    Tournament clone = null;
    try
    {
        clone = (Tournament) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

}