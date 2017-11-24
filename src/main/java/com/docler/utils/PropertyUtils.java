package com.docler.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
	private String reportURL;
	private int tcpTimeOut;
	private int responseTime;
	private int pingDelay;
	
	public void setProperties() throws FileNotFoundException, IOException {
		Properties props = new Properties();
        try (InputStream in = this.getClass().getResourceAsStream("/config.properties")) {
        	props.load(in);
        	setReportURL(props.getProperty("report.url"));
        	setTcpTimeOut(Integer.parseInt(props.getProperty("tcp.timeout"))); 
        	setResponseTime(Integer.parseInt(props.getProperty("response.time"))); 
        	setPingDelay(Integer.parseInt(props.getProperty("ping.delay"))); 
        }
	}

	public String getReportURL() {
		return reportURL;
	}

	public void setReportURL(String reportURL) {
		this.reportURL = reportURL;
	}

	public int getTcpTimeOut() {
		return tcpTimeOut;
	}

	public void setTcpTimeOut(int tcpTimeOut) {
		this.tcpTimeOut = tcpTimeOut;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(int responseTime) {
		this.responseTime = responseTime;
	}

	public int getPingDelay() {
		return pingDelay;
	}

	public void setPingDelay(int pingDelay) {
		this.pingDelay = pingDelay;
	}
	
	
	
}
