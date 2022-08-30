package com.cricket.model;

import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="tournament")
@XmlAccessorType(XmlAccessType.FIELD)
public class Tournament implements Comparable<Tournament> {

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
  
  @XmlElement(name = "ballsFaced")
  private int ballsFaced;
  
  @XmlElement(name = "strikeRate")
  private double strikeRate;
  
  @XmlElement(name = "opponentTeam")
  private String opponentTeam;
  
  @XmlTransient
  private Player player;
  
 private List<Integer> best_Stats;

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
public int compareTo(Tournament tor) {
	return (int) (tor.getRuns() - this.getRuns());
}

}