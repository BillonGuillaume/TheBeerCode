package com.fjbg.thebeercode.model;
import java.sql.*;

import com.fjbg.thebeercode.R;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonneDB extends Personne implements CRUD, Parcelable{
    
    public static Connection dbConnect = null;
    
    public PersonneDB() {
    }
    
    public PersonneDB(int idPersonne, String login, String mdp, String mail, String pays) {
        super(idPersonne, login, mdp, mail, pays);
    }
    
    public static void setDbConnect(Connection dbConnect) {
        PersonneDB.dbConnect = dbConnect;
    }

    @Override
    public void create() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call createPersonne(?,?,?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, java.sql.Types.INTEGER); 
            cstmt.setString(2, login);
            cstmt.setString(3, mdp);
            cstmt.setString(4, mail);
            cstmt.setString(5, pays);
            cstmt.executeUpdate();
            this.idPersonne = cstmt.getInt(1);
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de cr�ation/" + R.string.e000 + "/" + e.getMessage());
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
        Boolean ex = false;

        CallableStatement cstmt = null;
        try {
            cstmt = dbConnect.prepareCall(req);
            cstmt.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cstmt.setInt(2, idPersonne);
            cstmt.executeQuery();
            ResultSet rs = (ResultSet) cstmt.getObject(1);
            if (rs.next()) {
                this.idPersonne = rs.getInt("IDPERSONNE");
                this.login = rs.getString("LOGIN");
                this.mdp = rs.getString("MDP");
                this.mail = rs.getString("MAIL");
                this.pays = rs.getString("PAYS");
            } else {
            	ex = true;
                //throw new Exception("identifiant de la personne inconnu");
            }
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de lecture/" + R.string.e001 + "/" + e.getMessage());
        } finally {
        	if(ex) {
        		throw new Exception("Erreur personnalis�e/" + R.string.e200 + "/" + "identifiant de la personne inconnu");
        	}
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void update() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call updatePersonne(?,?,?,?,?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.setInt(1, idPersonne);
            cstmt.setString(2, login);
            cstmt.setString(3, mdp);
            cstmt.setString(4, mail);
            cstmt.setString(5, pays);
            cstmt.executeUpdate();
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur de mise � jour/" + R.string.e002 + "/" + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void delete() throws Exception {
        CallableStatement cstmt = null;
        try {
            String req = "call delPersonne(?)";
            cstmt = dbConnect.prepareCall(req);
            cstmt.setInt(1, idPersonne);
            cstmt.executeUpdate();
        } catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
        } catch (Exception e) {
            throw new Exception("Erreur d'effacement/" + R.string.e003 + "/" + e.getMessage());
        } finally {
            try {
                cstmt.close();
            } catch (Exception e) {
            }
        }
    }
    
    public void connection() throws Exception {
    	Boolean ex = false;
    	String req = "SELECT idPersonne, login, mdp, mail, pays FROM Personne WHERE login = ? AND mdp = ?";
    	
    	PreparedStatement pstmt = null;
    	try {
    		pstmt = dbConnect.prepareStatement(req);
    		pstmt.setString(1, login);
    		pstmt.setString(2,  mdp);
    		ResultSet rs = pstmt.executeQuery();
    		if (rs.next()) {
    			this.idPersonne = rs.getInt("IDPERSONNE");
    			this.login = rs.getString("LOGIN");
    			this.mdp = rs.getString("MDP");
    			this.mail = rs.getString("MAIL");
    			this.pays = rs.getString("PAYS");
    		} else {
    			ex = true;
    		}
    	} catch(SQLException e) {
        	throw new Exception("Erreur SQL/" + R.string.e100 + "/" + e.getMessage());
    	} catch (Exception e) {
    		throw new Exception("Erreur de lecture/" + R.string.unknown + "/" + e.getMessage());
    	} finally {
    		if(ex) {
    			throw new Exception("Erreur Personnalis�e/" + R.string.e201 + "/" + "Login et/ou mot de passe incorrect(s)");
    		}
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
		dest.writeInt(idPersonne);
		dest.writeString(login);
		dest.writeString(mdp);
		dest.writeString(mail);
		dest.writeString(pays);
	}

	public static final Parcelable.Creator<PersonneDB> CREATOR = new Parcelable.Creator<PersonneDB>() {
		@Override
		public PersonneDB createFromParcel(Parcel source) {
			return new PersonneDB(source);
		}
		@Override
		public PersonneDB[] newArray(int size) {
			return new PersonneDB[size];
		}
	};
	public PersonneDB(Parcel in) {
		idPersonne = in.readInt();
		login = in.readString();
		mdp = in.readString();
		mail = in.readString();
		pays = in.readString();
	}

}
