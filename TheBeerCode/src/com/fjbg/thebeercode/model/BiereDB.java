package com.fjbg.thebeercode.model;
import java.sql.*;
import java.util.ArrayList;

import android.util.Log;

public class BiereDB extends Biere implements CRUD{
	protected static Connection dbConnect=null;

    public BiereDB() {
    }
    
    public BiereDB(String nomBiere)
    {
        this.nomBiere= nomBiere;
    }
    
    public BiereDB(int idBiere) {
		super(idBiere);
		// TODO Auto-generated constructor stub
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
        CallableStatement   cstmt=null;
       try{
	     String req = "call createbiere(?,?,?,?,?)";
	     cstmt = dbConnect.prepareCall(req);
         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
         cstmt.setString(2,nomBiere);
         cstmt.setString(3,cheminImage);
         cstmt.setString(4,paysBiere);
         cstmt.setFloat(5,degreBiere);
         try {
             cstmt.executeUpdate();
             } catch (Exception e) {
             	Log.d("BiereDB", "executeUpdate exception : " + e.getMessage());
             }
         this.idBiere=cstmt.getInt(1);
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
	     	this.degreBiere=rs.getInt("DEGREBIERE");
	        
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
         }
         catch(Exception e ){
            
                  throw new Exception("Erreur de modification "+e.getMessage());
               }
         finally{
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
     
     public static ArrayList<BiereDB> rechBieresParPays(String pays) throws Exception{
         ArrayList<BiereDB> bieres=new ArrayList<BiereDB>();    
         String req = "{?=call READBIERE3(?)}";
         
         CallableStatement cstmt=null;
         try{
         cstmt=dbConnect.prepareCall(req);
         cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
         cstmt.setString(2,pays);
 	     cstmt.executeQuery();
         ResultSet rs=(ResultSet)cstmt.getObject(1);
 	     boolean trouve=false;
         
 	     while(rs.next()){
            trouve=true;
 	        int idBiere=rs.getInt(1);
 	     	String nomBiere=rs.getString(2);
            float coteBiere=rs.getFloat(3);
    		int nbreVotes=rs.getInt(4);
    		String cheminImage=rs.getString(5);
    		String paysBiere=rs.getString(6);
    		float degreBiere=rs.getFloat(7);
 	     	bieres.add(new BiereDB(idBiere,nomBiere,coteBiere,nbreVotes,cheminImage,paysBiere,degreBiere));
 	      }
 	   
          if(!trouve)throw new Exception("Aucune bière ne provient de ce pays");
            else return bieres;
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
     
     public void readBiere ()throws Exception{
    		
    		String req = "{?=call readbiere2(?)}";
    	        
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
    		     	this.degreBiere=rs.getInt("DEGREBIERE");
    		        
    	              }
    		      else { 
    		             throw new Exception("Bière inconnue");
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
   

}
