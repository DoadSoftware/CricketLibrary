package com.cricket.model;

import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BattingCard implements Comparable<BattingCard>{

  private int playerId;
  
  private int batterPosition;
  
  private String battingStyle;
  
  private int runs;
  
  private int fours;
  
  private int sixes;
  
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

  private long seconds;
  
  @JsonIgnore
  private Player player;

  @JsonIgnore
  private Player concussion_player;

  @JsonIgnore
  private Player howOutFielder;
  
  @JsonIgnore
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

public BattingCard(int playerId, int batterPosition, String battingStyle, int runs, int fours, int sixes, int balls,
		int howOutFielderId, int howOutBowlerId, String batsmanInningStarted, String status, String onStrike,
		String howOutText, String howOut, String howOutPartOne, String howOutPartTwo, String strikeRate,
		String wasHowOutFielderSubstitute, int concussionPlayerId, long seconds, Player player,
		Player concussion_player, Player howOutFielder, Player howOutBowler) {
	super();
	this.playerId = playerId;
	this.batterPosition = batterPosition;
	this.battingStyle = battingStyle;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.balls = balls;
	this.howOutFielderId = howOutFielderId;
	this.howOutBowlerId = howOutBowlerId;
	this.batsmanInningStarted = batsmanInningStarted;
	this.status = status;
	this.onStrike = onStrike;
	this.howOutText = howOutText;
	this.howOut = howOut;
	this.howOutPartOne = howOutPartOne;
	this.howOutPartTwo = howOutPartTwo;
	this.strikeRate = strikeRate;
	WasHowOutFielderSubstitute = wasHowOutFielderSubstitute;
	this.concussionPlayerId = concussionPlayerId;
	this.seconds = seconds;
	this.player = player;
	this.concussion_player = concussion_player;
	this.howOutFielder = howOutFielder;
	this.howOutBowler = howOutBowler;
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
		sortData = sortData + 1;
	}
	return 1000 * sortData + 1000 - this.getBalls();
}

public long getSeconds() {
	return seconds;
}
public void setSeconds(long seconds) {
	this.seconds = seconds;
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
	this.fours = fours;
}
public int getSixes() {
	return sixes;
}
public void setSixes(int sixes) {
	this.sixes = sixes;
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
	this.runs = runs;
}
public int getBalls() {
	return balls;
}
public void setBalls(int balls) {
	this.balls = balls;
}
public Player getPlayer() {
	return player;
}
public void setPlayer(Player player) {
	this.player = player;
}
@Override
public String toString() {
	return "BattingCard [playerId=" + playerId + ", batterPosition=" + batterPosition + ", runs=" + runs + ", fours="
			+ fours + ", sixes=" + sixes + ", balls=" + balls + ", howOutFielderId=" + howOutFielderId
			+ ", howOutBowlerId=" + howOutBowlerId + ", batsmanInningStarted=" + batsmanInningStarted + ", status="
			+ status + ", onStrike=" + onStrike + ", howOutText=" + howOutText + ", howOut=" + howOut
			+ ", howOutFielder=" + howOutFielder + ", howOutBowler=" + howOutBowler + ", WasHowOutFielderSubstitute="
			+ WasHowOutFielderSubstitute + ", concussionPlayerId=" + concussionPlayerId + ", player=" + player
			+ ", concussion_player=" + concussion_player + "]";
}
@Override
public int compareTo(BattingCard bc) {
	return (int) (this.getBatterPosition()-bc.getBatterPosition());
}

}