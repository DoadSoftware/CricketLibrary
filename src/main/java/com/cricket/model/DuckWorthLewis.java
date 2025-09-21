package com.cricket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DuckWorthLewis 
{
	private String over_left;
	private int balls_left;
	private String wkts_down;
	private int data;
	public DuckWorthLewis() {
		super();
	}
	
	public DuckWorthLewis(String over_left, String wkts_down) {
		super();
		this.over_left = over_left;
		this.wkts_down = wkts_down;
	}


	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public DuckWorthLewis(String over_left) {
		super();
		this.over_left = over_left;
	}
	public String getOver_left() {
		return over_left;
	}
	public void setOver_left(String over_left) {
		this.over_left = over_left;
	}
	public int getBalls_left() {
		return balls_left;
	}
	public void setBalls_left(int balls_left) {
		if(balls_left < 0) {
			this.balls_left = 0;
		}
		else {
			this.balls_left = balls_left;
		}
	}
	public String getWkts_down() {
		return wkts_down;
	}
	public void setWkts_down(String wkts_down) {
		this.wkts_down = wkts_down;
	}
	
	
}
