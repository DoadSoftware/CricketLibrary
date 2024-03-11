package com.cricket.model;

public class Teams_Total_Score_in_powerplays {
	private int TeamId;
	private String Team_Name;
	private String Team_Name4;
	private int Total_runs_pp1;
	private int Total_wickets_pp1;
	private int Total_runs_pp2;
	private int Total_wickets_pp2;
	private int Total_runs_pp3;
	private int Total_wickets_pp3;
	public int getTeamId() {
		return TeamId;
	}
	public void setTeamId(int teamId) {
		TeamId = teamId;
	}
	public String getTeam_Name() {
		return Team_Name;
	}
	public void setTeam_Name(String team_Name) {
		Team_Name = team_Name;
	}
	
	public String getTeam_Name4() {
		return Team_Name4;
	}
	public void setTeam_Name4(String team_Name4) {
		Team_Name4 = team_Name4;
	}
	public int getTotal_runs_pp1() {
		return Total_runs_pp1;
	}
	public void setTotal_runs_pp1(int total_runs_pp1) {
		Total_runs_pp1 = total_runs_pp1;
	}
	public int getTotal_wickets_pp1() {
		return Total_wickets_pp1;
	}
	public void setTotal_wickets_pp1(int total_wickets_pp1) {
		Total_wickets_pp1 = total_wickets_pp1;
	}
	
	
	public int getTotal_runs_pp2() {
		return Total_runs_pp2;
	}
	public void setTotal_runs_pp2(int total_runs_pp2) {
		Total_runs_pp2 = total_runs_pp2;
	}
	public int getTotal_wickets_pp2() {
		return Total_wickets_pp2;
	}
	public void setTotal_wickets_pp2(int total_wickets_pp2) {
		Total_wickets_pp2 = total_wickets_pp2;
	}
	public int getTotal_runs_pp3() {
		return Total_runs_pp3;
	}
	public void setTotal_runs_pp3(int total_runs_pp3) {
		Total_runs_pp3 = total_runs_pp3;
	}
	public int getTotal_wickets_pp3() {
		return Total_wickets_pp3;
	}
	public void setTotal_wickets_pp3(int total_wickets_pp3) {
		Total_wickets_pp3 = total_wickets_pp3;
	}
	public Teams_Total_Score_in_powerplays() {
		super();
	}
	
	public Teams_Total_Score_in_powerplays(int teamId, String team_Name,String team_Name4, int total_runs_pp1, int total_wickets_pp1,
			int total_runs_pp2, int total_wickets_pp2, int total_runs_pp3, int total_wickets_pp3) {
		super();
		TeamId = teamId;
		Team_Name = team_Name;
		Team_Name4 = team_Name4;
		Total_runs_pp1 = total_runs_pp1;
		Total_wickets_pp1 = total_wickets_pp1;
		Total_runs_pp2 = total_runs_pp2;
		Total_wickets_pp2 = total_wickets_pp2;
		Total_runs_pp3 = total_runs_pp3;
		Total_wickets_pp3 = total_wickets_pp3;
	}
	public Teams_Total_Score_in_powerplays(int teamId, String team_Name,String team_Name4, int total_runs_pp1, int total_wickets_pp1,
			int total_runs_pp2, int total_wickets_pp2) {
		super();
		TeamId = teamId;
		Team_Name = team_Name;
		Team_Name4 = team_Name4;
		Total_runs_pp1 = total_runs_pp1;
		Total_wickets_pp1 = total_wickets_pp1;
		Total_runs_pp2 = total_runs_pp2;
		Total_wickets_pp2 = total_wickets_pp2;
	}
	@Override
	public String toString() {
		return "Teams_Total_Score_in_powerplays [TeamId=" + TeamId + ", Team_Name=" + Team_Name + ", Team_Name4="
				+ Team_Name4 + ", Total_runs_pp1=" + Total_runs_pp1 + ", Total_wickets_pp1=" + Total_wickets_pp1
				+ ", Total_runs_pp2=" + Total_runs_pp2 + ", Total_wickets_pp2=" + Total_wickets_pp2
				+ ", Total_runs_pp3=" + Total_runs_pp3 + ", Total_wickets_pp3=" + Total_wickets_pp3 + "]";
	}
}
