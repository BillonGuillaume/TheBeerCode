package com.fjbg.thebeercode;

import java.sql.Connection;
import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.myconnections.DBConnection;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public final static int LOGIN_REQUEST = 0;
	public final static int CONNECTION_FAILED = -2;
	public final static String PERSONNE = "personne";
	static Connection connect;
	
	Button bConnection = null;
	Button bInscription = null;
	Button bBeers = null;
	Button bAdditions = null;
	Button bMyVotes = null;
	Button bDisconnection = null;
	Button bProfile = null;
	
	TextView tVWelcome = null;
	TextView tVNameMenu = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_principal_noco);
		DBConnection dbc = new DBConnection();
		try {
			connect = dbc.getConnection();
			PersonneDB.setDbConnect(connect);  // Comment gérer la connection au wifi en pleine application ?
		} catch (Exception e) {
			
		}		
		bConnection = (Button)findViewById(R.id.bConnection);
		bInscription = (Button)findViewById(R.id.bInscription);
		bBeers = (Button)findViewById(R.id.bBeers);
		
		bConnection.setOnClickListener(bConnectionListener);
		bInscription.setOnClickListener(bInscriptionListener);
		bBeers.setOnClickListener(bBeersListener);	
	}
	
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
			
		}
	};
	
	private OnClickListener bBeersListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};
	
	@Override
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
		if  (requestCode == LOGIN_REQUEST) {
			if  (resultCode == RESULT_OK) {
				Toast.makeText(MainActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();
				Intent result = getIntent();
				PersonneDB p=(PersonneDB)result.getParcelableExtra(PERSONNE);
				// TO DO
				// Changer le layout en mode connecté
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Connexion annulée", Toast.LENGTH_SHORT).show();
			}
			if(resultCode == CONNECTION_FAILED) {
				Toast.makeText(this, "Connexion echouée", Toast.LENGTH_SHORT).show();
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
