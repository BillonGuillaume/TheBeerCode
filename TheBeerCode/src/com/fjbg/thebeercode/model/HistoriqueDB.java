package com.fjbg.thebeercode.model;
import java.sql.*;

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
        String req = "{?=call readPersonne(?)}";

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
                throw new Exception("identifiant de l'historique inconnu");
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

    @Override
    public void update() throws Exception {
        // Not implemented
    }

    @Override
    public void delete() throws Exception {
    	// Not implemented
    }

}
