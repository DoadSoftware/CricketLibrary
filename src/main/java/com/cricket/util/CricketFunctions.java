package com.cricket.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Dictionary;
import com.cricket.model.Event;
import com.cricket.model.Fixture;
import com.cricket.model.ForeignLanguageData;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.MatchClock;
import com.cricket.model.MultiLanguageDatabase;
import com.cricket.model.OverByOverData;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Season;
import com.cricket.model.Speed;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;

public class CricketFunctions {
	
	public static String deletePreview() throws IOException
    {
		if(new File(CricketUtil.PREVIEW).exists()) {
			Files.delete(Paths.get(CricketUtil.PREVIEW));
		}
        return "";
    }
	
	public static String checkImpactPlayer(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if(player_id == events.get(i).getEventBatterNo() && events.get(i).getSubstitutionMade() != null) {
					return "YES";
				}
			}
		}
		return "";
	}
	public static String checkImpactPlayerBowler(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if(player_id == events.get(i).getEventBowlerNo() && events.get(i).getSubstitutionMade() != null) {
					return "YES";
				}
			}
		}
		return "";
	}
	public static String printInitials(String name)
    {
        if (name.length() > 0) {
        	 return Character.toUpperCase(name.split("_")[0].charAt(0)) + "" 
        		+ Character.toUpperCase(name.split("_")[1].charAt(0));
        }
        return "";
    }
	
	public static String getInteractive(Match match) throws IOException {

		if(match.getGenerateInteractiveFile() == null 
				|| match.getGenerateInteractiveFile().equalsIgnoreCase(CricketUtil.NO)) {
			return "";
		}
		if(match.getEvents() == null || match.getEvents().size() <= 0) {
			return "";
		}
		
		String this_ball_data = "", Bowler = "", Batsman = "", OtherBatsman = "", 
				over_number = "", over_ball = "", inning_number = "",batsman_style = "",
				bowler_handed = "",this_over = "",this_over_run = "",shot = "-",wagonX = "0", wagonY = "0";
		int j = 0;
	      
		String line_txt = String.format("%-140s", "");
		String txt = String.format("%-140s", "");
		
		txt = addSubString(txt,"============================================================================================================================================================" + "\n\n",0);
		txt = addSubString(txt,"# File Version 1.5" + "\n\n",0);
		txt = addSubString(txt,"# Match between " + match.getHomeTeam().getTeamName1() + " and " + match.getAwayTeam().getTeamName1() + "\n\n",0);
		txt = addSubString(txt,"# DOAD Interactive File generated on " + LocalDate.now() + " at " + LocalTime.now() + "\n\n",0);
		txt = addSubString(txt,"============================================================================================================================================================" + "\n\n",0);

		if(Files.exists(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT))) {
			Files.delete(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT));
		}
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT), 
				Arrays.asList(txt), StandardOpenOption.CREATE);
	
		line_txt = addSubString(line_txt,"Inns",2);
		line_txt = addSubString(line_txt,"Batsman",8);
		line_txt = addSubString(line_txt,"Bowler",34);
		line_txt = addSubString(line_txt,"Over",58);
		line_txt = addSubString(line_txt,"Ball",64);
		line_txt = addSubString(line_txt,"Runs",72);
		line_txt = addSubString(line_txt,"WagonX",80);
		line_txt = addSubString(line_txt,"WagonY",87);
		line_txt = addSubString(line_txt,"Wkt?",94);
		line_txt = addSubString(line_txt,"LH/RH",100);
		line_txt = addSubString(line_txt,"Shot",108);
		line_txt = addSubString(line_txt,"Hgt",114);
		line_txt = addSubString(line_txt,"LandX",119);
		line_txt = addSubString(line_txt,"LandY",125);
		line_txt = addSubString(line_txt,"Other Batsman",135);
		line_txt = addSubString(line_txt,"T/Ov",157);
		line_txt = addSubString(line_txt,"6D",162);
		line_txt = addSubString(line_txt,"Shot",166);
		line_txt = addSubString(line_txt,"Spin  N W T/O-2",173);

		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT), 
				Arrays.asList(line_txt), StandardOpenOption.APPEND);
		
		int max_inn = 2;
		if(match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			max_inn = 4;
		}
		
	    for (int i = 0; i <=  match.getEvents().size() - 1; i++)
	    {
		  if(match.getEvents().get(i).getEventInningNumber() >= 1 && match.getEvents().get(i).getEventInningNumber() <= max_inn) {
			if(match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER") || 
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER")||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN) ||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
				
			}else {
				line_txt = String.format("%-140s", "");
				j = j + 1;
				for(Inning inn : match.getInning()) {
					for(Player hs : match.getHomeSquad()) {
						if(match.getEvents().get(i).getEventBatterNo() == hs.getPlayerId()) {
							if(hs.getBattingStyle() == null) {
								batsman_style = "";
							}else {
								batsman_style = hs.getBattingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
						if(match.getEvents().get(i).getEventBowlerNo() == hs.getPlayerId()) {
							if(hs.getBowlingStyle() == null) {
								bowler_handed = "";
							}else {
								bowler_handed = hs.getBowlingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
					}
					for(Player as : match.getAwaySquad()) {
						if(match.getEvents().get(i).getEventBatterNo() == as.getPlayerId()) {
							if(as.getBattingStyle() == null) {
								batsman_style = "";
							}else {
								batsman_style = as.getBattingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
						if(match.getEvents().get(i).getEventBowlerNo() == as.getPlayerId()) {
							if(as.getBowlingStyle() == null) {
								bowler_handed = "";
							}else {
								bowler_handed = as.getBowlingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
					}
	    			for(BattingCard bc : inn.getBattingCard()) {
	    				if(match.getEvents().get(i).getEventBatterNo() == bc.getPlayerId()) {
	    					Batsman = bc.getPlayer().getTicker_name();
	    				}
	    				if(match.getEvents().get(i).getEventOtherBatterNo() == bc.getPlayerId()) {
	    					OtherBatsman = bc.getPlayer().getTicker_name();
	    				}
	    			}
	    			if(inn.getBowlingCard() != null) {
	    				for(BowlingCard boc : inn.getBowlingCard()) {
				    		if(match.getEvents().get(i).getEventBowlerNo() == boc.getPlayerId()) {
				    			Bowler = boc.getPlayer().getTicker_name();
					    	}
				    	}
	    			}
			    }
				this_ball_data = "";
				inning_number = String.valueOf(match.getEvents().get(i).getEventInningNumber());
				if(match.getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
					over_number = String.valueOf(match.getEvents().get(i).getEventOverNo() + 1);
					over_ball = String.valueOf(match.getEvents().get(i).getEventBallNo());
				}else {
					over_number = getOvers(match.getEvents().get(i).getEventOverNo(), match.getEvents().get(i).getEventBallNo());
					over_ball = getBalls(match.getEvents().get(i).getEventOverNo(), match.getEvents().get(i).getEventBallNo());
				}
				
				
				line_txt = addSubString(line_txt,inning_number,2);
	    		
	    		line_txt = addSubString(line_txt,Batsman,8);
				
				line_txt = addSubString(line_txt,Bowler,34);
	    		
	    		line_txt = addSubString(line_txt,over_number,63 - over_number.length());
	    		
	    		line_txt = addSubString(line_txt,over_ball,67 - over_ball.length());
	    		
	    		if(match.getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
	    			this_over = match.getEvents().get(i).getEventExtra();
	    		}else {
	    			this_over = match.getEvents().get(i).getEventType();
	    		}
	    		
	    		if(!this_over.trim().isEmpty()) {
	    			
					this_over = this_over.replace("WIDE", "WD");
					this_over = this_over.replace("NO_BALL", "NB");
					this_over = this_over.replace("LEG_BYE", "LB");
					this_over = this_over.replace("BYE", "B");
					this_over = this_over.replace("PENALTY", "PN");
					this_over = this_over.replace("LOG_WICKET", "W");
					this_over = this_over.replace("WICKET", "W");
				}	
			}
			
    		
			switch (match.getEvents().get(i).getEventType().toUpperCase()){
		    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		    case CricketUtil.FOUR: case CricketUtil.SIX:
		    	this_ball_data = String.valueOf(match.getEvents().get(i).getEventRuns());
		    	this_over_run = this_ball_data;
		    	line_txt = addSubString(line_txt,this_ball_data,74);
		    	
		      break;
		    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		    	this_ball_data = String.valueOf(match.getEvents().get(i).getEventRuns() + match.getEvents().get(i).getEventSubExtraRuns()) + this_over;
		    	this_over_run = this_ball_data;
		    	line_txt = addSubString(line_txt,"0",74);
		      break;
		    case CricketUtil.LOG_WICKET:
		    	line_txt = addSubString(line_txt,String.valueOf(match.getEvents().get(i).getEventRuns()),74);
	    		 
		      if (match.getEvents().get(i).getEventRuns() > 0) {
		    	  this_over_run = String.valueOf(match.getEvents().get(i).getEventRuns()) + this_over;
	    		 
		        this_ball_data = String.valueOf(match.getEvents().get(i).getEventRuns()) + this_over;
		      } else {
		    	  this_over_run = this_over;
	    		  
		        this_ball_data = this_over;
		      }
		    
		      break;
		    case CricketUtil.LOG_ANY_BALL:
		    	if (match.getEvents().get(i).getEventExtra() != null) {
		    		if(match.getEvents().get(i).getEventSubExtra() != null && match.getEvents().get(i).getEventSubExtraRuns() > 0) {
		    			if(match.getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				int runs = (match.getEvents().get(i).getEventRuns() + match.getEvents().get(i).getEventExtraRuns() + 
		    						match.getEvents().get(i).getEventSubExtraRuns());
		    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
	    					
		    				this_ball_data = String.valueOf(runs);
		    			}
		    		}
		    		if(this_ball_data.isEmpty()) {
		    			if(match.getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		    				if(match.getEvents().get(i).getEventRuns()>0) {
		    					this_over_run =  match.getEvents().get(i).getEventRuns() + this_over;
		    					line_txt = addSubString(line_txt,"0",74);
	    						
			    				this_ball_data = match.getEvents().get(i).getEventExtra() + match.getEvents().get(i).getEventRuns();
			    			}else {
			    				this_over_run = this_over;
			    				line_txt = addSubString(line_txt,"0",74);
		    					
			    				this_ball_data = match.getEvents().get(i).getEventExtra();
			    			}
		    			}else {
		    				if(match.getEvents().get(i).getEventRuns()>0) {
		    					line_txt = addSubString(line_txt,String.valueOf(match.getEvents().get(i).getEventRuns()),74);
			    				this_ball_data = String.valueOf(match.getEvents().get(i).getEventRuns());
			    			}
		    			}
		    		}else {
		    			this_over_run = this_over;
		    			line_txt = addSubString(line_txt,"0",74);
		    			this_ball_data = this_ball_data + match.getEvents().get(i).getEventExtra();
		    		}
		    	}
			    if (match.getEvents().get(i).getEventSubExtra() != null && match.getEvents().get(i).getEventSubExtraRuns() > 0){
			    	if(!match.getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
			    		if (this_ball_data.isEmpty()) {
			    			this_over_run = match.getEvents().get(i).getEventSubExtra().charAt(0) + String.valueOf(match.getEvents().get(i).getEventSubExtraRuns());
			    			line_txt = addSubString(line_txt,"0",74);
				          this_ball_data = String.valueOf(match.getEvents().get(i).getEventSubExtraRuns()) + match.getEvents().get(i).getEventSubExtra();
			        	
				        } else {
				        	this_over_run = this_over_run + "+" + String.valueOf(match.getEvents().get(i).getEventSubExtraRuns()) + match.getEvents().get(i).getEventSubExtra().charAt(0);
				        	line_txt = addSubString(line_txt,"0",74);
				          this_ball_data = this_ball_data + "+" + String.valueOf(match.getEvents().get(i).getEventSubExtraRuns()) + match.getEvents().get(i).getEventSubExtra();
				        }
			    	} 
			    }
		      if (match.getEvents().get(i).getEventHowOut() != null && !match.getEvents().get(i).getEventHowOut().isEmpty()) {
		        if (this_ball_data.isEmpty()) {
		          this_ball_data = CricketUtil.WICKET.charAt(0) + "";
		          line_txt = addSubString(line_txt,this_ball_data,74);
	        	  
		        } else {
		          this_ball_data = this_ball_data + "+" + CricketUtil.WICKET.charAt(0) + "";
		          line_txt = addSubString(line_txt,this_ball_data,74);
		        }
		      }
		      break;
			}
			
			if(match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER")  || 
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER")||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
					match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
				
			}else {
				
					for(int k = i+1; k < match.getEvents().size(); k++){
						switch (match.getEvents().get(k).getEventType().toUpperCase()){
						case CricketUtil.WAGON:
							if(match.getEvents().get(i).getEventInningNumber() == match.getEvents().get(k).getEventInningNumber()) {
								if(match.getEvents().get(i).getEventOverNo() == match.getEvents().get(k).getEventOverNo()) {
									if(match.getEvents().get(i).getEventBallNo() == match.getEvents().get(k).getEventBallNo()) {
										wagonX = String.valueOf(match.getEvents().get(k).getEventDescription()).split(",")[0];
										wagonY = String.valueOf(match.getEvents().get(k).getEventDescription()).split(",")[1];
									}
								}
							}
							
							break;
						case CricketUtil.SHOT:
							if(match.getEvents().get(i).getEventInningNumber() == match.getEvents().get(k).getEventInningNumber()) {
								if(match.getEvents().get(i).getEventOverNo() == match.getEvents().get(k).getEventOverNo()) {
									if(match.getEvents().get(i).getEventBallNo() == match.getEvents().get(k).getEventBallNo()) {
										if (match.getEvents().get(k).getEventDescription().contains("no_shot")) {
											shot = "N";
										}else if(match.getEvents().get(k).getEventDescription().contains("defence") || match.getEvents().get(k).getEventDescription().contains("nudge") ||
												match.getEvents().get(k).getEventDescription().contains("off_drive") || match.getEvents().get(k).getEventDescription().contains("on_drive") ||
												match.getEvents().get(k).getEventDescription().contains("straight_drive")) {
											 shot = "E";
										}else if(match.getEvents().get(k).getEventDescription().contains("front") || match.getEvents().get(k).getEventDescription().contains("back")) {
											 shot = "P";
										}else {
											shot = "M";
										}
										
										if (match.getEvents().get(k).getEventDescription().contains("no_shot")) {
											shot = shot + "N";
										}else if(match.getEvents().get(k).getEventDescription().contains("defence")) {
											shot = shot + "D";
										}else {
											shot = shot + "A";
										}
									}
								}
							}
							break;
						}
					}
				 line_txt = addSubString(line_txt,wagonX,84);
				 line_txt = addSubString(line_txt,wagonY,90);
				 
				if(match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.LOG_WICKET)) {
			    	line_txt = addSubString(line_txt,"Y",95);
		    		
			    }else {
			    	line_txt = addSubString(line_txt,"N",95);	
			    }
				
				line_txt = addSubString(line_txt,batsman_style,102);
				
				line_txt = addSubString(line_txt,shot,109);
				line_txt = addSubString(line_txt,"0",115);
				line_txt = addSubString(line_txt,"0",121);
				line_txt = addSubString(line_txt,"0",127);
				line_txt = addSubString(line_txt,bowler_handed,129);
				line_txt = addSubString(line_txt,OtherBatsman,131);
				line_txt = addSubString(line_txt,this_over_run,157);
			}
			
		}
		if(match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER")  || 
			match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER") ||
			match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)||
			match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
			match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
			match.getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
		}else {
			Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT), 
					Arrays.asList(line_txt), StandardOpenOption.APPEND);
		}
	  }
		return this_ball_data;
	}
	
	public static String addSubString(String main_string,String sub_string, int position) {
	    StringBuilder sb = new StringBuilder(main_string);
		    sb.insert(position, sub_string);
	    return sb.toString();
	}
	
	public static ForeignLanguageData generateMatchResultForeignLanguage(Match match, String teamNameType, 
			MultiLanguageDatabase multiLanguageDb)
	{
		List<ForeignLanguageData> resultToShow = new ArrayList<ForeignLanguageData>();
		List<String> insertTxt = new ArrayList<String>();
		
		if(match.getMatchResult() != null) {
			if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
					|| match.getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
				if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
						&& !match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
					resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
							CricketUtil.MATCH + " " + CricketUtil.TIED, "", null, 1, resultToShow);
				} else {
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								CricketUtil.MATCH + " " + CricketUtil.DRAWN, "", null, 1, resultToShow);
					} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								CricketUtil.MATCH + " " + CricketUtil.ABANDONED, "", null, 1, resultToShow);
					}
				}
			} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
						CricketUtil.NO_RESULT.replace("_", " "), "", null, 1, resultToShow);
			} else {
				if(match.getMatchResult().contains(",")) {
					if(Integer.valueOf(match.getMatchResult().split(",")[0]) == match.getHomeTeamId()) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.TEAM, teamNameType, multiLanguageDb, 
								match.getHomeTeam().getTeamName1(), "", null, 1, resultToShow);
					} else {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.TEAM, teamNameType, multiLanguageDb, 
								match.getAwayTeam().getTeamName1(), "", null, 1, resultToShow);
					}
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								"WIN THE SUPER OVER", "", null, resultToShow.size() + 1, resultToShow);
					} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
							&& match.getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						insertTxt.add(match.getMatchResult().split(",")[1]);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WON BY AN INNINGS AND RUN" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1])), 
								"", insertTxt, resultToShow.size() + 1, resultToShow);
					} else if (match.getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, match.getMatchResult().split(",")[1], 
								"", null, resultToShow.size() + 1, resultToShow);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WIN BY RUN" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1])), 
								"", null, resultToShow.size() + 1, resultToShow);
					} else if (match.getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, match.getMatchResult().split(",")[1], 
								"", null, resultToShow.size() + 1, resultToShow);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WIN BY WICKET" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1])), 
								"", null, resultToShow.size() + 1, resultToShow);
					}
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.DLS)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, " (" + CricketUtil.DLS + ")", 
								"", null, resultToShow.size() + 1, resultToShow);
					}else if(match.getMatchResult().toUpperCase().contains(CricketUtil.VJD)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, " (" + CricketUtil.VJD + ")", 
								"", null, resultToShow.size() + 1, resultToShow);
					}
				}
			}
		}
		if(resultToShow.size() > 0) {
			return MergeForeignLanguageDataListToSingleObject(resultToShow);
		} else {
			return null;
		}
	}	
	
	public static ForeignLanguageData generateBattingCardForeignLanguage(String whichdata1,String howOut1,String howOut2,String howOut3, 
			MultiLanguageDatabase multiLanguageDb)
	{
		List<ForeignLanguageData> resultToShow = new ArrayList<ForeignLanguageData>();
		
		if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.CAUGHT)){
			String englishTxt = "";
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut1.split(" ")[0], 
					"", null, resultToShow.size() + 1, resultToShow);
			
			Player plyr = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut1.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyr != null) {
				englishTxt = plyr.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
			
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut2.split(" ")[0], 
					"", null, resultToShow.size() + 1, resultToShow);
			
			Player plyrs = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut2.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyrs != null) {
				englishTxt = plyrs.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
			
		}else if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)){
			String englishTxt = "";
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut1, 
					"", null, resultToShow.size() + 1, resultToShow);
			Player plyr = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut2.equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyr != null) {
				englishTxt = plyr.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
		}else if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.BOWLED)){
			String englishTxt = "";
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut2.split(" ")[0], 
					"", null, resultToShow.size() + 1, resultToShow);
			Player plyr = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut2.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyr != null) {
				englishTxt = plyr.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
		}else if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.RUN_OUT) || whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.MANKAD)){
			if(!howOut3.isEmpty() && howOut3.trim() != null ) {
				String englishTxt = "";
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
						howOut1, "", null, resultToShow.size() + 1, resultToShow);
				
				resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "(", 
						"", null, resultToShow.size() + 1, resultToShow);
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
						"sub", "", null, resultToShow.size() + 1, resultToShow);
				resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "-", 
						"", null, resultToShow.size() + 1, resultToShow);
				Player plyr = multiLanguageDb.getPlayers().stream()
						.filter(player -> howOut2.split(" ")[0].equalsIgnoreCase(player.getTicker_name()))
						.findAny().orElse(null);
				if(plyr != null) {
					englishTxt = plyr.getFull_name();
				}
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
						englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
				
				resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, ")", 
						"", null, resultToShow.size() + 1, resultToShow);
			}else {
				String englishTxt = "";
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
						howOut1, "", null, resultToShow.size() + 1, resultToShow);
				
				resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "(", 
						"", null, resultToShow.size() + 1, resultToShow);
				
				Player plyr = multiLanguageDb.getPlayers().stream()
						.filter(player -> howOut2.equalsIgnoreCase(player.getTicker_name()))
						.findAny().orElse(null);
				if(plyr != null) {
					englishTxt = plyr.getFull_name();
				}
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
						englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
				
				resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, ")", 
						"", null, resultToShow.size() + 1, resultToShow);
			}
			
		}
		
		if(resultToShow.size() > 0) {
			return MergeForeignLanguageDataListToSingleObject(resultToShow);
		} else {
			return null;
		}
	}
	
	public static void DoadWriteSameCommandToEachViz(String SendTextIn, List<PrintWriter> print_writers, Configuration config) 
	{
		String which_language = "";
		for(int i = 0; i < print_writers.size(); i++) {

			switch (i) {
			case 0:
				which_language = config.getPrimaryLanguage();
				break;
			case 1:
				which_language = config.getSecondaryLanguage();
				break;
			case 2:
				which_language = config.getTertiaryLanguage();
				break;
			}
			if(which_language.equalsIgnoreCase("ENGLISH")) {
				if(SendTextIn.contains("$Language$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language1$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("HINDI")) {
				if(SendTextIn.contains("$Language$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language3$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("TAMIL")) {
				if(SendTextIn.contains("$Language$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language6$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("TELUGU")) {
				if(SendTextIn.contains("$Language$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language7$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}
		}
		for(int i = 0; i < print_writers.size(); i++) {
			print_writers.get(i).println("-1 " + SendTextIn + "\0");
		}
	}
	public static void DoadWriteVariousLanguageTextToEachViz(String SendTextIn, Configuration config, String broadcaster, 
			List<PrintWriter> print_writers,List<ForeignLanguageData> foreignLanguageData) 
	{
		String which_language = "";
		for(int i = 0; i < print_writers.size(); i++) {

			switch (i) {
			case 0:
				which_language = config.getPrimaryLanguage();
				break;
			case 1:
				which_language = config.getSecondaryLanguage();
				break;
			case 2:
				which_language = config.getTertiaryLanguage();
				break;
			}
			if(which_language.equalsIgnoreCase("ENGLISH")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language1$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getEnglishText() + "\0");
			}else if(which_language.equalsIgnoreCase("HINDI")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language3$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getHindiText() + "\0");
			}else if(which_language.equalsIgnoreCase("TAMIL")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language6$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getTamilText() + "\0");
			}else if(which_language.equalsIgnoreCase("TELUGU")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$Language$", "$Language7$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getTeluguText() + "\0");
			}
		}
	}			
	public static ForeignLanguageData MergeForeignLanguageDataListToSingleObject(List<ForeignLanguageData> foreignLanguageDataList) {
		
		ForeignLanguageData this_fd = new ForeignLanguageData();
		this_fd.setEnglishText(foreignLanguageDataList.get(0).getEnglishText().trim());
		this_fd.setHindiText(foreignLanguageDataList.get(0).getHindiText().trim());
		this_fd.setTamilText(foreignLanguageDataList.get(0).getTamilText().trim());
		this_fd.setTeluguText(foreignLanguageDataList.get(0).getTeluguText().trim());
		for(int fd = 1; fd <= foreignLanguageDataList.size() - 1; fd++) {
			this_fd.setEnglishText(this_fd.getEnglishText().trim() + " " + foreignLanguageDataList.get(fd).getEnglishText().trim()); // India<b>win by 23 runs
			this_fd.setHindiText(this_fd.getHindiText().trim() + " " + foreignLanguageDataList.get(fd).getHindiText().trim());
			this_fd.setTamilText(this_fd.getTamilText().trim() + " " + foreignLanguageDataList.get(fd).getTamilText().trim());
			this_fd.setTeluguText(this_fd.getTeluguText().trim() + " " + foreignLanguageDataList.get(fd).getTeluguText().trim());
		}
		return this_fd;
	}
	public static List<ForeignLanguageData> AssembleMultiLanguageData(String whichTableInDb, String whichDBColumnToProcess,  
			MultiLanguageDatabase multiLanguage, String foreignTextToProcess, String WhatTypeOfTextToReturn, List<String> InsertText,
			int ForeignLanguageArrayIndex, List<ForeignLanguageData> foreignLanguageDataList)
	{
		String englishTxt = "", hindiTxt = "", tamilTxt = "", teluguTxt = "";
		
		switch (whichTableInDb) {
		case CricketUtil.PLAYER:
			
			Player plyr = multiLanguage.getPlayers().stream()
				.filter(player -> foreignTextToProcess.equalsIgnoreCase(player.getFull_name()))
				.findAny().orElse(null);
			
			if(plyr != null) {
				
				switch(whichDBColumnToProcess) {
				case CricketUtil.FULLNAME:
			   	 	englishTxt = foreignTextToProcess;
		        	hindiTxt = plyr.getHindifull_name();
		        	tamilTxt = plyr.getTamilfull_name();
		        	teluguTxt = plyr.getTelugufull_name();

		        	switch(WhatTypeOfTextToReturn) {
					case CricketUtil.FIRSTNAME:
			        	if(englishTxt.contains(" ")) {
							englishTxt = englishTxt.split(" ")[0];
					    }
						if(hindiTxt.contains(" ")) {
							hindiTxt = hindiTxt.split(" ")[0];
					    }
						if(tamilTxt.contains(" ")) {
							tamilTxt = tamilTxt.split(" ")[0];
					    }
						if(teluguTxt.contains(" ")) {
							teluguTxt = teluguTxt.split(" ")[0];
					    }
	  	                break;
					case CricketUtil.LASTNAME:
			        	if(englishTxt.contains(" ")) {
							englishTxt = englishTxt.split(" ")[1];
					    }
						if(hindiTxt.contains(" ")) {
							hindiTxt = hindiTxt.split(" ")[1];
					    }
						if(tamilTxt.contains(" ")) {
							tamilTxt = tamilTxt.split(" ")[1];
					    }
						if(teluguTxt.contains(" ")) {
							teluguTxt = teluguTxt.split(" ")[1];
					    }
	  	                break;
					case CricketUtil.SURNAME:
				   	 	englishTxt = plyr.getSurname();
				   	 	hindiTxt = plyr.getHindi_surname();
				   	 	tamilTxt = plyr.getTamil_surname();
				   	 	teluguTxt = plyr.getTelugu_surname();
		                break;
			         }
		        	break;
				
				case CricketUtil.SURNAME:

					if(foreignTextToProcess.equalsIgnoreCase(plyr.getSurname())) {
				   	 	englishTxt = foreignTextToProcess;
			        	hindiTxt = plyr.getHindi_surname();
			        	tamilTxt = plyr.getTamil_surname();
			        	teluguTxt = plyr.getTelugu_surname();
					}
					break;
				
				default:

					if(foreignTextToProcess.equalsIgnoreCase(plyr.getFull_name())) {
				   	 	englishTxt = foreignTextToProcess;
			        	hindiTxt = plyr.getHindifull_name();
			        	tamilTxt = plyr.getTamilfull_name();
			        	teluguTxt = plyr.getTelugufull_name();
					}else if(foreignTextToProcess.equalsIgnoreCase(plyr.getSurname())) {
				   	 	englishTxt = foreignTextToProcess;
			        	hindiTxt = plyr.getHindi_surname();
			        	tamilTxt = plyr.getTamil_surname();
			        	teluguTxt = plyr.getTelugu_surname();
					}
					break;
				}			
			}			
			break;

		case CricketUtil.TEAM:

			Team tm = multiLanguage.getTeam().stream()
				.filter(team -> foreignTextToProcess.equalsIgnoreCase(team.getTeamName1()))
				.findAny().orElse(null);
	
			if(tm != null) {		
				
				switch(whichDBColumnToProcess) {
				case CricketUtil.TEAMNAME_2: case CricketUtil.TEAMNAME_3: case CricketUtil.TEAMNAME_4:
	                switch (whichDBColumnToProcess) {
					case CricketUtil.TEAMNAME_2: 
	            		englishTxt = tm.getTeamName2();
					case CricketUtil.TEAMNAME_3: 
	            		englishTxt = tm.getTeamName3();
					case CricketUtil.TEAMNAME_4:
	            		englishTxt = tm.getTeamName4();
					}
					hindiTxt = tm.getShortHindiTeamName();
					tamilTxt = tm.getShortTamilTeamName();
					teluguTxt = tm.getShortTeluguTeamName();
					break;
				default:
            		englishTxt = tm.getTeamName1();
					hindiTxt = tm.getFullHindiTeamName();
					tamilTxt = tm.getFullTamilTeamName();
					teluguTxt = tm.getFullTeluguTeamName();
					break;
				}
			}

			break;
			
		case CricketUtil.DICTIONARY: 

			Dictionary dict = multiLanguage.getDictionary().stream()
				.filter(dictionary -> foreignTextToProcess.equalsIgnoreCase(dictionary.getEnglishSentence()))
				.findAny().orElse(null);
			
			if(dict != null) {	
				
				englishTxt = dict.getEnglishSentence();
				hindiTxt = dict.getHindiSentence();
				tamilTxt = dict.getTamilSentence();
				teluguTxt = dict.getTeluguSentence();
				
				if(InsertText != null) {
					if (InsertText.size() >= 1) {
						englishTxt = englishTxt.replace(dict.getEnglishSentence(), 
								InsertText.get(0) + " " + dict.getEnglishSentence());
						hindiTxt = hindiTxt.replace(dict.getInsertBeforeFirstHindiText(), 
								InsertText.get(0) + " " + dict.getInsertBeforeFirstHindiText());
						tamilTxt = tamilTxt.replace(dict.getInsertBeforeFirstTamilText(), 
								InsertText.get(0) + " " + dict.getInsertBeforeFirstTamilText());
						teluguTxt = teluguTxt.replace(dict.getInsertBeforeFirstTeluguText(), 
								InsertText.get(0) + " " + dict.getInsertBeforeFirstTeluguText());
					}
					if (InsertText.size() >= 2) {
						englishTxt = englishTxt.replace(dict.getEnglishSentence(), 
								InsertText.get(1) + " " + dict.getEnglishSentence());
						hindiTxt = hindiTxt.replace(dict.getInsertBeforeSecondHindiText(), 
								InsertText.get(1) + " " + dict.getInsertBeforeSecondHindiText());
						tamilTxt = tamilTxt.replace(dict.getInsertBeforeSecondTamilText(), 
								InsertText.get(1) + " " + dict.getInsertBeforeSecondTamilText());
						teluguTxt = teluguTxt.replace(dict.getInsertBeforeSecondTeluguText(), 
								InsertText.get(1) + " " + dict.getInsertBeforeSecondTeluguText());
					}
				}
				
			}
			
			break;
		default:
			englishTxt = foreignTextToProcess;
			hindiTxt = foreignTextToProcess;
			tamilTxt = foreignTextToProcess;
			teluguTxt = foreignTextToProcess;
			break;
			
		}

		ForeignLanguageData foreignLanguageData = new ForeignLanguageData();

		foreignLanguageData.setEnglishText(englishTxt); // win by 23 runs 
		foreignLanguageData.setHindiText(hindiTxt);
		foreignLanguageData.setTamilText(tamilTxt);
		foreignLanguageData.setTeluguText(teluguTxt);

		if (ForeignLanguageArrayIndex > 0) { // 2

			if(ForeignLanguageArrayIndex == 1) {
				foreignLanguageDataList = new ArrayList<ForeignLanguageData>();
			}
        	foreignLanguageDataList.add(ForeignLanguageArrayIndex-1,foreignLanguageData); // India, win by 23 runs

		} else {
			
			foreignLanguageDataList = new ArrayList<ForeignLanguageData>();
        	foreignLanguageDataList.add(foreignLanguageData);
        	
		}
		
		return foreignLanguageDataList;
	}
				  
	@SuppressWarnings("resource")
	public static List<PrintWriter> processPrintWriter(Configuration config) throws UnknownHostException, IOException
	{
		List<PrintWriter> print_writer = new ArrayList<PrintWriter>();
		
		if(config.getPrimaryIpAddress() != null && !config.getPrimaryIpAddress().isEmpty()) {
			if(!config.getPrimaryLanguage().equalsIgnoreCase("ENGLISH")) {
				print_writer.add(new PrintWriter(new OutputStreamWriter(new Socket(config.getPrimaryIpAddress(), 
						config.getPrimaryPortNumber()).getOutputStream(), StandardCharsets.UTF_8),true));
			}else {
				print_writer.add(new PrintWriter(new Socket(config.getPrimaryIpAddress(), 
						config.getPrimaryPortNumber()).getOutputStream(), true));
			}
		}
		
		if(config.getSecondaryIpAddress() != null && !config.getSecondaryIpAddress().isEmpty()) {
			if(!config.getSecondaryLanguage().equalsIgnoreCase("ENGLISH")) {
				print_writer.add(new PrintWriter(new OutputStreamWriter(new Socket(config.getSecondaryIpAddress(), 
						config.getSecondaryPortNumber()).getOutputStream(), StandardCharsets.UTF_8),true));
			}else {
				print_writer.add(new PrintWriter(new Socket(config.getSecondaryIpAddress(), 
						config.getSecondaryPortNumber()).getOutputStream(), true));
			}
		}
		
		if(config.getTertiaryIpAddress() != null && !config.getTertiaryIpAddress().isEmpty()) {
			if(!config.getTertiaryLanguage().equalsIgnoreCase("ENGLISH")) {
				print_writer.add(new PrintWriter(new OutputStreamWriter(new Socket(config.getTertiaryIpAddress(), 
						config.getTertiaryPortNumber()).getOutputStream(), StandardCharsets.UTF_8),true));
			}else {
				print_writer.add(new PrintWriter(new Socket(config.getTertiaryIpAddress(), 
						config.getTertiaryPortNumber()).getOutputStream(), true));
			}
		}
		return print_writer;
	}

	public static MatchClock getmatchClock(Match match) throws IOException, JAXBException 
	{
		if(match.getInning() != null && match.getInning().size() > 0) {
			if(new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.CLOCK_XML).exists()) {
				MatchClock clock = (MatchClock) JAXBContext.newInstance(MatchClock.class).createUnmarshaller().unmarshal(
						new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CLOCK_XML));
				for(Inning inn : match.getInning()) {
					if(inn.getIsCurrentInning() != null && inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
						if(inn != null && clock != null && inn.getInningNumber() == clock.getInningNumber()) {
							return clock;
						}
					}
				}
			}
		}
		return null;
	}
	
	public static Path findUsingNIOApi(String sdir) throws IOException {
	    Path dir = Paths.get(sdir);
	    if (Files.isDirectory(dir)) {
	        Optional<Path> opPath = Files.list(dir)
	          .filter(p -> !Files.isDirectory(p))
	          .sorted((p1, p2)-> Long.valueOf(p2.toFile().lastModified())
	            .compareTo(p1.toFile().lastModified()))
	          .findFirst();

	        if (opPath.isPresent()){
	            return opPath.get();
	        }
	    }

	    return null;
	}
	public static Speed getCurrentSpeed(String speedFilePath, Speed lastSpeed) throws IOException {
		Speed speed_to_return = new Speed();
		File speed_file = new File(speedFilePath);
		if(speed_file.exists()) {
			if(lastSpeed.getSpeedFileModifiedTime() != speed_file.lastModified()) {
	        	for(String str_line : Files.readAllLines(Paths.get(speedFilePath), StandardCharsets.UTF_8)) {
					speed_to_return.setSpeedValue(str_line);
					speed_to_return.setSpeedFileModifiedTime(speed_file.lastModified());
					return speed_to_return;
	        	}
			}
		}
		return null;
	}
	
	public static Speed saveCurrentSpeed(String broadcaster, String speedSourcePath, 
			String speedDestinationPath, Speed lastSpeed) throws IOException 
	{
		Speed speed_to_return = new Speed();
		BufferedWriter writer;

		switch (broadcaster) {
		case CricketUtil.HAWKEYE:

			File this_dir = new File(speedSourcePath);
			
		    if (this_dir.isDirectory()) {
		    	
		        Optional<File> opFile = Arrays.stream(this_dir.listFiles(File::isFile))
		          .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
		        
		        if (opFile.isPresent()){
		        	if(lastSpeed.getSpeedFileModifiedTime() != opFile.get().lastModified()) {
			        	for(String str_line : Files.readAllLines(Paths.get(
			        			speedSourcePath + opFile.get().getName()), StandardCharsets.UTF_8)) {
			        		if(str_line.contains(",")) {
								speed_to_return.setSpeedValue(str_line.split(",")[1]);
								speed_to_return.setSpeedFileModifiedTime(opFile.get().lastModified());
								writer = new BufferedWriter(new FileWriter(speedDestinationPath));
							    writer.write(str_line.split(",")[1].trim());
							    writer.close();							
								return speed_to_return;
			        		}
			        	}
		        	}
		        }
		    }
			break;
		}
		return null;
	}
	
	public static BowlingCard getCurrentInningCurrentBowler(Match match) {
		BowlingCard current_bowler = null;
		for(Inning inn : match.getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					if(boc.getStatus().toUpperCase().equalsIgnoreCase("CURRENTBOWLER") 
							|| boc.getStatus().toUpperCase().equalsIgnoreCase("LASTBOWLER")) {
						current_bowler = boc;
					}
				}
			}
		}
		return current_bowler;
	}
	
	public static List<Fixture> processAllFixtures(CricketService cricketService) {
		List<Fixture> fixtures = cricketService.getFixtures();
		for(Team tm : cricketService.getTeams()) {
			for(Fixture fix : fixtures) {
				if(fix.getHometeamid() == tm.getTeamId()) {
					fix.setHome_Team(tm);
				}
				if(fix.getAwayteamid() == tm.getTeamId()) {
					fix.setAway_Team(tm);
				}
			}
		}
		return fixtures;
	}
	
	public static String whenWriteStringUsingBufferedWritter_thenCorrect(String str) 
			  throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Temp/LogFile.txt"));
			    writer.write(str);
			    
			    writer.close();
		return "";
	}
	
	public static BestStats getProcessedBatsmanBestStats(BestStats best_stats) {
		
		int equation = best_stats.getBestEquation();
		
		if(equation % 2 > 0) {
			best_stats.setNot_out(true);
			equation = equation - 1;
		} else {
			best_stats.setNot_out(false);
		}
		
		best_stats.setRuns(equation / 2);
		
		return best_stats;
	}

	public static BestStats getProcessedBowlerBestStats(BestStats best_stats) {
		
		best_stats.setRuns(1000 - (best_stats.getBestEquation() % 1000));
		if(best_stats.getBestEquation() % 1000 > 0) {
			best_stats.setWickets(best_stats.getBestEquation() / 1000 + 1);
		} else {
			best_stats.setWickets(best_stats.getBestEquation() / 1000);
		}

		return best_stats;
	
	}
	
	public static List<Match> getTournamentMatches(File[] files, CricketService cricketService) 
			throws IllegalAccessException, InvocationTargetException, JAXBException
	{
		List<Match> tournament_matches = new ArrayList<Match>();
		for(File file : files) {
			tournament_matches.add(CricketFunctions.populateMatchVariables(cricketService, (Match) 
				JAXBContext.newInstance(Match.class).createUnmarshaller().unmarshal(
				new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + file.getName()))));
			//System.out.println(CricketUtil.CRICKET_SERVER_DIRECTORY);
		}
		return tournament_matches;
	}
	public static String gettournamentFoursAndSixes(List<Match> tournament_matches,Match currentMatch) 
	{
		int Four = 0, Six = 0;
		for(Match match : tournament_matches) {
			if(!match.getMatchFileName().equalsIgnoreCase(currentMatch.getMatchFileName())) {
				if(match.getMatchType().equalsIgnoreCase(currentMatch.getMatchType())) {
					for(Inning inn : match.getInning()) {
						Four = Four + inn.getTotalFours();
						Six = Six + inn.getTotalSixes();
					}
				}
			}else {
				for(Inning inn : currentMatch.getInning()) {
					Four = Four + inn.getTotalFours();
					Six = Six + inn.getTotalSixes();
				}
			}
		}
		
		return Four + "," + Six;
	}

	public static Statistics updateTournamentDataWithStats(Statistics stat,List<Match> tournament_matches,Match currentMatch) 
	{
		boolean player_found = false;
		for(Match match : tournament_matches) {
			if(!match.getMatchFileName().equalsIgnoreCase(currentMatch.getMatchFileName())) {
				if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getMatchType())) {
					for(Inning inn : match.getInning()) {
						for(BattingCard bc : inn.getBattingCard()) {
							if(bc.getPlayerId() == stat.getPlayer_id()) {
								player_found = true;
								if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
									stat.setInnings(stat.getInnings() + 1);
								}
								stat.setRuns(stat.getRuns() + bc.getRuns());
								stat.setFours(stat.getFours() + bc.getFours());
								stat.setSixes(stat.getSixes() + bc.getSixes());
								stat.setBalls_faced(stat.getBalls_faced() + bc.getBalls());
								
								if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
									stat.setThirties(stat.getThirties() + 1);
								}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
									stat.setFifties(stat.getFifties() + 1);
								}else if(bc.getRuns() >= 100){
									stat.setHundreds(stat.getHundreds() + 1);
								}
								
							}
						}
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId() == stat.getPlayer_id()) {
									stat.setWickets(stat.getWickets() + boc.getWickets());
									stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
									stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*6 + boc.getBalls()));
									stat.setDotbowled(stat.getDotbowled() + boc.getDots());
									if(boc.getWickets() < 5 && boc.getWickets() >= 3) {
										stat.setPlus_3(stat.getPlus_3() + 1);
									}	
									else if(boc.getWickets() >= 5){
										stat.setPlus_5(stat.getPlus_5() + 1);
									}
								}
							}							
						}
					}
					player_found = false;
					for(Player hs : match.getHomeSquad()) {
						if(stat.getPlayer_id() == hs.getPlayerId()) {
							player_found = true;
						}
					}
					for(Player as : match.getAwaySquad()) {
						if(stat.getPlayer_id() == as.getPlayerId()) {
							player_found = true;
						}
					}
					if(player_found == true){
						stat.setMatches(stat.getMatches() + 1);
					}
				}
			}
		}
		return stat;
	}
	
	public static Statistics updateStatisticsWithMatchData(Statistics stat, Match match)
	{
		boolean player_found = false;
//		stat.setMatches(stat.getMatches());
		if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getMatchType())) {
			stat.setTournament_fours(stat.getTournament_fours() + match.getInning().get(0).getTotalFours());
			stat.setTournament_fours(stat.getTournament_fours() + match.getInning().get(1).getTotalFours());
			for(Inning inn : match.getInning()) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getPlayerId() == stat.getPlayer_id()) {
						player_found = true;
						if(bc.getBatsmanInningStarted() == null) {
						}
						else if(bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
							stat.setInnings(stat.getInnings() + 1);
						}
						
						stat.setRuns(stat.getRuns() + bc.getRuns());
						stat.setFours(stat.getFours() + bc.getFours());
						stat.setSixes(stat.getSixes() + bc.getSixes());
						stat.setBalls_faced(stat.getBalls_faced() + bc.getBalls());
				
						if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(bc.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
					}
				}
				if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getPlayerId() == stat.getPlayer_id()) {
							player_found = true;
							stat.setWickets(stat.getWickets() + boc.getWickets());
							stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
							stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*6 + boc.getBalls()));
							stat.setDotbowled(stat.getDotbowled() + boc.getDots());
							//System.out.println(boc.getWickets());
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								stat.setPlus_3(stat.getPlus_3() + 1);
							}else if(boc.getWickets() >= 5){
								stat.setPlus_5(stat.getPlus_5() + 1);
							}
						}
					}							
				}
			}
			player_found = false;
			for(Player hs : match.getHomeSquad()) {
				if(stat.getPlayer_id() == hs.getPlayerId()) {
					player_found = true;
				}
			}
			for(Player as : match.getAwaySquad()) {
				if(stat.getPlayer_id() == as.getPlayerId()) {
					player_found = true;
				}
			}
			if(player_found == true){
				stat.setMatches(stat.getMatches() + 1);
			}
		}
		return stat;
	}
	
	public static List<Tournament> extractTournamentStats(String typeOfExtraction, List<Match> tournament_matches, 
			CricketService cricketService,Match currentMatch, List<Tournament> past_tournament_stats) 
	{		
		int playerId = -1;
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
		boolean has_match_started = false;
		
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			
			 return extractTournamentStats("CURRENT_MATCH_DATA", tournament_matches, cricketService, currentMatch, 
					extractTournamentStats("PAST_MATCHES_DATA", tournament_matches, cricketService, currentMatch, null));
			
		case "PAST_MATCHES_DATA":
			
			for(Match mtch : tournament_matches) {
				if(!mtch.getMatchFileName().equalsIgnoreCase(currentMatch.getMatchFileName())) {
					
					has_match_started = false;
					
					if(mtch.getMatchType().equalsIgnoreCase(currentMatch.getMatchType())) {
						if(mtch.getInning().get(0).getTotalRuns() > 0 || (6 * mtch.getInning().get(0).getTotalOvers() + mtch.getInning().get(0).getTotalBalls()) > 0) {
							has_match_started = true;
						}
						for(Inning inn : mtch.getInning())
						{
							if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0) {
								has_match_started = true;
								System.out.println("match file : " + mtch.getMatchFileName() + " - " + has_match_started);
								System.out.println("Runs :" + inn.getTotalRuns());
							}
							
							if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
								
								for(BattingCard bc : inn.getBattingCard())
								{
									playerId = -1;
									for(int i=0; i<=tournament_stats.size() - 1;i++)
									{
										if(bc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
											playerId = i;
											break;
										}
									}
									if(playerId >= 0) {
										
										tournament_stats.get(playerId).setRuns(tournament_stats.get(playerId).getRuns() + bc.getRuns()); // existing record
										tournament_stats.get(playerId).setBallsFaced(tournament_stats.get(playerId).getBallsFaced() + bc.getBalls());
										tournament_stats.get(playerId).setFours(tournament_stats.get(playerId).getFours() + bc.getFours());
										tournament_stats.get(playerId).setSixes(tournament_stats.get(playerId).getSixes() + bc.getSixes());
										
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
													bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
											
										}else {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
													bc.getPlayerId(), bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
										}
										
									}else {
										tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, bc.getBalls(), 
												0,bc.getStatus(), bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
													new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(),
													inn.getBowling_team(), bc.getPlayer()));
											
										}else {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
													new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), bc.getBalls(),
													inn.getBowling_team(), bc.getPlayer()));
										}

									}	
								}
							}
							
							if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
								
								for(BowlingCard boc : inn.getBowlingCard())
								{
									playerId = -1;
									for(int i=0; i<=tournament_stats.size() - 1;i++)
									{
										if(boc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
											playerId = i;
											break;
										}
									}
									
									if(playerId >= 0) {
										
										tournament_stats.get(playerId).setWickets(tournament_stats.get(playerId).getWickets() + boc.getWickets());
										tournament_stats.get(playerId).setRunsConceded(tournament_stats.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
										tournament_stats.get(playerId).setDots(tournament_stats.get(playerId).getDots() + boc.getDots());
										tournament_stats.get(playerId).setBallsBowled(tournament_stats.get(playerId).getBallsBowled() + 
												6 * boc.getOvers() + boc.getBalls());

										tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
												inn.getBatting_team(), boc.getPlayer()));
										
									}else {
										
										tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
												boc.getDots(),null, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										
										tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(
												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
												inn.getBatting_team(), boc.getPlayer()));
																				
									}
								}
							}
						}
						
						if(has_match_started == true) {
							for(Tournament trmnt : tournament_stats) {
								for(Player plyr : mtch.getHomeSquad()) {
									if(plyr.getPlayerId() == trmnt.getPlayerId()) {
										trmnt.setMatches(trmnt.getMatches() + 1);
									}
								}
								for(Player plyr : mtch.getAwaySquad()) {
									if(plyr.getPlayerId() == trmnt.getPlayerId()) {
										trmnt.setMatches(trmnt.getMatches() + 1);
									}
								}
							}
						}
					}
				}
			}
			
			return tournament_stats;
			
		case "CURRENT_MATCH_DATA":
			
			has_match_started = false;
			if(currentMatch.getMatchType().equalsIgnoreCase(currentMatch.getMatchType())) {
				if(currentMatch.getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getInning().get(0).getTotalOvers() + currentMatch.getInning().get(0).getTotalBalls()) > 0) {
					has_match_started = true;
				}
				
				for(Inning inn : currentMatch.getInning())
				{
					if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0) {
						has_match_started = true;
					}

					for(BattingCard bc : inn.getBattingCard())
					{
						playerId = -1;
						for(int i=0; i<=past_tournament_stats.size() - 1;i++)
						{
							if(bc.getPlayerId() == past_tournament_stats.get(i).getPlayerId()) {
								playerId = i;
								break;
							}
						}
						
						if(playerId >= 0) {
							past_tournament_stats.get(playerId).setRuns(past_tournament_stats.get(playerId).getRuns() + bc.getRuns()); // existing record
							past_tournament_stats.get(playerId).setBallsFaced(past_tournament_stats.get(playerId).getBallsFaced() + bc.getBalls());
							past_tournament_stats.get(playerId).setFours(past_tournament_stats.get(playerId).getFours() + bc.getFours());
							past_tournament_stats.get(playerId).setSixes(past_tournament_stats.get(playerId).getSixes() + bc.getSixes());
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
										bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
								
							}else {
								past_tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
										bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
							}
							
						}else {
							past_tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, bc.getBalls(), 
									0,bc.getStatus(), bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>()));
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stats.get(past_tournament_stats.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
								
							}else {
								past_tournament_stats.get(past_tournament_stats.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
							}
						}	
					}

					if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
						for(BowlingCard boc : inn.getBowlingCard())
						{
							playerId = -1;
							for(int i=0; i<=past_tournament_stats.size() - 1;i++)
							{
								if(boc.getPlayerId() == past_tournament_stats.get(i).getPlayer().getPlayerId()) {
									playerId = i;
									break;
								}
							}
							if(playerId >= 0) {
								
								past_tournament_stats.get(playerId).setRunsConceded(past_tournament_stats.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
								past_tournament_stats.get(playerId).setWickets(past_tournament_stats.get(playerId).getWickets() + boc.getWickets());
								past_tournament_stats.get(playerId).setDots(past_tournament_stats.get(playerId).getDots() + boc.getDots());
								past_tournament_stats.get(playerId).setBallsBowled(past_tournament_stats.get(playerId).getBallsBowled() + 
										6 * boc.getOvers() + boc.getBalls());

								past_tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
										boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
										inn.getBatting_team(), boc.getPlayer()));
	
							}else {
								
								past_tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
										boc.getDots(),null, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
								
								past_tournament_stats.get(past_tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
										(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
										inn.getBatting_team(), boc.getPlayer()));
								
							}
						}
					}
					
				}
				
				if(has_match_started == true) {
					for(Tournament trmnt : past_tournament_stats) {
						for(Player plyr : currentMatch.getHomeSquad()) {
							if(trmnt.getPlayerId() == plyr.getPlayerId()) {
								trmnt.setMatches(trmnt.getMatches() + 1);
							}
						}
						for(Player plyr : currentMatch.getAwaySquad()) {
							if(trmnt.getPlayerId() == plyr.getPlayerId()) {
								trmnt.setMatches(trmnt.getMatches() + 1);
							}
						}
					}
				}
				
			}
			
			return past_tournament_stats;
		}
		return null;
	}
	
	public static String generateMatchResult(Match match, String teamNameType)
	{
		String resultToShow = "";
		if(match.getMatchResult() != null) {
			if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
					|| match.getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
				if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
						&& !match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
					resultToShow = CricketUtil.MATCH.toLowerCase() + " " + CricketUtil.TIED.toLowerCase();
				} else {
					resultToShow = CricketUtil.MATCH.toLowerCase();
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
						resultToShow = resultToShow + " " + CricketUtil.DRAWN.toLowerCase();
					} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
						resultToShow = resultToShow + " " + CricketUtil.ABANDONED.toLowerCase();
					}
				}
			} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
				resultToShow = CricketUtil.NO_RESULT.toLowerCase().replace("_", " ");
			} else {
				if(match.getMatchResult().contains(",")) {
					switch (teamNameType) {
					case CricketUtil.SHORT:
						if(Integer.valueOf(match.getMatchResult().split(",")[0]) == match.getHomeTeamId()) {
							resultToShow = match.getHomeTeam().getTeamName3();
						} else {
							resultToShow = match.getAwayTeam().getTeamName3();
						}
					    break;
					default:
						if(Integer.valueOf(match.getMatchResult().split(",")[0]) == match.getHomeTeamId()) {
							resultToShow = match.getHomeTeam().getTeamName1();
						} else {
							resultToShow = match.getAwayTeam().getTeamName1();
						}
					    break;
					}
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
						resultToShow = resultToShow + " win by super over";
					} else if(match.getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
							&& match.getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = resultToShow + " win by an inning and " + Integer.valueOf(match.getMatchResult().split(",")[1]) 
							+ " run" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1]));
					} else if (match.getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatchResult().split(",")[1]) 
							+ " run" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1]));
					} else if (match.getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatchResult().split(",")[1]) 
							+ " wicket" + Plural(Integer.valueOf(match.getMatchResult().split(",")[1]));
					}
					if(match.getMatchResult().toUpperCase().contains(CricketUtil.DLS)) {
						resultToShow = resultToShow + " (" + CricketUtil.DLS + ")";
					}else if(match.getMatchResult().toUpperCase().contains(CricketUtil.VJD)) {
						resultToShow = resultToShow + " (" + CricketUtil.VJD + ")";
					}
				}
			}
		}
		return resultToShow;
	}
	
	public static String getOnlineCurrentDate() throws MalformedURLException, IOException
	{
		HttpURLConnection httpCon = (HttpURLConnection) new URL("https://mail.google.com/").openConnection();
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(httpCon.getDate()));
	}	

	public static class PlayerBestStatsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats bs1, BestStats bs2) {
	       return Integer.compare(bs2.getBestEquation(), bs1.getBestEquation());
	    }
	}
	
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
	
	public static class BatsmenMostRunComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	    	if(bc2.getRuns() == bc1.getRuns()) {
	    		return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    	}else {
	    		return Integer.compare(bc2.getRuns(), bc1.getRuns());
	    	}
	    }
	}
	
	public static class BowlerWicketsComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	    	if(bc2.getWickets() == bc1.getWickets()) {
	    		return Integer.compare(bc2.getBowlerEconomySortData(), bc1.getBowlerEconomySortData());
	    	}else {
	    		return Integer.compare(bc2.getBowlerFigureSortData(), bc1.getBowlerFigureSortData());
	    	}
	       
	    }
	}
	
	public static class BatsmanFoursComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	       return Integer.compare(bc2.getBatsmanFoursSortData(), bc1.getBatsmanFoursSortData());
	    }
	}
	
	public static class BatsmanSixesComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	       return Integer.compare(bc2.getBatsmanSixesSortData(), bc1.getBatsmanSixesSortData());
	    }
	}
	
	public static Player populatePlayer(CricketService cricketService, Player player, Match match)
	{
		Player this_plyr = new Player();
		this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(player.getPlayerId()));
		if(this_plyr != null) {
			this_plyr.setPlayerPosition(player.getPlayerPosition()); this_plyr.setCaptainWicketKeeper(player.getCaptainWicketKeeper());
			if(match.getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
				this_plyr.setPhoto("");
			}
		}
		return this_plyr;
	}
	public static Match populateMatchVariables(CricketService cricketService,Match match) throws IllegalAccessException, InvocationTargetException 
	{
		List<Player> players = new ArrayList<Player>();
		
		for(Player plyr:match.getHomeSquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.setHomeSquad(players);

		players = new ArrayList<Player>();
		for(Player plyr:match.getHomeSubstitutes()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.setHomeSubstitutes(players);
		
		players = new ArrayList<Player>();
		if(match.getHomeOtherSquad() != null) {
			for(Player plyr:match.getHomeOtherSquad()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
		}
		match.setHomeOtherSquad(players);
		
		players = new ArrayList<Player>();
		for(Player plyr:match.getAwaySquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.setAwaySquad(players);

		players = new ArrayList<Player>();
		for(Player plyr:match.getAwaySubstitutes()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.setAwaySubstitutes(players);
		
		players = new ArrayList<Player>();
		if(match.getAwayOtherSquad() != null) {
			for(Player plyr:match.getAwayOtherSquad()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
		}
		match.setAwayOtherSquad(players);
		
		if(match.getHomeTeamId() > 0)
			match.setHomeTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getHomeTeamId())));
		if(match.getAwayTeamId() > 0)
			match.setAwayTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getAwayTeamId())));
		if(match.getGroundId() > 0) {
			match.setGround(cricketService.getGround(match.getGroundId()));
			if(match.getGround() != null) {
				match.setVenueName(match.getGround().getFullname());
			}
		}
		
		for(Inning inn:match.getInning()) {
			
			inn.setBatting_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBattingTeamId())));
			inn.setBowling_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBowlingTeamId())));
			
			if(inn.getBattingCard() != null)
				for(BattingCard batc:inn.getBattingCard()) 
					batc = processBattingcard(cricketService,batc);

			if(inn.getPartnerships() != null)
				for(Partnership part:inn.getPartnerships()) {
					part.setFirstPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(part.getFirstBatterNo())));
					part.setSecondPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(part.getSecondBatterNo())));
				}
			
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
				bc.setHowOutText("c & b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartOne("c & b");
				bc.setHowOutPartTwo(bc.getHowOutBowler().getTicker_name());
				break;
			case CricketUtil.CAUGHT: case CricketUtil.MANKAD: case CricketUtil.RUN_OUT:
				switch (bc.getHowOut().toUpperCase()) {
				case CricketUtil.CAUGHT: 
					bc.setHowOutText("c " + bc.getHowOutFielder().getTicker_name());
					bc.setHowOutPartOne("c " + bc.getHowOutFielder().getTicker_name());
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						bc.setHowOutText(bc.getHowOutText() + " (SUB)");
						bc.setHowOutPartOne(bc.getHowOutPartOne() + " (SUB)");
					}
					bc.setHowOutText(bc.getHowOutText() + " b " + bc.getHowOutBowler().getTicker_name());
					bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
					break;
				case CricketUtil.RUN_OUT:
					bc.setHowOutText("run out (" + bc.getHowOutFielder().getTicker_name() + ")");
					bc.setHowOutPartOne("run out");
					bc.setHowOutPartTwo(bc.getHowOutFielder().getTicker_name());
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						bc.setHowOutText(bc.getHowOutText() + " (SUB)");
						bc.setHowOutPartTwo(bc.getHowOutPartTwo() + " (SUB)");
					}
					break;
				case CricketUtil.MANKAD:
					bc.setHowOutText("run out (" + bc.getHowOutBowler().getTicker_name() + ")");
					bc.setHowOutPartOne("run out");
					bc.setHowOutPartTwo(bc.getHowOutBowler().getTicker_name());
					break;
				}
				break;
			case CricketUtil.BOWLED:
				bc.setHowOutText("b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartOne("");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
				break;
			case CricketUtil.STUMPED:
				bc.setHowOutText("st " + bc.getHowOutFielder().getTicker_name() + " b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartOne("st " + bc.getHowOutFielder().getTicker_name());
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
				break;
			case CricketUtil.LBW:
				bc.setHowOutText("lbw b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartOne("lbw");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
				break;
			case CricketUtil.HIT_WICKET:
				bc.setHowOutText("hit wicket b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartOne("hit wicket");
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
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
			case CricketUtil.TIMED_OUT:
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
	
	public static String getBalls(int Overs,int Balls) {
		String Overs_text = "" ;
		switch(Balls) {
		case 0:
			Overs_text = "6";
			return Overs_text;
		default:
//			System.out.println(String.valueOf(Overs + 1) + "," + String.valueOf(Balls));
			Overs_text = String.valueOf(Balls);
			return Overs_text;
		}
	}
	
	public static String getOvers(int Overs,int Balls) {
		String Overs_text = "" ;
		switch(Balls) {
		case 0:
			Overs_text = String.valueOf(Overs);
			return Overs_text;
		default:
//			System.out.println(String.valueOf(Overs + 1) + "," + String.valueOf(Balls));
			Overs_text = String.valueOf(Overs + 1);
			return Overs_text;
		}
	}
	
	public static List<Tournament> extractSeasonStats(String typeOfExtraction, List<Match> tournament_matches, 
			CricketService cricketService,Match currentMatch, List<Tournament> past_tournament_stats,List<Season> ses) 
	{
		int playerId = -1;
		int seasonID = 0;
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
		boolean has_match_started = false;
		
		switch(typeOfExtraction) {
		case "SEASON1": case "SEASON2": case "SEASON3":
			if(typeOfExtraction.equalsIgnoreCase("SEASON1")) {
				seasonID = 1;
//				for(Season seas : ses) {
//					if(seas.getSeasonDescription().equalsIgnoreCase(typeOfExtraction)) {
//						seasonID = seas.getSeasonId();
//					}
//				}
				
			}else if(typeOfExtraction.equalsIgnoreCase("SEASON2")) {
				seasonID = 2;
//				for(Season seas : ses) {
//					if(seas.getSeasonDescription().equalsIgnoreCase(typeOfExtraction)) {
//						seasonID = seas.getSeasonId();
//					}
//				}
			}else if(typeOfExtraction.equalsIgnoreCase("SEASON3")) {
				seasonID = 3;
//				for(Season seas : ses) {
//					if(seas.getSeasonDescription().equalsIgnoreCase(typeOfExtraction)) {
//						seasonID = seas.getSeasonId();
//					}
//				}
			}
			for(Match mtch : tournament_matches) {
				if(seasonID == mtch.getSeasonId()) {
					if(mtch.getInning().get(0).getTotalRuns() > 0 || (6 * mtch.getInning().get(0).getTotalOvers() + mtch.getInning().get(0).getTotalBalls()) > 0) {
						has_match_started = true;
					}
					for(Inning inn : mtch.getInning())
					{
						if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
							
							for(BattingCard bc : inn.getBattingCard())
							{
								playerId = -1;
								for(int i=0; i<=tournament_stats.size() - 1;i++)
								{
									if(bc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
										playerId = i;
										break;
									}
								}
								if(playerId >= 0) {
									
									tournament_stats.get(playerId).setRuns(tournament_stats.get(playerId).getRuns() + bc.getRuns()); // existing record
									tournament_stats.get(playerId).setBallsFaced(tournament_stats.get(playerId).getBallsFaced() + bc.getBalls());
									tournament_stats.get(playerId).setFours(tournament_stats.get(playerId).getFours() + bc.getFours());
									tournament_stats.get(playerId).setSixes(tournament_stats.get(playerId).getSixes() + bc.getSixes());
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
												bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
										
									}else {
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
												bc.getPlayerId(), bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(), bc.getPlayer()));
									}
									
								}else {
									tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, bc.getBalls(), 
											0,bc.getStatus(), bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
												new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(),
												inn.getBowling_team(), bc.getPlayer()));
										
									}else {
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
												new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), bc.getBalls(),
												inn.getBowling_team(), bc.getPlayer()));
									}

								}	
							}
						}
						
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
							
							for(BowlingCard boc : inn.getBowlingCard())
							{
								playerId = -1;
								for(int i=0; i<=tournament_stats.size() - 1;i++)
								{
									if(boc.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
										playerId = i;
										break;
									}
								}
								
								if(playerId >= 0) {
									
									tournament_stats.get(playerId).setWickets(tournament_stats.get(playerId).getWickets() + boc.getWickets());
									tournament_stats.get(playerId).setRunsConceded(tournament_stats.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
									tournament_stats.get(playerId).setBallsBowled(tournament_stats.get(playerId).getBallsBowled() + 
											6 * boc.getOvers() + boc.getBalls());

									tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
											boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
											inn.getBatting_team(), boc.getPlayer()));
									
								}else {
									
									tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
											boc.getDots(),null, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									
									tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(
											boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
											inn.getBatting_team(), boc.getPlayer()));
																			
								}
							}
						}
					}
					if(has_match_started == true) {
						for(Tournament trmnt : tournament_stats) {
							for(Player plyr : mtch.getHomeSquad()) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									trmnt.setMatches(trmnt.getMatches() + 1);
								}
							}
							for(Player plyr : mtch.getAwaySquad()) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									trmnt.setMatches(trmnt.getMatches() + 1);
								}
							}
						}
					}
				}
			}
			break;
		}
//		System.out.println("tourdata = " + tournament_stats);
		return tournament_stats;
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
	
	public static List<String> getSplit(int inning_number, int splitvalue, Match match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					//System.out.println("Inn Number" + inning_number);
					int max_balls = (match.getMaxOvers() * 6);
					int count_balls = ((match.getInning().get(inning_number-1).getTotalOvers() * 6) + match.getInning().get(inning_number-1).getTotalBalls());
					
					switch (events.get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE: case CricketUtil.SIX: 
					case CricketUtil.LEG_BYE: case CricketUtil.BYE: case CricketUtil.LOG_WICKET:
						total_balls = total_balls + 1 ;
						total_runs = total_runs + events.get(i).getEventRuns();
						break;
					
					case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
						total_runs = total_runs + events.get(i).getEventRuns();
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
					
					if(count_balls <= max_balls && total_runs >= splitvalue) {
						
						Balls.add(String.valueOf(total_balls));
						total_runs = total_runs - splitvalue;
						total_balls = 0;
						
						continue;
					}
				}
			}
		}
		return Balls ;
	}
	
	public static List<String> getPlayerSplit(int inning_number,int playerId ,int splitvalue, Match match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					if(playerId == events.get(i).getEventBatterNo()) {
						//System.out.println("Inn Number" + inning_number);
						int max_balls = (match.getMaxOvers() * 6);
						int count_balls = ((match.getInning().get(inning_number-1).getTotalOvers() * 6) + match.getInning().get(inning_number-1).getTotalBalls());
						
						switch (events.get(i).getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE: case CricketUtil.SIX: 
						case CricketUtil.LEG_BYE: case CricketUtil.BYE: case CricketUtil.LOG_WICKET:
							total_balls = total_balls + 1 ;
							total_runs = total_runs + events.get(i).getEventRuns();
							break;
						
						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
							total_runs = total_runs + events.get(i).getEventRuns();
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
						
						if(count_balls <= max_balls && total_runs >= splitvalue) {
							
							Balls.add(String.valueOf(total_balls));
							total_runs = total_runs - splitvalue;
							total_balls = 0;
							
							continue;
						}
					}
				}
			}
		}
		return Balls ;
	}
	
	public static String getFirstPowerPlayScore(Match match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0;
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
            {
            	if(events.get(i).getEventInningNumber() == inn_num)
                {
            		switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
						ball_count = ball_count + 1;
						break;
					}
            		
            		if(ball_count >= ((1 - 1) * 6) && ball_count < (6 * 6)) {
            			switch (events.get(i).getEventType())
                        {
                            case "1": case "2": case "3": case "5": case "0": case "4": case "6":
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                            case "WIDE": case "NO_BALL": case "BYE": case "LEG_BYE": case "PENALTY":
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                            case "LOG_WICKET":
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += events.get(i).getEventRuns();
                                }
                                total_wickets_PP += 1;
                                break;

                            case "LOG_ANY_BALL":
                                total_run_PP += events.get(i).getEventRuns();
                                if (events.get(i).getEventExtra() != null)
                                {
                                    total_run_PP += events.get(i).getEventExtraRuns();
                                }
                                if (events.get(i).getEventSubExtra() != null)
                                {
                                    total_run_PP += events.get(i).getEventSubExtraRuns();
                                }
                                if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                {
                                    total_wickets_PP += 1;
                                }
                                break;
                        }
            		} else if(ball_count >= ((1 - 1) * 6) && ball_count == (6 * 6)) {
            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
            				switch (events.get(i).getEventType())
                            {
                                case "1": case "2": case "3": case "5": case "0": case "4": case "6":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "WIDE": case "NO_BALL": case "BYE": case "LEG_BYE": case "PENALTY":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "LOG_WICKET":
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }
                                    total_wickets_PP += 1;
                                    break;

                                case "LOG_ANY_BALL":
                                    total_run_PP += events.get(i).getEventRuns();
                                    if (events.get(i).getEventExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventExtraRuns();
                                    }
                                    if (events.get(i).getEventSubExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventSubExtraRuns();
                                    }
                                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                    {
                                        total_wickets_PP += 1;
                                    }
                                    break;
                            }
            			}
            		}
                }
            }
        }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
    }
    public static String getSecPowerPlayScore(Match match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0;
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
            {
        		if(events.get(i).getEventInningNumber() == inn_num)
                {
        			switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
						ball_count = ball_count + 1;
						break;
					}
        			
        			if(ball_count > ((7 - 1) * 6) && ball_count < (15 * 6)) {
        				if(ball_count == 37) {
        					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
        						total_run_PP += events.get(i-1).getEventRuns();
        					}
        				}
            			switch (events.get(i).getEventType())
                        {
                            case "1": case "2": case "3": case "5": case "0": case "4": case "6":
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                            case "WIDE": case "NO_BALL": case "BYE": case "LEG_BYE": case "PENALTY":
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                            case "LOG_WICKET":
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += events.get(i).getEventRuns();
                                }
                                total_wickets_PP += 1;
                                break;

                            case "LOG_ANY_BALL":
                                total_run_PP += events.get(i).getEventRuns();
                                if (events.get(i).getEventExtra() != null)
                                {
                                    total_run_PP += events.get(i).getEventExtraRuns();
                                }
                                if (events.get(i).getEventSubExtra() != null)
                                {
                                    total_run_PP += events.get(i).getEventSubExtraRuns();
                                }
                                if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                {
                                    total_wickets_PP += 1;
                                }
                                break;
                        }
            		} else if(ball_count > ((7 - 1) * 6) && ball_count == (15 * 6)) {
            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
            				switch (events.get(i).getEventType())
                            {
                                case "1": case "2": case "3": case "5": case "0": case "4": case "6":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "WIDE": case "NO_BALL": case "BYE": case "LEG_BYE": case "PENALTY":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "LOG_WICKET":
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }
                                    total_wickets_PP += 1;
                                    break;

                                case "LOG_ANY_BALL":
                                    total_run_PP += events.get(i).getEventRuns();
                                    if (events.get(i).getEventExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventExtraRuns();
                                    }
                                    if (events.get(i).getEventSubExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventSubExtraRuns();
                                    }
                                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                    {
                                        total_wickets_PP += 1;
                                    }
                                    break;
                            }
            			}
            		}  
                }
            }
        }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
    }
    public static String getThirdPowerPlayScore(Match match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0;
        
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
                {
                	if(events.get(i).getEventInningNumber() == inn_num)
                    {
                		switch(events.get(i).getEventType()) {
    					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
    						ball_count = ball_count + 1;
    						break;
    					}
                		
                		if(ball_count > ((16 - 1) * 6) && ball_count <= (20 * 6)) {
                			if(ball_count == 91) {
            					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
            						total_run_PP += events.get(i-1).getEventRuns();
            					}
            				}
                			switch (events.get(i).getEventType())
                            {
                                case "1": case "2": case "3": case "5": case "0": case "4": case "6":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "WIDE": case "NO_BALL": case "BYE": case "LEG_BYE": case "PENALTY":
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case "LOG_WICKET":
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }
                                    total_wickets_PP += 1;
                                    break;

                                case "LOG_ANY_BALL":
                                    total_run_PP += events.get(i).getEventRuns();
                                    if (events.get(i).getEventExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventExtraRuns();
                                    }
                                    if (events.get(i).getEventSubExtra() != null)
                                    {
                                        total_run_PP += events.get(i).getEventSubExtraRuns();
                                    }
                                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                    {
                                        total_wickets_PP += 1;
                                    }
                                    break;
                            }
                		}
                    }
                }
            }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
    }
	
	public static List<String> getSplitBallls(int inning_number, int splitvalue, Match match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					//System.out.println("Inn Number" + inning_number);
					int max_balls = (match.getMaxOvers() * 6);
					int count_balls = ((match.getInning().get(inning_number-1).getTotalOvers() * 6) + match.getInning().get(inning_number-1).getTotalBalls());
					
					switch (events.get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE: case CricketUtil.SIX: 
					case CricketUtil.LEG_BYE: case CricketUtil.BYE: case CricketUtil.LOG_WICKET:
						total_balls = total_balls + 1 ;
						total_runs = total_runs + events.get(i).getEventRuns();
						break;
					
					case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
						total_runs = total_runs + events.get(i).getEventRuns();
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
					
					if(count_balls <= max_balls && total_balls >= splitvalue) {
						
						Balls.add(String.valueOf(total_runs));
						total_balls = total_balls - splitvalue;
						total_runs = 0;
						
						continue;
					}
				}
			}
		}
		return Balls ;
	}
	
	public static String getbattingstyle (String battingType,String FullNShort,boolean justHand,boolean men_women) {
		
		String HandWord="",Text="";
		
		if(justHand == true) {
			HandWord = "Hand";
		}else {
			HandWord = "Handed";
		}
		
		if(FullNShort == CricketUtil.FULL) {
			if(men_women == true) {
				if(battingType.charAt(0) == 'L') {
					Text = "Left-" + HandWord + " Batter";
				}else {
					Text = "Right-" + HandWord + " Batter";
				}
			}else {
				if(battingType.charAt(0) == 'L') {
					Text = "Left-" + HandWord + " Batter";
				}else {
					Text = "Right-" + HandWord + " Batter";
				}
			}
		}else {
			if(battingType.charAt(0) == 'L') {
				Text = "Left-" + HandWord + " Bat";
			}else {
				Text = "Right-" + HandWord + " Bat";
			}
		}

		return Text;
	}

	public static String getbowlingstyle(String bowlingType) {
		
		String text="",Style="";
		
		if(bowlingType.charAt(0) == 'L') {
			text = "Left Arm" ;
		}else {
			text = "Right Arm" ;
		}
		
		if(bowlingType == "WSL") {
			text = "Left Arm Wrist Spin";
		}else if(bowlingType == "WSR"){
			text = "Right Arm Wrist Spin";
		}
		
		Style = bowlingType.substring(1);
			
		switch (Style) {
		case "":
			text = text + " Bowler";
			break;
		case "F":
			text = text + " Fast";
			break;
		case "FM":
			text = text + " Fast Medium";
			break;
		case "MF":
			text = text + " Medium Fast";
			break;
		case "M":
			text = text + " Medium";
			break;
		case "SM":
			text = text + " Slow Medium";
			break;
		case "OB":
			text = text + " Off-Break";
			break;
		case "LB": case "LG":
			text = text + " Leg-Break";
			break;
		case "CH":
			text = text + " Chinaman";
			break;
		case "SO":
			text = text + " Orthodox";
			break;
		case "SL":
			text = "Slow Left Arm";
			break;
		
		}
		return text;
	}

	public static String processPowerPlay(String powerplay_return_type,Match match)
	{
		String return_pp_txt = "";
		int BallsBowledInInnings = 0;
	    
		for(Inning inn : match.getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				BallsBowledInInnings = inn.getTotalOvers() * 6 + inn.getTotalBalls();
			    if(match.getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getMatchType().equalsIgnoreCase("OD")) {
			    	
			    	if(BallsBowledInInnings >= ((inn.getFirstPowerplayStartOver() - 1) * 6 ) && BallsBowledInInnings < (inn.getFirstPowerplayEndOver()*6)) {
				    	return_pp_txt = CricketUtil.ONE;
				    }else if(BallsBowledInInnings >= ((inn.getSecondPowerplayStartOver() - 1) * 6) && BallsBowledInInnings < (inn.getSecondPowerplayEndOver()*6) ) {
				    	return_pp_txt = CricketUtil.TWO;
				    }else if(BallsBowledInInnings >= ((inn.getThirdPowerplayStartOver() - 1) * 6) && BallsBowledInInnings < (inn.getThirdPowerplayEndOver()*6)) {
				    	return_pp_txt = CricketUtil.THREE;
				    }
			    }else {
			    	if(BallsBowledInInnings >= ((inn.getFirstPowerplayStartOver() - 1) * 6 ) && BallsBowledInInnings < (inn.getFirstPowerplayEndOver()*6)) {
				    	return_pp_txt = CricketUtil.ONE;
				    }else {
				    	return_pp_txt = "";
				    }
			    }
			    
			    if(!return_pp_txt.trim().isEmpty()) {
			    	switch (powerplay_return_type)
				    {
				    case CricketUtil.FULL: 
				      return_pp_txt = CricketUtil.POWERPLAY + " " + return_pp_txt;
				      break;
				    case CricketUtil.SHORT: 
				      return_pp_txt = "PP" + return_pp_txt;
				      break;
				    case CricketUtil.MINI: 
					  return_pp_txt = "P" + return_pp_txt;
					  break;
				    }
			    }
			}
		}
	    
	    return return_pp_txt;
	}
	
	public static String lastFewOversData(String whatToProcess, List<Event> events,int inn_number)
	{
	    int count_lb = 0;
	    boolean exitLoop = false;
	    if ((events != null) && (events.size() > 0)) {
	      for (Event evnt : events)
	      {
	    	  if(evnt.getEventInningNumber() == inn_number) {
	    		  if (((whatToProcess.equalsIgnoreCase(CricketUtil.BOUNDARY)) 
	  	        		&& (evnt.getEventType().equalsIgnoreCase(CricketUtil.SIX))) 
	  	        		|| (evnt.getEventType().equalsIgnoreCase(CricketUtil.FOUR))) {
	    			  count_lb = 0;
	  	          //break;
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
	  	          }else {
	  	        	count_lb += 1;
	  	          }
	  	          break;
	  	        }
	  	        if (exitLoop == true) {
	  	          break;
	  	        }
	    	  }
	      }
	    }
	    return String.valueOf(count_lb);
	}
	
	public static String getlastthirtyballsdata(Match match,String separator,List<Event> events,int number_of_events) {
		
		int total_runs = 0, total_wickets = 0,ball_count = 0;
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch(events.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		        case CricketUtil.FOUR: case CricketUtil.SIX:
		        	ball_count = ball_count + 1;
		        	total_runs += events.get(i).getEventRuns();
		          break;
		          
		        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		        	ball_count = ball_count + 1;
		        	total_runs += events.get(i).getEventRuns();
		        	break;
		        case CricketUtil.LOG_WICKET:
		        	ball_count = ball_count + 1;
		        	if (events.get(i).getEventRuns() > 0) {
		        		total_runs += events.get(i).getEventRuns();
		        	}
		        	if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
		        		total_wickets += 0;
		        	}else {
		        		total_wickets += 1;
		        	}
		        	break;
		        case CricketUtil.LOG_ANY_BALL:
		        	ball_count = ball_count + 1;
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
				if(ball_count >= number_of_events) {
		    		break;
		    	}
			}
		}
			  
		return total_runs + separator + total_wickets;	
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
			        	if(events.get(i).getEventRuns() > 0) {
			        		total_runs += events.get(i).getEventRuns();
			        	}
			        	if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
			        		total_wickets += 0;
			        	}else {
			        		total_wickets += 1;
			        	}
			        	break;
			        
			        case CricketUtil.LOG_ANY_BALL:
			        	total_runs += events.get(i).getEventRuns();
				          if (events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty()) {
				        	  total_runs += events.get(i).getEventExtraRuns();
				          }
				          if (events.get(i).getEventSubExtra() != null && !events.get(i).getEventSubExtra().isEmpty()) {
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
	
	public static String compareData(Match match, int inning_number, List<Event> events,int Over) {
		
		int total_runs = 0;
		
		if((events != null) && (events.size() > 0)) { 
			for(int i =0; i <= events.size() - 1 ; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					if((events.get(i).getEventOverNo() < Over && events.get(i).getEventBallNo() >= 0) || (events.get(i).getEventOverNo() == Over && events.get(i).getEventBallNo() == 0)) {
						
						switch (events.get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				        case CricketUtil.FOUR: case CricketUtil.SIX: 
				        	total_runs += events.get(i).getEventRuns();
				          break;
				         
				        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
				        	total_runs += events.get(i).getEventRuns();
				        	break;
				        	
				        case CricketUtil.LOG_WICKET:
				        	if(events.get(i).getEventRuns() > 0) {
				        		total_runs += events.get(i).getEventRuns();
				        	}
				        	break;
				        
				        case CricketUtil.LOG_ANY_BALL:
				        	total_runs += events.get(i).getEventRuns();
					          if (events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty()) {
					        	  total_runs += events.get(i).getEventExtraRuns();
					          }
					          if (events.get(i).getEventSubExtra() != null && !events.get(i).getEventSubExtra().isEmpty()) {
					        	  total_runs += events.get(i).getEventSubExtraRuns();
					          }
					          break;
						}
					}
				}
			}
			total_runs = total_runs + 1;
		}
		return String.valueOf(total_runs);
	}

	public static String getEventsText(String whatToProcess, int player_id,String seperatorType, List<Event> events, int number_of_events) 
	{
		int total_runs = 0,ball_count = 0;
		String this_over = "";String this_ball_data = "";
		if ((events != null) && (events.size() > 0)) {
		  for (int i = events.size() - 1; i >= 0; i--)
		  {
			  
			  if(events.get(i).getEventBowlerNo() != 0) {
				  if (whatToProcess.equalsIgnoreCase(CricketUtil.OVER) 
							&& (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)|| events.get(i).getEventBowlerNo() != player_id)
							&& events.get(i).getEventBallNo() <= 0) {
						break;
		          }
			 }
			  
		    this_ball_data = "";
		    switch (events.get(i).getEventType())
		    {
		    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		    case CricketUtil.FOUR: case CricketUtil.SIX: 
		      this_ball_data = String.valueOf(events.get(i).getEventRuns());
		      total_runs += events.get(i).getEventRuns();
		      break;
		    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		      this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns()) + events.get(i).getEventType();
		      break;
		    case CricketUtil.LOG_WICKET: 
		      if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
		    	  break;
		      }else {
		    	  if (events.get(i).getEventRuns() > 0) {
			        this_ball_data = String.valueOf(events.get(i).getEventRuns()) + "+" + events.get(i).getEventType();
			      } else {
			        this_ball_data = events.get(i).getEventType();
			      }
			      total_runs = total_runs + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns();
		      }
		      break;
		    case CricketUtil.LOG_ANY_BALL:
		    	if (events.get(i).getEventExtra() != null) {
		    		if(events.get(i).getEventSubExtra() != null && events.get(i).getEventSubExtraRuns() > 0) {
		    			if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
		    						events.get(i).getEventSubExtraRuns());
		    			}else if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL) && events.get(i).getEventRuns() <= 0) {
		    				this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
		    						events.get(i).getEventSubExtraRuns());
		    			}
		    		}
		    		if(this_ball_data.isEmpty()) {
		    			if(events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		    				if(events.get(i).getEventRuns()>0) {
			    				this_ball_data = events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns();
			    			}else {
			    				this_ball_data = events.get(i).getEventExtra();
			    			}
		    			}else {
		    				if(events.get(i).getEventRuns()>0) {
			    				this_ball_data = String.valueOf(events.get(i).getEventRuns());
			    			}
		    			}
		    			
		    		}else {
		    			this_ball_data = this_ball_data + events.get(i).getEventExtra();
		    		}
		    	}
		    	
			    if (events.get(i).getEventSubExtra() != null && events.get(i).getEventSubExtraRuns() > 0){
			    	if(!events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE) && !events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			    		if (this_ball_data.isEmpty()) {
				          this_ball_data = String.valueOf(events.get(i).getEventSubExtraRuns()) + events.get(i).getEventSubExtra();
				        } else {
				          this_ball_data = this_ball_data + "+" + String.valueOf(events.get(i).getEventSubExtraRuns()) + events.get(i).getEventSubExtra();
				        }
			    	}
			        
			    }
		      if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
		        if (this_ball_data.isEmpty()) {
		          this_ball_data = CricketUtil.WICKET;
		        } else {
		          this_ball_data = this_ball_data + "+" + CricketUtil.WICKET;
		        }
		      }
		    }
		    if (!this_ball_data.isEmpty()) {
		    	ball_count = ball_count + 1;
		    	switch(whatToProcess.toUpperCase()) {
		    	case CricketUtil.OVER:
		    		if (this_over.isEmpty()) {
				        this_over = this_ball_data;
			      } else {
			    	  	this_over = this_ball_data + seperatorType + this_over;
			      }
		    		break;
		    	default:
		    		if (this_over.isEmpty()) {
				        this_over = this_ball_data;
			      } else {
			    	  	this_over = this_over + seperatorType + this_ball_data;
			      }
		    		break;
		    	}
		    	if(whatToProcess.equalsIgnoreCase(CricketUtil.TIMELINE) && ball_count >= number_of_events) {
		    		break;
		    	}
		    }
		  }
		}
		if(!this_over.trim().isEmpty()) {
			this_over = this_over.replace("WIDE", "wd");
			this_over = this_over.replace("NO_BALL", "nb");
			this_over = this_over.replace("LEG_BYE", "lb");
			this_over = this_over.replace("BYE", "b");
			this_over = this_over.replace("PENALTY", "pn");
			this_over = this_over.replace("LOG_WICKET", "w");
			this_over = this_over.replace("WICKET", "w");
		}
		//System.out.println(this_over);
		return this_over;
	}
	
	public static Event getLastBallData(List<Event> events) 
	{
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
			    switch (events.get(i).getEventType()) {
			    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
			    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			    	return events.get(i);
			    }
			}
		}
		return null;
	}	

	public static List<OverByOverData> getOverByOverData(Match match, int inn_num , String type,List<Event> events) 
	{
		List<OverByOverData> over_by_over_data = new ArrayList<OverByOverData>();
		
		int total_runs = 0, total_wickets = 0;
		
		if ((events != null) && (events.size() > 0)) {
			  for (int i = 0; i < events.size(); i++) {
				  if(events.get(i).getEventInningNumber() == inn_num) {
					  switch (events.get(i).getEventType().toUpperCase()) {
					    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT: case CricketUtil.FOUR: 
					    case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
					    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
					    	
					    	total_runs = total_runs + events.get(i).getEventRuns() ;
					    	
					    	switch (events.get(i).getEventType().toUpperCase()) {
						    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
						    	total_runs = total_runs + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns();
						    	
								if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() 
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
										total_wickets = total_wickets + 1;
								}
						    }
			  		        break;
			  		        
					    case CricketUtil.CHANGE_BOWLER:
					    	
					    	if(events.get(i).getEventBallNo() <= 0) {
						    	switch (processPowerPlay(CricketUtil.FULL, match).replace(CricketUtil.POWERPLAY, "").trim()) {
						    	case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE:
							    	over_by_over_data.add(new OverByOverData(events.get(i).getEventInningNumber(), events.get(i).getEventOverNo(), 
							    			total_runs, total_wickets, true));
						    		break;
						    	default:
							    	over_by_over_data.add(new OverByOverData(events.get(i).getEventInningNumber(), events.get(i).getEventOverNo(), 
							    			total_runs, total_wickets, false));
						    		break;
						    	}
						    	switch (type.toUpperCase()) {
								case "MANHATTAN":
									total_runs = 0;
									total_wickets = 0;
									break;
								case "WORM":
									total_wickets = 0;
									break;
								}
					    	}
					    	break;
					    	
					    }
				  }  
			  }
		}
		if(total_runs > 0 || total_wickets > 0) {
	    	switch (processPowerPlay(CricketUtil.FULL, match).replace(CricketUtil.POWERPLAY, "").trim()) {
	    	case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE:
		    	over_by_over_data.add(new OverByOverData(events.get(events.size()-1).getEventInningNumber(), 
		    			events.get(events.size()-1).getEventOverNo(), total_runs, total_wickets, true));
	    		break;
	    	default:
		    	over_by_over_data.add(new OverByOverData(events.get(events.size()-1).getEventInningNumber(), 
		    			events.get(events.size()-1).getEventOverNo(), total_runs, total_wickets, false));
	    		break;
	    	}
		}
		//System.out.println("over = " + over_by_over_data);
		return over_by_over_data;
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
	
	public static String generateTossResult(Match match,String TossType,String DecisionType, String teamNameType, String electedOrChoose) {

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
				TeamNameToUse = match.getHomeTeam().getTeamName4();
			} else {
				TeamNameToUse = match.getAwayTeam().getTeamName4();
			}
		    break;
		default:
			if(match.getTossWinningTeam() == match.getHomeTeamId()) {
				TeamNameToUse = match.getHomeTeam().getTeamName1();
			} else {
				TeamNameToUse = match.getAwayTeam().getTeamName1();
			}
		    break;
		}
		switch (TossType) {
		case CricketUtil.MINI:
			return CricketUtil.TOSS + ": " + TeamNameToUse;
		case CricketUtil.SHORT:
			return TeamNameToUse + " won the toss and " + decisionText;
		default:
			switch (electedOrChoose) {
			case CricketUtil.ELECTED:
				return TeamNameToUse + " won toss & elected to " + decisionText;
			default:
				return TeamNameToUse + " won toss & chose to " + decisionText;
			}
		}
	}
	
//	public static String generateTossResult(Match match,String TossType,String DecisionType, String teamNameType) {
//
//		String TeamNameToUse="", decisionText = ""; 
//		
//		switch (match.getTossWinningDecision()) {
//		case CricketUtil.BAT:
//			decisionText = CricketUtil.BAT.toLowerCase();
//			break;
//		default:
//			switch (DecisionType) {
//			case CricketUtil.FIELD:
//				decisionText = CricketUtil.FIELD.toLowerCase();
//				break;
//			default:
//				decisionText = CricketUtil.BOWL;
//				break;
//			}
//			break;
//		}
//		switch (teamNameType) {
//		case CricketUtil.SHORT:
//			if(match.getTossWinningTeam() == match.getHomeTeamId()) {
//				TeamNameToUse = match.getHomeTeam().getTeamName4();
//			} else {
//				TeamNameToUse = match.getAwayTeam().getTeamName4();
//			}
//		    break;
//		default:
//			if(match.getTossWinningTeam() == match.getHomeTeamId()) {
//				TeamNameToUse = match.getHomeTeam().getTeamName1();
//			} else {
//				TeamNameToUse = match.getAwayTeam().getTeamName1();
//			}
//		    break;
//		}
//		switch (TossType) {
//		case CricketUtil.MINI:
//			return CricketUtil.TOSS + ": " + TeamNameToUse;
//		case CricketUtil.SHORT:
//			return TeamNameToUse + " won the toss and " + decisionText;
//		default:
//			return TeamNameToUse + " won the toss and choose to " + decisionText;
//		}
//	}
	
	public static String Plural(int count){
		if (count == 1){
			return "";
		} else{
			return "s";
		}
	}

	public static int getTargetRuns(Match match) {
		if(match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			int targetRuns = ((match.getInning().get(0).getTotalRuns() + match.getInning().get(2).getTotalRuns()) - (match.getInning().get(1).getTotalRuns() + match.getInning().get(3).getTotalRuns())) + 1;
			if(match.getTargetRuns() > 0) {
				targetRuns = match.getTargetRuns();
			}
			return targetRuns;
		}else {
			int targetRuns = match.getInning().get(0).getTotalRuns() + 1;
			if(match.getTargetRuns() > 0) {
				targetRuns = match.getTargetRuns();
			}
			return targetRuns;
		}
	}

	public static String getTargetOvers(Match match) {
		
		String targetOvers = "";
		if(match.getTargetOvers() == null || match.getTargetOvers().trim().isEmpty()) {
			targetOvers = String.valueOf(match.getMaxOvers());
		} else {
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
		
		int requiredBalls;
		if(getTargetOvers(match).contains(".")) {
			requiredBalls = ((Integer.valueOf(getTargetOvers(match).split(".")[0]) * 6) + Integer.valueOf(getTargetOvers(match).split(".")[1])) 
					- (match.getInning().get(1).getTotalOvers() * 6) - match.getInning().get(1).getTotalBalls();
		} else {
			requiredBalls = ((Integer.valueOf(getTargetOvers(match)) * 6)) 
					- (match.getInning().get(1).getTotalOvers() * 6) - match.getInning().get(1).getTotalBalls();
		}
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

	public static int getTeamRunsAhead(int inning_number, Match match)
	{
		int total_runs = 0, batting_team_id = 0;
		for(Inning inn : match.getInning()) {
			if(inn.getInningNumber() == inning_number) {
				batting_team_id = inn.getBattingTeamId();
			}
		}
		if(batting_team_id > 0) {
			for(Inning inn : match.getInning()) {
				if (inn.getInningNumber() <= inning_number) {
					if(inn.getBattingTeamId() == batting_team_id) {
						total_runs = total_runs + inn.getTotalRuns();
					} else {
						total_runs = total_runs - inn.getTotalRuns();
					}
				}
			}
		}
		return total_runs;
	}
	
	public static String generateMatchSummaryStatus(int whichInning, Match match, String teamNameType) {

		String matchSummaryStatus = generateMatchResult(match, teamNameType);

	    if(matchSummaryStatus.trim().isEmpty()) {
	    	
	    	int lead_by = getTeamRunsAhead(whichInning,match);
			String batTeamNm = "", bowlTeamNm = "";

			switch (teamNameType) {
		    case CricketUtil.SHORT: 
		    	batTeamNm = match.getInning().get(whichInning - 1).getBatting_team().getTeamName3();
		    	bowlTeamNm = match.getInning().get(whichInning - 1).getBowling_team().getTeamName3();
		    	break;
		    default: 
		    	batTeamNm = (match.getInning().get(whichInning - 1)).getBatting_team().getTeamName1();
		    	bowlTeamNm = (match.getInning().get(whichInning - 1)).getBowling_team().getTeamName1();
		    	break;
		    }
	    	
		    switch (whichInning) {
		    case 1: 
		    	if (((match.getInning().get(whichInning - 1)).getTotalRuns() > 0) || 
		  		      ((match.getInning().get(whichInning - 1)).getTotalOvers() > 0) || 
		  		      ((match.getInning().get(whichInning - 1)).getTotalBalls() > 0)) {
		  		      return "Current Run Rate " + (match.getInning().get(0)).getRunRate();
		  		    }
		    	else {
		    		return CricketFunctions.generateTossResult(match, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL,CricketUtil.CHOSE);
		    	}
		    case 2: case 3:
		    	if(match.getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
		    		if(lead_by > 0) {
				    	matchSummaryStatus = batTeamNm + " lead by " + lead_by + " run" + Plural(lead_by);
		    		} else if(lead_by == 0) {
				    	matchSummaryStatus = "Scores are level";
		    		} else {
				    	matchSummaryStatus = batTeamNm + " trail by " + (-1 * lead_by) + " run" + Plural(-1 * lead_by);
		    		}
		    	} else {
				    if ((CricketFunctions.getRequiredRuns(match) > 0) && (CricketFunctions.getRequiredBalls(match) > 0) 
				    		&& (CricketFunctions.getWicketsLeft(match) > 0)) {

				    	matchSummaryStatus = batTeamNm + " need " + CricketFunctions.getRequiredRuns(match) + 
					        	" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " to win from ";
				    	if (CricketFunctions.getRequiredBalls(match) >= 100) {
				    		matchSummaryStatus = matchSummaryStatus + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + " overs";
						} else {
							matchSummaryStatus = matchSummaryStatus + CricketFunctions.getRequiredBalls(match) + 
									" ball" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match));
						}
				    } else if (CricketFunctions.getRequiredRuns(match) <= 0)
				    {
				    	matchSummaryStatus = batTeamNm + " win by " + CricketFunctions.getWicketsLeft(match) + 
				    		" wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match));
				    }
				    else if (CricketFunctions.getRequiredRuns(match) == 1 && (CricketFunctions.getRequiredBalls(match) <= 0 
				    		|| CricketFunctions.getWicketsLeft(match) <= 0)) 
				    {
				    	matchSummaryStatus = "Match tied";
				    } 
				    else 
				    {
				    	matchSummaryStatus = bowlTeamNm + " win by " + (CricketFunctions.getRequiredRuns(match) - 1) + 
				    		" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match) - 1);
				    }
				    if(match.getTargetType() != null) {
						if(match.getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
							matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.DLS + ")";
						}else if(match.getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
							matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.VJD + ")";
						}
				    }
		    	}
		    	break;
		    case 4:
		    	int required_runs = 1 - lead_by;
		    	if(required_runs > 0) {
		    		if(match.getInning().get(whichInning - 1).getTotalRuns() == 0) {
				    	matchSummaryStatus = batTeamNm + " need " + required_runs + " run" + CricketFunctions.Plural(required_runs) + " to win";
		    		} else {
				    	matchSummaryStatus = batTeamNm + " need " + required_runs + " more run" + CricketFunctions.Plural(required_runs) + " to win";
		    		}
		    	}
		    	break;
		    }
	    }
	    return matchSummaryStatus;
	}
	
	public static String getPowerPlayScore(Inning inning,int inn_number, String seperator,List<Event> events) {
		int total_run_PP=0, total_wickets_PP=0,ball_count = 0;
		if((events != null) && (events.size() > 0)) {
			for(int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inn_number) {
					switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
						ball_count = ball_count + 1;
						break;
					}
					
					if(ball_count <= (inning.getFirstPowerplayEndOver() * 6)) {
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

	public static String processThisOverRunsCount(int player_id, List<Event> events) {
		int total_runs=0;
		if((events != null) && (events.size() > 0)) {
			
			for(int i = events.size() - 1; i >= 0; i--) {
				if(events.get(i).getEventBowlerNo() != 0){
					if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)|| events.get(i).getEventBowlerNo() != player_id)
							&& events.get(i).getEventBallNo() <= 0) {
						break;
					}
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
							if(bc.getHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
								if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
									return bc.getPlayer().getTicker_name().toUpperCase() + " " + bc.getRuns() + " (" + bc.getBalls() + ")" + " " + 
											bc.getHowOutPartOne() + " (sub - " + bc.getHowOutPartTwo().split(" ")[0] + " )";
								}else {
									return bc.getPlayer().getTicker_name().toUpperCase() + " " + bc.getRuns() + " (" + bc.getBalls() + ")" + " " + 
											bc.getHowOutPartOne() + " ( " + bc.getHowOutPartTwo().split(" ")[0] + " )";
								}
							}else {
								return bc.getPlayer().getTicker_name().toUpperCase() + " " + bc.getRuns() + " (" + bc.getBalls() + ")" + " " + 
										bc.getHowOutText();
							}
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
					case CricketUtil.BATSMAN:
						if(evnt.getEventBatterNo() == player_id) {
							go_ahead = true;
						}
						break;
					case CricketUtil.BOWLER:
						if(evnt.getEventBowlerNo() == player_id) {
							go_ahead = true;
						}
						break;
					case "TEAM":
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
				        	case CricketUtil.BATSMAN: case CricketUtil.BOWLER:
								dots++;
								break;
							}
				        	break;
				        
				        case CricketUtil.LOG_ANY_BALL:
							if(evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
								if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
									switch (whatToProcess) {
									case CricketUtil.BATSMAN: case CricketUtil.BOWLER: 
										dots++;
										break;
									}
								}
								if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) && (evnt.getEventWasABoundary() != null) && 
										(evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
									switch (whatToProcess) {
									case CricketUtil.BATSMAN: case CricketUtil.BOWLER: case "TEAM":
										fours++;
										break;
									}
						        }
								if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.SIX)) && (evnt.getEventWasABoundary() != null) && 
										(evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
									switch (whatToProcess) {
									case CricketUtil.BATSMAN: case CricketUtil.BOWLER: case "TEAM":
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
	
	public static List<Double> speedData(Match match,int whichInning,int PlayerId) {
		List<Double> data = new ArrayList<Double>();
		for(Inning inn : match.getInning()) {
			if (inn.getInningNumber() == whichInning) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					if(PlayerId == boc.getPlayerId()) {
						if(boc.getSpeeds() != null) {
							for(int i = 0; i < boc.getSpeeds().size();i++) {
								data.add(Double.valueOf(boc.getSpeeds().get(i).getSpeedValue().trim()));
							}
							Collections.sort(data);
						}
//						System.out.println("Speed = " + data);
					}
				}
			}
		}
		return data;
	}
	
	public static List<String> projectedScore(Match match) {
		//rates= 6,8,10 or 8,10,12
		List<String> proj_score = new ArrayList<String>();
		String  PS_Curr="", PS_1 = "",PS_2 = "",PS_3 = "",RR_1 = "",RR_2 = "",RR_3 = "",CRR = "";
		int Balls_val = 0;

		if(!match.getTargetOvers().isEmpty() && Double.valueOf(match.getTargetOvers()) > 0) {
			if(match.getTargetOvers().contains(".")) {
				Balls_val = (Integer.valueOf(match.getTargetOvers().split("\\.")[0]) * 6) + Integer.valueOf(match.getTargetOvers().split("\\.")[1]);
			}else {
				Balls_val = Integer.valueOf(match.getTargetOvers()) * 6;
			}
			//Over_val = match.getTargetOvers();
		}else {
			Balls_val = match.getMaxOvers()*6;
		}
		
		int remaining_balls = (Balls_val - (match.getInning().get(0).getTotalOvers()*6 + match.getInning().get(0).getTotalBalls()));
		double value = (remaining_balls * Double.valueOf(match.getInning().get(0).getRunRate()));
		value  = value/6;
		
		PS_Curr = String.valueOf(Math.round(((value + match.getInning().get(0).getTotalRuns()))));
		CRR = match.getInning().get(0).getRunRate();
		
		proj_score.add(CRR);
		proj_score.add(String.valueOf(PS_Curr));
		
		String[] arr = (match.getInning().get(0).getRunRate().split("\\."));
	    double[] intArr= new double[2];
	    intArr[0]=Integer.parseInt(arr[0]);
	  
		for(int i=2;i<=6;i=i+2) {
			if(i==2) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_1 = String.valueOf(Math.round(value + match.getInning().get(0).getTotalRuns()));
				RR_1 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_1);
				proj_score.add(PS_1);
			}
			else if(i==4) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_2 = String.valueOf(Math.round(value + match.getInning().get(0).getTotalRuns()));
				RR_2 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_2);
				proj_score.add(PS_2);
			}else if(i==6) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_3 = String.valueOf(Math.round(value + match.getInning().get(0).getTotalRuns()));
				RR_3 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_3);
				proj_score.add(PS_3);
			}
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
