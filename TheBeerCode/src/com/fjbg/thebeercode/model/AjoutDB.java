package com.fjbg.thebeercode.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

public class AjoutDB{
	public static Connection dbConnect = null;
	int idBiere;
	String nomBiere;
	int idHistorique;
	int idPersonne;
	String login;
    
    public AjoutDB() {
    }
    
    public AjoutDB(int idBiere, String nomBiere, int idHistorique, int idPersonne, String login) {
        this.idBiere = idBiere;
        this.nomBiere = nomBiere;
        this.idHistorique = idHistorique;
        this.idPersonne = idPersonne;
        this.login = login;
    }
    
    public static void setDbConnect(Connection dbConnect) {
        AjoutDB.dbConnect = dbConnect;
    }
    
    public static ArrayList<String> readHistoriquePersonne(int id, int min, int max) throws Exception {
    	Boolean ex1 = false;
    	String req = "select * from ( select a.*, rownum rnum from (SELECT * FROM vueAjout WHERE idPersonne=? ORDER BY idBiere) a where rownum <= ? ) where rnum >= ?";
    	ArrayList <String> list = new ArrayList<String>();
    	AjoutDB obj;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, id);
    		pstmt.setInt(2, max);
    		pstmt.setInt(3, min);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new AjoutDB();
    			obj.idBiere = rs.getInt("IDBIERE");
    			obj.nomBiere = rs.getString("NOMBIERE");
    			obj.idHistorique = rs.getInt("IDHISTORIQUE");
    			obj.idPersonne = rs.getInt("IDPERSONNE");
    			obj.login = rs.getString("LOGIN");
    			list.add(obj.nomBiere);
    		}
    		if (list.size() == 0 && min==1) ex1 = true;//throw new Exception("Vous n'avez ajouté aucune bière.");
    		return list;
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
    	} catch (Exception e) {
    		throw new Exception("Erreur de lecture " + e.getMessage());
    	} finally {
    		if(ex1) {
    			throw new Exception("Erreur Personnalisée/" + R.string.e202 + "/" + "Aucune biere à afficher");
    		}
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
        }
    }
}