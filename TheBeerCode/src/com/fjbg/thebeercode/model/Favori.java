package com.fjbg.thebeercode.model;

public class Favori {
	protected int idFavori;
	protected int aimant;
	protected int favorite;
	
	public Favori() {
		
	}
	
	public Favori(int idFavori, int aimant, int favorite) {
		super();
		this.idFavori = idFavori;
		this.aimant = aimant;
		this.favorite = favorite;
	}

	public int getIdFavori() {
		return idFavori;
	}

	public void setIdFavori(int idFavori) {
		this.idFavori = idFavori;
	}

	public int getAimant() {
		return aimant;
	}

	public void setAimant(int aimant) {
		this.aimant = aimant;
	}

	public int getFavorite() {
		return favorite;
	}

	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
}
