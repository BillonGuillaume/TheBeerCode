package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	
	TextView tVLogin = null;
	EditText eTLogin = null;
	TextView tVPwd = null;
	EditText eTPwd = null;
	TextView tVNotYetRegistered = null;
	
	Button bSignIn = null;
	Button bCancel = null;
	public final static int INSCRIPTION_REQUEST = 2;
	
	
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
		tVNotYetRegistered.setOnClickListener(tVNYRListener);
	}
	
	private OnClickListener bSignInListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Connexion co = new Connexion();
			co.execute();
		}
	};
	
	private OnClickListener tVNYRListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent SignUpActivity = new  Intent(Login.this,Inscription.class);
			startActivityForResult(SignUpActivity, INSCRIPTION_REQUEST);
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
	protected  void onActivityResult(int requestCode, int resultCode, Intent data) {		
		if (requestCode == INSCRIPTION_REQUEST){
			if  (resultCode == RESULT_OK) {
				PersonneDB user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);
				Intent i= new Intent();
				i.putExtra(MainActivity.PERSONNE, user);
				setResult(RESULT_OK, i);
				finish();
			}
			if(resultCode == RESULT_CANCELED) {
				Intent i = new Intent();
				setResult(RESULT_CANCELED, i);
				finish();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public class Connexion extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		ExceptionError ee;
		
		public Connexion() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			String login = eTLogin.getText().toString();
			String mdp = eTPwd.getText().toString();
			PersonneDB p = new PersonneDB();
			p.setLogin(login);
			p.setMdp(mdp);
			try {
				if(login.matches("") || mdp.matches("")) throw new Exception("Login ou mdp vide/" + R.string.e209 + "/" + "Login ou mdp vide");
				p.connection();
				Intent result = new Intent();
				result.putExtra(MainActivity.PERSONNE, p);
				setResult(RESULT_OK, result);
				finish();
			} catch (Exception e) {
				exc = true;
				ee = new ExceptionError(e.getMessage());
			}		
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				eTLogin.setText("");
				eTPwd.setText("");
				Toast.makeText(Login.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT).show();				
			}
		}
	}

}
