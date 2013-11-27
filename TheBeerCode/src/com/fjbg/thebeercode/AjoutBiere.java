package com.fjbg.thebeercode;

import java.io.File;




import com.fjbg.thebeercode.model.BiereDB;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AjoutBiere extends Activity {
	
	EditText eTnom;
	EditText eTpays;
	EditText eTdegre;
	
	Button choisirImage = null;
	Button ajouter = null;
	Button retour = null;
	
	ImageView photoBiere = null;
	
	AlertDialog dialog = null;
	private Uri mImageCaptureUri;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ajout_biere);
		
		eTnom = (EditText)findViewById(R.id.eTnom);
		eTpays = (EditText)findViewById(R.id.eTpays);
		eTdegre = (EditText)findViewById(R.id.eTdegre);
		
		photoBiere = (ImageView) findViewById(R.id.BeerPicture);
		
		choisirImage = (Button) findViewById(R.id.bPicture);
		choisirImage.setOnClickListener(choisirImgListener);
		
		ajouter = (Button) findViewById(R.id.bAjBiere);
		ajouter.setOnClickListener(ajouterListener);
		
		retour = (Button) findViewById(R.id.bBack);
		retour.setOnClickListener(retourListener);
		
		final String [] items			= new String [] {"From Camera", "From SD Card"};
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory(),
							   			"tmp_beerpicture_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}

					dialog.cancel();
				} else {
					Intent intent = new Intent();

	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);

	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );

		dialog = builder.create();
		
	}
	
	private OnClickListener choisirImgListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog.show();
		}
	};
	
	private OnClickListener ajouterListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Ajout aj = new Ajout();
			aj.execute();
		}
	};
	
	private OnClickListener retourListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ajout_biere, menu);
		return true;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 

	    if (resultCode != RESULT_OK) return;
	    
        Bitmap bitmap   = null;
        String path     = "";
 
        if (requestCode == PICK_FROM_FILE) {
            mImageCaptureUri = imageReturnedIntent.getData();
            path = getRealPathFromURI(mImageCaptureUri); //from Gallery
 
            if (path == null)
                path = mImageCaptureUri.getPath(); //from File Manager
 
            if (path != null)
                bitmap  = BitmapFactory.decodeFile(path);
        } else {
            path    = mImageCaptureUri.getPath();
            bitmap  = BitmapFactory.decodeFile(path);
        }
 
        photoBiere.setImageBitmap(bitmap);
	}
	
	public class Ajout extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		Exception ex;
		
		public Ajout() {

		}

		@Override
		protected Boolean doInBackground(String... arg0) {
			BiereDB biere =new BiereDB();
			
			String nomBiere=eTnom.getText().toString();
			biere.setNomBiere(nomBiere);

			String paysBiere= eTpays.getText().toString();
			biere.setPaysBiere(paysBiere);
			try{
				float degreBiere= Float.parseFloat(eTdegre.getText().toString());
				biere.setDegreBiere(degreBiere);
			}
			catch(Exception e){
				Toast.makeText(AjoutBiere.this, "Le degre doit être un nombre !", Toast.LENGTH_SHORT ).show();
				exc = true;
				ex = e;
			}
			
			biere.setCheminImage("osef");
			if(!exc) {
				try {
					biere.create();
				} catch(Exception e) {
					ex = e;
					exc = true;
				}
			}
			return true;
		}
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(exc) {
				Toast.makeText(AjoutBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else {
				Toast.makeText(AjoutBiere.this, "Bière ajoutée !", Toast.LENGTH_SHORT ).show();
				eTnom.setText(""); 
				eTpays.setText("");
				eTdegre.setText("");
			}
			
		}
	}
	
	public String getRealPathFromURI(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = managedQuery( contentUri, proj, null, null,null);
 
        if (cursor == null) return null;
 
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
 
        cursor.moveToFirst();
 
        return cursor.getString(column_index);
    }

}
