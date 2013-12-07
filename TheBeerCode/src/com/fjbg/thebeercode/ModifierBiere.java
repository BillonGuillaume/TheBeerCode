package com.fjbg.thebeercode;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.fjbg.thebeercode.model.BiereDB;
import com.fjbg.thebeercode.model.ExceptionError;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ModifierBiere extends Activity {
	
	ProgressDialog progress;
	
	EditText eTnom;
	EditText eTpays;
	EditText eTdegre;
	
	Button choisirImage = null;
	Button modifier = null;
	Button retour = null;
	
	String path = null;
	ImageView photoBiere = null;
	Bitmap bitmap;
	
	AlertDialog dialog = null;
	private Uri mImageCaptureUri;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
	BiereDB biere;
	Bitmap bitmapModif;

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
		
		modifier = (Button) findViewById(R.id.bAjBiere);
		modifier.setOnClickListener(modifierListener);
		modifier.setText(R.string.Edit);
		
		retour = (Button) findViewById(R.id.bBack);
		retour.setOnClickListener(retourListener);
		
		Intent i= getIntent();
		biere= (BiereDB)i.getParcelableExtra(AffichageBiere.SELECTEDBEER);
		
		if(getIntent().hasExtra(AffichageBiere.IMAGE)) {
		    bitmap = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra(AffichageBiere.IMAGE),0,getIntent().getByteArrayExtra(AffichageBiere.IMAGE).length);        
		    photoBiere.setImageBitmap(bitmap);
		}
		
		eTnom.setText(biere.getNomBiere());
		eTpays.setText(biere.getPaysBiere());
		eTdegre.setText(String.valueOf(biere.getDegreBiere()));
		
		final String [] items			= new String [] {getResources().getString(R.string.FromCam), getResources().getString(R.string.FromCard)};
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);

		builder.setTitle(getResources().getString(R.string.SelectImage));
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
					startActivityForResult(intent, PICK_FROM_CAMERA);
					dialog.cancel();
				} else {
					Intent intent = new Intent();
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.CompleteUsing)), PICK_FROM_FILE);
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
	
	private OnClickListener modifierListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Modifier modif = new Modifier();
			modif.execute();
		}
	};
	
	private OnClickListener retourListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent i= new Intent();
			i.putExtra(AffichageBiere.SELECTEDBEER, biere);
			setResult(RESULT_OK, i);
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
	    
        bitmapModif = null;
 
        if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = imageReturnedIntent.getData();
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery

			if (path == null) path = mImageCaptureUri.getPath(); //from File Manager
			else{
				try{
					bitmapModif = decodeFile(new File(path));
				} catch(Exception e){
					path = null;
					bitmapModif = null;
				}
			}
			
			if(path!=null) photoBiere.setImageBitmap(bitmapModif);
		} 	
		else {
			Bundle extras = imageReturnedIntent.getExtras();
		    bitmapModif = (Bitmap) extras.get("data");
		    photoBiere.setImageBitmap(bitmapModif);
		}
	}
	
	public class Modifier extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		Exception ex;
		
		public Modifier() {

		}
		
		@Override
        protected void onPreExecute(){
                progress = new ProgressDialog(ModifierBiere.this);
                progress.setMessage(getResources().getString(R.string.UpdatingBeer));
                progress.setCancelable(false);
                progress.show();
        }

		@Override
		protected Boolean doInBackground(String... arg0) {

			BiereDB biereModif = biere;
			
			String nomBiere=eTnom.getText().toString();
			biereModif.setNomBiere(nomBiere);

			String paysBiere= eTpays.getText().toString();
			biereModif.setPaysBiere(paysBiere);
			
			String degre= eTdegre.getText().toString();
			try{
				if(nomBiere.matches("") || paysBiere.matches("") || degre.matches("")){
					throw new Exception("Exception personnalisée/" + R.string.e218 + "/" + "Tous les champs doivent être remplis !");
				}
				else{
					float degreBiere = Float.parseFloat(degre);
					biereModif.setDegreBiere(degreBiere);
				}
			}
			catch(Exception e){
				ex = e;
				exc = true;
			}
			
			if(bitmapModif != null && exc!=null){
				FTPClient mFtp = new FTPClient();
				try {
					mFtp.connect("ftp.alokar.site90.net",21); // Using port no=21
					mFtp.login("a7115779", "projet2013");
					mFtp.enterLocalPassiveMode();
					mFtp.setFileType(FTPClient.BINARY_FILE_TYPE);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmapModif.compress(CompressFormat.JPEG, 100, stream);
					InputStream is = new ByteArrayInputStream(stream.toByteArray());
					String cheminBiere = biereModif.getNomBiere().replace(' ', '_');
					mFtp.storeFile("/public_html/BeerPictures/image_" + cheminBiere + ".jpg", is);
					biereModif.setCheminImage("/public_html/BeerPictures/image_" + cheminBiere + ".jpg");
					is.close();
					mFtp.disconnect();
				} catch(Exception e) {
					ex = e;
					exc = true;
				}
			}
			
			if(!exc) {
				try {
					biereModif.update();
					biere=biereModif;
					
				} catch(Exception e) {
					ex = e;
					exc = true;
				}
			}
			
			return true;
		} 
		
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(progress.isShowing())
                progress.dismiss();
			if(exc) {
				ExceptionError ee = new ExceptionError(ex.getMessage());
				Toast.makeText(ModifierBiere.this, getResources().getString(ee.getCode()), Toast.LENGTH_SHORT ).show();
			}
			else {
				Toast.makeText(ModifierBiere.this, getResources().getString(R.string.BeerUpdated), Toast.LENGTH_SHORT ).show();
			}			
		}
	}
	
	public String getRealPathFromURI(Uri contentUri) {
        String [] proj = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
		Cursor cursor = managedQuery( contentUri, proj, null, null,null);
 
        if (cursor == null) return null;
 
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA); 
        cursor.moveToFirst(); 
        return cursor.getString(column_index);
    }
	
	private Bitmap decodeFile(File f){
        try {
            //decode image sizea
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=120;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
             
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {  // TODO exception non gerée ? rien à afficher ?
        } 
        catch (IOException e) {
            e.printStackTrace();  // Idem ?
        }
        return null;
    }

}
