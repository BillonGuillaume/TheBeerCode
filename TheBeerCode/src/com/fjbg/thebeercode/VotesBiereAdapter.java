package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.VueVoteDB;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class VotesBiereAdapter extends BaseAdapter {
	private Context activity;
    private static LayoutInflater inflater=null;
    private ArrayList<VueVoteDB> data;
 
    public VotesBiereAdapter(Context a, ArrayList<VueVoteDB> listVotes) {
        activity = a;
        data=listVotes;
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
            vi = inflater.inflate(R.layout.list_row2, null);
 
        TextView tVUser = (TextView)vi.findViewById(R.id.tVUser);
        TextView tVCom = (TextView)vi.findViewById(R.id.tVCom);
        RatingBar rating = (RatingBar)vi.findViewById(R.id.ratingBar);
 
        VueVoteDB vote = data.get(position);
        tVUser.setText(vote.getLogin());
        tVCom.setText(vote.getCommentaire());
        rating.setRating(vote.getVote()/2);
        return vi;
    }
    
    public void add(VueVoteDB item) {
    	data.add(item);
    }
    
    public void clear() {
    	data.clear();
    }
}
