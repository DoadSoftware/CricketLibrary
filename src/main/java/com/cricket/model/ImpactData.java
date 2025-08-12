package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImpactData {
	int inPlayerId;
	int outPlayerId;
	int teamId;
	
	public int getInPlayerId() {
		return inPlayerId;
	}
	public void setInPlayerId(int inPlayerId) {
		this.inPlayerId = inPlayerId;
	}
	public int getOutPlayerId() {
		return outPlayerId;
	}
	public void setOutPlayerId(int outPlayerId) {
		this.outPlayerId = outPlayerId;
	}
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
	
	
}
