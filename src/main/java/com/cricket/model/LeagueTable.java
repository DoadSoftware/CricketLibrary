package com.cricket.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeagueTable {
	
    private List<LeagueTeam> LeagueTeams;

	public List<LeagueTeam> getLeagueTeams() {
		return LeagueTeams;
	}

	public void setLeagueTeams(List<LeagueTeam> leagueTeams) {
		LeagueTeams = leagueTeams;
	}

}

