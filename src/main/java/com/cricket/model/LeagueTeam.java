package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="LeagueTeam")
@XmlAccessorType(XmlAccessType.FIELD)
public class LeagueTeam {
	@XmlElement(name="QualifiedStatus")
	private String QualifiedStatus;
	
	@XmlElement(name="TeamName")
	private String TeamName;
	
	@XmlElement(name="Played")
	private int Played;
	
	@XmlElement(name="Won")
	private int Won;
	
	@XmlElement(name="Lost")
	private int Lost;
	
	@XmlElement(name="NoResult")
	private int NoResult;
	
	@XmlElement(name="Points")
	private int Points;
	
	@XmlElement(name="NetRunRate")
	private int NetRunRate;

	
	public String getQualifiedStatus() {
		return QualifiedStatus;
	}

	public void setQualifiedStatus(String qualifiedStatus) {
		QualifiedStatus = qualifiedStatus;
	}

	public String getTeamName() {
		return TeamName;
	}

	public void setTeamName(String teamName) {
		TeamName = teamName;
	}

	public int getPlayed() {
		return Played;
	}

	public void setPlayed(int played) {
		Played = played;
	}

	public int getWon() {
		return Won;
	}

	public void setWon(int won) {
		Won = won;
	}

	public int getLost() {
		return Lost;
	}

	public void setLost(int lost) {
		Lost = lost;
	}

	public int getNoResult() {
		return NoResult;
	}

	public void setNoResult(int noResult) {
		NoResult = noResult;
	}

	public int getPoints() {
		return Points;
	}

	public void setPoints(int points) {
		Points = points;
	}

	public int getNetRunRate() {
		return NetRunRate;
	}

	public void setNetRunRate(int netRunRate) {
		NetRunRate = netRunRate;
	}
}
