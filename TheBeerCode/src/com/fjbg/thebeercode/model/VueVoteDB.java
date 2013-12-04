package com.fjbg.thebeercode.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

import android.os.Parcel;
import android.os.Parcelable;

public class VueVoteDB extends Vote implements Parcelable{
	public static Connection dbConnect = null;
	int idBiere;
	String nomBiere;
	int idPersonne;
	String login;
    
    public VueVoteDB() {
    }
    
    public VueVoteDB(int idVote, int votant, int notee, float vote, String commentaire, int idBiere, String nomBiere, int idPersonne, String login) {
        super(idVote, votant, notee, vote, commentaire);
        this.idBiere = idBiere;
        this.nomBiere = nomBiere;
        this.idPersonne = idPersonne;
        this.login = login;
    }
    
    public static void setDbConnect(Connection dbConnect) {
        VueVoteDB.dbConnect = dbConnect;
    }
    
    public static ArrayList<VueVoteDB> readVotesPersonne(int id, int min, int max) throws Exception {
    	String req = "SELECT * FROM vueVotes WHERE rownum>=? AND rownum<=? AND idPersonne=? ORDER BY idBiere";
    	ArrayList <VueVoteDB> listVotes = new ArrayList<VueVoteDB>();
    	VueVoteDB obj;
    	Boolean ex1 = false;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, min);
    		pstmt.setInt(2, max);
    		pstmt.setInt(3, id);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new VueVoteDB();
    			obj.idBiere = rs.getInt("IDBIERE");
    			obj.nomBiere = rs.getString("NOMBIERE");
    			obj.idVote = rs.getInt("IDVOTE");
    			obj.votant = rs.getInt("VOTANT");
    			obj.notee = rs.getInt("NOTEE");
    			obj.vote = rs.getFloat("VOTE");
    			obj.commentaire = rs.getString("COMMENTAIRE");
    			obj.idPersonne = rs.getInt("IDPERSONNE");
    			obj.login = rs.getString("LOGIN");
    			listVotes.add(obj);
    		}
    		if (listVotes.size() == 0 && min==1) ex1 = true; //throw new Exception("Vous n'avez laissé aucun commentaire.");
    		return listVotes;
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
    	} finally {
    		if(ex1) {
        		throw new Exception("Erreur personnalisée/" + R.string.e212 + "/" + "aucun commentaire");
        	}
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
        }
    }
    
    public static ArrayList<VueVoteDB> readVotesBiere(int id, int min, int max) throws Exception {
    	String req = "SELECT * FROM vueVotes WHERE rownum>=? AND rownum<=? AND idBiere=? ORDER BY idVote";
    	ArrayList <VueVoteDB> listVotes = new ArrayList<VueVoteDB>();
    	VueVoteDB obj;
    	Boolean ex1 = false;
    	Boolean ex2 = false;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, min);
    		pstmt.setInt(2, max);
    		pstmt.setInt(3, id);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new VueVoteDB();
    			obj.idBiere = rs.getInt("IDBIERE");
    			obj.nomBiere = rs.getString("NOMBIERE");
    			obj.idVote = rs.getInt("IDVOTE");
    			obj.votant = rs.getInt("VOTANT");
    			obj.notee = rs.getInt("NOTEE");
    			obj.vote = rs.getFloat("VOTE");
    			obj.commentaire = rs.getString("COMMENTAIRE");
    			obj.idPersonne = rs.getInt("IDPERSONNE");
    			obj.login = rs.getString("LOGIN");
    			listVotes.add(obj);
    		}
    		if (listVotes.size() == 0 && min==1) ex1 = true; //throw new Exception("Aucun commentaire.");
    		else if (listVotes.size() == 0) ex2 = true; //throw new Exception("Plus de commentaire à afficher.");
    		return listVotes;
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
    	} finally {
    		if(ex1) {
        		throw new Exception("Erreur personnalisée/" + R.string.e212 + "/" + "aucun commentaire");
        	}
    		if(ex2) {
        		throw new Exception("Erreur personnalisée/" + R.string.e203 + "/" + "plus de commentaire");
        	}
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
		dest.writeInt(idBiere);
		dest.writeString(nomBiere);
		dest.writeInt(idVote);
		dest.writeInt(votant);
		dest.writeInt(notee);
		dest.writeFloat(vote);
		dest.writeString(commentaire);
		dest.writeInt(idPersonne);
		dest.writeString(login);
	}

	public static final Parcelable.Creator<VueVoteDB> CREATOR = new Parcelable.Creator<VueVoteDB>() {
		@Override
		public VueVoteDB createFromParcel(Parcel source) {
			return new VueVoteDB(source);
		}
		@Override
		public VueVoteDB[] newArray(int size) {
			return new VueVoteDB[size];
		}
	};
	public VueVoteDB(Parcel in) {
		idBiere = in.readInt();
		nomBiere = in.readString();
		idVote = in.readInt();
		votant = in.readInt();
		notee = in.readInt();
		vote = in.readFloat();
		commentaire = in.readString();
		idPersonne = in.readInt();
		login = in.readString();
	}

	public String getNomBiere() {
		return nomBiere;
	}

	public String getLogin() {
		return login;
	}
}
