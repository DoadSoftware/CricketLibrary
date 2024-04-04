package com.cricket.model;

import java.util.HashMap;
import java.util.List;

public class AllEvents{
	
	private String This_Over;
	
	private List<Integer> Last_Over;
	
	private List<Integer> Last_30_Balls;
	
	private List<Team> team_summary;
	
	private List<Player> batsman_summary;
	
	private List<BowlingCard> bowler_summary;
	
	private int ballsSinceLastBoundary;
	
	private int lastBowlerId;
	
	private int secondlastBowlerId;
	
	private List<Integer>bothInningDotBalls;
	
	private List<Integer>This_over_run_wk;
	
	private List<Player> allRounderCatches;
	
	private Inning inningComaprision;
	
	private HashMap<String,List<OverByOverData>> overByOverData;
	
	private List<PowerPlays> powerplay;
	
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

	public Inning getInningComaprision() {
		return inningComaprision;
	}

	public void setInningComaprision(Inning inningComaprision) {
		this.inningComaprision = inningComaprision;
	}


	public List<Team> getTeam_summary() {
		return team_summary;
	}

	public void setTeam_summary(List<Team> team_summary) {
		this.team_summary = team_summary;
	}

	public List<Player> getBatsman_summary() {
		return batsman_summary;
	}

	public void setBatsman_summary(List<Player> batsman_summary) {
		this.batsman_summary = batsman_summary;
	}

	public List<BowlingCard> getBowler_summary() {
		return bowler_summary;
	}

	public void setBowler_summary(List<BowlingCard> bowler_summary) {
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

	public AllEvents(String this_Over, List<Integer> last_Over, List<Integer> last_30_Balls, int ballsSinceLastBoundary,
			int lastBowlerId,int secondlastBowlerId, List<Integer> bothInningDotBalls, List<Integer> this_over_run_wk,
			Inning inningComaprision) {
		super();
		This_Over = this_Over;
		Last_Over = last_Over;
		Last_30_Balls = last_30_Balls;
		this.ballsSinceLastBoundary = ballsSinceLastBoundary;
		this.lastBowlerId = lastBowlerId;
		this.secondlastBowlerId = secondlastBowlerId;
		this.bothInningDotBalls = bothInningDotBalls;
		This_over_run_wk = this_over_run_wk;
		this.inningComaprision = inningComaprision;
	}

	public AllEvents() {
		super();
		// TODO Auto-generated constructor stub
	}
}
