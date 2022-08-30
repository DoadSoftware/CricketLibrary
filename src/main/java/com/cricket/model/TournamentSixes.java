package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.cricket.util.CricketUtil;

@XmlRootElement(name="tournamentSixes")
@XmlAccessorType(XmlAccessType.FIELD)
public class TournamentSixes implements Comparable<TournamentSixes> {

  @XmlElement(name = "playerId")
  private int playerId;
  
  @XmlElement(name = "matches")
  private int matches;
  
  @XmlElement(name = "sixes")
  private int sixes;
  
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

public int getSixes() {
	return sixes;
}

public void setSixes(int sixes) {
	this.sixes = sixes;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

@Override
public int compareTo(TournamentSixes tor) {
	return (int) (tor.getSixes() - this.getSixes());
	
}

}