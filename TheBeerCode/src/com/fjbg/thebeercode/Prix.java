package com.fjbg.thebeercode;

public class Prix {
	public Prix(){
		
	}
	
	public Prix(int idPrix, float prix, int localisation, int localisee) {
		this.idPrix = idPrix;
		this.prix = prix;
		this.localisation = localisation;
		this.localisee = localisee;
	}

	public int getIdPrix() {
		return idPrix;
	}

	public void setIdPrix(int idPrix) {
		this.idPrix = idPrix;
	}

	public float getPrix() {
		return prix;
	}

	public void setPrix(float prix) {
		this.prix = prix;
	}

	public int getLocalisation() {
		return localisation;
	}

	public void setLocalisation(int localisation) {
		this.localisation = localisation;
	}

	public int getLocalisee() {
		return localisee;
	}

	public void setLocalisee(int localisee) {
		this.localisee = localisee;
	}

	protected  int idPrix;
	protected float prix;
	protected int localisation; //idCoordonnees
	protected int localisee; //idBiere
	
}
