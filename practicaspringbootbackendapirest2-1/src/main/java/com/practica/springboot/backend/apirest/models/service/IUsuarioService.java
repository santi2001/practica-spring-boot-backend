package com.practica.springboot.backend.apirest.models.service;

import com.practica.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioService  {
	public Usuario  findByNombreUsuario(String nombreUsuario);
}
