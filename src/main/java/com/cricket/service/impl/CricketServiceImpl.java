package com.cricket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cricket.dao.CricketDao;
import com.cricket.model.Bugs;
import com.cricket.model.Commentator;
import com.cricket.model.Dictionary;
import com.cricket.model.EverestBugs;
import com.cricket.model.FantasyImages;
import com.cricket.model.Fixture;
import com.cricket.model.Ground;
import com.cricket.model.InfobarStats;
import com.cricket.model.LeaderBoard;
import com.cricket.model.NameSuper;
import com.cricket.model.POTT;
import com.cricket.model.PerformanceBug;
import com.cricket.model.Player;
import com.cricket.model.Playoff;
import com.cricket.model.Pointers;
import com.cricket.model.Performer;
import com.cricket.model.Season;
import com.cricket.model.Split;
import com.cricket.model.Sponsor;
import com.cricket.model.Staff;
import com.cricket.model.Statistics;
import com.cricket.model.StatsType;
import com.cricket.model.Team;
import com.cricket.model.VariousText;
import com.cricket.model.Venue;
import com.cricket.model.Weather;
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
public List<Split> getSplit() {
	return cricketDao.getSplit();
}

@Override
public List<Statistics> getAllStats() {
	return cricketDao.getAllStats();
}

@Override
public List<Player> getAllPlayer() {
	return cricketDao.getAllPlayer();
}

@Override
public List<Fixture> getFixtures() {
	return cricketDao.getFixtures();
}

@Override
public List<VariousText> getVariousTexts() {
	return cricketDao.getVariousTexts();
}

@Override
public List<Venue> getVenues() {
	return cricketDao.getVenues();
}

@Override
public List<Dictionary> getDictionary() {
	return cricketDao.getDictionary();
}

@Override
public List<Playoff> getPlayOff() {
	return cricketDao.getPlayOff();
}

@Override
public List<Season> getSeasons() {
	return cricketDao.getSeasons();
}

@Override
public List<Pointers> getPointers() {
	return cricketDao.getPointers();
}

@Override
public List<StatsType> getAllStatsType() {
	return cricketDao.getAllStatsType();
}

@Override
public List<Commentator> getCommentator() {
	return cricketDao.getCommentator();
}

@Override
public List<Staff> getStaff() {
	return cricketDao.getStaff();
}

@Override
public List<POTT> getPott() {
	return cricketDao.getPott();
}


@Override
public List<Sponsor> getSponsor() {
	return cricketDao.getSponsor();
}

@Override
public List<FantasyImages> getFantasyImages() {
	return cricketDao.getFantasyImages();
}

@Override
public List<Performer> getPerformer() {
	return cricketDao.getPerformer();
}

@Override
public List<Weather> getWeather(){
	return cricketDao.getWeather();
}

@Override
public List<PerformanceBug> getPerformanceBugs() {
	return cricketDao.getPerformanceBugs();
}

@Override
public List<EverestBugs> getEverestBugs() {
	return cricketDao.getEverestBugs();
}
@Override
public List<LeaderBoard> getLeaderBoards() {
	return cricketDao.getLeaderBoards();
}
@Override
public List<Player> getArchivePlayers() {
	return cricketDao.getArchivePlayers();
}
}