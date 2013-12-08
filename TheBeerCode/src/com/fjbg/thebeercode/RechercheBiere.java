package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.BiereDB;
import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RechercheBiere extends Activity {
	
	ListView lvItems;
	int items = 0;
	RechercheAdapter rA;
	Button bBack;
	ArrayList<String> aL;
	ArrayList<BiereDB> listResult;
	public static final String SELECTEDBEER = "BEER";
	public static final String USER = "USER";
	Boolean scroll = false;
	
	Dialog custom;
	Button bFiltre;
	Boolean recherche = false;
	
	PersonneDB user;
	
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
		
		Intent data = getIntent();		
		user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);
		
		bFiltre = (Button)findViewById(R.id.bFiltre);
		bBack = (Button)findViewById(R.id.bBack);
		
		bBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
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
				
				rBNoteInf.setRating(5);
				
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
						nom = eTNom.getText().toString();
						noteSup = rBNoteSup.getRating();
						noteInf = rBNoteInf.getRating();
						String degSup = eTDegreSup.getText().toString();						
						try {							
							if(degSup != "") degreSup = Float.parseFloat(degSup);
						} catch (Exception e){
							degreSup = (float) 0.0;
						}
						String degInf = eTDegreInf.getText().toString();
						try {							
							if(degInf != "") degreInf = Float.parseFloat(degInf);
						} catch (Exception e){
							degreInf = (float) 99.0;
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
						rBNoteInf.setRating(5);
					}
				});
				custom.show();
			}
		});
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		listResult = new ArrayList<BiereDB>();
		rA = new RechercheAdapter(RechercheBiere.this, listResult);
		lvItems.setAdapter(rA);
		lvItems.setOnScrollListener(new EndlessScrollListener(1) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMore(totalItemsCount);
			}
		});

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
		GetMoar more = new GetMoar();
		more.execute();
	}
	
	public void loadMoreFiltered(int offset) {
		GetMoarBeers more = new GetMoarBeers();
		more.execute();
	}
	
	private void addItems(BiereDB item) {
		this.rA.add(item);
		this.rA.notifyDataSetChanged();
		items++;
	}
	
	private void changeItems(ArrayList<BiereDB> list) {
		this.rA.clear();
		items = 0;
		for(BiereDB item : list) {
			addItems(item);
		}
	}
	
	public class InitList extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<BiereDB> liste = new ArrayList<BiereDB>();
		
		public InitList() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				liste = BiereDB.readBieres(1, 5);
				
				lvItems.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						try {
						Intent showBeer = new Intent(RechercheBiere.this, AffichageBiere.class);
						String selectedBeer = (listResult.get(arg2)).getNomBiere();
						showBeer.putExtra(RechercheBiere.SELECTEDBEER, selectedBeer);
						showBeer.putExtra(RechercheBiere.USER, user);
						startActivity(showBeer);
						finish();
						}catch(Exception e) {
							ex = e;
							exc = true;
						}
					}
				});
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll = false;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			for(BiereDB vote : liste) {
				addItems(vote);
			}
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(RechercheBiere.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoar extends AsyncTask<String, Integer, Boolean>{  // Va chercher plus de bières sans filtre
		Boolean exc = false;
		Exception ex;
		ArrayList<BiereDB> liste;
		
		public GetMoar() {

		}
		
		protected void onPreExecute(){
            liste = new ArrayList<BiereDB>(); 
       }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				liste = BiereDB.readBieres(items+1, items+3);
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll = false;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			for(BiereDB item : liste) {
				addItems(item);
			}
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				if(ee.getCode() != R.string.e206)	Toast.makeText(RechercheBiere.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}
		}
	}
	
	public class GetBeers extends AsyncTask<String, Integer, Boolean>{  // Va chercher les bières en fonction du filtre
		Boolean exc = false;
		Exception ex;
		ArrayList<BiereDB> liste = new ArrayList<BiereDB>();
		
		public GetBeers() {

		}
		
		@Override
        protected void onPreExecute(){
			scroll = true;
        }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				liste = BiereDB.rechBieres(nom, degreInf, degreSup, country, noteSup, noteInf, 1, 5);
				
//				lvItems.setOnScrollListener(new EndlessScrollListener() {
//					@Override
//					public void onLoadMore(int page, int totalItemsCount) {
//						loadMoreFiltered(totalItemsCount);
//					}
//				});
				
				lvItems.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						Intent showBeer = new Intent(RechercheBiere.this, AffichageBiere.class);
						String selectedBeer = (listResult.get(arg2)).getNomBiere();
						showBeer.putExtra(MesVotes.SELECTEDBEER, selectedBeer);
						showBeer.putExtra(MesVotes.USER, user);
						startActivity(showBeer);
						finish();
					}
				});
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll = false;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			changeItems(liste);
			lvItems.setOnScrollListener(new EndlessScrollListener() {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					loadMoreFiltered(totalItemsCount);
				}
			});
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(RechercheBiere.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoarBeers extends AsyncTask<String, Integer, Boolean>{  // Va chercher plus de bières correspondanes au filtre
		Boolean exc = false;
		Exception ex;
		ArrayList<BiereDB> liste;
		
		public GetMoarBeers() {

		}
		
		protected void onPreExecute(){
            liste = new ArrayList<BiereDB>(); 
       }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				liste = BiereDB.rechBieres(nom, degreInf, degreSup, country, noteSup, noteInf, items+1, items+6);				
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll = false;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			for(BiereDB item : liste) {
				addItems(item);
			}
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				if(ee.getCode() != R.string.e205) Toast.makeText(RechercheBiere.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}
		}
	}
}
