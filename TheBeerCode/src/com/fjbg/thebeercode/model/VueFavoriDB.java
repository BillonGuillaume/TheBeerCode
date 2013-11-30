package com.fjbg.thebeercode.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class VueFavoriDB extends Favori implements Parcelable{
	public static Connection dbConnect = null;
	String nomBiere;
	String login;
    
    public VueFavoriDB() {
    }
    
    public VueFavoriDB(int idFavori, int aimant, int favorite, String nomBiere, String login) {
        super(idFavori, aimant, favorite);
        this.nomBiere = nomBiere;
        this.login = login;
    }
    
    public static void setDbConnect(Connection dbConnect) {
        VueFavoriDB.dbConnect = dbConnect;
    }
    
    public static ArrayList<String> readFavPersonne(int id, int min, int max) throws Exception {
    	String req = "SELECT * FROM vueFavori WHERE rownum>=? AND rownum<=? AND idPersonne=? ORDER BY idFavori";
    	ArrayList <String> listFav = new ArrayList<String>();
    	VueFavoriDB obj;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, min);
    		pstmt.setInt(2, max);
    		pstmt.setInt(3, id);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new VueFavoriDB();
    			obj.favorite = rs.getInt("IDBIERE");
    			obj.nomBiere = rs.getString("NOMBIERE");
    			obj.idFavori = rs.getInt("IDFAVORI");
    			obj.aimant = rs.getInt("IDPERSONNE");
    			obj.login = rs.getString("LOGIN");
    			listFav.add(obj.getNomBiere());
    		}
    		if (listFav.size() == 0 && min==1) throw new Exception("Vous n'avez aucun favori.");
    		else if (listFav.size() == 0) throw new Exception("Plus de favori à afficher.");
    		return listFav;
    	} catch (Exception e) {
    		throw new Exception("Erreur de lecture " + e.getMessage());
    	} finally {
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
        }
    }
    
    @Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nomBiere);
		dest.writeInt(idFavori);
		dest.writeInt(aimant);
		dest.writeInt(favorite);
		dest.writeString(login);
	}

	public static final Parcelable.Creator<VueFavoriDB> CREATOR = new Parcelable.Creator<VueFavoriDB>() {
		@Override
		public VueFavoriDB createFromParcel(Parcel source) {
			return new VueFavoriDB(source);
		}
		@Override
		public VueFavoriDB[] newArray(int size) {
			return new VueFavoriDB[size];
		}
	};
	public VueFavoriDB(Parcel in) {
		nomBiere = in.readString();
		idFavori = in.readInt();
		aimant = in.readInt();
		favorite = in.readInt();
		login = in.readString();
	}

	public String getNomBiere() {
		return nomBiere;
	}

	public String getLogin() {
		return login;
	}
}