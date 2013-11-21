package com.fjbg.thebeercode;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
	
	TextView tVLogin = null;
	EditText eTLogin = null;
	TextView tVPwd = null;
	EditText eTPwd = null;
	TextView tVNotYetRegistered = null;
	
	Button bSignIn = null;
	Button bCancel = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tVLogin = (TextView)findViewById(R.id.tVLogin);
		eTLogin = (EditText)findViewById(R.id.eTLogin);
		tVPwd = (TextView)findViewById(R.id.tVPwd);
		eTPwd = (EditText)findViewById(R.id.eTPwd);
		tVNotYetRegistered = (TextView)findViewById(R.id.tVNotRegistered);
		bSignIn = (Button)findViewById(R.id.bSignIn);
		bCancel = (Button)findViewById(R.id.bCancel);
		
		bSignIn.setOnClickListener(bSignInListener);
		bCancel.setOnClickListener(bCancelListener);
	}
	
	private OnClickListener bSignInListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String login = eTLogin.getText().toString();
			String mdp = eTPwd.getText().toString();
			Intent result = new  Intent();
			// TO DO
			// Vérifier si le compte est OK et renvoyer la Personne
			//result.putExtra();
			setResult(RESULT_OK, result);
			setResult(MainActivity.CONNECTION_FAILED);
			finish();
		}
	};
	
	private OnClickListener bCancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
