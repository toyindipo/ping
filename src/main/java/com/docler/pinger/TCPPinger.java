package com.docler.pinger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import com.docler.misc.PingTrace;
import com.docler.utils.MessageLogger;

public class TCPPinger  extends AbstractPinger {
	private int tcpTimeout;
	private long duration;
	private int statusCode;
	private long expectedResponseTime;
	
	public TCPPinger(String host, String postUrl, 
			int pingDelay, PingTrace pingTrace, int tcpTimeout, long expectedResponseTime) {
		super(host, postUrl, pingDelay, pingTrace);
		this.tcpTimeout = tcpTimeout;
		this.expectedResponseTime = expectedResponseTime;
	}
	
	@Override
	public int ping() {
		URL siteURL;
		try {
			siteURL = new URL(getHost());
		} catch (MalformedURLException e) {
			setStatusCode(421);
			MessageLogger.log("Malformed url, verify protocol");
			appendMessage("Malformed url, verify protocol");
			return 1;
		}		
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) siteURL.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(tcpTimeout);
			Date start = new Date();
			connection.connect();
			setStatusCode(connection.getResponseCode());
			Date stop = new Date(); 
			duration = stop.getTime() - start.getTime();
			connection.disconnect();
			if (statusCode == 200) {
				if (duration > expectedResponseTime) {
					appendMessage("Host did not respond within the set expected response time");
					return 1;
				} else {
					appendMessage(connection.getResponseMessage());
					return 0;
				}
			} else {
				appendMessage(connection.getResponseMessage());
				return 1;
			}
			
			
		} catch (IOException e) {
			setStatusCode(500);
			appendMessage("Internal error");
			return 1;
		} 
	}

	@Override
	public void appendMessage(String message) {
		MessageLogger.log(message);
		getPingTrace().setTcpPing(String.format("Code: %s, respons time: %d, message: %s", 
				statusCode, duration, message));
	}

	public int getTcpTimeout() {
		return tcpTimeout;
	}

	public void setTcpTimeout(int tcpTimeout) {
		this.tcpTimeout = tcpTimeout;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
}
