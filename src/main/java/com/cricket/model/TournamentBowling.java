package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cricket.util.CricketUtil;

@XmlRootElement(name="tournamentBowling")
@XmlAccessorType(XmlAccessType.FIELD)
public class TournamentBowling implements Comparable<TournamentBowling> {

  @XmlElement(name = "playerId")
  private int playerId;
  
  @XmlElement(name = "matches")
  private int matches;
  
  @XmlElement(name = "wickets")
  private int wickets;
  
  @XmlElement(name = "RunsConceded")
  private double RunsConceded;
  
  @XmlElement(name = "BallsBowled")
  private double BallsBowled;
  
  @XmlElement(name = "Economy")
  private double economy;
  
  @XmlTransient
  private Player player;

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

public int getWickets() {
	return wickets;
}

public void setWickets(int wickets) {
	this.wickets = wickets;
}

public double getRunsConceded() {
	return RunsConceded;
}

public void setRunsConceded(double runsConceded) {
	RunsConceded = runsConceded;
}

public double getBallsBowled() {
	return BallsBowled;
}

public void setBallsBowled(double ballsBowled) {
	BallsBowled = ballsBowled;
}

public double getEconomy() {
	return economy;
}

public void setEconomy(double economy) {
	this.economy = economy;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

@Override
public int compareTo(TournamentBowling tor) {
	return (int) (tor.getWickets() - this.getWickets());
	
}

}