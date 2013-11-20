package com.fjbg.thebeercode.model;

public class Historique {
	protected int idHistorique;
	protected int acteur;
	protected int utilise;
	protected String action;
	
	public Historique() {
		
	}
	
	public Historique(int idHistorique, int acteur, int utilise, String action) {
		this.idHistorique = idHistorique;
		this.acteur = acteur;
		this.utilise = utilise;
		this.action = action;
	}

	public int getIdHistorique() {
		return idHistorique;
	}

	public void setIdHistorique(int idHistorique) {
		this.idHistorique = idHistorique;
	}

	public int getActeur() {
		return acteur;
	}

	public void setActeur(int acteur) {
		this.acteur = acteur;
	}

	public int getUtilise() {
		return utilise;
	}

	public void setUtilise(int utilise) {
		this.utilise = utilise;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
}
