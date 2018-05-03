package com.cucund.work.security.amq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolException extends RuntimeException {

	protected final Logger LOGGER = LoggerFactory.getLogger(ProtocolException.class);
	
	private static final long serialVersionUID = 6774160966441100195L;
	
	private String errorMsg = "";
	
	public ProtocolException(String errorMsg) {
		super(errorMsg);
		this.errorMsg = errorMsg;
		LOGGER.error(errorMsg);
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}



}
