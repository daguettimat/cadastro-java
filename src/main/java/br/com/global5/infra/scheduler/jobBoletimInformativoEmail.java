package br.com.global5.infra.scheduler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.logging.Log;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.EnviaEmail;

@Scheduled(cronExpression = "0/40 * * * * ?")
public class jobBoletimInformativoEmail implements org.quartz.Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			//sendEmailBoletim();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void sendEmailBoletim() throws Exception{
		
		  System.out.println("**********************************************************");
          System.out.println(" ");
          System.out.println(" JOB Envio de email / Boletim Informativo / " + new Date() ) ;
          System.out.println(" Boletim No : " ) ;
          System.out.println(" ");
          System.out.println("**********************************************************");

          String bodyEmail = "";
          String tituloEmail = "";
          
          tituloEmail = "Boletim Informativo - Teste";
          
          try {
        	  
			bodyEmail = AppUtils.readFile("/opt/gcadastro/html/emailBoletimInformativo.html");
			
			List<InternetAddress> emails = new ArrayList<>();
			
			emails.add(new InternetAddress("francis.bueno@global5.com.br"));
			
			
			Address destTo[] = new InternetAddress[emails.size()];
			
			
			 int counter = 0;
             for( InternetAddress em : emails ) {
                 destTo[counter++] =  em;
             }
			
			new EnviaEmail().enviaEmail(bodyEmail, tituloEmail, "cadastro@global5.com.br", destTo, 
					 "mail.global5.com.br", "cadastro@global5.com.br",
                     "cadastro@global5.com.br", "!@Cadastro@!");
          
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
