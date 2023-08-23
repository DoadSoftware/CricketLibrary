package com.cricket.model.json;

import java.util.List;

import com.cricket.model.Ground;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Team;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "Match")
public class MatchJson {

  @JsonProperty(value = "generateInteractiveFile")
  private String generateInteractiveFile;
	
  @JsonProperty(value = "wagonXOffSet")
  private int wagonXOffSet;

  @JsonProperty(value = "wagonYOffSet")
  private int wagonYOffSet;
  
  @JsonProperty(value = "followOn")
  private String followOn;

  @JsonProperty(value = "followOnThreshold")
  private int followOnThreshold;

  @JsonProperty(value = "homeSubstitutesNumber")
  private int homeSubstitutesNumber;
  
  @JsonProperty(value = "awaySubstitutesNumber")
  private int awaySubstitutesNumber;
	
  @JsonProperty(value = "reviewsPerTeam")
  private String reviewsPerTeam;
	
  @JsonProperty(value = "matchFileTimeStamp")
  private String matchFileTimeStamp;

  @JsonProperty(value = "useSpeed")
  private String useSpeed;

  @JsonProperty(value = "matchType")
  private String matchType;
  
  @JsonProperty(value = "matchResult")
  private String matchResult;

  @JsonProperty(value = "matchStatus")
  private String matchStatus;
  
  @JsonProperty(value = "matchFileName")
  private String matchFileName;

  @JsonProperty(value = "readPhotoColumn")
  private String readPhotoColumn;
  
  @JsonProperty(value = "tossResult")
  private String tossResult;

  @JsonProperty(value = "tossWinningDecision")
  private String tossWinningDecision;

  @JsonProperty(value = "tournament")
  private String tournament;

  @JsonProperty(value = "matchIdent")
  private String matchIdent;

  @JsonProperty(value = "tossWinningTeam")
  private int tossWinningTeam;

  @JsonProperty(value = "numberOfPowerplays")
  private int numberOfPowerplays;
  
  @JsonProperty(value = "maxOvers")
  private int maxOvers;

  @JsonProperty(value = "reducedOvers")
  private int reducedOvers;
  
  @JsonProperty(value = "homeTeamId")
  private int homeTeamId;

  @JsonProperty(value = "awayTeamId")
  private int awayTeamId;

  @JsonProperty(value = "groundId")
  private int groundId;

  @JsonProperty(value = "seasonId")
  private int seasonId;
  
  @JsonProperty(value = "targetRuns")
  private int targetRuns;

  @JsonProperty(value = "targetType")
  private String targetType;
  
  @JsonProperty(value = "targetOvers")
  private String targetOvers;

  @JsonProperty(value = "venueName")
  private String venueName;

  @JsonProperty(value = "matchStartTime")
  private String matchStartTime;

  @JsonProperty(value = "matchTimeStatus")
  private String matchTimeStatus;

  @JsonProperty(value = "matchTotalSeconds")
  private long matchTotalSeconds;
  
  @JsonProperty(value = "bowlerRunning")
  private String bowlerRunning;

  @JsonProperty(value = "ballRelease")
  private String ballRelease;

  @JsonProperty(value = "homeTeam")
  private Team homeTeam;

  @JsonProperty(value = "awayTeam")
  private Team awayTeam;

  @JsonProperty(value = "currentSpeed")
  private String current_speed;
  
  @JsonIgnoreProperties
  private Ground ground;
  
  @JsonIgnoreProperties
  private NameSuper nameSuper;

  @JsonIgnoreProperties
  private int selected_inning;
  
  @JsonIgnoreProperties
  private int which_key_press;

  @JsonProperty(value = "inning", namespace = "innings")
  private List<InningJson> inning;

  @JsonProperty(value = "daySession", namespace = "daySessions")
  private List<DaySessionJson> daysSessions;
  
  @JsonProperty(value = "homeSquad", namespace = "homeSquads")
  private List<Player> homeSquad;

  @JsonProperty(value = "homeSubstitutes", namespace = "homeSubstitutes")
  private List<Player> homeSubstitutes;
  
  @JsonProperty(value = "awaySquad", namespace = "awaySquads")
  private List<Player> awaySquad;

  @JsonProperty(value = "awaySubstitute", namespace = "awaySubstitutes")
  private List<Player> awaySubstitutes;
  
  @JsonProperty(value = "homeOtherSquad", namespace = "homeOtherSquads")
  private List<Player> homeOtherSquad;

  @JsonProperty(value = "awayOtherSquad", namespace = "awayOtherSquads")
  private List<Player> awayOtherSquad;

  @JsonProperty(value = "shot", namespace = "shots")
  private List<ShotJson> shots;

  @JsonProperty(value = "wagon", namespace = "wagons")
  private List<WagonJson> wagons;
  
  @JsonProperty(value = "event", namespace = "events")
  private List<EventJson> events;

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

public String getCurrent_speed() {
	return current_speed;
}

public void setCurrent_speed(String current_speed) {
	this.current_speed = current_speed;
}

public String getUseSpeed() {
	return useSpeed;
}

public void setUseSpeed(String useSpeed) {
	this.useSpeed = useSpeed;
}

public List<WagonJson> getWagons() {
	return wagons;
}

public void setWagons(List<WagonJson> wagons) {
	this.wagons = wagons;
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

public String getMatchResult() {
	return matchResult;
}

public void setMatchResult(String matchResult) {
	this.matchResult = matchResult;
}

public String getMatchStatus() {
	return matchStatus;
}

public void setMatchStatus(String matchStatus) {
	this.matchStatus = matchStatus;
}

public String getMatchFileName() {
	return matchFileName;
}

public void setMatchFileName(String matchFileName) {
	this.matchFileName = matchFileName;
}

public String getReadPhotoColumn() {
	return readPhotoColumn;
}

public void setReadPhotoColumn(String readPhotoColumn) {
	this.readPhotoColumn = readPhotoColumn;
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

public int getReducedOvers() {
	return reducedOvers;
}

public void setReducedOvers(int reducedOvers) {
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

public String getMatchStartTime() {
	return matchStartTime;
}

public void setMatchStartTime(String matchStartTime) {
	this.matchStartTime = matchStartTime;
}

public String getMatchTimeStatus() {
	return matchTimeStatus;
}

public void setMatchTimeStatus(String matchTimeStatus) {
	this.matchTimeStatus = matchTimeStatus;
}

public long getMatchTotalSeconds() {
	return matchTotalSeconds;
}

public void setMatchTotalSeconds(long matchTotalSeconds) {
	this.matchTotalSeconds = matchTotalSeconds;
}

public String getBowlerRunning() {
	return bowlerRunning;
}

public void setBowlerRunning(String bowlerRunning) {
	this.bowlerRunning = bowlerRunning;
}

public String getBallRelease() {
	return ballRelease;
}

public void setBallRelease(String ballRelease) {
	this.ballRelease = ballRelease;
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

public List<InningJson> getInning() {
	return inning;
}

public void setInning(List<InningJson> inning) {
	this.inning = inning;
}

public List<DaySessionJson> getDaysSessions() {
	return daysSessions;
}

public void setDaysSessions(List<DaySessionJson> daysSessions) {
	this.daysSessions = daysSessions;
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

public List<ShotJson> getShots() {
	return shots;
}

public void setShots(List<ShotJson> shots) {
	this.shots = shots;
}

public List<EventJson> getEvents() {
	return events;
}

public void setEvents(List<EventJson> events) {
	this.events = events;
}

}