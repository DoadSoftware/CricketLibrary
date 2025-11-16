package com.cricket.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setup {

  private String specialMatchRules;
	
  private String noBallsRuns;
	
  private String ballsPerOver;
	
  private String matchDataUpdate;
  
  private String saveMatchFileAs;
	
  private String generateInteractiveFile;
	
  private int wagonXOffSet;

  private int wagonYOffSet;

  private String followOn;

  private int followOnThreshold;

  private int homeSubstitutesNumber;
  
  private int awaySubstitutesNumber;
	
  private String reviewsPerTeam;
	
  private String matchFileTimeStamp;

  private String speedFilePath;

  private String matchType;
  
  private String playerGender;

  private String tossResult;

  private String tossWinningDecision;

  private String tournament;

  private String matchIdent;

  private int tossWinningTeam;

  private int numberOfPowerplays;
  
  private int maxOvers;

  private String reducedOvers;
  
  private int homeTeamId;

  private int awayTeamId;

  private int groundId;

  private int seasonId;
  
  private int targetRuns;

  private String targetType;
  
  private String targetOvers;

  private int secondaryTargetRuns;

  private String secondaryTargetOvers;
  
  private String venueName;

  private Team homeTeam;

  private Team awayTeam;

  private String setupHomeTeam;

  private String setupAwayTeam;

  private boolean historicMatchLoaded;
  
  private List<Player> homeSquad;

  private List<Player> homeSubstitutes;
  
  private List<Player> awaySquad;

  private List<Player> awaySubstitutes;
  
  private List<Player> homeOtherSquad;

  private List<Player> awayOtherSquad;

  @JsonIgnore
  private Ground ground;
  
  @JsonIgnore
  private NameSuper nameSuper;
  
  @JsonIgnore
  private String LastXball;

  @JsonIgnore
  private int selected_inning;
  
  @JsonIgnore
  private int which_key_press;
  
public boolean isHistoricMatchLoaded() {
	return historicMatchLoaded;
}

public void setHistoricMatchLoaded(boolean historicMatchLoaded) {
	this.historicMatchLoaded = historicMatchLoaded;
}

public String getPlayerGender() {
	return playerGender;
}

public void setPlayerGender(String playerGender) {
	this.playerGender = playerGender;
}

public String getSpecialMatchRules() {
	return specialMatchRules;
}

public void setSpecialMatchRules(String specialMatchRules) {
	this.specialMatchRules = specialMatchRules;
}

public int getSecondaryTargetRuns() {
	return secondaryTargetRuns;
}

public void setSecondaryTargetRuns(int secondaryTargetRuns) {
	this.secondaryTargetRuns = secondaryTargetRuns;
}

public String getSecondaryTargetOvers() {
	return secondaryTargetOvers;
}

public void setSecondaryTargetOvers(String secondaryTargetOvers) {
	this.secondaryTargetOvers = secondaryTargetOvers;
}

public String getSaveMatchFileAs() {
	return saveMatchFileAs;
}

public void setSaveMatchFileAs(String saveMatchFileAs) {
	this.saveMatchFileAs = saveMatchFileAs;
}

public String getSetupHomeTeam() {
	return setupHomeTeam;
}

public void setSetupHomeTeam(String setupHomeTeam) {
	this.setupHomeTeam = setupHomeTeam;
}

public String getSetupAwayTeam() {
	return setupAwayTeam;
}

public void setSetupAwayTeam(String setupAwayTeam) {
	this.setupAwayTeam = setupAwayTeam;
}

public String getGenerateInteractiveFile() {
	return generateInteractiveFile;
}

public void setGenerateInteractiveFile(String generateInteractiveFile) {
	this.generateInteractiveFile = generateInteractiveFile;
}

public int getWagonXOffSet() {
	return wagonXOffSet;
}

public void setWagonXOffSet(int wagonXOffSet) {
	this.wagonXOffSet = wagonXOffSet;
}

public int getWagonYOffSet() {
	return wagonYOffSet;
}

public void setWagonYOffSet(int wagonYOffSet) {
	this.wagonYOffSet = wagonYOffSet;
}

public int getHomeSubstitutesNumber() {
	return homeSubstitutesNumber;
}

public void setHomeSubstitutesNumber(int homeSubstitutesNumber) {
	this.homeSubstitutesNumber = homeSubstitutesNumber;
}

public int getAwaySubstitutesNumber() {
	return awaySubstitutesNumber;
}

public void setAwaySubstitutesNumber(int awaySubstitutesNumber) {
	this.awaySubstitutesNumber = awaySubstitutesNumber;
}

public int getSeasonId() {
	return seasonId;
}

public void setSeasonId(int seasonId) {
	this.seasonId = seasonId;
}

public String getSpeedFilePath() {
	return speedFilePath;
}

public void setSpeedFilePath(String speedFilePath) {
	this.speedFilePath = speedFilePath;
}

public String getFollowOn() {
	return followOn;
}

public void setFollowOn(String followOn) {
	this.followOn = followOn;
}

public int getFollowOnThreshold() {
	return followOnThreshold;
}

public void setFollowOnThreshold(int followOnThreshold) {
	this.followOnThreshold = followOnThreshold;
}

public String getReviewsPerTeam() {
	return reviewsPerTeam;
}

public void setReviewsPerTeam(String reviewsPerTeam) {
	this.reviewsPerTeam = reviewsPerTeam;
}

public String getMatchFileTimeStamp() {
	return matchFileTimeStamp;
}

public void setMatchFileTimeStamp(String matchFileTimeStamp) {
	this.matchFileTimeStamp = matchFileTimeStamp;
}

public String getMatchType() {
	return matchType;
}

public void setMatchType(String matchType) {
	this.matchType = matchType;
}

public String getTossResult() {
	return tossResult;
}

public void setTossResult(String tossResult) {
	this.tossResult = tossResult;
}

public String getTossWinningDecision() {
	return tossWinningDecision;
}

public void setTossWinningDecision(String tossWinningDecision) {
	this.tossWinningDecision = tossWinningDecision;
}

public String getTournament() {
	return tournament;
}

public void setTournament(String tournament) {
	this.tournament = tournament;
}

public String getMatchIdent() {
	return matchIdent;
}

public void setMatchIdent(String matchIdent) {
	this.matchIdent = matchIdent;
}

public int getTossWinningTeam() {
	return tossWinningTeam;
}

public void setTossWinningTeam(int tossWinningTeam) {
	this.tossWinningTeam = tossWinningTeam;
}

public int getNumberOfPowerplays() {
	return numberOfPowerplays;
}

public void setNumberOfPowerplays(int numberOfPowerplays) {
	this.numberOfPowerplays = numberOfPowerplays;
}

public int getMaxOvers() {
	return maxOvers;
}

public void setMaxOvers(int maxOvers) {
	this.maxOvers = maxOvers;
}

public String getReducedOvers() {
	return reducedOvers;
}

public void setReducedOvers(String reducedOvers) {
	this.reducedOvers = reducedOvers;
}

public int getHomeTeamId() {
	return homeTeamId;
}

public void setHomeTeamId(int homeTeamId) {
	this.homeTeamId = homeTeamId;
}

public int getAwayTeamId() {
	return awayTeamId;
}

public void setAwayTeamId(int awayTeamId) {
	this.awayTeamId = awayTeamId;
}

public int getGroundId() {
	return groundId;
}

public void setGroundId(int groundId) {
	this.groundId = groundId;
}

public int getTargetRuns() {
	return targetRuns;
}

public void setTargetRuns(int targetRuns) {
	this.targetRuns = targetRuns;
}

public String getTargetType() {
	return targetType;
}

public void setTargetType(String targetType) {
	this.targetType = targetType;
}

public String getTargetOvers() {
	return targetOvers;
}

public void setTargetOvers(String targetOvers) {
	this.targetOvers = targetOvers;
}

public String getVenueName() {
	return venueName;
}

public void setVenueName(String venueName) {
	this.venueName = venueName;
}

public Ground getGround() {
	return ground;
}

public void setGround(Ground ground) {
	this.ground = ground;
}

public NameSuper getNameSuper() {
	return nameSuper;
}

public void setNameSuper(NameSuper nameSuper) {
	this.nameSuper = nameSuper;
}

public Team getHomeTeam() {
	return homeTeam;
}

public void setHomeTeam(Team homeTeam) {
	this.homeTeam = homeTeam;
}

public Team getAwayTeam() {
	return awayTeam;
}

public void setAwayTeam(Team awayTeam) {
	this.awayTeam = awayTeam;
}

public int getSelected_inning() {
	return selected_inning;
}

public void setSelected_inning(int selected_inning) {
	this.selected_inning = selected_inning;
}

public int getWhich_key_press() {
	return which_key_press;
}

public void setWhich_key_press(int which_key_press) {
	this.which_key_press = which_key_press;
}

public List<Player> getHomeSquad() {
	return homeSquad;
}

public void setHomeSquad(List<Player> homeSquad) {
	this.homeSquad = homeSquad;
}

public List<Player> getHomeSubstitutes() {
	return homeSubstitutes;
}

public void setHomeSubstitutes(List<Player> homeSubstitutes) {
	this.homeSubstitutes = homeSubstitutes;
}

public List<Player> getAwaySquad() {
	return awaySquad;
}

public void setAwaySquad(List<Player> awaySquad) {
	this.awaySquad = awaySquad;
}

public List<Player> getAwaySubstitutes() {
	return awaySubstitutes;
}

public void setAwaySubstitutes(List<Player> awaySubstitutes) {
	this.awaySubstitutes = awaySubstitutes;
}

public List<Player> getHomeOtherSquad() {
	return homeOtherSquad;
}

public void setHomeOtherSquad(List<Player> homeOtherSquad) {
	this.homeOtherSquad = homeOtherSquad;
}

public List<Player> getAwayOtherSquad() {
	return awayOtherSquad;
}

public void setAwayOtherSquad(List<Player> awayOtherSquad) {
	this.awayOtherSquad = awayOtherSquad;
}

public String getLastXball() {
	return LastXball;
}

public void setLastXball(String lastXball) {
	LastXball = lastXball;
}

public String getNoBallsRuns() {
	return noBallsRuns;
}

public void setNoBallsRuns(String noBallsRuns) {
	this.noBallsRuns = noBallsRuns;
}

public String getBallsPerOver() {
	return ballsPerOver;
}

public void setBallsPerOver(String ballsPerOver) {
	this.ballsPerOver = ballsPerOver;
}

public String getMatchDataUpdate() {
	return matchDataUpdate;
}

public void setMatchDataUpdate(String matchDataUpdate) {
	this.matchDataUpdate = matchDataUpdate;
}

@Override
public String toString() {
	return "Setup [specialMatchRules=" + specialMatchRules + ", noBallsRuns=" + noBallsRuns + ", ballsPerOver="
			+ ballsPerOver + ", matchDataUpdate=" + matchDataUpdate + ", saveMatchFileAs=" + saveMatchFileAs
			+ ", generateInteractiveFile=" + generateInteractiveFile + ", wagonXOffSet=" + wagonXOffSet
			+ ", wagonYOffSet=" + wagonYOffSet + ", followOn=" + followOn + ", followOnThreshold=" + followOnThreshold
			+ ", homeSubstitutesNumber=" + homeSubstitutesNumber + ", awaySubstitutesNumber=" + awaySubstitutesNumber
			+ ", reviewsPerTeam=" + reviewsPerTeam + ", matchFileTimeStamp=" + matchFileTimeStamp + ", speedFilePath="
			+ speedFilePath + ", matchType=" + matchType + ", playerGender=" + playerGender + ", tossResult="
			+ tossResult + ", tossWinningDecision=" + tossWinningDecision + ", tournament=" + tournament
			+ ", matchIdent=" + matchIdent + ", tossWinningTeam=" + tossWinningTeam + ", numberOfPowerplays="
			+ numberOfPowerplays + ", maxOvers=" + maxOvers + ", reducedOvers=" + reducedOvers + ", homeTeamId="
			+ homeTeamId + ", awayTeamId=" + awayTeamId + ", groundId=" + groundId + ", seasonId=" + seasonId
			+ ", targetRuns=" + targetRuns + ", targetType=" + targetType + ", targetOvers=" + targetOvers
			+ ", secondaryTargetRuns=" + secondaryTargetRuns + ", secondaryTargetOvers=" + secondaryTargetOvers
			+ ", venueName=" + venueName + ", homeTeam=" + homeTeam + ", awayTeam=" + awayTeam + ", setupHomeTeam="
			+ setupHomeTeam + ", setupAwayTeam=" + setupAwayTeam + ", historicMatchLoaded=" + historicMatchLoaded
			+ ", homeSquad=" + homeSquad + ", homeSubstitutes=" + homeSubstitutes + ", awaySquad=" + awaySquad
			+ ", awaySubstitutes=" + awaySubstitutes + ", homeOtherSquad=" + homeOtherSquad + ", awayOtherSquad="
			+ awayOtherSquad + ", ground=" + ground + ", nameSuper=" + nameSuper + ", LastXball=" + LastXball
			+ ", selected_inning=" + selected_inning + ", which_key_press=" + which_key_press + "]";
}

}
