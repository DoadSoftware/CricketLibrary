package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchFinishTime
{
	private String startOfPlay;

	private String startOfLunch;
	
	private String endOfLunch;
	
	private String startOfTea;
	
	private String endOfTea;
	
	private String endOfPlay;

	private String max_overs;
	
	private String new_ball_over;

	public MatchFinishTime(String startOfPlay, String startOfLunch, String endOfLunch, String startOfTea,
			String endOfTea, String endOfPlay, String max_overs, String new_ball_over) {
		super();
		this.startOfPlay = startOfPlay;
		this.startOfLunch = startOfLunch;
		this.endOfLunch = endOfLunch;
		this.startOfTea = startOfTea;
		this.endOfTea = endOfTea;
		this.endOfPlay = endOfPlay;
		this.max_overs = max_overs;
		this.new_ball_over = new_ball_over;
	}

	public MatchFinishTime() {
		super();
	}

	public String getNew_ball_over() {
		return new_ball_over;
	}

	public void setNew_ball_over(String new_ball_over) {
		this.new_ball_over = new_ball_over;
	}

	public String getMax_overs() {
		return max_overs;
	}

	public void setMax_overs(String max_overs) {
		this.max_overs = max_overs;
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

	@Override
	public String toString() {
		return "MatchFinishTime [new_ball_over=" + new_ball_over + ", max_overs=" + max_overs + ", startOfPlay="
				+ startOfPlay + ", startOfLunch=" + startOfLunch + ", endOfLunch=" + endOfLunch + ", startOfTea="
				+ startOfTea + ", endOfTea=" + endOfTea + ", endOfPlay=" + endOfPlay + "]";
	}
}
