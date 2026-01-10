package com.cricket.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.Ae_Third_Party_Xml.AE_Cricket;
import com.Ae_Third_Party_Xml.AE_Inning;
import com.Ae_Third_Party_Xml.AE_Last_Ball;
import com.Ae_Third_Party_Xml.AE_Six_Distance;
import com.WTV.BatsmanStats;
import com.WTV.BowlerStats;
import com.cricket.archive.Archive;
import com.cricket.model.BatBallGriff;
import com.cricket.model.BatSpeed;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.DaySession;
import com.cricket.model.Dictionary;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Event;
import com.cricket.model.EventFile;
import com.cricket.model.FieldersData;
import com.cricket.model.Fixture;
import com.cricket.model.ForeignLanguageData;
import com.cricket.model.HeadToHead;
import com.cricket.model.HeadToHeadPlayer;
import com.cricket.model.HeadToHeadTeam;
import com.cricket.model.ImpactData;
import com.cricket.model.Inning;
import com.cricket.model.InningStats;
import com.cricket.model.LeaderBoard;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.MatchClock;
import com.cricket.model.MatchStats;
import com.cricket.model.MatchStats.VariousStats;
import com.cricket.model.MultiLanguageDatabase;
import com.cricket.model.OverByOverData;
import com.cricket.model.POTT;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.PowerPlays;
import com.cricket.model.Review;
import com.cricket.model.Season;
import com.cricket.model.Setup;
import com.cricket.model.Speed;
import com.cricket.model.Staff;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.TargetData;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.sf.json.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.InputStream;

public class CricketFunctions {
	
	public static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	private static long lastModifiedTime = -1;
	private static ObjectMapper objectMapper = new ObjectMapper();
	public static int ball_number = 0, c = 0;
	public static boolean isLeagleBall = false;
	
    public static String RemoveUnicodeCharacters(String input) {
        if (input == null) return null;

        // Step 1: Normalize known tricky characters
        String cleaned = input
            // spaces
            .replace('\u202F', ' ')  // narrow no-break space
            .replace('\u00A0', ' ')  // non-breaking space
            .replace('\u2007', ' ')  // figure space
            .replace('\u2060', ' ')  // word joiner (invisible)
            // hyphens & dashes
            .replace('\u2011', '-')  // non-breaking hyphen
            .replace('\u2010', '-')  // hyphen
            .replace('\u2012', '-')  // figure dash
            .replace('\u2013', '-')  // en dash
            .replace('\u2014', '-')  // em dash
            .replace('\u2212', '-')  // minus sign
            .replace('\uFE58', '-')  // small em dash
            .replace('\uFE63', '-')  // small hyphen-minus
            .replace('\uFF0D', '-'); // fullwidth hyphen-minus

        // Step 2: Replace any other Unicode space separators with normal space
        cleaned = cleaned.replaceAll("\\p{Zs}+", " ");

        // Step 3: Remove other invisible control characters (if any)
        cleaned = cleaned.replaceAll("\\p{Cc}+", "");

        // Step 4: Trim leading and trailing spaces
        return cleaned.trim();
    }
    
    public static List<Double> getLastSpellSpeeds(List<Event> events) {

        List<Double> speedList = new ArrayList<>();

        // Step-1: Collect speeds in reverse
        for (int i = events.size() - 1; i >= 0; i--) {

            Event ev = events.get(i);
            String type = ev.getEventType();

            // STOP only for CHANGE_BOWLER
            if ("CHANGE_BOWLER".equalsIgnoreCase(type)) {
                break;
            }

            // ignore END_OVER
            if ("END_OVER".equalsIgnoreCase(type)) {
                continue;
            }

            String sp = ev.getEventSpeed();
            Double speedValue = null;

            if (sp != null && !sp.trim().isEmpty()) {
                try {
                    speedValue = Double.valueOf(sp.trim());
                } catch (Exception ignored) {}
            }

            // If null → store 0.0 now
            if (speedValue == null) {
                speedValue = 0.0;
            }

            speedList.add(speedValue);
        }

        // Step-2: Calculate average of NON-ZERO speeds
        double sum = 0;
        int count = 0;

        for (double s : speedList) {
            if (s > 0) {
                sum += s;
                count++;
            }
        }

        double average = count > 0 ? sum / count : 0.0;
        average = Math.round(average * 10.0) / 10.0;

        // Step-3: Replace all 0.0 with average
        for (int i = 0; i < speedList.size(); i++) {
            if (speedList.get(i) == 0.0) {
                speedList.set(i, average);
            }
        }
        return speedList;
    }
    
    public static Statistics getStatsByType(int playerId,String statsShortName,List<StatsType> statsTypes,List<Statistics> statistics) {
	    StatsType statsType = statsTypes.stream().filter(st -> st.getStats_short_name().equalsIgnoreCase(statsShortName))
	            .findAny().orElse(null);
	    if (statsType == null) {
	        return null;
	    }
	    return statistics.stream().filter(st -> st.getPlayer_id() == playerId && st.getStats_type_id() == statsType.getStats_id())
	            .findAny().orElse(null);
	}
    public static int[] parseBestFigures(String bestFigures) {
	    // Format: "W-R" e.g., "3-20"
	    if (bestFigures == null || bestFigures.trim().isEmpty()) return new int[]{0, Integer.MAX_VALUE};
	    String[] parts = bestFigures.split("-");
	    int wickets = Integer.parseInt(parts[0].trim());
	    int runs    = Integer.parseInt(parts[1].trim());
	    return new int[]{wickets, runs};
	}
	
    public static Statistics mergeIsplCareerStats(Statistics base, Statistics add) {
	    if (add == null) return base;
	    
	    // Batting
	    base.setMatches(base.getMatches() + add.getMatches());
	    base.setInnings(base.getInnings() + add.getInnings());
	    base.setNot_out(base.getNot_out() + add.getNot_out());
	    base.setRuns(base.getRuns() + add.getRuns());
	    base.setBalls_faced(base.getBalls_faced() + add.getBalls_faced());
	    base.setHundreds(base.getHundreds() + add.getHundreds());   
	    base.setFifties(base.getFifties() + add.getFifties());      
	    base.setThirties(base.getThirties() + add.getThirties());   
	    base.setFours(base.getFours() + add.getFours());             
	    base.setSixes(base.getSixes() + add.getSixes());             

	    if (add.getBest_score() != null && !add.getBest_score().isEmpty()) {

	        if (base.getBest_score() == null || base.getBest_score().isEmpty()) {
	            base.setBest_score(add.getBest_score());
	            base.setBest_score_against(add.getBest_score_against());
	            base.setBest_score_venue(add.getBest_score_venue());
	        } else {
	        	
	        	int baseRuns = Integer.parseInt(base.getBest_score().replace("*", "").trim());
	            int addRuns  = Integer.parseInt(add.getBest_score().replace("*", "").trim());

	            boolean baseNotOut = base.getBest_score().contains("*");
	            boolean addNotOut  = add.getBest_score().contains("*");

	            boolean shouldReplace = false;
	            
	            if (addRuns > baseRuns) {
	                shouldReplace = true;
	            }
	            else if (addRuns == baseRuns && addNotOut && !baseNotOut) {
	                shouldReplace = true;
	            }
	            
	            if (shouldReplace) {
	            	base.setBest_score(add.getBest_score());
		            base.setBest_score_against(add.getBest_score_against());
		            base.setBest_score_venue(add.getBest_score_venue());
	            }
	        }
	    }
	    
	    // Bowling
	    base.setBalls_bowled(base.getBalls_bowled() + add.getBalls_bowled());
	    base.setRuns_conceded(base.getRuns_conceded() + add.getRuns_conceded());
	    base.setWickets(base.getWickets() + add.getWickets());
	    base.setDotbowled(base.getDotbowled() + add.getDotbowled());
	    base.setPlus_3(base.getPlus_3() + add.getPlus_3()); // 3+
	    base.setPlus_5(base.getPlus_5() + add.getPlus_5());    // 5+
	    base.setBowler_inning(base.getBowler_inning() + add.getBowler_inning());
	    base.setCatches(base.getCatches() + add.getCatches());
	    
	    if (add.getBest_figures() != null && !add.getBest_figures().isEmpty() && 
	    		!add.getBest_figures().equals("0") && !add.getBest_figures().equals("0-0")) {

	        if (base.getBest_figures() == null || base.getBest_figures().isEmpty() || 
	        		base.getBest_figures().equals("0") || base.getBest_figures().equals("0-0")) {
	            base.setBest_figures(add.getBest_figures());
	            base.setBest_figures_against(add.getBest_figures_against());
	            base.setBest_figures_venue(add.getBest_figures_venue());
	        } else {
	        	
	        	int baseW = parseBestFigures(base.getBest_figures())[0], baseR = parseBestFigures(base.getBest_figures())[1];
	            int addW  = parseBestFigures(add.getBest_figures())[0],  addR  = parseBestFigures(add.getBest_figures())[1];
	        	
	            boolean shouldReplace = false;

	            if (addW > baseW) {
	                shouldReplace = true;
	            } else if (addW == baseW && addR < baseR) {
	                shouldReplace = true;
	            }

	            if (shouldReplace) {
	            	base.setBest_figures(add.getBest_figures());
		            base.setBest_figures_against(add.getBest_figures_against());
		            base.setBest_figures_venue(add.getBest_figures_venue());
	            }
	        }
	    }

	    return base;
	}
    
	public static List<Integer> getBallCountStartAndEndRange(MatchAllData match, Inning inning)
	{
		List<Integer> ballCount = new ArrayList<Integer>();
		String startPoint="",endPoints="";
		
		if(match.getSetup().getNumberOfPowerplays() > 0) {
			for(int i=1;i<=match.getSetup().getNumberOfPowerplays();i++) {
				startPoint = "";endPoints ="";
				switch(i) {
				case 1:
					startPoint = inning.getFirstPowerplayStartOver();
					endPoints = inning.getFirstPowerplayEndOver();
					break;
				case 2:
					startPoint = inning.getSecondPowerplayStartOver();
					endPoints = inning.getSecondPowerplayEndOver();
					break;
				case 3:
					startPoint = inning.getThirdPowerplayStartOver();
					endPoints = inning.getThirdPowerplayEndOver();
					break;
				}
				
				if(startPoint.contains(".")) {
					ballCount.add(((Integer.valueOf(startPoint.split("\\.")[0])*6) + Integer.valueOf(startPoint.split("\\.")[1])));
				}else {
					ballCount.add(((Integer.valueOf(startPoint)-1)*6));
				}
				
				if(endPoints.contains(".")) {
					ballCount.add(((Integer.valueOf(endPoints.split("\\.")[0])*6) + Integer.valueOf(endPoints.split("\\.")[1])));
				}else {
					ballCount.add((Integer.valueOf(endPoints)*6));
				}
			}
		}
		return ballCount;
	}
	public static String processPowerPlay(String powerplay_return_type, MatchAllData match)
	{
		String return_pp_txt = "";
		int BallsBowledInInnings = 0;
		List<Integer> ballCount = null;
		
		for(Inning inn : match.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				
				BallsBowledInInnings = inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver()) + inn.getTotalBalls();
				ballCount = getBallCountStartAndEndRange(match, inn);
				
				if(match.getSetup().getNumberOfPowerplays() > 0) {
					switch (match.getSetup().getNumberOfPowerplays()) {
					case 1:
						if(BallsBowledInInnings >= ballCount.get(0) && BallsBowledInInnings < ballCount.get(1)) {
					    	return_pp_txt = CricketUtil.ONE;
					    }
						break;
					case 2:
						if(BallsBowledInInnings >= ballCount.get(0) && BallsBowledInInnings < ballCount.get(1)) {
					    	return_pp_txt = CricketUtil.ONE;
					    }else if(BallsBowledInInnings >= ballCount.get(2) && BallsBowledInInnings < ballCount.get(3)) {
					    	return_pp_txt = CricketUtil.TWO;
					    }
						break;
					default:
						if(BallsBowledInInnings >= ballCount.get(0) && BallsBowledInInnings < ballCount.get(1)) {
					    	return_pp_txt = CricketUtil.ONE;
					    }else if(BallsBowledInInnings >= ballCount.get(2) && BallsBowledInInnings < ballCount.get(3)) {
					    	return_pp_txt = CricketUtil.TWO;
					    }else if(BallsBowledInInnings >= ballCount.get(4) && BallsBowledInInnings <= ballCount.get(5)) {
					    	return_pp_txt = CricketUtil.THREE;
					    }
						break;
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
	public static String processPowerPlayAnimation(MatchAllData match, int InningNumber)
	{
		if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
			for (int i = match.getEventFile().getEvents().size() - 1; i >= 0; i--) {

				if((match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) && 
						(match.getMatch().getInning().get(InningNumber-1).getFirstPowerplayEndOver().equalsIgnoreCase(CricketFunctions.OverBalls(
								match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo())) ||
								match.getMatch().getInning().get(InningNumber-1).getSecondPowerplayEndOver().equalsIgnoreCase(CricketFunctions.OverBalls(
										match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo()))))) {
					
					return CricketUtil.YES;
					
				}else if((!match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) && 
						(match.getMatch().getInning().get(InningNumber-1).getFirstPowerplayEndOver().equalsIgnoreCase(CricketFunctions.OverBalls(
								match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo()))||
								match.getMatch().getInning().get(InningNumber-1).getSecondPowerplayEndOver().equalsIgnoreCase(CricketFunctions.OverBalls(
								match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo()))||
								match.getMatch().getInning().get(InningNumber-1).getThirdPowerplayEndOver().equalsIgnoreCase(CricketFunctions.OverBalls(
								match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo()))))
								&& match.getEventFile().getEvents().get(i).getEventBallNo() == 0) {
					
					if(!match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE) &&
							!match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL) &&
							!match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
						
						return CricketUtil.NO;
					}
				}
			}
		}
	    return null;
	}
	
	public static Map<String, HeadToHeadTeam> getPowerPlayAverage(List<Team> team, List<HeadToHeadTeam> headToHead, MatchAllData match) {
		Map<String ,HeadToHeadTeam> powerplay = new HashMap<String, HeadToHeadTeam>();
		   for (HeadToHeadTeam h2h : headToHead) {
	            String teamName = h2h.getTeam().getTeamName4().trim().toUpperCase();

	            HeadToHeadTeam teams = powerplay.getOrDefault(teamName, new HeadToHeadTeam());
	            teams.setTotalmatches(teams.getTotalmatches() + 1);
	            teams.setP1_Run(teams.getP1_Run() + h2h.getP1_Run());
	            teams.setP2_Run(teams.getP2_Run() + h2h.getP2_Run());
	            teams.setP3_Run(teams.getP3_Run() + h2h.getP3_Run());
	            teams.setP1_Wicket(teams.getP1_Wicket() + h2h.getP1_Wicket());
	            teams.setP2_Wicket(teams.getP2_Wicket() + h2h.getP2_Wicket());
	            teams.setP3_Wicket(teams.getP3_Wicket() + h2h.getP3_Wicket());
	            teams.setTeam(h2h.getTeam());
	            powerplay.put(teamName, teams);
	        }

	        Inning inn = match.getMatch().getInning().stream().filter(in->in.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
	        MatchStats matchStats = getpowerPlay(match);
	        if (powerplay.containsKey(match.getMatch().getInning().get(0).getBatting_team().getTeamName4())) {
       		 HeadToHeadTeam teams = powerplay.get(match.getMatch().getInning().get(0).getBatting_team().getTeamName4());
       		 teams.setTotalmatches(teams.getTotalmatches()+1);
       		 teams.setP1_Run(Integer.valueOf(teams.getP1_Run()) + matchStats.getHomeFirstPowerPlay().getTotalRuns());
       		 teams.setP1_Wicket(Integer.valueOf(teams.getP1_Wicket()) + matchStats.getHomeFirstPowerPlay().getTotalWickets());

       		 if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
           		 teams.setP2_Run(Integer.valueOf(teams.getP2_Run()) + matchStats.getHomeSecondPowerPlay().getTotalRuns());
           		 teams.setP2_Wicket(Integer.valueOf(teams.getP2_Wicket()) + matchStats.getHomeSecondPowerPlay().getTotalWickets());
           		 teams.setP3_Run(Integer.valueOf(teams.getP3_Run()) + matchStats.getHomeThirdPowerPlay().getTotalRuns());
           		 teams.setP3_Wicket(Integer.valueOf(teams.getP3_Wicket()) + matchStats.getHomeThirdPowerPlay().getTotalWickets());
       		 }
            } else {
           	 HeadToHeadTeam teams = new HeadToHeadTeam();
           	 teams.setTotalmatches(1);
           	 teams.setTotalmatches(teams.getTotalmatches()+1);
       		 teams.setP1_Run(matchStats.getHomeFirstPowerPlay().getTotalRuns());
       		 teams.setP1_Wicket(matchStats.getHomeFirstPowerPlay().getTotalWickets());
       		 teams.setTeam(match.getMatch().getInning().get(0).getBatting_team());
       		 if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
       			teams.setP2_Run(matchStats.getHomeSecondPowerPlay().getTotalRuns());
          		teams.setP3_Run(matchStats.getHomeThirdPowerPlay().getTotalRuns());
          		teams.setP2_Wicket(matchStats.getHomeSecondPowerPlay().getTotalWickets());
          		teams.setP3_Wicket(matchStats.getHomeThirdPowerPlay().getTotalWickets());
       		 }
           	 powerplay.put(match.getMatch().getInning().get(0).getBatting_team().getTeamName4(),teams);
            }
	        if(inn.getInningNumber()==2) {
	        	 if (powerplay.containsKey(inn.getBatting_team().getTeamName4())) {
            		 HeadToHeadTeam teams = powerplay.get(match.getMatch().getInning().get(1).getBatting_team().getTeamName4());
            		 teams.setTotalmatches(teams.getTotalmatches()+1);
            		 teams.setP1_Run(Integer.valueOf(teams.getP1_Run()) + matchStats.getAwayFirstPowerPlay().getTotalRuns());
            		 teams.setP1_Wicket(Integer.valueOf(teams.getP1_Wicket()) + matchStats.getAwayFirstPowerPlay().getTotalWickets());
            		 
            		 if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
            			 teams.setP2_Run(Integer.valueOf(teams.getP2_Run()) + matchStats.getAwaySecondPowerPlay().getTotalRuns());
                		 teams.setP3_Run(Integer.valueOf(teams.getP3_Run()) + matchStats.getAwayThirdPowerPlay().getTotalRuns());
                		 teams.setP2_Wicket(Integer.valueOf(teams.getP2_Wicket()) + matchStats.getAwaySecondPowerPlay().getTotalWickets());
                		 teams.setP3_Wicket(Integer.valueOf(teams.getP3_Wicket()) + matchStats.getAwayThirdPowerPlay().getTotalWickets());
                		 
                		 }
                 } else {
                	 HeadToHeadTeam teams = new HeadToHeadTeam();
                	 teams.setTotalmatches(1);
                	 teams.setTotalmatches(teams.getTotalmatches()+1);
            		 teams.setP1_Run(matchStats.getAwayFirstPowerPlay().getTotalRuns());
            		 teams.setP1_Wicket(matchStats.getAwayFirstPowerPlay().getTotalWickets());
            		 teams.setTeam(match.getMatch().getInning().get(1).getBatting_team());
            		 if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
            			 teams.setP2_Run(matchStats.getAwaySecondPowerPlay().getTotalRuns());
                		 teams.setP3_Run(matchStats.getAwayThirdPowerPlay().getTotalRuns());	
                		 teams.setP2_Wicket(matchStats.getAwaySecondPowerPlay().getTotalWickets());
                		 teams.setP3_Wicket(matchStats.getAwayThirdPowerPlay().getTotalWickets());
            		 }
                	 powerplay.put(inn.getBatting_team().getTeamName4(),teams);
                 }
	        }
		return powerplay;	
	}
	public static List<String> getTeamScore(List<Team> team, List<HeadToHeadTeam> headToHead, MatchAllData match) {
	    List<String> teamSortedData = new ArrayList<>();

	    for (Team tm : team) {
	        String teamName = tm.getTeamName4();
	        int highestScore = Integer.MIN_VALUE;
	        int lowestScore = Integer.MAX_VALUE;
	        boolean matchPlayed = false;

	        // Step 1: From HeadToHead data
	        for (HeadToHeadTeam h2h : headToHead) {
	        	
	            if (h2h.getTeam().getTeamName4().equalsIgnoreCase(teamName) && h2h.getTeamRuns() > 0) {
	                int currentScore = h2h.getTeamRuns();
	                
	                highestScore = Math.max(highestScore, currentScore);
	                lowestScore = Math.min(lowestScore, currentScore);
	                matchPlayed = true;
	            }
	        }

	        // Step 2: Check current match innings
	        List<Inning> innings = match.getMatch().getInning();
	        for (Inning in : innings) {
	            if (in.getBatting_team().getTeamName4().equalsIgnoreCase(teamName) && in.getTotalRuns() > 0) {
	                int matchScore = in.getTotalRuns(); // Assuming getRuns() gives total runs
	                highestScore = Math.max(highestScore, matchScore);
	                lowestScore = Math.min(lowestScore, matchScore);
	                matchPlayed = true;
	            }
	        }

	        // Step 3: If no match played in any data, set to 0
	        if (!matchPlayed) {
	            highestScore = 0;
	            lowestScore = 0;
	        }

	        teamSortedData.add(teamName + " - " + highestScore + " - " + lowestScore);
	    }
	    return teamSortedData;
	}
	
	public static void processLeaderBoard(CricketService CricketService ,LeaderBoard leader) {
			leader.setPlayer1(CricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(leader.getPlayer1Id())));
			leader.setPlayer2(CricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(leader.getPlayer2Id())));
			leader.setPlayer3(CricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(leader.getPlayer3Id())));
			leader.setPlayer4(CricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(leader.getPlayer4Id())));
			leader.setPlayer5(CricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(leader.getPlayer5Id())));
			for(Team tm:CricketService.getTeams()) {
				if(tm.getTeamId()==leader.getPlayer1().getTeamId()) {
					leader.setTeam1(tm);
				}
				if(tm.getTeamId()==leader.getPlayer2().getTeamId()) {
					leader.setTeam2(tm);
				}
				if(tm.getTeamId()==leader.getPlayer3().getTeamId()) {
					leader.setTeam3(tm);
				}
				if(tm.getTeamId()==leader.getPlayer4().getTeamId()) {
					leader.setTeam4(tm);
				}
				if(tm.getTeamId()==leader.getPlayer5().getTeamId()) {
					leader.setTeam5(tm);
				}
			}
	}
	public static String findConsecutiveDupicateEvents(List<Event> allEvents, Event currentEvent)
	{
		if(allEvents.size() > 0) {
			Event last_event = allEvents.get(allEvents.size()-1);
			last_event.setEventNumber(currentEvent.getEventNumber());
			if(JSONObject.fromObject(last_event).toString().equals(JSONObject.fromObject(currentEvent).toString())) {
				return CricketUtil.BACK_TO_BACK + " " + currentEvent.getEventType();
			}
		}
		return "";
	}
	public static String isDeclared(Inning inn)
	{
	    return inn.getIsCurrentInning() != null && inn.getIsDeclared() != null && inn.getIsDeclared()
	    		 .equalsIgnoreCase(CricketUtil.YES) ? " dec" : "";
	}
	public static List<Partnership> ConcussedPartnership(Match match ,int inn_num) throws Exception{
		
		List<Partnership> Partnership = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(
				match.getInning().get(inn_num - 1).getPartnerships()), new TypeReference<List<Partnership>>() {});
		
		List<BattingCard> BattingCard = match.getInning().get(inn_num - 1).getBattingCard().stream().filter(b->
				b.getHowOut()!=null && (b.getHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED) ||
				b.getHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)||
				b.getHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)))
				.collect(Collectors.toList());
		
		int i=1;
		for(int j=0; j< Partnership.size(); j++) {
			
			final Partnership current = Partnership.get(j);
		    
		    final int firstBatterNo = current.getFirstBatterNo();
		    final int secondBatterNo = current.getSecondBatterNo();
		    
			if(j < Partnership.size()-1) {
				final Partnership next = Partnership.get(j + 1);
			    
			    boolean firstMatch = BattingCard.stream()
			            .anyMatch(bc -> bc.getPlayerId() == firstBatterNo);

		        boolean secondMatch = BattingCard.stream()
		            .anyMatch(bc -> bc.getPlayerId() == secondBatterNo);
		        
		        if (firstMatch && (next.getFirstBatterNo() != firstBatterNo)) {
		            current.setPartnershipNumber(0);
		        } else if (secondMatch && (next.getSecondBatterNo() != secondBatterNo)) {
		            current.setPartnershipNumber(0);
		        } else {
		            current.setPartnershipNumber(i);
		            i++;
		        }
			}else {
				current.setPartnershipNumber(i);
	            i++;
			}
		}
		return Partnership;
	}
	public static List<Map<String, String>> readExcelToMap() {
        List<Map<String, String>> dataList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File("C:\\Sports\\Cricket\\KeyPlayers.xlsx"));
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0); // Read first sheet
            Iterator<Row> rowIterator = sheet.iterator();

            // Read headers (first row) and ignore the first column (index)
            Row headerRow = rowIterator.next();
            List<String> headers = new ArrayList<>();
            for (int i = 1; i < headerRow.getLastCellNum(); i++) { // Start from 1 to skip index
                headers.add(headerRow.getCell(i).getStringCellValue());
            }

            // Read remaining rows as values
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new LinkedHashMap<>();

                for (int i = 1; i < headers.size() + 1; i++) { // Start from 1 to skip index
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i - 1), cell.toString()); // Map column name to value
                }
                dataList.add(rowData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
	
	public static Map<String, Map<String, Object>> ReadExcel(String Path) {

        Map<String, Map<String, Object>> dataMap = new LinkedHashMap<>();

        try (InputStream inputStream = new FileInputStream(Path);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); 
            Row headerRow = sheet.getRow(0); // Read the header row

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { 
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                    String key = getCellValueAsString(row.getCell(0)).trim();
                    if (!key.isEmpty()) {
                        Map<String, Object> rowData = new LinkedHashMap<>();

                        for (int j = 1; j < row.getLastCellNum(); j++) {
                            String header = getCellValueAsString(headerRow.getCell(j)).trim();
                            Object cellValue = getCellValue(row.getCell(j));
                            if (cellValue != null && !cellValue.toString().isEmpty()) {
                                rowData.put(header, cellValue);
                            }
                        }
                        dataMap.put(key, rowData);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		return dataMap;
    }
	
	public static Map<String, Object> Read_Excel(String Path) {

        Map<String, Object> rowData = new LinkedHashMap<>();

        try (InputStream inputStream = new FileInputStream(Path);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0); 
            Row headerRow = sheet.getRow(0); // Read the header row
            
            for (int i = 0; i <= sheet.getLastRowNum(); i++) { 
                Row row = sheet.getRow(i);
                if (row != null && row.getCell(0) != null) {
                	for (int j = 0; j < row.getLastCellNum(); j++) {
                            String header = getCellValueAsString(headerRow.getCell(j)).trim();
                            Object cellValue = getCellValue(row.getCell(j));
                            if (cellValue != null && !cellValue.toString().isEmpty()) {
                                rowData.put(header, cellValue);
                            }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
		return rowData;
    }
	private static String getCellValueAsString(Cell cell) {
	        Object value = getCellValue(cell);
	        return value == null ? "" : value.toString();
	    }
	private static Object getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return (long) numericValue; 
                    } else {
                        return numericValue;
                    }
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return "Unknown cell type";
        }
    }
    
	public static Match processInningTimeData(String whatToProcess, Match matchData, String timeStatsToProcess, Match lastMatchData) 
	{
		if(matchData != null && matchData.getInning() != null && matchData.getInning().size() > 0)
		{
			for (Inning this_inn : matchData.getInning()) {
				
				if(this_inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES) && this_inn.getInningStatus().equalsIgnoreCase(CricketUtil.START)) {
					
					switch (whatToProcess) {
					case "PROCESS_TIME_STATS":
						
						if(!timeStatsToProcess.isEmpty() && timeStatsToProcess.split(",").length >= 4) {
							
							this_inn.setDuration(Integer.valueOf(timeStatsToProcess.split(",")[0]));
							if(matchData.getDaysSessions() != null) {
								for(DaySession ds : matchData.getDaysSessions()) {
									if(ds.getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES)) {
										ds.setTotalSeconds(Integer.valueOf(timeStatsToProcess.split(",")[0]));
									}
								}
							}		
							
							if(this_inn.getInningStats() == null) {
								this_inn.setInningStats(new InningStats());
							}
							if(this_inn.getInningStats().getBallsPerHour() == null) {
								this_inn.getInningStats().setBallsPerHour(new ArrayList<Integer>());
							}

							if(this_inn.getDuration() / 3600 > this_inn.getInningStats().getBallsPerHour().size()) {
								this_inn.getInningStats().getBallsPerHour().add(6 * this_inn.getTotalOvers() + this_inn.getTotalBalls());
							}

							this_inn.getInningStats().setTimeSinceLastBoundary(Integer.valueOf(timeStatsToProcess.split(",")[1]));
							this_inn.getInningStats().setTimeSinceLastRun(Integer.valueOf(timeStatsToProcess.split(",")[2]));
							this_inn.getInningStats().setTimeSinceLastRunOffBat(Integer.valueOf(timeStatsToProcess.split(",")[3]));
							
							if(timeStatsToProcess.split(",").length >= 5) {
								if(this_inn.getBattingCard() != null && this_inn.getBattingCard().size() > 0) {
									List<BattingCard> this_bcs = this_inn.getBattingCard().stream().filter(
										bc -> bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)).collect(Collectors.toList());
									for (BattingCard bc : this_bcs) {
										for(int bc_index = 4; bc_index <= timeStatsToProcess.split(",").length-1; bc_index++) {
											if(timeStatsToProcess.split(",")[bc_index].contains(String.valueOf(bc.getPlayerId()) + "_")) {
												bc.setDuration(Integer.valueOf(timeStatsToProcess.split(",")[bc_index].split("_")[1]));
											}
										}
									}
								}
							}
							
						}
						if(lastMatchData != null && lastMatchData.getInning() != null && lastMatchData.getInning().size() > 0)
						{
							Inning last_inn = lastMatchData.getInning().stream().filter(
								inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES) 
								&& inn.getInningStatus().equalsIgnoreCase(CricketUtil.START)).findAny().orElse(null);

							if(last_inn != null) {
								if(last_inn.getInningStats() == null) {
									last_inn.setInningStats(new InningStats());
								}
								if(last_inn.getTotalRuns() != this_inn.getTotalRuns()) {
									this_inn.getInningStats().setTimeSinceLastRun(0);
								}
								if(last_inn.getTotalFours() != this_inn.getTotalFours()
									|| last_inn.getTotalSixes() != this_inn.getTotalSixes()) {
									this_inn.getInningStats().setTimeSinceLastBoundary(0);
								}
								if(last_inn.getBattingCard() != null && this_inn.getBattingCard() != null
									&& last_inn.getBattingCard().size() > 0 && this_inn.getBattingCard().size() > 0) {
									for (BattingCard last_bc : last_inn.getBattingCard()) {
										if(last_bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											for (BattingCard this_bc : this_inn.getBattingCard()) {
												if(this_bc.getPlayerId() == last_bc.getPlayerId() 
													&& this_bc.getRuns() != last_bc.getRuns()) {
													this_inn.getInningStats().setTimeSinceLastRunOffBat(0);
												}
											}
										}
									}
								}
							}
						}
						break;
					}
				}
			}
		}
		return matchData;
	}
	public static AE_Cricket getDataFromThirdParty(String FilePathName) throws JAXBException {
		AE_Cricket cricket_data =(AE_Cricket) JAXBContext.newInstance(AE_Cricket.class)
		.createUnmarshaller().unmarshal(new File(FilePathName));
		return cricket_data;
	}
	public static AE_Last_Ball getSpeed_of_ball_from_ThirdParty(String FilePathName) throws JAXBException {
		AE_Last_Ball cricket_data =(AE_Last_Ball)JAXBContext.newInstance(AE_Last_Ball.class)
		.createUnmarshaller().unmarshal(new File(FilePathName));
		
	return cricket_data;
}
//	public static MatchAllData getMatchDataFromWebsite(WebDriver driver, String whatToProcess, 
//		String broadcaster, String valueToProcess, List<Team> all_teams) throws StreamWriteException, DatabindException, JAXBException, IOException, URISyntaxException
//	{
//		List<BattingCard> this_battingcard = new ArrayList<BattingCard>();
//		List<FallOfWicket> this_FoWs = new ArrayList<FallOfWicket>();
//		List<Inning> this_inn = new ArrayList<Inning>();
//		List<BowlingCard> this_bowlingcard = new ArrayList<BowlingCard>();
//		Player this_player = new Player();
//		WebElement this_webElement;
//		int column_data_count = 0,bowling_card_count = 1;
//		boolean extras_found = false, total_found = false;
//		String data_to_process = "";
//
//		MatchAllData this_match = new MatchAllData();
//		this_match.setMatch(new Match());
//		this_match.setSetup(new Setup());
//		
//		if(valueToProcess.toUpperCase().contains("-" + CricketUtil.TEST + "-")) {
//			this_match.getSetup().setMatchType(CricketUtil.TEST);
//			this_match.getSetup().setMaxOvers(CricketUtil.TEST_MAXIMUM_OVERS);
//		} else if(valueToProcess.toUpperCase().contains("-" + CricketUtil.ODI + "-") || valueToProcess.toUpperCase().contains("-" + CricketUtil.OD + "-")) {
//			this_match.getSetup().setMatchType(CricketUtil.ODI);
//			this_match.getSetup().setMaxOvers(CricketUtil.ODI_MAXIMUM_OVERS);
//		} else {
//			this_match.getSetup().setMatchType(CricketUtil.IT20);
//			this_match.getSetup().setMaxOvers(CricketUtil.T20_MAXIMUM_OVERS);
//		}
//		this_match.getSetup().setSaveMatchFileAs("JSON");
//		this_match.getSetup().setMatchIdent(valueToProcess.split("/")[valueToProcess.split("/").length-2]);
//		this_match.getSetup().setTournament(valueToProcess.split("/")[valueToProcess.split("/").length-2]);
//		switch (broadcaster.toUpperCase()) {
//		case CricketUtil.CRIC_INFO:
//			
//			switch (whatToProcess) {
//			case "GET-SINGLE-MATCH-DATA":
//				
//				driver.get(valueToProcess);
//				
//				for (WebElement this_team : driver.findElements(By.className("ci-team-score"))) {
//					if(this_match.getMatch().getMatchResult() == null) {
//						this_webElement = this_team.findElement(By.xpath("../../.."));
//						if(!this_webElement.findElements(By.xpath("./p/span")).isEmpty()) {
//							this_match.getMatch().setMatchResult(this_webElement.findElement(By.xpath("./p/span")).getText());
//						}
//					}
//					//column_data_count = 0;
//					for (WebElement team_anchor : this_team.findElements(By.tagName("a"))) {
//						if(team_anchor.getAttribute("href").contains("/team/")
//							&& team_anchor.getAttribute("href").contains("-")) {
//							data_to_process = team_anchor.getAttribute("href").split("/")[team_anchor.getAttribute("href").split("/").length-1];
//							for (Team team : all_teams) {
//								if(team_anchor.findElement(By.tagName("span")).getText().toLowerCase().contains(team.getTeamName1().toLowerCase())
//										|| team_anchor.findElement(By.tagName("span")).getText().toLowerCase().contains(team.getTeamName4().toLowerCase())) {
//									
//									column_data_count = column_data_count + 1;
//									
//									switch (column_data_count) {
//									case 1:
//										this_match.getSetup().setHomeTeam(team);
//										this_match.getSetup().getHomeTeam().setTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
//										this_match.getSetup().setHomeTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
//										break;
//									case 2:
//										this_match.getSetup().setAwayTeam(team);
//										this_match.getSetup().getAwayTeam().setTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
//										this_match.getSetup().setAwayTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
//										break;
//									}
//									break;
//								}
//							}
//						}
//						if(column_data_count > 2) {
//							break;
//						}
//					}
//				}
//				
//				for (WebElement batting_card_table : driver.findElements(By.className("ci-scorecard-table"))) {
//
//					this_inn.add(new Inning()); // Add new inning when batting card is detected
//					this_inn.get(this_inn.size()-1).setInningNumber(this_inn.size());
//					this_battingcard = new ArrayList<BattingCard>();
//					
//					for(WebElement row : batting_card_table.findElements(By.xpath("./tbody/tr")))
//					{
//						column_data_count = 0;
//						for(WebElement column : row.findElements(By.tagName("td")))
//						{
//							if(column.getText().equalsIgnoreCase(CricketUtil.EXTRAS)) {
//								extras_found = true;
//							} else if(column.getText().equalsIgnoreCase(CricketUtil.TOTAL)) {
//								total_found = true;
//							}
//							if(extras_found == true) {
//								
//								if(!column.getText().isEmpty() && column.getText().contains("(")
//									&& column.getText().contains(")")) {
//									
//									data_to_process = column.getText().replace("(", "");
//									data_to_process = data_to_process.replace(")", "");
//									if(data_to_process.contains(",")) {
//										for (String ext : data_to_process.split(",")) {
//											if(ext.toUpperCase().contains("LB")) {
//												this_inn.get(this_inn.size()-1).setTotalLegByes(
//													Integer.valueOf(ext.toUpperCase().replace("LB", "").trim()));
//											}else if(ext.toUpperCase().contains("NB")) {
//												this_inn.get(this_inn.size()-1).setTotalNoBalls(
//													Integer.valueOf(ext.toUpperCase().replace("NB", "").trim()));
//											}else if(ext.toUpperCase().contains("B")) {
//												this_inn.get(this_inn.size()-1).setTotalByes(
//													Integer.valueOf(ext.toUpperCase().replace("B", "").trim()));
//											}else if(ext.toUpperCase().contains("W")) {
//												this_inn.get(this_inn.size()-1).setTotalWides(
//													Integer.valueOf(ext.toUpperCase().replace("W", "").trim()));
//											}
//										}
//									} else {
//										if(data_to_process.toUpperCase().contains("LB")) {
//											this_inn.get(this_inn.size()-1).setTotalLegByes(
//												Integer.valueOf(data_to_process.toUpperCase().replace("LB", "").trim()));
//										}else if(data_to_process.toUpperCase().contains("NB")) {
//											this_inn.get(this_inn.size()-1).setTotalNoBalls(
//												Integer.valueOf(data_to_process.toUpperCase().replace("NB", "").trim()));
//										}else if(data_to_process.toUpperCase().contains("B")) {
//											this_inn.get(this_inn.size()-1).setTotalByes(
//												Integer.valueOf(data_to_process.toUpperCase().replace("B", "").trim()));
//										}else if(data_to_process.toUpperCase().contains("W")) {
//											this_inn.get(this_inn.size()-1).setTotalWides(
//												Integer.valueOf(data_to_process.toUpperCase().replace("W", "").trim()));
//										}
//									}
//									
//								} else if(!column.findElements(By.tagName("strong")).isEmpty()) {
//									
//									this_inn.get(this_inn.size()-1).setTotalExtras(
//										Integer.valueOf(column.getText()));
//									
//									extras_found = false;
//									
//								} 
//
//								
//							} else if(total_found == true) {
//								
//								if(!column.findElements(By.tagName("span")).isEmpty()) {
//									for (WebElement this_span : column.findElements(By.tagName("span"))) {
//										if(this_span.getText().toUpperCase().contains(" OV") && !this_span.getText().toUpperCase().contains(",")) {
//											data_to_process = this_span.getText().toUpperCase().replace("OV", "").trim();
//											if(data_to_process.contains(".") && !data_to_process.contains(",")) {
//												this_inn.get(this_inn.size()-1).setTotalOvers(
//													Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("."))));
//												this_inn.get(this_inn.size()-1).setTotalBalls(
//														Integer.valueOf(data_to_process.substring(data_to_process.indexOf(".")+1)));
//											}else if(!data_to_process.contains(",")) {
//												this_inn.get(this_inn.size()-1).setTotalOvers(
//													Integer.valueOf(data_to_process));
//												this_inn.get(this_inn.size()-1).setTotalBalls(0);
//											}
//										} else {
//											if(this_span.getText().toUpperCase().contains("(RR:")) {
//												data_to_process = this_span.getText().toUpperCase().replace("(RR:", "");
//												data_to_process = data_to_process.replace(")", "").trim();
//												this_inn.get(this_inn.size()-1).setRunRate(data_to_process);
//											}
//										}
//									}
//								} else {
//									data_to_process = column.getText().trim();
//									if(data_to_process.contains("/")) {
//										this_inn.get(this_inn.size()-1).setTotalRuns(
//											Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("/"))));
//										this_inn.get(this_inn.size()-1).setTotalWickets(
//											Integer.valueOf(data_to_process.substring(data_to_process.indexOf("/")+1)));
//										total_found = false;
//									} else {
//										if(NumberUtils.isCreatable(data_to_process)) {
//											this_inn.get(this_inn.size()-1).setTotalRuns(Integer.valueOf(data_to_process));
//											this_inn.get(this_inn.size()-1).setTotalWickets(this_battingcard.size()-1);
//											total_found = false;
//										}
//									}
//								}
//								
//							} else { // Batting card data
//								
//								if(column.getText().toUpperCase().contains("FALL OF WICKETS:")) {
//									
//									data_to_process = column.getText().toUpperCase().replace("FALL OF WICKETS:", "").trim();
//									for(int i=0; i < data_to_process.split(",").length; i++) {
//										
//										for (BattingCard bc : this_battingcard) {
//
//											if(bc.getPlayer() != null && bc.getPlayer().getFull_name() != null
//												&& bc.getPlayer().getFull_name().equalsIgnoreCase(data_to_process.split(",")[i].substring(
//														data_to_process.split(",")[i].indexOf("(") + 1))) {
//												
//												this_FoWs.add(new FallOfWicket(Integer.valueOf(data_to_process.split(",")[i].substring(0, 
//													data_to_process.split(",")[i].indexOf("-")).trim()), 
//													bc.getPlayer().getPlayerId(), Integer.valueOf(data_to_process.split(",")[i].substring(
//													data_to_process.split(",")[i].indexOf("-") + 1, data_to_process.split(",")[i].indexOf("(") 
//													- data_to_process.split(",")[i].indexOf("-") + 1).trim()), 
//													Integer.valueOf(data_to_process.split(",")[i+1].toUpperCase()
//													.replace("OV)", "").substring(0,data_to_process.split(",")[i+1].indexOf(".")).trim()), 
//													Integer.valueOf(data_to_process.split(",")[i+1].toUpperCase()
//													.replace("OV)", "").substring(data_to_process.split(",")[i+1].indexOf(".")+1).trim()), ""));
//												i++;
//												break;
//											}
//										}										
//										
//									}
//								} else if(column.getText().toUpperCase().contains("DID NOT BAT:")) {
//									for (WebElement did_not_bat_anchor : column.findElements(By.tagName("a"))) {
//										if(did_not_bat_anchor.getAttribute("href").contains("/cricketers/")
//												&& did_not_bat_anchor.getAttribute("href").contains("-")) {
//											this_player = new Player();
//											this_player.setPlayerId(Integer.valueOf(did_not_bat_anchor.getAttribute("href").split("-")[
//											    did_not_bat_anchor.getAttribute("href").split("-").length-1]));
//											this_player.setFull_name(did_not_bat_anchor.findElement(
//												By.xpath("./span/span")).getText().replace(",", "").trim());
//											if(did_not_bat_anchor.findElement(
//													By.xpath("./span/span")).getText().replace(",", "").trim().contains(" ")) {
//												this_player.setFirstname(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[0]);
//												this_player.setSurname(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[1]);
//												this_player.setTicker_name(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[1]);
//											}else {
//												this_player.setFirstname(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim());
//												this_player.setSurname(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim());
//												this_player.setTicker_name(did_not_bat_anchor.findElement(
//														By.xpath("./span/span")).getText().replace(",", "").trim());
//											}
//											
//											this_battingcard.add(new BattingCard(this_player.getPlayerId(), 
//												this_battingcard.size() + 1,CricketUtil.STILL_TO_BAT));
//											this_battingcard.get(this_battingcard.size()-1).setPlayer(this_player);
//										}
//									}
//								} else {
//									if(!column.findElements(By.tagName("a")).isEmpty()) {
//										if(column.findElement(By.tagName("a")).getAttribute("href").contains("/cricketers/")
//											&& column.findElement(By.tagName("a")).getAttribute("href").contains("-")) {
//											this_battingcard.add(new BattingCard(Integer.valueOf(column.findElement(
//												By.tagName("a")).getAttribute("href").split("-")[
//												column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]), 
//												this_battingcard.size() + 1,CricketUtil.STILL_TO_BAT));
//											this_player = new Player();
//											this_player.setPlayerId(Integer.valueOf(column.findElement(
//												By.tagName("a")).getAttribute("href").split("-")[
//												column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]));
//											this_player.setFull_name(column.findElement(
//													By.tagName("a")).getAttribute("title"));
//											if(column.findElement(
//													By.tagName("a")).getAttribute("title").contains(" ")) {
//												this_player.setFirstname(column.findElement(
//														By.tagName("a")).getAttribute("title").split(" ")[0]);
//												this_player.setSurname(column.findElement(
//														By.tagName("a")).getAttribute("title").split(" ")[1]);
//												this_player.setTicker_name(column.findElement(
//														By.tagName("a")).getAttribute("title").split(" ")[1]);
//											}else {
//												this_player.setFirstname(column.findElement(
//														By.tagName("a")).getAttribute("title"));
//												this_player.setSurname(column.findElement(
//														By.tagName("a")).getAttribute("title"));
//												this_player.setTicker_name(column.findElement(
//														By.tagName("a")).getAttribute("title"));
//											}
//											
//											this_battingcard.get(this_battingcard.size()-1).setPlayer(this_player);
//
//											column_data_count = 0;
//											
//										}
//										
//									} else if(!column.findElements(By.xpath("./span/span")).isEmpty()) {
//
//										data_to_process = column.findElement(By.tagName("span")).findElement(
//												By.tagName("span")).getText().replaceAll("[^a-zA-Z0-9\\s\\/]", "");
//										
//										if(data_to_process.equalsIgnoreCase("not out")) {
//											this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.NOT_OUT);
//										} else if(!data_to_process.isEmpty()) {
//
//											this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.OUT);
//											
//											this_player = new Player();
//											
//											if(data_to_process.toLowerCase().contains("c ") && data_to_process.toLowerCase().contains(" b ") && 
//												!data_to_process.toLowerCase().contains("c & b")) {
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.CAUGHT);
//												if(data_to_process.toLowerCase().contains(" b ")) {
//													this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(data_to_process.toLowerCase().split(" b ")[0]);
//													this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + data_to_process.toLowerCase().split(" b ")[1]);
//													this_player.setFull_name(data_to_process.toLowerCase().split(" b ")[1]);
//													if(data_to_process.toLowerCase().split(" b ")[1].contains(" ")) {
//														this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[0]);
//														this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
//														this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
//													}else {
//														this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1]);
//														this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1]);
//														this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1]);
//													}
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												
//											}else if(data_to_process.toLowerCase().contains("c & b")) {
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.CAUGHT_AND_BOWLED);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne("c & b");
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo(data_to_process.split("c & b ")[1]);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
//												this_player.setFull_name(data_to_process.split("c & b ")[1]);
//												if(data_to_process.toLowerCase().split("c & b ")[1].contains(" ")) {
//													this_player.setFirstname(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[0]);
//													this_player.setSurname(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[1]);
//												}else {
//													this_player.setFirstname(data_to_process.toLowerCase().split("c & b ")[1]);
//													this_player.setSurname(data_to_process.toLowerCase().split("c & b ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("c & b ")[1]);
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												
//											}else if(data_to_process.toLowerCase().contains("b ")) {
//												this_player.setFull_name(data_to_process.split("b ")[1]);
//												if(data_to_process.toLowerCase().split("b ")[1].contains(" ")) {
//													this_player.setFirstname(data_to_process.toLowerCase().split("b ")[1].split(" ")[0]);
//													this_player.setSurname(data_to_process.toLowerCase().split("b ")[1].split(" ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("b ")[1].split(" ")[1]);
//												}else {
//													this_player.setFirstname(data_to_process.toLowerCase().split("b ")[1]);
//													this_player.setSurname(data_to_process.toLowerCase().split("b ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("b ")[1]);
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.BOWLED);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne("");
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
//												
//											}else if(data_to_process.toLowerCase().contains("lbw b ")) { // lbw
//												
//												this_player.setFull_name(data_to_process.split("lbw b ")[1]);
//												if(data_to_process.toLowerCase().split("lbw b ")[1].contains(" ")) {
//													this_player.setFirstname(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[0]);
//													this_player.setSurname(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[1]);
//												}else {
//													this_player.setFirstname(data_to_process.toLowerCase().split("lbw b ")[1]);
//													this_player.setSurname(data_to_process.toLowerCase().split("lbw b ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split("lbw b ")[1]);
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.LBW);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(CricketUtil.LBW);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
//												
//											}else if(data_to_process.toLowerCase().contains("st ") && data_to_process.toLowerCase().contains(" b ")) {
//												
//												this_player.setFull_name(data_to_process.split(" b ")[1]);
//												if(data_to_process.toLowerCase().split(" b ")[1].contains(" ")) {
//													this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[0]);
//													this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
//												}else {
//													this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1]);
//													this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1]);
//													this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1]);
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.STUMPED);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(data_to_process.split(" b ")[0]);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
//												
//											}else if(data_to_process.toLowerCase().contains("run out")) {
//												
//												data_to_process = data_to_process.toLowerCase().split("run out ")[1]
//													.replace("(", "").replace(")", ""); 
//												this_player.setFirstname("");
//												if(data_to_process.toLowerCase().contains("/")) {
//													this_player.setFull_name(data_to_process.split("/")[1]);
//													this_player.setSurname(data_to_process.split("/")[1]);
//													this_player.setTicker_name(data_to_process.split("/")[1]);
//												} else {
//													this_player.setFull_name(data_to_process);
//													this_player.setSurname(data_to_process);
//													this_player.setTicker_name(data_to_process);
//												}
//												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
//												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.RUN_OUT);
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(CricketUtil.RUN_OUT.replace("_", " "));
//												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo(data_to_process);
//											}
//										} 
//										
//									} else if(!column.findElements(By.tagName("strong")).isEmpty()) {
//										
//										this_battingcard.get(this_battingcard.size()-1).setRuns(Integer.valueOf(
//											column.findElement(By.tagName("strong")).getText()));
//
//									} else {
//										
//										if(this_inn.get(this_inn.size()-1).getTotalRuns() <= 0 && this_inn.get(this_inn.size()-1).getTotalWickets() <= 0 &&
//											this_inn.get(this_inn.size()-1).getTotalOvers() <= 0 && this_inn.get(this_inn.size()-1).getTotalBalls() <= 0) {
//											
//											if(column.getText().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//												this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.NOT_OUT);
//											}else {
//												column_data_count++;
//												data_to_process = column.getText().trim();
//												if(data_to_process.isEmpty()) {
//													if(column_data_count == 5) {
//														data_to_process = "0.0";
//													} else {
//														data_to_process = "0";
//													}
//												}
//												switch (column_data_count) {
//												case 1:
//													this_battingcard.get(this_battingcard.size()-1).setBalls(
//														Integer.valueOf(data_to_process));
//													break;
//												case 2: // Minutes
//													break;
//												case 3:
//													this_battingcard.get(this_battingcard.size()-1).setFours(
//														Integer.valueOf(data_to_process));
//													break;
//												case 4:
//													this_battingcard.get(this_battingcard.size()-1).setSixes(
//														Integer.valueOf(data_to_process));
//													break;
//												case 5:
//													this_battingcard.get(this_battingcard.size()-1).setStrikeRate(data_to_process);
//													break;
//												}
//											}
//											
//										}
//									}
//								}
//							}
//						}
//					}
//					switch (this_inn.size()) {
//					case 1:
//						if(this_match.getSetup().getHomeTeam() != null) {
//							this_inn.get(this_inn.size() - 1).setBatting_team(this_match.getSetup().getHomeTeam());
//							this_inn.get(this_inn.size() - 1).setBattingTeamId(this_match.getSetup().getHomeTeamId());
//							this_inn.get(this_inn.size() - 1).setBowling_team(this_match.getSetup().getAwayTeam());
//							this_inn.get(this_inn.size() - 1).setBowlingTeamId(this_match.getSetup().getAwayTeamId());
//						}
//						break;
//					case 2:
//						if(this_match.getSetup().getAwayTeam() != null) {
//							this_inn.get(this_inn.size() - 1).setBatting_team(this_match.getSetup().getAwayTeam());
//							this_inn.get(this_inn.size() - 1).setBattingTeamId(this_match.getSetup().getAwayTeamId());
//							this_inn.get(this_inn.size() - 1).setBowling_team(this_match.getSetup().getHomeTeam());
//							this_inn.get(this_inn.size() - 1).setBowlingTeamId(this_match.getSetup().getHomeTeamId());
//						}
//						break;
//					}
//					for (BattingCard bc : this_battingcard) {
//						this_inn.get(this_inn.size() - 1).setTotalFours(this_inn.get(this_inn.size() - 1).getTotalFours() + bc.getFours());
//						this_inn.get(this_inn.size() - 1).setTotalSixes(this_inn.get(this_inn.size() - 1).getTotalSixes() + bc.getSixes());
//					}
//					this_inn.get(this_inn.size() - 1).setBattingCard(this_battingcard);
//					this_inn.get(this_inn.size() - 1).setFallsOfWickets(this_FoWs);
//				}
//				
//				List<Player> home_squad = new ArrayList<Player>();
//				List<Player> away_squad = new ArrayList<Player>();
//				for(int i=0; i<this_inn.size(); i++) {
//					this_inn.get(i).setInningStatus(CricketUtil.PAUSE);
//					if(i == this_inn.size()-1) {
//						this_inn.get(i).setIsCurrentInning(CricketUtil.YES);
//					} else {
//						this_inn.get(i).setIsCurrentInning(CricketUtil.NO);
//					}
//					switch (i) {
//					case 0:
//						home_squad = new ArrayList<Player>();
//						for (BattingCard bc : this_inn.get(i).getBattingCard()) {
//							home_squad.add(bc.getPlayer());
//						}
//						if(this_inn.size() > 1) {
//							away_squad = new ArrayList<Player>();
//							for (BattingCard bc : this_inn.get(i+1).getBattingCard()) {
//								away_squad.add(bc.getPlayer());
//							}
//						}
//						break;
//					}
//				}
//				if(home_squad.size() > 0) {
//					this_match.getSetup().setHomeSquad(home_squad);
//				}
//				if(away_squad.size() > 0) {
//					this_match.getSetup().setAwaySquad(away_squad);
//				}
//				for (WebElement bowling_card_table : driver.findElements(By.tagName("table"))) {
//					
//					data_to_process = "";
//					if(bowling_card_table.findElements(By.xpath("./thead/tr/th")).size() > 0) {
//						for (WebElement table_header : bowling_card_table.findElements(By.xpath("./thead/tr/th"))) {
//							data_to_process = table_header.getText().toUpperCase().trim();
//							break;
//						}
//					}
//					if(data_to_process.equalsIgnoreCase("BOWLING")) {
//						
//						this_bowlingcard = new ArrayList<BowlingCard>();
//						
//						for(WebElement row : bowling_card_table.findElements(By.xpath("./tbody/tr")))
//						{
//							if(!row.getAttribute("class").toLowerCase().contains("ds-hidden")) {
//								column_data_count = 0;
//								for(WebElement column : row.findElements(By.tagName("td")))
//								{
//									if(!column.findElements(By.tagName("a")).isEmpty())  
//									{
//										if(column.findElement(By.tagName("a")).getAttribute("href").contains("/cricketers/")
//											&& column.findElement(By.tagName("a")).getAttribute("href").contains("-")) {
//											
//											this_bowlingcard.add(new BowlingCard(Integer.valueOf(column.findElement(
//													By.tagName("a")).getAttribute("href").split("-")[
//													column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]), 
//													this_bowlingcard.size() + 1, CricketUtil.LAST, 0));
//											
//											this_player = new Player();
//											this_player.setPlayerId(Integer.valueOf(column.findElement(
//													By.tagName("a")).getAttribute("href").split("-")[
//													column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]));
//											this_player.setFull_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
//											if(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().contains(" ")) {
//												this_player.setFirstname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[0]);
//												this_player.setSurname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[1]);
//												this_player.setTicker_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[1]);
//											}else {
//												this_player.setFirstname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
//												this_player.setSurname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
//												this_player.setTicker_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
//											}
//											
//											this_bowlingcard.get(this_bowlingcard.size()-1).setPlayer(this_player);
//											
//											column_data_count = 0;
//											
//										}
//									} else {
//										
//										column_data_count++;
//										data_to_process = column.getText().trim();
//										if(data_to_process.isEmpty()) {
//											if(column_data_count == 5) {
//												data_to_process = "0.0";
//											} else {
//												data_to_process = "0";
//											}
//										}
//										switch (column_data_count) {
//										case 1:
//											if(column.getText().contains(".")) {
//												this_bowlingcard.get(this_bowlingcard.size()-1).setOvers(
//													Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("."))));
//												this_bowlingcard.get(this_bowlingcard.size()-1).setBalls((
//													Integer.valueOf(data_to_process.substring(data_to_process.indexOf(".")+1))));
//											} else {
//												this_bowlingcard.get(this_bowlingcard.size()-1).setOvers(Integer.valueOf(data_to_process));
//											}
//											break;
//										case 2: 
//											this_bowlingcard.get(this_bowlingcard.size()-1).setMaidens(Integer.valueOf(data_to_process));
//											break;
//										case 3:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setRuns(Integer.valueOf(data_to_process));
//											break;
//										case 4:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setWickets(Integer.valueOf(data_to_process));
//											break;
//										case 5:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setEconomyRate(data_to_process);
//											break;
//										case 6:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setDots(Integer.valueOf(data_to_process));
//											break;
//										case 9:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setWides(Integer.valueOf(data_to_process));
//											break;
//										case 10:
//											this_bowlingcard.get(this_bowlingcard.size()-1).setNoBalls(Integer.valueOf(data_to_process));
//											break;	
//										}
//									}
//								}
//							}
//						}
//						this_inn.get(bowling_card_count - 1).setBowlingCard(this_bowlingcard);
//						bowling_card_count = bowling_card_count + 1;
//						
//					} else if (data_to_process.equalsIgnoreCase("PLAYER NAME") || data_to_process.equalsIgnoreCase("TEAM")) {
//						break;
//					}
//				}
//				this_match.getMatch().setInning(this_inn);
//				this_match.getMatch().setMatchFileName(valueToProcess.split("/")[valueToProcess.split("/").length-2] + "." + CricketUtil.JSON);
//				this_match.getMatch().setMatchStatus("");
//				break;
//			}
//			break;
//		}
//		readOrSaveMatchFile("WRITE", CricketUtil.SETUP, this_match);
//		readOrSaveMatchFile("WRITE", CricketUtil.MATCH, this_match);
//		
//		return this_match;
//	}
//	public static List<ArchiveData> getStatsFromWebsite(WebDriver driver, String whatToProcess, 
//			String broadcaster, String valueToProcess, CricketService cricketService)
//	{
//		List<ArchiveData> all_stats = new ArrayList<ArchiveData>();
//		String this_url = "",this_team_id ="";
//		List<String> this_teams = new ArrayList<String>();
//		int teams_found_count = 0;
//		
//		switch (broadcaster.toUpperCase()) {
//		case CricketUtil.CRIC_INFO:
//			
//			switch (whatToProcess) {
//			case "GET-SERIES-MATCHES-DATA":
//				
//				driver.get(valueToProcess);
//				this_team_id = valueToProcess.split("/")[valueToProcess.split("/").length-2];
//				this_team_id = this_team_id.split("-")[this_team_id.split("-").length-1];
//				
//				for (Team team : cricketService.getTeams()) {
//					if(valueToProcess.toLowerCase().contains(
//							team.getTeamName1().replace(" ", "-").toLowerCase())) {
//						this_teams.add(team.getTeamName1().replace(" ", "-").toLowerCase());
//					}
//				}
//				for(WebElement anchor : driver.findElements(By.tagName("a")))
//				{
//					teams_found_count = 0;
//					if(anchor.getAttribute("href").toLowerCase().contains("/full-scorecard")
//						|| anchor.getAttribute("href").toLowerCase().contains("/live-cricket-score")) {
//						
//						if(this_teams.size() > 0) {
//							for (String team_str : this_teams) {
//								if(anchor.getAttribute("href").toLowerCase().contains(team_str.toLowerCase())) {
//									teams_found_count++;
//								}
//							}
//						} else if(anchor.getAttribute("href").toLowerCase().contains("/series/")
//							&& anchor.getAttribute("href").toLowerCase().contains(this_team_id + "/")) {
//							teams_found_count = 2;
//						}
//						if(teams_found_count == 2) {
//							this_url = anchor.getAttribute("href").split("/")[anchor.getAttribute("href").split("/").length - 2];
//							if(this_url.contains("-")) {
//								all_stats.add(new ArchiveData(Long.valueOf(this_url.split("-")[this_url.split("-").length-1]), 
//									this_url, anchor.getAttribute("href")));
//							}
//						}
//					}
//				}
//				break;
//				
//			case "GET-SEASON-SERIES-DATA":
//				
//				driver.get("https://www.espncricinfo.com/ci/engine/series/index.html?season=" 
//					+ valueToProcess + ";view=season");
//				for(WebElement section : driver.findElements(By.className("series-summary-block")))
//				{
//					all_stats.add(new ArchiveData(Long.valueOf(section.getAttribute("data-series-id")), 
//						section.findElement(By.tagName("a")).getText(), 
//						section.findElement(By.tagName("a")).getAttribute("href")));
//				}
//				break;
//				
//			case "GET-ALL-SEASONS":
//				
//				driver.get("https://www.espncricinfo.com/ci/engine/series/index.html");
//				for(WebElement section : driver.findElements(By.className("season-links")))
//				{
//					for(WebElement anchor : section.findElements(By.tagName("a")))
//					{
//						all_stats.add(new ArchiveData(0,anchor.getText(), anchor.getAttribute("href")));
//					}
//				}
//				break;
//				
//			}
//			break;
//		}
//		return all_stats;
//	}
	public static MatchAllData readOrSaveMatchFile(String whatToProcess, String whichFileToProcess, MatchAllData match, boolean backUpAllFiles) 
		throws JAXBException, StreamWriteException, DatabindException, IOException, URISyntaxException
	{
		switch (whatToProcess) {
		case CricketUtil.WRITE:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY 
					+ match.getMatch().getMatchFileName()), 
					objectWriter.writeValueAsString(match.getSetup()).getBytes());			
				if(backUpAllFiles == true) {
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
						+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getSetup()).getBytes());			
				}
			}
			if(match.getSetup().getMatchDataUpdate() != null && match.getSetup().getMatchDataUpdate().equalsIgnoreCase(CricketUtil.START)) {
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getEventFile()).getBytes());
					if(backUpAllFiles == true) {
						Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
							+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName()),
							objectWriter.writeValueAsString(match.getEventFile()).getBytes());			
					}
				}
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.MATCH)) {
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getMatch()).getBytes());
					if(backUpAllFiles == true) {
						Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
							+ CricketUtil.MATCHES_DIRECTORY + match.getMatch().getMatchFileName()), 
							objectWriter.writeValueAsString(match.getMatch()).getBytes());			
					}
				}
			}
			break;
		case CricketUtil.READ:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName()).exists() == true) {
					match.setSetup(new ObjectMapper().readValue(new InputStreamReader(new FileInputStream(new File(CricketUtil.CRICKET_DIRECTORY 
						+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName())), StandardCharsets.UTF_8), Setup.class));
				}
			}
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
				if(new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName()).exists() == true) {
					match.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
						+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName()), EventFile.class));
					}
			}
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.MATCH)) {
				match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.MATCHES_DIRECTORY + match.getMatch().getMatchFileName()), Match.class));
			}
			break;
		}
		return match;
	}	
	public static MatchAllData readOrSaveMatchFile(String whatToProcess, String whichFileToProcess, MatchAllData match,Configuration config) 
		throws JAXBException, StreamWriteException, DatabindException, IOException, URISyntaxException
	{
		switch (whatToProcess) {
		case CricketUtil.WRITE:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getSetup()).getBytes());
				
							
				Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
					+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName()), 
					objectWriter.writeValueAsString(match.getSetup()).getBytes());			
			}
			if(match.getSetup().getMatchDataUpdate() != null && match.getSetup().getMatchDataUpdate().equalsIgnoreCase(CricketUtil.START)) {
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
					
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getEventFile()).getBytes());
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
						+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName()),
						objectWriter.writeValueAsString(match.getEventFile()).getBytes());			
				}
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.MATCH)) {
					
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getMatch()).getBytes());
					
					
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY 
						+ CricketUtil.MATCHES_DIRECTORY + match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getMatch()).getBytes());			
				}
			}
			break;
		case CricketUtil.READ:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				
				if (config.getType() == null || config.getType().isEmpty()) {
					if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName().toUpperCase().replace(
						".XML", ".JSON")).exists() == true) {
						match.setSetup(new ObjectMapper().readValue(new InputStreamReader(new FileInputStream(new File(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName())), StandardCharsets.UTF_8), Setup.class));
					}
			    } else {
			    	if(new File(CricketUtil.CRICKET_ARCHIVE_DIRECTORY + CricketUtil.ARCHIVE_MATCHES_DIRECTORY + config.getType() + "/" +
			                CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName().toUpperCase().replace(
						".XML", ".JSON")).exists() == true) {
						match.setSetup(new ObjectMapper().readValue(new InputStreamReader(new FileInputStream(new File(CricketUtil.CRICKET_ARCHIVE_DIRECTORY + CricketUtil.ARCHIVE_MATCHES_DIRECTORY + config.getType() + "/" +
			                CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName())), StandardCharsets.UTF_8), Setup.class));
					}
			    }
			}
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
				if (config.getType() == null || config.getType().isEmpty()) {
					if(new File(CricketUtil.CRICKET_DIRECTORY 
						+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName().toUpperCase().replace(
						".XML", ".JSON")).exists() == true) {
						match.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName()), EventFile.class));
					}
			    }
			}
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.MATCH)) {
				
				if (config.getType() == null || config.getType().isEmpty()) {
					match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.MATCHES_DIRECTORY + match.getMatch().getMatchFileName()), Match.class));
			    } else {
			    	match.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_ARCHIVE_DIRECTORY + CricketUtil.ARCHIVE_MATCHES_DIRECTORY + config.getType() + "/" +
			                CricketUtil.MATCHES_DIRECTORY + match.getMatch().getMatchFileName()), Match.class));
			    }
			}
			break;
		}
		return match;
	}
	public static String deletePreview() throws IOException
    {
		if(new File(CricketUtil.PREVIEW).exists()) {
			Files.delete(Paths.get(CricketUtil.PREVIEW));
		}
        return "";
    }
	
	public static List<Integer> calculateOrderRuns(Inning inning) {
	    int rowId = 0;
	    int topOrder = 0, middleOrder = 0, lowerOrder = 0;

	    for (BattingCard batCard : inning.getBattingCard()) {
	        rowId++;
	        int runs = batCard.getRuns();
	        if (runs > 0) {
	            if (rowId <= 4) {
	                topOrder += runs;
	            } else if (rowId <= 8) {
	                middleOrder += runs;
	            } else if (rowId <= 11) {
	                lowerOrder += runs;
	            }
	        }
	    }

	    return Arrays.asList(topOrder, middleOrder, lowerOrder);
	}
	
	public static FieldersData getFielderFormation(String filePathName) throws IOException {
		FieldersData fielderFormationData = new ObjectMapper().
				readValue(new File(filePathName), FieldersData.class);
          return fielderFormationData;
    }
	public static String readFileAsString(String fileName) throws Exception {
        String data = new String(Files.readAllBytes(Paths.get(fileName)));
        return data;
    }
	 
	public static ImpactData[] getImpactPlayerList(MatchAllData match, CricketService cricketService) {
		ImpactData[] impactData = new ImpactData[2];
		int count = 0;
		for (int i = match.getEventFile().getEvents().size() - 1; i >= 0; i--) {
			if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT)){
				if(count>=2) {
					break;
				}
				ImpactData impactPlayer = new ImpactData();
				if(match.getEventFile().getEvents().get(i).getEventBatterNo() != 0) {
					impactPlayer.setInPlayerId(match.getEventFile().getEvents().get(i).getEventBatterNo());
					impactPlayer.setOutPlayerId(match.getEventFile().getEvents().get(i).getEventOtherBatterNo());
				}
				
				for(Player plyr : cricketService.getAllPlayer()) {
					if(impactPlayer.getOutPlayerId() != 0) {
						if(plyr.getPlayerId() == impactPlayer.getOutPlayerId()) {
							impactPlayer.setTeamId(plyr.getTeamId());
						}
					}
				}
				impactData[count] = impactPlayer;
				count++;
			}
		}
		return impactData;
	}
	public static String isImpactPlayer(List<Event> events,int inning_number,int player_id) {
			if ((events != null) && (events.size() > 0)) {
				for (int i = events.size() - 1; i >= 0; i--) {
					if(events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && events.get(i).getEventBatterNo() == player_id) {
						return CricketUtil.YES;
					}
				}
			}
			
			return "";
	}
	public static String checkImpactPlayer(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBatterNo() && events.get(i).getSubstitutionMade()!=null && 
						events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
					return CricketUtil.YES;
				}
			}
		}
		return "";
	}
	public static String checkImpactPlayerBowler(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBowlerNo() && events.get(i).getSubstitutionMade()!=null  && 
						events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
					return CricketUtil.YES;
				}
			}
		}
		return "";
	}
	
	public static int getImpactPlayerId(List<Event> events, int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventOtherBatterNo() && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT))) {
					return events.get(i).getEventBatterNo();
				}
			}
		}
		return 0;
	}
	
	public static String checkBatAndBallImpactInOutPlayer(List<Event> events, int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && 
						events.get(i).getEventExtra().equalsIgnoreCase("impact") && 
						events.get(i).getEventBatterNo() == player_id)) {
					return "IMP_IN";
				}else if((player_id == events.get(i).getEventOtherBatterNo() && 
						events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && 
						events.get(i).getEventExtra().equalsIgnoreCase("impact"))) {
					return "IMP_OUT";
				}else if((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && 
						events.get(i).getEventExtra().equalsIgnoreCase("concussed") && 
						events.get(i).getEventBatterNo() == player_id)) {
					return "CON_IN";
				}else if((player_id == events.get(i).getEventOtherBatterNo() && 
						events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && 
						events.get(i).getEventExtra().equalsIgnoreCase("concussed"))) {
					return "CON_OUT";
				}else if(player_id == events.get(i).getEventConcussionReplacePlayerId() &&
						events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_OVERWRITE_BATSMAN_HOWOUT) &&
						events.get(i).getEventHowOut().equalsIgnoreCase("CONCUSSED")) {
					return "CON_IN";
				}else if(events.get(i).getEventBattingCard() != null && player_id == events.get(i).getEventBattingCard().getPlayerId() &&
						events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_OVERWRITE_BATSMAN_HOWOUT) &&
						events.get(i).getEventHowOut().equalsIgnoreCase("CONCUSSED")) {
					return "CON_OUT";
				}
			}
		}
		
		
		
		return "";
	}
	
	public static String checkBatAndBallImpactInOutPlayer(MatchAllData matchalldata, int player_id) {
		
		for(Inning inn : matchalldata.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				for(BattingCard bc : inn.getBattingCard()) {
					if(bc.getHowOut() != null && !bc.getHowOut().isEmpty()) {
						if(bc.getHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
							if(player_id == bc.getPlayerId()) {
								return "CON_OUT";
							}else if(player_id == bc.getConcussionPlayerId()) {
								return "CON_IN";
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	public static String getListOfImpact(List<Event> events,int whichInning) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_IMPACT) && events.get(i).getEventInningNumber() == whichInning)) {
					return events.get(i).getEventBatterNo() + "," + events.get(i).getEventOtherBatterNo();
				}
			}
		}
		return "";
	}
	
	public static String checkImpactInOutPlayer(List<Event> events,int player_id,String type) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch (type.toUpperCase()) {
				case "IN":
					if((player_id == events.get(i).getEventBatterNo() && events.get(i).getSubstitutionMade()!=null && 
							events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
						return "IN";
					}
					break;
				case "OUT":
					if((player_id == events.get(i).getEventConcussionReplacePlayerId() && events.get(i).getSubstitutionMade()!=null && 
							events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
						return "OUT";
					}
					break;	
				}
				
			}
		}
		return "";
	}
	
	public static String checkImpactInOutBowler(List<Event> events,int player_id,String type) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch (type.toUpperCase()) {
				case "IN":
					if((player_id == events.get(i).getEventBowlerNo() && events.get(i).getSubstitutionMade()!=null && 
							events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
						return "IN";
					}
					break;
				case "OUT":
					if((player_id == events.get(i).getEventOtherBowlerNo() && events.get(i).getSubstitutionMade()!=null && 
							events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.IMPACT))) {
						return "OUT";
					}
					break;	
				}
				
			}
		}
		return "";
	}
	
	public static String whichLogo(String whatToProcess, String Logo) {
		switch (Logo) {
		case "KNIGHTS":
			switch (whatToProcess) {
			case "Control_F7": case "Control_F11": case "Shift_F11": 
			case "p_light": //Not Current Match Team Part
				return "KNIGHTS_DARK";
			default:
				return "KNIGHTS_LIGHT";
			}
		default:
			return Logo;
		}
	}
	
	public static String whichTextColor(String Logo) {
		switch (Logo) {
		case "ARCS": case "STRIKERS":
			return "DarkGrey";
		default:
			return "LightGrey";
		}
	}
	
	public static String checkConcussedPlayer(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBatterNo() && events.get(i).getSubstitutionMade()!=null && 
						events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
					return CricketUtil.YES;
				}
			}
		}
		return "";
	}
	public static ArrayList<String> TeamStatsComparison(List<Event> events) {
		ArrayList<String> TeamStats= new ArrayList<String>();
		int inn_one=0,inn_two=0,inn_three=0,inn_five=0,inn_dot=0,one=0,two=0,three=0,five=0,dot=0;
		
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch (events.get(i).getEventType()) {
				case CricketUtil.ONE:
					if(events.get(i).getEventInningNumber() == 1) {
						inn_one++;
					} else if(events.get(i).getEventInningNumber() == 2) {
						one++;					}
					break;
				case CricketUtil.TWO:
					if(events.get(i).getEventInningNumber() == 1) {
						inn_two++;
					} else if(events.get(i).getEventInningNumber() == 2) {
						two++;
					}
					break;
				case CricketUtil.THREE:
					if(events.get(i).getEventInningNumber() == 1) {
						inn_three++;
					} else if(events.get(i).getEventInningNumber() == 2) {
						three++;
					}
					break;
				case CricketUtil.FIVE:
					if(events.get(i).getEventInningNumber() == 1) {
						inn_five++;

					} else if(events.get(i).getEventInningNumber() == 2) {
						five++;
					}
					break;
				case CricketUtil.DOT:
					if(events.get(i).getEventInningNumber() == 1) {
						inn_dot++;
					} else if(events.get(i).getEventInningNumber() == 2) {
						dot++;
					}
					break;
				case CricketUtil.LOG_WICKET:
					switch (String.valueOf(events.get(i).getEventRuns())) {
					case CricketUtil.ONE:
						if(events.get(i).getEventInningNumber() == 1) {
							inn_one++;
						} else if(events.get(i).getEventInningNumber() == 2) {
							one++;					}
						break;
					case CricketUtil.TWO:
						if(events.get(i).getEventInningNumber() == 1) {
							inn_two++;
						} else if(events.get(i).getEventInningNumber() == 2) {
							two++;
						}
						break;
					case CricketUtil.THREE:
						if(events.get(i).getEventInningNumber() == 1) {
							inn_three++;
						} else if(events.get(i).getEventInningNumber() == 2) {
							three++;
						}
						break;
					case CricketUtil.FIVE:
						if(events.get(i).getEventInningNumber() == 1) {
							inn_five++;

						} else if(events.get(i).getEventInningNumber() == 2) {
							five++;
						}
						break;
					case CricketUtil.DOT:
						if(events.get(i).getEventInningNumber() == 1) {
							inn_dot++;
						} else if(events.get(i).getEventInningNumber() == 2) {
							dot++;
						}
						break;
					}
					break;
				}
				
			}
		}
		TeamStats.add(inn_dot+","+inn_one+","+inn_two+","+inn_three+","+inn_five);
		TeamStats.add(dot+","+one+","+two+","+three+","+five);
		return TeamStats;
	}
	public static String checkConcussedPlayerBowler(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBowlerNo() && events.get(i).getSubstitutionMade()!=null  && 
						events.get(i).getSubstitutionMade().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
					return CricketUtil.YES;
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
	public static void setInteractiveData(MatchAllData match,String line_txt, int i) throws IOException {
		String this_ball_data = "", Bowler = "", Batsman = "", OtherBatsman = "", 
		over_number = "", over_ball = "", inning_number = "",batsman_style = "",
		bowler_handed = "",this_over = "",this_over_run = "",shot = "-",wagonX = "0", wagonY = "0",height = "0",six_distance = "";
		int j = 0,roundedX=0,roundedY = 0;
		double clickX = 0,clickY = 0,thisX= 0,thisY= 0;
		
		switch (match.getEventFile().getEvents().get(i).getEventType().toUpperCase()) {
		
		  case CricketUtil.END_OVER:
			  if((match.getEventFile().getEvents().get(i-1).getEventBallNo() != 0) && (match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE)) ||
							  (match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) ||  
							  (match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL))){ 
			  }else {
				  ball_number = 0;
				  c = 0;
			  }
			  break;
			  
		  case CricketUtil.NEW_BATSMAN:
			  if(match.getEventFile().getEvents().get(i).getEventNumber() > 2 && 
					  match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)) {
				  ball_number = 0;
				  c = 0;
				  
			  }
			  break;  
			  
		  case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		  case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
		  case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
			  
			  line_txt = String.format("%-140s", "");
				j = j + 1;
				for(Inning inn : match.getMatch().getInning()) {
					for(Player hs : match.getSetup().getHomeSquad()) {
						if(match.getEventFile().getEvents().get(i).getEventBatterNo() == hs.getPlayerId()) {
							if(hs.getBattingStyle() == null) {
								batsman_style = "";
							}else {
								batsman_style = hs.getBattingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
						if(match.getEventFile().getEvents().get(i).getEventBowlerNo() == hs.getPlayerId()) {
							if(hs.getBowlingStyle() == null) {
								bowler_handed = "";
							}else {
								bowler_handed = hs.getBowlingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
					}
					
					if(match.getSetup().getHomeSubstitutes() != null) {
						for(Player hsub : match.getSetup().getHomeSubstitutes()) {
							if(match.getEventFile().getEvents().get(i).getEventBatterNo() == hsub.getPlayerId()) {
								if(hsub.getBattingStyle() == null) {
									batsman_style = "";
								}else {
									batsman_style = hsub.getBattingStyle().toUpperCase().charAt(0) + "";
								}
		    				}
							if(match.getEventFile().getEvents().get(i).getEventBowlerNo() == hsub.getPlayerId()) {
								if(hsub.getBowlingStyle() == null) {
									bowler_handed = "";
								}else {
									bowler_handed = hsub.getBowlingStyle().toUpperCase().charAt(0) + "";
								}
		    				}
						}
					}
					
					for(Player as : match.getSetup().getAwaySquad()) {
						if(match.getEventFile().getEvents().get(i).getEventBatterNo() == as.getPlayerId()) {
							if(as.getBattingStyle() == null) {
								batsman_style = "";
							}else {
								batsman_style = as.getBattingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
						if(match.getEventFile().getEvents().get(i).getEventBowlerNo() == as.getPlayerId()) {
							if(as.getBowlingStyle() == null) {
								bowler_handed = "";
							}else {
								bowler_handed = as.getBowlingStyle().toUpperCase().charAt(0) + "";
							}
	    				}
					}
					
					if(match.getSetup().getAwaySubstitutes() != null) {
						for(Player asub : match.getSetup().getAwaySubstitutes()) {
							if(match.getEventFile().getEvents().get(i).getEventBatterNo() == asub.getPlayerId()) {
								if(asub.getBattingStyle() == null) {
									batsman_style = "";
								}else {
									batsman_style = asub.getBattingStyle().toUpperCase().charAt(0) + "";
								}
		    				}
							if(match.getEventFile().getEvents().get(i).getEventBowlerNo() == asub.getPlayerId()) {
								if(asub.getBowlingStyle() == null) {
									bowler_handed = "";
								}else {
									bowler_handed = asub.getBowlingStyle().toUpperCase().charAt(0) + "";
								}
		    				}
						}
					}
					
	    			for(BattingCard bc : inn.getBattingCard()) {
	    				if(match.getEventFile().getEvents().get(i).getEventBatterNo() == bc.getPlayerId()) {
	    					Batsman = bc.getPlayer().getTicker_name();
	    				}
	    				if(match.getEventFile().getEvents().get(i).getEventOtherBatterNo() == bc.getPlayerId()) {
	    					OtherBatsman = bc.getPlayer().getTicker_name();
	    				}
	    			}
	    			if(inn.getBowlingCard() != null) {
	    				for(BowlingCard boc : inn.getBowlingCard()) {
				    		if(match.getEventFile().getEvents().get(i).getEventBowlerNo() == boc.getPlayerId()) {
				    			Bowler = boc.getPlayer().getTicker_name();
					    	}
				    	}
	    			}
			    }
				this_ball_data = "";
				
				inning_number = String.valueOf(match.getEventFile().getEvents().get(i).getEventInningNumber());
				if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
					isLeagleBall = true;
					if(match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
						over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), 1);
						over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), 1);
						ball_number = ball_number + 1;
					}else {
						ball_number = ball_number + 1;
						over_number = String.valueOf(match.getEventFile().getEvents().get(i).getEventOverNo() + 1);
						over_ball = String.valueOf(match.getEventFile().getEvents().get(i).getEventBallNo() + ball_number);
					}
				}else {
					if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || 
							match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
						
						isLeagleBall = true;
						
						if(match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
							ball_number = ball_number + 1;
							over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), 1);
							if(c == 0) {
								over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), 1);
							}else {
								over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo() + ball_number);
								
							}
						}else {
							ball_number = ball_number + 1;
							over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo() + ball_number);
							over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo() + ball_number);
						}
						
					}else {
						over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
						over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
						over_ball = String.valueOf(Integer.valueOf(over_ball) + ball_number);
						c=c+1;
					}
//					over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
//					over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
				}
				
				
				line_txt = addSubString(line_txt,inning_number,2);
	    		
	    		line_txt = addSubString(line_txt,Batsman,8);
				
				line_txt = addSubString(line_txt,Bowler,34);
	    		
	    		line_txt = addSubString(line_txt,over_number,63 - over_number.length());
	    		
	    		line_txt = addSubString(line_txt,over_ball,67 - over_ball.length());
	    		
	    		if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
	    			this_over = match.getEventFile().getEvents().get(i).getEventExtra();
	    		}else {
	    			this_over = match.getEventFile().getEvents().get(i).getEventType();
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
	    		
			  switch (match.getEventFile().getEvents().get(i).getEventType().toUpperCase()){
			    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
			    	this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns());
			    	this_over_run = this_ball_data;
			    	line_txt = addSubString(line_txt,this_ball_data,74);
			    	
			      break;
			    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			    	this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + this_over;
			    	this_over_run = this_ball_data;
			    	line_txt = addSubString(line_txt,"0",74);
			      break;
			    case CricketUtil.LOG_WICKET:
			    	line_txt = addSubString(line_txt,String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()),74);
		    		 
			      if (match.getEventFile().getEvents().get(i).getEventRuns() > 0) {
			    	  this_over_run = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()) + this_over;
		    		 
			        this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()) + this_over;
			      } else {
			    	  this_over_run = this_over;
		    		  
			        this_ball_data = this_over;
			      }
			    
			      break;
			    case CricketUtil.LOG_ANY_BALL:
			    	if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
			    		if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null && 
			    				!match.getEventFile().getEvents().get(i).getEventSubExtra().isEmpty() &&
			    				match.getEventFile().getEvents().get(i).getEventSubExtraRuns() > 0) {
			    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)||
			    					match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			    				int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
			    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
			    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
			    				this_ball_data = String.valueOf(runs);

			    			}
			    		}
			    		if(this_ball_data.isEmpty()) {
			    			if(match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||
			    					match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
			    				if(match.getEventFile().getEvents().get(i).getEventRuns()>0) {
			    					this_over_run =  match.getEventFile().getEvents().get(i).getEventRuns() + this_over;
			    					int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
				    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
				    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
		    						
				    				this_ball_data = match.getEventFile().getEvents().get(i).getEventExtra() + match.getEventFile().getEvents().get(i).getEventRuns();
				    			}else {
				    				this_over_run = this_over;
				    				int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
				    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
				    				line_txt = addSubString(line_txt,String.valueOf(runs),74);			    					
				    				this_ball_data = match.getEventFile().getEvents().get(i).getEventExtra();
				    			}
			    			}else {
			    				if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null && match.getEventFile().getEvents().get(i).getEventSubExtra().length() > 0) {
				    				this_over_run = match.getEventFile().getEvents().get(i).getEventSubExtra().charAt(0) + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
			    				}
				    			int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
			    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
			    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
					            this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra();
			    			}
			    		}else {
			    			if(!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)&&
					    			!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			    				this_over_run = this_over_run + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra().charAt(0);
					        	int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
			    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
			    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
					          this_ball_data = this_ball_data + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra();
			    			}else {
			    				this_over_run = this_over;
				    			line_txt = addSubString(line_txt,"",74);
				    			this_ball_data = this_ball_data + match.getEventFile().getEvents().get(i).getEventExtra();
			    			}
			    			
			    		}
			    	}
			      if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
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
			  
			  //-----------WAGON AND SHOTS------------------//
			  
			  wagonX = "0";wagonY = "0";shot = "-";
			  switch(match.getEventFile().getEvents().get(i).getEventType()) {
			   
			  case CricketUtil.DOT: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: 
			  case CricketUtil.LEG_BYE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET:
				    wagonX = String.valueOf(0);
			        wagonY = String.valueOf(0);
				  break;
			  case CricketUtil.LOG_ANY_BALL:
				  if(match.getMatch().getWagons() != null) {
					  for(int k = 0; k < match.getMatch().getWagons().size(); k++){
						  if(match.getEventFile().getEvents().get(i).getEventInningNumber() == match.getMatch().getWagons().get(k).getInningNumber()) {
							  if(match.getEventFile().getEvents().get(i).getEventOverNo() == match.getMatch().getWagons().get(k).getOverNumber()) {
								  if(match.getEventFile().getEvents().get(i).getEventBallNo() == match.getMatch().getWagons().get(k).getBallNumber()) {
									  if(match.getMatch().getWagons().get(k).getRuns() == (match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
												match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventSubExtraRuns())) {
//										
//										wagonX = String.valueOf(match.getMatch().getWagons().get(k).getWagonXCord());
//										wagonY = String.valueOf(match.getMatch().getWagons().get(k).getWagonYCord());
									
										  clickX = match.getMatch().getWagons().get(k).getWagonXCord();
										  clickY = match.getMatch().getWagons().get(k).getWagonYCord();

								        // Transform using double
	//								        
//										  thisX = Math.round(((clickX - 42) / 98.0) * 79 + 1);
//										  thisY = Math.round(0.00263 * clickY * clickY + 0.317 * clickY - 16.95);
										  
										  thisX = Math.min(80, Math.max(0, Math.round((((clickX - 42) / 98.0) * 79 + 1) * 1.085 + 31.9)));
									      thisY = Math.min(80, Math.max(0, Math.round((0.00263 * clickY * clickY + 0.317 * clickY - 16.95) * 1.438 + 26.0)));
									        
										  // Round to nearest integer
										  roundedX = (int) Math.round(thisX);
										  roundedY = (int) Math.round(thisY);
							        
										  wagonX = String.valueOf(roundedX);
										  wagonY = String.valueOf(roundedY);
									  }
								  }
							  }
						  }
					  }
				  }
				  break;
			default:
				  if(match.getMatch().getWagons() != null) {
					  for(int k = 0; k < match.getMatch().getWagons().size(); k++){
						  if(match.getEventFile().getEvents().get(i).getEventInningNumber() == match.getMatch().getWagons().get(k).getInningNumber()) {
							  if(match.getEventFile().getEvents().get(i).getEventOverNo() == match.getMatch().getWagons().get(k).getOverNumber()) {
								  if(match.getEventFile().getEvents().get(i).getEventBallNo() == match.getMatch().getWagons().get(k).getBallNumber()) {
										
	//										wagonX = String.valueOf(match.getMatch().getWagons().get(k).getWagonXCord());
	//										wagonY = String.valueOf(match.getMatch().getWagons().get(k).getWagonYCord());
										
									    clickX = match.getMatch().getWagons().get(k).getWagonXCord();
								        clickY = match.getMatch().getWagons().get(k).getWagonYCord();
	
								        // Transform using double
	//								        
//								        thisX = Math.round(((clickX - 42) / 98.0) * 79 + 1);
//								        thisY = Math.round(0.00263 * clickY * clickY + 0.317 * clickY - 16.95);
								        
								        thisX = Math.min(80, Math.max(0, Math.round((((clickX - 42) / 98.0) * 79 + 1) * 1.085 + 31.9)));
								        thisY = Math.min(80, Math.max(0, Math.round((0.00263 * clickY * clickY + 0.317 * clickY - 16.95) * 1.438 + 26.0)));
								        
								        // Round to nearest integer
								        roundedX = (int) Math.round(thisX);
								        roundedY = (int) Math.round(thisY);
								        
								        wagonX = String.valueOf(roundedX);
								        wagonY = String.valueOf(roundedY);
								        
										if(match.getMatch().getWagons().get(k).getBoundaryHeight().contains("along_ground")) {
											height = "0";
										}else if(match.getMatch().getWagons().get(k).getBoundaryHeight().contains("below_head_height")) {
											height = "1";
										}else if(match.getMatch().getWagons().get(k).getBoundaryHeight().contains("just_over_head_height")) {
											height = "2";
										}else if(match.getMatch().getWagons().get(k).getBoundaryHeight().contains("high_in_the_air")) {
											height = "3";
										}else if(match.getMatch().getWagons().get(k).getBoundaryHeight().contains("very_high_in_the_air")) {
											height = "4";
										}
										if(match.getMatch().getShots().get(k).getRuns() == 6) {
											six_distance = String.valueOf(match.getMatch().getWagons().get(k).getSixDistance());
										}
								        break;
								  }
							  }
						  }
					  }
				  }
				  break; 
			  }
			 
			  if(match.getMatch().getShots() != null) {
				  for(int k = 0; k < match.getMatch().getShots().size(); k++){
					  if(match.getEventFile().getEvents().get(i).getEventInningNumber() == match.getMatch().getShots().get(k).getInningNumber()) {
							if(match.getEventFile().getEvents().get(i).getEventOverNo() == match.getMatch().getShots().get(k).getOverNumber()) {
								if(match.getEventFile().getEvents().get(i).getEventBallNo() == match.getMatch().getShots().get(k).getBallNumber()) {
									if (match.getMatch().getShots().get(k).getShotType().contains("no_shot")) {
										shot = "N";
									}else if(match.getMatch().getShots().get(k).getShotType().contains("nudge")) {
										 shot = "E";
									}else if(match.getMatch().getShots().get(k).getShotType().contains("defence") || match.getMatch().getShots().get(k).getShotType().contains("off_drive") || 
											match.getMatch().getShots().get(k).getShotType().contains("on_drive") || match.getMatch().getShots().get(k).getShotType().contains("straight_drive") || 
											match.getMatch().getShots().get(k).getShotType().contains("front") || match.getMatch().getShots().get(k).getShotType().contains("back")) {
										 shot = "P";
									}else {
										shot = "M";
									}
									
									if (match.getMatch().getShots().get(k).getShotType().contains("no_shot")) {
										shot = shot + "N";
									}else if(match.getMatch().getShots().get(k).getShotType().contains("defence")) {
										shot = shot + "D";
									}else {
										shot = shot + "A";
									}
								}
							}
						}
				  }
			  }
			 line_txt = addSubString(line_txt,wagonX,84);
			 line_txt = addSubString(line_txt,wagonY,90);
			 
			if(match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.LOG_WICKET)) {
		    	line_txt = addSubString(line_txt,"Y",95);
	    		
		    }else {
		    	line_txt = addSubString(line_txt,"N",95);	
		    }
			
			line_txt = addSubString(line_txt,batsman_style,102);
			line_txt = addSubString(line_txt,shot,109);
			line_txt = addSubString(line_txt,height,115);
			line_txt = addSubString(line_txt,wagonX,120);
			line_txt = addSubString(line_txt,wagonY,126);
			line_txt = addSubString(line_txt,bowler_handed,129);
			line_txt = addSubString(line_txt,OtherBatsman,131);
			line_txt = addSubString(line_txt,this_over_run,157);
			line_txt = addSubString(line_txt,six_distance,162);
			
			Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT), 
				Arrays.asList(line_txt), StandardOpenOption.APPEND);
			
			break;
	    }
	}
	
	public static String getInteractive(MatchAllData match,String type) throws IOException 
	{
		if(match.getSetup() == null || (match.getSetup().getGenerateInteractiveFile() == null 
			|| match.getSetup().getGenerateInteractiveFile().equalsIgnoreCase(CricketUtil.NO))) {
			return "";
		}
		if(match.getEventFile() == null || (match.getEventFile().getEvents() == null 
			|| match.getEventFile().getEvents().size() <= 0)) {
			return "";
		}
		Inning inning=match.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
			.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null);
		int max_inn = 2;
		String line_txt = String.format("%-140s", "");
		String txt = String.format("%-140s", "");
		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST) || 
				match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)) {
			max_inn = 4;
		}
		
		switch(type.toUpperCase()){
		case "FULL_WRITE":
			
			txt = addSubString(txt,"============================================================================================================================================================" + "\n\n",0);
			txt = addSubString(txt,"# 174-178               Spin text" + "\n",0);
			txt = addSubString(txt,"# 167-172               Shot type code" + "\n",0);
			txt = addSubString(txt,"# 163-165               Distance of sixes (in metres)" + "\n",0);
			txt = addSubString(txt,"# 158-161               This Over text" + "\n",0);
			txt = addSubString(txt,"# 132-156               Non-facing batsman name" + "\n",0);
			txt = addSubString(txt,"# 130                   Bowler handed ('L' or 'R')" + "\n",0);
			txt = addSubString(txt,"# 126-128               Ball landing y ordinate (for boundaries not along the ground only)" + "\n",0);
			txt = addSubString(txt,"# 120-122               Ball landing x ordinate (for boundaries not along the ground only)" + "\n",0);
			txt = addSubString(txt,"# 116                   Shot Height (for boundaries only)" + "\n",0);
			txt = addSubString(txt,"# 111                   Shot played code 2 ('A' for attack, 'D' for defend, 'N' for no shot)" + "\n",0);
			txt = addSubString(txt,"# 110                   Shot played code 1 ('P' for played, 'M' for missed, 'N' for no shot, 'E' for edge/miss-hit)" + "\n",0);
			txt = addSubString(txt,"# 103                   Batsman style ('L' or 'R')" + "\n",0);
			txt = addSubString(txt,"# 96                    Wicket attributed to bowler this ball? (Y or N)" + "\n",0);
			txt = addSubString(txt,"# 89 - 91               Wagon Wheel y ordinate" + "\n",0);
			txt = addSubString(txt,"# 83 - 85               Wagon Wheel x ordinate" + "\n",0);
			txt = addSubString(txt,"# 75                    Runs scored off the bat for this ball" + "\n",0);
			txt = addSubString(txt,"# 66 - 67               Ball within over (2 figures, starting at 1)" + "\n",0);
			txt = addSubString(txt,"# 61 - 63               Over number (3 figures, starting at 1)" + "\n",0);
			txt = addSubString(txt,"# 35 - 59               Bowler name (25 characters)" + "\n",0);
			txt = addSubString(txt,"# 9 - 33                Batsman name (25 characters)" + "\n",0);
			txt = addSubString(txt,"# 3                     Innings (1 to 4)" + "\n",0);
			txt = addSubString(txt,"# " + "\n",0);
			txt = addSubString(txt,"# -------               --------" + "\n",0);
			txt = addSubString(txt,"# Columns               Contents" + "\n",0);
			txt = addSubString(txt,"# File Version 1.5" + "\n",0);
			txt = addSubString(txt,"# Match between " + match.getSetup().getHomeTeam().getTeamName1() + " and " + match.getSetup().getAwayTeam().getTeamName1() + "\n",0);
			txt = addSubString(txt,"# DOAD Interactive File generated on " + LocalDate.now() + " at " + LocalTime.now() + "\n",0);
			txt = addSubString(txt,"============================================================================================================================================================" + "\n\n",0);

			if(Files.exists(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT))) {
				FileOutputStream fs = new FileOutputStream(CricketUtil.CRICKET_SERVER_DIRECTORY 
					+ CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT);
				fs.write(new byte[0]);
				fs.close();
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
			
		    for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++)
		    {
			  if(match.getEventFile().getEvents().get(i).getEventInningNumber() >= 1 && 
					  match.getEventFile().getEvents().get(i).getEventInningNumber() <= max_inn) {
				  setInteractiveData(match,line_txt,i);
			  }
		    }
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT).exists() == true
				&& new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY).exists() == true) {
				Files.copy(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY + CricketUtil.DOAD_INTERACTIVE_TXT), 
					Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.BACK_UP_DIRECTORY + CricketUtil.INTERACTIVE_DIRECTORY 
					+ CricketUtil.DOAD_INTERACTIVE_TXT), StandardCopyOption.REPLACE_EXISTING);
			}
		    break;
		case"OVERWRITE":
			  if(match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventInningNumber() == inning.getInningNumber() &&
				  inning.getTotalOvers() == match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventOverNo() &&
				  inning.getTotalBalls() == match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventBallNo() &&
				  match.getEventFile().getEvents().get(match.getEventFile().getEvents().size() - 1).getEventInningNumber() <= max_inn) 
			  {
				  setInteractiveData(match,line_txt,match.getEventFile().getEvents().size() - 1);
			  }
			break;
		}
		return null;
	}
	
	public static List<String> getTapeBalldetails(int inning, List<Event> event, MatchAllData matchAllData) {

	    int bowlerId = 0, runs = 0, wicket = 0, over = 0, dots = 0;
	    boolean bowlerFound = false,tapeballOver = false;
	    Map<Integer, String> bowlerStats = new LinkedHashMap<>(); // Maintain insertion order
	    List<String> bowlerDetails = new ArrayList<>();

	    if (matchAllData.getEventFile().getEvents() != null && !matchAllData.getEventFile().getEvents().isEmpty()) {
	        for (Event evnt : matchAllData.getEventFile().getEvents()) {
	        	if(evnt.getEventInningNumber() == inning) {
	        		if(evnt.getEventExtra() != null && !evnt.getEventExtra().isEmpty() && 
	            			evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
	 
	        			tapeballOver = true;       			
	            		// New Bowler Handling
		                if (bowlerId != evnt.getEventBowlerNo() && evnt.getEventBowlerNo() != 0) {
		                    if (bowlerFound) {
		                        bowlerStats.put(bowlerId, bowlerId + "," + over + "," + runs + "," + wicket + "," + dots);
		                    }

		                    // Initialize new bowler
		                    bowlerId = evnt.getEventBowlerNo();
		                    over = evnt.getEventOverNo()+1;
		                    runs = 0; wicket = 0; dots = 0;
		                    bowlerFound = true;
		                }
	            	}
	        		
	        		// Process Event Types
	            	if(bowlerFound && tapeballOver && evnt.getEventBowlerNo() == bowlerId) {
	            		switch (evnt.getEventType().toUpperCase()) {
	            			case CricketUtil.END_OVER:
	            				tapeballOver = false;
	            				break;
		                    case CricketUtil.DOT:
		                        dots++;
		                        break;
		                    case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
		                    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		                        runs += evnt.getEventRuns();
		                        break;
		                    case CricketUtil.BYE: case CricketUtil.LEG_BYE:
		                    	dots++;
		                    	break;
		                    case CricketUtil.WIDE: case CricketUtil.NO_BALL:
		                        runs += evnt.getEventRuns();
		                        break;
		                    case CricketUtil.LOG_WICKET:
		                        if (evnt.getEventRuns() > 0) {
		                            runs += evnt.getEventRuns();
		                        }else {
		                        	dots++;
		                        }
		                        switch(evnt.getEventHowOut()) {
		                        case CricketUtil.CAUGHT: case CricketUtil.CAUGHT_AND_BOWLED: case CricketUtil.BOWLED: 
		                        case CricketUtil.LBW: case CricketUtil.STUMPED: case CricketUtil.HIT_WICKET:
		                        	wicket++;
		                        	break;
		                        }
		                        break;
		                    case CricketUtil.LOG_ANY_BALL:
		                        runs += evnt.getEventRuns();
		                        if (evnt.getEventExtra() != null) {
		                            runs += evnt.getEventExtraRuns();
		                        }
		                        if (evnt.getEventSubExtra() != null) {
		                            runs += evnt.getEventSubExtraRuns();
		                        }
		                        break;
		                }
	            	}
	        	}
	        }

	        // Add final bowler's data to the map
	        if (bowlerFound) {
	            bowlerStats.put(bowlerId, bowlerId + "," + over + "," + runs + "," + wicket + "," + dots);
	        }
	        // Collect all bowler data for output
	        bowlerDetails.addAll(bowlerStats.values());
	    }

	    return bowlerDetails;
	}
	
	public static List<String> getChallengeRunsDetails(int inning, List<Event> event, MatchAllData matchAllData) {

	    int bowlerId = 0, runs = 0, wicket = 0, over = 0, dots = 0,bouns=0,cr=0;
	    String type="";
	    boolean bowlerFound = false,challengeRuns = false;
	    Map<Integer, String> bowlerStats = new LinkedHashMap<>(); // Maintain insertion order
	    List<String> bowlerDetails = new ArrayList<>();

	    if (matchAllData.getEventFile().getEvents() != null && !matchAllData.getEventFile().getEvents().isEmpty()) {
	        for (Event evnt : matchAllData.getEventFile().getEvents()) {
	        	if(evnt.getEventInningNumber() == inning) {
	        		if(evnt.getEventExtra() != null && !evnt.getEventExtra().isEmpty() && 
	            			evnt.getEventExtra().equalsIgnoreCase("challenge")) {
	 
	        			challengeRuns = true;       			
	            		// New Bowler Handling
		                if (bowlerId != evnt.getEventBowlerNo() && evnt.getEventBowlerNo() != 0) {
		                    if (bowlerFound) {
		                    	bowlerStats.put(bowlerId, bowlerId + "," + over + "," + runs + "," + wicket + "," + dots + "," + type + "," + cr + "," + bouns);
		                    }

		                    // Initialize new bowler
		                    bowlerId = evnt.getEventBowlerNo();
		                    over = evnt.getEventOverNo()+1;
		                    cr = Integer.valueOf(evnt.getEventSubExtra());
		                    runs = 0; wicket = 0; dots = 0;
		                    bowlerFound = true;
		                }
	            	}
	        		
	        		// Process Event Types
	            	if(bowlerFound && challengeRuns && evnt.getEventBowlerNo() == bowlerId) {
	            		switch (evnt.getEventType().toUpperCase()) {
	            			case CricketUtil.END_OVER:
	            				challengeRuns = false;
	            				break;
		                    case CricketUtil.DOT:
		                        dots++;
		                        break;
		                    case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: 
		                    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		                        runs += evnt.getEventRuns();
		                        break;
		                    case CricketUtil.BYE: case CricketUtil.LEG_BYE:
		                    	dots++;
		                    	break;
		                    case CricketUtil.WIDE: case CricketUtil.NO_BALL:
		                        runs += evnt.getEventRuns();
		                        break;
		                    case CricketUtil.LOG_WICKET:
		                        if (evnt.getEventRuns() > 0) {
		                            runs += evnt.getEventRuns();
		                        }else {
		                        	dots++;
		                        }
		                        switch(evnt.getEventHowOut()) {
		                        case CricketUtil.CAUGHT: case CricketUtil.CAUGHT_AND_BOWLED: case CricketUtil.BOWLED: 
		                        case CricketUtil.LBW: case CricketUtil.STUMPED: case CricketUtil.HIT_WICKET:
		                        	wicket++;
		                        	break;
		                        }
		                        break;
		                    case CricketUtil.LOG_ANY_BALL:
		                        runs += evnt.getEventRuns();
		                        if (evnt.getEventExtra() != null) {
		                            runs += evnt.getEventExtraRuns();
		                        }
		                        if (evnt.getEventSubExtra() != null) {
		                            runs += evnt.getEventSubExtraRuns();
		                        }
		                        break;
		                }
	            	}
	            	
	            	if(runs >= cr) {
	            		type = "+";
	            	}else if(runs < cr) {
	            		type = "-";
	            	}
	            	bouns = (runs/2);
	        	}
	        }

	        // Add final bowler's data to the map
	        if (bowlerFound) {
	            bowlerStats.put(bowlerId, bowlerId + "," + over + "," + runs + "," + wicket + "," + dots + "," + type + "," + cr + "," + bouns);
	        }
	        // Collect all bowler data for output
	        bowlerDetails.addAll(bowlerStats.values());
	    }

	    return bowlerDetails;
	}

	public static void exportMatchData(MatchAllData match) throws IOException 
	{
		List<String> lineByLineData = new ArrayList<String>();
		StringBuilder matchDataTxt = new StringBuilder();
		
		lineByLineData.add("|");
		lineByLineData.add("|    (B) - 'BO' - Bowling details");
		lineByLineData.add("|    (A) - 'IS' - Batting details");
		lineByLineData.add("|");
		lineByLineData.add("| DOAD Export File generated on " + LocalDate.now() + " at " + LocalTime.now());
		lineByLineData.add("|============================================================================================================================================================");
		lineByLineData.add("|   1 - 2       Line Ident ('IS')('BO')");
		lineByLineData.add("|   4 - 22      Match file name");
		lineByLineData.add("|  24 - 42      Venue name");
		lineByLineData.add("|  44 - 62      Team full name");
		lineByLineData.add("|  64 - 82      Opponent full name");
		lineByLineData.add("|  84 - 86      Batsman code");
		lineByLineData.add("|  88 - 91      Runs");
		lineByLineData.add("|  92 - 94      Balls");
		lineByLineData.add("|  95 - 97      Fours");
		lineByLineData.add("|  98 - 100     Sixes");
		lineByLineData.add("| 101 - 103     Balls to reach 50");
		lineByLineData.add("| 104 - 106     Balls to reach 100");
		lineByLineData.add("|       108     Did batsman innings start?");
		lineByLineData.add("|       111     Was batsman dismissed?");
		lineByLineData.add("| 121 - 123     Dots");
		lineByLineData.add("| 124 - 126     Ones");
		lineByLineData.add("| 127 - 129     Twos");
		lineByLineData.add("| 130 - 132     Threes");
		lineByLineData.add("| 133 - 135     Catches");
		lineByLineData.add("| 136 - 138     Stumpings");
		lineByLineData.add("|");
		lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><BAT><R><B><4><6><F><H><I><D><TN><ON><D><1><2><3><C><S>");
		
		//Batting Card
		
		for(Inning inn : match.getMatch().getInning()) {
			matchDataTxt = new StringBuilder();
			for (BattingCard bc : inn.getBattingCard()) {
			    matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
			    
			    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
			    
			    // Add substrings at specific positions using StringBuilder methods
			    matchDataTxt.insert(0, "IS");
			    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
			    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
			    matchDataTxt.insert(43, inn.getBatting_team().getTeamName4());
			    matchDataTxt.insert(63, inn.getBowling_team().getTeamName4());
			    matchDataTxt.insert(86 - String.valueOf(bc.getPlayerId()).length(), bc.getPlayerId());
			    matchDataTxt.insert(90 - String.valueOf(bc.getRuns()).length(), bc.getRuns());
			    matchDataTxt.insert(93 - String.valueOf(bc.getBalls()).length(), bc.getBalls());
			    matchDataTxt.insert(96 - String.valueOf(bc.getFours()).length(), bc.getFours());
			    matchDataTxt.insert(99 - String.valueOf(bc.getSixes()).length(), bc.getSixes());
			    
			    String[] ball_count = ballCountOfFiftyAndHundred(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getPlayerId()).split("-");
			    if (bc.getRuns() >= 50 && bc.getRuns() < 100) {
			        matchDataTxt.insert(102 - ball_count[0].length(), ball_count[0]);
			        matchDataTxt.insert(104, "0");
			    } else if (bc.getRuns() >= 100) {
			        matchDataTxt.insert(102 - ball_count[0].length(), ball_count[0]);
			        matchDataTxt.insert(105 - ball_count[1].length(), ball_count[1]);
			    } else {
			        matchDataTxt.insert(101, "0");
			        matchDataTxt.insert(104, "0");
			    }
			    
			    if (bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
			        matchDataTxt.insert(106, "Y");
			        matchDataTxt.insert(109, bc.getStatus().equalsIgnoreCase(CricketUtil.OUT) ? "Y" : "N");
			    } else {
			        matchDataTxt.insert(106, "N");
			        matchDataTxt.insert(109, "-");
			    }
			    
			    matchDataTxt.insert(112, "-");
			    matchDataTxt.insert(116, "-");
			    
			    String[] Runs_Count = getScoreTypeData(CricketUtil.BATSMAN, match, inn.getInningNumber(), bc.getPlayerId(), "-", match.getEventFile().getEvents()).split("-");
			    matchDataTxt.insert(122 - Runs_Count[0].length(), Runs_Count[0]);
			    matchDataTxt.insert(125 - Runs_Count[1].length(), Runs_Count[1]);
			    matchDataTxt.insert(128 - Runs_Count[2].length(), Runs_Count[2]);
			    matchDataTxt.insert(131 - Runs_Count[3].length(), Runs_Count[3]);
			    
			    String[] Count = caughtAndStumpedCount(match.getEventFile().getEvents(), bc.getPlayerId()).split("-");
			    matchDataTxt.insert(132, Count[0]);
			    matchDataTxt.insert(135, Count[1]);
			    
			    lineByLineData.add(matchDataTxt.toString());
			}
		}
		
		//Bowling card
		
		lineByLineData.add("|");
		lineByLineData.add("|============================================================================================================================================================");
		lineByLineData.add("|	1 - 2       Line Ident ('IS')('BO')");
		lineByLineData.add("|   4 - 23      Match file name");
		lineByLineData.add("|  25 - 44      Venue name");
		lineByLineData.add("|  46 - 65      Team name");
		lineByLineData.add("|  67 - 83      Opponent name");
		lineByLineData.add("|  84 - 88      Bowler code");
		lineByLineData.add("|  89 - 91      Balls");
		lineByLineData.add("|  92 - 94      Maidens");
		lineByLineData.add("|  95 - 97      Runs");
		lineByLineData.add("|  98 - 100     Wickets");
		lineByLineData.add("| 101 - 103     Dot balls");
		lineByLineData.add("| 104 - 107     Team ticker name");
		lineByLineData.add("| 108 - 111     Opponent ticker name");
		lineByLineData.add("| 112 - 115     Last wicket ball count");
		lineByLineData.add("|");
		lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><BWL><B><M><R><W><D><TN><ON><LW>");
		
		for(Inning inn : match.getMatch().getInning()) {
			matchDataTxt = new StringBuilder();
			if(inn.getBowlingCard() != null) {
				for (BowlingCard boc : inn.getBowlingCard()) {
				    matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
				    
				    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
				    
				    // Add substrings at specific positions using StringBuilder methods
				    matchDataTxt.insert(0, "BO");
				    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
				    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
				    matchDataTxt.insert(43, inn.getBowling_team().getTeamName4());
				    matchDataTxt.insert(63, inn.getBatting_team().getTeamName4());
				    
				    matchDataTxt.insert(86-String.valueOf(boc.getPlayerId()).length(), boc.getPlayerId());
				    matchDataTxt.insert(90-String.valueOf((boc.getOvers() * 6) + boc.getBalls()).length(), String.valueOf((boc.getOvers() * 6) + boc.getBalls()));
				    matchDataTxt.insert(93-String.valueOf(boc.getMaidens()).length(), boc.getMaidens());
				    matchDataTxt.insert(96-String.valueOf(boc.getRuns()).length(), boc.getRuns());
				    matchDataTxt.insert(99-String.valueOf(boc.getWickets()).length(), boc.getWickets());
				    matchDataTxt.insert(102-String.valueOf(boc.getDots()).length(), boc.getDots());
				    
				    matchDataTxt.insert(103, "-");
				    matchDataTxt.insert(107, "-");
				    
					if(boc.getWickets() > 0) {
						matchDataTxt.insert(114-String.valueOf(lastWicketBallCount(match.getEventFile().getEvents(), inn.getInningNumber(), boc.getPlayerId())).length(), 
								lastWicketBallCount(match.getEventFile().getEvents(), inn.getInningNumber(), boc.getPlayerId()));
					}else {
						matchDataTxt.insert(113, "0");
					}
				    
				    lineByLineData.add(matchDataTxt.toString());
				}
			}
		}
		
		// Teams Total
		
		lineByLineData.add("|");
		lineByLineData.add("|============================================================================================================================================================");
		lineByLineData.add("|	1 - 2       Line Ident ('TT')");
		lineByLineData.add("|   4 - 23      Match file name");
		lineByLineData.add("|  25 - 44      Venue name");
		lineByLineData.add("|  46 - 65      Team name");
		lineByLineData.add("|  67 - 83      Opponent name");
		lineByLineData.add("|  84 - 86      Balls");
		lineByLineData.add("|  87 - 89      Runs");
		lineByLineData.add("|  90 - 92      Wickets");
		lineByLineData.add("|");
		lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><BALLS><RUNS><WKTS>");
		
		for(Inning inn : match.getMatch().getInning()) {
			matchDataTxt = new StringBuilder();
		    matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
		    
		    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
		    
		    // Add substrings at specific positions using StringBuilder methods
		    matchDataTxt.insert(0, "TT");
		    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
		    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
		    matchDataTxt.insert(43, inn.getBatting_team().getTeamName4());
		    matchDataTxt.insert(63, inn.getBowling_team().getTeamName4());
		    
		    matchDataTxt.insert(86-String.valueOf((inn.getTotalOvers()*6) + inn.getTotalBalls()).length(), String.valueOf((inn.getTotalOvers()*6) + inn.getTotalBalls()));
		    matchDataTxt.insert(93-String.valueOf(inn.getTotalRuns()).length(), inn.getTotalRuns());
		    matchDataTxt.insert(99-String.valueOf(inn.getTotalWickets()).length(), inn.getTotalWickets());
		    
		    lineByLineData.add(matchDataTxt.toString());
		}
//				// POWERPLAY
		
		if(!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC) || 
				!match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			
			MatchStats matchStats = CricketFunctions.getpowerPlay(match);
			
			lineByLineData.add("|");
			lineByLineData.add("|============================================================================================================================================================");
			lineByLineData.add("|	1 - 2       Line Ident ('PP')");
			lineByLineData.add("|   4 - 23      Match file name");
			lineByLineData.add("|  25 - 44      Venue name");
			lineByLineData.add("|  46 - 65      Team name");
			lineByLineData.add("|  67 - 83      Opponent name");
			lineByLineData.add("|  84 - 89      P1_R");
			lineByLineData.add("|  90 - 95      P2_R");
			lineByLineData.add("|  96 - 101     P3_R");
			lineByLineData.add("|  102 - 107    P1_W");
			lineByLineData.add("|  108 - 113    P2_W");
			lineByLineData.add("|  114 - 119    P3_W");
			lineByLineData.add("|");
			lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><P1_R><P2_R><P3_R><P1_W><P2_W><P3_W>");
			
			for(int i = 0; i < 2;i++) {
				matchDataTxt = new StringBuilder();
			    matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
			    
			    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
			    
			    // Add substrings at specific positions using StringBuilder methods
			    matchDataTxt.insert(0, "PP");
			    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
			    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
			    matchDataTxt.insert(43, match.getMatch().getInning().get(i).getBatting_team().getTeamName4());
			    matchDataTxt.insert(63, match.getMatch().getInning().get(i).getBowling_team().getTeamName4());
			    
			    VariousStats pp1 ,pp2,pp3 ;
			    if(i==0) {
			    	pp1 = matchStats.getHomeFirstPowerPlay();
			    	pp2 = matchStats.getHomeSecondPowerPlay();
			    	pp3 = matchStats.getHomeThirdPowerPlay();
			    }else {
			    	pp1 = matchStats.getAwayFirstPowerPlay();
			    	pp2 = matchStats.getAwaySecondPowerPlay();
			    	pp3 = matchStats.getAwayThirdPowerPlay();
			    }
			    if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
			    	matchDataTxt.insert(86-String.valueOf(pp1.getTotalRuns()).length(), String.valueOf(pp1.getTotalRuns()));
				    matchDataTxt.insert(93-String.valueOf(pp2.getTotalRuns()).length(), pp2.getTotalRuns());
				    matchDataTxt.insert(99-String.valueOf(pp3.getTotalRuns()).length(), pp3.getTotalRuns());
				    
				    matchDataTxt.insert(105-String.valueOf(pp1.getTotalWickets()).length(),pp1.getTotalWickets());
				    matchDataTxt.insert(111-String.valueOf(pp2.getTotalWickets()).length(),pp2.getTotalWickets());
				    matchDataTxt.insert(117-String.valueOf(pp3.getTotalWickets()).length(), pp3.getTotalWickets());
				    
			    }else {
			    	matchDataTxt.insert(86-String.valueOf(pp1.getTotalRuns()).length(), String.valueOf(pp1.getTotalRuns()));
				    matchDataTxt.insert(93-String.valueOf(pp2.getTotalRuns()).length(), pp2.getTotalRuns());
				    matchDataTxt.insert(99-String.valueOf(pp3.getTotalRuns()).length(), pp3.getTotalRuns());
				    
				    matchDataTxt.insert(105-String.valueOf(pp1.getTotalWickets()).length(),pp1.getTotalWickets());
				    matchDataTxt.insert(111-String.valueOf(pp2.getTotalWickets()).length(),pp2.getTotalWickets());
				    matchDataTxt.insert(117-String.valueOf(pp3.getTotalWickets()).length(), pp3.getTotalWickets());
				    
			    }
			    
			    lineByLineData.add(matchDataTxt.toString());
			}
		}
				
		if(match.getSetup().getSpecialMatchRules() != null) {
			if(match.getSetup().getSpecialMatchRules().equalsIgnoreCase(CricketUtil.ISPL)) {
				lineByLineData.add("|");
				lineByLineData.add("|============================================================================================================================================================");
				lineByLineData.add("|	1 - 2       Line Ident ('TB')");
				lineByLineData.add("|   4 - 23      Match file name");
				lineByLineData.add("|  25 - 44      Venue name");
				lineByLineData.add("|  46 - 65      Team name");
				lineByLineData.add("|  67 - 83      Opponent name");
				lineByLineData.add("|  84 - 88      Bowler code");
				lineByLineData.add("|  89 - 91      Balls");
				lineByLineData.add("|  92 - 94      Runs");
				lineByLineData.add("|  95 - 97      Wickets");
				lineByLineData.add("|  98 - 100     Dot balls");
				lineByLineData.add("|");
				lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><BWL><B><R><W><D>");
				
				for(Inning inn : match.getMatch().getInning()) {
				    List<String> tape_ball_data = getTapeBalldetails(inn.getInningNumber(), match.getEventFile().getEvents(), match);
				    for(int i=0;i<=tape_ball_data.size()-1;i++) {
				    	matchDataTxt = new StringBuilder();
						matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
					    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
					    
					    // Add substrings at specific positions using StringBuilder methods
					    
					    matchDataTxt.insert(0, "TB");
					    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
					    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
					    matchDataTxt.insert(43, inn.getBowling_team().getTeamName4());
					    matchDataTxt.insert(63, inn.getBatting_team().getTeamName4());
				    	
					    matchDataTxt.insert(86-tape_ball_data.get(i).split(",")[0].length(), tape_ball_data.get(i).split(",")[0]);
					    matchDataTxt.insert(90-tape_ball_data.get(i).split(",")[1].length(), tape_ball_data.get(i).split(",")[1]);
					    matchDataTxt.insert(93-tape_ball_data.get(i).split(",")[2].length(), tape_ball_data.get(i).split(",")[2]);
					    matchDataTxt.insert(96-tape_ball_data.get(i).split(",")[3].length(), tape_ball_data.get(i).split(",")[3]);
					    matchDataTxt.insert(99-tape_ball_data.get(i).split(",")[4].length(), tape_ball_data.get(i).split(",")[4]);
					    
					    lineByLineData.add(matchDataTxt.toString());
				    }
				}
				
				lineByLineData.add("|");
				lineByLineData.add("|============================================================================================================================================================");
				lineByLineData.add("|	1 - 2       Line Ident ('CR')");
				lineByLineData.add("|   4 - 23      Match file name");
				lineByLineData.add("|  25 - 44      Venue name");
				lineByLineData.add("|  46 - 65      Team name");
				lineByLineData.add("|  67 - 83      Opponent name");
				lineByLineData.add("|  84 - 88      Bowler code");
				lineByLineData.add("|  89 - 91      Balls");
				lineByLineData.add("|  92 - 94      Runs");
				lineByLineData.add("|  95 - 97      Wickets");
				lineByLineData.add("|  98 - 100     Dot balls");
				lineByLineData.add("|  101 - 104    Bonus Added & subtract");
				lineByLineData.add("|  105 - 108    Challenge Runs");
				lineByLineData.add("|  109 - 112    Bonus");
				lineByLineData.add("|");
				lineByLineData.add("| <Match File Name   >< Venue Name       >< Team name        >< Opponent Name    ><BWL><B><R><W><D><IN><CR><BS>");
				
				for(Inning inn : match.getMatch().getInning()) {
				    List<String> cr_data = getChallengeRunsDetails(inn.getInningNumber(), match.getEventFile().getEvents(), match);
				    for(int i=0;i<=cr_data.size()-1;i++) {
				    	matchDataTxt = new StringBuilder();
						matchDataTxt.setLength(0); // Clear the StringBuilder for each iteration
					    matchDataTxt.append(String.format("%-140s", "")); // Initial padding
					    
					    // Add substrings at specific positions using StringBuilder methods
					    
					    matchDataTxt.insert(0, "CR");
					    matchDataTxt.insert(3, match.getMatch().getMatchFileName());
					    matchDataTxt.insert(23, match.getSetup().getGround().getCity());
					    matchDataTxt.insert(43, inn.getBowling_team().getTeamName4());
					    matchDataTxt.insert(63, inn.getBatting_team().getTeamName4());
				    	
					    matchDataTxt.insert(86-cr_data.get(i).split(",")[0].length(), cr_data.get(i).split(",")[0]);
					    matchDataTxt.insert(90-cr_data.get(i).split(",")[1].length(), cr_data.get(i).split(",")[1]);
					    matchDataTxt.insert(93-cr_data.get(i).split(",")[2].length(), cr_data.get(i).split(",")[2]);
					    matchDataTxt.insert(96-cr_data.get(i).split(",")[3].length(), cr_data.get(i).split(",")[3]);
					    matchDataTxt.insert(99-cr_data.get(i).split(",")[4].length(), cr_data.get(i).split(",")[4]);
					    matchDataTxt.insert(102-cr_data.get(i).split(",")[4].length(), cr_data.get(i).split(",")[5]);
					    matchDataTxt.insert(105-cr_data.get(i).split(",")[4].length(), cr_data.get(i).split(",")[6]);
					    matchDataTxt.insert(109-cr_data.get(i).split(",")[4].length(), cr_data.get(i).split(",")[7]);
					    
					    lineByLineData.add(matchDataTxt.toString());
				    }
				}
			}
		}
		
		if(!new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY 
			+ match.getMatch().getMatchFileName().replace(".json", ".h2h")).exists()) {
			
			File h2hFile = new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().getMatchFileName().replace(".json", ".h2h"));
			h2hFile.getParentFile().mkdirs(); 
			h2hFile.createNewFile();

		}
		FileWriter fileWriter = new FileWriter(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY 
			+ match.getMatch().getMatchFileName().replace(".json", ".h2h"));
		
		for(String str: lineByLineData) {
			fileWriter.write(str + System.lineSeparator());
		}
		fileWriter.close();		
	}
	
	public static String writeHeadToHead(MatchAllData match) throws IOException 
	{
		String line_txt = String.format("%-140s", "");
		String txt = String.format("%-140s", "");
		
		txt = addSubString(txt,"|",0);
		txt = addSubString(txt,"|    (B) - 'BO' - Bowling details" + "\n",0);
		txt = addSubString(txt,"|    (A) - 'IS' - Batting details" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"| Contents" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"| DOAD H2H File generated on " + LocalDate.now() + " at " + LocalTime.now() + "\n",0);
		txt = addSubString(txt,"| " + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);

		//Match_1_H2H.txt, Match_2_H2H.txt
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
			getMatchFileName().replace(".json", ".txt")), Arrays.asList(txt), StandardOpenOption.CREATE);
		setTextToTextFile(match, txt,line_txt);
			
		return null;
	}
	public static void setTextToTextFile(MatchAllData match,String txt,String line_txt) throws IOException {
					
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
				getMatchFileName().replace(".json", ".txt")), Arrays.asList(txt), StandardOpenOption.CREATE);
			
		txt = String.format("%-140s", "");
		
		txt = addSubString(txt,"|",0);
		txt = addSubString(txt,"|============================================================================================================================================================" + "\n",0);
		txt = addSubString(txt,"| 152 -154       Stumpings" + "\n",0);
		txt = addSubString(txt,"| 148 -150       Catches" + "\n",0);
		txt = addSubString(txt,"| 144 -146       Threes" + "\n",0);
		txt = addSubString(txt,"| 140 -142       Twos" + "\n",0);
		txt = addSubString(txt,"| 136 -138       Ones" + "\n",0);
		txt = addSubString(txt,"| 132 -134       Dots" + "\n",0);
		txt = addSubString(txt,"| 127 -130       Opponent ticker name" + "\n",0);
		txt = addSubString(txt,"| 122 -125       Team ticker name" + "\n",0);
		txt = addSubString(txt,"|      120       Was batsman dismissed?" + "\n",0);
		txt = addSubString(txt,"|      118       Did batsman innings start?" + "\n",0);
		txt = addSubString(txt,"| 114 -116       Balls to reach 100" + "\n",0);
		txt = addSubString(txt,"| 110 -112       Balls to reach 50" + "\n",0);
		txt = addSubString(txt,"| 106 -108       Sixes" + "\n",0);
		txt = addSubString(txt,"| 102 -104       Fours" + "\n",0);
		txt = addSubString(txt,"|  98 -100       Balls" + "\n",0);
		txt = addSubString(txt,"|  94 - 96       Runs" + "\n",0);
		txt = addSubString(txt,"|  88 - 92       Batsman code" + "\n",0);
		txt = addSubString(txt,"|  67 - 86       Opponent full name" + "\n",0);
		txt = addSubString(txt,"|  46 - 65       Team full name" + "\n",0);
		txt = addSubString(txt,"|  25 - 44       Venue name" + "\n",0);
		txt = addSubString(txt,"|   4 - 23       Match file name" + "\n",0);
		txt = addSubString(txt,"|   1 -  2       Line Ident ('IS')" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"|  Columns       Meaning" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"|  Batting data" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
				getMatchFileName().replace(".json", ".txt")), Arrays.asList(txt), StandardOpenOption.APPEND);
		
		line_txt = addSubString(line_txt,"|",0);
		line_txt = addSubString(line_txt,"<Match File Name   >",3);
		line_txt = addSubString(line_txt,"< Venue Name       >",24);
		line_txt = addSubString(line_txt,"< Team name        >",45);
		line_txt = addSubString(line_txt,"< Opponent Name    >",66);
		line_txt = addSubString(line_txt,"<BAT>",87);
		line_txt = addSubString(line_txt,"<R>",93);
		line_txt = addSubString(line_txt,"<B>",97);
		line_txt = addSubString(line_txt,"<4>",101);
		line_txt = addSubString(line_txt,"<6>",105);
		line_txt = addSubString(line_txt,"<F>",109);
		line_txt = addSubString(line_txt,"<H>",113);
		line_txt = addSubString(line_txt,"I",117);
		line_txt = addSubString(line_txt,"D",119);
		line_txt = addSubString(line_txt,"<TN>",121);
		line_txt = addSubString(line_txt,"<ON>",126);
		line_txt = addSubString(line_txt,"<D>",131);
		line_txt = addSubString(line_txt,"<1>",135);
		line_txt = addSubString(line_txt,"<2>",139);
		line_txt = addSubString(line_txt,"<3>",143);
		line_txt = addSubString(line_txt,"<C>",147);
		line_txt = addSubString(line_txt,"<S>",151);
		
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
			getMatchFileName().replace(".json", ".txt")), Arrays.asList(line_txt), StandardOpenOption.APPEND);
		
		setHeadToHeadData(match, line_txt, "BATTING");
		
		txt = String.format("%-140s", "");
		
		txt = addSubString(txt,"|",0);
		txt = addSubString(txt,"|============================================================================================================================================================" + "\n",0);
		txt = addSubString(txt,"| 124 -127       Last wicket ball count" + "\n",0);
		txt = addSubString(txt,"| 119 -122       Opponent ticker name" + "\n",0);
		txt = addSubString(txt,"| 114 -117       Team ticker name" + "\n",0);
		txt = addSubString(txt,"| 110 -112       Dot balls" + "\n",0);
		txt = addSubString(txt,"| 106 -108       Wickets" + "\n",0);
		txt = addSubString(txt,"| 102 -104       Runs" + "\n",0);
		txt = addSubString(txt,"|  98 -100       Maidens" + "\n",0);
		txt = addSubString(txt,"|  94 - 96       Balls" + "\n",0);
		txt = addSubString(txt,"|  88 - 92       Bowler code" + "\n",0);
		txt = addSubString(txt,"|  67 - 86       Opponent name" + "\n",0);
		txt = addSubString(txt,"|  46 - 65       Team name" + "\n",0);
		txt = addSubString(txt,"|  25 - 44       Venue name" + "\n",0);
		txt = addSubString(txt,"|   4 - 23       Match file name" + "\n",0);
		txt = addSubString(txt,"|   1 -  2       Line Ident ('BO')" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"|  Columns       Meaning" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		txt = addSubString(txt,"|  Bowling data" + "\n",0);
		txt = addSubString(txt,"|" + "\n",0);
		
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
				getMatchFileName().replace(".json", ".txt")), Arrays.asList(txt), StandardOpenOption.APPEND);
		
		line_txt = String.format("%-140s", "");
		
		line_txt = addSubString(line_txt,"|",0);
		line_txt = addSubString(line_txt,"<Match File Name   >",3);
		line_txt = addSubString(line_txt,"< Venue Name       >",24);
		line_txt = addSubString(line_txt,"< Team name        >",45);
		line_txt = addSubString(line_txt,"< Opponent Name    >",66);
		line_txt = addSubString(line_txt,"<BWL>",87);
		line_txt = addSubString(line_txt,"<B>",93);
		line_txt = addSubString(line_txt,"<M>",97);
		line_txt = addSubString(line_txt,"<R>",101);
		line_txt = addSubString(line_txt,"<W>",105);
		line_txt = addSubString(line_txt,"<D>",109);
		line_txt = addSubString(line_txt,"<TN>",113);
		line_txt = addSubString(line_txt,"<ON>",118);
		line_txt = addSubString(line_txt,"<LW>",123);
		
		Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
				getMatchFileName().replace(".json", ".txt")), Arrays.asList(line_txt), StandardOpenOption.APPEND);
		
		setHeadToHeadData(match, line_txt, "BOWLING");
	}
	public static void setHeadToHeadData(MatchAllData match, String line_txt, String type) throws IOException {
		
		for(Inning inn : match.getMatch().getInning()) {
			switch(type) {
			case "BATTING":
				for(BattingCard bc : inn.getBattingCard()) {
					line_txt = String.format("%-140s", "");
					
					line_txt = addSubString(line_txt,"IS",0);
					line_txt = addSubString(line_txt,match.getMatch().getMatchFileName(),3);
					line_txt = addSubString(line_txt,match.getSetup().getGround().getCity(),24);
					
					line_txt = addSubString(line_txt,inn.getBatting_team().getTeamName1(),45);
					line_txt = addSubString(line_txt,inn.getBowling_team().getTeamName1(),66);
					
					line_txt = addSubString(line_txt,String.valueOf(bc.getPlayerId()),92 - String.valueOf(bc.getPlayerId()).length());
					
					line_txt = addSubString(line_txt,String.valueOf(bc.getRuns()),96-String.valueOf(bc.getRuns()).length());
					line_txt = addSubString(line_txt,String.valueOf(bc.getBalls()),100-String.valueOf(bc.getBalls()).length());
					line_txt = addSubString(line_txt,String.valueOf(bc.getFours()),104 - String.valueOf(bc.getFours()).length());
					line_txt = addSubString(line_txt,String.valueOf(bc.getSixes()),108 - String.valueOf(bc.getSixes()).length());
					
					String[] ball_count = ballCountOfFiftyAndHundred(match.getEventFile().getEvents(), inn.getInningNumber(), bc.getPlayerId()).split("-");
					
					if(bc.getRuns() >= 50 && bc.getRuns() < 100) {
						line_txt = addSubString(line_txt,ball_count[0],112-ball_count[0].length());
						line_txt = addSubString(line_txt,"0",115);
					}else if(bc.getRuns() >= 100) {
						line_txt = addSubString(line_txt,ball_count[0],112-ball_count[0].length());
						line_txt = addSubString(line_txt,ball_count[1],116-ball_count[1].length());
					}else {
						line_txt = addSubString(line_txt,"0",111);
						line_txt = addSubString(line_txt,"0",115);
					}
					
					if(bc.getBatsmanInningStarted() != null && 
							bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
						line_txt = addSubString(line_txt,"Y",117);
						
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							line_txt = addSubString(line_txt,"Y",119);
						}else {
							line_txt = addSubString(line_txt,"N",119);
						}
						
					}else {
						line_txt = addSubString(line_txt,"N",117);
						line_txt = addSubString(line_txt,"-",119);
					}
					
					line_txt = addSubString(line_txt,"-",121);
					line_txt = addSubString(line_txt,"-",126);
					
					String[] Runs_Count = getScoreTypeData(CricketUtil.BATSMAN,match, inn.getInningNumber(), bc.getPlayerId(),
							"-", match.getEventFile().getEvents()).split("-");
					
					line_txt = addSubString(line_txt,Runs_Count[0],134-Runs_Count[0].length());
					line_txt = addSubString(line_txt,Runs_Count[1],138-Runs_Count[1].length());
					line_txt = addSubString(line_txt,Runs_Count[2],142-Runs_Count[2].length());
					line_txt = addSubString(line_txt,Runs_Count[3],146-Runs_Count[3].length());
					
					String[] Count = caughtAndStumpedCount(match.getEventFile().getEvents(), bc.getPlayerId()).split("-");
					line_txt = addSubString(line_txt,Count[0],148);
					line_txt = addSubString(line_txt,Count[1],152);
					
					Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
							getMatchFileName().replace(".json", ".txt")), Arrays.asList(line_txt), StandardOpenOption.APPEND);
				}
				break;
			case "BOWLING":
				for(BowlingCard boc : inn.getBowlingCard()) {
					line_txt = String.format("%-140s", "");
					
					line_txt = addSubString(line_txt,"BO",0);
					line_txt = addSubString(line_txt,match.getMatch().getMatchFileName(),3);
					
					line_txt = addSubString(line_txt,match.getSetup().getGround().getCity(),24);
					
					line_txt = addSubString(line_txt,inn.getBowling_team().getTeamName1(),45);
					line_txt = addSubString(line_txt,inn.getBatting_team().getTeamName1(),66);
					
					line_txt = addSubString(line_txt,String.valueOf(boc.getPlayerId()),92-String.valueOf(boc.getPlayerId()).length());
					line_txt = addSubString(line_txt,String.valueOf((boc.getOvers() * 6) + boc.getBalls()),96-String.valueOf((boc.getOvers() * 6) + boc.getBalls()).length());
					line_txt = addSubString(line_txt,String.valueOf(boc.getMaidens()),100-String.valueOf(boc.getMaidens()).length());
					line_txt = addSubString(line_txt,String.valueOf(boc.getRuns()),104-String.valueOf(boc.getRuns()).length());
					line_txt = addSubString(line_txt,String.valueOf(boc.getWickets()),108-String.valueOf(boc.getWickets()).length());
					line_txt = addSubString(line_txt,String.valueOf(boc.getDots()),112-String.valueOf(boc.getDots()).length());
					
					line_txt = addSubString(line_txt,"-",113);
					line_txt = addSubString(line_txt,"-",118);
					
					if(boc.getWickets() > 0) {
						line_txt = addSubString(line_txt,String.valueOf(lastWicketBallCount(match.getEventFile().getEvents(), inn.getInningNumber(), boc.getPlayerId())),
								126 - String.valueOf(lastWicketBallCount(match.getEventFile().getEvents(), inn.getInningNumber(), boc.getPlayerId())).length());
					}else {
						line_txt = addSubString(line_txt,"0",125);
					}
					
					Files.write(Paths.get(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().
							getMatchFileName().replace(".json", ".txt")), Arrays.asList(line_txt), StandardOpenOption.APPEND);
				}
				break;
			}
	    }
	}
	
	public static HeadToHead extractHeadToHead(MatchAllData match, CricketService cricketService) throws IOException {
		//Read Head To Head text file and store Data in Array List
		int playerId = -1;
		List<String> TeamName = new ArrayList<String>();
		List<String> headToHead = new ArrayList<String>();
		HeadToHead headToHead_master = new HeadToHead();
		int index = 2;
		
		if(new File (CricketUtil.CRICKET_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + match.getMatch().getMatchFileName().replace(".json", ".h2h")).exists()) {
			String text_to_return = "";
			try(BufferedReader br = new BufferedReader(new FileReader(CricketUtil.CRICKET_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY + 
					match.getMatch().getMatchFileName().replace(".json", ".h2h")))){
				while((text_to_return = br.readLine()) != null) {
					if(text_to_return.contains("|")) {
						
					}else {
						if(text_to_return.contains("IS") || text_to_return.contains("BO") || text_to_return.contains("TT") || 
								text_to_return.contains("PP") || text_to_return.contains("TB")) {
							headToHead.add(text_to_return);
						}
					}
				}
			}
		}
		
		for(int i=0;i<=headToHead.size()-1;i++) {
			if(headToHead.get(i).substring(0,3).trim().contains("IS")) {
				index = 2;
				TeamName.add(headToHead.get(i).substring(43,63).trim());	
				TeamName.add(headToHead.get(i).substring(63,82).trim());
				
				try {
					
					headToHead_master.getH2hPlayer().add(new HeadToHeadPlayer(Integer.valueOf(headToHead.get(i).substring(83,86).trim()),Integer.valueOf(headToHead.get(i).substring(87,90).trim()), 
							Integer.valueOf(headToHead.get(i).substring(90,93).trim()), Integer.valueOf(headToHead.get(i).substring(119,122).trim()), 
							Integer.valueOf(headToHead.get(i).substring(122,125).trim()), Integer.valueOf(headToHead.get(i).substring(125,128).trim()), 
							Integer.valueOf(headToHead.get(i).substring(128,131).trim()), Integer.valueOf(headToHead.get(i).substring(93,96).trim()), 
							Integer.valueOf(headToHead.get(i).substring(96,99).trim()), 0, 0, 0, 0, 0, headToHead.get(i).substring(2,22).trim(),
							cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(0))).findAny().orElse(null),
							cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(1))).findAny().orElse(null),
							headToHead.get(i).substring(106,108).trim(),headToHead.get(i).substring(109,111).trim(),headToHead.get(i).substring(23,42).trim()));
					} catch (Exception e) {
					
				}
				TeamName.clear();
				
			}
			else if(headToHead.get(i).substring(0,3).trim().contains("BO")) {
				index = 2;
				playerId = -1;
				for(int j=0;j<=headToHead_master.getH2hPlayer().size()-1;j++)
				{
					
					if(headToHead_master.getH2hPlayer().get(j).getPlayerId() == Integer.valueOf(headToHead.get(i).substring(83,86).trim()) &&
							headToHead_master.getH2hPlayer().get(j).getMatchFileName().equalsIgnoreCase(headToHead.get(i).substring(2,22).trim())) {
						playerId = j;
						break;
					}
				}
				if(playerId >= 0) {
					headToHead_master.getH2hPlayer().get(playerId).setWickets(Integer.valueOf(headToHead.get(i).substring(96,99).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setRunsConceded(Integer.valueOf(headToHead.get(i).substring(93,96).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setBallsBowled(Integer.valueOf(headToHead.get(i).substring(87,90).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setMaidens(Integer.valueOf(headToHead.get(i).substring(90,93).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setBalldots(Integer.valueOf(headToHead.get(i).substring(99,102).trim()));
					
				}else {
					TeamName.add(headToHead.get(i).substring(43,63).trim());	
					TeamName.add(headToHead.get(i).substring(63,82).trim());
					
					headToHead_master.getH2hPlayer().add(new HeadToHeadPlayer(Integer.valueOf(headToHead.get(i).substring(83,86).trim()), 0, 0, 0, 0, 0, 0, 0, 0, 
							Integer.valueOf(headToHead.get(i).substring(96,99).trim()), Integer.valueOf(headToHead.get(i).substring(93,96).trim()), 
							Integer.valueOf(headToHead.get(i).substring(87,90).trim()), Integer.valueOf(headToHead.get(i).substring(90,93).trim()), 
							Integer.valueOf(headToHead.get(i).substring(99,102).trim()), headToHead.get(i).substring(2,22).trim(), 
							cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(0))).findAny().orElse(null),
							cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(1))).findAny().orElse(null),
							"Y", "-", headToHead.get(i).substring(23,42)));
					
					TeamName.clear();
				}
			}else if(headToHead.get(i).substring(0,3).trim().contains("TT")) {
				
				TeamName.add(headToHead.get(i).substring(43,63).trim());	
				TeamName.add(headToHead.get(i).substring(63,82).trim());
				
				headToHead_master.getH2hTeam().add(new HeadToHeadTeam(Integer.valueOf(headToHead.get(i).substring(90,95).trim()),Integer.valueOf(headToHead.get(i).substring(96,102).trim()),
						Integer.valueOf(headToHead.get(i).substring(83,89).trim()),headToHead.get(i).substring(2,22).trim(),
								cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(0))).findAny().orElse(null),
								cricketService.getTeams().stream().filter(team -> team.getTeamName4().equalsIgnoreCase(TeamName.get(1))).findAny().orElse(null),headToHead.get(i).substring(23,42).trim()));
				TeamName.clear();
			}else if(headToHead.get(i).substring(0,3).trim().contains("PP")) {
				
				TeamName.add(headToHead.get(i).substring(43,63).trim());	
				TeamName.add(headToHead.get(i).substring(63,82).trim());
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP1_Run(Integer.valueOf(headToHead.get(i).substring(83,88).trim()));
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP2_Run(Integer.valueOf(headToHead.get(i).substring(89,94).trim()));
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP3_Run(Integer.valueOf(headToHead.get(i).substring(95,100).trim()));
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP1_Wicket(Integer.valueOf(headToHead.get(i).substring(101,106).trim()));
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP2_Wicket(Integer.valueOf(headToHead.get(i).substring(107,112).trim()));
				headToHead_master.getH2hTeam().get(headToHead_master.getH2hTeam().size() - index).setP3_Wicket(Integer.valueOf(headToHead.get(i).substring(113,118).trim()));
				index--;
				TeamName.clear();
			}else if(headToHead.get(i).substring(0,3).trim().contains("TB")) {
				playerId = -1;
				index = 2;
				for(int j=0;j<=headToHead_master.getH2hPlayer().size()-1;j++)
				{
					if(headToHead_master.getH2hPlayer().get(j).getPlayerId() == Integer.valueOf(headToHead.get(i).substring(83,86).trim()) &&
							headToHead_master.getH2hPlayer().get(j).getMatchFileName().equalsIgnoreCase(headToHead.get(i).substring(2,22).trim())) {
						playerId = j;
						break;
					}
				}
				if(playerId >= 0) {
					headToHead_master.getH2hPlayer().get(playerId).setTapeBall_wickets(Integer.valueOf(headToHead.get(i).substring(94,96).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setTapeBall_runs(Integer.valueOf(headToHead.get(i).substring(91,93).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setTapeBall_balls(Integer.valueOf(headToHead.get(i).substring(88,90).trim()));
					headToHead_master.getH2hPlayer().get(playerId).setTapeBall_dotsBall(Integer.valueOf(headToHead.get(i).substring(97,99).trim()));
				}
			}
		}
		
		return headToHead_master;
	}

	public static String addSubString(String main_string,String sub_string, int position) {
	    StringBuilder sb = new StringBuilder(main_string);
		    sb.insert(position, sub_string);
	    return sb.toString();
	}
	
	@SuppressWarnings("unused")
	public static void processCricketStats() {
        // Store batsman and bowler stats per innings
        Map<Integer, Map<String, BatsmanStats>> inningsBatsmanStats = new HashMap<>();
        Map<Integer, Map<String, BowlerStats>> inningsBowlerStats = new HashMap<>();

        String lastLine = null;
        String lastBatsman = null;
        String lastBowler = null;
        String lastOtherBatsman = null;
        
        Map<Integer, List<String>> overBallData = new HashMap<>(); // Stores ball-by-ball data per over
        int lastFullOver = -1; // Stores the number of the last full over (6 balls)

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Sports\\Cricket\\WT_File.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
            	
            	if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
            	
            	lastLine = line;
            	
            	int overNumber;
            	int innings;
            	
            	try {
                    innings = Integer.parseInt(line.substring(1, 4).trim());
                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                    System.err.println("Skipping line due to format issue: " + line);
                    continue; // Skip this line if it doesn't match the expected format
                }
               // innings = Integer.parseInt(line.substring(1, 4).trim());

                try {
                    overNumber = Integer.parseInt(line.substring(60, 64).trim());
                } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
                    System.err.println("Skipping line due to format issue: " + line);
                    continue; // Skip this line if it doesn't match the expected format
                }

                // Track ball-by-ball data for the current over
              //  overBallData.computeIfAbsent(overNumber, k -> new ArrayList<>()).add(line);

                // If the over has reached 6 balls, mark it as the last full over
                if (innings == Integer.valueOf(lastLine.substring(1, 4).trim())) {
                	String tovData = line.substring(157, 162).trim();
                    overBallData.computeIfAbsent(overNumber, k -> new ArrayList<>()).add(tovData);
                    
                    if (overBallData.get(overNumber).size() <= 6) {
                        lastFullOver = overNumber;
                    }
                }

                // Extract batsman, bowler, runs, and wicket
                String batsman = line.substring(8, 34).trim();
                String bowler = line.substring(34, 60).trim();
                String runsStr = line.substring(74, 78).trim();
                String wicket = line.substring(95, 97).trim(); // 'Y' indicates a wicket
                int runs = runsStr.isEmpty() ? 0 : Integer.parseInt(runsStr);

                // Initialize stats for the current innings if necessary
                inningsBatsmanStats.computeIfAbsent(innings, k -> new HashMap<>());
                inningsBowlerStats.computeIfAbsent(innings, k -> new HashMap<>());

                // Update batsman stats
                Map<String, BatsmanStats> batsmanStatsMap = inningsBatsmanStats.get(innings);
                batsmanStatsMap.computeIfAbsent(batsman, k -> new BatsmanStats()).addRuns(runs);
                batsmanStatsMap.get(batsman).addBall();

                // Update bowler stats
                Map<String, BowlerStats> bowlerStatsMap = inningsBowlerStats.get(innings);
                bowlerStatsMap.computeIfAbsent(bowler, k -> new BowlerStats()).addRunsConceded(runs);
                bowlerStatsMap.get(bowler).addBall();

                // If a wicket was taken, add it to the bowler's stats
                if ("Y".equals(wicket)) {
                    bowlerStatsMap.get(bowler).addWicket();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if (lastFullOver != -1) {
//            List<String> lastOverTovData = overBallData.get(lastFullOver);
//        }
        
        if (lastLine != null && lastLine.length() >= 128) {
            lastBatsman = lastLine.substring(8, 34).trim();
            lastBowler = lastLine.substring(34, 60).trim();
            lastOtherBatsman = lastLine.substring(131, 157).trim();  // Other batsman name
        }
        
        // Print Batsman Stats per Innings
//        for (Map.Entry<Integer, Map<String, BatsmanStats>> inningsEntry : inningsBatsmanStats.entrySet()) {
//            int innings = inningsEntry.getKey();
//            for (Map.Entry<String, BatsmanStats> entry : inningsEntry.getValue().entrySet()) {
//            	
//            }
//        }

//        for (Map.Entry<Integer, Map<String, BowlerStats>> inningsEntry : inningsBowlerStats.entrySet()) {
//            int innings = inningsEntry.getKey();
//            for (Map.Entry<String, BowlerStats> entry : inningsEntry.getValue().entrySet()) {
//            	if(lastBowler.equalsIgnoreCase(entry.getKey())) {
//            	}
//            }
//        }
    }
	public static ForeignLanguageData generateMatchResultForeignLanguage(MatchAllData match, String teamNameType, 
			MultiLanguageDatabase multiLanguageDb)
	{
		List<ForeignLanguageData> resultToShow = new ArrayList<ForeignLanguageData>();
		List<String> insertTxt = new ArrayList<String>();
		
		if(match.getMatch().getMatchResult() != null) {
			if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
					|| match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
				if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
						&& !match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
					resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
							CricketUtil.MATCH + " " + CricketUtil.TIED, "", null, 1, resultToShow);
				} else {
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								CricketUtil.MATCH + " " + CricketUtil.DRAWN, "", null, 1, resultToShow);
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								CricketUtil.MATCH + " " + CricketUtil.ABANDONED, "", null, 1, resultToShow);
					}
				}
			} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
				resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
						CricketUtil.NO_RESULT.replace("_", " "), "", null, 1, resultToShow);
			} else {
				if(match.getMatch().getMatchResult().contains(",")) {
					if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.TEAM, teamNameType, multiLanguageDb, 
								match.getSetup().getHomeTeam().getTeamName1(), "", null, 1, resultToShow);
					} else {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.TEAM, teamNameType, multiLanguageDb, 
								match.getSetup().getAwayTeam().getTeamName1(), "", null, 1, resultToShow);
					}
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, "", multiLanguageDb, 
								"WIN THE SUPER OVER", "", null, resultToShow.size() + 1, resultToShow);
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
							&& match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						insertTxt.add(match.getMatch().getMatchResult().split(",")[1]);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WON BY AN INNINGS AND RUN" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1])), 
								"", insertTxt, resultToShow.size() + 1, resultToShow);
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, match.getMatch().getMatchResult().split(",")[1], 
								"", null, resultToShow.size() + 1, resultToShow);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WIN BY RUN" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1])), 
								"", null, resultToShow.size() + 1, resultToShow);
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, match.getMatch().getMatchResult().split(",")[1], 
								"", null, resultToShow.size() + 1, resultToShow);
						resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.DICTIONARY, 
								"", multiLanguageDb, "WIN BY WICKET" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1])), 
								"", null, resultToShow.size() + 1, resultToShow);
					}
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DLS)) {
						resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, " (" + CricketUtil.DLS + ")", 
								"", null, resultToShow.size() + 1, resultToShow);
					}else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.VJD)) {
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
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut1.split(" ")[0]+"|", 
					"", null, resultToShow.size() + 1, resultToShow);
			
			Player plyr = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut1.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyr != null) {
				englishTxt = plyr.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
			
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "|" + howOut2.split(" ")[0]+ "|", 
					"", null, resultToShow.size() + 1, resultToShow);
			
			Player plyrs = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut2.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyrs != null) {
				englishTxt = plyrs.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
			
		}else if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.LBW)){
			String englishTxt = "";
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut1+"| |", 
					"", null, resultToShow.size() + 1, resultToShow);
			
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, howOut2.split(" ")[0]+ "|", 
					"", null, resultToShow.size() + 1, resultToShow);
			
			Player plyr = multiLanguageDb.getPlayers().stream()
					.filter(player -> howOut2.split(" ")[1].equalsIgnoreCase(player.getTicker_name()))
					.findAny().orElse(null);
			if(plyr != null) {
				englishTxt = plyr.getFull_name();
			}
			resultToShow = CricketFunctions.AssembleMultiLanguageData(CricketUtil.PLAYER, CricketUtil.FULLNAME, multiLanguageDb, 
					englishTxt, CricketUtil.FIRSTNAME, null, resultToShow.size() + 1, resultToShow);
		}else if(whichdata1.toUpperCase().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)){
			String englishTxt = "";
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "| |"+howOut1+"|", 
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
			resultToShow = CricketFunctions.AssembleMultiLanguageData("", "", multiLanguageDb, "| |" + howOut2.split(" ")[0] + "|", 
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
				if(SendTextIn.contains("$English$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$English$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("HINDI")) {
				if(SendTextIn.contains("$English$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Hindi$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("TAMIL")) {
				if(SendTextIn.contains("$English$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Tamil$") + "\0");
				}else {
					print_writers.get(i).println("-1 " + SendTextIn + "\0");
				}
			}else if(which_language.equalsIgnoreCase("TELUGU")) {
				if(SendTextIn.contains("$English$")) {
					print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Telugu$") + "\0");
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
				print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$English$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getEnglishText() + "\0");
			}else if(which_language.equalsIgnoreCase("HINDI")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Hindi$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getHindiText() + "\0");
			}else if(which_language.equalsIgnoreCase("TAMIL")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Tamil$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getTamilText() + "\0");
			}else if(which_language.equalsIgnoreCase("TELUGU")) {
				print_writers.get(i).println("-1 " + SendTextIn.replace("$English$", "$Telugu$") + foreignLanguageData.get(foreignLanguageData.size() - 1).getTeluguText() + "\0");
			}
		}
	}
	public static String GetVariousLanguageTextToEachViz(Configuration config, String broadcaster, List<PrintWriter> print_writers,
			List<ForeignLanguageData> foreignLanguageData) 
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
				return foreignLanguageData.get(foreignLanguageData.size() - 1).getEnglishText();
			}else if(which_language.equalsIgnoreCase("HINDI")) {
				return foreignLanguageData.get(foreignLanguageData.size() - 1).getHindiText();
			}else if(which_language.equalsIgnoreCase("TAMIL")) {
				return foreignLanguageData.get(foreignLanguageData.size() - 1).getTamilText();
			}else if(which_language.equalsIgnoreCase("TELUGU")) {
				return foreignLanguageData.get(foreignLanguageData.size() - 1).getTeluguText();
			}
		}
		return null;
	}
	public static void DoadWriteCommandToSelectedViz(int SelectedViz, String SendTextIn, List<PrintWriter> print_writers) 
	{
		if(SelectedViz > 0 && SelectedViz <= print_writers.size()) {
			print_writers.get(SelectedViz-1).println(SendTextIn);
		}
	}	
	public static void DoadWriteCommandToAllViz(String SendTextIn, List<PrintWriter> print_writers) 
	{
		for(int i = 0; i < print_writers.size(); i++) {
			print_writers.get(i).println(SendTextIn);
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
			Player plyr = null;
			switch(whichDBColumnToProcess) {
			case CricketUtil.TICKERNAME:
				plyr = multiLanguage.getPlayers().stream()
				.filter(player -> foreignTextToProcess.equalsIgnoreCase(player.getTicker_name()))
				.findAny().orElse(null);
				break;
			default:
				plyr = multiLanguage.getPlayers().stream()
					.filter(player -> foreignTextToProcess.equalsIgnoreCase(player.getFull_name()))
					.findAny().orElse(null);
				break;
			}
			
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
				case CricketUtil.TICKERNAME:
					englishTxt = plyr.getTicker_name();
		        	hindiTxt = plyr.getHindiTickerName();
		        	tamilTxt = plyr.getTamilTickerName();
		        	teluguTxt = plyr.getTeluguTickerName();
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

		foreignLanguageData.setEnglishText(englishTxt);
		foreignLanguageData.setHindiText(hindiTxt);
		foreignLanguageData.setTamilText(tamilTxt);
		foreignLanguageData.setTeluguText(teluguTxt);

		if (ForeignLanguageArrayIndex > 0) { // 2

			if(ForeignLanguageArrayIndex == 1) {
				foreignLanguageDataList = new ArrayList<ForeignLanguageData>();
			}
        	foreignLanguageDataList.add(ForeignLanguageArrayIndex-1,foreignLanguageData); 

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
		
		try {
			if(config.getQtIpAddress() != null && !config.getQtIpAddress().isEmpty()) {
				print_writer.add(new PrintWriter(new Socket(config.getQtIpAddress(), 
					config.getQtPortNumber()).getOutputStream(), true));
			}
		} catch (ConnectException e) {
			System.out.println("Unable to create print writer for QT");
		}

		return print_writer;
	}

	public static MatchClock getMatchClock(MatchAllData match) throws IOException, JAXBException 
	{
		MatchClock clock;
		if(match.getMatch().getInning() != null && match.getMatch().getInning().size() > 0) {
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.CLOCK_XML.toUpperCase().replace(".XML", ".JSON")).exists()) {
				clock = (MatchClock) new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.CLOCK_XML.toUpperCase().replace(".XML", ".JSON")), MatchClock.class);
				for(Inning inn : match.getMatch().getInning()) {
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
		if(!speedFilePath.trim().isEmpty()) {
			File speed_file = new File(speedFilePath);
			if(speed_file.exists()) {
				if(lastSpeed.getSpeedFileModifiedTime() != speed_file.lastModified()) {
					return new Speed(Files.readAllLines(Paths.get(speedFilePath), StandardCharsets.UTF_8).get(0), 
							speed_file.lastModified());
				}
			}
		}
		return null;
	}

	public static BatSpeed processCurrentBatSpeed(String batSpeedSourceFilePath, 
		String batSpeedDestinationFilePath, BatSpeed lastBatSpeed) throws IOException {
		
		if(!batSpeedSourceFilePath.trim().isEmpty()) {
        	if(lastBatSpeed.getBatSpeedFileModifiedTime() != new File(batSpeedSourceFilePath).lastModified()) {
    			BatSpeed bat_speed = new ObjectMapper().readValue(
   					new File(batSpeedSourceFilePath), new BatSpeed().getClass());
    			bat_speed.setBatSpeedFileModifiedTime(new File(batSpeedSourceFilePath).lastModified());
    			objectWriter.writeValue(new File(batSpeedDestinationFilePath), bat_speed);
   				//Files.write(Paths.get(batSpeedDestinationFilePath),objectWriter.writeValueAsString(bat_speed).getBytes());			
   				return bat_speed;
        	}
		}
		return null;
	}
	public static Speed saveCurrentSpeeds(String broadcaster, String speedSourcePath,String speedDestinationPath, Speed lastSpeed) throws IOException {

		File dir = new File(speedSourcePath);
	
		if (dir.isDirectory()) {
			Optional<File> opFile = Arrays.stream(dir.listFiles(File::isFile)).max(Comparator.comparingLong(File::lastModified));
			
			if (opFile.isPresent()) {
				File latestFile = opFile.get();
				String latestFileName = latestFile.getName();
				long latestModifiedTime = latestFile.lastModified();
				
				boolean shouldUpdate = 
				!latestFileName.equals(lastSpeed.getSpeedExtra()) ||
				latestModifiedTime != lastSpeed.getSpeedFileModifiedTime();
				
				if (shouldUpdate) {
					List<String> allLines = Files.readAllLines(latestFile.toPath(), StandardCharsets.UTF_8);
					
					if (allLines.size() >= 2) {
						String secondLine = allLines.get(1); // second line
					
						if (secondLine.contains(",")) {
							String speedValue = secondLine.split(",")[1].trim();
							lastSpeed.setSpeedValue(speedValue);
							lastSpeed.setSpeedFileModifiedTime(latestModifiedTime);
							lastSpeed.setSpeedExtra(latestFileName);
							
							try (BufferedWriter writer = new BufferedWriter(new FileWriter(speedDestinationPath))) {
							   writer.write(speedValue);
							}
							
						return lastSpeed;
						}
					}
				}
			}
		}	
	  return lastSpeed;
	}

	public static Speed saveCurrentSpeed(String broadcaster, String speedSourcePath, 
			String speedDestinationPath, Speed lastSpeed) throws IOException 
	{
		Speed speed_to_return = new Speed();
		BufferedWriter writer;
		switch (broadcaster.toUpperCase()) {
		case CricketUtil.HAWKEYE:

//			File this_dir = new File(speedSourcePath);
//			
//		    if (this_dir.isDirectory()) {
//		    	
//		        Optional<File> opFile = Arrays.stream(this_dir.listFiles(File::isFile))
//		          .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
//		        
//		        if (opFile.isPresent()){
//		        	if(lastSpeed.getSpeedFileModifiedTime() != opFile.get().lastModified()) {
//			        	for(String str_line : Files.readAllLines(Paths.get(
//			        			speedSourcePath + opFile.get().getName()), StandardCharsets.UTF_8)) {
//			        		if(str_line.contains(",")) {
//			        			System.out.println("str_line = " + str_line);
//								speed_to_return.setSpeedValue(str_line.split(",")[1]);
//								speed_to_return.setSpeedFileModifiedTime(opFile.get().lastModified());
//								writer = new BufferedWriter(new FileWriter(speedDestinationPath));
//							    writer.write(str_line.split(",")[1].trim());
//							    writer.close();							
//								return speed_to_return;
//			        		}
//			        	}
//		        	}
//		        }
//		    }
			File dir = new File(speedSourcePath);
			
			if (dir.isDirectory()) {
				Optional<File> opFile = Arrays.stream(dir.listFiles(File::isFile)).max(Comparator.comparingLong(File::lastModified));
				
				if (opFile.isPresent()) {
					File latestFile = opFile.get();
					String latestFileName = latestFile.getName();
					long latestModifiedTime = latestFile.lastModified();
					
					boolean shouldUpdate = 
					!latestFileName.equals(lastSpeed.getSpeedExtra()) ||
					latestModifiedTime != lastSpeed.getSpeedFileModifiedTime();
					
					if (shouldUpdate) {
						List<String> allLines = Files.readAllLines(latestFile.toPath(), StandardCharsets.UTF_8);
						
						if (allLines.size() >= 2) {
							String secondLine = allLines.get(1); // second line
						
							if (secondLine.contains(",")) {
								String speedValue = secondLine.split(",")[1].trim();
								lastSpeed.setSpeedValue(speedValue);
								lastSpeed.setSpeedFileModifiedTime(latestModifiedTime);
								lastSpeed.setSpeedExtra(latestFileName);
								
								try (BufferedWriter writers = new BufferedWriter(new FileWriter(speedDestinationPath))) {
									writers.write(speedValue);
								}
								
							return lastSpeed;
							}
						}
					}
				}
			}
//		    File this_dir = new File(speedSourcePath);
//
//		    if (this_dir.isDirectory()) {
//
//		        Optional<File> opFile = Arrays.stream(this_dir.listFiles(File::isFile))
//		            .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
//
//		        if (opFile.isPresent()) {
//		            File latestFile = opFile.get();
//
//		            if (lastSpeed.getSpeedFileModifiedTime() != latestFile.lastModified()) {
//
//		                List<String> allLines = Files.readAllLines(Paths.get(speedSourcePath + latestFile.getName()), StandardCharsets.UTF_8);
//
//		                if (allLines.size() >= 2) {
//		                    String secondLine = allLines.get(1);  // Index 1 = second line
//
//		                    if (secondLine.contains(",")) {
//		                        System.out.println("Second line = " + secondLine);
//
//		                        String speedValue = secondLine.split(",")[1].trim();
//
//		                        speed_to_return.setSpeedValue(speedValue);
//		                        speed_to_return.setSpeedFileModifiedTime(latestFile.lastModified());
//
//		                        writer = new BufferedWriter(new FileWriter(speedDestinationPath));
//		                        writer.write(speedValue);
//		                        writer.close();
//
//		                        return speed_to_return;
//		                    }
//		                }
//		            }
//		        }
//		    }

			break;
		case CricketUtil.KHELAI:

//			File this_dir1 = new File(speedSourcePath);
//			
//		    if (this_dir1.isDirectory()) {
//		    	
//		        Optional<File> opFile = Arrays.stream(this_dir1.listFiles(File::isFile))
//		          .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
//		        
//		        if (opFile.isPresent()){
//		        	if(lastSpeed.getSpeedFileModifiedTime() != opFile.get().lastModified()) {
//			        	for(String str_line : Files.readAllLines(Paths.get(
//			        			speedSourcePath + opFile.get().getName()), StandardCharsets.UTF_8)) {
//			        		speed_to_return.setSpeedValue(str_line);
//							speed_to_return.setSpeedFileModifiedTime(opFile.get().lastModified());
//							writer = new BufferedWriter(new FileWriter(speedDestinationPath));
//						    writer.write(str_line.trim());
//						    writer.close();							
//							return speed_to_return;
//			        	}
//		        	}
//		        }
//		    }
			
			File this_dir1 = new File(speedSourcePath);

	        if (this_dir1.isDirectory()) {
	            Optional<File> opFile = Arrays.stream(this_dir1.listFiles(File::isFile))
	                .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

	            if (opFile.isPresent()) {
	                File latestFile = opFile.get();
	                long currentModifiedTime = latestFile.lastModified();

	                // Check if the file has been modified
	                if (lastModifiedTime != currentModifiedTime) {
	                    lastModifiedTime = currentModifiedTime; // Update global timestamp

	                    // Read the file content
	                    for (String str_line : Files.readAllLines(Paths.get(latestFile.getPath()), StandardCharsets.UTF_8)) {
	                        //SpeedObject speedToReturn = new SpeedObject();
	                    	speed_to_return.setSpeedValue(str_line);
	                    	speed_to_return.setSpeedFileModifiedTime(currentModifiedTime);

	                        // Write to the destination file
	                        try (BufferedWriter writers = new BufferedWriter(new FileWriter(speedDestinationPath))) {
	                            writers.write(str_line.trim());
	                        }

	                        return speed_to_return; // Return the updated speed object
	                    }
	                }
	            }
	        }

	        //return null;
			break;
		case "KADAMBA":

			File this_dir2 = new File(speedSourcePath);

	        if (this_dir2.isDirectory()) {
	            Optional<File> opFile = Arrays.stream(this_dir2.listFiles(File::isFile))
	                .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));

	            if (opFile.isPresent()) {
	                File latestFile = opFile.get();
	                long currentModifiedTime = latestFile.lastModified();

	                // Check if the file has been modified
	                if (lastModifiedTime != currentModifiedTime) {
	                    lastModifiedTime = currentModifiedTime; // Update global timestamp

	                    // Read the file content
	                    for (String str_line : Files.readAllLines(Paths.get(latestFile.getPath()), StandardCharsets.UTF_8)) {
	                        //SpeedObject speedToReturn = new SpeedObject();
	                    	speed_to_return.setSpeedValue(str_line);
	                    	speed_to_return.setSpeedFileModifiedTime(currentModifiedTime);

	                        // Write to the destination file
	                        try (BufferedWriter writers = new BufferedWriter(new FileWriter(speedDestinationPath))) {
	                            writers.write(str_line.trim());
	                        }

	                        return speed_to_return; // Return the updated speed object
	                    }
	                }
	            }
	        }

	        //return null;
			break;
		case "VIRTUAL_EYE":
			File this_directory = new File(speedSourcePath);
			
		    if (this_directory.isDirectory()) {
		    	
		        Optional<File> opFile = Arrays.stream(this_directory.listFiles(File::isFile))
		          .max((f1, f2) -> Long.compare(f1.lastModified(), f2.lastModified()));
		        
		        if (opFile.isPresent()){
		        	if(lastSpeed.getSpeedFileModifiedTime() != opFile.get().lastModified()) {
			        	for(String str_line : Files.readAllLines(Paths.get(
			        			speedSourcePath + opFile.get().getName()), StandardCharsets.UTF_8)) {
								speed_to_return.setSpeedValue(str_line.substring(1));
								speed_to_return.setSpeedFileModifiedTime(opFile.get().lastModified());
								writer = new BufferedWriter(new FileWriter(speedDestinationPath));
							    writer.write(str_line);
							    writer.close();							
								return speed_to_return;
			        	}
		        	}
		        }
		    }
			break;
		}
		return null;
	}
	
	public static Review getCurrentReview(String reviewFilePath, Review lastReview) throws IOException {
		if(!reviewFilePath.trim().isEmpty()) {
			File review_file = new File(reviewFilePath);
			if(review_file.exists()) {
				if(lastReview.getLastTimeStamp() != review_file.lastModified()) {
	        		return new Review(Files.readAllLines(Paths.get(reviewFilePath), StandardCharsets.UTF_8).get(0), 
	        			review_file.lastModified());
				}
			}
		}
		return null;
	}
	public static Review getReviewRemaining(MatchAllData match) throws Exception {
		int Home_review = Integer.valueOf(match.getSetup().getReviewsPerTeam());
		int Away_review = Integer.valueOf(match.getSetup().getReviewsPerTeam());
		for(Inning inn :match.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				if(inn.getReviews()!= null) {
					for(Review rw :inn.getReviews()) {
						if(rw.getReviewRetained().equalsIgnoreCase("unretained")) {
							if(rw.getReviewTeamId() == match.getSetup().getHomeTeamId()) {
								Home_review = Math.max(0, (Home_review-1));
							}else if(rw.getReviewTeamId() == match.getSetup().getAwayTeamId()) {
								Away_review = Math.max(0, (Away_review-1));
							}
						}
					}	
				}
			}
		}
		return new Review(Home_review +","+Away_review,0);
	}
	
	public static int lastWicketBallCount(List<Event> events, int inningNumber, int playerId) {
		int ball_count = 0;
		
		if(events != null && events.size()>0) {
			for(Event evnt : events) {
				if(inningNumber == evnt.getEventInningNumber()) {
					if(evnt.getEventBowlerNo() == playerId) {
						switch (evnt.getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: case CricketUtil.SIX:
						case CricketUtil.FIVE: case CricketUtil.BYE: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.LEG_BYE:
						case CricketUtil.LOG_ANY_BALL:
			  	        	ball_count ++;
			  	          break;
			  	        case CricketUtil.LOG_WICKET:
			  	        	ball_count = 0;
			  	        	break;
			  	        }
					}
				}
			}
		}
		return ball_count;
	}
	
	public static String caughtAndStumpedCount(List<Event> events, int playerId) {
		int caught_count = 0,stumped_count = 0;
		
		if(events != null && events.size()>0) {
			for(Event evnt : events) {
				if(evnt.getEventHowOutFielderId() > 0) {
					if(evnt.getEventType().equalsIgnoreCase(CricketUtil.LOG_WICKET)) {
						if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)) {
							if(evnt.getEventHowOutFielderId() == playerId) {
								caught_count ++ ;
							}
						}else if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)) {
							if(evnt.getEventHowOutFielderId() == playerId) {
								stumped_count ++ ;
							}
						}
					}
				}
			}
		}
		return caught_count + "-" + stumped_count;
	}
	
	public static String ballCountOfFiftyAndHundred(List<Event> events, int inningNumber, int playerId) {
		int ball50_count = 0,ball100_count = 0,runs=0;
		
		if(events != null && events.size()>0) {
			for(Event evnt : events) {
				if(inningNumber == evnt.getEventInningNumber()) {
					if(evnt.getEventBatterNo() == playerId) {
						switch(evnt.getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
						case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE:
							runs = runs + evnt.getEventRuns(); 
							if(runs > 50 && runs <= 100) {
								ball100_count ++;
							}else if(runs <= 50) {
								ball50_count ++;
								ball100_count ++;
							}
							break;
						case CricketUtil.LOG_ANY_BALL:
						  runs = runs + evnt.getEventRuns();
				          if (evnt.getEventExtra() != null) {
				        	  runs = runs + evnt.getEventExtraRuns();
				          }
				          if (evnt.getEventSubExtra() != null) {
				        	  runs = runs + evnt.getEventSubExtraRuns();
				          }
				          
				          if(runs > 50 && runs <= 100) {
								ball100_count ++;
				          }else if(runs <= 50) {
								ball50_count ++;
								ball100_count ++;
						  }
				          break;
						case CricketUtil.LOG_WICKET:
							if (evnt.getEventRuns() > 0)
                            {
								runs = runs + evnt.getEventRuns();
                            }
							
							if(runs > 50 && runs <= 100) {
								ball100_count ++;
							}else if(runs <= 50) {
								ball50_count ++;
								ball100_count ++;
							}
							break;
						}
					}
				}
			}
		}
		return ball50_count + "-" + ball100_count;
	}
	
	public static BowlingCard getCurrentInningCurrentBowler(MatchAllData match) {
		BowlingCard current_bowler = null;
		for(Inning inn : match.getMatch().getInning()) {
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
	public static List<LeaderBoard> processLeaderBoard(CricketService cricketService) {
		List<LeaderBoard> leaderboards = cricketService.getLeaderBoards();
		
	    Map<Integer, Player> playerMap = cricketService.getAllPlayer()
	        .stream().collect(Collectors.toMap(Player::getPlayerId, Function.identity()));
	    Map<Integer, Team> teamMap = cricketService.getTeams()
		        .stream().collect(Collectors.toMap(Team::getTeamId, Function.identity()));

	    leaderboards.forEach(lb -> {
	        lb.setPlayer1(playerMap.get(lb.getPlayer1Id()));
	        lb.setPlayer2(playerMap.get(lb.getPlayer2Id()));
	        lb.setPlayer3(playerMap.get(lb.getPlayer3Id()));
	        lb.setPlayer4(playerMap.get(lb.getPlayer4Id()));
	        lb.setPlayer5(playerMap.get(lb.getPlayer5Id()));
	        
	        lb.setTeam1(teamMap.get(lb.getPlayer1().getTeamId()));
	        lb.setTeam2(teamMap.get(lb.getPlayer2().getTeamId()));
	        lb.setTeam3(teamMap.get(lb.getPlayer3().getTeamId()));
	        lb.setTeam4(teamMap.get(lb.getPlayer4().getTeamId()));
	        lb.setTeam5(teamMap.get(lb.getPlayer5().getTeamId()));
	    });

	    return leaderboards;
	}
	
	public static List<Fixture> getFixturesByTeam(int teamId, List<Fixture> allFixtures) {
        List<Fixture> filteredFixtures = new ArrayList<>();

        for (Fixture fixture : allFixtures) {
            if (fixture.getHometeamid() == teamId) {
                filteredFixtures.add(fixture);  // Add if home team matches
            } else if (fixture.getAwayteamid() == teamId) {
                filteredFixtures.add(fixture);  // Add if away team matches
            }
        }
        return filteredFixtures;  // Ensure return statement is correctly placed
    }
	
	public static List<Statistics> processAllStats(CricketService cricketService){
		List<Statistics> stats = cricketService.getAllStats();
		for(Statistics s : stats) {
			for(StatsType st : cricketService.getAllStatsType()) {
				if(st.getStats_id() == s.getStats_type_id()) {
					s.setStats_type(st);
					break;
				}
			}
		}
		return stats;
	}
	
	public static List<Staff> processAllStaff(CricketService cricketService, int teamId) {
		
		List<Staff> staff = new ArrayList<Staff>();
		for(Staff st : cricketService.getStaff()) {
			if(st.getClubId() == teamId) {
				st.setTeam(cricketService.getTeams().get(teamId-1));
				staff.add(st);
			}
		}
		return staff;
	}
	
	public static String whenWriteStringUsingBufferedWritter_thenCorrect(String str) 
			  throws IOException {
			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Temp/LogFile.txt"));
			    writer.write(str);
			    
			    writer.close();
		return "";
	}
	
	public static BestStats getProcessedBatsmanBestStats(BestStats best_stats) throws JsonMappingException, JsonProcessingException {
		
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

	public static BestStats getProcessedBowlerBestStats(BestStats best_stats) throws JsonMappingException, JsonProcessingException {
		
//		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//		objectMapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
		
//		BestStats return_best_stats = objectMapper.readValue(objectMapper.writeValueAsString(best_stats), BestStats.class);

		best_stats.setRuns(1000 - (best_stats.getBestEquation() % 1000));
		if(best_stats.getBestEquation() % 1000 > 0) {
			best_stats.setWickets(best_stats.getBestEquation() / 1000 + 1);
		} else {
			best_stats.setWickets(best_stats.getBestEquation() / 1000);
		}

		return best_stats;
	
	}
	
	public static String getBowlerType(String BowlerType) {
		switch(BowlerType.toUpperCase()) {
		case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
			return "PACE";
		case "ROB": case "RLB": case "RLG": case "WSR": case "LSL": case "WSL": case "LCH":  case "LSO":
			return "SPIN";
		}
		return "";
	}
	
	public static String RoleType(String Role) {
		switch (Role) {
		case CricketUtil.BATSMAN: case CricketUtil.BAT: case "BAT/KEEPER":
			return "BATTER";
		case CricketUtil.ALL_ROUNDER: case CricketUtil.BOWLER:
			return Role;
		case CricketUtil.WICKET_KEEPER: case "CAPTAIN_WICKET_KEEPER": 
			return CricketUtil.WICKET_KEEPER.replace("_", "-");
		default:
			return Role;
		}
	}
	
	public static void getBatsmanSRAgainstPaceAndSpin(int PlayerId,int Value,CricketService cricketService,List<Tournament> tournament_stats,MatchAllData match) {
		for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
			if(PlayerId == match.getEventFile().getEvents().get(i).getEventBatterNo()) {
				switch (match.getEventFile().getEvents().get(i).getEventType()) {
				case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
				case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET:
					
					if(getBowlerType(cricketService.getAllPlayer().get(match.getEventFile().getEvents().get(i).getEventBowlerNo()-1)
							.getBowlingStyle()).equalsIgnoreCase("PACE")) {
						
						tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
								match.getEventFile().getEvents().get(i).getEventRuns());
						tournament_stats.get(Value).setBalls_against_pace(tournament_stats.get(Value).getBalls_against_pace() + 1);
						
					}else if(getBowlerType(cricketService.getAllPlayer().get(match.getEventFile().getEvents().get(i)
							.getEventBowlerNo()-1).getBowlingStyle()).equalsIgnoreCase("SPIN")) {
						
						tournament_stats.get(Value).setRuns_against_spin(tournament_stats.get(Value).getRuns_against_spin() + 
								match.getEventFile().getEvents().get(i).getEventRuns());
						tournament_stats.get(Value).setBalls_against_spin(tournament_stats.get(Value).getBalls_against_spin() + 1);
					}
					
					break;
				
				case CricketUtil.LOG_ANY_BALL:
					if(getBowlerType(cricketService.getAllPlayer().get(match.getEventFile().getEvents().get(i).getEventBowlerNo()-1)
							.getBowlingStyle()).equalsIgnoreCase("PACE")) {
						
						tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
								match.getEventFile().getEvents().get(i).getEventRuns());
						
						if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
							tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
									match.getEventFile().getEvents().get(i).getEventExtraRuns());
				        }
				        if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
				        	tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
				        			match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
				        }
				        
				        tournament_stats.get(Value).setBalls_against_pace(tournament_stats.get(Value).getBalls_against_pace() + 1);
						
					}else if(getBowlerType(cricketService.getAllPlayer().get(match.getEventFile().getEvents().get(i).getEventBowlerNo()-1)
							.getBowlingStyle()).equalsIgnoreCase("SPIN")) {
						
						tournament_stats.get(Value).setRuns_against_spin(tournament_stats.get(Value).getRuns_against_spin() + 
								match.getEventFile().getEvents().get(i).getEventRuns());
						if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
							tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
									match.getEventFile().getEvents().get(i).getEventExtraRuns());
				        }
				        if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
				        	tournament_stats.get(Value).setRuns_against_pace(tournament_stats.get(Value).getRuns_against_pace() + 
				        			match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
				        }
				        
				        tournament_stats.get(Value).setBalls_against_spin(tournament_stats.get(Value).getBalls_against_spin() + 1);
					}
					break;
				}
			}
		}
	}
	
	public static List<MatchAllData> getTournamentMatches(File[] files, CricketService cricketService) 
			throws IllegalAccessException, InvocationTargetException, JAXBException, StreamReadException, DatabindException, IOException
	{
		MatchAllData this_matchAllData = new MatchAllData();
		List<MatchAllData> tournament_matches = new ArrayList<MatchAllData>();
		for(File file : files) {
			this_matchAllData = new MatchAllData();
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + file.getName().toUpperCase()).exists()) {
				this_matchAllData.setSetup(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + 
							file.getName().toUpperCase()), Setup.class));
				this_matchAllData.setMatch(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY + 
						file.getName().toUpperCase()), Match.class));
			}
			if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + file.getName().toUpperCase()).exists()) {
				this_matchAllData.setEventFile(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY + 
						file.getName().toUpperCase()), EventFile.class));
			}

			tournament_matches.add(CricketFunctions.populateMatchVariables(cricketService,this_matchAllData));

		}
		
		return tournament_matches;
	}
	
	public static Tournament extracttournamentFoursAndSixes(String typeOfExtraction, List<MatchAllData> tournament_matches, MatchAllData currentMatch, 
			Tournament past_tournament_stat) throws CloneNotSupportedException 
	{
		Tournament tournament_stats = new Tournament();	
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			 return extracttournamentFoursAndSixes("CURRENT_MATCH_DATA", tournament_matches, currentMatch, 
					 extracttournamentFoursAndSixes("PAST_MATCHES_DATA", tournament_matches, currentMatch, null));
		case "PAST_MATCHES_DATA":
			for(MatchAllData match : tournament_matches) {
				if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					for(Inning inn : match.getMatch().getInning()) {
						tournament_stats.setTournament_fours(tournament_stats.getTournament_fours() + inn.getTotalFours());
						tournament_stats.setTournament_sixes(tournament_stats.getTournament_sixes() + inn.getTotalSixes());
					}
				}
			}
			return tournament_stats;
		case "CURRENT_MATCH_DATA":
			Tournament past_tournament_stat_clone = new Tournament();
			if(past_tournament_stat  != null) {
				past_tournament_stat_clone = past_tournament_stat.clone(); // create clone of past_tournament_stat
			}
			
			for(Inning inn : currentMatch.getMatch().getInning()) {
				past_tournament_stat_clone.setTournament_fours(past_tournament_stat_clone.getTournament_fours() + inn.getTotalFours());
				past_tournament_stat_clone.setTournament_sixes(past_tournament_stat_clone.getTournament_sixes() + inn.getTotalSixes());
			}
			return past_tournament_stat_clone;
		}
		
		return null;
	}
	
	public static Tournament extracttournamentFoursAndSixesData(String typeOfExtraction, List<HeadToHeadPlayer> tournament_matches, 
			MatchAllData currentMatch, Tournament past_tournament_stat) throws CloneNotSupportedException 
	{
		Tournament tournament_stats = new Tournament();	
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			 return extracttournamentFoursAndSixesData("CURRENT_MATCH_DATA", tournament_matches, currentMatch, 
					 extracttournamentFoursAndSixesData("PAST_MATCHES_DATA", tournament_matches, currentMatch, null));
		case "PAST_MATCHES_DATA":
			for (HeadToHeadPlayer mtch : tournament_matches) {
				if(!mtch.getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					tournament_stats.setTournament_fours(tournament_stats.getTournament_fours() + mtch.getFours());
					tournament_stats.setTournament_sixes(tournament_stats.getTournament_sixes() + mtch.getSixes());
				}
			}
			
			return tournament_stats;
		case "CURRENT_MATCH_DATA":
			Tournament past_tournament_stat_clone = new Tournament();
			if(past_tournament_stat  != null) {
				past_tournament_stat_clone = past_tournament_stat.clone(); // create clone of past_tournament_stat
			}
			
			for(Inning inn : currentMatch.getMatch().getInning()) {
				past_tournament_stat_clone.setTournament_fours(past_tournament_stat_clone.getTournament_fours() + inn.getTotalFours());
				past_tournament_stat_clone.setTournament_sixes(past_tournament_stat_clone.getTournament_sixes() + inn.getTotalSixes());
			}
			return past_tournament_stat_clone;
		}
		
		return null;
	}
	
	public static List<BestStats> Top10Players(List<Tournament> tournament) throws CloneNotSupportedException, JsonMappingException, JsonProcessingException 
	{
		List<BestStats> top_ten_beststat = new ArrayList<BestStats>();
		for(Tournament tourn : tournament) {
			for(BestStats bs : tourn.getBatsman_best_Stats()) {
				top_ten_beststat.add(CricketFunctions.getProcessedBatsmanBestStats(bs));
			}
		}
		Collections.sort(top_ten_beststat,new CricketFunctions.BatsmanBestStatsComparator());
//		for(BestStats bs: top_ten_beststat) {
//			System.out.println(bs.getPlayerId()+"  "+ bs.getRuns());
//		}
		return top_ten_beststat;
		
	}
	public static List<Tournament> extractPlayersStats(String typeOfExtraction, boolean showBowlerDissmisal, List<MatchAllData> tournament_matches, MatchAllData currentMatch, 
			List<Tournament> past_tournament_dismissal_stat) throws CloneNotSupportedException 
	{
		int playerId = -1;
		List<Tournament> tournament_dismissal_stats = new ArrayList<Tournament>();
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			 return extractPlayersStats("CURRENT_MATCH_DATA", showBowlerDissmisal, tournament_matches, currentMatch, 
					 extractPlayersStats("PAST_MATCHES_DATA", showBowlerDissmisal, tournament_matches, currentMatch, null));
		case "PAST_MATCHES_DATA":
			for(MatchAllData match : tournament_matches) {
				if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					for(Inning inn : match.getMatch().getInning()) {
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
							
							for(BowlingCard boc : inn.getBowlingCard())
							{
								playerId = -1;
								for(int i=0; i<=tournament_dismissal_stats.size() - 1;i++)
								{
									if(boc.getPlayerId() == tournament_dismissal_stats.get(i).getPlayerId()) {
										playerId = i;
										break;
									}
								}
								
								if(playerId >= 0) {
									if(boc.getWickets() > 0) {
										switch(String.valueOf(boc.getWickets())) {
										case CricketUtil.ONE:
											tournament_dismissal_stats.get(playerId).setOne_wickets(tournament_dismissal_stats.get(playerId).getOne_wickets() + 1);
											break;
										case CricketUtil.TWO:
											tournament_dismissal_stats.get(playerId).setTwo_wickets(tournament_dismissal_stats.get(playerId).getTwo_wickets() + 1);
											break;
										case CricketUtil.THREE:
											tournament_dismissal_stats.get(playerId).setThree_wickets(tournament_dismissal_stats.get(playerId).getThree_wickets() + 1);
											break;
										case CricketUtil.FOUR:
											tournament_dismissal_stats.get(playerId).setFour_wickets(tournament_dismissal_stats.get(playerId).getFour_wickets() + 1);
											break;
										case CricketUtil.FIVE:
											tournament_dismissal_stats.get(playerId).setFive_wickets(tournament_dismissal_stats.get(playerId).getFive_wickets() + 1);
											break;
										case CricketUtil.SIX:
											tournament_dismissal_stats.get(playerId).setSix_wickets(tournament_dismissal_stats.get(playerId).getSix_wickets() + 1);
											break;
										}
									}else {
										tournament_dismissal_stats.get(playerId).setZero_wickets(tournament_dismissal_stats.get(playerId).getZero_wickets() + 1);
									}
									
									if(showBowlerDissmisal == true) {
										getBowlerDissmisal(boc.getPlayerId(), playerId, null, tournament_dismissal_stats, match);
									}
									
								}else {
									tournament_dismissal_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, boc.getPlayer()));
									
									if(boc.getWickets() > 0) {
										switch(String.valueOf(boc.getWickets())) {
										case CricketUtil.ONE:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setOne_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getOne_wickets() + 1);
											break;
										case CricketUtil.TWO:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setTwo_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getTwo_wickets() + 1);
											break;
										case CricketUtil.THREE:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setThree_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getThree_wickets() + 1);
											break;
										case CricketUtil.FOUR:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setFour_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getFour_wickets() + 1);
											break;
										case CricketUtil.FIVE:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setFive_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getFive_wickets() + 1);
											break;
										case CricketUtil.SIX:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setSix_wickets(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getSix_wickets() + 1);
											break;
										}
									}else {
										tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).setZero_wickets(
												tournament_dismissal_stats.get(tournament_dismissal_stats.size()-1).getZero_wickets() + 1);
									}
									
									if(showBowlerDissmisal == true) {
										getBowlerDissmisal(boc.getPlayerId(), (tournament_dismissal_stats.size()-1), null, tournament_dismissal_stats, match);
									}
								}
							}
						}
						
						if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
							for(BattingCard bc : inn.getBattingCard())
							{
								playerId = -1;
								for(int i=0; i<=tournament_dismissal_stats.size() - 1;i++)
								{
									if(bc.getPlayerId() == tournament_dismissal_stats.get(i).getPlayerId()) {
										playerId = i;
										break;
									}
								}
								if(playerId >= 0) {
									if(bc.getHowOut() != null) {
										tournament_dismissal_stats.get(playerId).setTotal_dismissal(tournament_dismissal_stats.get(playerId).getTotal_dismissal() + 1);
										
										switch (bc.getHowOut().toUpperCase()) {
										case CricketUtil.CAUGHT:
											tournament_dismissal_stats.get(playerId).setCaught(tournament_dismissal_stats.get(playerId).getCaught() + 1);
											break;
										case CricketUtil.CAUGHT_AND_BOWLED:
											tournament_dismissal_stats.get(playerId).setCtAndBowled(tournament_dismissal_stats.get(playerId).getCtAndBowled() + 1);
											break;
										case CricketUtil.BOWLED:
											tournament_dismissal_stats.get(playerId).setBowled(tournament_dismissal_stats.get(playerId).getBowled() + 1);
											break;
										case CricketUtil.LBW:
											tournament_dismissal_stats.get(playerId).setLbw(tournament_dismissal_stats.get(playerId).getLbw() + 1);
											break;
										case CricketUtil.RUN_OUT:
											tournament_dismissal_stats.get(playerId).setRun_out(tournament_dismissal_stats.get(playerId).getRun_out() + 1);
											break;
										case CricketUtil.STUMPED:
											tournament_dismissal_stats.get(playerId).setStumped(tournament_dismissal_stats.get(playerId).getStumped() + 1);
											break;
										case CricketUtil.HIT_WICKET:
											tournament_dismissal_stats.get(playerId).setHit_wicket(tournament_dismissal_stats.get(playerId).getHit_wicket() + 1);
											break;
										default:
											tournament_dismissal_stats.get(playerId).setOther(tournament_dismissal_stats.get(playerId).getOther() + 1);
											break;
										}
									}
									
									if(bc.getRuns() > 0) {
										if(bc.getRuns() > 199) {
											tournament_dismissal_stats.get(playerId).setPlus_199(tournament_dismissal_stats.get(playerId).getPlus_199() + 1);
										}else if(bc.getRuns() >= 100 && bc.getRuns() <= 199) {
											tournament_dismissal_stats.get(playerId).setHundred_to_199(tournament_dismissal_stats.get(playerId).getHundred_to_199() + 1);
										}else if(bc.getRuns() >= 90 && bc.getRuns() <= 99) {
											tournament_dismissal_stats.get(playerId).setNinty_to_99(tournament_dismissal_stats.get(playerId).getNinty_to_99() + 1);
										}else if(bc.getRuns() >= 70 && bc.getRuns() <= 89) {
											tournament_dismissal_stats.get(playerId).setSeventy_to_89(tournament_dismissal_stats.get(playerId).getSeventy_to_89() + 1);
										}else if(bc.getRuns() >= 50 && bc.getRuns() <= 69) {
											tournament_dismissal_stats.get(playerId).setFifty_to_69(tournament_dismissal_stats.get(playerId).getFifty_to_69() + 1);
										}else if(bc.getRuns() >= 40 && bc.getRuns() <= 49) {
											tournament_dismissal_stats.get(playerId).setForty_to_49(tournament_dismissal_stats.get(playerId).getForty_to_49() + 1);
										}else if(bc.getRuns() >= 10 && bc.getRuns() <= 39) {
											tournament_dismissal_stats.get(playerId).setTen_to_39(tournament_dismissal_stats.get(playerId).getTen_to_39() + 1);
										}else if(bc.getRuns() >= 0 && bc.getRuns() <= 9) {
											tournament_dismissal_stats.get(playerId).setUnder_10(tournament_dismissal_stats.get(playerId).getUnder_10() + 1);
										}
									}else {
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_dismissal_stats.get(playerId).setDucks(tournament_dismissal_stats.get(playerId).getDucks() + 1);
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_dismissal_stats.get(playerId).setUnder_10(tournament_dismissal_stats.get(playerId).getUnder_10() + 1);
										}
									}
									
								}else {
									tournament_dismissal_stats.add(new Tournament(bc.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, bc.getPlayer()));
									
									if(bc.getHowOut() != null) {
										tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setTotal_dismissal(
												tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getTotal_dismissal() + 1);
										
										switch (bc.getHowOut().toUpperCase()) {
										case CricketUtil.CAUGHT:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setCaught(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getCaught() + 1);
											break;
										case CricketUtil.CAUGHT_AND_BOWLED:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setCtAndBowled(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getCtAndBowled() + 1);
											break;
										case CricketUtil.BOWLED:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setBowled(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getBowled() + 1);
											break;
										case CricketUtil.LBW:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setLbw(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getLbw() + 1);
											break;
										case CricketUtil.RUN_OUT:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setRun_out(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getRun_out() + 1);
											break;
										case CricketUtil.STUMPED:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setStumped(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getStumped() + 1);
											break;
										case CricketUtil.HIT_WICKET:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setHit_wicket(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getHit_wicket() + 1);
											break;
										default:
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setOther(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getOther() + 1);
											break;
										}
									}
									
									if(bc.getRuns() > 0) {
										if(bc.getRuns() > 199) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setPlus_199(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getPlus_199() + 1);
										}else if(bc.getRuns() >= 100 && bc.getRuns() <= 199) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setHundred_to_199(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getHundred_to_199() + 1);
										}else if(bc.getRuns() >= 90 && bc.getRuns() <= 99) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setNinty_to_99(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getNinty_to_99() + 1);
										}else if(bc.getRuns() >= 70 && bc.getRuns() <= 89) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setSeventy_to_89(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getSeventy_to_89() + 1);
										}else if(bc.getRuns() >= 50 && bc.getRuns() <= 69) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setFifty_to_69(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getFifty_to_69() + 1);
										}else if(bc.getRuns() >= 40 && bc.getRuns() <= 49) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setForty_to_49(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getForty_to_49() + 1);
										}else if(bc.getRuns() >= 10 && bc.getRuns() <= 39) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setTen_to_39(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getTen_to_39() + 1);
										}else if(bc.getRuns() >= 0 && bc.getRuns() <= 9) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setUnder_10(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getUnder_10() + 1);
										}
									}else {
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setDucks(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getDucks() + 1);
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).setUnder_10(
													tournament_dismissal_stats.get(tournament_dismissal_stats.size() - 1).getUnder_10() + 1);
										}
									}
								}	
							}
						}
					}
				}
			}
			return tournament_dismissal_stats;
		case "CURRENT_MATCH_DATA":
			
			List<Tournament> past_tournament_dismissal_stat_clone = past_tournament_dismissal_stat.stream().map(tourn_stats -> {
				try {
					return tourn_stats.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				return tourn_stats;
			}).collect(Collectors.toList());
			
			for(Inning inn : currentMatch.getMatch().getInning()) {
				for(BowlingCard boc : inn.getBowlingCard())
				{
					playerId = -1;
					for(int i=0; i<=past_tournament_dismissal_stat_clone.size() - 1;i++)
					{
						if(boc.getPlayerId() == past_tournament_dismissal_stat_clone.get(i).getPlayerId()) {
							playerId = i;
							break;
						}
					}
					
					if(playerId >= 0) {
						if(boc.getWickets() > 0) {
							switch(String.valueOf(boc.getWickets())) {
							case CricketUtil.ONE:
								past_tournament_dismissal_stat_clone.get(playerId).setOne_wickets(past_tournament_dismissal_stat_clone.get(playerId).getOne_wickets() + 1);
								break;
							case CricketUtil.TWO:
								past_tournament_dismissal_stat_clone.get(playerId).setTwo_wickets(past_tournament_dismissal_stat_clone.get(playerId).getTwo_wickets() + 1);
								break;
							case CricketUtil.THREE:
								past_tournament_dismissal_stat_clone.get(playerId).setThree_wickets(past_tournament_dismissal_stat_clone.get(playerId).getThree_wickets() + 1);
								break;
							case CricketUtil.FOUR:
								past_tournament_dismissal_stat_clone.get(playerId).setFour_wickets(past_tournament_dismissal_stat_clone.get(playerId).getFour_wickets() + 1);
								break;
							case CricketUtil.FIVE:
								past_tournament_dismissal_stat_clone.get(playerId).setFive_wickets(past_tournament_dismissal_stat_clone.get(playerId).getFive_wickets() + 1);
								break;
							case CricketUtil.SIX:
								past_tournament_dismissal_stat_clone.get(playerId).setSix_wickets(past_tournament_dismissal_stat_clone.get(playerId).getSix_wickets() + 1);
								break;
							}
						}else {
							past_tournament_dismissal_stat_clone.get(playerId).setZero_wickets(past_tournament_dismissal_stat_clone.get(playerId).getZero_wickets() + 1);
						}
						
						if(showBowlerDissmisal == true) {
							getBowlerDissmisal(boc.getPlayerId(), playerId, null, past_tournament_dismissal_stat_clone, currentMatch);
						}
						
					}else {
						past_tournament_dismissal_stat_clone.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, boc.getPlayer()));
						
						if(boc.getWickets() > 0) {
							switch(String.valueOf(boc.getWickets())) {
							case CricketUtil.ONE:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setOne_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getOne_wickets() + 1);
								break;
							case CricketUtil.TWO:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setTwo_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getTwo_wickets() + 1);
								break;
							case CricketUtil.THREE:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setThree_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getThree_wickets() + 1);
								break;
							case CricketUtil.FOUR:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setFour_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getFour_wickets() + 1);
								break;
							case CricketUtil.FIVE:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setFive_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getFive_wickets() + 1);
								break;
							case CricketUtil.SIX:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setSix_wickets(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getSix_wickets() + 1);
								break;
							}
						}else {
							past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).setZero_wickets(
									past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size()-1).getZero_wickets() + 1);
						}
						
						if(showBowlerDissmisal == true) {
							getBowlerDissmisal(boc.getPlayerId(), (past_tournament_dismissal_stat_clone.size()-1), null, 
									past_tournament_dismissal_stat_clone, currentMatch);
						}
					}
				}
				
				for(BattingCard bc : inn.getBattingCard())
				{
					playerId = -1;
					for(int i=0; i<=past_tournament_dismissal_stat_clone.size() - 1;i++)
					{
						if(bc.getPlayerId() == past_tournament_dismissal_stat_clone.get(i).getPlayerId()) {
							playerId = i;
							break;
						}
					}
					
					if(playerId >= 0) {
						if(bc.getHowOut() != null) {
							past_tournament_dismissal_stat_clone.get(playerId).setTotal_dismissal(past_tournament_dismissal_stat_clone.get(playerId).getTotal_dismissal() + 1);
							
							switch (bc.getHowOut().toUpperCase()) {
							case CricketUtil.CAUGHT:
								past_tournament_dismissal_stat_clone.get(playerId).setCaught(past_tournament_dismissal_stat_clone.get(playerId).getCaught() + 1);
								break;
							case CricketUtil.CAUGHT_AND_BOWLED:
								past_tournament_dismissal_stat_clone.get(playerId).setCtAndBowled(past_tournament_dismissal_stat_clone.get(playerId).getCtAndBowled() + 1);
								break;
							case CricketUtil.BOWLED:
								past_tournament_dismissal_stat_clone.get(playerId).setBowled(past_tournament_dismissal_stat_clone.get(playerId).getBowled() + 1);
								break;
							case CricketUtil.LBW:
								past_tournament_dismissal_stat_clone.get(playerId).setLbw(past_tournament_dismissal_stat_clone.get(playerId).getLbw() + 1);
								break;
							case CricketUtil.RUN_OUT:
								past_tournament_dismissal_stat_clone.get(playerId).setRun_out(past_tournament_dismissal_stat_clone.get(playerId).getRun_out() + 1);
								break;
							case CricketUtil.STUMPED:
								past_tournament_dismissal_stat_clone.get(playerId).setStumped(past_tournament_dismissal_stat_clone.get(playerId).getStumped() + 1);
								break;
							case CricketUtil.HIT_WICKET:
								past_tournament_dismissal_stat_clone.get(playerId).setHit_wicket(past_tournament_dismissal_stat_clone.get(playerId).getHit_wicket() + 1);
								break;
							default:
								past_tournament_dismissal_stat_clone.get(playerId).setOther(past_tournament_dismissal_stat_clone.get(playerId).getOther() + 1);
								break;
							}
						}
						
						if(bc.getRuns() > 0) {
							if(bc.getRuns() > 199) {
								past_tournament_dismissal_stat_clone.get(playerId).setPlus_199(past_tournament_dismissal_stat_clone.get(playerId).getPlus_199() + 1);
							}else if(bc.getRuns() >= 100 && bc.getRuns() <= 199) {
								past_tournament_dismissal_stat_clone.get(playerId).setHundred_to_199(past_tournament_dismissal_stat_clone.get(playerId).getHundred_to_199() + 1);
							}else if(bc.getRuns() >= 90 && bc.getRuns() <= 99) {
								past_tournament_dismissal_stat_clone.get(playerId).setNinty_to_99(past_tournament_dismissal_stat_clone.get(playerId).getNinty_to_99() + 1);
							}else if(bc.getRuns() >= 70 && bc.getRuns() <= 89) {
								past_tournament_dismissal_stat_clone.get(playerId).setSeventy_to_89(past_tournament_dismissal_stat_clone.get(playerId).getSeventy_to_89() + 1);
							}else if(bc.getRuns() >= 50 && bc.getRuns() <= 69) {
								past_tournament_dismissal_stat_clone.get(playerId).setFifty_to_69(past_tournament_dismissal_stat_clone.get(playerId).getFifty_to_69() + 1);
							}else if(bc.getRuns() >= 40 && bc.getRuns() <= 49) {
								past_tournament_dismissal_stat_clone.get(playerId).setForty_to_49(past_tournament_dismissal_stat_clone.get(playerId).getForty_to_49() + 1);
							}else if(bc.getRuns() >= 10 && bc.getRuns() <= 39) {
								past_tournament_dismissal_stat_clone.get(playerId).setTen_to_39(past_tournament_dismissal_stat_clone.get(playerId).getTen_to_39() + 1);
							}else if(bc.getRuns() >= 0 && bc.getRuns() <= 9) {
								past_tournament_dismissal_stat_clone.get(playerId).setUnder_10(past_tournament_dismissal_stat_clone.get(playerId).getUnder_10() + 1);
							}
						}else {
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_dismissal_stat_clone.get(playerId).setDucks(past_tournament_dismissal_stat_clone.get(playerId).getDucks() + 1);
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_dismissal_stat_clone.get(playerId).setUnder_10(past_tournament_dismissal_stat_clone.get(playerId).getUnder_10() + 1);
							}
						}
						
					}else {
						past_tournament_dismissal_stat_clone.add(new Tournament(bc.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, bc.getPlayer()));
						
						if(bc.getHowOut() != null) {
							past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setTotal_dismissal(
									past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getTotal_dismissal() + 1);
							
							switch (bc.getHowOut().toUpperCase()) {
							case CricketUtil.CAUGHT:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setCaught(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getCaught() + 1);
								break;
							case CricketUtil.CAUGHT_AND_BOWLED:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setCtAndBowled(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getCtAndBowled() + 1);
								break;
							case CricketUtil.BOWLED:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setBowled(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getBowled() + 1);
								break;
							case CricketUtil.LBW:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setLbw(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getLbw() + 1);
								break;
							case CricketUtil.RUN_OUT:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setRun_out(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getRun_out() + 1);
								break;
							case CricketUtil.STUMPED:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setStumped(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getStumped() + 1);
								break;
							case CricketUtil.HIT_WICKET:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setHit_wicket(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getHit_wicket() + 1);
								break;
							default:
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setOther(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getOther() + 1);
								break;
							}
						}
						
						if(bc.getRuns() > 0) {
							if(bc.getRuns() > 199) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setPlus_199(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getPlus_199() + 1);
							}else if(bc.getRuns() >= 100 && bc.getRuns() <= 199) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setHundred_to_199(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getHundred_to_199() + 1);
							}else if(bc.getRuns() >= 90 && bc.getRuns() <= 99) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setNinty_to_99(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getNinty_to_99() + 1);
							}else if(bc.getRuns() >= 70 && bc.getRuns() <= 89) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setSeventy_to_89(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getSeventy_to_89() + 1);
							}else if(bc.getRuns() >= 50 && bc.getRuns() <= 69) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setFifty_to_69(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getFifty_to_69() + 1);
							}else if(bc.getRuns() >= 40 && bc.getRuns() <= 49) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setForty_to_49(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getForty_to_49() + 1);
							}else if(bc.getRuns() >= 10 && bc.getRuns() <= 39) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setTen_to_39(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getTen_to_39() + 1);
							}else if(bc.getRuns() >= 0 && bc.getRuns() <= 9) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setUnder_10(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getUnder_10() + 1);
							}
						}else {
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setDucks(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getDucks() + 1);
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).setUnder_10(
										past_tournament_dismissal_stat_clone.get(past_tournament_dismissal_stat_clone.size() - 1).getUnder_10() + 1);
							}
						}
					}	
				}
			}
			return past_tournament_dismissal_stat_clone;
		}
		
		return null;
	}
	
	public static void getBowlerDissmisal(int PlayerId,int Value, CricketService cricketService, List<Tournament> tournament_stats, MatchAllData match) {
		
		for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
			if(PlayerId == match.getEventFile().getEvents().get(i).getEventBallNo()) {
				switch (match.getEventFile().getEvents().get(i).getEventType()) {
				case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
					switch (match.getEventFile().getEvents().get(i).getEventHowOut().toUpperCase()) {
					case CricketUtil.CAUGHT:
						tournament_stats.get(Value).setCaught_bowler(tournament_stats.get(Value).getCaught_bowler() + 1);
						break;
					case CricketUtil.CAUGHT_AND_BOWLED:
						tournament_stats.get(Value).setCtAndBowled_bowler(tournament_stats.get(Value).getCtAndBowled_bowler() + 1);
						break;
					case CricketUtil.BOWLED:
						tournament_stats.get(Value).setBowled_bowler(tournament_stats.get(Value).getBowled_bowler() + 1);
						break;
					case CricketUtil.LBW:
						tournament_stats.get(Value).setLbw_bowler(tournament_stats.get(Value).getLbw_bowler() + 1);
						break;
					case CricketUtil.STUMPED:
						tournament_stats.get(Value).setStumped_bowler(tournament_stats.get(Value).getStumped_bowler() + 1);
						break;
					case CricketUtil.HIT_WICKET:
						tournament_stats.get(Value).setHit_wicket_bowler(tournament_stats.get(Value).getHit_wicket_bowler() + 1);
						break;
					default:
						tournament_stats.get(Value).setOther_bowler(tournament_stats.get(Value).getOther_bowler() + 1);
						break;
					}
					break;
				}
			}
		}
	}
	
	public static int SecondLastBowlerId(MatchAllData matchData,int currentBowlerId) {
		int over_c=0;
		for (int i = matchData.getEventFile().getEvents().size() - 1; i >= 0; i--) {
			if (matchData.getEventFile().getEvents().get(i).getEventInningNumber() 
					== matchData.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
							.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {

				if (matchData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
					over_c++;
					if(over_c == 2) {
						if(matchData.getEventFile().getEvents().get(i).getEventBowlerNo() != currentBowlerId) {
							return matchData.getEventFile().getEvents().get(i).getEventBowlerNo();
						}
					}
				}
			}
		}
		return 0;
	}
	
	public static int PreviousBowler(MatchAllData matchData,int currentBowlerId) {
		int over_c=0;
		for (int i = matchData.getEventFile().getEvents().size() - 1; i >= 0; i--) {
			if (matchData.getEventFile().getEvents().get(i).getEventInningNumber() 
					== matchData.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
							.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {

				if (matchData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
					over_c++;
					if(over_c == 1) {
						if(matchData.getEventFile().getEvents().get(i).getEventBowlerNo() != currentBowlerId) {
							return matchData.getEventFile().getEvents().get(i).getEventBowlerNo();
						}
					}
				}
			}
		}
		return 0;
	}
	public static int PreviousBowlerRuns(MatchAllData matchData,int currentBowlerId) {
		int over_c=0,total_runs=0;
		for (int i = matchData.getEventFile().getEvents().size() - 1; i >= 0; i--) {
			if (matchData.getEventFile().getEvents().get(i).getEventInningNumber() 
					== matchData.getMatch().getInning().stream().filter(in -> in.getIsCurrentInning()
							.equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getInningNumber()) {

				if (matchData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
					over_c++;
					if(over_c == 2) {
						return total_runs;
					}
				}else {
					if(over_c == 1) {
						switch(matchData.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				        case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
				        	total_runs += matchData.getEventFile().getEvents().get(i).getEventRuns();
				          break;
				          
				        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
				        	total_runs += matchData.getEventFile().getEvents().get(i).getEventRuns();
				        	break;
				        case CricketUtil.LOG_WICKET:
				        	total_runs += matchData.getEventFile().getEvents().get(i).getEventRuns();
					          if (matchData.getEventFile().getEvents().get(i).getEventExtra() != null) {
					        	 total_runs += matchData.getEventFile().getEvents().get(i).getEventExtraRuns();
					          }
					          if (matchData.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
					        	 total_runs += matchData.getEventFile().getEvents().get(i).getEventSubExtraRuns();
					          }
					          break;
				        case CricketUtil.LOG_ANY_BALL:
				        	total_runs += matchData.getEventFile().getEvents().get(i).getEventRuns();
					          if (matchData.getEventFile().getEvents().get(i).getEventExtra() != null) {
					        	 total_runs += matchData.getEventFile().getEvents().get(i).getEventExtraRuns();
					          }
					          if (matchData.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
					        	 total_runs += matchData.getEventFile().getEvents().get(i).getEventSubExtraRuns();
					          }
					          break;
						}
					}
				}
			}
		}
		return 0;
	}
	
	public static Event batsmanSubstitution(MatchAllData matchData,int Inning_Number) {
		Event event = matchData.getEventFile().getEvents().stream().filter(ent->ent.getEventType().equalsIgnoreCase(CricketUtil.LOG_OVERWRITE_SUBSTITUTION) 
				&& ent.getEventInningNumber() == Inning_Number).findAny().orElse(null);
		return event;
	}
	
	public static String hundredsTensUnits(String number) {
		String hundReds ="0",tens="0",units="0";
		
		switch (number.length()) {
		case 1:
			units = String.valueOf(number.charAt(0));
			break;
		case 2:
			tens = String.valueOf(number.charAt(0));
			units = String.valueOf(number.charAt(1));
			break;
		case 3:
			hundReds = String.valueOf(number.charAt(0));
			tens = String.valueOf(number.charAt(1));
			units = String.valueOf(number.charAt(2));
			break;
		}
		
		return hundReds + "," + tens + "," + units;
	}
	
	public static String hundredsTensUnitsTeamScore(String number) {
		String hundReds ="",tens="",units="";
		
		switch (number.length()) {
		case 1:
			units = String.valueOf(number.charAt(0));
			break;
		case 2:
			tens = String.valueOf(number.charAt(0));
			units = String.valueOf(number.charAt(1));
			break;
		case 3:
			hundReds = String.valueOf(number.charAt(0));
			tens = String.valueOf(number.charAt(1));
			units = String.valueOf(number.charAt(2));
			break;
		}
		
		return hundReds + "," + tens + "," + units;
	}

	public static Statistics updateTournamentWithH2h(Statistics stats,List<HeadToHeadPlayer> headToHead_matches,MatchAllData currentMatch,String teamNameType) throws JsonMappingException, JsonProcessingException, InterruptedException 
	{
		boolean player_found = false,impact_player_found=false;
		String batTeamName = "",ballTeamName = "";
		Statistics statsdata = stats;
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		for(HeadToHeadPlayer match : headToHead_matches) {
			//System.out.println(match.getMatch().getMatchFileName());
			if(!match.getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
				if(stat.getStats_type().getStats_short_name().contains(currentMatch.getSetup().getMatchType())) {
//					TimeUnit.MILLISECONDS.sleep(500);
					
					switch (teamNameType) {
					case CricketUtil.FULL:
						batTeamName = match.getOpponentTeam().getTeamName1();
						ballTeamName = match.getTeam().getTeamName1();
						break;
					case CricketUtil.SHORT:
						batTeamName = match.getOpponentTeam().getTeamName4();
						ballTeamName = match.getTeam().getTeamName4();
						break;
					default:
						batTeamName = match.getOpponentTeam().getTeamName3();
						ballTeamName = match.getTeam().getTeamName3();
						break;
					}
					
					if(match.getPlayerId() == stat.getPlayer_id()) {
						player_found = true;
						if(match.getInningStarted().equalsIgnoreCase("Y")) {
							stat.setInnings(stat.getInnings() + 1);
						}
						stat.setRuns(stat.getRuns() + match.getRuns());
						stat.setFours(stat.getFours() + match.getFours());
						stat.setSixes(stat.getSixes() + match.getSixes());
						stat.setBalls_faced(stat.getBalls_faced() + match.getBallsFaced());
						
						if(match.getDismissed().equalsIgnoreCase("N")) {
							stat.setNot_out(stat.getNot_out() + 1);
						}
						
						if(match.getRuns() < 50 && match.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(match.getRuns() < 100 && match.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(match.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
						
						if(stat.getBest_score().equalsIgnoreCase("0")) {
							if(match.getDismissed().equalsIgnoreCase("N")) {
								stat.setBest_score(match.getRuns() + "*");
							}else if(match.getDismissed().equalsIgnoreCase("Y")) {
								stat.setBest_score(String.valueOf(match.getRuns()));
							}
							stat.setBest_score_against(batTeamName);
							stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
						}else {
							if(stat.getBest_score().contains("*")) {
								if(Integer.valueOf(stat.getBest_score().replace("*", "")) < match.getRuns()) {
									if(match.getDismissed().equalsIgnoreCase("N")) {
										stat.setBest_score(match.getRuns()+"*");
									}else if(match.getDismissed().equalsIgnoreCase("Y")) {
										stat.setBest_score(String.valueOf(match.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
							}else {
								if(Integer.valueOf(stat.getBest_score()) == match.getRuns() && match.getDismissed().equalsIgnoreCase("N")) {
									stat.setBest_score(match.getRuns() + "*");
									stat.setBest_score_against(match.getOpponentTeam().getTeamName1());
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
								else if(Integer.valueOf(stat.getBest_score()) < match.getRuns()) {
									if(match.getDismissed().equalsIgnoreCase("N")) {
										stat.setBest_score(match.getRuns() + "*");
									}else if(match.getDismissed().equalsIgnoreCase("Y")) {
										stat.setBest_score(String.valueOf(match.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
							}
						}
					}
					
//-------------------------------------------Bowler------------------------------------------------------------------------------//
					
					if(match.getPlayerId() == stat.getPlayer_id()) {
						stat.setWickets(stat.getWickets() + match.getWickets());
						stat.setRuns_conceded(stat.getRuns_conceded() + match.getRunsConceded());
						stat.setBalls_bowled(stat.getBalls_bowled() + match.getBallsBowled());
						stat.setDotbowled(stat.getDotbowled() + match.getBalldots());
						if(match.getWickets() < 5 && match.getWickets() >= 3) {
							stat.setPlus_3(stat.getPlus_3() + 1);
						}	
						else if(match.getWickets() >= 5){
							stat.setPlus_5(stat.getPlus_5() + 1);
						}
						
						if(stat.getBest_figures().equalsIgnoreCase("0")) {
							stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
							stat.setBest_figures_against(ballTeamName);
							stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
						}else {
							if(match.getWickets() > Integer.valueOf(stat.getBest_figures().split("-")[0])) {
								stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
							}
							else if(match.getWickets() == Integer.valueOf(stat.getBest_figures().split("-")[0]) && 
									match.getRunsConceded() < Integer.valueOf(stat.getBest_figures().split("-")[1])) {
								stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
							}
						}
					}
					
					if(player_found == true){
						player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
					if(impact_player_found == true){
						impact_player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
				}
			}
		}
		return stat;
	}
	
	public static Statistics updateH2h(Statistics stats,List<HeadToHeadPlayer> headToHead_matches,MatchAllData currentMatch, String teamNameType) throws JsonMappingException, JsonProcessingException, InterruptedException 
	{
		boolean player_found = false,impact_player_found=false;
		String batTeamName = "",ballTeamName = "";
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		for(HeadToHeadPlayer match : headToHead_matches) {
			//System.out.println(match.getMatch().getMatchFileName());
			if(!match.getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
//				if(stat.getStats_type().getStats_short_name().contains(currentMatch.getSetup().getMatchType())) {
//					TimeUnit.MILLISECONDS.sleep(500);
				
					switch (teamNameType) {
					case CricketUtil.FULL:
						batTeamName = match.getOpponentTeam().getTeamName1();
						ballTeamName = match.getTeam().getTeamName1();
						break;
					case CricketUtil.SHORT:
						batTeamName = match.getOpponentTeam().getTeamName4();
						ballTeamName = match.getTeam().getTeamName4();
						break;
					default:
						batTeamName = match.getOpponentTeam().getTeamName3();
						ballTeamName = match.getTeam().getTeamName3();
						break;
					}
				
					if(match.getPlayerId() == stat.getPlayer_id()) {
						player_found = true;
						if(match.getInningStarted().equalsIgnoreCase("Y")) {
							stat.setInnings(stat.getInnings() + 1);
						}
						stat.setRuns(stat.getRuns() + match.getRuns());
						stat.setFours(stat.getFours() + match.getFours());
						stat.setSixes(stat.getSixes() + match.getSixes());
						stat.setBalls_faced(stat.getBalls_faced() + match.getBallsFaced());
						
						if(match.getDismissed().equalsIgnoreCase("N")) {
							stat.setNot_out(stat.getNot_out() + 1);
						}
						
						if(match.getRuns() < 50 && match.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(match.getRuns() < 100 && match.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(match.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
						
						if(stat.getBest_score().equalsIgnoreCase("0")) {
							if(match.getDismissed().trim().equalsIgnoreCase("N")) {
								stat.setBest_score(match.getRuns() + "*");
							}else if(match.getDismissed().trim().equalsIgnoreCase("Y")) {
								stat.setBest_score(String.valueOf(match.getRuns()));
							}
							stat.setBest_score_against(batTeamName);
							stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
						}else {
							if(stat.getBest_score().contains("*")) {
								if(Integer.valueOf(stat.getBest_score().replace("*", "")).intValue() < match.getRuns()) {
									if(match.getDismissed().trim().equalsIgnoreCase("N")) {
										stat.setBest_score(match.getRuns()+"*");
									}else if(match.getDismissed().trim().equalsIgnoreCase("Y")) {
										stat.setBest_score(String.valueOf(match.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
							}else {
								if(Integer.valueOf(stat.getBest_score()) == match.getRuns() && match.getDismissed().trim().equalsIgnoreCase("N")) {
									stat.setBest_score(match.getRuns() + "*");
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
								else if(Integer.valueOf(stat.getBest_score()) < match.getRuns()) {
									if(match.getDismissed().trim().equalsIgnoreCase("N")) {
										stat.setBest_score(match.getRuns() + "*");
									}else if(match.getDismissed().trim().equalsIgnoreCase("Y")) {
										stat.setBest_score(String.valueOf(match.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getVenue() + ", " + Year.now());
								}
							}
						}
					}
					
//-------------------------------------------Bowler------------------------------------------------------------------------------//
					
					if(match.getPlayerId() == stat.getPlayer_id()) {
						stat.setWickets(stat.getWickets() + match.getWickets());
						stat.setRuns_conceded(stat.getRuns_conceded() + match.getRunsConceded());
						stat.setBalls_bowled(stat.getBalls_bowled() + match.getBallsBowled());
						stat.setDotbowled(stat.getDotbowled() + match.getBalldots());
						if(match.getWickets() < 5 && match.getWickets() >= 3) {
							stat.setPlus_3(stat.getPlus_3() + 1);
						}	
						else if(match.getWickets() >= 5){
							stat.setPlus_5(stat.getPlus_5() + 1);
						}
						
						if(stat.getBest_figures().equalsIgnoreCase("0")) {
							stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
							stat.setBest_figures_against(ballTeamName);
							stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
						}else {
							if(match.getWickets() > Integer.valueOf(stat.getBest_figures().split("-")[0])) {
								stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
							}
							else if(match.getWickets() == Integer.valueOf(stat.getBest_figures().split("-")[0]) && 
									match.getRunsConceded() < Integer.valueOf(stat.getBest_figures().split("-")[1])) {
								stat.setBest_figures(match.getWickets() + "-" + match.getRunsConceded());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getVenue() + ", " + Year.now());
							}
						}
					}
					
					if(player_found == true){
						player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
					if(impact_player_found == true){
						impact_player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
//				}
			}
		}
		
//		System.out.println("MATCHES : "+stat.getMatches()+" : "+stat.getBest_score());
		return stat;
	}
	
	public static Statistics updateTournamentDataWithStats(Statistics stats,List<MatchAllData> tournament_matches,MatchAllData currentMatch, String teamNameType) throws JsonMappingException, JsonProcessingException, InterruptedException 
	{
		boolean player_found = false,impact_player_found=false;
		String batTeamName = "",ballTeamName = "";
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		for(MatchAllData match : tournament_matches) {
			//System.out.println(match.getMatch().getMatchFileName());
			if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
				if(stat.getStats_type().getStats_short_name().contains(match.getSetup().getMatchType())) {
//					TimeUnit.MILLISECONDS.sleep(500);
					for(Inning inn : match.getMatch().getInning()) {
						switch (teamNameType) {
						case CricketUtil.FULL:
							batTeamName = inn.getBowling_team().getTeamName1();
							ballTeamName = inn.getBatting_team().getTeamName1();
							break;
						case CricketUtil.SHORT:
							batTeamName = inn.getBowling_team().getTeamName4();
							ballTeamName = inn.getBatting_team().getTeamName4();
							break;
						default:
							batTeamName = inn.getBowling_team().getTeamName3();
							ballTeamName = inn.getBatting_team().getTeamName3();
							break;
						}
						
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
								
								if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									stat.setNot_out(stat.getNot_out() + 1);
								}
								
								if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
									stat.setThirties(stat.getThirties() + 1);
								}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
									stat.setFifties(stat.getFifties() + 1);
								}else if(bc.getRuns() >= 100){
									stat.setHundreds(stat.getHundreds() + 1);
								}
								
								if(stat.getBest_score().equalsIgnoreCase("0")) {
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										stat.setBest_score(bc.getRuns()+"*");
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										stat.setBest_score(String.valueOf(bc.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}else {
									if(stat.getBest_score().contains("*")) {
										if(Integer.valueOf(stat.getBest_score().replace("*", "")) < bc.getRuns()) {
											if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
												stat.setBest_score(bc.getRuns()+"*");
											}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
												stat.setBest_score(String.valueOf(bc.getRuns()));
											}
											stat.setBest_score_against(batTeamName);
											stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
										}
									}else {
										if(Integer.valueOf(stat.getBest_score()) == bc.getRuns() && bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											stat.setBest_score(bc.getRuns()+"*");
											stat.setBest_score_against(batTeamName);
											stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
										}
										else if(Integer.valueOf(stat.getBest_score()) < bc.getRuns()) {
											if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
												stat.setBest_score(bc.getRuns()+"*");
											}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
												stat.setBest_score(String.valueOf(bc.getRuns()));
											}
											stat.setBest_score_against(batTeamName);
											stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
										}
									}
								}
							}
						}
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId() == stat.getPlayer_id()) {
									stat.setWickets(stat.getWickets() + boc.getWickets());
									stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
									stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*Integer.valueOf(match.getSetup().getBallsPerOver()) + boc.getBalls()));
									stat.setDotbowled(stat.getDotbowled() + boc.getDots());
									if(boc.getWickets() < 5 && boc.getWickets() >= 3) {
										stat.setPlus_3(stat.getPlus_3() + 1);
									}	
									else if(boc.getWickets() >= 5){
										stat.setPlus_5(stat.getPlus_5() + 1);
									}
									
									if(stat.getBest_figures().equalsIgnoreCase("0")) {
										stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
										stat.setBest_figures_against(ballTeamName);
										stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
									}else {
										if(boc.getWickets() > Integer.valueOf(stat.getBest_figures().split("-")[0])) {
											stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
											stat.setBest_figures_against(ballTeamName);
											stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
										}
										else if(boc.getWickets() == Integer.valueOf(stat.getBest_figures().split("-")[0]) && 
												boc.getRuns() < Integer.valueOf(stat.getBest_figures().split("-")[1])) {
											stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
											stat.setBest_figures_against(ballTeamName);
											stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
										}
									}
								}
							}							
						}
					}
					for(Player hs : match.getSetup().getHomeSubstitutes()) {
//						if(hs.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
							if(hs.getPlayerId() == stat.getPlayer_id()) {
								impact_player_found = true;
							}
//						}
					}
					for(Player as : match.getSetup().getAwaySubstitutes()) {
//						if(as.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
							if(as.getPlayerId() == stat.getPlayer_id()) {
								impact_player_found = true;
							}
//						}
					}
					if(player_found == true){
						player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
					if(impact_player_found == true){
						impact_player_found = false;
						stat.setMatches(stat.getMatches() + 1);
					}
				}
			}
		}
		return stat;
	}
	
	public static Statistics updateMatchData(Statistics stats, MatchAllData match, String teamNameType) throws JsonMappingException, JsonProcessingException
	{
		boolean player_found = false,impact_player_found=false;
		String batTeamName = "",ballTeamName = "";
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
//		if(stat.getStats_type().getStats_short_name().contains(match.getSetup().getMatchType())) {
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(0).getTotalFours());
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(1).getTotalFours());
			for(Inning inn : match.getMatch().getInning()) {
				switch (teamNameType) {
				case CricketUtil.FULL:
					batTeamName = inn.getBowling_team().getTeamName1();
					ballTeamName = inn.getBatting_team().getTeamName1();
					break;
				case CricketUtil.SHORT:
					batTeamName = inn.getBowling_team().getTeamName4();
					ballTeamName = inn.getBatting_team().getTeamName4();
					break;
				default:
					batTeamName = inn.getBowling_team().getTeamName3();
					ballTeamName = inn.getBatting_team().getTeamName3();
					break;
				}
				
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
						
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							stat.setNot_out(stat.getNot_out() + 1);
						}
				
						if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(bc.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
						if(stat.getBest_score().equalsIgnoreCase("0")) {
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								stat.setBest_score(bc.getRuns()+"*");
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								stat.setBest_score(String.valueOf(bc.getRuns()));
							}
							stat.setBest_score_against(batTeamName);
							stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
						}else {
							if(stat.getBest_score().contains("*")) {
								if(Integer.valueOf(stat.getBest_score().replace("*", "")) < bc.getRuns()) {
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										stat.setBest_score(bc.getRuns()+"*");
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										stat.setBest_score(String.valueOf(bc.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}else {
								if(Integer.valueOf(stat.getBest_score()) == bc.getRuns() && bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									stat.setBest_score(bc.getRuns()+"*");
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
								else if(Integer.valueOf(stat.getBest_score()) < bc.getRuns()) {
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										stat.setBest_score(bc.getRuns()+"*");
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										stat.setBest_score(String.valueOf(bc.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}
						}
					}
				}
				if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getPlayerId() == stat.getPlayer_id()) {
							player_found = true;
							stat.setWickets(stat.getWickets() + boc.getWickets());
							stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
							stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()* Integer.valueOf(match.getSetup().getBallsPerOver()) + boc.getBalls()));
							stat.setDotbowled(stat.getDotbowled() + boc.getDots());
							//System.out.println(boc.getWickets());
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								stat.setPlus_3(stat.getPlus_3() + 1);
							}else if(boc.getWickets() >= 5){
								stat.setPlus_5(stat.getPlus_5() + 1);
							}
							
							if(stat.getBest_figures().equalsIgnoreCase("0")) {
								stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
							}else {
								if(boc.getWickets() > Integer.valueOf(stat.getBest_figures().split("-")[0])) {
									stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
									stat.setBest_figures_against(ballTeamName);
									stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
								else if(boc.getWickets() == Integer.valueOf(stat.getBest_figures().split("-")[0]) && 
										boc.getRuns() < Integer.valueOf(stat.getBest_figures().split("-")[1])) {
									stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
									stat.setBest_figures_against(ballTeamName);
									stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}
						}
					}							
				}
			}
			for(Player hs : match.getSetup().getHomeSubstitutes()) {
//				if(hs.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
					if(hs.getPlayerId() == stat.getPlayer_id()) {
						impact_player_found = true;
					}
//				}
			}
			for(Player as : match.getSetup().getAwaySubstitutes()) {
//				if(as.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
					if(as.getPlayerId() == stat.getPlayer_id()) {
						impact_player_found = true;
					}
//				}
			}
			if(player_found == true && impact_player_found == false){
				player_found = false;
				stat.setMatches(stat.getMatches() + 1);
			}
			if(impact_player_found == true){
				impact_player_found = false;
				stat.setMatches(stat.getMatches() + 1);
			}
//		}
		return stat;
	}
	
	public static Statistics updateStatisticsWithMatchData(Statistics stats, MatchAllData match, String teamNameType) throws JsonMappingException, JsonProcessingException
	{
		boolean player_found = false,impact_player_found=false;
		String batTeamName = "",ballTeamName = "";
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		if(stat.getStats_type().getStats_short_name().contains(match.getSetup().getMatchType())) {
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(0).getTotalFours());
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(1).getTotalFours());
			for(Inning inn : match.getMatch().getInning()) {
				switch (teamNameType) {
				case CricketUtil.FULL:
					batTeamName = inn.getBowling_team().getTeamName1();
					ballTeamName = inn.getBatting_team().getTeamName1();
					break;
				case CricketUtil.SHORT:
					batTeamName = inn.getBowling_team().getTeamName4();
					ballTeamName = inn.getBatting_team().getTeamName4();
					break;
				default:
					batTeamName = inn.getBowling_team().getTeamName3();
					ballTeamName = inn.getBatting_team().getTeamName3();
					break;
				}
				
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
						
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							stat.setNot_out(stat.getNot_out() + 1);
						}
				
						if(bc.getRuns() < 50 && bc.getRuns() >= 30) {
							stat.setThirties(stat.getThirties() + 1);
						}else if(bc.getRuns() < 100 && bc.getRuns() >= 50) {
							stat.setFifties(stat.getFifties() + 1);
						}else if(bc.getRuns() >= 100){
							stat.setHundreds(stat.getHundreds() + 1);
						}
						
						if(stat.getBest_score().equalsIgnoreCase("0")) {
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								stat.setBest_score(bc.getRuns()+"*");
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								stat.setBest_score(String.valueOf(bc.getRuns()));
							}
							stat.setBest_score_against(batTeamName);
							stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
						}else {
							if(stat.getBest_score().contains("*")) {
								if(Integer.valueOf(stat.getBest_score().replace("*", "")) < bc.getRuns()) {
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										stat.setBest_score(bc.getRuns()+"*");
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										stat.setBest_score(String.valueOf(bc.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}else {
								if(Integer.valueOf(stat.getBest_score()) == bc.getRuns() && bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
									stat.setBest_score(bc.getRuns()+"*");
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
								else if(Integer.valueOf(stat.getBest_score()) < bc.getRuns()) {
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										stat.setBest_score(bc.getRuns()+"*");
									}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
										stat.setBest_score(String.valueOf(bc.getRuns()));
									}
									stat.setBest_score_against(batTeamName);
									stat.setBest_score_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}
						}
					}
				}
				if(inn.getBowlingCard() != null && inn.getBowlingCard().size()>0) {
					for(BowlingCard boc : inn.getBowlingCard()) {
						if(boc.getPlayerId() == stat.getPlayer_id()) {
							player_found = true;
							stat.setWickets(stat.getWickets() + boc.getWickets());
							stat.setRuns_conceded(stat.getRuns_conceded() + boc.getRuns());
							stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()* Integer.valueOf(match.getSetup().getBallsPerOver()) + boc.getBalls()));
							stat.setDotbowled(stat.getDotbowled() + boc.getDots());
							//System.out.println(boc.getWickets());
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								stat.setPlus_3(stat.getPlus_3() + 1);
							}else if(boc.getWickets() >= 5){
								stat.setPlus_5(stat.getPlus_5() + 1);
							}
							
							if(stat.getBest_figures().equalsIgnoreCase("0")) {
								stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
								stat.setBest_figures_against(ballTeamName);
								stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
							}else {
								if(boc.getWickets() > Integer.valueOf(stat.getBest_figures().split("-")[0])) {
									stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
									stat.setBest_figures_against(ballTeamName);
									stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
								else if(boc.getWickets() == Integer.valueOf(stat.getBest_figures().split("-")[0]) && 
										boc.getRuns() < Integer.valueOf(stat.getBest_figures().split("-")[1])) {
									stat.setBest_figures(boc.getWickets() + "-" + boc.getRuns());
									stat.setBest_figures_against(ballTeamName);
									stat.setBest_figures_venue(match.getSetup().getGround().getCountry() + ", " + Year.now());
								}
							}
						}
					}							
				}
			}
			if(match.getSetup().getHomeSubstitutes() != null) {
				for(Player hs : match.getSetup().getHomeSubstitutes()) {
//					if(hs.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
						if(hs.getPlayerId() == stat.getPlayer_id()) {
							impact_player_found = true;
						}
//					}
				}
			}
			if(match.getSetup().getAwaySubstitutes() != null) {
				for(Player as : match.getSetup().getAwaySubstitutes()) {
//					if(as.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
						if(as.getPlayerId() == stat.getPlayer_id()) {
							impact_player_found = true;
						}
//					}
				}
			}
			
			if(player_found == true){
				player_found = false;
				stat.setMatches(stat.getMatches() + 1);
			}
			if(impact_player_found == true){
				impact_player_found = false;
				stat.setMatches(stat.getMatches() + 1);
			}
		}
		return stat;
	}
	
	public static List<BestStats> extractLogFifty(String typeOfExtraction,String typeOfData ,CricketService cricketService,List<MatchAllData> tournament_matches,
			MatchAllData currentMatch, List<BestStats> past_logFifty) throws IOException 
	{
		List<BestStats> log_50_50 = new ArrayList<BestStats>();
		
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			return extractLogFifty("CURRENT_MATCH_DATA",typeOfData, cricketService,tournament_matches, currentMatch, 
					extractLogFifty("PAST_MATCHES_DATA",typeOfData, cricketService,tournament_matches, currentMatch, null));
		case "PAST_MATCHES_DATA":
			for(MatchAllData mtch : tournament_matches) {
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					int over_number=0,bowlerId = 0,batterId=0,runs = 0,wicket = 0,inning_num = 0,teamid = -1;
					if ((mtch.getEventFile().getEvents() != null) && (mtch.getEventFile().getEvents().size() > 0)) {
						for(int i=mtch.getEventFile().getEvents().size()-1;i>=0;i--) {
							if(mtch.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
								bowlerId = mtch.getEventFile().getEvents().get(i).getEventBowlerNo();
								batterId = mtch.getEventFile().getEvents().get(i).getEventBatterNo();
								over_number = mtch.getEventFile().getEvents().get(i).getEventOverNo();
								runs = mtch.getEventFile().getEvents().get(i).getEventTotalRunsInAnOver();
								inning_num = mtch.getEventFile().getEvents().get(i).getEventInningNumber();
								
								if(typeOfData.equalsIgnoreCase(CricketUtil.BOWLER)) {
									for(int j=0;j<= mtch.getEventFile().getEvents().size()-1;j++) {
										if(mtch.getEventFile().getEvents().get(j).getEventBowlerNo() == bowlerId && ((mtch.getEventFile().getEvents().get(j).getEventOverNo()*6)+
											mtch.getEventFile().getEvents().get(j).getEventBallNo()) > ((over_number-1)*6) && ((mtch.getEventFile().getEvents().get(j).getEventOverNo()*6)+
													mtch.getEventFile().getEvents().get(j).getEventBallNo()) <= (over_number*6)) {
											
											switch(mtch.getEventFile().getEvents().get(j).getEventType()) {
											case CricketUtil.LOG_WICKET:
							                    wicket += 1;
							                    break;
											case CricketUtil.LOG_ANY_BALL:
												if (mtch.getEventFile().getEvents().get(j).getEventHowOut() != null && 
													!mtch.getEventFile().getEvents().get(j).getEventHowOut().isEmpty())
					                            {
													wicket += 1;
					                            }
							                    break;
											}
										}
									}
									log_50_50.add(new BestStats(bowlerId, null, null, null, runs, wicket));
									
									runs = 0;
									wicket = 0;
									
									for (BowlingCard boc : mtch.getMatch().getInning().get(inning_num - 1).getBowlingCard()) {
										if(boc.getPlayerId() == bowlerId) {
											log_50_50.get(log_50_50.size() - 1).setPlayer(boc.getPlayer());
										}
									}
									
									if(CricketFunctions.getHomeAwayPlayer(bowlerId, mtch).getTeamId() == mtch.getSetup().getHomeTeamId()) {
										log_50_50.get(log_50_50.size() - 1).setOpponentTeam(mtch.getSetup().getAwayTeam());
									}else {
										log_50_50.get(log_50_50.size() - 1).setOpponentTeam(mtch.getSetup().getHomeTeam());
									}
								}else {
									for(BattingCard bc : mtch.getMatch().getInning().get(inning_num - 1).getBattingCard()) {
										if(bc.getPlayerId() == batterId) {
											teamid = -1;
											for(int k=0;k<=log_50_50.size()-1;k++) {
												if(log_50_50.get(k).getTeamId() == bc.getPlayer().getTeamId()) {
													teamid = k;
													break;
												}
											}
											if(teamid > 0) {
												log_50_50.get(teamid).setMatches(log_50_50.get(teamid).getMatches()+1);
												log_50_50.get(teamid).setChallengeRuns(log_50_50.get(teamid).getChallengeRuns()+runs);
												break;
											}else {
												log_50_50.add(new BestStats(bc.getPlayer().getTeamId(), 1, runs));
											}
										}
									}
									runs = 0;
								}
							}
						}
					}
				}
			}
			return log_50_50;
		case "CURRENT_MATCH_DATA":
			List<BestStats> past_log_fifty_clone = past_logFifty.stream().map(logfifty ->{
				try {
					return logfifty.clone();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return logfifty;
			}).collect(Collectors.toList());
			if ((currentMatch.getEventFile().getEvents() != null) && (currentMatch.getEventFile().getEvents().size() > 0)) {
				int over_number=0,bowlerId = 0, batterId=0,runs = 0,wicket = 0,inning_num = 0, teamid = -1;
				for(int i=currentMatch.getEventFile().getEvents().size()-1;i>=0;i--) {
					if(currentMatch.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_50_50)) {
						bowlerId = currentMatch.getEventFile().getEvents().get(i).getEventBowlerNo();
						over_number = currentMatch.getEventFile().getEvents().get(i).getEventOverNo();
						runs = currentMatch.getEventFile().getEvents().get(i).getEventTotalRunsInAnOver();
						inning_num = currentMatch.getEventFile().getEvents().get(i).getEventInningNumber();
						
						if(typeOfData.equalsIgnoreCase(CricketUtil.BOWLER)) {
							for(int j=0;j<= currentMatch.getEventFile().getEvents().size()-1;j++) {
								if(currentMatch.getEventFile().getEvents().get(j).getEventBowlerNo() == bowlerId && ((currentMatch.getEventFile().getEvents().get(j).getEventOverNo()*6)+
									currentMatch.getEventFile().getEvents().get(j).getEventBallNo()) > ((over_number-1)*6) && ((currentMatch.getEventFile().getEvents().get(j).getEventOverNo()*6)+
									 currentMatch.getEventFile().getEvents().get(j).getEventBallNo()) <= (over_number*6)) {
									
									switch(currentMatch.getEventFile().getEvents().get(j).getEventType()) {
									case CricketUtil.LOG_WICKET:
					                    wicket += 1;
					                    break;
									case CricketUtil.LOG_ANY_BALL:
										if (currentMatch.getEventFile().getEvents().get(j).getEventHowOut() != null && 
											!currentMatch.getEventFile().getEvents().get(j).getEventHowOut().isEmpty())
			                            {
											wicket += 1;
			                            }
					                    break;
									}
								}
							}
							
							past_log_fifty_clone.add(new BestStats(bowlerId, null, null, null, runs, wicket));
							
							runs = 0;
							wicket = 0;
							
							for (BowlingCard boc : currentMatch.getMatch().getInning().get(inning_num - 1).getBowlingCard()) {
								if(boc.getPlayerId() == bowlerId) {
									past_log_fifty_clone.get(past_log_fifty_clone.size() - 1).setPlayer(boc.getPlayer());
								}
							}
							
							if(CricketFunctions.getHomeAwayPlayer(bowlerId, currentMatch).getTeamId() == currentMatch.getSetup().getHomeTeamId()) {
								past_log_fifty_clone.get(past_log_fifty_clone.size() - 1).setOpponentTeam(currentMatch.getSetup().getAwayTeam());
							}else {
								past_log_fifty_clone.get(past_log_fifty_clone.size() - 1).setOpponentTeam(currentMatch.getSetup().getHomeTeam());
							}
						}else {
							for(BattingCard bc : currentMatch.getMatch().getInning().get(inning_num - 1).getBattingCard()) {
								if(bc.getPlayerId() == batterId) {
									teamid = -1;
									for(int k=0;k<=log_50_50.size()-1;k++) {
										if(log_50_50.get(k).getTeamId() == bc.getPlayer().getTeamId()) {
											teamid = k;
											break;
										}
									}
									if(teamid > 0) {
										past_log_fifty_clone.get(teamid).setMatches(past_log_fifty_clone.get(teamid).getMatches()+1);
										past_log_fifty_clone.get(teamid).setChallengeRuns(past_log_fifty_clone.get(teamid).getChallengeRuns()+runs);
										break;
									}else {
										past_log_fifty_clone.add(new BestStats(bc.getPlayer().getTeamId(), 1, runs));
									}
								}
							}
							runs = 0;
						}
					}
				}
			}
			
			return past_log_fifty_clone;	
		}
		return null;
	}
	public static List<BestStats> extractTapeData(String typeOfData,CricketService cricketService,List<MatchAllData> tournament_matches,
			MatchAllData currentMatch, List<BestStats> past_tapeBall) throws IOException 
	{
		List<BestStats> tapeBall = new ArrayList<BestStats>();
		
		switch(typeOfData) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			return extractTapeData("CURRENT_MATCH_DATA", cricketService,tournament_matches, currentMatch, 
					extractTapeData("PAST_MATCHES_DATA", cricketService,tournament_matches, currentMatch, null));
		case "PAST_MATCHES_DATA":
			for(MatchAllData mtch : tournament_matches) {
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					int bowlerId = 0,runs = 0,wicket = 0,inning_num = 0;
					boolean bowler_found = false, data_set = false;
					if ((mtch.getEventFile().getEvents() != null) && (mtch.getEventFile().getEvents().size() > 0)) {
						for(Event evnt: mtch.getEventFile().getEvents()) {
							if(evnt.getEventExtra() != null) {
								if(evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
									bowlerId = evnt.getEventBowlerNo();
									inning_num = evnt.getEventInningNumber();
									bowler_found = true;
									data_set = true;
									runs = 0;
									wicket = 0;
								}
							}
							
							if(bowler_found && evnt.getEventBowlerNo() == bowlerId) {
								switch(evnt.getEventType()) {
								case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
				            		runs += evnt.getEventRuns();
				                    break;
				            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
				            		runs += evnt.getEventRuns();
				                    break;

				            	case CricketUtil.LOG_WICKET:
				                    if (evnt.getEventRuns() > 0)
				                    {
				                    	runs += evnt.getEventRuns();
				                    }
				                    wicket += 1;
				                    break;

				            	case CricketUtil.LOG_ANY_BALL:
				            		runs += evnt.getEventRuns();
				                    if (evnt.getEventExtra() != null)
				                    {
				                    	runs += evnt.getEventExtraRuns();
				                    }
				                    if (evnt.getEventSubExtra() != null)
				                    {
				                    	runs += evnt.getEventSubExtraRuns();
				                    }
				                    break;										
								}
							}else if(evnt.getEventBowlerNo() != bowlerId && evnt.getEventBowlerNo() != 0) {
								bowler_found = false;
								
								if(data_set) {
									
									tapeBall.add(new BestStats(0,null, null, mtch.getSetup().getMatchIdent(), runs, wicket));
									
									for (BowlingCard boc : mtch.getMatch().getInning().get(inning_num - 1).getBowlingCard()) {
										if(boc.getPlayerId() == bowlerId) {
											tapeBall.get(tapeBall.size() - 1).setPlayerId(boc.getPlayerId());
											tapeBall.get(tapeBall.size() - 1).setPlayer(boc.getPlayer());
										}
									}
									
									if(CricketFunctions.getHomeAwayPlayer(bowlerId, mtch).getTeamId() == mtch.getSetup().getHomeTeamId()) {
										tapeBall.get(tapeBall.size() - 1).setOpponentTeam(mtch.getSetup().getAwayTeam());
									}else {
										tapeBall.get(tapeBall.size() - 1).setOpponentTeam(mtch.getSetup().getHomeTeam());
									}
									
									data_set = false;
								}
							}
						}
					}
				}
			}
			return tapeBall;
		case "CURRENT_MATCH_DATA":
			
			List<BestStats> past_tape_ball_clone = past_tapeBall.stream().map(tapeball ->{
				try {
					return tapeball.clone();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return tapeball;
			}).collect(Collectors.toList());
			
			int bowlerId = 0,runs = 0,wicket = 0,inning_num = 0;
			boolean bowler_found = false,data_set = false;
			if ((currentMatch.getEventFile().getEvents() != null) && (currentMatch.getEventFile().getEvents().size() > 0)) {
				for(Event evnt: currentMatch.getEventFile().getEvents()) {
					if(evnt.getEventExtra() != null) {
						if(evnt.getEventExtra().equalsIgnoreCase("TAPE")) {
							bowlerId = evnt.getEventBowlerNo();
							inning_num = evnt.getEventInningNumber();
							bowler_found = true;
							data_set = true;
							runs = 0;
							wicket = 0;
						}
					}
					
					if(bowler_found && evnt.getEventBowlerNo() == bowlerId) {
						switch(evnt.getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		            		runs += evnt.getEventRuns();
		                    break;
		            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		            		runs += evnt.getEventRuns();
		                    break;

		            	case CricketUtil.LOG_WICKET:
		                    if (evnt.getEventRuns() > 0)
		                    {
		                    	runs += evnt.getEventRuns();
		                    }
		                    wicket += 1;
		                    break;

		            	case CricketUtil.LOG_ANY_BALL:
		            		runs += evnt.getEventRuns();
		                    if (evnt.getEventExtra() != null)
		                    {
		                    	runs += evnt.getEventExtraRuns();
		                    }
		                    if (evnt.getEventSubExtra() != null)
		                    {
		                    	runs += evnt.getEventSubExtraRuns();
		                    }
		                    break;										
						}
					}else if(evnt.getEventBowlerNo() != bowlerId && evnt.getEventBowlerNo() != 0) {
						bowler_found = false;
						
						if(data_set) {
							
							past_tape_ball_clone.add(new BestStats(0,null, null, currentMatch.getSetup().getMatchIdent(), runs, wicket));
							
							for (BowlingCard boc : currentMatch.getMatch().getInning().get(inning_num - 1).getBowlingCard()) {
								if(boc.getPlayerId() == bowlerId) {
									past_tape_ball_clone.get(past_tape_ball_clone.size() - 1).setPlayerId(boc.getPlayerId());
									past_tape_ball_clone.get(past_tape_ball_clone.size() - 1).setPlayer(boc.getPlayer());
								}
							}
							
							if(CricketFunctions.getHomeAwayPlayer(bowlerId, currentMatch).getTeamId() == currentMatch.getSetup().getHomeTeamId()) {
								past_tape_ball_clone.get(past_tape_ball_clone.size() - 1).setOpponentTeam(currentMatch.getSetup().getAwayTeam());
							}else {
								past_tape_ball_clone.get(past_tape_ball_clone.size() - 1).setOpponentTeam(currentMatch.getSetup().getHomeTeam());
							}
							
							data_set = false;
						}
					}
				}
			}
			return past_tape_ball_clone;	
		}
		return null;
	}

	public static List<Tournament> extractTournamentStats(String typeOfExtraction,boolean ShowStrikeRate, List<MatchAllData> tournament_matches, 
			CricketService cricketService,MatchAllData currentMatch, List<Tournament> past_tournament_stat) throws IOException 
	{		
		int playerId = -1;
		//String text_to_return = "";
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
//		ArrayList<String> ImpactData = new ArrayList<String>();
		boolean has_match_started = false,is_player_found = false,fielder_found = false;
		
//		try (BufferedReader br = new BufferedReader(new FileReader(CricketUtil.CRICKET_DIRECTORY + "ImpactPlayer.txt"))) {
//			while((text_to_return = br.readLine()) != null) {
//				if(text_to_return.contains("|")) {
//					
//				}else {
//					ImpactData.add(text_to_return);
//				}
//			}
//		}
		
		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			
			 return extractTournamentStats("CURRENT_MATCH_DATA",ShowStrikeRate, tournament_matches, cricketService, currentMatch, 
					extractTournamentStats("PAST_MATCHES_DATA",ShowStrikeRate, tournament_matches, cricketService, currentMatch, null));
			
		case "PAST_MATCHES_DATA":
			
			for(MatchAllData mtch : tournament_matches) {
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					has_match_started = false;
					
					if(mtch.getSetup().getMatchType().equalsIgnoreCase(currentMatch.getSetup().getMatchType())) {
						if(mtch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * mtch.getMatch().getInning().get(0).getTotalOvers() 
							+ mtch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
							has_match_started = true;
						}
						is_player_found = false;
						for(Inning inn : mtch.getMatch().getInning())
						{
							
							if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0) {
								has_match_started = true;
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
										
										if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
											tournament_stats.get(playerId).setThreeWicketHaul(tournament_stats.get(playerId).getThreeWicketHaul() + 1);
										}else if(boc.getWickets() >= 5) {
											tournament_stats.get(playerId).setFiveWicketHaul(tournament_stats.get(playerId).getFiveWicketHaul() + 1);
										}
										
										tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
											(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(), 
											mtch.getSetup().getGround(), mtch.getMatch().getMatchFileName().replace(".json", "") ,boc.getPlayer(),""));
										
									}else {
										
										tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0,0, 0, 0, boc.getWickets(), 
												boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, boc.getDots(),0,0,null,0,0,0,0, 
												boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										
										if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
											tournament_stats.get(tournament_stats.size() - 1).setThreeWicketHaul(
													tournament_stats.get(tournament_stats.size() - 1).getThreeWicketHaul() + 1);
										}else if(boc.getWickets() >= 5) {
											tournament_stats.get(tournament_stats.size() - 1).setFiveWicketHaul(
													tournament_stats.get(tournament_stats.size() - 1).getFiveWicketHaul() + 1);
										}
										
										tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
												(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(), 
												mtch.getSetup().getGround(), mtch.getMatch().getMatchFileName().replace(".json", ""), boc.getPlayer(),""));
																				
									}
								}
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
										
										if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
											tournament_stats.get(playerId).setThirty(tournament_stats.get(playerId).getThirty() + 1);
										}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
											tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + 1);
										}else if(bc.getRuns() >= 100) {
											tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + 1);
										}
										
										if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
											tournament_stats.get(playerId).setInnings(tournament_stats.get(playerId).getInnings()+1);
										}
										
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(playerId).setNot_out(tournament_stats.get(playerId).getNot_out() + 1);
											
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
													(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(),
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.NOT_OUT));
											
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
													(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(),
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.OUT));
											
										}
										else {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
													bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(),
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.STILL_TO_BAT));
										}
										
										
										//getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, tournament_stats, mtch);
										
									}else {
										tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0, 0,0, 0, 0, 0, 
												bc.getBalls(), 0,0,0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>(), 
												new ArrayList<BestStats>()));
										
										if(bc.getRuns() >= 30 && bc.getRuns() < 50) {
											tournament_stats.get(tournament_stats.size() - 1).setThirty(tournament_stats.get(tournament_stats.size() - 1).getThirty() + 1);
										}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
											tournament_stats.get(tournament_stats.size() - 1).setFifty(tournament_stats.get(tournament_stats.size() - 1).getFifty() + 1);
										}else if(bc.getRuns() >= 100) {
											tournament_stats.get(tournament_stats.size() - 1).setHundreds(tournament_stats.get(tournament_stats.size() - 1).getHundreds() + 1);
										}
										
										if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
											tournament_stats.get(tournament_stats.size() - 1).setInnings(tournament_stats.get(tournament_stats.size() - 1).getInnings()+1);
										}
										
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(tournament_stats.size() - 1).setNot_out(tournament_stats.get(tournament_stats.size() - 1).getNot_out() + 1);
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
													bc.getBalls(),inn.getBowling_team(), mtch.getSetup().getGround(),mtch.getMatch().getMatchFileName().replace(".json", ""), 
													bc.getPlayer(),CricketUtil.NOT_OUT));
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
													bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(), mtch.getMatch().getMatchFileName().replace(".json", ""),
													bc.getPlayer(),CricketUtil.OUT));
										}
										else {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
													bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(), mtch.getMatch().getMatchFileName().replace(".json", ""),
													bc.getPlayer(),CricketUtil.STILL_TO_BAT));
										}
										
										//getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), (tournament_stats.size() - 1), cricketService, tournament_stats, mtch);
									}	
								}
							}
							for(Tournament trmnt : tournament_stats) {
								for(Player plyr : mtch.getSetup().getHomeSubstitutes()) {
//									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trmnt.getPlayerId()) {
											fielder_found = true;
										}
//									}
								}
								
								for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
//									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trmnt.getPlayerId()) {
											fielder_found = true;
										}
//									}
								}
							}
							
							if(fielder_found == false) {
								for(Player plyr : mtch.getSetup().getHomeSubstitutes()) {
//									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0,0,0,CricketUtil.STILL_TO_BAT,
												0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//										fielder_found = true;
//									}
								}
								
								for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
//									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,CricketUtil.STILL_TO_BAT,
												0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									}
//								}
							}
						}
						
						if(has_match_started == true) {
							for(Tournament trmnt : tournament_stats) {
								is_player_found = false;
								for(Inning inn : mtch.getMatch().getInning())
								{
									if(is_player_found == false) {
										if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
											for(BowlingCard boc : inn.getBowlingCard())
											{
												if(boc.getPlayerId() == trmnt.getPlayerId()) {
													is_player_found = true;
													trmnt.setMatches(trmnt.getMatches() + 1);
												}
											}
										}
									}
									
									if(is_player_found == false) {
										for(BattingCard bc : inn.getBattingCard())
										{
											if(bc.getPlayerId() == trmnt.getPlayerId()) {
												is_player_found = true;
												trmnt.setMatches(trmnt.getMatches() + 1);
											}
										}
									}
									
									if(is_player_found == false) {
										for(Player plyr : mtch.getSetup().getHomeSubstitutes()) {
//											if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
												if(plyr.getPlayerId() == trmnt.getPlayerId()) {
													is_player_found = true;
													trmnt.setMatches(trmnt.getMatches() + 1);
												}
//											}
										}
										
										if(is_player_found == false) {
											for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
//												if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
													if(plyr.getPlayerId() == trmnt.getPlayerId()) {
														is_player_found = true;
														trmnt.setMatches(trmnt.getMatches() + 1);
													}
//												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return tournament_stats;
			
		case "CURRENT_MATCH_DATA":
			
			List<Tournament> past_tournament_stat_clone = new ArrayList<Tournament>();
			for(Tournament tourn : past_tournament_stat) {
				past_tournament_stat_clone.add(objectMapper.readValue(objectMapper.writeValueAsString(tourn), Tournament.class));
			}
				
//			List<Tournament> past_tournament_stat_clone = past_tournament_stat.stream().map(tourn_stats -> {
//				try {
//					return tourn_stats.clone();
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//				}
//				return tourn_stats;
//			}).collect(Collectors.toList());
			
			has_match_started = false;
			is_player_found = false;
			fielder_found = false;
			if(currentMatch.getSetup().getMatchType().equalsIgnoreCase(currentMatch.getSetup().getMatchType())) {
				if(currentMatch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getMatch().getInning().get(0).getTotalOvers() 
						+ currentMatch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
					has_match_started = true;
				}
				
				for(Inning inn : currentMatch.getMatch().getInning())
				{
					if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0 || inn.getInningStatus().equalsIgnoreCase(CricketUtil.START) || inn.getInningStatus().equalsIgnoreCase(CricketUtil.PAUSE)) {
						has_match_started = true;
					}
					
					if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
						for(BowlingCard boc : inn.getBowlingCard())
						{
							playerId = -1;
							for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
							{
								if(boc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayer().getPlayerId()) {
									playerId = i;
									break;
								}
							}
							if(playerId >= 0) {
								past_tournament_stat_clone.get(playerId).setRunsConceded(past_tournament_stat_clone.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
								past_tournament_stat_clone.get(playerId).setWickets(past_tournament_stat_clone.get(playerId).getWickets() + boc.getWickets());
								past_tournament_stat_clone.get(playerId).setDots(past_tournament_stat_clone.get(playerId).getDots() + boc.getDots());
								past_tournament_stat_clone.get(playerId).setBallsBowled(past_tournament_stat_clone.get(playerId).getBallsBowled() + 
										6 * boc.getOvers() + boc.getBalls());
								
								if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
									past_tournament_stat_clone.get(playerId).setThreeWicketHaul(past_tournament_stat_clone.get(playerId).getThreeWicketHaul() + 1);
								}else if(boc.getWickets() >= 5) {
									past_tournament_stat_clone.get(playerId).setFiveWicketHaul(past_tournament_stat_clone.get(playerId).getFiveWicketHaul() + 1);
								}
								
								past_tournament_stat_clone.get(playerId).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
										(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
										currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
	
							}else {
								past_tournament_stat_clone.add(new Tournament(boc.getPlayerId(), 0, 0,0, 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 
										6 * boc.getOvers() + boc.getBalls(), 0, boc.getDots(),0,0,null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), 
										new ArrayList<BestStats>(), new ArrayList<BestStats>()));
								
								if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setThreeWicketHaul(
											past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThreeWicketHaul() + 1);
								}else if(boc.getWickets() >= 5) {
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setFiveWicketHaul(
											past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFiveWicketHaul() + 1);
								}
								
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
										(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
										currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
								
							}
						}
					}
					
					for(BattingCard bc : inn.getBattingCard())
					{
						playerId = -1;
						for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
						{
							if(bc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayerId()) {
								playerId = i;
								break;
							}
						}
						
						if(playerId >= 0) {
							past_tournament_stat_clone.get(playerId).setRuns(past_tournament_stat_clone.get(playerId).getRuns() + bc.getRuns()); // existing record
							past_tournament_stat_clone.get(playerId).setBallsFaced(past_tournament_stat_clone.get(playerId).getBallsFaced() + bc.getBalls());
							past_tournament_stat_clone.get(playerId).setFours(past_tournament_stat_clone.get(playerId).getFours() + bc.getFours());
							past_tournament_stat_clone.get(playerId).setSixes(past_tournament_stat_clone.get(playerId).getSixes() + bc.getSixes());
							
							if(bc.getRuns() >= 30 && bc.getRuns() < 50) {
								past_tournament_stat_clone.get(playerId).setThirty(past_tournament_stat_clone.get(playerId).getThirty() + 1);	
							}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
								past_tournament_stat_clone.get(playerId).setFifty(past_tournament_stat_clone.get(playerId).getFifty() + 1);
							}else if(bc.getRuns()>= 100) {
								past_tournament_stat_clone.get(playerId).setHundreds(past_tournament_stat_clone.get(playerId).getHundreds() + 1);
							}
							
							if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.get(playerId).setInnings(past_tournament_stat_clone.get(playerId).getInnings()+1);
							}
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stat_clone.get(playerId).setNot_out(past_tournament_stat_clone.get(playerId).getNot_out() + 1);
								
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
										bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
								
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
										bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
								
							}
							else {
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
										bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(),
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
							}
							
							if(ShowStrikeRate == true) {
								getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, past_tournament_stat_clone, currentMatch);
							}
							
							
						}else {
							past_tournament_stat_clone.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0,0, 0, 0, 0, 0, 
									bc.getBalls(),0,0,0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>(), 
									new ArrayList<BestStats>()));
							if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
									setThirty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThirty() + 1);
							}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
									setFifty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFifty() + 1);
							}else if(bc.getRuns()>= 100) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
									setHundreds(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getHundreds() + 1);
							}
							
							if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setInnings(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getInnings()+1);
							}
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setNot_out(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getNot_out() + 1);
								
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
							}
							else {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
										currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
							}
							
							if(ShowStrikeRate == true) {
								getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, past_tournament_stat_clone, currentMatch);
							}
						}	
					}
					
					for(Tournament trmnt : past_tournament_stat_clone) {
						for(Player plyr : currentMatch.getSetup().getHomeSubstitutes()) {
//							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									fielder_found = true;
								}
//							}
						}
						
						for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
//							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									fielder_found = true;
								}
//							}
						}
					}
					
					if(fielder_found == false) {
						for(Player plyr : currentMatch.getSetup().getHomeSubstitutes()) {
//							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,CricketUtil.STILL_TO_BAT,
										0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
								//fielder_found = true;
//							}
						}
						
						for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
//							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, 0,0,0,CricketUtil.STILL_TO_BAT,
										0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//							}
						}
					}
				}
				
				if(has_match_started == true) {
					for(Tournament trment : past_tournament_stat_clone) {
						is_player_found = false;
						for(Inning inn : currentMatch.getMatch().getInning())
						{
							if(is_player_found == false) {
								for(BattingCard bc : inn.getBattingCard())
								{
									if(bc.getPlayerId() == trment.getPlayerId()) {
										trment.setMatches(trment.getMatches() + 1);
										
										is_player_found = true;
									}
								}
							}
							
							if(is_player_found == false) {
								if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
									for(BowlingCard boc : inn.getBowlingCard())
									{
										if(boc.getPlayerId() == trment.getPlayerId()) {
											trment.setMatches(trment.getMatches() + 1);
											is_player_found = true;
										}
									}
								}
							}
							
							if(is_player_found == false) {
								for(Player plyr : currentMatch.getSetup().getHomeSubstitutes()) {
//									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trment.getPlayerId()) {
											is_player_found = true;
											trment.setMatches(trment.getMatches() + 1);
										}
//									}
								}
								
								if(is_player_found == false) {
									for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
//										if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
											if(plyr.getPlayerId() == trment.getPlayerId()) {
												is_player_found = true;
												trment.setMatches(trment.getMatches() + 1);
											}
//										}
									}
								}
							}
						}
					}
				}
			}
			return past_tournament_stat_clone;
		}
		return null;
	}

    public static void extractBatscoreByPosition(List<Team> team) {
        
    	for (File file : new File(CricketUtil.CRICKET_SERVER_DIRECTORY + CricketUtil.HEADTOHEAD_DIRECTORY).listFiles()) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    int lineIndex = 0;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("IS")) {
                            String[] parts = line.split("\\s+");
                            if (team == null || team.stream().noneMatch(tm -> tm.getTeamName1().equalsIgnoreCase(parts[4].trim()))) {
                                team.add(new Team(parts[4].trim(), new ArrayList<Player>()));
                            }
                            if (lineIndex == 12) {
                                lineIndex = 0;
                            }
                            lineIndex++;
                            if (parts.length >= 10) {
                                String teamName = parts[4].trim();
                                for (Team tm : team) {
                                    if (tm.getTeamName1().equals(teamName)) {

                                        int batsmanId = Integer.parseInt(parts[6]);
                                        int runs = Integer.parseInt(parts[7]);
                                        int balls = Integer.parseInt(parts[8]);
                                        
                                        if (tm.getPlayer() == null || tm.getPlayer().stream().noneMatch(pl -> pl.getPlayerId() == batsmanId)) {
                                            if (tm.getPlayer() == null) {
                                                tm.setPlayer(new ArrayList<Player>());
                                            }
                                            tm.getPlayer().add(new Player(batsmanId, new ArrayList<>(Arrays.asList(
                                                    new Player(1, 0, 0), new Player(2, 0, 0), new Player(3, 0, 0),
                                                    new Player(4, 0, 0), new Player(5, 0, 0), new Player(6, 0, 0),
                                                    new Player(7, 0, 0), new Player(8, 0, 0), new Player(9, 0, 0),
                                                    new Player(10, 0, 0), new Player(11, 0, 0)))));
                                        }

                                        for (Player ply : tm.getPlayer()) {
                                            if (ply.getPlayerId() == batsmanId) {
                                                for (Player pos : ply.getPlayerPos()) {
                                                    if (lineIndex == pos.getPlayerPosition()) {
                                                        pos.setRuns((pos.getRuns() + runs));
                                                        pos.setBalls((pos.getBalls() + balls));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T deepCopy(T object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deep copy object", e);
        }
    }
    
    public static List<Tournament> extractTournamentData(String typeOfExtraction,boolean ShowStrikeRate, List<HeadToHeadPlayer> headToHead_matches, 
			CricketService cricketService,MatchAllData currentMatch, List<Tournament> past_tournament_stat){
		
		int playerId = -1;
		List<Tournament> tournament_stats = new ArrayList<Tournament>();

		switch(typeOfExtraction) {
		case "COMBINED_PAST_CURRENT_MATCH_DATA":
			 return extractTournamentData("CURRENT_MATCH_DATA",ShowStrikeRate, headToHead_matches, cricketService, currentMatch, 
					 extractTournamentData("PAST_MATCHES_DATA",ShowStrikeRate, headToHead_matches, cricketService, currentMatch, null));
			 
		case "PAST_MATCHES_DATA":
			
			for(HeadToHeadPlayer mtch : headToHead_matches) {
				
				if(!mtch.getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
					
					playerId = -1;
					for(int i=0; i<=tournament_stats.size() - 1;i++)
					{
						if(mtch.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
							playerId = i;
							break;
						}
					}
					
					if(playerId >= 0) 
					{
						
						tournament_stats.get(playerId).setRuns(tournament_stats.get(playerId).getRuns() + mtch.getRuns());
						tournament_stats.get(playerId).setBallsFaced(tournament_stats.get(playerId).getBallsFaced() + mtch.getBallsFaced());
						tournament_stats.get(playerId).setFours(tournament_stats.get(playerId).getFours() + mtch.getFours());
						tournament_stats.get(playerId).setSixes(tournament_stats.get(playerId).getSixes() + mtch.getSixes());
						tournament_stats.get(playerId).setMatches(tournament_stats.get(playerId).getMatches() + 1);
						
						if(mtch.getRuns()>= 30 && mtch.getRuns() < 50) {
							tournament_stats.get(playerId).setThirty(tournament_stats.get(playerId).getThirty() + 1);
						}else if(mtch.getRuns()>= 50 && mtch.getRuns() < 100) {
							tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + 1);
						}else if(mtch.getRuns() >= 100) {
							tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + 1);
						}
						
						if(mtch.getInningStarted() != null && mtch.getInningStarted().trim().contains("Y")) {
							tournament_stats.get(playerId).setInnings(tournament_stats.get(playerId).getInnings()+1);
						}
						
						if(mtch.getDismissed() != null && mtch.getDismissed().trim().contains("N")) {
							tournament_stats.get(playerId).setNot_out(tournament_stats.get(playerId).getNot_out() + 1);
							tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), ((mtch.getRuns() * 2) + 1), 
									mtch.getBallsFaced(),mtch.getOpponentTeam(), null,mtch.getMatchFileName().replace(".json", ""), 
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.NOT_OUT));
						}else if(mtch.getDismissed() != null && mtch.getDismissed().trim().contains("Y")) {
							tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.OUT));
						}
						else {
							tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.STILL_TO_BAT));
						}
						
						tournament_stats.get(playerId).setWickets(tournament_stats.get(playerId).getWickets() + mtch.getWickets());
						tournament_stats.get(playerId).setRunsConceded(tournament_stats.get(playerId).getRunsConceded() + mtch.getRunsConceded());
						tournament_stats.get(playerId).setDots(tournament_stats.get(playerId).getDots() + mtch.getBalldots());
						tournament_stats.get(playerId).setBallsBowled(tournament_stats.get(playerId).getBallsBowled() + mtch.getBallsBowled());
						
						if(mtch.getWickets() >= 3 && mtch.getWickets() < 5) {
							tournament_stats.get(playerId).setThreeWicketHaul(tournament_stats.get(playerId).getThreeWicketHaul() + 1);
						}else if(mtch.getWickets() >= 5) {
							tournament_stats.get(playerId).setFiveWicketHaul(tournament_stats.get(playerId).getFiveWicketHaul() + 1);
						}
						
						tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(mtch.getPlayerId(), 
							(1000 * mtch.getWickets()) - mtch.getRunsConceded(), mtch.getBallsBowled(), mtch.getOpponentTeam(), null, 
							mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
						
						//Player Tape Ball Data
						tournament_stats.get(playerId).setTapeBall_balls(tournament_stats.get(playerId).getTapeBall_balls() + mtch.getTapeBall_balls());
						tournament_stats.get(playerId).setTapeBall_runs(tournament_stats.get(playerId).getTapeBall_runs() + mtch.getTapeBall_runs());
						tournament_stats.get(playerId).setTapeBall_wickets(tournament_stats.get(playerId).getTapeBall_wickets() + mtch.getTapeBall_wickets());
						tournament_stats.get(playerId).setTapeBall_dotsBall(tournament_stats.get(playerId).getTapeBall_dotsBall() + mtch.getTapeBall_dotsBall());
						
						tournament_stats.get(playerId).getTapeBall_best_Stats().add(new BestStats(mtch.getPlayerId(), 
								(1000 * mtch.getTapeBall_wickets()) - mtch.getTapeBall_runs(), mtch.getTapeBall_balls(), mtch.getOpponentTeam(), null, 
								mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
					}
					else {
						tournament_stats.add(new Tournament(mtch.getPlayerId(), mtch.getRuns(), mtch.getFours(), mtch.getSixes(), 0, 0, 0, 0, 0, 
								mtch.getWickets(), mtch.getRunsConceded(), mtch.getBallsBowled(), mtch.getBallsFaced(), mtch.getBalldots(), 
								0,0,"",0,0,0,0, cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())), 
								new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
						
						tournament_stats.get(tournament_stats.size() - 1).setMatches(tournament_stats.get(tournament_stats.size() - 1).getMatches() + 1);
						
						if(mtch.getRuns() >= 30 && mtch.getRuns() < 50) {
							tournament_stats.get(tournament_stats.size() - 1).setThirty(tournament_stats.get(tournament_stats.size() - 1).getThirty() + 1);
						}else if(mtch.getRuns()>= 50 && mtch.getRuns() < 100) {
							tournament_stats.get(tournament_stats.size() - 1).setFifty(tournament_stats.get(tournament_stats.size() - 1).getFifty() + 1);
						}else if(mtch.getRuns() >= 100) {
							tournament_stats.get(tournament_stats.size() - 1).setHundreds(tournament_stats.get(tournament_stats.size() - 1).getHundreds() + 1);
						}
						
						if(mtch.getInningStarted() != null && mtch.getInningStarted().trim().contains("Y")) {
							tournament_stats.get(tournament_stats.size() - 1).setInnings(tournament_stats.get(tournament_stats.size() - 1).getInnings()+1);
						}
						
						if(mtch.getDismissed() != null && mtch.getDismissed().trim().contains("N")) {
							tournament_stats.get(tournament_stats.size() - 1).setNot_out(tournament_stats.get(tournament_stats.size() - 1).getNot_out() + 1);
							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), ((mtch.getRuns() * 2) + 1), 
									mtch.getBallsFaced(),mtch.getOpponentTeam(), null,mtch.getMatchFileName().replace(".json", ""), 
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.NOT_OUT));
						}
						else if(mtch.getDismissed() != null && mtch.getDismissed().trim().contains("Y")) {
							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.OUT));
						}
						else {
							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.STILL_TO_BAT));
						}
						
						if(mtch.getWickets() >= 3 && mtch.getWickets() < 5) {
							tournament_stats.get(tournament_stats.size() - 1).setThreeWicketHaul(
								tournament_stats.get(tournament_stats.size() - 1).getThreeWicketHaul() + 1);
						}else if(mtch.getWickets() >= 5) {
							tournament_stats.get(tournament_stats.size() - 1).setFiveWicketHaul(
								tournament_stats.get(tournament_stats.size() - 1).getFiveWicketHaul() + 1);
						}
						
						tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(mtch.getPlayerId(), 
							((1000 * mtch.getWickets()) - mtch.getRunsConceded()), mtch.getBallsBowled(), mtch.getOpponentTeam(), null, 
							mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
						
						//Player Tape Ball Data
						tournament_stats.get(tournament_stats.size() - 1).setTapeBall_balls(tournament_stats.get(tournament_stats.size() - 1).getTapeBall_balls() + mtch.getTapeBall_balls());
						tournament_stats.get(tournament_stats.size() - 1).setTapeBall_runs(tournament_stats.get(tournament_stats.size() - 1).getTapeBall_runs() + mtch.getTapeBall_runs());
						tournament_stats.get(tournament_stats.size() - 1).setTapeBall_wickets(tournament_stats.get(tournament_stats.size() - 1).getTapeBall_wickets() + mtch.getTapeBall_wickets());
						tournament_stats.get(tournament_stats.size() - 1).setTapeBall_dotsBall(tournament_stats.get(tournament_stats.size() - 1).getTapeBall_dotsBall() + mtch.getTapeBall_dotsBall());
						
						tournament_stats.get(tournament_stats.size() - 1).getTapeBall_best_Stats().add(new BestStats(mtch.getPlayerId(), 
								(1000 * mtch.getTapeBall_wickets()) - mtch.getTapeBall_runs(), mtch.getTapeBall_balls(), mtch.getOpponentTeam(), null, 
								mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
					}
					Collections.sort(tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats(),
						new CricketFunctions.BatsmanBestStatsComparator());
					Collections.sort(tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats(),
						new CricketFunctions.BowlerBestStatsComparator());
					Collections.sort(tournament_stats.get(tournament_stats.size() - 1).getTapeBall_best_Stats(),
						new CricketFunctions.BowlerBestStatsComparator());
				}
			}
			return tournament_stats;
	
		case "CURRENT_MATCH_DATA":
			
			List<Tournament> past_tournament_stat_clone = new ArrayList<Tournament>();
			
//			past_tournament_stat_clone = past_tournament_stat.stream().map(tourn_stats -> {
//				try {
//					return tourn_stats.clone();
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//				}
//				return tourn_stats;
//			}).collect(Collectors.toList());
			
			
			past_tournament_stat_clone = past_tournament_stat.stream().map(tourn_stats -> {
			    try {
			        return tourn_stats.clone(); // Updated deep clone
			    } catch (CloneNotSupportedException e) {
			        e.printStackTrace();
			    }
			    return null;
			}).collect(Collectors.toList());
			
			
			for(Inning inn : currentMatch.getMatch().getInning())
			{
				if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
					for(BowlingCard boc : inn.getBowlingCard())
					{
						playerId = -1;
						for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
						{
							if(boc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayer().getPlayerId()) {
								playerId = i;
								break;
							}
						}
						if(playerId >= 0) {
							past_tournament_stat_clone.get(playerId).setRunsConceded(past_tournament_stat_clone.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
							past_tournament_stat_clone.get(playerId).setWickets(past_tournament_stat_clone.get(playerId).getWickets() + boc.getWickets());
							past_tournament_stat_clone.get(playerId).setDots(past_tournament_stat_clone.get(playerId).getDots() + boc.getDots());
							past_tournament_stat_clone.get(playerId).setBallsBowled(past_tournament_stat_clone.get(playerId).getBallsBowled() + 
									6 * boc.getOvers() + boc.getBalls());
							
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								past_tournament_stat_clone.get(playerId).setThreeWicketHaul(past_tournament_stat_clone.get(playerId).getThreeWicketHaul() + 1);
							}else if(boc.getWickets() >= 5) {
								past_tournament_stat_clone.get(playerId).setFiveWicketHaul(past_tournament_stat_clone.get(playerId).getFiveWicketHaul() + 1);
							}
							
							past_tournament_stat_clone.get(playerId).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
								(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
								currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
							Collections.sort(past_tournament_stat_clone.get(playerId).getBowler_best_Stats(),new CricketFunctions.BowlerBestStatsComparator());
							
							//Tape Ball Data
							List<String> tapeBall = getTapeBalldetails(inn.getInningNumber(), currentMatch.getEventFile().getEvents(), currentMatch);
							for(int i=0;i<=tapeBall.size()-1;i++) {
								if(Integer.valueOf(tapeBall.get(i).split(",")[0].trim()).intValue() == boc.getPlayerId()) {
									past_tournament_stat_clone.get(playerId).setTapeBall_balls(past_tournament_stat_clone.get(playerId).getTapeBall_balls() + 
											Integer.valueOf(tapeBall.get(i).split(",")[1]).intValue());
									past_tournament_stat_clone.get(playerId).setTapeBall_runs(past_tournament_stat_clone.get(playerId).getTapeBall_runs() + 
											Integer.valueOf(tapeBall.get(i).split(",")[2]).intValue());
									past_tournament_stat_clone.get(playerId).setTapeBall_wickets(past_tournament_stat_clone.get(playerId).getTapeBall_wickets() + 
											Integer.valueOf(tapeBall.get(i).split(",")[3]).intValue());
									past_tournament_stat_clone.get(playerId).setTapeBall_dotsBall(past_tournament_stat_clone.get(playerId).getTapeBall_dotsBall() + 
											Integer.valueOf(tapeBall.get(i).split(",")[4]).intValue());
									
									past_tournament_stat_clone.get(playerId).getTapeBall_best_Stats().add(new BestStats(boc.getPlayerId(), 
											(1000 * Integer.valueOf(tapeBall.get(i).split(",")[3])) - Integer.valueOf(tapeBall.get(i).split(",")[2]), 
											Integer.valueOf(tapeBall.get(i).split(",")[1]), inn.getBatting_team(),currentMatch.getSetup().getGround(),
											currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
								}
							}
							Collections.sort(past_tournament_stat_clone.get(playerId).getTapeBall_best_Stats(),new CricketFunctions.BowlerBestStatsComparator());
							
						}else {
							past_tournament_stat_clone.add(new Tournament(boc.getPlayerId(), 0, 0,0, 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 
									6 * boc.getOvers() + boc.getBalls(), 0, boc.getDots(),0,0,null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), 
									new ArrayList<BestStats>(), new ArrayList<BestStats>()));
							
							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setThreeWicketHaul(
										past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThreeWicketHaul() + 1);
							}else if(boc.getWickets() >= 5) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setFiveWicketHaul(
										past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFiveWicketHaul() + 1);
							}
							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
								(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
								currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
							Collections.sort(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getBowler_best_Stats(),
								new CricketFunctions.BowlerBestStatsComparator());
							
							//Tape Ball Data
							List<String> tapeBall = getTapeBalldetails(inn.getInningNumber(), currentMatch.getEventFile().getEvents(), currentMatch);
							for(int i=0;i<=tapeBall.size()-1;i++) {
								if(Integer.valueOf(tapeBall.get(i).split(",")[0].trim()).intValue() == boc.getPlayerId()) {
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setTapeBall_balls(past_tournament_stat_clone.get(
											past_tournament_stat_clone.size() - 1).getTapeBall_balls() + Integer.valueOf(tapeBall.get(i).split(",")[1]));
									
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setTapeBall_runs(past_tournament_stat_clone.get(
											past_tournament_stat_clone.size() - 1).getTapeBall_runs() + Integer.valueOf(tapeBall.get(i).split(",")[2]));
									
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setTapeBall_wickets(past_tournament_stat_clone.get(
											past_tournament_stat_clone.size() - 1).getTapeBall_wickets() + Integer.valueOf(tapeBall.get(i).split(",")[3]));
									
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setTapeBall_dotsBall(past_tournament_stat_clone.get(
											past_tournament_stat_clone.size() - 1).getTapeBall_dotsBall() + Integer.valueOf(tapeBall.get(i).split(",")[4]));
									
									past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getTapeBall_best_Stats().add(new BestStats(boc.getPlayerId(), 
											(1000 * Integer.valueOf(tapeBall.get(i).split(",")[3])) - Integer.valueOf(tapeBall.get(i).split(",")[2]), 
											Integer.valueOf(tapeBall.get(i).split(",")[1]), inn.getBatting_team(),currentMatch.getSetup().getGround(),
											currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
								}
							}
							if(playerId > 0) {
								Collections.sort(past_tournament_stat_clone.get(playerId).getTapeBall_best_Stats(),new CricketFunctions.BowlerBestStatsComparator());
							}
						}
					}
				}
				
				for(BattingCard bc : inn.getBattingCard())
				{
					playerId = -1;
					for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
					{
						if(bc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayerId()) {
							playerId = i;
							break;
						}
					}
					
					if(playerId >= 0) {
						past_tournament_stat_clone.get(playerId).setRuns(past_tournament_stat_clone.get(playerId).getRuns() + bc.getRuns()); // existing record
						past_tournament_stat_clone.get(playerId).setBallsFaced(past_tournament_stat_clone.get(playerId).getBallsFaced() + bc.getBalls());
						past_tournament_stat_clone.get(playerId).setFours(past_tournament_stat_clone.get(playerId).getFours() + bc.getFours());
						past_tournament_stat_clone.get(playerId).setSixes(past_tournament_stat_clone.get(playerId).getSixes() + bc.getSixes());
						
						if(bc.getRuns() >= 30 && bc.getRuns() < 50) {
							past_tournament_stat_clone.get(playerId).setThirty(past_tournament_stat_clone.get(playerId).getThirty() + 1);	
						}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
							past_tournament_stat_clone.get(playerId).setFifty(past_tournament_stat_clone.get(playerId).getFifty() + 1);
						}else if(bc.getRuns()>= 100) {
							past_tournament_stat_clone.get(playerId).setHundreds(past_tournament_stat_clone.get(playerId).getHundreds() + 1);
						}
						
						if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
							past_tournament_stat_clone.get(playerId).setInnings(past_tournament_stat_clone.get(playerId).getInnings()+1);
						}
						
//						if(bc.getPlayerId() == 115) {
//							System.out.println("name = " + bc.getPlayer().getFull_name() + "   runs = " + bc.getRuns());
//						}
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							past_tournament_stat_clone.get(playerId).setNot_out(past_tournament_stat_clone.get(playerId).getNot_out() + 1);
							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
							
						}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
							
						}
						else {
							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(),
									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
						}
						if(currentMatch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getMatch().getInning().get(0).getTotalOvers() 
								+ currentMatch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
							past_tournament_stat_clone.get(playerId).setMatches(past_tournament_stat_clone.get(playerId).getMatches() + 1);
						}
						Collections.sort(past_tournament_stat_clone.get(playerId).getBatsman_best_Stats(),new CricketFunctions.BatsmanBestStatsComparator());
					}else {
						past_tournament_stat_clone.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0,0, 0, 0, 0, 0, 
								bc.getBalls(),0,0,0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>(), new ArrayList<BestStats>()));
						if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
								setThirty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThirty() + 1);
						}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
								setFifty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFifty() + 1);
						}else if(bc.getRuns()>= 100) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
								setHundreds(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getHundreds() + 1);
						}
						
						if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setInnings(
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getInnings()+1);
						}
						
						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setNot_out(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getNot_out() + 1);
							
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
								(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
								currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
						}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
								(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
								currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
						}
						else {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
								(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
								currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
						}
						if(currentMatch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getMatch().getInning().get(0).getTotalOvers() 
							+ currentMatch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setMatches(
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getMatches() + 1);
						}
						Collections.sort(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats(),new CricketFunctions.BatsmanBestStatsComparator());
					}
				}
			}
			return past_tournament_stat_clone;
		}
		
		return null;
	}

//	public static List<Tournament> extractTournamentData(String typeOfExtraction,boolean ShowStrikeRate, List<HeadToHead> headToHead_matches, 
//			CricketService cricketService,MatchAllData currentMatch, List<Tournament> past_tournament_stat) throws JsonMappingException, JsonProcessingException{
//		
//		int playerId = -1;
//		List<Tournament> tournament_stats = new ArrayList<Tournament>();
//		//tournament_stats.clear();
//		boolean has_match_started = false,is_player_found = false;
//		
//		switch(typeOfExtraction) {
//		case "COMBINED_PAST_CURRENT_MATCH_DATA":
//			 return extractTournamentData("CURRENT_MATCH_DATA",ShowStrikeRate, headToHead_matches, cricketService, currentMatch, 
//					 extractTournamentData("PAST_MATCHES_DATA",ShowStrikeRate, headToHead_matches, cricketService, currentMatch, null));
//			 
//		case "PAST_MATCHES_DATA":
//			
//			for(HeadToHead mtch : headToHead_matches) {
//				
//				if(!mtch.getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
//					
//					playerId = -1;
//					for(int i=0; i<=tournament_stats.size() - 1;i++)
//					{
//						if(mtch.getPlayerId() == tournament_stats.get(i).getPlayerId()) {
//							playerId = i;
//							break;
//						}
//					}
//					
//					if(playerId >= 0) {
//						
//						tournament_stats.get(playerId).setRuns(tournament_stats.get(playerId).getRuns() + mtch.getRuns());
//						tournament_stats.get(playerId).setBallsFaced(tournament_stats.get(playerId).getBallsFaced() + mtch.getBallsFaced());
//						tournament_stats.get(playerId).setFours(tournament_stats.get(playerId).getFours() + mtch.getFours());
//						tournament_stats.get(playerId).setSixes(tournament_stats.get(playerId).getSixes() + mtch.getSixes());
//						
//						
//						tournament_stats.get(playerId).setMatches(tournament_stats.get(playerId).getMatches() + 1);
//						
//						if(mtch.getRuns()>= 30 && mtch.getRuns() < 50) {
//							tournament_stats.get(playerId).setThirty(tournament_stats.get(playerId).getThirty() + 1);
//						}else if(mtch.getRuns()>= 50 && mtch.getRuns() < 100) {
//							tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + 1);
//						}else if(mtch.getRuns() >= 100) {
//							tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + 1);
//						}
//						
//						if(mtch.getInningStarted().trim().contains("Y")) {
//							tournament_stats.get(playerId).setInnings(tournament_stats.get(playerId).getInnings()+1);
//						}
//						
//						if(mtch.getDismissed().trim().contains("N")) {
//							tournament_stats.get(tournament_stats.size() - 1).setNot_out(tournament_stats.get(tournament_stats.size() - 1).getNot_out() + 1);
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), ((mtch.getRuns() * 2) + 1), 
//									mtch.getBallsFaced(),mtch.getOpponentTeam(), null,mtch.getMatchFileName().replace(".json", ""), 
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.NOT_OUT));
//						}else if(mtch.getDismissed().trim().contains("Y")) {
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
//									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.OUT));
//						}
//						else {
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
//									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.STILL_TO_BAT));
//						}
//						
//						tournament_stats.get(playerId).setWickets(tournament_stats.get(playerId).getWickets() + mtch.getWickets());
//						tournament_stats.get(playerId).setRunsConceded(tournament_stats.get(playerId).getRunsConceded() + mtch.getRunsConceded());
//						tournament_stats.get(playerId).setDots(tournament_stats.get(playerId).getDots() + mtch.getBalldots());
//						tournament_stats.get(playerId).setBallsBowled(tournament_stats.get(playerId).getBallsBowled() + mtch.getBallsBowled());
//						
//						if(mtch.getWickets() >= 3 && mtch.getWickets() < 5) {
//							tournament_stats.get(playerId).setThreeWicketHaul(tournament_stats.get(playerId).getThreeWicketHaul() + 1);
//						}else if(mtch.getWickets() >= 5) {
//							tournament_stats.get(playerId).setFiveWicketHaul(tournament_stats.get(playerId).getFiveWicketHaul() + 1);
//						}
//						
//						tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(mtch.getPlayerId(), 
//								(1000 * mtch.getWickets()) - mtch.getRunsConceded(), mtch.getBallsBowled(), mtch.getOpponentTeam(), null, 
//								mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
//						
//					}
//					else {
//						tournament_stats.add(new Tournament(mtch.getPlayerId(), mtch.getRuns(), mtch.getFours(), mtch.getSixes(), 0, 0, 0, 0, 0, 
//								mtch.getWickets(), mtch.getRunsConceded(), mtch.getBallsBowled(), mtch.getBallsFaced(), mtch.getBalldots(), 
//								0,0,"",0,0,0,0, cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())), 
//								new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//						
//						tournament_stats.get(tournament_stats.size() - 1).setMatches(tournament_stats.get(tournament_stats.size() - 1).getMatches() + 1);
//						
//						if(mtch.getRuns() >= 30 && mtch.getRuns() < 50) {
//							tournament_stats.get(tournament_stats.size() - 1).setThirty(tournament_stats.get(tournament_stats.size() - 1).getThirty() + 1);
//						}else if(mtch.getRuns()>= 50 && mtch.getRuns() < 100) {
//							tournament_stats.get(tournament_stats.size() - 1).setFifty(tournament_stats.get(tournament_stats.size() - 1).getFifty() + 1);
//						}else if(mtch.getRuns() >= 100) {
//							tournament_stats.get(tournament_stats.size() - 1).setHundreds(tournament_stats.get(tournament_stats.size() - 1).getHundreds() + 1);
//						}
//						
//						if(mtch.getInningStarted().trim().contains("Y")) {
//							tournament_stats.get(tournament_stats.size() - 1).setInnings(tournament_stats.get(tournament_stats.size() - 1).getInnings()+1);
//						}
//						
//						if(mtch.getDismissed().trim().contains("N")) {
//							tournament_stats.get(tournament_stats.size() - 1).setNot_out(tournament_stats.get(tournament_stats.size() - 1).getNot_out() + 1);
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), ((mtch.getRuns() * 2) + 1), 
//									mtch.getBallsFaced(),mtch.getOpponentTeam(), null,mtch.getMatchFileName().replace(".json", ""), 
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.NOT_OUT));
//						}
//						else if(mtch.getDismissed().trim().contains("Y")) {
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
//									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.OUT));
//						}
//						else {
//							tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(mtch.getPlayerId(), (mtch.getRuns() * 2), 
//									mtch.getBallsFaced(), mtch.getOpponentTeam(), null, mtch.getMatchFileName().replace(".json", ""),
//									cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),CricketUtil.STILL_TO_BAT));
//						}
//						
//						if(mtch.getWickets() >= 3 && mtch.getWickets() < 5) {
//							tournament_stats.get(tournament_stats.size() - 1).setThreeWicketHaul(
//									tournament_stats.get(tournament_stats.size() - 1).getThreeWicketHaul() + 1);
//						}else if(mtch.getWickets() >= 5) {
//							tournament_stats.get(tournament_stats.size() - 1).setFiveWicketHaul(
//									tournament_stats.get(tournament_stats.size() - 1).getFiveWicketHaul() + 1);
//						}
//						
//						tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(mtch.getPlayerId(), 
//								((1000 * mtch.getWickets()) - mtch.getRunsConceded()), mtch.getBallsBowled(), mtch.getOpponentTeam(), null, 
//								mtch.getMatchFileName().replace(".json", ""), cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(mtch.getPlayerId())),""));
//					}
//				}
//			}
//			return tournament_stats;
//		case "CURRENT_MATCH_DATA":
//			
//			List<Tournament> past_tournament_stat_clone = new ArrayList<Tournament>();
////			past_tournament_stat_clone.clear();
//			
////			for(Tournament tourn : past_tournament_stat) {
////				past_tournament_stat_clone.add(objectMapper.readValue(objectMapper.writeValueAsString(tourn), Tournament.class));
////			}
////			
////			for(Tournament pas : past_tournament_stat_clone) {
////				System.out.println("before = " + pas.getPlayer().getFull_name() + "   Runs = " + pas.getRuns());
////			}
//			
//			past_tournament_stat_clone = past_tournament_stat.stream().map(tourn_stats -> {
//				try {
//					return tourn_stats.clone();
//				} catch (CloneNotSupportedException e) {
//					e.printStackTrace();
//				}
//				return tourn_stats;
//			}).collect(Collectors.toList());
//			
//			//past_tournament_stat_clone.clear();
//			
//			has_match_started = false;
//			is_player_found = false;
//			
//			if(currentMatch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * currentMatch.getMatch().getInning().get(0).getTotalOvers() 
//					+ currentMatch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
//				has_match_started = true;
//			}
//			
//			for(Inning inn : currentMatch.getMatch().getInning())
//			{
//				if(inn.getTotalRuns() > 0 || (6 * inn.getTotalOvers() + inn.getTotalBalls()) > 0 || inn.getInningStatus().equalsIgnoreCase(CricketUtil.START) 
//						|| inn.getInningStatus().equalsIgnoreCase(CricketUtil.PAUSE)) {
//					has_match_started = true;
//				}
//				
//				if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
//					for(BowlingCard boc : inn.getBowlingCard())
//					{
//						playerId = -1;
//						for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
//						{
//							if(boc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayer().getPlayerId()) {
//								playerId = i;
//								break;
//							}
//						}
//						if(playerId >= 0) {
//							past_tournament_stat_clone.get(playerId).setRunsConceded(past_tournament_stat_clone.get(playerId).getRunsConceded() + boc.getRuns()); // existing record
//							past_tournament_stat_clone.get(playerId).setWickets(past_tournament_stat_clone.get(playerId).getWickets() + boc.getWickets());
//							past_tournament_stat_clone.get(playerId).setDots(past_tournament_stat_clone.get(playerId).getDots() + boc.getDots());
//							past_tournament_stat_clone.get(playerId).setBallsBowled(past_tournament_stat_clone.get(playerId).getBallsBowled() + 
//									6 * boc.getOvers() + boc.getBalls());
//							
//							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
//								past_tournament_stat_clone.get(playerId).setThreeWicketHaul(past_tournament_stat_clone.get(playerId).getThreeWicketHaul() + 1);
//							}else if(boc.getWickets() >= 5) {
//								past_tournament_stat_clone.get(playerId).setFiveWicketHaul(past_tournament_stat_clone.get(playerId).getFiveWicketHaul() + 1);
//							}
//							
//							past_tournament_stat_clone.get(playerId).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
//									(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
//									currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
//
//						}else {
//							past_tournament_stat_clone.add(new Tournament(boc.getPlayerId(), 0, 0,0, 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
//									boc.getDots(),0,0,null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//							
//							if(boc.getWickets() >= 3 && boc.getWickets() < 5) {
//								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setThreeWicketHaul(
//										past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThreeWicketHaul() + 1);
//							}else if(boc.getWickets() >= 5) {
//								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).setFiveWicketHaul(
//										past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFiveWicketHaul() + 1);
//							}
//							
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
//									(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
//									currentMatch.getSetup().getGround(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
//							
//						}
//					}
//				}
//				
//				for(BattingCard bc : inn.getBattingCard())
//				{
//					playerId = -1;
//					for(int i=0; i<=past_tournament_stat_clone.size() - 1;i++)
//					{
//						if(bc.getPlayerId() == past_tournament_stat_clone.get(i).getPlayerId()) {
//							playerId = i;
//							break;
//						}
//					}
//					
//					if(playerId >= 0) {
//						past_tournament_stat_clone.get(playerId).setRuns(past_tournament_stat_clone.get(playerId).getRuns() + bc.getRuns()); // existing record
//						past_tournament_stat_clone.get(playerId).setBallsFaced(past_tournament_stat_clone.get(playerId).getBallsFaced() + bc.getBalls());
//						past_tournament_stat_clone.get(playerId).setFours(past_tournament_stat_clone.get(playerId).getFours() + bc.getFours());
//						past_tournament_stat_clone.get(playerId).setSixes(past_tournament_stat_clone.get(playerId).getSixes() + bc.getSixes());
//						
//						if(bc.getRuns() >= 30 && bc.getRuns() < 50) {
//							past_tournament_stat_clone.get(playerId).setThirty(past_tournament_stat_clone.get(playerId).getThirty() + 1);	
//						}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
//							past_tournament_stat_clone.get(playerId).setFifty(past_tournament_stat_clone.get(playerId).getFifty() + 1);
//						}else if(bc.getRuns()>= 100) {
//							past_tournament_stat_clone.get(playerId).setHundreds(past_tournament_stat_clone.get(playerId).getHundreds() + 1);
//						}
//						
//						if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
//							past_tournament_stat_clone.get(playerId).setInnings(past_tournament_stat_clone.get(playerId).getInnings()+1);
//						}
//						
//						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//							past_tournament_stat_clone.get(playerId).setNot_out(past_tournament_stat_clone.get(playerId).getNot_out() + 1);
//							
//							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
//									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
//							
//						}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
//							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
//									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(), 
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
//							
//						}
//						else {
//							past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
//									bc.getBalls(), inn.getBowling_team(), currentMatch.getSetup().getGround(),
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
//						}
//						
//					}else {
//						past_tournament_stat_clone.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0, 0,0, 0, 0, 0, 0, 
//								bc.getBalls(),0,0,0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>()));
//						if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
//								setThirty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getThirty() + 1);
//						}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
//								setFifty(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getFifty() + 1);
//						}else if(bc.getRuns()>= 100) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).
//								setHundreds(past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getHundreds() + 1);
//						}
//						
//						if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setInnings(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getInnings()+1);
//						}
//						
//						if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).setNot_out(past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getNot_out() + 1);
//							
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
//									(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
//						}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
//									(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
//						}
//						else {
//							past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
//									(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getSetup().getGround(),
//									currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
//						}
//					}	
//				}
//			}
//			
//			if(has_match_started == true) {
//				for(Tournament trment : past_tournament_stat_clone) {
//					is_player_found = false;
//					for(Inning inn : currentMatch.getMatch().getInning())
//					{
//						if(is_player_found == false) {
//							for(BattingCard bc : inn.getBattingCard())
//							{
//								if(bc.getPlayerId() == trment.getPlayerId()) {
//									trment.setMatches(trment.getMatches() + 1);
//									
//									is_player_found = true;
//								}
//							}
//						}
//						
//						if(is_player_found == false) {
//							if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0 ) {
//								for(BowlingCard boc : inn.getBowlingCard())
//								{
//									if(boc.getPlayerId() == trment.getPlayerId()) {
//										trment.setMatches(trment.getMatches() + 1);
//										is_player_found = true;
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//			for(Tournament pas : past_tournament_stat_clone) {
//				System.out.println("after = " + pas.getPlayer().getFull_name() + "   Runs = " + pas.getRuns());
//			}
//			return past_tournament_stat_clone;
//		}
//		
//		return null;
//		
//	}
	
	public static String GenerateMatchResult(MatchAllData match, String teamNameType, String broadcaster, 
		String splitResultTxt, boolean ballsRemaining)
	{
		String resultToShow = "", opponentTeamName = "";
		
		if(match.getMatch().getMatchResult() != null) {
			if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
					|| match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
				if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
						&& !match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
					switch (broadcaster) {
					case "ICC-U19-2023": case "T20_MUMBAI":
						if(splitResultTxt.isEmpty()) {
							resultToShow = CricketUtil.MATCH.toLowerCase() + " " + CricketUtil.TIED.toLowerCase();
						} else {
							resultToShow = CricketUtil.MATCH.toLowerCase() + splitResultTxt + CricketUtil.TIED.toLowerCase();
						}
						break;
					default:
						resultToShow = CricketUtil.MATCH.toLowerCase() + " " + CricketUtil.TIED.toLowerCase();
						break;
					}
				} else {
					resultToShow = CricketUtil.MATCH.toLowerCase();
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
						switch (broadcaster) {
						case "ICC-U19-2023": case "T20_MUMBAI":
							if(splitResultTxt.isEmpty()) {
								resultToShow = resultToShow + " " + CricketUtil.DRAWN.toLowerCase();
							} else {
								resultToShow = resultToShow + splitResultTxt + CricketUtil.DRAWN.toLowerCase();
							}
							break;
						default:
							resultToShow = resultToShow + " " + CricketUtil.DRAWN.toLowerCase();
							break;
						}
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
						switch (broadcaster) {
						case "ICC-U19-2023": case "T20_MUMBAI":
							if(splitResultTxt.isEmpty()) {
								resultToShow = resultToShow + " " + CricketUtil.ABANDONED.toLowerCase();
							} else {
								resultToShow = resultToShow + splitResultTxt + CricketUtil.ABANDONED.toLowerCase();
							}
							break;
						default:
							resultToShow = resultToShow + " " + CricketUtil.ABANDONED.toLowerCase();
							break;
						}
					}
				}
			} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
				resultToShow = CricketUtil.NO_RESULT.toLowerCase().replace("_", " ");
			} else {
				if(match.getMatch().getMatchResult().contains(",")) {
					switch (teamNameType) {
					case CricketUtil.SHORT:
						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
							resultToShow = match.getSetup().getHomeTeam().getTeamName4();
							opponentTeamName = match.getSetup().getAwayTeam().getTeamName4();
						} else {
							resultToShow = match.getSetup().getAwayTeam().getTeamName4();
							opponentTeamName = match.getSetup().getHomeTeam().getTeamName4();
						}
					    break;
					default:
						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
							resultToShow = match.getSetup().getHomeTeam().getTeamName1();
							opponentTeamName = match.getSetup().getAwayTeam().getTeamName1();
						} else {
							resultToShow = match.getSetup().getAwayTeam().getTeamName1();
							opponentTeamName = match.getSetup().getHomeTeam().getTeamName1();
						}
					    break;
					}
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
						resultToShow = resultToShow + " win the super over";
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING + "_LEAD")) {
						resultToShow = resultToShow + " win on first inning lead";
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
						&& match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = resultToShow + " win by an inning and " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
							+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						switch (broadcaster) {
						case "ICC-U19-2023": case "T20_MUMBAI":
							if(splitResultTxt.isEmpty()) {
								resultToShow = resultToShow + " beat " + opponentTeamName + " by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
									+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							} else {
								resultToShow = resultToShow + " beat " + opponentTeamName + splitResultTxt + "by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
									+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							}
							break;
						default:
							resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
								+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							break;
						}
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
						
						switch (broadcaster) {
						case "ICC-U19-2023": case "T20_MUMBAI":
							if(splitResultTxt.isEmpty()) {
								resultToShow = resultToShow + " beat " + opponentTeamName + " by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
									+ " wicket" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							} else {
								resultToShow = resultToShow + " beat " + opponentTeamName + splitResultTxt + "by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
									+ " wicket" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							}
							break;
						default:
							resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
								+ " wicket" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
							if(ballsRemaining) {
								TargetData targetData = GetTargetData(match);
								if(targetData.getRemaningBall() > 0) {
									if(targetData.getRemaningBall() > 120) {
										resultToShow = resultToShow + " with " + CricketFunctions.OverBalls(0, targetData.getRemaningBall()) 
											+ " overs remaining";
									} else {
										resultToShow = resultToShow + " with " + targetData.getRemaningBall() 
											+ " ball" + CricketFunctions.Plural(targetData.getRemaningBall()) + " remaining";
									}
								}
							}
							break;
						}
					}
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DLS)) {
						resultToShow = resultToShow + " (" + CricketUtil.DLS + ")";
					}else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.VJD)) {
						resultToShow = resultToShow + " (" + CricketUtil.VJD + ")";
					}
				}
			}
		}
		return resultToShow;
	}
//	public static String generateMatchResult(MatchAllData match, String teamNameType)
//	{
//		String resultToShow = "";
//		if(match.getMatch().getMatchResult() != null) {
//			if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
//					|| match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
//				if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
//						&& !match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
//					resultToShow = CricketUtil.MATCH.toLowerCase() + " " + CricketUtil.TIED.toLowerCase();
//				} else {
//					resultToShow = CricketUtil.MATCH.toLowerCase();
//					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
//						resultToShow = resultToShow + " " + CricketUtil.DRAWN.toLowerCase();
//					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
//						resultToShow = resultToShow + " " + CricketUtil.ABANDONED.toLowerCase();
//					}
//				}
//			} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
//				resultToShow = CricketUtil.NO_RESULT.toLowerCase().replace("_", " ");
//			} else {
//				if(match.getMatch().getMatchResult().contains(",")) {
//					switch (teamNameType) {
//					case CricketUtil.SHORT:
//						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
//							resultToShow = match.getSetup().getHomeTeam().getTeamName4();
//						} else {
//							resultToShow = match.getSetup().getAwayTeam().getTeamName4();
//						}
//					    break;
//					default:
//						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
//							resultToShow = match.getSetup().getHomeTeam().getTeamName1();
//						} else {
//							resultToShow = match.getSetup().getAwayTeam().getTeamName1();
//						}
//					    break;
//					}
//					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
//						resultToShow = resultToShow + " win by super over";
//					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING + "_LEAD")) {
//						resultToShow = resultToShow + " win on first inning lead";
//					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
//							&& match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
//						resultToShow = resultToShow + " win by an inning and " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
//							+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
//					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
//						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
//							+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
//					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
//						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
//							+ " wicket" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
//					}
//					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DLS)) {
//						resultToShow = resultToShow + " (" + CricketUtil.DLS + ")";
//					}else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.VJD)) {
//						resultToShow = resultToShow + " (" + CricketUtil.VJD + ")";
//					}
//				}
//			}
//		}
//		return resultToShow;
//	}
	
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
	
	public static class BatsmenRunComparator implements Comparator<BattingCard> {
	    @Override
	    public int compare(BattingCard bc1, BattingCard bc2) {
	    	if(bc2.getRuns() == bc1.getRuns()) {
	    		if(bc2.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT) || bc1.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
	    			return Integer.compare(bc2.getBatsmanScoreSortData(), bc1.getBatsmanScoreSortData());
	    		}
	    		if(bc1.getBalls() == bc2.getBalls()) {
	    			return Integer.compare(bc2.getFours(), bc1.getFours());
	    		}
	    		return Integer.compare(bc1.getBalls(), bc2.getBalls());
	    	}
	    	return Integer.compare(bc2.getRuns(), bc1.getRuns());
	    }
	}

	public static class BowlerFiguresComparator implements Comparator<BowlingCard> {
	    @Override
	    public int compare(BowlingCard bc1, BowlingCard bc2) {
	    	if(bc1.getWickets() == bc2.getWickets()) {
	    		return Float.compare(Float.valueOf(CricketFunctions.getEconomy(bc1.getRuns(), (bc1.getOvers()*6) + (bc1.getBalls()), 2, "0")), 
	    				Float.valueOf(CricketFunctions.getEconomy(bc2.getRuns(), (bc2.getOvers()*6) + (bc2.getBalls()), 2, "0")));
			}else {
				return Integer.compare(bc2.getBowlerFigureSortData(), bc1.getBowlerFigureSortData());
			}
	    }
	}
	
	public static class BowlerEconomyComparator implements Comparator<BowlingCard> {

		@Override
		public int compare(BowlingCard boc1, BowlingCard boc2) {
			return Float.compare(Float.valueOf(CricketFunctions.getEconomy(boc1.getRuns(), (boc1.getOvers()*6) + (boc1.getBalls()), 2, "0")), 
					Float.valueOf(CricketFunctions.getEconomy(boc2.getRuns(), (boc2.getOvers()*6) + (boc2.getBalls()), 2, "0")));
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
	    	if(bc2.getBatsmanFoursSortData() == bc1.getBatsmanFoursSortData()) {
	    		//return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    		return Integer.compare(bc2.getRuns(), bc1.getRuns());
	    	}else {
	    		return Integer.compare(bc2.getBatsmanFoursSortData(), bc1.getBatsmanFoursSortData());
	    	}
	    }
	}
	
	public static class BatsmanSixesComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	    	if(bc2.getBatsmanSixesSortData() == bc1.getBatsmanSixesSortData()) {
	    		//return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    		return Integer.compare(bc2.getRuns(), bc1.getRuns());
	    	}else {
	    		return Integer.compare(bc2.getBatsmanSixesSortData(), bc1.getBatsmanSixesSortData());
	    	}
	    }
	}
	
	public static class BestBatsmanStrikeRateComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	    	return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    }
	}
	public static class BestBowlerEconomyComparator implements Comparator<Tournament> {

		@Override
		public int compare(Tournament boc1, Tournament boc2) {
			return Float.compare(Float.valueOf(CricketFunctions.getEconomy(boc1.getRunsConceded(), boc1.getBallsBowled(), 2, "0")), 
					Float.valueOf(CricketFunctions.getEconomy(boc2.getRunsConceded(), boc2.getBallsBowled(), 2, "0")));
		}
		
	}
	
	public static class TopBatsmenBestStatsComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament tourn1, Tournament tourn2) {
	    	if(tourn1.getBatsman_best_Stats().get(0).getBestEquation() == tourn1.getBatsman_best_Stats().get(0).getBestEquation()) {
	    		return Integer.compare(tourn1.getBatsman_best_Stats().get(0).getBatsmanStrikeRateSortData(), tourn1.getBatsman_best_Stats().get(0).getBatsmanStrikeRateSortData());
	    	}else {
	    		return Integer.compare(tourn1.getBatsman_best_Stats().get(0).getBestEquation(), tourn1.getBatsman_best_Stats().get(0).getBestEquation());
	    	}
	    }
	}
	public static class TopBowlerBestStatsComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament tourn1, Tournament tourn2) {
	    	if(tourn1.getBowler_best_Stats().get(0).getBestEquation() == tourn1.getBowler_best_Stats().get(0).getBestEquation()) {
	    		return Integer.compare(tourn1.getBowler_best_Stats().get(0).getBalls(), tourn1.getBowler_best_Stats().get(0).getBalls());
	    	}else {
	    		return Integer.compare(tourn1.getBowler_best_Stats().get(0).getBestEquation(), tourn1.getBowler_best_Stats().get(0).getBestEquation());
	    	}
	    }
	}
	
	public static class BatsmanBestStatsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats bs1, BestStats bs2) {
	    	if(bs2.getBestEquation() == bs1.getBestEquation()) {
	    		return Integer.compare(bs2.getBatsmanStrikeRateSortData(), bs1.getBatsmanStrikeRateSortData());
	    	}else {
	    		return Integer.compare(bs2.getBestEquation(), bs1.getBestEquation());
	    	}
	    }
	}
	public static class BowlerBestStatsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats bs1, BestStats bs2) {
	    	if(bs2.getBestEquation() == bs1.getBestEquation()) {
	    		return Integer.compare(bs1.getBalls(), bs2.getBalls());
	    	}else {
	    		return Integer.compare(bs2.getBestEquation(), bs1.getBestEquation());
	    	}
	    }
	}
	
	public static class TapeBowlerWicketsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats bc1, BestStats bc2) {
	    	if(bc2.getWickets() == bc1.getWickets()) {
	    		return Integer.compare(bc1.getRuns(), bc2.getRuns());
	    	}else {
	    		return Integer.compare(bc2.getWickets(), bc1.getWickets());
	    	}
	    }
	}
	
	public static class LogFiftyWicketsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats boc1, BestStats boc2) {
	    	if(boc2.getWickets() == boc1.getWickets()) {
	    		return Integer.compare(boc1.getRuns(), boc2.getRuns());
	    	}else {
	    		return Integer.compare(boc2.getWickets(), boc1.getWickets());
	    	}
	    }
	}
	public static class LogFiftyRunsComparator implements Comparator<BestStats> {
	    @Override
	    public int compare(BestStats bc1, BestStats bc2) {
	    	return Integer.compare(bc2.getChallengeRuns(), bc1.getChallengeRuns());
	    }
	}
	
	public static Player populatePlayer(CricketService cricketService, Player player, MatchAllData match)
	{
		Player this_plyr = new Player();
		this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(player.getPlayerId()));
		if(this_plyr != null) {
			this_plyr.setPlayerPosition(player.getPlayerPosition()); 
			this_plyr.setCaptainWicketKeeper(player.getCaptainWicketKeeper());
//			this_plyr.setImpactPlayer(player.getImpactPlayer()); 
//			if(match.getSetup().getReadPhotoColumn() != null && match.getSetup().getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
//				this_plyr.setPhoto("");
//			}
		}
		return this_plyr;
	}
	
	public static MatchAllData populateMatchVariables(CricketService cricketService, MatchAllData match) 
			throws IllegalAccessException, InvocationTargetException 
	{
		List<Player> players = new ArrayList<Player>();
		
		for(Player plyr:match.getSetup().getHomeSquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.getSetup().setHomeSquad(players);

		if(match.getSetup().getHomeSubstitutes() != null) {
			players = new ArrayList<Player>();
			for(Player plyr:match.getSetup().getHomeSubstitutes()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
			match.getSetup().setHomeSubstitutes(players);
			
			players = new ArrayList<Player>();
			if(match.getSetup().getHomeOtherSquad() != null) {
				for(Player plyr:match.getSetup().getHomeOtherSquad()) {
					players.add(populatePlayer(cricketService, plyr, match));
				}
			}
			match.getSetup().setHomeOtherSquad(players);
		}
		
		if(match.getSetup().getAwaySubstitutes() != null) {
			players = new ArrayList<Player>();
			for(Player plyr:match.getSetup().getAwaySubstitutes()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
			match.getSetup().setAwaySubstitutes(players);
			
			players = new ArrayList<Player>();
			if(match.getSetup().getAwayOtherSquad() != null) {
				for(Player plyr:match.getSetup().getAwayOtherSquad()) {
					players.add(populatePlayer(cricketService, plyr, match));
				}
			}
			match.getSetup().setAwayOtherSquad(players);
		}
		
		players = new ArrayList<Player>();
		for(Player plyr:match.getSetup().getAwaySquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.getSetup().setAwaySquad(players);

		if(match.getSetup().getHomeTeamId() > 0)
			match.getSetup().setHomeTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getSetup().getHomeTeamId())));
		if(match.getSetup().getAwayTeamId() > 0)
			match.getSetup().setAwayTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getSetup().getAwayTeamId())));
		if(match.getSetup().getGroundId() > 0) {
			match.getSetup().setGround(cricketService.getGround(match.getSetup().getGroundId()));
			if(match.getSetup().getGround() != null) {
				match.getSetup().setVenueName(match.getSetup().getGround().getFullname());
			}
		}
		
		if(match.getMatch().getInning() != null) {
			for(Inning inn : match.getMatch().getInning()) {
				
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
	
				if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
					inn.setFielders(match.getSetup().getHomeSquad());
				} else if(inn.getBowlingTeamId() == match.getSetup().getAwayTeamId()) {
					inn.setFielders(match.getSetup().getAwaySquad());
				}
	
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
					if(bc.getHowOutFielderId() < 0) {
						bc.setHowOutText("c substitute");
						bc.setHowOutPartOne("c substitute");
					} else {
						bc.setHowOutText("c " + bc.getHowOutFielder().getTicker_name());
						bc.setHowOutPartOne("c " + bc.getHowOutFielder().getTicker_name());
						if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
							bc.setHowOutText(bc.getHowOutText() + " (SUB)");
							bc.setHowOutPartOne(bc.getHowOutPartOne() + " (SUB)");
						}
					}
					bc.setHowOutText(bc.getHowOutText() + " b " + bc.getHowOutBowler().getTicker_name());
					bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
					break;
				case CricketUtil.RUN_OUT:
					bc.setHowOutPartOne("run out");
					if(bc.getHowOutFielderId() < 0) {
						bc.setHowOutText("run out substitute");
						bc.setHowOutPartTwo("substitute");
					} else {
						bc.setHowOutText("run out (" + bc.getHowOutFielder().getTicker_name() + ")");
						bc.setHowOutPartTwo(bc.getHowOutFielder().getTicker_name());
						if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
							bc.setHowOutText(bc.getHowOutText() + " (SUB)");
							bc.setHowOutPartTwo(bc.getHowOutPartTwo() + " (SUB)");
						}
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
//				if(bc.getHowOutFielderId() < 0) {
//					bc.setHowOutText("st substitute b " + bc.getHowOutBowler().getTicker_name());
//					bc.setHowOutPartOne("st substitute");
//				} else {
//					bc.setHowOutText("st " + bc.getHowOutFielder().getTicker_name() + " b " + bc.getHowOutBowler().getTicker_name());
//					bc.setHowOutPartOne("st " + bc.getHowOutFielder().getTicker_name());
//				}
//				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
				if(bc.getHowOutFielderId() < 0) {
					bc.setHowOutText("st substitute");
					bc.setHowOutPartOne("st substitute");
				} else {
					bc.setHowOutText("st " + bc.getHowOutFielder().getTicker_name());
					bc.setHowOutPartOne("st " + bc.getHowOutFielder().getTicker_name());
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						bc.setHowOutText(bc.getHowOutText() + " (SUB)");
						bc.setHowOutPartOne(bc.getHowOutPartOne() + " (SUB)");
					}
				}
				bc.setHowOutText(bc.getHowOutText() + " b " + bc.getHowOutBowler().getTicker_name());
				bc.setHowOutPartTwo("b " + bc.getHowOutBowler().getTicker_name());
				break;
			case CricketUtil.LBW:
				System.out.println(bc.getPlayerId());
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
	public static String processHowOutText(String whatToProcess, BattingCard bc)
	{
		if(bc.getStatus() != null && bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
			switch (whatToProcess) {
			case "FOUR-PART-HOW-OUT":
				switch (bc.getHowOut().toUpperCase()) {
				case CricketUtil.CAUGHT_AND_BOWLED:
					return " | |c & b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.CAUGHT: case CricketUtil.MANKAD: case CricketUtil.RUN_OUT:
					switch (bc.getHowOut().toUpperCase()) {
					case CricketUtil.CAUGHT: 
						if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
							if(bc.getHowOutFielderId() <= 0) {
								return "c|" +  "substitute|b|" + bc.getHowOutBowler().getTicker_name();
							} else {
								return "c|" +  "(sub - " + bc.getHowOutFielder().getTicker_name()+")|b|" + bc.getHowOutBowler().getTicker_name();
							}
						} else {
							if(bc.getHowOutFielderId() <= 0) {
								return "c|substitute|b|" + bc.getHowOutBowler().getTicker_name();
							} else {
								return "c|" + bc.getHowOutFielder().getTicker_name() + "|b|" + bc.getHowOutBowler().getTicker_name();
							}
						}
					case CricketUtil.RUN_OUT:
						if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
							if(bc.getHowOutFielderId() <= 0) {
								return "run out|(substitute)| | ";
							} else {
								return "run out|" + "(sub - " + bc.getHowOutFielder().getTicker_name() + ")| | ";
							}
						} else {
							if(bc.getHowOutFielderId() <= 0) {
								return "run out|(substitute)| | ";
							} else {
								return "run out|(" + bc.getHowOutFielder().getTicker_name() + ")| | ";
							}
						}
					case CricketUtil.MANKAD:
						return "run out|(" + bc.getHowOutBowler().getTicker_name() + ")| | ";
					}
					break;
				case CricketUtil.BOWLED:
					return "||b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.STUMPED:
					if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
						if(bc.getHowOutFielderId() <= 0) {
							return "st|substitute|b|" + bc.getHowOutBowler().getTicker_name();
						} else {
							return "st|" +  "(sub - " + bc.getHowOutFielder().getTicker_name()+")|b|" + bc.getHowOutBowler().getTicker_name();
						}
					} else {
						if(bc.getHowOutFielderId() <= 0) {
							return "st|substitute|b|" + bc.getHowOutBowler().getTicker_name();
						} else {
							return "st|" + bc.getHowOutFielder().getTicker_name() + "|b|" + bc.getHowOutBowler().getTicker_name();
						}
					}
				case CricketUtil.LBW:
					return "lbw||b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.HIT_WICKET:
					return "hit wicket| |b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.HANDLED_THE_BALL:
					return "handled the ball| | | ";
				case CricketUtil.HIT_BALL_TWICE:
					return "hit the ball twice| | | ";
				case CricketUtil.OBSTRUCTING_FIELDER:
					return "obstructing the field| | | ";
				case CricketUtil.TIMED_OUT:
					return "timed out| | | ";
				case CricketUtil.RETIRED_HURT:
					return "retired hurt| | | ";
				case CricketUtil.RETIRED_OUT:
					return "retired out| | | ";
				case CricketUtil.ABSENT_HURT:
					return "absent hurt| | | ";
				case CricketUtil.CONCUSSED:
					return "concussed| | | ";
				}
				break;
			}
		}
		return null;
	}	
	public static BattingCard processWebBattingcard(CricketService cricketService,BattingCard bc,Archive archive)
	{
		Player this_player1 = new Player();
		
		//bc.setPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getPlayerId())));
		for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
			if(hs.getFull_name().equalsIgnoreCase(bc.getPlayer().getFull_name())) {
				this_player1.setFull_name(hs.getFull_name());
				this_player1.setFirstname(hs.getFirstname());
				this_player1.setSurname(hs.getSurname());
				this_player1.setTicker_name(hs.getTicker_name());
				this_player1.setPlayerId(hs.getPlayerId());
				bc.setPlayer(this_player1);
			}
			
		}
		
		for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
			if(as.getFull_name().equalsIgnoreCase(bc.getPlayer().getFull_name())) {
				this_player1.setFull_name(as.getFull_name());
				this_player1.setFirstname(as.getFirstname());
				this_player1.setSurname(as.getSurname());
				this_player1.setTicker_name(as.getTicker_name());
				this_player1.setPlayerId(as.getPlayerId());
				bc.setPlayer(this_player1);
			}
			
		}
		//System.out.println("player = " + bc.getPlayer().getTicker_name());
		if (bc.getConcussionPlayerId() > 0) {
			bc.setConcussion_player(cricketService.getPlayer(CricketUtil.PLAYER, 
				String.valueOf(bc.getConcussionPlayerId())));
		}
		Player this_player = new Player();
		if(bc.getStatus() != null && bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {

			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT_AND_BOWLED: case CricketUtil.CAUGHT: case CricketUtil.BOWLED: 
			case CricketUtil.STUMPED: case CricketUtil.LBW: case CricketUtil.HIT_WICKET: case CricketUtil.MANKAD:
				for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							this_player.setFull_name(hs.getFull_name());
							this_player.setFirstname(hs.getFirstname());
							this_player.setSurname(hs.getSurname());
							this_player.setTicker_name(hs.getTicker_name());
							this_player.setPlayerId(hs.getPlayerId());
							bc.setHowOutBowler(this_player);
						}
					}else {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							this_player.setFull_name(hs.getFull_name());
							this_player.setFirstname(hs.getFirstname());
							this_player.setSurname(hs.getSurname());
							this_player.setTicker_name(hs.getTicker_name());
							this_player.setPlayerId(hs.getPlayerId());
							bc.setHowOutBowler(this_player);
						}
					}
					
				}
				
				for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							this_player.setFull_name(as.getFull_name());
							this_player.setFirstname(as.getFirstname());
							this_player.setSurname(as.getSurname());
							this_player.setTicker_name(as.getTicker_name());
							this_player.setPlayerId(as.getPlayerId());
							bc.setHowOutBowler(this_player);
						}
					}else {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							this_player.setFull_name(as.getFull_name());
							this_player.setFirstname(as.getFirstname());
							this_player.setSurname(as.getSurname());
							this_player.setTicker_name(as.getTicker_name());
							this_player.setPlayerId(as.getPlayerId());
							bc.setHowOutBowler(this_player);
						}
					}
					
				}
				break;
			}
			
			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT: case CricketUtil.STUMPED: case CricketUtil.RUN_OUT: 
				for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							this_player.setFull_name(hs.getFull_name());
							this_player.setFirstname(hs.getFirstname());
							this_player.setSurname(hs.getSurname());
							this_player.setTicker_name(hs.getTicker_name());
							this_player.setPlayerId(hs.getPlayerId());
							bc.setHowOutFielder(this_player);
						}
					}else {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							this_player.setFull_name(hs.getFull_name());
							this_player.setFirstname(hs.getFirstname());
							this_player.setSurname(hs.getSurname());
							this_player.setTicker_name(hs.getTicker_name());
							this_player.setPlayerId(hs.getPlayerId());
							bc.setHowOutFielder(this_player);
						}
					}
					
				}
				
				for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							this_player.setFull_name(as.getFull_name());
							this_player.setFirstname(as.getFirstname());
							this_player.setSurname(as.getSurname());
							this_player.setTicker_name(as.getTicker_name());
							this_player.setPlayerId(as.getPlayerId());
							bc.setHowOutFielder(this_player);
						}
					}else {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							this_player.setFull_name(as.getFull_name());
							this_player.setFirstname(as.getFirstname());
							this_player.setSurname(as.getSurname());
							this_player.setTicker_name(as.getTicker_name());
							this_player.setPlayerId(as.getPlayerId());
							bc.setHowOutFielder(this_player);
						}
					}
					
				}
				//bc.setHowOutFielder(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getHowOutFielderId())));
				break;
			}

			switch (bc.getHowOut().toUpperCase()) {
			case CricketUtil.CAUGHT_AND_BOWLED:
				for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							bc.setHowOutText("c & b " + hs.getTicker_name());
							bc.setHowOutPartOne("c & b ");
							bc.setHowOutPartTwo(hs.getTicker_name());
						}
					}else {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							bc.setHowOutText("c & b " + hs.getTicker_name());
							bc.setHowOutPartOne("c & b ");
							bc.setHowOutPartTwo(hs.getTicker_name());
						}
					}
					
				}
				
				for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							bc.setHowOutText("c & b " + as.getTicker_name());
							bc.setHowOutPartOne("c & b ");
							bc.setHowOutPartTwo(as.getTicker_name());
						}
					}else {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							bc.setHowOutText("c & b " + as.getTicker_name());
							bc.setHowOutPartOne("c & b ");
							bc.setHowOutPartTwo(as.getTicker_name());
						}
					}
					
				}
				break;
			case CricketUtil.CAUGHT: case CricketUtil.MANKAD: case CricketUtil.RUN_OUT:
				switch (bc.getHowOut().toUpperCase()) {
				case CricketUtil.CAUGHT:
					for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
						if(bc.getHowOutPartTwo().contains("b ")) {
							if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
								bc.setHowOutText(bc.getHowOutPartOne()  + " "+ bc.getHowOutPartTwo());
								bc.setHowOutPartTwo("b " + hs.getTicker_name());
							}
							
							if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartOne().split("c ")[1])) {
								bc.setHowOutPartOne("c " + bc.getHowOutPartOne().split("c ")[1]);
							}
						}else {
							if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
								bc.setHowOutText(bc.getHowOutPartOne()  + " "+ bc.getHowOutPartTwo());
								bc.setHowOutPartTwo("b " + hs.getTicker_name());
							}
							if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartOne().split("c ")[1])) {
								bc.setHowOutPartOne("c " + bc.getHowOutPartOne().split("c ")[1]);
							}
						}
						
					}
					
					for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
						if(bc.getHowOutPartTwo().contains("b ")) {
							if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
								bc.setHowOutText(bc.getHowOutPartOne()  + " "+ bc.getHowOutPartTwo());
								bc.setHowOutPartTwo("b " + as.getTicker_name());
							}
							
							if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartOne().split("c ")[1])) {
								bc.setHowOutPartOne("c " + bc.getHowOutPartOne().split("c ")[1]);
							}
						}else {
							if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
								bc.setHowOutText(bc.getHowOutPartOne()  + " "+ bc.getHowOutPartTwo());
								bc.setHowOutPartTwo("b " + as.getTicker_name());
							}
							if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartOne().split("c ")[1])) {
								bc.setHowOutPartOne("c " + bc.getHowOutPartOne().split("c ")[1]);
							}
						}
						
					}
					
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
				for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							bc.setHowOutText("b " + hs.getTicker_name());
							bc.setHowOutPartOne("");
							bc.setHowOutPartTwo("b " + hs.getTicker_name());
						}
					}else {
						if(hs.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							bc.setHowOutText("b " + hs.getTicker_name());
							bc.setHowOutPartOne("");
							bc.setHowOutPartTwo("b " + hs.getTicker_name());
						}
					}
					
				}
				
				for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
					if(bc.getHowOutPartTwo().contains("b ")) {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo().split("b ")[1])) {
							bc.setHowOutText("b " + as.getTicker_name());
							bc.setHowOutPartOne("");
							bc.setHowOutPartTwo("b " + as.getTicker_name());
						}
					}else {
						if(as.getFull_name().equalsIgnoreCase(bc.getHowOutPartTwo())) {
							bc.setHowOutText("b " + as.getTicker_name());
							bc.setHowOutPartOne("");
							bc.setHowOutPartTwo("b " + as.getTicker_name());
						}
					}
					
				}
				
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
	public static MatchAllData populateApiMatchVariables(CricketService cricketService, MatchAllData match,Archive archive) 
			throws IllegalAccessException, InvocationTargetException 
	{
		List<Player> players = new ArrayList<Player>();
		
		for(Player plyr:match.getSetup().getHomeSquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.getSetup().setHomeSquad(players);

		if(match.getSetup().getHomeSubstitutes() != null) {
			players = new ArrayList<Player>();
			for(Player plyr:match.getSetup().getHomeSubstitutes()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
			match.getSetup().setHomeSubstitutes(players);
			
			players = new ArrayList<Player>();
			if(match.getSetup().getHomeOtherSquad() != null) {
				for(Player plyr:match.getSetup().getHomeOtherSquad()) {
					players.add(populatePlayer(cricketService, plyr, match));
				}
			}
			match.getSetup().setHomeOtherSquad(players);
		}
		
		if(match.getSetup().getAwaySubstitutes() != null) {
			players = new ArrayList<Player>();
			for(Player plyr:match.getSetup().getAwaySubstitutes()) {
				players.add(populatePlayer(cricketService, plyr, match));
			}
			match.getSetup().setAwaySubstitutes(players);
			
			players = new ArrayList<Player>();
			if(match.getSetup().getAwayOtherSquad() != null) {
				for(Player plyr:match.getSetup().getAwayOtherSquad()) {
					players.add(populatePlayer(cricketService, plyr, match));
				}
			}
			match.getSetup().setAwayOtherSquad(players);
		}
		
		players = new ArrayList<Player>();
		for(Player plyr:match.getSetup().getAwaySquad()) {
			players.add(populatePlayer(cricketService, plyr, match));
		}
		match.getSetup().setAwaySquad(players);
		
		if(match.getSetup().getHomeTeamId() > 0)
			match.getSetup().setHomeTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getSetup().getHomeTeamId())));
		if(match.getSetup().getAwayTeamId() > 0)
			match.getSetup().setAwayTeam(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(match.getSetup().getAwayTeamId())));
		if(match.getSetup().getGroundId() > 0) {
			match.getSetup().setGround(cricketService.getGround(match.getSetup().getGroundId()));
			if(match.getSetup().getGround() != null) {
				match.getSetup().setVenueName(match.getSetup().getGround().getFullname());
			}
		}
		
		if(match.getMatch().getInning() != null) {
			for(Inning inn : match.getMatch().getInning()) {
				
				inn.setBatting_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBattingTeamId())));
				inn.setBowling_team(cricketService.getTeam(CricketUtil.TEAM, String.valueOf(inn.getBowlingTeamId())));
				
				if(inn.getBattingCard() != null)
					for(BattingCard batc:inn.getBattingCard()) 
						batc = processWebBattingcard(cricketService,batc,archive);
	
				if(inn.getPartnerships() != null)
					for(Partnership part:inn.getPartnerships()) {
						part.setFirstPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(part.getFirstBatterNo())));
						part.setSecondPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(part.getSecondBatterNo())));
					}
				
				if(inn.getBowlingCard() != null)
					for(BowlingCard bowlc:inn.getBowlingCard()) {
						Player players1 = new Player();
						
						//bc.setPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bc.getPlayerId())));
						for(Player hs : archive.getMatchAllData().getSetup().getHomeSquad()) {
							if(hs.getFull_name().equalsIgnoreCase(bowlc.getPlayer().getFull_name())) {
								players1.setFull_name(hs.getFull_name());
								players1.setFirstname(hs.getFirstname());
								players1.setSurname(hs.getSurname());
								players1.setTicker_name(hs.getTicker_name());
								players1.setPlayerId(hs.getPlayerId());
								bowlc.setPlayer(players1);
							}
							
						}
						
						for(Player as : archive.getMatchAllData().getSetup().getAwaySquad()) {
							if(as.getFull_name().equalsIgnoreCase(bowlc.getPlayer().getFull_name())) {
								players1.setFull_name(as.getFull_name());
								players1.setFirstname(as.getFirstname());
								players1.setSurname(as.getSurname());
								players1.setTicker_name(as.getTicker_name());
								players1.setPlayerId(as.getPlayerId());
								bowlc.setPlayer(players1);
							}
							
						}
							//bowlc.setPlayer(cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(bowlc.getPlayerId())));
					}
						
	
				if(inn.getBowlingTeamId() == match.getSetup().getHomeTeamId()) {
					inn.setFielders(match.getSetup().getHomeSquad());
				} else if(inn.getBowlingTeamId() == match.getSetup().getAwayTeamId()) {
					inn.setFielders(match.getSetup().getAwaySquad());
				}
	
			}
		}
		return match;
	}
	public static String getBalls(int Overs,int Balls) {
		String Overs_text = "" ;
		switch(Balls) {
		case 0:
			Overs_text = "6";
			return Overs_text;
		default:
			Overs_text = String.valueOf(Balls);
			return Overs_text;
		}
	}
	
	public static String getAverage(int inningsCount, int notOuts, int totalRuns, 
		int numberOfDecimals, String defaultValue) 
	{
		if(inningsCount - notOuts <= 0) {
			return defaultValue;
		} else {
			if (numberOfDecimals > 0) {
				return String.format("%.0" + numberOfDecimals + "f", (float)totalRuns / (float)(inningsCount - notOuts));
			} else {
				return defaultValue;
			}
		}
	}

	public static String getEconomy(int totalRunsConceded, int totalBallsBowled, int numberOfDecimals, String defaultValue) 
	{
		if(totalBallsBowled <= 0) {
			return defaultValue;
		} else {
			if (numberOfDecimals > 0) {
				return String.format("%.0" + numberOfDecimals + "f", ((float)totalRunsConceded / (float)totalBallsBowled) * 6);
			} else {
				return defaultValue;
			}
		}
	}
	
	public static String getOvers(int Overs,int Balls) {
		String Overs_text = "" ;
		switch(Balls) {
		case 0:
			Overs_text = String.valueOf(Overs);
			return Overs_text;
		default:
			Overs_text = String.valueOf(Overs + 1);
			return Overs_text;
		}
	}
	
	public static String ordinal(int i) {
	    int mod100 = i % 100;
	    int mod10 = i % 10;
	    if(mod10 == 1 && mod100 != 11) {
	        return i + "st";
	    } else if(mod10 == 2 && mod100 != 12) {
	        return i + "nd";
	    } else if(mod10 == 3 && mod100 != 13) {
	        return i + "rd";
	    } else {
	        return i + "th";
	    }
	}
	public static String getOrdinalSuffix(int number) {
        int lastTwoDigits = number % 100;
        int lastDigit = number % 10;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 13) {
            return "th";
        }

        switch (lastDigit) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
	
	public static List<Tournament> extractSeasonStats(String typeOfExtraction, List<MatchAllData> tournament_matches, 
			CricketService cricketService,MatchAllData currentMatch, List<Tournament> past_tournament_stats,List<Season> ses) 
	{
		int playerId = -1;
		int seasonID = 0;
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
		boolean has_match_started = false;
		
		switch(typeOfExtraction) {
		case "SEASON1": case "SEASON2": case "SEASON3":
			if(typeOfExtraction.equalsIgnoreCase("SEASON1")) {
				seasonID = 1;
				
			}else if(typeOfExtraction.equalsIgnoreCase("SEASON2")) {
				seasonID = 2;
			}else if(typeOfExtraction.equalsIgnoreCase("SEASON3")) {
				seasonID = 3;
			}
			for(MatchAllData mtch : tournament_matches) {
				if(seasonID == mtch.getSetup().getSeasonId()) {
					if(mtch.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * mtch.getMatch().getInning().get(0).getTotalOvers() 
							+ mtch.getMatch().getInning().get(0).getTotalBalls()) > 0) {
						has_match_started = true;
					}
					for(Inning inn : mtch.getMatch().getInning())
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
									
									int thirty =0,fifty=0,hundreds=0;
									if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
										thirty = thirty + 1;
									}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
										fifty = fifty + 1;
									}else if(bc.getRuns()>= 100) {
										hundreds = hundreds + 1;
									}
									
									tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + fifty);
									tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + hundreds);
									
									if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.get(playerId).setInnings(tournament_stats.get(playerId).getInnings()+1);
									}
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(playerId).setNot_out(tournament_stats.get(playerId).getNot_out() + 1);
										
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
												(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),mtch.getSetup().getGround(),
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
										
									}else {
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
												bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(),mtch.getSetup().getGround(),
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
									}
									
								}else {
									int thirty =0,fifty=0,hundreds=0;
									if(bc.getRuns()>= 30 && bc.getRuns() < 50) {
										thirty = thirty + 1;
									}else if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
										fifty = fifty + 1;
									}else if(bc.getRuns()>= 100) {
										hundreds = hundreds + 1;
									}
									
									tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), 0, 0,thirty, fifty, hundreds, 
											0, 0, 0, bc.getBalls(), 0,0,0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>(), 
											new ArrayList<BestStats>()));
									
									if(bc.getBatsmanInningStarted() != null && bc.getBatsmanInningStarted().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.get(tournament_stats.size() - 1).setInnings(tournament_stats.get(tournament_stats.size() - 1).getInnings()+1);
									}
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(tournament_stats.size() - 1).setNot_out(tournament_stats.get(tournament_stats.size() - 1).getNot_out() + 1);
										
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
												(bc.getRuns() * 2) + 1, bc.getBalls(),inn.getBowling_team(),mtch.getSetup().getGround(),
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
										
									}else {
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
												(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(), mtch.getSetup().getGround(), 
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
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

									tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
											(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
											mtch.getSetup().getGround(),mtch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
									
								}else {
									
									tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0,0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 
											6 * boc.getOvers() + boc.getBalls(), 0, boc.getDots(),0,0,null,0,0,0,0, boc.getPlayer(), 
											new ArrayList<BestStats>(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									
									tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
											(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), inn.getBatting_team(),
											mtch.getSetup().getGround(),mtch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
																			
								}
							}
						}
					}
					if(has_match_started == true) {
						for(Tournament trmnt : tournament_stats) {
							for(Player plyr : mtch.getSetup().getHomeSquad()) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									trmnt.setMatches(trmnt.getMatches() + 1);
								}
							}
							for(Player plyr : mtch.getSetup().getAwaySquad()) {
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
		return tournament_stats;
	}

	public static String OverBalls(int Overs,int Balls) {
		
		int TotalBalls=0, WholeOv, OddBalls;
		String Overs_text = "0" ;
		
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
	
	public static List<String> getSplit(int inning_number, int splitvalue, MatchAllData match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					int max_balls = (match.getSetup().getMaxOvers() * Integer.valueOf(match.getSetup().getBallsPerOver()));
					int count_balls = ((match.getMatch().getInning().get(inning_number-1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) 
							+ match.getMatch().getInning().get(inning_number-1).getTotalBalls());
					
					switch (events.get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  
					case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LEG_BYE: case CricketUtil.BYE: 
						total_balls = total_balls + 1 ;
						total_runs = total_runs + events.get(i).getEventRuns();
						break;
					
					case CricketUtil.LOG_WICKET:
						if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
								!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) &&
								!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.TIMED_OUT) &&
								!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.MANKAD)) {
							
							total_balls = total_balls + 1 ;
							total_runs = total_runs + events.get(i).getEventRuns();
						}
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
	
	public static List<String> getPlayerSplit(int inning_number,int playerId ,int splitvalue,int plyr_balls_count, MatchAllData match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ,count_balls=0;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					if(playerId == events.get(i).getEventBatterNo()) {
						switch (events.get(i).getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE:
						case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET:
							total_balls = total_balls + 1 ;
							count_balls = count_balls + 1 ;
							total_runs = total_runs + events.get(i).getEventRuns();
							break;
						
						case CricketUtil.LEG_BYE: case CricketUtil.BYE: case CricketUtil.NO_BALL:
							total_balls = total_balls + 1 ;
							count_balls = count_balls + 1 ;
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
						
						if((total_runs >= splitvalue && count_balls < plyr_balls_count) || (count_balls == plyr_balls_count)) {
							
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
	
	public static List<String> getScoreTypeData(String whatToProcess, MatchAllData match, List<Integer> inning_numbers, int player_id, String separator) {
	    List<String> return_score_data = new ArrayList<>();
	    int[] dots = {0, 0}, ones = {0, 0}, twos = {0, 0}, threes = {0, 0}, fours = {0, 0}, fives = {0, 0}, sixes = {0, 0}, nines = {0, 0};
	    boolean go_ahead = false;

	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	        for (Event evnt : match.getEventFile().getEvents()) {
	            for (Integer inn_num : inning_numbers) {
	                if (evnt.getEventInningNumber() == inn_num) {
	                    go_ahead = false;
	                    
	                    switch (whatToProcess) {
	                        case CricketUtil.BATSMAN:
	                            if (evnt.getEventBatterNo() == player_id) {
	                                go_ahead = true;
	                            }
	                            break;
	                        case CricketUtil.BOWLER:
	                            if (evnt.getEventBowlerNo() == player_id) {
	                                go_ahead = true;
	                            }
	                            break;
	                        case "TEAM":
	                            go_ahead = true;
	                            break;
	                    }
	                    
	                    if (go_ahead) {
	                        switch (evnt.getEventType()) {
	                            case CricketUtil.ONE:
	                                ones[inn_num - 1]++;
	                                break;
	                            case CricketUtil.TWO:
	                                twos[inn_num - 1]++;
	                                break;
	                            case CricketUtil.THREE:
	                                threes[inn_num - 1]++;
	                                break;
	                            case CricketUtil.FOUR:
	                            	if(evnt.getEventWasABoundary() != null && 
	                            		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		fours[inn_num - 1]++;
	                            	}
	                                break;
	                            case CricketUtil.FIVE:
	                                fives[inn_num - 1]++;
	                                break;
	                            case CricketUtil.SIX:
	                            	if(evnt.getEventWasABoundary() != null && 
	                            		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		sixes[inn_num - 1]++;
	                            	}
	                                break;
	                            case CricketUtil.NINE:
	                            	if(evnt.getEventWasABoundary() != null && 
	                            		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		nines[inn_num - 1]++;
	                            	}
	                                break;
	                            case CricketUtil.DOT:
	                            	dots[inn_num - 1]++;
	                                break;
	                            case CricketUtil.LOG_WICKET:
	                            	if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)
	                            		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.BOWLED) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)
	                            		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.LBW) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_WICKET)
	                            		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_BALL_TWICE)) 
	                            	{
	                            		dots[inn_num - 1]++;
	                            	}
	                                break;
	                            case CricketUtil.BYE: case CricketUtil.LEG_BYE:
	                                switch (whatToProcess) {
	                                    case CricketUtil.BATSMAN:
	                                    case CricketUtil.BOWLER:
	                                        dots[inn_num - 1]++;
	                                        break;
	                                }
	                                break;
	                            case CricketUtil.LOG_ANY_BALL:
	                                if (evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                    if (evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
	                                        switch (whatToProcess) {
	                                            case CricketUtil.BATSMAN:
	                                            case CricketUtil.BOWLER:
	                                                dots[inn_num - 1]++;
	                                                break;
	                                        }
	                                    }
	                                    if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) && (evnt.getEventWasABoundary() != null) &&
	                                            (evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
	                                        switch (whatToProcess) {
	                                            case CricketUtil.BATSMAN:
	                                            case CricketUtil.BOWLER:
	                                            case "TEAM":
	                                                fours[inn_num - 1]++;
	                                                break;
	                                        }
	                                    }
	                                    if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.SIX)) && (evnt.getEventWasABoundary() != null) &&
	                                            (evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
	                                        switch (whatToProcess) {
	                                            case CricketUtil.BATSMAN:
	                                            case CricketUtil.BOWLER:
	                                            case "TEAM":
	                                                sixes[inn_num - 1]++;
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
	    }

	    return_score_data.add(String.valueOf(dots[0]) + separator + String.valueOf(ones[0]) + separator + String.valueOf(twos[0]) +
	            separator + String.valueOf(threes[0]) + separator + String.valueOf(fours[0]) + separator + String.valueOf(fives[0]) +
	            separator + String.valueOf(sixes[0]) + separator + String.valueOf(nines[0]));

	    return_score_data.add(String.valueOf(dots[1]) + separator + String.valueOf(ones[1]) + separator + String.valueOf(twos[1]) +
	            separator + String.valueOf(threes[1]) + separator + String.valueOf(fours[1]) + separator + String.valueOf(fives[1]) +
	            separator + String.valueOf(sixes[1]) + separator + String.valueOf(nines[1]));

	    return return_score_data;
	}
	
	public static List<String> getFirstPowerPlayScores(MatchAllData match, List<Integer> inning_numbers, List<Event> event)
    {
	    List<String> powerPlayScores = new ArrayList<>();
	    int[] totalRuns = {0, 0};int[] totalWickets = {0, 0};int[] ballCount = {0, 0};
	    List<Integer> powerplayValues = new ArrayList<Integer>();
	    
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= event.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	                if (event.get(i).getEventInningNumber() == inn_num) {
	                	powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
	                	
	                	switch(event.get(i).getEventType()) {
							case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1] ++;
								break;										
							}
			                if(ballCount[inn_num-1] >= powerplayValues.get(0) && ballCount[inn_num-1] < powerplayValues.get(1)) {
			        			switch (event.get(i).getEventType())
			                    {
			                    	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			                    	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
			                            totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                            break;
			                    	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			                            totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                            break;
			
			                    	case CricketUtil.LOG_WICKET:
			                            if (event.get(i).getEventRuns() > 0)
			                            {
			                                totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                            }
			                            totalWickets[inn_num-1] += 1;
			                            break;
			
			                    	case CricketUtil.LOG_ANY_BALL:
			                            totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                            if (event.get(i).getEventExtra() != null)
			                            {
			                                totalRuns[inn_num-1] += event.get(i).getEventExtraRuns();
			                            }
			                            if (event.get(i).getEventSubExtra() != null)
			                            {
			                                totalRuns[inn_num-1] += event.get(i).getEventSubExtraRuns();
			                            }
			                            if (event.get(i).getEventHowOut() != null && !event.get(i).getEventHowOut().isEmpty())
			                            {
			                                totalWickets[inn_num-1] += 1;
			                            }
			                            break;
			                    }
			        		} else if(ballCount[inn_num-1] >= powerplayValues.get(0) && ballCount[inn_num-1] == powerplayValues.get(1)) {
			        			if(!event.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || 
			        					!event.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
			        				
			        				if ((ballCount[inn_num-1] == 12 && event.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) &&
			        				        (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10) ||
			        				         (ballCount[inn_num-1] == 60 && event.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) &&
			        				          (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)))) ||
			        				        (ballCount[inn_num-1] == 36 && event.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&&
			        				        		(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20))))) {
			        				    break;
			        				}

			        				switch (event.get(i).getEventType())
			                        {
			                        	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			                        	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
			                                totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                                break;
			
			                        	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			                                totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                                break;
			
			                        	case CricketUtil.LOG_WICKET:
			                                if (event.get(i).getEventRuns() > 0)
			                                {
			                                    totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                                }
			                                totalWickets[inn_num-1] += 1;
			                                break;
			
			                        	case CricketUtil.LOG_ANY_BALL:
			                                totalRuns[inn_num-1] += event.get(i).getEventRuns();
			                                if (event.get(i).getEventExtra() != null)
			                                {
			                                    totalRuns[inn_num-1] += event.get(i).getEventExtraRuns();
			                                }
			                                if (event.get(i).getEventSubExtra() != null)
			                                {
			                                    totalRuns[inn_num-1] += event.get(i).getEventSubExtraRuns();
			                                }
			                                if (event.get(i).getEventHowOut() != null && !event.get(i).getEventHowOut().isEmpty())
			                                {
			                                    totalWickets[inn_num-1] += 1;
			                                }
			                                break; 
			                        }
			        			}
			        		}	                	
	                	}
	            	}
	        	}
	    	} 
		    powerPlayScores.add(String.valueOf(totalRuns[0]) + "-" + String.valueOf(totalWickets[0]));
		    powerPlayScores.add(String.valueOf(totalRuns[1]) + "-" + String.valueOf(totalWickets[1]));
			return powerPlayScores;
    }
    public static List<String> getSecPowerPlayScores(MatchAllData match, List<Integer> inning_numbers, List<Event> events)
    {
    	List<String> powerPlayScores = new ArrayList<>();
	    int[] totalRuns = {0, 0};int[] totalWickets = {0, 0};int[] ballCount = {0, 0};
	    List<Integer> powerplayValues = new ArrayList<Integer>();
	    int StartOver=0,EndOver=0;
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= events.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	            	powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
	            	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
	                	StartOver = powerplayValues.get(2);
	                	EndOver = powerplayValues.get(3);
	                }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	                	StartOver = 7;
	                	EndOver = 15;
	                }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)){
	                	StartOver = 3;
	                	EndOver = 6;
	                }
	                if (events.get(i).getEventInningNumber() == inn_num) {
	                	switch(events.get(i).getEventType()) {
							case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1]++;
								break;
							}
	                	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	            			if (ballCount[inn_num-1] == 36 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                            
	            				if (ballCount[inn_num-1] == 36 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                int j = i;
	                                while (j < events.size() && ballCount[inn_num-1] == 36) {
	                                	if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
                                        } else {
                                        	if(events.get(j).getEventExtra() != null) {
                                        		if(events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                        		if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	    	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        	}
                                        	}else {
                                        		break;
                                        	}
                                        }
	                                    j++;
	                                }  
	            				}
	        				 }
	            			}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
	                			if (ballCount[inn_num-1] == 12 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                
	                				if (ballCount[inn_num-1] == 12 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                    int j = i;
	                                    while (j < events.size() && ballCount[inn_num-1] == 12) {
	                                        
	                                        if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                        	if(events.get(j).getEventExtra() != null) {
	                                        		if(events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		                                        		if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
		    	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
		                                        	}
	                                        	}else {
	                                        		break;
	                                        	}
	                                        }
	                                        j++;
	                                    }  
	                				}
	                			}
	        				}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)){

	                			if (ballCount[inn_num-1] == ((StartOver-1)*6) && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                
	                				if (ballCount[inn_num-1] == ((StartOver-1)*6) && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                    int j = i;
	                                    while (j < events.size() && ballCount[inn_num-1] == ((StartOver-1)*6)) {
	                                    	
	                                        if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                        	if(events.get(j).getEventExtra() != null) {
	                                        		if(events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		                                        		if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
		    	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
		                                        	}
	                                        	}else {
	                                        		break;
	                                        	}
	                                        }
	                                        j++;
	                                    }  
	                				}
	                			}
	        				}
	                	
	        			
	        			if(ballCount[inn_num-1] > ((StartOver - 1) * 6) && ballCount[inn_num-1] < (EndOver * 6)) {
	        				if ((ballCount[inn_num-1] == 90 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&&(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20))||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20))||
	        						(ballCount[inn_num-1] == 36 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&& match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10))) {
	        	                break;
	        				}
	        				if((ballCount[inn_num-1] == 37 &&(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20))||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20))||
	        						(ballCount[inn_num-1] == 13 &&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10))) {
	        					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	        						totalRuns[inn_num-1] += events.get(i-1).getEventRuns();
	        					}
	        				}
	        				if((ballCount[inn_num-1] == ((StartOver*6)+1) &&(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD))||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI))||
	        						(ballCount[inn_num-1] == ((StartOver*6)+1) &&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10))) {
	        					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	        						totalRuns[inn_num-1] += events.get(i-1).getEventRuns();
	        					}
	        				}
	        				
	        				//System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
	            			switch (events.get(i).getEventType())
	                        {
	                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
	    					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
	                                totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                break;

                            case CricketUtil.WIDE:
                            	if(ballCount[inn_num-1] == 90 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&& (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20))||
                            			ballCount[inn_num-1] == 36 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)||
                            			ballCount[inn_num-1] == EndOver && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&& (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI))) {
                                    break;
                            	}else {
                            		totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                    break;
                            	}
                            case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                break;
                            case CricketUtil.LOG_WICKET:
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                }
                                totalWickets[inn_num-1] += 1;
                                break;

                            case CricketUtil.LOG_ANY_BALL:
                                totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                if (events.get(i).getEventExtra() != null)
                                {
                                    totalRuns[inn_num-1] += events.get(i).getEventExtraRuns();
                                }
                                if (events.get(i).getEventSubExtra() != null)
                                {
                                    totalRuns[inn_num-1] += events.get(i).getEventSubExtraRuns();
                                }
                                if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                {
                                    totalWickets[inn_num-1] += 1;
                                }
                                break;
	                        }
	            		} else if(ballCount[inn_num-1] > ((StartOver - 1) * 6) && ballCount[inn_num-1] == (EndOver * 6)) {
	            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	            				switch (events.get(i).getEventType())
	                            {
	                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
	                            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
	                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                    break;

	        						case CricketUtil.WIDE: 
	        							if((ballCount[inn_num-1] == 90 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&& (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)))||
	        									(ballCount[inn_num-1] == (EndOver*6) && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&& (match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)))||
	        									ballCount[inn_num-1] == 36 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)&&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
	        							
	                                        break;
	                                	}else {
	                                		totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                        break;
	                                	}
	        						case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
	                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                    break;
	        						case CricketUtil.LOG_WICKET:
	                                    if (events.get(i).getEventRuns() > 0)
	                                    {
	                                        totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                    }
	                                    totalWickets[inn_num-1] += 1;
	                                    break;

	        						case CricketUtil.LOG_ANY_BALL:
	                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
	                                    if (events.get(i).getEventExtra() != null)
	                                    {
	                                        totalRuns[inn_num-1] += events.get(i).getEventExtraRuns();
	                                    }
	                                    if (events.get(i).getEventSubExtra() != null)
	                                    {
	                                        totalRuns[inn_num-1] += events.get(i).getEventSubExtraRuns();
	                                    }
	                                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
	                                    {
	                                        totalWickets[inn_num-1] += 1;
	                                    }
	                                    break;
	                            }
	            			}
	            		}  

	                	}
	                }
	            }
	    	}
    powerPlayScores.add(String.valueOf(totalRuns[0]) + "-" + String.valueOf(totalWickets[0]));
    powerPlayScores.add(String.valueOf(totalRuns[1]) + "-" + String.valueOf(totalWickets[1]));
	return powerPlayScores;
    }
    public static List<String> getThirdPowerPlayScore(MatchAllData match, List<Integer> inning_numbers, List<Event> events)
    {

    	 List<String> powerPlayScores = new ArrayList<>();
  	    int[] totalRuns = {0, 0};int[] totalWickets = {0, 0};int[] ballCount = {0, 0};
  	    int StartOver=0,EndOver=0;
  	  List<Integer> powerplayValues = new ArrayList<Integer>();
  	    
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= events.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	            	powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
	            	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
	                	StartOver = powerplayValues.get(4);
	                	EndOver = powerplayValues.get(5);
	                }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	                	StartOver = 16;
	                	EndOver = 20;
	                }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)){
	                	StartOver = 7;
	                	EndOver = 10;
	                }
	                if (events.get(i).getEventInningNumber() == inn_num) {
	                	switch(events.get(i).getEventType()) {
							case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1]++;
								break;
							}
	                	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	            			if (ballCount[inn_num-1] == 90 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                            
	            				if (ballCount[inn_num-1] == 90 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                int j = i;
	                                while (j < events.size() && ballCount[inn_num-1] == 90) {
	                                	if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
                                        } else {
                                        	if(events.get(j).getEventExtra() != null) {
                                        		if(events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                        		if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	    	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        	}
                                        	}else {
                                        		break;
                                        	}
                                        }
	                                    j++;
	                                }  
	            				}
	        				 }
	            			}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)){

	                			if (ballCount[inn_num-1] == ((StartOver-1)*6) && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                
	                				if (ballCount[inn_num-1] == ((StartOver-1)*6) && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                    int j = i;
	                                    while (j < events.size() && ballCount[inn_num-1] == ((StartOver-1)*6)) {
	                                    	if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                        	if(events.get(j).getEventExtra() != null) {
	                                        		if(events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		                                        		if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
		    	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
		                                        	}
	                                        	}else {
	                                        		break;
	                                        	}
	                                        }
	                                        j++;
	                                    }  
	                				}
	                			}
	        				}
	                	
	              		if(ballCount[inn_num-1] > ((StartOver - 1) * 6) && ballCount[inn_num-1] <= (EndOver * 6)) {
                			if((ballCount[inn_num-1] == 91 &&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) || 
                					(ballCount[inn_num-1] == 241)&&(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI))  ||
                					((ballCount[inn_num-1] == 37)&&match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10))) {
            					
                				if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
                					totalRuns[inn_num-1] += events.get(i-1).getEventRuns();
            					}
            				}
                			//System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
                			switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                    break;

                                case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                    break;

                                case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                    }
                                    totalWickets[inn_num-1] += 1;
                                    break;

                                case CricketUtil.LOG_ANY_BALL:
                                    totalRuns[inn_num-1] += events.get(i).getEventRuns();
                                    if (events.get(i).getEventExtra() != null)
                                    {
                                        totalRuns[inn_num-1] += events.get(i).getEventExtraRuns();
                                    }
                                    if (events.get(i).getEventSubExtra() != null)
                                    {
                                        totalRuns[inn_num-1] += events.get(i).getEventSubExtraRuns();
                                    }
                                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty())
                                    {
                                        totalWickets[inn_num-1] += 1;
                                    }
                                    break;
                            }
                		}  

	                	}
	                }
	            }
	    	}
	    System.out.println("totalRuns[0] = " + totalRuns[0] + "    w = " + totalWickets[0]);
	    System.out.println("totalRuns[1] = " + totalRuns[1] + "    w = " + totalWickets[1]);
		    powerPlayScores.add(String.valueOf(totalRuns[0]) + "-" + String.valueOf(totalWickets[0]));
		    powerPlayScores.add(String.valueOf(totalRuns[1]) + "-" + String.valueOf(totalWickets[1]));
			return powerPlayScores;
    }

    public static String generateRunRates(int runs, int startOvers, int endOvers, int numberOfDecimals, MatchAllData match) {

	    String run_rate = "";
	    int total_balls = ((endOvers - startOvers + 1) * Integer.valueOf(match.getSetup().getBallsPerOver()));

	    if (total_balls > 0) {
	        float run_rate_val = ((float) runs / (float) total_balls) * Integer.valueOf(match.getSetup().getBallsPerOver());
	        
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
	    } 
	    return run_rate;
	}
	
    public static String PowerPlayMatchOvers(int inn_num, MatchAllData match, String separator) {
		String pp_overs="";
			if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD)) {
				pp_overs=match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver()+separator+
						match.getMatch().getInning().get(inn_num-1).getSecondPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getSecondPowerplayEndOver()+separator+
						match.getMatch().getInning().get(inn_num-1).getThirdPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getThirdPowerplayEndOver();
				
			}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20)||match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) {
				pp_overs=match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver()+separator+
						 match.getMatch().getInning().get(inn_num-1).getSecondPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getSecondPowerplayEndOver()+separator+
						 match.getMatch().getInning().get(inn_num-1).getThirdPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getThirdPowerplayEndOver();
				
			}else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
				pp_overs= match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver()+separator+
						  match.getMatch().getInning().get(inn_num-1).getSecondPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getSecondPowerplayEndOver()+separator+
						  match.getMatch().getInning().get(inn_num-1).getThirdPowerplayStartOver()+"-"+match.getMatch().getInning().get(inn_num-1).getThirdPowerplayEndOver();				
			}	
		
		return pp_overs;
	}
   
	public static int getAllRounderCatches(int allRoubderId,MatchAllData match,List<Event> event)
    {
		int catches=0;
		if(event.size()>0) {
		for(Event ev:event) {
			if((allRoubderId==ev.getEventBowlerNo()||allRoubderId==ev.getEventHowOutFielderId())&&ev.getEventType().equalsIgnoreCase(CricketUtil.LOG_WICKET)){
				if(!ev.getEventHowOut().isEmpty()&& ev.getEventHowOut()!=null&&(ev.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT))|| (ev.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED))){
					catches ++;	
				}			
			}
		}
	}
		return catches;
		
    }
	public static  List<Object>  getSessionPerformer(MatchAllData match ,List<Event> event){
		
		int ball_count = 0;
		
		HashSet<Player> batter = new HashSet<Player>();
		HashSet<Player> bowler = new HashSet<Player>();
		List<Object> SessionPerformer = new ArrayList<>();
		
		int total_ball = match.getMatch().getDaysSessions().stream().filter(dy-> dy.getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES))
				.findAny().orElse(null).getTotalBalls();
		
		for (int i = event.size() - 1; i >= 0; i--)
        {
			switch(event.get(i).getEventType()) {
			case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET:
			case CricketUtil.BYE: case CricketUtil.LEG_BYE:
				
					ball_count = ball_count + 1;
					
				break;
			}
			
	    	if(ball_count <= total_ball) {
	    		switch(event.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
				case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET:
				case CricketUtil.BYE: case CricketUtil.LEG_BYE:
					
						int bat_Id = event.get(i).getEventBatterNo(), ball_Id = event.get(i).getEventBowlerNo();
						
						if(batter == null || batter.stream().noneMatch(bc -> bc.getPlayerId() == bat_Id)) {
							batter.add(new Player(event.get(i).getEventBatterNo(),0,0,0,0,0,0));
						}
						if(bowler == null || bowler.stream().noneMatch(boc -> boc.getPlayerId() == ball_Id)) {
							bowler.add(new Player(event.get(i).getEventBowlerNo(),0,0,0));
						}
						
					break;
				}
	    		
	    		for(Player bc : batter) {
	    			if(bc.getPlayerId() == event.get(i).getEventBatterNo()) {
	    				String data = getpowerplay(event.get(i));
	    				
	    				bc.setRuns((bc.getRuns() + Integer.valueOf(data.split(",")[0])));
	    				bc.setBalls((bc.getBalls() + Integer.valueOf(data.split(",")[6])));
	    			}
	    		}
	    		
	    		for(Player boc : bowler) {
	    			if(boc.getPlayerId() == event.get(i).getEventBowlerNo()) {
	    				String data = getpowerplay(event.get(i));
	    				
	    				boc.setRuns((boc.getRuns() + Integer.valueOf(data.split(",")[0])));
	    				boc.setBalls((boc.getBalls() + Integer.valueOf(data.split(",")[6])));
	    				boc.setWickets((boc.getWickets() + Integer.valueOf(data.split(",")[1])));
	    			}
	    		}
	    	}
        }
    		
    		SessionPerformer.add(batter);
    		SessionPerformer.add(bowler);
		return SessionPerformer ;
		
	}

	public static List<Object> getPerformarOfmatch(MatchAllData match, List<Event> events)
    {

        int ball_count = 0;
        int total_ball=match.getMatch().getDaysSessions().stream().filter(dy->dy.getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES)).findAny().orElse(null).getTotalBalls();
       Set<Player>batsman = new HashSet<Player>();
       Set<Player>bowler = new HashSet<Player>();
       Set<Object>PerformarOfmatch = new HashSet<Object>();
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = events.size() - 1; i >0; i--)
            {
            		switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						ball_count = ball_count + 1;
						
						break;
                }
            	if(ball_count<=total_ball) {
            		switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						int batter_id = events.get(i).getEventBatterNo();
						int bowler_id = events.get(i).getEventBowlerNo();
						 // Add batsman if not already present
                        if (batsman==null||batsman.stream().noneMatch(bc -> bc.getPlayerId() == batter_id)) {
                            batsman.add(new Player(batter_id, 0, 0, 0, 0, 0, 0));
                        }

                        // Add bowler if not already present
                        if (bowler==null||bowler.stream().noneMatch(bc -> bc.getPlayerId() == bowler_id)) {
                            bowler.add(new Player(bowler_id, 0, 0, 0));
                            //System.out.println("Added new bowler with ID: " + bowler_id);
                        }
						break;
                }
            		for(Player bc:batsman) {
            			if(bc.getPlayerId()== events.get(i).getEventBatterNo()) {
            				String data= getpowerplay(events.get(i));
            				bc.setRuns((bc.getRuns()+Integer.valueOf(data.split(",")[1])));
            				bc.setBalls((bc.getBalls()+Integer.valueOf(data.split(",")[6])));
            				bc.setFour((bc.getFour()+Integer.valueOf(data.split(",")[3])));
            				bc.setSix((bc.getSix()+Integer.valueOf(data.split(",")[4])));
            				bc.setNine((bc.getNine()+Integer.valueOf(data.split(",")[5])));
            			}
            		}
//            		for(Player bc:bowler) {
//            			if(bc.getPlayerId()== events.get(i).getEventBowlerNo()) {
//            				String data= getpowerplay(events.get(i));
//            				bc.setRuns((bc.getRuns()+Integer.valueOf(data.split(",")[0])));
//            				bc.setBalls((bc.getBalls()+Integer.valueOf(data.split(",")[6])));
//            				bc.setWickets((bc.getWickets()+Integer.valueOf(data.split(",")[1])));
//            			}
//            		}
            	}
            }
        }
//        for(Player p:batsman ) {
//        	System.out.println("ID  "+p.getPlayerId()+"  RUN:- "+p.getRuns()+" Balls :- "+p.getBalls());
//        }
        PerformarOfmatch.add(batsman);
        PerformarOfmatch.add(bowler);
        List<Object> arr = new ArrayList<>(PerformarOfmatch);
		return arr;
    }
	
	public static int getOverNumberFromString(String overStr) {
	    if (overStr == null || overStr.trim().isEmpty()) {
	        return 0; // or throw exception based on your requirement
	    }

	    double overValue = Double.parseDouble(overStr.trim());
	    return (int) Math.floor(overValue) + 1;
	}
	
	public static String getFirstPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0,Fours=0,Sixes=0,Dots = 0,Nines=0;
        List<Integer> powerplayValues = new ArrayList<Integer>();
        
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
            {
            	if(events.get(i).getEventInningNumber() == inn_num)
                {
            		powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
            		
            		switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						ball_count = ball_count + 1;
						break;
					case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
                        if(events.get(i).getEventOverNo() == 0 && events.get(i).getEventBallNo() == 0) {
                        	total_run_PP += events.get(i).getEventRuns();
                        }
                        break;	
					case CricketUtil.LOG_WICKET:
						if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                            	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                            	
							ball_count = ball_count + 1;
                        }
						break;
					}
            		
            		if(ball_count >= powerplayValues.get(0) && ball_count < powerplayValues.get(1)) {
            			switch (events.get(i).getEventType())
                        {
                        	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                        	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                total_run_PP += events.get(i).getEventRuns();
                                switch(events.get(i).getEventType()) {
	                        	case CricketUtil.FOUR:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Fours ++;
	                        		}
	                        		break;
	                        	case CricketUtil.SIX:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Sixes ++;
	                        		}
	                        		break;
	                        	case CricketUtil.NINE:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Nines ++;
	                        		}
	                        		break;
	                        	case CricketUtil.DOT:
	                        		Dots ++;
	                        		break;	
	                            }
                                break;

                        	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                        	case CricketUtil.LOG_WICKET:
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += events.get(i).getEventRuns();
                                }else {
                                	Dots ++;
                                }
                                
                                if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                	
                                	total_wickets_PP += 1;
                                }
                                break;

                        	case CricketUtil.LOG_ANY_BALL:
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
                                if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            	Fours ++;
		                        }

		                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		                        	Sixes ++;
		                        }
                                break;
                        }
            		} else if(ball_count >= powerplayValues.get(0) && ball_count == powerplayValues.get(1)) {
            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) && !events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
            				switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                    total_run_PP += events.get(i).getEventRuns();
                                    switch(events.get(i).getEventType()) {
                                    case CricketUtil.FOUR:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Fours ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.SIX:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Sixes ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.NINE:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Nines ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.DOT:
    	                        		Dots ++;
    	                        		break;	
    	                            }
                                    break;

                            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                            	case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }else {
                                    	Dots ++;
                                    }
                                    if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                        	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                        	
                                    	total_wickets_PP += 1;
                                    }
                                    break;

                            	case CricketUtil.LOG_ANY_BALL:
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
                                    if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                            	Fours ++;
    		                        }

    		                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    		                        	Sixes ++;
    		                        }
                                    break;
                            }
            			}
            		}
                }
            }
        }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP)+","+Fours+","+Sixes+","+Dots+","+Nines;
    }
	public static String getSecPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0,Fours=0,Sixes=0,Dots = 0,Nines=0;
        int StartOver=0,EndOver=0;
        List<Integer> powerplayValues = new ArrayList<Integer>();
        powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
        
        StartOver = powerplayValues.get(2);
    	EndOver = powerplayValues.get(3);
    	
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
            {
        		if(events.get(i).getEventInningNumber() == inn_num)
                {
        			switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						ball_count = ball_count + 1;
//						System.out.println("event = " + events.get(i).getEventType());
						break;
					case CricketUtil.LOG_WICKET:
						if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                            	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                            	
							ball_count = ball_count + 1;
                        }
						break;
					}
        			
//        			System.out.println("ball_count =  "+ ball_count  +"    StartOver = " + StartOver + "   EndOver = " + EndOver + "   bpo = " + Integer.valueOf(match.getSetup().getBallsPerOver()));
        			if((ball_count >= StartOver) && (ball_count < EndOver)) {
        				if(ball_count == StartOver) {
        					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
        						total_run_PP += events.get(i-1).getEventRuns();
        					}
        				}
        				
//        				System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
            			switch (events.get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                total_run_PP += events.get(i).getEventRuns();
                                switch(events.get(i).getEventType()) {
                                case CricketUtil.FOUR:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Fours ++;
	                        		}
	                        		break;
	                        	case CricketUtil.SIX:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Sixes ++;
	                        		}
	                        		break;
	                        	case CricketUtil.NINE:
	                        		if(events.get(i).getEventWasABoundary() != null && 
	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                        			Nines ++;
	                        		}
	                        		break;
	                        	case CricketUtil.DOT:
	                        		Dots ++;
	                        		break;	
	                            }
                                break;

                            case CricketUtil.WIDE:
                            	if((ball_count == EndOver) && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
                                    break;
                            	}else {
                            		total_run_PP += events.get(i).getEventRuns();
                                    break;
                            	}
                            case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                total_run_PP += events.get(i).getEventRuns();
                                break;
                            case CricketUtil.LOG_WICKET:
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += events.get(i).getEventRuns();
                                }else {
                                	Dots ++;
                                }
                                if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                    	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                    	
                                    total_wickets_PP += 1;
                                }
                                break;

                            case CricketUtil.LOG_ANY_BALL:
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
                                if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            	Fours ++;
		                        }

		                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		                        	Sixes ++;
		                        }
                                break;
                        }
            		} else if((ball_count >= StartOver) && (ball_count == EndOver)) {
            			if(!events.get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !events.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
            				switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                    total_run_PP += events.get(i).getEventRuns();
                                    switch(events.get(i).getEventType()) {
                                    case CricketUtil.FOUR:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Fours ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.SIX:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Sixes ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.NINE:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Nines ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.DOT:
    	                        		Dots ++;
    	                        		break;	
    	                            }
                                    break;

        						case CricketUtil.WIDE: 
        							if((ball_count == EndOver) && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
                                        break;
                                	}else {
                                		total_run_PP += events.get(i).getEventRuns();
                                        break;
                                	}
        						case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;
        						case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }else {
                                    	Dots ++;
                                    }
                                    if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                        	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                        	
                                    	total_wickets_PP += 1;
                                    }
                                    break;

        						case CricketUtil.LOG_ANY_BALL:
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
                                    if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                            	Fours ++;
    		                        }

    		                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    		                        	Sixes ++;
    		                        }
                                    break;
                            }
            			}
            		}  
                }
            }
        }
        System.out.println("total_run_PP = " + total_run_PP);
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP)+","+Fours+","+Sixes+","+Dots+","+Nines;
    }
    public static String getThirdPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
    {
        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0,Fours=0,Sixes=0,Dots=0,Nines=0;
        int StartOver=0,EndOver=0;
        List<Integer> powerplayValues = new ArrayList<Integer>();
        powerplayValues = getBallCountStartAndEndRange(match, match.getMatch().getInning().get(inn_num-1));
        
        StartOver = powerplayValues.get(4);
    	EndOver = powerplayValues.get(5);
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
                {
                	if(events.get(i).getEventInningNumber() == inn_num)
                    {
                		switch(events.get(i).getEventType()) {
    					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
    					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
    						ball_count = ball_count + 1;
    						
    						break;
    					}
                		
                		if((ball_count >= StartOver) && (ball_count <= EndOver)) {
                			if((ball_count == StartOver)) {
            					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
            						total_run_PP += events.get(i-1).getEventRuns();
            					}
            				}
                			//System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
                			switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                                    total_run_PP += events.get(i).getEventRuns();
                                    switch(events.get(i).getEventType()) {
                                    case CricketUtil.FOUR:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Fours ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.SIX:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Sixes ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.NINE:
    	                        		if(events.get(i).getEventWasABoundary() != null && 
    	                        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                        			Nines ++;
    	                        		}
    	                        		break;
    	                        	case CricketUtil.DOT:
    	                        		Dots ++;
    	                        		break;	
    	                            }
                                    break;

                                case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }else {
                                    	Dots ++;
                                    }
                                    if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                        	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                        	
                                    	total_wickets_PP += 1;
                                    }
                                    break;

                                case CricketUtil.LOG_ANY_BALL:
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
                                    if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    	                            	Fours ++;
    		                        }

    		                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
    		                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
    		                        	Sixes ++;
    		                        }
                                    break;
                            }
                		}
                    }
                }
            }
        System.out.println(String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP));
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP)+","+Fours+","+Sixes+","+Dots+","+Nines;
    }

	public static List<String> getSplitBallls(int inning_number, int splitvalue, MatchAllData match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					//System.out.println("Inn Number" + inning_number);
					int max_balls = (match.getSetup().getMaxOvers() * Integer.valueOf(match.getSetup().getBallsPerOver()));
					int count_balls = ((match.getMatch().getInning().get(inning_number-1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) 
							+ match.getMatch().getInning().get(inning_number-1).getTotalBalls());
					
					switch (events.get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE: case CricketUtil.SIX: 
					case CricketUtil.LEG_BYE: case CricketUtil.BYE:
						total_balls = total_balls + 1 ;
						total_runs = total_runs + events.get(i).getEventRuns();
						break;
					case CricketUtil.LOG_WICKET:
						if (events.get(i).getEventRuns() > 0)
                        {
							total_runs = total_runs + events.get(i).getEventRuns();
                        }else {
                        	//Dots ++;
                        }
						if(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                            	!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                            	
							total_balls = total_balls + 1 ;
                        }
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

	public static String getbowlingstyle(String bowlingType) throws InterruptedException {
		
		String text="";
		
		if(bowlingType.charAt(0) == 'L') {
			text = "Left-Arm" ;
		}else {
			text = "Right-Arm" ;
		}
		
		if(bowlingType == "WSL") {
			text = "Left-Arm Wrist Spin";
		}else if(bowlingType == "WSR"){
			text = "Right-Arm Wrist Spin";
		}
		
		switch (bowlingType.substring(1).trim()) {
		case "":
			text = text + " Bowler";
			break;
		case "F":
			text = text + " Fast";
			break;
		case "FM":
			text = text + " Fast-Medium";
			break;
		case "MF":
			text = text + " Medium-Fast";
			break;
		case "M":
			text = text + " Medium";
			break;
		case "SM":
			text = text + " Slow-Medium";
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
			text = "Slow Left-Arm";
			break;
		
		}
		return text;
	}
		
	public static String totalnoballs(List<Event> events,int inn_number)
	{
	    int count_lb = 0;
	    if ((events != null) && (events.size() > 0)) {
	      for (Event evnt : events)
	      {
	    	  if(evnt.getEventInningNumber() == inn_number) {
	    		 
	  	        switch (evnt.getEventType()) {
	  	      case CricketUtil.NO_BALL:
	  	          count_lb += 2;
	  	          break;
	  	        case CricketUtil.LOG_ANY_BALL: 
	  	          if ((evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) && (evnt.getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL))) {
		  	        	count_lb = count_lb + 2 + evnt.getEventSubExtraRuns();
		  	            //exitLoop = true;
		  	      }else if ((evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL))) {
		  	        	count_lb = count_lb + 2;
		  	            //exitLoop = true;
		  	      }
	  	          break;
	  	        }
	    	  }
	      }
	    }
	    return String.valueOf(count_lb);
	}
	
	public static String lastFewOversData(String whatToProcess, List<Event> events,int inn_number)
	{
	    int count_lb = 0;
	    boolean exitLoop = false;
	    if ((events != null) && (events.size() > 0)) {
	    	for(int i = events.size()-1; i>=0; i--) {
	    		if(events.get(i).getEventInningNumber() == inn_number) {
	    			
	    			if(events.get(i).getEventWasABoundary() != null) {
	    				if (((whatToProcess.equalsIgnoreCase(CricketUtil.BOUNDARY)) 
     	  	        		&& (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) 
     	  	        		&& events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) 
     	  	        		|| (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR))
     	  	        		&& events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)
     	  	        		||(events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE))
     	  	        		&& events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
     	    			  break;
     	  	        	}
	    			}
	    			 switch (events.get(i).getEventType()) {
	    			 	
	    			 	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
	    			 		if(events.get(i).getEventWasABoundary() != null) {
	    			 		}else {
	    			 			count_lb += 1;
	    			 		}
	    			 		break;
	    				 
		 	  	        case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.DOT: case CricketUtil.FIVE: case CricketUtil.BYE: 
		 	  	        case CricketUtil.LEG_BYE: case CricketUtil.LOG_WICKET:
		 	  	          count_lb += 1;
		 	  	          break;
		 	  	        case CricketUtil.LOG_ANY_BALL: 
		 	  	          if (((events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) || (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.SIX)) 
		 	  	        		  || (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.NINE))) && (events.get(i).getEventWasABoundary() != null) &&
		 	  	        		  (events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
		 	  	            exitLoop = true;
		 	  	          }
		 	  	          else {
		 	  	        	if(events.get(i).getEventExtra() != null && (!events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) && 
		 	  	        			!events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL))) {
		 	  	        		if(events.get(i).getEventSubExtra() != null && !events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY) 
		 	  	        				&& !events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)
										&& !events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		 	  	        			count_lb += 1;
								}
		 	  	        	}

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
	
	public static String getlastthirtyballsdata(MatchAllData match,String separator,List<Event> events,int number_of_events) {
		
		int total_runs = 0, total_wickets = 0,total_fours=0,total_sixes=0,ball_count = 0;
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch(events.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		        	ball_count = ball_count + 1;
		        	total_runs += events.get(i).getEventRuns();
		        	
		          break;
		          
		        case CricketUtil.FOUR: case CricketUtil.SIX:
		        	ball_count = ball_count + 1;
		        	total_runs += events.get(i).getEventRuns();
		        	
		        	if(events.get(i).getEventWasABoundary() != null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) && 
		        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		        		total_fours = total_fours + 1;
		        	}else if(events.get(i).getEventWasABoundary() != null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) && 
		        			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		        		total_sixes = total_sixes + 1;
		        	}
		          break; 
		        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
		        	total_runs += events.get(i).getEventRuns();
		        	break;
		          
		        case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
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
		        	
		        	if (events.get(i).getEventExtra() != null) {
		        		if(!events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) && 
			        			!events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			        		ball_count = ball_count + 1;
			        	}
		        	}
		        	
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
		          if (((events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) || (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.SIX))) 
	  	        		  && (events.get(i).getEventWasABoundary() != null) &&  (events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
		        	  if(events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) {
			        		total_fours = total_fours + 1;
		        	  }else if(events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.SIX)) {
			        		total_sixes = total_sixes + 1;
		        	  }
	  	          }
		          break;
				}
				if(ball_count >= number_of_events) {
		    		break;
		    	}
			}
		}
			  
		return total_runs + separator + total_wickets + separator + total_fours + separator + total_sixes;	
	}
	public static String compareInningData(MatchAllData match, String separator, int inning_number, List<Event> events) {
		
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
				          
			        case CricketUtil.LOG_50_50:
			        	if(events.get(i).getEventExtra().trim().equalsIgnoreCase("+")) {
			        		total_runs += events.get(i).getEventExtraRuns();
			        	}else if(events.get(i).getEventExtra().trim().equalsIgnoreCase("-")) {
			        		total_runs -= events.get(i).getEventExtraRuns();
			        	}
			        	break;
					}
					if(events.get(i).getEventOverNo() == match.getMatch().getInning().get(1).getTotalOvers() && events.get(i).getEventBallNo() == match.getMatch().getInning().get(1).getTotalBalls()) {
						return total_runs + separator + total_wickets;
					}
				}
			}
		}
		return "";
	} 
	
	public static String compareData(MatchAllData match, int inning_number, List<Event> events,int Over) {
		
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
							&& events.get(i).getEventBallNo() <= 0 && !events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
						break;
		          }
			 }
		    this_ball_data = "";
		    
		    switch (events.get(i).getEventType())
		    {
		    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		    	if(events.get(i).getEventWasABoundary() != null && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		    		this_ball_data = String.valueOf(events.get(i).getEventRuns())+"BOUNDARY";
				    total_runs += events.get(i).getEventRuns();
		    	}else {
		    		this_ball_data = String.valueOf(events.get(i).getEventRuns());
				    total_runs += events.get(i).getEventRuns();
		    	}
		    	break;
		    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		    
		      this_ball_data = String.valueOf(events.get(i).getEventRuns());
		      total_runs += events.get(i).getEventRuns();
		      break;
		    case CricketUtil.NO_BALL: case CricketUtil.PENALTY:
		    	if((events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns()) > 1) {
		    		this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns()) + events.get(i).getEventType();
		    	}else {
		    		this_ball_data = events.get(i).getEventType();
		    	}
		      break;
		    case CricketUtil.BYE: case CricketUtil.LEG_BYE:
		    	this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns()) + events.get(i).getEventType();
		      break;  
		    case CricketUtil.WIDE:
		    	this_ball_data = events.get(i).getEventType();
		      break;  
		    case CricketUtil.LOG_WICKET: 
		      if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
		    	  break;
		      }else {
		    	  if (events.get(i).getEventRuns() > 0) {
			        this_ball_data = String.valueOf(events.get(i).getEventRuns()) +"+"+ events.get(i).getEventType();
			      } else {
			        this_ball_data = events.get(i).getEventType();
			      }
			      total_runs = total_runs + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns();
		      }
		      break;
		    case CricketUtil.LOG_ANY_BALL:
		    	if (events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty()) {
		    		if(!events.get(i).getEventSubExtra().isEmpty()&& events.get(i).getEventSubExtra() != null && events.get(i).getEventSubExtraRuns() > 0) {
		    			if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns());
		    			}
		    			else if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL) && events.get(i).getEventRuns() <= 0) {
		    				this_ball_data = String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
		    						events.get(i).getEventSubExtraRuns());
		    			}
		    		}
		    		if(this_ball_data.isEmpty()) {
		    			if(events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				this_ball_data =events.get(i).getEventExtraRuns()+ events.get(i).getEventExtra();
		    			}
		    			else if(events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		    				if(!events.get(i).getEventSubExtra().isEmpty() && events.get(i).getEventSubExtra() != null && events.get(i).getEventSubExtraRuns() > 0) {
		    					if(events.get(i).getEventRuns()>0) {
				    				this_ball_data = events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns() + "+" + 
				    						events.get(i).getEventSubExtraRuns() + events.get(i).getEventSubExtra();
				    				
				    			}else {
				    				this_ball_data = events.get(i).getEventExtra() + "+" + 
				    						events.get(i).getEventSubExtraRuns() + events.get(i).getEventSubExtra();
				    			}
		    				}else if(!events.get(i).getEventSubExtra().isEmpty()&& events.get(i).getEventSubExtra() != null 
		    						&& events.get(i).getEventSubExtraRuns() <= 0) {
		    					if(events.get(i).getEventRuns()>0) {
				    				this_ball_data = events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns() +
				    						events.get(i).getEventSubExtra();
				    			}else {
				    				this_ball_data = events.get(i).getEventExtra() + "+" + events.get(i).getEventSubExtra();
				    			}
		    				}else {
		    					if(events.get(i).getEventRuns()>0) {
				    				this_ball_data = events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns();
				    			}else {
				    				this_ball_data = events.get(i).getEventExtra();
				    			}
		    				}
		    			}else {
		    				if(events.get(i).getEventRuns()>0) {
			    				this_ball_data = String.valueOf(events.get(i).getEventRuns());
			    			}
		    			}
		    			
		    		}else {
		    			this_ball_data = this_ball_data + "" + events.get(i).getEventExtra();
		    		}
		    	}else {
		    		if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
	    				this_ball_data = String.valueOf(events.get(i).getEventSubExtraRuns()) + CricketUtil.PENALTY;
	    				if(events.get(i).getEventRuns() > 0) {
			    			this_ball_data = this_ball_data + "+" + events.get(i).getEventRuns(); 
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
			this_over = this_over.replace("WIDE", "WD");
			this_over = this_over.replace("NO_BALL", "NB");
			this_over = this_over.replace("LEG_BYE", "LB");
			this_over = this_over.replace("BYE", "B");
			this_over = this_over.replace("PENALTY", "PN");
			this_over = this_over.replace("LOG_WICKET", "W");
			this_over = this_over.replace("WICKET", "W");
		}
		return this_over;
	}
	
	public static Event getLastBallData(List<Event> events) 
	{
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
			    switch (events.get(i).getEventType()) {
			    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			    case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
			    case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
			    	return events.get(i);
			    }
			}
		}
		return null;
	}	

	public static List<OverByOverData> getOverByOverData(MatchAllData match, int inn_num , String type,List<Event> events) 
	{
		List<OverByOverData> over_by_over_data = new ArrayList<OverByOverData>();
		
		int total_runs = 0, total_wickets = 0;
		
		if ((events != null) && (events.size() > 0)) {
			  for (int i = 0; i <=events.size()-1; i++) {
				  if(events.get(i).getEventInningNumber() == inn_num) {
					  switch (events.get(i).getEventType().toUpperCase()) {
//					  	case CricketUtil.WIDE:
//					  		if(!events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) &&
//					  				events.get(i).getEventOverNo() != 0) {
//					  			total_runs = total_runs + events.get(i).getEventRuns();
//					  		}
//					  		break;
					    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT: case CricketUtil.FOUR: 
					    case CricketUtil.SIX: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
					    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL: case CricketUtil.NINE: case CricketUtil.WIDE:
					    	
					    	total_runs = total_runs + events.get(i).getEventRuns();
					    	
					    	switch (events.get(i).getEventType().toUpperCase()) {
						    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
						    	total_runs = total_runs + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns();
						    	
								if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() 
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)
									&& !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
										total_wickets = total_wickets + 1;
								}
								break;
						    }
			  		        break;
					    case CricketUtil.LOG_OVERWRITE_BATSMAN_HOWOUT:
					    	
							if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() 
								&& events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_OUT)) {
									total_wickets = total_wickets + 1;
									//total_runs = total_runs + events.get(i).getEventBattingCard().getRuns();
							}
							break;
							
					    case CricketUtil.CHANGE_BOWLER:
					    	if(type.equalsIgnoreCase("WORM")) {
					    		if(events.get(i).getEventExtra().equalsIgnoreCase("challenge")) {
						    		if(match.getMatch().getInning().get(inn_num-1).getSpecialRuns() != null && 
						    				!match.getMatch().getInning().get(inn_num-1).getSpecialRuns().isEmpty()) {
							    		if(match.getMatch().getInning().get(inn_num-1).getSpecialRuns().startsWith("+")) {
							    			total_runs = total_runs + Integer.parseInt(match.getMatch().getInning().get(inn_num-1).getSpecialRuns().replace("+", ""));
										}else {
											total_runs = total_runs - Integer.parseInt(match.getMatch().getInning().get(inn_num-1).getSpecialRuns().replace("-", ""));
										}
						    		}
						    	}
					    	}
					    	
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
	    		over_by_over_data.add(new OverByOverData(inn_num, events.get(events.size()-1).getEventOverNo(), total_runs, total_wickets, true));
	    		break;
	    	default:
		    	over_by_over_data.add(new OverByOverData(inn_num, 
		    		events.get(events.size()-1).getEventOverNo(), total_runs, total_wickets, false));
	    		break;
	    	}
		}
		return over_by_over_data;
	}
	public static String generateStrikeRate(int runs, int balls, int numberOfDecimals) {
		
		String strike_rate = "";
		if (balls > 0) {
			float sr_val = (100 / (float) balls) * (float) runs;
			switch (numberOfDecimals) {
			case 0: 
				return String.valueOf((int)Math.round(sr_val));
			case 1:
				strike_rate = String.format("%.01f", sr_val);
				break;
			default:
				strike_rate = String.format("%.02f", sr_val);
				break;
			}
		}
		if(strike_rate.contains(".") && strike_rate.split("\\.")[1].charAt(0) == '0') {
			return strike_rate.split("\\.")[0];
		}
		return strike_rate;
	}
	public static String CurrentDayStats(MatchAllData match, String Separator, String whichDay) {
	    switch (whichDay) {
	        case "CURRENT":
	            int Day_num = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size() - 1).getDayNumber();
	            int total_runs = 0, total_wickets = 0, totalBalls = 0;
	            double durationInMinutes = 0;
	            String oversInDay = "",overrate = "",runRate = "-";

	            for (DaySession ds : match.getMatch().getDaysSessions()) {
	                if (ds.getDayNumber() == Day_num) {
	                    total_runs += ds.getTotalRuns();
	                    total_wickets += ds.getTotalWickets();
	                    totalBalls += ds.getTotalBalls();
	                    durationInMinutes += ds.getTotalSeconds() / 60;
	                }
	            }
	            
	            oversInDay = OverBalls(0, totalBalls);
	            overrate = BetterOverRate(0, totalBalls, durationInMinutes, "", false);
	            runRate = generateRunRate(total_runs, 0, totalBalls, 2, match);
	            
	            return oversInDay + Separator + total_runs + Separator + total_wickets + Separator + overrate + Separator + runRate;

	        case "AllDAY":
	            Set<Integer> DayNumbers = match.getMatch().getDaysSessions().stream()
	                    .map(DaySession::getDayNumber)
	                    .collect(Collectors.toSet());

	            List<String> allDayStats = new ArrayList<>();
	            for (Integer dayNumber : DayNumbers) {
	                 total_runs = 0; total_wickets = 0; totalBalls = 0;
	                 durationInMinutes = 0;
	                 oversInDay = "";overrate = "";runRate = "-";

	                for (DaySession ds : match.getMatch().getDaysSessions()) {
	                    if (dayNumber == ds.getDayNumber()) {
	                        total_runs += ds.getTotalRuns();
	                        total_wickets += ds.getTotalWickets();
	                        totalBalls += ds.getTotalBalls();
	                        durationInMinutes += ds.getTotalSeconds() / 60;
	                    }
	                }

	                oversInDay = OverBalls(0, totalBalls);
		            overrate = BetterOverRate(0, totalBalls, durationInMinutes, "", false);
		            runRate = generateRunRate(total_runs, 0, totalBalls, 2, match);
		            
	                allDayStats.add(dayNumber + Separator + oversInDay + Separator + total_runs + Separator + total_wickets + Separator + 
	                		overrate + Separator +  runRate);
	            }

	            return String.join("\n", allDayStats);

	        default:
	            return "Invalid input for 'whichDay'.";
	    }
	}

	public static String generateRunRate(int runs, int overs, int balls, int numberOfDecimals, MatchAllData match) {
		
		String run_rate = "";
		int total_balls = (overs * Integer.valueOf(match.getSetup().getBallsPerOver())) + balls;
		if (total_balls > 0) {
			float run_rate_val = ((float) runs / (float) total_balls) * Integer.valueOf(match.getSetup().getBallsPerOver());
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
	
	public static String generateTossResult(MatchAllData match,String TossType,String DecisionType, String teamNameType, String electedOrChoose) {

		String TeamNameToUse="", decisionText = ""; 
		
		switch (match.getSetup().getTossWinningDecision()) {
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
		case CricketUtil.TEAM_BADGE:
			if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
				TeamNameToUse = match.getSetup().getHomeTeam().getTeamBadge();
			} else {
				TeamNameToUse = match.getSetup().getAwayTeam().getTeamBadge();
			}
		    break;
		case CricketUtil.SHORT:
			if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
				TeamNameToUse = match.getSetup().getHomeTeam().getTeamName4();
			} else {
				TeamNameToUse = match.getSetup().getAwayTeam().getTeamName4();
			}
		    break;
		case CricketUtil.MIDDLE:
			if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
				TeamNameToUse = match.getSetup().getHomeTeam().getTeamName3();
			} else {
				TeamNameToUse = match.getSetup().getAwayTeam().getTeamName3();
			}
		    break;
		default:
			if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
				TeamNameToUse = match.getSetup().getHomeTeam().getTeamName1();
			} else {
				TeamNameToUse = match.getSetup().getAwayTeam().getTeamName1();
			}
		    break;
		}
		switch (TossType) {
		case CricketUtil.MINI:
			return CricketUtil.TOSS + ": " + TeamNameToUse;
		case CricketUtil.SHORT:
			return TeamNameToUse + " won the toss & " + decisionText;
		case "MEDIUM":
			switch (electedOrChoose) {
			case CricketUtil.ELECTED:
				return TeamNameToUse + " won | elected to " + decisionText;	
			default:
				return TeamNameToUse + " won | chose to " + decisionText;	
			}	
		default:
			if(electedOrChoose == null) {
				return TeamNameToUse + " won the toss";
			} else {
				switch (electedOrChoose) {
				case CricketUtil.ELECTED:
					return TeamNameToUse + " won the toss & elected to " + decisionText;
				default:
					return TeamNameToUse + " won the toss & chose to " + decisionText;
				}
			}
		}
	}
	
	public static String getTeamScoreAddBonusRuns(List<Event> sessionEvent, Inning inning, int bowlerId, String slashOrDash, boolean wicketsFirst) throws IOException {
	    //String[] isplCr = new String(Files.readAllBytes(Paths.get(CricketUtil.CRICKET_DIRECTORY + "ISPL_CR.txt"))).trim().split(",");
		
		int crTarget=0, totalRuns=inning.getTotalRuns(), challengeBowlerId = 0, thisOverRuns=0;
		boolean this_50_50 = false;
		
		for(int i=0;i<=sessionEvent.size()-1;i++) {
			if(sessionEvent.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) && 
					sessionEvent.get(i).getEventExtra() != null && !sessionEvent.get(i).getEventExtra().isEmpty() && 
					sessionEvent.get(i).getEventExtra().equalsIgnoreCase("challenge")) {
				crTarget = Integer.valueOf(sessionEvent.get(i).getEventSubExtra());
				this_50_50 = true;
				challengeBowlerId = sessionEvent.get(i).getEventBowlerNo();
				break;
			}
		}
		
		if(challengeBowlerId == bowlerId) {
			thisOverRuns = Integer.parseInt(CricketFunctions.processThisOverRunsCount(bowlerId, sessionEvent).split("-")[0]);
			if(this_50_50 && crTarget <= thisOverRuns) {
		    	if(inning.getSpecialRuns() != null && !inning.getSpecialRuns().trim().isEmpty()) {
					if(inning.getSpecialRuns().startsWith("+")) {
						totalRuns = inning.getTotalRuns() + Integer.parseInt(inning.getSpecialRuns().replace("+", ""));
					}
				} else {
					totalRuns = inning.getTotalRuns();
				}
		    }
		    else if(this_50_50 && crTarget > thisOverRuns) {
		    	if(sessionEvent.get(sessionEvent.size()-1).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
		    		totalRuns = inning.getTotalRuns() - Integer.parseInt(inning.getSpecialRuns().replace("-", ""));
		    		//totalRuns = inning.getTotalRuns() - (thisOverRuns/2);
		    		this_50_50 = false;
		    	}
		    }
		}else {
			if(inning.getSpecialRuns() != null && !inning.getSpecialRuns().trim().isEmpty()) {
				if(inning.getSpecialRuns().startsWith("+")) {
					totalRuns = inning.getTotalRuns() + Integer.parseInt(inning.getSpecialRuns().replace("+", ""));
				}else {
					totalRuns = inning.getTotalRuns() - Integer.parseInt(inning.getSpecialRuns().replace("-", ""));
				}
			} else {
				totalRuns = inning.getTotalRuns();
			}
		}
	    
	    if (inning.getTotalWickets() >= 10) {
	        return String.valueOf(totalRuns);
	    }
	    
	    return wicketsFirst ? inning.getTotalWickets() + slashOrDash + totalRuns
	                        : totalRuns + slashOrDash + inning.getTotalWickets();
	}

	public static String getTeamScore(Inning inning, String slashOrDash, boolean wicketsFirst){
		if(inning.getTotalWickets() >= 10) {
			return String.valueOf(inning.getTotalRuns());
		} else{
			if(wicketsFirst == true) {
				return String.valueOf(inning.getTotalWickets()) + slashOrDash + String.valueOf(inning.getTotalRuns());
			} else {
				return String.valueOf(inning.getTotalRuns()) + slashOrDash + String.valueOf(inning.getTotalWickets());
			}
		}
	}
	
	public static String Plural(int count){
		if (count == 1){
			return "";
		} else{
			return "s";
		}
	}
	
//	public static String getnewBallOver(MatchAllData match, int whichInning) {
//		int day=0,totalBall=0,InningBall=0,newBall=0;
//		
//		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)) {
//			if(match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getIsCurrentSession().equalsIgnoreCase(CricketUtil.YES)) {
//				day = match.getMatch().getDaysSessions().get(match.getMatch().getDaysSessions().size()-1).getDayNumber();
//				if(match.getSetup().getOverRemainingNewBall().get(day-1).getNewBallOver().contains(".")) {
//					totalBall = ((Integer.valueOf(match.getSetup().getOverRemainingNewBall().get(day-1).getNewBallOver().split("\\.")[0])*6)
//								+ Integer.valueOf(match.getSetup().getOverRemainingNewBall().get(day-1).getNewBallOver().split("\\.")[1]));
//				}else {
//					totalBall = (Integer.valueOf(match.getSetup().getOverRemainingNewBall().get(day-1).getNewBallOver())*6);
//				}
//			}
//			InningBall = ((match.getMatch().getInning().get(whichInning-1).getTotalOvers()*6) + match.getMatch().getInning().get(whichInning-1).getTotalBalls());
//			
//			newBall = totalBall - InningBall;
//		}
//		if(newBall > 0) {
//			return OverBalls(0, newBall);
//		}else {
//			return null;
//		}	
//	}
//
//	public static int GetTargetRuns(MatchAllData match) 
//	{
//		if(match.getSetup().getTargetRuns() > 0) {
//			return match.getSetup().getTargetRuns();
//		} else {
//			return match.getMatch().getInning().get(0).getTotalRuns() + 1;
//		}
//	}
//
//	public static String GetTargetOvers(MatchAllData match) 
//	{
//		if(match.getSetup().getMaxOvers() <= 0) {
//			return match.getMatch().getMatchFinishTime().getMax_overs();
//		} else {
//			if(match.getSetup().getTargetOvers() == null || match.getSetup().getTargetOvers().trim().isEmpty()) {
//				return String.valueOf(match.getSetup().getMaxOvers());
//			} else {
//				return match.getSetup().getTargetOvers();
//			}
//		}
//	}
//
//	public static int GetRequiredRuns(MatchAllData match) {
//		int requiredRuns = 0;
//		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
//			 requiredRuns = getTargetRuns(match);	
//		}else {
//			 requiredRuns = getTargetRuns(match) - match.getMatch().getInning().get(1).getTotalRuns();
//		}
//		
//		if(requiredRuns <= 0) {
//			requiredRuns = 0;
//		}
//		return requiredRuns;
//	}
//
//	public static int GetRequiredBalls(MatchAllData match) {
//		int requiredBalls;
//		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
//			if(getTargetOvers(match).contains("\\.")) {
//				requiredBalls = ((Integer.valueOf(getTargetOvers(match).split("\\.")[0]) * Integer.valueOf(match.getSetup().getBallsPerOver())) + Integer.valueOf(getTargetOvers(match).split("\\.")[1])) 
//					- (match.getMatch().getInning().get(3).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(3).getTotalBalls();
//			} else {
//				requiredBalls = ((Integer.valueOf(getTargetOvers(match)) * Integer.valueOf(match.getSetup().getBallsPerOver()))) 
//					- (match.getMatch().getInning().get(3).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(3).getTotalBalls();
//			}
//		}else {
//			if(getTargetOvers(match).contains("\\.")||getTargetOvers(match).contains(".")) {
//				requiredBalls = ((Integer.valueOf(getTargetOvers(match).split("\\.")[0]) * Integer.valueOf(match.getSetup().getBallsPerOver())) + Integer.valueOf(getTargetOvers(match).split("\\.")[1])) 
//					- (match.getMatch().getInning().get(1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(1).getTotalBalls();
//			} else {
//				requiredBalls = ((Integer.valueOf(getTargetOvers(match)) * Integer.valueOf(match.getSetup().getBallsPerOver()))) 
//					- (match.getMatch().getInning().get(1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(1).getTotalBalls();
//			}
//		}
//		
//		if(requiredBalls <= 0) {
//			requiredBalls = 0;
//		}
//		return requiredBalls;
//	}

	public static int getWicketsLeft(MatchAllData match, int whichInning) {
		
		int wicketsLeft = 0;
		
		if(match.getSetup().getMaxOvers() == 1) {
			wicketsLeft = 2 - (match.getMatch().getInning().get(whichInning-1).getTotalWickets()); 
		} else {
			wicketsLeft = 10 - (match.getMatch().getInning().get(whichInning-1).getTotalWickets()); 
		}
		
		if(wicketsLeft <= 0) {
			wicketsLeft = 0;
		}
		
		return wicketsLeft;
	}

	public static int GetTeamRunsAhead(int inning_number, MatchAllData matchAllData)
	{
		int teamTotalRuns = 0;
		if(inning_number > 0 && inning_number > matchAllData.getMatch().getInning().size()) {
			return teamTotalRuns;
		} else {
		    for(int inn = 1; inn <= inning_number; inn++) {
		    	if(matchAllData.getMatch().getInning().get(inn - 1).getBattingTeamId() 
		    		== matchAllData.getMatch().getInning().get(inning_number - 1).getBattingTeamId()) 
		    	{
					if(matchAllData.getSetup().getSpecialMatchRules() != null && matchAllData.getSetup().getSpecialMatchRules().equalsIgnoreCase(CricketUtil.ISPL)
						&& matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns() != null 
						&& !matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().trim().isEmpty()) 
					{
						if(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().startsWith("+")) {
				    		teamTotalRuns = teamTotalRuns + matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns()
				    			+ Integer.parseInt(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().replace("+", ""));
						}else if(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().startsWith("-")) {
				    		teamTotalRuns = teamTotalRuns + matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns()
				    			- Integer.parseInt(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().replace("-", ""));
						}
					} else {
			    		teamTotalRuns = teamTotalRuns + matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns();
					}
		    	}else {
					if(matchAllData.getSetup().getSpecialMatchRules() != null && matchAllData.getSetup().getSpecialMatchRules().equalsIgnoreCase(CricketUtil.ISPL)
						&& matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns() != null 
						&& !matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().trim().isEmpty()) 
					{
						if(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().startsWith("+")) {
				    		teamTotalRuns = teamTotalRuns - matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns()
				    			+ Integer.parseInt(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().replace("+", ""));
						}else if(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().startsWith("-")) {
				    		teamTotalRuns = teamTotalRuns - matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns()
				    			- Integer.parseInt(matchAllData.getMatch().getInning().get(inn - 1).getSpecialRuns().replace("-", ""));
						}
					} else {
			    		teamTotalRuns = teamTotalRuns - matchAllData.getMatch().getInning().get(inn - 1).getTotalRuns();
					}
		    	}
		    }
		}
		return teamTotalRuns;
	}
	
//	public static String generateMatchSummaryStatus(int whichInning, MatchAllData match, String teamNameType, String broadcaster) 
//	{
//		String matchSummaryStatus = generateMatchResult(match, teamNameType);
//		System.out.println("match.getMatch().getMatchResult() = " + match.getMatch().getMatchResult());
//
//	    if(matchSummaryStatus.trim().isEmpty()) {
//	    	
//	    	int lead_by = GetTeamRunsAhead(whichInning,match);
//			String batTeamNm = "", bowlTeamNm = "";
//
//			switch (teamNameType) {
//			case CricketUtil.TEAMNAME_3:
//				batTeamNm = match.getMatch().getInning().get(whichInning - 1).getBatting_team().getTeamName3();
//		    	bowlTeamNm = match.getMatch().getInning().get(whichInning - 1).getBowling_team().getTeamName3();
//		    	break;
//		    case CricketUtil.SHORT: 
//		    	batTeamNm = match.getMatch().getInning().get(whichInning - 1).getBatting_team().getTeamName4();
//		    	bowlTeamNm = match.getMatch().getInning().get(whichInning - 1).getBowling_team().getTeamName4();
//		    	break;
//		    default: 
//		    	batTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBatting_team().getTeamName1();
//		    	bowlTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBowling_team().getTeamName1();
//		    	break;
//		    }
//	    	
//		    switch (whichInning) {
//		    case 1: 
//		    	if (((match.getMatch().getInning().get(whichInning - 1)).getTotalRuns() > 0) || 
//		  		      ((match.getMatch().getInning().get(whichInning - 1)).getTotalOvers() > 0) || 
//		  		      ((match.getMatch().getInning().get(whichInning - 1)).getTotalBalls() > 0)) {
//		  		      return "Current Run Rate " + match.getMatch().getInning().get(whichInning - 1).getRunRate();
//		  		    }
//		    	else {
//		    		return CricketFunctions.generateTossResult(match, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, CricketUtil.CHOSE);
//		    	}
//		    case 2: case 3:
//		    	
//		    	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST) 
//		    		|| match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.FC)) {
//
//		    		if(lead_by >= 0) {
//			    		if(lead_by > 0) {
//					    	matchSummaryStatus = batTeamNm + " lead by " + lead_by + " run" + Plural(lead_by);
//			    		} else if(lead_by == 0) {
//					    	matchSummaryStatus = "Scores are level";
//			    		}
//		    		} else {
//		    			if(whichInning == 3 && CricketFunctions.getWicketsLeft(match,whichInning) <= 0) {
//		    				matchSummaryStatus = batTeamNm + " win by innings & " + (-1 * lead_by) + " run" + Plural(-1 * lead_by);
//		    			} else {
//					    	matchSummaryStatus = batTeamNm + " trail by " + (-1 * lead_by) + " run" + Plural(-1 * lead_by);
//		    			}
//		    		}
//		    		
//		    	} else {
//		    		
//				    if ((CricketFunctions.getRequiredRuns(match) > 0) && (CricketFunctions.getRequiredBalls(match) > 0) 
//				    		&& (CricketFunctions.getWicketsLeft(match,whichInning) > 0)) {
//
//				    	switch (broadcaster.toUpperCase()) {
//						case "ICC_BIGSCREEN_DOAD_SCORING":
//							matchSummaryStatus = "need " + CricketFunctions.getRequiredRuns(match) + 
//				        		" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " off ";
//							break;
//
//						default:
//							matchSummaryStatus = batTeamNm + " need " + CricketFunctions.getRequiredRuns(match) + 
//				        	" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " to win from ";
//							break;
//						}
//				    	
//				    	if (CricketFunctions.getRequiredBalls(match) > 120) {
//				    		matchSummaryStatus = matchSummaryStatus + CricketFunctions.OverBalls(0,CricketFunctions.getRequiredBalls(match)) + " overs";
//						} else {
//							if(match.getSetup().getReducedOvers() != 0) {
//								matchSummaryStatus = matchSummaryStatus + (match.getSetup().getReducedOvers()*6) + 
//										" ball" + CricketFunctions.Plural((match.getSetup().getReducedOvers()*6));
//							}else {
//								matchSummaryStatus = matchSummaryStatus + CricketFunctions.getRequiredBalls(match) + 
//										" ball" + CricketFunctions.Plural(CricketFunctions.getRequiredBalls(match));
//							}
//						}
//				    } else if (CricketFunctions.getRequiredRuns(match) <= 0)
//				    {
//				    	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
//				    		matchSummaryStatus = batTeamNm + " win by super over";
//						}else {
//							matchSummaryStatus = batTeamNm + " win by " + CricketFunctions.getWicketsLeft(match,whichInning) + 
//						    	" wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning));
//						}
//				    }
//				    else if (CricketFunctions.getRequiredRuns(match) == 1 && (CricketFunctions.getRequiredBalls(match) <= 0 
//				    	|| CricketFunctions.getWicketsLeft(match,whichInning) <= 0)) 
//				    {
//				    	switch (broadcaster) {
//						case "ICC_BIG_SCREEN":
//							matchSummaryStatus = "Match tied - winner will be decided by super over";
//							break;
//						case "DOAD_LLC":
//							matchSummaryStatus = "Match tied - super over to follow";
//							break;
//						default:
//					    	matchSummaryStatus = "Match tied - winner will be decided by super over";
//							break;
//						}
//				    } 
//				    else 
//				    {
//				    	matchSummaryStatus = bowlTeamNm + " win by " + (CricketFunctions.getRequiredRuns(match) - 1) + 
//				    		" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match) - 1);
//				    }
//				    
//				    if(!matchSummaryStatus.contains("Match tied -")) {
//				    	if(match.getSetup().getTargetType() != null) {
//							if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
//								matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.DLS + ")";
//							}else if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
//								matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.VJD + ")";
//							}
//					    }
//				    }
//		    	}
//		    	break;
//		    	
//		    case 4:
//		    	
//		    	if((1 - lead_by) <= 0) {
//		    		matchSummaryStatus = batTeamNm + " win by " + CricketFunctions.getWicketsLeft(match,whichInning) 
//		    			+ " wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning));
//		    	} else {
//		    		if(CricketFunctions.getWicketsLeft(match,whichInning) <= 0) {
//		    			if(CricketFunctions.getRequiredRuns(match) == 1) {
//		    				matchSummaryStatus = "match tied";
//		    			} else {
//		    				matchSummaryStatus = bowlTeamNm + " win by " + (CricketFunctions.getRequiredRuns(match) - 1) + 
//					    		" run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match) - 1);
//		    			}
//		    		} else {
//			    		if(match.getMatch().getInning().get(whichInning - 1).getTotalRuns() == 0) {
//					    	matchSummaryStatus = batTeamNm + " need " + (1 - lead_by) + " run" + CricketFunctions.Plural((1 - lead_by)) + " to win";
//			    		} else {
//					    	matchSummaryStatus = batTeamNm + " need " + (1 - lead_by) + " run" + CricketFunctions.Plural((1 - lead_by)) + " to win";
//			    		}
//		    		}
//		    	}
//		    	break;
//		    }
//	    }
//		System.out.println("matchSummaryStatus AFTER = " + matchSummaryStatus);
//	    return matchSummaryStatus;
//	}
	
	public static TargetData GenerateMatchSummaryStatus(int whichInning, MatchAllData match, String teamNameType, 
		String SplitSummaryText, String broadcaster, boolean ballsRemaining) 
	{
		TargetData targetData = new TargetData();
		
		if(match.getMatch().getInning().get(0).getTotalRuns() > 0 || (6 * match.getMatch().getInning().get(0).getTotalOvers() 
			+ match.getMatch().getInning().get(0).getTotalBalls()) > 0) 
		{
			targetData = new TargetData(GenerateMatchResult(match, teamNameType, broadcaster, 
				SplitSummaryText, ballsRemaining));
			if(!targetData.getTargetOrResult().trim().isEmpty()) {
				
				targetData.setMatchFinished(true);
				
			} else {
				
		    	int lead_by = GetTeamRunsAhead(whichInning,match);
	    		targetData = GetTargetData(match);
				String batTeamNm = "", bowlTeamNm = "";

				switch (teamNameType) {
			    case CricketUtil.SHORT: 
			    	batTeamNm = match.getMatch().getInning().get(whichInning - 1).getBatting_team().getTeamName4();
			    	bowlTeamNm = match.getMatch().getInning().get(whichInning - 1).getBowling_team().getTeamName4();
			    	break;
			    case CricketUtil.MIDDLE: 
			    	batTeamNm = match.getMatch().getInning().get(whichInning - 1).getBatting_team().getTeamName3();
			    	bowlTeamNm = match.getMatch().getInning().get(whichInning - 1).getBowling_team().getTeamName3();
			    	break;
			    default: 
			    	batTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBatting_team().getTeamName1();
			    	bowlTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBowling_team().getTeamName1();
			    	break;
			    }
				
			    switch (whichInning) {
			    case 1:
			    	if ((match.getMatch().getInning().get(whichInning - 1).getTotalRuns() > 0) || 
			  		      (match.getMatch().getInning().get(whichInning - 1).getTotalOvers() > 0) || 
			  		      (match.getMatch().getInning().get(whichInning - 1).getTotalBalls() > 0)) {
			    		targetData.setTargetOrResult("Current Run Rate " + (match.getMatch().getInning().get(0)).getRunRate());
			  		    }
			    	else {
			    		targetData.setTargetOrResult(CricketFunctions.generateTossResult(match, CricketUtil.FULL, 
			    			CricketUtil.FIELD, CricketUtil.FULL, CricketUtil.CHOSE));
			    	}
			    	break;
			    	
			    case 2: case 3:
			    	
			    	if(match.getSetup().getMaxOvers() <= 0) { //Test & FC matches

			    		if(lead_by >= 0) {
				    		if(lead_by > 0) {
				    			targetData.setTargetOrResult(batTeamNm + " lead by " + lead_by + " run" + Plural(lead_by));
				    		} else if(lead_by == 0) {
				    			targetData.setTargetOrResult("Scores are level");
				    		}
			    		} else {
			    			if(whichInning == 3 && CricketFunctions.getWicketsLeft(match,whichInning) <= 0) {
			    				targetData.setTargetOrResult(bowlTeamNm + " win by innings & " + (-1 * lead_by) 
			    					+ " run" + Plural(-1 * lead_by));
			    				targetData.setMatchFinished(true);
			    			} else {
			    				targetData.setTargetOrResult(batTeamNm + " trail by " + (-1 * lead_by) 
			    					+ " run" + Plural(-1 * lead_by));
			    			}
			    		}
				    		
			    	} else { //Limited overs matches
			    		
					    if (targetData.getRemaningRuns() > 0 && targetData.getRemaningBall() > 0 && (CricketFunctions.getWicketsLeft(match,whichInning) > 0)) {
					    	
					    	switch (broadcaster) {
					    	case "ICC-U19-2023":
					    		if(targetData.getRemaningRuns() == 1) {
					    			targetData.setTargetOrResult(batTeamNm + " need " + targetData.getRemaningRuns() + 
								        " run" + CricketFunctions.Plural(targetData.getRemaningRuns()) + " to win from ");
						    	}else {
						    		targetData.setTargetOrResult(batTeamNm + " need " + targetData.getRemaningRuns() + 
								        " more run" + CricketFunctions.Plural(targetData.getRemaningRuns()) + " to win from ");
						    	}
					    		break;
					    	default:
					    		targetData.setTargetOrResult(batTeamNm + " need " + targetData.getRemaningRuns() + 
					        		" run" + CricketFunctions.Plural(targetData.getRemaningRuns()) + " to win from ");
					    		break;
					    	}
					    	
					    	if (targetData.getRemaningBall() > 120) {
					    		targetData.setTargetOrResult(targetData.getTargetOrResult() 
					    			+ CricketFunctions.OverBalls(0,targetData.getRemaningBall()) + " overs");
							} else {
								targetData.setTargetOrResult(targetData.getTargetOrResult() + targetData.getRemaningBall() + 
									" ball" + CricketFunctions.Plural(targetData.getRemaningBall()));
							}
					    } 
					    else if (targetData.getRemaningRuns() <= 0) 
					    {
					    	switch (broadcaster) {
							case "ICC-U19-2023": case "T20_MUMBAI":
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult(batTeamNm + " beat " + bowlTeamNm + " in the super over");
									} else {
										targetData.setTargetOrResult(batTeamNm + " beat " + bowlTeamNm 
											+ SplitSummaryText + "in the super over");
									}
								}else {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult(batTeamNm + " beat " + bowlTeamNm + " by " + CricketFunctions.getWicketsLeft(match,whichInning) + 
									    		" wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning)));
									} else {
										targetData.setTargetOrResult(batTeamNm + " beat " + bowlTeamNm + SplitSummaryText 
											+ "by " + CricketFunctions.getWicketsLeft(match,whichInning) + " wicket" 
											+ CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning)));
									}
								}
								targetData.setMatchFinished(true);
								break;
							default:
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									targetData.setTargetOrResult(batTeamNm + " win the super over");
								} else {
									targetData.setTargetOrResult(batTeamNm + " win by " + CricketFunctions.getWicketsLeft(match,whichInning) + 
							    		" wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning))); 
									if(ballsRemaining) {
										if(targetData.getRemaningBall() > 0) {
											if(targetData.getRemaningBall() <= 120) {
												targetData.setTargetOrResult(targetData.getTargetOrResult() + " with " + targetData.getRemaningBall() 
													+ " ball" + CricketFunctions.Plural(targetData.getRemaningBall()) + " remaining");
											}else {
												targetData.setTargetOrResult(targetData.getTargetOrResult() + " with " + 
													CricketFunctions.OverBalls(0, targetData.getRemaningBall()) + " overs remaining");
											}
										}
									}
								}
								targetData.setMatchFinished(true);
								break;
							}
					    }
					    else if (targetData.getRemaningRuns() == 1 && (targetData.getRemaningBall() <= 0 
					    	|| CricketFunctions.getWicketsLeft(match,whichInning) <= 0)) {
					    	switch (broadcaster) {
							case "ICC-U19-2023": case "T20_MUMBAI": case "NPL": case "BENGAL-T20": case "ISPL":
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									targetData.setTargetOrResult("Super Over tied - Another super over to follow");
								}else {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult("Match tied - super over to follow");
									} else {
										targetData.setTargetOrResult("Match tied" + SplitSummaryText + "super over to follow");
									}
								}
								break;
							default:
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									targetData.setTargetOrResult("Super Over tied - Another super over to follow");
								}else {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult("Match tied - winner will be decided by super over");
									} else {
										targetData.setTargetOrResult("Match tied" + SplitSummaryText + "winner will be decided by super over");
									}
								}
								break;
							}
							targetData.setMatchFinished(true);
					    } 
					    else 
					    {
					    	switch (broadcaster) {
							case "ICC-U19-2023": case "T20_MUMBAI":
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult(bowlTeamNm + " beat " + batTeamNm + " in the super over");
									} else {
										targetData.setTargetOrResult(bowlTeamNm + " beat " + batTeamNm + SplitSummaryText + "in the super over");
									}
								}else {
									if(SplitSummaryText.isEmpty()) {
										targetData.setTargetOrResult(bowlTeamNm + " beat " + batTeamNm + " by " + (targetData.getRemaningRuns() - 1) + 
								    		" run" + CricketFunctions.Plural(targetData.getRemaningRuns() - 1));
									} else {
										targetData.setTargetOrResult(bowlTeamNm + " beat " + batTeamNm + SplitSummaryText + "by " + (targetData.getRemaningRuns() - 1) + 
								    		" run" + CricketFunctions.Plural(targetData.getRemaningRuns() - 1));
									}
								}
								targetData.setMatchFinished(true);
								break;
							default:
								if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
									targetData.setTargetOrResult(bowlTeamNm + " win the super over");
								} else {
									targetData.setTargetOrResult(bowlTeamNm + " win by " + (targetData.getRemaningRuns() - 1) + 
							    		" run" + CricketFunctions.Plural(targetData.getRemaningRuns() - 1));
								}
								targetData.setMatchFinished(true);
								break;
							}
					    }
					    if(match.getSetup().getTargetType() != null) {
							if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
								targetData.setTargetOrResult(targetData.getTargetOrResult() + " (" + CricketUtil.DLS + ")");
							}else if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
								targetData.setTargetOrResult(targetData.getTargetOrResult() + " (" + CricketUtil.VJD + ")");
							}
					    }
			    	}
			    	break;
			    case 4:
			    	if((1 - lead_by) <= 0) {
			    		targetData.setTargetOrResult(batTeamNm + " win by " + CricketFunctions.getWicketsLeft(match,whichInning) 
			    			+ " wicket" + CricketFunctions.Plural(CricketFunctions.getWicketsLeft(match,whichInning)));
						targetData.setMatchFinished(true);
			    	} else {
			    		if(CricketFunctions.getWicketsLeft(match,whichInning) <= 0) {
			    			if(targetData.getRemaningRuns() == 1) {
			    				targetData.setTargetOrResult("match tied");
			    			} else {
			    				targetData.setTargetOrResult(bowlTeamNm + " win by " + (targetData.getRemaningRuns() - 1) + 
						    		" run" + CricketFunctions.Plural(targetData.getRemaningRuns() - 1));
			    			}
			    			targetData.setMatchFinished(true);
			    		} else {
				    		if(match.getMatch().getInning().get(whichInning - 1).getTotalRuns() == 0) {
				    			targetData.setTargetOrResult(batTeamNm + " need " + (1 - lead_by) + " run" + CricketFunctions.Plural((1 - lead_by)) + " to win");
				    		} else {
				    			targetData.setTargetOrResult(batTeamNm + " need " + (1 - lead_by) + " run" + CricketFunctions.Plural((1 - lead_by)) + " to win");
				    		}
			    		}
			    	}		    	
			    	break;
			    }
			}
		} else {
    		targetData.setTargetOrResult(CricketFunctions.generateTossResult(match, CricketUtil.FULL, 
       			CricketUtil.FIELD, CricketUtil.FULL, CricketUtil.CHOSE));
		}
	    return targetData;
	}	
	
	public static String playerStyle(String ProfileType,String bat_ball_style) {
		String return_value="";
		
		switch(ProfileType) {
		case CricketUtil.BATSMAN:
			if(bat_ball_style.equalsIgnoreCase("RHB")) {
				return_value= "RIGHT HANDED BATTER" ;
			}else if(bat_ball_style.equalsIgnoreCase("LHB")) {
				return_value= "LEFT HANDED BATTER" ;
			}
			break;
		
		case CricketUtil.BOWLER:
			
			if(bat_ball_style.equalsIgnoreCase("RF")) {
				return_value = "RIGHT ARM FAST" ;
			}else if(bat_ball_style.equalsIgnoreCase("RFM")) {
				return_value= "RIGHT ARM FAST MEDIUM" ;
			}else if(bat_ball_style.equalsIgnoreCase("RMF")) {
				return_value= "RIGHT ARM MEDIUM FAST" ;
			}else if(bat_ball_style.equalsIgnoreCase("RM")) {
				return_value= "RIGHT ARM MEDIUM" ;
			}else if(bat_ball_style.equalsIgnoreCase("RSM")) {
				return_value= "RIGHT ARM SLOW MEDIUM" ;
			}else if(bat_ball_style.equalsIgnoreCase("ROB")) {
				return_value= "RIGHT ARM OFF-BREAK" ;
			}else if(bat_ball_style.equalsIgnoreCase("RLB")) {
				return_value= "RIGHT ARM LEG-BREAK" ;
			}
			else if(bat_ball_style.equalsIgnoreCase("RAB")) {
				return_value= "RIGHT ARM BOWLER" ;
			}
			else if(bat_ball_style.equalsIgnoreCase("LAB")) {
				return_value= "LEFT ARM BOWLER";
			}
			else if(bat_ball_style.equalsIgnoreCase("LF")) {
				return_value= "LEFT ARM FAST" ;
			}else if(bat_ball_style.equalsIgnoreCase("LFM")) {
				return_value= "LEFT ARM FAST MEDIUM" ;
			}else if(bat_ball_style.equalsIgnoreCase("LMF")) {
				return_value= "LEFT ARM MEDIUM FAST" ;
			}else if(bat_ball_style.equalsIgnoreCase("LM")) {
				return_value= "LEFT ARM MEDIUM" ;
			}else if(bat_ball_style.equalsIgnoreCase("LSL")) {
				return_value= "SLOW LEFT ARM" ;
			}else if(bat_ball_style.equalsIgnoreCase("WSL")) {
				return_value= "LEFT ARM WRIST SPIN" ;
			}else if(bat_ball_style.equalsIgnoreCase("LCH")) {
				return_value= "LEFT ARM CHINAMAN" ;
			}else if(bat_ball_style.equalsIgnoreCase("RLG")) {
				return_value= "RIGHT ARM LEG-BREAK" ;
			}else if(bat_ball_style.equalsIgnoreCase("WSR")) {
				return_value= "RIGHT ARM WRIST SPIN" ;
			}else if(bat_ball_style.equalsIgnoreCase("LSO")) {
				return_value= "LEFT ARM ORTHODOX" ;
			}
			break;
		}
		return return_value ;
	}
	
	public static String getPowerPlayScore(Inning inning,int inn_number, String seperator,MatchAllData match) {
		
		int total_run_PP=0, total_wickets_PP=0,ball_count = 0;
		List<Integer> powerplayValues = new ArrayList<Integer>();
		
		if((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
			for(int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_number) {
					powerplayValues = getBallCountStartAndEndRange(match, inning);
					switch(match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.BYE: case CricketUtil.LEG_BYE:  //case CricketUtil.LOG_ANY_BALL:
						ball_count = ball_count + 1;
						break;
					case CricketUtil.LOG_ANY_BALL:
						if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
							if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY) && 
									match.getEventFile().getEvents().get(i).getDoNotIncrementBall().equalsIgnoreCase(CricketUtil.NO)) {
								ball_count = ball_count + 1;
							}
						}
						break;
					case CricketUtil.LOG_WICKET:
						if(!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                            	!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                            	
							ball_count = ball_count + 1;
                        }
						break;
					}
 
					if(ball_count >= powerplayValues.get(0) && ball_count < powerplayValues.get(1)) {
						switch(match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
							break;
						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
							break;
						case CricketUtil.LOG_WICKET:
							if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                            {
                                total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
                            }
							if(!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                	!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                	
                            	total_wickets_PP += 1;
                            }
							break;
						case CricketUtil.LOG_ANY_BALL:
							total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
							if (match.getEventFile().getEvents().get(i).getEventExtra() != null) {
								total_run_PP += match.getEventFile().getEvents().get(i).getEventExtraRuns();
							}
							if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null) {
								total_run_PP += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
							}
							if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty()) {
								total_wickets_PP += 1;
							}
							break;
						}
					}else if(ball_count >= powerplayValues.get(0) && ball_count == powerplayValues.get(1)){
						if(match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
							break;
						}else if(!match.getEventFile().getEvents().get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
            				switch (match.getEventFile().getEvents().get(i).getEventType())
                            {
                            case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    						case CricketUtil.FOUR: case CricketUtil.SIX: 
                                total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
                                break;

    						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
                                break;

    						case CricketUtil.LOG_WICKET:
                                if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
                                }
                                if(!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) && 
                                    	!match.getEventFile().getEvents().get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)) {
                                    	
                                	total_wickets_PP += 1;
                                }
                                break;

    						case CricketUtil.LOG_ANY_BALL:
                                total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
                                if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                                {
                                    total_run_PP += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                                }
                                if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                                {
                                    total_run_PP += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                                }
                                if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
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
		return String.valueOf(total_run_PP) + seperator + String.valueOf(total_wickets_PP);
	}
	
	public static String previousBowler(MatchAllData match ,List<Event> events) {
		String bowler="";
		if((events != null) && (events.size() > 0)) {
			
			for(int i = events.size() - 1; i >= 0; i--) {
				if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER))) {
					for(Inning inn : match.getMatch().getInning()) {
						if(inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId() == events.get(i).getEventBowlerNo()) {
									bowler = boc.getPlayer().getTicker_name() + ',' + boc.getWickets() + '-' + boc.getRuns() + ',' + boc.getDots() + ',' +
										boc.getEconomyRate() + ',' + OverBalls(boc.getOvers(), boc.getBalls()) + ',' + boc.getPlayerId();
								}
							}
						}
					}
					break;
				}
			}
		}
		return bowler;
	}
	public static String otherBowler(MatchAllData match ,List<Event> events) {
		String bowler="";
		if((events != null) && (events.size() > 0)) {
			
			for(int i = events.size() - 2; i >= 0; i--) {
				if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER))) {
					for(Inning inn : match.getMatch().getInning()) {
						if(inn.getIsCurrentInning().equalsIgnoreCase("YES")) {
							for(BowlingCard boc : inn.getBowlingCard()) {
								if(boc.getPlayerId() == events.get(i).getEventBowlerNo()) {
									bowler = boc.getPlayer().getTicker_name() + ',' + boc.getWickets() + '-' + boc.getRuns() + ',' + boc.getDots() + ',' +
											boc.getEconomyRate() + ',' + OverBalls(boc.getOvers(), boc.getBalls());
								}
							}
						}
					}
					break;
				}
			}
		}
		return bowler;
	}
	
	public static String PreOtherRunWicket(int playerId , int inningNumber, String Seperator,MatchAllData match ,List<Event> events) {
		int run_count = 0,wicket_count = 0;
		if((events != null) && (events.size() > 0)) {
			for(int i = events.size() - 1; i >= 0; i--) {
				if (events.get(i).getEventInningNumber() == inningNumber) {
					if(match.getMatch().getInning().get(inningNumber - 1).getTotalOvers() > 0 || match.getMatch().getInning().get(inningNumber - 1).getTotalBalls() > 0) {
						if(playerId == events.get(i).getEventBowlerNo()) {
							if(match.getMatch().getInning().get(inningNumber - 1).getTotalOvers() == events.get(i).getEventOverNo() && events.get(i).getEventBallNo() == 0) {
								switch(events.get(i).getEventType()) {
								case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						        case CricketUtil.FOUR: case CricketUtil.SIX: 
						        	run_count += events.get(i).getEventRuns();
						          break;
						          
						        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						        	run_count += events.get(i).getEventRuns();
						        	break;
						        
						        case CricketUtil.LOG_ANY_BALL:
						        	run_count += events.get(i).getEventRuns();
							          if (events.get(i).getEventExtra() != null) {
							        	  run_count += events.get(i).getEventExtraRuns();
							          }
							          if (events.get(i).getEventSubExtra() != null) {
							        	  run_count += events.get(i).getEventSubExtraRuns();
							          }
							          break;
						        case CricketUtil.WICKET:
						        	wicket_count += 1;
						        	break;
								}
								//run_count = run_count + events.get(i).getEventRuns();
							}
							if((match.getMatch().getInning().get(inningNumber - 1).getTotalOvers() - 1) == events.get(i).getEventOverNo()) {
								switch(events.get(i).getEventType()) {
								case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						        case CricketUtil.FOUR: case CricketUtil.SIX: 
						        	run_count += events.get(i).getEventRuns();
						          break;
						          
						        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
						        	run_count += events.get(i).getEventRuns();
						        	break;
						        
						        case CricketUtil.LOG_ANY_BALL:
						        	run_count += events.get(i).getEventRuns();
							          if (events.get(i).getEventExtra() != null) {
							        	  run_count += events.get(i).getEventExtraRuns();
							          }
							          if (events.get(i).getEventSubExtra() != null) {
							        	  run_count += events.get(i).getEventSubExtraRuns();
							          }
							          break;
						        case CricketUtil.WICKET:
						        	wicket_count += 1;
						        	break;      
								}
							}
						}
					}
				}
			}
		}
		return String.valueOf(run_count) + Seperator + String.valueOf(wicket_count);
	}
	public static String processThisOverRunsCount(int player_id, List<Event> events) {
		int total_runs=0,ball_count=0,total_wicket=0;
		if((events != null) && (events.size() > 0)) {
			
			for(int i = events.size() - 1; i >= 0; i--) {
				if(events.get(i).getEventBowlerNo() != 0){
					if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)|| events.get(i).getEventBowlerNo() != player_id)) {
						break;
					}
				}
				
				switch(events.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		        case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
		        	total_runs += events.get(i).getEventRuns();
		        	ball_count = ball_count + 1;
		          break;
		          
		        case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
		        	total_runs += events.get(i).getEventRuns();
		        	ball_count = ball_count + 1;
		        	break;
		        case CricketUtil.LOG_WICKET:
		        	total_runs += events.get(i).getEventRuns();
		        	total_wicket += 1;
			          if (events.get(i).getEventExtra() != null) {
			        	 total_runs += events.get(i).getEventExtraRuns();
			          }
			          if (events.get(i).getEventSubExtra() != null) {
			        	 total_runs += events.get(i).getEventSubExtraRuns();
			          }
			          ball_count = ball_count + 1;
			          break;
		        case CricketUtil.LOG_ANY_BALL:
		        	total_runs += events.get(i).getEventRuns();
			          if (events.get(i).getEventExtra() != null) {
			        	 total_runs += events.get(i).getEventExtraRuns();
			        	 ball_count = ball_count + 1;
			          }
			          if (events.get(i).getEventSubExtra() != null) {
			        	 total_runs += events.get(i).getEventSubExtraRuns();
			        	 ball_count = ball_count + 1;
			          }
			          if(events.get(i).getEventHowOut() != null) {
			        	  total_wicket += 1;
			        	  ball_count = ball_count + 1;
			          }
			          break;
				}
			}
		}
		return total_runs + "-" + ball_count + "-" + total_wicket;
	}

	public static String getLastWicket(MatchAllData match) {

		for(Inning inn : match.getMatch().getInning()) {
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

	public static String getScoreTypeData(String whatToProcess, MatchAllData match, int inning_number, int player_id, String seperator, List<Event> events) 
	{
		int dots = 0, ones = 0, twos = 0, threes = 0, fours = 0, fives = 0, sixes = 0,nines = 0;
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
				        	if(evnt.getEventWasABoundary() != null && 
	                    		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
				        		fours++;
	                    	}
				        	break;
				        case CricketUtil.FIVE: 
				        	fives++;
				        	break;
				        case CricketUtil.SIX:
				        	if(evnt.getEventWasABoundary() != null && 
	                    		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
				        		sixes++;
	                    	}
				        	break;
				        case CricketUtil.NINE:
				        	if(evnt.getEventWasABoundary() != null && 
	                    		evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
				        		nines++;
	                    	}
				        	break;	
				        case CricketUtil.DOT:
				        	dots++;
				        	break;
				        case CricketUtil.LOG_WICKET:
				        	if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)
                        		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.BOWLED) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)
                        		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.LBW) || evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_WICKET)
                        		|| evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_BALL_TWICE)) 
                        	{
                        		
				        		dots++;
                        	}else if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
                        		if(evnt.getEventRuns() > 0) {
                        			ones++;
                        		}else {
                        			dots++;
                        		}
                        	}
					        break;
				        case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
				        	switch (whatToProcess) {
				        	case CricketUtil.BATSMAN: case CricketUtil.BOWLER:case "TEAM":
								dots++;
								break;
							}
				        	break;
				        	
				        case CricketUtil.NO_BALL:
				        	switch (whatToProcess) {
				        	case CricketUtil.BATSMAN:
								dots++;
								break;
							}
				        	break;
				        
				        case CricketUtil.LOG_ANY_BALL:
				        	if(evnt.getEventExtra() != null) {
				        		if(evnt.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
									if(evnt.getEventHowOut() != null) {
										if(evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
											switch (whatToProcess) {
											case CricketUtil.BATSMAN: case CricketUtil.BOWLER: 
												dots++;
												break;
											}
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
									if ((evnt.getEventRuns() == Integer.valueOf(CricketUtil.NINE)) && (evnt.getEventWasABoundary() != null) && 
											(evnt.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
										switch (whatToProcess) {
										case CricketUtil.BATSMAN: case CricketUtil.BOWLER: case "TEAM":
											nines++;
											break;
										}
							        }
									if(evnt.getEventSubExtra() != null) {
										if ((evnt.getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE)|| evnt.getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE))&& 
							            		evnt.getEventHowOut()!= null && evnt.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
							            	  dots++;
							            }
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
			+ seperator + String.valueOf(fours) + seperator + String.valueOf(fives) + seperator + String.valueOf(sixes) + seperator + String.valueOf(nines);
	}
	
	public static List<Double> speedData(MatchAllData match,int whichInning,int PlayerId) {
		List<Double> data = new ArrayList<Double>();
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getInningNumber() == whichInning) {
				for(BowlingCard boc : inn.getBowlingCard()) {
					if(PlayerId == boc.getPlayerId()) {
						if(boc.getSpeeds() != null) {
							for(int i = 0; i < boc.getSpeeds().size();i++) {
								data.add(Double.valueOf(boc.getSpeeds().get(i).getSpeedValue().trim()));
							}
							Collections.sort(data);
						}
					}
				}
			}
		}
		return data;
	}
	
	public static List<String> GetProjectedScore(MatchAllData match) {

	    List<String> projectedScoreResult = new ArrayList<>();

	    int ballsPerOver = Integer.parseInt(match.getSetup().getBallsPerOver());
	    int maxOvers = match.getSetup().getMaxOvers();

	    String reducedOvers = match.getSetup().getReducedOvers();
	    int totalBalls;

	    if (reducedOvers != null && !reducedOvers.trim().isEmpty() && !reducedOvers.trim().equals("0")) {

	        if (reducedOvers.contains(".")) {
	            if (Integer.parseInt(reducedOvers.split("\\.")[1]) >= ballsPerOver) { 
		        totalBalls = (Integer.parseInt(reducedOvers.split("\\.")[0]) + 1) * ballsPerOver;
		    } else {
		        totalBalls = Integer.parseInt(reducedOvers.split("\\.")[0]) * ballsPerOver + Integer.parseInt(reducedOvers.split("\\.")[1]);
		    }
	        } else {
	            totalBalls = Integer.parseInt(reducedOvers) * ballsPerOver;
	        }

	    } else {
	        totalBalls = maxOvers * ballsPerOver;
	    }

	    int bowled = match.getMatch().getInning().get(0).getTotalOvers() * ballsPerOver
	          + match.getMatch().getInning().get(0).getTotalBalls();

	    int remaining = Math.max(0, totalBalls - Math.min(bowled, totalBalls));

	    int runs = match.getMatch().getInning().get(0).getTotalRuns();
	    double rr = Double.parseDouble(match.getMatch().getInning().get(0).getRunRate());
	    double baseRR = Math.floor(rr);
	    // Current RR
	    projectedScoreResult.add(String.format("%.2f", rr));
	    projectedScoreResult.add(String.valueOf(Math.round(runs + (remaining / 6.0) * rr)));

	    // @2, @4, @6
	    for (int i = 2; i <= 6; i += 2) {
	        double rrb = baseRR + i;
	        projectedScoreResult.add(String.format("%.2f", rrb));
	        projectedScoreResult.add(String.valueOf(Math.round(runs + (remaining / 6.0) * rrb)));
	    }

	    return projectedScoreResult;
	}
	
	public static List<String> projectedScore(MatchAllData match) {
		List<String> proj_score = new ArrayList<String>();
		String  PS_Curr="", PS_1 = "",PS_2 = "",PS_3 = "",RR_1 = "",RR_2 = "",RR_3 = "",CRR = "";
		int Balls_val = 0;
		
		if(match.getSetup().getReducedOvers() != null && !match.getSetup().getReducedOvers().isEmpty()){
			if(match.getSetup().getReducedOvers().contains(".")) {
		    	Balls_val = Integer.valueOf(match.getSetup().getReducedOvers().split("\\.")[0]) * Integer.valueOf(match.getSetup().getBallsPerOver()) + 
	    			Integer.valueOf(match.getSetup().getReducedOvers().split("\\.")[1]);
			} else {
		    	Balls_val = Integer.valueOf(match.getSetup().getReducedOvers()) * Integer.valueOf(match.getSetup().getBallsPerOver());
			}
		} else {
			Balls_val = match.getSetup().getMaxOvers()* Integer.valueOf(match.getSetup().getBallsPerOver());
		}
//		if(match.getSetup().getReducedOvers() > 0) {
//			Balls_val = Integer.valueOf(match.getSetup().getReducedOvers()) * Integer.valueOf(match.getSetup().getBallsPerOver());
//		}else {
//			Balls_val = match.getSetup().getMaxOvers()* Integer.valueOf(match.getSetup().getBallsPerOver());
//		}
		
		int remaining_balls = (Balls_val - (match.getMatch().getInning().get(0).getTotalOvers()* Integer.valueOf(match.getSetup().getBallsPerOver()) 
			+ match.getMatch().getInning().get(0).getTotalBalls()));
		double value = (remaining_balls * Double.valueOf(match.getMatch().getInning().get(0).getRunRate()));
		value  = value/6;
		
		PS_Curr = String.valueOf(Math.round(((value + match.getMatch().getInning().get(0).getTotalRuns()))));
		CRR = match.getMatch().getInning().get(0).getRunRate();
		
		proj_score.add(CRR);
		proj_score.add(String.valueOf(PS_Curr));
		
		String[] arr = (match.getMatch().getInning().get(0).getRunRate().split("\\."));
	    double[] intArr= new double[2];
	    intArr[0]=Integer.parseInt(arr[0]);
	  
		for(int i=2;i<=6;i=i+2) {
			if(i==2) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_1 = String.valueOf(Math.round(value + match.getMatch().getInning().get(0).getTotalRuns()));
				RR_1 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_1);
				proj_score.add(PS_1);
			}
			else if(i==4) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_2 = String.valueOf(Math.round(value + match.getMatch().getInning().get(0).getTotalRuns()));
				RR_2 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_2);
				proj_score.add(PS_2);
			}else if(i==6) {
				value = (remaining_balls * (intArr[0] + i));
				value = value / 6;
				PS_3 = String.valueOf(Math.round(value + match.getMatch().getInning().get(0).getTotalRuns()));
				RR_3 = String.valueOf(((int)intArr[0] + i));
				
				proj_score.add(RR_3);
				proj_score.add(PS_3);
			}
		}
		return proj_score ;
	}

	public static List<Player> getPlayersFromDB(CricketService cricketService, String whichTeamToProcess, MatchAllData match)
	{
		List<Player> players = new ArrayList<Player>(),whichTeamToCheck = new ArrayList<Player>();
		boolean player_found = false; 
		int whichTeamId = 0; 
		
		switch (whichTeamToProcess) {
		case CricketUtil.HOME:
			whichTeamId = match.getSetup().getHomeTeamId();
			whichTeamToCheck = match.getSetup().getHomeSquad();
			break;
		case CricketUtil.AWAY:
			whichTeamId = match.getSetup().getAwayTeamId();
			whichTeamToCheck = match.getSetup().getAwaySquad();
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
	public static List<DuckWorthLewis> populateDuckWorthLewisAe(AE_Cricket match) throws InterruptedException 
	{
		int noOfWicket = 0;
		Document htmlFile = null; 
		try {
//			for(Inning inn : match.getMatch().getInning()) {
//				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
//					int totalball = 0;
//					totalball =((inn.getTotalOvers()* Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls());
//					if(totalball < 42) {
//						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores BB.html"), "ISO-8859-1");
//
//					}else if(totalball >= 42) {
//						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores OO.html"), "ISO-8859-1");
//
//					}
//				}
//			}
			htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores BB.html"), "ISO-8859-1");
			
		} catch (IOException e) {  
			e.printStackTrace(); 
		} 
		
		List<DuckWorthLewis> this_dls = new ArrayList<DuckWorthLewis>();
		for(int i=14; i<htmlFile.body().getElementsByTag("font").size() - 1;i++) {
			if(htmlFile.body().getElementsByTag("font").get(i).text().contains("TableID")) {
				i = i + 15;
				if(i > htmlFile.body().getElementsByTag("font").size()) {
					break;
				}
			}
			
			for(AE_Inning inn : match.getInning()) {
				if(match.getMatchDetails().getStatus().getCurrentInnings() == inn.getNumber()) {
					if(inn.getNoOfWickets() < 10) {
						noOfWicket = inn.getNoOfWickets();
					}else {
						noOfWicket = 9;
					}
					this_dls.add(new DuckWorthLewis(htmlFile.body().getElementsByTag("font").get(i).text(),
							htmlFile.body().getElementsByTag("font").get(i+(2+(noOfWicket))).text()));
				}
			}
			i = i +11;
			
		}
		
		return this_dls;
	}
	public static String populateDlsAe(AE_Cricket match,String teamNameType,int dlsRuns) throws InterruptedException 
	{
		String team="",ahead_behind="";
		int runs = 0;
		for(AE_Inning inn : match.getInning()) {
			if(match.getMatchDetails().getStatus().getCurrentInnings() == inn.getNumber()) {
				
				if(inn.getShortName().equalsIgnoreCase(match.getMatchDetails().getHomeTeam().getShortName())) {
					team = match.getMatchDetails().getHomeTeam().getLongName();
				}else {
					team = match.getMatchDetails().getAwayTeam().getLongName();
				}
				
				runs = (inn.getRuns() - dlsRuns);
				
				if(runs < 0)
                {
                    ahead_behind = team + "-are " + (Math.abs(runs)) + " runs behind";
                }

                if (runs > 0)
                {
                    ahead_behind = team + "-are " + runs + " runs ahead";
                }
                
                if (runs == 0)
                {
                	ahead_behind = "DLS score is level";
                }
			}
		}
		return ahead_behind;
	}
	public static List<DuckWorthLewis> populateDuckWorthLewis(MatchAllData match) throws InterruptedException 
	{
		int noOfWicket = 0;
		Document htmlFile = null; 
		try {
//			for(Inning inn : match.getMatch().getInning()) {
//				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
//					int totalball = 0;
//					totalball =((inn.getTotalOvers()* Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls());
//					if(totalball < 42) {
//						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores BB.html"), "ISO-8859-1");
//
//					}else if(totalball >= 42) {
//						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores OO.html"), "ISO-8859-1");
//
//					}
//				}
//			}
			htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores BB.html"), "ISO-8859-1");
			
		} catch (IOException e) {  
			e.printStackTrace(); 
		} 
		
		List<DuckWorthLewis> this_dls = new ArrayList<DuckWorthLewis>();
		for(int i=14; i<htmlFile.body().getElementsByTag("font").size() - 1;i++) {
			if(htmlFile.body().getElementsByTag("font").get(i).text().contains("TableID")) {
				i = i + 15;
				if(i > htmlFile.body().getElementsByTag("font").size()) {
					break;
				}
			}
			
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					if(inn.getTotalWickets() < 10) {
						noOfWicket = inn.getTotalWickets();
					}else {
						noOfWicket = 9;
					}
					this_dls.add(new DuckWorthLewis(htmlFile.body().getElementsByTag("font").get(i).text(),
							htmlFile.body().getElementsByTag("font").get(i+(2+(noOfWicket))).text()));
				}
				
			}
			i = i +11;
			
		}
		
		return this_dls;
	}
	public static String populateDls(MatchAllData match,String teamNameType,int dlsRuns) throws InterruptedException 
	{
		String team="",ahead_behind="";
		int runs = 0;
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				switch (teamNameType.toUpperCase()) {
				case CricketUtil.SHORT:
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
						team = match.getSetup().getHomeTeam().getTeamName4();
					}
					if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						team = match.getSetup().getAwayTeam().getTeamName4();
					}
					break;
				case CricketUtil.MIDDLE:
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
						team = match.getSetup().getHomeTeam().getTeamName3();
					}
					if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						team = match.getSetup().getAwayTeam().getTeamName3();
					}
					break;
				default:
					if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
						team = match.getSetup().getHomeTeam().getTeamName1();
					}
					if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
						team = match.getSetup().getAwayTeam().getTeamName1();
					}
					break;
				}
				
				runs = (inn.getTotalRuns() - dlsRuns);
				
				if(runs < 0)
                {
                    ahead_behind = team + " are " + (Math.abs(runs)) + " run" + Plural(runs) + " behind";
                }

                if (runs > 0)
                {
                    ahead_behind = team + " are " + runs + " run" + Plural(runs) + " ahead";
                }
                
                if (runs == 0)
                {
                	ahead_behind = "VJD SCORES are level";
                }
			}
		}
		return ahead_behind;
	}
	public static String populateRetiredHurt(MatchAllData match,List<Event> events) throws InterruptedException 
	{
		String ahead_behind="";
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch(events.get(events.size() - 1).getEventType()) {
				case CricketUtil.LOG_WICKET:
					if(events.get(events.size() - 1).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
						
					}
					break;
				}
			}
		}
		return ahead_behind;
	}
	
	public static ArrayList<BestStats> getBowlerVsAllBat(int PlayerId,int inn_number,List<Player> plyer,MatchAllData match) {
		ArrayList<BestStats> batsman_data = new ArrayList<BestStats>();
		int playerId = -1,four=0,six=0, ball = 0;
		Player this_batter = new Player();
		
		for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
			if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_number) {
				if(PlayerId == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
					case CricketUtil.FIVE: case CricketUtil.SIX:
						if(batsman_data.size() > 0 && match.getEventFile().getEvents().get(i).getEventBatterNo() > 0) {
							playerId = -1;
							for(int j=0; j<=batsman_data.size()-1; j++) {
								if(batsman_data.get(j).getPlayerId() == match.getEventFile().getEvents().get(i).getEventBatterNo()) {
									playerId = j;
									break;
								}
							}
							
							if(playerId >=0) {
								batsman_data.get(playerId).setBalls(batsman_data.get(playerId).getBalls()+1);
								batsman_data.get(playerId).setRuns(batsman_data.get(playerId).getRuns() + match.getEventFile().getEvents().get(i).getEventRuns());
							}else {
								int Player_id = match.getEventFile().getEvents().get(i).getEventBatterNo();
								
								this_batter = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
								batsman_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(), ball,four,six, this_batter));
							}
						}else {
							ball = 1;
							int Player_id = match.getEventFile().getEvents().get(i).getEventBatterNo();
							this_batter = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
							
							batsman_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(), ball,four,six, this_batter));
						}
						break;
					case CricketUtil.LOG_WICKET:
						if(batsman_data.size() > 0 && match.getEventFile().getEvents().get(i).getEventBatterNo() > 0) {
							playerId = -1;
							for(int j=0; j<=batsman_data.size()-1; j++) {
								if(batsman_data.get(j).getPlayerId() == match.getEventFile().getEvents().get(i).getEventBatterNo()) {
									playerId = j;
									break;
								}
							}
							
							if(playerId >=0) {
								batsman_data.get(playerId).setBalls(batsman_data.get(playerId).getBalls()+1);
							}else {
								int Player_id = match.getEventFile().getEvents().get(i).getEventBatterNo();
								ball = 1;
								this_batter = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
								batsman_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(), ball,four,six, this_batter));
							}
						}else {
							ball = 1;
							int Player_id = match.getEventFile().getEvents().get(i).getEventBatterNo();
							this_batter = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
							
							batsman_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(), ball,four,six, this_batter));
						}
						break;
					}
				}
			}
		}
		return batsman_data;
	}
	
	public static ArrayList<BestStats> getBatsmanRunsVsAllBowlers(int PlayerId,int inn_number,List<Player> plyer,MatchAllData match) {
		
		ArrayList<BestStats> bowler_data = new ArrayList<BestStats>();
		int playerId = -1,four=0,six=0,ball=0;
		Player this_bowler = new Player();
		
		for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
			if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_number) {
				if(PlayerId == match.getEventFile().getEvents().get(i).getEventBatterNo()) {
					switch (match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
					case CricketUtil.FIVE: case CricketUtil.SIX:
						
						if(bowler_data.size() > 0 && match.getEventFile().getEvents().get(i).getEventBowlerNo() > 0) {
							
							playerId = -1;
							for(int j=0; j<=bowler_data.size()-1; j++) {
								if(bowler_data.get(j).getPlayerId() == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
									playerId = j;
									break;
								}
							}
							
							if(playerId >=0) {
								bowler_data.get(playerId).setBalls(bowler_data.get(playerId).getBalls()+1);
								bowler_data.get(playerId).setRuns(bowler_data.get(playerId).getRuns() + match.getEventFile().getEvents().get(i).getEventRuns());
								if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)){
									bowler_data.get(playerId).setFours(bowler_data.get(playerId).getFours() + 1);
								}
								else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
									bowler_data.get(playerId).setSixes(bowler_data.get(playerId).getSixes() + 1);
								}
							}else {
								int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
								switch(match.getEventFile().getEvents().get(i).getEventType().toUpperCase()) {
								case CricketUtil.FOUR:
									four = 1;
									six = 0;
									ball = 1;
									break;
								case CricketUtil.SIX:
									four = 0;
									six = 1;
									ball = 1;
									break;
								case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE:
								case CricketUtil.BYE: case CricketUtil.LEG_BYE:
									ball = 1;
									six=0;
									ball=1;
									break;
								default:
									four=0;
									six=0;
									ball=0;
								}
								
								this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
								bowler_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),ball,four,six, this_bowler));
							}
						}else {
							
							int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
							
							switch(match.getEventFile().getEvents().get(i).getEventType().toUpperCase()) {
							case CricketUtil.FOUR:
								four = 1;
								six = 0;
								ball = 1;
								break;
							case CricketUtil.SIX:
								four = 0;
								six = 1;
								ball = 1;
								break;
							case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ball = 1;
								six=0;
								ball=1;
								break;
							default:
								four=0;
								six=0;
								ball=0;
							}
							
							this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
							
							bowler_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),ball,four,six, this_bowler));
						}
						break;
					case CricketUtil.LOG_ANY_BALL:
						/*if(bowler_data.size() > 0 && match.getEventFile().getEvents().get(i).getEventBowlerNo() > 0) {
							
							playerId = -1;
							for(int j=0; j<=bowler_data.size()-1; j++) {
								if(bowler_data.get(j).getPlayerId() == match.getEventFile().getEvents().get(i).getEventBowlerNo()) {
									playerId = j;
									break;
								}
							}
							
							if(playerId >=0) {
								bowler_data.get(playerId).setRuns(bowler_data.get(playerId).getRuns() + match.getEventFile().getEvents().get(i).getEventRuns());
								if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.FOUR) && (match.getEventFile().getEvents().get(i).
										getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)){
									bowler_data.get(playerId).setFours(bowler_data.get(playerId).getFours() + 1);
								}
								else if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.SIX) && (match.getEventFile().getEvents().get(i).
										getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
									bowler_data.get(playerId).setSixes(bowler_data.get(playerId).getSixes() + 1);
								}
							}else {
								int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
								
								if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.FOUR) && (match.getEventFile().getEvents().get(i).
										getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)){
									four = 1;
									six = 0;
								}else if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.SIX) && (match.getEventFile().getEvents().get(i).
										getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
									four = 0;
									six = 1;
								}else {
									four=0;
									six=0;
								}
								
								this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
								bowler_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),ball,four,six, this_bowler));
							}
						}else {
							
							int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
							
							if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.FOUR) && (match.getEventFile().getEvents().get(i).
									getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)){
								four = 1;
								six = 0;
							}else if(match.getEventFile().getEvents().get(i).getEventRuns() ==  Integer.valueOf(CricketUtil.SIX) && (match.getEventFile().getEvents().get(i).
									getEventWasABoundary() != null) && match.getEventFile().getEvents().get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
								four = 0;
								six = 1;
							}else {
								four=0;
								six=0;
							}
							
							this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
							bowler_data.add(new BestStats(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),ball,four,six, this_bowler));
						}*/
						break;
					}
				}
			}
		}
//		for(int k=0;k<=bowler_data.size()-1;k++) {
//			System.out.println("Player Name : " + bowler_data.get(k).getPlayer().getFull_name() + " - Runs : " + bowler_data.get(k).getRuns() + 
//					" - FOURS/SIXES : " + bowler_data.get(k).getFours() + "/" + bowler_data.get(k).getSixes());
//		}
		return bowler_data;
	}
	
	public static String runSinceLastWicket(MatchAllData match)
	{
	    int total_runs = 0;
	    for(Inning inn : match.getMatch().getInning()) {
	    	if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
	    		total_runs = inn.getPartnerships().get(inn.getPartnerships().size()-1).getTotalRuns();
	    	}
	    }
	    return String.valueOf(total_runs);
	}
	
	public static int ballSinceLastRun(List<Event> events, int inningNumber) {
		int dot_count = 0;
		
		if(events != null && events.size()>0) {
			for(Event evnt : events) {
				if(inningNumber == evnt.getEventInningNumber()) {
					switch (evnt.getEventType()) {
		  	        case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FIVE: case CricketUtil.BYE: 
		  	        case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:case CricketUtil.LOG_ANY_BALL: 
		  	        	dot_count = 0;
		  	          break;
		  	        case CricketUtil.LOG_WICKET:
		  	        	if(evnt.getEventRuns()!=0 || evnt.getEventExtraRuns() != 0 || evnt.getEventSubExtraRuns() != 0) {
		  	        		dot_count = 0;
		  	        	}else {
		  	        		dot_count++;
		  	        	}
		  	        	break;
		  	        case CricketUtil.DOT:
		  	        	dot_count++;
		  	        	break;
		  	        }
				}
			}
		}
		return dot_count;
	}
	public static String compareInning_Data(MatchAllData match, String separator, int inning_number, List<Event> events) {
	    int total_runs = 0, total_wickets = 0, total_Four = 0, total_SIX = 0, total_nine = 0;

	    if (events != null && !events.isEmpty()) {
	        for (int i = 0; i < events.size(); i++) {

	            if ( events.get(i).getEventInningNumber() == inning_number) {
	                switch (events.get(i).getEventType()) {
	                    case CricketUtil.ONE: case CricketUtil.TWO:case CricketUtil.THREE:case CricketUtil.FIVE:case CricketUtil.DOT:
	                    case CricketUtil.FOUR:case CricketUtil.SIX:
	                        total_runs += events.get(i).getEventRuns();

	                        switch ( events.get(i).getEventType()) {
	                            case CricketUtil.FOUR:
	                            	if(events.get(i).getEventWasABoundary() != null && 
	                            			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		total_Four++;
			                    	}
	                                break;
	                            case CricketUtil.SIX:
	                            	if(events.get(i).getEventWasABoundary() != null && 
	                            			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		total_SIX++;
			                    	}
	                                break;
	                            case CricketUtil.NINE:
	                            	if(events.get(i).getEventWasABoundary() != null && 
	                            			events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            		total_nine++;
			                    	}
	                                break;    
	                        }
	                        break;

	                    case CricketUtil.WIDE:case CricketUtil.NO_BALL:case CricketUtil.BYE:case CricketUtil.LEG_BYE:case CricketUtil.PENALTY:
	                        total_runs +=  events.get(i).getEventRuns();
	                        break;

	                    case CricketUtil.LOG_WICKET:
	                        if ( events.get(i).getEventRuns() > 0) {
	                            total_runs += events.get(i).getEventRuns();
	                        }
	                        if (! events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)) {
	                            total_wickets++;
	                        }
	                        break;
	                    case CricketUtil.LOG_50_50:
				        	if(events.get(i).getEventExtra().trim().equalsIgnoreCase("+")) {
				        		total_runs += events.get(i).getEventExtraRuns();
				        	}else if(events.get(i).getEventExtra().trim().equalsIgnoreCase("-")) {
				        		total_runs -= events.get(i).getEventExtraRuns();
				        	}
				        	break;

	                    case CricketUtil.LOG_ANY_BALL:
	                        total_runs +=  events.get(i).getEventRuns();
	                        if ( events.get(i).getEventExtra() != null && ! events.get(i).getEventExtra().isEmpty()) {
	                            total_runs +=  events.get(i).getEventExtraRuns();
	                        }
	                        if ( events.get(i).getEventSubExtra() != null && ! events.get(i).getEventSubExtra().isEmpty()) {
	                            total_runs +=  events.get(i).getEventSubExtraRuns();
	                        }
	                        if ( events.get(i).getEventHowOut() != null && ! events.get(i).getEventHowOut().isEmpty()) {
	                            total_wickets++;
	                        }

	                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  events.get(i).getEventWasABoundary() != null
	                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            total_Four++;
	                        }

	                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  events.get(i).getEventWasABoundary() != null
	                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            total_SIX++;
	                        }
	                        
	                        if ( events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE) &&  events.get(i).getEventWasABoundary() != null
	                                &&  events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                            total_nine++;
	                        }
	                        break;
	                }

	                if ( events.get(i).getEventOverNo() == match.getMatch().getInning().get(1).getTotalOvers()
	                        &&  events.get(i).getEventBallNo() == match.getMatch().getInning().get(1).getTotalBalls()) {
	                    return total_runs + separator + total_wickets + separator + total_SIX + separator + total_Four + separator + total_nine;
	                }
	            }
	        }
	    }

	    return "";
	}
	
	public static Player getHomeAwayPlayer(int plyr_id, MatchAllData match)
	{
		for(Player plyr : match.getSetup().getHomeSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		for(Player plyr : match.getSetup().getAwaySquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		for(Player plyr : match.getSetup().getHomeOtherSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					if(plyr.getTeamId() == match.getSetup().getHomeTeamId()) {
						return plyr;
					}
				}
		}
		for(Player plyr : match.getSetup().getAwayOtherSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) {
					if(plyr.getTeamId() == match.getSetup().getAwayTeamId()) {
						return plyr;
					}
				}
		}
		return null;
	}
	public static List<BatBallGriff> getBatBallGriff(Player player , String dataType, int PlayerId, Team team, List<HeadToHeadPlayer> headToHead, MatchAllData match) throws StreamReadException, DatabindException, FileNotFoundException, IOException
	{
		List<BatBallGriff> griffBatBall = new ArrayList<BatBallGriff>();
		String matchname = "",runs="",balls="",status= "",ident="";
		boolean player_check = false;
		Team teams = null;
		switch (dataType.toUpperCase()) {
		case CricketUtil.BATSMAN:
			for (HeadToHeadPlayer h2h : headToHead) {
				
				if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + h2h.getMatchFileName()).exists() == true) {
					ident = new ObjectMapper().readValue(new InputStreamReader(new FileInputStream(new File(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.SETUP_DIRECTORY + h2h.getMatchFileName())), StandardCharsets.UTF_8), Setup.class).getMatchIdent();
				}
				
			    if (!h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
			        continue;
			    }
			    if(!matchname.equalsIgnoreCase(h2h.getMatchFileName())) {
			    	// Always add a default "DNP"
				    matchname = h2h.getMatchFileName();
				    griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0", h2h.getOpponentTeam(), 
				    		player, matchname.replace(".json", ""),ident));
			    }
			    // Check if this h2h is for the same player and team
			    if (h2h.getPlayerId() == PlayerId) {
			        status = "DNB"; runs = "0";  balls = "0";

			        if ("Y".equalsIgnoreCase(h2h.getInningStarted().trim())) {
			            status = "Y".equalsIgnoreCase(h2h.getDismissed().trim()) ? "OUT" : "NOT OUT";
			            runs = String.valueOf(h2h.getRuns());
			            balls = String.valueOf(h2h.getBallsFaced());
			        }
			        griffBatBall.set(griffBatBall.size() - 1, new BatBallGriff(PlayerId, Integer.parseInt(runs), Integer.parseInt(balls), status, "", 0, 0, "0",
		                    h2h.getOpponentTeam(), player, matchname.replace(".json", ""),ident));
			    }
			}
			if(player != null) {
				for(Inning inn : match.getMatch().getInning())
				{
					teams = inn.getBowling_team();
					if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
						for(BattingCard bc : inn.getBattingCard())
						{
							if(bc.getPlayerId() == PlayerId) {
								player_check = true;
								griffBatBall.add(new BatBallGriff(bc.getPlayerId(), bc.getRuns(), bc.getBalls(), bc.getStatus(), bc.getHowOut(), 0, 0, "0",
										inn.getBowling_team(), bc.getPlayer(),match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
								break;
							}
						}
					}
				}	
				if(player_check != true) {
					griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0", teams, player,
							match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
				}
			}
			break;
		case CricketUtil.BOWLER:
				for (HeadToHeadPlayer h2h : headToHead) {
					if (!h2h.getTeam().getTeamName4().equalsIgnoreCase(team.getTeamName4())) {
				        continue;
				    }
					
					if(new File(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY + h2h.getMatchFileName()).exists() == true) {
						ident = new ObjectMapper().readValue(new InputStreamReader(new FileInputStream(new File(CricketUtil.CRICKET_DIRECTORY 
								+ CricketUtil.SETUP_DIRECTORY + h2h.getMatchFileName())), StandardCharsets.UTF_8), Setup.class).getMatchIdent();
					}
					
					if(!matchname.equalsIgnoreCase(h2h.getMatchFileName())) {
				    	// Always add a default "DNP"
					    matchname = h2h.getMatchFileName();
					    griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0", h2h.getOpponentTeam(), player, 
					    		matchname.replace(".json", ""),ident));	
				    }	
				    // Check if this h2h is for the same player and team
				    if (h2h.getPlayerId() == PlayerId) {
				        status = "DNB"; runs = "0";  balls = "0";
				        int wicket = 0;
				        if(h2h.getBallsBowled() > 0) {
				        	status = "BALL";
				        	wicket = h2h.getWickets();
				            runs = h2h.getRunsConceded()+"";
				            if(h2h.getBallsBowled()%6 == 0) {
				            	 balls = String.valueOf((h2h.getBallsBowled()/6)) ;
							}else {
								 balls = String.valueOf((h2h.getBallsBowled()/6)+"."+h2h.getBallsBowled()%6) ;
							}
				        }
				        griffBatBall.set(griffBatBall.size() - 1, new BatBallGriff(PlayerId, 0,0, status, "", Integer.valueOf(runs), wicket, balls,
			                    h2h.getOpponentTeam(), player, matchname.replace(".json", ""),ident));
				    }
				}
				if(player != null) {
					for(Inning inn : match.getMatch().getInning())
					{
						if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
							for(BowlingCard boc : inn.getBowlingCard())
							{
								if(boc.getPlayerId() == PlayerId) {
									player_check = true;
									griffBatBall.add(new BatBallGriff(boc.getPlayerId(), 0, 0, "BALL", "", boc.getRuns(), boc.getWickets(), 
											CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()),inn.getBatting_team(), boc.getPlayer(),
											match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
									break;
								}
							}
						}
					}
					for(Inning inn : match.getMatch().getInning())
					{
						if(player_check != true) {
							for(BattingCard bc : inn.getBattingCard())
							{
								teams = inn.getBatting_team();
								if(bc.getPlayerId() == PlayerId) {
									player_check = true;
									griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNB", "", 0, 0,"0",inn.getBowling_team(),
											bc.getPlayer(),match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
								}
							}
						}
					}
					
					if(player_check != true) {
						griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0",teams, player,
								match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
					}
				}
			break;
		}
		return griffBatBall;
		
	}
	public static List<BatBallGriff> getBatBallGriffData(String dataType,int PlayerId,List<Team> team,List<MatchAllData> all_matches, MatchAllData match)
	{
		boolean player_check = false;
		
		List<BatBallGriff> griffBatBall = new ArrayList<BatBallGriff>();
		
		switch (dataType.toUpperCase()) {
		case CricketUtil.BATSMAN:
			for(MatchAllData mtch : all_matches) {
				player_check = false;
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(match.getMatch().getMatchFileName())) {
					if(CricketFunctions.getHomeAwayPlayer(PlayerId, mtch) != null) {
						for(Inning inn : mtch.getMatch().getInning())
						{
							if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
								for(BattingCard bc : inn.getBattingCard())
								{
									if(bc.getPlayerId() == PlayerId) {
										player_check = true;
										griffBatBall.add(new BatBallGriff(bc.getPlayerId(), bc.getRuns(), bc.getBalls(), bc.getStatus(), bc.getHowOut(), 0, 0, "0",
												team.get(inn.getBowlingTeamId() - 1), bc.getPlayer(), mtch.getMatch().getMatchFileName().replace(".json", ""),mtch.getSetup().getMatchIdent()));
										break;
									}
								}
							}
						}	
						if(player_check != true) {
							
							griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0", null, 
									CricketFunctions.getHomeAwayPlayer(PlayerId, mtch),mtch.getMatch().getMatchFileName().replace(".json", ""),mtch.getSetup().getMatchIdent()));
							
							if(CricketFunctions.getHomeAwayPlayer(PlayerId, mtch).getTeamId() == mtch.getSetup().getHomeTeamId()) {
								griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(mtch.getSetup().getAwayTeam());
							}else {
								griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(mtch.getSetup().getHomeTeam());
							}
						}
					}
				}
			}
			
			if(CricketFunctions.getHomeAwayPlayer(PlayerId, match) != null) {
				for(Inning inn : match.getMatch().getInning())
				{
					if(inn.getBattingCard() != null && inn.getBattingCard().size() > 0) {
						for(BattingCard bc : inn.getBattingCard())
						{
							if(bc.getPlayerId() == PlayerId) {
								player_check = true;
								
								griffBatBall.add(new BatBallGriff(bc.getPlayerId(), bc.getRuns(), bc.getBalls(), bc.getStatus(), bc.getHowOut(), 0, 0, "0",
										team.get(inn.getBowlingTeamId() - 1), bc.getPlayer(),match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
								break;
							}
						}
					}
				}	
				if(player_check != true) {
					griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0", null, CricketFunctions.getHomeAwayPlayer(PlayerId, match),
							match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
					
					if(CricketFunctions.getHomeAwayPlayer(PlayerId, match).getTeamId() == match.getSetup().getHomeTeamId()) {
						griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(match.getSetup().getAwayTeam());
					}else {
						griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(match.getSetup().getHomeTeam());
					}
				}
			}
			break;
		case CricketUtil.BOWLER:
			for(MatchAllData mtch : all_matches) {
				player_check = false;
				if(!mtch.getMatch().getMatchFileName().equalsIgnoreCase(match.getMatch().getMatchFileName())) {
					if(CricketFunctions.getHomeAwayPlayer(PlayerId, mtch) != null) {
						for(Inning inn : mtch.getMatch().getInning())
						{
							if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
								
								for(BowlingCard boc : inn.getBowlingCard())
								{
									if(boc.getPlayerId() == PlayerId) {
										//System.out.println("Player1 = " + PlayerId);
										player_check = true;
										
										griffBatBall.add(new BatBallGriff(boc.getPlayerId(), 0, 0, "BALL", "", boc.getRuns(), boc.getWickets(), 
												CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()),team.get(inn.getBattingTeamId() - 1),
												boc.getPlayer(),mtch.getMatch().getMatchFileName().replace(".json", ""),mtch.getSetup().getMatchIdent()));
										break;
									}
								}
							}
						}
						for(Inning inn : mtch.getMatch().getInning())
						{
							if(player_check != true) {
								for(BattingCard bc : inn.getBattingCard())
								{
									if(bc.getPlayerId() == PlayerId) {
										player_check = true;
										griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNB", "", 0, 0, "0",team.get(inn.getBowlingTeamId() - 1),
												bc.getPlayer(),mtch.getMatch().getMatchFileName().replace(".json", ""),mtch.getSetup().getMatchIdent()));
									}
								}
							}
						}
						
						
						if(player_check != true) {
							
							griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0",null, CricketFunctions.getHomeAwayPlayer(PlayerId, mtch),
									mtch.getMatch().getMatchFileName().replace(".json", ""),mtch.getSetup().getMatchIdent()));
							
							if(CricketFunctions.getHomeAwayPlayer(PlayerId, mtch).getTeamId() == mtch.getSetup().getHomeTeamId()) {
								griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(mtch.getSetup().getAwayTeam());
							}else {
								griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(mtch.getSetup().getHomeTeam());
							}
						}
					}
				}
			}
			//System.out.println(CricketFunctions.getHomeAwayPlayer(PlayerId, match));
			if(CricketFunctions.getHomeAwayPlayer(PlayerId, match) != null) {
				for(Inning inn : match.getMatch().getInning())
				{
					//System.out.println("hello");
					if(inn.getBowlingCard() != null && inn.getBowlingCard().size() > 0) {
						for(BowlingCard boc : inn.getBowlingCard())
						{
							if(boc.getPlayerId() == PlayerId) {
								player_check = true;
								
								griffBatBall.add(new BatBallGriff(boc.getPlayerId(), 0, 0, "BALL", "", boc.getRuns(), boc.getWickets(), 
										CricketFunctions.OverBalls(boc.getOvers(), boc.getBalls()),team.get(inn.getBattingTeamId() - 1), boc.getPlayer(),
										match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
								break;
							}
						}
					}
				}
				for(Inning inn : match.getMatch().getInning())
				{
					if(player_check != true) {
						for(BattingCard bc : inn.getBattingCard())
						{
							if(bc.getPlayerId() == PlayerId) {
								player_check = true;
								
								griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNB", "", 0, 0,"0",team.get(inn.getBowlingTeamId() - 1),
										bc.getPlayer(),match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
							}
						}
					}
				}
				
				if(player_check != true) {
					
					griffBatBall.add(new BatBallGriff(PlayerId, 0, 0, "DNP", "", 0, 0, "0",null, CricketFunctions.getHomeAwayPlayer(PlayerId, match),
							match.getMatch().getMatchFileName().replace(".json", ""),match.getSetup().getMatchIdent()));
					
					if(CricketFunctions.getHomeAwayPlayer(PlayerId, match).getTeamId() == match.getSetup().getHomeTeamId()) {
						griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(match.getSetup().getAwayTeam());
					}else {
						griffBatBall.get(griffBatBall.size() - 1).setOpponentTeam(match.getSetup().getHomeTeam());
					}
				}
			}
			break;	
		}
		
		return griffBatBall;
	}
	
	public static String getRunScored(Inning inning,int inn_number, String seperator,MatchAllData match) {
		
		int total_run_1_2_over = 0,total_run_3_5_over = 0,total_run_6_8_over = 0,total_run_9_10_over = 0,
				total_wkt_1_2_over = 0,total_wkt_3_5_over = 0,total_wkt_6_8_over = 0,total_wkt_9_10_over = 0,bowlerID = 0;
		
		if((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
			for(int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_number) {
					
					if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
						bowlerID = match.getEventFile().getEvents().get(i).getEventBowlerNo();
					}
					
					if((match.getEventFile().getEvents().get(i).getEventOverNo()+1) <= 2 ||
							match.getEventFile().getEvents().get(i).getEventBallNo() == 0 && 
							match.getEventFile().getEvents().get(i).getEventBowlerNo() == bowlerID) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.LOG_WICKET:
                            if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                            {
                            	total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            }
                            total_wkt_1_2_over += 1;
                            break;

						case CricketUtil.LOG_ANY_BALL:
							total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                            {
                            	total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                            {
                            	total_run_1_2_over += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                            {
                            	total_wkt_1_2_over += 1;
                            }
                            break;
                        }
					}else if((match.getEventFile().getEvents().get(i).getEventOverNo()+1) <= 5 ) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.LOG_WICKET:
                            if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                            {
                            	total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            }
                            total_wkt_3_5_over += 1;
                            break;

						case CricketUtil.LOG_ANY_BALL:
							total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                            {
                            	total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                            {
                            	total_run_3_5_over += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                            {
                            	total_wkt_3_5_over += 1;
                            }
                            break;
                        }
					}else if((match.getEventFile().getEvents().get(i).getEventOverNo()+1) <= 8 ) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.LOG_WICKET:
                            if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                            {
                            	total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            }
                            total_wkt_6_8_over += 1;
                            break;

						case CricketUtil.LOG_ANY_BALL:
							total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                            {
                            	total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                            {
                            	total_run_6_8_over += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                            {
                            	total_wkt_6_8_over += 1;
                            }
                            break;
                        }
					}else if((match.getEventFile().getEvents().get(i).getEventOverNo()+1) <= 10 ) {
						switch (match.getEventFile().getEvents().get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            break;

						case CricketUtil.LOG_WICKET:
                            if (match.getEventFile().getEvents().get(i).getEventRuns() > 0)
                            {
                            	total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            }
                            total_wkt_9_10_over += 1;
                            break;

						case CricketUtil.LOG_ANY_BALL:
							total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventRuns();
                            if (match.getEventFile().getEvents().get(i).getEventExtra() != null)
                            {
                            	total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null)
                            {
                            	total_run_9_10_over += match.getEventFile().getEvents().get(i).getEventSubExtraRuns();
                            }
                            if (match.getEventFile().getEvents().get(i).getEventHowOut() != null && !match.getEventFile().getEvents().get(i).getEventHowOut().isEmpty())
                            {
                            	total_wkt_9_10_over += 1;
                            }
                            break;
                        }
					}
				}
			}
		}
		return String.valueOf(total_run_1_2_over) + "-" + String.valueOf(total_wkt_1_2_over) + "," + String.valueOf(total_run_3_5_over) + "-" + String.valueOf(total_wkt_3_5_over) + "," + 
		String.valueOf(total_run_6_8_over) + "-" + String.valueOf(total_wkt_6_8_over) + "," + String.valueOf(total_run_9_10_over) + "-" + String.valueOf(total_wkt_9_10_over);
	}

	public static List<BattingCard> getPlayerListFromMatchData(MatchAllData match)
	{
		 List<BattingCard>  plyr =new ArrayList<BattingCard>();
		
		for(BattingCard ply : match.getMatch().getInning().get(0).getBattingCard()) {
			if(!ply.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
				plyr.add(ply);
			}
		}
		for(BattingCard ply :match.getMatch().getInning().get(1).getBattingCard()) {
			if(!ply.getStatus().equalsIgnoreCase(CricketUtil.STILL_TO_BAT)) {
				plyr.add(ply);
			}
		}
		return plyr;
	}

	public static List<Player> getAllRounderCatches(MatchAllData match){
		List<Player>  arr =new ArrayList<Player>();
		for(Inning inn : match.getMatch().getInning()) {
			
			for(BattingCard bat: inn.getBattingCard()) {
					
				if(bat.getStatus().equalsIgnoreCase(CricketUtil.OUT)&& 
						(bat.getHowOut()!=null||!bat.getHowOut().isEmpty())&&
						(bat.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)||
						bat.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED))) {
						
						int Fielder_Id =bat.getHowOutFielderId();
						
						if(arr==null ||arr.stream().noneMatch(bc -> bc.getPlayerId() == Fielder_Id)) {
							Player player =getPlayerFromMatchData(Fielder_Id ,match);
							
							if(player.getRole().equalsIgnoreCase(CricketUtil.ALL_ROUNDER)) {
								arr.add(player);
							}
						}
						for(Player py : arr) {
							if(py.getPlayerId()==Fielder_Id) {
								py.setCatches(py.getCatches()+1);
							}
						}
					}
			}   

		}
			
		return arr;
		
	}
	public static List<Player> AllRounderCatches(MatchAllData match){
		List<Player>  arr =new ArrayList<Player>();
		for(Inning inn: match.getMatch().getInning()) {
			for(BattingCard bat: inn.getBattingCard()) {
				if(bat.getStatus().equalsIgnoreCase(CricketUtil.OUT)&&
						(bat.getHowOut()!=null && !bat.getHowOut().isEmpty())&&
						(bat.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT)||
						bat.getHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED))) {
						
						int Fielder_Id =bat.getHowOutFielderId();
						
						if(arr==null ||arr.stream().noneMatch(bc -> bc.getPlayerId() == Fielder_Id)) {
							Player player =getPlayerFromMatchData(Fielder_Id ,match);
							
							if(player.getRole().equalsIgnoreCase(CricketUtil.ALL_ROUNDER)) {
								arr.add(player);
							}
						}
						for(Player py : arr) {
							if(py.getPlayerId()==Fielder_Id) {
								py.setCatches(py.getCatches()+1);
							}
						}
					}
			} 
		}	
		return arr;
		
	}

	public static MatchStats getAllEventsStats(Match match, List<Event> events) {
		return new MatchStats();
	}
	
	public static MatchStats getAllEventsStatsMASTER(MatchStats matchStats,Match match, List<Event> events) 
	{
		
		String typeOfStats = "", statsData = "";
		BowlingCard currentBowlerBC = null;
		Inning currentInning = null;
		int overbyRun=0, overbyWkts=0, overbyRun1=0, overbyWkts1=0;
		String thisOverTxt = "";
		int thisOverRun=0, thisOverWkts=0;
		String outBatsman = "",notWicketCount= "";
		typeOfStats = "INNING_COMPARE,";
		
		List<Event> Event = new ArrayList<Event>();
		
		for (Inning inn : match.getInning()) {
			if(inn.getInningNumber() == 2 && (inn.getTotalOvers() > 0 || inn.getTotalBalls() > 0 
				|| inn.getTotalRuns() > 0 || inn.getTotalExtras() > 0))
			{
				typeOfStats = typeOfStats.replace("INNING_COMPARE,", "");
			}
		}
		
		if(events != null && events.size() > 0) {
			
			for (int i = events.size() - 1; i >= 0; i--) {

				if(currentInning == null) {
					currentInning = match.getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(
						CricketUtil.YES)).findAny().orElse(null);
				}
				
				if(currentInning != null) {
					if(events.get(i).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
						if(matchStats.getBowlingCard().getLastBowlerId()<=0) {
							   matchStats.getBowlingCard().setLastBowlerId(events.get(i).getEventBowlerNo());
						}else if(matchStats.getBowlingCard().getLastBowlerId()> 0 && matchStats.getBowlingCard().getReplacementBowlerId()<=0) {
							typeOfStats += "LAST_OVER,";
							matchStats.getBowlingCard().setReplacementBowlerId(events.get(i).getEventBowlerNo());							
						}
					}
				}
				switch (events.get(i).getEventType()) {
				case CricketUtil.CHANGE_BOWLER:
					if(!typeOfStats.contains("THIS_OVER")) {
						typeOfStats += "THIS_OVER,";
					}
					if(events.get(i).getEventInningNumber()==1) {
						matchStats.getHomeOverByOverData().add(new OverByOverData(events.get(i).getEventInningNumber(), events.get(i).getEventOverNo()+1,
	                            overbyRun, overbyWkts, false , outBatsman , notWicketCount));
						matchStats.getHomeOverByOverData().get(matchStats.getHomeOverByOverData().size() - 1).setOvertype(events.get(i).getEventExtra());
						if(events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty() && events.get(i).getEventExtra().equalsIgnoreCase("challenge")) {
							if(overbyRun >= Integer.valueOf(events.get(i).getEventSubExtra())) {
								matchStats.getHomeOverByOverData().get(matchStats.getHomeOverByOverData().size() - 1).setChallengeOverRuns("+" + (overbyRun/2));
							}else {
								matchStats.getHomeOverByOverData().get(matchStats.getHomeOverByOverData().size() - 1).setChallengeOverRuns("-" + (overbyRun/2));
							}
						}
						
						overbyRun=0; overbyWkts=0;outBatsman = "";notWicketCount= "";
						
					}else if(events.get(i).getEventInningNumber()==2) {
						matchStats.getAwayOverByOverData().add(new OverByOverData(events.get(i).getEventInningNumber(), events.get(i).getEventOverNo()+1,
	                            overbyRun1, overbyWkts1, false, outBatsman , notWicketCount));
						
						matchStats.getAwayOverByOverData().get(matchStats.getAwayOverByOverData().size() - 1).setOvertype(events.get(i).getEventExtra());
						
						if(events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty() && events.get(i).getEventExtra().equalsIgnoreCase("challenge")) {
							if(overbyRun1 >= Integer.valueOf(events.get(i).getEventSubExtra())) {
								matchStats.getAwayOverByOverData().get(matchStats.getAwayOverByOverData().size() - 1).setChallengeOverRuns("+" + (overbyRun1/2));
							}else {
								matchStats.getAwayOverByOverData().get(matchStats.getAwayOverByOverData().size() - 1).setChallengeOverRuns("-" + (overbyRun1/2));
							}
						}
						
						overbyRun1=0; overbyWkts1=0;outBatsman = "";notWicketCount= "";
					}
					if(currentInning != null && currentInning.getInningNumber()== events.get(i).getEventInningNumber()) {
						if(!matchStats.getTimeLine().isEmpty()) {
							matchStats.setTimeLine(matchStats.getTimeLine()+",|");
						}
					}
					if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= (matchStats.getPhase1StartOver() - 1) * 6 &&
		                    ((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= matchStats.getPhase1EndOver() * 6)) {
						if(events.get(i).getEventInningNumber()==1) {
							matchStats.getPowerPlay1ThisOver().add(new VariousStats(events.get(i).getEventOverNo() , thisOverTxt, thisOverRun,thisOverWkts));

						}else if(events.get(i).getEventInningNumber()==2) {
							matchStats.getPowerPlay2ThisOver().add(new VariousStats(events.get(i).getEventOverNo() , thisOverTxt, thisOverRun,thisOverWkts));

						}
						thisOverTxt = ""; thisOverRun=0; thisOverWkts=0;
					}
					break;	
				case CricketUtil.LOG_OVERWRITE_BATSMAN_HOWOUT :
					Event.add(events.get(i));
				 break;
				case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
			    case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
			    case CricketUtil.WIDE: case CricketUtil.PENALTY: case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL: case CricketUtil.NINE:
			    	//Last 30 balls
			    	if(currentInning != null && currentInning.getInningNumber()== events.get(i).getEventInningNumber()) {
			    		if(matchStats.getTimeLine().isEmpty()) {
							matchStats.setTimeLine(updateOverStats(events.get(i)).split(",")[0]);
						}else {
							matchStats.setTimeLine(matchStats.getTimeLine()+","+updateOverStats(events.get(i)).split(",")[0]);
						}
			    	}
					if(matchStats.getLastThirtyBalls().getTotalBalls() > 0) {
				    	switch (events.get(i).getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
					    case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.LOG_WICKET: case CricketUtil.BYE: 
					    case CricketUtil.LEG_BYE:	
					    	matchStats.getLastThirtyBalls().setTotalBalls(matchStats.getLastThirtyBalls().getTotalBalls() - 1);
					    	matchStats.getLastThirtyBalls().setTotalRuns(matchStats.getLastThirtyBalls().getTotalRuns() + events.get(i).getEventRuns());
					    	
					    	if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
						    	matchStats.getLastThirtyBalls().setTotalWickets(matchStats.getLastThirtyBalls().getTotalWickets()+ 1);
						    	
					    	}
					    	break;
					    case CricketUtil.NO_BALL:case CricketUtil.WIDE: case CricketUtil.PENALTY:case CricketUtil.LOG_ANY_BALL:
					    	switch (events.get(i).getEventType()) {
					    	case CricketUtil.WIDE:case CricketUtil.NO_BALL:case CricketUtil.PENALTY:
					    		matchStats.getLastThirtyBalls().setTotalRuns(matchStats.getLastThirtyBalls().getTotalRuns()+
					    			events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns());
					    		break;
						    case CricketUtil.LOG_ANY_BALL:
						    	if(events.get(i).getDoNotIncrementBall().equalsIgnoreCase(CricketUtil.NO)) {
							    	matchStats.getLastThirtyBalls().setTotalBalls(matchStats.getLastThirtyBalls().getTotalBalls() - 1);
						    	}
						    	if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
						    		matchStats.getLastThirtyBalls().setTotalWickets(matchStats.getLastThirtyBalls().getTotalWickets()+ 1);
						    	}
						    	matchStats.getLastThirtyBalls().setTotalRuns(matchStats.getLastThirtyBalls().getTotalRuns()+(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns()+ events.get(i).getEventSubExtraRuns()));
					    		break;
					    	}				    		
					
						}
					}
			    	//This over
					if(!typeOfStats.contains("THIS_OVER")) {
						
						if (currentBowlerBC == null) {
						    currentBowlerBC = match.getInning().stream()
						        .filter(in -> CricketUtil.YES.equalsIgnoreCase(in.getIsCurrentInning()))
						        .findAny()
						        .map(in -> {
						            if (in.getBowlingCard() != null) {
						                return in.getBowlingCard().stream()
						                    .filter(bc -> (bc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)
						                                || bc.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER)))
						                    .findAny()
						                    .orElse(null);
						            }
						            return null;
						        })
						        .orElse(null);
						}
						
						if(currentBowlerBC != null && currentBowlerBC.getPlayerId() == events.get(i).getEventBowlerNo()) {
							
							switch (events.get(i).getEventType()) {
							
							case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE: 
							case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE:
								
								if(!typeOfStats.contains("THIS_OVER")) {
							    	if(!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    	}
							    	matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + 
							    		    (events.get(i).getEventWasABoundary() != null && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES) 
							    		    ? events.get(i).getEventRuns()+"BOUNDARY"  : events.get(i).getEventRuns()));

						    		
						    		matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() + events.get(i).getEventRuns());
								}
						        break;
							case CricketUtil.LOG_ANY_BALL:
					    		matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() + events.get(i).getEventRuns()
					    				+ events.get(i).getEventExtraRuns()+ events.get(i).getEventSubExtraRuns());
							    if (!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							        matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    }

							    if(events.get(i).getEventExtra() != null) {
							    	if (events.get(i).getEventExtra().equals(CricketUtil.WIDE) || events.get(i).getEventExtra().equals(CricketUtil.NO_BALL)) {
								        if(events.get(i).getEventSubExtra() != null) {
								        	if (events.get(i).getEventSubExtra().equals(CricketUtil.WIDE) || events.get(i).getEventSubExtra().equals(CricketUtil.NO_BALL)) {
									            if (!events.get(i).getEventSubExtra().isEmpty() && events.get(i).getEventSubExtraRuns() > 0) {
									                matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() +
									                    (events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns()) +
									                    events.get(i).getEventExtra());
									            } else {
									                if (!events.get(i).getEventExtra().equalsIgnoreCase(events.get(i).getEventSubExtra())) {
									                    matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() +  events.get(i).getEventExtra() + "+" +
									                        (events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns() > 1 ?
									                            (events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns()) : "") +
									                        events.get(i).getEventSubExtra());
									                } else {
									                    matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + (events.get(i).getEventRuns() +
									                    	events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns()) +  events.get(i).getEventExtra());
									                }
									            }
									        } else if (events.get(i).getEventSubExtra().equals(CricketUtil.LEG_BYE) || events.get(i).getEventSubExtra().equals(CricketUtil.BYE)) {
									            	matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra() + "+" +
									            		    (events.get(i).getEventSubExtraRuns() > 1 ? events.get(i).getEventSubExtraRuns() + events.get(i).getEventSubExtra() 
									            		     : events.get(i).getEventSubExtra()) + (events.get(i).getEventRuns() > 0 ? "+" + events.get(i).getEventRuns() : ""));

									        } else {
									        	if(events.get(i).getEventSubExtra().isEmpty()) {
									        		if(events.get(i).getEventRuns() > 0) {
									        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns());	
									        		}else {
									        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra());		
									        		}
									        	}else {
									        		
									        		if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
									        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + CricketUtil.PENALTY);
							    	    				if(events.get(i).getEventRuns() > 0) {
							    	    					matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() +  "+" +  events.get(i).getEventRuns()); 
							    			    		}
							    	    			}else {
							    	    				if(events.get(i).getEventRuns() > 0) {
										        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns());	
										        		}else {
										        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra());		
										        		}
										        		if(events.get(i).getEventSubExtraRuns() > 0) {
										        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + "+" + events.get(i).getEventSubExtra() + "+" + events.get(i).getEventSubExtraRuns());	
										        		}else {
										        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + "+" + events.get(i).getEventSubExtra());		
										        		}
			
							    	    			}							        	}
									        }
								        }else {
								        	if(events.get(i).getEventRuns() > 0) {
							        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra() + "+" + events.get(i).getEventRuns());	
							        		}else {
							        			 matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventExtra());		
							        		}
								        }
								    }else {
								        matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + (events.get(i).getEventRuns() > 0 ? events.get(i).getEventRuns() + "+" : "") +
									            (events.get(i).getEventSubExtraRuns() > 1 ? events.get(i).getEventSubExtraRuns() : "") +  events.get(i).getEventSubExtra());
									 }
							    }else {
							    	if(events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
							    		if(events.get(i).getDoNotIncrementBall().equalsIgnoreCase(CricketUtil.YES)) {
							    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + (events.get(i).getEventRuns() > 0 ? events.get(i).getEventRuns() + "+" : "") +
										            (events.get(i).getEventSubExtraRuns() > 1 ? events.get(i).getEventSubExtraRuns() : "") +  events.get(i).getEventSubExtra()+"_Y");
							    		}else {
							    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + (events.get(i).getEventRuns() > 0 ? events.get(i).getEventRuns() + "+" : "") +
										            (events.get(i).getEventSubExtraRuns() > 1 ? events.get(i).getEventSubExtraRuns() : "") +  events.get(i).getEventSubExtra()+"_N");
							    		}
							    	}else {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + (events.get(i).getEventRuns() > 0 ? events.get(i).getEventRuns() + "+" : "") +
									            (events.get(i).getEventSubExtraRuns() > 1 ? events.get(i).getEventSubExtraRuns() : "") +  events.get(i).getEventSubExtra());
							    	} 
							    }
							    
							    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
							        matchStats.getOverData().setTotalWickets(matchStats.getOverData().getTotalWickets() + 1);
							        matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + "+" + CricketUtil.LOG_WICKET);
							    }
							    break;
						    case CricketUtil.LOG_WICKET:
						    	
						    	matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() +
					    			events.get(i).getEventRuns() + events.get(i).getEventExtraRuns()+ events.get(i).getEventSubExtraRuns());
					    		
								switch (events.get(i).getEventHowOut().toUpperCase()) {
								case CricketUtil.ABSENT_HURT: case CricketUtil.RETIRED_HURT: case CricketUtil.CONCUSSED:
									break;
								default:
						    		if(!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    	}
									matchStats.getOverData().setTotalWickets(matchStats.getOverData().getTotalWickets() + 1);
									if(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() 
						    			+ events.get(i).getEventSubExtraRuns()>0) {
										matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + String.valueOf(events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() 
								    			+ events.get(i).getEventSubExtraRuns()) +"+"+ events.get(i).getEventType());	
									}else {
										matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventType());
									}
							    	
									break;
								}
				    		    break;						    		
						        
							case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.WIDE: case CricketUtil.PENALTY:
								switch (events.get(i).getEventType()) {
						    	case CricketUtil.WIDE:case CricketUtil.NO_BALL:
  
						    		if(!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    	}
							    	
						    		matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() +
						    				events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns());
						    		
						    		if(events.get(i).getEventRuns() > 1) {
						    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + String.valueOf((events.get(i).getEventRuns()  
							    			+ events.get(i).getEventSubExtraRuns())) + events.get(i).getEventType());
						    		} else {
						    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt()+  events.get(i).getEventType());
						    		}
						    		break;
						    	case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						    		  
						    		if(!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    	}
							    	
						    		matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() +
						    				events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns());
						    		
						    		if(events.get(i).getEventRuns() > 1) {
						    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + String.valueOf((events.get(i).getEventRuns()  
							    			+ events.get(i).getEventSubExtraRuns())) + events.get(i).getEventType());
						    		} else {
						    			matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + events.get(i).getEventRuns() +  events.get(i).getEventType());
						    		}
						    		break;	
						    	case CricketUtil.PENALTY:
						    		if(!matchStats.getOverData().getThisOverTxt().isEmpty()) {
							    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + ",");
							    	}
						    		matchStats.getOverData().setTotalRuns(matchStats.getOverData().getTotalRuns() +
						    				events.get(i).getEventRuns() + events.get(i).getEventExtraRuns()+ events.get(i).getEventSubExtraRuns());
						    		
						    		matchStats.getOverData().setThisOverTxt(matchStats.getOverData().getThisOverTxt() + String.valueOf((events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() 
						    			+ events.get(i).getEventSubExtraRuns())) + "+" +  events.get(i).getEventType());
						    		
						    		break;
								}
								break;
							}
						}
					} 
					
					//Innings comparision
					if(!typeOfStats.contains("INNING_COMPARE")) {

						if (currentInning != null && events.get(i).getEventInningNumber() == 1  && (events.get(i).getEventOverNo() < currentInning.getTotalOvers() || 
							(events.get(i).getEventOverNo() == currentInning.getTotalOvers() && events.get(i).getEventBallNo() <= currentInning.getTotalBalls()))) {
							
							switch (events.get(i).getEventType()) {
							case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
								
								matchStats.getInningCompare().setTotalRuns(matchStats.getInningCompare().getTotalRuns()+ 
										events.get(i).getEventRuns() + events.get(i).getEventExtraRuns()+ events.get(i).getEventSubExtraRuns());
								if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() && 
									(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)&&
										!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)&&
										!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
									 	matchStats.getInningCompare().setTotalWickets(matchStats.getInningCompare().getTotalWickets()+1);
									 
									 	if (Integer.parseInt(getpowerplay(events.get(i)).split(",")[7]) > 0) {
									 		matchStats.getInningCompare().setOutBatsman(
									 			matchStats.getInningCompare().getOutBatsman().isEmpty()
								 	            ? String.valueOf(Integer.parseInt(getpowerplay(events.get(i)).split(",")[8]))
								 	            : matchStats.getInningCompare().getOutBatsman() + "," + Integer.parseInt(getpowerplay(events.get(i)).split(",")[8]));	
									 	}
								}
								if (Integer.parseInt(getpowerplay(events.get(i)).split(",")[8]) > 0) {
							 		matchStats.getInningCompare().setNotWicketCount(
							 			matchStats.getInningCompare().getNotWicketCount().isEmpty()
						 	            ? String.valueOf(Integer.parseInt(getpowerplay(events.get(i)).split(",")[8]))
						 	            : matchStats.getInningCompare().getNotWicketCount() + "," + Integer.parseInt(getpowerplay(events.get(i)).split(",")[8]));
								}
								if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
									matchStats.getInningCompare().setTotalSixes(matchStats.getInningCompare().getTotalSixes()+1);		
	                           	} 
	                           	if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                           		matchStats.getInningCompare().setTotalFours(matchStats.getInningCompare().getTotalFours()+1);		
	                           	} 
	                           	if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                           		matchStats.getInningCompare().setTotalNines(matchStats.getInningCompare().getTotalNines()+1);		
	                           	}
								break;
							 case CricketUtil.LOG_50_50:
								 if(events.get(i).getEventExtra().trim().equalsIgnoreCase("+")) {
									 matchStats.getInningCompare().setTotalRuns(matchStats.getInningCompare().getTotalRuns() +
											 events.get(i).getEventExtraRuns());
						        	}else if(events.get(i).getEventExtra().trim().equalsIgnoreCase("-")) {
						        		 matchStats.getInningCompare().setTotalRuns(matchStats.getInningCompare().getTotalRuns() -
						        				 events.get(i).getEventExtraRuns());
						        	}
								 break;
								 
							 case CricketUtil.BYE:case CricketUtil.LEG_BYE:case CricketUtil.WIDE:case CricketUtil.NO_BALL:
							 case CricketUtil.DOT: case CricketUtil.FOUR: case CricketUtil.SIX:  case CricketUtil.NINE:
							 case CricketUtil.ONE: case CricketUtil.TWO:case CricketUtil.THREE: case CricketUtil.FIVE:
							 case CricketUtil.PENALTY:
								 
								 matchStats.getInningCompare().setTotalRuns(matchStats.getInningCompare().getTotalRuns()+ 
											events.get(i).getEventRuns());
								 
								 if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
										matchStats.getInningCompare().setTotalSixes(matchStats.getInningCompare().getTotalSixes()+1);		
	                           	 } 
	                           	 if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                           		matchStats.getInningCompare().setTotalFours(matchStats.getInningCompare().getTotalFours()+1);		
	                           	 } 
	                           	 if (events.get(i).getEventWasABoundary()!=null && events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE) && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                           		matchStats.getInningCompare().setTotalNines(matchStats.getInningCompare().getTotalNines()+1);		
	                           	 }
	                           	 if (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.DOT)) {  	
				                    	matchStats.getInningCompare().setTotalDots(matchStats.getInningCompare().getTotalDots()+1); 
	                           	 }
		                    }
						}
					} 
					//Last over
					if(!typeOfStats.contains("LAST_OVER")) {

						if(matchStats.getBowlingCard().getLastBowlerId() > 0 &&
							events.get(i).getEventBowlerNo() == matchStats.getBowlingCard().getLastBowlerId()) {
							
							switch(events.get(i).getEventType()) {
							case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
								matchStats.getLastOverData().setTotalRuns(matchStats.getLastOverData().getTotalRuns() +
										events.get(i).getEventRuns());
								break;
							case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
								if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() && 
									(!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)&&
									!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) &&
									!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
										matchStats.getLastOverData().setTotalWickets(matchStats.getLastOverData().getTotalWickets() + 1);
								}
								matchStats.getLastOverData().setTotalRuns(matchStats.getLastOverData().getTotalRuns() +
									events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns());
								break;
							default:
								matchStats.getLastOverData().setTotalRuns(matchStats.getLastOverData().getTotalRuns() +
									events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns());
								break;
							}
							
						} 
//						else 
//							typeOfStats += "LAST_OVER,";
						}

						// Last boundary
						if(currentInning.getInningNumber() == events.get(i).getEventInningNumber() && !typeOfStats.contains("LAST_BOUNDARY")) {
							if ((events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX) 
					    		|| events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)
					    		|| events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NINE)
					    		|| events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL))
					    		&& (events.get(i).getEventWasABoundary() != null && 
					    		 events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES))) {
								typeOfStats += "LAST_BOUNDARY,";
							}else {
								switch(events.get(i).getEventType()) {
									case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:
									case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE: case CricketUtil.FIVE: 
									case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.LOG_WICKET:
										matchStats.setBallsSinceLastBoundary(matchStats.getBallsSinceLastBoundary() + 1);
										break;
									case CricketUtil.LOG_ANY_BALL:
										if(events.get(i).getEventExtra() != null) {
											if((!events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) && !events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL))) {
												if(!events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)&&!events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL)
														&&!events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
													matchStats.setBallsSinceLastBoundary(matchStats.getBallsSinceLastBoundary() + 1);
												}
											}
										}
										break;
								}
							}
						}

						//Teams score data
						if(matchStats.getHomeTeamScoreData()==null && matchStats.getAwayTeamScoreData()==null) {
							
							switch (events.get(i).getEventInningNumber()) {
							case 1:
								matchStats.getHomeTeamScoreData().setId(currentInning.getBattingTeamId());
								matchStats.getAwayTeamScoreData().setId(currentInning.getBowlingTeamId());
								break;
							case 2:
								matchStats.getHomeTeamScoreData().setId(currentInning.getBowlingTeamId());
								matchStats.getAwayTeamScoreData().setId(currentInning.getBattingTeamId());
								break;
							}
						}
						switch (events.get(i).getEventType()) {
						case CricketUtil.ONE:
							if(events.get(i).getEventInningNumber() == 1) {
								matchStats.getHomeTeamScoreData().setTotalOnes(matchStats.getHomeTeamScoreData().getTotalOnes()+1);
							} else if(events.get(i).getEventInningNumber() == 2) {
								matchStats.getAwayTeamScoreData().setTotalOnes(matchStats.getAwayTeamScoreData().getTotalOnes()+1);
							}
							break;
						case CricketUtil.TWO:
							if(events.get(i).getEventInningNumber() == 1) {
								matchStats.getHomeTeamScoreData().setTotalTwos(matchStats.getHomeTeamScoreData().getTotalTwos()+1);
							} else if(events.get(i).getEventInningNumber() == 2) {
								matchStats.getAwayTeamScoreData().setTotalTwos(matchStats.getAwayTeamScoreData().getTotalTwos()+1);
							}
							break;
						case CricketUtil.THREE:
							if(events.get(i).getEventInningNumber() == 1) {
								matchStats.getHomeTeamScoreData().setTotalThrees(matchStats.getHomeTeamScoreData().getTotalThrees()+1);
							} else if(events.get(i).getEventInningNumber() == 2) {
								matchStats.getAwayTeamScoreData().setTotalThrees(matchStats.getAwayTeamScoreData().getTotalThrees()+1);
							}
							break;
						case CricketUtil.FIVE:
							if(events.get(i).getEventInningNumber() == 1) {
								matchStats.getHomeTeamScoreData().setTotalFives(matchStats.getHomeTeamScoreData().getTotalFives()+1);
							} else if(events.get(i).getEventInningNumber() == 2) {
								matchStats.getAwayTeamScoreData().setTotalFives(matchStats.getAwayTeamScoreData().getTotalFives()+1);
							}
							break;
						case CricketUtil.DOT:case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
							if(events.get(i).getEventInningNumber() == 1) {
								matchStats.getHomeTeamScoreData().setTotalDots(matchStats.getHomeTeamScoreData().getTotalDots()+1);
							} else if(events.get(i).getEventInningNumber() == 2) {
								matchStats.getAwayTeamScoreData().setTotalDots(matchStats.getAwayTeamScoreData().getTotalDots()+1);
							}
							break;
						  case CricketUtil.LOG_ANY_BALL:
							  	if(events.get(i).getEventSubExtra() != null && !events.get(i).getEventSubExtra().isEmpty()) {
							  		if (events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE)||
						            		events.get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
						                if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
						                	if (events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
							                	if(events.get(i).getEventInningNumber() == 1) {
													matchStats.getHomeTeamScoreData().setTotalDots(matchStats.getHomeTeamScoreData().getTotalDots()+1);
												} else if(events.get(i).getEventInningNumber() == 2) {
													matchStats.getAwayTeamScoreData().setTotalDots(matchStats.getAwayTeamScoreData().getTotalDots()+1);
												}
							                }
						                }
						            }
							  	}
					            break;
						case CricketUtil.LOG_WICKET:
							if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT) || events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CAUGHT_AND_BOWLED)
	                        		|| events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.BOWLED) || events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.STUMPED)
	                        		|| events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.LBW) || events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_WICKET)
	                        		|| events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_BALL_TWICE)) 
                        		{
                        		
								if(events.get(i).getEventInningNumber() == 1) {
									matchStats.getHomeTeamScoreData().setTotalDots(matchStats.getHomeTeamScoreData().getTotalDots()+1);
								} else if(events.get(i).getEventInningNumber() == 2) {
									matchStats.getAwayTeamScoreData().setTotalDots(matchStats.getAwayTeamScoreData().getTotalDots()+1);
								}
                        	}else if(events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
                        		if(events.get(i).getEventRuns() == 0) {
                        			if(events.get(i).getEventInningNumber() == 1) {
    									matchStats.getHomeTeamScoreData().setTotalDots(matchStats.getHomeTeamScoreData().getTotalDots()+1);
    								} else if(events.get(i).getEventInningNumber() == 2) {
    									matchStats.getAwayTeamScoreData().setTotalDots(matchStats.getAwayTeamScoreData().getTotalDots()+1);
    								}
                        		}
                        	}
							switch (String.valueOf(events.get(i).getEventRuns())) {
							case CricketUtil.ONE:
								if(events.get(i).getEventInningNumber() == 1) {
									matchStats.getHomeTeamScoreData().setTotalOnes(matchStats.getHomeTeamScoreData().getTotalOnes()+1);
								} else if(events.get(i).getEventInningNumber() == 2) {
									matchStats.getAwayTeamScoreData().setTotalOnes(matchStats.getAwayTeamScoreData().getTotalOnes()+1);
								}
								break;
							case CricketUtil.TWO:
								if(events.get(i).getEventInningNumber() == 1) {
									matchStats.getHomeTeamScoreData().setTotalTwos(matchStats.getHomeTeamScoreData().getTotalTwos()+1);
								} else if(events.get(i).getEventInningNumber() == 2) {
									matchStats.getAwayTeamScoreData().setTotalTwos(matchStats.getAwayTeamScoreData().getTotalTwos()+1);
								}
								break;
							case CricketUtil.THREE:
								if(events.get(i).getEventInningNumber() == 1) {
									matchStats.getHomeTeamScoreData().setTotalThrees(matchStats.getHomeTeamScoreData().getTotalThrees()+1);
								} else if(events.get(i).getEventInningNumber() == 2) {
									matchStats.getAwayTeamScoreData().setTotalThrees(matchStats.getAwayTeamScoreData().getTotalThrees()+1);
								}
								break;
							case CricketUtil.FIVE:
								if(events.get(i).getEventInningNumber() == 1) {
									matchStats.getHomeTeamScoreData().setTotalFives(matchStats.getHomeTeamScoreData().getTotalFives()+1);
								} else if(events.get(i).getEventInningNumber() == 2) {
									matchStats.getAwayTeamScoreData().setTotalFives(matchStats.getAwayTeamScoreData().getTotalFives()+1);
								}
								break;
							}
							break;	
						}
						//Player stats
						    
					    VariousStats bowlerStats = null , batterStats = null;

					    for (VariousStats varStat : matchStats.getPlayerStats()) {
					        if (varStat.getId() == events.get(i).getEventBowlerNo() && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BOWL)) {
					            bowlerStats = varStat;
					        }
					        if (varStat.getId() == events.get(i).getEventBatterNo() && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BAT)) {
					            batterStats = varStat;
					        }
					    }

					    if (bowlerStats == null) {
					        bowlerStats = new VariousStats(events.get(i).getEventBowlerNo(), CricketUtil.BOWL);
					        matchStats.getPlayerStats().add(bowlerStats);
					    }
					    if (batterStats == null) {
					        batterStats = new VariousStats(events.get(i).getEventBatterNo(), CricketUtil.BAT);
					        matchStats.getPlayerStats().add(batterStats);
					    }
					    
					  //batter timeline
					    batterStats.setThisOverTxt(batterStats.getThisOverTxt().isEmpty() ? updateOverBatter(events.get(i)) : 
					    	batterStats.getThisOverTxt() + "," + updateOverBatter(events.get(i)));
					    //bowler timeline
					    bowlerStats.setThisOverTxt(bowlerStats.getThisOverTxt().isEmpty() ? updateOverBowler(events.get(i)).split(",")[0] : 
					    	bowlerStats.getThisOverTxt() + "," + updateOverBowler(events.get(i)).split(",")[0]);

					    switch (events.get(i).getEventType()) {
				        case CricketUtil.ONE:
				            bowlerStats.setTotalOnes(bowlerStats.getTotalOnes() + 1);
				            batterStats.setTotalOnes(batterStats.getTotalOnes() + 1);
				            break;
				        case CricketUtil.TWO:
				            bowlerStats.setTotalTwos(bowlerStats.getTotalTwos() + 1);
				            batterStats.setTotalTwos(batterStats.getTotalTwos() + 1);
				            break;
				        case CricketUtil.THREE:
				            bowlerStats.setTotalThrees(bowlerStats.getTotalThrees() + 1);
				            batterStats.setTotalThrees(batterStats.getTotalThrees() + 1);
				            break;
				        case CricketUtil.FIVE:
				            bowlerStats.setTotalFives(bowlerStats.getTotalFives() + 1);
				            batterStats.setTotalFives(batterStats.getTotalFives() + 1);
				            break;
				        case CricketUtil.DOT: case CricketUtil.LEG_BYE: case CricketUtil.BYE:
				            bowlerStats.setTotalDots(bowlerStats.getTotalDots() + 1);
				            batterStats.setTotalDots(batterStats.getTotalDots() + 1);
				            break;
				        case CricketUtil.NO_BALL:
				        	batterStats.setTotalDots(batterStats.getTotalDots() + 1);
				        	break;
				        case CricketUtil.FOUR:
				            bowlerStats.setTotalFours(bowlerStats.getTotalFours() + 1);
				            batterStats.setTotalFours(batterStats.getTotalFours() + 1);
				            break;
				        case CricketUtil.SIX:
				            bowlerStats.setTotalSixes(bowlerStats.getTotalSixes() + 1);
				            batterStats.setTotalSixes(batterStats.getTotalSixes() + 1);
				            break;
				        case CricketUtil.NINE:
				            bowlerStats.setTotalNines(bowlerStats.getTotalNines() + 1);
				            batterStats.setTotalNines(batterStats.getTotalNines() + 1);
				            break;
				        case CricketUtil.LOG_ANY_BALL:
				        	if(events.get(i).getEventExtra() != null && !events.get(i).getEventExtra().isEmpty()) {
				        		if (events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
					                if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() && 
					                		events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT) && events.get(i).getEventRuns() <= 0) {
					                    batterStats.setTotalDots(batterStats.getTotalDots() + 1);
					                }
					                if (events.get(i).getEventWasABoundary() != null && events.get(i).getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
					                    if (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) {
					                        bowlerStats.setTotalFours(bowlerStats.getTotalFours() + 1);
					                        batterStats.setTotalFours(batterStats.getTotalFours() + 1);
					                    } else if (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.SIX)) {
					                        bowlerStats.setTotalSixes(bowlerStats.getTotalSixes() + 1);
					                        batterStats.setTotalSixes(batterStats.getTotalSixes() + 1);
					                    } else if (events.get(i).getEventRuns() == Integer.valueOf(CricketUtil.NINE)) {
					                        bowlerStats.setTotalNines(bowlerStats.getTotalNines() + 1);
					                        batterStats.setTotalNines(batterStats.getTotalNines() + 1);
					                    }
					                }
					            }
				        	}
				            break;
				        case CricketUtil.LOG_WICKET:
				            switch (String.valueOf(events.get(i).getEventRuns())) {
				                case CricketUtil.ONE:
				                    bowlerStats.setTotalOnes(bowlerStats.getTotalOnes() + 1);
				                    batterStats.setTotalOnes(batterStats.getTotalOnes() + 1);
				                    break;
				                case CricketUtil.TWO:
				                    bowlerStats.setTotalTwos(bowlerStats.getTotalTwos() + 1);
				                    batterStats.setTotalTwos(batterStats.getTotalTwos() + 1);
				                    break;
				                case CricketUtil.THREE:
				                    bowlerStats.setTotalThrees(bowlerStats.getTotalThrees() + 1);
				                    batterStats.setTotalThrees(batterStats.getTotalThrees() + 1);
				                    break;
				                case CricketUtil.FIVE:
				                    bowlerStats.setTotalFives(bowlerStats.getTotalFives() + 1);
				                    batterStats.setTotalFives(batterStats.getTotalFives() + 1);
				                    break;
				                case CricketUtil.DOT:
				                    bowlerStats.setTotalDots(bowlerStats.getTotalDots() + 1);
				                    batterStats.setTotalDots(batterStats.getTotalDots() + 1);
				                    break;
				            }
				            break;
					    }					    
					 //Powerplay
						    switch(events.get(i).getEventType()) {
						    	
						    	case CricketUtil.LOG_ANY_BALL: case CricketUtil.WIDE: case CricketUtil.NO_BALL:
							    	if( events.get(i).getEventBowlerNo() > 0) {
								         if (events.get(i).getEventOverNo() < matchStats.getPhase1EndOver()) {
								        	String thisOver = updateOverStats(events.get(i));
								        	if(thisOverTxt.isEmpty()) {
								        		thisOverTxt = thisOver.split(",")[0];
								        	}else {
								        		thisOverTxt += ","+thisOver.split(",")[0];
								        	}
								        	thisOverRun +=Integer.valueOf(thisOver.split(",")[1]);
								        	thisOverWkts +=Integer.valueOf(thisOver.split(",")[2]);
								        	
											statsData = getpowerplay(events.get(i));
											if(statsData.contains(",") && statsData.split(",").length >= 7) {
												if(events.get(i).getEventInningNumber()==1) {
													//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 1 HOME
													updateMatchStats(matchStats.getHomeFirstPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
													//PHASE_SCORE PHASE 1 HOME
													matchStats.setHomeFirstPowerPlay(new VariousStats(
															matchStats.getHomeFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
															matchStats.getHomeFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
															matchStats.getHomeFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
															matchStats.getHomeFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
															matchStats.getHomeFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
															Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeFirstPowerPlay().getOutBatsman().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
														        : matchStats.getHomeFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
														    : matchStats.getHomeFirstPowerPlay().getOutBatsman(),
														    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeFirstPowerPlay().getNotWicketCount().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
															        : matchStats.getHomeFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
															    : matchStats.getHomeFirstPowerPlay().getNotWicketCount()));		
													
												}else if(events.get(i).getEventInningNumber()==2) {
													//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 1 AWAY
													updateMatchStats(matchStats.getAwayFirstPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
													//PHASE_SCORE PHASE 1 AWAY
													matchStats.setAwayFirstPowerPlay(new VariousStats(
															matchStats.getAwayFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
															matchStats.getAwayFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
															matchStats.getAwayFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
															matchStats.getAwayFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
															matchStats.getAwayFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
															Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayFirstPowerPlay().getOutBatsman().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
														        : matchStats.getAwayFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
														    : matchStats.getAwayFirstPowerPlay().getOutBatsman(),
														    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayFirstPowerPlay().getNotWicketCount().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
															        : matchStats.getAwayFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
															    : matchStats.getAwayFirstPowerPlay().getNotWicketCount()));
												 
													}
												
												}
											}
										if (events.get(i).getEventOverNo() == (matchStats.getPhase2StartOver() - 1) || (events.get(i).getEventOverNo() > (matchStats.getPhase2StartOver() - 1) && 
								                    events.get(i).getEventOverNo() < matchStats.getPhase2EndOver())) {

												statsData = getpowerplay(events.get(i));
												if(statsData.contains(",") && statsData.split(",").length >= 7) {
													if(events.get(i).getEventInningNumber()==1) {
														//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 2 HOME
														updateMatchStats(matchStats.getHomeSecondPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
														//PHASE_SCORE PHASE 2 HOME
														matchStats.setHomeSecondPowerPlay(new VariousStats(
																matchStats.getHomeSecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																matchStats.getHomeSecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																matchStats.getHomeSecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																matchStats.getHomeSecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																matchStats.getHomeSecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeSecondPowerPlay().getOutBatsman().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
															        : matchStats.getHomeSecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
															    : matchStats.getHomeSecondPowerPlay().getOutBatsman(),
															    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeSecondPowerPlay().getNotWicketCount().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																        : matchStats.getHomeSecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																    : matchStats.getHomeSecondPowerPlay().getNotWicketCount()));
														
													}else if(events.get(i).getEventInningNumber()==2) {
														//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 2 AWAY
														updateMatchStats(matchStats.getAwaySecondPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
														//PHASE_SCORE PHASE 2 AWAY
														matchStats.setAwaySecondPowerPlay(new VariousStats(
																matchStats.getAwaySecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																matchStats.getAwaySecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																matchStats.getAwaySecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																matchStats.getAwaySecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																matchStats.getAwaySecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwaySecondPowerPlay().getOutBatsman().isEmpty()? 
																	String.valueOf(Integer.valueOf(statsData.split(",")[7]))
															        : matchStats.getAwaySecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
															    : matchStats.getAwaySecondPowerPlay().getOutBatsman(),
															    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwaySecondPowerPlay().getNotWicketCount().isEmpty()? 
																		String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																        : matchStats.getAwaySecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																    : matchStats.getAwaySecondPowerPlay().getNotWicketCount()
																));
													 }
												}
											}
											if (events.get(i).getEventOverNo() == (matchStats.getPhase3StartOver() - 1) || (events.get(i).getEventOverNo() > (matchStats.getPhase3StartOver() - 1) && 
								                    events.get(i).getEventOverNo() <= matchStats.getPhase3EndOver())) {

												    statsData = getpowerplay(events.get(i));
													
													if(statsData.contains(",") && statsData.split(",").length >= 7) {
														if(events.get(i).getEventInningNumber()==1) {
															//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 3 HOME
															updateMatchStats(matchStats.getHomeThirdPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
															//PHASE_SCORE PHASE 3 HOME
															matchStats.setHomeThirdPowerPlay(new VariousStats(
																	matchStats.getHomeThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																	matchStats.getHomeThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																	matchStats.getHomeThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																	matchStats.getHomeThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																	matchStats.getHomeThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																	Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeThirdPowerPlay().getOutBatsman().isEmpty()
																	        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
																	        : matchStats.getHomeThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
																	    : matchStats.getHomeThirdPowerPlay().getOutBatsman(),
																	    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeThirdPowerPlay().getNotWicketCount().isEmpty()
																		        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																		        : matchStats.getHomeThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																		    : matchStats.getHomeThirdPowerPlay().getNotWicketCount()));
														
														}else if(events.get(i).getEventInningNumber()==2) {
															//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 3 AWAY
															updateMatchStats(matchStats.getAwayThirdPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
															//PHASE_SCORE PHASE 3 AWAY
															matchStats.setAwayThirdPowerPlay(new VariousStats(
																	matchStats.getAwayThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																	matchStats.getAwayThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																	matchStats.getAwayThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																	matchStats.getAwayThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																	matchStats.getAwayThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																	Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayThirdPowerPlay().getOutBatsman().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
																        : matchStats.getAwayThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
																    : matchStats.getAwayThirdPowerPlay().getOutBatsman(),
																    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayThirdPowerPlay().getNotWicketCount().isEmpty()
																	        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																	        : matchStats.getAwayThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																	    : matchStats.getAwayThirdPowerPlay().getNotWicketCount()
																	));
														 }
													}
											}
									} 
						    		break;
						    	case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
							    case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
							    case CricketUtil.LOG_WICKET:case CricketUtil.NINE:
							    	
							    	if( events.get(i).getEventBowlerNo() > 0) {
										if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= (matchStats.getPhase1StartOver() - 1) * 6 &&
									                    ((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= matchStats.getPhase1EndOver() * 6)) {
											
											String thisOver = updateOverStats(events.get(i));
											if(thisOverTxt.isEmpty()) {
								        		thisOverTxt = thisOver.split(",")[0];
								        	}else {
								        		thisOverTxt += ","+thisOver.split(",")[0];
								        	}
								        	thisOverRun +=Integer.valueOf(thisOver.split(",")[1]);
								        	thisOverWkts +=Integer.valueOf(thisOver.split(",")[2]);
								        	
											statsData = getpowerplay(events.get(i));
											
											if(statsData.contains(",") && statsData.split(",").length >= 7) {
												if(events.get(i).getEventInningNumber()==1) {
													//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 1 HOME
													updateMatchStats(matchStats.getHomeFirstPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
													//PHASE_SCORE PHASE 1 HOME
													matchStats.setHomeFirstPowerPlay(new VariousStats(
															matchStats.getHomeFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
															matchStats.getHomeFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
															matchStats.getHomeFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
															matchStats.getHomeFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
															matchStats.getHomeFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
															Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeFirstPowerPlay().getOutBatsman().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
														        : matchStats.getHomeFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
														    : matchStats.getHomeFirstPowerPlay().getOutBatsman(),
														    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeFirstPowerPlay().getNotWicketCount().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
															        : matchStats.getHomeFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
															    : matchStats.getHomeFirstPowerPlay().getNotWicketCount()));	
													
												}else if(events.get(i).getEventInningNumber()==2) {
													//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 1 AWAY
													updateMatchStats(matchStats.getAwayFirstPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
													//PHASE_SCORE PHASE 1 AWAY
													matchStats.setAwayFirstPowerPlay(new VariousStats(
															matchStats.getAwayFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
															matchStats.getAwayFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
															matchStats.getAwayFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
															matchStats.getAwayFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
															matchStats.getAwayFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
															Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayFirstPowerPlay().getOutBatsman().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
														        : matchStats.getAwayFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
														    : matchStats.getAwayFirstPowerPlay().getOutBatsman(),
														    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayFirstPowerPlay().getNotWicketCount().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
															        : matchStats.getAwayFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
															    : matchStats.getAwayFirstPowerPlay().getNotWicketCount()));
														
													}
												
												}
											}
										
										 if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= ((matchStats.getPhase2StartOver() - 1) * 6) + 1) &&
								                    (((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= ((matchStats.getPhase2EndOver()) * 6))) {
 
												statsData = getpowerplay(events.get(i));
												
												if(statsData.contains(",") && statsData.split(",").length >= 7) {
													if(events.get(i).getEventInningNumber()==1) {
														//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 2 HOME
														updateMatchStats(matchStats.getHomeSecondPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
														//PHASE_SCORE PHASE 2 HOME
														matchStats.setHomeSecondPowerPlay(new VariousStats(
																matchStats.getHomeSecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																matchStats.getHomeSecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																matchStats.getHomeSecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																matchStats.getHomeSecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																matchStats.getHomeSecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeSecondPowerPlay().getOutBatsman().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
															        : matchStats.getHomeSecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
															    : matchStats.getHomeSecondPowerPlay().getOutBatsman(),
															    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeSecondPowerPlay().getNotWicketCount().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																        : matchStats.getHomeSecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																    : matchStats.getHomeSecondPowerPlay().getNotWicketCount()));
														
													}else if(events.get(i).getEventInningNumber()==2) {
														//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 2 AWAY
														updateMatchStats(matchStats.getAwaySecondPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
														//PHASE_SCORE PHASE 2 AWAY
														matchStats.setAwaySecondPowerPlay(new VariousStats(
																matchStats.getAwaySecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																matchStats.getAwaySecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																matchStats.getAwaySecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																matchStats.getAwaySecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																matchStats.getAwaySecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwaySecondPowerPlay().getOutBatsman().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
															        : matchStats.getAwaySecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
															    : matchStats.getAwaySecondPowerPlay().getOutBatsman(),
															    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwaySecondPowerPlay().getNotWicketCount().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																        : matchStats.getAwaySecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																    : matchStats.getAwaySecondPowerPlay().getNotWicketCount()));
														
													 }
												}
											}
										if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= ((matchStats.getPhase3StartOver() - 1) * 6) + 1) &&
							                    (((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= ((matchStats.getPhase3EndOver()) * 6))) {
   
													statsData = getpowerplay(events.get(i));
													
													if(statsData.contains(",") && statsData.split(",").length >= 7) {
														if(events.get(i).getEventInningNumber()==1) {
															//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 3 HOME
															updateMatchStats(matchStats.getHomeThirdPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
															//PHASE_SCORE PHASE 3 HOME
															matchStats.setHomeThirdPowerPlay(new VariousStats(
																	matchStats.getHomeThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																	matchStats.getHomeThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																	matchStats.getHomeThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																	matchStats.getHomeThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																	matchStats.getHomeThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																	Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeThirdPowerPlay().getOutBatsman().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
																        : matchStats.getHomeThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
																    : matchStats.getHomeThirdPowerPlay().getOutBatsman(),
																    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeThirdPowerPlay().getNotWicketCount().isEmpty()
																	        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																	        : matchStats.getHomeThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																	    : matchStats.getHomeThirdPowerPlay().getNotWicketCount()));
					
														}else if(events.get(i).getEventInningNumber()==2) {
															//PHASE_SCORE BATSMAN /BOWLER STATS PHASE 3 AWAY
															updateMatchStats(matchStats.getAwayThirdPowerPlayBatsman(), events.get(i).getEventBatterNo(), events.get(i).getEventBowlerNo(), statsData, events.get(i).getEventType());
															//PHASE_SCORE PHASE 3 AWAY
															matchStats.setAwayThirdPowerPlay(new VariousStats(
																	matchStats.getAwayThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
																	matchStats.getAwayThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
																	matchStats.getAwayThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
																	matchStats.getAwayThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
																	matchStats.getAwayThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
																	Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayThirdPowerPlay().getOutBatsman().isEmpty()
																        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
																        : matchStats.getAwayThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
																    : matchStats.getAwayThirdPowerPlay().getOutBatsman(),
																    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayThirdPowerPlay().getNotWicketCount().isEmpty()
																	        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
																	        : matchStats.getAwayThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
																	    : matchStats.getAwayThirdPowerPlay().getNotWicketCount()));
		
														 }
													}
											}
									} 
						    		
						    		break;
						    }
						//OverByOverData
						statsData = getpowerplay(events.get(i));
						switch(events.get(i).getEventType()) {
						case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
							if(Integer.valueOf(statsData.split(",")[7]) > 0) {
								outBatsman = outBatsman.isEmpty()? String.valueOf(Integer.valueOf(statsData.split(",")[7])): outBatsman + "," + Integer.valueOf(statsData.split(",")[7]);
							}
							if(Integer.valueOf(statsData.split(",")[8]) > 0) {
								notWicketCount = notWicketCount.isEmpty()? String.valueOf(Integer.valueOf(statsData.split(",")[8])): notWicketCount + "," + Integer.valueOf(statsData.split(",")[8]);
							}
							if(events.get(i).getEventInningNumber()==1) {
								overbyRun += events.get(i).getEventRuns()+ events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns();
							}else if(events.get(i).getEventInningNumber()==2) {
								overbyRun1 += events.get(i).getEventRuns()+ events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns();
							}
							if(events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty() && 
							  (!events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)&&
							  !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) &&
							  !events.get(i).getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
								if(events.get(i).getEventInningNumber()==1) {
									overbyWkts ++;
								}else if(events.get(i).getEventInningNumber()==2) {
									overbyWkts1 ++;
								}
							}
						break;
						default:
							if(events.get(i).getEventInningNumber()==1) {
								overbyRun += events.get(i).getEventRuns();
							}else if(events.get(i).getEventInningNumber()==2) {
								overbyRun1 += events.get(i).getEventRuns();
							}
							if(Integer.valueOf(statsData.split(",")[7]) > 0) {
								outBatsman = outBatsman.isEmpty()? String.valueOf(Integer.valueOf(statsData.split(",")[7])): outBatsman + "," + Integer.valueOf(statsData.split(",")[7]);
							}
							if(Integer.valueOf(statsData.split(",")[8]) > 0) {
								notWicketCount = notWicketCount.isEmpty()? String.valueOf(Integer.valueOf(statsData.split(",")[8])): notWicketCount + "," + Integer.valueOf(statsData.split(",")[8]);
							}
							break;
						}
					} 
				}
			}
		
		for (Event even : Event) {
		    int Batter_id = even.getEventBattingCard().getPlayerId();
//		    boolean isRetired = even.getEventHowOut() != null && !even.getEventHowOut().isEmpty() && 
//                    (even.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) ||
//                     even.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) ||
//                     even.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED));
		    
		    boolean isRetired = even.getEventHowOut() != null && !even.getEventHowOut().isEmpty() && 
                    (even.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_OUT));
		    
		    // First PP
		    updateWickets(matchStats.getHomeFirstPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwayFirstPowerPlay(), Batter_id, isRetired);
		    
		    // Second PP
		    updateWickets(matchStats.getHomeSecondPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwaySecondPowerPlay(), Batter_id, isRetired);
		    
		    // Third PP
		    updateWickets(matchStats.getHomeThirdPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwayThirdPowerPlay(), Batter_id, isRetired);
		    
		    // Inning Compare
		    updateWickets(matchStats.getInningCompare(), Batter_id, isRetired);
		    
		    //OverByOver
		    updateWickets(matchStats.getHomeOverByOverData(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwayOverByOverData(), Batter_id, isRetired);
		}
		
		Collections.reverse(matchStats.getHomeOverByOverData());
		Collections.reverse(matchStats.getAwayOverByOverData());
		return matchStats;
	}

	public static void updateWickets(VariousStats powerPlayStats, int batterId, boolean isRetired) {
		
//	    boolean isBatterOut = Arrays.stream(powerPlayStats.getOutBatsman().split(",")).map(String::trim)
//	    		.filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).anyMatch(id -> id == batterId);
	    boolean isBatterNotOut = Arrays.stream(powerPlayStats.getNotWicketCount().split(",")).map(String::trim)
	            .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).anyMatch(id -> id == batterId);
	    
//	    if (isBatterOut && isRetired) {
//	    	powerPlayStats.setTotalWickets(Math.max(0, powerPlayStats.getTotalWickets() - 1));
//	    }else if ((isBatterOut && !isRetired) || (isBatterNotOut && !isRetired)) {
//	    	powerPlayStats.setTotalWickets(Math.max(0, powerPlayStats.getTotalWickets() + 1));
//	    }
	    
	    if (isBatterNotOut && isRetired) {
	    	powerPlayStats.setTotalWickets(Math.max(0, powerPlayStats.getTotalWickets() + 1));
	    }
	}
	
	public static void updateWickets(List<OverByOverData> powerPlayStats, int batterId, boolean isRetired) {
	    for (OverByOverData stat : powerPlayStats) {
	    	
	    	stat.setOverTotalWickets((int) Arrays.stream(stat.getOutBatsman().split(",")).filter(s -> !s.trim().isEmpty()).count());

//	        boolean isBatterOut = Arrays.stream(stat.getOutBatsman().split(",")).map(String::trim)
//	                .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).anyMatch(id -> id == batterId);
	        boolean isBatterNotOut = Arrays.stream(stat.getNotWicketCount().split(",")).map(String::trim)
	                .filter(s -> !s.isEmpty()).mapToInt(Integer::parseInt).anyMatch(id -> id == batterId);

	        int wickets = stat.getOverTotalWickets();
//	        if (isBatterOut && isRetired) {
//	            stat.setOverTotalWickets(Math.max(0, wickets - 1));
//	        } else if ((isBatterOut && !isRetired) || (isBatterNotOut && !isRetired)) {
//		            if (stat.getOutBatsman().isEmpty()) {
//		                stat.setOutBatsman(String.valueOf(batterId));
//		            } else if (!stat.getOutBatsman().contains(String.valueOf(batterId))) {
//		                stat.setOutBatsman(stat.getOutBatsman() + "," + batterId);
//		            }
//	            stat.setOverTotalWickets(stat.getOverTotalWickets() + 1);
//	        }
	        
	        if (isBatterNotOut && isRetired) {
	            stat.setOverTotalWickets(Math.max(0, wickets + 1));
	        } 
	    }
	}
	public static MatchStats getAllEvents(MatchAllData Match, String Broadcaster, List<Event> events) {
		 MatchStats matchStats = new MatchStats();
	    if (Match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.D10)) {
	    	
	    	matchStats.setPhase1StartOver(1); matchStats.setPhase1EndOver(2);
        	matchStats.setPhase2StartOver(3); matchStats.setPhase2EndOver(6);
        	matchStats.setPhase3StartOver(7); matchStats.setPhase3EndOver(10);

	    } else if (Match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || 
	                Match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)) {
	    	switch (Broadcaster) {
//			case "LEGENDS-90":
//				matchStats.setPhase1StartOver(1); matchStats.setPhase1EndOver(4);
//	        	matchStats.setPhase2StartOver(5); matchStats.setPhase2EndOver(10);
//	        	matchStats.setPhase3StartOver(11); matchStats.setPhase3EndOver(15);
//				break;
			default:
				matchStats.setPhase1StartOver(1); matchStats.setPhase1EndOver(6);
	        	matchStats.setPhase2StartOver(7); matchStats.setPhase2EndOver(15);
	        	matchStats.setPhase3StartOver(16); matchStats.setPhase3EndOver(20);
				break;
			}
	    }else if(Match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) ||
	    		Match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
	    	matchStats.setPhase1StartOver(1); matchStats.setPhase1EndOver(10);
        	matchStats.setPhase2StartOver(11); matchStats.setPhase2EndOver(40);
        	matchStats.setPhase3StartOver(41); matchStats.setPhase3EndOver(50);
	    }
	   
	    return getAllEventsStatsMASTER(matchStats ,Match.getMatch(), Match.getEventFile().getEvents());
	}
	public static String updateOverStats(Event events) {
	    String ThisOverTxt = "";
	    int ThisOverRun = 0; int ThisOverwkts = 0;
	    switch (events.getEventType()) {
	    	
	        case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: 
	        case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE:

	            ThisOverTxt = (events.getEventWasABoundary() != null && events.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES) 
	                ? events.getEventRuns() + "BOUNDARY" : events.getEventRuns()+"");
	            ThisOverRun += events.getEventRuns();

	            break;

	        case CricketUtil.LOG_ANY_BALL:
	        	
	        	ThisOverRun += events.getEventRuns()+events.getEventExtraRuns() + events.getEventSubExtraRuns();
	            
	            if(events.getEventExtra() != null) {
	            	if (events.getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	            		if(events.getEventSubExtra() != null) {
	            			if ((events.getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE) || events.getEventSubExtra().equalsIgnoreCase(CricketUtil.NO_BALL))) {
			                    if (!events.getEventSubExtra().isEmpty() && events.getEventSubExtraRuns() > 0) {
			                    	 ThisOverTxt = ThisOverTxt + (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns()) +
			                            events.getEventExtra();
			                    } else {
			                        if (!events.getEventExtra().equalsIgnoreCase(events.getEventSubExtra())) {
			                            ThisOverTxt = ThisOverTxt +  events.getEventExtra() + "+" +
			                                (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns() > 1 ?
			                                    (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns()) : "") +
			                                events.getEventSubExtra();
			                        } else {
			                            ThisOverTxt = ThisOverTxt +  (events.getEventRuns() +
			                                events.getEventExtraRuns() + events.getEventSubExtraRuns()) + events.getEventExtra();
			                        }
			                    }
			                } else if ((events.getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE) || events.getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE))) {
			                	ThisOverTxt = 	ThisOverTxt + events.getEventExtra() + "+" + (events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() 
			                				+ events.getEventSubExtra() : events.getEventSubExtra()) + (events.getEventRuns() > 0 ? "+" + events.getEventRuns() : "");
			                } else {
			                	if(events.getEventSubExtra().isEmpty()) {
									if(events.getEventRuns() > 0) {
										ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventRuns();
									}else {
										ThisOverTxt = ThisOverTxt + events.getEventExtra();		
									}
								}else {
									
									if(events.getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
										ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + CricketUtil.PENALTY;
										if(events.getEventRuns() > 0) {
											ThisOverTxt = ThisOverTxt + "+" + events.getEventRuns(); 
										}
									}else {
										if(events.getEventRuns() > 0) {
											ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventRuns(); 
										}else {
											ThisOverTxt = ThisOverTxt + events.getEventExtra() + events.getEventExtra();		
										}
										if(events.getEventSubExtraRuns() > 0) {
											ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventSubExtra() + "+" + events.getEventSubExtraRuns(); 
										}else {
											ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventSubExtra();	
										}

									}
								}
			                }
	            		}else {
	            			ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") + events.getEventExtra();
	            		}
		            }else {
		            	ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") +
			                    (events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() : "") + events.getEventSubExtra();
			        }
	            }else {
	            	if(events.getEventSubExtra().equalsIgnoreCase(CricketUtil.PENALTY)) {
	        			if(events.getDoNotIncrementBall().equalsIgnoreCase(CricketUtil.YES)) {
	        				ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") +
	        						(events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() : "") +  events.getEventSubExtra()+"_Y";
	        			}else {
	        				ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") +
	        						(events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() : "") +  events.getEventSubExtra()+"_N";
	        			}
	        		}else {
	        			ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") +
	        					(events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() : "") +  events.getEventSubExtra();
	        		} 
	            }
	            
	            if (events.getEventHowOut() != null && !events.getEventHowOut().isEmpty()) {
	                ThisOverTxt = ThisOverTxt + "+" + CricketUtil.LOG_WICKET;
	                ThisOverwkts++;
	            }
	            break;

	        case CricketUtil.LOG_WICKET:
	            switch (events.getEventHowOut().toUpperCase()) {
	                case CricketUtil.ABSENT_HURT:
	                case CricketUtil.RETIRED_HURT:
	                    break;
	                default:
	                    if (events.getEventRuns() + events.getEventExtraRuns() +
	                        events.getEventSubExtraRuns() > 0) {
	                        ThisOverTxt = String.valueOf(events.getEventRuns() + events.getEventExtraRuns() +
	                            events.getEventSubExtraRuns()) + "+" + events.getEventType();
	                    } else {
	                        ThisOverTxt = events.getEventType();
	                    }
	                    ThisOverRun += events.getEventRuns();
	                    ThisOverwkts++;
	                    break;
	            }
	            break;

	        case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.WIDE: case CricketUtil.PENALTY:
	            switch (events.getEventType()) {
	                case CricketUtil.WIDE: case CricketUtil.NO_BALL:
	                    if (events.getEventRuns() > 1) {
	                        ThisOverTxt = String.valueOf((events.getEventRuns() + events.getEventSubExtraRuns())) + events.getEventType();
	                    } else {
	                       ThisOverTxt =  events.getEventType();
	                    }
	    	        	ThisOverRun += events.getEventRuns()+ events.getEventSubExtraRuns();
	                    break;
	                case CricketUtil.BYE: case CricketUtil.LEG_BYE:
	                    if (events.getEventRuns() > 1) {
	                        ThisOverTxt = String.valueOf((events.getEventRuns() + events.getEventSubExtraRuns())) + events.getEventType();
	                    } else {
	                       ThisOverTxt =  events.getEventRuns() + events.getEventType();
	                    }
	    	        	ThisOverRun += events.getEventRuns()+ events.getEventSubExtraRuns();
	                    break;    
	                case CricketUtil.PENALTY:
	                    ThisOverTxt = String.valueOf((events.getEventRuns() + events.getEventExtraRuns() +
	                        events.getEventSubExtraRuns())) + "+" + events.getEventType();
	    	        	ThisOverRun += events.getEventRuns()+events.getEventExtraRuns() + events.getEventSubExtraRuns();
	                    break;
	            }
	            break;
	    }
		return ThisOverTxt+","+ThisOverRun+","+ThisOverwkts;
	}
	public static String updateOverBatter(Event events) {
	    String ThisOverTxt = "";

	    switch (events.getEventType()) {
	        case CricketUtil.DOT:  case CricketUtil.ONE:  case CricketUtil.TWO: case CricketUtil.THREE:
	        case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE:
	            ThisOverTxt = (events.getEventWasABoundary() != null &&
	                           events.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES) 
	                           ? events.getEventRuns() + "BOUNDARY" : events.getEventRuns() + "");
	            break;
	        case CricketUtil.LOG_ANY_BALL: case CricketUtil.LOG_WICKET:
	        	if(events.getEventExtra() != null) {
	        		if(events.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	        			ThisOverTxt = events.getEventExtra() + "+" + (events.getEventRuns() > 0 ? events.getEventRuns() + "" : "");
	        		}else {
	        			ThisOverTxt = (events.getEventRuns() > 0 ? events.getEventRuns() + "" : "");
	        		}
	        	}
	            break;
	    }

	    return ThisOverTxt;
	}
	public static String updateOverBowler(Event events) {
	    String ThisOverTxt = "";
	    int ThisOverRun = 0; int ThisOverwkts = 0;
	    switch (events.getEventType()) {

	        case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: 
	        case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.NINE:

	            ThisOverTxt = 
	                (events.getEventWasABoundary() != null && events.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES) 
	                ? events.getEventRuns() + "BOUNDARY" : events.getEventRuns()+"");
	            ThisOverRun += events.getEventRuns();

	            break;

	        case CricketUtil.LOG_ANY_BALL:
	        	
	        	ThisOverRun += events.getEventRuns()+events.getEventExtraRuns() + events.getEventSubExtraRuns();
	            if (events.getEventHowOut() != null && !events.getEventHowOut().isEmpty()) {
	                ThisOverTxt = ThisOverTxt + CricketUtil.LOG_WICKET +
	                    (events.getEventExtra() != null && !events.getEventExtra().isEmpty() ? "+" : "");
	                ThisOverwkts++;
	            }
	            
	            if(events.getEventExtra() != null) {
	            	 if (events.getEventExtra().equals(CricketUtil.WIDE) || events.getEventExtra().equals(CricketUtil.NO_BALL)) {
	 	                if(events.getEventSubExtra() != null) {
	 	                	if (events.getEventSubExtra().equals(CricketUtil.WIDE) || events.getEventSubExtra().equals(CricketUtil.NO_BALL)) {
	 		                    if (!events.getEventSubExtra().isEmpty() && events.getEventSubExtraRuns() > 0) {
	 		                    	 ThisOverTxt = ThisOverTxt + (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns()) +
	 		                            events.getEventExtra();
	 		                    } else {
	 		                        if (!events.getEventExtra().equalsIgnoreCase(events.getEventSubExtra())) {
	 		                            ThisOverTxt = ThisOverTxt +  events.getEventExtra() + "+" +
	 		                                (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns() > 1 ?
	 		                                    (events.getEventRuns() + events.getEventExtraRuns() + events.getEventSubExtraRuns()) : "") +
	 		                                events.getEventSubExtra();
	 		                        } else {
	 		                            ThisOverTxt = ThisOverTxt +  (events.getEventRuns() +
	 		                                events.getEventExtraRuns() + events.getEventSubExtraRuns()) + events.getEventExtra();
	 		                        }
	 		                    }
	 		                } else if (events.getEventSubExtra().equals(CricketUtil.LEG_BYE) || events.getEventSubExtra().equals(CricketUtil.BYE)) {
	 		                	 ThisOverTxt = 	ThisOverTxt + events.getEventExtra() + "+" + (events.getEventRuns() + events.getEventSubExtraRuns() > 0 ?
	 		                            events.getEventSubExtra() + "+" + (events.getEventRuns() + events.getEventSubExtraRuns()) :
	 		                            events.getEventSubExtra());
	 		                } else {
	 		                    if (events.getEventSubExtra().isEmpty()) {
	 		                        if (events.getEventRuns() > 0) {
	 		                            ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventRuns();
	 		                        } else {
	 		                            ThisOverTxt = ThisOverTxt + events.getEventExtra();
	 		                        }
	 		                    } else {
	 		                    	if (events.getEventRuns() > 0) {
	 		                            ThisOverTxt = ThisOverTxt + events.getEventExtra() + "+" + events.getEventRuns();
	 		                        } else {
	 		                            ThisOverTxt = ThisOverTxt + events.getEventExtra();
	 		                        }
	 		                        if (events.getEventSubExtraRuns() > 0) {
	 		                            ThisOverTxt = ThisOverTxt + "+" + events.getEventSubExtra() + "+" + events.getEventSubExtraRuns();
	 		                        } else {
	 		                            ThisOverTxt = ThisOverTxt + "+" + events.getEventSubExtra();
	 		                        }
	 		                    }
	 		                }
	 	                }
	 	            }
	            }
	            else {
	                ThisOverTxt = ThisOverTxt + (events.getEventRuns() > 0 ? events.getEventRuns() + "+" : "") +
	                    (events.getEventSubExtraRuns() > 1 ? events.getEventSubExtraRuns() : "") + events.getEventSubExtra();
	            }
	            break;

	        case CricketUtil.LOG_WICKET:
	            switch (events.getEventHowOut().toUpperCase()) {
	                case CricketUtil.ABSENT_HURT:
	                case CricketUtil.RETIRED_HURT:
	                    break;
	                default:
	                    if (events.getEventRuns() + events.getEventExtraRuns() +
	                        events.getEventSubExtraRuns() > 0) {
	                        ThisOverTxt = String.valueOf(events.getEventRuns() + events.getEventExtraRuns() +
	                            events.getEventSubExtraRuns()) + "+" + events.getEventType();
	                    } else {
	                        ThisOverTxt = events.getEventType();
	                    }
	                    ThisOverRun += events.getEventRuns();
	                    ThisOverwkts++;
	                    break;
	            }
	            break;

	        case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.WIDE:
	        	if (events.getEventRuns() > 1) {
                    ThisOverTxt = String.valueOf((events.getEventRuns() + events.getEventSubExtraRuns())) + events.getEventType();
                } else {
                   ThisOverTxt =  events.getEventType();
                }
	            break;
	    }
		return ThisOverTxt+","+ThisOverRun+","+ThisOverwkts;
	}

	public static void updateMatchStats(List<VariousStats> matchStatsList, int batterNum, int bowlerNum, String statsData, String eventType) {
	    VariousStats batter = null, bowler = null;
	    // Loop to find the corresponding batter and bowler
	    for (VariousStats varStat : matchStatsList) {
	        if (varStat.getId() == batterNum && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BAT)) {
	            batter = varStat;
	        }
	        if (varStat.getId() == bowlerNum && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BOWL)) {
	            bowler = varStat;
	        }
	    }
	    // If batter is not found, create a new batter and add to list
	    if (batter == null) {
	        batter = new VariousStats(batterNum, CricketUtil.BAT);
	        batter.setOutnotOut("*");
	        matchStatsList.add(batter);
	    }

	    // If bowler is not found, create a new bowler and add to list
	    if (bowler == null) {
	        bowler = new VariousStats(bowlerNum, CricketUtil.BOWL);
	        matchStatsList.add(bowler);
	    }

	    // Update bowler's stats
	    bowler.setTotalRuns(bowler.getTotalRuns() + Integer.valueOf(statsData.split(",")[0]));
	    bowler.setTotalFours(bowler.getTotalFours() + Integer.valueOf(statsData.split(",")[3]));
	    bowler.setTotalSixes(bowler.getTotalSixes() + Integer.valueOf(statsData.split(",")[4]));
	    bowler.setTotalNines(bowler.getTotalNines() + Integer.valueOf(statsData.split(",")[5]));
	    bowler.setTotalBalls(bowler.getTotalBalls() + Integer.valueOf(statsData.split(",")[6]));
	    bowler.setTotalWickets(bowler.getTotalWickets() + Integer.valueOf(statsData.split(",")[1]));
	
	    // Update batter's stats
	    switch(eventType) {
	    case CricketUtil.BYE: case CricketUtil.LEG_BYE:
	    	break;
	    case CricketUtil.LOG_WICKET:
	    	batter.setOutnotOut("");
	    	batter.setTotalRuns(batter.getTotalRuns() + Integer.valueOf(statsData.split(",")[0]));
	    	break;
	    default:
	    	batter.setTotalRuns(batter.getTotalRuns() + Integer.valueOf(statsData.split(",")[0]));
	    	break;
	    }
	    
	    batter.setTotalFours(batter.getTotalFours() + Integer.valueOf(statsData.split(",")[3]));
	    batter.setTotalSixes(batter.getTotalSixes() + Integer.valueOf(statsData.split(",")[4]));
	    batter.setTotalNines(batter.getTotalNines() + Integer.valueOf(statsData.split(",")[5]));
	    batter.setTotalBalls(batter.getTotalBalls() + Integer.valueOf(statsData.split(",")[6]));

	    // If the batter is the one that got out, update the "out/not out" status
	    if (Integer.valueOf(statsData.split(",")[statsData.split(",").length - 1]) == batter.getId()) {
	        batter.setOutnotOut("");
	    }
	}
	public static List<VariousStats> BowlerDataPerOver(List<Event> events, int player_number, int inn_num) {
		List<VariousStats> bcard = new ArrayList<>();
		int currentOver = -1;
		int run = 0, wicket = 0, eventOver = 0;
		boolean bowlerActive = false;

		for (int i = 0; i < events.size(); i++) {
			Event event = events.get(i);

			if (event.getEventInningNumber() == inn_num) {
				// Bowler change event
				if (event.getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
					if (bowlerActive && event.getEventBowlerNo() != player_number) {
						// End of bowler's spell
						VariousStats vs = new VariousStats();
						vs.setOver(currentOver);
						vs.setTotalRuns(run);
						vs.setTotalWickets(wicket);
						bcard.add(vs);

						// Reset state
						currentOver = -1;
						run = 0;
						wicket = 0;
						bowlerActive = false;
					}
				}
				if (event.getEventBowlerNo() == player_number) {
					bowlerActive = true;
					eventOver = event.getEventOverNo();
					if (currentOver == -1) currentOver = eventOver;
					switch (event.getEventType()) {
						case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE:
						case CricketUtil.FOUR: case CricketUtil.FIVE: case CricketUtil.SIX:
						case CricketUtil.DOT: case CricketUtil.NINE:case CricketUtil.WIDE:case CricketUtil.NO_BALL:
							run += event.getEventRuns();
							break;

						case CricketUtil.LOG_WICKET:
							run += event.getEventRuns();
							String howOut = event.getEventHowOut();
							if (howOut != null && !howOut.equalsIgnoreCase(CricketUtil.RETIRED_HURT)
									&& !howOut.equalsIgnoreCase(CricketUtil.ABSENT_HURT)
									&& !howOut.equalsIgnoreCase(CricketUtil.CONCUSSED)) {
								wicket++;
							}
							break;

						case CricketUtil.LOG_ANY_BALL:
							run += event.getEventRuns();
							if (event.getEventExtra() != null) run += event.getEventExtraRuns();
							if (event.getEventSubExtra() != null) run += event.getEventSubExtraRuns();
							if (event.getEventHowOut() != null && !event.getEventHowOut().isEmpty()) {
								wicket++;
							}
							break;
					}
				}
			}
		}

		// Add last over stats if bowler was still active
		if (bowlerActive) {
			VariousStats vs = new VariousStats();
			vs.setOver(eventOver);
			vs.setTotalRuns(run);
			vs.setTotalWickets(wicket);
			bcard.add(vs);
		}
		return bcard;
	}

	public static String getPhaseBatter(List<VariousStats> player) {
	    List<VariousStats> batter = player.stream()
	            .filter(bs -> bs.getStatsType().equalsIgnoreCase(CricketUtil.BAT))
	            .sorted(Comparator.comparingInt(VariousStats::getTotalRuns).reversed())
	            .limit(2)
	            .collect(Collectors.toList());

	    StringBuilder stats = new StringBuilder();
	    
	    for (int i = 0; i < 2; i++) {
	        if (i < batter.size()) {
	            stats.append(batter.get(i).getId()).append("_").append(batter.get(i).getTotalRuns()+ batter.get(i).getOutnotOut())
	                 .append("_").append(batter.get(i).getTotalBalls());
	        } else {
	            stats.append(i + 1).append("_0_0");
	        }
	        
	        if (i < 1) {
	            stats.append(",");
	        }
	    }
	    
	    return stats.toString();
	}

	public static String getPhaseBowler(List<VariousStats> player) {
	    List<VariousStats> bowler = player.stream().filter(bs -> bs.getStatsType().equalsIgnoreCase(CricketUtil.BOWL)).filter(bs -> bs.getTotalWickets() >= 1)
	            .sorted(Comparator.comparingInt(VariousStats::getTotalWickets).reversed()).limit(2)
	            .collect(Collectors.toList());
	   
	    if (bowler.isEmpty()) {
	        return "None";  // No bowlers found
	    }
	    StringBuilder stats = new StringBuilder();
	    
	    for (int i = 0; i < 2; i++) {
	        if (i < bowler.size()) {
	            stats.append(bowler.get(i).getId()).append("_").append(bowler.get(i).getTotalWickets())
	            .append("_").append(bowler.get(i).getTotalRuns()).append("_").append(bowler.get(i).getTotalBalls());
	        } 
	        if (i < 1) {
	            stats.append(",");
	        }
	    }
	    
	    return stats.toString();
	}

	public static  String getpowerplay(Event event){
		int run=0, wicket=0, dot=0, four=0, six=0, nine=0,ball=0,out_batsman= 0,Notout_batsman= 0;
		switch (event.getEventType())
        {
        	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
        	case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.NINE:
                run += event.getEventRuns();
                ball++;
                switch(event.getEventType()) {
            	case CricketUtil.FOUR:
            		if(event.getEventWasABoundary() != null && 
            			event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
            			four ++;
            		}
            		break;
            	case CricketUtil.SIX:
            		if(event.getEventWasABoundary() != null && 
            			event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
            			six ++;
            		}
            		break;
            	case CricketUtil.NINE:
            		if(event.getEventWasABoundary() != null && 
            			event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
            			nine ++;
            		}
            		break;
            	case CricketUtil.DOT:
            		dot ++;
            		break;	
                }
                break;

        	case CricketUtil.WIDE: case CricketUtil.NO_BALL:case CricketUtil.PENALTY:
                run += event.getEventRuns();
                break;
        	 case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
                 run += event.getEventRuns();
                 ball++;
                 break;

        	case CricketUtil.LOG_WICKET:
                if (event.getEventRuns() > 0)
                {
                    run += event.getEventRuns();
                }else {
                	dot ++;
                }
                switch (event.getEventHowOut()) {
				case CricketUtil.RETIRED_HURT : case CricketUtil.ABSENT_HURT :case CricketUtil.CONCUSSED:
					Notout_batsman = event.getEventHowOutBatterNo();
					break;
				default:
					wicket ++ ;
	                out_batsman = event.getEventHowOutBatterNo();
					break;
				}
                ball++;
                break;

        	case CricketUtil.LOG_ANY_BALL:
                run += event.getEventRuns();
                if (event.getEventExtra() != null)
                {
                    run += event.getEventExtraRuns();
                }
                if (event.getEventSubExtra() != null)
                {
                    run += event.getEventSubExtraRuns();
                }
                if (event.getEventHowOut() != null && !event.getEventHowOut().isEmpty())
                {
                    wicket += 1;
                    out_batsman = event.getEventHowOutBatterNo();
                }
                if ( event.getEventType().equalsIgnoreCase(CricketUtil.FOUR) &&  event.getEventWasABoundary() != null
                        &&  event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
                	four ++;
                }

                if ( event.getEventType().equalsIgnoreCase(CricketUtil.SIX) &&  event.getEventWasABoundary() != null
                        &&  event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
                	six ++;
                }
                break;
        }
		return run+","+wicket+","+dot+","+four+","+six+","+nine+","+ball+","+out_batsman +","+Notout_batsman;
		
	}
	
	public static  List<OverByOverData>getWorm(List<OverByOverData> worm){
		for(int i=1;i<worm.size();i++) {
			int runs =worm.get(i-1).getOverTotalRuns()+worm.get(i).getOverTotalRuns();
			worm.get(i).setOverTotalRuns(runs);
		}
		return worm;
	}
	
	public static  List<PowerPlays> AllpowerplayScores(List<MatchAllData> match, MatchAllData currentMatch,List<PowerPlays> total_score,String Type) {
		List<PowerPlays> tournament_stats = new ArrayList<PowerPlays>();
		
		switch (Type) {
	        case "CURRENT":
	        	boolean p1=false,p2=false,p21=false,p22=false ;
	        	for (Inning inn : currentMatch.getMatch().getInning()) {
	        		 if (tournament_stats.isEmpty() || 
	        	        	    !tournament_stats.stream().anyMatch(obj -> obj.getTeam().getTeamId() == inn.getBattingTeamId())) {
	        	            tournament_stats.add(new PowerPlays(new Team(inn.getBattingTeamId(), inn.getBatting_team().getTeamName1(), inn.getBatting_team().getTeamName4()), new ArrayList<>(Arrays.asList(0, 0)),new ArrayList<>(Arrays.asList(0, 0))));
	        	           
	        	        }
        	        for (PowerPlays stats : tournament_stats) {
        	            if ((inn.getInningNumber() == 1 && inn.getBattingTeamId() == stats.getTeam().getTeamId()) ||
        	                    (inn.getInningNumber() == 2 && inn.getBattingTeamId() == stats.getTeam().getTeamId())) {
        	                String data = "";
        	                if (inn.getInningNumber() == 1 && !p1) {
        	                    data = getFirstPowerPlayScore(currentMatch, inn.getInningNumber(), currentMatch.getEventFile().getEvents());
        	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
        	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
        	                    p1 = true;
        	                }
        	                if (inn.getInningNumber() == 1 && !p2) {
        	                    data = getSecPowerPlayScore(currentMatch, inn.getInningNumber(), currentMatch.getEventFile().getEvents());
        	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
        	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
        	                    p2 = true;
        	                }
        	                if (inn.getInningNumber() == 2 && !p21) {
        	                    data = getFirstPowerPlayScore(currentMatch, inn.getInningNumber(), currentMatch.getEventFile().getEvents());
        	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
        	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
        	                    p21 = true;
        	                }
        	                if (inn.getInningNumber() == 2 && !p22) {
        	                    data = getSecPowerPlayScore(currentMatch, inn.getInningNumber(), currentMatch.getEventFile().getEvents());
        	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
        	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
        	                    p22 = true;
        	                }
        	            }
        	        }
        	    }
	            break;
	        case "PAST":
	        	
	        	for(MatchAllData tn :match) {
		    		boolean pp1=false,pp2=false,pp21=false,pp22=false ;
		    		if(!tn.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
		    			for (Inning inn : tn.getMatch().getInning()) {
		    				 if (tournament_stats.isEmpty() || 
			        	        	    !tournament_stats.stream().anyMatch(obj -> obj.getTeam().getTeamId() == inn.getBattingTeamId())) {
			        	            tournament_stats.add(new PowerPlays(new Team(inn.getBattingTeamId(), inn.getBatting_team().getTeamName1(), inn.getBatting_team().getTeamName4()), new ArrayList<>(Arrays.asList(0, 0)),new ArrayList<>(Arrays.asList(0, 0))));
			        	           
			        	        }
		    				 for (PowerPlays stats : tournament_stats) {
		         	            if ((inn.getInningNumber() == 1 && inn.getBattingTeamId() == stats.getTeam().getTeamId()) ||
		         	                    (inn.getInningNumber() == 2 && inn.getBattingTeamId() == stats.getTeam().getTeamId())) {
		         	                String data = "";
		         	                if (inn.getInningNumber() == 1 && !pp1) {
		         	                    data = getFirstPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
		         	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
		         	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
		         	                    pp1 = true;
		         	                }
		         	                if (inn.getInningNumber() == 1 && !pp2) {
		         	                    data = getSecPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
		         	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
		         	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
		         	                    pp2 = true;
		         	                }
		         	                if (inn.getInningNumber() == 2 && !pp21) {
		         	                    data = getFirstPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
		         	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
		         	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
		         	                    pp21 = true;
		         	                }
		         	                if (inn.getInningNumber() == 2 && !pp22) {
		         	                    data = getSecPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
		         	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
		         	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
		         	                    pp22 = true;
		         	                }
		         	            }
		         	        }
		        	    }
					}
	    		}
	            break;
	        case "PAST_CURRENT":
	        	
	        	for (MatchAllData tn : match) {
		    		boolean pp1=false,pp2=false,pp21=false,pp22=false ;
	        	    for (Inning inn : tn.getMatch().getInning()) {
	        	    	if (tournament_stats.isEmpty() || 
	        	        	    !tournament_stats.stream().anyMatch(obj -> obj.getTeam().getTeamId() == inn.getBattingTeamId())) {
	        	            tournament_stats.add(new PowerPlays(new Team(inn.getBattingTeamId(), inn.getBatting_team().getTeamName1(), inn.getBatting_team().getTeamName4()), new ArrayList<>(Arrays.asList(0, 0)),new ArrayList<>(Arrays.asList(0, 0))));
	        	           
	        	        }
	        	    	 for(PowerPlays stats : tournament_stats) {
	        	            if ((inn.getInningNumber() == 1 && inn.getBattingTeamId() == stats.getTeam().getTeamId()) ||
	        	                    (inn.getInningNumber() == 2 && inn.getBattingTeamId() == stats.getTeam().getTeamId())) {
	        	                String data = "";
	        	                if (inn.getInningNumber() == 1 && !pp1) {
	        	                    data = getFirstPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
	        	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
	        	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
	        	                    pp1 = true;
	        	                }
	        	                if (inn.getInningNumber() == 1 && !pp2) {
	        	                    data = getSecPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
	        	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
	        	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
	        	                    pp2 = true;
	        	                }
	        	                if (inn.getInningNumber() == 2 && !pp21) {
	        	                    data = getFirstPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
	        	                    stats.getTotal_runs().set(0,Integer.valueOf( stats.getTotal_runs().get(0) + Integer.valueOf(data.split("-")[0])));
	        	                    stats.getTotal_wickets().set(0,Integer.valueOf(stats.getTotal_wickets().get(0) + Integer.valueOf(data.split("-")[1].split(",")[0])));
	        	                    pp21 = true;
	        	                }
	        	                if (inn.getInningNumber() == 2 && !pp22) {
	        	                    data = getSecPowerPlayScore(tn, inn.getInningNumber(), tn.getEventFile().getEvents());
	        	                    stats.getTotal_runs().set(1,Integer.valueOf( stats.getTotal_runs().get(1) + Integer.valueOf(data.split("-")[0])));
	        	                    stats.getTotal_wickets().set(1,Integer.valueOf(stats.getTotal_wickets().get(1) + Integer.valueOf(data.split("-")[1].split(",")[0])));
	        	                    pp22 = true;
	        	                }
	        	            }
	        	        }
	        	    }
	        	}


	            break;
	    }
		
		return tournament_stats;
	}
	
	public static List<POTT> processAllPott(CricketService cricketService) {
		List<POTT> pott = cricketService.getPott();
		for(Player player : cricketService.getAllPlayer()) {
			for(POTT pt : pott) {
				if(pt.getPlayerId1() == player.getPlayerId()) {
					pt.setPlayer1(player);
					//pott.add(pt);
				}
				if(pt.getPlayerId2() == player.getPlayerId()) {
					pt.setPlayer2(player);
					//pott.add(pt);
				}
				if(pt.getPlayerId3() == player.getPlayerId()) {
					pt.setPlayer3(player);
					//pott.add(pt);
				}
				if(pt.getPlayerId4() == player.getPlayerId()) {
					pt.setPlayer4(player);
					//pott.add(pt);
				}
			}
		}
		return pott;
	}
	public static Player getPlayerFromMatchData(int plyr_id, MatchAllData match)
	{
		for(Player plyr : match.getSetup().getHomeSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		for(Player plyr : match.getSetup().getAwaySquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		for(Player plyr : match.getSetup().getHomeOtherSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		for(Player plyr : match.getSetup().getAwayOtherSquad()) {
			if(plyr!=null)
				if(plyr_id == plyr.getPlayerId()) { 
					return plyr;
				}
		}
		return null;
	}
	
	public static String getnumeric(int numeric_data)
	{
		String value = "";
		switch (numeric_data) {
		case 1:
			value = "first";
			break;
		case 2:
			value = "second";
			break;
		case 3:
			value = "third";
			break;
		case 4:
			value = "fourth";
			break;
		case 5:
			value = "fifth";
			break;	
		}
		return value;
	}

			
	public static List<Player> getPlayerFromMatchData(List<Player>player, MatchAllData match)
	{
		for(Player plyer : player) {
			for(Player plyr : match.getSetup().getHomeSquad()) {
				if(plyer.getPlayerId() == plyr.getPlayerId()) { 
					plyer.setTicker_name(plyr.getTicker_name());
					plyer.setFull_name(plyr.getFull_name());
					plyer.setFirstname(plyr.getFirstname());
					plyer.setSurname(plyr.getSurname());
				}
			}
			for(Player plyr : match.getSetup().getAwaySquad()) {
				if(plyer.getPlayerId()  == plyr.getPlayerId()) { 
					plyer.setTicker_name(plyr.getTicker_name());
					plyer.setFull_name(plyr.getFull_name());
					plyer.setFirstname(plyr.getFirstname());
					plyer.setSurname(plyr.getSurname());
				}
			}
			for(Player plyr : match.getSetup().getHomeOtherSquad()) {
				if(plyer.getPlayerId() == plyr.getPlayerId()) { 
					plyer.setTicker_name(plyr.getTicker_name());
					plyer.setFull_name(plyr.getFull_name());
					plyer.setFirstname(plyr.getFirstname());
					plyer.setSurname(plyr.getSurname());
				}
			}
			for(Player plyr : match.getSetup().getAwayOtherSquad()) {
				if(plyer.getPlayerId()  == plyr.getPlayerId()) { 
					plyer.setTicker_name(plyr.getTicker_name());
					plyer.setFull_name(plyr.getFull_name());
					plyer.setFirstname(plyr.getFirstname());
					plyer.setSurname(plyr.getSurname());
				}
			}
		}
		return player;	
	
	}
	public static String ConvertToOvers(int balls, MatchAllData match){
		int ball_per_over=Integer.valueOf(match.getSetup().getBallsPerOver());
			if((balls % ball_per_over)==0) {
				return String.valueOf((balls / ball_per_over));
			}else {
				return String.valueOf((balls % ball_per_over)+"."+Integer.valueOf((balls / ball_per_over)));
			}
	}
	public static String BetterOverRate(int Overs, int OddBalls, double Mins, String RateX, boolean Valid) {
		double ti = 0,r = 0;
		int O = 0 ,b = 0;
		int Bls = 6 * Overs + OddBalls;
		if(Mins < 1){
			RateX = "";
		    Valid = false;
		}else{
			ti = Mins / 60;
		    r = (Bls / 6) / ti;
		    O = (int)Math.floor(r);
		    r = r - O;
		    b = (int)Math.floor(6 * r);
		    RateX = String.valueOf(O) + "." + String.valueOf(b);
		    Valid = true;
		}
		return RateX;
	}
	
	public static String calculateOverRate(int overs, int balls, double durationInSeconds) {
	    if (durationInSeconds <= 0) {
	        return "-";
	    }

	    // Step 1: Convert overs + balls → total overs in decimal
	    double totalOvers = overs + (balls / 6.0);

	    // Step 2: Convert seconds → hours
	    double hours = durationInSeconds / 3600.0;

	    // Step 3: Calculate over rate
	    double overRate = totalOvers / hours;

	    // Step 4: Format to 2 decimal places
	    return String.format("%.2f", overRate);
	}



	public static AE_Six_Distance getDistance_of_ball_from_ThirdParty(String FilePathName) throws JAXBException {
		AE_Six_Distance cricket_data =(AE_Six_Distance)JAXBContext.newInstance(AE_Six_Distance.class)
		.createUnmarshaller().unmarshal(new File(FilePathName));
		
	return cricket_data;
	}
	
	public static String getPhaseWiseScore(MatchAllData match, int inn_num, ArrayList<Event> events) 
	{
		int oneToSixRuns = 0, oneToSixWkts = 0, sevenToFifteenRuns = 0, sevenToFifteenWkts = 0, sixteenToTwentyRuns = 0, sixteenToTwentyWkts = 0;
		// Track batsmen and bowlers performance
        Map<Integer, String> batsmanRunsPhase1 = new HashMap<>(), batsmanRunsPhase2 = new HashMap<>(), batsmanRunsPhase3 = new HashMap<>();
        Map<Integer, Integer> batsmanBallsPhase1 = new HashMap<>(), batsmanBallsPhase2 = new HashMap<>(), batsmanBallsPhase3 = new HashMap<>();
        Map<Integer, Integer> bowlerWicketsPhase1 = new HashMap<>(), bowlerWicketsPhase2 = new HashMap<>(), bowlerWicketsPhase3 = new HashMap<>();
        Map<Integer, Integer> bowlerBallsPhase1 = new HashMap<>(), bowlerBallsPhase2 = new HashMap<>(), bowlerBallsPhase3 = new HashMap<>();
        Map<Integer, Integer> bowlerRunsConcededPhase1 = new HashMap<>(), bowlerRunsConcededPhase2 = new HashMap<>(), bowlerRunsConcededPhase3 = new HashMap<>();
        boolean isVisited = false;
		
		if ((events != null) && (events.size() > 0)) {
			  for (int i = 0; i <=events.size()-1; i++) {
				  if(events.get(i).getEventInningNumber() == inn_num) {
					  
					  float overBalls = Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo()));
					  isVisited = false;
					  
					  switch (events.get(i).getEventType().toUpperCase()) {
					  case CricketUtil.NEW_BATSMAN:
						  updateMap(batsmanRunsPhase1, events.get(i).getEventBatterNo(), -1,"");
						  updateMap(batsmanRunsPhase2, events.get(i).getEventBatterNo(), -1 ,"");
						  updateMap(batsmanRunsPhase3, events.get(i).getEventBatterNo(), -1 ,"");
						  updateMap(batsmanBallsPhase1, events.get(i).getEventBatterNo(), -1);
						  updateMap(batsmanBallsPhase2, events.get(i).getEventBatterNo(), -1);
						  updateMap(batsmanBallsPhase3, events.get(i).getEventBatterNo(), -1);
						  break;
					    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT: case CricketUtil.FOUR: 
					    case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
					    case CricketUtil.NINE: case CricketUtil.LOG_WICKET:
					    	
					    	if(overBalls == 0.0 
					    		&& (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE) 
					    		|| events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL))) {
					    		
					    		isVisited = true;
					    		oneToSixRuns = oneToSixRuns + events.get(i).getEventRuns()+events.get(i).getEventExtraRuns()+events.get(i).getEventSubExtraRuns();
					    		oneToSixWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase1, batsmanBallsPhase1, bowlerRunsConcededPhase1
					    				, bowlerBallsPhase1, bowlerWicketsPhase1, oneToSixWkts);
					    		
					    	}else if(overBalls == 6.0 
					    			&& (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE) 
					    			|| events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL))) {
					    		
					    		isVisited = true;
					    		sevenToFifteenRuns = sevenToFifteenRuns + events.get(i).getEventRuns()+events.get(i).getEventExtraRuns()+events.get(i).getEventSubExtraRuns();
					    		sevenToFifteenWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase2, batsmanBallsPhase2, bowlerRunsConcededPhase2
					    				, bowlerBallsPhase2, bowlerWicketsPhase2, sevenToFifteenWkts);
					    		
					    	}else if(overBalls == 15.0 
					    			&& (events.get(i).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.WIDE) 
					    			|| events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER) || events.get(i).getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL))) {
					    		isVisited = true;
					    		sixteenToTwentyRuns = sixteenToTwentyRuns + events.get(i).getEventRuns()+events.get(i).getEventExtraRuns()+events.get(i).getEventSubExtraRuns();
					    		sixteenToTwentyWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase3, batsmanBallsPhase3, bowlerRunsConcededPhase3
					    				, bowlerBallsPhase3, bowlerWicketsPhase3, sixteenToTwentyWkts);
					    	}
					    	
					    	
					    	if(overBalls <= 6.0 && !isVisited) {
					    		oneToSixRuns = oneToSixRuns + events.get(i).getEventRuns();
					    		oneToSixWkts = handlePhaseByMainEvents(events.get(i), overBalls, batsmanRunsPhase1, batsmanBallsPhase1, bowlerRunsConcededPhase1
					    				, bowlerBallsPhase1, bowlerWicketsPhase1, oneToSixWkts);
					    		
					    	}else if(overBalls > 6.0 && overBalls <= 15.0 && !isVisited) {
					    		
					    		sevenToFifteenRuns = sevenToFifteenRuns + events.get(i).getEventRuns();
					    		sevenToFifteenWkts = handlePhaseByMainEvents(events.get(i), overBalls, batsmanRunsPhase2, batsmanBallsPhase2, bowlerRunsConcededPhase2
					    				, bowlerBallsPhase2, bowlerWicketsPhase2, sevenToFifteenWkts);
					    		
					    	}else if(overBalls >15.0 && overBalls <= 20 && !isVisited) {
					    		sixteenToTwentyRuns = sixteenToTwentyRuns + events.get(i).getEventRuns();
					    		sixteenToTwentyWkts = handlePhaseByMainEvents(events.get(i), overBalls, batsmanRunsPhase3, batsmanBallsPhase3, bowlerRunsConcededPhase3
					    				, bowlerBallsPhase3, bowlerWicketsPhase3, sixteenToTwentyWkts);
					    		
					    	}
			  		        break;
			  		        
						    case CricketUtil.LOG_ANY_BALL:
						    	if(Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo())) <= 6.0) {
						    		
						    		oneToSixRuns = oneToSixRuns + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
						    				events.get(i).getEventSubExtraRuns();
						    		oneToSixWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase1, batsmanBallsPhase1, bowlerRunsConcededPhase1
						    				, bowlerBallsPhase1, bowlerWicketsPhase1, oneToSixWkts);
						    		
						    	}else if(Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo())) > 6.0 && 
						    			Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo())) <= 15.0) {
						    		
						    		sevenToFifteenRuns = sevenToFifteenRuns + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
						    				events.get(i).getEventSubExtraRuns();
						    		sevenToFifteenWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase2, batsmanBallsPhase2, bowlerRunsConcededPhase2
						    				, bowlerBallsPhase2, bowlerWicketsPhase2, sevenToFifteenWkts);
						    		
						    	}else if(Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo())) >15.0 && 
						    			Float.valueOf(CricketFunctions.OverBalls(events.get(i).getEventOverNo(), events.get(i).getEventBallNo())) <= 20) {
						    		
						    		sixteenToTwentyRuns = sixteenToTwentyRuns + events.get(i).getEventRuns() + events.get(i).getEventExtraRuns() + 
						    				events.get(i).getEventSubExtraRuns();
						    		sixteenToTwentyWkts = handlePhaseByExtras(events.get(i), overBalls, batsmanRunsPhase3, batsmanBallsPhase3, bowlerRunsConcededPhase3
						    				, bowlerBallsPhase3, bowlerWicketsPhase3, sixteenToTwentyWkts);
						    		
						    	}
								break;
					    }
				  }  
			  }
		}
		
		
		String topBatsmanPhase1 = getTopTwoPerformers(batsmanRunsPhase1,batsmanBallsPhase1);
	    String topBatsmanPhase2 = getTopTwoPerformers(batsmanRunsPhase2,batsmanBallsPhase2);
	    String topBatsmanPhase3 = getTopTwoPerformers(batsmanRunsPhase3,batsmanBallsPhase3);
	    String topBowlerPhase1 = getTopTwoBowlers(bowlerWicketsPhase1, bowlerRunsConcededPhase1,bowlerBallsPhase1);
	    String topBowlerPhase2 = getTopTwoBowlers(bowlerWicketsPhase2, bowlerRunsConcededPhase2,bowlerBallsPhase2);
	    String topBowlerPhase3 = getTopTwoBowlers(bowlerWicketsPhase3, bowlerRunsConcededPhase3,bowlerBallsPhase3);
		return oneToSixRuns + "," + oneToSixWkts + "_" + sevenToFifteenRuns + "," + sevenToFifteenWkts + "_" + sixteenToTwentyRuns + "," + sixteenToTwentyWkts 
				+ "|" + topBatsmanPhase1 + "." + topBatsmanPhase2 + "." + topBatsmanPhase3 
				+ "|" +topBowlerPhase1 + "." + topBowlerPhase2 + "." + topBowlerPhase3;
	}
	
	//USING IN PHASE WISE
	private static int handlePhaseByMainEvents(Event event, float overBalls, Map<Integer, String> batsmanRuns, Map<Integer, Integer> batsmanBalls, Map<Integer,
			Integer> bowlerRunsConceded, Map<Integer, Integer> bowlerBallsBowled, Map<Integer, Integer> bowlerWickets, int phaseWickets) {
		
		if(event.getEventType().equalsIgnoreCase(CricketUtil.WIDE)) {
			updateMap(bowlerRunsConceded, event.getEventBowlerNo(),event.getEventRuns());
		}else if(event.getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			updateMap(batsmanBalls, event.getEventBatterNo(), 1);
			updateMap(bowlerRunsConceded, event.getEventBowlerNo(), event.getEventRuns());
		}else if(event.getEventType().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
			updateMap(batsmanBalls, event.getEventBatterNo(), 1);
			updateMap(bowlerBallsBowled, event.getEventBowlerNo(), 1);
		}else if(event.getEventType().equalsIgnoreCase(CricketUtil.BYE)) {
			updateMap(batsmanBalls, event.getEventBatterNo(), 1);
			updateMap(bowlerBallsBowled, event.getEventBowlerNo(), 1);
		}else if(!event.getEventType().equalsIgnoreCase(CricketUtil.PENALTY)) {
			updateMap(batsmanRuns, event.getEventBatterNo(), event.getEventRuns(),"");
			updateMap(batsmanBalls, event.getEventBatterNo(), 1);
			updateMap(bowlerRunsConceded, event.getEventBowlerNo(), event.getEventRuns());
			updateMap(bowlerBallsBowled, event.getEventBowlerNo(), 1);
			if(event.getEventType().equalsIgnoreCase(CricketUtil.LOG_WICKET)) {
				phaseWickets = phaseWickets + 1;
				if(event.getEventHowOut() != null && !event.getEventHowOut().isEmpty() 
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.TIMED_OUT)
						&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.HANDLED_THE_BALL)) {
							updateMap(bowlerWickets, event.getEventBowlerNo(), 1);
							updateMap(batsmanRuns, event.getEventHowOutBatterNo(),(event.getEventRuns()+ event.getEventExtraRuns()) ,"out");
				}
			}
		}
		return phaseWickets;
	}
	private static int handlePhaseByExtras(Event event, float overBalls, Map<Integer, String> batsmanRuns, Map<Integer, Integer> batsmanBalls, Map<Integer,
			Integer> bowlerRunsConceded, Map<Integer, Integer> bowlerBallsBowled, Map<Integer, Integer> bowlerWickets, int phaseWickets) {
		
		if(event.getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
			updateMap(batsmanBalls, event.getEventBatterNo(), 1);
			updateMap(bowlerRunsConceded, event.getEventBowlerNo(), event.getEventRuns());
		}else if(event.getEventType().equalsIgnoreCase(CricketUtil.WIDE)) {
			updateMap(bowlerRunsConceded, event.getEventBowlerNo(), event.getEventRuns());
		}else if(event.getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
			
			if(event.getEventExtra() != null && event.getEventSubExtra() != null &&
    				!event.getEventSubExtra().isEmpty()) {
    			if(event.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL) && 
    					(event.getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE) || 
    					event.getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE))) {
    				updateMap(batsmanBalls, event.getEventBatterNo(), 1);
    				updateMap(bowlerRunsConceded, event.getEventBowlerNo(),event.getEventRuns()+ event.getEventExtraRuns());
    			}else if(event.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL) && 
    					(!event.getEventSubExtra().equalsIgnoreCase(CricketUtil.BYE) || 
    					!event.getEventSubExtra().equalsIgnoreCase(CricketUtil.LEG_BYE))) {
    				updateMap(batsmanBalls, event.getEventBatterNo(), 1);
    				updateMap(bowlerRunsConceded, event.getEventBowlerNo(),event.getEventRuns()+ event.getEventExtraRuns() + event.getEventSubExtraRuns());
    			}else if(event.getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
    				updateMap(bowlerRunsConceded, event.getEventBowlerNo(),event.getEventRuns()+ event.getEventExtraRuns() + event.getEventSubExtraRuns());
    			}
    		}else if(event.getEventExtra() != null) {
    			if(event.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
    				updateMap(batsmanRuns, event.getEventBatterNo(), event.getEventRuns(),"");
    				updateMap(batsmanBalls, event.getEventBatterNo(), 1);
    				updateMap(bowlerRunsConceded, event.getEventBowlerNo(), event.getEventRuns()+event.getEventExtraRuns() + event.getEventSubExtraRuns());
    			}else if(event.getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
    				updateMap(bowlerRunsConceded, event.getEventBowlerNo(),event.getEventRuns()+ event.getEventExtraRuns() + event.getEventSubExtraRuns());
    			}
    		}
			if(event.getEventHowOut() != null && !event.getEventHowOut().isEmpty() 
				&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT)
				&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT)
				&& !event.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED)) {
					phaseWickets = phaseWickets + 1;
					if(!event.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
						updateMap(bowlerWickets, event.getEventBowlerNo(), 1);
						updateMap(batsmanRuns, event.getEventHowOutBatterNo(), (event.getEventRuns()+ event.getEventExtraRuns()),"out");
					}
			}
		}
		return phaseWickets;
	}
	
	//USING THIS METHOD IN PHASE WISE SCORE FUNCTION
	private static void updateMap(Map<Integer, Integer> map, int key, int value) {
		 if(map.get(key) != null && map.get(key) == -1 && value !=0) {
		 		map.put(key, map.getOrDefault(key, 0) + value+1);
		 }else {
		 		map.put(key, map.getOrDefault(key, 0) + value);
		 }
	 }
	 
	private static void updateMap(Map<Integer, String> map, int key, int value, String outNot) {
		    String currentValue = map.getOrDefault(key, "0");
		    int newValue = (currentValue.equals("-1") && value != 0) 
		                   ? Integer.parseInt(currentValue) + value + 1 
		                   : Integer.parseInt(currentValue) + value;
		    map.put(key, Integer.toString(newValue)+outNot);
		}

	 //USING IN PHASE WISE SCORE
	private static String getTopTwoPerformers(Map<Integer, String> map,Map<Integer, Integer> ballsMap) {
		    return map.entrySet()
		    		 .stream()
		             .sorted((a, b) -> Integer.compare(
		                  Integer.parseInt(b.getValue().replace("out", "").trim()), 
		                  Integer.parseInt(a.getValue().replace("out", "").trim())))  // Sort by runs in descending order
		              .limit(2)  // Limit to top two
		              .map(entry -> {
	                      int player = entry.getKey();
	                      String runs = entry.getValue().contains("out") ? entry.getValue().replace("out", "") : entry.getValue().replace("out", "") + "*";
	                      int balls = ballsMap.getOrDefault(player, 0);  // Default balls to 0 if not found
	                      return player + "_" + runs + "_" + balls;  // Include balls in the format
	                  })  // Format output
		              .reduce((a, b) -> a + "," + b)  // Join with ","
		              .orElse("None");
	}
	 
	//USING IN PHASE WISE SCORE
	private static String getTopTwoBowlers(Map<Integer, Integer> wicketsMap, Map<Integer, Integer> runsConcededMap,Map<Integer, Integer> ballsMap) {
		    return wicketsMap.entrySet()
		                     .stream()
		                     .sorted((a, b) -> {
		                         int wicketComparison = b.getValue().compareTo(a.getValue());  // Descending wickets
		                         if (wicketComparison == 0) {
		                             // Compare by runs conceded in ascending order
		                             return Integer.compare(runsConcededMap.getOrDefault(a.getKey(), Integer.MAX_VALUE),
		                                                    runsConcededMap.getOrDefault(b.getKey(), Integer.MAX_VALUE));
		                         }
		                         return wicketComparison;
		                     })
		                     .limit(2)  // Limit to top two
		                     .map(entry -> {
		                         int bowler = entry.getKey();
		                         int wickets = entry.getValue();
		                         int runsConceded = runsConcededMap.getOrDefault(bowler, 0);
		                         int balls = ballsMap.getOrDefault(bowler, 0);
		                         return bowler + "_" + wickets + "_" + runsConceded + "_" + balls; // Include balls in output
		                     })  // Format output
		                     .reduce((a, b) -> a + "," + b)  // Join with "|"
		                     .orElse("None");
	}
	 

   public static List<VariousStats> BowlerVsBatsman(int bowlerNum, int innNum, List<Event> events, MatchAllData matchAllData) {
		    List<VariousStats> playerStatsList = new ArrayList<>();	    
		    Event previousEvent = null;

		    if (events != null && !events.isEmpty()) {
		        for (Event event : events) {	            
		            if (innNum == event.getEventInningNumber() && bowlerNum == event.getEventBowlerNo() && event.getEventBatterNo() != 0) {
		                VariousStats batterStats = null;
		                for (VariousStats varStat : playerStatsList) {
		                    if (varStat.getId() == event.getEventBatterNo() && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BAT)) {
		                        batterStats = varStat;
		                    }
		                }
		                if (batterStats == null) {
		                    batterStats = new VariousStats(event.getEventBatterNo(), CricketUtil.BAT);
		                    playerStatsList.add(batterStats);
		                }
		                batterStats.setTotalRuns(batterStats.getTotalRuns() + 
		                		event.getEventRuns() + event.getEventSubExtraRuns() + event.getEventExtraRuns());
		                if (previousEvent != null && event.getEventBallNo() != previousEvent.getEventBallNo()) {
		                    batterStats.setTotalBalls(batterStats.getTotalBalls() + 1);
		                }
		                switch (event.getEventType()) {
		                    case CricketUtil.ONE:
		                        batterStats.setTotalOnes(batterStats.getTotalOnes() + 1);
		                        break;
		                    case CricketUtil.TWO:
		                        batterStats.setTotalTwos(batterStats.getTotalTwos() + 1);
		                        break;
		                    case CricketUtil.THREE:
		                        batterStats.setTotalThrees(batterStats.getTotalThrees() + 1);
		                        break;
		                    case CricketUtil.FIVE:
		                        batterStats.setTotalFives(batterStats.getTotalFives() + 1);
		                        break;
		                    case CricketUtil.DOT:
		                        batterStats.setTotalDots(batterStats.getTotalDots() + 1);
		                        break;
		                    case CricketUtil.FOUR:
		                        batterStats.setTotalFours(batterStats.getTotalFours() + 1);
		                        break;
		                    case CricketUtil.SIX:
		                        batterStats.setTotalSixes(batterStats.getTotalSixes() + 1);
		                        break;
		                    case CricketUtil.NINE:
		                        batterStats.setTotalNines(batterStats.getTotalNines() + 1);
		                        break;
		                    case CricketUtil.LOG_ANY_BALL:
		                        if (event.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		                            if (event.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
		                                batterStats.setTotalDots(batterStats.getTotalDots() + 1);
		                            }
		                            if (event.getEventWasABoundary() != null && event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
		                                if (event.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) {
		                                    batterStats.setTotalFours(batterStats.getTotalFours() + 1);
		                                } else if (event.getEventRuns() == Integer.valueOf(CricketUtil.SIX)) {
		                                    batterStats.setTotalSixes(batterStats.getTotalSixes() + 1);
		                                } else if (event.getEventRuns() == Integer.valueOf(CricketUtil.NINE)) {
		                                    batterStats.setTotalNines(batterStats.getTotalNines() + 1);
		                                }
		                            }
		                        }
		                        break;
		                    case CricketUtil.LOG_WICKET:
		                        switch (String.valueOf(event.getEventRuns())) {
		                            case CricketUtil.ONE:
		                                batterStats.setTotalOnes(batterStats.getTotalOnes() + 1);
		                                break;
		                            case CricketUtil.TWO:
		                                batterStats.setTotalTwos(batterStats.getTotalTwos() + 1);
		                                break;
		                            case CricketUtil.THREE:
		                                batterStats.setTotalThrees(batterStats.getTotalThrees() + 1);
		                                break;
		                            case CricketUtil.FIVE:
		                                batterStats.setTotalFives(batterStats.getTotalFives() + 1);
		                                break;
		                            case CricketUtil.DOT:
		                                batterStats.setTotalDots(batterStats.getTotalDots() + 1);
		                                break;
		                        }
		                        break;
		                }
		            }
		            previousEvent = event;
		        }
		    }

		    // Set player names
		    for (VariousStats vs : playerStatsList) {
		        vs.setName(getPlayerFromMatchData(vs.getId(), matchAllData).getTicker_name());
		        vs.setBowlerName(getPlayerFromMatchData(bowlerNum, matchAllData).getTicker_name());
		    }

		    return playerStatsList;
		} 
	 
   public static List<VariousStats> BatsmanVsBowler(int batsmanNum, int innNum, List<Event> events, MatchAllData matchAllData) {
	    List<VariousStats> playerStatsList = new ArrayList<>();    
	    Event previousEvent = null;

	    if (events != null && !events.isEmpty()) {
	        for (Event event : events) {            
	            if (innNum == event.getEventInningNumber() && batsmanNum == event.getEventBatterNo() && event.getEventBowlerNo() != 0) {

	                VariousStats bowlerStats = null;
	                for (VariousStats varStat : playerStatsList) {
	                    if (varStat.getId() == event.getEventBowlerNo() && varStat.getStatsType().equalsIgnoreCase(CricketUtil.BOWL)) {
	                        bowlerStats = varStat;
	                    }
	                }

	                if (bowlerStats == null) {
	                    bowlerStats = new VariousStats(event.getEventBowlerNo(), CricketUtil.BOWL);
	                    playerStatsList.add(bowlerStats);
	                }
	                bowlerStats.setTotalRuns(bowlerStats.getTotalRuns() + 
	                    event.getEventRuns() + event.getEventSubExtraRuns() + event.getEventExtraRuns());

	                if (previousEvent != null && event.getEventBallNo() != previousEvent.getEventBallNo()) {
	                    bowlerStats.setTotalBalls(bowlerStats.getTotalBalls() + 1);
	                }
	                switch (event.getEventType()) {
	                    case CricketUtil.ONE:
	                        bowlerStats.setTotalOnes(bowlerStats.getTotalOnes() + 1);
	                        break;
	                    case CricketUtil.TWO:
	                        bowlerStats.setTotalTwos(bowlerStats.getTotalTwos() + 1);
	                        break;
	                    case CricketUtil.THREE:
	                        bowlerStats.setTotalThrees(bowlerStats.getTotalThrees() + 1);
	                        break;
	                    case CricketUtil.FIVE:
	                        bowlerStats.setTotalFives(bowlerStats.getTotalFives() + 1);
	                        break;
	                    case CricketUtil.DOT:
	                        bowlerStats.setTotalDots(bowlerStats.getTotalDots() + 1);
	                        break;
	                    case CricketUtil.FOUR:
	                        bowlerStats.setTotalFours(bowlerStats.getTotalFours() + 1);
	                        break;
	                    case CricketUtil.SIX:
	                        bowlerStats.setTotalSixes(bowlerStats.getTotalSixes() + 1);
	                        break;
	                    case CricketUtil.NINE:
	                        bowlerStats.setTotalNines(bowlerStats.getTotalNines() + 1);
	                        break;
	                    case CricketUtil.LOG_ANY_BALL:
	                        if (event.getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                            if (event.getEventHowOut().equalsIgnoreCase(CricketUtil.RUN_OUT)) {
	                                bowlerStats.setTotalDots(bowlerStats.getTotalDots() + 1);
	                            }
	                            if (event.getEventWasABoundary() != null && event.getEventWasABoundary().equalsIgnoreCase(CricketUtil.YES)) {
	                                if (event.getEventRuns() == Integer.valueOf(CricketUtil.FOUR)) {
	                                    bowlerStats.setTotalFours(bowlerStats.getTotalFours() + 1);
	                                } else if (event.getEventRuns() == Integer.valueOf(CricketUtil.SIX)) {
	                                    bowlerStats.setTotalSixes(bowlerStats.getTotalSixes() + 1);
	                                } else if (event.getEventRuns() == Integer.valueOf(CricketUtil.NINE)) {
	                                    bowlerStats.setTotalNines(bowlerStats.getTotalNines() + 1);
	                                }
	                            }
	                        }
	                        break;
	                    case CricketUtil.LOG_WICKET:
	                        switch (String.valueOf(event.getEventRuns())) {
	                            case CricketUtil.ONE:
	                                bowlerStats.setTotalOnes(bowlerStats.getTotalOnes() + 1);
	                                break;
	                            case CricketUtil.TWO:
	                                bowlerStats.setTotalTwos(bowlerStats.getTotalTwos() + 1);
	                                break;
	                            case CricketUtil.THREE:
	                                bowlerStats.setTotalThrees(bowlerStats.getTotalThrees() + 1);
	                                break;
	                            case CricketUtil.FIVE:
	                                bowlerStats.setTotalFives(bowlerStats.getTotalFives() + 1);
	                                break;
	                            case CricketUtil.DOT:
	                                bowlerStats.setTotalDots(bowlerStats.getTotalDots() + 1);
	                                break;
	                        }
	                        break;
	                }
	            }
	            previousEvent = event;
	        }
	    }

	    // Set player names and bowler's name
	    for (VariousStats vs : playerStatsList) {
	        vs.setName(getPlayerFromMatchData(vs.getId(), matchAllData).getTicker_name());
	        vs.setBowlerName(getPlayerFromMatchData(batsmanNum, matchAllData).getTicker_name());
	    }

	    return playerStatsList;
	}
//   public static Map<Integer, List<String>> PowerPlayTeamThisSeries(MatchAllData currentMatch, List<MatchAllData> match) {
//	    Map<Integer, List<String>> thisSeries = new HashMap<>();
//	    
//	    // Initialize map for both teams
//	    thisSeries.put(currentMatch.getSetup().getHomeTeamId(), new ArrayList<>());
//	    thisSeries.put(currentMatch.getSetup().getAwayTeamId(), new ArrayList<>());
//
//	    for (MatchAllData mtch : match) {
//
//	        if (!mtch.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName()) &&
//	        		(mtch.getSetup().getHomeTeamId() == currentMatch.getSetup().getHomeTeamId() || mtch.getSetup().getHomeTeamId() == currentMatch.getSetup().getAwayTeamId()||
//	        		mtch.getSetup().getAwayTeamId() == currentMatch.getSetup().getHomeTeamId() || 
//	        		mtch.getSetup().getAwayTeamId() == currentMatch.getSetup().getAwayTeamId())) {
//	            // Inning 1
//	            if (mtch.getMatch().getInning().get(0).getBattingTeamId() == currentMatch.getSetup().getHomeTeamId() || mtch.getMatch().getInning().get(0).getBattingTeamId() == currentMatch.getSetup().getAwayTeamId()) {
//	            	if(mtch.getMatch().getInning().get(0).getTotalOvers()==0) {
//		                thisSeries.get(mtch.getMatch().getInning().get(0).getBattingTeamId()).add("-,"+ mtch.getMatch().getInning().get(0).getFirstPowerplayEndOver()
//		                		+", v " + mtch.getMatch().getInning().get(0).getBowlingTeamId());	
//	            	}else {
//	            		String data = getFirstPowerPlayScore(mtch, 1, mtch.getEventFile().getEvents());
//		                thisSeries.get(mtch.getMatch().getInning().get(0).getBattingTeamId()).add(data.split(",")[0] +","+ mtch.getMatch().getInning().get(0).getFirstPowerplayEndOver()
//		                		+", v " + mtch.getMatch().getInning().get(0).getBowlingTeamId());	
//	            	}
//	            }
//	            // Inning 2
//	            if (mtch.getMatch().getInning().get(1).getBattingTeamId() == currentMatch.getSetup().getHomeTeamId() || mtch.getMatch().getInning().get(1).getBattingTeamId() == currentMatch.getSetup().getAwayTeamId()) {
//	            	if(mtch.getMatch().getInning().get(0).getTotalOvers()==0) {
//	 	                thisSeries.get(mtch.getMatch().getInning().get(1).getBattingTeamId()).add("-,"+ mtch.getMatch().getInning().get(1).getFirstPowerplayEndOver()
//	 	                		+ ", v " + mtch.getMatch().getInning().get(1).getBowlingTeamId());
//	            	}else {
//	            		 String data = getFirstPowerPlayScore(mtch, 2, mtch.getEventFile().getEvents());
//	 	                thisSeries.get(mtch.getMatch().getInning().get(1).getBattingTeamId()).add(data.split(",")[0] +","+ mtch.getMatch().getInning().get(1).getFirstPowerplayEndOver()
//	 	                		+ ", v " + mtch.getMatch().getInning().get(1).getBowlingTeamId());	
//	            	}
//	            }
//	        }
//	    }
//	    return thisSeries;
//	}

   public static List<String> BowlerVsBatsmanLHB_RHB(int Bolwer_num, int BowlerTeam, String Type, MatchAllData match) {
	    int run = 0, ball = 0, wicket = 0;
	    int run1 = 0, ball1 = 0, wicket1 = 0;
	    Event previousEvent = null;
	    List<String> results = new ArrayList<>();	    
	    for (Event evn : match.getEventFile().getEvents()) {
	        if (evn.getEventBowlerNo() == Bolwer_num) {
	            for (BattingCard ply : match.getMatch().getInning().get(evn.getEventInningNumber()-1).getBattingCard()) {
	                if (ply.getPlayerId() == evn.getEventBatterNo()) {
	                    // for RHS players
	                    if (ply.getPlayer().getBattingStyle().equalsIgnoreCase("LHB")) {
	                    	if (evn.getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.WIDE)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.BYE)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
                            } else {
    	                        run += evn.getEventRuns() + evn.getEventSubExtraRuns() + evn.getEventExtraRuns();
    	                        if (previousEvent != null && evn.getEventBallNo() != previousEvent.getEventBallNo()) {
 		    	                   ball++;
 		    	             }
                            }
	                        if (evn.getEventType().equalsIgnoreCase(CricketUtil.LOG_WICKET) || evn.getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
	                            if (evn.getEventHowOut() != null && !evn.getEventHowOut().isEmpty() &&
	                               (!evn.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_WICKET) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.OBSTRUCTING_FIELDER) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.HANDLED_THE_BALL) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.TIMED_OUT) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_OUT) &&
	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
	                                wicket++;
	                            }
	                        }
	                    }
	                    // for LHS players
	                    else if (ply.getPlayer().getBattingStyle().equalsIgnoreCase("RHB")) {
	                    	if (evn.getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.WIDE)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.BYE)
	                                || evn.getEventType().equalsIgnoreCase(CricketUtil.LEG_BYE)) {
                            } else {
    	                        run1 += evn.getEventRuns() + evn.getEventSubExtraRuns() + evn.getEventExtraRuns();
    	                        if (previousEvent != null && evn.getEventBallNo() != previousEvent.getEventBallNo()) {
 		    	                   ball1++;
 		    	             }
                            }
	                        if (evn.getEventType().equalsIgnoreCase(CricketUtil.LOG_WICKET) || evn.getEventType().equalsIgnoreCase(CricketUtil.LOG_ANY_BALL)) {
	                            if (evn.getEventHowOut() != null && !evn.getEventHowOut().isEmpty() &&
	                            	(!evn.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.HIT_WICKET) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.OBSTRUCTING_FIELDER) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.HANDLED_THE_BALL) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.TIMED_OUT) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_OUT) &&
    	                                !evn.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED))) {
	                                wicket1++;
	                            }
	                        }
	                    }
	                    break;
	                }
	            }
	            previousEvent = evn;
	        }
	    }
	    results.add(ball +","+ run +","+ wicket);
	    results.add(ball1 +","+ run1 +","+ wicket1);
	    
	    return results;
	}	
   public static List<BestStats> getBatterIndividual(MatchAllData session_match, List<HeadToHeadPlayer> headToHead, 
			CricketService cricketService, List<Tournament> past_tournament_stats) throws JsonMappingException, JsonProcessingException 
	{
	 
	 List<BestStats> top_ten_beststats = new ArrayList<BestStats>();
      top_ten_beststats.removeAll(top_ten_beststats);
      for(Tournament tourn : CricketFunctions.extractTournamentData("CURRENT_MATCH_DATA", false, headToHead, cricketService, 
              session_match, past_tournament_stats)) {
      	
          for(BestStats bs : tourn.getBatsman_best_Stats()) {
          	BestStats processedBs = CricketFunctions.getProcessedBatsmanBestStats(bs);
              top_ten_beststats.add(processedBs);
          }
//          System.out.println("Batsman stats: " + tourn.getBatsman_best_Stats());
      }
      
      Collections.sort(top_ten_beststats, new CricketFunctions.BatsmanBestStatsComparator());
      
//      System.out.println("-----------------------------------------------");
//      for (BestStats bs : top_ten_beststats) {
//          System.out.println("bsID = " + bs.getPlayerId() + "   runs = " + bs.getRuns());
//      }
//      System.out.println("-----------------------------------------------");
	 
	 return top_ten_beststats;
	}
   public static String LastFewOvers(int over, Match match, List<Event> events) {
    int TotalBalls = (over * 6);
    int TotalRuns = 0 ,TotalFours = 0 ,TotalSixes = 0,TotalNines = 0,TotalWickets = 0;

    if (events != null && !events.isEmpty()) {
        for (int i = events.size() - 1; i >= 0; i--) {
        	if(TotalBalls > 0) {
        	  switch (events.get(i).getEventType()) {
                case CricketUtil.DOT: case CricketUtil.ONE:  case CricketUtil.TWO: case CricketUtil.THREE:
                case CricketUtil.FOUR: case CricketUtil.FIVE:case CricketUtil.SIX:case CricketUtil.NINE: 
                case CricketUtil.LOG_WICKET: case CricketUtil.BYE: case CricketUtil.LEG_BYE:
                   TotalBalls =TotalBalls -1;;
                   TotalRuns = TotalRuns + + events.get(i).getEventRuns();
                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
                        TotalWickets = TotalWickets + + 1;
                    }
                    if (CricketUtil.YES.equalsIgnoreCase(events.get(i).getEventWasABoundary())) {
                        if (CricketUtil.SIX.equalsIgnoreCase(events.get(i).getEventType())) TotalSixes = TotalSixes + 1;
                        if (CricketUtil.FOUR.equalsIgnoreCase(events.get(i).getEventType()))TotalFours = TotalFours + 1;
                        if (CricketUtil.NINE.equalsIgnoreCase(events.get(i).getEventType()))TotalNines = TotalNines  + 1;
                    }
                    break;

                case CricketUtil.NO_BALL: case CricketUtil.WIDE:case CricketUtil.PENALTY:
                   TotalRuns = TotalRuns + + events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns();
                    break;

                case CricketUtil.LOG_ANY_BALL:
                    if (!CricketUtil.NO.equalsIgnoreCase(events.get(i).getDoNotIncrementBall())) {
                       TotalBalls = TotalBalls -1;;
                    }
                   TotalRuns = TotalRuns + + events.get(i).getEventRuns() + events.get(i).getEventSubExtraRuns();
                    if (events.get(i).getEventHowOut() != null && !events.get(i).getEventHowOut().isEmpty()) {
                        TotalWickets = TotalWickets + + 1;
                    }
                    if (CricketUtil.YES.equalsIgnoreCase(events.get(i).getEventWasABoundary())) {
                        if (CricketUtil.SIX.equalsIgnoreCase(events.get(i).getEventType()))TotalSixes = TotalSixes + 1;
                        if (CricketUtil.FOUR.equalsIgnoreCase(events.get(i).getEventType()))TotalFours = TotalFours + 1;
                        if (CricketUtil.NINE.equalsIgnoreCase(events.get(i).getEventType()))TotalNines = TotalNines + 1;
                    }
                    break;
              }
        	}
          }
       }
      return TotalRuns +","+TotalWickets+","+TotalFours+","+TotalSixes+","+TotalNines;
  	}
   public static String AnalyzeSpeeds(List<Speed> speeds) {
	    double fastest = Double.MIN_VALUE , slowest = Double.MAX_VALUE ,totalSpeed = 0;
	    int count = 0;
	    for (Speed speed : speeds) {
	        double speedValue =Double.valueOf(speed.getSpeedValue().substring(1));
	        if (speedValue > fastest) {
	            fastest = speedValue;
	        }
	        if (speedValue < slowest) {
	            slowest = speedValue;
	        }
	        totalSpeed += speedValue;
	        count++;
	    }

	    double averageSpeed = totalSpeed / count;
	    return  fastest  + "," + String.format("%.1f", averageSpeed)+ "," + slowest;
	}
  
   public static List<Speed> getThisOverSpeeds(BowlingCard bowlingCard) {
	    return bowlingCard.getSpeeds().stream()
	            .filter(s ->(s.getOverNumber() == bowlingCard.getOvers()&& s.getBallNumber() >= 0))
	            .collect(Collectors.toList());
  }
   public static List<Double> ThisOverSpeed(BowlingCard bowlingCard){
	  return bowlingCard.getSpeeds().stream()
			    .filter(s -> s.getOverNumber() == bowlingCard.getOvers() || 
                  (s.getOverNumber() == bowlingCard.getOvers() && s.getBallNumber() >= 0))
			    .map(s -> Double.parseDouble(s.getSpeedValue()))
			    .collect(Collectors.toList());
  }
	
  public static List<Speed> getThisOverSpeeds(BowlingCard bowlingCard,String speed ,String this_over) throws Exception {
		   List<Speed> thisOverSpeeds = new ObjectMapper().readValue(
				    new ObjectMapper().writeValueAsString(
				        bowlingCard.getSpeeds().stream()
				            .filter(s -> s.getOverNumber() == bowlingCard.getOvers() && s.getBallNumber() >= 0)
				            .collect(Collectors.toList())
				    ),new ObjectMapper().getTypeFactory().constructCollectionType(List.class, Speed.class));
		   	   
		   String[] balls = this_over.split(",");

		   for (int i = 0; i < balls.length; i++) {
		       String str = balls[i];

		       if (str.matches(CricketUtil.WIDE + "|" + CricketUtil.NO_BALL)) {
		           Speed extraBall = new Speed();
		       		extraBall.setOverNumber(thisOverSpeeds.get(i).getOverNumber());
		       		extraBall.setBallNumber(thisOverSpeeds.get(i).getBallNumber());
		           	extraBall.setSpeedValue(str);
		           if (i <= thisOverSpeeds.size()) {
		               thisOverSpeeds.add(i, extraBall);
		           } else {
		               while (thisOverSpeeds.size() < i) {
		                   thisOverSpeeds.add(null);
		               }
		               thisOverSpeeds.add(extraBall);
		           }
		       }
		   }
	       
		    return thisOverSpeeds;
	 }
	
   public static List<Speed> getoverspeed(MatchAllData matchAllData)
   {
	   List<String> this_data_str = new ArrayList<String>();
	   Inning inning = matchAllData.getMatch().getInning().stream().filter(inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES))
				.findAny().orElse(null);
		
	   BowlingCard bowlingCard = inning.getBowlingCard().stream().filter(bc -> bc.getStatus().equalsIgnoreCase(CricketUtil.LAST + CricketUtil.BOWLER) ||
				bc.getStatus().equalsIgnoreCase(CricketUtil.CURRENT + CricketUtil.BOWLER)).findAny().orElse(null);
	   this_data_str = new ArrayList<String>();
		this_data_str.add(CricketFunctions.getEventsText(CricketUtil.OVER,bowlingCard.getPlayerId() ,
				",", matchAllData.getEventFile().getEvents(),0));
		
		ArrayList<String> this_speed_str = new ArrayList<String>(); 
		  
		for (int i = matchAllData.getEventFile().getEvents().size() - 1; i >= 0; i--)
		  {    
			  if(matchAllData.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER))
			  {
				  break;
			  }
			  if(matchAllData.getEventFile().getEvents().get(i).getEventInningNumber() == inning.getInningNumber() && 
					 matchAllData.getEventFile().getEvents().get(i).getEventBowlerNo() == bowlingCard.getPlayerId() ) {
			           for(int j = bowlingCard.getSpeeds().size()-1; j >=0; j--) {
			        	     this_speed_str.add(bowlingCard.getSpeeds().get(j).getSpeedValue()); 
                            
				
			 }               
			break;
			}
		  }
		 Collections.reverse(this_speed_str);
//		  System.out.println(this_data_str);
//		  System.out.println(this_speed_str); 
		
		return null;
	}
//   public static String getteamname(int teamId,CricketService cricketService) {
//	   String name = "";
//	    for(Team team: cricketService.getTeams()) {
//	    	if(team.getTeamId() == teamId) {
//	    		  name = team.getTeamName1();
//	    	}
//	    }
//		return name;
//	}
   public static TargetData GetTargetData(MatchAllData matchAllData) 
   {
	    TargetData targetData = new TargetData();
	   
	    if (matchAllData.getSetup().getTargetRuns() > 0) {
	    	targetData.setTargetRuns(matchAllData.getSetup().getTargetRuns());
	    }else {
	        if (matchAllData.getSetup().getMaxOvers() == 0) {
	        	targetData.setTargetRuns(GetTeamRunsAhead(3, matchAllData) + 1);
	        } else {
	        	targetData.setTargetRuns(GetTeamRunsAhead(1, matchAllData) + 1);
	        }
	    }
	    if (matchAllData.getSetup().getTargetOvers() != null && !matchAllData.getSetup().getTargetOvers().isEmpty()
	    	&& Integer.valueOf(matchAllData.getSetup().getTargetOvers().split("\\.")[0]) > 0) {
	    	targetData.setTargetOvers(matchAllData.getSetup().getTargetOvers());
	    } else {
	    	targetData.setTargetOvers(String.valueOf(matchAllData.getSetup().getMaxOvers()));
	    }
	    if (matchAllData.getSetup().getMaxOvers() > 0) {
	    	if(targetData.getTargetOvers().contains(".")) {
	    		targetData.setRemaningBall((Integer.valueOf(targetData.getTargetOvers().split("\\.")[0]) * 6 + 
	    			Integer.valueOf(targetData.getTargetOvers().split("\\.")[1])) - (matchAllData.getMatch().getInning().get(1).getTotalOvers() * 6)
			    		- matchAllData.getMatch().getInning().get(1).getTotalBalls());
	    	} else {
		    	targetData.setRemaningBall((Integer.valueOf(targetData.getTargetOvers()) * 6) - (matchAllData.getMatch().getInning().get(1).getTotalOvers() * 6)
		    		- matchAllData.getMatch().getInning().get(1).getTotalBalls());
	    	}
			if(matchAllData.getSetup().getSpecialMatchRules() != null && matchAllData.getSetup().getSpecialMatchRules().equalsIgnoreCase(CricketUtil.ISPL)
				&& matchAllData.getMatch().getInning().get(1).getSpecialRuns() != null && !matchAllData.getMatch().getInning().get(1).getSpecialRuns().trim().isEmpty()) 
			{
				if(matchAllData.getMatch().getInning().get(1).getSpecialRuns().startsWith("+")) {
			    	targetData.setRemaningRuns(targetData.getTargetRuns() - (matchAllData.getMatch().getInning().get(1).getTotalRuns() 
			    		+ Integer.parseInt(matchAllData.getMatch().getInning().get(1).getSpecialRuns().replace("+", ""))));
				}else if(matchAllData.getMatch().getInning().get(1).getSpecialRuns().startsWith("-")) {
			    	targetData.setRemaningRuns(targetData.getTargetRuns() - (matchAllData.getMatch().getInning().get(1).getTotalRuns() 
			    		- Integer.parseInt(matchAllData.getMatch().getInning().get(1).getSpecialRuns().replace("-", ""))));
				}
			} else {
		    	targetData.setRemaningRuns(targetData.getTargetRuns() - matchAllData.getMatch().getInning().get(1).getTotalRuns());
			}
	    } else {
	    	if (matchAllData.getSetup().getTargetOvers() != null && Integer.valueOf(matchAllData.getSetup().getTargetOvers()) > 0) {
		    	if(targetData.getTargetOvers().contains(".")) {
		    		targetData.setRemaningBall((Integer.valueOf(targetData.getTargetOvers().split("\\.")[0]) * 6 + 
		    			Integer.valueOf(targetData.getTargetOvers().split("\\.")[1])) - (matchAllData.getMatch().getInning().get(3).getTotalOvers() * 6)
				    		- matchAllData.getMatch().getInning().get(3).getTotalBalls());
		    	} else {
		    		targetData.setRemaningBall((Integer.valueOf(targetData.getTargetOvers()) * 6) - (matchAllData.getMatch().getInning().get(3).getTotalOvers() * 6)
			    		- matchAllData.getMatch().getInning().get(3).getTotalBalls());
		    	}
	    	} else {
	    		targetData.setRemaningBall(0);
	    	}
	    	targetData.setRemaningRuns(targetData.getTargetRuns() - matchAllData.getMatch().getInning().get(3).getTotalRuns());
	    }
	    if(targetData.getRemaningBall() < 0) {
	    	targetData.setRemaningBall(0);
	    } 
	    if(targetData.getRemaningRuns() < 0) {
	    	targetData.setRemaningRuns(0);
	    } 
	    return targetData;
	}

//   public static String CheckIfMatchIsFinishedAndGenerateResult(MatchAllData matchAllData, String teamNameType, String withBallsLeft) 
//   { 
//	  int runsRemaining = 0;
//	  Inning currentInning = matchAllData.getMatch().getInning().stream().filter(
//			  inn -> inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)).findFirst().orElse(null);
//	  
//	  String resultTxt = generateMatchResult(matchAllData, teamNameType);
//	  
//	   if (resultTxt.trim().isEmpty() && currentInning != null) {
//	    	
//		   if(matchAllData.getSetup().getMaxOvers() <= 0) {
//			  
//	          switch(currentInning.getInningNumber()){
//        	  case 3:
//        		  
//        		  int tm = matchAllData.getMatch().getInning().get(2).getBowlingTeamId();
//        		  
//        		  runsRemaining = GetTeamRunsAhead(currentInning.getInningNumber(), matchAllData);
//        		  
//        		  if(runsRemaining >=0) {
//        		  }else {
//        				int WksLeft = 10 - matchAllData.getMatch().getInning().get(ing-1).getTotalWickets();
//        				if(WksLeft <= 0) {
//        					resultTxt = getteamname(tm,cricketService) + " win by an innings and " + RunsAhead + " run" + Plural(RunsAhead);
//        				}
//        	       }
//        	     break;	
//        	     
//        	  case 4: 
//        		  int tm1 = matchAllData.getMatch().getInning().get(3).getBattingTeamId();
//        		  int WksLeft = 10 - matchAllData.getMatch().getInning().get(ing-1).getTotalWickets();
//        		  
//        		  if (ct.getRemaningRuns() < 0) {
//        		      ct.setRemaningRuns(0); 
//        		  }
//        		  if (ct.getRemaningBall() < 0) {
//        		      ct.setRemaningBall(0);
//        		  }
//        		  if(ct.getRemaningRuns() <= 0) {
//        			  ResultTxt = getteamname(tm1,cricketService) + " win by " + WksLeft + " wicket" + Plural(WksLeft);
//        		  }else {
//        			  if(WksLeft <= 0) {
//        				  if(ct.getRemaningRuns() == 1) {
//        					  ResultTxt = "Match Tied"; 
//        				  }else {
//        					  int tm2 = matchAllData.getMatch().getInning().get(3).getBowlingTeamId();
//        					  ResultTxt = getteamname(tm2,cricketService) + " win by " + (ct.getRemaningRuns() - 1) + " run" + Plural(ct.getRemaningRuns() - 1);
//        				  }
//        			  }
//        		  }
//        	     break;	
//	        	      	
//	           	}
//		   }
//		   //limited over match
//		   else {
//			  
//			   if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
//				   
//				    TotalWktsAlowd = 2;
//				  }else
//				  {
//					  TotalWktsAlowd = 10;
//				  }
//			   int WksLeft = TotalWktsAlowd - matchAllData.getMatch().getInning().get(ing-1).getTotalWickets();
//			   if (ct.getRemaningRuns() < 0) {
//     		       ct.setRemaningRuns(0);
//     		   }
//     		   if (ct.getRemaningBall() < 0) {
//     		      ct.setRemaningBall(0); 
//     		   }
//     		    String DLTXT="";
//     		    if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)){
//     		    	DLTXT = " (" + CricketUtil.DLS + ")";
//     		    }else if(matchAllData.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)){
//     		    	DLTXT = " (" + CricketUtil.VJD + ")";
//     		    }
//     		    if(ct.getRemaningRuns() > 0 && WksLeft > 0 && 
//					   ct.getRemaningBall()>0) {
//     		    }else {
//     		    	if(matchAllData.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.SUPER_OVER)) {
//     		    		if(ct.getRemaningRuns() == 0) {
//     						if(WksLeft == 0) {
//     							
//     						}else {
//     							int batID = matchAllData.getMatch().getInning().get(ing-1).getBattingTeamId();
//     							System.out.println(matchAllData.getMatch().getInning().get(ing-1).getBattingTeamId());
//     							 ResultTxt = getteamname(batID,cricketService) + " win the super over";
//     						}
//     							
//     					}else if(ct.getRemaningRuns() == 1) {
// 							ResultTxt = "Super over tied";
// 						}else {
// 							int bowId = matchAllData.getMatch().getInning().get(ing-1).getBowlingTeamId();
// 							ResultTxt = getteamname(bowId,cricketService) + " win the super over";
// 						}
//     		    	}
//     		    	//t20
//     		    	else {
//     		    		if(ct.getRemaningRuns() == 0) {
//     						if(WksLeft == 0) {
//     							
//     						}else {
//     							int batID = matchAllData.getMatch().getInning().get(ing-1).getBattingTeamId();
//     							System.out.println(matchAllData.getMatch().getInning().get(ing -1).getBattingTeamId());
//     							ResultTxt = getteamname(batID,cricketService) + " win by " + WksLeft + " wicket" + Plural(WksLeft) + DLTXT;
//     						}
//     					}else if(ct.getRemaningRuns() == 1) {
//     						ResultTxt = "Match Tied" + DLTXT;
//     					}else {
//     						int bowId = matchAllData.getMatch().getInning().get(ing-1).getBowlingTeamId();
// 							ResultTxt = getteamname(bowId,cricketService) + " win by" +  (ct.getRemaningRuns() - 1) + " run" + Plural(ct.getRemaningRuns() - 1) + DLTXT ;
//     					}
//     		    	}
//     		    }
//
//     		}
//	    }
//	    return ResultTxt;
//     }
	   
  public static String playerRoleIcons(Player hs) {
	  String role = "";
	  if(hs.getCaptainWicketKeeper().equalsIgnoreCase(CricketUtil.WICKET_KEEPER)||hs.getCaptainWicketKeeper().equalsIgnoreCase("CAPTAIN_WICKET_KEEPER")) {
			role = "WicketKeeper";
		}else if (hs.getRole().equalsIgnoreCase("BATSMAN") || hs.getRole().equalsIgnoreCase("BATTER") || hs.getRole().equalsIgnoreCase("BAT/KEEPER")) {
		    role = hs.getBattingStyle().equalsIgnoreCase("RHB") ? "Batsman" : "Batsman_Lefthand";
		} else if (hs.getRole().equalsIgnoreCase("BOWLER")) {
			if(hs.getBowlingStyle() == null) {
				role = "FastBowler";
			}else {
				switch(hs.getBowlingStyle()) {
				case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
					role = "FastBowler";
					break;
				case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
					role = "SpinBowlerIcon";
					break;
				}
			}
		} else if (hs.getRole().equalsIgnoreCase("ALL-ROUNDER")) {
			if(hs.getBowlingStyle() == null) {
				if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
					role = "FastBowlerAllrounder";
				}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
					role = "FastBowlerAllrounderLeft";
				}
			}else {
				switch(hs.getBowlingStyle()) {
				case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
					if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
						role = "FastBowlerAllrounder";
					}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
						role = "FastBowlerAllrounderLeft";
					}
					break;
				case "ROB": case "RLB": case "LSL": case "WSL": case "LCH": case "RLG": case "WSR": case "LSO":
					if(hs.getBattingStyle().equalsIgnoreCase("RHB")) {
						role = "SpinBowlerAllrounder";
					}else if(hs.getBattingStyle().equalsIgnoreCase("LHB")) {
						role = "SpinBowlerAllrounderLeft";
					}
					break;
				}
			}
		}
	return role;
		
  }
  public static MatchStats getpowerPlay(MatchAllData session_match) {
	   
	   MatchStats matchStats = new MatchStats();
	   
	   List<Event> events = session_match.getEventFile().getEvents();
	   List<Event> Event = new ArrayList<Event>();
	   boolean setInning1 = false, setInning2 = false;
	   if(events != null && events.size() > 0) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if(events.get(i).getEventInningNumber()==1 && !setInning1) {
					Inning inn = session_match.getMatch().getInning().get(0);
					List<Integer> powerplay = getBallCountStartAndEndRange(session_match, inn);
					
					if (powerplay == null || powerplay.isEmpty()) {
					    // Handle missing or invalid powerplay data
					    matchStats.setPhase1StartOver(0);matchStats.setPhase1EndOver(0);
					    matchStats.setPhase2StartOver(0);matchStats.setPhase2EndOver(0);
					    matchStats.setPhase3StartOver(0);matchStats.setPhase3EndOver(0);
					}else {
						for (int j = 0; j < powerplay.size(); j += 2) {
						    int phase = (j / 2) + 1;
						    switch (phase) {
						        case 1:
						            matchStats.setPhase1StartOver(powerplay.get(j));
						            matchStats.setPhase1EndOver(powerplay.get(j + 1));
						            break;
						        case 2:
						            matchStats.setPhase2StartOver(powerplay.get(j));
						            matchStats.setPhase2EndOver(powerplay.get(j + 1));
						            break;
						        case 3:
						            matchStats.setPhase3StartOver(powerplay.get(j));
						            matchStats.setPhase3EndOver(powerplay.get(j + 1));
						            break;
						    }
						}
					}
		        	setInning1 = true;
				}else if(events.get(i).getEventInningNumber()==2 && !setInning2) {
					Inning inn = session_match.getMatch().getInning().get(1);
					List<Integer> powerplay = getBallCountStartAndEndRange(session_match, inn);
					
					if (powerplay == null || powerplay.isEmpty()) {
					    // Handle missing or invalid powerplay data
					    matchStats.setPhase1StartOver(0);matchStats.setPhase1EndOver(0);
					    matchStats.setPhase2StartOver(0);matchStats.setPhase2EndOver(0);
					    matchStats.setPhase3StartOver(0);matchStats.setPhase3EndOver(0);
					}else {
						for (int j = 0; j < powerplay.size(); j += 2) {
						    int phase = (j / 2) + 1;
						    switch (phase) {
						        case 1:
						            matchStats.setPhase1StartOver(powerplay.get(j));
						            matchStats.setPhase1EndOver(powerplay.get(j + 1));
						            break;
						        case 2:
						            matchStats.setPhase2StartOver(powerplay.get(j));
						            matchStats.setPhase2EndOver(powerplay.get(j + 1));
						            break;
						        case 3:
						            matchStats.setPhase3StartOver(powerplay.get(j));
						            matchStats.setPhase3EndOver(powerplay.get(j + 1));
						            break;
						    }
						}
					}
					
		        	setInning2 = true;
				}
				//Powerplay
			    switch(session_match.getEventFile().getEvents().get(i).getEventType()) {
				    case CricketUtil.LOG_OVERWRITE_BATSMAN_HOWOUT :
						Event.add(events.get(i));
					 break;
			    	case CricketUtil.LOG_ANY_BALL:case CricketUtil.WIDE:case CricketUtil.NO_BALL:
				    	if( events.get(i).getEventBowlerNo() > 0) {
					         if (events.get(i).getEventOverNo() *6 < matchStats.getPhase1EndOver()) {
								String statsData = getpowerplay(events.get(i));
								if(statsData.contains(",") && statsData.split(",").length >= 7) {
									if(events.get(i).getEventInningNumber()==1) {
										//PHASE_SCORE PHASE 1 HOME
										matchStats.setHomeFirstPowerPlay(new VariousStats(
												matchStats.getHomeFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
												matchStats.getHomeFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
												matchStats.getHomeFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
												matchStats.getHomeFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
												matchStats.getHomeFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
												Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeFirstPowerPlay().getOutBatsman().isEmpty()
											        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
											        : matchStats.getHomeFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
											    : matchStats.getHomeFirstPowerPlay().getOutBatsman(),
											    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeFirstPowerPlay().getNotWicketCount().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
												        : matchStats.getHomeFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
												    : matchStats.getHomeFirstPowerPlay().getNotWicketCount()));		
										
									}else if(events.get(i).getEventInningNumber()==2) {
										//PHASE_SCORE PHASE 1 AWAY
										matchStats.setAwayFirstPowerPlay(new VariousStats(
												matchStats.getAwayFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
												matchStats.getAwayFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
												matchStats.getAwayFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
												matchStats.getAwayFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
												matchStats.getAwayFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
												Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayFirstPowerPlay().getOutBatsman().isEmpty()
											        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
											        : matchStats.getAwayFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
											    : matchStats.getAwayFirstPowerPlay().getOutBatsman(),
											    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayFirstPowerPlay().getNotWicketCount().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
												        : matchStats.getAwayFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
												    : matchStats.getAwayFirstPowerPlay().getNotWicketCount()));
									 
										}
									
									}
								}
							if (events.get(i).getEventOverNo()*6 == (matchStats.getPhase2StartOver()) || (events.get(i).getEventOverNo()*6 > (matchStats.getPhase2StartOver()) && 
					                    events.get(i).getEventOverNo() < matchStats.getPhase2EndOver())) {

									String statsData = getpowerplay(events.get(i));
									if(statsData.contains(",") && statsData.split(",").length >= 7) {
										if(events.get(i).getEventInningNumber()==1) {
											//PHASE_SCORE PHASE 2 HOME
											matchStats.setHomeSecondPowerPlay(new VariousStats(
													matchStats.getHomeSecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getHomeSecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getHomeSecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getHomeSecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getHomeSecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeSecondPowerPlay().getOutBatsman().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getHomeSecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getHomeSecondPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeSecondPowerPlay().getNotWicketCount().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getHomeSecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getHomeSecondPowerPlay().getNotWicketCount()));
											
										}else if(events.get(i).getEventInningNumber()==2) {
											//PHASE_SCORE PHASE 2 AWAY
											matchStats.setAwaySecondPowerPlay(new VariousStats(
													matchStats.getAwaySecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getAwaySecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getAwaySecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getAwaySecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getAwaySecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwaySecondPowerPlay().getOutBatsman().isEmpty()? 
														String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getAwaySecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getAwaySecondPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwaySecondPowerPlay().getNotWicketCount().isEmpty()? 
															String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getAwaySecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getAwaySecondPowerPlay().getNotWicketCount()
													));
										 }
									}
								}
								if (events.get(i).getEventOverNo()*6 == (matchStats.getPhase3StartOver()) || (events.get(i).getEventOverNo()*6 > (matchStats.getPhase3StartOver()) && 
					                    events.get(i).getEventOverNo() <= matchStats.getPhase3EndOver())) {

									   String statsData = getpowerplay(events.get(i));
										
										if(statsData.contains(",") && statsData.split(",").length >= 7) {
											if(events.get(i).getEventInningNumber()==1) {
												//PHASE_SCORE PHASE 3 HOME
												matchStats.setHomeThirdPowerPlay(new VariousStats(
														matchStats.getHomeThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
														matchStats.getHomeThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
														matchStats.getHomeThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
														matchStats.getHomeThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
														matchStats.getHomeThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
														Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeThirdPowerPlay().getOutBatsman().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
														        : matchStats.getHomeThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
														    : matchStats.getHomeThirdPowerPlay().getOutBatsman(),
														    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeThirdPowerPlay().getNotWicketCount().isEmpty()
															        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
															        : matchStats.getHomeThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
															    : matchStats.getHomeThirdPowerPlay().getNotWicketCount()));
											
											}else if(events.get(i).getEventInningNumber()==2) {
												//PHASE_SCORE PHASE 3 AWAY
												matchStats.setAwayThirdPowerPlay(new VariousStats(
														matchStats.getAwayThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
														matchStats.getAwayThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
														matchStats.getAwayThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
														matchStats.getAwayThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
														matchStats.getAwayThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
														Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayThirdPowerPlay().getOutBatsman().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
													        : matchStats.getAwayThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
													    : matchStats.getAwayThirdPowerPlay().getOutBatsman(),
													    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayThirdPowerPlay().getNotWicketCount().isEmpty()
														        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
														        : matchStats.getAwayThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
														    : matchStats.getAwayThirdPowerPlay().getNotWicketCount()
														));
											 }
										}
								}
						} 
			    		break;
			    	case CricketUtil.DOT: case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
				    case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.BYE: case CricketUtil.LEG_BYE: 
				    case CricketUtil.LOG_WICKET:case CricketUtil.NINE:
				    	
				    	if( events.get(i).getEventBowlerNo() > 0) {
							if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= (matchStats.getPhase1StartOver()) &&
						                    ((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= matchStats.getPhase1EndOver())) {
								
								String statsData = getpowerplay(events.get(i));
								if(statsData.contains(",") && statsData.split(",").length >= 7) {
									if(events.get(i).getEventInningNumber()==1) {
										//PHASE_SCORE PHASE 1 HOME
										matchStats.setHomeFirstPowerPlay(new VariousStats(
												matchStats.getHomeFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
												matchStats.getHomeFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
												matchStats.getHomeFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
												matchStats.getHomeFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
												matchStats.getHomeFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
												Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeFirstPowerPlay().getOutBatsman().isEmpty()
											        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
											        : matchStats.getHomeFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
											    : matchStats.getHomeFirstPowerPlay().getOutBatsman(),
											    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeFirstPowerPlay().getNotWicketCount().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
												        : matchStats.getHomeFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
												    : matchStats.getHomeFirstPowerPlay().getNotWicketCount()));	
										
									}else if(events.get(i).getEventInningNumber()==2) {
										//PHASE_SCORE PHASE 1 AWAY
										matchStats.setAwayFirstPowerPlay(new VariousStats(
												matchStats.getAwayFirstPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
												matchStats.getAwayFirstPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
												matchStats.getAwayFirstPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
												matchStats.getAwayFirstPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
												matchStats.getAwayFirstPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
												Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayFirstPowerPlay().getOutBatsman().isEmpty()
											        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
											        : matchStats.getAwayFirstPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
											    : matchStats.getAwayFirstPowerPlay().getOutBatsman(),
											    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayFirstPowerPlay().getNotWicketCount().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
												        : matchStats.getAwayFirstPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
												    : matchStats.getAwayFirstPowerPlay().getNotWicketCount()));
										}
									
									}
								}
							
							 if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= ((matchStats.getPhase2StartOver()))) &&
					                    (((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= ((matchStats.getPhase2EndOver())))) {

									String statsData = getpowerplay(events.get(i));
									
									if(statsData.contains(",") && statsData.split(",").length >= 7) {
										if(events.get(i).getEventInningNumber()==1) {
											//PHASE_SCORE PHASE 2 HOME
											matchStats.setHomeSecondPowerPlay(new VariousStats(
													matchStats.getHomeSecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getHomeSecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getHomeSecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getHomeSecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getHomeSecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeSecondPowerPlay().getOutBatsman().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getHomeSecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getHomeSecondPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeSecondPowerPlay().getNotWicketCount().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getHomeSecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getHomeSecondPowerPlay().getNotWicketCount()));
											
										}else if(events.get(i).getEventInningNumber()==2) {
											//PHASE_SCORE PHASE 2 AWAY
											matchStats.setAwaySecondPowerPlay(new VariousStats(
													matchStats.getAwaySecondPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getAwaySecondPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getAwaySecondPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getAwaySecondPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getAwaySecondPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwaySecondPowerPlay().getOutBatsman().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getAwaySecondPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getAwaySecondPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwaySecondPowerPlay().getNotWicketCount().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getAwaySecondPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getAwaySecondPowerPlay().getNotWicketCount()));
											
										 }
									}
								}
							if ((((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) >= ((matchStats.getPhase3StartOver()))) &&
				                    (((events.get(i).getEventOverNo() * 6) + events.get(i).getEventBallNo()) <= ((matchStats.getPhase3EndOver())))) {

									String statsData = getpowerplay(events.get(i));
									
									if(statsData.contains(",") && statsData.split(",").length >= 7) {
										if(events.get(i).getEventInningNumber()==1) {
											//PHASE_SCORE PHASE 3 HOME
											matchStats.setHomeThirdPowerPlay(new VariousStats(
													matchStats.getHomeThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getHomeThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getHomeThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getHomeThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getHomeThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getHomeThirdPowerPlay().getOutBatsman().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getHomeThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getHomeThirdPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getHomeThirdPowerPlay().getNotWicketCount().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getHomeThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getHomeThirdPowerPlay().getNotWicketCount()));

										}else if(events.get(i).getEventInningNumber()==2) {
											//PHASE_SCORE PHASE 3 AWAY
											matchStats.setAwayThirdPowerPlay(new VariousStats(
													matchStats.getAwayThirdPowerPlay().getTotalRuns() + Integer.valueOf(statsData.split(",")[0]), 
													matchStats.getAwayThirdPowerPlay().getTotalWickets() + Integer.valueOf(statsData.split(",")[1]), 
													matchStats.getAwayThirdPowerPlay().getTotalFours() + Integer.valueOf(statsData.split(",")[3]), 
													matchStats.getAwayThirdPowerPlay().getTotalSixes() + Integer.valueOf(statsData.split(",")[4]), 
													matchStats.getAwayThirdPowerPlay().getTotalNines() + Integer.valueOf(statsData.split(",")[5]),
													Integer.valueOf(statsData.split(",")[7]) > 0 ? (matchStats.getAwayThirdPowerPlay().getOutBatsman().isEmpty()
												        ? String.valueOf(Integer.valueOf(statsData.split(",")[7]))
												        : matchStats.getAwayThirdPowerPlay().getOutBatsman() + "," + Integer.valueOf(statsData.split(",")[7]))
												    : matchStats.getAwayThirdPowerPlay().getOutBatsman(),
												    Integer.valueOf(statsData.split(",")[8]) > 0 ? (matchStats.getAwayThirdPowerPlay().getNotWicketCount().isEmpty()
													        ? String.valueOf(Integer.valueOf(statsData.split(",")[8]))
													        : matchStats.getAwayThirdPowerPlay().getNotWicketCount() + "," + Integer.valueOf(statsData.split(",")[8]))
													    : matchStats.getAwayThirdPowerPlay().getNotWicketCount()));

										 }
									}
								}
						} 
			    		
			    		break;
			    }
			}
	   }
	   for (Event even : Event) {
		    int Batter_id = even.getEventBattingCard().getPlayerId();
		    boolean isRetired = even.getEventHowOut() != null && !even.getEventHowOut().isEmpty() && 
                   (even.getEventHowOut().equalsIgnoreCase(CricketUtil.RETIRED_HURT) ||
                    even.getEventHowOut().equalsIgnoreCase(CricketUtil.ABSENT_HURT) ||
                    even.getEventHowOut().equalsIgnoreCase(CricketUtil.CONCUSSED));
			
		    // First PP
		    updateWickets(matchStats.getHomeFirstPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwayFirstPowerPlay(), Batter_id, isRetired);

		    // Second PP
		    updateWickets(matchStats.getHomeSecondPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwaySecondPowerPlay(), Batter_id, isRetired);

		    // Third PP
		    updateWickets(matchStats.getHomeThirdPowerPlay(), Batter_id, isRetired);
		    updateWickets(matchStats.getAwayThirdPowerPlay(), Batter_id, isRetired);
		}
		
	 
	return matchStats;
	   
   }
}
