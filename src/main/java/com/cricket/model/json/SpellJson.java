package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "spell", namespace = "spells")
public class SpellJson {

  @JsonProperty(value = "spellNumber")
  private int spellNumber;

  @JsonProperty(value = "playerId")
  private int playerId;

  @JsonProperty(value = "balls")
  private int balls;

  @JsonProperty(value = "runs")
  private int runs;
  
  @JsonProperty(value = "wickets")
  private int wickets;

  @JsonProperty(value = "maidens")
  private int maidens;

public SpellJson() {
	super();
}

public SpellJson(int spellNumber, int playerId) {
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