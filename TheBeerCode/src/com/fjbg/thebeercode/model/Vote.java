package com.fjbg.thebeercode.model;

public class Vote {
	protected int idVote;
	protected int votant;
	protected int notee;
	protected float vote;
	protected String commentaire;
	
	public Vote() {
		
	}

	public Vote(int idVote, int votant, int notee, float vote, String commentaire) {
		super();
		this.idVote = idVote;
		this.votant = votant;
		this.notee = notee;
		this.vote = vote;
		this.commentaire = commentaire;
	}

	public int getIdVote() {
		return idVote;
	}

	public void setIdVote(int idVote) {
		this.idVote = idVote;
	}

	public int getVotant() {
		return votant;
	}

	public void setVotant(int votant) {
		this.votant = votant;
	}

	public int getNotee() {
		return notee;
	}

	public void setNotee(int notee) {
		this.notee = notee;
	}

	public float getVote() {
		return vote;
	}

	public void setVote(float vote) {
		this.vote = vote;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
}
