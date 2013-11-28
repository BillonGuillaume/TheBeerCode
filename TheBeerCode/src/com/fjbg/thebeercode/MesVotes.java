package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.model.VueVoteDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MesVotes extends Activity {

	ListView lvItems;
	int items;
	VotesAdapter vA;
	Button bBack;
	ArrayList<VueVoteDB> listVotes;
	public static final String SELECTEDBEER = "BEER";
	PersonneDB user;
	Boolean scroll = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_ajouts);
		Intent data = getIntent();
		user = (PersonneDB)data.getParcelableExtra(MainActivity.PERSONNE);

		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(bBackListener);

		lvItems = (ListView)findViewById(R.id.lvItems);
		listVotes = new ArrayList<VueVoteDB>();
		vA = new VotesAdapter(MesVotes.this, listVotes);
		lvItems.setAdapter(vA);
		GetVotes getter = new GetVotes();
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
		getMenuInflater().inflate(R.menu.mes_votes, menu);
		return true;
	}

	public void loadMore(int offset) {
		if(scroll) {
		GetMoarVotes getMore = new GetMoarVotes();
		getMore.execute();
		}
	}

	private void addItems(VueVoteDB item) {
		if (item != null){
			this.vA.add(item);
			this.vA.notifyDataSetChanged();
			items++;
		}
	}

	public class GetVotes extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<VueVoteDB> liste = new ArrayList<VueVoteDB>();

		public GetVotes() {

		}
		
		@Override
        protected void onPreExecute(){
                
        }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {				
				liste = VueVoteDB.readVotesPersonne(user.getIdPersonne(), 1, 5);

				lvItems.setOnScrollListener(new EndlessScrollListener(2) {
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
						loadMore(totalItemsCount);
					}
				});

				lvItems.setOnItemClickListener(new OnItemClickListener()
				{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						Intent showVote = new Intent(MesVotes.this, AffichageBiere.class);
						String selectedBeer = (listVotes.get(arg2)).getNomBiere();
						showVote.putExtra(MesVotes.SELECTEDBEER, selectedBeer);
						startActivity(showVote);
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
			for(VueVoteDB vote : liste) {
				addItems(vote);
			}
			if(exc) {
				Toast.makeText(MesVotes.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}

	public class GetMoarVotes extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		ArrayList<VueVoteDB> liste;

		public GetMoarVotes() {

		}
		
		@Override
        protected void onPreExecute(){
             liste = new ArrayList<VueVoteDB>(); 
        }

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {				
				liste = VueVoteDB.readVotesPersonne(user.getIdPersonne(), items+1, items+6);			
			}catch(Exception e) {
				ex = e;
				exc = true;
				scroll = false;
			}
			return true;
		}

		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			for(VueVoteDB vote : liste) {
				addItems(vote);
			}
			if(exc) {
				Toast.makeText(MesVotes.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
