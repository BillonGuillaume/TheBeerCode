package com.fjbg.thebeercode.model;

public class Coordonnees {
	public Coordonnees(){
		
	}
	
	public Coordonnees(int idCoordonnee, String nomLieu, double longitude,
			double latitude) {
		this.idCoordonnee = idCoordonnee;
		this.nomLieu = nomLieu;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public int getIdCoordonnee() {
		return idCoordonnee;
	}
	public void setIdCoordonnee(int idCoordonnee) {
		this.idCoordonnee = idCoordonnee;
	}
	public String getNomLieu() {
		return nomLieu;
	}
	public void setNomLieu(String nomLieu) {
		this.nomLieu = nomLieu;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	protected int idCoordonnee;
	protected String nomLieu;
	protected double longitude;
	protected double latitude;
}
