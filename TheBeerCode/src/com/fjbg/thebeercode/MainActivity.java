package com.fjbg.thebeercode;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
