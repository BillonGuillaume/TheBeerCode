package com.fjbg.thebeercode;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class RechercheBiere extends Activity {
	
	Dialog custom;
	Button bFiltre;
	Boolean recherche = false;
	
	// Valeurs du filtre
	String country;
	float degreSup;
	float degreInf;
	String nom;
	float noteSup;
	float noteInf;
	
	// Champs du filtre
	TextView tVPays;
	TextView tVDegreSup;
	TextView tVDegreInf;
	TextView tVNom;
	TextView tVNoteSup;
	TextView tVNoteInf;
	
	EditText eTPays;
	EditText eTDegreSup;
	EditText eTDegreInf;
	EditText eTNom;
	RatingBar rBNoteSup;
	RatingBar rBNoteInf;
	
	Button bRecherche;
	Button bCancel;
	Button bReset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recherche_biere);
		
		bFiltre = (Button)findViewById(R.id.bFiltre);
		
		bFiltre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				custom = new Dialog(RechercheBiere.this);
				custom.setContentView(R.layout.filtre_layout);
				tVPays = (TextView)custom.findViewById(R.id.tVPays);
				tVDegreSup = (TextView)custom.findViewById(R.id.tVDegreSup);
				tVDegreInf = (TextView)custom.findViewById(R.id.tVDegreInf);
				tVNom = (TextView)custom.findViewById(R.id.tVNom);
				tVNoteSup = (TextView)custom.findViewById(R.id.tVNoteSup);
				tVNoteInf = (TextView)custom.findViewById(R.id.tVNoteInf);
				
				eTPays = (EditText)custom.findViewById(R.id.eTPays);
				eTDegreSup = (EditText)custom.findViewById(R.id.eTDegreSup);
				eTDegreInf = (EditText)custom.findViewById(R.id.eTDegreInf);
				eTNom = (EditText)custom.findViewById(R.id.eTNom);
				rBNoteSup = (RatingBar)custom.findViewById(R.id.ratingBarSup);
				rBNoteInf = (RatingBar)custom.findViewById(R.id.ratingBarInf);
				
				bRecherche = (Button)custom.findViewById(R.id.bRecherche);
				bCancel = (Button)custom.findViewById(R.id.bCancel);
				bReset = (Button)custom.findViewById(R.id.bReset);
				custom.setTitle("Filtre de recherche");
				if(recherche) {
					eTPays.setText(country);
					eTNom.setText(nom);
					eTDegreSup.setText(String.valueOf(degreSup));
					eTDegreInf.setText(String.valueOf(degreInf));
					rBNoteSup.setRating(noteSup);
					rBNoteInf.setRating(noteInf);
				}
				bRecherche.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						recherche = true;
						country = eTPays.getText().toString();
						nom = eTNom.getText().toString();noteSup = rBNoteSup.getRating();
						noteInf = rBNoteInf.getRating();
						try {
							degreSup = Float.parseFloat(eTDegreSup.getText().toString());
							degreInf = Float.parseFloat(eTDegreInf.getText().toString());

						} catch (Exception e){
							Log.d("RechercheBiere", "Erreur de parsing");
						}
						custom.dismiss();
					}

				});
				bCancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						custom.dismiss();

					}
				});
				bReset.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						initValues();
						recherche = false;
						eTPays.setText("");
						eTDegreSup.setText("");
						eTDegreInf.setText("");
						eTNom.setText("");
						rBNoteSup.setRating(0);
						rBNoteInf.setRating(0);
					}
				});
				custom.show();

			}
		});
	}		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.recherche_biere, menu);
		return true;
	}
	
	public void initValues() {
		country = null;
		degreSup = 0;
		degreInf = 100;
		nom = null;
		noteSup = 0;
		noteInf = 100;
	}
}
