package com.cricket.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.Ae_Third_Party_Xml.AE_Cricket;
import com.cricket.archive.Archive;
import com.cricket.archive.ArchiveData;
import com.cricket.model.BatSpeed;
import com.cricket.model.BattingCard;
import com.cricket.model.BestStats;
import com.cricket.model.BowlerData;
import com.cricket.model.BowlingCard;
import com.cricket.model.Configuration;
import com.cricket.model.Dictionary;
import com.cricket.model.DuckWorthLewis;
import com.cricket.model.Event;
import com.cricket.model.EventFile;
import com.cricket.model.FallOfWicket;
import com.cricket.model.FieldersData;
import com.cricket.model.Fixture;
import com.cricket.model.ForeignLanguageData;
import com.cricket.model.Inning;
import com.cricket.model.Match;
import com.cricket.model.MatchAllData;
import com.cricket.model.MatchClock;
import com.cricket.model.MultiLanguageDatabase;
import com.cricket.model.OverByOverData;
import com.cricket.model.Partnership;
import com.cricket.model.Player;
import com.cricket.model.Review;
import com.cricket.model.Season;
import com.cricket.model.Setup;
import com.cricket.model.Speed;
import com.cricket.model.Statistics;
import com.cricket.model.Team;
import com.cricket.model.Tournament;
import com.cricket.service.CricketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class CricketFunctions {
	
	public static ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
	
	public static AE_Cricket getDataFromThirdParty(String FilePathName) throws JAXBException {
		AE_Cricket cricket_data =(AE_Cricket)JAXBContext.newInstance(AE_Cricket.class)
		.createUnmarshaller().unmarshal(new File(FilePathName));
		return cricket_data;
	}
	
	public static MatchAllData getMatchDataFromWebsite(WebDriver driver, String whatToProcess, 
		String broadcaster, String valueToProcess, List<Team> all_teams) throws StreamWriteException, DatabindException, JAXBException, IOException, URISyntaxException
	{
		List<BattingCard> this_battingcard = new ArrayList<BattingCard>();
		List<FallOfWicket> this_FoWs = new ArrayList<FallOfWicket>();
		List<Inning> this_inn = new ArrayList<Inning>();
		List<BowlingCard> this_bowlingcard = new ArrayList<BowlingCard>();
		Player this_player = new Player();
		WebElement this_webElement;
		int column_data_count = 0,bowling_card_count = 1;
		boolean extras_found = false, total_found = false;
		String data_to_process = "";

		MatchAllData this_match = new MatchAllData();
		this_match.setMatch(new Match());
		this_match.setSetup(new Setup());
		
		
		if(valueToProcess.toUpperCase().contains("-" + CricketUtil.TEST + "-")) {
			this_match.getSetup().setMatchType(CricketUtil.TEST);
			this_match.getSetup().setMaxOvers(CricketUtil.TEST_MAXIMUM_OVERS);
		} else if(valueToProcess.toUpperCase().contains("-" + CricketUtil.ODI + "-") || valueToProcess.toUpperCase().contains("-" + CricketUtil.OD + "-")) {
			this_match.getSetup().setMatchType(CricketUtil.ODI);
			this_match.getSetup().setMaxOvers(CricketUtil.ODI_MAXIMUM_OVERS);
		} else {
			this_match.getSetup().setMatchType(CricketUtil.IT20);
			this_match.getSetup().setMaxOvers(CricketUtil.T20_MAXIMUM_OVERS);
		}
		System.out.println(valueToProcess);
		this_match.getSetup().setSaveMatchFileAs("JSON");
		this_match.getSetup().setMatchIdent(valueToProcess.split("/")[valueToProcess.split("/").length-2]);
		this_match.getSetup().setTournament(valueToProcess.split("/")[valueToProcess.split("/").length-2]);
		switch (broadcaster.toUpperCase()) {
		case CricketUtil.CRIC_INFO:
			
			switch (whatToProcess) {
			case "GET-SINGLE-MATCH-DATA":
				
				driver.get(valueToProcess);
				
				for (WebElement this_team : driver.findElements(By.className("ci-team-score"))) {
					if(this_match.getMatch().getMatchResult() == null) {
						this_webElement = this_team.findElement(By.xpath("../../.."));
						System.out.println("this_webElement tag name = " + this_webElement.getTagName());
						System.out.println("this_webElement p/span is empty = " + this_webElement.findElements(By.xpath("./p/span")).isEmpty());
						if(!this_webElement.findElements(By.xpath("./p/span")).isEmpty()) {
							System.out.println("Result = " + this_webElement.findElement(By.xpath("./p/span")).getText());
							this_match.getMatch().setMatchResult(this_webElement.findElement(By.xpath("./p/span")).getText());
						}
					}
					//column_data_count = 0;
					for (WebElement team_anchor : this_team.findElements(By.tagName("a"))) {
						if(team_anchor.getAttribute("href").contains("/team/")
							&& team_anchor.getAttribute("href").contains("-")) {
							System.out.println("team_anchor href = " + team_anchor.getAttribute("href"));
							data_to_process = team_anchor.getAttribute("href").split("/")[team_anchor.getAttribute("href").split("/").length-1];
							System.out.println("team name = " + team_anchor.findElement(By.tagName("span")).getText());
							System.out.println("team ID = " + data_to_process.split("-")[data_to_process.split("-").length-1]);
							for (Team team : all_teams) {
								if(team_anchor.findElement(By.tagName("span")).getText().toLowerCase().contains(team.getTeamName1().toLowerCase())
										|| team_anchor.findElement(By.tagName("span")).getText().toLowerCase().contains(team.getTeamName4().toLowerCase())) {
									
									column_data_count = column_data_count + 1;
									
									switch (column_data_count) {
									case 1:
										System.out.println("column_data_count1 = " + column_data_count);
										System.out.println(team_anchor.findElement(By.tagName("span")).getText().toLowerCase());
										System.out.println("teams = " + team.getTeamName4().toLowerCase());
										this_match.getSetup().setHomeTeam(team);
										this_match.getSetup().getHomeTeam().setTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
										this_match.getSetup().setHomeTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
										break;
									case 2:
										System.out.println("column_data_count2 = " + column_data_count);
										System.out.println(team_anchor.findElement(By.tagName("span")).getText().toLowerCase());
										System.out.println("teams = " + team.getTeamName4().toLowerCase());
										this_match.getSetup().setAwayTeam(team);
										this_match.getSetup().getAwayTeam().setTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
										this_match.getSetup().setAwayTeamId(Integer.valueOf(data_to_process.split("-")[data_to_process.split("-").length-1]));
										break;
									}
									break;
								}
							}
						}
						if(column_data_count > 2) {
							break;
						}
					}
				}
				
				for (WebElement batting_card_table : driver.findElements(By.className("ci-scorecard-table"))) {

					this_inn.add(new Inning()); // Add new inning when batting card is detected
					this_inn.get(this_inn.size()-1).setInningNumber(this_inn.size());
					this_battingcard = new ArrayList<BattingCard>();
					
					for(WebElement row : batting_card_table.findElements(By.xpath("./tbody/tr")))
					{
						column_data_count = 0;
						for(WebElement column : row.findElements(By.tagName("td")))
						{
							System.out.println("column.getText() = " + column.getText());
							
							if(column.getText().equalsIgnoreCase(CricketUtil.EXTRAS)) {
								extras_found = true;
							} else if(column.getText().equalsIgnoreCase(CricketUtil.TOTAL)) {
								total_found = true;
							}
							if(extras_found == true) {
								
								if(!column.getText().isEmpty() && column.getText().contains("(")
									&& column.getText().contains(")")) {
									
									data_to_process = column.getText().replace("(", "");
									data_to_process = data_to_process.replace(")", "");
									System.out.println("Extra data_to_process = " + data_to_process);
									if(data_to_process.contains(",")) {
										for (String ext : data_to_process.split(",")) {
											if(ext.toUpperCase().contains("LB")) {
												this_inn.get(this_inn.size()-1).setTotalLegByes(
													Integer.valueOf(ext.toUpperCase().replace("LB", "").trim()));
											}else if(ext.toUpperCase().contains("NB")) {
												this_inn.get(this_inn.size()-1).setTotalNoBalls(
													Integer.valueOf(ext.toUpperCase().replace("NB", "").trim()));
											}else if(ext.toUpperCase().contains("B")) {
												this_inn.get(this_inn.size()-1).setTotalByes(
													Integer.valueOf(ext.toUpperCase().replace("B", "").trim()));
											}else if(ext.toUpperCase().contains("W")) {
												this_inn.get(this_inn.size()-1).setTotalWides(
													Integer.valueOf(ext.toUpperCase().replace("W", "").trim()));
											}
										}
									} else {
										if(data_to_process.toUpperCase().contains("LB")) {
											this_inn.get(this_inn.size()-1).setTotalLegByes(
												Integer.valueOf(data_to_process.toUpperCase().replace("LB", "").trim()));
										}else if(data_to_process.toUpperCase().contains("NB")) {
											this_inn.get(this_inn.size()-1).setTotalNoBalls(
												Integer.valueOf(data_to_process.toUpperCase().replace("NB", "").trim()));
										}else if(data_to_process.toUpperCase().contains("B")) {
											this_inn.get(this_inn.size()-1).setTotalByes(
												Integer.valueOf(data_to_process.toUpperCase().replace("B", "").trim()));
										}else if(data_to_process.toUpperCase().contains("W")) {
											this_inn.get(this_inn.size()-1).setTotalWides(
												Integer.valueOf(data_to_process.toUpperCase().replace("W", "").trim()));
										}
									}
									
								} else if(!column.findElements(By.tagName("strong")).isEmpty()) {
									
									this_inn.get(this_inn.size()-1).setTotalExtras(
										Integer.valueOf(column.getText()));
									
									extras_found = false;
									
								} 

								
							} else if(total_found == true) {
								
								if(!column.findElements(By.tagName("span")).isEmpty()) {
									for (WebElement this_span : column.findElements(By.tagName("span"))) {
										System.out.println("this_span = " + this_span);
										System.out.println("TOTAL OVERS = " + this_span.getText());
										if(this_span.getText().toUpperCase().contains(" OV") && !this_span.getText().toUpperCase().contains(",")) {
											data_to_process = this_span.getText().toUpperCase().replace("OV", "").trim();
											System.out.println("Overs = " + data_to_process);
											if(data_to_process.contains(".") && !data_to_process.contains(",")) {
												System.out.println("Over no = " + data_to_process.substring(0,data_to_process.indexOf(".")));
												System.out.println("Ball no = " + data_to_process.substring(data_to_process.indexOf(".")+1));
												this_inn.get(this_inn.size()-1).setTotalOvers(
													Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("."))));
												this_inn.get(this_inn.size()-1).setTotalBalls(
														Integer.valueOf(data_to_process.substring(data_to_process.indexOf(".")+1)));
											}else if(!data_to_process.contains(",")) {
												System.out.println("50Over no = " + data_to_process);
												this_inn.get(this_inn.size()-1).setTotalOvers(
													Integer.valueOf(data_to_process));
												this_inn.get(this_inn.size()-1).setTotalBalls(0);
											}
										} else {
											if(this_span.getText().toUpperCase().contains("(RR:")) {
												data_to_process = this_span.getText().toUpperCase().replace("(RR:", "");
												data_to_process = data_to_process.replace(")", "").trim();
												this_inn.get(this_inn.size()-1).setRunRate(data_to_process);
											}
										}
									}
								} else {
									System.out.println("TOTAL = " + column.getText());
									data_to_process = column.getText().trim();
									if(data_to_process.contains("/")) {
										System.out.println("Total Score = " + data_to_process);
										System.out.println("Runs = " + data_to_process.substring(0,data_to_process.indexOf("/")));
										System.out.println("Wickets = " + data_to_process.substring(data_to_process.indexOf("/")+1));
										this_inn.get(this_inn.size()-1).setTotalRuns(
											Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("/"))));
										this_inn.get(this_inn.size()-1).setTotalWickets(
											Integer.valueOf(data_to_process.substring(data_to_process.indexOf("/")+1)));
										total_found = false;
									} else {
										if(NumberUtils.isCreatable(data_to_process)) {
											System.out.println("Total Runs without / = " + data_to_process);
											System.out.println("Total Wickets = " + this_battingcard.size());
											this_inn.get(this_inn.size()-1).setTotalRuns(Integer.valueOf(data_to_process));
											this_inn.get(this_inn.size()-1).setTotalWickets(this_battingcard.size()-1);
											total_found = false;
										}
									}
								}
								
							} else { // Batting card data
								
								if(column.getText().toUpperCase().contains("FALL OF WICKETS:")) {
									
									data_to_process = column.getText().toUpperCase().replace("FALL OF WICKETS:", "").trim();
									System.out.println("FoW -> data_to_process = " + data_to_process);
									for(int i=0; i < data_to_process.split(",").length; i++) {
										
										for (BattingCard bc : this_battingcard) {

											if(bc.getPlayer() != null && bc.getPlayer().getFull_name() != null
												&& bc.getPlayer().getFull_name().equalsIgnoreCase(data_to_process.split(",")[i].substring(
														data_to_process.split(",")[i].indexOf("(") + 1))) {
												
												this_FoWs.add(new FallOfWicket(Integer.valueOf(data_to_process.split(",")[i].substring(0, 
														data_to_process.split(",")[i].indexOf("-")).trim()), 
													bc.getPlayer().getPlayerId(), Integer.valueOf(data_to_process.split(",")[i].substring(
													data_to_process.split(",")[i].indexOf("-") + 1, data_to_process.split(",")[i].indexOf("(") 
													- data_to_process.split(",")[i].indexOf("-") + 1).trim()), 
													Integer.valueOf(data_to_process.split(",")[i+1].toUpperCase()
													.replace("OV)", "").substring(0,data_to_process.split(",")[i+1].indexOf(".")).trim()), 
													Integer.valueOf(data_to_process.split(",")[i+1].toUpperCase()
													.replace("OV)", "").substring(data_to_process.split(",")[i+1].indexOf(".")+1).trim())));
												i++;
												System.out.println("FoW Variable = " + this_FoWs.get(this_FoWs.size()-1).toString());
												break;
											}
										}										
										
									}
								} else if(column.getText().toUpperCase().contains("DID NOT BAT:")) {
									for (WebElement did_not_bat_anchor : column.findElements(By.tagName("a"))) {
										if(did_not_bat_anchor.getAttribute("href").contains("/cricketers/")
												&& did_not_bat_anchor.getAttribute("href").contains("-")) {
											System.out.println("did_not_bat_anchor = " + did_not_bat_anchor.getAttribute("href"));
											System.out.println("did_not_bat_anchor span->span = " + did_not_bat_anchor.findElement(
												By.xpath("./span/span")).getText());
											this_player = new Player();
											this_player.setPlayerId(Integer.valueOf(did_not_bat_anchor.getAttribute("href").split("-")[
											    did_not_bat_anchor.getAttribute("href").split("-").length-1]));
											this_player.setFull_name(did_not_bat_anchor.findElement(
												By.xpath("./span/span")).getText().replace(",", "").trim());
											if(did_not_bat_anchor.findElement(
													By.xpath("./span/span")).getText().replace(",", "").trim().contains(" ")) {
												this_player.setFirstname(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[0]);
												this_player.setSurname(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[1]);
												this_player.setTicker_name(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim().split(" ")[1]);
											}else {
												this_player.setFirstname(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim());
												this_player.setSurname(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim());
												this_player.setTicker_name(did_not_bat_anchor.findElement(
														By.xpath("./span/span")).getText().replace(",", "").trim());
											}
											
											this_battingcard.add(new BattingCard(this_player.getPlayerId(), 
												this_battingcard.size() + 1,CricketUtil.STILL_TO_BAT));
											this_battingcard.get(this_battingcard.size()-1).setPlayer(this_player);
										}
									}
								} else {
									if(!column.findElements(By.tagName("a")).isEmpty()) {
										if(column.findElement(By.tagName("a")).getAttribute("href").contains("/cricketers/")
											&& column.findElement(By.tagName("a")).getAttribute("href").contains("-")) {
										
											System.out.println("Bat anchor HREF = " + column.findElement(
													By.tagName("a")).getAttribute("href"));
											System.out.println("Bat player full name = " + column.findElement(By.tagName("a")).
												findElement(By.tagName("span")).getText());
											this_battingcard.add(new BattingCard(Integer.valueOf(column.findElement(
												By.tagName("a")).getAttribute("href").split("-")[
												column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]), 
												this_battingcard.size() + 1,CricketUtil.STILL_TO_BAT));
											this_player = new Player();
											this_player.setPlayerId(Integer.valueOf(column.findElement(
												By.tagName("a")).getAttribute("href").split("-")[
												column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]));
											this_player.setFull_name(column.findElement(
													By.tagName("a")).getAttribute("title"));
											if(column.findElement(
													By.tagName("a")).getAttribute("title").contains(" ")) {
												this_player.setFirstname(column.findElement(
														By.tagName("a")).getAttribute("title").split(" ")[0]);
												this_player.setSurname(column.findElement(
														By.tagName("a")).getAttribute("title").split(" ")[1]);
												this_player.setTicker_name(column.findElement(
														By.tagName("a")).getAttribute("title").split(" ")[1]);
											}else {
												this_player.setFirstname(column.findElement(
														By.tagName("a")).getAttribute("title"));
												this_player.setSurname(column.findElement(
														By.tagName("a")).getAttribute("title"));
												this_player.setTicker_name(column.findElement(
														By.tagName("a")).getAttribute("title"));
											}
											
											this_battingcard.get(this_battingcard.size()-1).setPlayer(this_player);

											column_data_count = 0;
											
										}
										
									} else if(!column.findElements(By.xpath("./span/span")).isEmpty()) {

										data_to_process = column.findElement(By.tagName("span")).findElement(
												By.tagName("span")).getText().replaceAll("[^a-zA-Z0-9\\s\\/]", "");
										
										if(data_to_process.equalsIgnoreCase("not out")) {
											this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.NOT_OUT);
										} else if(!data_to_process.isEmpty()) {

											this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.OUT);
											
											this_player = new Player();
											
											if(data_to_process.toLowerCase().contains("c ") && data_to_process.toLowerCase().contains(" b ") && 
												!data_to_process.toLowerCase().contains("c & b")) {
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.CAUGHT);
												if(data_to_process.toLowerCase().contains(" b ")) {
													this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(data_to_process.toLowerCase().split(" b ")[0]);
													this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + data_to_process.toLowerCase().split(" b ")[1]);
													this_player.setFull_name(data_to_process.toLowerCase().split(" b ")[1]);
													if(data_to_process.toLowerCase().split(" b ")[1].contains(" ")) {
														this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[0]);
														this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
														this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
													}else {
														this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1]);
														this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1]);
														this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1]);
													}
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												
											}else if(data_to_process.toLowerCase().contains("c & b")) {
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.CAUGHT_AND_BOWLED);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne("c & b");
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo(data_to_process.split("c & b ")[1]);
												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
												this_player.setFull_name(data_to_process.split("c & b ")[1]);
												if(data_to_process.toLowerCase().split("c & b ")[1].contains(" ")) {
													this_player.setFirstname(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[0]);
													this_player.setSurname(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("c & b ")[1].split(" ")[1]);
												}else {
													this_player.setFirstname(data_to_process.toLowerCase().split("c & b ")[1]);
													this_player.setSurname(data_to_process.toLowerCase().split("c & b ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("c & b ")[1]);
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												
											}else if(data_to_process.toLowerCase().contains("b ")) {
												this_player.setFull_name(data_to_process.split("b ")[1]);
												if(data_to_process.toLowerCase().split("b ")[1].contains(" ")) {
													this_player.setFirstname(data_to_process.toLowerCase().split("b ")[1].split(" ")[0]);
													this_player.setSurname(data_to_process.toLowerCase().split("b ")[1].split(" ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("b ")[1].split(" ")[1]);
												}else {
													this_player.setFirstname(data_to_process.toLowerCase().split("b ")[1]);
													this_player.setSurname(data_to_process.toLowerCase().split("b ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("b ")[1]);
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												this_battingcard.get(this_battingcard.size()-1).setHowOutText("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.BOWLED);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne("");
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
												
											}else if(data_to_process.toLowerCase().contains("lbw b ")) { // lbw
												
												this_player.setFull_name(data_to_process.split("lbw b ")[1]);
												if(data_to_process.toLowerCase().split("lbw b ")[1].contains(" ")) {
													this_player.setFirstname(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[0]);
													this_player.setSurname(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("lbw b ")[1].split(" ")[1]);
												}else {
													this_player.setFirstname(data_to_process.toLowerCase().split("lbw b ")[1]);
													this_player.setSurname(data_to_process.toLowerCase().split("lbw b ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split("lbw b ")[1]);
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.LBW);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(CricketUtil.LBW);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
												
											}else if(data_to_process.toLowerCase().contains("st ") && data_to_process.toLowerCase().contains(" b ")) {
												
												this_player.setFull_name(data_to_process.split(" b ")[1]);
												if(data_to_process.toLowerCase().split(" b ")[1].contains(" ")) {
													this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[0]);
													this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1].split(" ")[1]);
												}else {
													this_player.setFirstname(data_to_process.toLowerCase().split(" b ")[1]);
													this_player.setSurname(data_to_process.toLowerCase().split(" b ")[1]);
													this_player.setTicker_name(data_to_process.toLowerCase().split(" b ")[1]);
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.STUMPED);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(data_to_process.split(" b ")[0]);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo("b " + this_battingcard.get(this_battingcard.size()-1).getHowOutBowler().getTicker_name());
												
											}else if(data_to_process.toLowerCase().contains("run out")) {
												
												data_to_process = data_to_process.toLowerCase().split("run out ")[1]
													.replace("(", "").replace(")", ""); 
												this_player.setFirstname("");
												if(data_to_process.toLowerCase().contains("/")) {
													this_player.setFull_name(data_to_process.split("/")[1]);
													this_player.setSurname(data_to_process.split("/")[1]);
													this_player.setTicker_name(data_to_process.split("/")[1]);
												} else {
													this_player.setFull_name(data_to_process);
													this_player.setSurname(data_to_process);
													this_player.setTicker_name(data_to_process);
												}
												this_battingcard.get(this_battingcard.size()-1).setHowOutBowler(this_player);
												this_battingcard.get(this_battingcard.size()-1).setHowOutText(data_to_process);
												this_battingcard.get(this_battingcard.size()-1).setHowOut(CricketUtil.RUN_OUT);
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartOne(CricketUtil.RUN_OUT.replace("_", " "));
												this_battingcard.get(this_battingcard.size()-1).setHowOutPartTwo(data_to_process);
											}
										} 
										
									} else if(!column.findElements(By.tagName("strong")).isEmpty()) {
										
										System.out.println("Bat Runs = " + column.findElement(By.tagName("strong")).getText());
										this_battingcard.get(this_battingcard.size()-1).setRuns(Integer.valueOf(
											column.findElement(By.tagName("strong")).getText()));

									} else {
										
										if(this_inn.get(this_inn.size()-1).getTotalRuns() <= 0 && this_inn.get(this_inn.size()-1).getTotalWickets() <= 0 &&
											this_inn.get(this_inn.size()-1).getTotalOvers() <= 0 && this_inn.get(this_inn.size()-1).getTotalBalls() <= 0) {
											
											if(column.getText().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
												this_battingcard.get(this_battingcard.size()-1).setStatus(CricketUtil.NOT_OUT);
											}else {
												column_data_count++;
												data_to_process = column.getText().trim();
												if(data_to_process.isEmpty()) {
													if(column_data_count == 5) {
														data_to_process = "0.0";
													} else {
														data_to_process = "0";
													}
												}
												System.out.println("Bat column_data_count = " + column_data_count + " -> " + data_to_process);
												switch (column_data_count) {
												case 1:
													this_battingcard.get(this_battingcard.size()-1).setBalls(
														Integer.valueOf(data_to_process));
													break;
												case 2: // Minutes
													break;
												case 3:
													this_battingcard.get(this_battingcard.size()-1).setFours(
														Integer.valueOf(data_to_process));
													break;
												case 4:
													this_battingcard.get(this_battingcard.size()-1).setSixes(
														Integer.valueOf(data_to_process));
													break;
												case 5:
													this_battingcard.get(this_battingcard.size()-1).setStrikeRate(data_to_process);
													break;
												}
											}
											
										}
									}
								}
							}
						}
					}
					switch (this_inn.size()) {
					case 1:
						if(this_match.getSetup().getHomeTeam() != null) {
							this_inn.get(this_inn.size() - 1).setBatting_team(this_match.getSetup().getHomeTeam());
							this_inn.get(this_inn.size() - 1).setBattingTeamId(this_match.getSetup().getHomeTeamId());
							this_inn.get(this_inn.size() - 1).setBowling_team(this_match.getSetup().getAwayTeam());
							this_inn.get(this_inn.size() - 1).setBowlingTeamId(this_match.getSetup().getAwayTeamId());
						}
						break;
					case 2:
						if(this_match.getSetup().getAwayTeam() != null) {
							this_inn.get(this_inn.size() - 1).setBatting_team(this_match.getSetup().getAwayTeam());
							this_inn.get(this_inn.size() - 1).setBattingTeamId(this_match.getSetup().getAwayTeamId());
							this_inn.get(this_inn.size() - 1).setBowling_team(this_match.getSetup().getHomeTeam());
							this_inn.get(this_inn.size() - 1).setBowlingTeamId(this_match.getSetup().getHomeTeamId());
						}
						break;
					}
					for (BattingCard bc : this_battingcard) {
						this_inn.get(this_inn.size() - 1).setTotalFours(this_inn.get(this_inn.size() - 1).getTotalFours() + bc.getFours());
						this_inn.get(this_inn.size() - 1).setTotalSixes(this_inn.get(this_inn.size() - 1).getTotalSixes() + bc.getSixes());
					}
					this_inn.get(this_inn.size() - 1).setBattingCard(this_battingcard);
					this_inn.get(this_inn.size() - 1).setFallsOfWickets(this_FoWs);
				}
				
				List<Player> home_squad = new ArrayList<Player>();
				List<Player> away_squad = new ArrayList<Player>();
				for(int i=0; i<this_inn.size(); i++) {
					this_inn.get(i).setInningStatus(CricketUtil.PAUSE);
					if(i == this_inn.size()-1) {
						this_inn.get(i).setIsCurrentInning(CricketUtil.YES);
					} else {
						this_inn.get(i).setIsCurrentInning(CricketUtil.NO);
					}
					switch (i) {
					case 0:
						home_squad = new ArrayList<Player>();
						for (BattingCard bc : this_inn.get(i).getBattingCard()) {
							home_squad.add(bc.getPlayer());
						}
						if(this_inn.size() > 1) {
							away_squad = new ArrayList<Player>();
							for (BattingCard bc : this_inn.get(i+1).getBattingCard()) {
								away_squad.add(bc.getPlayer());
							}
						}
						break;
					}
				}
				if(home_squad.size() > 0) {
					this_match.getSetup().setHomeSquad(home_squad);
				}
				if(away_squad.size() > 0) {
					this_match.getSetup().setAwaySquad(away_squad);
				}
				for (WebElement bowling_card_table : driver.findElements(By.tagName("table"))) {
					
					data_to_process = "";
					System.out.println("Table head:row:th found = " + bowling_card_table.findElements(By.xpath("./thead/tr/th")).size());
					if(bowling_card_table.findElements(By.xpath("./thead/tr/th")).size() > 0) {
						for (WebElement table_header : bowling_card_table.findElements(By.xpath("./thead/tr/th"))) {
							System.out.println("table_header.getText() = " + table_header.getText());
							data_to_process = table_header.getText().toUpperCase().trim();
							break;
						}
					}
					if(data_to_process.equalsIgnoreCase("BOWLING")) {
						
						this_bowlingcard = new ArrayList<BowlingCard>();
						
						for(WebElement row : bowling_card_table.findElements(By.xpath("./tbody/tr")))
						{
							System.out.println("row.getAttribute(class) = " + row.getAttribute("class"));
							if(!row.getAttribute("class").toLowerCase().contains("ds-hidden")) {
								column_data_count = 0;
								for(WebElement column : row.findElements(By.tagName("td")))
								{
									if(!column.findElements(By.tagName("a")).isEmpty())  
									{
										if(column.findElement(By.tagName("a")).getAttribute("href").contains("/cricketers/")
											&& column.findElement(By.tagName("a")).getAttribute("href").contains("-")) {
											
											System.out.println("Bowling anchor HREF = " + column.findElement(By.tagName("a")).getAttribute("href"));
											System.out.println("Bowling player full name = " + column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
											this_bowlingcard.add(new BowlingCard(Integer.valueOf(column.findElement(
													By.tagName("a")).getAttribute("href").split("-")[
													column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]), 
													this_bowlingcard.size() + 1, CricketUtil.LAST, 0));
											
											this_player = new Player();
											this_player.setPlayerId(Integer.valueOf(column.findElement(
													By.tagName("a")).getAttribute("href").split("-")[
													column.findElement(By.tagName("a")).getAttribute("href").split("-").length-1]));
											this_player.setFull_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
											if(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().contains(" ")) {
												this_player.setFirstname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[0]);
												this_player.setSurname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[1]);
												this_player.setTicker_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText().split(" ")[1]);
											}else {
												this_player.setFirstname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
												this_player.setSurname(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
												this_player.setTicker_name(column.findElement(By.tagName("a")).findElement(By.tagName("span")).getText());
											}
											
											this_bowlingcard.get(this_bowlingcard.size()-1).setPlayer(this_player);
											
											column_data_count = 0;
											
										}
									} else {
										
										column_data_count++;
										data_to_process = column.getText().trim();
										if(data_to_process.isEmpty()) {
											if(column_data_count == 5) {
												data_to_process = "0.0";
											} else {
												data_to_process = "0";
											}
										}
										System.out.println("Bowl column_data_count = " + column_data_count + " -> " + data_to_process);
										switch (column_data_count) {
										case 1:
											if(column.getText().contains(".")) {
												this_bowlingcard.get(this_bowlingcard.size()-1).setOvers(
													Integer.valueOf(data_to_process.substring(0,data_to_process.indexOf("."))));
												this_bowlingcard.get(this_bowlingcard.size()-1).setBalls((
													Integer.valueOf(data_to_process.substring(data_to_process.indexOf(".")+1))));
											} else {
												this_bowlingcard.get(this_bowlingcard.size()-1).setOvers(Integer.valueOf(data_to_process));
											}
											break;
										case 2: 
											this_bowlingcard.get(this_bowlingcard.size()-1).setMaidens(Integer.valueOf(data_to_process));
											break;
										case 3:
											this_bowlingcard.get(this_bowlingcard.size()-1).setRuns(Integer.valueOf(data_to_process));
											break;
										case 4:
											this_bowlingcard.get(this_bowlingcard.size()-1).setWickets(Integer.valueOf(data_to_process));
											break;
										case 5:
											this_bowlingcard.get(this_bowlingcard.size()-1).setEconomyRate(data_to_process);
											break;
										case 6:
											this_bowlingcard.get(this_bowlingcard.size()-1).setDots(Integer.valueOf(data_to_process));
											break;
										case 9:
											this_bowlingcard.get(this_bowlingcard.size()-1).setWides(Integer.valueOf(data_to_process));
											break;
										case 10:
											this_bowlingcard.get(this_bowlingcard.size()-1).setNoBalls(Integer.valueOf(data_to_process));
											break;	
										}
									}
								}
							}
						}
						
						System.out.println("Bowling card " + this_bowlingcard.toString());
						System.out.println("Inn Size " + this_inn.size());
						this_inn.get(bowling_card_count - 1).setBowlingCard(this_bowlingcard);
						bowling_card_count = bowling_card_count + 1;
						
					} else if (data_to_process.equalsIgnoreCase("PLAYER NAME") || data_to_process.equalsIgnoreCase("TEAM")) {
						break;
					}
				}
				this_match.getMatch().setInning(this_inn);
				this_match.getMatch().setMatchFileName(valueToProcess.split("/")[valueToProcess.split("/").length-2] + "." + CricketUtil.JSON);
				this_match.getMatch().setMatchStatus("");
				break;
			}
			break;
		}
		readOrSaveMatchFile("WRITE", CricketUtil.SETUP, this_match);
		readOrSaveMatchFile("WRITE", CricketUtil.MATCH, this_match);
		
		return this_match;
	}
	public static List<ArchiveData> getStatsFromWebsite(WebDriver driver, String whatToProcess, 
			String broadcaster, String valueToProcess, CricketService cricketService)
	{
		List<ArchiveData> all_stats = new ArrayList<ArchiveData>();
		String this_url = "",this_team_id ="";
		List<String> this_teams = new ArrayList<String>();
		int teams_found_count = 0;
		
		switch (broadcaster.toUpperCase()) {
		case CricketUtil.CRIC_INFO:
			
			switch (whatToProcess) {
			case "GET-SERIES-MATCHES-DATA":
				
				driver.get(valueToProcess);
				System.out.println("valueToProcess = " + valueToProcess);
				System.out.println("this_team_id = " + this_team_id);
				this_team_id = valueToProcess.split("/")[valueToProcess.split("/").length-2];
				System.out.println("1st this_team_id = " + this_team_id);
				this_team_id = this_team_id.split("-")[this_team_id.split("-").length-1];
				System.out.println("2nd this_team_id = " + this_team_id);
				
				for (Team team : cricketService.getTeams()) {
					System.out.println("team = " + team.getTeamName1());
					if(valueToProcess.toLowerCase().contains(
							team.getTeamName1().replace(" ", "-").toLowerCase())) {
						this_teams.add(team.getTeamName1().replace(" ", "-").toLowerCase());
						System.out.println("this_teams = " + this_teams.get(this_teams.size()-1));
					}
				}
				for(WebElement anchor : driver.findElements(By.tagName("a")))
				{
					teams_found_count = 0;
					System.out.println("anchor.getAttribute(href) = " + anchor.getAttribute("href"));
					if(anchor.getAttribute("href").toLowerCase().contains("/full-scorecard")
						|| anchor.getAttribute("href").toLowerCase().contains("/live-cricket-score")) {
						
						if(this_teams.size() > 0) {
							for (String team_str : this_teams) {
								if(anchor.getAttribute("href").toLowerCase().contains(team_str.toLowerCase())) {
									teams_found_count++;
								}
							}
						} else if(anchor.getAttribute("href").toLowerCase().contains("/series/")
							&& anchor.getAttribute("href").toLowerCase().contains(this_team_id + "/")) {
							teams_found_count = 2;
						}
						System.out.println("teams_found_count = " + teams_found_count);
						if(teams_found_count == 2) {
							this_url = anchor.getAttribute("href").split("/")[anchor.getAttribute("href").split("/").length - 2];
							System.out.println("this_url = " + this_url);
							if(this_url.contains("-")) {
								System.out.println("match id = " + Long.valueOf(this_url.split("-")[this_url.split("-").length-1]));
								all_stats.add(new ArchiveData(Long.valueOf(this_url.split("-")[this_url.split("-").length-1]), 
									this_url, anchor.getAttribute("href")));
							}
						}
					}
				}
				break;
				
			case "GET-SEASON-SERIES-DATA":
				
				driver.get("https://www.espncricinfo.com/ci/engine/series/index.html?season=" 
					+ valueToProcess + ";view=season");
				for(WebElement section : driver.findElements(By.className("series-summary-block")))
				{
					all_stats.add(new ArchiveData(Long.valueOf(section.getAttribute("data-series-id")), 
						section.findElement(By.tagName("a")).getText(), 
						section.findElement(By.tagName("a")).getAttribute("href")));
				}
				break;
				
			case "GET-ALL-SEASONS":
				
				driver.get("https://www.espncricinfo.com/ci/engine/series/index.html");
				for(WebElement section : driver.findElements(By.className("season-links")))
				{
					for(WebElement anchor : section.findElements(By.tagName("a")))
					{
						all_stats.add(new ArchiveData(0,anchor.getText(), anchor.getAttribute("href")));
					}
				}
				break;
				
			}
			break;
		}
		return all_stats;
	}
	
	public static MatchAllData readOrSaveMatchFile(String whatToProcess, String whichFileToProcess, MatchAllData match) 
		throws JAXBException, StreamWriteException, DatabindException, IOException, URISyntaxException
	{
		switch (whatToProcess) {
		case CricketUtil.WRITE:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.SETUP_DIRECTORY 
					+ match.getMatch().getMatchFileName()), 
					objectWriter.writeValueAsString(match.getSetup()).getBytes());			
			}
			if(match.getSetup().getMatchDataUpdate().equalsIgnoreCase(CricketUtil.START)) {
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.EVENT_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getEventFile()).getBytes());
				}
				if(whichFileToProcess.toUpperCase().contains(CricketUtil.MATCH)) {
					Files.write(Paths.get(CricketUtil.CRICKET_DIRECTORY + CricketUtil.MATCHES_DIRECTORY 
						+ match.getMatch().getMatchFileName()), 
						objectWriter.writeValueAsString(match.getMatch()).getBytes());
				}
			}
			break;
		case CricketUtil.READ:
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.SETUP)) {
				if(new File(CricketUtil.CRICKET_DIRECTORY 
					+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName().toUpperCase().replace(
					".XML", ".JSON")).exists() == true) {
					match.setSetup(new ObjectMapper().readValue(new File(CricketUtil.CRICKET_DIRECTORY 
							+ CricketUtil.SETUP_DIRECTORY + match.getMatch().getMatchFileName()), Setup.class));
				}
			}
			if(whichFileToProcess.toUpperCase().contains(CricketUtil.EVENT)) {
				if(new File(CricketUtil.CRICKET_DIRECTORY 
						+ CricketUtil.EVENT_DIRECTORY + match.getMatch().getMatchFileName().toUpperCase().replace(
						".XML", ".JSON")).exists() == true) {
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
	public static String deletePreview() throws IOException
    {
		if(new File(CricketUtil.PREVIEW).exists()) {
			Files.delete(Paths.get(CricketUtil.PREVIEW));
		}
        return "";
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
	public static String checkImpactPlayer(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBatterNo() && events.get(i).getSubstitutionMade() != null) || 
						(player_id == events.get(i).getEventBatterNo() && events.get(i).getEventType().equalsIgnoreCase("LOG_OVERWRITE_SUBSTITUTION"))) {
					return "YES";
				}
			}
		}
		return "";
	}
	public static String checkImpactPlayerBowler(List<Event> events,int inning_number,int player_id) {
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				if((player_id == events.get(i).getEventBowlerNo() && events.get(i).getSubstitutionMade() != null) || 
						(player_id == events.get(i).getEventBowlerNo() && events.get(i).getEventType().equalsIgnoreCase("LOG_OVERWRITE_SUBSTITUTION"))) {
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
	
	public static String getInteractive(MatchAllData match) throws IOException {

		if(match.getSetup() == null || (match.getSetup().getGenerateInteractiveFile() == null 
			|| match.getSetup().getGenerateInteractiveFile().equalsIgnoreCase(CricketUtil.NO))) {
			return "";
		}
		if(match.getEventFile() == null || (match.getEventFile().getEvents() == null 
			|| match.getEventFile().getEvents().size() <= 0)) {
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
		txt = addSubString(txt,"# Match between " + match.getSetup().getHomeTeam().getTeamName1() + " and " + match.getSetup().getAwayTeam().getTeamName1() + "\n\n",0);
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
		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			max_inn = 4;
		}
		
	    for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++)
	    {
		  if(match.getEventFile().getEvents().get(i).getEventInningNumber() >= 1 && match.getEventFile().getEvents().get(i).getEventInningNumber() <= max_inn) {
			if(match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER") || 
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER")||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN) ||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
				
			}else {
				line_txt = String.format("%-140s", "");
				j = j + 1;
				System.out.println(match.getMatch().getInning());
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
					over_number = String.valueOf(match.getEventFile().getEvents().get(i).getEventOverNo() + 1);
					over_ball = String.valueOf(match.getEventFile().getEvents().get(i).getEventBallNo());
				}else {
					over_number = getOvers(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
					over_ball = getBalls(match.getEventFile().getEvents().get(i).getEventOverNo(), match.getEventFile().getEvents().get(i).getEventBallNo());
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
			}
			
    		
			switch (match.getEventFile().getEvents().get(i).getEventType().toUpperCase()){
		    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		    case CricketUtil.FOUR: case CricketUtil.SIX:
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
		    		if(match.getEventFile().getEvents().get(i).getEventSubExtra() != null && match.getEventFile().getEvents().get(i).getEventSubExtraRuns() > 0) {
		    			if(match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				int runs = (match.getEventFile().getEvents().get(i).getEventRuns() + match.getEventFile().getEvents().get(i).getEventExtraRuns() + 
		    						match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
		    				line_txt = addSubString(line_txt,String.valueOf(runs),74);
	    					
		    				this_ball_data = String.valueOf(runs);
		    			}
		    		}
		    		if(this_ball_data.isEmpty()) {
		    			if(match.getEventFile().getEvents().get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
		    				if(match.getEventFile().getEvents().get(i).getEventRuns()>0) {
		    					this_over_run =  match.getEventFile().getEvents().get(i).getEventRuns() + this_over;
		    					line_txt = addSubString(line_txt,"0",74);
	    						
			    				this_ball_data = match.getEventFile().getEvents().get(i).getEventExtra() + match.getEventFile().getEvents().get(i).getEventRuns();
			    			}else {
			    				this_over_run = this_over;
			    				line_txt = addSubString(line_txt,"0",74);
		    					
			    				this_ball_data = match.getEventFile().getEvents().get(i).getEventExtra();
			    			}
		    			}else {
		    				if(match.getEventFile().getEvents().get(i).getEventRuns()>0) {
		    					line_txt = addSubString(line_txt,String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns()),74);
			    				this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventRuns());
			    			}
		    			}
		    		}else {
		    			this_over_run = this_over;
		    			line_txt = addSubString(line_txt,"0",74);
		    			this_ball_data = this_ball_data + match.getEventFile().getEvents().get(i).getEventExtra();
		    		}
		    	}
			    if (match.getEventFile().getEvents().get(i).getEventSubExtra() != null && match.getEventFile().getEvents().get(i).getEventSubExtraRuns() > 0){
			    	if(!match.getEventFile().getEvents().get(i).getEventSubExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
			    		if (this_ball_data.isEmpty()) {
			    			this_over_run = match.getEventFile().getEvents().get(i).getEventSubExtra().charAt(0) + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns());
			    			line_txt = addSubString(line_txt,"0",74);
				          this_ball_data = String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra();
			        	
				        } else {
				        	this_over_run = this_over_run + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra().charAt(0);
				        	line_txt = addSubString(line_txt,"0",74);
				          this_ball_data = this_ball_data + "+" + String.valueOf(match.getEventFile().getEvents().get(i).getEventSubExtraRuns()) + match.getEventFile().getEvents().get(i).getEventSubExtra();
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
			
			if(match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER")  || 
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER")||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
					match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
				
			}else {
				
					for(int k = i+1; k < match.getEventFile().getEvents().size(); k++){
						switch (match.getEventFile().getEvents().get(k).getEventType().toUpperCase()){
						case CricketUtil.WAGON:
							if(match.getEventFile().getEvents().get(i).getEventInningNumber() == match.getEventFile().getEvents().get(k).getEventInningNumber()) {
								if(match.getEventFile().getEvents().get(i).getEventOverNo() == match.getEventFile().getEvents().get(k).getEventOverNo()) {
									if(match.getEventFile().getEvents().get(i).getEventBallNo() == match.getEventFile().getEvents().get(k).getEventBallNo()) {
										wagonX = String.valueOf(match.getEventFile().getEvents().get(k).getEventDescription()).split(",")[0];
										wagonY = String.valueOf(match.getEventFile().getEvents().get(k).getEventDescription()).split(",")[1];
									}
								}
							}
							
							break;
						case CricketUtil.SHOT:
							if(match.getEventFile().getEvents().get(i).getEventInningNumber() == match.getEventFile().getEvents().get(k).getEventInningNumber()) {
								if(match.getEventFile().getEvents().get(i).getEventOverNo() == match.getEventFile().getEvents().get(k).getEventOverNo()) {
									if(match.getEventFile().getEvents().get(i).getEventBallNo() == match.getEventFile().getEvents().get(k).getEventBallNo()) {
										if (match.getEventFile().getEvents().get(k).getEventDescription().contains("no_shot")) {
											shot = "N";
										}else if(match.getEventFile().getEvents().get(k).getEventDescription().contains("defence") || match.getEventFile().getEvents().get(k).getEventDescription().contains("nudge") ||
												match.getEventFile().getEvents().get(k).getEventDescription().contains("off_drive") || match.getEventFile().getEvents().get(k).getEventDescription().contains("on_drive") ||
												match.getEventFile().getEvents().get(k).getEventDescription().contains("straight_drive")) {
											 shot = "E";
										}else if(match.getEventFile().getEvents().get(k).getEventDescription().contains("front") || match.getEventFile().getEvents().get(k).getEventDescription().contains("back")) {
											 shot = "P";
										}else {
											shot = "M";
										}
										
										if (match.getEventFile().getEvents().get(k).getEventDescription().contains("no_shot")) {
											shot = shot + "N";
										}else if(match.getEventFile().getEvents().get(k).getEventDescription().contains("defence")) {
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
				 
				if(match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.LOG_WICKET)) {
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
		if(match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("END_OVER")  || 
			match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase("CHANGE_BOWLER") ||
			match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.NEW_BATSMAN)||
			match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.WAGON) ||
			match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SHOT) ||
			match.getEventFile().getEvents().get(i).getEventType().toUpperCase().equalsIgnoreCase(CricketUtil.SWAP_BATSMAN)) {
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
	public static void DoadWriteCommandToSelectedViz(int SelectedViz, String SendTextIn, List<PrintWriter> print_writers) 
	{
		if(SelectedViz >= 0 && SelectedViz < print_writers.size()) {
			print_writers.get(SelectedViz).println(SendTextIn);
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
		
		try {
			if(config.getQtIpAddress() != null && !config.getQtIpAddress().isEmpty()) {
				print_writer.add(new PrintWriter(new Socket(config.getQtIpAddress(), 
					config.getQtPortNumber()).getOutputStream(), true));
			}
		} catch (ConnectException e) {
			System.out.println("Unable to create print writer for QT");
		}
		
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
	
	public static String getBowlerType(String BowlerType) {
		switch(BowlerType.toUpperCase()) {
		case "RF": case "RFM": case "RMF": case "RM": case "RSM": case "LF": case "LFM": case "LMF": case "LM":
			return "PACE";
		case "ROB": case "RLB": case "RLG": case "WSR": case "LSL": case "WSL": case "LCH":  case "LSO":
			return "SPIN";
		}
		return "";
	}
	
	public static void getBatsmanSRAgainstPaceAndSpin(int PlayerId,int Value,CricketService cricketService,List<Tournament> tournament_stats,MatchAllData match) {
		for (int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
			if(PlayerId == match.getEventFile().getEvents().get(i).getEventBatterNo()) {
				switch (match.getEventFile().getEvents().get(i).getEventType()) {
				case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR: 
				case CricketUtil.FIVE: case CricketUtil.SIX: case CricketUtil.LOG_WICKET:
					
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
	public static String gettournamentFoursAndSixes(List<MatchAllData> tournament_matches,MatchAllData currentMatch) 
	{
		int Four = 0, Six = 0;
		for(MatchAllData match : tournament_matches) {
			if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
				for(Inning inn : match.getMatch().getInning()) {
					Four = Four + inn.getTotalFours();
					Six = Six + inn.getTotalSixes();
				}
			}else {
				for(Inning inn : currentMatch.getMatch().getInning()) {
					Four = Four + inn.getTotalFours();
					Six = Six + inn.getTotalSixes();
				}
			}
		}
		System.out.println(Four + "," + Six);
		return Four + "," + Six;
	}

	public static Statistics updateTournamentDataWithStats(Statistics stats,List<MatchAllData> tournament_matches,MatchAllData currentMatch) throws JsonMappingException, JsonProcessingException, InterruptedException 
	{
		boolean player_found = false,impact_player_found=false;
		
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		for(MatchAllData match : tournament_matches) {
			//System.out.println(match.getMatch().getMatchFileName());
			if(!match.getMatch().getMatchFileName().equalsIgnoreCase(currentMatch.getMatch().getMatchFileName())) {
				if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getSetup().getMatchType())) {
					TimeUnit.MILLISECONDS.sleep(500);
					for(Inning inn : match.getMatch().getInning()) {
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
									stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()*Integer.valueOf(match.getSetup().getBallsPerOver()) + boc.getBalls()));
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
					for(Player hs : match.getSetup().getHomeSubstitutes()) {
						if(hs.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
							if(hs.getPlayerId() == stat.getPlayer_id()) {
								impact_player_found = true;
							}
						}
					}
					for(Player as : match.getSetup().getAwaySubstitutes()) {
						if(as.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
							if(as.getPlayerId() == stat.getPlayer_id()) {
								impact_player_found = true;
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
	
	public static Statistics updateStatisticsWithMatchData(Statistics stats, MatchAllData match) throws JsonMappingException, JsonProcessingException
	{
		boolean player_found = false,impact_player_found=false;
		
		Statistics statsdata = stats;
		ObjectMapper objectMapper = new ObjectMapper();    
		Statistics stat = objectMapper.readValue(objectMapper.writeValueAsString(statsdata), Statistics.class);
		
		if(stat.getStats_type().getStats_short_name().equalsIgnoreCase(match.getSetup().getMatchType())) {
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(0).getTotalFours());
			stat.setTournament_fours(stat.getTournament_fours() + match.getMatch().getInning().get(1).getTotalFours());
			for(Inning inn : match.getMatch().getInning()) {
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
							stat.setBalls_bowled(stat.getBalls_bowled() + (boc.getOvers()* Integer.valueOf(match.getSetup().getBallsPerOver()) + boc.getBalls()));
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
			for(Player hs : match.getSetup().getHomeSubstitutes()) {
				if(hs.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
					if(hs.getPlayerId() == stat.getPlayer_id()) {
						impact_player_found = true;
					}
				}
			}
			for(Player as : match.getSetup().getAwaySubstitutes()) {
				if(as.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
					if(as.getPlayerId() == stat.getPlayer_id()) {
						impact_player_found = true;
					}
				}
			}
			if(stat.getPlayer_id() == 76) {
				System.out.println("namebefore = " + stat.getPlayer_id() + " matches = " + stat.getMatches());
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
		if(stat.getPlayer_id() == 76) {
			System.out.println("namecurrent = " + stat.getPlayer_id() + " matches = " + stat.getMatches());
		}
		return stat;
	}
	
	public static List<Tournament> extractTournamentStats(String typeOfExtraction,boolean ShowStrikeRate, List<MatchAllData> tournament_matches, 
			CricketService cricketService,MatchAllData currentMatch, List<Tournament> past_tournament_stat) throws IOException 
	{		
		int playerId = -1;
		String text_to_return = "";
		List<Tournament> tournament_stats = new ArrayList<Tournament>();
		ArrayList<String> ImpactData = new ArrayList<String>();
		boolean has_match_started = false,is_player_found = false,fielder_found = false;
		
		try (BufferedReader br = new BufferedReader(new FileReader(CricketUtil.CRICKET_DIRECTORY + "ImpactPlayer.txt"))) {
			while((text_to_return = br.readLine()) != null) {
				if(text_to_return.contains("|")) {
					
				}else {
					ImpactData.add(text_to_return);
				}
			}
		}
		
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

										tournament_stats.get(playerId).getBowler_best_Stats().add(new BestStats(
												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
												inn.getBatting_team(),mtch.getMatch().getMatchFileName().replace(".json", "") ,boc.getPlayer(),""));
										
									}else {
										tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
												boc.getDots(),null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										
										tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(
												boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
												inn.getBatting_team(),mtch.getMatch().getMatchFileName().replace(".json", "") , boc.getPlayer(),""));
																				
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
										
										int fifty=0,hundreds=0;
										if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
											fifty = fifty + 1;
										}else if(bc.getRuns()>= 100) {
											hundreds = hundreds + 1;
										}
										
										tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + fifty);
										tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + hundreds);
										
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
													bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(), 
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.NOT_OUT));
											
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
													bc.getPlayerId(), (bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.OUT));
											
										}
										else {
											tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
													bc.getPlayerId(), bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(),
													mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.STILL_TO_BAT));
										}
										
										
										//getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, tournament_stats, mtch);
										
									}else {
										int fifty=0,hundreds=0;
										if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
											fifty = fifty + 1;
										}else if(bc.getRuns()>= 100) {
											hundreds = hundreds + 1;
										}
										
										tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes() , fifty, hundreds, 0, 0, 0, bc.getBalls(), 
												0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
										//tournament_stats.get(tournament_stats.size() - 1).setMatches((tournament_stats.get(tournament_stats.size() - 1).getMatches() + 1));
										if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
													bc.getBalls(),inn.getBowling_team(),mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.NOT_OUT));
										}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
													bc.getBalls(),inn.getBowling_team(),mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.OUT));
										}
										else {
											tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
													bc.getBalls(),inn.getBowling_team(),mtch.getMatch().getMatchFileName().replace(".json", "") ,bc.getPlayer(),CricketUtil.STILL_TO_BAT));
										}
										
										//getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), (tournament_stats.size() - 1), cricketService, tournament_stats, mtch);
									}	
								}
							}
							for(Tournament trmnt : tournament_stats) {
								for(Player plyr : mtch.getSetup().getHomeSubstitutes()) {
									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trmnt.getPlayerId()) {
											fielder_found = true;
										}
									}
								}
								
								for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trmnt.getPlayerId()) {
											fielder_found = true;
										}
									}
								}
							}
							
							if(fielder_found == false) {
								for(Player plyr : mtch.getSetup().getHomeSubstitutes()) {
									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 
												0,CricketUtil.STILL_TO_BAT,0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>()));
//										fielder_found = true;
									}
								}
								
								for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										tournament_stats.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 
												0,CricketUtil.STILL_TO_BAT,0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									}
								}
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
											if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
												if(plyr.getPlayerId() == trmnt.getPlayerId()) {
													is_player_found = true;
													trmnt.setMatches(trmnt.getMatches() + 1);
												}
											}
										}
										
										if(is_player_found == false) {
											for(Player plyr : mtch.getSetup().getAwaySubstitutes()) {
												if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
													if(plyr.getPlayerId() == trmnt.getPlayerId()) {
														is_player_found = true;
														trmnt.setMatches(trmnt.getMatches() + 1);
													}
												}
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
			
			List<Tournament> past_tournament_stat_clone = past_tournament_stat.stream().map(tourn_stats -> {
				try {
					return tourn_stats.clone();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				return tourn_stats;
			}).collect(Collectors.toList());
			
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

								past_tournament_stat_clone.get(playerId).getBowler_best_Stats().add(new BestStats(
										boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
										inn.getBatting_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
	
							}else {
								past_tournament_stat_clone.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
										boc.getDots(),null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
								
								past_tournament_stat_clone.get(past_tournament_stat_clone.size() - 1).getBowler_best_Stats().add(new BestStats(boc.getPlayerId(), 
										(1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
										inn.getBatting_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
								
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
							
							int fifty=0,hundreds=0;
							if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
								fifty = fifty + 1;
							}else if(bc.getRuns()>= 100) {
								hundreds = hundreds + 1;
							}
							
							past_tournament_stat_clone.get(playerId).setFifty(past_tournament_stat_clone.get(playerId).getFifty() + fifty);
							past_tournament_stat_clone.get(playerId).setHundreds(past_tournament_stat_clone.get(playerId).getHundreds() + hundreds);
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, 
										bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
								
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
										bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
								
							}
							else {
								past_tournament_stat_clone.get(playerId).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), 
										bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
							}
							
							if(ShowStrikeRate == true) {
								getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, past_tournament_stat_clone, currentMatch);
							}
							
							
						}else {
							int fifty=0,hundreds=0;
							if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
								fifty = fifty + 1;
							}else if(bc.getRuns()>= 100) {
								hundreds = hundreds + 1;
							}
							
							past_tournament_stat_clone.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), fifty, hundreds, 0, 0, 0, bc.getBalls(), 
									0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(),new ArrayList<BestStats>()));
							
							if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.NOT_OUT));
							}else if(bc.getStatus().equalsIgnoreCase(CricketUtil.OUT)) {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.OUT));
							}
							else {
								past_tournament_stat_clone.get(past_tournament_stat_clone.size()-1).getBatsman_best_Stats().add(new BestStats(bc.getPlayerId(), 
										(bc.getRuns() * 2), bc.getBalls(), inn.getBowling_team(),currentMatch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),CricketUtil.STILL_TO_BAT));
							}
							
							if(ShowStrikeRate == true) {
								getBatsmanSRAgainstPaceAndSpin(bc.getPlayerId(), playerId, cricketService, past_tournament_stat_clone, currentMatch);
							}
						}	
					}
					
					for(Tournament trmnt : past_tournament_stat_clone) {
						for(Player plyr : currentMatch.getSetup().getHomeSubstitutes()) {
							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									fielder_found = true;
								}
							}
						}
						
						for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								if(plyr.getPlayerId() == trmnt.getPlayerId()) {
									fielder_found = true;
								}
							}
						}
					}
					
					if(fielder_found == false) {
						for(Player plyr : currentMatch.getSetup().getHomeSubstitutes()) {
							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 
										0,CricketUtil.STILL_TO_BAT,0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>()));
								//fielder_found = true;
							}
						}
						
						for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
							if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
								past_tournament_stat_clone.add(new Tournament(plyr.getPlayerId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 
										0,CricketUtil.STILL_TO_BAT,0,0,0,0, plyr, new ArrayList<BestStats>(), new ArrayList<BestStats>()));
							}
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
									if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
										if(plyr.getPlayerId() == trment.getPlayerId()) {
											is_player_found = true;
											trment.setMatches(trment.getMatches() + 1);
										}
									}
								}
								
								if(is_player_found == false) {
									for(Player plyr : currentMatch.getSetup().getAwaySubstitutes()) {
										if(plyr.getImpactPlayer().equalsIgnoreCase(CricketUtil.YES)) {
											if(plyr.getPlayerId() == trment.getPlayerId()) {
												is_player_found = true;
												trment.setMatches(trment.getMatches() + 1);
											}
										}
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
	
	public static String generateMatchResult(MatchAllData match, String teamNameType)
	{
		String resultToShow = "";
		if(match.getMatch().getMatchResult() != null) {
			if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)
					|| match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
				if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN) 
						&& !match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) { // For limited over match use match tied
					resultToShow = CricketUtil.MATCH.toLowerCase() + " " + CricketUtil.TIED.toLowerCase();
				} else {
					resultToShow = CricketUtil.MATCH.toLowerCase();
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.DRAWN)) {
						resultToShow = resultToShow + " " + CricketUtil.DRAWN.toLowerCase();
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.ABANDONED)) {
						resultToShow = resultToShow + " " + CricketUtil.ABANDONED.toLowerCase();
					}
				}
			} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.NO_RESULT)) {
				resultToShow = CricketUtil.NO_RESULT.toLowerCase().replace("_", " ");
			} else {
				if(match.getMatch().getMatchResult().contains(",")) {
					switch (teamNameType) {
					case CricketUtil.SHORT:
						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
							resultToShow = match.getSetup().getHomeTeam().getTeamName3();
						} else {
							resultToShow = match.getSetup().getAwayTeam().getTeamName3();
						}
					    break;
					default:
						if(Integer.valueOf(match.getMatch().getMatchResult().split(",")[0]) == match.getSetup().getHomeTeamId()) {
							resultToShow = match.getSetup().getHomeTeam().getTeamName1();
						} else {
							resultToShow = match.getSetup().getAwayTeam().getTeamName1();
						}
					    break;
					}
					if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.SUPER_OVER)) {
						resultToShow = resultToShow + " win by super over";
					} else if(match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.INNING) 
							&& match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = resultToShow + " win by an inning and " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
							+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.RUN)) {
						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
							+ " run" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
					} else if (match.getMatch().getMatchResult().toUpperCase().contains(CricketUtil.WICKET)) {
						resultToShow = resultToShow + " win by " + Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]) 
							+ " wicket" + Plural(Integer.valueOf(match.getMatch().getMatchResult().split(",")[1]));
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
	    	if(bc2.getFours() == bc1.getFours()) {
	    		return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    	}else {
	    		return Integer.compare(bc2.getBatsmanFoursSortData(), bc1.getBatsmanFoursSortData());
	    	}
	    }
	}
	
	public static class BatsmanSixesComparator implements Comparator<Tournament> {
	    @Override
	    public int compare(Tournament bc1, Tournament bc2) {
	    	if(bc2.getSixes() == bc1.getSixes()) {
	    		return Integer.compare(bc2.getBatsmanStrikeRateSortData(), bc1.getBatsmanStrikeRateSortData());
	    	}else {
	    		return Integer.compare(bc2.getBatsmanSixesSortData(), bc1.getBatsmanSixesSortData());
	    	}
	    }
	}
	
	public static Player populatePlayer(CricketService cricketService, Player player, MatchAllData match)
	{
		Player this_plyr = new Player();
		this_plyr = cricketService.getPlayer(CricketUtil.PLAYER, String.valueOf(player.getPlayerId()));
		if(this_plyr != null) {
			this_plyr.setPlayerPosition(player.getPlayerPosition()); 
			this_plyr.setCaptainWicketKeeper(player.getCaptainWicketKeeper());
			this_plyr.setImpactPlayer(player.getImpactPlayer()); 
			if(match.getSetup().getReadPhotoColumn() != null && match.getSetup().getReadPhotoColumn().equalsIgnoreCase(CricketUtil.NO)) {
				this_plyr.setPhoto("");
			}
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
				//System.out.println("BOWLER_NAME BOWLED = " + bc.getHowOutBowler().getTicker_name());
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
							return "c|" + bc.getHowOutFielder().getTicker_name() + " (SUB)|b|" + bc.getHowOutBowler().getTicker_name();
						} else {
							return "c|" + bc.getHowOutFielder().getTicker_name() + "|b|" + bc.getHowOutBowler().getTicker_name();
						}
					case CricketUtil.RUN_OUT:
						if(bc.getWasHowOutFielderSubstitute() != null && bc.getWasHowOutFielderSubstitute().equalsIgnoreCase(CricketUtil.YES)) {
							return "run out|" + bc.getHowOutFielder().getTicker_name() + " (SUB)"+"| | ";
						} else {
							System.out.println("Hello");
							return "run out|(" + bc.getHowOutFielder().getTicker_name() + ")| | ";
						}
					case CricketUtil.MANKAD:
						return "run out|(" + bc.getHowOutBowler().getTicker_name() + ")| | ";
					}
					break;
				case CricketUtil.BOWLED:
					return " | |b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.STUMPED:
					return "st|" + bc.getHowOutFielder().getTicker_name() + "|b|" + bc.getHowOutBowler().getTicker_name();
				case CricketUtil.LBW:
					return "lbw| |b|" + bc.getHowOutBowler().getTicker_name();
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
									
									int fifty=0,hundreds=0;
									if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
										fifty = fifty + 1;
									}else if(bc.getRuns()>= 100) {
										hundreds = hundreds + 1;
									}
									
									tournament_stats.get(playerId).setFifty(tournament_stats.get(playerId).getFifty() + fifty);
									tournament_stats.get(playerId).setHundreds(tournament_stats.get(playerId).getHundreds() + hundreds);
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
												bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(), inn.getBowling_team(),
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
										
									}else {
										tournament_stats.get(playerId).getBatsman_best_Stats().add(new BestStats(
												bc.getPlayerId(), bc.getRuns() * 2, bc.getBalls(), inn.getBowling_team(),
												mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
									}
									
								}else {
									int fifty=0,hundreds=0;
									if(bc.getRuns()>= 50 && bc.getRuns() < 100) {
										fifty = fifty + 1;
									}else if(bc.getRuns()>= 100) {
										hundreds = hundreds + 1;
									}
									
									tournament_stats.add(new Tournament(bc.getPlayerId(), bc.getRuns(), bc.getFours(), bc.getSixes(), fifty, hundreds, 0, 0, 0, bc.getBalls(), 
											0,bc.getStatus(),0,0,0,0, bc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									
									if(bc.getStatus().equalsIgnoreCase(CricketUtil.NOT_OUT)) {
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
												new BestStats(bc.getPlayerId(), (bc.getRuns() * 2) + 1, bc.getBalls(),
												inn.getBowling_team(),mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
										
									}else {
										tournament_stats.get(tournament_stats.size() - 1).getBatsman_best_Stats().add(
												new BestStats(bc.getPlayerId(), (bc.getRuns() * 2), bc.getBalls(),
												inn.getBowling_team(),mtch.getMatch().getMatchFileName().replace(".json", ""),bc.getPlayer(),""));
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
											inn.getBatting_team(),mtch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
									
								}else {
									
									tournament_stats.add(new Tournament(boc.getPlayerId(), 0, 0, 0, 0, 0, boc.getWickets(), boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 0, 
											boc.getDots(),null,0,0,0,0, boc.getPlayer(), new ArrayList<BestStats>(), new ArrayList<BestStats>()));
									
									tournament_stats.get(tournament_stats.size() - 1).getBowler_best_Stats().add(new BestStats(
											boc.getPlayerId(), (1000 * boc.getWickets()) - boc.getRuns(), 6 * boc.getOvers() + boc.getBalls(), 
											inn.getBatting_team(),mtch.getMatch().getMatchFileName().replace(".json", ""),boc.getPlayer(),""));
																			
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
	
	public static List<String> getPlayerSplit(int inning_number,int playerId ,int splitvalue,int plyr_balls_count, MatchAllData match,List<Event> events) {
		int total_runs = 0, total_balls = 0 ,count_balls=0;
		List<String> Balls = new ArrayList<String>();
		if((events != null) && (events.size() > 0)) {
			for (int i = 0; i <= events.size() - 1; i++) {
				if(events.get(i).getEventInningNumber() == inning_number) {
					if(playerId == events.get(i).getEventBatterNo()) {
						switch (events.get(i).getEventType()) {
						case CricketUtil.DOT: case CricketUtil.ONE: case CricketUtil.TWO: case CricketUtil.THREE: case CricketUtil.FOUR:  case CricketUtil.FIVE:
						case CricketUtil.SIX: case CricketUtil.LOG_WICKET:
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
	    int[] dots = {0, 0}, ones = {0, 0}, twos = {0, 0}, threes = {0, 0}, fours = {0, 0}, fives = {0, 0}, sixes = {0, 0};

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
	                                fours[inn_num - 1]++;
	                                break;
	                            case CricketUtil.FIVE:
	                                fives[inn_num - 1]++;
	                                break;
	                            case CricketUtil.SIX:
	                                sixes[inn_num - 1]++;
	                                break;
	                            case CricketUtil.DOT:
	                            case CricketUtil.LOG_WICKET:
	                                dots[inn_num - 1]++;
	                                break;
	                            case CricketUtil.BYE:
	                            case CricketUtil.LEG_BYE:
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
	            separator + String.valueOf(sixes[0]));

	    return_score_data.add(String.valueOf(dots[1]) + separator + String.valueOf(ones[1]) + separator + String.valueOf(twos[1]) +
	            separator + String.valueOf(threes[1]) + separator + String.valueOf(fours[1]) + separator + String.valueOf(fives[1]) +
	            separator + String.valueOf(sixes[1]));

	    return return_score_data;
	}
	
	public static List<String> getFirstPowerPlayScores(MatchAllData match, List<Integer> inning_numbers, List<Event> event)
    {
	    List<String> powerPlayScores = new ArrayList<>();
	    int[] totalRuns = {0, 0};int[] totalWickets = {0, 0};int[] ballCount = {0, 0};
	    
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= event.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	                if (event.get(i).getEventInningNumber() == inn_num) {
	                	switch(event.get(i).getEventType()) {
							case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1] ++;
								break;										
							}
			                if(ballCount[inn_num-1] >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * 6) && 
			        				ballCount[inn_num-1] < (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * 6)) {
			        			switch (event.get(i).getEventType())
			                    {
			                    	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
			                    	case CricketUtil.FOUR: case CricketUtil.SIX:
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
			        		} else if(ballCount[inn_num-1] >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * 6) && 
			        				ballCount[inn_num-1] == (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * 6)) {
			        			if(!event.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !event.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
			        				
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
			                        	case CricketUtil.FOUR: case CricketUtil.SIX:
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
	    int StartOver=0,EndOver=0;
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= events.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	            	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
	                	StartOver = match.getMatch().getInning().get(inn_num-1).getSecondPowerplayStartOver();
	                	EndOver = match.getMatch().getInning().get(inn_num-1).getSecondPowerplayEndOver();
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
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1]++;
								break;
							}
	                	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	            			if (ballCount[inn_num-1] == 36 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                            
	            				if (ballCount[inn_num-1] == 36 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                int j = i;
	                                while (j < events.size() && ballCount[inn_num-1] == 36) {
	                                    if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                        if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==15)
	                                    	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                    } else {
	                                        break; 
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
	                                        if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==6)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                            break; // Stop the loop if the event is not a wide or no-ball
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
	                                        if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                            break; 
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
	    					case CricketUtil.FOUR: case CricketUtil.SIX:
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
	                            	case CricketUtil.FOUR: case CricketUtil.SIX:
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
	    if ((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
	    	for (int i = 0; i <= events.size() - 1; i++){
	            for (Integer inn_num : inning_numbers) {
	            	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
	                	StartOver = match.getMatch().getInning().get(inn_num-1).getThirdPowerplayStartOver();
	                	EndOver = match.getMatch().getInning().get(inn_num-1).getThirdPowerplayEndOver();
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
							case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
							case CricketUtil.BYE: case CricketUtil.LEG_BYE:
								ballCount[inn_num-1]++;
								break;
							}
	                	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
	            			if (ballCount[inn_num-1] == 90 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                            
	            				if (ballCount[inn_num-1] == 90 && events.get(i - 1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
	                                int j = i;
	                                while (j < events.size() && ballCount[inn_num-1] == 90) {
	                                    if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                        if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==15)
	                                    	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                    } else {
	                                        break; 
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
	                                        if (events.get(j).getEventType().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE) ||events.get(j).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)||events.get(j).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
	                                            if(events.get(j).getEventBallNo()==0 && events.get(j).getEventOverNo()==EndOver)
	                                        	totalRuns[inn_num-1] += events.get(j).getEventRuns()+ events.get(j).getEventSubExtraRuns()+events.get(j).getEventExtraRuns();
	                                        } else {
	                                            break; 
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
                            	case CricketUtil.FOUR: case CricketUtil.SIX:
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
		    powerPlayScores.add(String.valueOf(totalRuns[0]) + "-" + String.valueOf(totalWickets[0]));
		    powerPlayScores.add(String.valueOf(totalRuns[1]) + "-" + String.valueOf(totalWickets[1]));
			return powerPlayScores;
    }
	
	public static String getFirstPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
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
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						ball_count = ball_count + 1;
						break;
					}
            		
            		if(ball_count >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
            				ball_count < (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
            			switch (events.get(i).getEventType())
                        {
                        	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                        	case CricketUtil.FOUR: case CricketUtil.SIX:
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                        	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                        	case CricketUtil.LOG_WICKET:
                                if (events.get(i).getEventRuns() > 0)
                                {
                                    total_run_PP += events.get(i).getEventRuns();
                                }
                                total_wickets_PP += 1;
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
                                break;
                        }
            		} else if(ball_count >= ((match.getMatch().getInning().get(inn_num-1).getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && 
            				ball_count == (match.getMatch().getInning().get(inn_num-1).getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
            				switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                            	case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                            	case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }
                                    total_wickets_PP += 1;
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
                                    break;
                            }
            			}
            		}
                }
            }
        }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
    }
    public static String getSecPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0;
        int StartOver=0,EndOver=0;
        if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
        	StartOver = match.getMatch().getInning().get(inn_num-1).getSecondPowerplayStartOver();
        	EndOver = match.getMatch().getInning().get(inn_num-1).getSecondPowerplayEndOver();
        }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
        	StartOver = 7;
        	EndOver = 15;
        }
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
            {
        		if(events.get(i).getEventInningNumber() == inn_num)
                {
        			switch(events.get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
						ball_count = ball_count + 1;
						break;
					}
        			
        			if(ball_count == 37) {
    					if(events.get(i+1).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
    						ball_count = 36;
    					}
    				}
        			
        			if(ball_count > ((StartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count < (EndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
        				if(ball_count == 37) {
        					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
        						total_run_PP += events.get(i-1).getEventRuns();
        					}
        				}
        				
        				//System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
            			switch (events.get(i).getEventType())
                        {
                        case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    					case CricketUtil.FOUR: case CricketUtil.SIX:
                                total_run_PP += events.get(i).getEventRuns();
                                break;

                            case CricketUtil.WIDE:
                            	if(ball_count == 90 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
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
                                }
                                total_wickets_PP += 1;
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
                                break;
                        }
            		} else if(ball_count > ((StartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count == (EndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
            			if(!events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER) || !events.get(i-2).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
            				switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

        						case CricketUtil.WIDE: 
        							if(ball_count == 90 && events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.CHANGE_BOWLER)) {
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
                                    }
                                    total_wickets_PP += 1;
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
                                    break;
                            }
            			}
            		}  
                }
            }
        }
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
    }
    public static String getThirdPowerPlayScore(MatchAllData match, int inn_num, List<Event> events)
    {

        int total_run_PP = 0, total_wickets_PP = 0,ball_count = 0;
        int StartOver=0,EndOver=0;
        if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.OD) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI)) {
        	StartOver = match.getMatch().getInning().get(inn_num-1).getThirdPowerplayStartOver();
        	EndOver = match.getMatch().getInning().get(inn_num-1).getThirdPowerplayEndOver();
        }else if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.DT20) || match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.IT20)){
        	StartOver = 16;
        	EndOver = 20;
        }
        if((events != null) && (events.size() > 0)) 
        {
        	for (int i = 0; i <= events.size() - 1; i++)
                {
                	if(events.get(i).getEventInningNumber() == inn_num)
                    {
                		switch(events.get(i).getEventType()) {
    					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
    					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET: //case CricketUtil.LOG_ANY_BALL:
    					case CricketUtil.BYE: case CricketUtil.LEG_BYE:
    						ball_count = ball_count + 1;
    						break;
    					}
                		
                		if(ball_count > ((StartOver - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count <= (EndOver * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
                			if(ball_count == 91 || ball_count == 241) {
            					if(events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.WIDE) || events.get(i-1).getEventType().equalsIgnoreCase(CricketUtil.NO_BALL)) {
            						total_run_PP += events.get(i-1).getEventRuns();
            					}
            				}
                			//System.out.println("inning = " + events.get(i).getEventInningNumber() + "   event = " + events.get(i).getEventType());
                			switch (events.get(i).getEventType())
                            {
                            	case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
                            	case CricketUtil.FOUR: case CricketUtil.SIX:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
                                    total_run_PP += events.get(i).getEventRuns();
                                    break;

                                case CricketUtil.LOG_WICKET:
                                    if (events.get(i).getEventRuns() > 0)
                                    {
                                        total_run_PP += events.get(i).getEventRuns();
                                    }
                                    total_wickets_PP += 1;
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
                                    break;
                            }
                		}
                    }
                }
            }
        //System.out.println(String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP));
        return String.valueOf(total_run_PP) + "-" + String.valueOf(total_wickets_PP);
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

	public static String getbowlingstyle(String bowlingType) throws InterruptedException {
		
		String text="";
		
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
		
		switch (bowlingType.substring(1).trim()) {
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

	public static String processPowerPlay(String powerplay_return_type,MatchAllData match)
	{
		String return_pp_txt = "";
		int BallsBowledInInnings = 0;
	    
		for(Inning inn : match.getMatch().getInning()) {
			if(inn.getIsCurrentInning().equalsIgnoreCase(CricketUtil.YES)) {
				BallsBowledInInnings = inn.getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver()) + inn.getTotalBalls();
			    if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.ODI) || match.getSetup().getMatchType().equalsIgnoreCase("OD")) {
			    	
			    	if(BallsBowledInInnings >= ((inn.getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver()) ) && BallsBowledInInnings < (inn.getFirstPowerplayEndOver()* Integer.valueOf(match.getSetup().getBallsPerOver()))) {
				    	return_pp_txt = CricketUtil.ONE;
				    }else if(BallsBowledInInnings >= ((inn.getSecondPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && BallsBowledInInnings < (inn.getSecondPowerplayEndOver()* Integer.valueOf(match.getSetup().getBallsPerOver())) ) {
				    	return_pp_txt = CricketUtil.TWO;
				    }else if(BallsBowledInInnings >= ((inn.getThirdPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && BallsBowledInInnings < (inn.getThirdPowerplayEndOver()* Integer.valueOf(match.getSetup().getBallsPerOver()))) {
				    	return_pp_txt = CricketUtil.THREE;
				    }
			    }else {
			    	if(BallsBowledInInnings >= ((inn.getFirstPowerplayStartOver() - 1) * Integer.valueOf(match.getSetup().getBallsPerOver()) ) && BallsBowledInInnings < (inn.getFirstPowerplayEndOver()* Integer.valueOf(match.getSetup().getBallsPerOver()))) {
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
	  	        	count_lb = 0;
	  	            //exitLoop = true;
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
	
	public static String getlastthirtyballsdata(MatchAllData match,String separator,List<Event> events,int number_of_events) {
		
		int total_runs = 0, total_wickets = 0,total_fours=0,total_sixes=0,ball_count = 0;
		if ((events != null) && (events.size() > 0)) {
			for (int i = events.size() - 1; i >= 0; i--) {
				switch(events.get(i).getEventType()) {
				case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
		        case CricketUtil.FOUR: case CricketUtil.SIX:
		        	ball_count = ball_count + 1;
		        	total_runs += events.get(i).getEventRuns();
		        	if(events.get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)) {
		        		total_fours = total_fours + 1;
		        	}else if(events.get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
		        		total_sixes = total_sixes + 1;
		        	}
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
		    			if(events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.WIDE)) {
		    				this_ball_data = events.get(i).getEventExtra();
		    			}
		    			else if(events.get(i).getEventExtra().equalsIgnoreCase(CricketUtil.NO_BALL)) {
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

	public static List<OverByOverData> getOverByOverData(MatchAllData match, int inn_num , String type,List<Event> events) 
	{
		List<OverByOverData> over_by_over_data = new ArrayList<OverByOverData>();
		
		int total_runs = 0, total_wickets = 0;
		
		if ((events != null) && (events.size() > 0)) {
			  for (int i = 0; i <=events.size()-1; i++) {
				  if(events.get(i).getEventInningNumber() == inn_num) {
					  switch (events.get(i).getEventType().toUpperCase()) {
					    case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT: case CricketUtil.FOUR: 
					    case CricketUtil.SIX: case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
					    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
					    	
					    	total_runs = total_runs + events.get(i).getEventRuns() ;
					    	
					    	switch (events.get(i).getEventType().toUpperCase()) {
						    case CricketUtil.LOG_WICKET: case CricketUtil.LOG_ANY_BALL:
						    	total_runs = total_runs + events.get(i).getEventExtraRuns() + events.get(i).getEventSubExtraRuns();
						    	
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
		case CricketUtil.SHORT:
			if(match.getSetup().getTossWinningTeam() == match.getSetup().getHomeTeamId()) {
				TeamNameToUse = match.getSetup().getHomeTeam().getTeamName4();
			} else {
				TeamNameToUse = match.getSetup().getAwayTeam().getTeamName4();
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
			return TeamNameToUse + " won the toss and " + decisionText;
		default:
			if(electedOrChoose == null) {
				return TeamNameToUse + " won the toss";
			} else {
				switch (electedOrChoose) {
				case CricketUtil.ELECTED:
					return TeamNameToUse + " won the toss and elected to " + decisionText;
				default:
					return TeamNameToUse + " won the toss and chose to " + decisionText;
				}
			}
		}
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

	public static int getTargetRuns(MatchAllData match) {
		if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
			int targetRuns = ((match.getMatch().getInning().get(0).getTotalRuns() + 
				match.getMatch().getInning().get(2).getTotalRuns()) - (match.getMatch().getInning().get(1).getTotalRuns() 
				+ match.getMatch().getInning().get(3).getTotalRuns())) + 1;
			if(match.getSetup().getTargetRuns() > 0) {
				targetRuns = match.getSetup().getTargetRuns();
			}
			return targetRuns;
		}else {
			int targetRuns = match.getMatch().getInning().get(0).getTotalRuns() + 1;
			if(match.getSetup().getTargetRuns() > 0) {
				targetRuns = match.getSetup().getTargetRuns();
			}
			return targetRuns;
		}
	}

	public static String getTargetOvers(MatchAllData match) {
		
		String targetOvers = "";
		if(match.getSetup().getTargetOvers() == null || match.getSetup().getTargetOvers().trim().isEmpty()) {
			targetOvers = String.valueOf(match.getSetup().getMaxOvers());
		} else {
			targetOvers = match.getSetup().getTargetOvers();
		}
		return targetOvers;
	}

	public static int getRequiredRuns(MatchAllData match) {
		
		int requiredRuns = getTargetRuns(match) - match.getMatch().getInning().get(1).getTotalRuns();
		if(requiredRuns <= 0) {
			requiredRuns = 0;
		}
		return requiredRuns;
	}

	public static int getRequiredBalls(MatchAllData match) {
		int requiredBalls;
		if(getTargetOvers(match).contains(".")) {
			requiredBalls = ((Integer.valueOf(getTargetOvers(match).split(".")[0]) * Integer.valueOf(match.getSetup().getBallsPerOver())) + Integer.valueOf(getTargetOvers(match).split(".")[1])) 
					- (match.getMatch().getInning().get(1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(1).getTotalBalls();
		} else {
			requiredBalls = ((Integer.valueOf(getTargetOvers(match)) * Integer.valueOf(match.getSetup().getBallsPerOver()))) 
					- (match.getMatch().getInning().get(1).getTotalOvers() * Integer.valueOf(match.getSetup().getBallsPerOver())) - match.getMatch().getInning().get(1).getTotalBalls();
		}
		if(requiredBalls <= 0) {
			requiredBalls = 0;
		}
		return requiredBalls;
	}

	public static int getWicketsLeft(MatchAllData match) {
		
		int wicketsLeft = 0;
		
		if(match.getSetup().getMaxOvers() == 1) {
			wicketsLeft = 2 - (match.getMatch().getInning().get(1).getTotalWickets()); 
		} else {
			wicketsLeft = 10 - (match.getMatch().getInning().get(1).getTotalWickets()); 
		}
		
		if(wicketsLeft <= 0) {
			wicketsLeft = 0;
		}
		
		return wicketsLeft;
	}

	public static int getTeamRunsAhead(int inning_number, MatchAllData match)
	{
		int total_runs = 0, batting_team_id = 0;
		for(Inning inn : match.getMatch().getInning()) {
			if(inn.getInningNumber() == inning_number) {
				batting_team_id = inn.getBattingTeamId();
			}
		}
		if(batting_team_id > 0) {
			for(Inning inn : match.getMatch().getInning()) {
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
	
	public static String generateMatchSummaryStatus(int whichInning, MatchAllData match, String teamNameType) {

		String matchSummaryStatus = generateMatchResult(match, teamNameType);

	    if(matchSummaryStatus.trim().isEmpty()) {
	    	
	    	int lead_by = getTeamRunsAhead(whichInning,match);
			String batTeamNm = "", bowlTeamNm = "";

			switch (teamNameType) {
		    case CricketUtil.SHORT: 
		    	batTeamNm = match.getMatch().getInning().get(whichInning - 1).getBatting_team().getTeamName4();
		    	bowlTeamNm = match.getMatch().getInning().get(whichInning - 1).getBowling_team().getTeamName4();
		    	break;
		    default: 
		    	batTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBatting_team().getTeamName1();
		    	bowlTeamNm = (match.getMatch().getInning().get(whichInning - 1)).getBowling_team().getTeamName1();
		    	break;
		    }
	    	
		    switch (whichInning) {
		    case 1: 
		    	if (((match.getMatch().getInning().get(whichInning - 1)).getTotalRuns() > 0) || 
		  		      ((match.getMatch().getInning().get(whichInning - 1)).getTotalOvers() > 0) || 
		  		      ((match.getMatch().getInning().get(whichInning - 1)).getTotalBalls() > 0)) {
		  		      return "Current Run Rate " + (match.getMatch().getInning().get(0)).getRunRate();
		  		    }
		    	else {
		    		return CricketFunctions.generateTossResult(match, CricketUtil.FULL, CricketUtil.FIELD, CricketUtil.FULL, CricketUtil.CHOSE);
		    	}
		    case 2: case 3:
		    	if(match.getSetup().getMatchType().equalsIgnoreCase(CricketUtil.TEST)) {
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
					        	" more run" + CricketFunctions.Plural(CricketFunctions.getRequiredRuns(match)) + " to win from ";
				    	if (CricketFunctions.getRequiredBalls(match) > 120) {
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
				    if(match.getSetup().getTargetType() != null) {
						if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.DLS)) {
							matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.DLS + ")";
						}else if(match.getSetup().getTargetType().equalsIgnoreCase(CricketUtil.VJD)) {
							matchSummaryStatus = matchSummaryStatus + " (" + CricketUtil.VJD + ")";
						}
				    }
		    	}
		    	break;
		    case 4:
		    	int required_runs = 1 - lead_by;
		    	if(required_runs > 0) {
		    		if(match.getMatch().getInning().get(whichInning - 1).getTotalRuns() == 0) {
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
		
		if((match.getEventFile().getEvents() != null) && (match.getEventFile().getEvents().size() > 0)) {
			for(int i = 0; i <= match.getEventFile().getEvents().size() - 1; i++) {
				if(match.getEventFile().getEvents().get(i).getEventInningNumber() == inn_number) {
					switch(match.getEventFile().getEvents().get(i).getEventType()) {
					case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
					case CricketUtil.FOUR: case CricketUtil.SIX: case CricketUtil.LOG_WICKET:
					case CricketUtil.BYE: case CricketUtil.LEG_BYE:  //case CricketUtil.LOG_ANY_BALL:
						ball_count = ball_count + 1;
						break;
					}
 
					if(ball_count >= ((inning.getFirstPowerplayStartOver()-1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count < (inning.getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))) {
						switch(match.getEventFile().getEvents().get(i).getEventType()) {
						case CricketUtil.ONE : case CricketUtil.TWO: case CricketUtil.THREE:  case CricketUtil.FIVE : case CricketUtil.DOT:
						case CricketUtil.FOUR: case CricketUtil.SIX: 
							total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
							break;
						case CricketUtil.WIDE: case CricketUtil.NO_BALL: case CricketUtil.BYE: case CricketUtil.LEG_BYE: case CricketUtil.PENALTY:
							total_run_PP += match.getEventFile().getEvents().get(i).getEventRuns();
							break;
						case CricketUtil.LOG_WICKET:
							total_wickets_PP += 1;
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
					}else if(ball_count >= ((inning.getFirstPowerplayStartOver()-1) * Integer.valueOf(match.getSetup().getBallsPerOver())) && ball_count == (inning.getFirstPowerplayEndOver() * Integer.valueOf(match.getSetup().getBallsPerOver()))){
						if(!match.getEventFile().getEvents().get(i-2).getEventType().equalsIgnoreCase(CricketUtil.END_OVER)) {
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
                                total_wickets_PP += 1;
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
	
	public static List<String> projectedScore(MatchAllData match) {
		List<String> proj_score = new ArrayList<String>();
		String  PS_Curr="", PS_1 = "",PS_2 = "",PS_3 = "",RR_1 = "",RR_2 = "",RR_3 = "",CRR = "";
		int Balls_val = 0;

		if(!match.getSetup().getTargetOvers().isEmpty() && Double.valueOf(match.getSetup().getTargetOvers()) > 0) {
			if(match.getSetup().getTargetOvers().contains(".")) {
				Balls_val = (Integer.valueOf(match.getSetup().getTargetOvers().split("\\.")[0]) * Integer.valueOf(match.getSetup().getBallsPerOver())) 
					+ Integer.valueOf(match.getSetup().getTargetOvers().split("\\.")[1]);
			}else {
				Balls_val = Integer.valueOf(match.getSetup().getTargetOvers()) * Integer.valueOf(match.getSetup().getBallsPerOver());
			}
		}else {
			Balls_val = match.getSetup().getMaxOvers()* Integer.valueOf(match.getSetup().getBallsPerOver());
		}
		
		int remaining_balls = (Balls_val - (match.getMatch().getInning().get(0).getTotalOvers()* Integer.valueOf(match.getSetup().getBallsPerOver()) + match.getMatch().getInning().get(0).getTotalBalls()));
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
	public static List<DuckWorthLewis> populateDuckWorthLewis(MatchAllData match) throws InterruptedException 
	{
		Document htmlFile = null; 
		try {
			for(Inning inn : match.getMatch().getInning()) {
				if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
					int totalball = 0;
					totalball =((inn.getTotalOvers()* Integer.valueOf(match.getSetup().getBallsPerOver())) + inn.getTotalBalls());
					if(totalball < 42) {
						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores BB.html"), "ISO-8859-1");

					}else if(totalball >= 42) {
						htmlFile = Jsoup.parse(new File("C:\\Sports\\Cricket\\ParScores OO.html"), "ISO-8859-1");

					}
				}
			}
			
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
					this_dls.add(new DuckWorthLewis(htmlFile.body().getElementsByTag("font").get(i).text(),
							htmlFile.body().getElementsByTag("font").get(i+(2+(inn.getTotalWickets()))).text()));
				}
				
			}
			i = i +11;
			
		}
		
		return this_dls;
	}
	public static String populateDls(MatchAllData match) throws InterruptedException 
	{
		String team="",ahead_behind="";
		int runs = 0;
		for(Inning inn : match.getMatch().getInning()) {
			if (inn.getIsCurrentInning().toUpperCase().equalsIgnoreCase(CricketUtil.YES)) {
				if(inn.getBattingTeamId() == match.getSetup().getHomeTeamId()) {
					team = match.getSetup().getHomeTeam().getTeamName4();
				}
				if(inn.getBattingTeamId() == match.getSetup().getAwayTeamId()) {
					team = match.getSetup().getAwayTeam().getTeamName4();
				}
				
				for(int i = 0; i<= CricketFunctions.populateDuckWorthLewis(match).size() -1;i++) {
					if(CricketFunctions.populateDuckWorthLewis(match).get(i).getOver_left().equalsIgnoreCase(CricketFunctions.OverBalls(inn.getTotalOvers(),inn.getTotalBalls()))) {
						runs = (inn.getTotalRuns() - Integer.valueOf(CricketFunctions.populateDuckWorthLewis(match).get(i).getWkts_down()));
					}
				}
				if(runs < 0)
                {
                    ahead_behind = team + " is " + (Math.abs(runs)) + " runs behind";
                }

                if (runs > 0)
                {
                    ahead_behind = team + " is " + runs + " runs ahead";
                }
                
                if (runs == 0)
                {
                	ahead_behind = "DLS score is level";
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
	
	public static ArrayList<BowlerData> getBatsmanRunsVsAllBowlers(int PlayerId,int inn_number,List<Player> plyer,MatchAllData match) {
		
		ArrayList<BowlerData> bowler_data = new ArrayList<BowlerData>();
		int playerId = -1,four=0,six=0;
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
								bowler_data.get(playerId).setRuns(bowler_data.get(playerId).getRuns() + match.getEventFile().getEvents().get(i).getEventRuns());
								if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)){
									bowler_data.get(playerId).setFours(bowler_data.get(playerId).getFours() + 1);
								}
								else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
									bowler_data.get(playerId).setSixes(bowler_data.get(playerId).getSixes() + 1);
								}
							}else {
								int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
								
								if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)){
									four = 1;
									six = 0;
								}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
									four = 0;
									six = 1;
								}else {
									four=0;
									six=0;
								}
								
								this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
								bowler_data.add(new BowlerData(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),four,six, this_bowler));
							}
						}else {
							
							int Player_id = match.getEventFile().getEvents().get(i).getEventBowlerNo();
							
							if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.FOUR)){
								four = 1;
								six = 0;
							}else if(match.getEventFile().getEvents().get(i).getEventType().equalsIgnoreCase(CricketUtil.SIX)) {
								four = 0;
								six = 1;
							}else {
								four=0;
								six=0;
							}
							
							this_bowler = plyer.stream().filter(plyr -> plyr.getPlayerId() == Player_id).findAny().orElse(null);
							
							bowler_data.add(new BowlerData(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),four,six, this_bowler));
						}
						break;
					case CricketUtil.LOG_ANY_BALL:
						if(bowler_data.size() > 0 && match.getEventFile().getEvents().get(i).getEventBowlerNo() > 0) {
							
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
								bowler_data.add(new BowlerData(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),four,six, this_bowler));
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
							bowler_data.add(new BowlerData(Player_id,match.getEventFile().getEvents().get(i).getEventRuns(),four,six, this_bowler));
						}
						break;
					}
				}
			}
		}
		for(int k=0;k<=bowler_data.size()-1;k++) {
			System.out.println("Player Name : " + bowler_data.get(k).getPlayer().getFull_name() + " - Runs : " + bowler_data.get(k).getRuns() + 
					" - FOURS/SIXES : " + bowler_data.get(k).getFours() + "/" + bowler_data.get(k).getSixes());
		}
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
	    int total_runs = 0, total_wickets = 0, total_Four = 0, total_SIX = 0;

	    if (events != null && !events.isEmpty()) {
	        for (int i = 0; i < events.size(); i++) {

	            if ( events.get(i).getEventInningNumber() == inning_number) {
	                switch (events.get(i).getEventType()) {
	                    case CricketUtil.ONE: case CricketUtil.TWO:case CricketUtil.THREE:case CricketUtil.FIVE:case CricketUtil.DOT:
	                    case CricketUtil.FOUR:case CricketUtil.SIX:
	                        total_runs += events.get(i).getEventRuns();

	                        switch ( events.get(i).getEventType()) {
	                            case CricketUtil.FOUR:
	                                total_Four++;
	                                break;
	                            case CricketUtil.SIX:
	                                total_SIX++;
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
	                        break;
	                }

	                if ( events.get(i).getEventOverNo() == match.getMatch().getInning().get(1).getTotalOvers()
	                        &&  events.get(i).getEventBallNo() == match.getMatch().getInning().get(1).getTotalBalls()) {
	                    return total_runs + separator + total_wickets + separator + total_SIX + separator + total_Four;
	                }
	            }
	        }
	    }

	    return "";
	}
	public static Player getPlayerFromMatchData(int plyr_id, MatchAllData match)
	{
		for(Player plyr : match.getSetup().getHomeSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getAwaySquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getHomeOtherSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		for(Player plyr : match.getSetup().getAwayOtherSquad()) {
			if(plyr_id == plyr.getPlayerId()) { 
				return plyr;
			}
		}
		return null;
	}	
}