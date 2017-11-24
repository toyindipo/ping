package com.docler.utils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

public class MessageLogger {
	
	private static Logger logger = Logger.getLogger(MessageLogger.class);;

	public static void postAndLog(String message, String postUrl) {
		log(message);
		try {
			post(message, postUrl);
		} catch (IOException e) {
			System.out.println("Error posting data");
		}
	}
	
	public static void log(String message) {
		logger.warn(message);
	}
	
	public static void post(String message, String reportUrl) throws IOException {
		URL url = new URL(reportUrl);
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setConnectTimeout(5000);//5 secs
	    connection.setReadTimeout(5000);//5 secs

	    connection.setRequestMethod("POST");
	    connection.setDoOutput(true);
	    connection.setRequestProperty("Content-Type", "application/json");

	    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
	    out.write(message);
	    out.flush();
	    out.close();
	    
	    connection.disconnect();
	}
}
