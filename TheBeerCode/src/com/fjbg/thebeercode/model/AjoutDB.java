package com.fjbg.thebeercode.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import android.util.Log;

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
    	String req = "SELECT * FROM vueAjout WHERE rownum>=? AND rownum<=? AND idPersonne=? ORDER BY idBiere";
    	ArrayList <String> list = new ArrayList<String>();
    	AjoutDB obj;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, min);
    		pstmt.setInt(2, max);
    		pstmt.setInt(3, id);
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
    		if (list.size() == 0 && min==1) throw new Exception("Vous n'avez ajouté aucune bière.");
    		else if (list.size() == 0) throw new Exception("Plus d'ajout à afficher.");
    		return list;
    	} catch (Exception e) {
    		throw new Exception("Erreur de lecture " + e.getMessage());
    	} finally {
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
        }
    }
}