package com.cricket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cricket.dao.CricketDao;
import com.cricket.model.Bugs;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.NameSuper;
import com.cricket.model.Player;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.service.CricketService;

@Service("cricketService")
@Transactional
public class CricketServiceImpl implements CricketService {

 @Autowired
 private CricketDao cricketDao;
 
@Override
public Player getPlayer(String whatToProcess, String valueToProcess) {
	return cricketDao.getPlayer(whatToProcess, valueToProcess);
}

@Override
public Team getTeam(String whatToProcess, String valueToProcess) {
	return cricketDao.getTeam(whatToProcess, valueToProcess);
}

@Override
public List<Team> getTeams() {
	return cricketDao.getTeams();
}

@Override
public List<Player> getPlayers(String whatToProcess, String valueToProcess) {
	return cricketDao.getPlayers(whatToProcess, valueToProcess);
}

@Override
public List<Ground> getGrounds() {
	return cricketDao.getGrounds();
}

@Override
public Ground getGround(int ground_id) {
	return cricketDao.getGround(ground_id);
}

@Override
public StatsType getStatsType(int stats_type_id) {
	return cricketDao.getStatsType(stats_type_id);
}

/*@Override
public List<Statistics> getPlayerStatistics(int player_id) {
	return cricketDao.getPlayerStatistics(player_id);
}*/

@Override
public List<NameSuper> getNameSupers() {
	return cricketDao.getNameSupers();
}

@Override
public List<InfobarStats> getInfobarStats() {
	return cricketDao.getInfobarStats();
}

@Override
public List<Bugs> getBugs() {
	return cricketDao.getBugs();
}

@Override
public List<Statistics> getAllStats() {
	return cricketDao.getAllStats();
}

}