package com.cricket.model;

public class MatchFinishTime
{
	private String startOfPlay;

	private String startOfLunch;
	
	private String endOfLunch;
	
	private String startOfTea;
	
	private String endOfTea;
	
	private String endOfPlay;

	public MatchFinishTime(String startOfPlay, String startOfLunch, String endOfLunch, String startOfTea,
			String endOfTea, String endOfPlay) {
		super();
		this.startOfPlay = startOfPlay;
		this.startOfLunch = startOfLunch;
		this.endOfLunch = endOfLunch;
		this.startOfTea = startOfTea;
		this.endOfTea = endOfTea;
		this.endOfPlay = endOfPlay;
	}

	public MatchFinishTime() {
		super();
	}

	public String getStartOfPlay() {
		return startOfPlay;
	}

	public void setStartOfPlay(String startOfPlay) {
		this.startOfPlay = startOfPlay;
	}

	public String getStartOfLunch() {
		return startOfLunch;
	}

	public void setStartOfLunch(String startOfLunch) {
		this.startOfLunch = startOfLunch;
	}

	public String getEndOfLunch() {
		return endOfLunch;
	}

	public void setEndOfLunch(String endOfLunch) {
		this.endOfLunch = endOfLunch;
	}

	public String getStartOfTea() {
		return startOfTea;
	}

	public void setStartOfTea(String startOfTea) {
		this.startOfTea = startOfTea;
	}

	public String getEndOfTea() {
		return endOfTea;
	}

	public void setEndOfTea(String endOfTea) {
		this.endOfTea = endOfTea;
	}

	public String getEndOfPlay() {
		return endOfPlay;
	}

	public void setEndOfPlay(String endOfPlay) {
		this.endOfPlay = endOfPlay;
	}
	
}
