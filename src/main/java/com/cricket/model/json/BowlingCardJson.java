package com.cricket.model.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "bowlingCard", namespace = "bowlingCards")
public class BowlingCardJson implements Comparable<BowlingCardJson> {

  @JsonIgnoreProperties
  private Player player;

  @JsonProperty("bowlingPosition")
  private int bowlingPosition;
  
  @JsonProperty("status")
  private String status;

  @JsonProperty("economyRate")
  private String economyRate;
  
  @JsonProperty("bowling_end")
  private int bowling_end;
  
  @JsonProperty("overs")
  private int overs;

  @JsonProperty("runs")
  private int runs;
  
  @JsonProperty("balls")
  private int balls;

  @JsonProperty("wickets")
  private int wickets;

  @JsonProperty("playerId")
  private int playerId;
  
  @JsonProperty("wides")
  private int wides;
  
  @JsonProperty("noBalls")
  private int noBalls;
  
  @JsonProperty("runOuts")
  private int runOuts;
  
  @JsonProperty("stumpings")
  private int stumpings;
  
  @JsonProperty("catchAsFielder")
  private int catchAsFielder;
  
  @JsonProperty("catchAsBowler")
  private int catchAsBowler;

  @JsonProperty("maidens")
  private int maidens;

  @JsonProperty("dots")
  private int dots;

  @JsonProperty("totalRunsThisOver")
  private int totalRunsThisOver;

  @JsonProperty(value = "speed", namespace = "speeds")
  private List<SpeedJson> speeds;
  
public BowlingCardJson() {
	super();
}
public BowlingCardJson(Player player, int bowlingPosition, String status, int bowling_end) {
	super();
	this.player = player;
	this.playerId = player.getPlayerId();
	this.bowlingPosition = bowlingPosition;
	this.status = status;
	this.bowling_end = bowling_end;
}

public BowlingCardJson(int overs, int runs, int balls, int wickets, int playerId, int wides, int noBalls, int maidens,
		int dots) {
	super();
	this.overs = overs;
	this.runs = runs;
	this.balls = balls;
	this.wickets = wickets;
	this.playerId = playerId;
	this.wides = wides;
	this.noBalls = noBalls;
	this.maidens = maidens;
	this.dots = dots;
}
public int getBowlerFigureSortData() {
	return 1000 * this.getWickets() - this.getRuns();
}
public List<SpeedJson> getSpeeds() {
	return speeds;
}
public void setSpeeds(List<SpeedJson> speeds) {
	this.speeds = speeds;
}
public int getTotalRunsThisOver() {
	return totalRunsThisOver;
}
public void setTotalRunsThisOver(int totalRunsThisOver) {
	this.totalRunsThisOver = totalRunsThisOver;
}
public String getEconomyRate() {
	return economyRate;
}
public void setEconomyRate(String economyRate) {
	this.economyRate = economyRate;
}
public int getBowlingPosition() {
	return bowlingPosition;
}
public void setBowlingPosition(int bowlingPosition) {
	this.bowlingPosition = bowlingPosition;
}
public int getMaidens() {
	return maidens;
}
public void setMaidens(int maidens) {
	if(maidens < 0) {
		maidens = 0;
	}
	this.maidens = maidens;
}
public int getDots() {
	return dots;
}
public void setDots(int dots) {
	this.dots = dots;
}
public int getBowling_end() {
	return bowling_end;
}
public void setBowling_end(int bowling_end) {
	this.bowling_end = bowling_end;
}
public int getStumpings() {
	return stumpings;
}
public void setStumpings(int stumpings) {
	this.stumpings = stumpings;
}
public int getCatchAsFielder() {
	return catchAsFielder;
}
public void setCatchAsFielder(int catchAsFielder) {
	this.catchAsFielder = catchAsFielder;
}
public int getCatchAsBowler() {
	return catchAsBowler;
}
public void setCatchAsBowler(int catchAsBowler) {
	this.catchAsBowler = catchAsBowler;
}
public int getRunOuts() {
	return runOuts;
}
public void setRunOuts(int runOuts) {
	this.runOuts = runOuts;
}
public int getWides() {
	return wides;
}
public void setWides(int wides) {
	this.wides = wides;
}
public int getNoBalls() {
	return noBalls;
}
public void setNoBalls(int noBalls) {
	this.noBalls = noBalls;
}
public int getPlayerId() {
	return playerId;
}
public void setPlayerId(int playerId) {
	this.playerId = playerId;
}
public Player getPlayer() {
	return player;
}
public void setPlayer(Player player) {
	this.player = player;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public int getOvers() {
	return overs;
}
public void setOvers(int overs) {
	this.overs = overs;
}
public int getRuns() {
	return runs;
}
public void setRuns(int runs) {
	this.runs = runs;
}
public int getBalls() {
	return balls;
}
public void setBalls(int balls) {
	this.balls = balls;
}
public int getWickets() {
	return wickets;
}
public void setWickets(int wickets) {
	this.wickets = wickets;
}
@Override
public int compareTo(BowlingCardJson bc) {
	return (int) (this.getBowlingPosition()-bc.getBowlingPosition());
}
}