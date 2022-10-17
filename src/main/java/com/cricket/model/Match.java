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

  @XmlElement(name = "followOn")
  private String followOn;

  @XmlElement(name = "followOnThreshold")
  private int followOnThreshold;
  
  @XmlElement(name = "substitutesPerTeam")
  private int substitutesPerTeam;
	
  @XmlElement(name = "reviewsPerTeam")
  private String reviewsPerTeam;
	
  @XmlElement(name = "matchFileTimeStamp")
  private String matchFileTimeStamp;

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

  @XmlElement(name = "targetRuns")
  private int targetRuns;

  @XmlElement(name = "targetType")
  private String targetType;
  
  @XmlElement(name = "targetOvers")
  private String targetOvers;

  @XmlElement(name = "venueName")
  private String venueName;

  @XmlElement(name = "matchStartTime")
  private String matchStartTime;

  @XmlElement(name = "matchTimeStatus")
  private String matchTimeStatus;

  @XmlElement(name = "matchTotalSeconds")
  private long matchTotalSeconds;
  
  @XmlElement(name = "bowlerRunning")
  private String bowlerRunning;

  @XmlElement(name = "ballRelease")
  private String ballRelease;
  
  @XmlTransient
  private Ground ground;
  
  @XmlTransient
  private NameSuper nameSuper;

  @XmlTransient
  private Team homeTeam;

  @XmlTransient
  private Team awayTeam;

  @XmlTransient
  private int selected_inning;
  
  @XmlTransient
  private int which_key_press;
  
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

  @XmlTransient
  private List<Event> events;
  
public int getFollowOnThreshold() {
	return followOnThreshold;
}
public void setFollowOnThreshold(int followOnThreshold) {
	this.followOnThreshold = followOnThreshold;
}
public String getFollowOn() {
	return followOn;
}
public void setFollowOn(String followOn) {
	this.followOn = followOn;
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
public List<Player> getHomeSubstitutes() {
	return homeSubstitutes;
}
public void setHomeSubstitutes(List<Player> homeSubstitutes) {
	this.homeSubstitutes = homeSubstitutes;
}
public List<Player> getAwaySubstitutes() {
	return awaySubstitutes;
}
public void setAwaySubstitutes(List<Player> awaySubstitutes) {
	this.awaySubstitutes = awaySubstitutes;
}
public int getSubstitutesPerTeam() {
	return substitutesPerTeam;
}
public void setSubstitutesPerTeam(int substitutesPerTeam) {
	this.substitutesPerTeam = substitutesPerTeam;
}
public String getTargetType() {
	return targetType;
}
public void setTargetType(String targetType) {
	this.targetType = targetType;
}
public String getMatchStatus() {
	return matchStatus;
}
public void setMatchStatus(String matchStatus) {
	this.matchStatus = matchStatus;
}
public List<Event> getEvents() {
	return events;
}
public void setEvents(List<Event> events) {
	this.events = events;
}
public String getMatchResult() {
	return matchResult;
}
public void setMatchResult(String matchResult) {
	this.matchResult = matchResult;
}
public List<DaySession> getDaysSessions() {
	return daysSessions;
}
public void setDaysSessions(List<DaySession> daysSessions) {
	this.daysSessions = daysSessions;
}
public long getMatchTotalSeconds() {
	return matchTotalSeconds;
}
public void setMatchTotalSeconds(long matchTotalSeconds) {
	this.matchTotalSeconds = matchTotalSeconds;
}
public int getSelected_inning() {
	return selected_inning;
}
public void setSelected_inning(int selected_inning) {
	this.selected_inning = selected_inning;
}
public String getMatchTimeStatus() {
	return matchTimeStatus;
}
public void setMatchTimeStatus(String matchTimeStatus) {
	this.matchTimeStatus = matchTimeStatus;
}
public String getMatchStartTime() {
	return matchStartTime;
}
public void setMatchStartTime(String matchStartTime) {
	this.matchStartTime = matchStartTime;
}
public String getReadPhotoColumn() {
	return readPhotoColumn;
}
public void setReadPhotoColumn(String readPhotoColumn) {
	this.readPhotoColumn = readPhotoColumn;
}
public String getVenueName() {
	return venueName;
}
public void setVenueName(String venueName) {
	this.venueName = venueName;
}
public String getMatchFileTimeStamp() {
	return matchFileTimeStamp;
}
public void setMatchFileTimeStamp(String matchFileTimeStamp) {
	this.matchFileTimeStamp = matchFileTimeStamp;
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
public int getNumberOfPowerplays() {
	return numberOfPowerplays;
}
public void setNumberOfPowerplays(int numberOfPowerplays) {
	this.numberOfPowerplays = numberOfPowerplays;
}
public String getReviewsPerTeam() {
	return reviewsPerTeam;
}
public void setReviewsPerTeam(String reviewsPerTeam) {
	this.reviewsPerTeam = reviewsPerTeam;
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
public int getTargetRuns() {
	return targetRuns;
}
public void setTargetRuns(int targetRuns) {
	this.targetRuns = targetRuns;
}
public String getTargetOvers() {
	return targetOvers;
}
public void setTargetOvers(String targetOvers) {
	this.targetOvers = targetOvers;
}
public List<Player> getHomeSquad() {
	return homeSquad;
}
public void setHomeSquad(List<Player> homeSquad) {
	this.homeSquad = homeSquad;
}
public List<Player> getAwaySquad() {
	return awaySquad;
}
public void setAwaySquad(List<Player> awaySquad) {
	this.awaySquad = awaySquad;
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
public int getGroundId() {
	return groundId;
}
public void setGroundId(int groundId) {
	this.groundId = groundId;
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
public int getTossWinningTeam() {
	return tossWinningTeam;
}
public void setTossWinningTeam(int tossWinningTeam) {
	this.tossWinningTeam = tossWinningTeam;
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
public String getMatchType() {
	return matchType;
}
public void setMatchType(String matchType) {
	this.matchType = matchType;
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
public String getMatchFileName() {
	return matchFileName;
}
public void setMatchFileName(String matchFileName) {
	this.matchFileName = matchFileName;
}
public List<Inning> getInning() {
	return inning;
}
public void setInning(List<Inning> inning) {
	this.inning = inning;
}
public int getWhich_key_press() {
	return which_key_press;
}
public void setWhich_key_press(int which_key_press) {
	this.which_key_press = which_key_press;
}


}