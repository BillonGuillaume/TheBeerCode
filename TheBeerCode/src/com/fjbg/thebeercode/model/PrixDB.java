package com.fjbg.thebeercode.model;
import java.sql.*;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

public class PrixDB extends Prix implements CRUD{
	protected static Connection dbConnect=null;

	public PrixDB() {
	}


	public PrixDB(float prix, int localisation, int localisee) {
		super(0, prix, localisation, localisee);
	}

	public PrixDB(int idPrix, float prix, int localisation, int localisee) {
		super(idPrix, prix, localisation, localisee);
	}



	public static void setConnection(Connection nouvdbConnect) {
		dbConnect=nouvdbConnect;
	}


	public void create() throws Exception{
		CallableStatement   cstmt=null;
		try{
			String req = "call createcoordonnee(?,?,?,?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
			cstmt.setFloat(2,prix);
			cstmt.setInt(3,localisation);
			cstmt.setInt(4,localisee);
			cstmt.executeUpdate();
			this.idPrix=cstmt.getInt(1);
		} catch(SQLException e) {
			throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
		} catch (Exception e) {
			throw new Exception("Erreur de création/" + R.string.e000 + "/" + e.getMessage());
		} finally{
			try{
				cstmt.close();
			} catch (Exception e){}
		}
	}

	public void read ()throws Exception{
		// pas besoin pour l'instant

	}

	public void update() throws Exception {
		CallableStatement   cstmt=null;
		try{
			String req = "call updateprix(?,?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.setInt(1,idPrix);
			cstmt.setFloat(2,prix);
			cstmt.executeUpdate();
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de mise à jour/" + R.string.e002 + "/" + e.getMessage());
		} finally{
			try{
				cstmt.close();
			} catch (Exception e){}
		}
	}

	public void delete() throws Exception {
		CallableStatement cstmt =null;
		try{
			String req = "call delprix(?)";
			cstmt = dbConnect.prepareCall(req);
			cstmt.setInt(1,idPrix);
			cstmt.executeUpdate();
		} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur d'effacement/" + R.string.e003 + "/" + e.getMessage());
		} finally{ 
			try{
				cstmt.close();
			} catch (Exception e){}
		}
	}

	public static ArrayList<PrixDB> rechPrix(int idBiere) throws Exception{  // TODO A internationaliser
		ArrayList<PrixDB> prix=new ArrayList<PrixDB>();    
		String req = "{?=call READPRIX(?)}";

		CallableStatement cstmt=null;
		try{
			cstmt=dbConnect.prepareCall(req);
			cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
			cstmt.setInt(2,idBiere);
			cstmt.executeQuery();
			ResultSet rs=(ResultSet)cstmt.getObject(1);
			boolean trouve=false;

			while(rs.next()){
				trouve=true;
				int idPrix=rs.getInt(1);
				float px=rs.getFloat(2);
				int localisation=rs.getInt(3);
				int localisee=rs.getInt(4);
				prix.add(new PrixDB(idPrix,px,localisation,localisee));
			}

			if(!trouve)throw new Exception("Aucune bière ne provient de ce pays");
			else return prix;
		}

		catch(Exception e){
			throw new Exception("Erreur de lecture "+e.getMessage());
		}

		finally{//effectué dans tous les cas 
			try{
				cstmt.close();
			}
			catch (Exception e){}
		}
	}
}