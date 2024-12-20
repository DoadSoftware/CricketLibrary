package com.cricket.model;

import java.util.ArrayList;
import java.util.List;

public class MatchStats{
	
	private VariousStats overData;
	private List<VariousStats> playerStats; 
	private List<OverByOverData> homeOverByOverData; 
	private List<OverByOverData> awayOverByOverData; 
	private VariousStats bowlingCard; 
	private int ballsSinceLastBoundary; 
	private VariousStats lastOverData;
	private VariousStats inningCompare;
	private VariousStats lastThirtyBalls;
	private VariousStats homeTeamScoreData;
	private VariousStats awayTeamScoreData;

	private VariousStats homeFirstPowerPlay;
	private VariousStats homeSecondPowerPlay;
	private VariousStats homeThirdPowerPlay;
	
	private VariousStats awayFirstPowerPlay;
	private VariousStats awaySecondPowerPlay;
	private VariousStats awayThirdPowerPlay;
	
	public MatchStats() {
		super();
		this.overData = new VariousStats();
		this.inningCompare = new VariousStats();
		this.lastThirtyBalls = new VariousStats();
		this.lastThirtyBalls.setTotalBalls(30);
		this.lastOverData = new VariousStats();
		this.bowlingCard = new VariousStats();
		this.awayTeamScoreData = new VariousStats();
		this.homeTeamScoreData = new VariousStats();
		this.playerStats = new ArrayList<MatchStats.VariousStats>();
		this.homeFirstPowerPlay = new VariousStats();
		this.homeSecondPowerPlay = new VariousStats();
		this.homeThirdPowerPlay = new VariousStats();
		this.awayFirstPowerPlay = new VariousStats();
		this.awaySecondPowerPlay = new VariousStats();
		this.awayThirdPowerPlay = new VariousStats();
		this.homeOverByOverData = new ArrayList<OverByOverData>();
		this.awayOverByOverData = new ArrayList<OverByOverData>();
	}
	
	public List<VariousStats> getPlayerStats() {
		return playerStats;
	}

	public VariousStats getHomeFirstPowerPlay() {
		return homeFirstPowerPlay;
	}

	public void setHomeFirstPowerPlay(VariousStats homeFirstPowerPlay) {
		this.homeFirstPowerPlay = homeFirstPowerPlay;
	}

	public VariousStats getHomeSecondPowerPlay() {
		return homeSecondPowerPlay;
	}

	public void setHomeSecondPowerPlay(VariousStats homeSecondPowerPlay) {
		this.homeSecondPowerPlay = homeSecondPowerPlay;
	}

	public VariousStats getHomeThirdPowerPlay() {
		return homeThirdPowerPlay;
	}

	public void setHomeThirdPowerPlay(VariousStats homeThirdPowerPlay) {
		this.homeThirdPowerPlay = homeThirdPowerPlay;
	}

	public VariousStats getAwayFirstPowerPlay() {
		return awayFirstPowerPlay;
	}

	public void setAwayFirstPowerPlay(VariousStats awayFirstPowerPlay) {
		this.awayFirstPowerPlay = awayFirstPowerPlay;
	}

	public VariousStats getAwaySecondPowerPlay() {
		return awaySecondPowerPlay;
	}

	public void setAwaySecondPowerPlay(VariousStats awaySecondPowerPlay) {
		this.awaySecondPowerPlay = awaySecondPowerPlay;
	}

	public VariousStats getAwayThirdPowerPlay() {
		return awayThirdPowerPlay;
	}

	public void setAwayThirdPowerPlay(VariousStats awayThirdPowerPlay) {
		this.awayThirdPowerPlay = awayThirdPowerPlay;
	}

	public void setPlayerStats(List<VariousStats> playerStats) {
		this.playerStats = playerStats;
	}

	public List<OverByOverData> getHomeOverByOverData() {
		return homeOverByOverData;
	}

	public void setHomeOverByOverData(List<OverByOverData> homeOverByOverData) {
		this.homeOverByOverData = homeOverByOverData;
	}

	public List<OverByOverData> getAwayOverByOverData() {
		return awayOverByOverData;
	}

	public void setAwayOverByOverData(List<OverByOverData> awayOverByOverData) {
		this.awayOverByOverData = awayOverByOverData;
	}

	public VariousStats getHomeTeamScoreData() {
		return homeTeamScoreData;
	}

	public void setHomeTeamScoreData(VariousStats homeTeamScoreData) {
		this.homeTeamScoreData = homeTeamScoreData;
	}

	public VariousStats getAwayTeamScoreData() {
		return awayTeamScoreData;
	}

	public void setAwayTeamScoreData(VariousStats awayTeamScoreData) {
		this.awayTeamScoreData = awayTeamScoreData;
	}

	public VariousStats getLastOverData() {
		return lastOverData;
	}

	public void setLastOverData(VariousStats lastOverData) {
		this.lastOverData = lastOverData;
	}

	public VariousStats getOverData() {
		return overData;
	}

	public void setOverData(VariousStats overData) {
		this.overData = overData;
	}

	public VariousStats getBowlingCard() {
		return bowlingCard;
	}

	public void setBowlingCard(VariousStats bowlingCard) {
		this.bowlingCard = bowlingCard;
	}

	public int getBallsSinceLastBoundary() {
		return ballsSinceLastBoundary;
	}

	public void setBallsSinceLastBoundary(int ballsSinceLastBoundary) {
		this.ballsSinceLastBoundary = ballsSinceLastBoundary;
	}

	public VariousStats getInningCompare() {
		return inningCompare;
	}

	public void setInningCompare(VariousStats inningCompare) {
		this.inningCompare = inningCompare;
	}

	public VariousStats getLastThirtyBalls() {
		return lastThirtyBalls;
	}

	public void setLastThirtyBalls(VariousStats lastThirtyBalls) {
		this.lastThirtyBalls = lastThirtyBalls;
	}

@Override
	public String toString() {
		return "MatchStats [overData=" + overData + ", playerStats=" + playerStats + ", homeOverByOverData="
				+ homeOverByOverData + ", awayOverByOverData=" + awayOverByOverData + ", bowlingCard=" + bowlingCard
				+ ", ballsSinceLastBoundary=" + ballsSinceLastBoundary + ", lastOverData=" + lastOverData
				+ ", inningCompare=" + inningCompare + ", lastThirtyBalls=" + lastThirtyBalls + ", homeTeamScoreData="
				+ homeTeamScoreData + ", awayTeamScoreData=" + awayTeamScoreData + ", homeFirstPowerPlay="
				+ homeFirstPowerPlay + ", homeSecondPowerPlay=" + homeSecondPowerPlay + ", homeThirdPowerPlay="
				+ homeThirdPowerPlay + ", awayFirstPowerPlay=" + awayFirstPowerPlay + ", awaySecondPowerPlay="
				+ awaySecondPowerPlay + ", awayThirdPowerPlay=" + awayThirdPowerPlay + "]";
	}
	public static class VariousStats{
		
		private int totalRuns;
		private int totalBalls;
		private int id;
		private int totalWickets;
		private int totalDots;
		private int totalOnes;
		private int totalTwos;
		private int totalThrees;
		private int totalFours;
		private int totalFives;
		private int totalSixes;
		private int totalNines;
		private String statsType,name,BowlerName;
		private String thisOverTxt;
		private int lastBowlerId;
		private int replacementBowlerId;
		
		public VariousStats(int totalRuns, int totalWickets, int totalFours, int totalSixes, int totalNines) {
			super();
			this.totalRuns = totalRuns;
			this.totalWickets = totalWickets;
			this.totalFours = totalFours;
			this.totalSixes = totalSixes;
			this.totalNines = totalNines;
		}
		public VariousStats() {
			super();
			this.thisOverTxt = "";
		}
		public VariousStats(int id) {
			super();
			this.id = id;
		}
		public VariousStats(int id, String statsType) {
			super();
			this.id = id;
			this.statsType = statsType;
		}
		public String getStatsType() {
			return statsType;
		}
		public void setStatsType(String statsType) {
			this.statsType = statsType;
		}
		public int getTotalBalls() {
			return totalBalls;
		}
		public void setTotalBalls(int totalBalls) {
			this.totalBalls = totalBalls;
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
		public int getTotalOnes() {
			return totalOnes;
		}
		public void setTotalOnes(int totalOnes) {
			this.totalOnes = totalOnes;
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
		public int getTotalFours() {
			return totalFours;
		}
		public void setTotalFours(int totalFours) {
			this.totalFours = totalFours;
		}
		public int getTotalFives() {
			return totalFives;
		}
		public void setTotalFives(int totalFives) {
			this.totalFives = totalFives;
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
		public String getThisOverTxt() {
			return thisOverTxt;
		}
		public void setThisOverTxt(String thisOverTxt) {
			this.thisOverTxt = thisOverTxt;
		}
		public int getLastBowlerId() {
			return lastBowlerId;
		}
		public void setLastBowlerId(int lastBowlerId) {
			this.lastBowlerId = lastBowlerId;
		}
		public int getReplacementBowlerId() {
			return replacementBowlerId;
		}
		public void setReplacementBowlerId(int replacementBowlerId) {
			this.replacementBowlerId = replacementBowlerId;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getBowlerName() {
			return BowlerName;
		}
		public void setBowlerName(String bowlerName) {
			BowlerName = bowlerName;
		}
		@Override
		public String toString() {
			return "VariousStats [totalRuns=" + totalRuns + ", totalBalls=" + totalBalls + ", id=" + id
					+ ", totalWickets=" + totalWickets + ", totalDots=" + totalDots + ", totalOnes=" + totalOnes
					+ ", totalTwos=" + totalTwos + ", totalThrees=" + totalThrees + ", totalFours=" + totalFours
					+ ", totalFives=" + totalFives + ", totalSixes=" + totalSixes + ", totalNines=" + totalNines
					+ ", statsType=" + statsType + ", thisOverTxt=" + thisOverTxt + ", lastBowlerId=" + lastBowlerId
					+ ", replacementBowlerId=" + replacementBowlerId + "]";
		}

	}
}