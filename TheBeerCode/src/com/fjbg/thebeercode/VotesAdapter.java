package com.fjbg.thebeercode;

import java.util.ArrayList;

import com.fjbg.thebeercode.model.VoteDB;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class VotesAdapter extends BaseAdapter {
	private Context activity;
    private static LayoutInflater inflater=null;
    private ArrayList<VoteDB> data;
 
    public VotesAdapter(Context a, ArrayList<VoteDB> listVotes) {
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
    	
    	Log.d("VotesAdapter", "getView");
        View vi=convertView;
        
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);
 
        TextView tVLogin = (TextView)vi.findViewById(R.id.tVLogin);
        TextView tVCom = (TextView)vi.findViewById(R.id.tVCom);
        RatingBar rating = (RatingBar)vi.findViewById(R.id.ratingBar);
 
        VoteDB vote = data.get(position);
        String votant = Integer.toString(vote.getVotant());
        tVLogin.setText(votant);
        tVCom.setText(vote.getCommentaire());
        rating.setRating(vote.getVote());
        rating.setIsIndicator(true);
        return vi;
    }
    
    public void add(VoteDB item) {
    	data.add(item);
    }
}
