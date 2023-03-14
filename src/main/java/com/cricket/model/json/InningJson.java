package com.cricket.model.json;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "inning", namespace = "innings")
public class InningJson {

  @JsonProperty(value ="isDeclared")
  private String isDeclared;
	
  @JsonProperty(value ="inningNumber")
  private int inningNumber;

  @JsonProperty(value ="battingTeamId")
  private int battingTeamId;

  @JsonProperty(value ="bowlingTeamId")
  private int bowlingTeamId;

  @JsonProperty(value ="totalRuns")
  private int totalRuns;

  @JsonProperty(value ="totalWickets")
  private int totalWickets;

  @JsonProperty(value ="totalOvers")
  private int totalOvers;

  @JsonProperty(value ="totalBalls")
  private int totalBalls;

  @JsonProperty(value ="totalExtras")
  private int totalExtras;

  @JsonProperty(value ="totalWides")
  private int totalWides;

  @JsonProperty(value ="totalNoBalls")
  private int totalNoBalls;

  @JsonProperty(value ="totalByes")
  private int totalByes;

  @JsonProperty(value ="totalLegByes")
  private int totalLegByes;

  @JsonProperty(value ="totalPenalties")
  private int totalPenalties;

  @JsonProperty(value ="totalFours")
  private int totalFours;

  @JsonProperty(value ="totalSixes")
  private int totalSixes;

  @JsonProperty(value ="runRate")
  private String runRate;
  
  @JsonProperty(value ="isCurrentInning")
  private String isCurrentInning;

  @JsonProperty(value ="inningStatus")
  private String inningStatus;

  @JsonProperty(value ="firstPowerplayStartOver")
  private int firstPowerplayStartOver;

  @JsonProperty(value ="firstPowerplayEndOver")
  private int firstPowerplayEndOver;

  @JsonProperty(value ="secondPowerplayStartOver")
  private int secondPowerplayStartOver;

  @JsonProperty(value ="secondPowerplayEndOver")
  private int secondPowerplayEndOver;

  @JsonProperty(value ="thirdPowerplayStartOver")
  private int thirdPowerplayStartOver;

  @JsonProperty(value ="thirdPowerplayEndOver")
  private int thirdPowerplayEndOver;

  @JsonProperty(value ="oversRemaining")
  private int oversRemaining;
  
  @JsonIgnoreProperties
  private Team batting_team;

  @JsonIgnoreProperties
  private Team bowling_team;
  
  @JsonProperty(value ="battingCard", namespace = "battingCards")
  private List<BattingCardJson> battingCard;

  @JsonProperty(value ="partnership", namespace = "partnerships")
  private List<PartnershipJson> partnerships;
  
  @JsonProperty(value ="bowlingCard", namespace = "bowlingCards")
  private List<BowlingCardJson> bowlingCard;

  @JsonProperty(value ="fielder", namespace = "fielders")
  private List<Player> fielders;

  @JsonProperty(value ="review", namespace = "reviews")
  private List<ReviewJson> reviews;

  @JsonProperty(value ="fallsOfWicket", namespace = "fallsOfWickets")
  private List<FallOfWicketJson> fallsOfWickets;

  @JsonProperty(value ="spell", namespace = "spells")
  private List<SpellJson> spells;
  
  @JsonIgnoreProperties
  private Map<String, String> stats;
  
public InningJson() {
	super();
}

public InningJson(int oversRemaining) {
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

public List<SpellJson> getSpells() {
	return spells;
}

public void setSpells(List<SpellJson> spells) {
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

public List<FallOfWicketJson> getFallsOfWickets() {
	return fallsOfWickets;
}

public void setFallsOfWickets(List<FallOfWicketJson> fallsOfWickets) {
	this.fallsOfWickets = fallsOfWickets;
}

public List<ReviewJson> getReviews() {
	return reviews;
}

public void setReview(List<ReviewJson> reviews) {
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

public List<PartnershipJson> getPartnerships() {
	return partnerships;
}

public void setPartnerships(List<PartnershipJson> partnerships) {
	this.partnerships = partnerships;
}

public void setReviews(List<ReviewJson> reviews) {
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
public List<BattingCardJson> getBattingCard() {
	return battingCard;
}
public void setBattingCard(List<BattingCardJson> battingCard) {
	this.battingCard = battingCard;
}
public List<BowlingCardJson> getBowlingCard() {
	return bowlingCard;
}
public void setBowlingCard(List<BowlingCardJson> bowlingCard) {
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