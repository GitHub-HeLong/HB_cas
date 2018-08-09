package org.jasig.cas.adaptors.jdbc;

import javax.validation.constraints.NotNull;

import org.jasig.cas.LoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcDatabaseLoginLogHandler extends AbstractJdbcLoginLogHandler{

	@NotNull
	private String loginSql;
	
	@NotNull
	private String logoutSql;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getLoginSql() {
		return loginSql;
	}

	public void setLoginSql(String loginSql) {
		this.loginSql = loginSql;
	}

	public String getLogoutSql() {
		return logoutSql;
	}

	public void setLogoutSql(String logoutSql) {
		this.logoutSql = logoutSql;
	}

	@Override
	public boolean login(LoginLog log) {
		if(log==null){
			return false;
		}
		logger.info("user {} login,the loginlog is {}",log.getUsername(),log.toString());
		try {
			getJdbcTemplate().update(this.loginSql, new Object [] {log.getId(),
					log.getUsername(),log.getClientIp(),log.getLoginTime(),log.getStatus()});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean logout(LoginLog log) {
		if(log==null){
			return false;
		}
		logger.info("user {} logout,the loginlog is {}",log.getUsername(),log.toString());
		try {
			getJdbcTemplate().update(this.logoutSql, new Object [] {log.getStatus(),
					log.getLogoutTime(),log.getLogoutWay(),log.getLogoutCode(),log.getId()});
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
