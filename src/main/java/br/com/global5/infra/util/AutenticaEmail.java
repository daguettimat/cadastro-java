package br.com.global5.infra.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class AutenticaEmail extends Authenticator{

	private String usuario;
	private String senha;

	public AutenticaEmail(String usuario, String senha){
		this.usuario = usuario;
		this.senha = senha;
	}
	
	public PasswordAuthentication getPasswordAuthentication(){
		return new PasswordAuthentication(this.usuario, this.senha);
	}
	
}
