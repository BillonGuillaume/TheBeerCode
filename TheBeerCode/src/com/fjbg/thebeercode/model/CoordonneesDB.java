package com.fjbg.thebeercode.model;
import java.sql.*;
import java.util.ArrayList;

public class CoordonneesDB extends Coordonnees implements CRUD{
	protected static Connection dbConnect=null;

    public CoordonneesDB() {
    }
    
    public CoordonneesDB(String nomLieu, double longitude,
			double latitude) {
		super(0, nomLieu, longitude, latitude);
		// TODO Auto-generated constructor stub
	}
    
    public CoordonneesDB(int idCoordonnee, String nomLieu, double longitude,
			double latitude) {
		super(idCoordonnee, nomLieu, longitude, latitude);
		// TODO Auto-generated constructor stub
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
       }
       catch(Exception e ){
          
                throw new Exception("Erreur de création "+e.getMessage());
             }
       finally{
            try{
              cstmt.close();
            }
            catch (Exception e){}
        }
    }
   
   public void read ()throws Exception{
	
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
	     	this.longitude=rs.getFloat("LONGITUDE");
	     	this.latitude=rs.getInt("LATITUDE");
        }
	      else { 
	             throw new Exception("Code inconnu");
	      }

            }
	catch(Exception e){
             
                throw new Exception("Erreur de lecture "+e.getMessage());
             }
        finally{ 
            try{
              cstmt.close();
            }
            catch (Exception e){}
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
	     	     
	     }	
	   catch (Exception e){
	     	
                throw new Exception("Erreur d'effacement "+e.getMessage());
             }
           finally{ 
            try{
              cstmt.close();
            }
            catch (Exception e){}
          }
     }
     
     public static ArrayList<CoordonneesDB> rechCoordonnees(String lieuRecherche) throws Exception{
         ArrayList<CoordonneesDB> lieux=new ArrayList<CoordonneesDB>();    
         String req = "{?=call READCOORDONNEE2(?)}";
         
         CallableStatement cstmt=null;
         try{
         cstmt=dbConnect.prepareCall(req);
         cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
         cstmt.setString(2,lieuRecherche);
 	     cstmt.executeQuery();
         ResultSet rs=(ResultSet)cstmt.getObject(1);
 	     boolean trouve=false;
         
 	     while(rs.next()){
            trouve=true;
 	        int idCoordonnee=rs.getInt(1);
 	     	String nomLieu=rs.getString(2);
            double longitude=rs.getDouble(3);
    		double latitude=rs.getDouble(4);
 	     	lieux.add(new CoordonneesDB(idCoordonnee,nomLieu,longitude,latitude));
 	      }
 	   
          if(!trouve)throw new Exception("Aucun  lieu ne correspond");
            else return lieux;
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