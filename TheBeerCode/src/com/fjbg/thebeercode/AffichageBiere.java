package com.fjbg.thebeercode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;

import com.fjbg.thebeercode.RechercheBiere.GetBeers;
import com.fjbg.thebeercode.model.BiereDB;
import com.fjbg.thebeercode.model.FavoriDB;
import com.fjbg.thebeercode.model.HistoriqueDB;
import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.model.VoteDB;
import com.fjbg.thebeercode.model.VueVoteDB;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class AffichageBiere extends Activity {

	public static final String SELECTEDBEER = "BEER";
	public static final String USER = "USER";

	private ListView listComments;
	private TextView BeerName;
	private TextView BeerCountry;
	private TextView ABV;
	private TextView NbVotes;
	private ImageView BeerPicture;
	private RatingBar ratingBeer;
	private Button retour = null;
	private ImageButton favoris = null;
	private ImageButton noter = null;
	private ImageButton edit = null;
	private ImageButton delete = null;
	ProgressDialog progress;
	Boolean scroll = true;
	
	Dialog custom;
	private TextView tvNoter;
	private TextView tvCommenter;
	private RatingBar rbBeer;
	private EditText etCommentary;
	private Button bConfirm;
	private Button bCancel;
	
	ArrayList<VueVoteDB> listVotes;
	int items;
	VotesBiereAdapter vbA;
	private BiereDB biere;
	private String nomBiere;
	PersonneDB user;
	boolean favorite = false;
	boolean createur = false;
	FavoriDB favori;
	HistoriqueDB histo;
	String commentaire;
	Float note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_affichage_biere);

		BeerName = (TextView)findViewById(R.id.Beername);
		BeerCountry = (TextView)findViewById(R.id.BeerCountry);
		ABV = (TextView)findViewById(R.id.ABV);
		NbVotes = (TextView)findViewById(R.id.NbVotes);

		BeerPicture = (ImageView) findViewById(R.id.BeerPicture);

		ratingBeer = (RatingBar) findViewById(R.id.ratingBeer);

		listComments = (ListView)findViewById(R.id.listComments);

		retour = (Button) findViewById(R.id.btBack);
		retour.setOnClickListener(retourListener);

		favoris = (ImageButton) findViewById(R.id.imageFavoris);
		favoris.setOnClickListener(favorisListener);
		
		noter = (ImageButton) findViewById(R.id.imageNote);
		noter.setOnClickListener(noterListener);
		
		Intent i=getIntent();
		nomBiere=(String)i.getStringExtra(SELECTEDBEER);
		user= (PersonneDB)i.getParcelableExtra(USER);

		listVotes = new ArrayList<VueVoteDB>();
		vbA = new VotesBiereAdapter(AffichageBiere.this, listVotes);
		listComments.setAdapter(vbA);
		
		Lecture lec= new Lecture();
		lec.execute();
	}

	private OnClickListener retourListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	private OnClickListener favorisListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(user!=null){
				ModifFavoris MF= new ModifFavoris();
				MF.execute();
			}
			else{
				Toast.makeText(AffichageBiere.this, "Vous devez être connecté pour ajouter cette bière aux favoris !", Toast.LENGTH_SHORT ).show();
			}
		}
	};
	
	private OnClickListener noterListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(user!=null){
				custom = new Dialog(AffichageBiere.this);
				custom.setContentView(R.layout.noter_layout);
				
				tvNoter = (TextView)custom.findViewById(R.id.tvNoter);
				tvCommenter = (TextView)custom.findViewById(R.id.tvCommenter);
				
				rbBeer = (RatingBar)custom.findViewById(R.id.rbBeer);
				etCommentary = (EditText)custom.findViewById(R.id.etCommentary);
				
				bConfirm = (Button)custom.findViewById(R.id.bConfirm);
				bCancel = (Button)custom.findViewById(R.id.bCancel);
	
				custom.setTitle("Evaluation");
	
				bConfirm.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View view) {
						commentaire = etCommentary.getText().toString();
						note = rbBeer.getRating();
						
						custom.dismiss();
						ModifNote MN = new ModifNote();
						MN.execute();
					}
	
				});
				bCancel.setOnClickListener(new View.OnClickListener() {
	
					@Override
					public void onClick(View view) {
						custom.dismiss();
	
					}
				});
				custom.show();
			}
			else{
				Toast.makeText(AffichageBiere.this, "Vous devez être connecté pour noter cette bière !", Toast.LENGTH_SHORT ).show();
			}
		}
	};

	private OnClickListener editListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};
	
	private OnClickListener deleteListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
		}
	};
	
	public void loadMore(int offset) {
		if(scroll) {
			GetMoarVotes getMore = new GetMoarVotes();
			getMore.execute();
		}
	}

	private void addItems(VueVoteDB item) {
		if (item != null){
			this.vbA.add(item);
			this.vbA.notifyDataSetChanged();
			items++;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.affichage_biere, menu);
		return true;
	}

	public class Lecture extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		Exception ex;
		BiereDB biereRech;
		Bitmap bitmap   = null;
		File downloadFile;
		File file;

		public Lecture() {

		}

		@Override
		protected void onPreExecute(){
			progress = new ProgressDialog(AffichageBiere.this);
			progress.setMessage("Téléchargement des données en cours...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			biereRech =new BiereDB();
			biereRech.setNomBiere(nomBiere);

			try{
				biereRech.readBiere();
			}
			catch(Exception e){
				ex = e;
				exc = true;
			}
			
			if(!exc){
				
				if(user!=null){
					favori= new FavoriDB();
					favori.setAimant(user.getIdPersonne());
					favori.setFavorite(biereRech.getIdBiere());
					
					try{
						favori.readFavorite();
						favorite=true;
					}
					catch(Exception e){
						favorite= false;
					}
					
					histo= new HistoriqueDB();
					histo.setActeur(user.getIdPersonne());
					histo.setUtilise(biereRech.getIdBiere());
					
					try{
						histo.readHistorique();
						createur = true;
					}
					catch(Exception e){
						createur = false;
					}
				}
				
				
				try {
					FTPClient mFTPClient = new FTPClient();
					mFTPClient.connect("ftp.alokar.site90.net",21);      
					mFTPClient.login("a7115779", "projet2013");
					mFTPClient.enterLocalPassiveMode();
					InputStream inputStream = mFTPClient.retrieveFileStream(biereRech.getCheminImage());
					String cheminBiere = biere.getNomBiere().replace(' ', '_');
					file = new File(Environment.getExternalStorageDirectory() + File.separator + cheminBiere +".jpg"); // TODO mettre toutes les images dans me même dossier
					OutputStream outputStream = new FileOutputStream(file);
					int read = 0;
					byte[] bytes = new byte[1024*1024];
					while ((read = inputStream.read(bytes)) != -1) {
						outputStream.write(bytes, 0, read);
					}
					outputStream.close();
					inputStream.close();
				}catch(Exception e) {
					Log.d("exception", "exception : " + e.getMessage());
				}


			}
			return true;
		}

		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);

			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 2;
			try {
				bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			}catch (Exception e) {
				Log.d("exception post", "exception 2 : " + e.getMessage());  // TODO reception d'exception quand affichage via MesFavoris
			}				
			BeerPicture.setImageBitmap(bitmap);
			if(progress.isShowing())
				progress.dismiss();
			if(exc) {
				Toast.makeText(AffichageBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else {
				try{
					if(createur){
						setContentView(R.layout.activity_affichage_biere2);
						
						BeerName = (TextView)findViewById(R.id.Beername);
						BeerCountry = (TextView)findViewById(R.id.BeerCountry);
						ABV = (TextView)findViewById(R.id.ABV);
						NbVotes = (TextView)findViewById(R.id.NbVotes);

						BeerPicture = (ImageView) findViewById(R.id.BeerPicture);

						ratingBeer = (RatingBar) findViewById(R.id.ratingBeer);

						listComments = (ListView)findViewById(R.id.listComments);

						retour = (Button) findViewById(R.id.btBack);
						retour.setOnClickListener(retourListener);

						favoris = (ImageButton) findViewById(R.id.imageFavoris);
						favoris.setOnClickListener(favorisListener);
						
						noter = (ImageButton) findViewById(R.id.imageNote);
						noter.setOnClickListener(noterListener);
						
						Intent i=getIntent();
						nomBiere=(String)i.getStringExtra(SELECTEDBEER);
						user= (PersonneDB)i.getParcelableExtra(USER);

						listVotes = new ArrayList<VueVoteDB>();
						vbA = new VotesBiereAdapter(AffichageBiere.this, listVotes);
						listComments.setAdapter(vbA);
						
						edit = (ImageButton) findViewById(R.id.imageEdit);
						edit.setOnClickListener(editListener);

						delete = (ImageButton) findViewById(R.id.imageDelete);
						delete.setOnClickListener(deleteListener);
					}
					biere=biereRech;
					BeerName.setText(biere.getNomBiere());
					BeerCountry.setText(biere.getPaysBiere());
					ABV.setText(String.valueOf(biere.getDegreBiere()));
					ratingBeer.setRating((biere.getCoteBiere())/2);
					NbVotes.setText(String.valueOf(biere.getNbreVotes()));
					if(favorite)favoris.setImageResource(R.drawable.ic_stat_favoritetrue);
				}
				catch(Exception e){
					Toast.makeText(AffichageBiere.this, e.getMessage(), Toast.LENGTH_SHORT ).show();
				}
			}
			GetVotes getter = new GetVotes();
			getter.execute();

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
			Log.d("AffichageBiere", "onPreExecute");
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try {				
				Log.d("image","test avant readVotesBiere" + biere.getIdBiere());
				liste = VueVoteDB.readVotesBiere(biere.getIdBiere(), 1, 10);

				listComments.setOnScrollListener(new EndlessScrollListener(5) {
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
						loadMore(totalItemsCount);
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
				Toast.makeText(AffichageBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
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
				liste = VueVoteDB.readVotesBiere(biere.getIdBiere(), items+1, items+6);			
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
				Toast.makeText(AffichageBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
		}
	}
	
	public class ModifFavoris extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;

		public ModifFavoris() {
			
		}

		@Override
		protected void onPreExecute(){

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			if(favorite){
				try{
					favori.delete();
					favorite=false;
				}
				catch(Exception e){
					ex = e;
					exc = true;
				}
			}
			else{
				favori.setAimant(user.getIdPersonne());
				favori.setFavorite(biere.getIdBiere());
				try{
					favori.create();
					favorite=true;
				}
				catch(Exception e){
					ex = e;
					exc = true;
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(AffichageBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else{
				if(favorite){
					favoris.setImageResource(R.drawable.ic_stat_favoritetrue);
					Toast.makeText(AffichageBiere.this, "Ajoutée aux favoris !", Toast.LENGTH_SHORT ).show();
				}
				else {
					favoris.setImageResource(R.drawable.ic_stat_favoritefalse);
					Toast.makeText(AffichageBiere.this, "Supprimée des favoris !", Toast.LENGTH_SHORT ).show();
				}
			}
			
		}
		
		
	}
	
	public class ModifNote extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Boolean dejaVote = false;
		Exception ex;

		public ModifNote() {
			
		}

		@Override
		protected void onPreExecute(){

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			VoteDB vote = new VoteDB();
			vote.setVotant(user.getIdPersonne());
			vote.setNotee(biere.getIdBiere());
			
			try{
				vote.readVote();
				dejaVote = true;
			}
			catch(Exception e){
				
			}
			
			vote.setVote(note*2);
			vote.setCommentaire(commentaire);
			
			if(dejaVote){
				try{
					vote.update();
				}
				catch(Exception e){
					ex = e;
					exc = true;
				}
				
			}
			else{
				try{
					vote.create();
				}
				catch(Exception e){
					ex = e;
					exc = true;
				}
			}
			
			if(!exc){
				try{
					biere.read();
				}
				catch(Exception e){
					
				}
			}

			return true;
		}

		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(AffichageBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else{
				Toast.makeText(AffichageBiere.this, "Vote enregistré !", Toast.LENGTH_SHORT ).show();
				ratingBeer.setRating(biere.getCoteBiere()/2);
				NbVotes.setText(String.valueOf(biere.getNbreVotes()));
			}
			
		}
	}
}
