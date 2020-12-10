package com.practica.springboot.backend.apirest.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.practica.springboot.backend.apirest.models.entity.Cliente;
import com.practica.springboot.backend.apirest.models.entity.Factura;
import com.practica.springboot.backend.apirest.models.entity.Producto;
import com.practica.springboot.backend.apirest.models.entity.Region;

public interface IClienteService {

	public List<Cliente> findAll();
	public Page<Cliente> findAll(Pageable pageable);
	public Cliente findAndById(Long id);
	public Cliente saveCliente (Cliente cliente);
	public Cliente updateCliente (Long id,Cliente cliente);
	public void deleteCliente (Long id);
	public List<Region> findAllRegiones();
	public Factura findFacturaById(Long id);
	public Factura saveFactura(Factura factura);
	public void deleteFacturaById(Long id);
	public List<Producto> findByNombreContainingIgnoreCase(String termino);
}
