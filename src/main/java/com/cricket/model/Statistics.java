package com.cricket.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Statistics")
public class Statistics
{
  @Id
  @Column(name="STATISTICSID")
  private Integer statistics_id;
  
  @Column(name="PLAYERID")
  private Integer player_id;
  
  @Column(name="STATSTYPEID")
  private Integer stats_type_id;
  
  @Column(name="MATCHES")
  private Integer matches;
  
  @Column(name="INNINGS")
  private Integer innings;

  @Column(name="NOTOUT")
  private Integer not_out;
  
  @Column(name="RUNS")
  private Integer runs;
  
  @Column(name="BALLSFACED")
  private Integer balls_faced;
  
  @Column(name="BESTSCORE")
  private String best_score;
  
  @Column(name="100s")
  private Integer hundreds;
  
  @Column(name="50s")
  private Integer fifties;
  
  @Column(name="30s")
  private Integer thirties;
  
  @Column(name="BALLSBOWLED")
  private Integer balls_bowled;
  
  @Column(name="RUNSCONCEDED")
  private Integer runs_conceded;
  
  @Column(name="WICKETS")
  private Integer wickets;
  
  @Column(name="BESTFIGURES")
  private String best_figures;
  
  @Transient
  private StatsType stats_type;
  
  public Integer getStatistics_id()
  {
    return statistics_id;
  }
  
  public void setStatistics_id(Integer statistics_id)
  {
    this.statistics_id = statistics_id;
  }
  
  public Integer getPlayer_id()
  {
    return player_id;
  }
  
  public void setPlayer_id(Integer player_id)
  {
    this.player_id = player_id;
  }
  
  public Integer getStats_type_id()
  {
    return stats_type_id;
  }
  
  public void setStats_type_id(Integer stats_type_id)
  {
    this.stats_type_id = stats_type_id;
  }
  
  public StatsType getStats_type()
  {
    return stats_type;
  }
  
  public void setStats_type(StatsType stats_type)
  {
    this.stats_type = stats_type;
  }
  
  public Integer getMatches()
  {
    return matches;
  }
  
  public void setMatches(Integer matches)
  {
    this.matches = matches;
  }

public Integer getInnings() {
	return innings;
}

public void setInnings(Integer innings) {
	this.innings = innings;
}

public Integer getNot_out() {
	return not_out;
}

public void setNot_out(Integer not_out) {
	this.not_out = not_out;
}

public Integer getRuns() {
	return runs;
}

public void setRuns(Integer runs) {
	this.runs = runs;
}

public Integer getBalls_faced() {
	return balls_faced;
}

public void setBalls_faced(Integer balls_faced) {
	this.balls_faced = balls_faced;
}

public String getBest_score() {
	return best_score;
}

public void setBest_score(String best_score) {
	this.best_score = best_score;
}

public Integer getHundreds() {
	return hundreds;
}

public void setHundreds(Integer hundreds) {
	this.hundreds = hundreds;
}

public Integer getFifties() {
	return fifties;
}

public void setFifties(Integer fifties) {
	this.fifties = fifties;
}

public Integer getThirties() {
	return thirties;
}

public void setThirties(Integer thirties) {
	this.thirties = thirties;
}

public Integer getBalls_bowled() {
	return balls_bowled;
}

public void setBalls_bowled(Integer balls_bowled) {
	this.balls_bowled = balls_bowled;
}

public Integer getRuns_conceded() {
	return runs_conceded;
}

public void setRuns_conceded(Integer runs_conceded) {
	this.runs_conceded = runs_conceded;
}

public Integer getWickets() {
	return wickets;
}

public void setWickets(Integer wickets) {
	this.wickets = wickets;
}

public String getBest_figures() {
	return best_figures;
}

public void setBest_figures(String best_figures) {
	this.best_figures = best_figures;
}

}