package com.fjbg.thebeercode.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;


public class VoteDB extends Vote implements CRUD, Parcelable{
public static Connection dbConnect = null;
    
    public VoteDB() {
    }
    
    public VoteDB(int idVote, int votant, int notee, float vote, String commentaire) {
        super(idVote, votant, notee, vote, commentaire);
    }
    
    public static void setDbConnect(Connection dbConnect) {
        VoteDB.dbConnect = dbConnect;
    }

    @Override
    public void create() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call createVote(?,?,?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER); 
            cstmt.setInt(2, votant);
            cstmt.setInt(3, notee);
            cstmt.setFloat(4, vote);
            cstmt.setString(5, commentaire);
            cstmt.executeUpdate();
            this.idVote = cstmt.getInt(1);
        } catch (Exception e) {
            throw new Exception("Erreur de création " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void read() throws Exception {
        // Not implemented
    }

    @Override
    public void update() throws Exception {
        CallableStatement cstmt = null;

        try {
            String req = "call updateVote(?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.setInt(1, idVote);
            cstmt.setFloat(2, vote);
            cstmt.setString(3, commentaire);
            cstmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur de mise à jour " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void delete() throws Exception {
        // Not implemented
    }
    
    public void readVote() throws Exception {
    	String req = "{?=call readVote(?,?)}";

        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, votant);
            cstmt.setInt(3, notee);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            if (rs.next()) {
                this.idVote = rs.getInt("IDVOTE");
                this.votant = rs.getInt("VOTANT");
                this.notee = rs.getInt("NOTEE");
                this.vote = rs.getFloat("VOTE");
                this.commentaire = rs.getString("COMMENTAIRE");
            } else {
                throw new Exception("vote inconnu");
            }
        } catch (Exception e) {
            throw new Exception("Erreur de lecture " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    public static ArrayList<VoteDB> readCommentairesBiere(int notee) throws Exception {
    	String req = "{?=call readCommentaire(?)}";
    	ArrayList <VoteDB> listCom = new ArrayList<VoteDB>();
    	VoteDB obj;
        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, notee);
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            while (rs.next()) {
            	obj = new VoteDB();
                obj.idVote = rs.getInt("IDVOTE");
                obj.votant = rs.getInt("VOTANT");
                obj.notee = rs.getInt("NOTEE");
                obj.vote = rs.getFloat("VOTE");
                obj.commentaire = rs.getString("COMMENTAIRE");
                listCom.add(obj);
            }
            return listCom;
        } catch (Exception e) {
            throw new Exception("Erreur de lecture " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    public static ArrayList<VoteDB> readCommentairesPersonne(int votant) throws Exception {
    	String req = "SELECT idVote, votant, notee, vote, commentaire FROM Vote WHERE votant = ?";
    	ArrayList <VoteDB> listVotes = new ArrayList<VoteDB>();
    	VoteDB obj;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, votant);
    		ResultSet rs = null;
    		rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new VoteDB();
    			obj.idVote = rs.getInt("IDVOTE");
    			obj.votant = rs.getInt("VOTANT");
    			obj.notee = rs.getInt("NOTEE");
    			obj.vote = rs.getFloat("VOTE");
    			obj.commentaire = rs.getString("COMMENTAIRE");
    			listVotes.add(obj);
    		}
    		if (listVotes.size() == 0) throw new Exception("Vous n'avez laissé aucun commentaire.");
    		return listVotes;
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
		dest.writeInt(idVote);
		dest.writeInt(votant);
		dest.writeInt(notee);
		dest.writeFloat(vote);
		dest.writeString(commentaire);
	}

	public static final Parcelable.Creator<VoteDB> CREATOR = new Parcelable.Creator<VoteDB>() {
		@Override
		public VoteDB createFromParcel(Parcel source) {
			return new VoteDB(source);
		}
		@Override
		public VoteDB[] newArray(int size) {
			return new VoteDB[size];
		}
	};
	public VoteDB(Parcel in) {
		idVote = in.readInt();
		votant = in.readInt();
		notee = in.readInt();
		vote = in.readFloat();
		commentaire = in.readString();
	}
}
