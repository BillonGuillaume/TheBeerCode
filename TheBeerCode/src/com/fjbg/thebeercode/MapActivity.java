package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.VueLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.UiSettings;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

public class MapActivity extends Activity{
	GoogleMap mMap;
	int idBiere;
	ArrayList <VueLocation> listLoc;
	ArrayList<Marker> listMarker;
	LocationManager mgr;
	Marker myPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		Intent i = getIntent();
		idBiere = i.getIntExtra(AffichageBiere.SELECTEDBEER, idBiere);

		setUpMapIfNeeded();
		mgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		
		if ( !mgr.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	        customAlert();
	    } else {
	    	initUpdate();
	    }	
        
        ActionDB action = new ActionDB();//a
        action.execute();		
	}
	
	private void initUpdate() {
		LocationListener onLocationChange=new LocationListener() {
			public void onLocationChanged(Location location) {
				LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());				

				if(myPosition == null) {
					myPosition = mMap.addMarker(new MarkerOptions()
					.position(userLocation)
					.title("Votre position"));
					mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 9.0f));
				} else {
					myPosition.setPosition(userLocation);
				}
			}

			public void onProviderDisabled(String provider) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
			}
		};

		mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3600000, 1000, onLocationChange);
	}
	
	private void setUpMapIfNeeded() {
	    if (mMap == null) {        
	        
	        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	        GoogleMapOptions options = new GoogleMapOptions();
	        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
			.rotateGesturesEnabled(false)
			.tiltGesturesEnabled(false);        
	        mMap.setMyLocationEnabled(true);
			
			UiSettings settings = mMap.getUiSettings();
			settings.setMyLocationButtonEnabled(true);
	        MapFragment.newInstance(options);
	    }
	}
	
	
	public class ActionDB extends AsyncTask<String, Integer, Boolean>{
		Boolean exc = false;
		Exception ex;
		
		public ActionDB() {

		}
		
		@Override
		protected void onPreExecute(){
		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			try{
				listLoc = VueLocation.readLocBiere(idBiere);
			}
			catch(Exception e){
				ex = e;
				exc = true;
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(MapActivity.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT).show();				
			} else {
				Toast.makeText(MapActivity.this, getResources().getString(R.string.GetPositions), Toast.LENGTH_SHORT).show();
				for(VueLocation item : listLoc) {
					mMap.addMarker(new MarkerOptions()
					.position(new LatLng(item.getLat(),item.getLon()))
					.title(item.getNomLieu())
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
					.snippet(item.getNomBiere() + ", " + String.valueOf(item.getPrix()) + "�"));
				}
			}			
		}		
	}

	private void customAlert() {
		try {
		final AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this);
		builder.setMessage(getResources().getString(R.string.GPSdisabled))
		.setCancelable(false)
		.setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
				initUpdate();
			}
		})
		.setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.cancel();
				Toast.makeText(MapActivity.this, getResources().getString(R.string.GPSdisabled2), Toast.LENGTH_LONG).show();
			}
		});
		final AlertDialog alert = builder.create();
		alert.show();
		}catch(Exception e) {
		}

	}
}