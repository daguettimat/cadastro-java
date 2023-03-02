package br.com.global5.infra.scheduler;

/**
 * Created by zielinski on 29/06/17.
 */

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.EnviaEmail;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.cadastro.FichaRenovacao;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.RenovacaoStatus;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.cadastro.FichaRenovacaoService;
import br.com.global5.manager.service.cadastro.VeiculoService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.logging.Log;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.hibernate.Hibernate;
import org.hibernate.search.util.impl.HibernateSearchResourceLoader;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

// seconds minutes hours day-of-month month day-of-week year

//@Scheduled(cronExpression = "0 0/5 0 * * ?")
//@Scheduled(cronExpression = "5 * * * * ?")
@Scheduled(cronExpression = "0 0 1-3 * * ?")
public class JobRenovacao implements org.quartz.Job {

    @Inject
    private FichaRenovacaoService renovacaoService;

    @Inject
    private br.com.global5.manager.service.geral.MotoristaService motoristaService;

    @Inject
    private VeiculoService veiculoService;

    @Inject
    private AreaService areaService;

    private List<FichaRenovacao> lstFichas;
    private List<FichaRenovacao.lVeiculo> lstVeiculos;
    private Area areaCliente;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            System.out.println("Job Renovação");
            SendMailRenovacao();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SendMailRenovacao() throws Exception {

        if( renovacaoService == null ) {
            return;
        }

        try {

            lstFichas = renovacaoService.crud().eq("status.id", 213).list();
            
			System.out.println("EMAILRENOVACAO inicio de busca no banco");
            System.out.println("EMAILRENOVACAO " + lstFichas.size() + " a notificar");
            
            System.out.println("**********************************************************");
            System.out.println(" ");
            System.out.println(" JOB RENOVACAO CADASTRAL / " + new Date() ) ;
            System.out.println(" Fichas Listadas : " + lstFichas.size());
            System.out.println(" ");
            System.out.println("**********************************************************");

            String bodyemail = "";
            String tituloEmail = "";
            

            String listaEmail = "";
            
            for (FichaRenovacao renovacao: lstFichas) {

                tituloEmail = "Renovação " + renovacao.getId() + ", precisa de sua atenção";
                bodyemail = AppUtils.readFile("/opt/gcadastro/html/emailRenovacao.html");
                bodyemail = bodyemail.replace("|tabela-de-dados|", processaLista(renovacao));

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                bodyemail = bodyemail.replace("|data-email|", dateFormat.format(date));
                bodyemail = bodyemail.replace("|numero-ficha|", renovacao.getId().toString());
                bodyemail = bodyemail.replace("|data-criacao|", dateFormat.format(renovacao.getDtRegistro()));

                areaCliente = areaService.crud().eq("id", renovacao.getFilialArea().getId()).find();

                String[] recipientList = areaCliente.getEmails().split(";");

                List<InternetAddress> emails = new ArrayList<InternetAddress>();

                for (String recipient : recipientList) {
                    if (recipient.matches(AppUtils.regex)) {
                        emails.add(new InternetAddress(recipient.trim()));
                    }
                }

                if( emails.size() == 0 ) {
                    emails.add(new InternetAddress("cadastro@global5.com.br"));
                    tituloEmail = "(FCR) Ficha de Renovação " + renovacao.getId() + " não possue e-mails cadastrados...";

                }

                Address destTo[] = new InternetAddress[emails.size()];

                int counter = 0;
                for( InternetAddress em : emails ) {
                    destTo[counter++] =  em;
                }
                
                listaEmail = listaEmail + emails.toString();
                
                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                        "mail.global5.com.br", "cadastro@global5.com.br",
                        "cadastro@global5.com.br", "iKzC%M%IY0q1");
             
                // Antiga senha: !@Cadastro@!
                /*
                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                        "mail.global5.com.br", "cadastro@global5.com.br",
                        "cadastro@global5.com.br", "!@Cadastro@!");
				*/
                renovacao.setStatus(new RenovacaoStatus(214));
                renovacaoService.crud().update(renovacao);

                System.out.println("EMAILRENOVACAO notificando ficha " + renovacao.getId() + " , email: " + emails.toString());
                
            }
            
            System.out.println("EMAILRENOVACAO Fim notificação emails");
            System.out.println("EMAILRENOVACAO Fim processo");
            
            String teste = listaEmail;
            String a = teste;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String processaLista(FichaRenovacao renovacao) {
        String tabela = "";
        int line = 0;

        //
        // Dados do Cadastro
        //
        tabela += "<tr>";
        tabela += " <td class='tg-yw4l'>" + line++ + "</td>";
        tabela += " <td class='tg-yw4l'>N&uacute;mero da Renova&ccedil;&atilde;o</td>";
        tabela += " <td class='tg-yw4l'>" + renovacao.getId() + "</td>";
        tabela += "</tr>";

        //
        // Dados do Motorista
        //

        Hibernate.initialize(renovacao.getMotorista());

        if( renovacao.getMotorista() != null ) {
            tabela += "<tr>";
            tabela += " <td class='tg-b7b8'>" + line++ + "</td>";
            tabela += " <td class='tg-b7b8'>Motorista</td>";
            tabela += " <td class='tg-b7b8'>" + renovacao.getMotorista().getDoc1() + " - "
                    + renovacao.getMotorista().getNome() + "</td>";
            tabela += "</tr>";
        }

        //
        // Dados dos Veículos
        //

        FichaRenovacao.lVeiculo[] veiculos = new Gson().fromJson(renovacao.getVeiculos().toString(), FichaRenovacao.lVeiculo[].class);
        for(FichaRenovacao.lVeiculo vv : veiculos) {
            Veiculo veiculo = veiculoService.crud().get(vv.getVeioid());
            tabela += "<tr>";
            tabela += " <td class='tg-yw4l'>" + line++ + "</td>";
            tabela += " <td class='tg-yw4l'>" + veiculo.getPlaca() +"</td>";
            tabela += " <td class='tg-yw4l'>" + veiculo.getRenavam()  + " - "
                    + veiculo.getMarca().getNome().trim() + " - "
                    + veiculo.getModelo().getNome().trim() + "</td>";
            tabela += "</tr>";

        }

        return tabela;
    }

}