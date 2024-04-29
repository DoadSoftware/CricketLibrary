package com.cricket.model;

import java.util.HashMap;
import java.util.List;

public class AllEvents{
	
	private String This_Over;
	
	private List<Integer> Last_Over;
	
	private List<Integer> Last_30_Balls;
	
	private List<Team> team_summary;
	
	private List<BattingCard> batsman_summary;
	
	private List<Player> bowler_summary;
	
	private int ballsSinceLastBoundary;
	
	private int lastBowlerId;
	
	private int secondlastBowlerId;
	
	private List<Integer> bothInningDotBalls;
	
	private List<Integer> This_over_run_wk;
	
	private List<Player> allRounderCatches;
	
	private Inning inningComparison;
	
	private HashMap<String,List<OverByOverData>> overByOverData;
	
	private List<PowerPlays> powerplay;
	
	//batsman stats again bolwler
	private List<BattingCard> BatStats;
	
	//bolwler stats again batsman
	private List<Player> BallStats;
	
	public String getThis_Over() {
		return This_Over;
	}

	public void setThis_Over(String this_Over) {
		This_Over = this_Over;
	}

	public List<Integer> getLast_Over() {
		return Last_Over;
	}

	public void setLast_Over(List<Integer> last_Over) {
		Last_Over = last_Over;
	}

	public List<Integer> getLast_30_Balls() {
		return Last_30_Balls;
	}

	public void setLast_30_Balls(List<Integer> last_30_Balls) {
		Last_30_Balls = last_30_Balls;
	}
	public int getBallsSinceLastBoundary() {
		return ballsSinceLastBoundary;
	}
	public void setBallsSinceLastBoundary(int ballsSinceLastBoundary) {
		this.ballsSinceLastBoundary = ballsSinceLastBoundary;
	}

	public int getLastBowlerId() {
		return lastBowlerId;
	}

	public void setLastBowlerId(int lastBowlerId) {
		this.lastBowlerId = lastBowlerId;
	}

	public List<Integer> getBothInningDotBalls() {
		return bothInningDotBalls;
	}

	public void setBothInningDotBalls(List<Integer> bothInningDotBalls) {
		this.bothInningDotBalls = bothInningDotBalls;
	}

	public List<Integer> getThis_over_run_wk() {
		return This_over_run_wk;
	}
	public void setThis_over_run_wk(List<Integer> this_over_run_wk) {
		This_over_run_wk = this_over_run_wk;
	}

	public List<Player> getAllRounderCatches() {
		return allRounderCatches;
	}


	public void setAllRounderCatches(List<Player> allRounderCatches) {
		this.allRounderCatches = allRounderCatches;
	}

	public Inning getInningComparison() {
		return inningComparison;
	}

	public void setInningComparison(Inning inningComparison) {
		this.inningComparison = inningComparison;
	}


	public List<Team> getTeam_summary() {
		return team_summary;
	}

	public void setTeam_summary(List<Team> team_summary) {
		this.team_summary = team_summary;
	}

	public List<BattingCard> getBatsman_summary() {
		return batsman_summary;
	}

	public void setBatsman_summary(List<BattingCard> batsman_summary) {
		this.batsman_summary = batsman_summary;
	}

	public List<Player> getBowler_summary() {
		return bowler_summary;
	}

	public void setBowler_summary(List<Player> bowler_summary) {
		this.bowler_summary = bowler_summary;
	}

	public HashMap<String, List<OverByOverData>> getOverByOverData() {
		return overByOverData;
	}

	public void setOverByOverData(HashMap<String, List<OverByOverData>> overByOverData) {
		this.overByOverData = overByOverData;
	}

	public List<PowerPlays> getPowerplay() {
		return powerplay;
	}

	public void setPowerplay(List<PowerPlays> powerplay) {
		this.powerplay = powerplay;
	}

	public int getSecondlastBowlerId() {
		return secondlastBowlerId;
	}

	public void setSecondlastBowlerId(int secondlastBowlerId) {
		this.secondlastBowlerId = secondlastBowlerId;
	}
	
	public List<BattingCard> getBatStats() {
		return BatStats;
	}

	public void setBatStats(List<BattingCard> batStats) {
		BatStats = batStats;
	}

	public List<Player> getBallStats() {
		return BallStats;
	}

	public void setBallStats(List<Player> list) {
		BallStats = list;
	}

	public AllEvents(String this_Over, List<Integer> last_Over, List<Integer> last_30_Balls, int ballsSinceLastBoundary,
			int lastBowlerId,int secondlastBowlerId, List<Integer> bothInningDotBalls, List<Integer> this_over_run_wk,
			Inning inningComparison) {
		super();
		This_Over = this_Over;
		Last_Over = last_Over;
		Last_30_Balls = last_30_Balls;
		this.ballsSinceLastBoundary = ballsSinceLastBoundary;
		this.lastBowlerId = lastBowlerId;
		this.secondlastBowlerId = secondlastBowlerId;
		this.bothInningDotBalls = bothInningDotBalls;
		This_over_run_wk = this_over_run_wk;
		this.inningComparison = inningComparison;
	}

	public AllEvents() {
		super();
	}
}
