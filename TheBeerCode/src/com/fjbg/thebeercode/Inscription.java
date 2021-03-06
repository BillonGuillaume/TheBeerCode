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
import android.widget.Toast;

public class Inscription extends Activity {
	
	private EditText edlogin;
	private EditText edpassword;
	private EditText edconfPassword;
	private EditText edmail;
	private EditText edpays;
	
	private Button valider = null;
	private Button annuler = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		edlogin = (EditText)findViewById(R.id.edlogin);
		edpassword = (EditText)findViewById(R.id.edpassword);
		edconfPassword = (EditText)findViewById(R.id.edconfPassword);
		edmail = (EditText)findViewById(R.id.edmail);
		edpays = (EditText)findViewById(R.id.edpays);
		
		valider = (Button) findViewById(R.id.validerInscription);
		valider.setOnClickListener(validerListener);
		
		annuler = (Button) findViewById(R.id.annulerInscription);
		annuler.setOnClickListener(new  View.OnClickListener() {
		@Override
		public void onClick(View v){			
			Intent i = new Intent();
			setResult(RESULT_CANCELED, i);
			finish();			
		}
		});
	}
	
	private OnClickListener validerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Create insc = new Create();
			insc.execute();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inscription, menu);
		return true;
	}
	
	public class Create extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		public Create() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			PersonneDB personne =new PersonneDB();
			
			String login = null;
			login=edlogin.getText().toString();
			personne.setLogin(login);

			String password= null;
			password = edpassword.getText().toString();
			personne.setMdp(password);
			
			String confPassword= null;
			confPassword = edconfPassword.getText().toString();
			
			String mail= null;
			mail= edmail.getText().toString();
			personne.setMail(mail);
			
			String pays= null;
			pays= edpays.getText().toString();
			personne.setPays(pays);
			try {
				if(login.matches("") || password.matches("") || confPassword.matches("") || mail.matches("") || pays.matches("")){
					throw new Exception("Exception personnalisée/" + R.string.e218 + "/" + "Tous les champs doivent être remplis !");
				}
				if(password.compareTo(confPassword)!=0) {
					throw new Exception("Exception personnalisée/" + R.string.e214 + "/" + "Mots de passe ne correspondent pas");
				}
			}
			catch(Exception e){
				ex = e;
				exc = true;
			}
	
			if(!exc){	
				try {
					personne.create();
					Intent i= new Intent();
					i.putExtra(MainActivity.PERSONNE, personne);
					setResult(RESULT_OK, i);
					finish();
				 } catch(Exception e){
					ex = e;
					exc = true;
				 }
			}
			
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(Inscription.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}			
		}
	}

}
