package com.fjbg.thebeercode.model;
import java.sql.*;

import com.fjbg.thebeercode.R;

import android.os.Parcel;
import android.os.Parcelable;


public class FavoriDB extends Favori implements CRUD, Parcelable{
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
    
    public static boolean verifFavorite(int aimant, int favorite) throws Exception {  // verifier si la biere est dans les favoris de l'utilisateur
    	String req = "SELECT idfavori, aimant, favorite FROM Favori WHERE aimant = ? AND favorite = ?";
    	
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setInt(1, aimant);
    		pstmt.setInt(2,  favorite);
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next()) {
    			return true;
    		} else {
    			return false;
    		}
    	} catch (Exception e) { // Exception internationalisée
    		throw new Exception("Erreur de lecture/" + R.string.unknown + "/" + e.getMessage());
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
		dest.writeInt(idFavori);
		dest.writeInt(aimant);
		dest.writeInt(favorite);
	}

	public static final Parcelable.Creator<FavoriDB> CREATOR = new Parcelable.Creator<FavoriDB>() {
		@Override
		public FavoriDB createFromParcel(Parcel source) {
			return new FavoriDB(source);
		}
		@Override
		public FavoriDB[] newArray(int size) {
			return new FavoriDB[size];
		}
	};
	public FavoriDB(Parcel in) {
		idFavori = in.readInt();
		aimant = in.readInt();
		favorite = in.readInt();
	}
}
