package com.docler.pinger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.docler.misc.PingTrace;

public class ICMPPinger extends AbstractPinger {
	
	private final String triesSwitch;
	private int tries;
	
	public ICMPPinger(String host, String postUrl, 
			int pingDelay, PingTrace pingTrace, int tries, String triesSwitch) {
		super(host, postUrl, pingDelay, pingTrace);
		this.triesSwitch = triesSwitch;
		this.tries = tries;
	}

	@Override
	public int ping() {
		List<String> commands = new ArrayList<String>();
	    commands.add("ping");
	    commands.add(triesSwitch);
	    commands.add("" + tries);
	    commands.add(getHost());
	    ProcessBuilder pb = new ProcessBuilder(commands);
	    try {
			Process process = pb.start();
			InputStream stream = process.getInputStream();
			return readMessage(stream);
		} catch (IOException e) {
			appendMessage("Error occured while pinging host");
			return 1;
		}
	}

	@Override
	public void appendMessage(String message) {
		getPingTrace().setIcmpPing(message);
	}

	public String getTriesSwitch() {
		return triesSwitch;
	}
}
