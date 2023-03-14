package com.cricket.model.json;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "EventFile")
public class EventFileJson {

  @JsonProperty(value = "event", namespace = "events")
  private ArrayList<EventJson> events;

public ArrayList<EventJson> getEvents() {
	return events;
}

public void setEvents(ArrayList<EventJson> events) {
	this.events = events;
}

}
