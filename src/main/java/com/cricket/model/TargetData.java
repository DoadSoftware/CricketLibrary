package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetData {

	String targetOvers;
	int targetRuns;
	int remaningRuns;
	int remaningBall;
	
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
		this.targetRuns = targetRuns;
	}
	public int getRemaningRuns() {
		return remaningRuns;
	}
	public void setRemaningRuns(int remaningRuns) {
		this.remaningRuns = remaningRuns;
	}
	public int getRemaningBall() {
		return remaningBall;
	}
	public void setRemaningBall(int remaningBall) {
		this.remaningBall = remaningBall;
	}
	@Override
	public String toString() {
		return "TargetData [targetOvers=" + targetOvers + ", targetRuns=" + targetRuns + ", remaningRuns="
				+ remaningRuns + ", remaningBall=" + remaningBall + "]";
	}
}
	
	