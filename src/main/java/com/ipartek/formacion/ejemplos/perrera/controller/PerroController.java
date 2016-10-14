package com.ipartek.formacion.ejemplos.perrera.controller;

import java.util.ArrayList;

import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.ipartek.formacion.ejemplos.perrera.model.HibernateUtil;
import com.ipartek.formacion.ejemplos.perrera.pojo.FechaHora;
import com.ipartek.formacion.ejemplos.perrera.pojo.Perro;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * El poryecto hace refencia al proyecto skalada
 *
 * @author Curso
 *
 */
@Path("/perro")
@Api(value = "/perro")
public class PerroController {
	private static final String MensajeOrden = "Orden ascente o descendente";
	private static final String MensajeNombre = "Ordena por el valor, nombre o raza";
	private Session s;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Listado de Perros", notes = "Listado de perros existentes en la perrera, limitado a 1.000", response = Perro.class, responseContainer = "List")
	// @ApiParam(value = Mensaje, name = "order", required = false)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getAll(
			@ApiParam(value = MensajeOrden, name = "order", required = false) @DefaultValue("asc") @QueryParam("order") String order,
			@ApiParam(value = MensajeNombre, name = "value", required = false) @DefaultValue("id") @QueryParam("value") String value) {
		try {
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();

			ArrayList<Perro> perros = null;

			// controlar QueryParam order y value
			if (order != null && value != null) {
				if (order.equals("asc") && (value.equals("nombre") || value.equals("raza") || value.equals("id"))) {
					perros = (ArrayList<Perro>) this.s.createCriteria(Perro.class).addOrder(Order.asc(value)).list();

				} else if (order.equals("desc")
						&& (value.equals("nombre") || value.equals("raza") || value.equals("id"))) {
					perros = (ArrayList<Perro>) this.s.createCriteria(Perro.class).addOrder(Order.desc(value)).list();
				} else {
					perros = (ArrayList<Perro>) this.s.createCriteria(Perro.class).list();
				}
			} else {
				perros = (ArrayList<Perro>) this.s.createCriteria(Perro.class).list();
			}

			this.s.beginTransaction().commit();
			this.s.close();

			return Response.ok().entity(perros).build();
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca un perro por su ID", notes = "devuelve un perro mediante el paso de su ID", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe perro con esa ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getById(@PathParam("id") int idPerro) {

		try {
			Perro perro = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			perro = (Perro) this.s.get(Perro.class, idPerro);
			this.s.beginTransaction().commit();
			this.s.close();
			if (perro == null) {
				return Response.noContent().build();
			}
			return Response.ok().entity(perro).build();
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Elimina un perro", notes = "Elimina un perro mediante el paso de su ID", response = Perro.class, responseContainer = "FechaHora")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Perro eliminado"),
			@ApiResponse(code = 204, message = "No existe Perro con ese ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response delete(@PathParam("id") int idPerro) {

		try {
			Perro pElimnar = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			pElimnar = (Perro) this.s.get(Perro.class, idPerro);
			this.s.beginTransaction().commit();
			this.s.close();
			if (pElimnar == null) {
				return Response.noContent().build();
			} else {
				this.s = HibernateUtil.getSession();
				this.s.beginTransaction();
				this.s.delete(pElimnar);
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.ok().entity(new FechaHora()).build();
			}
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/{nombre}/{raza}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Añade un perro", notes = "Crea y persiste un nuevo perro", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Perro Creado con exito"),
			@ApiResponse(code = 409, message = "Perro ya Existente"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response post(@PathParam("nombre") String nombrePerro, @PathParam("raza") String razaPerro) {
		try {

			final Perro pCreado = new Perro(nombrePerro, razaPerro);
			int idpCreado = 0;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			idpCreado = (Integer) this.s.save(pCreado);
			this.s.save(pCreado);
			if (idpCreado != 0) {
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.status(201).entity(pCreado).build();
			} else {
				this.s.beginTransaction().rollback();
				this.s.close();
				return Response.status(409).build();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return Response.serverError().build();
		}
	}

	@PUT
	@Path("/{id}/{nombre}/{raza}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifica un perro", notes = "Modifica un perro ya existente mediante su identificador", response = Perro.class, responseContainer = "Perro")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe perro con ese ID"),
			@ApiResponse(code = 409, message = "Perro existente, no se puede modificar"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response put(@PathParam("id") int idPerro, @PathParam("nombre") String nombrePerro,
			@PathParam("raza") String razaPerro) {
		try {
			Perro pModificar = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			pModificar = (Perro) this.s.get(Perro.class, idPerro);
			this.s.beginTransaction().commit();
			this.s.close();
			if (pModificar == null) {
				return Response.noContent().build();
			} else {
				pModificar.setNombre(nombrePerro);
				pModificar.setRaza(razaPerro);
				this.s = HibernateUtil.getSession();
				this.s.beginTransaction();
				this.s.update(pModificar);
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.ok().entity(pModificar).build();
			}
		} catch (final Exception e) {
			return Response.status(500).build();

		}
	}
}
