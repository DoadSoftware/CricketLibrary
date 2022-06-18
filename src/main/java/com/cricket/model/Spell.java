package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="spell")
@XmlAccessorType(XmlAccessType.FIELD)
public class Spell {

  @XmlElement(name = "spellNumber")
  private int spellNumber;

  @XmlElement(name = "playerId")
  private int playerId;

  @XmlElement(name = "overs")
  private int overs;

  @XmlElement(name = "balls")
  private int balls;

  @XmlElement(name = "runs")
  private int runs;
  
  @XmlElement(name = "wickets")
  private int wickets;

  @XmlElement(name = "maidens")
  private int maidens;

public Spell() {
	super();
}

public Spell(int spellNumber, int playerId) {
	super();
	this.spellNumber = spellNumber;
	this.playerId = playerId;
}

public int getSpellNumber() {
	return spellNumber;
}

public void setSpellNumber(int spellNumber) {
	this.spellNumber = spellNumber;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public int getOvers() {
	return overs;
}

public void setOvers(int overs) {
	this.overs = overs;
}

public int getBalls() {
	return balls;
}

public void setBalls(int balls) {
	this.balls = balls;
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

public int getMaidens() {
	return maidens;
}

public void setMaidens(int maidens) {
	this.maidens = maidens;
}
  
}