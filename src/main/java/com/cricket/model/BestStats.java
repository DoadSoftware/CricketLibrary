package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement(name="bestStats")
@XmlAccessorType(XmlAccessType.FIELD)
public class BestStats{

  @XmlElement(name = "playerId")
  private int playerId;
  
  @XmlElement(name = "batsmanBestEquation")
  private int batsmanBestEquation;
  
  @XmlElement(name = "bowlerBestEquation")
  private int bowlerBestEquation;
  
  @XmlElement(name = "ballsBowled")
  private int ballsBowled;
  
  @XmlElement(name = "ballsFaced")
  private int ballsFaced;
  
  @XmlElement(name = "opponentTeam")
  private Team opponentTeam;
  
  @XmlTransient
  private Player player;

public BestStats() {
	super();
}

public BestStats(int playerId, int batsmanBestEquation, int bowlerBestEquation, int ballsBowled, int ballsFaced,
		Team opponentTeam, Player player) {
	super();
	this.playerId = playerId;
	this.batsmanBestEquation = batsmanBestEquation;
	this.bowlerBestEquation = bowlerBestEquation;
	this.ballsBowled = ballsBowled;
	this.ballsFaced = ballsFaced;
	this.opponentTeam = opponentTeam;
	this.player = player;
}

public int getBatsmanBestScoreSortData() {
	return 1000 * this.getBatsmanBestEquation() + 1000 - this.getBallsFaced();
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public int getBatsmanBestEquation() {
	return batsmanBestEquation;
}

public void setBatsmanBestEquation(int batsmanBestEquation) {
	this.batsmanBestEquation = batsmanBestEquation;
}

public int getBowlerBestEquation() {
	return bowlerBestEquation;
}

public void setBowlerBestEquation(int bowlerBestEquation) {
	this.bowlerBestEquation = bowlerBestEquation;
}

public int getBallsFaced() {
	return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
	this.ballsFaced = ballsFaced;
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

public int getBallsBowled() {
	return ballsBowled;
}

public void setBallsBowled(int ballsBowled) {
	this.ballsBowled = ballsBowled;
}

}