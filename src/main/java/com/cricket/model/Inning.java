package com.cricket.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

  private int firstPowerplayStartOver;

  private int firstPowerplayEndOver;

  private int secondPowerplayStartOver;

  private int secondPowerplayEndOver;

  private int thirdPowerplayStartOver;

  private int thirdPowerplayEndOver;

  private int oversRemaining;

  private String specialRuns;
  
  @JsonIgnore
  private Team batting_team;

  @JsonIgnore
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
}

public Inning(int oversRemaining) {
	super();
	this.oversRemaining = oversRemaining;
	this.inningStats = new InningStats();
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
	this.duration = duration;
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

public int getOversRemaining() {
	return oversRemaining;
}

public void setOversRemaining(int oversRemaining) {
	this.oversRemaining = oversRemaining;
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

public int getFirstPowerplayStartOver() {
	return firstPowerplayStartOver;
}

public void setFirstPowerplayStartOver(int firstPowerplayStartOver) {
	this.firstPowerplayStartOver = firstPowerplayStartOver;
}

public int getFirstPowerplayEndOver() {
	return firstPowerplayEndOver;
}

public void setFirstPowerplayEndOver(int firstPowerplayEndOver) {
	this.firstPowerplayEndOver = firstPowerplayEndOver;
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

public int getSecondPowerplayStartOver() {
	return secondPowerplayStartOver;
}

public void setSecondPowerplayStartOver(int secondPowerplayStartOver) {
	this.secondPowerplayStartOver = secondPowerplayStartOver;
}

public int getSecondPowerplayEndOver() {
	return secondPowerplayEndOver;
}

public void setSecondPowerplayEndOver(int secondPowerplayEndOver) {
	this.secondPowerplayEndOver = secondPowerplayEndOver;
}

public int getThirdPowerplayStartOver() {
	return thirdPowerplayStartOver;
}

public void setThirdPowerplayStartOver(int thirdPowerplayStartOver) {
	this.thirdPowerplayStartOver = thirdPowerplayStartOver;
}

public int getThirdPowerplayEndOver() {
	return thirdPowerplayEndOver;
}

public void setThirdPowerplayEndOver(int thirdPowerplayEndOver) {
	this.thirdPowerplayEndOver = thirdPowerplayEndOver;
}

public int getTotalFours() {
	return totalFours;
}

public void setTotalFours(int totalFours) {
	this.totalFours = totalFours;
}

public int getTotalSixes() {
	return totalSixes;
}

public void setTotalSixes(int totalSixes) {
	this.totalSixes = totalSixes;
}

public int getTotalNines() {
	return totalNines;
}

public void setTotalNines(int totalNines) {
	this.totalNines = totalNines;
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
	this.totalOvers = totalOvers;
}
public int getTotalBalls() {
	return totalBalls;
}
public void setTotalBalls(int totalBalls) {
	this.totalBalls = totalBalls;
}
public int getTotalRuns() {
	return totalRuns;
}
public void setTotalRuns(int totalRuns) {
	this.totalRuns = totalRuns;
}
public int getTotalWickets() {
	return totalWickets;
}
public void setTotalWickets(int totalWickets) {
	this.totalWickets = totalWickets;
}
public int getTotalExtras() {
	return totalExtras;
}
public void setTotalExtras(int totalExtras) {
	this.totalExtras = totalExtras;
}
public int getTotalWides() {
	return totalWides;
}
public void setTotalWides(int totalWides) {
	this.totalWides = totalWides;
}
public int getTotalNoBalls() {
	return totalNoBalls;
}
public void setTotalNoBalls(int totalNoBalls) {
	this.totalNoBalls = totalNoBalls;
}
public int getTotalByes() {
	return totalByes;
}
public void setTotalByes(int totalByes) {
	this.totalByes = totalByes;
}
public int getTotalLegByes() {
	return totalLegByes;
}
public void setTotalLegByes(int totalLegByes) {
	this.totalLegByes = totalLegByes;
}
public int getTotalPenalties() {
	return totalPenalties;
}
public void setTotalPenalties(int totalPenalties) {
	this.totalPenalties = totalPenalties;
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
	this.dots = dots;
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
	return "Inning [isDeclared=" + isDeclared + ", inningNumber=" + inningNumber + ", battingTeamId=" + battingTeamId
			+ ", bowlingTeamId=" + bowlingTeamId + ", totalRuns=" + totalRuns + ", totalWickets=" + totalWickets
			+ ", totalOvers=" + totalOvers + ", totalBalls=" + totalBalls + ", totalExtras=" + totalExtras
			+ ", totalWides=" + totalWides + ", totalNoBalls=" + totalNoBalls + ", totalByes=" + totalByes
			+ ", totalLegByes=" + totalLegByes + ", totalPenalties=" + totalPenalties + ", totalFours=" + totalFours
			+ ", totalSixes=" + totalSixes + ", totalNines=" + totalNines + ", runRate=" + runRate
			+ ", isCurrentInning=" + isCurrentInning + ", inningStatus=" + inningStatus + ", firstPowerplayStartOver="
			+ firstPowerplayStartOver + ", firstPowerplayEndOver=" + firstPowerplayEndOver
			+ ", secondPowerplayStartOver=" + secondPowerplayStartOver + ", secondPowerplayEndOver="
			+ secondPowerplayEndOver + ", thirdPowerplayStartOver=" + thirdPowerplayStartOver
			+ ", thirdPowerplayEndOver=" + thirdPowerplayEndOver + ", oversRemaining=" + oversRemaining
			+ ", batting_team=" + batting_team + ", bowling_team=" + bowling_team + ", battingCard=" + battingCard
			+ ", partnerships=" + partnerships + ", bowlingCard=" + bowlingCard + ", fielders=" + fielders
			+ ", reviews=" + reviews + ", fallsOfWickets=" + fallsOfWickets + ", spells=" + spells + ", stats=" + stats
			+ "]";
}

}