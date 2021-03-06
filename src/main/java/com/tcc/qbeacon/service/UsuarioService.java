package com.tcc.qbeacon.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public UsuarioService() {
		bCryptPasswordEncoder = new BCryptPasswordEncoder();
	}
	
	public Usuario atualizaUsuario(Usuario usuario){
		return usuarioRepository.save(usuario);
	}
	
	public Usuario salvarUsuario(Usuario usuario){
		usuario.setSenha(bCryptPasswordEncoder.encode(usuario.getSenha()));
		return usuarioRepository.save(usuario);
	}
	
	public boolean logar(String email, String senha){
		Usuario userBanco = usuarioRepository.findByEmail(email);
		if(userBanco != null && new BCryptPasswordEncoder().matches(senha, userBanco.getSenha())) return true;
		else return false;
	}
	
	public boolean logar(Usuario usuario){
		Usuario userBanco = usuarioRepository.findByEmail(usuario.getEmail());
		if(userBanco != null && new BCryptPasswordEncoder().matches(usuario.getSenha(), userBanco.getSenha())) return true;
		else return false;
	}
	
	public boolean compararSenha(String senhaCrip, String senhaLimpa){
		if(new BCryptPasswordEncoder().matches(senhaLimpa, senhaCrip)) return true;
		else return false;
	}
	
	public List<Usuario> getTodosUsuarios(){
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuario(String email){
		return usuarioRepository.findByEmail(email);
	}
	
	public Usuario getUsuario(Integer id){
		return usuarioRepository.findOne(id);
	}
	
	public Usuario getUsuarioLogado(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		Usuario usuarioLogado = this.getUsuario(email);
		return usuarioLogado;
	}

}
