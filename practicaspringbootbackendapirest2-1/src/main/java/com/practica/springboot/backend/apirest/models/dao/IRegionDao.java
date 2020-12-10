package com.practica.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.practica.springboot.backend.apirest.models.entity.Region;

public interface IRegionDao  extends JpaRepository<Region, Long> {

}
