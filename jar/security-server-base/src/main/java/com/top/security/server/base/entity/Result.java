package com.top.security.server.base.entity;

import java.util.List;

public class Result{
	protected String resultCode;
	
	protected String resultStatus;
	
	protected String resultMsg;

	public Result() {
		super();
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	@Override
	public String toString() {
		return "Result [resultCode=" + resultCode + ", resultStatus=" + resultStatus + ", resultMsg=" + resultMsg + "]";
	}		
}
