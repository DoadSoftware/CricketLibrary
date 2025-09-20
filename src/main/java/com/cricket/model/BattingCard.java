package com.cricket.model;

import java.util.List;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BattingCard implements Comparable<BattingCard>
{
  private int playerId;
  
  private int batterPosition;
  
  private String battingStyle;

  private int duration;
  
  private int runs;
  
  private int fours;
  
  private int seconds;
  
  private int sixes;
  
  private int nines;
  
  private int balls;
  
  private int howOutFielderId;
  
  private int howOutBowlerId;

  private String batsmanInningStarted;
  
  private String status;
  
  private String onStrike;
  
  private String howOutText;
  
  private String howOut;

  private String howOutPartOne;

  private String howOutPartTwo;
  
  private String strikeRate;

  private String WasHowOutFielderSubstitute;
  
  private int concussionPlayerId;

  private String startTime;

  private String endTime;
  
  private Player player;

  private List<DaySession> battingSession; 
  
  private Player concussion_player;

  private Player howOutFielder;
  
  private Player howOutBowler;
  
public BattingCard(int playerId, int batterPosition, String status) {
	super();
	this.playerId = playerId;
	this.batterPosition = batterPosition;
	this.status = status;
}
public BattingCard() {
	super();
}
public BattingCard(Player player) {
	super();
	this.player = player;
}
public BattingCard(int playerId, int howOutFielderId, int howOutBowlerId, String status, 
		int concussionPlayerId, String how_out) {
	super();
	this.playerId = playerId;
	this.howOutFielderId = howOutFielderId;
	this.howOutBowlerId = howOutBowlerId;
	this.status = status;
	this.concussionPlayerId = concussionPlayerId;
	this.howOut = how_out;
}
public BattingCard(int playerId, int runs, int fours, int sixes, int balls) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.balls = balls;
}

public int getBatsmanScoreSortData() {
	int sortData = this.getRuns();
	if(this.getStatus() != null && this.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
		sortData = 2*sortData + 1;
	}else {
		sortData = 2*sortData;
	}
	return 1000 * sortData + 1000 - this.getBalls();
}

public List<DaySession> getBattingSession() {
	return battingSession;
}
public void setBattingSession(List<DaySession> battingSession) {
	this.battingSession = battingSession;
}
public int getDuration() {
	return duration;
}
public void setDuration(int duration) {
	this.duration = duration;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getStrikeRate() {
	return strikeRate;
}
public void setStrikeRate(String strikeRate) {
	this.strikeRate = strikeRate;
}
public String getHowOutPartOne() {
	return howOutPartOne;
}
public void setHowOutPartOne(String howOutPartOne) {
	this.howOutPartOne = howOutPartOne;
}
public String getHowOutPartTwo() {
	return howOutPartTwo;
}
public void setHowOutPartTwo(String howOutPartTwo) {
	this.howOutPartTwo = howOutPartTwo;
}
public String getWasHowOutFielderSubstitute() {
	return WasHowOutFielderSubstitute;
}
public void setWasHowOutFielderSubstitute(String wasHowOutFielderSubstitute) {
	WasHowOutFielderSubstitute = wasHowOutFielderSubstitute;
}
public int getConcussionPlayerId() {
	return concussionPlayerId;
}
public void setConcussionPlayerId(int concussionPlayerId) {
	this.concussionPlayerId = concussionPlayerId;
}
public Player getConcussion_player() {
	return concussion_player;
}
public void setConcussion_player(Player concussion_player) {
	this.concussion_player = concussion_player;
}
public String getBatsmanInningStarted() {
	return batsmanInningStarted;
}
public void setBatsmanInningStarted(String batsmanInningStarted) {
	this.batsmanInningStarted = batsmanInningStarted;
}
public int getFours() {
	return fours;
}
public void setFours(int fours) {
	if(fours < 0) {
		this.fours = 0;
	}
	else {
		this.fours = fours;
	}
}
public int getSixes() {
	return sixes;
}
public void setSixes(int sixes) {
	if(sixes < 0) {
		this.sixes = 0;
	}
	else {
		this.sixes = sixes;
	}
}
public int getNines() {
	return nines;
}
public void setNines(int nines) {
	if(nines < 0) {
		this.nines = 0;
	}
	else {
		this.nines = nines;
	}
}
public String getHowOutText() {
	return howOutText;
}
public void setHowOutText(String howOutText) {
	this.howOutText = howOutText;
}
public Player getHowOutFielder() {
	return howOutFielder;
}
public void setHowOutFielder(Player howOutFielder) {
	this.howOutFielder = howOutFielder;
}
public Player getHowOutBowler() {
	return howOutBowler;
}
public void setHowOutBowler(Player howOutBowler) {
	this.howOutBowler = howOutBowler;
}
public String getHowOut() {
	return howOut;
}
public void setHowOut(String howOut) {
	this.howOut = howOut;
}
public String getOnStrike() {
	return onStrike;
}
public void setOnStrike(String onStrike) {
	this.onStrike = onStrike;
}
public int getHowOutFielderId() {
	return howOutFielderId;
}
public void setHowOutFielderId(int howOutFielderId) {
	this.howOutFielderId = howOutFielderId;
}
public int getHowOutBowlerId() {
	return howOutBowlerId;
}
public void setHowOutBowlerId(int howOutBowlerId) {
	this.howOutBowlerId = howOutBowlerId;
}
public int getPlayerId() {
	return playerId;
}
public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public String getBattingStyle() {
	return battingStyle;
}
public void setBattingStyle(String battingStyle) {
	this.battingStyle = battingStyle;
}
public int getBatterPosition() {
	return batterPosition;
}
public void setBatterPosition(int batterPosition) {
	this.batterPosition = batterPosition;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
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
public Player getPlayer() {
	return player;
}
public void setPlayer(Player player) {
	this.player = player;
}

public int getSeconds() {
	return seconds;
}
public void setSeconds(int seconds) {
	this.seconds = seconds;
}
@Override
public String toString() {
	return "BattingCard [playerId=" + playerId + ", batterPosition=" + batterPosition + ", battingStyle=" + battingStyle
			+ ", runs=" + runs + ", fours=" + fours + ", sixes=" + sixes + ", nines=" + nines + ", balls=" + balls
			+ ", howOutFielderId=" + howOutFielderId + ", howOutBowlerId=" + howOutBowlerId + ", batsmanInningStarted="
			+ batsmanInningStarted + ", status=" + status + ", onStrike=" + onStrike + ", howOutText=" + howOutText
			+ ", howOut=" + howOut + ", howOutPartOne=" + howOutPartOne + ", howOutPartTwo=" + howOutPartTwo
			+ ", strikeRate=" + strikeRate + ", WasHowOutFielderSubstitute=" + WasHowOutFielderSubstitute
			+ ", concussionPlayerId=" + concussionPlayerId + ", startTime=" + startTime + ", player=" + player
			+ ", concussion_player=" + concussion_player + ", howOutFielder=" + howOutFielder + ", howOutBowler="
			+ howOutBowler + "]";
}

@Override
public int compareTo(BattingCard bc) {
	return (int) (this.getBatterPosition()-bc.getBatterPosition());
}
@Override
public int hashCode() {
	return playerId;
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	BattingCard other = (BattingCard) obj;
	if (playerId != other.playerId)
		return false;
	return true;
}

}