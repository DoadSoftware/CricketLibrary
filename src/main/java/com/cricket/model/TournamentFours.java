package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cricket.util.CricketUtil;

@XmlRootElement(name="tournamentFours")
@XmlAccessorType(XmlAccessType.FIELD)
public class TournamentFours implements Comparable<TournamentFours> {

  @XmlElement(name = "playerId")
  private int playerId;
  
  @XmlElement(name = "matches")
  private int matches;
  
  @XmlElement(name = "fours")
  private int fours;
  
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

public int getFours() {
	return fours;
}

public void setFours(int fours) {
	this.fours = fours;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

@Override
public int compareTo(TournamentFours tor) {
	return (int) (tor.getFours() - this.getFours());
	
}

}