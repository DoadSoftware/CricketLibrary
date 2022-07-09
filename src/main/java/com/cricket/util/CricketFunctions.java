package com.cricket.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.cricket.model.BattingCard;
import com.cricket.model.BowlingCard;
import com.cricket.model.Event;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.Player;
import com.cricket.service.CricketService;

public class CricketFunctions {

	public static class BatsmenScoreComparator implements Comparator<BattingCard> {
	    @Override
	    public int compare(BattingCard bc1, BattingCard bc2) {
	       return Integer.compare(bc2.getBatsmanScoreSortData(), bc1.getBatsmanScoreSortData());
	    }
	}

	public static class BowlerFiguresComparator implements Comparator<BowlingCard> {
	    @Override
	    public int compare(BowlingCard bc1, BowlingCard bc2) {
	       return Integer.compare(bc2.getBowlerFigureSortData(), bc1.getBowlerFigureSortData());
	    }
	}
	
	public static Match populateMatchVariables(CricketService cricketService,Match match) throws IllegalAccessException, InvocationTargetException 
	{
		List<Player> players = new ArrayList<Player>();
		Player this_plyr = new Player();
		
		for(Player plyr:match.getHomeSquad()) {
			this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(plyr.getPlayerId()));
			if(this_plyr != null) {
				this_plyr.setPlayerPosition(plyr.getPlayerPosition()); this_plyr.setCaptainWicketKeeper(plyr.getCaptainWicketKeeper());
				if(match.getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
					this_plyr.setPhoto("");
				}
				players.add(this_plyr);
			}
		}
		match.setHomeSquad(players);

		players = new ArrayList<Player>();
		if(match.getHomeOtherSquad() != null) {
			for(Player plyr:match.getHomeOtherSquad()) {
				this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(plyr.getPlayerId()));
				if(this_plyr != null) {
					if(match.getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
						this_plyr.setPhoto("");
					}
					players.add(this_plyr);
				}
			}
		}
		match.setHomeOtherSquad(players);
		
		players = new ArrayList<Player>();
		for(Player plyr:match.getAwaySquad()) {
			this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(plyr.getPlayerId()));
			if(this_plyr != null) {
				this_plyr.setPlayerPosition(plyr.getPlayerPosition()); this_plyr.setCaptainWicketKeeper(plyr.getCaptainWicketKeeper());
				if(match.getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
					this_plyr.setPhoto("");
				}
				players.add(this_plyr);
			}
		}
		match.setAwaySquad(players);

		players = new ArrayList<Player>();
		if(match.getAwayOtherSquad() != null) {
			for(Player plyr:match.getAwayOtherSquad()) {
				this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(plyr.getPlayerId()));
				if(this_plyr != null) {
					if(match.getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
						this_plyr.setPhoto("");
					}
					players.add(this_plyr);
				}
			}
		}
		match.setAwayOtherSquad(players);
		
		if(match.getHomeTeamId() > 0)
			match.setHomeTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getHomeTeamId())));
		if(match.getAwayTeamId() > 0)
			match.setAwayTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getAwayTeamId())));
		if(match.getGroundId() > 0) {
			match.setGround(cricketService.getGround(match.getGroundId()));
			match.setVenueName(match.getGround().getFullname());
		}
		
		for(Inning inn:match.getInning()) {
			
			inn.setBatting_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBattingTeamId())));
			inn.setBowling_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBowlingTeamId())));
			
			if(inn.getBattingCard() != null)
				for(BattingCard batc:inn.getBattingCard()) 
					batc = processBattingcard(cricketService,batc);
			
			if(inn.getBowlingCard() != null)
				for(BowlingCard bowlc:inn.getBowlingCard())
					bowlc.setPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bowlc.getPlayerId())));

			if(inn.getBowlingTeamId() == match.getHomeTeamId()) {
				inn.setFielders(match.getHomeSquad());
			} else if(inn.getBowlingTeamId() == match.getAwayTeamId()) {
				inn.setFielders(match.getAwaySquad());
			}

		}
		return match;
	}
	
	public static BattingCard processBattingcard(CricketService cricketService,BattingCard bc)
	{
		bc.setPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getPlayerId())));
		if (bc.getConcussionPlayerId() > 0) {
			bc.setConcussion_player(cricketService.getPlayer(CricketUtil.PLAYER, 
				String.valueOf(bc.getConcussionPlayerId())));
		}
		
		if(bc.getStatus() != null && bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {

			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT_AND_BOWLED: case CricketUtil.CAUGHT: case CricketUtil.BOWLED: 
			case CricketUtil.STUMPED: case CricketUtil.LBW: case CricketUtil.HIT_WICKET: case CricketUtil.MANKAD:
				bc.setHowOutBowler(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getHowOutBowlerId())));
				break;
			}
			
			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT: case CricketUtil.STUMPED: case CricketUtil.RUN_OUT:  
				bc.setHowOutFielder(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getHowOutFielderId())));
				break;
			}

			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT_AND_BOWLED:
				bc.setHowOutText("c & b " + bc.getHowOutBowler().getSurname());
				bc.setHowOutPartOne("c & b");
				bc.setHowOutPartTwo(bc.getHowOutBowler().getSurname());
				break;
			case CricketUtil.CAUGHT: case CricketUtil.MANKAD: case CricketUtil.RUN_OUT:
				switch (bc.getHowOut().toUpperCase()) {
				case CricketUtil.CAUGHT: 
					bc.setHowOutText("c " + bc.getHowOutFielder().getSurname());
					bc.setHowOutPartOne("c " + bc.getHowOutFielder().getSurname());
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						bc.setHowOutText(bc.getHowOutText() + " (SUB)");
						bc.setHowOutPartOne(bc.getHowOutPartOne() + " (SUB)");
					}
					bc.setHowOutText(bc.getHowOutText() + " b " + bc.getHowOutBowler().getSurname());
					bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getSurname());
					break;
				case CricketUtil.RUN_OUT:
					bc.setHowOutText("run out (" + bc.getHowOutFielder().getSurname() + ")");
					bc.setHowOutPartOne("run out");
					bc.setHowOutPartTwo(bc.getHowOutFielder().getSurname());
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						bc.setHowOutText(bc.getHowOutText() + " (SUB)");
						bc.setHowOutPartTwo(bc.getHowOutPartTwo() + " (SUB)");
					}
					break;
				case CricketUtil.MANKAD:
					bc.setHowOutText("run out (" + bc.getHowOutBowler().getSurname() + ")");
					bc.setHowOutPartOne("run out");
					bc.setHowOutPartTwo(bc.getHowOutBowler().getSurname());
					break;
				}
				break;
			case CricketUtil.BOWLED:
				bc.setHowOutText("b " + bc.getHowOutBowler().getSurname());
				bc.setHowOutPartOne("");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getSurname());
				break;
			case CricketUtil.STUMPED:
				bc.setHowOutText("st " + bc.getHowOutFielder().getSurname() + " b " + bc.getHowOutBowler().getSurname());
				bc.setHowOutPartOne("st " + bc.getHowOutFielder().getSurname());
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getSurname());
				break;
			case CricketUtil.LBW:
				bc.setHowOutText("lbw b " + bc.getHowOutBowler().getSurname());
				bc.setHowOutPartOne("lbw");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getSurname());
				break;
			case CricketUtil.HIT_WICKET:
				bc.setHowOutText("hit wicket b " + bc.getHowOutBowler().getSurname());
				bc.setHowOutPartOne("hit wicket");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getSurname());
				break;
			case CricketUtil.HANDLED_THE_BALL:
				bc.setHowOutText("handled the ball");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.HIT_BALL_TWICE:
				bc.setHowOutText("hit the ball twice");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.OBSTRUCTING_FIELDER:
				bc.setHowOutText("obstructing the field");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.TIME_OUT:
				bc.setHowOutText("timed out");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.RETIRED_HURT:
				bc.setHowOutText("retired hurt");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.RETIRED_OUT:
				bc.setHowOutText("retired out");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.ABSENT_HURT:
				bc.setHowOutText("absent hurt");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			case CricketUtil.CONCUSSED:
				bc.setHowOutText("concussed");
				bc.setHowOutPartOne(bc.getHowOutText());
				bc.setHowOutPartTwo("");
				break;
			}
			
		}
		return bc;
	}
	
	public static String OverBalls(int Overs,int Balls) {
		
		int TotalBalls=0, WholeOv, OddBalls;
		String Overs_text = "0.0" ;
		
		TotalBalls = 6 * Overs + Balls ;

		if(TotalBalls > 0) {
			WholeOv = ((TotalBalls)/6);
			OddBalls = (TotalBalls - 6 * (WholeOv));
			if(OddBalls == 0) {
				Overs_text = String.valueOf(WholeOv);
			} else {
				Overs_text = String.valueOf(WholeOv)+"."+String.valueOf(OddBalls);
			}
		}
		
		return Overs_text;
		
	}
	
	public static String processPowerPlay(String powerplay_return_type, Inning inning, int total_overs, int total_balls)
	{
	    int cuEcoent_over = total_overs;
	    if (total_balls > 0) {
	      cuEcoent_over += 1;
	    }
	    String return_pp_txt = "";
	    switch (powerplay_return_type)
	    {
	    case CricketUtil.FULL: 
	      return_pp_txt = "POWERPLAY ";
	      break;
	    case CricketUtil.SHORT: 
	      return_pp_txt = "PP";
	    case CricketUtil.MINI: 
		  return_pp_txt = "P";
	    }
	    
	    if((inning.getFirstPowerplayEndOver() >= cuEcoent_over)) {
	    	return_pp_txt = return_pp_txt + CricketUtil.ONE;
	    }else if ((inning.getSecondPowerplayEndOver() >= cuEcoent_over) || (inning.getSecondPowerplayStartOver() <= cuEcoent_over )) {
	    	return_pp_txt = return_pp_txt + CricketUtil.TWO;
	    }else if ((inning.getThirdPowerplayEndOver() >= cuEcoent_over) || (inning.getThirdPowerplayStartOver() <= cuEcoent_over )) {
	    	return_pp_txt = return_pp_txt + CricketUtil.THREE;
	    }
	    
	    return return_pp_txt;
	}
	
	public static String lastFewOversData(String whatToProcess, List<Event> events)
	  {
	    int count_lb = 0;
	    boolean exitLoop = false;
	    if ((events != null) && (events.size() > 0)) {
	      for (Event evnt : events)
	      {
	        if (((whatToProcess.equalsIgnoreCase(CricketUtil.BOUNDARY)) 
	        		&& (evnt.getEventType().equalsIgnoreCase(CricketUtil.SIX))) 
	        		|| (evnt.getEventType().equalsIgnoreCase(CricketUtil.FOUR))) {
	          break;
	        }
	        switch (evnt.getEventType()) {
	        case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.DOT: case CricketUtil.FIVE: case CricketUtil.BYE: 
	        case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET: 
	          count_lb += 1;
	          break;
	        case CricketUtil.LOG_ANY_BALL: 
	          if (((evnt.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) || (evnt.getEventRuns() == Integer.valueOf(CricketUtil.SIX))) 
	        		  && (evnt.getEventWasABoundary() != null) &&  (evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
	            exitLoop = true;
	          }
	          break;
	        }
	        if (exitLoop == true) {
	          break;
	        }
	      }
	    }
	    return String.valueOf(count_lb);
	}
	
	public static String compareInningData(Match match, String separator, int inning_number, List<Event> events) {
		
		int total_runs = 0, total_wickets = 0;
		
		if((events != null) && (events.size() > 0)) { 
			for(int i =0; i <= events.size() - 1 ; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					switch (events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			        case CricketUtil.FOUR: case CricketUtil.SIX: 
			        	total_runs += events.get(i).getEventRuns();
			          break;
			         
			        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			        	total_runs += events.get(i).getEventRuns();
			        	break;
			        	
			        case CricketUtil.LOG_WICKET:
			        	total_wickets += 1;
			        	break;
			        
			        case CricketUtil.LOG_ANY_BALL:
			        	total_runs += events.get(i).getEventRuns();
				          if (events.get(i).getEventExtra() != null) {
				        	 total_runs += events.get(i).getEventExtraRuns();
				          }
				          if (events.get(i).getEventSubExtra() != null) {
				        	 total_runs += events.get(i).getEventSubExtraRuns();
				          }
				          if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
				        	  total_wickets += 1;
				          }
				          break;
					}
					if(events.get(i).getEventOverNo() == match.getInning().get(1).getTotalOvers() && events.get(i).getEventBallNo() == match.getInning().get(1).getTotalBalls()) {
						return total_runs + separator + total_wickets;
					}
				}
			}
		}
		return "";
	}

	public static String getEventsText(String whatToProcess, String seperatorType, List<Event> events) 
	{
		int total_runs = 0;
		String this_over = "";String this_ball_data = "";
		if ((events != null) && (events.size() > 0)) {
		  for (Event evnt : events)
		  {
		    if ((whatToProcess.equalsIgnoreCase(CricketUtil.OVER)) && (evnt.getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER))) {
		      break;
		    }
		    this_ball_data = "";
		    switch (evnt.getEventType())
		    {
		    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		    case CricketUtil.FOUR: case CricketUtil.SIX: 
		      this_ball_data = String.valueOf(evnt.getEventRuns());
		      total_runs += evnt.getEventRuns();
		      break;
		    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		      this_ball_data = String.valueOf(evnt.getEventRuns()) + evnt.getEventType();
		      switch (evnt.getEventType())
		      {
		      case CricketUtil.WIDE: case CricketUtil.NO_BALL:
		        total_runs = total_runs + evnt.getEventRuns() + evnt.getEventExtraRuns() + evnt.getEventSubExtraRuns();
		      }
		      break;
		    case CricketUtil.LOG_WICKET: 
		      if (evnt.getEventRuns() > 0) {
		        this_ball_data = String.valueOf(evnt.getEventRuns()) + "+" + evnt.getEventType();
		      } else {
		        this_ball_data = evnt.getEventType();
		      }
		      total_runs = total_runs + evnt.getEventRuns() + evnt.getEventExtraRuns() + evnt.getEventSubExtraRuns();
		      break;
		    case CricketUtil.LOG_ANY_BALL:
		      if (evnt.getEventExtra() != null) {
		        this_ball_data = evnt.getEventExtra();
		      }
		      if (evnt.getEventSubExtra() != null)
		      {
		        if (this_ball_data.isEmpty()) {
		          this_ball_data = evnt.getEventSubExtra();
		        } else {
		          this_ball_data = this_ball_data + "+" + evnt.getEventSubExtra();
		        }
		        if (evnt.getEventExtraRuns() > 0) {
		          if (this_ball_data.isEmpty()) {
		            this_ball_data = String.valueOf(evnt.getEventExtraRuns());
		          } else {
		            this_ball_data = this_ball_data + String.valueOf(evnt.getEventExtraRuns());
		          }
		        }
		      }
		      if (evnt.getEventRuns() > 0) {
		        if (this_ball_data.isEmpty()) {
		          this_ball_data = String.valueOf(evnt.getEventRuns());
		        } else {
		          this_ball_data = this_ball_data + "+" + String.valueOf(evnt.getEventRuns());
		        }
		      }
		      if (evnt.getEventHowOut() != null && !evnt.getEventHowOut().isEmpty()) {
		        if (this_ball_data.isEmpty()) {
		          this_ball_data = "WICKET";
		        } else {
		          this_ball_data = this_ball_data + "+" + "WICKET";
		        }
		      }
		      total_runs = total_runs + evnt.getEventRuns() + evnt.getEventExtraRuns() + evnt.getEventSubExtraRuns();
		    }
		    if (!this_ball_data.isEmpty()) {
		      if (this_over.isEmpty()) {
		        this_over = this_ball_data;
		      } else {
		        this_over = this_over + seperatorType + this_ball_data;
		      }
		    }
		  }
		}
		this_over = this_over.replace("WIDE", "wd");
		this_over = this_over.replace("NO_BALL", "nb");
		this_over = this_over.replace("LEG_BYE", "lb");
		this_over = this_over.replace("BYE", "b");
		this_over = this_over.replace("PENALTY", "pen");
		this_over = this_over.replace("LOG_WICKET", "w");
		this_over = this_over.replace("WICKET", "w");
		
		return this_over;
	}
	
	public static String generateStrikeRate(int runs, int balls, int numberOfDecimals) {
		
		String strike_rate = "";
		if (balls > 0) {
			float sr_val = (100 / (float) balls) * (float) runs;
			switch (numberOfDecimals) {
			case 1:
				strike_rate = String.format("%.01f", sr_val);
				break;
			default:
				strike_rate = String.format("%.02f", sr_val);
				break;
			}
		}
		return strike_rate;
	}

	public static String generateRunRate(int runs, int overs, int balls, int numberOfDecimals) {
		
		String run_rate = "";
		int total_balls = (overs * 6) + balls;
		if (total_balls > 0) {
			float run_rate_val = ((float) runs / (float) total_balls) * 6;
			switch (numberOfDecimals) {
			case 1:
				run_rate = String.format("%.01f", run_rate_val);
				break;
			default:
				run_rate = String.format("%.02f", run_rate_val);
				break;
			}
		} else if (total_balls == 0) {
			switch (numberOfDecimals) {
			case 1:
				run_rate = String.format("%.01f", (float) total_balls);
				break;
			default:
				run_rate = String.format("%.02f", (float) total_balls);
				break;
			}
		} else if (balls < 0) {
			run_rate = "-";
		}
		return run_rate;
	}
	
	public static String generateTossResult(Match match,String TossType,String DecisionType, String teamNameType) {

		String TeamNameToUse="", decisionText = ""; 
		
		switch (match.getTossWinningDecision()) {
		case CricketUtil.BAT:
			decisionText = CricketUtil.BAT.toLowerCase();
			break;
		default:
			switch (DecisionType) {
			case CricketUtil.FIELD:
				decisionText = CricketUtil.FIELD.toLowerCase();
				break;
			default:
				decisionText = CricketUtil.BOWL;
				break;
			}
			break;
		}
		switch (teamNameType) {
		case CricketUtil.SHORT:
			if(match.getTossWinningTeam() == match.getHomeTeamId()) {
				TeamNameToUse = match.getHomeTeam().getShortname();
			} else {
				TeamNameToUse = match.getAwayTeam().getShortname();
			}
		    break;
		default:
			if(match.getTossWinningTeam() == match.getHomeTeamId()) {
				TeamNameToUse = match.getHomeTeam().getFullname();
			} else {
				TeamNameToUse = match.getAwayTeam().getFullname();
			}
		    break;
		}
		switch (TossType) {
		case CricketUtil.MINI:
			return CricketUtil.TOSS + ": " + TeamNameToUse;
		case CricketUtil.SHORT:
			return TeamNameToUse + " won the toss and " + decisionText;
		default:
			return TeamNameToUse + " won the toss and choose to " + decisionText;
		}
	}
	
	public static String Plural(int count){
		if (count == 1){
			return "";
		} else{
			return "s";
		}
	}

	public static int getTargetRuns(Match match) {
		
		int targetRuns = match.getInning().get(0).getTotalRuns() + 1;
		if(match.getTargetRuns() > 0) {
			targetRuns = match.getTargetRuns();
		}
		return targetRuns;
	}

	public static int getTargetOvers(Match match) {
		
		int targetOvers = match.getMaxOvers();
		if(match.getTargetOvers() > 0) {
			targetOvers = match.getTargetOvers();
		}
		return targetOvers;
	}

	public static int getRequiredRuns(Match match) {
		
		int requiredRuns = getTargetRuns(match) - match.getInning().get(1).getTotalRuns();
		if(requiredRuns <= 0) {
			requiredRuns = 0;
		}
		return requiredRuns;
	}

	public static int getRequiredBalls(Match match) {
		
		int requiredBalls = (getTargetOvers(match) * 6) - (match.getInning().get(1).getTotalOvers()) * 6 - match.getInning().get(1).getTotalBalls();
		if(requiredBalls <= 0) {
			requiredBalls = 0;
		}
		return requiredBalls;
	}

	public static int getWicketsLeft(Match match) {
		
		int wicketsLeft = 0;
		
		if(match.getMaxOvers() == 1) {
			wicketsLeft = 2 - (match.getInning().get(1).getTotalWickets()); 
		} else {
			wicketsLeft = 10 - (match.getInning().get(1).getTotalWickets()); 
		}
		
		if(wicketsLeft <= 0) {
			wicketsLeft = 0;
		}
		
		return wicketsLeft;
	}
	
	public static String generateMatchSummaryStatus(int whichInning, Match match, String teamNameType)
	  {
	    if (match.getMaxOvers() <= 0) {
	    	System.out.println("EROR: generateMatchSummaryStatus NOT available for test matches");
	    } else {
	      switch (whichInning) {
	      case 1: case 2: 
	        break;
	      default: 
	        System.out.println("EROR: Selected inning is wrong [" + whichInning + "]");
	        return null;
	      }
	    }
	    String teamNameToUse = "", bottomLineText = "";
	    if (CricketFunctions.getRequiredRuns(match) <= 0 || CricketFunctions.getRequiredRuns(match) > 0) {
	    	switch (teamNameType) {
		    case CricketUtil.SHORT: 
		    	teamNameToUse = (match.getInning().get(1)).getBatting_team().getShortname();
		      break;
		    default: 
		    	teamNameToUse = (match.getInning().get(1)).getBatting_team().getFullname();
		    	break;
		    }
	    }
	    else {
	    	switch (teamNameType) {
		    case CricketUtil.SHORT: 
		    	teamNameToUse = (match.getInning().get(0)).getBatting_team().getShortname();
		      break;
		    default: 
		    	teamNameToUse = (match.getInning().get(0)).getBatting_team().getFullname();
		    	break;
		    }
	    }
	    switch (whichInning) {
	    case 1: 
	    	if (((match.getInning().get(whichInning - 1)).getTotalRuns() > 0) || 
	  		      ((match.getInning().get(whichInning - 1)).getTotalOvers() > 0) || 
	  		      ((match.getInning().get(whichInning - 1)).getTotalBalls() > 0)) {
	  		      return "Current RunRate " + (match.getInning().get(0)).getRunRate();
	  		    }
	    	else {
	    		return CricketFunctions.generateTossResult(match, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL);
	    	}
	    case 2:
		    if ((CricketFunctions.getRequiredRuns(match) > 0) && (CricketFunctions.getRequiredBalls(match) > 0) && (CricketFunctions.getWicketsLeft(match) > 0))
		    {
		      switch (teamNameType)
		      {
		      case "SHORT": 
		        bottomLineText = teamNameToUse + " need " + CricketFunctions.getRequiredRuns(match) + " more run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " to win from ";
		        break;
		      default: 
		        bottomLineText = teamNameToUse + " need " + CricketFunctions.getRequiredRuns(match) + " more run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " to win from ";
		      }
		      if (CricketFunctions.getRequiredBalls(match) >= 150) {
		        bottomLineText = bottomLineText + CricketFunctions.OverBalls((match.getInning().get(1)).getTotalOvers(), (match.getInning().get(1)).getTotalBalls()) + " over";
		      } else {
		        bottomLineText = bottomLineText + CricketFunctions.getRequiredBalls(match) + " ball" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match));
		      }
		    }
		    else if (CricketFunctions.getRequiredRuns(match) <= 0)
		    {
		    	bottomLineText = teamNameToUse + " win by " + CricketFunctions.getWicketsLeft(match) + " wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match));
		    }
		    else if (CricketFunctions.getRequiredBalls(match) <= 0 || CricketFunctions.getWicketsLeft(match) <= 0)
		    {
		    	bottomLineText = teamNameToUse + " win by " + (CricketFunctions.getRequiredRuns(match) - 1) + " run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match) - 1);
		    }
	    }
		return bottomLineText;
		  
	}
	
	public static String getPowerPlayScore(Inning inning,int inn_number, String seperator,List<Event> events) {
		int total_run_PP=0, total_wickets_PP=0;
		if((events != null) && (events.size() > 0)) {
			for(int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inn_number) {
					int event_overs = ((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo());
					if(event_overs <= (inning.getFirstPowerplayEndOver() * 6)) {
						switch(events.get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_PP += events.get(i).getEventRuns();
							break;
						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_PP += events.get(i).getEventRuns();
							break;
						case CricketUtil.LOG_WICKET:
							total_wickets_PP += 1;
							break;
						case CricketUtil.LOG_ANY_BALL:
							total_run_PP += events.get(i).getEventRuns();
							if (events.get(i).getEventExtra() != null) {
								total_run_PP += events.get(i).getEventExtraRuns();
							}
							if (events.get(i).getEventSubExtra() != null) {
								total_run_PP += events.get(i).getEventSubExtraRuns();
							}
							if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
								total_wickets_PP += 1;
							}
							break;
						}
					}
				}
			}
		}
		return String.valueOf(total_run_PP) + seperator + String.valueOf(total_wickets_PP);
	}

	public static String processThisOverRunsCount(List<Event> events) {
		int total_runs=0;
		if((events != null) && (events.size() > 0)) {
			for(int i = 0; i <= events.size() - 1; i++) {
				if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER))) {
					break;
				}
				switch(events.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		        case CricketUtil.FOUR: case CricketUtil.SIX: 
		        	total_runs += events.get(i).getEventRuns();
		          break;
		          
		        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		        	total_runs += events.get(i).getEventRuns();
		        	break;
		        
		        case CricketUtil.LOG_ANY_BALL:
		        	total_runs += events.get(i).getEventRuns();
			          if (events.get(i).getEventExtra() != null) {
			        	 total_runs += events.get(i).getEventExtraRuns();
			          }
			          if (events.get(i).getEventSubExtra() != null) {
			        	 total_runs += events.get(i).getEventSubExtraRuns();
			          }
			          break;
				}
			}
		}
		return String.valueOf(total_runs);
	}

	public static String getLastWicket(Match match) {

		for(Inning inn : match.getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)){
				for(BattingCard bc : inn.getBattingCard()){
					if(inn.getFallsOfWickets().size() > 0){
						if(inn.getFallsOfWickets().get(inn.getFallsOfWickets().size() - 1).getFowPlayerID() == bc.getPlayerId()) {
							return bc.getPlayer().getFull_name() + " " + bc.getRuns() + "-" + bc.getBalls() + " (" + bc.getHowOutText() + ")" ; 
						}
					}								
				}
			}
		}
		return "";
		
	}

	public static String getScoreTypeData(String whatToProcess, Match match, int inning_number, int player_id, String seperator, List<Event> events) 
	{
		int dots = 0, ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0;
		boolean go_ahead = false;
		
		if((events != null) && (events.size() > 0)) {
			for (Event evnt : events) {
				if(evnt.getEventInningNumber() == inning_number) {
					go_ahead = false;
					switch (whatToProcess) {
					case CricketUtil.PLAYER:
						if(evnt.getEventBatterNo() == player_id) {
							go_ahead = true;
						}
						break;
					case CricketUtil.TEAM:
						go_ahead = true;
						break;
					}
					if(go_ahead == true) {
						switch (evnt.getEventType()) {
						case CricketUtil.ONE :
							ones++;
				          break;
				        case CricketUtil.TWO: 
				        	twos++;
				          break;
				        case CricketUtil.THREE: 
				        	threes++;
				        	break;
				        case CricketUtil.FOUR: 
				        	fours++;
				        	break;
				        case CricketUtil.FIVE: 
				        	fives++;
				        	break;
				        case CricketUtil.SIX: 
				        	sixes++;
				        	break;
				        case CricketUtil.DOT:  case CricketUtil.LOG_WICKET: 
				        	dots++;
				          break;
				        case CricketUtil.BYE: case CricketUtil.LEG_BYE:
				        	switch (whatToProcess) {
							case CricketUtil.PLAYER:
								dots++;
								break;
							}
				        	break;
				        
				        case CricketUtil.LOG_ANY_BALL:
							if(evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
								if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
									switch (whatToProcess) {
									case CricketUtil.PLAYER:
										dots++;
										break;
									}
								}
								if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) && (evnt.getEventWasABoundary() != null) && 
										(evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
									switch (whatToProcess) {
									case CricketUtil.PLAYER:
										fours++;
										break;
									}
						        }
								if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.SIX)) && (evnt.getEventWasABoundary() != null) && 
										(evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
									switch (whatToProcess) {
									case CricketUtil.PLAYER:
										sixes++;
										break;
									}
						        }
							}
				        	break;
						}
					}
				}
			}
		}
		return String.valueOf(dots) + seperator + String.valueOf(ones) + seperator + String.valueOf(twos) + seperator + String.valueOf(threes)
			+ seperator + String.valueOf(fours) + seperator + String.valueOf(fives) + seperator + String.valueOf(sixes);
	}
	
	public List<String> projectedScore(Match match, List<Integer> rates, String seperator) {

		List<String> proj_score = new ArrayList<String>();
		
		int remaining_balls_count = (match.getMaxOvers() * 6 - (match.getInning().get(0).getTotalOvers() * 6 + match.getInning().get(0).getTotalBalls()));
		int PS_Curr = (int) ((match.getInning().get(0).getTotalRuns() + remaining_balls_count * Double.valueOf((match.getInning().get(0).getRunRate()))) / 6);
		
		proj_score.add(match.getInning().get(0).getRunRate());
		proj_score.add(String.valueOf(PS_Curr));

		for(Integer rate : rates) {
			proj_score.add(rate + seperator + String.valueOf(((match.getInning().get(0).getTotalRuns()) + remaining_balls_count * rate)/6));
		}
		
		return proj_score ;
	}

	public static List<Player> getPlayersFromDB(CricketService cricketService, String whichTeamToProcess, Match match)
	{
		List<Player> players = new ArrayList<Player>(),whichTeamToCheck = new ArrayList<Player>();
		boolean player_found = false; 
		int whichTeamId = 0; 
		
		switch (whichTeamToProcess) {
		case CricketUtil.HOME:
			whichTeamId = match.getHomeTeamId();
			whichTeamToCheck = match.getHomeSquad();
			break;
		case CricketUtil.AWAY:
			whichTeamId = match.getAwayTeamId();
			whichTeamToCheck = match.getAwaySquad();
			break;
		}
		for(Player plyr : cricketService.getPlayers(CricketUtil.TEAM,String.valueOf(whichTeamId))) {
			player_found = false;
			for(Player subPlyr : whichTeamToCheck) {
				if (subPlyr.getPlayerId() == plyr.getPlayerId()) {
					player_found = true;
				}
			}
			if(player_found == false) {
				players.add(plyr);
			}
		}
		return players;
	}
	
}
