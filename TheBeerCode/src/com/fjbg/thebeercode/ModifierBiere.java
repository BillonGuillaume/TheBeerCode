package com.fjbg.thebeercode;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ModifierBiere extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modif_biere);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.modifier_biere, menu);
		return true;
	}

}