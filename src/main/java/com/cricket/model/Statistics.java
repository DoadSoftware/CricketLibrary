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
  
  @Column(name="BestScoreAgainst")
  private String best_score_against;
  
  @Column(name="BestScoreVenue")
  private String best_score_venue;
  
  @Column(name="100s")
  private Integer hundreds;
  
  @Column(name="50s")
  private Integer fifties;
  
  @Column(name="30s")
  private Integer thirties;
  
  @Column(name="4s")
  private Integer fours;
  
  @Column(name="6s")
  private Integer sixes;
  
  @Column(name="BALLSBOWLED")
  private double balls_bowled;
  
  @Column(name="RUNSCONCEDED")
  private double runs_conceded;
  
  @Column(name="3Plus")
  private Integer plus_3;
  
  @Column(name="5Plus")
  private Integer plus_5;
  
  @Column(name="DotBowled")
  private Integer dotbowled;
  
  @Column(name="WICKETS")
  private Integer wickets;
  
  @Column(name="BESTFIGURES")
  private String best_figures;
  
  @Column(name="BestFiguresAgainst")
  private String best_figures_against;
  
  @Column(name="BestFiguresVenue")
  private String best_figures_venue;
  
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

public String getBest_score_against() {
	return best_score_against;
}

public void setBest_score_against(String best_score_against) {
	this.best_score_against = best_score_against;
}

public String getBest_score_venue() {
	return best_score_venue;
}

public void setBest_score_venue(String best_score_venue) {
	this.best_score_venue = best_score_venue;
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

public Integer getFours() {
	return fours;
}

public void setFours(Integer fours) {
	this.fours = fours;
}

public Integer getSixes() {
	return sixes;
}

public void setSixes(Integer sixes) {
	this.sixes = sixes;
}

public double getBalls_bowled() {
	return balls_bowled;
}

public void setBalls_bowled(double balls_bowled) {
	this.balls_bowled = balls_bowled;
}

public double getRuns_conceded() {
	return runs_conceded;
}

public void setRuns_conceded(double runs_conceded) {
	this.runs_conceded = runs_conceded;
}

public void setRuns_conceded(Integer runs_conceded) {
	this.runs_conceded = runs_conceded;
}

public Integer getPlus_3() {
	return plus_3;
}

public void setPlus_3(Integer plus_3) {
	this.plus_3 = plus_3;
}

public Integer getPlus_5() {
	return plus_5;
}

public void setPlus_5(Integer plus_5) {
	this.plus_5 = plus_5;
}

public Integer getDotbowled() {
	return dotbowled;
}

public void setDotbowled(Integer dotbowled) {
	this.dotbowled = dotbowled;
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

public String getBest_figures_against() {
	return best_figures_against;
}

public void setBest_figures_against(String best_figures_against) {
	this.best_figures_against = best_figures_against;
}

public String getBest_figures_venue() {
	return best_figures_venue;
}

public void setBest_figures_venue(String best_figures_venue) {
	this.best_figures_venue = best_figures_venue;
}

}