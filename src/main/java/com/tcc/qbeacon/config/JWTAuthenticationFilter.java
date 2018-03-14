package com.tcc.qbeacon.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcc.qbeacon.exceptions.TokenException;
import com.tcc.qbeacon.util.Constants;
import com.tcc.qbeacon.util.MensagemRetorno;


public class JWTAuthenticationFilter extends GenericFilterBean {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		Authentication authentication;
		try {
			authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest)request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (TokenException e) {
			ObjectMapper mapper = new ObjectMapper();
			String json =  mapper.writeValueAsString(new MensagemRetorno(Constants.TOKEN_INVALIDO));
			HttpServletResponse res = (HttpServletResponse) response;
			res.reset();
			res.setHeader("Content-Type", "application/json;charset=UTF-8");
			res.setStatus(400);
			res.getWriter().write(json);
		}
		
		/*Authentication authentication = null;
		
		try {
			authentication = TokenAuthenticationService.getAuthentication((HttpServletRequest) request);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.err.println("TENTADO");
			filterChain.doFilter(request, response);
			System.err.println("TENTADO2");
		} catch (Exception e) {
			System.err.println("TENTADO3");
			ObjectMapper mapper = new ObjectMapper();
			String json =  mapper.writeValueAsString(new MensagemRetorno(Constants.TOKEN_INVALIDO));
			HttpServletResponse res = (HttpServletResponse) response;
			res.reset();
			res.setHeader("Content-Type", "application/json;charset=UTF-8");
			res.setStatus(400);
			res.getWriter().write(json);
		}*/
		
	}
	
	
	
}
