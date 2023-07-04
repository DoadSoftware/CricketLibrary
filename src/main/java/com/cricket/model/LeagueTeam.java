package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueTeam {

	private String QualifiedStatus;
	
	private String pool;
	
	private String TeamName;
	
	private int Played;
	
	private int Won;
	
	private int Lost;
	
	private int NoResult;
	
	private String Points;
	
	private double NetRunRate;
	
	private String Count;

	
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

	public String getPoints() {
		return Points;
	}

	public void setPoints(String points) {
		Points = points;
	}

	public double getNetRunRate() {
		return NetRunRate;
	}

	public void setNetRunRate(double netRunRate) {
		NetRunRate = netRunRate;
	}

	public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}
	
}
