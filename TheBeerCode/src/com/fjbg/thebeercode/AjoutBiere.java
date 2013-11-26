package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.BiereDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AjoutBiere extends Activity {
	
	EditText eTnom;
	EditText eTpays;
	EditText eTdegre;
	EditText eTimage;
	
	Button ajouter = null;
	Button retour = null;
	
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
			Ajout aj = new Ajout();
			aj.execute();
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
	
	public class Ajout extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		Exception ex;
		
		public Ajout() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			BiereDB biere =new BiereDB();
			
			String nomBiere=eTnom.getText().toString();
			biere.setNomBiere(nomBiere);

			String paysBiere= eTpays.getText().toString();
			biere.setPaysBiere(paysBiere);
			try{
				float degreBiere= Float.parseFloat(eTdegre.getText().toString());
				biere.setDegreBiere(degreBiere);
			}
			catch(Exception e){
				Toast.makeText(AjoutBiere.this, "Le degre doit être un nombre !", Toast.LENGTH_SHORT ).show();
				exc = true;
				ex = e;
			}
			
			String imageBiere= eTimage.getText().toString();
			biere.setCheminImage(imageBiere);
			if(!exc) {
				try {
					biere.create();
				} catch(Exception e) {
					ex = e;
					exc = true;
				}
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(AjoutBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else {
				Toast.makeText(AjoutBiere.this, "Bière ajoutée !", Toast.LENGTH_SHORT ).show();
				eTnom.setText("");
				eTpays.setText("");
				eTdegre.setText("");
				eTimage.setText("");
			}
			
		}
	}

}
