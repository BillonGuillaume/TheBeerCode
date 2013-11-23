package com.fjbg.thebeercode.myconnections;

import java.sql.*;
import java.util.*;

import com.fjbg.thebeercode.model.BiereDB;
import com.fjbg.thebeercode.model.CoordonneesDB;
import com.fjbg.thebeercode.model.FavoriDB;
import com.fjbg.thebeercode.model.HistoriqueDB;
import com.fjbg.thebeercode.model.PersonneDB;
import com.fjbg.thebeercode.model.PrixDB;
import com.fjbg.thebeercode.model.VoteDB;

public class DBConnection {

	protected String serverName;
	protected String username;
	protected String password;
	protected String dbName;
	protected String dbPort;
	protected static DBConnection instance;

	public DBConnection(){
		PropertyResourceBundle properties = (PropertyResourceBundle)
				PropertyResourceBundle.getBundle("resources.application");
		serverName=properties.getString("cours.DB.server");
		dbName =properties.getString("cours.DB.database");
		username=properties.getString("cours.DB.login");
		password=properties.getString("cours.DB.password");
		dbPort=properties.getString("cours.DB.port");
	}


	public DBConnection(String username,String password){
		this();
		this.username=username;
		this.password=password;
	}

	public static DBConnection getInstance(){
		if(instance == null)
			instance = new DBConnection();
		return instance;
	}

	public void init(Connection connect)throws Exception{
		if(connect==null)throw new Exception("Connexion impossible");
		PersonneDB.setDbConnect(connect);
		FavoriDB.setDbConnect(connect);
		HistoriqueDB.setDbConnect(connect);
		VoteDB.setDbConnect(connect);
		BiereDB.setConnection(connect);
		CoordonneesDB.setConnection(connect);
		PrixDB.setConnection(connect);
	}



	public Connection getConnection() throws Exception{
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@//"+serverName+":"+dbPort+"/"+dbName;
			Connection dbConnect = DriverManager.getConnection(url, username, password);
			return dbConnect;

		}
		catch(Exception e) {
			e.printStackTrace();
			throw new Exception(e);     
		}
	}

}


