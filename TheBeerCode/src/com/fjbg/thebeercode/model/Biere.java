package com.fjbg.thebeercode.model;

public class Biere {
	
	public Biere(int idBiere, String nomBiere, float coteBiere, int nbreVotes,
			String cheminImage, String paysBiere, float degreBiere) {
		this.idBiere = idBiere;
		this.nomBiere = nomBiere;
		this.coteBiere = coteBiere;
		this.nbreVotes = nbreVotes;
		this.cheminImage = cheminImage;
		this.paysBiere = paysBiere;
		this.degreBiere = degreBiere;
	}
	
	public Biere(int idBiere) {
		super();
		this.idBiere = idBiere;
	}

	public Biere(){
		
	}
	
	
	public int getIdBiere() {
		return idBiere;
	}

	public void setIdBiere(int idBiere) {
		this.idBiere = idBiere;
	}

	public String getNomBiere() {
		return nomBiere;
	}

	public void setNomBiere(String nomBiere) {
		this.nomBiere = nomBiere;
	}

	public float getCoteBiere() {
		return coteBiere;
	}

	public void setCoteBiere(float coteBiere) {
		this.coteBiere = coteBiere;
	}

	public int getNbreVotes() {
		return nbreVotes;
	}

	public void setNbreVotes(int nbreVotes) {
		this.nbreVotes = nbreVotes;
	}

	public String getCheminImage() {
		return cheminImage;
	}

	public void setCheminImage(String cheminImage) {
		this.cheminImage = cheminImage;
	}

	public String getPaysBiere() {
		return paysBiere;
	}

	public void setPaysBiere(String paysBiere) {
		this.paysBiere = paysBiere;
	}

	public float getDegreBiere() {
		return degreBiere;
	}

	public void setDegreBiere(float degreBiere) {
		this.degreBiere = degreBiere;
	}


	protected int idBiere;
	protected String nomBiere;
	protected float coteBiere;
	protected int nbreVotes;
	protected String cheminImage;
	protected String paysBiere;
	protected float degreBiere;
	
}
