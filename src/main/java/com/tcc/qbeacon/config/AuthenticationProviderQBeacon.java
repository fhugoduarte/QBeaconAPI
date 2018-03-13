package com.tcc.qbeacon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.UsuarioService;
import com.tcc.qbeacon.util.Constants;

@Component
public class AuthenticationProviderQBeacon implements AuthenticationProvider{

	@Autowired
	UsuarioService usuarioService;
	
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String email = auth.getName();
		String senha = auth.getCredentials().toString();

		if (usuarioService.logar(email, senha)) {
			Usuario userBanco = usuarioService.getUsuario(email);
			return new UsernamePasswordAuthenticationToken(userBanco, senha, userBanco.getAuthorities());
		}
		throw new UsernameNotFoundException(Constants.ERRO_EMAIL_SENHA);
	}

	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
