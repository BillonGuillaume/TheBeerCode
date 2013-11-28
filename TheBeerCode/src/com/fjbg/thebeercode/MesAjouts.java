package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.AjoutDB;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MesAjouts extends Activity {
	
	ListView lvItems;
	int items;
	ArrayAdapter<String> aa;
	Button bBack;
	public static final String SELECTEDBEER = "beer";
	PersonneDB user;
	Boolean scroll = true;
	ArrayList<String> listBeers = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_ajouts);
		
		Intent data = getIntent();
		user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);
		
		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(bBackListener);
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listBeers);
		lvItems.setAdapter(aa);
		
		GetBeers getter = new GetBeers();		
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
		GetMoarBeers getMore = new GetMoarBeers();
		getMore.execute();
		}
	}
	
	private void addItems(String item) {
		this.aa.add(item);
		this.aa.notifyDataSetChanged();
		items++;
	}
	
	public class GetBeers extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<String> list = new ArrayList<String>();
		
		public GetBeers() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				list = AjoutDB.readHistoriquePersonne(user.getIdPersonne(), 1, 5);
				
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
						Intent showBeer = new Intent(MesAjouts.this, AffichageBiere.class);
						String selectedBeer = (String)aa.getItem(arg2);
						showBeer.putExtra(MesAjouts.SELECTEDBEER, selectedBeer);
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
			for(String item : list) {
				addItems(item);
			}
			if(exc) {
				Toast.makeText(MesAjouts.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public class GetMoarBeers extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<String> list = new ArrayList<String>();
		
		public GetMoarBeers() {

		}
		
		@Override
        protected void onPreExecute(){
             list = new ArrayList<String>(); 
        }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				list = AjoutDB.readHistoriquePersonne(user.getIdPersonne(), items+1, items+6);
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
				Toast.makeText(MesAjouts.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
