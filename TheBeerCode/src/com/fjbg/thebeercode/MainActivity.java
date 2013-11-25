package com.fjbg.thebeercode;

import java.sql.Connection;

import com.fjbg.thebeercode.model.ConnexionDB;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
	public final static String PERSONNE = "personne";
	static Connection connect;
	static ConnexionDB connec;
	
	Button bConnection = null;
	Button bInscription = null;
	Button bBeers = null;
	Button bAdditions = null;
	Button bMyVotes = null;
	Button bDisconnection = null;
	Button bProfile = null;
	
	TextView tVWelcome = null;
	TextView tVNameMenu = null;
	
	PersonneDB user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal_noco);
				
		bConnection = (Button)findViewById(R.id.bConnection);
		bInscription = (Button)findViewById(R.id.bInscription);
		bBeers = (Button)findViewById(R.id.bBeers);
		
		bConnection.setOnClickListener(bConnectionListener);
		bInscription.setOnClickListener(bInscriptionListener);
		bBeers.setOnClickListener(bBeersListener);
		
		try {
			connec = new ConnexionDB();
			connec.execute();
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Exception  : " + e.getMessage(), Toast.LENGTH_SHORT).show();
		}	
	}
	
	private OnClickListener bConnectionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			PersonneDB p = new PersonneDB(1, "Login", "mdp", "email", "pays");
//			try {
//			p.create();
//			}catch (Exception e) {
//				Log.d("MainActivity", "Exception create : " + e.getMessage());
//			}
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
	
	private OnClickListener bBeersListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(user!=null){
				Intent BeersActivity = new  Intent(MainActivity.this,AjoutBiere.class);
				startActivity(BeersActivity);
			}
			else Toast.makeText(MainActivity.this, "Vous devez être connecté !", Toast.LENGTH_SHORT).show();
		}
	};
	
	private OnClickListener bAdditionsListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent AdditionsActivity = new  Intent(MainActivity.this,MesAjouts.class);
			startActivity(AdditionsActivity);
		}
	};
	
	private OnClickListener bMyVotesListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			Intent SignUpActivity = new  Intent(MainActivity.this,Inscription.class);
//			startActivityForResult(SignUpActivity,INSCRIPTION_REQUEST);
		}
	};
	
	private OnClickListener bDisconnectionListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setContentView(R.layout.menu_principal_noco);

			bConnection = (Button)findViewById(R.id.bConnection);
			bInscription = (Button)findViewById(R.id.bInscription);
			bBeers = (Button)findViewById(R.id.bBeers);

			bConnection.setOnClickListener(bConnectionListener);
			bInscription.setOnClickListener(bInscriptionListener);
			bBeers.setOnClickListener(bBeersListener);
			
			user = null;
			Toast.makeText(MainActivity.this, "Déconnexion effectuée", Toast.LENGTH_SHORT).show();
		}
	};
	
	private OnClickListener bProfileListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			Intent SignUpActivity = new  Intent(MainActivity.this,Inscription.class);
//			startActivityForResult(SignUpActivity,INSCRIPTION_REQUEST);
			Intent ProfileActivity = new  Intent(MainActivity.this,MonProfil.class);
			ProfileActivity.putExtra(PERSONNE, user);
			startActivityForResult(ProfileActivity,PROFILE_REQUEST);
		}
	};
	
	@Override
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		if  (requestCode == LOGIN_REQUEST) {
			if  (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
				user = (PersonneDB)data.getParcelableExtra(PERSONNE);
				// TODO Changer le layout en mode connecté
				setContentView(R.layout.menu_principal_co);
				tVWelcome = (TextView)findViewById(R.id.tVWelcome);
				tVNameMenu = (TextView)findViewById(R.id.tVNameMenu);
				bBeers = (Button)findViewById(R.id.bBeers);
				bAdditions = (Button)findViewById(R.id.bAdditions);
				bMyVotes = (Button)findViewById(R.id.bMyVotes);
				bDisconnection = (Button)findViewById(R.id.bDisconnection);
				bProfile = (Button)findViewById(R.id.bProfile);
				
				bBeers.setOnClickListener(bBeersListener);
				bAdditions.setOnClickListener(bAdditionsListener);
				bMyVotes.setOnClickListener(bMyVotesListener);
				bDisconnection.setOnClickListener(bDisconnectionListener);
				bProfile.setOnClickListener(bProfileListener);
				
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
				Intent result = getIntent();
				user = (PersonneDB)result.getParcelableExtra(PERSONNE);
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Inscription annulée", Toast.LENGTH_SHORT).show();
			}
		}
		
		if (requestCode == PROFILE_REQUEST){
			if  (resultCode == RESULT_OK) {
				Toast.makeText(this, "Profil modifié", Toast.LENGTH_SHORT).show();
				Intent result = getIntent();
				user = (PersonneDB)result.getParcelableExtra(PERSONNE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
