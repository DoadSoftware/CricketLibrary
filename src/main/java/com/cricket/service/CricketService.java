package com.cricket.service;

import java.util.List;

import com.cricket.model.Bugs;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;

public interface CricketService {
  Player getPlayer(String whatToProcess, String valueToProcess);
  Team getTeam(String whatToProcess, String valueToProcess);
  Ground getGround(int ground_id);
  StatsType getStatsType(int stats_type_id);
  List<Player> getPlayers(String whatToProcess, String valueToProcess);
  List<Team> getTeams();
  List<NameSuper> getNameSupers();
  List<InfobarStats> getInfobarStats();
  List<Bugs> getBugs();
  List<Ground> getGrounds();
  List<Statistics> getPlayerStatistics(int player_id);
}