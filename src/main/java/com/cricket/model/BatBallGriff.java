package com.cricket.model;

public class BatBallGriff {

	private int playerId;
  
	private int Runs;
	
	private int ballsFaced;
	
	private String status;
	
	private String how_out;
	
	private int RunsConceded;

	private int wickets;
  
	private String oversBowled;

	private String opponentTeam;
	
	private Player player;
	
	public BatBallGriff() {
		super();
	}

	public BatBallGriff(int playerId, int runs, int ballsFaced, String status, String how_out, int runsConceded,
			int wickets, String oversBowled, String opponentTeam, Player player) {
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

	public int getBallsFaced() {
		return ballsFaced;
	}

	public void setBallsFaced(int ballsFaced) {
		this.ballsFaced = ballsFaced;
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
		this.wickets = wickets;
	}

	public String getOversBowled() {
		return oversBowled;
	}

	public void setOversBowled(String oversBowled) {
		this.oversBowled = oversBowled;
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
		return "BatBallGriff [playerId=" + playerId + ", Runs=" + Runs + ", ballsFaced=" + ballsFaced + ", status="
				+ status + ", how_out=" + how_out + ", RunsConceded=" + RunsConceded + ", wickets=" + wickets
				+ ", oversBowled=" + oversBowled + ", opponentTeam=" + opponentTeam + ", player=" + player + "]";
	}
}
