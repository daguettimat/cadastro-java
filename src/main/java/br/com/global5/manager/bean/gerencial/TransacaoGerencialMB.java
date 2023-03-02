package br.com.global5.manager.bean.gerencial;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.apache.poi.hssf.usermodel.HSSFAutoFilter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.export.ExcelOptions;
import org.primefaces.context.RequestContext;

import br.com.global5.manager.model.gerencial.TransacaoGerencial;
import br.com.global5.manager.service.gerencial.TransacaoGerencialService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class TransacaoGerencialMB implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private TransacaoGerencial tns;
	
	private Integer id;
	
	private List<TransacaoGerencial> listaTransacao;
	
	private Date dataInicial = null;
	private Date dataFinal = null;
	private String nomeModulo = "";
	private String detalheDaChamada = "";
	private String moduloWsImportado = "";
	private String statusChamadas = "finalizada";

	// Variaveis teste Excel
	private ExcelOptions excelOpt;
	
	
	@Inject
	private TransacaoGerencialService tnsService;
	
	public void init(){
		clear();
	}

	private void clear() {
		
		tns = new TransacaoGerencial();
		id = null;
		
	}
	
	public void findById(Integer id){
		
		if ( id == null ) {
			throw new BusinessException("O id é obrigatório");
		}
		
		tns = tnsService.crud().get(id);
		
		if ( tns == null ) {
			throw new BusinessException("O registro não foi encontrado pelo id: " + id);
		}
		
	}
	
	public void pesquisarTransacao(){
		
		Criteria criteria = tnsService.crud().getSession().createCriteria(TransacaoGerencial.class);
		criteria.addOrder(Order.desc("dtInicio"));
		
		// Entre Datas 
		if ( dataInicial == null &&  dataFinal == null ) {

						
		} else {

			// com dataInicial e sem dataFinal, então critério é a partir da dataInicial até a corrente data 
			if ( dataInicial != null && dataFinal == null) {
				criteria.add(Restrictions.ge("dtInicio", dataInicial));
			} else
				if ( dataInicial == null && dataFinal != null ) {
				criteria.add(Restrictions.le("dtInicio", dataFinal));
			}
			
			// Se ha dataInicial e dataFinal
			if ( dataInicial != null && dataFinal != null ) {
				
				int comparaDataInicialFinal = getDataInicial().compareTo(getDataFinal());
				
				// 0 qdo datas iguais | 1 qdo dataInicial > dataFinal | -1 qdo dataInicial < dataFinal
				if ( comparaDataInicialFinal == -1 || comparaDataInicialFinal == 0 ) {
					criteria.add(Restrictions.ge("dtInicio", dataInicial));
					criteria.add(Restrictions.le("dtInicio", dataFinal));
				} else
					if ( comparaDataInicialFinal == 1) {
						FacesContext.getCurrentInstance().addMessage(null, 
								new FacesMessage(FacesMessage.SEVERITY_ERROR,"Atenção: Em filtro de pesquisa", 
										"Data Inicial é maior que a Data Final!"));
						return;
				}				
				
			}
			
		}
		
		// Módulos ( todos ou conforme lista)
		if( nomeModulo.toString().length() > 5) {
			criteria.add(Restrictions.ilike("modulo", nomeModulo.toString()));
		}
		
		// Transação ( todas, finalizadas e canceladas )	
		// Transação finalizada
		if ( statusChamadas.equals("finalizada" )) {
			// com data informada no campo dtFim
			criteria.add(Restrictions.isNotNull("dtFim"));
		}
		// Transação Cancelada
		if ( statusChamadas.equals("finalizadaErro")) {
			// com data informada no campo dtCancelamento
			criteria.add(Restrictions.isNull("dtFim"));
		}			
		// Transação Cancelada
		if ( statusChamadas.equals("cancelada")) {
			// com data informada no campo dtCancelamento
			criteria.add(Restrictions.isNotNull("dtCancelamento"));
		}	
		
		
		int resultTns = criteria.list().size();
		
		if ( resultTns > 0 ) {
			
			listaTransacao = criteria.list();
			
			String a = "bb";
			String c = a;
			
		} else {
			listaTransacao = null;
			FacesContext.getCurrentInstance().addMessage(null, 
						new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso", "Sem dados para ser apresentado!"));
		}
		
		
	}
	
	public void limparFiltro(){
		
		this.setDataInicial(null);
		this.setDataFinal(null);
		this.setNomeModulo("");
		this.setStatusChamadas("todos");
		this.setListaTransacao(null);
		
		try {
			
			init();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso", "Filtro limpo para nova pesquisa."));
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmListaRegistrosWS:tbListAutotrac");
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("../trafegus/chamada_ws_chp_executadas_lst.xhtml");
			
		} catch (IOException e){
            
			FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção","Não foi possível aplicar o filtro. Entre em contrato com o suporte!"));
            
        }
		
	}
	
	public void gerarBaseParaDownload(Integer idTransacaoGerencial){

		if( idTransacaoGerencial != null ) {
			
			//tnsService
			
		}
	}
	
	public void customizacaoExcel(Object document){
		excelOpt = new ExcelOptions();
		
		HSSFWorkbook wb = (HSSFWorkbook) document;
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow header = sheet.getRow(0);		
		HSSFAutoFilter filter = sheet.setAutoFilter(CellRangeAddress.valueOf("A1:I1"));
		
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.AUTOMATIC.getIndex());
		//cellStyle.setFillPattern(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND.BIG_SPOTS);
		
		for(int i=0; i < header.getPhysicalNumberOfCells(); i++){
			HSSFCell cell = header.getCell(i);
			cell.setCellStyle(cellStyle);
		}
		
		//HSSFAutoFilter filter = sheet.setAutoFilter(null);
		
	}
	
	public TransacaoGerencial getTns() {
		return tns;
	}

	public void setTns(TransacaoGerencial tns) {
		this.tns = tns;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDataInicial() {
		return dataInicial;
	}

	public void setDataInicial(Date dataInicial) {
		this.dataInicial = dataInicial;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(String nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public String getDetalheDaChamada() {
		return detalheDaChamada;
	}

	public void setDetalheDaChamada(String detalheDaChamada) {
		this.detalheDaChamada = detalheDaChamada;
	}

	
	
	public String getModuloWsImportado() {
		return moduloWsImportado;
	}

	public void setModuloWsImportado(String moduloWsImportado) {
		this.moduloWsImportado = moduloWsImportado;
	}

	public String getStatusChamadas() {
		return statusChamadas;
	}

	public void setStatusChamadas(String statusChamadas) {
		this.statusChamadas = statusChamadas;
	}

	public List<TransacaoGerencial> getListaTransacao() {
		return listaTransacao;
	}

	public void setListaTransacao(List<TransacaoGerencial> listaTransacao) {
		this.listaTransacao = listaTransacao;
	}



}
