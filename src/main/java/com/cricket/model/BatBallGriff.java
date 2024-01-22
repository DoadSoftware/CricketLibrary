package com.cricket.model;

public class BatBallGriff {

	private int playerId;
  
	private int Runs;
	
	private int ballsFaced;
	
	private String status;
	
	private int RunsConceded;

	private int wickets;
  
	private String oversBowled;

	private String opponentTeam;
	
	private Player player;
	
	public BatBallGriff() {
		super();
	}

	public BatBallGriff(int playerId, int runs, int ballsFaced, String status, int runsConceded, int wickets,
			String oversBowled, String opponentTeam, Player player) {
		super();
		this.playerId = playerId;
		Runs = runs;
		this.ballsFaced = ballsFaced;
		this.status = status;
		RunsConceded = runsConceded;
		this.wickets = wickets;
		this.oversBowled = oversBowled;
		this.opponentTeam = opponentTeam;
		this.player = player;
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
		Runs = runs;
	}

	public int getRunsConceded() {
		return RunsConceded;
	}

	public void setRunsConceded(int runsConceded) {
		RunsConceded = runsConceded;
	}

	public int getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(int ballsFaced) {
		this.ballsFaced = ballsFaced;
	}

	public int getWickets() {
		return wickets;
	}

	public void setWickets(int wickets) {
		this.wickets = wickets;
	}

	public String getOversBowled() {
		return oversBowled;
	}

	public void setOversBowled(String oversBowled) {
		this.oversBowled = oversBowled;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpponentTeam() {
		return opponentTeam;
	}

	public void setOpponentTeam(String opponentTeam) {
		this.opponentTeam = opponentTeam;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	@Override
	public String toString() {
		return "BatBallGriff [playerId=" + playerId + ", Runs=" + Runs + ", RunsConceded=" + RunsConceded
				+ ", ballsFaced=" + ballsFaced + ", wickets=" + wickets + ", oversBowled=" + oversBowled + ", status="
				+ status + ", opponentTeam=" + opponentTeam + ", player=" + player + "]";
	}
}
