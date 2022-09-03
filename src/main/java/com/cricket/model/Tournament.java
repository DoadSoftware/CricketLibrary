package com.cricket.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cricket.util.CricketUtil;

@XmlRootElement(name="tournament")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tournament implements Cloneable {

  @XmlElement(name = "playerId")
  private int playerId;
  
  @XmlElement(name = "matches")
  private int matches;
  
  @XmlElement(name = "runs")
  private int runs;
  
  @XmlElement(name = "fours")
  private int fours;
  
  @XmlElement(name = "sixes")
  private int sixes;
  
  @XmlElement(name = "wickets")
  private int wickets;
  
  @XmlElement(name = "runsConceded")
  private int runsConceded;
  
  @XmlElement(name = "ballsBowled")
  private int ballsBowled;
  
  @XmlElement(name = "ballsFaced")
  private int ballsFaced;
  
  @XmlElement(name = "strikeRate")
  private double strikeRate;
  
  @XmlElement(name = "opponentTeam")
  private String opponentTeam;
  
  @XmlElement(name = "status")
  private String status;
  
  @XmlElement(name = "balls")
  private int balls;
  
  @XmlTransient
  private Player player;
  
 private List<Integer> best_Stats;

 public int getBatsmanScoreSortData() {
		int sortData = this.getRuns();
		if(this.getStatus() != null && this.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
			sortData = sortData + 1;
		}
		return 1000 * sortData + 1000 - this.getBalls();
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

public double getStrikeRate() {
	return strikeRate;
}

public void setStrikeRate(double strikeRate) {
	this.strikeRate = strikeRate;
}

public String getOpponentTeam() {
	return opponentTeam;
}

public void setOpponentTeam(String opponentTeam) {
	this.opponentTeam = opponentTeam;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public int getBalls() {
	return balls;
}

public void setBalls(int balls) {
	this.balls = balls;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

public List<Integer> getBest_Stats() {
	return best_Stats;
}

public void setBest_Stats(List<Integer> best_Stats) {
	this.best_Stats = best_Stats;
}

@Override
public Object clone() throws CloneNotSupportedException {
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