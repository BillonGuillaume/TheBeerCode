package com.fjbg.thebeercode.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Personne implements Parcelable{
	protected int idPersonne;
	protected String login;
	protected String mdp;
	protected String mail;
	protected String pays;
	
	public Personne() {
		
	}
	
	public Personne(int idPersonne, String login, String mdp, String mail, String pays) {
		this.idPersonne = idPersonne;
		this.login = login;
		this.mdp = mdp;
		this.mail = mail;
		this.pays = pays;
	}
	
	public int getIdPersonne() {
		return idPersonne;
	}
	
	public void setIdPersonne(int idPersonne) {
		this.idPersonne = idPersonne;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPays() {
		return pays;
	}
	public void setPays(String pays) {
		this.pays = pays;
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

	public static final Parcelable.Creator<Personne> CREATOR = new Parcelable.Creator<Personne>() {
		@Override
		public Personne createFromParcel(Parcel source) {
			return new Personne(source);
		}
		@Override
		public Personne[] newArray(int size) {
			return new Personne[size];
		}
	};
	public Personne(Parcel in) {
		idPersonne = in.readInt();
		login = in.readString();
		mdp = in.readString();
		mail = in.readString();
		pays=in.readString();
	}

}
