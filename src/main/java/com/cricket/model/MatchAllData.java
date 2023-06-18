package com.cricket.model;

public class MatchAllData {

  private Match match;
  private Setup setup;
  private EventFile eventFile;
  
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
	return "MatchAllData [match=" + match + ", setup=" + setup + ", eventFile=" + eventFile + "]";
}
  
}