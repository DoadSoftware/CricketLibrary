package com.Ae_Third_Party_Xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Cricket")
@XmlAccessorType(XmlAccessType.FIELD)
public class AE_Cricket {
	
	@XmlElement(name="MatchDetails")
	private AE_MatchDetails matchDetails;
	public AE_MatchDetails getMatchDetails() {
		return matchDetails;
	}
	public void setMatchDetails(AE_MatchDetails matchDetails) {
		this.matchDetails = matchDetails;
	}
	@XmlElementWrapper(name="InningsScores")
	@XmlElement(name="Innings")
	private List<AE_Inning> inning;
	public List<AE_Inning> getInning() {
		return inning;
	}

	public void setInning(List<AE_Inning> inning) {
		this.inning = inning;
	}
	@XmlElementWrapper(name="Teams")
	@XmlElement(name="Team")
	private List<AE_Team> Team;
	public List<AE_Team> getTeam() {
		return Team;
	}
	public void setTeam(List<AE_Team> team) {
		Team = team;
	}
	@XmlElement(name="CurrentPosition")
    private AE_CurrentPosition CurrentPosition ;
	public AE_CurrentPosition getCurrentPosition() {
		return CurrentPosition;
	}

	public void setCurrentPosition(AE_CurrentPosition currentPosition) {
		CurrentPosition = currentPosition;
	}
	@XmlElementWrapper(name="CareerDetails")
	@XmlElement(name="Player")
	private List<AE_Player>player;

	public List<AE_Player> getPlayer() {
		return player;
	}

	public void setPlayer(List<AE_Player> player) {
		this.player = player;
	}
	
	
	@XmlElement(name="SessionDetails")
	private AE_SessionDetails sessionDetails;
	
	public AE_SessionDetails getSessionDetails() {
		return sessionDetails;
	}

	public void setPlayer(AE_SessionDetails sessionDetails) {
		this.sessionDetails = sessionDetails;
	}
	
	
	@XmlElementWrapper(name="CurrentOver")
	@XmlElement(name="Ball")
	private List<AE_Ball> balls;
	public List<AE_Ball> getBalls() {
		return balls;
	}

	public void setBalls(List<AE_Ball> balls) {
		this.balls = balls;
	}

	@Override
	public String toString() {
		return "Cricket [inning=" + inning + ", player=" + player + "]";
	}

	

	

}
