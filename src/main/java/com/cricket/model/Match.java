package com.cricket.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="Match")
@XmlAccessorType(XmlAccessType.FIELD)
public class Match {

  @XmlElement(name = "generateInteractiveFile")
  private String generateInteractiveFile;
	
  @XmlElement(name = "wagonXOffSet")
  private int wagonXOffSet;

  @XmlElement(name = "wagonYOffSet")
  private int wagonYOffSet;
  
  @XmlElement(name = "followOn")
  private String followOn;

  @XmlElement(name = "followOnThreshold")
  private int followOnThreshold;

  @XmlElement(name = "homeSubstitutesNumber")
  private int homeSubstitutesNumber;
  
  @XmlElement(name = "awaySubstitutesNumber")
  private int awaySubstitutesNumber;
	
  @XmlElement(name = "reviewsPerTeam")
  private String reviewsPerTeam;
	
  @XmlElement(name = "matchFileTimeStamp")
  private String matchFileTimeStamp;

  @XmlElement(name = "speedFilePath")
  private String speedFilePath;

  @XmlElement(name = "matchType")
  private String matchType;
  
  @XmlElement(name = "matchResult")
  private String matchResult;

  @XmlElement(name = "matchStatus")
  private String matchStatus;
  
  @XmlElement(name = "matchFileName")
  private String matchFileName;

  @XmlElement(name = "readPhotoColumn")
  private String readPhotoColumn;
  
  @XmlElement(name = "tossResult")
  private String tossResult;

  @XmlElement(name = "tossWinningDecision")
  private String tossWinningDecision;

  @XmlElement(name = "tournament")
  private String tournament;

  @XmlElement(name = "matchIdent")
  private String matchIdent;

  @XmlElement(name = "tossWinningTeam")
  private int tossWinningTeam;

  @XmlElement(name = "numberOfPowerplays")
  private int numberOfPowerplays;
  
  @XmlElement(name = "maxOvers")
  private int maxOvers;

  @XmlElement(name = "reducedOvers")
  private int reducedOvers;
  
  @XmlElement(name = "homeTeamId")
  private int homeTeamId;

  @XmlElement(name = "awayTeamId")
  private int awayTeamId;

  @XmlElement(name = "groundId")
  private int groundId;

  @XmlElement(name = "seasonId")
  private int seasonId;
  
  @XmlElement(name = "targetRuns")
  private int targetRuns;

  @XmlElement(name = "targetType")
  private String targetType;
  
  @XmlElement(name = "targetOvers")
  private String targetOvers;

  @XmlElement(name = "venueName")
  private String venueName;

  @XmlElement(name = "matchTotalSeconds")
  private long matchTotalSeconds;
  
  @XmlElement(name = "bowlerRunning")
  private String bowlerRunning;

  @XmlElement(name = "ballRelease")
  private String ballRelease;

  @XmlElement(name = "homeTeam")
  private Team homeTeam;

  @XmlElement(name = "awayTeam")
  private Team awayTeam;

  @XmlElement(name = "setupHomeTeam")
  private String setupHomeTeam;

  @XmlElement(name = "setupAwayTeam")
  private String setupAwayTeam;
  
  @XmlElement(name = "currentSpeed")
  private String current_speed;

  @XmlElementWrapper(name = "innings")
  @XmlElement(name = "inning")
  private List<Inning> inning;

  @XmlElementWrapper(name = "daysSessions")
  @XmlElement(name = "daySession")
  private List<DaySession> daysSessions;
  
  @XmlElementWrapper(name = "homeSquad")
  @XmlElement(name = "homeSquad")
  private List<Player> homeSquad;

  @XmlElementWrapper(name = "homeSubstitutes")
  @XmlElement(name = "homeSubstitutes")
  private List<Player> homeSubstitutes;
  
  @XmlElementWrapper(name = "awaySquad")
  @XmlElement(name = "awaySquad")
  private List<Player> awaySquad;

  @XmlElementWrapper(name = "awaySubstitutes")
  @XmlElement(name = "awaySubstitutes")
  private List<Player> awaySubstitutes;
  
  @XmlElementWrapper(name = "homeOtherSquad")
  @XmlElement(name = "homeOtherSquad")
  private List<Player> homeOtherSquad;

  @XmlElementWrapper(name = "awayOtherSquad")
  @XmlElement(name = "awayOtherSquad")
  private List<Player> awayOtherSquad;

  @XmlElementWrapper(name = "shots")
  @XmlElement(name = "shot")
  private List<Shot> shots;

  @XmlElementWrapper(name = "wagons")
  @XmlElement(name = "wagon")
  private List<Wagon> wagons;
  
  @XmlElementWrapper(name = "events")
  @XmlElement(name = "event")
  private List<Event> events;

  @XmlTransient
  private MatchClock clock;
  
  @XmlTransient
  private Ground ground;
  
  @XmlTransient
  private NameSuper nameSuper;

  @XmlTransient
  private int selected_inning;
  
  @XmlTransient
  private int which_key_press;
  
public MatchClock getClock() {
	return clock;
}

public void setClock(MatchClock clock) {
	this.clock = clock;
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

public String getCurrent_speed() {
	return current_speed;
}

public void setCurrent_speed(String current_speed) {
	this.current_speed = current_speed;
}

public String getSpeedFilePath() {
	return speedFilePath;
}

public void setSpeedFilePath(String speedFilePath) {
	this.speedFilePath = speedFilePath;
}

public List<Wagon> getWagons() {
	return wagons;
}

public void setWagons(List<Wagon> wagons) {
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

public List<Inning> getInning() {
	return inning;
}

public void setInning(List<Inning> inning) {
	this.inning = inning;
}

public List<DaySession> getDaysSessions() {
	return daysSessions;
}

public void setDaysSessions(List<DaySession> daysSessions) {
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

public List<Shot> getShots() {
	return shots;
}

public void setShots(List<Shot> shots) {
	this.shots = shots;
}

public List<Event> getEvents() {
	return events;
}

public void setEvents(List<Event> events) {
	this.events = events;
}

}