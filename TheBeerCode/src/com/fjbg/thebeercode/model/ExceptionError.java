package com.fjbg.thebeercode.model;

import com.fjbg.thebeercode.R;

import android.util.Log;

public class ExceptionError {
	private String text;
	private String exceptionMessage;
	private int code;
	
	public ExceptionError(String exception) {
		String[] result;
		try {
		result = exception.split("/");
		text = result[0];
		code = Integer.parseInt(result[1]);
		exceptionMessage = result[2];
		}catch(Exception e) {
			text = "Erreur inconnue";
			code = R.string.unknown;
			exceptionMessage = "Exception inconnue";
		}
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
