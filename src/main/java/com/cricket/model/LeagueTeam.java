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
	
	@XmlElement(name="pool")
	private String pool;
	
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
	private double NetRunRate;
	
	@XmlElement(name="NRRR")
	private double NRRR;
	
	@XmlElement(name="Count")
	private int count;

	public String getQualifiedStatus() {
		return QualifiedStatus;
	}

	public void setQualifiedStatus(String qualifiedStatus) {
		QualifiedStatus = qualifiedStatus;
	}

	public String getPool() {
		return pool;
	}

	public void setPool(String pool) {
		this.pool = pool;
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

	public double getNetRunRate() {
		return NetRunRate;
	}

	public void setNetRunRate(double netRunRate) {
		NetRunRate = netRunRate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
   }

	public double getNRRR() {
		return NRRR;
	}

	public void setNRRR(double nRRR) {
		NRRR = nRRR;
	}
	
}
