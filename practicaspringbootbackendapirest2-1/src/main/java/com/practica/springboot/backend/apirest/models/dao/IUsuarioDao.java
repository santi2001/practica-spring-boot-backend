package com.practica.springboot.backend.apirest.models.dao;

/*import org.springframework.data.jpa.repository.Query;
*/
import org.springframework.data.repository.CrudRepository;

import com.practica.springboot.backend.apirest.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {

	public Usuario  findByNombreUsuario(String nombreUsuario);
	/*
	@Query(   "select u"
			+ "from Usuario u"
			+ "where u.nombreUsuario=?1")
	public Usuario  findByNombreUsuario1(String nombreUsuario);
*/
}
