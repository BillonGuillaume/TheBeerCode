package com.fjbg.thebeercode.model;

import android.os.AsyncTask;

import com.fjbg.thebeercode.myconnections.DBConnection;

public class ConnexionDB extends AsyncTask<String, Integer, Boolean>{
	private DBConnection dbconnect = null;
	private Exception e = null;
	
	public ConnexionDB() {

	}

//	@Override
//	protected void onPreExecute(){
//		progress = new ProgressDialog(MainActivity.this);
//		progress.setMessage("Connexion √  la base de donn√©es en cours...");
//		progress.setCancelable(false);
//		progress.show();
//	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		dbconnect = DBConnection.getInstance();
		try{

			dbconnect.init(dbconnect.getConnection());
		}
		catch(Exception e){
			this.e = e;
		}

		return true;
	}

//	@Override
//	protected void onPostExecute(Boolean result){
//		Intent i;
//		if(progress.isShowing())
//			progress.dismiss();
//		if(this.e!=null){
//			Toast.makeText(MainActivity.this,"Erreur lors de la connexion √  la base de donn√©es : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//			Handler handler = new Handler();
//			handler.postDelayed(new Runnable(){
//				@Override
//				public void run() {
//					finish();
//				}
//
//			}, 2000);
//		}
//		else{
//			switch(valeur){
//			case 0:i=new Intent(MainActivity.this,ActivityInscription.class);
//			startActivity(i);
//			break;
//			case 1:i=new Intent(MainActivity.this,ActivityConnection.class);
//			startActivity(i);
//			break;
//			}
//			finish();
//		}
//	}
}
