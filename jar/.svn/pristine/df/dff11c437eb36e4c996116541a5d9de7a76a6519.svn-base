package com.top.security.server.base.entity;

public class RequestResult<T> extends Result{	
	
	private T result;
	
	public RequestResult() {
		super();
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	@Override
	public String toString() {
		try {
			return "RequestResult [resultCode=" + resultCode + ", resultStatus=" + resultStatus + ", resultMsg=" + resultMsg
					+ ", result=" + result != null ? result.toString() : "null" + "]";
		} catch (Exception e) {
			return "";
		}
		
	}		
}
