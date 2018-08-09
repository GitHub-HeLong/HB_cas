package org.jasig.cas.adaptors.mongo;

import org.jasig.cas.LoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class MongoDatabaseLoginLogHandler extends AbstractMongoLoginLogHandler {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean login(LoginLog log) {
		if(log==null){
			return false;
		}
		logger.info("user {} login,the loginlog is {}",log.getUsername(),logger.toString());
		Criteria criteria = new Criteria();
	    criteria.and("_id").is(log.getId());
	    Query query = Query.query(criteria);
		try {
			LoginLog login = getMongoTemplate().findOne(query, LoginLog.class, getLogCollection());
			if(login!=null){
				return false;
			}
			getMongoTemplate().save(log,getLogCollection());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean logout(LoginLog log) {
		if(log==null){
			return false;
		}
		logger.info("user {} logout,the loginlog is {}",log.getUsername(),log.toString());
		try {
			Criteria criteria = new Criteria();
		    criteria.and("_id").is(log.getId());
		    Query query = Query.query(criteria);
		    Update update = new Update();
		    update.set("logoutTime", log.getLogoutTime());
		    update.set("logoutWay", log.getLogoutWay());
		    update.set("logoutCode", log.getLogoutCode());
		    update.set("status", log.getStatus());
			getMongoTemplate().updateFirst(query, update, getLogCollection());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
