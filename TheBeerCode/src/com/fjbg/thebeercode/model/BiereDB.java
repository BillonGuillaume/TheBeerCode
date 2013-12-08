package com.fjbg.thebeercode.model;

import java.sql.*;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class BiereDB extends Biere implements CRUD, Parcelable{
	protected static Connection dbConnect=null;

	public BiereDB() {
	}

	public BiereDB(String nomBiere)
	{
		this.nomBiere= nomBiere;
	}

	public BiereDB(int idBiere) {
		super(idBiere);
	}

	public BiereDB(String nomBiere, float coteBiere, int nbreVotes,
			String cheminImage, String paysBiere, float degreBiere) {
		super(0,nomBiere, coteBiere, nbreVotes,
				cheminImage, paysBiere, degreBiere);
	}

	public BiereDB(int idBiere, String nomBiere, float coteBiere, int nbreVotes,
			String cheminImage, String paysBiere, float degreBiere) {
		super(idBiere,nomBiere, coteBiere, nbreVotes,
				cheminImage, paysBiere, degreBiere);
	}

	public static void setConnection(Connection nouvdbConnect) {
		dbConnect=nouvdbConnect;
	}


	public void create() throws Exception{
		CallableStatement cstmt=null;
		Boolean exc = false;
		try {
			readBiere();
			exc = true;
		} catch(Exception ex) {
			try{
				String req = "call createbiere(?,?,?,?,?)";
				cstmt = dbConnect.prepareCall(req);
				cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
				cstmt.setString(2,nomBiere);
				cstmt.setString(3,cheminImage);
				cstmt.setString(4,paysBiere);
				cstmt.setFloat(5,degreBiere);
				cstmt.executeUpdate();
				this.idBiere=cstmt.getInt(1);
			} catch(SQLException e) {
				throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
			} catch(Exception e ){          
				throw new Exception("Erreur de création/" + R.string.e000 + "/" + e.getMessage());
			} finally{
				try{
					cstmt.close();
				} catch (Exception e){}
			}
		}if(exc) {
			throw new Exception("Erreur de création/" + R.string.e101 + "/Bière déjà existante");
		}
	}

	public void read ()throws Exception{
		Boolean ex = false;
		String req = "{?=call readbiere(?)}";

		CallableStatement cstmt=null;
		try{
			cstmt=dbConnect.prepareCall(req);
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setInt(2,idBiere);
			cstmt.executeQuery();
			ResultSet rs=(ResultSet)cstmt.getObject(1);
			if(rs.next()){
				this.nomBiere=rs.getString("NOMBIERE");
				this.coteBiere=rs.getFloat("COTEBIERE");
				this.nbreVotes=rs.getInt("NBREVOTES");
				this.cheminImage=rs.getString("CHEMINIMAGE");
				this.paysBiere=rs.getString("PAYSBIERE");
				this.degreBiere=rs.getFloat("DEGREBIERE");
			}
			else {
				ex = true;
			}
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
		}
		catch(Exception e){
			throw new Exception("Erreur de lecture "+e.getMessage());
		}
		finally{
			if(ex) {
				throw new Exception("Erreur personnalisée/" + R.string.e204 + "/" + "code inconnu");
			}
			try{
				cstmt.close();
			}
			catch (Exception e){}
		}
	}

	public void update() throws Exception {
		CallableStatement   cstmt=null;
		try{
			String req = "call updatebiere(?,?,?,?,?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.setInt(1,idBiere);
			cstmt.setString(2,nomBiere);
			cstmt.setString(3, cheminImage);
			cstmt.setString(4, paysBiere);
			cstmt.setFloat(5, degreBiere);
			cstmt.executeUpdate();
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de mise à jour/" + R.string.e002 + "/" + e.getMessage());
        } finally{
			try{
				cstmt.close();
			}
			catch (Exception e){}
		}
	}

	public void delete() throws Exception {
		CallableStatement cstmt =null;
		try{
			String req = "call delbiere(?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.setInt(1,idBiere);
			cstmt.executeUpdate();
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur d'effacement/" + R.string.e003 + "/" + e.getMessage());
		} finally{ 
			try{
				cstmt.close();
			} catch (Exception e) {}
		}
	}


	public static ArrayList<BiereDB> rechBieres(String nomBiere, float degInf, float degSup, String pays, float noteMin, float noteMax, int min, int max) throws Exception{
		//String req = "SELECT * FROM Biere WHERE rownum>=? AND rownum<=? AND upper(nomBiere) LIKE '%'||?||'%' AND (degreBiere BETWEEN ? AND ?) AND paysBiere LIKE '%'||?||'%' AND (coteBiere BETWEEN ? AND ?)";
		String req = "select * from ( select a.*, rownum rnum from (SELECT * FROM Biere WHERE upper(nomBiere) LIKE '%'||?||'%' AND (degreBiere BETWEEN ? AND ?) AND paysBiere LIKE '%'||?||'%' AND (coteBiere BETWEEN ? AND ?)) a where rownum <= ? ) where rnum >= ?";
		ArrayList <BiereDB> list = new ArrayList<BiereDB>();
		BiereDB obj;
		Boolean ex = false;
		PreparedStatement pstmt = null;
		Log.d("BiereDB", "Recherche de " + min + " à " + max);
		try {
			pstmt = dbConnect.prepareStatement(req);
			nomBiere = nomBiere.toUpperCase();
			pstmt.setString(1, nomBiere);
			pstmt.setFloat(2, degSup);
			pstmt.setFloat(3, degInf);
			pstmt.setString(4, pays);
			pstmt.setFloat(5, noteMin*2);
			pstmt.setFloat(6, noteMax*2);
			pstmt.setInt(7, max);
			pstmt.setInt(8, min);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				obj = new BiereDB();
				obj.idBiere = rs.getInt("IDBIERE");
				obj.nomBiere=rs.getString("NOMBIERE");
				obj.coteBiere=rs.getFloat("COTEBIERE");
				obj.nbreVotes=rs.getInt("NBREVOTES");
				obj.cheminImage=rs.getString("CHEMINIMAGE");
				obj.paysBiere=rs.getString("PAYSBIERE");
				obj.degreBiere=rs.getFloat("DEGREBIERE");
				list.add(obj);
			}
			if (list.size() == 0) ex = true;//throw new Exception("Aucune bière correspondante.");
			return list;
		} catch (Exception e) {
			throw new Exception("Erreur de lecture " + e.getMessage());
		} finally {
			if(ex) {
				throw new Exception("Erreur Personnalisée/" + R.string.e205 + "/" + "Aucune biere correspondante au filtre");
			}
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	public static ArrayList<BiereDB> readBieres(int min, int max) throws Exception{
		String req = "select * from ( select a.*, rownum rnum from (SELECT * FROM Biere ORDER BY idBiere DESC) a where rownum <= ? ) where rnum >= ?";
		ArrayList <BiereDB> list = new ArrayList<BiereDB>();
		BiereDB obj;
		Boolean ex1 = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = dbConnect.prepareStatement(req);
			pstmt.setInt(1, max);
			pstmt.setInt(2, min);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				obj = new BiereDB();
				obj.idBiere = rs.getInt("IDBIERE");
				obj.nomBiere=rs.getString("NOMBIERE");
				obj.coteBiere=rs.getFloat("COTEBIERE");
				obj.nbreVotes=rs.getInt("NBREVOTES");
				obj.cheminImage=rs.getString("CHEMINIMAGE");
				obj.paysBiere=rs.getString("PAYSBIERE");
				obj.degreBiere=rs.getFloat("DEGREBIERE");
				list.add(obj);
			}
			if (list.size() == 0 && min ==1) ex1 = true;//throw new Exception("Aucune bière existante.");
			return list;
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
    	} catch (Exception e) {
    		throw new Exception("Erreur de lecture/" + R.string.unknown + "/" + e.getMessage());
		} finally {
			if(ex1) {
				throw new Exception("Erreur Personnalisée/" + R.string.e206 + "/" + "Aucune bière à afficher");
			}
			try {
				pstmt.close();
			} catch (Exception e) {
			}
		}
	}

	public void readBiere()throws Exception{

		String req = "{?=call readbiere2(?)}";
		Boolean ex = false;

		CallableStatement cstmt=null;
		try{
			cstmt=dbConnect.prepareCall(req);
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setString(2,nomBiere);
			cstmt.executeQuery();
			ResultSet rs=(ResultSet)cstmt.getObject(1);
			if(rs.next()){
				this.idBiere=rs.getInt("IDBIERE");
				this.coteBiere=rs.getFloat("COTEBIERE");
				this.nbreVotes=rs.getInt("NBREVOTES");
				this.cheminImage=rs.getString("CHEMINIMAGE");
				this.paysBiere=rs.getString("PAYSBIERE");
				this.degreBiere=rs.getFloat("DEGREBIERE");
			} else { 
				ex = true;//throw new Exception("Bière inconnue");
			}

		} catch(Exception e){
			throw new Exception("Erreur de lecture "+e.getMessage());
		} finally{
			if(ex){
				throw new Exception("Erreur Personnalisée/" + R.string.e204 + "/" + "Bière inconnue");
			}
			try{
				cstmt.close();
			} catch (Exception e){}
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
		dest.writeFloat(coteBiere);
		dest.writeInt(nbreVotes);
		dest.writeString(cheminImage);
		dest.writeString(paysBiere);
		dest.writeFloat(degreBiere);
	}

	public static final Parcelable.Creator<BiereDB> CREATOR = new Parcelable.Creator<BiereDB>() {
		@Override
		public BiereDB createFromParcel(Parcel source) {
			return new BiereDB(source);
		}
		@Override
		public BiereDB[] newArray(int size) {
			return new BiereDB[size];
		}
	};

	public BiereDB(Parcel in) {
		idBiere = in.readInt();
		nomBiere = in.readString();
		coteBiere = in.readFloat();
		nbreVotes = in.readInt();
		cheminImage = in.readString();
		paysBiere = in.readString();
		degreBiere = in.readFloat();
	}
}
