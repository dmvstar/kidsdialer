package net.sf.dvstar.kidsdialer.utils;

import java.io.IOException;

import android.content.pm.PackageManager.NameNotFoundException;


public class Log implements Commons {

	public Log() {
	}
	
	public static void v(String message) {
		android.util.Log.v(TAG, message);
	}

	public static void d(String message) {
		android.util.Log.d(TAG, message);	
	}

	public static void e(String message) {
		android.util.Log.e(TAG, message);	
	}
	
	public static void e(Throwable throwses) {
		android.util.Log.e(TAG, "Exception", throwses);	
	}
	
	public static void i(String message) {
		android.util.Log.i(TAG, message);	
	}

	public static void e(String message, IOException e) {
		android.util.Log.e(TAG, message, e);	
	}

	public static void d(Throwable e) {
		android.util.Log.e(TAG, "",  e);	
	}
	
}
