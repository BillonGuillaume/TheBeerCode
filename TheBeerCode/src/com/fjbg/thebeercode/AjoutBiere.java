package com.fjbg.thebeercode;

import java.io.File;





import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.fjbg.thebeercode.model.BiereDB;
import com.fjbg.thebeercode.model.HistoriqueDB;
import com.fjbg.thebeercode.model.PersonneDB;

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
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AjoutBiere extends Activity {
	
	ProgressDialog progress;
	
	EditText eTnom;
	EditText eTpays;
	EditText eTdegre;
	
	Button choisirImage = null;
	Button ajouter = null;
	Button retour = null;
	
	String path = null;
	File file = null;
	ImageView photoBiere = null;
	
	AlertDialog dialog = null;
	private Uri mImageCaptureUri;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
	PersonneDB user;

	
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
		
		Intent i= getIntent();
		user= (PersonneDB)i.getParcelableExtra(MainActivity.PERSONNE);
		
		final String [] items			= new String [] {"From Camera", "From SD Card"};
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					file		 = new File(Environment.getExternalStorageDirectory(), 
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

        if (requestCode == PICK_FROM_FILE) {
           mImageCaptureUri = imageReturnedIntent.getData();
           path = getRealPathFromURI(mImageCaptureUri); //from Gallery
 
           if (path == null) path = mImageCaptureUri.getPath(); //from File Manager
        } 	
        else {
        		path    = file.getAbsolutePath();
        }
        
        try{
        	bitmap = decodeFile(new File(path));
        }
        catch(Exception e){
        	path = null;
        	bitmap = null;
        	file=null;
        }
        
        if(path!=null) photoBiere.setImageBitmap(bitmap);
	}
	
	public class Ajout extends AsyncTask<String, Integer, Boolean>{	
		Boolean exc = false;
		Boolean FTPpbm = false;
		Exception ex;
		
		public Ajout() {

		}
		
		@Override
        protected void onPreExecute(){
                progress = new ProgressDialog(AjoutBiere.this);
                progress.setMessage("Enregistrement de la biere en cours...");
                progress.setCancelable(false);
                progress.show();
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
				exc = true;
				ex = e;
			}
			
			if(path != null && exc!=null){
				FTPClient mFtp = new FTPClient();
			        try {
			        	mFtp.connect("ftp.alokar.site90.net",21); // Using port no=21
			        	mFtp.login("a7115779", "projet2013");
			        	mFtp.enterLocalPassiveMode();
				        mFtp.setFileType(FTPClient.BINARY_FILE_TYPE);
				        InputStream is = new FileInputStream(path);
				        String cheminBiere = biere.getNomBiere().replace(' ', '_');
			        	mFtp.storeFile("/public_html/BeerPictures/image_" + cheminBiere + ".jpg", is);
			        	biere.setCheminImage("/public_html/BeerPictures/image_" + cheminBiere + ".jpg");
			        	is.close();
			        	mFtp.disconnect();
					} catch(Exception e) {
						ex = e;
						FTPpbm = true;
					}
			}
			
			if(!exc) {
				try {
					biere.create();
					HistoriqueDB histo= new HistoriqueDB(0, user.getIdPersonne(),biere.getIdBiere(),"ajout");
					histo.create();
					
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
				Toast.makeText(AjoutBiere.this, ex.getMessage(), Toast.LENGTH_SHORT ).show();
			}
			else {
				eTnom.setText(""); 
				eTpays.setText("");
				eTdegre.setText("");
				photoBiere = (ImageView) findViewById(R.id.BeerPicture);
				path = null;
				if(FTPpbm){
					Toast.makeText(AjoutBiere.this, "Bière ajoutée mais problème lors de l' upload de la photo (ftp) : " + ex.getMessage(), Toast.LENGTH_SHORT ).show();
				}
				else{
					Toast.makeText(AjoutBiere.this, "Bière ajoutée !", Toast.LENGTH_SHORT ).show();
				}
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

	private Bitmap decodeFile(File f){
        try {
            //decode image size
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
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
