package com.publishingsystem.mainclasses;

public class Criticism {
	private int criticismId;
	private String criticism;
	private String answer;
	
	public Criticism(String criticism) {
		this.criticism = criticism;
	}
	
	public int getCriticismId() {
		return criticismId;
	}
	
	public void setCriticismId(int criticismId) {
		this.criticismId = criticismId;
	}
	
	public void answer(String answer) {
		this.answer = answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	public String getCriticism() {
		return this.criticism;
	}
}
