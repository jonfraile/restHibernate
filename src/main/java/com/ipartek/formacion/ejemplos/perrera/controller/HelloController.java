package com.ipartek.formacion.ejemplos.perrera.controller;

import io.swagger.annotations.Api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.ipartek.formacion.ejemplos.perrera.pojo.Perro;

@Path("/hello")
@Api(value = "/hello")
public class HelloController {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {

		String output = "Jersey say : " + msg;

		return Response.status(200).entity(output).build();

	}

	@GET
	@Path("/json/perro")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPerro(@PathParam("param") String msg) {

		Perro perro = new Perro("Lagun", "Americano");
		return Response.status(200).entity(perro).build();

	}

}
