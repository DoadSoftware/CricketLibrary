package com.cricket.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Inning {

  private String startTime;

  private String endTime;

  private int duration;

  private String isDeclared;
	
  private int inningNumber;

  private int battingTeamId;

  private int bowlingTeamId;

  private int totalRuns;

  private int totalWickets;

  private int totalOvers;

  private int totalBalls;

  private int totalExtras;

  private int totalWides;

  private int totalNoBalls;

  private int totalByes;

  private int totalLegByes;

  private int totalPenalties;

  private int totalFours;

  private int totalSixes;
  
  private int totalNines;
  
  private int dots;
  
  private String runRate;
  
  private String isCurrentInning;

  private String inningStatus;

  private String firstPowerplayStartOver;

  private String firstPowerplayEndOver;

  private String secondPowerplayStartOver;

  private String secondPowerplayEndOver;

  private String thirdPowerplayStartOver;

  private String thirdPowerplayEndOver;

  private String newBallOver;

  private String specialRuns;
  
  private Team batting_team;

  private Team bowling_team;
  
  private List<BattingCard> battingCard;

  private List<Partnership> partnerships;
  
  private List<BowlingCard> bowlingCard;

  private List<Player> fielders;

  private List<Review> reviews;

  private List<FallOfWicket> fallsOfWickets;

  private List<Spell> spells;
  
  private InningStats inningStats;

  @JsonIgnore
  private Map<String, String> stats;
  
public Inning() {
	super();
	this.inningStats = new InningStats();
}

public String getNewBallOver() {
	return newBallOver;
}

public void setNewBallOver(String newBallOver) {
	this.newBallOver = newBallOver;
}

public String getSpecialRuns() {
	return specialRuns;
}

public void setSpecialRuns(String specialRuns) {
	this.specialRuns = specialRuns;
}

public InningStats getInningStats() {
	return inningStats;
}

public void setInningStats(InningStats inningStats) {
	this.inningStats = inningStats;
}

public int getDuration() {
	return duration;
}

public void setDuration(int duration) {
	if(duration < 0) {
		this.duration = 0;
	}
	else {
		this.duration = duration;
	}
}

public String getStartTime() {
	return startTime;
}

public void setStartTime(String startTime) {
	this.startTime = startTime;
}

public String getEndTime() {
	return endTime;
}

public void setEndTime(String endTime) {
	this.endTime = endTime;
}

public String getIsDeclared() {
	return isDeclared;
}

public void setIsDeclared(String isDeclared) {
	this.isDeclared = isDeclared;
}

public List<Spell> getSpells() {
	return spells;
}

public void setSpells(List<Spell> spells) {
	this.spells = spells;
}

public Map<String, String> getStats() {
	return stats;
}

public void setStats(Map<String, String> stats) {
	this.stats = stats;
}

public String getRunRate() {
	return runRate;
}

public void setRunRate(String runRate) {
	this.runRate = runRate;
}

public List<FallOfWicket> getFallsOfWickets() {
	return fallsOfWickets;
}

public void setFallsOfWickets(List<FallOfWicket> fallsOfWickets) {
	this.fallsOfWickets = fallsOfWickets;
}

public List<Review> getReviews() {
	return reviews;
}

public void setReview(List<Review> reviews) {
	this.reviews = reviews;
}

public List<Partnership> getPartnerships() {
	return partnerships;
}

public void setPartnerships(List<Partnership> partnerships) {
	this.partnerships = partnerships;
}

public void setReviews(List<Review> reviews) {
	this.reviews = reviews;
}

public String getFirstPowerplayStartOver() {
	return firstPowerplayStartOver;
}

public void setFirstPowerplayStartOver(String firstPowerplayStartOver) {
	this.firstPowerplayStartOver = firstPowerplayStartOver;
}

public String getFirstPowerplayEndOver() {
	return firstPowerplayEndOver;
}

public void setFirstPowerplayEndOver(String firstPowerplayEndOver) {
	this.firstPowerplayEndOver = firstPowerplayEndOver;
}

public String getSecondPowerplayStartOver() {
	return secondPowerplayStartOver;
}

public void setSecondPowerplayStartOver(String secondPowerplayStartOver) {
	this.secondPowerplayStartOver = secondPowerplayStartOver;
}

public String getSecondPowerplayEndOver() {
	return secondPowerplayEndOver;
}

public void setSecondPowerplayEndOver(String secondPowerplayEndOver) {
	this.secondPowerplayEndOver = secondPowerplayEndOver;
}

public String getThirdPowerplayStartOver() {
	return thirdPowerplayStartOver;
}

public void setThirdPowerplayStartOver(String thirdPowerplayStartOver) {
	this.thirdPowerplayStartOver = thirdPowerplayStartOver;
}

public String getThirdPowerplayEndOver() {
	return thirdPowerplayEndOver;
}

public void setThirdPowerplayEndOver(String thirdPowerplayEndOver) {
	this.thirdPowerplayEndOver = thirdPowerplayEndOver;
}

public int getTotalFours() {
	return totalFours;
}

public void setTotalFours(int totalFours) {
	if(totalFours < 0) {
		this.totalFours = 0;
	}
	else {
		this.totalFours = totalFours;
	}
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	if(totalSixes < 0) {
		this.totalSixes = 0;
	}
	else {
		this.totalSixes = totalSixes;
	}
}

public int getTotalNines() {
	return totalNines;
}

public void setTotalNines(int totalNines) {
	if(totalNines < 0) {
		this.totalNines = 0;
	}
	else {
		this.totalNines = totalNines;
	}
}

public List<Player> getFielders() {
	return fielders;
}
public void setFielders(List<Player> fielders) {
	this.fielders = fielders;
}
public String getInningStatus() {
	return inningStatus;
}
public void setInningStatus(String inningStatus) {
	this.inningStatus = inningStatus;
}
public String getIsCurrentInning() {
	return isCurrentInning;
}
public void setIsCurrentInning(String isCurrentInning) {
	this.isCurrentInning = isCurrentInning;
}
public int getTotalOvers() {
	return totalOvers;
}
public void setTotalOvers(int totalOvers) {
	if(totalOvers < 0) {
		this.totalOvers = 0;
	}
	else {
		this.totalOvers = totalOvers;
	}
}
public int getTotalBalls() {
	return totalBalls;
}
public void setTotalBalls(int totalBalls) {
	if(totalBalls < 0) {
		this.totalBalls = 0;
	}
	else {
		this.totalBalls = totalBalls;
	}
}
public int getTotalRuns() {
	return totalRuns;
}
public void setTotalRuns(int totalRuns) {
	if(totalRuns < 0) {
		this.totalRuns = 0;
	}
	else {
		this.totalRuns = totalRuns;
	}
}
public int getTotalWickets() {
	return totalWickets;
}
public void setTotalWickets(int totalWickets) {
	if(totalWickets < 0) {
		this.totalWickets = 0;
	}
	else {
		this.totalWickets = totalWickets;
	}
}
public int getTotalExtras() {
	return totalExtras;
}
public void setTotalExtras(int totalExtras) {
	if(totalExtras < 0) {
		this.totalExtras = 0;
	}
	else {
		this.totalExtras = totalExtras;
	}
}
public int getTotalWides() {
	return totalWides;
}
public void setTotalWides(int totalWides) {
	if(totalWides < 0) {
		this.totalWides = 0;
	}
	else {
		this.totalWides = totalWides;
	}
}
public int getTotalNoBalls() {
	return totalNoBalls;
}
public void setTotalNoBalls(int totalNoBalls) {
	if(totalNoBalls < 0) {
		this.totalNoBalls = 0;
	}
	else {
		this.totalNoBalls = totalNoBalls;
	}
}
public int getTotalByes() {
	return totalByes;
}
public void setTotalByes(int totalByes) {
	if(totalByes < 0) {
		this.totalByes = 0;
	}
	else {
		this.totalByes = totalByes;
	}
}
public int getTotalLegByes() {
	return totalLegByes;
}
public void setTotalLegByes(int totalLegByes) {
	if(totalLegByes < 0) {
		this.totalLegByes = 0;
	}
	else {
		this.totalLegByes = totalLegByes;
	}
}
public int getTotalPenalties() {
	return totalPenalties;
}
public void setTotalPenalties(int totalPenalties) {
	if(totalPenalties < 0) {
		this.totalPenalties = 0;
	}
	else {
		this.totalPenalties = totalPenalties;
	}
}
public int getBattingTeamId() {
	return battingTeamId;
}
public void setBattingTeamId(int battingTeamId) {
	this.battingTeamId = battingTeamId;
}
public int getBowlingTeamId() {
	return bowlingTeamId;
}
public void setBowlingTeamId(int bowlingTeamId) {
	this.bowlingTeamId = bowlingTeamId;
}
public Team getBatting_team() {
	return batting_team;
}
public void setBatting_team(Team batting_team) {
	this.batting_team = batting_team;
}
public Team getBowling_team() {
	return bowling_team;
}
public void setBowling_team(Team bowling_team) {
	this.bowling_team = bowling_team;
}
public int getInningNumber() {
	return inningNumber;
}
public void setInningNumber(int inningNumber) {
	this.inningNumber = inningNumber;
}
public List<BattingCard> getBattingCard() {
	return battingCard;
}
public void setBattingCard(List<BattingCard> battingCard) {
	this.battingCard = battingCard;
}
public List<BowlingCard> getBowlingCard() {
	return bowlingCard;
}
public void setBowlingCard(List<BowlingCard> bowlingCard) {
	this.bowlingCard = bowlingCard;
}
public int getDots() {
	return dots;
}
public void setDots(int dots) {
	if(dots < 0) {
		this.dots = 0;
	}
	else {
		this.dots = dots;
	}
}

public Inning(int inningNumber, int totalRuns, int totalWickets, int totalFours, int totalSixes, int totalNines,int dots) {
	super();
	this.inningNumber = inningNumber;
	this.totalRuns = totalRuns;
	this.totalWickets = totalWickets;
	this.totalFours = totalFours;
	this.totalSixes = totalSixes;
	this.totalNines = totalNines;
	this.dots = dots;
}

@Override
public String toString() {
	return "Inning [startTime=" + startTime + ", endTime=" + endTime + ", duration=" + duration + ", isDeclared="
			+ isDeclared + ", inningNumber=" + inningNumber + ", battingTeamId=" + battingTeamId + ", bowlingTeamId="
			+ bowlingTeamId + ", totalRuns=" + totalRuns + ", totalWickets=" + totalWickets + ", totalOvers="
			+ totalOvers + ", totalBalls=" + totalBalls + ", totalExtras=" + totalExtras + ", totalWides=" + totalWides
			+ ", totalNoBalls=" + totalNoBalls + ", totalByes=" + totalByes + ", totalLegByes=" + totalLegByes
			+ ", totalPenalties=" + totalPenalties + ", totalFours=" + totalFours + ", totalSixes=" + totalSixes
			+ ", totalNines=" + totalNines + ", dots=" + dots + ", runRate=" + runRate + ", isCurrentInning="
			+ isCurrentInning + ", inningStatus=" + inningStatus + ", firstPowerplayStartOver="
			+ firstPowerplayStartOver + ", firstPowerplayEndOver=" + firstPowerplayEndOver
			+ ", secondPowerplayStartOver=" + secondPowerplayStartOver + ", secondPowerplayEndOver="
			+ secondPowerplayEndOver + ", thirdPowerplayStartOver=" + thirdPowerplayStartOver
			+ ", thirdPowerplayEndOver=" + thirdPowerplayEndOver + ", newBallOver=" + newBallOver + ", specialRuns="
			+ specialRuns + ", batting_team=" + batting_team + ", bowling_team=" + bowling_team + ", battingCard="
			+ battingCard + ", partnerships=" + partnerships + ", bowlingCard=" + bowlingCard + ", fielders=" + fielders
			+ ", reviews=" + reviews + ", fallsOfWickets=" + fallsOfWickets + ", spells=" + spells + ", inningStats="
			+ inningStats + ", stats=" + stats + "]";
}

}
