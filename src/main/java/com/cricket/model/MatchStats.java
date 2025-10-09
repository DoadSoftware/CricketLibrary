package com.cricket.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchStats{
	
	private VariousStats overData;
	private List<VariousStats> playerStats; 
	private List<OverByOverData> homeOverByOverData; 
	private List<OverByOverData> awayOverByOverData; 
	private VariousStats bowlingCard; 
	private int ballsSinceLastBoundary,
		phase1StartOver , phase1EndOver, phase2StartOver , phase2EndOver,
		phase3StartOver , phase3EndOver; 
	private String timeLine ;
	List<VariousStats> powerPlay1ThisOver;
	List<VariousStats> powerPlay2ThisOver;
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
	
    private List<VariousStats> homeFirstPowerPlayBatsman;  
    private List<VariousStats> homeSecondPowerPlayBatsman; 
    private List<VariousStats> homeThirdPowerPlayBatsman;  
    
    private List<VariousStats> awayFirstPowerPlayBatsman;  
    private List<VariousStats> awaySecondPowerPlayBatsman; 
    private List<VariousStats> awayThirdPowerPlayBatsman;  
    
    private List<BatterOut> outBatsman;
    
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
		this.homeFirstPowerPlayBatsman = new ArrayList<>();
        this.homeSecondPowerPlayBatsman = new ArrayList<>();
        this.homeThirdPowerPlayBatsman = new ArrayList<>();
        this.awayFirstPowerPlayBatsman = new ArrayList<>();
        this.awaySecondPowerPlayBatsman = new ArrayList<>();
        this.awayThirdPowerPlayBatsman = new ArrayList<>();
        this.timeLine ="";
        this.outBatsman = new ArrayList<>();
        this.powerPlay1ThisOver = new ArrayList<>();
        this.powerPlay2ThisOver = new ArrayList<>();
	}
	
	public String getTimeLine() {
		return timeLine;
	}

	public void setTimeLine(String timeLine) {
		this.timeLine = timeLine;
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

	public List<VariousStats> getHomeFirstPowerPlayBatsman() {
		return homeFirstPowerPlayBatsman;
	}

	public void setHomeFirstPowerPlayBatsman(List<VariousStats> homeFirstPowerPlayBatsman) {
		this.homeFirstPowerPlayBatsman = homeFirstPowerPlayBatsman;
	}

	public List<VariousStats> getHomeSecondPowerPlayBatsman() {
		return homeSecondPowerPlayBatsman;
	}

	public void setHomeSecondPowerPlayBatsman(List<VariousStats> homeSecondPowerPlayBatsman) {
		this.homeSecondPowerPlayBatsman = homeSecondPowerPlayBatsman;
	}

	public List<VariousStats> getHomeThirdPowerPlayBatsman() {
		return homeThirdPowerPlayBatsman;
	}

	public void setHomeThirdPowerPlayBatsman(List<VariousStats> homeThirdPowerPlayBatsman) {
		this.homeThirdPowerPlayBatsman = homeThirdPowerPlayBatsman;
	}

	public List<VariousStats> getAwayFirstPowerPlayBatsman() {
		return awayFirstPowerPlayBatsman;
	}

	public void setAwayFirstPowerPlayBatsman(List<VariousStats> awayFirstPowerPlayBatsman) {
		this.awayFirstPowerPlayBatsman = awayFirstPowerPlayBatsman;
	}

	public List<VariousStats> getAwaySecondPowerPlayBatsman() {
		return awaySecondPowerPlayBatsman;
	}

	public void setAwaySecondPowerPlayBatsman(List<VariousStats> awaySecondPowerPlayBatsman) {
		this.awaySecondPowerPlayBatsman = awaySecondPowerPlayBatsman;
	}

	public List<VariousStats> getAwayThirdPowerPlayBatsman() {
		return awayThirdPowerPlayBatsman;
	}

	public void setAwayThirdPowerPlayBatsman(List<VariousStats> awayThirdPowerPlayBatsman) {
		this.awayThirdPowerPlayBatsman = awayThirdPowerPlayBatsman;
	}

	public int getPhase1StartOver() {
		return phase1StartOver;
	}

	public void setPhase1StartOver(int phase1StartOver) {
		this.phase1StartOver = phase1StartOver;
	}

	public int getPhase1EndOver() {
		return phase1EndOver;
	}

	public void setPhase1EndOver(int phase1EndOver) {
		this.phase1EndOver = phase1EndOver;
	}

	public int getPhase2StartOver() {
		return phase2StartOver;
	}

	public void setPhase2StartOver(int phase2StartOver) {
		this.phase2StartOver = phase2StartOver;
	}

	public int getPhase2EndOver() {
		return phase2EndOver;
	}

	public void setPhase2EndOver(int phase2EndOver) {
		this.phase2EndOver = phase2EndOver;
	}

	public int getPhase3StartOver() {
		return phase3StartOver;
	}

	public void setPhase3StartOver(int phase3StartOver) {
		this.phase3StartOver = phase3StartOver;
	}

	public int getPhase3EndOver() {
		return phase3EndOver;
	}

	public void setPhase3EndOver(int phase3EndOver) {
		this.phase3EndOver = phase3EndOver;
	}

	public List<BatterOut> getOutBatsman() {
		return outBatsman;
	}

	public void setOutBatsman(List<BatterOut> outBatsman) {
		this.outBatsman = outBatsman;
	}

	public List<VariousStats> getPowerPlay1ThisOver() {
		return powerPlay1ThisOver;
	}

	public void setPowerPlay1ThisOver(List<VariousStats> powerPlay1ThisOver) {
		this.powerPlay1ThisOver = powerPlay1ThisOver;
	}

	public List<VariousStats> getPowerPlay2ThisOver() {
		return powerPlay2ThisOver;
	}

	public void setPowerPlay2ThisOver(List<VariousStats> powerPlay2ThisOver) {
		this.powerPlay2ThisOver = powerPlay2ThisOver;
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
			+ awaySecondPowerPlay + ", awayThirdPowerPlay=" + awayThirdPowerPlay + ", homeFirstPowerPlayBatsman="
			+ homeFirstPowerPlayBatsman + ", homeSecondPowerPlayBatsman=" + homeSecondPowerPlayBatsman
			+ ", homeThirdPowerPlayBatsman=" + homeThirdPowerPlayBatsman + ", awayFirstPowerPlayBatsman="
			+ awayFirstPowerPlayBatsman + ", awaySecondPowerPlayBatsman=" + awaySecondPowerPlayBatsman
			+ ", awayThirdPowerPlayBatsman=" + awayThirdPowerPlayBatsman + "]";
}	
	public static class BatterOut{
		private int Batter_id;
		private int over ;
		private int ball;
		private int inning;
		public int getBatter_id() {
			return Batter_id;
		}
		public void setBatter_id(int batter_id) {
			Batter_id = batter_id;
		}
		public int getOver() {
			return over;
		}
		public void setOver(int over) {
			this.over = over;
		}
		public int getBall() {
			return ball;
		}
		public void setBall(int ball) {
			this.ball = ball;
		}
		public int getInning() {
			return inning;
		}
		public void setInning(int inning) {
			this.inning = inning;
		}
		@Override
		public String toString() {
			return "BatterOut [Batter_id=" + Batter_id + ", over=" + over + ", ball=" + ball + ", inning=" + inning
					+ "]";
		}
		public BatterOut(int batter_id, int over, int ball, int inning) {
			super();
			Batter_id = batter_id;
			this.over = over;
			this.ball = ball;
			this.inning = inning;
		}
		public BatterOut() {
			super();
			// TODO Auto-generated constructor stub
		}
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
		private int over;
		private String statsType,name,BowlerName,outnotOut;
		private String thisOverTxt,outBatsman,notWicketCount;
		private int consecutiveW=0;
		private int lastBowlerId;
		private int replacementBowlerId;
		
		public VariousStats(int totalRuns, int totalWickets, int totalFours, int totalSixes, int totalNines,String OutBatsman,String notWicketCount) {
			
			super();
			this.totalRuns = totalRuns;
			this.totalWickets = totalWickets;
			this.totalFours = totalFours;
			this.totalSixes = totalSixes;
			this.totalNines = totalNines;
			this.outBatsman = OutBatsman;
			this.notWicketCount = notWicketCount;
		}
		public VariousStats() {
			super();
			this.thisOverTxt = "";
			this.outBatsman = "";
			this.notWicketCount = "";
		}
		
		public int getConsecutiveW() {
			return consecutiveW;
		}
		public void setConsecutiveW(int consecutiveW) {
			this.consecutiveW = consecutiveW;
		}
		public String getNotWicketCount() {
			return notWicketCount;
		}
		public void setNotWicketCount(String notWicketCount) {
			this.notWicketCount = notWicketCount;
		}
		public VariousStats(int id) {
			super();
			this.id = id;
		}
		
		public String getOutBatsman() {
			return outBatsman;
		}
		public void setOutBatsman(String outBatsman) {
			this.outBatsman = outBatsman;
		}
		public String getOutnotOut() {
			return outnotOut;
		}
		public void setOutnotOut(String outnotOut) {
			this.outnotOut = outnotOut;
		}
		public VariousStats(int id, String statsType) {
			super();
			this.id = id;
			this.statsType = statsType;
			this.thisOverTxt = "";
		}
		
		public VariousStats(int over, String thisOverTxt ,int totalRuns, int totalWickets) {
			super();
			this.over = over;
			this.thisOverTxt = thisOverTxt;
			this.totalRuns = totalRuns;
			this.totalWickets = totalWickets;
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
		
		public int getOver() {
			return over;
		}
		public void setOver(int over) {
			this.over = over;
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
					+ ", over=" + over + ", statsType=" + statsType + ", name=" + name + ", BowlerName=" + BowlerName
					+ ", outnotOut=" + outnotOut + ", thisOverTxt=" + thisOverTxt + ", outBatsman=" + outBatsman
					+ ", notWicketCount=" + notWicketCount + ", consecutiveW=" + consecutiveW + ", lastBowlerId="
					+ lastBowlerId + ", replacementBowlerId=" + replacementBowlerId + "]";
		}

	}
	}
