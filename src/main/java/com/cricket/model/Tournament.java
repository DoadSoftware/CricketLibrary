package com.cricket.model;

import java.util.ArrayList;
import java.util.List;
import com.cricket.util.CricketUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tournament implements Cloneable {

  private int playerId;
  
  private int matches;
  
  private int innings;
  
  private int not_out;
  
  private int runs;
  
  private int fours;
  
  private int sixes;
  
  private int thirty;
  
  private int fifty;
  
  private int hundreds;
  
  private int wickets;
  
  private int runsConceded;
  
  private int ballsBowled;
  
  private int threeWicketHaul;
  
  private int fiveWicketHaul;
  
  private int ballsFaced;
  
  private int dots;
  
  private String notOut;
  
  private int runs_against_pace;
  private int balls_against_pace;
  
  private int runs_against_spin;
  private int balls_against_spin;
  
  private int total_dismissal;
  private int other;
  private int caught;
  private int ctAndBowled;
  private int bowled;
  private int run_out;
  private int stumped;
  private int hit_wicket;
  private int lbw;
  
  private int ducks;
  private int under_10;
  private int ten_to_39;
  private int forty_to_49;
  private int fifty_to_69;
  private int seventy_to_89;
  private int ninty_to_99;
  private int hundred_to_199;
  private int plus_199;
  
  private int zero_wickets;
  private int one_wickets;
  private int two_wickets;
  private int three_wickets;
  private int four_wickets;
  private int five_wickets;
  private int six_wickets;
  
  private int other_bowler;
  private int caught_bowler;
  private int ctAndBowled_bowler;
  private int bowled_bowler;
  private int stumped_bowler;
  private int hit_wicket_bowler;
  private int lbw_bowler;
  
  private int tapeBall_balls;
  private int tapeBall_runs;
  private int tapeBall_wickets;
  private int tapeBall_dotsBall;
  
  @JsonIgnore
  private Player player;
  
  private int tournament_fours;
  
  private int tournament_sixes;
  
  private List<BestStats> batsman_best_Stats;

  private List<BestStats> bowler_best_Stats;
  
  private List<BestStats> tapeBall_best_Stats;

public Tournament() {
	super();
}

public Tournament(int playerId, int runs, int fours, int sixes, int innings, int not_out, int thirty, int fifty, int hundreds, int wickets, int runsConceded,
		int ballsBowled, int ballsFaced, int dots, int threeWicketHaul, int fiveWicketHaul, String notOut, int runs_against_pace, int balls_against_pace,
		int runs_against_spin, int balls_against_spin, Player player, List<BestStats> batsman_best_Stats,
		List<BestStats> bowler_best_Stats, List<BestStats> tapeBall_best_Stats) {
	super();
	this.playerId = playerId;
	this.runs = runs;
	this.fours = fours;
	this.sixes = sixes;
	this.innings = innings;
	this.not_out = not_out;
	this.thirty = thirty;
	this.fifty = fifty;
	this.hundreds = hundreds;
	this.wickets = wickets;
	this.runsConceded = runsConceded;
	this.ballsBowled = ballsBowled;
	this.ballsFaced = ballsFaced;
	this.dots = dots;
	this.threeWicketHaul = threeWicketHaul;
	this.fiveWicketHaul = fiveWicketHaul;
	this.notOut = notOut;
	this.runs_against_pace = runs_against_pace;
	this.balls_against_pace = balls_against_pace;
	this.runs_against_spin = runs_against_spin;
	this.balls_against_spin = balls_against_spin;
	this.player = player;
	this.batsman_best_Stats = batsman_best_Stats;
	this.bowler_best_Stats = bowler_best_Stats;
	this.tapeBall_best_Stats = tapeBall_best_Stats;
}

public Tournament(int playerId, int total_dismissal, int other, int caught, int ctAndBowled, int bowled, int run_out,
		int stumped, int hit_wicket, int lbw, Player player) {
	super();
	this.playerId = playerId;
	this.total_dismissal = total_dismissal;
	this.other = other;
	this.caught = caught;
	this.ctAndBowled = ctAndBowled;
	this.bowled = bowled;
	this.run_out = run_out;
	this.stumped = stumped;
	this.hit_wicket = hit_wicket;
	this.lbw = lbw;
	this.player = player;
}

public Tournament(int playerId, int zero_wickets, int one_wickets, int two_wickets, int three_wickets, int four_wickets,
		int five_wickets, int six_wickets, Player player) {
	super();
	this.playerId = playerId;
	this.zero_wickets = zero_wickets;
	this.one_wickets = one_wickets;
	this.two_wickets = two_wickets;
	this.three_wickets = three_wickets;
	this.four_wickets = four_wickets;
	this.five_wickets = five_wickets;
	this.six_wickets = six_wickets;
	this.player = player;
}

public int getBatsmanScoreSortData() {
	int sortData = this.getRuns();
	if(this.getNotOut() != null && this.getNotOut().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
		sortData = sortData + 1;
	}
	return 1000 * sortData + 1000 - this.getBallsFaced();
}

public int getBatsmanStrikeRateSortData() {
	int temp = 0;
	if(this.getBallsFaced() >= 1) {
		temp = (100*this.getRuns())/this.getBallsFaced();
	}
	if(temp > 32000) {
		return 0 ;
	}else {
		return temp;
	}
}

public int getBowlerFigureSortData() {	
	return 1000 * this.getWickets() - this.getRunsConceded();
}

public int getBowlerEconomySortData() {
	int temp = 0;
	if(this.getBallsBowled()>0) {
		temp = (1000*this.getRunsConceded())/this.getBallsBowled();
	}
	if(temp > 32000) {
		return 32000;
	}else {
		return 20000-temp;
	}
}

public int getBowlerStrikeRateSortData() {
	int temp = 0;
	if(this.getBallsBowled() >= 1 && this.getWickets() >= 1) {
		temp = (100*this.getBallsBowled())/this.getWickets();
	}
	if(temp > 32000) {
		return 0 ;
	}else {
		return temp;
	}
}

public int getBatsmanFoursSortData() {
	return 1000 * this.getFours() - this.getInnings();
}

public int getBatsmanSixesSortData() {
	return 1000 * this.getSixes() - this.getInnings();
}

public List<BestStats> getBatsman_best_Stats() {
	return batsman_best_Stats;
}

public void setBatsman_best_Stats(List<BestStats> batsman_best_Stats) {
	this.batsman_best_Stats = batsman_best_Stats;
}

public List<BestStats> getBowler_best_Stats() {
	return bowler_best_Stats;
}

public void setBowler_best_Stats(List<BestStats> bowler_best_Stats) {
	this.bowler_best_Stats = bowler_best_Stats;
}

public int getPlayerId() {
	return playerId;
}

public void setPlayerId(int playerId) {
	this.playerId = playerId;
}

public int getMatches() {
	return matches;
}

public void setMatches(int matches) {
	this.matches = matches;
}

public int getFours() {
	return fours;
}

public void setFours(int fours) {
	if(fours < 0) {
		this.fours = 0;
	}
	else {
		this.fours = fours;
	}
}

public int getSixes() {
	return sixes;
}

public void setSixes(int sixes) {
	if(sixes < 0) {
		this.sixes = 0;
	}
	else {
		this.sixes = sixes;
	}
}

public int getRuns() {
	return runs;
}

public void setRuns(int runs) {
	if(runs < 0) {
		this.runs = 0;
	}
	else {
		this.runs = runs;
	}
}

public int getWickets() {
	return wickets;
}

public void setWickets(int wickets) {
	if(wickets < 0) {
		this.wickets = 0;
	}
	else {
		this.wickets = wickets;
	}
}

public int getRunsConceded() {
	return runsConceded;
}

public void setRunsConceded(int runsConceded) {
	if(runsConceded < 0) {
		this.runsConceded = 0;
	}
	else {
		this.runsConceded = runsConceded;
	}
}

public int getBallsBowled() {
	return ballsBowled;
}

public void setBallsBowled(int ballsBowled) {
	if(ballsBowled < 0) {
		this.ballsBowled = 0;
	}
	else {
		this.ballsBowled = ballsBowled;
	}
}

public int getBallsFaced() {
	return ballsFaced;
}

public void setBallsFaced(int ballsFaced) {
	if(ballsFaced < 0) {
		this.ballsFaced = 0;
	}
	else {
		this.ballsFaced = ballsFaced;
	}
}

public String getNotOut() {
	return notOut;
}

public void setNotOut(String notOut) {
	this.notOut = notOut;
}

public Player getPlayer() {
	return player;
}

public void setPlayer(Player player) {
	this.player = player;
}

public int getDots() {
	return dots;
}

public void setDots(int dots) {
	if(dots < 0) {
		this.dots = 0;
	}
	else {
		this.dots = dots;
	}
}

public int getTournament_fours() {
	return tournament_fours;
}

public void setTournament_fours(int tournament_fours) {
	if(tournament_fours < 0) {
		this.tournament_fours = 0;
	}
	else {
		this.tournament_fours = tournament_fours;
	}
}

public int getTournament_sixes() {
	return tournament_sixes;
}

public void setTournament_sixes(int tournament_sixes) {
	if(tournament_sixes < 0) {
		this.tournament_sixes = 0;
	}
	else {
		this.tournament_sixes = tournament_sixes;
	}
}

public int getRuns_against_pace() {
	return runs_against_pace;
}

public void setRuns_against_pace(int runs_against_pace) {
	if(runs_against_pace < 0) {
		this.runs_against_pace = 0;
	}
	else {
		this.runs_against_pace = runs_against_pace;
	}
}

public int getBalls_against_pace() {
	return balls_against_pace;
}

public void setBalls_against_pace(int balls_against_pace) {
	if(balls_against_pace < 0) {
		this.balls_against_pace = 0;
	}
	else {
		this.balls_against_pace = balls_against_pace;
	}
}

public int getRuns_against_spin() {
	return runs_against_spin;
}

public void setRuns_against_spin(int runs_against_spin) {
	if(runs_against_spin < 0) {
		this.runs_against_spin = 0;
	}
	else {
		this.runs_against_spin = runs_against_spin;
	}
}

public int getBalls_against_spin() {
	return balls_against_spin;
}

public void setBalls_against_spin(int balls_against_spin) {
	if(balls_against_spin < 0) {
		this.balls_against_spin = 0;
	}
	else {
		this.balls_against_spin = balls_against_spin;
	}
}

public int getFifty() {
	return fifty;
}

public void setFifty(int fifty) {
	if(fifty < 0) {
		this.fifty = 0;
	}
	else {
		this.fifty = fifty;
	}
}

public int getHundreds() {
	return hundreds;
}

public void setHundreds(int hundreds) {
	if(hundreds < 0) {
		this.hundreds = 0;
	}
	else {
		this.hundreds = hundreds;
	}
}

public int getInnings() {
	return innings;
}

public void setInnings(int innings) {
	if(innings < 0) {
		this.innings = 0;
	}
	else {
		this.innings = innings;
	}
}

public int getNot_out() {
	return not_out;
}

public void setNot_out(int not_out) {
	this.not_out = not_out;
}

public int getThreeWicketHaul() {
	return threeWicketHaul;
}

public void setThreeWicketHaul(int threeWicketHaul) {
	this.threeWicketHaul = threeWicketHaul;
}

public int getFiveWicketHaul() {
	return fiveWicketHaul;
}

public void setFiveWicketHaul(int fiveWicketHaul) {
	this.fiveWicketHaul = fiveWicketHaul;
}

public int getThirty() {
	return thirty;
}

public void setThirty(int thirty) {
	this.thirty = thirty;
}

public int getTotal_dismissal() {
	return total_dismissal;
}

public void setTotal_dismissal(int total_dismissal) {
	this.total_dismissal = total_dismissal;
}

public int getOther() {
	return other;
}

public void setOther(int other) {
	this.other = other;
}

public int getCaught() {
	return caught;
}

public void setCaught(int caught) {
	this.caught = caught;
}

public int getCtAndBowled() {
	return ctAndBowled;
}

public void setCtAndBowled(int ctAndBowled) {
	this.ctAndBowled = ctAndBowled;
}

public int getBowled() {
	return bowled;
}

public void setBowled(int bowled) {
	this.bowled = bowled;
}

public int getRun_out() {
	return run_out;
}

public void setRun_out(int run_out) {
	this.run_out = run_out;
}

public int getStumped() {
	return stumped;
}

public void setStumped(int stumped) {
	this.stumped = stumped;
}

public int getHit_wicket() {
	return hit_wicket;
}

public void setHit_wicket(int hit_wicket) {
	this.hit_wicket = hit_wicket;
}

public int getLbw() {
	return lbw;
}

public void setLbw(int lbw) {
	this.lbw = lbw;
}

public int getDucks() {
	return ducks;
}

public void setDucks(int ducks) {
	this.ducks = ducks;
}

public int getUnder_10() {
	return under_10;
}

public void setUnder_10(int under_10) {
	this.under_10 = under_10;
}

public int getTen_to_39() {
	return ten_to_39;
}

public void setTen_to_39(int ten_to_39) {
	this.ten_to_39 = ten_to_39;
}

public int getForty_to_49() {
	return forty_to_49;
}

public void setForty_to_49(int forty_to_49) {
	this.forty_to_49 = forty_to_49;
}

public int getFifty_to_69() {
	return fifty_to_69;
}

public void setFifty_to_69(int fifty_to_69) {
	this.fifty_to_69 = fifty_to_69;
}

public int getSeventy_to_89() {
	return seventy_to_89;
}

public void setSeventy_to_89(int seventy_to_89) {
	this.seventy_to_89 = seventy_to_89;
}

public int getNinty_to_99() {
	return ninty_to_99;
}

public void setNinty_to_99(int ninty_to_99) {
	this.ninty_to_99 = ninty_to_99;
}

public int getHundred_to_199() {
	return hundred_to_199;
}

public void setHundred_to_199(int hundred_to_199) {
	this.hundred_to_199 = hundred_to_199;
}

public int getPlus_199() {
	return plus_199;
}

public void setPlus_199(int plus_199) {
	this.plus_199 = plus_199;
}

public int getZero_wickets() {
	return zero_wickets;
}

public void setZero_wickets(int zero_wickets) {
	this.zero_wickets = zero_wickets;
}

public int getOne_wickets() {
	return one_wickets;
}

public void setOne_wickets(int one_wickets) {
	this.one_wickets = one_wickets;
}

public int getTwo_wickets() {
	return two_wickets;
}

public void setTwo_wickets(int two_wickets) {
	this.two_wickets = two_wickets;
}

public int getThree_wickets() {
	return three_wickets;
}

public void setThree_wickets(int three_wickets) {
	this.three_wickets = three_wickets;
}

public int getFour_wickets() {
	return four_wickets;
}

public void setFour_wickets(int four_wickets) {
	this.four_wickets = four_wickets;
}

public int getFive_wickets() {
	return five_wickets;
}

public void setFive_wickets(int five_wickets) {
	this.five_wickets = five_wickets;
}

public int getSix_wickets() {
	return six_wickets;
}

public void setSix_wickets(int six_wickets) {
	this.six_wickets = six_wickets;
}

public int getOther_bowler() {
	return other_bowler;
}

public void setOther_bowler(int other_bowler) {
	this.other_bowler = other_bowler;
}

public int getCaught_bowler() {
	return caught_bowler;
}

public void setCaught_bowler(int caught_bowler) {
	this.caught_bowler = caught_bowler;
}

public int getCtAndBowled_bowler() {
	return ctAndBowled_bowler;
}

public void setCtAndBowled_bowler(int ctAndBowled_bowler) {
	this.ctAndBowled_bowler = ctAndBowled_bowler;
}

public int getBowled_bowler() {
	return bowled_bowler;
}

public void setBowled_bowler(int bowled_bowler) {
	this.bowled_bowler = bowled_bowler;
}

public int getStumped_bowler() {
	return stumped_bowler;
}

public void setStumped_bowler(int stumped_bowler) {
	this.stumped_bowler = stumped_bowler;
}

public int getHit_wicket_bowler() {
	return hit_wicket_bowler;
}

public void setHit_wicket_bowler(int hit_wicket_bowler) {
	this.hit_wicket_bowler = hit_wicket_bowler;
}

public int getLbw_bowler() {
	return lbw_bowler;
}

public void setLbw_bowler(int lbw_bowler) {
	this.lbw_bowler = lbw_bowler;
}

public int getTapeBall_balls() {
	return tapeBall_balls;
}

public void setTapeBall_balls(int tapeBall_balls) {
	this.tapeBall_balls = tapeBall_balls;
}

public int getTapeBall_runs() {
	return tapeBall_runs;
}

public void setTapeBall_runs(int tapeBall_runs) {
	this.tapeBall_runs = tapeBall_runs;
}

public int getTapeBall_wickets() {
	return tapeBall_wickets;
}

public void setTapeBall_wickets(int tapeBall_wickets) {
	this.tapeBall_wickets = tapeBall_wickets;
}

public int getTapeBall_dotsBall() {
	return tapeBall_dotsBall;
}

public void setTapeBall_dotsBall(int tapeBall_dotsBall) {
	this.tapeBall_dotsBall = tapeBall_dotsBall;
}

public List<BestStats> getTapeBall_best_Stats() {
	return tapeBall_best_Stats;
}

public void setTapeBall_best_Stats(List<BestStats> tapeBall_best_Stats) {
	this.tapeBall_best_Stats = tapeBall_best_Stats;
}

@Override
public String toString() {
	return "Tournament [playerId=" + playerId + ", matches=" + matches + ", runs=" + runs + ", fours=" + fours
			+ ", sixes=" + sixes + ", wickets=" + wickets + ", runsConceded=" + runsConceded + ", ballsBowled="
			+ ballsBowled + ", ballsFaced=" + ballsFaced + ", dots=" + dots + ", notOut=" + notOut + ", player="
			+ player + ", batsman_best_Stats=" + batsman_best_Stats + ", bowler_best_Stats=" + bowler_best_Stats + "]";
}

//@Override
//public Tournament clone() throws CloneNotSupportedException {
//    Tournament clone = null;
//    try
//    {
//        clone = (Tournament) super.clone();
//    } 
//    catch (CloneNotSupportedException e) 
//    {
//        throw new RuntimeException(e);
//    }
//    return clone;
//}

@Override
public Tournament clone() throws CloneNotSupportedException {
    Tournament clone = (Tournament) super.clone();
    if (this.batsman_best_Stats != null) {
        clone.batsman_best_Stats = new ArrayList<>(this.batsman_best_Stats.size());
        for (BestStats stat : this.batsman_best_Stats) {
            clone.batsman_best_Stats.add(stat.clone()); // Ensure `BestStats` also supports deep copying
        }
    }
    if (this.bowler_best_Stats != null) {
        clone.bowler_best_Stats = new ArrayList<>(this.bowler_best_Stats.size());
        for (BestStats stat : this.bowler_best_Stats) {
            clone.bowler_best_Stats.add(stat.clone());
        }
    }
    return clone;
}

}