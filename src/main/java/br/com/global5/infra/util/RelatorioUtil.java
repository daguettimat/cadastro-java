package br.com.global5.infra.util;

import java.util.Date;
import java.util.HashMap;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import javax.faces.application.FacesMessage;
import javax.faces.context.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

public class RelatorioUtil {
	
	public static final int RELATORIO_PDF 		=	1;
	public static final int RELATORIO_EXCEL 	= 	2;
	public static final int RELATORIO_HTML		= 	3;
	public static final int	RELATORIO_PANILHA_OPEN_OFFICE = 4;
	public static final int	RELATORIO_PNG		= 	5;
	public static final int	RELATORIO_JPG		= 	6;
	
	public StreamedContent geraRelatorio (HashMap parametrosRelatorio, String nomeRelatorioJasper , String nomeRelatorioSaida, int tipoRelatorio) throws UtilException {
		
		StreamedContent arquivoRetorno = null;
		
		try{
			///var/www/anexos_bi/envio/
			Connection conexao = this.getConexao();
			FacesContext contextoFaces = FacesContext.getCurrentInstance();
			ExternalContext contextoExterno = contextoFaces.getExternalContext();
			ServletContext contextoServlet = (ServletContext) contextoExterno.getContext(); 
			
			String caminhoRelatorios = contextoServlet.getRealPath("/relatorios");
			String caminhoRelatoriosGuarda = identifOS();
			String caminhoArquivoJasper = caminhoRelatorios + File.separator + nomeRelatorioJasper + ".jasper";
			String caminhoArquivoRelatorio = caminhoRelatoriosGuarda + nomeRelatorioSaida;
			
			JasperReport relatorioJasper  = (JasperReport) JRLoader.loadObjectFromFile(caminhoArquivoJasper);
			JasperPrint  impressoraJasper = JasperFillManager.fillReport(relatorioJasper,parametrosRelatorio, conexao);
			
			String extensao = null;
			File arquivoGerado = null;
			
			switch (tipoRelatorio) {
			
			case RelatorioUtil.RELATORIO_PDF:
				
				JRPdfExporter pdfExportado = new JRPdfExporter();
				extensao = "pdf";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				pdfExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				pdfExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				pdfExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;

			case RelatorioUtil.RELATORIO_HTML:
				
				JRPdfExporter htmlExportado = new JRPdfExporter();
				extensao = "html";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				htmlExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				htmlExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				htmlExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;			

			case RelatorioUtil.RELATORIO_EXCEL:
				
				JRPdfExporter xlsExportado = new JRPdfExporter();
				extensao = "xls";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				xlsExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				xlsExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				xlsExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;	

			case RelatorioUtil.RELATORIO_PANILHA_OPEN_OFFICE:
				
				JRPdfExporter odsExportado = new JRPdfExporter();
				extensao = "ods";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				odsExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				odsExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				odsExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;				

			case RelatorioUtil.RELATORIO_PNG:
				
				JRPdfExporter pngExportado = new JRPdfExporter();
				extensao = "png";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				pngExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				pngExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				pngExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;				

			case RelatorioUtil.RELATORIO_JPG:
				
				JRPdfExporter jpgExportado = new JRPdfExporter();
				extensao = "jpg";
				arquivoGerado = new java.io.File(caminhoArquivoRelatorio + "." + extensao);
				jpgExportado.setExporterInput(new SimpleExporterInput(impressoraJasper));
				jpgExportado.setExporterOutput(new SimpleOutputStreamExporterOutput(arquivoGerado));
				jpgExportado.exportReport();
				arquivoGerado.deleteOnExit();
				
			break;	
			
			}

			InputStream conteudoRelatorio = new FileInputStream(arquivoGerado);
			arquivoRetorno = new DefaultStreamedContent(conteudoRelatorio, "application/" + extensao, nomeRelatorioSaida + "." + extensao);
			
			conexao.close();
			
			
		}catch (JRException e) {
			throw new UtilException("Não foi possível gerar o relatório.",e);
		}catch (FileNotFoundException e){
			throw new UtilException("Arquivo do relatório não encontrado.",e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arquivoRetorno;
		
	}
	
	private Connection getConexao()throws UtilException{
		
		try{
			
			Context initContext = new InitialContext();
			Context envContext  = (Context) initContext.lookup("java:/");
			DataSource ds = (javax.sql.DataSource) envContext.lookup("localDS");
			return (java.sql.Connection) ds.getConnection();
			
		} catch (NamingException e){
			throw new UtilException("Não foi possível encontrar o nome da conexão do banco." , e);
		} catch (SQLException e){
			throw new UtilException("Ocorreu um erro de SQL.", e);
		}
	}
	
	
	public String identifOS() {

		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String anoAtual = sf.format(new Date());

		String dirXmlRem = "";

		String sysOpe = AppUtils.identificaOS();

		if (sysOpe == "win") {

			dirXmlRem = String.format("C:\\Boletos_\\%4s\\", anoAtual);

			File fileWin = new File(dirXmlRem);

			if (!fileWin.exists()) {
				if (fileWin.mkdir()) {
					new File(dirXmlRem).mkdir();
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Aviso", "Erro na geração do diretório no servidor para armazenar XML!"));
				}
			}

			return dirXmlRem;

		} else if (sysOpe == "nix") {

			dirXmlRem = String.format("/var/www/anexos_bi/envio/");

			File fileNix = new File(dirXmlRem);

			if (!fileNix.exists()) {
				if (fileNix.mkdirs()) {
					System.out.println("Diretorios criado com sucesso! ( " + dirXmlRem + " ) ");

				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Aviso", "Erro na geração do diretório no servidor para armazenar XML!"));
				}
			}

			return dirXmlRem;
		}

		return null;
	}

}
