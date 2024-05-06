package com.cricket.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatchStats{
	
	private ThisOverData overData;
	private BowlingCard BowlingCard; 
	private List<Team> team_Summary; 
	private int ballsSinceLastBoundary; 
	private InningCompare LastoverData;
	private InningCompare inningCompare;
	private InningCompare LastThirtyBalls;

	public MatchStats() {
		super();
		this.overData = new ThisOverData();
		this.inningCompare = new InningCompare();
		this.LastThirtyBalls = new InningCompare();
		this.LastThirtyBalls.setTotalBalls(30);
		this.LastoverData = new InningCompare();
		this.BowlingCard = new BowlingCard();
		this.team_Summary = new ArrayList<>(Arrays.asList(new Team(),new Team()));
	}

	
	public List<Team> getTeam_Summary() {
		return team_Summary;
	}


	public void setTeam_Summary(List<Team> team_Summary) {
		this.team_Summary = team_Summary;
	}


	@Override
	public String toString() {
		return "MatchStats [overData=" + overData + ", BowlingCard=" + BowlingCard + ", team_Summary=" + team_Summary
				+ ", ballsSinceLastBoundary=" + ballsSinceLastBoundary + ", LastoverData=" + LastoverData
				+ ", inningCompare=" + inningCompare + ", LastThirtyBalls=" + LastThirtyBalls + "]";
	}


	public BowlingCard getBowlingCard() {
		return BowlingCard;
	}
	public void setBowlingCard(BowlingCard bowlingCard) {
		BowlingCard = bowlingCard;
	}
	public ThisOverData getOverData() {
		return overData;
	}

	public int getBallsSinceLastBoundary() {
		return ballsSinceLastBoundary;
	}


	public void setBallsSinceLastBoundary(int ballsSinceLastBoundary) {
		this.ballsSinceLastBoundary = ballsSinceLastBoundary;
	}


	public void setOverData(ThisOverData overData) {
		this.overData = overData;
	}

	public InningCompare getInningCompare() {
		return inningCompare;
	}

	public void setInningCompare(InningCompare InningCompare) {
		this.inningCompare = InningCompare;
	}

	public InningCompare getLastThirtyBalls() {
		return LastThirtyBalls;
	}

	public void setLastThirtyBalls(InningCompare last30Balls) {
		this.LastThirtyBalls = last30Balls;
	}
	public InningCompare getLastoverData() {
		return LastoverData;
	}

	public void setLastoverData(InningCompare lastoverData) {
		LastoverData = lastoverData;
	}
	public class Team{
		private String name;
		private int totalRuns;
		private int id;
		private int totalWickets;
		private int totalDots;
		private int totalones;
		private int totalTwos;
		private int totalThrees;
		private int totalFours;
		private int totalFives;
		private int totalSixes;
		private int totalNines;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getTotalRuns() {
			return totalRuns;
		}
		public void setTotalRuns(int totalRuns) {
			this.totalRuns = totalRuns;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public int getTotalWickets() {
			return totalWickets;
		}
		public void setTotalWickets(int totalWickets) {
			this.totalWickets = totalWickets;
		}
		public int getTotalDots() {
			return totalDots;
		}
		public void setTotalDots(int totalDots) {
			this.totalDots = totalDots;
		}
		public int getTotalFours() {
			return totalFours;
		}
		public void setTotalFours(int totalFours) {
			this.totalFours = totalFours;
		}
		public int getTotalSixes() {
			return totalSixes;
		}
		public void setTotalSixes(int totalSixes) {
			this.totalSixes = totalSixes;
		}
		public int getTotalNines() {
			return totalNines;
		}
		public void setTotalNines(int totalNines) {
			this.totalNines = totalNines;
		}
		
		public int getTotalOnes() {
			return totalones;
		}
		public void setTotalOnes(int totalones) {
			this.totalones = totalones;
		}
		public int getTotalTwos() {
			return totalTwos;
		}
		public void setTotalTwos(int totalTwos) {
			this.totalTwos = totalTwos;
		}
		public int getTotalThrees() {
			return totalThrees;
		}
		public void setTotalThrees(int totalThrees) {
			this.totalThrees = totalThrees;
		}
		public int getTotalFives() {
			return totalFives;
		}
		public void setTotalFives(int totalFives) {
			this.totalFives = totalFives;
		}
		public Team() {
			super();
			this.totalDots =0;
			this.totalRuns =0;
			this.id =0;
			this.totalWickets = 0;
			this.totalones =0;
			this.totalTwos = 0;
			this.totalThrees = 0;
			this.totalFours =0;
			this.totalFives = 0;
			this.totalNines = 0;
			this.totalSixes = 0;
			
		}
		@Override
		public String toString() {
			return "Team [name=" + name + ", totalRuns=" + totalRuns + ", id=" + id + ", totalWickets=" + totalWickets
					+ ", totalDots=" + totalDots + ", totalFours=" + totalFours + ", totalSixes=" + totalSixes
					+ ", totalNines=" + totalNines + "]";
		}
		
	}
	public class InningCompare{

		private int totalRuns;
		private int totalBalls;
		private int totalWickets;
		private int totalDots;
		private int totalFours;
		private int totalSixes;
		private int totalNines;
		
		public int getTotalRuns() {
			return totalRuns;
		}
		public void setTotalRuns(int totalRuns) {
			this.totalRuns = totalRuns;
		}
		public int getTotalWickets() {
			return totalWickets;
		}
		public void setTotalWickets(int totalWickets) {
			this.totalWickets = totalWickets;
		}
		public int getTotalDots() {
			return totalDots;
		}
		public void setTotalDots(int totalDots) {
			this.totalDots = totalDots;
		}
		public int getTotalFours() {
			return totalFours;
		}
		public void setTotalFours(int totalFours) {
			this.totalFours = totalFours;
		}
		public int getTotalSixes() {
			return totalSixes;
		}
		public void setTotalSixes(int totalSixes) {
			this.totalSixes = totalSixes;
		}
		public int getTotalNines() {
			return totalNines;
		}
		public void setTotalNines(int totalNines) {
			this.totalNines = totalNines;
		}
		
		public int getTotalBalls() {
			return totalBalls;
		}
		public void setTotalBalls(int totalBalls) {
			this.totalBalls = totalBalls;
		}
		public InningCompare() {
			super();
			this.totalDots =0;
			this.totalRuns =0;
			this.totalBalls =0;
			this.totalWickets = 0;
			this.totalNines = 0;
			this.totalSixes = 0;
			this.totalFours =0;

		}
		@Override
		public String toString() {
			return "InningCompare [totalRuns=" + totalRuns + ", totalBalls=" + totalBalls + ", totalWickets="
					+ totalWickets + ", totalDots=" + totalDots + ", totalFours=" + totalFours + ", totalSixes="
					+ totalSixes + ", totalNines=" + totalNines + "]";
		}
		
	}
	
	public class ThisOverData{
		
		private int totalRuns;
		private int totalWickets;
		private String thisOverTxt;
		
		public int getTotalRuns() {
			return totalRuns;
		}
		public void setTotalRuns(int totalRuns) {
			this.totalRuns = totalRuns;
		}
		public int getTotalWickets() {
			return totalWickets;
		}
		public void setTotalWickets(int totalWickets) {
			this.totalWickets = totalWickets;
		}
		public String getThisOverTxt() {
			return thisOverTxt;
		}
		public void setThisOverTxt(String thisOverTxt) {
			this.thisOverTxt = thisOverTxt;
		}
		public ThisOverData() {
			super();
			this.totalRuns = 0;
			this.totalWickets = 0;
			this.thisOverTxt = "";
		}
		@Override
		public String toString() {
			return "ThisOverData [totalRuns=" + totalRuns + ", totalWickets=" + totalWickets + ", thisOverTxt="
					+ thisOverTxt + "]";
		}
		
	}
	
	public class BowlingCard{
		
		private int Last_bowler_id;
		private int SLast_bowler_id;
		private String name;
		private int totalRuns;
		private int totalWickets;

		
		

		public int getLast_bowler_id() {
			return Last_bowler_id;
		}

		public void setLast_bowler_id(int last_bowler_id) {
			Last_bowler_id = last_bowler_id;
		}

		public int getSLast_bowler_id() {
			return SLast_bowler_id;
		}
		public void setSLast_bowler_id(int sLast_bowler_id) {
			SLast_bowler_id = sLast_bowler_id;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		public int getTotalRuns() {
			return totalRuns;
		}

		public void setTotalRuns(int totalRuns) {
			this.totalRuns = totalRuns;
		}

		public int getTotalWickets() {
			return totalWickets;
		}

		public void setTotalWickets(int totalWickets) {
			this.totalWickets = totalWickets;
		}

		public BowlingCard() {
			super();
			this.Last_bowler_id = 0;
			this.SLast_bowler_id = 0;
			this.totalWickets = 0;
			this.totalWickets = 0;
			this.name = "";
		}

		@Override
		public String toString() {
			return "BowlingCard [Last_bowler_id=" + Last_bowler_id + ", SLast_bowler_id=" + SLast_bowler_id + ", name="
					+ name + ", totalRuns=" + totalRuns + ", totalWickets=" + totalWickets + "]";
		}
		
		
	}
}