package com.ipartek.formacion.ejemplos.perrera.pojo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "empleado")
public class Empleado {

	@Id
	@GeneratedValue
	private int id;// clave y se genera automaticamente

	/* Persistente, un tipo basico (string) */
	@Basic
	@Column(name = "nombre")
	private String nombre;

	/**
	 * @param nombre
	 */
	public Empleado(String nombre) {
		super();
		this.nombre = nombre;
	}

	public Empleado() {
		super();
		this.nombre = "";
	}

	public String getNombre() {
		return this.nombre;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Empleado [id=" + this.id + ", nombre=" + this.nombre + "]";
	}

}
