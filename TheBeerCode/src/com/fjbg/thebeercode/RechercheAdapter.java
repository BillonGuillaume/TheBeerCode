package com.fjbg.thebeercode;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fjbg.thebeercode.model.BiereDB;

public class RechercheAdapter extends BaseAdapter {
	private Context activity;
    private static LayoutInflater inflater=null;
    private ArrayList<BiereDB> data;
 
    public RechercheAdapter(Context a, ArrayList<BiereDB> listBeers) {
        activity = a;
        data=listBeers;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return data.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row3, null);
 
        TextView tVBeer = (TextView)vi.findViewById(R.id.tVBeer);
        TextView tVPays = (TextView)vi.findViewById(R.id.tVPays);
        TextView tVDegre = (TextView)vi.findViewById(R.id.tVDegre);
        RatingBar rBNote = (RatingBar)vi.findViewById(R.id.rBNote);
 
        BiereDB biere = data.get(position);
        tVBeer.setText(biere.getNomBiere());
        tVPays.setText(biere.getPaysBiere());
        tVDegre.setText(String.valueOf(biere.getDegreBiere()));
        rBNote.setRating(biere.getCoteBiere()/2);
        return vi;
    }
    
    public void add(BiereDB item) {
    	data.add(item);
    }
    
    public void clear() {
    	data.clear();
    }
}