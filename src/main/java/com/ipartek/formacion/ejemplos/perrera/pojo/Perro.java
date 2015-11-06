package com.ipartek.formacion.ejemplos.perrera.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "perro")
public class Perro {

	@Id
	@GeneratedValue
	private int Id;// clave y se genera automaticamente

	/* Persistente, un tipo basico (string) */
	@Basic
	@Column(name = "nombre")
	private String nombre;

	@Basic
	@Column(name = "raza")
	private String raza;

	/**
	 * @param nombre
	 * @param raza
	 */
	public Perro(String nombre, String raza) {
		super();
		this.nombre = nombre;
		this.raza = raza;
	}

	public Perro() {
		super();
		this.nombre = "";
		this.raza = "";
	}

	public String getNombre() {
		return this.nombre;
	}

	public int getId() {
		return this.Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRaza() {
		return this.raza;
	}

	public void setRaza(String raza) {
		this.raza = raza;
	}

	@Override
	public String toString() {
		return "Perro [nombre=" + this.nombre + ", raza=" + this.raza + "]";
	}

}
