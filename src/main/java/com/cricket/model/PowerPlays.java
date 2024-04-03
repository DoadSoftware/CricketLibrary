package com.cricket.model;

import java.util.List;

public class PowerPlays {
	private Team Team;
	private List<Integer> Total_runs;
	private List<Integer> Total_wickets;
	
	public Team getTeam() {
		return Team;
	}
	public void setTeam(Team team) {
		Team = team;
	}
	public List<Integer> getTotal_runs() {
		return Total_runs;
	}
	public void setTotal_runs(List<Integer> total_runs) {
		Total_runs = total_runs;
	}
	public List<Integer> getTotal_wickets() {
		return Total_wickets;
	}
	public void setTotal_wickets(List<Integer> total_wickets) {
		Total_wickets = total_wickets;
	}
	
	
	public PowerPlays(Team team, List<Integer> total_runs, List<Integer> total_wickets) {
		super();
		Team = team;
		Total_runs = total_runs;
		Total_wickets = total_wickets;
	}
	public PowerPlays() {
		super();
	}
	@Override
	public String toString() {
		return "PowerPlays [Team=" + Team + ", Total_runs=" + Total_runs + ", Total_wickets=" + Total_wickets
				+ ", getTeam()=" + getTeam() + ", getTotal_runs()=" + getTotal_runs() + ", getTotal_wickets()="
				+ getTotal_wickets() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}