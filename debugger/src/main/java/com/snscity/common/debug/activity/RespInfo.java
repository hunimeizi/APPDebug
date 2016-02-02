package com.snscity.common.debug.activity;

public class RespInfo {
	public String createTime;
	public String requestUrl;
	public String requestParams;
	public String response;
	public String responseMsgSize;
	public RespInfo(String createTime, String requestUrl, String requestParams, String response, String responseMsgSize) {
		super();
		this.createTime = createTime;
		this.requestUrl = requestUrl;
		this.requestParams = requestParams;
		this.response = response;
		this.responseMsgSize = responseMsgSize;
	}
	
}
