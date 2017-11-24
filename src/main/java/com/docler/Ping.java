package com.docler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.docler.executor.PingExecutor;
import com.docler.misc.PingTrace;
import com.docler.pinger.AbstractPinger;
import com.docler.pinger.ICMPPinger;
import com.docler.pinger.TCPPinger;
import com.docler.pinger.TracePinger;
import com.docler.utils.PropertyUtils;

public class Ping {

	public static void main(String[] args) throws FileNotFoundException, 
	IOException, InterruptedException, ExecutionException {
		Scanner input = new Scanner(System.in);
		System.out.print("Enter command (e.g icmping google.com): ");
		String command = input.nextLine();
		String[] arguments;
		Map<String, PingTrace> traceMap = new HashMap<>();
		PingExecutor executor = new PingExecutor(traceMap);
		PropertyUtils propUtils = new PropertyUtils();
		propUtils.setProperties();		
		
		boolean initiatePing = false;
		
		while (!command.equals("exit")) {
			arguments = command.split(" ");
			if (arguments.length >= 2) {
				AbstractPinger pinger;
				String pingType = arguments[0];
				String hostUrl = arguments[1];
				
				PingTrace trace = traceMap.get(hostUrl);
				if (trace == null) {
					trace = new PingTrace(hostUrl);
					traceMap.put(hostUrl, trace);
				}				
				
				if (arguments.length == 2) {
					if (pingType.equals("tcping")) {
						pinger = new TCPPinger(hostUrl, propUtils.getReportURL(),
								propUtils.getPingDelay(), trace, propUtils.getTcpTimeOut(), 
								propUtils.getResponseTime());
						initiatePing = true;
						initiatePing(executor, pinger);
					} else if (pingType.equals("trace")) {
						String tracePrefix = isWindows() ? "tracert " : "traceroute ";
						pinger = new TracePinger(hostUrl, propUtils.getReportURL(),
								propUtils.getPingDelay(), trace, tracePrefix);
						initiatePing = true;
						initiatePing(executor, pinger);					
					}
				} else if (arguments.length == 3) {
					if (pingType.equals("icmping")) {
						String triesSwitch = isWindows() ? "-n" : "-c";
						int tries = Integer.parseInt(arguments[2]);
						pinger = new ICMPPinger(hostUrl, propUtils.getReportURL(),
								propUtils.getPingDelay(), trace, tries, triesSwitch);
						initiatePing = true;
						initiatePing(executor, pinger);
					}
				}							
			} 
			if (!initiatePing) {
				System.out.println("Please enter the right command: ");
			}
			
			System.out.print("Enter command (e.g icmping google.com 2): ");
			command = input.nextLine();
		}
		
		executor.shutdown();
	}
	
	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("window");
	}
	
	public static Future<?> initiatePing(PingExecutor executor, AbstractPinger pinger) {
		return executor.signalProcess(true, pinger);
	}

}
