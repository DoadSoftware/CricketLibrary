package com.cricket.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BowlingCard implements Comparable<BowlingCard> {

  private Player player;

  private int bowlingPosition;

  private String status;

  private String economyRate;
  
  private int bowling_end;
  
  private int overs;

  private int runs;
  
  private int balls;

  private int wickets;

  private int playerId;
  
  private int wides;
  
  private int noBalls;
  
  private int runOuts;
  
  private int stumpings;
  
  private int catchAsFielder;
  
  private int catchAsBowler;

  private int maidens;

  private int dots;

  private int totalRunsThisOver;

  private List<Speed> speeds;

  private String ballTypeOverNo;
  
  private List<DaySession> bowlingSession; 
  
public BowlingCard() {
	super();
}

public BowlingCard(int playerId,int bowlingPosition, String status, int bowling_end) {
	super();
	this.playerId = playerId;
	this.bowlingPosition = bowlingPosition;
	this.status = status;
	this.bowling_end = bowling_end;
}

public BowlingCard(Player player,int bowlingPosition, String status, int bowling_end) {
	super();
	if(player != null) {
		this.playerId = player.getPlayerId();
	}
	this.player = player;
	this.bowlingPosition = bowlingPosition;
	this.status = status;
	this.bowling_end = bowling_end;
}
public BowlingCard(Player player) {
	super();
	this.player = player;
}

public BowlingCard(int overs, int runs, int balls, int wickets, int playerId, int wides, int noBalls, int maidens,
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
	int sortData = this.getWickets();
//	if(this.getStatus() != null && this.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//		sortData = sortData + 1;
//	}else {
//		sortData = sortData;
//	}
	return 1000 * sortData - this.getRuns();
}

public List<DaySession> getBowlingSession() {
	return bowlingSession;
}

public void setBowlingSession(List<DaySession> bowlingSession) {
	this.bowlingSession = bowlingSession;
}

public List<Speed> getSpeeds() {
	return speeds;
}
public String getBallTypeOverNo() {
	return ballTypeOverNo;
}
public void setBallTypeOverNo(String ballTypeOverNo) {
	this.ballTypeOverNo = ballTypeOverNo;
}

public void setSpeeds(List<Speed> speeds) {
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
	}else {
		this.maidens = maidens;
	}
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
	if(overs < 0) {
		this.overs = 0;
	}
	else {
		this.overs = overs;
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
@Override
public int compareTo(BowlingCard bc) {
	return (int) (this.getBowlingPosition()-bc.getBowlingPosition());
}

@Override
public String toString() {
	return "BowlingCard [player=" + player + ", bowlingPosition=" + bowlingPosition + ", status=" + status
			+ ", economyRate=" + economyRate + ", bowling_end=" + bowling_end + ", overs=" + overs + ", runs=" + runs
			+ ", balls=" + balls + ", wickets=" + wickets + ", playerId=" + playerId + ", wides=" + wides + ", noBalls="
			+ noBalls + ", runOuts=" + runOuts + ", stumpings=" + stumpings + ", catchAsFielder=" + catchAsFielder
			+ ", catchAsBowler=" + catchAsBowler + ", maidens=" + maidens + ", dots=" + dots + ", totalRunsThisOver="
			+ totalRunsThisOver + ", speeds=" + speeds + ", ballTypeOverNo=" + ballTypeOverNo + ", bowlingSession=" + bowlingSession + "]";
}

}