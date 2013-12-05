package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.CoordonneesDB;
import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PrixDB;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocalizationActivity extends Activity {
	
	private EditText etPlaceName;
	private EditText etPrice;
	private Button btLocalize;
	private Button btCancel;
	
	private double longitude = 0;
	private double latitude = 0;
	int idBiere;
	
	LocationManager mlocManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_localization);
		
		etPlaceName = (EditText)findViewById(R.id.etPlaceName);
		etPrice = (EditText)findViewById(R.id.etPrice);
		
		btLocalize = (Button)findViewById(R.id.btLocalize);
		btCancel = (Button)findViewById(R.id.btCancel);

		btLocalize.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if ( (mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER)) && longitude != 0 && latitude != 0){
					Ajout aj = new Ajout();
					aj.execute();
				}
				else{
					customAlert();
				}
			}

		});
		btCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		
		Intent i = getIntent();
		idBiere = i.getIntExtra(AffichageBiere.SELECTEDBEER, 0);
		
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		if ( !mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        customAlert();
	    }
		else{
			initUpdate();
		}
	}
	
	
	public class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(android.location.Location location){
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
	
	
		@Override
		public void onProviderDisabled(String provider){
			Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();  // TODO a ajouer dans string.xml
		}
	
	
		@Override
		public void onProviderEnabled(String provider){
			Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();  // TODO a ajouer dans string.xml
		}
	
	
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras){
		}
	}	
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.localization, menu);
		return true;
	}
	
	private void initUpdate() {
		LocationListener mlocListener = new MyLocationListener();
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER,0, 0, mlocListener);
	}
	
	private void customAlert() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(LocalizationActivity.this);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")  // TODO a ajouer dans string.xml
		.setCancelable(false)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {  // TODO a ajouer dans string.xml
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {  // TODO a ajouer dans string.xml
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
				Toast.makeText(LocalizationActivity.this, "Module GPS désactivé, localisation impossible.", Toast.LENGTH_LONG).show();  // TODO a ajouer dans string.xml
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
	}
	
	public class Ajout extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		String nomLieu;
		float prix = 0;
		CoordonneesDB coordonnees;
		PrixDB localisation;
		
		public Ajout() {
			
		}

		@Override
		protected void onPreExecute(){
			nomLieu = etPlaceName.getText().toString();
			try{
				prix =Float.parseFloat(etPrice.getText().toString());
			}
			catch(Exception e){
				exc = true;
				ex = e;
			}
			
			if(!exc){
				localisation= new PrixDB();
				localisation.setLocalisee(idBiere);
				localisation.setPrix(prix);
				coordonnees = new CoordonneesDB();
				coordonnees.setNomLieu(nomLieu);
				coordonnees.setLatitude(latitude);
				coordonnees.setLongitude(longitude);
			}
		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			if(!exc){
				try{
					coordonnees.create();
					Log.d("test", "" +coordonnees.getIdCoordonnee() + idBiere);
					localisation.setLocalisation(coordonnees.getIdCoordonnee());
					localisation.create();
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
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(LocalizationActivity.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}
			else{
				Toast.makeText(LocalizationActivity.this, "Localisation ajoutée !", Toast.LENGTH_SHORT ).show();  // TODO a ajouter dans string.xml
				finish();
			}
			
		}
		
		
	}

}