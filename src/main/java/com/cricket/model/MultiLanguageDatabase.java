package com.cricket.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MultiLanguageDatabase {
	
	public List<Dictionary> dictionary;
	public List<Venue> venue;
	public List<Team> team;
	public List<Player> players;
	
	public List<Dictionary> getDictionary() {
		return dictionary;
	}
	public void setDictionary(List<Dictionary> dictionary) {
		this.dictionary = dictionary;
	}
	public List<Venue> getVenue() {
		return venue;
	}
	public void setVenue(List<Venue> venue) {
		this.venue = venue;
	}
	public List<Team> getTeam() {
		return team;
	}
	public void setTeam(List<Team> team) {
		this.team = team;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}
