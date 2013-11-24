package com.fjbg.thebeercode;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;

public class MesAjouts extends Activity {
	
	ListView lvItems;
	int items;
	ArrayAdapter<String> aa;
	Button bBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_ajouts);
		
		bBack = (Button)findViewById(R.id.bBack);
		bBack.setOnClickListener(bBackListener);
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		ArrayList<String> aL = new ArrayList<String>();
		aL.add("Bière 1");
		aL.add("Bière 2");
		aL.add("Bière 3");
		items = 3;
		Log.d("MesAjouts", "items ajoutés");
		aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, aL);
		lvItems.setAdapter(aa);
		lvItems.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMore(totalItemsCount);
			}
		});
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
		getMenuInflater().inflate(R.menu.mes_ajouts, menu);
		return true;
	}
	
	public void loadMore(int offset) { // TODO en faire une asynctask
		int nbrAjout;
		for(nbrAjout = 0; nbrAjout <=5; nbrAjout++) {
			addItems("Bière " + (items + 1));
			items++;
		}	
		Toast.makeText(this, "Chargement déclenché", Toast.LENGTH_SHORT).show();
	}
	
	private void addItems(String item) {
		if (item.length()>0){
            this.aa.add(item);
            this.aa.notifyDataSetChanged();
        }
	}

}
