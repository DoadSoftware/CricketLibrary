package com.cricket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Comparable<Event> {

  @XmlElement(name = "eventNumber")
  private int eventNumber;

  @XmlElement(name = "eventStatNumber")
  private int eventStatNumber;
  
  @XmlElement(name = "eventWasABoundary")
  private String eventWasABoundary;
  
  @XmlElement(name = "eventType")
  private String eventType;

  @XmlElement(name = "eventInningNumber")
  private int eventInningNumber;
  
  @XmlElement(name = "eventBatterNo")
  private int eventBatterNo;

  @XmlElement(name = "eventConcussionReplacePlayerId")
  private int eventConcussionReplacePlayerId;
  
  @XmlElement(name = "eventOnStrike")
  private String eventOnStrike;

  @XmlElement(name = "eventBatterPosition")
  private int eventBatterPosition;

  @XmlElement(name = "eventBatterPreviousPosition")
  private int eventBatterPreviousPosition;
  
  @XmlElement(name = "eventOtherBatterNo")
  private int eventOtherBatterNo;
  
  @XmlElement(name = "eventBowlerNo")
  private int eventBowlerNo;

  @XmlElement(name = "eventBowlingEnd")
  private int eventBowlingEnd;
  
  @XmlElement(name = "eventOtherBowlerNo")
  private int eventOtherBowlerNo;

  @XmlElement(name = "eventRuns")
  private int eventRuns;

  @XmlElement(name = "EventWickets")
  private int eventWickets;

  @XmlElement(name = "eventFours")
  private int eventFours;

  @XmlElement(name = "eventSixes")
  private int eventSixes;
  
  @XmlElement(name = "eventOverNo")
  private int eventOverNo;
  
  @XmlElement(name = "eventBallNo")
  private int eventBallNo;

  @XmlElement(name = "eventExtra")
  private String eventExtra;
  
  @XmlElement(name = "eventExtraRuns")
  private int eventExtraRuns;

  @XmlElement(name = "eventSubExtra")
  private String eventSubExtra;
  
  @XmlElement(name = "eventSubExtraRuns")
  private int eventSubExtraRuns;
  
  @XmlElement(name = "eventHowOut")
  private String eventHowOut;

  @XmlElement(name = "eventHowOutBatterNo")
  private int eventHowOutBatterNo;
  
  @XmlElement(name = "eventHowOutFielderId")
  private int eventHowOutFielderId;

  @XmlElement(name = "eventTotalRunsInAnOver")
  private int eventTotalRunsInAnOver;

  @XmlElement(name = "doNotIncrementBall")
  private String doNotIncrementBall;

  @XmlElement(name = "eventDescription")
  private String eventDescription;

  @XmlElement(name = "substitutionMade")
  private String substitutionMade;

  @XmlElement(name = "eventSpeed")
  private String eventSpeed;

  @XmlElement(name = "EventBattingCard")
  private BattingCard EventBattingCard;

  @XmlElement(name = "EventBowlingCard")
  private BowlingCard EventBowlingCard;

  @XmlElement(name = "EventPartnership")
  private Partnership EventPartnership;

public Event() {
	super();
}

public Event(int eventNumber, int eventStatNumber, String eventType, String eventDescription, int eventInningNumber, int eventBatterNo,
		int eventBowlerNo, int eventRuns, int eventOverNo, int eventBallNo) {
	super();
	this.eventNumber = eventNumber;
	this.eventStatNumber = eventStatNumber;
	this.eventType = eventType;
	this.eventDescription = eventDescription;
	this.eventInningNumber = eventInningNumber;
	this.eventBatterNo = eventBatterNo;
	this.eventBowlerNo = eventBowlerNo;
	this.eventRuns = eventRuns;
	this.eventOverNo = eventOverNo;
	this.eventBallNo = eventBallNo;
}

public int getEventWickets() {
	return eventWickets;
}

public void setEventWickets(int eventWickets) {
	this.eventWickets = eventWickets;
}

public int getEventFours() {
	return eventFours;
}

public void setEventFours(int eventFours) {
	this.eventFours = eventFours;
}

public int getEventSixes() {
	return eventSixes;
}

public void setEventSixes(int eventSixes) {
	this.eventSixes = eventSixes;
}

public BattingCard getEventBattingCard() {
	return EventBattingCard;
}

public void setEventBattingCard(BattingCard eventBattingCard) {
	EventBattingCard = eventBattingCard;
}

public BowlingCard getEventBowlingCard() {
	return EventBowlingCard;
}

public void setEventBowlingCard(BowlingCard eventBowlingCard) {
	EventBowlingCard = eventBowlingCard;
}

public Partnership getEventPartnership() {
	return EventPartnership;
}

public void setEventPartnership(Partnership eventPartnership) {
	EventPartnership = eventPartnership;
}

public String getEventSpeed() {
	return eventSpeed;
}

public void setEventSpeed(String eventSpeed) {
	this.eventSpeed = eventSpeed;
}

public String getSubstitutionMade() {
	return substitutionMade;
}

public void setSubstitutionMade(String substitutionMade) {
	this.substitutionMade = substitutionMade;
}

public String getDoNotIncrementBall() {
	return doNotIncrementBall;
}

public void setDoNotIncrementBall(String doNotIncrementBall) {
	this.doNotIncrementBall = doNotIncrementBall;
}

public String getEventDescription() {
	return eventDescription;
}

public void setEventDescription(String eventDescription) {
	this.eventDescription = eventDescription;
}

public int getEventTotalRunsInAnOver() {
	return eventTotalRunsInAnOver;
}

public void setEventTotalRunsInAnOver(int eventTotalRunsInAnOver) {
	this.eventTotalRunsInAnOver = eventTotalRunsInAnOver;
}

public int getEventStatNumber() {
	return eventStatNumber;
}

public void setEventStatNumber(int eventStatNumber) {
	this.eventStatNumber = eventStatNumber;
}

public int getEventConcussionReplacePlayerId() {
	return eventConcussionReplacePlayerId;
}

public void setEventConcussionReplacePlayerId(int eventConcussionReplacePlayerId) {
	this.eventConcussionReplacePlayerId = eventConcussionReplacePlayerId;
}

public int getEventBowlingEnd() {
	return eventBowlingEnd;
}

public void setEventBowlingEnd(int eventBowlingEnd) {
	this.eventBowlingEnd = eventBowlingEnd;
}

public String getEventSubExtra() {
	return eventSubExtra;
}

public void setEventSubExtra(String eventSubExtra) {
	this.eventSubExtra = eventSubExtra;
}

public int getEventSubExtraRuns() {
	return eventSubExtraRuns;
}

public void setEventSubExtraRuns(int eventSubExtraRuns) {
	this.eventSubExtraRuns = eventSubExtraRuns;
}

public int getEventHowOutBatterNo() {
	return eventHowOutBatterNo;
}

public void setEventHowOutBatterNo(int eventHowOutBatterNo) {
	this.eventHowOutBatterNo = eventHowOutBatterNo;
}

public String getEventOnStrike() {
	return eventOnStrike;
}

public void setEventOnStrike(String eventOnStrike) {
	this.eventOnStrike = eventOnStrike;
}

public int getEventBatterPreviousPosition() {
	return eventBatterPreviousPosition;
}

public void setEventBatterPreviousPosition(int eventBatterPreviousPosition) {
	this.eventBatterPreviousPosition = eventBatterPreviousPosition;
}

public int getEventBatterPosition() {
	return eventBatterPosition;
}

public void setEventBatterPosition(int eventBatterPosition) {
	this.eventBatterPosition = eventBatterPosition;
}

public String getEventWasABoundary() {
	return eventWasABoundary;
}

public void setEventWasABoundary(String eventWasABoundary) {
	this.eventWasABoundary = eventWasABoundary;
}

public int getEventOverNo() {
	return eventOverNo;
}

public void setEventOverNo(int eventOverNo) {
	this.eventOverNo = eventOverNo;
}

public int getEventBallNo() {
	return eventBallNo;
}

public void setEventBallNo(int eventBallNo) {
	this.eventBallNo = eventBallNo;
}

public String getEventExtra() {
	return eventExtra;
}

public void setEventExtra(String eventExtra) {
	this.eventExtra = eventExtra;
}

public int getEventBatterNo() {
	return eventBatterNo;
}

public void setEventBatterNo(int eventBatterNo) {
	this.eventBatterNo = eventBatterNo;
}

public int getEventOtherBatterNo() {
	return eventOtherBatterNo;
}

public void setEventOtherBatterNo(int eventOtherBatterNo) {
	this.eventOtherBatterNo = eventOtherBatterNo;
}

public int getEventBowlerNo() {
	return eventBowlerNo;
}

public void setEventBowlerNo(int eventBowlerNo) {
	this.eventBowlerNo = eventBowlerNo;
}

public int getEventOtherBowlerNo() {
	return eventOtherBowlerNo;
}

public void setEventOtherBowlerNo(int eventOtherBowlerNo) {
	this.eventOtherBowlerNo = eventOtherBowlerNo;
}

public int getEventRuns() {
	return eventRuns;
}

public void setEventRuns(int eventRuns) {
	this.eventRuns = eventRuns;
}

public int getEventExtraRuns() {
	return eventExtraRuns;
}

public void setEventExtraRuns(int eventExtraRuns) {
	this.eventExtraRuns = eventExtraRuns;
}

public String getEventHowOut() {
	return eventHowOut;
}

public void setEventHowOut(String eventHowOut) {
	this.eventHowOut = eventHowOut;
}

public int getEventHowOutFielderId() {
	return eventHowOutFielderId;
}

public void setEventHowOutFielderId(int eventHowOutFielderId) {
	this.eventHowOutFielderId = eventHowOutFielderId;
}

public int getEventInningNumber() {
	return eventInningNumber;
}

public void setEventInningNumber(int eventInningNumber) {
	this.eventInningNumber = eventInningNumber;
}

public int getEventNumber() {
	return eventNumber;
}

public void setEventNumber(int eventNumber) {
	this.eventNumber = eventNumber;
}

public String getEventType() {
	return eventType;
}

public void setEventType(String eventType) {
	this.eventType = eventType;
}

@Override
public int compareTo(Event evnt) {
	return (int) (this.getEventNumber()-evnt.getEventNumber());
}

}