package com.cricket.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventFile {

  private String status;
  private ArrayList<Event> events;

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}

public ArrayList<Event> getEvents() {
	return events;
}

public void setEvents(ArrayList<Event> events) {
	this.events = events;
}

}
