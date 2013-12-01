package com.fjbg.thebeercode;

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

public class MonProfil extends Activity {
	
	private EditText edlogin;
	private EditText edpassword;
	private EditText edconfPassword;
	private EditText edmail;
	private EditText edpays;
	
	private Button modifier = null;
	private Button retour = null;
	private PersonneDB p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		Intent i=getIntent();
	    p=(PersonneDB)i.getParcelableExtra(MainActivity.PERSONNE);
	    
		edlogin = (EditText)findViewById(R.id.edlogin);
		edlogin.setText(p.getLogin());
		edpassword = (EditText)findViewById(R.id.edpassword);
		edpassword.setText(p.getMdp());
		edconfPassword = (EditText)findViewById(R.id.edconfPassword);
		edconfPassword.setText(p.getMdp());
		edmail = (EditText)findViewById(R.id.edmail);
		edmail.setText(p.getMail());
		edpays = (EditText)findViewById(R.id.edpays);
		edpays.setText(p.getPays());
		
		modifier = (Button) findViewById(R.id.validerInscription);
		modifier.setText("Modifier");
		modifier.setOnClickListener(modifierListener);
		
		retour = (Button) findViewById(R.id.annulerInscription);
		retour.setText("Retour");
		retour.setOnClickListener(new  View.OnClickListener() {
		@Override
		public void onClick(View v){
			Intent i = new Intent();
			setResult(RESULT_CANCELED, i);
			finish();
		}
		});
	}
	
	private OnClickListener modifierListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Update update = new Update();
			update.execute();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mon_profil, menu);
		return true;
	}
	
	public class Update extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		public Update() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			PersonneDB personne =new PersonneDB();
			personne.setIdPersonne(p.getIdPersonne());
			String login=edlogin.getText().toString();
			personne.setLogin(login);

			String password= edpassword.getText().toString();
			personne.setMdp(password);
			String confPassword= edconfPassword.getText().toString();
			try {
				if(password.compareTo(confPassword)!=0) {
					throw new Exception("Les mots de passe ne correspondent pas !");
				}else {
					String mail= edmail.getText().toString();
					personne.setMail(mail);
					String pays= edpays.getText().toString();
					personne.setPays(pays);

					try {
						personne.update();
						Intent i= new Intent();
						i.putExtra(MainActivity.PERSONNE, personne);
						setResult(RESULT_OK, i);
						finish();
					} catch(Exception e) {
						ex = e;
						exc = true;
					}
				}
			}catch(Exception e) {
				ex = e;
				exc = true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(MonProfil.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}

}
