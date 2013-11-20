package com.fjbg.thebeercode.model;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;


public class VoteDB extends Vote implements CRUD{
public static Connection dbConnect = null;
    
    public VoteDB() {
    }
    
    public VoteDB(int idVote, int votant, int notee, float vote, String commentaire) {
        super(idVote, votant, notee, vote, commentaire);
    }
    
    public static void setDbConnect(Connection dbConnect) {
        PersonneDB.dbConnect = dbConnect;
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
                this.vote = rs.getFloat("VOTEE");
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
    
    public ArrayList<VoteDB> readCommentaires() throws Exception {
    	String req = "{?=call readCommentaire(?)}";
    	ArrayList <VoteDB> listCom = new ArrayList<VoteDB>();
    	VoteDB obj;
        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, notee);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            while (rs.next()) {
            	obj = new VoteDB();
                this.idVote = rs.getInt("IDVOTE");
                this.votant = rs.getInt("VOTANT");
                this.notee = rs.getInt("NOTEE");
                this.vote = rs.getFloat("VOTEE");
                this.commentaire = rs.getString("COMMENTAIRE");
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
}
