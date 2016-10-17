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
import com.ipartek.formacion.ejemplos.perrera.pojo.Empleado;
import com.ipartek.formacion.ejemplos.perrera.pojo.FechaHora;

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
@Path("/empleado")
@Api(value = "/empleado")
public class EmpleadoController {
	private static final String MensajeOrden = "Orden ascente o descendente";
	private static final String MensajeNombre = "Ordena por el valor, nombre";
	private Session s;

	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Listado de Empleado", notes = "Listado de empleados, limitado a 1.000", response = Empleado.class, responseContainer = "List")
	// @ApiParam(value = Mensaje, name = "order", required = false)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getAll(
			@ApiParam(value = MensajeOrden, name = "order", required = false) @DefaultValue("asc") @QueryParam("order") String order,
			@ApiParam(value = MensajeNombre, name = "value", required = false) @DefaultValue("id") @QueryParam("value") String value) {
		try {
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();

			ArrayList<Empleado> empleados = null;

			// controlar QueryParam order y value
			if (order != null && value != null) {
				if (order.equals("asc") && (value.equals("nombre") || value.equals("id"))) {
					empleados = (ArrayList<Empleado>) this.s.createCriteria(Empleado.class).addOrder(Order.asc(value))
							.list();

				} else if (order.equals("desc") && (value.equals("nombre") || value.equals("id"))) {
					empleados = (ArrayList<Empleado>) this.s.createCriteria(Empleado.class).addOrder(Order.desc(value))
							.list();
				} else {
					empleados = (ArrayList<Empleado>) this.s.createCriteria(Empleado.class).list();
				}
			} else {
				empleados = (ArrayList<Empleado>) this.s.createCriteria(Empleado.class).list();
			}

			this.s.beginTransaction().commit();
			this.s.close();

			return Response.ok().entity(empleados).build();
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Busca un empleado por su ID", notes = "devuelve un empleado mediante el paso de su ID", response = Empleado.class, responseContainer = "Empleado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe empleado con esa ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response getById(@PathParam("id") int idEmpleado) {

		try {
			Empleado empleado = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			empleado = (Empleado) this.s.get(Empleado.class, idEmpleado);
			this.s.beginTransaction().commit();
			this.s.close();
			if (empleado == null) {
				return Response.noContent().build();
			}
			return Response.ok().entity(empleado).build();
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Elimina un empleado", notes = "Elimina un empleado mediante el paso de su ID", response = Empleado.class, responseContainer = "FechaHora")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Empleado eliminado"),
			@ApiResponse(code = 204, message = "No existe Empleado con ese ID"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response delete(@PathParam("id") int idEmpleado) {

		try {
			Empleado eElimnar = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			eElimnar = (Empleado) this.s.get(Empleado.class, idEmpleado);
			this.s.beginTransaction().commit();
			this.s.close();
			if (eElimnar == null) {
				return Response.noContent().build();
			} else {
				this.s = HibernateUtil.getSession();
				this.s.beginTransaction();
				this.s.delete(eElimnar);
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.ok().entity(new FechaHora()).build();
			}
		} catch (final Exception e) {
			return Response.serverError().build();
		}
	}

	@POST
	@Path("/{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Añade un empleado", notes = "Crea y persiste un nuevo empleado", response = Empleado.class, responseContainer = "Empleado")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Empleado Creado con exito"),
			@ApiResponse(code = 409, message = "Empleado ya Existente"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response post(@PathParam("nombre") String nombreEmpleado) {
		try {

			final Empleado eCreado = new Empleado(nombreEmpleado);
			int ideCreado = 0;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			ideCreado = (Integer) this.s.save(eCreado);
			this.s.save(eCreado);
			if (ideCreado != 0) {
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.status(201).entity(eCreado).build();
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
	@Path("/{id}/{nombre}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Modifica un empleado", notes = "Modifica un empleado ya existente mediante su identificador", response = Empleado.class, responseContainer = "Empleado")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Todo OK"),
			@ApiResponse(code = 204, message = "No existe empleado con ese ID"),
			@ApiResponse(code = 409, message = "Empleado existente, no se puede modificar"),
			@ApiResponse(code = 500, message = "Error inexperado en el servidor") })
	public Response put(@PathParam("id") int idEmpleado, @PathParam("nombre") String nombreEmpleado) {
		try {
			Empleado eModificar = null;
			this.s = HibernateUtil.getSession();
			this.s.beginTransaction();
			eModificar = (Empleado) this.s.get(Empleado.class, idEmpleado);
			this.s.beginTransaction().commit();
			this.s.close();
			if (eModificar == null) {
				return Response.noContent().build();
			} else {
				eModificar.setNombre(nombreEmpleado);
				this.s = HibernateUtil.getSession();
				this.s.beginTransaction();
				this.s.update(eModificar);
				this.s.beginTransaction().commit();
				this.s.close();
				return Response.ok().entity(eModificar).build();
			}
		} catch (final Exception e) {
			return Response.status(500).build();

		}
	}
}
