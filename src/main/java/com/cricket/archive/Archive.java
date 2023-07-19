package com.cricket.archive;

import java.util.List;

import com.cricket.model.MatchAllData;

public class Archive{

  private List<ArchiveData> seasons;
  private List<ArchiveData> series;
  private List<ArchiveData> matches;
  private MatchAllData matchAllData;
  
  public MatchAllData getMatchAllData() {
	return matchAllData;
}
public void setMatchAllData(MatchAllData matchAllData) {
	this.matchAllData = matchAllData;
}
public List<ArchiveData> getMatches() {
	return matches;
}
public void setMatches(List<ArchiveData> matches) {
	this.matches = matches;
}
public List<ArchiveData> getSeasons() {
	return seasons;
}
public void setSeasons(List<ArchiveData> seasons) {
	this.seasons = seasons;
}
public List<ArchiveData> getSeries() {
	return series;
}
public void setSeries(List<ArchiveData> series) {
	this.series = series;
}
	  
}