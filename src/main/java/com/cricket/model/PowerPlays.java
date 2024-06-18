package com.cricket.model;

import java.util.List;

public class PowerPlays {
	
	private Team Team;
	private List<Integer> Total_runs;
	private List<Integer> Total_wickets;
	
	private List<Integer> Total_four;
	private List<Integer> Total_dots;
	
	private List<Integer> Total_sixs;
	private List<Integer> Total_nines;
	
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
	public List<Integer> getTotal_four() {
		return Total_four;
	}
	public void setTotal_four(List<Integer> total_four) {
		Total_four = total_four;
	}
	public List<Integer> getTotal_dots() {
		return Total_dots;
	}
	public void setTotal_dots(List<Integer> total_dots) {
		Total_dots = total_dots;
	}
	public List<Integer> getTotal_sixs() {
		return Total_sixs;
	}
	public void setTotal_sixs(List<Integer> total_sixs) {
		Total_sixs = total_sixs;
	}
	public List<Integer> getTotal_nines() {
		return Total_nines;
	}
	public void setTotal_nines(List<Integer> total_nines) {
		Total_nines = total_nines;
	}
	public PowerPlays(Team team, List<Integer> total_runs, List<Integer> total_wickets) {
		super();
		Team = team;
		Total_runs = total_runs;
		Total_wickets = total_wickets;
	}
	
	public PowerPlays(com.cricket.model.Team team, List<Integer> total_runs, List<Integer> total_wickets,
			List<Integer> total_four, List<Integer> total_dots, List<Integer> total_sixs, List<Integer> total_nines) {
		super();
		Team = team;
		Total_runs = total_runs;
		Total_wickets = total_wickets;
		Total_four = total_four;
		Total_dots = total_dots;
		Total_sixs = total_sixs;
		Total_nines = total_nines;
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