package ping;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.docler.executor.PingExecutor;
import com.docler.misc.PingTrace;
import com.docler.misc.ScheduledType;
import com.docler.pinger.ICMPPinger;
import com.docler.pinger.TCPPinger;

public class PingTest {
	private PingExecutor pingExecutor;
	private PingTrace pingTrace;
	private ICMPPinger icmpPinger;
	Map<String, PingTrace> traceMap;
	private final String host = "google.com";
	private final String postUrl = "127.0.0.1:80";
	private final int timePlaceholder = 5000;
	private final String triesSwitch = isWindows() ? "-n" : "-c";
	@Before
	public void setup() {
		pingTrace = new PingTrace(host);
		traceMap = new HashMap<>();
		traceMap.put(host, pingTrace);
	    pingExecutor = new PingExecutor(traceMap);
	}

	@After
	public void tearDown() {
	    pingExecutor.shutdown();
	}
	
	@Test
	public void testForPingStatus() {
		assertEquals(ScheduledType.NONE, pingTrace.getScheduledType());
		icmpPinger = new ICMPPinger(host, postUrl, timePlaceholder, pingTrace, 1, triesSwitch);
		pingExecutor.signalProcess(true, icmpPinger);
		assertEquals(ScheduledType.ACTIVE, pingTrace.getScheduledType());
	}
	
	@Test
	public void testForPingSchedule() {
		assertEquals(ScheduledType.NONE, pingTrace.getScheduledType());
		icmpPinger = new ICMPPinger(host, postUrl, timePlaceholder, pingTrace, 1, triesSwitch);
		pingExecutor.signalProcess(true, icmpPinger);
		assertEquals(ScheduledType.ACTIVE, pingTrace.getScheduledType());
		TCPPinger tcpPinger = new TCPPinger(host, postUrl, timePlaceholder, pingTrace, 
				timePlaceholder, timePlaceholder);
		pingExecutor.signalProcess(true, tcpPinger);
		assertEquals(ScheduledType.SCHEDULED, pingTrace.getScheduledType());
	}
	
	@Test
	public void testForMessage() {
		assertNull(pingTrace.getIcmpPing());
		icmpPinger = new ICMPPinger(host, postUrl, timePlaceholder, pingTrace, 1, triesSwitch);
		Future<?> future = pingExecutor.signalProcess(true, icmpPinger);
		try {
			future.get();
		} catch (Exception e) {}
		assertNotNull(pingTrace.getIcmpPing());
		
	}
	
	
	private static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().contains("window");
	}

}
