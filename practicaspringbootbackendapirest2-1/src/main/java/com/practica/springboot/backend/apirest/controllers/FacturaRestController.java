package com.practica.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.practica.springboot.backend.apirest.models.entity.Factura;
import com.practica.springboot.backend.apirest.models.entity.Producto;
import com.practica.springboot.backend.apirest.models.service.IClienteService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
@RequestMapping("/api")
public class FacturaRestController {

	@Autowired
	private IClienteService clienteService;
	
	@Secured({"ROLE_USER","ROLE_ADMIN"})
	@GetMapping("/facturas/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public Factura showFactura(@PathVariable Long id)
	{
		return clienteService.findFacturaById(id);
	}
	@Secured("ROLE_ADMIN")
	@DeleteMapping ("/facturas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteFactura(@PathVariable Long id)
	{
		clienteService.deleteFacturaById(id);
	}
	@Secured("ROLE_ADMIN")
	@GetMapping("/facturas/filtrar-productos/{term}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<Producto> filtrarProducto(@PathVariable String term)
	{
		return clienteService.findByNombreContainingIgnoreCase(term);
	}
	@Secured("ROLE_ADMIN")
	@PostMapping("/facturas")
	@ResponseStatus(HttpStatus.CREATED)
	public Factura crearFactura(@RequestBody Factura factura)
	{
		return clienteService.saveFactura(factura);
	}
}
