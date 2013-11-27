package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.model.VoteDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
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
	ArrayList<VoteDB> listVotes;
	public static final String SELECTEDVOTE = "VOTE";
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

		VoteDB vote = new VoteDB(1, 2, 3, 3, "aa");
		VoteDB vote2 = new VoteDB(12, 2, 3, 4, "aaa");
		VoteDB vote3 = new VoteDB(21, 2, 3, 5, "aaaa");
		listVotes = new ArrayList();
		listVotes.add(vote);
		listVotes.add(vote2);
		listVotes.add(vote3);

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
		Log.d("loadMore", "loeaee");
		GetMoarVotes getMore = new GetMoarVotes();
		getMore.execute();
	}

	private void addItems(VoteDB item) {
		if (item != null){
			this.vA.add(item);
			this.vA.notifyDataSetChanged();
			items++;
		}
	}

	public class GetVotes extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;

		public GetVotes() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {
				ArrayList<VoteDB> liste = new ArrayList<VoteDB>();
				liste = VoteDB.readCommentairesPersonne(1);
				for(VoteDB vote : liste) {
					addItems(vote);
				}

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
						//String selectedItem=(String)arg0.getItemAtPosition(arg2);
						Intent showVote = new Intent(MesVotes.this, AjoutBiere.class); // TODO Lancer l'activité d'affichage du vote ou alertBox
						VoteDB selectedVote = (VoteDB)listVotes.get(arg2);
						showVote.putExtra(MesVotes.SELECTEDVOTE, selectedVote);
						startActivity(showVote);
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
				Toast.makeText(MesVotes.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}			
		}
	}

	public class GetMoarVotes extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;

		public GetMoarVotes() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {  // TODO Créer la méthode pour récupérer un certain nombre + Que faire quand plus rien dans DB ?
			try {
				int nbrAjout;
				for(nbrAjout = 0; nbrAjout <=2; nbrAjout++) {
					VoteDB vote = new VoteDB(1, 2, 3, 3, "test");
					addItems(vote);
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
				Toast.makeText(MesVotes.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}

}
