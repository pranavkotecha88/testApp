package com.samsung.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Log {
	// Log priorities
	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;
	
	public static int LOG_LEVEL = 4;
	
	// these 6 vars would be all private but LogTest needs access
	public static final int BUFFER_SIZE = 256000; // 256KB
	public static final String LOG_PATH = "logs";
	private static DateFormat logDf = new SimpleDateFormat("yyyy-MM-dd");
	final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public static StringBuilder stringBuffer = initializeWriter();

	public static StringBuilder initializeWriter() {
		/* -- TEMPORARY DISABLED
		// called only once, so make the directory here
		new File(LOG_PATH).mkdir();
		*/
		return new StringBuilder(BUFFER_SIZE);
		
	}
	
	/**
	 * Log debug message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		nativePrint(DEBUG, tag, msg);
	}

	/**
	 * Log info message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		nativePrint(INFO, tag, msg);
	}

	/**
	 * Log warning message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		nativePrint(WARN, tag, msg);
	}

	/**
	 * Log error message
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		nativePrint(ERROR, tag, msg);
	}

	public static String getStringBuffer() {
		return stringBuffer.toString();
	}
	
	private static void nativePrint(int priority, String tag, String msg) {
		if(priority >= LOG_LEVEL){
			// logMsg contains newline (for ease in writing to file)
			
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			String logMsg = sdf.format(new Date()) + " - " + getPriorityTitle(priority) + ": " + tag + "(" + Thread.currentThread().getName() + ") - " + msg + "\n";
			
			System.out.print(logMsg);			
		}
	}
	
	/**
	 * Returns priority title string. Null if not found.
	 * 
	 * @param priority
	 * @return
	 */
	public static String getPriorityTitle(int priority) {
		String priorityMsg = null;
		switch (priority) {
			case VERBOSE :
				priorityMsg = "VERBOSE";
				break;
			case DEBUG :
				priorityMsg = "DEBUG";
				break;
			case INFO :
				priorityMsg = "INFO";
				break;
			case WARN :
				priorityMsg = "WARN";
				break;
			case ERROR :
				priorityMsg = "ERROR";
				break;
			case ASSERT :
				priorityMsg = "ASSERT";
				break;
			default :
				break;
		}

		return priorityMsg;
	}

/*	*//**
	 * Returns string representation of stacktrace
	 * 
	 * @param tr
	 * @return
	 *//*
	public static String getStackTraceString(Throwable tr) {
		if (tr == null) {
			return "";
		}

		// This is to reduce the amount of log spew that apps do in the
		// non-error
		// condition of the network being unavailable.
		Throwable t = tr;
		while (t != null) {
			if (t instanceof UnknownHostException) {
				return "";
			}
			t = t.getCause();
		}

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, false);
		tr.printStackTrace(pw);
		pw.flush();
		return sw.toString();
	}*/
}
