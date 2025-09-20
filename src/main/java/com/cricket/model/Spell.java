package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Spell {

  private int spellNumber;

  private int playerId;

  private int balls;

  private int runs;
  
  private int wickets;

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

public int getBalls() {
	return balls;
}

public void setBalls(int balls) {
	if(balls < 0) {
		this.balls = 0;
	}
	else {
		this.balls = balls;
	}
}

public int getRuns() {
	return runs;
}

public void setRuns(int runs) {
	if(runs < 0) {
		this.runs = 0;
	}
	else {
		this.runs = runs;
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
  
}