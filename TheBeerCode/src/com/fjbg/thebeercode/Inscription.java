package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.PersonneDB;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Inscription extends Activity {
	
	private EditText edlogin;
	private EditText edpassword;
	private EditText edconfPassword;
	private EditText edmail;
	private EditText edpays;
	
	private  Button valider = null;
	private  Button annuler = null;
	private PersonneDB personne;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inscription);
		
		Intent i = getIntent();
		PersonneDB personne = i.getParcelableExtra(MainActivity.PERSONNE);
		
		
		valider = (Button) findViewById(R.id.validerInscription);
		valider.setOnClickListener(new  View.OnClickListener() {
		@Override
		public void onClick(View v){
			PersonneDB personne =new PersonneDB();
			try{
				String login=edlogin.getText().toString();
				personne.setLogin(login);
			    
				try{
			    	String password= edpassword.getText().toString();
			    	personne.setMdp(password);
			    	String confPassword= edconfPassword.getText().toString();
			    	if(password.compareTo(confPassword)!=0) throw new Exception();
			    	
			    }
			    catch(Exception e){
			    	throw new Exception("Les mots de passe ne correspondent pas !",e);
			    }
			    
			    String mail= edmail.getText().toString();
				personne.setMail(mail);

			    String pays= edpays.getText().toString();
				personne.setPays(pays);
				
				personne.create();
				
			    Intent i= new Intent();
				i.putExtra(MainActivity.PERSONNE, personne);
				setResult(RESULT_OK, i);
				finish();
			}
			catch(Exception e){
				Toast.makeText(Inscription.this,e.getMessage(),Toast.LENGTH_SHORT ).show();
			}
		}
		}
		);
		
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inscription, menu);
		return true;
	}

}
