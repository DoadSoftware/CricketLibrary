package com.cricket.model;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="inning")
@XmlAccessorType(XmlAccessType.FIELD)
public class Inning {

  @XmlElement(name = "isDeclared")
  private String isDeclared;
	
  @XmlElement(name = "inningNumber")
  private int inningNumber;

  @XmlElement(name = "battingTeamId")
  private int battingTeamId;

  @XmlElement(name = "bowlingTeamId")
  private int bowlingTeamId;

  @XmlElement(name = "totalRuns")
  private int totalRuns;

  @XmlElement(name = "totalWickets")
  private int totalWickets;

  @XmlElement(name = "totalOvers")
  private int totalOvers;

  @XmlElement(name = "totalBalls")
  private int totalBalls;

  @XmlElement(name = "totalExtras")
  private int totalExtras;

  @XmlElement(name = "totalWides")
  private int totalWides;

  @XmlElement(name = "totalNoBalls")
  private int totalNoBalls;

  @XmlElement(name = "totalByes")
  private int totalByes;

  @XmlElement(name = "totalLegByes")
  private int totalLegByes;

  @XmlElement(name = "totalPenalties")
  private int totalPenalties;

  @XmlElement(name = "totalFours")
  private int totalFours;

  @XmlElement(name = "totalSixes")
  private int totalSixes;

  @XmlElement(name = "runRate")
  private String runRate;
  
  @XmlElement(name = "isCurrentInning")
  private String isCurrentInning;

  @XmlElement(name = "inningStatus")
  private String inningStatus;

  @XmlElement(name = "firstPowerplayStartOver")
  private int firstPowerplayStartOver;

  @XmlElement(name = "firstPowerplayEndOver")
  private int firstPowerplayEndOver;

  @XmlElement(name = "secondPowerplayStartOver")
  private int secondPowerplayStartOver;

  @XmlElement(name = "secondPowerplayEndOver")
  private int secondPowerplayEndOver;

  @XmlElement(name = "thirdPowerplayStartOver")
  private int thirdPowerplayStartOver;

  @XmlElement(name = "thirdPowerplayEndOver")
  private int thirdPowerplayEndOver;

  @XmlElement(name = "oversRemaining")
  private int oversRemaining;
  
  @XmlTransient
  private Team batting_team;

  @XmlTransient
  private Team bowling_team;
  
  @XmlElementWrapper(name = "battingCard")
  @XmlElement(name = "batter")
  private List<BattingCard> battingCard;

  @XmlElementWrapper(name = "partnerships")
  @XmlElement(name = "partnership")
  private List<Partnership> partnerships;
  
  @XmlElementWrapper(name = "bowlingCard")
  @XmlElement(name = "bowler")
  private List<BowlingCard> bowlingCard;

  @XmlElementWrapper(name = "fielders")
  @XmlElement(name = "fielder")
  private List<Player> fielders;

  @XmlElementWrapper(name = "reviews")
  @XmlElement(name = "review")
  private List<Review> reviews;

  @XmlElementWrapper(name = "fallsOfWickets")
  @XmlElement(name = "fallsOfWicket")
  private List<FallOfWicket> fallsOfWickets;

  @XmlElementWrapper(name = "spell")
  @XmlElement(name = "spell")
  private List<Spell> spells;
  
  @XmlTransient
  private Map<String, String> stats;
  
public Inning() {
	super();
}

public Inning(int oversRemaining) {
	super();
	this.oversRemaining = oversRemaining;
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

@Override
public String toString() {
	return "Inning [inningNumber=" + inningNumber + ", battingTeamId=" + battingTeamId + ", bowlingTeamId="
			+ bowlingTeamId + ", totalRuns=" + totalRuns + ", totalWickets=" + totalWickets + ", totalOvers="
			+ totalOvers + ", totalBalls=" + totalBalls + ", totalExtras=" + totalExtras + ", totalWides=" + totalWides
			+ ", totalNoBalls=" + totalNoBalls + ", totalByes=" + totalByes + ", totalLegByes=" + totalLegByes
			+ ", totalPenalties=" + totalPenalties + ", totalFours=" + totalFours + ", totalSixes=" + totalSixes
			+ ", isCurrentInning=" + isCurrentInning + ", inningStatus=" + inningStatus + ", batting_team="
			+ batting_team + ", bowling_team=" + bowling_team + ", battingCard=" + battingCard + ", bowlingCard="
			+ bowlingCard + ", fielders=" + fielders + "]";
}

}