package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.FavoriDB;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MesFavoris extends Activity {
	
	ListView lvItems;
	int items;
	ArrayAdapter<String> aa;
	Button bBack;
	ArrayList<FavoriDB> listFavorites;
	public static final String SELECTEDFAV = "FAV";
	PersonneDB user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_ajouts);
		
		Intent data = getIntent();
		user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);
		
		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(bBackListener);
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		lvItems.setAdapter(aa);
		
		GetFavs getter = new GetFavs();		
		getter.execute();
	}
	
	private OnClickListener bBackListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mes_ajouts, menu);
		return true;
	}
	
	public void loadMore(int offset) {
		GetMoarFavs getMore = new GetMoarFavs();
		getMore.execute();
	}
	
	private void addItems(String item) {
		if (item.length()>0){
            this.aa.add(item);
            this.aa.notifyDataSetChanged();
            items++;
        }
	}
	
	public class GetFavs extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		public GetFavs() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				addItems("Favori"); // TODO Ajouter un throw à la fonction de recup si rien à afficher pour éviter un scroll et un click
				
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
						Intent showFav = new Intent(MesFavoris.this, AjoutBiere.class); // TODO Lancer l'activité d'affichage de bière
						FavoriDB selectedFav = (FavoriDB)listFavorites.get(arg2);
						showFav.putExtra(MesFavoris.SELECTEDFAV, selectedFav);
						startActivity(showFav);
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
				Toast.makeText(MesFavoris.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoarFavs extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		public GetMoarFavs() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {  // TODO Que faire il n'y a plus rien dans la DB ?
			try {
				int nbrAjout;
				for(nbrAjout = 0; nbrAjout <=5; nbrAjout++) {
					addItems("Favori " + (items + 1));
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
				Toast.makeText(MesFavoris.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
