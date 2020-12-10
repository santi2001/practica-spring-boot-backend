package com.practica.springboot.backend.apirest.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practica.springboot.backend.apirest.models.dao.IRegionDao;
import com.practica.springboot.backend.apirest.models.entity.Region;

@Service
public class RegionServiceImpl implements IRegionService {
	@Autowired
	IRegionDao regionDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Region> findAll() {
		
		return (List<Region>) regionDao.findAll();
	}

	@Override
	public Region saveCliente(Region region) {
		// TODO Auto-generated method stub
		return regionDao.save(region);
	}



}
