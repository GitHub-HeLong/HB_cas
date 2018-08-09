package org.jasig.cas.log;

import javax.validation.constraints.NotNull;

import org.jasig.cas.LoginLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogManagerHandler implements LogManager {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@NotNull
	LoginLogHandler loginLogHandler;

	public LoginLogHandler getLoginLogHandler() {
		return loginLogHandler;
	}

	public void setLoginLogHandler(LoginLogHandler loginLogHandler) {
		this.loginLogHandler = loginLogHandler;
	}

	@Override
	public boolean addLoginLog(LoginLog log) {
		return loginLogHandler.login(log);
	}

	@Override
	public boolean updateLoginLog(LoginLog log) {
		return loginLogHandler.logout(log);
	}

}
