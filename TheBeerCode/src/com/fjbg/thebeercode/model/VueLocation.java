package com.fjbg.thebeercode.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

public class VueLocation extends Prix{
	public static Connection dbConnect = null;
	int idBiere;
	String nomBiere;
	String nomLieu;
	Double lon;
	Double lat;
    
    public VueLocation() {
    }
    
    public VueLocation(int idPrix, float prix, int localisation, int localisee, int idBiere, String nomBiere, String nomLieu, Double lon, Double lat) {
        super(idPrix, prix, localisation, localisee);
        this.idBiere = idBiere;
        this.nomBiere = nomBiere;
        this.nomLieu = nomLieu;
        this.lon = lon;
        this.lat = lat;
    }
    
    public static void setDbConnect(Connection dbConnect) {
        VueLocation.dbConnect = dbConnect;
    }
    
    public static ArrayList<VueLocation> readLocBiere(int id) throws Exception {
    	String req = "SELECT * FROM vueLocation WHERE idBiere = ?";
    	ArrayList <VueLocation> listLoc = new ArrayList<VueLocation>();
    	VueLocation obj;
    	Boolean ex = false;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, id);
    		ResultSet rs = pstmt.executeQuery();
    		while (rs.next()) {
    			obj = new VueLocation();
    			obj.idBiere = rs.getInt("IDBIERE");
    			obj.nomBiere = rs.getString("NOMBIERE");
    			obj.idPrix = rs.getInt("IDPRIX");
    			obj.prix = rs.getFloat("PRIX");
    			obj.nomLieu = rs.getString("NOMLIEU");
    			obj.lon = rs.getDouble("LONGITUDE");
    			obj.lat = rs.getDouble("LATITUDE");
    			listLoc.add(obj);
    		}
    		if (listLoc.size() == 0) ex = true;
    		return listLoc;
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
    	} finally {
    		if(ex) {
        		throw new Exception("Erreur personnalisée/" + R.string.e217 + "/" + "Aucune position connue");
        	}
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
        }
    }

	public String getNomBiere() {
		return nomBiere;
	}

	public String getNomLieu() {
		return nomLieu;
	}

	public Double getLon() {
		return lon;
	}

	public Double getLat() {
		return lat;
	}
}