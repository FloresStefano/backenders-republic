package it.addvalue.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address {
	private @Id @GeneratedValue Long id;
	private String indirizzo;
	private String numeroCivo;
	
	public Address() {
		this.indirizzo = null;
		this.numeroCivo = null;
	}

	public Address(String indirizzo, String numeroCivo) {
		super();
		this.indirizzo = indirizzo;
		this.numeroCivo = numeroCivo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNumeroCivo() {
		return numeroCivo;
	}

	public void setNumeroCivo(String numeroCivo) {
		this.numeroCivo = numeroCivo;
	}

	
	
	
}
