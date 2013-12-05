package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.myconnections.DBConnection;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static int LOGIN_REQUEST = 4;
	public final static int INSCRIPTION_REQUEST = 2;
	public final static int CONNECTION_FAILED = -2;
	public final static int PROFILE_REQUEST = 5;
	public final static String USER = "USER";
	public final static String PERSONNE = "PERSONNE";
	static ConnectDB connec;
	ProgressDialog progress;
	
	Button bConnection = null;
	Button bInscription = null;
	Button bAddBeer = null;
	Button bSearchBeer = null;
	Button bAdditions = null;
	Button bMyVotes = null;
	Button bMyFavorites = null;
	Button bDisconnection = null;
	Button bProfile = null;
	Button bLeave = null;
	
	TextView tVWelcome = null;
	TextView tVNameMenu = null;
	
	PersonneDB user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal_noco);
				
		bConnection = (Button)findViewById(R.id.bConnection);
		bInscription = (Button)findViewById(R.id.bInscription);
		bSearchBeer = (Button)findViewById(R.id.bSearchBeer);
		
		bLeave = (Button)findViewById(R.id.bLeave);
		bLeave.setOnClickListener(bLeaveListener);
		
		bConnection.setOnClickListener(bConnectionListener);
		bInscription.setOnClickListener(bInscriptionListener);
		bSearchBeer.setOnClickListener(bSearchBeerListener);
		
		initConnec();

		
	}
	
	private OnClickListener bLeaveListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
			}
	};
	
	private OnClickListener bConnectionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent loginActivity = new  Intent(MainActivity.this,Login.class);
			startActivityForResult(loginActivity,LOGIN_REQUEST);
			}
	};
	
	private OnClickListener bInscriptionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent SignUpActivity = new  Intent(MainActivity.this,Inscription.class);
			startActivityForResult(SignUpActivity,INSCRIPTION_REQUEST);
		}
	};
	
	private OnClickListener bSearchBeerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent searchBeerActivity = new  Intent(MainActivity.this,RechercheBiere.class);
			searchBeerActivity.putExtra(PERSONNE, user);
			startActivity(searchBeerActivity);
		}
	};
	
	private OnClickListener bAddBeerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(user!=null){
				Intent addBeerActivity = new  Intent(MainActivity.this,AjoutBiere.class);
				addBeerActivity.putExtra(PERSONNE, user);
				startActivity(addBeerActivity);
			}
			else Toast.makeText(MainActivity.this, getResources().getString(R.string.NeedCo), Toast.LENGTH_SHORT).show();
		}
	};
	
	private OnClickListener bAdditionsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent AdditionsActivity = new  Intent(MainActivity.this,MesAjouts.class);
			AdditionsActivity.putExtra(PERSONNE, user);
			startActivity(AdditionsActivity);
		}
	};
	
	private OnClickListener bMyVotesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent MyVotesActivity = new  Intent(MainActivity.this,MesVotes.class);
			MyVotesActivity.putExtra(PERSONNE, user);
			startActivity(MyVotesActivity);
		}
	};
	
	private OnClickListener bDisconnectionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setContentView(R.layout.menu_principal_noco);

			bConnection = (Button)findViewById(R.id.bConnection);
			bInscription = (Button)findViewById(R.id.bInscription);
			bSearchBeer = (Button)findViewById(R.id.bSearchBeer);
			bLeave = (Button)findViewById(R.id.bLeave);

			bConnection.setOnClickListener(bConnectionListener);
			bInscription.setOnClickListener(bInscriptionListener);
			bSearchBeer.setOnClickListener(bSearchBeerListener);
			bLeave.setOnClickListener(bLeaveListener);
			
			user = null;
			Toast.makeText(MainActivity.this, getResources().getString(R.string.DiscSuccess), Toast.LENGTH_SHORT).show();
		}
	};
	
	private OnClickListener bProfileListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent ProfileActivity = new  Intent(MainActivity.this,MonProfil.class);
			ProfileActivity.putExtra(PERSONNE, user);
			startActivityForResult(ProfileActivity,PROFILE_REQUEST);
		}
	};
	
	private OnClickListener bMyFavoritesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent mesFavoris = new  Intent(MainActivity.this,MesFavoris.class);
			mesFavoris.putExtra(PERSONNE, user);
			startActivity(mesFavoris);
		}
	};
	
	@Override
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		if  (requestCode == LOGIN_REQUEST) {
			if  (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, getResources().getString(R.string.Connected), Toast.LENGTH_SHORT).show();
				user = (PersonneDB)data.getParcelableExtra(PERSONNE);
				setContentView(R.layout.menu_principal_co);
				tVWelcome = (TextView)findViewById(R.id.tVWelcome);
				tVNameMenu = (TextView)findViewById(R.id.tVNameMenu);
				bAddBeer = (Button)findViewById(R.id.bAddBeer);
				bSearchBeer = (Button)findViewById(R.id.bSearchBeer);
				bAdditions = (Button)findViewById(R.id.bAdditions);
				bMyVotes = (Button)findViewById(R.id.bMyVotes);
				bDisconnection = (Button)findViewById(R.id.bDisconnection);
				bProfile = (Button)findViewById(R.id.bProfile);
				bMyFavorites = (Button)findViewById(R.id.bMyFavorites);
				
				bAddBeer.setOnClickListener(bAddBeerListener);
				bSearchBeer.setOnClickListener(bSearchBeerListener);
				bAdditions.setOnClickListener(bAdditionsListener);
				bMyVotes.setOnClickListener(bMyVotesListener);
				bDisconnection.setOnClickListener(bDisconnectionListener);
				bProfile.setOnClickListener(bProfileListener);
				bMyFavorites.setOnClickListener(bMyFavoritesListener);
				
				tVNameMenu.setText(" " + user.getLogin());
						
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getResources().getString(R.string.ConnCanceled), Toast.LENGTH_SHORT).show();
			}
			if(resultCode == CONNECTION_FAILED) {
				Toast.makeText(this, getResources().getString(R.string.ConnFailed), Toast.LENGTH_SHORT).show();
			}
		}
		
		if (requestCode == INSCRIPTION_REQUEST){
			if  (resultCode == RESULT_OK) {
				Toast.makeText(this, getResources().getString(R.string.SignUpSuccess), Toast.LENGTH_SHORT).show();
				user = (PersonneDB)data.getParcelableExtra(PERSONNE);
				setContentView(R.layout.menu_principal_co);
				tVWelcome = (TextView)findViewById(R.id.tVWelcome);
				tVNameMenu = (TextView)findViewById(R.id.tVNameMenu);
				bAddBeer = (Button)findViewById(R.id.bAddBeer);
				bSearchBeer = (Button)findViewById(R.id.bSearchBeer);
				bAdditions = (Button)findViewById(R.id.bAdditions);
				bMyVotes = (Button)findViewById(R.id.bMyVotes);
				bDisconnection = (Button)findViewById(R.id.bDisconnection);
				bProfile = (Button)findViewById(R.id.bProfile);
				bMyFavorites = (Button)findViewById(R.id.bMyFavorites);
				
				bAddBeer.setOnClickListener(bAddBeerListener);
				bSearchBeer.setOnClickListener(bSearchBeerListener);
				bAdditions.setOnClickListener(bAdditionsListener);
				bMyVotes.setOnClickListener(bMyVotesListener);
				bDisconnection.setOnClickListener(bDisconnectionListener);
				bProfile.setOnClickListener(bProfileListener);
				bMyFavorites.setOnClickListener(bMyFavoritesListener);
				
				tVNameMenu.setText(" " + user.getLogin());
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, getResources().getString(R.string.SignUpCanceled), Toast.LENGTH_SHORT).show();
			}
		}
		
		if (requestCode == PROFILE_REQUEST){
			if  (resultCode == RESULT_OK) {
				Toast.makeText(this, getResources().getString(R.string.ProfileMod), Toast.LENGTH_SHORT).show();
				user = (PersonneDB)data.getParcelableExtra(PERSONNE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public class ConnectDB extends AsyncTask<String, Integer, Boolean>{
		private DBConnection dbconnect = null;
		Boolean exc = false;
		Exception ex;
		
		public ConnectDB() {

		}
		
		@Override
		protected void onPreExecute(){
			progress = new ProgressDialog(MainActivity.this);
			progress.setMessage(getResources().getString(R.string.connection));
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			dbconnect = DBConnection.getInstance();
			try{
				dbconnect.init(dbconnect.getConnection());
			}
			catch(Exception e){
				ex = e;
				exc = true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(progress.isShowing())
			progress.dismiss();
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(MainActivity.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT).show();				
			} else Toast.makeText(MainActivity.this, getResources().getString(R.string.ConnecSuccess), Toast.LENGTH_SHORT).show();
		}		
	}
	
	private void customAlert() {
		try {
		final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage(getResources().getString(R.string.NetworkDisabled))
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					public void run() {
						initConnec();

					}
				}, 10000);

			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
				Toast.makeText(MainActivity.this, getResources().getString(R.string.NetworkDisabled2), Toast.LENGTH_LONG).show();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
		}catch(Exception e) {
		}

	}
	
	private void initConnec() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

		//For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		//For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

		if (!is3g && !isWifi){ 
			customAlert();
		} else {
			connec = new ConnectDB();
			connec.execute();
		} 
	}

}
