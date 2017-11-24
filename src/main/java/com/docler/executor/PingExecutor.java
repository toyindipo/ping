package com.docler.executor;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.docler.misc.PingTrace;
import com.docler.misc.ScheduledType;
import com.docler.pinger.AbstractPinger;
import com.docler.utils.MessageLogger;

public class PingExecutor {
	private ExecutorService executorService;
	public PingExecutor(Map<String, PingTrace> traceMap) {
		executorService = Executors.newFixedThreadPool(5);		
	}
	
	public synchronized Future<?> signalProcess(boolean newAttempt, AbstractPinger pinger) {
		if (newAttempt) {
			return attemptNewPinging(pinger);
		} else {
			return resumeScheduledPinger(pinger);
		}
		
	}
	
	public Future<?> execute(AbstractPinger pinger) {
		pinger.getPingTrace().setScheduledType(ScheduledType.ACTIVE);
		return executorService.submit(pinger);
	}
	
	private Future<?> attemptNewPinging(AbstractPinger pinger) {
		PingTrace trace = pinger.getPingTrace();
		ScheduledType scheduledType = trace.getScheduledType();
		if (scheduledType.equals(ScheduledType.NONE)) {
			return execute(pinger);
		} else {				
			trace.setScheduledType(ScheduledType.SCHEDULED);
			trace.setScheduledPinger(pinger);
			return null;
		}		
	}
	
	private Future<?> resumeScheduledPinger(AbstractPinger pinger) {
		if (pinger.getPingTrace().getScheduledType().equals(ScheduledType.SCHEDULED)) {
			return execute(pinger.getPingTrace().getScheduledPinger());
		} 
		return null;
	}
	
	public void shutdown() {
		try {
		    System.out.println("attempt to shutdown executor");
		    executorService.shutdown();
		    executorService.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
		    MessageLogger.log("tasks interrupted");
		}
		finally {
		    if (!executorService.isTerminated()) {
		        System.err.println("cancel non-finished tasks");
		    }
		    executorService.shutdownNow();
		    System.out.println("shutdown finished");
		}
	}
}
