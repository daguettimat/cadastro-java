package br.com.global5.infra.scheduler;

/**
 * Created by zielinski on 29/06/17.
 */

import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.EnviaEmail;
import br.com.global5.manager.model.cadastro.FichaEmail;
import br.com.global5.manager.model.cadastro.FichaRenovacao;
import br.com.global5.manager.model.cadastro.Veiculo;
import br.com.global5.manager.model.enums.RenovacaoStatus;
import br.com.global5.manager.service.cadastro.FichaEmailService;
import br.com.global5.template.config.AdminConfig;
import com.google.gson.Gson;
import org.apache.deltaspike.scheduler.api.Scheduled;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.Hibernate;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Scheduled(cronExpression = "0 0 0/12 * * ?")
public class JobPlanilha implements org.quartz.Job {

    @Inject
    private FichaEmailService fichaService;

    @Inject
    private AdminConfig adminConfig;

    private List<FichaEmail> lstFichas;

    @Override
    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        try {
            System.out.println("Job Renovação");
            SendMailClientes();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void SendMailClientes() throws Exception {

        FileInputStream fileInput = null;
        BufferedInputStream bufferInput = null;
        POIFSFileSystem poiFileSystem = null;
        FileOutputStream fileOut = null;
        String fname = adminConfig.getComercialXlsDir() + adminConfig.getComercialXlsCliente();

        if( fichaService == null ) {
            return;
        }

        try {

            lstFichas = fichaService.crud().eq("dtEmail", null).list();

            System.out.println("**********************************************************");
            System.out.println(" ");
            System.out.println(" JOB PLANILHA CADASTRO / " + new Date() ) ;
            System.out.println(" Fichas Listadas : " + lstFichas.size());
            System.out.println(" ");
            System.out.println("**********************************************************");

            String bodyemail = "";
            String tituloEmail = "";

            for (FichaEmail fichaEmail: lstFichas) {

                tituloEmail = "Controle Cadastral";
                bodyemail = AppUtils.readFile(adminConfig.getQuartzHtmlPlanilha());

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();

                bodyemail = bodyemail.replace("|data-email|", dateFormat.format(date));
                bodyemail = bodyemail.replace("|data-criacao|", dateFormat.format(fichaEmail.getDtCriacao()));


                String[] recipientList = fichaEmail.getEmail().split(";");

                List<InternetAddress> emails = new ArrayList<InternetAddress>();

                for (String recipient : recipientList) {
                    if (recipient.matches(AppUtils.regex)) {
                        emails.add(new InternetAddress(recipient.trim()));
                    }
                }

                if( emails.size() == 0 ) {
                    emails.add(new InternetAddress("cadastro@global5.com.br"));
                    tituloEmail = "(PPD) Planilha de Preenchimento de Dados " + fichaEmail.getId() + " não possue e-mails cadastrados...";

                }

                Address destTo[] = new InternetAddress[emails.size()];

                int counter = 0;
                for( InternetAddress em : emails ) {
                    destTo[counter++] =  em;
                }


                try {

                    fileInput = new FileInputStream(fname);
                    bufferInput = new BufferedInputStream(fileInput);
                    poiFileSystem = new POIFSFileSystem(bufferInput);

                    Biff8EncryptionKey.setCurrentUserPassword("03n2vqv7g80h74");
                    HSSFWorkbook workbook = new HSSFWorkbook(poiFileSystem, true);
                    HSSFSheet sheet = workbook.getSheetAt(0);

                    HSSFRow row = sheet.createRow(0);
                    Cell cell = row.createCell(0);

                    cell.setCellValue(fichaEmail.getDocumento());

                    fileOut = new FileOutputStream(fname);
                    workbook.writeProtectWorkbook(Biff8EncryptionKey.getCurrentUserPassword(), "");
                    workbook.write(fileOut);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                } finally {
                    try {
                        bufferInput.close();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                    try {
                        fileOut.close();
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }



                new EnviaEmail().enviaEmail(bodyemail, tituloEmail, "cadastro@global5.com.br", destTo,
                        "mail.global5.com.br", "cadastro@global5.com.br",
                        "cadastro@global5.com.br", "!@Cadastro@!", adminConfig.getComercialXlsDir(), adminConfig.getComercialXlsCliente());

                fichaEmail.setDtEmail(new Date());
                fichaService.crud().update(fichaEmail);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}