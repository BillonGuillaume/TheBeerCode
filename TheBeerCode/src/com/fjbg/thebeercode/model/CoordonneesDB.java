package com.fjbg.thebeercode.model;
import java.sql.*;
import java.util.ArrayList;

import com.fjbg.thebeercode.R;

public class CoordonneesDB extends Coordonnees implements CRUD{
	protected static Connection dbConnect=null;

    public CoordonneesDB() {
    }
    
    public CoordonneesDB(String nomLieu, double longitude, double latitude) {
		super(0, nomLieu, longitude, latitude);
	}
    
    public CoordonneesDB(int idCoordonnee, String nomLieu, double longitude, double latitude) {
		super(idCoordonnee, nomLieu, longitude, latitude);
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
			cstmt.setString(2,nomLieu);
			cstmt.setDouble(3,longitude);
			cstmt.setDouble(4,latitude);
			cstmt.executeUpdate();
			this.idCoordonnee=cstmt.getInt(1);
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
	   Boolean ex = false;
	   String req = "{?=call readcoordonnee(?)}";

	   CallableStatement cstmt=null;
	   try{
		   cstmt=dbConnect.prepareCall(req);
		   cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
		   cstmt.setInt(2,idCoordonnee);
		   cstmt.executeQuery();
		   ResultSet rs=(ResultSet)cstmt.getObject(1);
		   if(rs.next()){
			   this.nomLieu=rs.getString("NOMLIEU");
			   this.longitude=rs.getDouble("LONGITUDE");
			   this.latitude=rs.getDouble("LATITUDE");
		   } else { 
			   ex = true; //throw new Exception("Code inconnu");
		   }
	   } catch(SQLException e) {
       	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
       } catch (Exception e) {
           throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
	   } finally{
		   if(ex) {
			   throw new Exception("Erreur Personnalisée/" + R.string.e207 + "/" + "Code inconnu");
		   }
		   try{
			   cstmt.close();
		   } catch (Exception e){}
	   }
   }
   
     public void update() throws Exception {
    	 //Pas utilisé pour l'instant
     }
     
     public void delete() throws Exception {
    	 CallableStatement cstmt =null;
    	 try{
    		 String req = "call delcoordonnee(?)";
    		 cstmt = dbConnect.prepareCall(req);
    		 cstmt.setInt(1,idCoordonnee);
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
     
     public static ArrayList<CoordonneesDB> rechCoordonnees(String lieuRecherche) throws Exception{
    	 ArrayList<CoordonneesDB> lieux=new ArrayList<CoordonneesDB>();    
    	 String req = "{?=call READCOORDONNEE2(?)}";
    	 Boolean trouve = false;

    	 CallableStatement cstmt=null;
    	 try{
    		 cstmt=dbConnect.prepareCall(req);
    		 cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
    		 cstmt.setString(2,lieuRecherche);
    		 cstmt.executeQuery();
    		 ResultSet rs=(ResultSet)cstmt.getObject(1);

    		 while(rs.next()){
    			 trouve=true;
    			 int idCoordonnee=rs.getInt(1);
    			 String nomLieu=rs.getString(2);
    			 double longitude=rs.getDouble(3);
    			 double latitude=rs.getDouble(4);
    			 lieux.add(new CoordonneesDB(idCoordonnee,nomLieu,longitude,latitude));
    		 }
    		 if(trouve) return lieux;
    	 } catch(Exception e){
    		 throw new Exception("Erreur de lecture "+e.getMessage());
    	 } finally{
    		 if(!trouve) throw new Exception("Erreur personnalisée/" + R.string.e208 + "/" + "Aucun lieu correspondant");
    		 try{
    			 cstmt.close();
    		 } catch (Exception e){}
    	 }
		return null;
     }
 }