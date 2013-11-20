package com.fjbg.thebeercode.model;


public class Personne {
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
	
}
