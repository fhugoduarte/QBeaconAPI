package com.tcc.qbeacon.util;

public class Constants {
	
	public static final long TOKEN_EXPIRAR_MINUTOS = 24 * 60;
	
	public static final String HEADER_STRING = "Authorization";
	public static final String TOKEN_PREFIX = "QBeacon";
	public static final String CHAVE_SECRETA = "secretkeyqbeacon";
	
	public static final String URI_MQTT = "tcp://localhost:1883";
	public static final String TOPICO_MQTT_API = "qbeacon";
	public static final String TOPICO_MQTT_ARDUINO = "inTopic";
	
	/**
	 *  Contants de retornos da API
	 */
	public static final String ERRO_EMAIL_SENHA = "Senha e/ou Email incorretos!";
	public static final String SUCESSO_LOGIN_USUARIO = "Usuário logado com sucesso!";
	
	public static final String SUCESSO_PRESENCA_USUARIO = "Presença realizada com sucesso!";
	public static final String ERRO_PRESENCA_USUARIO = "Não foi possível realizar a presença desse aluno!";
	
	public static final String SUCESSO_CADASTRO_USUARIO = "Usuário cadastrado com Sucesso!";
	public static final String SUCESSO_ATUALIZAR_USUARIO = "Usuário atualizado com Sucesso!";
	public static final String SUCESSO_EXCLUIR_USUARIO = "Usuário excluído com Sucesso!";
	public static final String ERRO_CADASTRO_USUARIO = "Verifique sua conexão ou o Email informado !";
	
	public static final String TOKEN_VALIDO = "Token válido";
	public static final String TOKEN_INVALIDO = "Token inválido";
	
}
