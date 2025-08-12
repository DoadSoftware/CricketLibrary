package com.cricket.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadToHead implements Cloneable {

  private List<HeadToHeadTeam> H2hTeam =  new ArrayList<HeadToHeadTeam>();
  private List<HeadToHeadPlayer> H2hPlayer = new ArrayList<HeadToHeadPlayer>();
  
  
public List<HeadToHeadTeam> getH2hTeam() {
	return H2hTeam;
}

public void setH2hTeam(List<HeadToHeadTeam> h2hTeam) {
	H2hTeam = h2hTeam;
}

public List<HeadToHeadPlayer> getH2hPlayer() {
	return H2hPlayer;
}

public void setH2hPlayer(List<HeadToHeadPlayer> h2hPlayer) {
	H2hPlayer = h2hPlayer;
}

@Override
public HeadToHead clone() throws CloneNotSupportedException {
    HeadToHead clone = null;
    try
    {
        clone = (HeadToHead) super.clone();
    } 
    catch (CloneNotSupportedException e) 
    {
        throw new RuntimeException(e);
    }
    return clone;
}

@Override
public String toString() {
	return "HeadToHead [H2hTeam=" + H2hTeam + ", H2hPlayer=" + H2hPlayer + "]";
}

}