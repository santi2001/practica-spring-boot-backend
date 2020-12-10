package com.practica.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.practica.springboot.backend.apirest.models.entity.Usuario;
import com.practica.springboot.backend.apirest.models.service.IUsuarioService;

@SuppressWarnings("deprecation")
@Component
public class InfoAdicionalTocken implements TokenEnhancer{

	@Autowired
	IUsuarioService usuarioService;
	
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		Usuario usuario = usuarioService.findByNombreUsuario(authentication.getName());
		
		Map<String, Object> infoMap  = new HashMap<>();
		infoMap.put("info adicional", "esso"+ authentication.getName());
		infoMap.put("nombreUsuario", usuario.getNombreUsuario());// agrega informacion al tocken y concatena el nombre
		infoMap.put("id", usuario.getId());
		infoMap.put("habilitado", usuario.getHabilitado());
		infoMap.put("email", usuario.getEmail());
		((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(infoMap);
		return accessToken;
	}

}
