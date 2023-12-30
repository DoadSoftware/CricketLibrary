package com.cricket.service;

import java.util.List;

import com.cricket.model.Bugs;
import com.cricket.model.Dictionary;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Playoff;
import com.cricket.model.Pointers;
import com.cricket.model.Season;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.VariousText;
import com.cricket.model.Venue;

public interface CricketService {
  Player getPlayer(String whatToProcess, String valueToProcess);
  Team getTeam(String whatToProcess, String valueToProcess);
  Ground getGround(int ground_id);
  List<StatsType> getAllStatsType();
  StatsType getStatsType(int stats_type_id);
  List<Player> getPlayers(String whatToProcess, String valueToProcess);
  List<Team> getTeams();
  List<NameSuper> getNameSupers();
  List<InfobarStats> getInfobarStats();
  List<Bugs> getBugs();
  List<Pointers> getPointers();
  List<Ground> getGrounds();
  List<Statistics> getAllStats();
  List<Player> getAllPlayer();
  List<Venue> getVenues();
  List<Dictionary> getDictionary();
  List<Fixture> getFixtures();
  List<VariousText> getVariousTexts();
  List<Playoff> getPlayOff();
  List<Season> getSeasons();
}