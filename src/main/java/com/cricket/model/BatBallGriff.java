package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BatBallGriff {

	private int playerId;
  
	private int Runs;
	
	private int ballsFaced;
	
	private String status;
	
	private String how_out;
	
	private int RunsConceded;

	private int wickets;
  
	private String oversBowled;

	private Team opponentTeam;
	
	private Player player;
	
	private String MatchNumber;
	
	private String MatchIdent;
	
	public BatBallGriff() {
		super();
	}
	

	public BatBallGriff(int runs, int ballsFaced, String status, int runsConceded, int wickets, String oversBowled,
			Team opponentTeam, String matchNumber) {
		super();
		this.Runs = runs;
		this.ballsFaced = ballsFaced;
		this.status = status;
		this.RunsConceded = runsConceded;
		this.wickets = wickets;
		this.oversBowled = oversBowled;
		this.opponentTeam = opponentTeam;
		this.MatchNumber = matchNumber;
	}


	public BatBallGriff(int playerId, int runs, int ballsFaced, String status, String how_out, int runsConceded,
			int wickets, String oversBowled, Team opponentTeam, Player player, String matchNumber,String matchIdent) {
		super();
		this.playerId = playerId;
		Runs = runs;
		this.ballsFaced = ballsFaced;
		this.status = status;
		this.how_out = how_out;
		RunsConceded = runsConceded;
		this.wickets = wickets;
		this.oversBowled = oversBowled;
		this.opponentTeam = opponentTeam;
		this.player = player;
		MatchNumber = matchNumber;
		MatchIdent = matchIdent;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getRuns() {
		return Runs;
	}

	public void setRuns(int runs) {
		if(Runs < 0) {
			Runs = 0;
		}
		else {
			Runs = runs;
		}
	}

	public int getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(int ballsFaced) {
		if(ballsFaced < 0) {
			ballsFaced = 0;
		}
		else {
			this.ballsFaced = ballsFaced;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getHow_out() {
		return how_out;
	}

	public void setHow_out(String how_out) {
		this.how_out = how_out;
	}

	public int getRunsConceded() {
		return RunsConceded;
	}

	public void setRunsConceded(int runsConceded) {
		RunsConceded = runsConceded;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		if(wickets < 0) {
			wickets = 0;
		}
		else {
			this.wickets = wickets;
		}
		
	}

	public String getOversBowled() {
		return oversBowled;
	}

	public void setOversBowled(String oversBowled) {
		this.oversBowled = oversBowled;
	}
	


	public Team getOpponentTeam() {
		return opponentTeam;
	}


	public void setOpponentTeam(Team opponentTeam) {
		this.opponentTeam = opponentTeam;
	}


	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public String getMatchNumber() {
		return MatchNumber;
	}

	public void setMatchNumber(String matchNumber) {
		MatchNumber = matchNumber;
	}

	public String getMatchIdent() {
		return MatchIdent;
	}

	public void setMatchIdent(String matchIdent) {
		MatchIdent = matchIdent;
	}


	@Override
	public String toString() {
		return "BatBallGriff [playerId=" + playerId + ", Runs=" + Runs + ", ballsFaced=" + ballsFaced + ", status="
				+ status + ", how_out=" + how_out + ", RunsConceded=" + RunsConceded + ", wickets=" + wickets
				+ ", oversBowled=" + oversBowled + ", opponentTeam=" + opponentTeam + ", player=" + player
				+ ", MatchNumber=" + MatchNumber + ", MatchIdent=" + MatchIdent + "]";
	}
}
