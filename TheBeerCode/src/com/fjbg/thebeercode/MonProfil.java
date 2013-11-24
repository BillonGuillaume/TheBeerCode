package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.PersonneDB;


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
			PersonneDB personne =new PersonneDB();
			personne.setIdPersonne(p.getIdPersonne());
			String login=edlogin.getText().toString();
			personne.setLogin(login);

			String password= edpassword.getText().toString();
			personne.setMdp(password);
			String confPassword= edconfPassword.getText().toString();
			if(password.compareTo(confPassword)!=0) {
				Toast.makeText(MonProfil.this, "Les mots de passe ne correspondent pas !", Toast.LENGTH_SHORT ).show();
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
					Toast.makeText(MonProfil.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
				}
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mon_profil, menu);
		return true;
	}

}
