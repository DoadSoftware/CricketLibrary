package com.cricket.dao.impl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
import com.cricket.util.CricketUtil;

@Transactional
@Repository("cricketDao")
@SuppressWarnings("unchecked")
public class CricketDaoImpl implements CricketDao {

 @Autowired
 private SessionFactory sessionFactory;
 
 @Autowired
 private SessionFactory sessionFactorySecondaryDb;
 
@Override
public Player getPlayer(String whatToProcess, String valueToProcess) {
	switch (whatToProcess) {
	case CricketUtil.PLAYER:
		return (Player) sessionFactory.getCurrentSession().createQuery("from Player WHERE PlayerId=" + valueToProcess).uniqueResult();  
	default:
		return null;  
	}
}

@Override
public Team getTeam(String whatToProcess, String valueToProcess) {
	switch (whatToProcess) {
	case CricketUtil.TEAM:
		return (Team) sessionFactory.getCurrentSession().createQuery("from Team WHERE TeamId=" + valueToProcess).uniqueResult();  
	default:
		return null;  
	}
}

@Override
public List<Team> getTeams() {
	return sessionFactory.getCurrentSession().createQuery("from Team").list();
}

@Override
public List<Player> getPlayers(String whatToProcess, String valueToProcess) {
	switch (whatToProcess) {
	case CricketUtil.TEAM:
		return sessionFactory.getCurrentSession().createQuery("from Player WHERE TeamId=" + valueToProcess).list();  
	case CricketUtil.GENDER_SPECIFIC_TEAM:
		if(valueToProcess.contains(",")) {
			return sessionFactory.getCurrentSession().createQuery("from Player WHERE TeamId=" + valueToProcess.split(",")[0] 
				+ " AND Gender='" + valueToProcess.split(",")[1] + "'").list();
		} else {
			return null;  
		}
	default:
		return null;  
	}
}

@Override
public List<Ground> getGrounds() {
	return sessionFactory.getCurrentSession().createQuery("from Ground").list();  
}

@Override
public Ground getGround(int ground_id) {
	return (Ground) sessionFactory.getCurrentSession().createQuery("from Ground WHERE GroundId=" + ground_id).uniqueResult();  
}

@Override
public StatsType getStatsType(int stats_type_id) {
	return (StatsType) sessionFactory.getCurrentSession().createQuery("from StatsType WHERE StatsId=" + stats_type_id).uniqueResult();  
}

@Override
public List<NameSuper> getNameSupers() {
	return sessionFactory.getCurrentSession().createQuery("from NameSuper").list();
}

@Override
public List<InfobarStats> getInfobarStats() {
	return sessionFactory.getCurrentSession().createQuery("from InfobarStats").list();
}

@Override
public List<Sponsor> getSponsor() {
	return sessionFactory.getCurrentSession().createQuery("from Sponsor").list();
}

@Override
public List<FantasyImages> getFantasyImages() {
	return sessionFactory.getCurrentSession().createQuery("from FantasyImages").list();
}

@Override
public List<Bugs> getBugs() {
	return sessionFactory.getCurrentSession().createQuery("from Bugs").list();
}
@Override
public List<EverestBugs> getEverestBugs() {
	return sessionFactory.getCurrentSession().createQuery("from EverestBugs").list();
}
@Override
public List<LeaderBoard> getLeaderBoards() {
	return sessionFactory.getCurrentSession().createQuery("from LeaderBoard").list();
}
@Override
public List<Split> getSplit() {
	return sessionFactory.getCurrentSession().createQuery("from Split").list();
}

@Override
public List<Statistics> getAllStats() {
	return sessionFactory.getCurrentSession().createQuery("from Statistics").list();
}

@Override
public List<Player> getAllPlayer() {
	return sessionFactory.getCurrentSession().createQuery("from Player").list();
}

@Override
public List<Fixture> getFixtures() {
	return sessionFactory.getCurrentSession().createQuery("from Fixture").list();
}

@Override
public List<VariousText> getVariousTexts() {
	return sessionFactory.getCurrentSession().createQuery("from VariousText").list();
}

@Override
public List<Venue> getVenues() {
	return sessionFactory.getCurrentSession().createQuery("from Venue").list();
}

@Override
public List<Dictionary> getDictionary() {
	return sessionFactory.getCurrentSession().createQuery("from Dictionary").list();
}
@Override
public List<Playoff> getPlayOff() {
	return sessionFactory.getCurrentSession().createQuery("from Playoff").list();
}

@Override
public List<Season> getSeasons() {
	return sessionFactory.getCurrentSession().createQuery("from Season").list();  
}

@Override
public List<Pointers> getPointers() {
	return sessionFactory.getCurrentSession().createQuery("from Pointers").list();
}

@Override
public List<StatsType> getAllStatsType() {
	return sessionFactory.getCurrentSession().createQuery("from StatsType").list();
}

@Override
public List<Commentator> getCommentator() {
	return sessionFactory.getCurrentSession().createQuery("from Commentator").list();
}

public List<Staff> getStaff() {
	return sessionFactory.getCurrentSession().createQuery("from Staff").list();
}

@Override
public List<POTT> getPott() {
	return sessionFactory.getCurrentSession().createQuery("from POTT").list();
}

@Override
public List<Performer> getPerformer() {
	return sessionFactory.getCurrentSession().createQuery("from Performer").list();
}

@Override
public List<Weather> getWeather() {
	return sessionFactory.getCurrentSession().createQuery("from Weather").list();
}

@Override
public List<PerformanceBug> getPerformanceBugs() {
	return sessionFactory.getCurrentSession().createQuery("from PerformanceBug").list();
}

@Override
public List<Player> getArchivePlayers() {
	return sessionFactorySecondaryDb.getCurrentSession().createQuery("from Player").list();
}
}