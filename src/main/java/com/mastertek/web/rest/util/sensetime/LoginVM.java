package com.mastertek.web.rest.util.sensetime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginVM {

	@JsonProperty("user_name")
	String username;
	
	@JsonProperty("user_pwd")
	String password;
	
	@JsonProperty("msg_id")
	String msgId ="257";
	
	

	public LoginVM(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}
