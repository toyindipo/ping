package com.docler.pinger;

import java.io.IOException;

import com.docler.misc.PingTrace;
import com.docler.utils.MessageLogger;

public class TracePinger  extends AbstractPinger {
	
	private final String tracePrefix;
	
	public TracePinger(String host, String postUrl, 
			int pingDelay, PingTrace pingTrace, String tracePrefix) {
		super(host, postUrl, pingDelay, pingTrace);
		this.tracePrefix = tracePrefix;
	}

	@Override
	public int ping() {
		try {
			Process traceProcess = 
					Runtime.getRuntime().exec(tracePrefix + getHost());
			return readMessage(traceProcess.getInputStream());
		} catch (IOException e) {
			appendMessage("Error occured while pinging host");
			return 1;
		}
	}

	@Override
	public void appendMessage(String message) {
		MessageLogger.log(message);
		getPingTrace().setTrace(message);
	}
}
