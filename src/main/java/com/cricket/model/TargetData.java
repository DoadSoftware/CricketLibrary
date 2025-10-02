package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetData {

	boolean isMatchFinished;
	String targetOrResult;
	String targetOvers;
	int targetRuns;
	int remaningRuns;
	int remaningBall;
	
	public TargetData(String targetOrResult) {
		super();
		this.targetOrResult = targetOrResult;
	}
	public TargetData() {
		super();
		this.isMatchFinished = false;
		this.targetOrResult = "";
	}
	public String getTargetOrResult() {
		return targetOrResult;
	}
	public void setTargetOrResult(String targetOrResult) {
		this.targetOrResult = targetOrResult;
	}
	public boolean isMatchFinished() {
		return isMatchFinished;
	}
	public void setMatchFinished(boolean isMatchFinished) {
		this.isMatchFinished = isMatchFinished;
	}
	public String getTargetOvers() {
		return targetOvers;
	}
	public void setTargetOvers(String targetOvers) {
		this.targetOvers = targetOvers;
	}
	public int getTargetRuns() {
		return targetRuns;
	}
	public void setTargetRuns(int targetRuns) {
		if(targetRuns < 0) {
			this.targetRuns = 0;
		}
		else {
			this.targetRuns = targetRuns;
		}
	}
	public int getRemaningRuns() {
		return remaningRuns;
	}
	public void setRemaningRuns(int remaningRuns) {
		if(remaningRuns < 0) {
			this.remaningRuns = 0;
		}
		else {
			this.remaningRuns = remaningRuns;
		}
	}
	public int getRemaningBall() {
		return remaningBall;
	}
	public void setRemaningBall(int remaningBall) {
		if(remaningBall < 0) {
			this.remaningBall = 0;
		}
		else {
			this.remaningBall = remaningBall;
		}
	}
	@Override
	public String toString() {
		return "TargetData [isMatchFinished=" + isMatchFinished + ", targetOrResult=" + targetOrResult
				+ ", targetOvers=" + targetOvers + ", targetRuns=" + targetRuns + ", remaningRuns=" + remaningRuns
				+ ", remaningBall=" + remaningBall + "]";
	}
}
	
	