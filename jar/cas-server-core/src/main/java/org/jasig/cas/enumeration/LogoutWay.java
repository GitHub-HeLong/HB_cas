package org.jasig.cas.enumeration;

public enum LogoutWay {
	normal("normal", "正常退出"),
	
	expired("expired", "超时退出"),
	
	aborted("aborted", "异常退出"),
	
	kicked("kicked", "被挤下线");
	
	private String logoutCode;
	
	private String logoutType;

	private LogoutWay(String logoutCode, String logoutType) {
		this.logoutCode = logoutCode;
		this.logoutType = logoutType;
	}

	public String getLogoutCode() {
		return logoutCode;
	}

	public void setLogoutCode(String logoutCode) {
		this.logoutCode = logoutCode;
	}

	public String getLogoutType() {
		return logoutType;
	}

	public void setLogoutType(String logoutType) {
		this.logoutType = logoutType;
	}
	
	
}
