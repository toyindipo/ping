package com.docler.pinger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.docler.executor.PingExecutor;
import com.docler.misc.PingTrace;
import com.docler.misc.ScheduledType;
import com.docler.utils.JsonUtil;
import com.docler.utils.MessageLogger;

public abstract class AbstractPinger implements Runnable {
	
	private String host;
	private String postUrl;
	private int pingDelay;
	private volatile PingTrace pingTrace;
	private PingExecutor executor;
	
	public AbstractPinger(String host, String postUrl, int pingDelay,
			PingTrace pingTrace) {
		this.host = host;
		this.postUrl = postUrl;
		this.pingDelay = pingDelay;
		this.pingTrace = pingTrace;
	}
	public abstract int ping();
	public abstract void appendMessage(String message);
	
	public void reset() {
		pingTrace.setScheduledPinger(null);
		pingTrace.setScheduledType(ScheduledType.NONE);
	}
	
	public void report() throws IOException {
		MessageLogger.postAndLog(JsonUtil.convertToString(pingTrace), postUrl);
	}
	
	public int readMessage(InputStream stream) {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
	    String line = null;
	    try {
			while((line = br.readLine() ) != null) {
				appendMessage(line);
				MessageLogger.log(line);
			    if (line.toLowerCase().contains("destination host unreachable")) {			    	
			    	report();
			    	return 1;
			    }
			}
		} catch (IOException e) {
			MessageLogger.log("IO exception");
		}
	    
		return 0;
	}	
	
	@Override
	public void run() {
		
		int result = ping();
		if (result != 0) {
			reset();
		} else {
			try {
				Thread.sleep(pingDelay);
			} catch (InterruptedException e) {
				MessageLogger.log("Thread sleep interrupted");
			}
		}
		
		ScheduledType scheduledType = getPingTrace().getScheduledType();
		if (scheduledType.equals(ScheduledType.ACTIVE)) {
			getPingTrace().setScheduledType(ScheduledType.NONE);
		}		
		executor.signalProcess(false, this);
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public int getPingDelay() {
		return pingDelay;
	}

	public void setPingDelay(int pingDelay) {
		this.pingDelay = pingDelay;
	}

	public PingTrace getPingTrace() {
		return pingTrace;
	}

	public void setPingTrace(PingTrace pingTrace) {
		this.pingTrace = pingTrace;
	}
	public PingExecutor getExecutor() {
		return executor;
	}
	public void setExecutor(PingExecutor executor) {
		this.executor = executor;
	}	
	
}
