package org.jasig.cas.log;

import org.jasig.cas.LoginLog;

public interface LogManager {
	public boolean addLoginLog(LoginLog log);
	public boolean updateLoginLog(LoginLog log);
}
