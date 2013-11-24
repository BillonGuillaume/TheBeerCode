package com.fjbg.thebeercode.model;

import android.util.Log;

public class ExceptionError {
	private String text;
	private String exceptionMessage;
	private int code;
	
	public ExceptionError(String exception) {
		String[] result;
		result = exception.split("/");
		text = result[0];
		code = Integer.parseInt(result[1]);
		exceptionMessage = result[2];
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
	
	public void doLog() {
		Log.d("ExceptionError", "Text : " + text + ". Message : " + exceptionMessage + ". Code : " + code);
	}
}
