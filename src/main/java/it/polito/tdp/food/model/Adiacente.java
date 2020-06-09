package it.polito.tdp.food.model;

public class Adiacente {

	private String name;
	private Double peso;
	
	public Adiacente(String name, Double peso) {
		super();
		this.name = name;
		this.peso = peso;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	
	
}
