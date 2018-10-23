package br.com.global5.infra.scheduler;

/*
 * Created by zielinski on 29/06/17.
 */

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.EnviaEmail;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.analise.acPendencias;
import br.com.global5.manager.model.analise.acVeiculos;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.model.enums.PendenciasStatus;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.analise.PendService;
import br.com.global5.manager.service.analise.VeiculosService;
import br.com.global5.manager.service.areas.AreaService;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.hibernate.Hibernate;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Scheduled(cronExpression = "0 0/1 * * * ?")
public class SchedulerJob implements org.quartz.Job {

    private List<acCadastro> lstFichas;
    private List<acVeiculos> lstVeiculos;

    private acMotorista acMot;
    private Area areaCliente;

    @Inject
    private CadastroService cadastroService;

    @Inject
    private MotoristaService motoristaService;

    @Inject
    private VeiculosService veiculosService;

    @Inject
    private AreaService areaService;

    @Inject
    private PendService pendenciasService;

    private Integer line;

    private String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";


    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            SendMailAprovadas();
            SendMailReprovadas();
            SendMailPendencias();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private String processaLista( acCadastro cadastro ) {
        String tabela = "";
        int line = 1;
        //
        // Dados do Cadastro
        //
//        tabela += "<tr>";
//        tabela += " <td class='tg-yw4l'>" + line++ + "</td>";
//        tabela += " <td class='tg-yw4l'>N&uacute;mero da Ficha</td>";
//        tabela += " <td class='tg-yw4l'>" + cadastro.getId() + "</td>";
//        tabela += "</tr>";

        //
        // Dados do Motorista
        //

        List<acPendencias> pendencias = pendenciasService.crud().eq("cadastro.id", cadastro.getId())
                .eq("pendenciasStatus.id", 205).list();

        for( acPendencias p : pendencias ) {
            if (p != null) {
                Hibernate.initialize(p.getTipoPendencia());
                tabela += "<tr>";
                tabela += " <td class='tg-b7b8'>" + line++ + "</td>";
                tabela += " <td class='tg-b7b8'>" + p.getTipoParte().getDescricao() + "</td>";
                tabela += " <td class='tg-b7b8'>" + p.getTipoPendencia().getDescricao() + "</td>";
                tabela += "</tr>";
                if( ! p.getObservacaoRequisicao().isEmpty() ) {
                    tabela += "<tr>";
                    tabela += " <td class='tg-b7b8' width='10%'>&nbsp;</td>";
                    tabela += " <td class='tg-b7b8' width='20%'>&nbsp;</td>";
                    tabela += " <td class='tg-b7b8' width='70%'>" + p.getObservacaoRequisicao() + "</td>";
                    tabela += "</tr>";
                }


                p.setPendenciasStatus(new PendenciasStatus(206));
                pendenciasService.crud().update(p);
            }
        }

        return tabela;
    }


    private void SendMailPendencias() throws Exception {


        if( cadastroService == null ) {
            return;
        }

        if( pendenciasService.crud().eq("pendenciasStatus.id", 205).count() == 0 ) {
            return;
        }

        try {
            lstFichas = cadastroService.crud().eq("status.id", 6).list();

            String bodyemail;
            String tituloEmail;

            for (acCadastro cadastro: lstFichas) {
                line = 1;

                int pendencias = pendenciasService.crud().eq("cadastro.id", cadastro.getId()).eq("pendenciasStatus.id", 205).list().size();

                if( pendencias == 0) {
                    continue;
                }

                tituloEmail = "Ficha Cadastral " + cadastro.getId() + ", precisa de sua atenção";
                bodyemail = AppUtils.readFile("/opt/gcadastro/html/emailPendencia.html");
                bodyemail = bodyemail.replace("|dados-da-ficha|", processaAprovada(cadastro));
                bodyemail = bodyemail.replace("|tabela-de-dados|", processaLista(cadastro));

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss 'GMT'Z ");
                Date date = new Date();

                bodyemail = bodyemail.replace("|data-email|", dateFormat.format(date));
                bodyemail = bodyemail.replace("|numero-ficha|", cadastro.getId().toString());
                bodyemail = bodyemail.replace("|data-criacao|", dateFormat.format(cadastro.getDtCriacao()));

                areaCliente = areaService.crud().eq("id", cadastro.getFilialArea().getId()).find();

                String[] recipientList = areaCliente.getEmails().split(";");

                List<InternetAddress> emails = new ArrayList<>();

                for (String recipient : recipientList) {
                    if (recipient.matches(regex)) {
                        emails.add(new InternetAddress(recipient.trim()));
                    }
                }

                if( emails.size() == 0 ) {
                    emails.add(new InternetAddress("liberacao@global5.com.br"));
                    emails.add(new InternetAddress("cadastro@global5.com.br"));
                    tituloEmail = "(FCP) Cliente da Ficha " + cadastro.getId() + " nao possue e-mails cadastrados...";

                }

                Address destTo[] = new InternetAddress[emails.size()];

                int counter = 0;
                for( InternetAddress em : emails ) {
                    destTo[counter++] =  em;
                }

                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                        "mail.global5.com.br", "cadastro@global5.com.br",
                        "cadastro@global5.com.br", "!@Cadastro@!");

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }




    private void SendMailAprovadas() throws Exception {


        if( cadastroService == null ) {
            return;
        }

        try {
            lstFichas = cadastroService.crud().eq("status.id", 142).list();

            String bodyemail = "";
            String tituloEmail = "";

            for (acCadastro cadastro: lstFichas) {
                line = 1;

                if( cadastro.getStatus().getId() == 142 ) {
                    tituloEmail = "Consulta Cadastral " + cadastro.getId() + " foi recomendada";
                    bodyemail = AppUtils.readFile("/opt/gcadastro/html/emailAprovado.html");
                    bodyemail = bodyemail.replace("|tabela-de-dados|", processaAprovada(cadastro));
                }

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssZ");
                Date date = new Date();

                bodyemail = bodyemail.replace("|data-email|", dateFormat.format(date));
                bodyemail = bodyemail.replace("|numero-ficha|", cadastro.getId().toString());
                bodyemail = bodyemail.replace("|data-criacao|", dateFormat.format(cadastro.getDtCriacao()));

                areaCliente = areaService.crud().eq("id", cadastro.getFilialArea().getId()).find();


                List<InternetAddress> emails = new ArrayList<>();

                if( areaCliente != null ) {

                    String[] recipientList = areaCliente.getEmails().split(";");

                    for (String recipient : recipientList) {
                        if (recipient.matches(regex)) {
                            emails.add(new InternetAddress(recipient.trim()));
                        }
                    }
                } else {

                    if (emails.size() == 0) {
                        emails.add(new InternetAddress("liberacao@global5.com.br"));
                        emails.add(new InternetAddress("cadastro@global5.com.br"));
                        tituloEmail = "(FCR) Cliente da Ficha " + cadastro.getId() + " nao possue e-mails cadastrados...";

                    }
                }

                areaCliente = areaService.crud().eq("id", cadastro.getClienteArea().getId()).find();
                if( areaCliente != null ) {
                    String[] recipientList = areaCliente.getEmails().split(";");

                    for (String recipient : recipientList) {
                        if (recipient.matches(regex)) {
                            emails.add(new InternetAddress(recipient.trim()));
                        }
                    }
                }

                Address destTo[] = new InternetAddress[emails.size()];

                int counter = 0;
                for( InternetAddress em : emails ) {
                    destTo[counter++] =  em;
                }

                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                            "mail.global5.com.br", "cadastro@global5.com.br",
                            "cadastro@global5.com.br", "!@Cadastro@!");

                //
                // Finaliza Ficha
                //
                cadastro.setStatus(new FichaStatus(8));
                cadastroService.crud().update(cadastro);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void SendMailReprovadas() throws Exception {


        if( cadastroService == null ) {
            return;
        }

        try {

            lstFichas = cadastroService.crud().eq("status.id", 143).list();

            String bodyemail;
            String tituloEmail;

            for (acCadastro cadastro: lstFichas) {
                line = 1;
                tituloEmail = "Consulta Cadastral " + cadastro.getId() + " não pode ser recomendada";
                bodyemail = AppUtils.readFile("/opt/gcadastro/html/emailNegativo.html");

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ssZ");
                Date date = new Date();

                bodyemail = bodyemail.replace("|data-email|", dateFormat.format(date));
                bodyemail = bodyemail.replace("|numero-ficha|", cadastro.getId().toString());
                bodyemail = bodyemail.replace("|data-criacao|", dateFormat.format(cadastro.getDtCriacao()));

                //
                // pegando dados da matriz
                //
                areaCliente = areaService.crud().eq("id", cadastro.getClienteArea().getId()).find();

                String[] recipientList = areaCliente.getEmails().split(";");

                List<InternetAddress> emails = new ArrayList<>();

                for (String recipient : recipientList) {
                    if (recipient.matches(regex)) {
                        emails.add(new InternetAddress(recipient.trim()));
                    }
                }

                //
                // Pegando dados da Filial
                //
                areaCliente = areaService.crud().eq("id", cadastro.getFilialArea().getId()).find();

                recipientList = areaCliente.getEmails().split(";");

                for (String recipient : recipientList) {
                    if (recipient.matches(regex)) {
                        emails.add(new InternetAddress(recipient.trim()));
                    }
                }

                if( emails.size() == 0 ) {
                    emails.add(new InternetAddress("liberacao@global5.com.br"));
                    emails.add(new InternetAddress("cadastro@global5.com.br"));
                    tituloEmail = "(FNR) Cliente da Ficha " + cadastro.getId() + " nao possue e-mails cadastrados...";

                }

                Address destTo[] = new InternetAddress[emails.size()];

                int counter = 0;
                for( InternetAddress em : emails ) {
                    destTo[counter++] =  em;
                }

                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                        "mail.global5.com.br", "cadastro@global5.com.br",
                        "cadastro@global5.com.br", "!@Cadastro@!");

                //
                // Finaliza Ficha
                //
                cadastro.setStatus(new FichaStatus( 9 ));
                cadastroService.crud().update(cadastro);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String processaAprovada( acCadastro cadastro ) {
        String tabela = "";

        //
        // Dados do Cadastro
        //
        tabela += "<tr>";
        tabela += " <td class='tg-yw4l'>" + line++ + "</td>";
        tabela += " <td class='tg-yw4l'>N&uacute;mero da Ficha</td>";
        tabela += " <td class='tg-yw4l'>" + cadastro.getId() + "</td>";
        tabela += "</tr>";

        //
        // Dados do Motorista
        //

        acMot = motoristaService.crud().eq("acCadastro.id", cadastro.getId()).find();

        if( acMot != null ) {
            Hibernate.initialize(acMot.getMotorista());
            tabela += "<tr>";
            tabela += " <td class='tg-b7b8'>" + line++ + "</td>";
            tabela += " <td class='tg-b7b8'>Motorista</td>";
            tabela += " <td class='tg-b7b8'>" + acMot.getMotorista().getDoc1() + " - "
                    + acMot.getMotorista().getNome() + "</td>";
            tabela += "</tr>";
        }

        //
        // Dados dos Veículos
        //

        lstVeiculos = veiculosService.crud().eq("acCadastro.id", cadastro.getId()).list();

        if( lstVeiculos.size() >= 1 ) {
            for( acVeiculos acV : lstVeiculos ) {
                Hibernate.initialize(acV.getVeiculo());
                Hibernate.initialize(acV.getVeiculo().getMarca());
                Hibernate.initialize(acV.getVeiculo().getModelo());

                tabela += "<tr>";
                tabela += " <td class='tg-yw4l'>" + line++ + "</td>";
                tabela += " <td class='tg-yw4l'>" + acV.getTipo().getDescricao() +"</td>";
                tabela += " <td class='tg-yw4l'>" + acV.getVeiculo().getPlaca()  + " - "
                                                  + acV.getVeiculo().getMarca().getNome().trim() + " - "
                                                  + acV.getVeiculo().getModelo().getNome().trim() + "</td>";
                tabela += "</tr>";
            }
        }

        //
        // Numero da Liberacao
        //
        if( cadastro.getAcLiberacao() != null ) {
            tabela += "<tr>";
            tabela += " <td class='tg-b7b8'>" + line++ + "</td>";
            tabela += " <td class='tg-b7b8'>No. Libera&ccedil;&atilde;o</td>";
            tabela += " <td class='tg-b7b8'>" + cadastro.getAcLiberacao().getId() + "</td>";
            tabela += "</tr>";
        }



        return tabela;
    }



}