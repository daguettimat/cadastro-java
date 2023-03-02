package br.com.global5.infra.util;

import br.com.global5.template.config.AdminConfig;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("unused")
public class EnviaEmail implements Serializable {

    @Inject
    private AdminConfig adminConfig;

	/*
	 
	  Como Chamar :
	  
	  		Address para = new  InternetAddress("comercial@zsoftware.com.br");
			//String arr[] = {"comercial@zsoftware.com.br"};
			Address alis[] = {para};
			
			new EnviaEmail().enviaEmail("<h1>teste</h1>", "assunto Teste", "comercial@zsoftware.com.br",alis , alis, alis,
					"mail.zsoftware.com.br", "html", "comercial@zsoftware.com.br", "comercial", "");
	  
	  */	
	
	public void enviaEmail(String bodyEmail, String assunto ,
			String remetente, Address destTo[], String host, String mail, String user, 
			String passwd, String ... optional) throws Exception {

		Properties mailServerProperties;
		Session getMailSession;
		MimeMessage generateMailMessage;

		if( ! AppUtils.enviarEmail ) {
			return;
		}

		try {
			mailServerProperties = System.getProperties();
			mailServerProperties.put("mail.smtp.port", "587");
			mailServerProperties.put("mail.smtp.auth", "true");
			mailServerProperties.put("mail.smtp.starttls.enable", "true");
			mailServerProperties.put("mail.smtp.host", "mail.global5.com.br");
			mailServerProperties.put("mail.smtp.ssl.trust", "mail.global5.com.br");

			getMailSession = Session.getDefaultInstance(mailServerProperties, null);
			generateMailMessage = new MimeMessage(getMailSession);

//			// Step2

//			List<InternetAddress> emails = new ArrayList<InternetAddress>();
//
//
//			emails.add(new InternetAddress("jose.zielinski@global5.com.br"));
//			emails.add(new InternetAddress("jrzielinski@gmail.com"));
//
//			Address dest[] = new InternetAddress[emails.size()];
//
//			int counter = 0;
//			for (InternetAddress em : emails) {
//				dest[counter++] = em;
//			}
//			generateMailMessage.addRecipients(Message.RecipientType.TO, dest);

			generateMailMessage.setRecipients(Message.RecipientType.TO,destTo);		
			generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("liberacao@global5.com.br"));
			/* Retirei aqui 23/06/2020
			generateMailMessage.setRecipients(Message.RecipientType.TO,destTo);
			generateMailMessage.addRecipient(Message.RecipientType.CC, new InternetAddress("liberacao@global5.com.br"));
			*/
			/* Retirei aqui 23/06/2020	
			generateMailMessage.setFrom(new InternetAddress(remetente));
			generateMailMessage.setFrom(new InternetAddress("francis.bueno@global5.com.br"));
			*/
			generateMailMessage.setFrom(new InternetAddress(remetente));			
			generateMailMessage.setSubject(assunto);
			generateMailMessage.setContent(bodyEmail, "text/html; charset=utf-8");

			// Step3
			Transport transport = getMailSession.getTransport("smtp");

			// "!@Cadastro@!"
			int port = 587;
			transport.connect(host, port, user, passwd);
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
		}
		catch (MessagingException e) {
			e.printStackTrace();			
			throw new Exception("Nao foi possível enviar o e-mail.\n  \n" +
					"ERRO: "+e.getMessage()); 
		}
	}


}