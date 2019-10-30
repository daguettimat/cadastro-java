package br.com.global5.manager.bean.autotrac;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.areas.AreaQry;
import br.com.global5.manager.model.autotrac.AutotracSinal;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.service.autotrac.AutotracSinalService;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class AutotracSinalMB implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AutotracSinal autotracSinal;
	private Integer id;
	
	private Usuario usuario;

	private boolean liberarAcessoEmissao = false;
	
	// Variaveis para Dlg
	private Map<String, Object> options;
	
	private String nomeCliente = "";
	private Integer numCodArea = 0;
	
	private String pesqNomeCliente = "";
	private String pesqEmailCliente = "";
	private Date   pesqDtInicio = null;
	private Date   pesqDtFinal  = null;
	private String pesqNomeRequisitante = "";
	private String pesqMCT = "";
	private String pesqPlaca = "";
	private String pesqStsStatus = "";
	
	private Date   dtFinalizacao = null;
	
	private boolean dcDtFinalizacao = false;
	private boolean dcFinalizarOperacao = false;
	
	private List<AutotracSinal> listAutotracSinal;
	
	@Inject
	AutotracSinalService autotracSinalService;
	
	@Inject
	Crud<AutotracSinal> autotracSinalCrud;
	
	@PostConstruct
	public void init() {
	
		clear();
		
		options = new HashMap<String, Object>();
		options.put("draggable", true);
		options.put("modal", true);
		options.put("contentWidth", "98%");
		options.put("contentheight", "90%");
		options.put("resizable", false);
		

	}
	
	public void clear() {
		
		autotracSinal = new AutotracSinal();
		
		id = null;	
		
		usuario = checkUsuario.valid();
		
	}

	public void pesquisarVeiculos(){
		this.getListAutotracSinal();
	}
	
	public void openDlgPesqCliente() {
		options.put("width", "70%");
		options.put("height", "60%");

		RequestContext.getCurrentInstance().openDialog("dlg_pesqCliente", options, null);
		RequestContext.getCurrentInstance().execute("onTop('dlg_pesqCliente')");
	}
	
	public void clienteSelPesq(AreaQry cli) {
		RequestContext.getCurrentInstance().closeDialog(cli);
	}

	public void clienteSelecionado(SelectEvent event) {
		
		AreaQry areaqry = (AreaQry) event.getObject();
		nomeCliente = " - " + areaqry.getRazao();
		
		numCodArea = areaqry.getIdcliente();
		autotracSinal.setAreaCliente(new Area(areaqry.getIdcliente()));
		autotracSinal.setNomeCliente(areaqry.getRazao());
	}
	
	public void insert() {
		try {
			
			limparVariaveis();
			
			clear();
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("../autotrac/add/chamados_autotrac_add.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					" Inserir Autotrac não pode ser carregado. Informe ao suporte técnico. ", null));
		}
	}
	
	public void update(){
		
		if( numCodArea == 0 ) {
			
    		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Creio que você não selecionou o cliente. Faça isso para continuar."));
    		return;
    		
		}
		
		try {
			
			autotracSinal.setAreaCliente(new Area(numCodArea));
			autotracSinal.setDtInclusao(new Date());
			autotracSinal.setUsuInclusao(checkUsuario.valid());
			
			autotracSinalService.crud().save(autotracSinal);
			
			RequestContext context = RequestContext.getCurrentInstance();
			
			context.update("formAddAutotrac");
			
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso",
					"Requisição Autotrac Gravada com sucesso!"));
			
		}catch (Exception e ) {
			e.printStackTrace();
		}
		
		
	}
	
	public void limparVariaveis(){
		this.setNomeCliente("");
		
		this.setPesqDtInicio(null);
		this.setPesqDtFinal(null);
		
		this.setPesqMCT("");
		this.setPesqPlaca("");
		
		this.setPesqNomeCliente("");
		this.setPesqNomeRequisitante("");
		this.setPesqEmailCliente("");
		
		this.setPesqStsStatus("");
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmFiltroPesq");
		context.update("frmListaChamados:tbListRec");
		
	}
	
	public void listarVeiculoAutotrac(){
		
		if ( pesqStsStatus != null ) {
		
		Criteria criteria = autotracSinalService.crud().getSession().createCriteria(AutotracSinal.class);
		
		String nomeCampoPesquisar = "";
		
		// Botao Iniciado ativo
		if ( pesqStsStatus.equals("iniciado")) {
			this.setDcFinalizarOperacao(true);
			criteria.add(Restrictions.isNull("finalVigencia"));
			criteria.add(Restrictions.eq("cancelado", false));
			nomeCampoPesquisar = "dtInclusao";
		}

		// Botao Finalizado
		if ( pesqStsStatus.equals("finalizado")) {
			
			if ( pesqDtInicio == null ) {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esss status!"));
				return;
						
			}

			if ( pesqDtFinal == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esss status!"));
				return;
			}

			this.setDcFinalizarOperacao(false);
			criteria.add(Restrictions.between("finalVigencia", pesqDtInicio, pesqDtFinal));
			criteria.add(Restrictions.eq("cancelado", false));
			nomeCampoPesquisar = "finalVigencia";
		}
		
		// Botao Cancelado
		if ( pesqStsStatus.equals("cancelado")) {
			
			if ( pesqDtInicio == null ) {
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esse status!"));
				return;
						
			}

			if ( pesqDtFinal == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esse status!"));
				return;
			}
			this.setDcFinalizarOperacao(false);
			criteria.add(Restrictions.between("dtFinalizacao", pesqDtInicio, pesqDtFinal));
			criteria.add(Restrictions.eq("cancelado", true));
			nomeCampoPesquisar="dtExclusao";
		}

		// Pesquisa Data
		if (pesqDtInicio == null || pesqDtFinal == null) {

			// com dt inicio vcto e sem dt fim vcto então parametro data
			// apartir da data inicio de vcto
			if (pesqDtInicio != null && pesqDtFinal == null) {

				criteria.add(Restrictions.ge(nomeCampoPesquisar, pesqDtInicio));

			} else
			// sem dt inicio vcto e com dt fim vcto então parametro data
			// até a data final de vcto
			if (pesqDtInicio == null && pesqDtFinal != null) {

				criteria.add(Restrictions.le(nomeCampoPesquisar, pesqDtFinal));

			} else {
			}

		} else {

			// Se as datas Iniciais e Finais não estão vazias
			if (pesqDtFinal != null && pesqDtInicio != null) {
				int comparaDataFiltro = getPesqDtInicio().compareTo(getPesqDtFinal());
				// 0 qdo datas iguais | 1 qdo DtInicio > DtFim | -1 qdo
				// DtFim > DtInicio
				if (comparaDataFiltro == -1 || comparaDataFiltro == 0) {

					//criteria.add(Restrictions.between(nomeCampoPesquisar, pesqDtInicio, pesqDtFinal));

				} else if (comparaDataFiltro == 1) {
					// Data de Final Maior que Data Inicio de vencimento
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção: Em filtro de pesquisa",
									"Data Final é maior que a Data Inicial!"));
				}
			}
		} // Final pesquisa Data
		
		
		// MCT
		if ( !pesqMCT.equals("")){
			criteria.add(Restrictions.ilike("mct", pesqMCT, MatchMode.ANYWHERE));
		}
		
		// Placa
		if ( !pesqPlaca.equals("")){
			criteria.add(Restrictions.ilike("placa", pesqPlaca, MatchMode.ANYWHERE));
		}		
		
		// Nome
		if ( !pesqNomeCliente.equals("")){
			criteria.add(Restrictions.ilike("nomeCliente", pesqNomeCliente, MatchMode.ANYWHERE));
		}	
		
		// Requisitante
		if ( !pesqNomeRequisitante.equals("")){
			criteria.add(Restrictions.ilike("nomeRequisitante", pesqNomeRequisitante, MatchMode.ANYWHERE));
		}	
		
		// Requisitante
		if ( !pesqEmailCliente.equals("")){
			criteria.add(Restrictions.ilike("emailRequisitante", pesqEmailCliente, MatchMode.ANYWHERE));
		}
		
		int result = criteria.list().size();
		
		if ( result > 0 ){
			
			this.setListAutotracSinal(criteria.list());	
			
			RequestContext context = RequestContext.getCurrentInstance();
					
			context.update("frmListaChamados:tbListRec");
			
		} else {
			this.setListAutotracSinal(null);
			
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Nenhum registro encontrado conforme as especificações do filtro." ));
			RequestContext context = RequestContext.getCurrentInstance();
			
			//context.update("frmListaRec:tbListRec");
			//RequestContext context = RequestContext.getCurrentInstance();
			
			context.update("frmListaChamados:tbListRec");
		}
		
		} else {
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmListaChamados:tbListRec");
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione algum tipo de status do Registro!"));
			
		
		}
		
	}
	
	public void abrirMensagem(Integer idChamadoAutotrac) {
		
		findById(idChamadoAutotrac);
		
		if (idChamadoAutotrac != null) {
			
			RequestContext context = RequestContext.getCurrentInstance();

			context.update("frmListaChamados:dlgView");
			
		}
		
	}
	
	public void solicitarCancelamentoDeRequisicaoAutotrac(Integer idChamadoAutotrac){
		
		findById(idChamadoAutotrac);
		
		if (idChamadoAutotrac != null) {
			
			RequestContext context = RequestContext.getCurrentInstance();

			context.update("frmListaChamados:dlgCancel");
			
		}
				
	}
	
	
	public void cancelarRequisicaoAutotrac(){
				
			autotracSinal.setDtFinalizacao(new Date());
			autotracSinal.setCancelado(true);
			autotracSinal.setUsuExclusao(checkUsuario.valid());
			
			autotracSinalService.crud().update(autotracSinal);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Registro foi cancelado com sucesso!"));
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmListaChamados:dlgCancel");
			context.update("frmListaChamados:tbListRec");
		
			listarVeiculoAutotrac();
	}
	
	
	public void solicitarFinalizacaoDeRequisicaoAutotrac(Integer idChamadoAutotrac){
		
		
		try {
			
			findById(idChamadoAutotrac);
			
			FacesContext.getCurrentInstance().getExternalContext().redirect("../autotrac/add/chamados_autotrac_finalizacao.xhtml");

		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					" Inserir Autotrac não pode ser carregado. Informe ao suporte técnico. ", null));
		}
		
//		if (idChamadoAutotrac != null) {
//			
//			RequestContext context = RequestContext.getCurrentInstance();
//
//			context.update("frmListaChamados:dlgFinal");
//			
//		}
				
	}
	
	public void finalizarRequisicaoAutotrac(){

			
			autotracSinal.setUsuExclusao(checkUsuario.valid());
			autotracSinal.setDtFinalizacao(new Date());
			autotracSinalService.crud().update(autotracSinal);
						
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Registro foi finalizado com sucesso!"));

			this.setDcDtFinalizacao(true);
			
			RequestContext context = RequestContext.getCurrentInstance();

			context.update("formFinalAutotrac");
	
	}
	
	public void findById(Integer id) {
		
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}

		autotracSinal = autotracSinalCrud.get(id);

		if (autotracSinal == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}
	}
	
	public void voltarListaVeiculos() {
		try {			
			clear();
			FacesContext.getCurrentInstance().getExternalContext().redirect("../chamados_autotrac_list.xhtml");
		} catch (IOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,"A lista de veículos Autotrac" + getId()
							+ " não pode ser carregada. Informe ao suporte técnico.",null));
		}
	}

	public void dtFinalizacaoRequisicao(AjaxBehaviorEvent event){
		
		setDtFinalizacao(this.autotracSinal.getFinalVigencia());
		
		String dtI = "a";
		String d = dtI;
		
	}
	
	/**
	 * 
	 * Gets e Setters
	 * 
	 */
	
	public AutotracSinal getAutotracSinal() {
		return autotracSinal;
	}

	public void setAutotracSinal(AutotracSinal autotracSinal) {
		this.autotracSinal = autotracSinal;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public AutotracSinalService getAutotracSinalService() {
		return autotracSinalService;
	}

	public void setAutotracSinalService(AutotracSinalService autotracSinalService) {
		this.autotracSinalService = autotracSinalService;
	}

	public boolean isLiberarAcessoEmissao() {
		return liberarAcessoEmissao;
	}

	public void setLiberarAcessoEmissao(boolean liberarAcessoEmissao) {
		this.liberarAcessoEmissao = liberarAcessoEmissao;
	}

	public Map<String, Object> getOptions() {
		return options;
	}

	public void setOptions(Map<String, Object> options) {
		this.options = options;
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public Integer getNumCodArea() {
		return numCodArea;
	}

	public void setNumCodArea(Integer numCodArea) {
		this.numCodArea = numCodArea;
	}

	public List<AutotracSinal> getListAutotracSinal() {		
			return listAutotracSinal;
	}

	public void setListAutotracSinal(List<AutotracSinal> listAutotracSinal) {
		this.listAutotracSinal = listAutotracSinal;
	}

	public String getPesqNomeCliente() {
		return pesqNomeCliente;
	}

	public void setPesqNomeCliente(String pesqNomeCliente) {
		this.pesqNomeCliente = pesqNomeCliente;
	}

	public String getPesqEmailCliente() {
		return pesqEmailCliente;
	}

	public void setPesqEmailCliente(String pesqEmailCliente) {
		this.pesqEmailCliente = pesqEmailCliente;
	}

	public Date getPesqDtInicio() {
		return pesqDtInicio;
	}

	public void setPesqDtInicio(Date pesqDtInicio) {
		this.pesqDtInicio = pesqDtInicio;
	}

	public Date getPesqDtFinal() {
		return pesqDtFinal;
	}

	public void setPesqDtFinal(Date pesqDtFinal) {
		this.pesqDtFinal = pesqDtFinal;
	}

	public String getPesqNomeRequisitante() {
		return pesqNomeRequisitante;
	}

	public void setPesqNomeRequisitante(String pesqNomeRequisitante) {
		this.pesqNomeRequisitante = pesqNomeRequisitante;
	}

	public String getPesqMCT() {
		return pesqMCT;
	}

	public void setPesqMCT(String pesqMCT) {
		this.pesqMCT = pesqMCT;
	}

	public String getPesqPlaca() {
		return pesqPlaca;
	}

	public void setPesqPlaca(String pesqPlaca) {
		this.pesqPlaca = pesqPlaca;
	}

	public String getPesqStsStatus() {
		return pesqStsStatus;
	}

	public void setPesqStsStatus(String pesqStsStatus) {
		this.pesqStsStatus = pesqStsStatus;
	}

	public Date getDtFinalizacao() {
		return dtFinalizacao;
	}

	public void setDtFinalizacao(Date dtFinalizacao) {
		this.dtFinalizacao = dtFinalizacao;
	}

	public boolean isDcDtFinalizacao() {
		return dcDtFinalizacao;
	}

	public void setDcDtFinalizacao(boolean dcDtFinalizacao) {
		this.dcDtFinalizacao = dcDtFinalizacao;
	}

	public boolean isDcFinalizarOperacao() {
		return dcFinalizarOperacao;
	}

	public void setDcFinalizarOperacao(boolean dcFinalizarOperacao) {
		this.dcFinalizarOperacao = dcFinalizarOperacao;
	}
	
	
}
