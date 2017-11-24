package com.docler.misc;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import com.docler.pinger.AbstractPinger;

public class PingTrace {	
	private String host;
	@JsonProperty("icmp_ping")
	private String icmpPing;
	@JsonProperty("tcp_ping")
	private String tcpPing;
	private String trace;
	@JsonIgnore
	private ScheduledType scheduledType;
	@JsonIgnore
	private AbstractPinger scheduledPinger;
	
	public PingTrace() {}
	
	public PingTrace(String host) {
		this(host, null, null, null);
	}
	
	public PingTrace(String host, String icmpPing, String tcpPing, String trace) {
		this.host = host;
		this.icmpPing = icmpPing;
		this.tcpPing = tcpPing;
		this.trace = trace;
		scheduledType = ScheduledType.NONE;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getIcmpPing() {
		return icmpPing;
	}
	public void setIcmpPing(String icmpPing) {
		this.icmpPing = icmpPing;
	}
	public String getTcpPing() {
		return tcpPing;
	}
	public void setTcpPing(String tcpPing) {
		this.tcpPing = tcpPing;
	}
	public String getTrace() {
		return trace;
	}
	public void setTrace(String trace) {
		this.trace = trace;
	}
	public ScheduledType getScheduledType() {
		return scheduledType;
	}
	public void setScheduledType(ScheduledType scheduledType) {
		this.scheduledType = scheduledType;
	}

	public AbstractPinger getScheduledPinger() {
		return scheduledPinger;
	}

	public void setScheduledPinger(AbstractPinger scheduledPinger) {
		this.scheduledPinger = scheduledPinger;
	}	
}
