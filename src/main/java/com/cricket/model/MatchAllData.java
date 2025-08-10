package com.cricket.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchAllData {

  private String timeStats;
  private Match match;
  private Setup setup;
  private EventFile eventFile;
  private List<String> eventsDataList;
  
public List<String> getEventsDataList() {
	return eventsDataList;
}
public void setEventsDataList(List<String> eventsDataList) {
	this.eventsDataList = eventsDataList;
}
public String getTimeStats() {
	return timeStats;
}
public void setTimeStats(String timeStats) {
	this.timeStats = timeStats;
}
public Match getMatch() {
	return match;
}
public void setMatch(Match match) {
	this.match = match;
}
public Setup getSetup() {
	return setup;
}
public void setSetup(Setup setup) {
	this.setup = setup;
}
public EventFile getEventFile() {
	return eventFile;
}
public void setEventFile(EventFile eventFile) {
	this.eventFile = eventFile;
}
@Override
public String toString() {
	return "MatchAllData [timeStats=" + timeStats + ", match=" + match + ", setup=" + setup + ", eventFile=" + eventFile
			+ "]";
}

}