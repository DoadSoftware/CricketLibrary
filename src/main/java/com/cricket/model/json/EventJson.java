package com.cricket.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value="event", namespace = "events")
public class EventJson implements Comparable<EventJson> {

  @JsonProperty(value ="eventNumber")
  private int eventNumber;

  @JsonProperty(value ="eventStatNumber")
  private int eventStatNumber;
  
  @JsonProperty(value ="eventWasABoundary")
  private String eventWasABoundary;
  
  @JsonProperty(value ="eventType")
  private String eventType;

  @JsonProperty(value ="eventInningNumber")
  private int eventInningNumber;
  
  @JsonProperty(value ="eventBatterNo")
  private int eventBatterNo;

  @JsonProperty(value ="eventConcussionReplacePlayerId")
  private int eventConcussionReplacePlayerId;
  
  @JsonProperty(value ="eventOnStrike")
  private String eventOnStrike;

  @JsonProperty(value ="eventBatterPosition")
  private int eventBatterPosition;

  @JsonProperty(value ="eventBatterPreviousPosition")
  private int eventBatterPreviousPosition;
  
  @JsonProperty(value ="eventOtherBatterNo")
  private int eventOtherBatterNo;
  
  @JsonProperty(value ="eventBowlerNo")
  private int eventBowlerNo;

  @JsonProperty(value ="eventBowlingEnd")
  private int eventBowlingEnd;
  
  @JsonProperty(value ="eventOtherBowlerNo")
  private int eventOtherBowlerNo;

  @JsonProperty(value ="eventRuns")
  private int eventRuns;

  @JsonProperty(value ="EventWickets")
  private int eventWickets;

  @JsonProperty(value ="eventFours")
  private int eventFours;

  @JsonProperty(value ="eventSixes")
  private int eventSixes;
  
  @JsonProperty(value ="eventOverNo")
  private int eventOverNo;
  
  @JsonProperty(value ="eventBallNo")
  private int eventBallNo;

  @JsonProperty(value ="eventExtra")
  private String eventExtra;
  
  @JsonProperty(value ="eventExtraRuns")
  private int eventExtraRuns;

  @JsonProperty(value ="eventSubExtra")
  private String eventSubExtra;
  
  @JsonProperty(value ="eventSubExtraRuns")
  private int eventSubExtraRuns;
  
  @JsonProperty(value ="eventHowOut")
  private String eventHowOut;

  @JsonProperty(value ="eventHowOutBatterNo")
  private int eventHowOutBatterNo;
  
  @JsonProperty(value ="eventHowOutFielderId")
  private int eventHowOutFielderId;

  @JsonProperty(value ="eventTotalRunsInAnOver")
  private int eventTotalRunsInAnOver;

  @JsonProperty(value ="doNotIncrementBall")
  private String doNotIncrementBall;

  @JsonProperty(value ="eventDescription")
  private String eventDescription;

  @JsonProperty(value ="substitutionMade")
  private String substitutionMade;

  @JsonProperty(value ="eventSpeed")
  private String eventSpeed;

  @JsonProperty(value ="EventBattingCard")
  private BattingCardJson EventBattingCard;

  @JsonProperty(value ="EventBowlingCard")
  private BowlingCardJson EventBowlingCard;

  @JsonProperty(value ="EventPartnership")
  private PartnershipJson EventPartnership;

public EventJson() {
	super();
}

public EventJson(int eventNumber, int eventStatNumber, String eventType, String eventDescription, int eventInningNumber, int eventBatterNo,
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

public BattingCardJson getEventBattingCard() {
	return EventBattingCard;
}

public void setEventBattingCard(BattingCardJson eventBattingCard) {
	EventBattingCard = eventBattingCard;
}

public BowlingCardJson getEventBowlingCard() {
	return EventBowlingCard;
}

public void setEventBowlingCard(BowlingCardJson eventBowlingCard) {
	EventBowlingCard = eventBowlingCard;
}

public PartnershipJson getEventPartnership() {
	return EventPartnership;
}

public void setEventPartnership(PartnershipJson eventPartnership) {
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
public int compareTo(EventJson evnt) {
	return (int) (this.getEventNumber()-evnt.getEventNumber());
}

}