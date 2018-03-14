package com.tcc.qbeacon.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.qbeacon.config.JwtEvaluator;
import com.tcc.qbeacon.datas.AuthToken;
import com.tcc.qbeacon.datas.UsuarioData;
import com.tcc.qbeacon.exceptions.TokenException;
import com.tcc.qbeacon.model.Papel;
import com.tcc.qbeacon.model.Turma;
import com.tcc.qbeacon.model.Usuario;
import com.tcc.qbeacon.service.UsuarioService;
import com.tcc.qbeacon.util.Constants;
import com.tcc.qbeacon.util.MensagemRetorno;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	JwtEvaluator jwtEvaluator;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping
	public MensagemRetorno cadastrar(@RequestBody @Valid Usuario usuario) throws Exception{
		try {
			usuario.setPapel(Papel.ALUNO);
			usuarioService.salvarUsuario(usuario);
			return new MensagemRetorno(Constants.SUCESSO_CADASTRO_USUARIO);
		} catch (Exception e) {
			throw new Exception(Constants.ERRO_CADASTRO_USUARIO);
		}
	}
	
	@GetMapping
	public ResponseEntity<UsuarioData> buscar() throws TokenException{
		Usuario usuarioLogado = jwtEvaluator.usuarioToken();
		
		List<Integer> turmas = new ArrayList<Integer>();
		
		if(usuarioLogado.getTurmas() != null) {			
			for (Turma turma : usuarioLogado.getTurmas()) {
				turmas.add(turma.getId());
			}
		}
			
		UsuarioData usuario = new UsuarioData(
			usuarioLogado.getId(), 
			usuarioLogado.getNome(),
			usuarioLogado.getEmail(),
			usuarioLogado.getSenha(),
			turmas
		);
		
		return new ResponseEntity<UsuarioData>(usuario, HttpStatus.OK);
	}
	
	@PostMapping(path="/validartoken")
	public MensagemRetorno token(@RequestBody AuthToken authToken) throws TokenException {
		String token = authToken.getToken();
		if(token != null) {
			String email = null;
			try {
				email = Jwts.parser()
							.setSigningKey(Constants.CHAVE_SECRETA)
							.parseClaimsJws(token.replace(Constants.TOKEN_PREFIX, ""))
							.getBody()
							.getSubject();
			}catch (Exception e) {
				throw new TokenException(Constants.TOKEN_INVALIDO);
			}if(usuarioService.getUsuario(email) != null) {
				return new MensagemRetorno(Constants.TOKEN_VALIDO);
			}
			return new MensagemRetorno(Constants.ERRO_EMAIL_SENHA);
		}
		throw new TokenException(Constants.TOKEN_INVALIDO);
	}
	
}
