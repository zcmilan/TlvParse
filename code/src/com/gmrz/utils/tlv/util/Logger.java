package com.gmrz.utils.tlv.util;

public class Logger {
	
	public static void d(String TAG,String msg){
		System.out.print("debug/ " + TAG + " " + msg + "\n");
	}
	
	public static void i(String TAG,String msg){
		System.out.print("info/ " + TAG + " " + msg + "\n");
	}

	public static void v(String TAG,String msg){
		System.out.print("verbos/ " + TAG + " " + msg + "\n");
	}

	public static void e(String TAG,String msg){
		System.out.print("error/ " + TAG + " " + msg + "\n");
	}

	public static void f(String TAG,String msg){
		System.out.print("fatal/ " + TAG + " " + msg + "\n");
	}
}
