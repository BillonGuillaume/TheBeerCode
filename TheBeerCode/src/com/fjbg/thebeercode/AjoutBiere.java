package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.BiereDB;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AjoutBiere extends Activity {
	
	private EditText eTnom;
	private EditText eTpays;
	private EditText eTdegre;
	private EditText eTimage;
	
	private Button ajouter = null;
	private Button retour = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajout_biere);
		
		eTnom = (EditText)findViewById(R.id.eTnom);
		eTpays = (EditText)findViewById(R.id.eTpays);
		eTdegre = (EditText)findViewById(R.id.eTdegre);
		eTimage = (EditText)findViewById(R.id.eTimage);
		
		ajouter = (Button) findViewById(R.id.bAjBiere);
		ajouter.setOnClickListener(ajouterListener);
		
		retour = (Button) findViewById(R.id.bBack);
		retour.setOnClickListener(retourListener);
		
	}
	
	private OnClickListener ajouterListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int ok=1;
			BiereDB biere =new BiereDB();
			
			String nomBiere=eTnom.getText().toString();
			biere.setNomBiere(nomBiere);

			String paysBiere= eTpays.getText().toString();
			biere.setPaysBiere(paysBiere);
			
			try{
				float degreBiere= Float.parseFloat(eTdegre.getText().toString());
			}
			catch(Exception e){
				Toast.makeText(AjoutBiere.this, "Le degre doit être un nombre !", Toast.LENGTH_SHORT ).show();
				ok = 0;
			}
			
			String imageBiere= eTimage.getText().toString();
			biere.setCheminImage(imageBiere);
			
			if(ok==1) {

				try {
					biere.create();
					Toast.makeText(AjoutBiere.this, "Bière ajoutée !", Toast.LENGTH_SHORT ).show();
					eTnom.setText("");
					eTpays.setText("");
					eTdegre.setText("");
					eTimage.setText("");
				} catch(Exception e) {
					Toast.makeText(AjoutBiere.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
				}
			}
		}
	};
	
	private OnClickListener retourListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajout_biere, menu);
		return true;
	}

}
