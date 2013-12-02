package com.fjbg.thebeercode.model;

import java.sql.*;
import java.util.ArrayList;

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
    	CallableStatement   cstmt=null;
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
	     	this.degreBiere=rs.getFloat("DEGREBIERE");
	        
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
     
     public static ArrayList<BiereDB> rechBieres(String nomBiere, float degInf, float degSup, String pays, float noteMin, float noteMax, int min, int max) throws Exception{
    	String req = "SELECT * FROM Biere WHERE rownum>=? AND rownum<=? AND nomBiere LIKE '%'||?||'%' AND (degreBiere BETWEEN ? AND ?) AND paysBiere LIKE '%'||?||'%' AND (coteBiere BETWEEN ? AND ?)";
     	ArrayList <BiereDB> list = new ArrayList<BiereDB>();
     	BiereDB obj;
     	PreparedStatement pstmt = null;
     	try {
     		pstmt = dbConnect.prepareStatement(req);
     		pstmt.setInt(1, min);
     		pstmt.setInt(2, max);
     		pstmt.setString(3, nomBiere);
     		pstmt.setFloat(4, degSup);
     		pstmt.setFloat(5, degInf);
     		pstmt.setString(6, pays);
     		pstmt.setFloat(7, noteMin*2);
     		pstmt.setFloat(8, noteMax*2);
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
     		if (list.size() == 0) throw new Exception("Aucune bière correspondante.");
     		return list;
     	} catch (Exception e) {
     		throw new Exception("Erreur de lecture " + e.getMessage());
     	} finally {
     		try {
     			pstmt.close();
     		} catch (Exception e) {
     		}
         }
     }
     
     public static ArrayList<BiereDB> readBieres(int min, int max) throws Exception{
     	String req = "SELECT * FROM Biere WHERE rownum>=? AND rownum<=?";
      	ArrayList <BiereDB> list = new ArrayList<BiereDB>();
      	BiereDB obj;
      	PreparedStatement pstmt = null;
      	try {
      		pstmt = dbConnect.prepareStatement(req);
      		pstmt.setInt(1, min);
     		pstmt.setInt(2, max);
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
      		if (list.size() == 0) throw new Exception("Aucune bière existante.");
      		else if (list.size() == 0) throw new Exception("Plus de bière.");
      		return list;
      	} catch (Exception e) {
      		throw new Exception("Erreur de lecture " + e.getMessage());
      	} finally {
      		try {
      			pstmt.close();
      		} catch (Exception e) {
      		}
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
    		     	this.degreBiere=rs.getFloat("DEGREBIERE");
    		        
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
