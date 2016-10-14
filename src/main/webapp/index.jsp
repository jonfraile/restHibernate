<%@page import="com.ipartek.formacion.ejemplos.perrera.Constantes"%>

<!DOCTYPE html>
<html lang="es">
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Perrera Mobile</title>


<link rel="stylesheet" href="js/jqm/jquery.mobile-1.4.5.min.css">
<script src="js/jqm/jquery.js"></script>
<script src="js/jqm/jquery.mobile-1.4.5.min.js"></script>

</head>
<body>


<!-- page HOME -->
	<div data-role="page" id="home">
		<div data-role="header">
			<h2>Perrera</h2>
		</div>
		<div data-role="navbar">
			<ul>
				<li>
					<a href="#crear" class="ui-btn ui-shadow ui-corner-all ui-btn-icon-left ui-icon-plus">Crear nuevo perro</a>
				</li>
				<li>
					<a href="#eliminar" class="ui-btn ui-shadow ui-corner-all ui-btn-icon-left ui-icon-delete">Eliminar perro</a>
				</li>
			</ul>
		</div>
		<div data-role="main">
			<div id="mensaje" data-theme="b"></div>		
			<ul id="perros_home" data-role="listview" data-inset="true" data-filter="true">
				<li>Sin cargar perros</li>
			</ul>
		</div>
		<div data-role="footer">
			
			<a target="_blank" href="/can/api.jsp">
				<img src="/images/logo_small.png">
				Documentacion Api
			</a>
		</div>
	</div>


<!-- page MODIFICAR -->
	<div data-role="page" id="modificar">	
		<div data-role="header">
			<a href="#home" class="ui-btn ui-shadow ui-corner-all ui-btn-icon-left ui-icon-home">Home</a>
			<h2>Modificar-Detalle</h2>
		</div>
		<div data-role="main">		
			<input type="hidden" name="id_modificar" id="id_modificar">		
			<label for="nombre_modificar">Nombre:</label> 
			<input type="text" name="nombre_modificar" id="nombre_modificar" value=""> 			
			<label for="raza_modificar">Raza:</label> 
			<input type="text" name="raza_modificar" id="raza_modificar" value=""> 			
			<a href="#" id="botonModificar" data-role="button" data-theme="e" 
			   class="ui-btn ui-shadow ui-corner-all ui-btn-icon-bottom ui-icon-edit"
			   onclick="modificarPerro();">
				Modificar 
			</a>
		</div>
	</div>	
	
	
<!-- page CREAR -->
	<div data-role="page" id="crear">
		<div data-role="header">
			<a href="#home" class="ui-btn ui-shadow ui-corner-all ui-btn-icon-left ui-icon-home">Home</a>
			<h2>Crear Perro</h2>
		</div>
		<div data-role="main">
			<label for="nombre_crear">Nombre:</label> 
			<input type="text" name="nombre_crear" id="nombre_crear" value=""> 			
			<label for="raza_crear">Raza:</label> 
			<input type="text" name="raza_crear" id="raza_crear" value=""> 
			<a href="#" id="botonCrear" data-role="button" data-theme="e"
			   class="ui-btn ui-shadow ui-corner-all ui-btn-icon-bottom ui-icon-plus"
			   onclick="crearPerro();">
				Crear 
			</a>
		</div>
	</div>
	
	
<!-- page ELIMINAR -->
	<div data-role="page" id="eliminar">
		<div data-role="header">
			<a href="#home" class="ui-btn ui-shadow ui-corner-all ui-btn-icon-left ui-icon-home">Home</a>
			<h2>Eliminar Perro</h2>
		</div>
		<div data-role="main">
			<div class="ui-field-contain">
				<label for="select-perro">Perro a eliminar:</label> 
				<select name="select-perro" id="select-perro">
					<option value="0">Sin cargar perros</option>
				</select>
			</div>
			<a href="#" id="botonEliminar" data-role="button" data-theme="e" 
			   class="ui-btn ui-shadow ui-corner-all ui-btn-icon-bottom ui-icon-delete"
			   onclick="eliminarPerro();">
				Eliminar
			</a>
		</div>
	</div>


	<script>
		/* Antes de carga la HOME, llamar al servicio y rellenar lista */
		var servicio = "/service/";
		var perros = [];
		var perro_selec;

		$(document).on("pagebeforeshow", "#home", function() {
			perros = [];
			$.get(servicio + "perro", function(data) {
				var output = '';
				$.each(data, function(index, value) {
					output += '<li><a href="#">' + value.nombre + '</a></li>';
					perros.push(value);
				});
				$('#perros_home').html(output).listview("refresh");
			});
		});
		
		/* Detectar click en listado perros HOME */
		$('#perros_home').on('vclick', 'li', function(event){
			perro_selec = perros[ $(this).index() ];
			$.mobile.navigate("#modificar");
		});
		
		$(document).on("pagebeforeshow", "#modificar", function(){
			//rellenar dormulario con "perro_seleccionado"
			$("#id_modificar").val(perro_selec.id);
			$("#nombre_modificar").val(perro_selec.nombre);
			$("#raza_modificar").val(perro_selec.raza);
		});
		
		function modificarPerro(){
			var id = $('#id_modificar').val();
			var nombre = $('#nombre_modificar').val();
			var raza = $('#raza_modificar').val();
			
			$.ajax({
			    type: "PUT",
			    url: servicio + 'perro/' + id + '/' + nombre + '/' + raza,
			    success: function(data) {
			    	//called when successful
			    	alert('ok');
			    	//$('#mensaje').html('Perro '+ id + ' modificado.');
			    	$.mobile.navigate("#home");
			      },
			      error: function(e) {
			    	//called when there is an error
			    	alert('fail');
			    	$.mobile.navigate("#home");
			      }
			});
		};
		
		function crearPerro(){
			var nombre = $('#nombre_crear').val();
			var raza = $('#raza_crear').val();			
			$.ajax({
			    type: "POST",
			    url: servicio + 'perro/' + nombre + '/' + raza,
			    success: function(data) {
			    	//called when successful
			    	alert('ok');
			    	$.mobile.navigate("#home");
			      },
			      error: function(e) {
			    	//called when there is an error
			    	alert('fail');
			    	$.mobile.navigate("#home");
			      }
			});
		};
		
		$(document).on("pagebeforeshow", "#eliminar", function() {
			$.ajax({
			    type: "GET",
			    url: servicio + 'perro',
			    success: function(data) {
			    	var output = '';
			    	$.each(data, function(index, value){
			    		output += '<option value="' + value.id + '">' + value.nombre + '</option>';
			    	});
			    	$('#select-perro').html(output).selectmenu('refresh', true);			    				    	
			      },
			      error: function(e) {			    	
			    	alert('fail');			    	
			      }
			});
		});
		
		function eliminarPerro(){
			var id = $('#select-perro option:selected').val();						
			$.ajax({
			    type: "DELETE",
			    url: servicio + 'perro/' + id,
			    success: function(data) {
			    	//called when successful
			    	alert('ok');
			    	$.mobile.navigate("#home");
			      },
			      error: function(e) {
			    	//called when there is an error
			    	alert('fail');
			    	$.mobile.navigate("#home");
			      }
			});
		};
		
		
			
	</script>


</body>
</html>
