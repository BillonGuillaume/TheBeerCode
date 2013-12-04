package com.fjbg.thebeercode.model;
import java.sql.*;

import com.fjbg.thebeercode.R;

public class HistoriqueDB extends Historique implements CRUD {

	public static Connection dbConnect = null;
    
    public HistoriqueDB() {
    }
    
    public HistoriqueDB(int idHistorique, int acteur, int utilise, String action) {
        super(idHistorique, acteur, utilise, action);
    }
    
    public static void setDbConnect(Connection dbConnect) {
        HistoriqueDB.dbConnect = dbConnect;
    }

    @Override
    public void create() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call createHistorique(?,?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER); 
            cstmt.setInt(2, acteur);
            cstmt.setInt(3, utilise);
            cstmt.setString(4, action);
            cstmt.executeUpdate();
            this.idHistorique = cstmt.getInt(1);
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de création/" + R.string.e000 + "/" + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void read() throws Exception {
        String req = "{?=call readPersonne(?)}";
        Boolean ex = false;
        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, idHistorique);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            if (rs.next()) {
                this.idHistorique = rs.getInt("IDHISTORIQUE");
                this.acteur = rs.getInt("ACTEUR");
                this.utilise = rs.getInt("UTILISE");
                this.action = rs.getString("ACTION");
            } else {
                ex = true; //throw new Exception("identifiant de l'historique inconnu");
            }
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
        } finally {
        	if(ex) {
        		throw new Exception("Erreur personnalisée/" + R.string.e207 + "/" + "identifiant de l'historique inconnu");
        	}
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void readHistorique() throws Exception {  // verifier si la biere a été crée par l'utilisateur
    	String req = "SELECT idhistorique, acteur, utilise, action FROM Historique WHERE acteur = ? AND utilise = ? AND action LIKE 'ajout'";
    	Boolean ex = false;
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, acteur);
    		pstmt.setInt(2,  utilise);
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next()) {
    			this.idHistorique = rs.getInt("IDHISTORIQUE");
    			this.action = rs.getString("ACTION");    			
    		} else {
    			ex = true; //throw new Exception("Pas créateur");
    		}
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
    	} finally {
    		if(ex) {
        		throw new Exception("Erreur personnalisée/" + R.string.e211 + "/" + "no match");
        	}
    		try {
    			pstmt.close();
    		} catch (Exception e) {
    		}
    	}
    }

    @Override
    public void update() throws Exception {
        // Not implemented
    }

    @Override
    public void delete() throws Exception {
    	// Not implemented
    }

}
