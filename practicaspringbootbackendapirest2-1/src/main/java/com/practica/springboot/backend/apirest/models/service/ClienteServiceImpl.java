package com.practica.springboot.backend.apirest.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practica.springboot.backend.apirest.models.dao.IClienteDao;
import com.practica.springboot.backend.apirest.models.dao.IFacturaDao;
import com.practica.springboot.backend.apirest.models.dao.IProductoDao;
import com.practica.springboot.backend.apirest.models.entity.Cliente;
import com.practica.springboot.backend.apirest.models.entity.Factura;
import com.practica.springboot.backend.apirest.models.entity.Producto;
import com.practica.springboot.backend.apirest.models.entity.Region;

@Service
public class ClienteServiceImpl implements IClienteService{

	@Autowired
	IClienteDao clienteDao;
	@Autowired
	IFacturaDao facturaDao;
	@Autowired
	IProductoDao productodao;
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		
		return(List<Cliente>) clienteDao.findAll();
	}

	@Override
	@Transactional (readOnly = true)
	public Cliente findAndById(Long id) {
		// TODO Auto-generated method stub
		return this.clienteDao.findById(id).orElse(null);
	}

	@Override
	public Cliente saveCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		return this.clienteDao.save(cliente);
	}

	@Override
	public Cliente updateCliente(Long id, Cliente cliente) {
		// TODO Auto-generated method stub
		return this.clienteDao.save(cliente);
	}

	@Override
	public void deleteCliente(Long id) {
	this.clienteDao.deleteById(id);		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return this.clienteDao.findAll(pageable);
	}

	@Override
	@Transactional (readOnly = true)
	public List<Region> findAllRegiones() {
		
		return clienteDao.findAllRegiones();
	}

	@Override
	@Transactional (readOnly = true)
	public Factura findFacturaById(Long id) {
		
		return this.facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public Factura saveFactura(Factura factura) {
		// TODO Auto-generated method stub
		return this.facturaDao.save(factura);
	}

	@Override
	@Transactional
	public void deleteFacturaById(Long id) {
		this.facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombreContainingIgnoreCase(String termino) {
		// TODO Auto-generated method stub
		return productodao.findByNombreContainingIgnoreCase(termino);
	}
	
}
