package com.fjbg.thebeercode.model;

import android.os.AsyncTask;
import android.util.Log;

import com.fjbg.thebeercode.myconnections.DBConnection;

public class ConnexionDB extends AsyncTask<String, Integer, Boolean>{
	private DBConnection dbconnect = null;
	private Exception e = null;
	
	public ConnexionDB() {

	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		Log.d("ConnexionDB", "doInBackground");
		dbconnect = DBConnection.getInstance();
		try{
			dbconnect.init(dbconnect.getConnection());
		}
		catch(Exception e){
			this.e = e;
		}
		return true;
	}
	
	protected void onPostExecute(Boolean result){
		super.onPostExecute(result);
		Log.d("ConnexionDB", "onPostExecute");
	}
}
