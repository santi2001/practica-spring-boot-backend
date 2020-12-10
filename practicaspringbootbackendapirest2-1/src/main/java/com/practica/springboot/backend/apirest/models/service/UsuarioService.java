package com.practica.springboot.backend.apirest.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.practica.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.practica.springboot.backend.apirest.models.entity.Usuario;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	
	private final Logger logg= org.slf4j.LoggerFactory.getLogger(UsuarioService.class);
	@Autowired
	private IUsuarioDao IUsuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = IUsuarioDao.findByNombreUsuario(username);
		if(usuario ==null)
		{
			logg.error("error en el login: no existe el usuario '"+ username+"' en el sistema !!!");
			throw new  UsernameNotFoundException("error en el login: no existe el usuario '"+ username+"' en el sistema !!!");
		}
		List<GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(roles -> new SimpleGrantedAuthority(roles.getNombre())).peek(authority-> logg.info("role:"+authority.getAuthority())).collect(Collectors.toList());
		return new User(usuario.getNombreUsuario(), usuario.getPassword(), usuario.getHabilitado(), true, true, true,authorities);
	}

	
	@Override
	@Transactional(readOnly = true)
	public Usuario findByNombreUsuario(String nombreUsuario) {
		// TODO Auto-generated method stub
		return IUsuarioDao.findByNombreUsuario(nombreUsuario);
	}
	
}
