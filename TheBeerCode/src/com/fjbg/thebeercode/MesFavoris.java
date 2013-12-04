package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.model.VueFavoriDB;

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
	ArrayList<String> listFav;
	public static final String SELECTEDBEER = "BEER";
	public static final String USER = "USER";
	PersonneDB user;
	Boolean scroll = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_favoris);
		
		Intent data = getIntent();
		user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);
		
		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(bBackListener);
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		listFav = new ArrayList<String>();
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listFav);
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
		if(scroll) {
			GetMoarFavs getMore = new GetMoarFavs();
			getMore.execute();
		}
		
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
		ArrayList<String> list = new ArrayList<String>();
		
		public GetFavs() {
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				list = VueFavoriDB.readFavPersonne(user.getIdPersonne(), 1, 5);
				
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
						Intent showFav = new Intent(MesFavoris.this, AffichageBiere.class);
						String selectedFav = (String)aa.getItem(arg2);
						showFav.putExtra(MesFavoris.SELECTEDBEER, selectedFav);
						showFav.putExtra(MesFavoris.USER, user);
						startActivity(showFav);
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
			for(String item : list) {
				addItems(item);
			}
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(MesFavoris.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoarFavs extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<String> list = new ArrayList<String>();
		
		public GetMoarFavs() {

		}
		
		@Override
        protected void onPreExecute(){
             list = new ArrayList<String>(); 
        }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				list = VueFavoriDB.readFavPersonne(user.getIdPersonne(), items+1, items+6);
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll =  false;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			for(String item : list) {
				addItems(item);
			}
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				if(ee.getCode() != R.string.e212) Toast.makeText(MesFavoris.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
