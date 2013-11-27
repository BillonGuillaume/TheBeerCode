package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.BiereDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RechercheBiere extends Activity {
	
	ListView lvItems;
	int items;
	ArrayAdapter<String> aa;
	Button bBack;
	ArrayList<String> aL;
	ArrayList<BiereDB> listBeers;
	public static final String SELECTEDBEER = "beer";
	
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
						GetBeers getter = new GetBeers();
						getter.execute();
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
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		lvItems.setAdapter(aa);
		
		InitList init = new InitList();
		init.execute();
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
	
	public void loadMore(int offset) {
		GetMoarBeers more = new GetMoarBeers();
		more.execute();
	}
	
	private void addItems(String item) {
		if (item.length()>0){
            this.aa.add(item);
            this.aa.notifyDataSetChanged();
            items++;
        }
	}
	
	private void changeItems(ArrayList<String> list) {
		this.aa.clear();
		items = 0;
		for(String item : list) {
			addItems(item);
		}
	}
	
	public class InitList extends AsyncTask<String, Integer, Boolean>{  // Doit afficher des bières de base
		Boolean exc = false;
		Exception ex;
		
		public InitList() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				addItems("biere"); // TODO Ajouter un throw à la fonction de recup si rien à afficher pour éviter un scroll et un click
				
				lvItems.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						//String selectedItem=(String)arg0.getItemAtPosition(arg2);
						Intent showBeer = new Intent(RechercheBiere.this, AjoutBiere.class); // TODO Lancer l'activité d'affichage de bière
						BiereDB selectedBeer = (BiereDB)listBeers.get(arg2);
						showBeer.putExtra(MesAjouts.SELECTEDBEER, selectedBeer);
						startActivity(showBeer);
						finish();
					}
				});
			}catch(Exception e) {
				ex = e;
				exc = true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(RechercheBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetBeers extends AsyncTask<String, Integer, Boolean>{  // Va chercher les bières en fonction du filtre
		Boolean exc = false;
		Exception ex;
		
		public GetBeers() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				addItems("biere"); // TODO Ajouter un throw à la fonction de recup si rien à afficher pour éviter un scroll et un click
				//changeItems();  // Récupérer la liste correspondant à la recherche puis intialiser la nouvelle liste
				
				lvItems.setOnScrollListener(new EndlessScrollListener() {
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
						loadMore(totalItemsCount);
					}
				});
				
				lvItems.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						//String selectedItem=(String)arg0.getItemAtPosition(arg2);
						Intent showBeer = new Intent(RechercheBiere.this, AjoutBiere.class); // TODO Lancer l'activité d'affichage de bière
						BiereDB selectedBeer = (BiereDB)listBeers.get(arg2);
						showBeer.putExtra(MesAjouts.SELECTEDBEER, selectedBeer);
						startActivity(showBeer);
						finish();
					}
				});
			}catch(Exception e) {
				ex = e;
				exc = true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(RechercheBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoarBeers extends AsyncTask<String, Integer, Boolean>{  // Va chercher plus de bières correspondanes au filtre
		Boolean exc = false;
		Exception ex;
		
		public GetMoarBeers() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {  // TODO Que faire il n'y a plus rien dans la DB ?
			try {
				int nbrAjout;
				for(nbrAjout = 0; nbrAjout <=5; nbrAjout++) {
					addItems("Bière " + (items + 1));
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
				Toast.makeText(RechercheBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}
}
