package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Comparable<Event> {

  private int eventNumber;

  private int eventStatNumber;
  
  private String eventWasABoundary;
  
  private String eventType;

  private int eventInningNumber;
  
  private int eventBatterNo;

  private int eventConcussionReplacePlayerId;
  
  private String eventOnStrike;

  private int eventBatterPosition;

  private int eventBatterPreviousPosition;
  
  private int eventOtherBatterNo;
  
  private int eventBowlerNo;

  private int eventBowlingEnd;
  
  private int eventOtherBowlerNo;

  private int eventRuns;

  private int eventWickets;

  private int eventFours;

  private int eventSixes;
  
  private int eventOverNo;
  
  private int eventBallNo;

  private String eventExtra;
  
  private int eventExtraRuns;

  private String eventSubExtra;
  
  private int eventSubExtraRuns;
  
  private String eventHowOut;

  private int eventHowOutBatterNo;
  
  private int eventHowOutFielderId;

  private int eventTotalRunsInAnOver;

  private String doNotIncrementBall;

  private String eventDescription;

  private String substitutionMade;

  private String eventSpeed;

  private String whatToProcess;

  private String valueToProcess;
  
  private BattingCard EventBattingCard;

  private BowlingCard EventBowlingCard;

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

public Event(String whatToProcess, String valueToProcess) {
	super();
	this.whatToProcess = whatToProcess;
	this.valueToProcess = valueToProcess;
}

public String getWhatToProcess() {
	return whatToProcess;
}

public void setWhatToProcess(String whatToProcess) {
	this.whatToProcess = whatToProcess;
}

public String getValueToProcess() {
	return valueToProcess;
}

public void setValueToProcess(String valueToProcess) {
	this.valueToProcess = valueToProcess;
}

public int getEventWickets() {
	return eventWickets;
}

public void setEventWickets(int eventWickets) {
	if(eventWickets < 0) {
		this.eventWickets = 0;
	}
	else {
		this.eventWickets = eventWickets;
	}
}

public int getEventFours() {
	return eventFours;
}

public void setEventFours(int eventFours) {
	if(eventFours < 0) {
		this.eventFours = 0;
	}
	else {
		this.eventFours = eventFours;
	}
}

public int getEventSixes() {
	return eventSixes;
}

public void setEventSixes(int eventSixes) {
	if(eventSixes < 0) {
		this.eventSixes = 0;
	}
	else {
		this.eventSixes = eventSixes;
	}
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
	if(eventSubExtraRuns < 0) {
		this.eventSubExtraRuns = 0;
	}
	else {
		this.eventSubExtraRuns = eventSubExtraRuns;
	}
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
	if(eventOverNo < 0) {
		this.eventOverNo = 0;
	}
	else {
		this.eventOverNo = eventOverNo;
	}
}

public int getEventBallNo() {
	return eventBallNo;
}

public void setEventBallNo(int eventBallNo) {
	if(eventBallNo < 0) {
		this.eventBallNo = 0;
	}
	else {
		this.eventBallNo = eventBallNo;
	}
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
	if(eventRuns < 0) {
		this.eventRuns = 0;
	}
	else {
		this.eventRuns = eventRuns;
	}
}

public int getEventExtraRuns() {
	return eventExtraRuns;
}

public void setEventExtraRuns(int eventExtraRuns) {
	if(eventExtraRuns < 0) {
		this.eventExtraRuns = 0;
	}
	else {
		this.eventExtraRuns = eventExtraRuns;
	}
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

@Override
public String toString() {
	return "Event [eventNumber=" + eventNumber + ", eventStatNumber=" + eventStatNumber + ", eventWasABoundary="
			+ eventWasABoundary + ", eventType=" + eventType + ", eventInningNumber=" + eventInningNumber
			+ ", eventBatterNo=" + eventBatterNo + ", eventConcussionReplacePlayerId=" + eventConcussionReplacePlayerId
			+ ", eventOnStrike=" + eventOnStrike + ", eventBatterPosition=" + eventBatterPosition
			+ ", eventBatterPreviousPosition=" + eventBatterPreviousPosition + ", eventOtherBatterNo="
			+ eventOtherBatterNo + ", eventBowlerNo=" + eventBowlerNo + ", eventBowlingEnd=" + eventBowlingEnd
			+ ", eventOtherBowlerNo=" + eventOtherBowlerNo + ", eventRuns=" + eventRuns + ", eventWickets="
			+ eventWickets + ", eventFours=" + eventFours + ", eventSixes=" + eventSixes + ", eventOverNo="
			+ eventOverNo + ", eventBallNo=" + eventBallNo + ", eventExtra=" + eventExtra + ", eventExtraRuns="
			+ eventExtraRuns + ", eventSubExtra=" + eventSubExtra + ", eventSubExtraRuns=" + eventSubExtraRuns
			+ ", eventHowOut=" + eventHowOut + ", eventHowOutBatterNo=" + eventHowOutBatterNo
			+ ", eventHowOutFielderId=" + eventHowOutFielderId + ", eventTotalRunsInAnOver=" + eventTotalRunsInAnOver
			+ ", doNotIncrementBall=" + doNotIncrementBall + ", eventDescription=" + eventDescription
			+ ", substitutionMade=" + substitutionMade + ", eventSpeed=" + eventSpeed + ", whatToProcess="
			+ whatToProcess + ", valueToProcess=" + valueToProcess + ", EventBattingCard=" + EventBattingCard
			+ ", EventBowlingCard=" + EventBowlingCard + ", EventPartnership=" + EventPartnership + "]";
}

}