package com.practica.springboot.backend.apirest.models.service;

import java.util.List;

import com.practica.springboot.backend.apirest.models.entity.Region;

public interface IRegionService {
	public List<Region> findAll();
	public Region saveCliente (Region region);
}
