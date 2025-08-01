package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Gfx {

    @JsonProperty("CompetitionDetails")
    private List<CompetitionDetail> competitionDetails;

    @JsonProperty("Batting")
    private List<Batting> batting;
    
    @JsonProperty("Bowling")
    private List<Batting> bowling;


    public List<Batting> getBowling() {
		return bowling;
	}

	public void setBowling(List<Batting> bowling) {
		this.bowling = bowling;
	}

	public List<CompetitionDetail> getCompetitionDetails() {
        return competitionDetails;
    }

    public void setCompetitionDetails(List<CompetitionDetail> competitionDetails) {
        this.competitionDetails = competitionDetails;
    }

    public List<Batting> getBatting() {
        return batting;
    }

    public void setBatting(List<Batting> batting) {
        this.batting = batting;
    }

    
    @Override
	public String toString() {
		return "Gfx [competitionDetails=" + competitionDetails + ", batting=" + batting + "]";
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompetitionDetail {
        @JsonProperty("CompetitionID")
        private Long competitionID;

        @JsonProperty("CompetitionName")
        private String competitionName;

        @JsonProperty("Format")
        private String format;

        @JsonProperty("TournamentID")
        private Long tournamentID;

        @JsonProperty("TournamentName")
        private String tournamentName;

        @JsonProperty("Teams")
        private List<Team> teams;

		public Long getCompetitionID() {
			return competitionID;
		}

		public void setCompetitionID(Long competitionID) {
			this.competitionID = competitionID;
		}

		public String getCompetitionName() {
			return competitionName;
		}

		public void setCompetitionName(String competitionName) {
			this.competitionName = competitionName;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}

		public Long getTournamentID() {
			return tournamentID;
		}

		public void setTournamentID(Long tournamentID) {
			this.tournamentID = tournamentID;
		}

		public String getTournamentName() {
			return tournamentName;
		}

		public void setTournamentName(String tournamentName) {
			this.tournamentName = tournamentName;
		}

		public List<Team> getTeams() {
			return teams;
		}

		public void setTeams(List<Team> teams) {
			this.teams = teams;
		}

		@Override
		public String toString() {
			return "CompetitionDetail [competitionID=" + competitionID + ", competitionName=" + competitionName
					+ ", format=" + format + ", tournamentID=" + tournamentID + ", tournamentName=" + tournamentName
					+ ", teams=" + teams + "]";
		}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Team {
        @JsonProperty("TeamName")
        private String teamName;

        @JsonProperty("TeamID")
        private Long teamID;

        @JsonProperty("Squad")
        private List<Player> squad;

		public String getTeamName() {
			return teamName;
		}

		public void setTeamName(String teamName) {
			this.teamName = teamName;
		}

		public Long getTeamID() {
			return teamID;
		}

		public void setTeamID(Long teamID) {
			this.teamID = teamID;
		}

		public List<Player> getSquad() {
			return squad;
		}

		public void setSquad(List<Player> squad) {
			this.squad = squad;
		}

		@Override
		public String toString() {
			return "Team [teamName=" + teamName + ", teamID=" + teamID + ", squad=" + squad + "]";
		}

       
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Player {
        @JsonProperty("PlayerID")
        private Long playerID;

        @JsonProperty("Player")
        private String player;

		public Long getPlayerID() {
			return playerID;
		}

		public void setPlayerID(Long playerID) {
			this.playerID = playerID;
		}

		public String getPlayer() {
			return player;
		}

		public void setPlayer(String player) {
			this.player = player;
		}

		@Override
		public String toString() {
			return "Player [playerID=" + playerID + ", player=" + player + "]";
		}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Batting {
        @JsonProperty("Format")
        private List<BattingStatsCategory> format;

        @JsonProperty("Trophy")
        private List<BattingStatsCategory> trophy;

        @JsonProperty("Series")
        private List<BattingStatsCategory> series;

        @JsonProperty("Position")
        private List<BattingStatsCategory> position;
        
        @JsonProperty("Innings")
        private List<BattingStatsCategory> innings;
        
        @JsonProperty("Venue")
        private List<BattingStatsCategory> venue;
        
        @JsonProperty("Captain")
        private List<BattingStatsCategory> Captain;
        
        @JsonProperty("WicketKeeper")
        private List<BattingStatsCategory> wicketKeeper;
        
        @JsonProperty("Opponent")
        private List<BattingStatsCategory> opponent;

		@Override
		public String toString() {
			return "Batting [format=" + format + ", trophy=" + trophy + ", series=" + series + ", position=" + position
					+ "]";
		}

		public List<BattingStatsCategory> getFormat() {
			return format;
		}

		public void setFormat(List<BattingStatsCategory> format) {
			this.format = format;
		}

		public List<BattingStatsCategory> getTrophy() {
			return trophy;
		}

		public void setTrophy(List<BattingStatsCategory> trophy) {
			this.trophy = trophy;
		}

		public List<BattingStatsCategory> getSeries() {
			return series;
		}

		public void setSeries(List<BattingStatsCategory> series) {
			this.series = series;
		}

		public List<BattingStatsCategory> getPosition() {
			return position;
		}

		public void setPosition(List<BattingStatsCategory> position) {
			this.position = position;
		}

		public List<BattingStatsCategory> getInnings() {
			return innings;
		}

		public void setInnings(List<BattingStatsCategory> innings) {
			this.innings = innings;
		}

		public List<BattingStatsCategory> getVenue() {
			return venue;
		}

		public void setVenue(List<BattingStatsCategory> venue) {
			this.venue = venue;
		}

		public List<BattingStatsCategory> getCaptain() {
			return Captain;
		}

		public void setCaptain(List<BattingStatsCategory> captain) {
			Captain = captain;
		}

		public List<BattingStatsCategory> getWicketKeeper() {
			return wicketKeeper;
		}

		public void setWicketKeeper(List<BattingStatsCategory> wicketKeeper) {
			this.wicketKeeper = wicketKeeper;
		}

		public List<BattingStatsCategory> getOpponent() {
			return opponent;
		}

		public void setOpponent(List<BattingStatsCategory> opponent) {
			this.opponent = opponent;
		}
    }
   
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BattingStatsCategory {
        @JsonProperty("Header")
        private String header;

        @JsonProperty("Stats")
        private List<PlayerStat> stats;

		public String getHeader() {
			return header;
		}

		public void setHeader(String header) {
			this.header = header;
		}

		public List<PlayerStat> getStats() {
			return stats;
		}

		public void setStats(List<PlayerStat> stats) {
			this.stats = stats;
		}

		@Override
		public String toString() {
			return "BattingStatsCategory [header=" + header + ", stats=" + stats + "]";
		}
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PlayerStat {
        @JsonProperty("PlayerID")
        private Long playerID;

        @JsonProperty("Player")
        private String player;

        @JsonProperty("Matches")
        private Integer matches;

        @JsonProperty("Inns")
        private Integer inns;

        @JsonProperty("Runs")
        private Integer runs;

        @JsonProperty("Balls")
        private Integer balls;

        @JsonProperty("_50s")
        private Integer fifties;

        @JsonProperty("_100s")
        private Integer hundreds;

        @JsonProperty("_30Plus")
        private Integer thirtyPlus;

        @JsonProperty("Fours")
        private Integer fours;

        @JsonProperty("Sixes")
        private Integer sixes;

        @JsonProperty("NO")
        private Integer notOut;

        @JsonProperty("SR")
        private Double strikeRate;

        @JsonProperty("Avg")
        private Double average;

        @JsonProperty("HS")
        private String highScore;

        @JsonProperty("HS_OppTeamID")
        private Long hsOppTeamID;

        @JsonProperty("HSAgainst")
        private String hsAgainst;

        @JsonProperty("HS_VenueID")
        private Long hsVenueID;

        @JsonProperty("HSAt")
        private String hsAt;

        @JsonProperty("Wkts")
        private Integer wkts;
        
        @JsonProperty("_3plusWkts")
        private Integer plus3Wkts;
        
        @JsonProperty("_5plusWkts")
        private Integer plus5Wkts;
        
        @JsonProperty("BestBowling")
        private String bestBowling;

        @JsonProperty("Best_OppID")
        private String bestAgainstId;

        @JsonProperty("Best_VenueID")
        private Long venueID;

        @JsonProperty("BestAgainst")
        private String bestAgainst;
        
        @JsonProperty("BestAt")
        private String bestAt;

		public Long getPlayerID() {
			return playerID;
		}

		public void setPlayerID(Long playerID) {
			this.playerID = playerID;
		}

		public String getPlayer() {
			return player;
		}

		public void setPlayer(String player) {
			this.player = player;
		}

		public Integer getMatches() {
			return matches;
		}

		public void setMatches(Integer matches) {
			this.matches = matches;
		}

		public Integer getInns() {
			return inns;
		}

		public void setInns(Integer inns) {
			this.inns = inns;
		}

		public Integer getRuns() {
			return runs;
		}

		public void setRuns(Integer runs) {
			this.runs = runs;
		}

		public Integer getBalls() {
			return balls;
		}

		public void setBalls(Integer balls) {
			this.balls = balls;
		}

		public Integer getFifties() {
			return fifties;
		}

		public void setFifties(Integer fifties) {
			this.fifties = fifties;
		}

		public Integer getHundreds() {
			return hundreds;
		}

		public void setHundreds(Integer hundreds) {
			this.hundreds = hundreds;
		}

		public Integer getThirtyPlus() {
			return thirtyPlus;
		}

		public void setThirtyPlus(Integer thirtyPlus) {
			this.thirtyPlus = thirtyPlus;
		}

		public Integer getFours() {
			return fours;
		}

		public void setFours(Integer fours) {
			this.fours = fours;
		}

		public Integer getSixes() {
			return sixes;
		}

		public void setSixes(Integer sixes) {
			this.sixes = sixes;
		}

		public Integer getNotOut() {
			return notOut;
		}

		public void setNotOut(Integer notOut) {
			this.notOut = notOut;
		}

		public Double getStrikeRate() {
			return strikeRate;
		}

		public void setStrikeRate(Double strikeRate) {
			this.strikeRate = strikeRate;
		}

		public Double getAverage() {
			return average;
		}

		public void setAverage(Double average) {
			this.average = average;
		}

		public String getHighScore() {
			return highScore;
		}

		public void setHighScore(String highScore) {
			this.highScore = highScore;
		}

		public Long getHsOppTeamID() {
			return hsOppTeamID;
		}

		public void setHsOppTeamID(Long hsOppTeamID) {
			this.hsOppTeamID = hsOppTeamID;
		}

		public String getHsAgainst() {
			return hsAgainst;
		}

		public void setHsAgainst(String hsAgainst) {
			this.hsAgainst = hsAgainst;
		}

		public Long getHsVenueID() {
			return hsVenueID;
		}

		public void setHsVenueID(Long hsVenueID) {
			this.hsVenueID = hsVenueID;
		}

		public String getHsAt() {
			return hsAt;
		}

		public void setHsAt(String hsAt) {
			this.hsAt = hsAt;
		}

		public Integer getWkts() {
			return wkts;
		}

		public void setWkts(Integer wkts) {
			this.wkts = wkts;
		}

		public Integer getPlus3Wkts() {
			return plus3Wkts;
		}

		public void setPlus3Wkts(Integer plus3Wkts) {
			this.plus3Wkts = plus3Wkts;
		}

		public Integer getPlus5Wkts() {
			return plus5Wkts;
		}

		public void setPlus5Wkts(Integer plus5Wkts) {
			this.plus5Wkts = plus5Wkts;
		}

		public String getBestBowling() {
			return bestBowling;
		}

		public void setBestBowling(String bestBowling) {
			this.bestBowling = bestBowling;
		}

		public String getBestAgainstId() {
			return bestAgainstId;
		}

		public void setBestAgainstId(String bestAgainstId) {
			this.bestAgainstId = bestAgainstId;
		}

		public Long getVenueID() {
			return venueID;
		}

		public void setVenueID(Long venueID) {
			this.venueID = venueID;
		}

		public String getBestAgainst() {
			return bestAgainst;
		}

		public void setBestAgainst(String bestAgainst) {
			this.bestAgainst = bestAgainst;
		}

		public String getBestAt() {
			return bestAt;
		}

		public void setBestAt(String bestAt) {
			this.bestAt = bestAt;
		}

		@Override
		public String toString() {
			return "PlayerStat [playerID=" + playerID + ", player=" + player + ", matches=" + matches + ", inns=" + inns
					+ ", runs=" + runs + ", balls=" + balls + ", fifties=" + fifties + ", hundreds=" + hundreds
					+ ", thirtyPlus=" + thirtyPlus + ", fours=" + fours + ", sixes=" + sixes + ", notOut=" + notOut
					+ ", strikeRate=" + strikeRate + ", average=" + average + ", highScore=" + highScore
					+ ", hsOppTeamID=" + hsOppTeamID + ", hsAgainst=" + hsAgainst + ", hsVenueID=" + hsVenueID
					+ ", hsAt=" + hsAt + "]";
		}
    }
}

