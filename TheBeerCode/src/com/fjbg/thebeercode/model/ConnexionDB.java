package com.fjbg.thebeercode.model;

import android.os.AsyncTask;

import com.fjbg.thebeercode.myconnections.DBConnection;

public class ConnexionDB extends AsyncTask<String, Integer, Boolean>{
	private DBConnection dbconnect = null;
	
	public ConnexionDB() {

	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		dbconnect = DBConnection.getInstance();
		try{
			dbconnect.init(dbconnect.getConnection());
		}
		catch(Exception e){
		}
		return true;
	}
}
