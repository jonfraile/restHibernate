package com.ipartek.formacion.ejemplos.perrera.pojo;

import java.sql.Timestamp;
import java.util.Date;

public class FechaHora {
	private Timestamp fechaHora;
	
	public FechaHora() {
		super();
		Date date= new Date();
		this.setFechaHora(new Timestamp(date.getTime()));
	}

	public Timestamp getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Timestamp fechaHora) {
		this.fechaHora = fechaHora;
	}
}
