package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.myconnections.DBConnection;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
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
	ProgressDialog progress;
	static ConnecDB connec;
	
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
		
		try {
			connec = new ConnecDB();
			connec.execute();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Exception  : " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}	
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
	
	private OnClickListener bSearchBeerListener = new OnClickListener() { // TODO gérer quand on se connecte pas
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
			else Toast.makeText(MainActivity.this, "Vous devez être connecté !", Toast.LENGTH_SHORT).show();
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

			bConnection.setOnClickListener(bConnectionListener);
			bInscription.setOnClickListener(bInscriptionListener);
			bSearchBeer.setOnClickListener(bSearchBeerListener);
			
			user = null;
			Toast.makeText(MainActivity.this, "Déconnexion effectuée", Toast.LENGTH_SHORT).show();
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
				Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
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
				bLeave = (Button)findViewById(R.id.bLeave);
				
				bAddBeer.setOnClickListener(bAddBeerListener);
				bSearchBeer.setOnClickListener(bSearchBeerListener);
				bAdditions.setOnClickListener(bAdditionsListener);
				bMyVotes.setOnClickListener(bMyVotesListener);
				bDisconnection.setOnClickListener(bDisconnectionListener);
				bProfile.setOnClickListener(bProfileListener);
				bMyFavorites.setOnClickListener(bMyFavoritesListener);
				bLeave.setOnClickListener(bLeaveListener);
				
				tVNameMenu.setText(" " + user.getLogin());
						
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Connexion annulée", Toast.LENGTH_SHORT).show();
			}
			if(resultCode == CONNECTION_FAILED) {
				Toast.makeText(this, "Connexion echouée", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (requestCode == INSCRIPTION_REQUEST){
			if  (resultCode == RESULT_OK) {
				Toast.makeText(this, "Inscription réussie", Toast.LENGTH_SHORT).show();
				user = (PersonneDB)data.getParcelableExtra(PERSONNE);
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Inscription annulée", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (requestCode == PROFILE_REQUEST){
			if  (resultCode == RESULT_OK) {
				Toast.makeText(this, "Profil modifié", Toast.LENGTH_SHORT).show();
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
	
	public class ConnecDB extends AsyncTask<String, Integer, Boolean>{
		private DBConnection dbconnect = null;
		Boolean exc = false;
		Exception ex;
		
		public ConnecDB() {

		}
		
		protected void onPreExecute(){
			progress = new ProgressDialog(MainActivity.this);
			progress.setMessage("Connexion à internet en cours...");
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
				Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			} else Toast.makeText(MainActivity.this, "Connexion réussie.", Toast.LENGTH_SHORT ).show();
		}
	}

}
