package com.fjbg.thebeercode;

import com.fjbg.thebeercode.model.ExceptionError;
import com.fjbg.thebeercode.model.PersonneDB;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	
	TextView tVLogin = null;
	EditText eTLogin = null;
	TextView tVPwd = null;
	EditText eTPwd = null;
	TextView tVNotYetRegistered = null;
	
	Button bSignIn = null;
	Button bCancel = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		tVLogin = (TextView)findViewById(R.id.tVLogin);
		eTLogin = (EditText)findViewById(R.id.eTLogin);
		tVPwd = (TextView)findViewById(R.id.tVPwd);
		eTPwd = (EditText)findViewById(R.id.eTPwd);
		tVNotYetRegistered = (TextView)findViewById(R.id.tVNotRegistered);
		bSignIn = (Button)findViewById(R.id.bSignIn);
		bCancel = (Button)findViewById(R.id.bCancel);
		
		bSignIn.setOnClickListener(bSignInListener);
		bCancel.setOnClickListener(bCancelListener);
	}
	
	private OnClickListener bSignInListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String login = eTLogin.getText().toString();
			String mdp = eTPwd.getText().toString();
			PersonneDB p = new PersonneDB();
			p.setLogin(login);
			p.setMdp(mdp);
			try {
				p.connection();
				Intent result = new Intent();
				result.putExtra(MainActivity.PERSONNE, p);
				setResult(RESULT_OK, result);
				finish();				
			} catch (Exception e) {				
				eTLogin.setText("");
				eTPwd.setText("");
				ExceptionError er = new ExceptionError(e.getMessage());
				Log.d("Login", "ExceptionError créée");
				Toast.makeText(Login.this, getResources().getString(er.getCode()), Toast.LENGTH_SHORT).show();
				Log.d("Login", "Exception : " + e.getMessage());
			}			
		}
	};
	
	private OnClickListener bCancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
