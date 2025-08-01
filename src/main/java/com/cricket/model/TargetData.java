package com.cricket.model;

public class TargetData {

	int targetOvers;
	int targetRuns;
	int remaningRuns;
	int remaningBall;
	public int getTargetOvers() {
		return targetOvers;
	}
	public void setTargetOvers(int targetOvers) {
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
	
	