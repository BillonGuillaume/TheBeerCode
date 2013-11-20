package com.fjbg.thebeercode.model;
import java.sql.*;


public class FavoriDB extends Favori implements CRUD{
public static Connection dbConnect = null;
    
    public FavoriDB() {
    }
    
    public FavoriDB(int idFavori, int aimant, int favorite) {
        super(idFavori, aimant, favorite);
    }
    
    public static void setDbConnect(Connection dbConnect) {
        FavoriDB.dbConnect = dbConnect;
    }

    @Override
    public void create() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call createFavori(?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER); 
            cstmt.setInt(2, aimant);
            cstmt.setInt(3, favorite);
            cstmt.executeUpdate();
            this.idFavori = cstmt.getInt(1);
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
        String req = "{?=call readFavori(?)}";

        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, idFavori);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            if (rs.next()) {
                this.idFavori = rs.getInt("IDFAVORI");
                this.aimant = rs.getInt("AIMANT");
                this.favorite = rs.getInt("FAVORITE");
            } else {
                throw new Exception("identifiant du favori inconnu");
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
    	CallableStatement cstmt = null;
        try {
            String req = "call delFavori(?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.setInt(1, idFavori);
            cstmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Erreur d'effacement " + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }
}
