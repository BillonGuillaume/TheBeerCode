package com.fjbg.thebeercode.model;

import android.util.Log;

public class ExceptionError {
	private String text;
	private String exceptionMessage;
	private int code;
	
	public ExceptionError(String exception) {
		Log.d("ExceptionError", "Creation exception");
		String[] result;
		result = exception.split("/");
		Log.d("ExceptionError", "size : " + result.length);
		text = result[0];
		Log.d("ExceptionError", "text ok");
		code = Integer.parseInt(result[1]);
		Log.d("ExceptionError", "code ok");
		exceptionMessage = result[2];
		Log.d("ExceptionError", "text : " + text);
		Log.d("ExceptionError", "code : " + code);
		Log.d("ExceptionError", "message : " + exceptionMessage);
	}
	
	public String getText() {
		return text;
	}

	public String getMessage() {
		return exceptionMessage;
	}

	public int getCode() {
		return code;
	}
}
