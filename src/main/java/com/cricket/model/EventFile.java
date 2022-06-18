package com.cricket.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="EventFile")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventFile {

  @XmlElementWrapper(name = "Events")
  @XmlElement(name = "event")
  private ArrayList<Event> events;

public ArrayList<Event> getEvents() {
	return events;
}

public void setEvents(ArrayList<Event> events) {
	this.events = events;
}

}
