package br.com.global5.manager.bean.historico;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;

import br.com.global5.infra.Crud;
import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.chamado.Chamado;
import br.com.global5.manager.model.analise.acCadastro;
import br.com.global5.manager.model.analise.acMotorista;
import br.com.global5.manager.model.cadastro.HistoricoNegativo;
import br.com.global5.manager.model.cadastro.Pessoa;
import br.com.global5.manager.model.enums.DocumentoTipo;
import br.com.global5.manager.model.enums.FichaStatus;
import br.com.global5.manager.service.analise.CadastroService;
import br.com.global5.manager.service.analise.MotoristaService;
import br.com.global5.manager.service.cadastro.HistoricoNegativoService;
import br.com.global5.manager.service.cadastro.PessoaService;
import br.com.global5.template.exception.BusinessException;


@Named
@ViewAccessScoped
public class HistoricoNegativoMB implements Serializable{

	private static final long serialVersionUID = 202027080942L;
	
	private HistoricoNegativo his;
	private Integer id;
	
	private Integer tipoRegistro = null;
	private boolean mostrarTipoRegistroM = false;
	private boolean mostrarTipoRegistroV = false;
	private boolean disBtnMotoristaVeiculo = false;
	private boolean disBtnSalvarMotoristaVeiculo = false;
	private boolean dcFinalizarOperacao = false;
	private boolean invalidarBtnAcao = false;
	private boolean liberarAcessoGestores = false;
	private Integer valQtdCaracteres = null;
	private List<HistoricoNegativo> lstHistNegativo;
	private String  txtHeaderDtTable = " Ativos";
	
	
	private Date   pesqDtInicio = null;
	private Date   pesqDtFinal  = null;
	private String pesqMotorista = "";
	private String pesqCPF = "";
	private String pesqPlaca = "";
	private String pesqStsStatus = "";
	
	@Inject
	private HistoricoNegativoService histNegativoService;
	
	@Inject
	private MotoristaService acMotService;
	
	@Inject
	private CadastroService cadService;

	@Inject
	private PessoaService pesService;
	
	@Inject
	Crud<HistoricoNegativo> hisNegativoCrud;
	
	@PostConstruct
	public void init(){
		clear();		
	}

	private void clear() {
		
		his = new HistoricoNegativo();
		//his.setDocumentoTipo(new DocumentoTipo());
		
		lstHistNegativo = histNegativoService.crud().isNull("dtExclusao").list();
		
	}

	public void btnHistorico(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("../historico/historico_negativo_list.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Histórico Negativo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }	
	}
	
	public void insert(){
        try {
        	checarTipoRegistro();
        	clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../historico/add/historico_negativo_add.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inclusão do Histórico Negativo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }	
	}
	
	public void insertNew(){
        try {
			checarTipoRegistro();
        	clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../add/historico_negativo_add.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Inclusão do Histórico Negativo " + getId()
                            + " não pode ser carregado. Informe ao suporte técnico.", null));
        }	
	}
	
	public void checarTipoRegistro(){
		RequestContext context = RequestContext.getCurrentInstance();
		limparCampos();
		
		if (tipoRegistro == null) {
			mostrarTipoRegistroM = false;
			mostrarTipoRegistroV = false;
		} 
		
		else
		
		if (tipoRegistro == 1){
			mostrarTipoRegistroM = true;
			mostrarTipoRegistroV = false;
			disBtnMotoristaVeiculo = true;
			context.update("formAddHistorico");
		} else if(tipoRegistro == 2) {
			mostrarTipoRegistroV = true;
			mostrarTipoRegistroM = false;
			disBtnMotoristaVeiculo = true;
			context.update("formAddHistorico");
		} 
	}
	
	public void validarCpf(String cpf){
		String valCpf = cpf.replace(".","");
		
		boolean result = AppUtils.isInteger(valCpf);
		
		valQtdCaracteres = valCpf.length();
		
		if ( result == true) {
			if ( valQtdCaracteres == 11) {
				// 
				String b = "a";
				String c = b;
			} else {
				// Mensagem de recusa do CPF
	            FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_WARN, "CPF informado não é válido. Por favor redigite.", null));
	            return;
			}		
			
		} else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "CPF informado não é válido. Por favor redigite.", null));
            return;
		}
		

	}
	
	private void limparCampos(){
		disBtnMotoristaVeiculo = false;	// habilita botao Motorista e Veiculo
		disBtnSalvarMotoristaVeiculo = false; // habilita bota Salvar
		his.setDocumento(null);
		his.setNome("");
		his.setObservacao("");
		his.setPlaca("");
		his.setDocumentoTipo(null);
		this.setValQtdCaracteres(null);
	}
	
	public void atualizar(){
		
		if(his.getId() == null){			
			// Salvando o registro
			his.setDtCriacao(new Date());

			if(valQtdCaracteres != null){				
				his.setDocumentoTipo(new DocumentoTipo(10));
			} 
			
			// Checar se documento ou placa já estão cadastrados no historico negativo
			Criteria criHistorico = histNegativoService.crud().getSession().createCriteria(HistoricoNegativo.class);			
			criHistorico.add(Restrictions.isNull("dtExclusao"));
			
			if( valQtdCaracteres != null) {
				// Pesquisa Documento
				
				criHistorico.add(Restrictions.eq("documento" , his.getDocumento()));
				
				//HistoricoNegativo hisNeg = hisNegativoCrud.eq("documento", his.getDocumento()).isNull("dtExclusao").find();
				
				if(criHistorico != null){
					
					int result = criHistorico.list().size();
					int a = result;
					
					if(result > 0) {
				      FacesContext.getCurrentInstance().addMessage(
			                    null,
			                    new FacesMessage(FacesMessage.SEVERITY_WARN, 
			                    		"Atenção","O cpf informado já encontra registrado no histórico negativo. Não será registrado!"));
				      return;
					}
				}
				
			} else {
				// Pesquisa Placa
				
				criHistorico.add(Restrictions.eq("placa" , his.getPlaca()));
				
				//HistoricoNegativo hisNeg = hisNegativoCrud.eq("placa", his.getPlaca()).isNull("dtExclusao").find();
				
				if(criHistorico != null){
					int result = criHistorico.list().size();
					int a = result;
					
					if(result > 0) {
				      FacesContext.getCurrentInstance().addMessage(
			                    null,
			                    new FacesMessage(FacesMessage.SEVERITY_WARN, 
			                    		"Atenção","A placa informada já encontra registrado no histórico negativo.  Não será registrado!"));
				      return;
					}
				}
				
			}
			
			// Registrar Histórico
			
			his.setUsuCriacao(checkUsuario.valid());
			
			histNegativoService.crud().save(his);
			
			tipoRegistro = null; // limpa a variavel responsavel por checar os tipos selecionados
			
			disBtnSalvarMotoristaVeiculo = true; // Desabilita o botão Salvar
			
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Adicionado" , "O registro de histórico foi adicionado com sucesso!."));
			
			// Buscando fichas na analise de cadastro para atualização
				// MOTORISTA
            	// -- CPF existe no cadastro de motoristas?
	            // ----- Faço a consulta na tabela (analise_cadastral_motorista acMotorista pesquisa Motorista.documento M
	            // ----- motorista.anacm_anacoid.status  Motorista.anacm_id)  se existir então faço a atualização na tabela acCadastro...
            	if(valQtdCaracteres != null) {
            		
            	List<acCadastro> lstCadMot = cadService.listAcCadastroMot(his.getDocumento()); 
            		
            		if ( lstCadMot != null){
            			
            		
            		int resultAcCadMotorista = lstCadMot.size();
            		
            		if ( resultAcCadMotorista > 0) {
            			// Faça isso
            			for(Integer i = 0; i < resultAcCadMotorista ; i++){

            				acCadastro acCad = (acCadastro) cadService.findById(lstCadMot.get(i)) ;
            				
            				acCad.getId();
            				acCad.getAnaliseEspecialDetalhe();
            				
            				acCad.setAnaliseEspecialStatus(37);  // Negativo red-cross.png
            				
            				cadService.crud().update(acCad);
            				
            			}
            			
       			     FacesContext.getCurrentInstance().addMessage(
 		                    null,
 		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Foram atualizados algumas análises onde seu status estava diferentes de 7,8 e 9!" ));

            		}
            		
            	} else {
            		
   			     FacesContext.getCurrentInstance().addMessage(
		                    null,
		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Nenhuma análises foi encontrada para ser atualizada!" ));             		
            	}
            		
            	} 
            	
            // VEICULO
            	// Adicionar as placas
            	if(valQtdCaracteres == null) {	
            	
            		List<acCadastro> lstPlaca = cadService.listAcCadastroVei(his.getPlaca()); 
            		
            		if(lstPlaca != null){
            			
                		int resultAcCadPlaca = lstPlaca.size();
                		
                		if ( resultAcCadPlaca > 0) {

                			for(Integer i = 0; i < resultAcCadPlaca ; i++){
                				
                				acCadastro acCad = (acCadastro) cadService.findById(lstPlaca.get(i)) ;
                				
                				acCad.getId();
                				//acCad.getAnaliseEspecialDetalhe();
                				
                				acCad.setAnaliseEspecialStatus(37);  // Negativo red-cross.png
                				
                				cadService.crud().update(acCad);
                				
                			}

             			     FacesContext.getCurrentInstance().addMessage(
          		                    null,
          		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Foram atualizados algumas análises onde seu status estava diferentes de 7,8 e 9!" ));            			
                			
                		}	
            			            			
            			
            		} else {
        			     FacesContext.getCurrentInstance().addMessage(
       		                    null,
       		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Nenhuma análises foi encontrada para ser atualizada!" ));            			
            			            			
            		}

            
            	}	
            	
            
		} else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção", "O registro de histórico não é permitido atualização."));
            return;
		}
	}
	
    public void btnBack() {
        try {
        	clear();
        	limparCampos();
            FacesContext.getCurrentInstance().getExternalContext().redirect("../historico_negativo_list.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Lista de histórico negativo não pode ser carregada. Informe ao suporte técnico.", null));
        }
    }
    
	public void abrirMensagem(Integer idHistorico) {
		
		findById(idHistorico);
		
		if (idHistorico != null) {
			
			RequestContext context = RequestContext.getCurrentInstance();

			context.update("frmListaHistoricos:dlgView");
			
		}
			
	}
	
	public void solicitarCancelamentoDeHistorico(Integer idHistorico){
		
		findById(idHistorico);
		if(his.getDocumento() != null){
		  valQtdCaracteres = his.getDocumento().length();
		} else {
			valQtdCaracteres = null;
		}
		if (idHistorico != null) {
			
			RequestContext context = RequestContext.getCurrentInstance();
			
			context.update("frmListaHistoricos:dlgCancel");
			
		}
				
	}
	
	public void findById(Integer id) {
		
		if (id == null) {
			throw new BusinessException("O id é obrigatório");
		}
		
		his = hisNegativoCrud.get(id);

		if (his == null) {
			throw new BusinessException("Registro não foi encontrado pelo id: " + id);
		}
	}
	
	
	public void cancelarHistoricoNegativo(){
		
		if(his.getId() != null){
			
			his.setDtExclusao(new Date());
			his.setUsuExclusao(checkUsuario.valid());
			
			histNegativoService.crud().update(his);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção", "Registro foi cancelado com sucesso!"));
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmListaHistoricos:dlgCancel");
			context.update("frmListaHistoricos:tbListRec");
			
			if(valQtdCaracteres != null) {
			// Atualizando a ficha de analise por Motoristas
			List<acCadastro> lstCadMot = cadService.listAcCadastroMot(his.getDocumento());
			
				if ( lstCadMot != null){
									
					Integer resultAcCadMotorista = lstCadMot.size();
					
					if ( resultAcCadMotorista >0 ) {
						
						// Então Remova as restrições de histórico negativo
						for(int i = 0 ; i < resultAcCadMotorista ; i++){
							
							acCadastro acCad = (acCadastro) cadService.findById(lstCadMot.get(i));
							
							acCad.getId();
							acCad.setAnaliseEspecialStatus(35); // Positivo green-ok.png
							cadService.crud().update(acCad);
							
						}
						
					     FacesContext.getCurrentInstance().addMessage(
		 		                    null,
	 		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Foram atualizados algumas análises deixando as liberadas do Histórico Negativo!" ));
						
					}
					
				} else {
					// Final if lstCadMot
				     FacesContext.getCurrentInstance().addMessage(
	 		                    null,
		                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção","Não foram encontrados nenhuma ficha para ser atualizados para esse motorista!" ));
					
				}
				
			 } 
				 
			 if (valQtdCaracteres == null){
				 // Atualizando a ficha de analise por Placas
					 List<acCadastro> lstPlaca = cadService.listAcCadastroVei(his.getPlaca()); 
	                 
					 if (lstPlaca != null) {
					 
	            		int resultAcCadPlaca = lstPlaca.size();
	            		
	            		if ( resultAcCadPlaca > 0) {

	            			for(Integer i = 0; i < resultAcCadPlaca ; i++){
	            				
	            				acCadastro acCad = (acCadastro) cadService.findById(lstPlaca.get(i)) ;
	            				
	            				acCad.getId();
	            				
	            				acCad.setAnaliseEspecialStatus(35);  // Positivo green-ok.png
	            				
	            				cadService.crud().update(acCad);
	            				
	            			}

	         			     FacesContext.getCurrentInstance().addMessage(
	      		                    null,
	      		                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Foram atualizados algumas análises deixando as liberadas do Histórico Negativo!" ));            			
	            			
	            		}	
					
					}else {
					     FacesContext.getCurrentInstance().addMessage(
		 		                    null,
			                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção","Não foram encontrados nenhuma ficha para ser atualizados para essa placa!" ));
					}
					 
			}
				 
		} else {
			
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,"Atenção", "O registro de cancelamento do histórico negativo não foi alterado."));
            return;            
		}
		
		// Comentado
			//listarVeiculoAutotrac();
		lstHistNegativo = null; 
				//histNegativoService.crud().isNull("dtExclusao").list();
		
	}
	
	
	public void listarHistorico(){
		// parte da lista de historico
		if(pesqStsStatus != null){
			
			Criteria criteria = histNegativoService.crud().getSession().createCriteria(HistoricoNegativo.class);
			
			String nomeCampoPesquisar = "";
			
			if(pesqStsStatus.equals("iniciado")){
				
				criteria.add(Restrictions.isNull("dtExclusao"));
				nomeCampoPesquisar = "dtCriacao";
				invalidarBtnAcao = true;
			}
			
			if(pesqStsStatus.equals("cancelado")){
				
				invalidarBtnAcao = false;
				
				if ( pesqDtInicio == null ) {
					
					this.setLstHistNegativo(null); 
					RequestContext context = RequestContext.getCurrentInstance();
					context.update("frmListaHistoricos:tbListRec");
					
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esse status!"));
					return;
							
				}

				if ( pesqDtFinal == null){
					this.setLstHistNegativo(null); 
					RequestContext context = RequestContext.getCurrentInstance();					
					context.update("frmListaHistoricos:tbListRec");
					
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção", "Selecione as data de inicio e final para esse status!"));
					return;
				}
				
				criteria.add(Restrictions.isNotNull("dtExclusao"));
				nomeCampoPesquisar = "dtExclusao";
			}
			
			if (pesqDtInicio == null || pesqDtFinal == null) {
				
				// com dt inicio vcto e sem dt fim vcto então parametro data
				// apartir da data inicio de vcto
				if (pesqDtInicio != null && pesqDtFinal == null) {

					criteria.add(Restrictions.ge(nomeCampoPesquisar, pesqDtInicio));
					criteria.addOrder(Order.asc("dtCriacao"));

				} else
				// sem dt inicio vcto e com dt fim vcto então parametro data
				// até a data final de vcto
				if (pesqDtInicio == null && pesqDtFinal != null) {

					criteria.add(Restrictions.le(nomeCampoPesquisar, pesqDtFinal));
					criteria.addOrder(Order.asc("dtCriacao"));

				}
				
			} else {

				// Se as datas Iniciais e Finais não estão vazias
				if (pesqDtFinal != null && pesqDtInicio != null) {
					int comparaDataFiltro = getPesqDtInicio().compareTo(getPesqDtFinal());
					// 0 qdo datas iguais | 1 qdo DtInicio > DtFim | -1 qdo
					// DtFim > DtInicio
					if (comparaDataFiltro == -1 || comparaDataFiltro == 0) {
						
						criteria.add(Restrictions.between("dtCriacao", pesqDtInicio, pesqDtFinal));
						criteria.addOrder(Order.asc("dtCriacao"));
					} else if (comparaDataFiltro == 1) {
						// Data de Final Maior que Data Inicio de vencimento
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "Atenção: Em filtro de pesquisa",
										"Data Final é maior que a Data Inicial!"));
					}
				}
			} // Final pesquisa Data
			
			// CPF
			if ( !pesqCPF.equals("")){
				criteria.add(Restrictions.ilike("documento", pesqCPF, MatchMode.ANYWHERE));
			}
			// Placa
			if ( !pesqPlaca.equals("")){
				criteria.add(Restrictions.ilike("placa", pesqPlaca, MatchMode.ANYWHERE));
			}		
			// Motorista
			if ( !pesqMotorista.equals("")){
				criteria.add(Restrictions.ilike("nome", pesqMotorista, MatchMode.ANYWHERE));
			}
			
			if (criteria != null){
				
				
				
				int result = criteria.list().size();
				
				if(result > 0){
					
					this.setLstHistNegativo(criteria.list()); 
									
				} else {
					this.setLstHistNegativo(null); 					
				}
 
				RequestContext context = RequestContext.getCurrentInstance();
				
				context.update("frmListaHistoricos:tbListRec");

				
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
								"Não há nada em nosso banco de dados com os filtros escolhidos!"));
				return;
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
							"Escolha um status Iniciado ou Cancelado"));
			return;
			
		}
	}
	
	public void limparVariaveis(){
		
		this.setPesqDtInicio(null);
		this.setPesqDtFinal(null);
		
		this.setPesqCPF("");
		this.setPesqPlaca("");
		
		this.setPesqMotorista("");		
		
		this.setPesqStsStatus("");
		
		this.setLstHistNegativo(null); 	
		
		invalidarBtnAcao = false;
		
		RequestContext context = RequestContext.getCurrentInstance();
		context.update("frmFiltroPesq");
		context.update("frmListaHistoricos:tbListRec");
		
	}
	
    public Integer verificarHistoricos(){
    	
		Number size = 0;
		
		Criteria criteria = histNegativoService.crud().getSession().createCriteria(HistoricoNegativo.class);
		criteria.add(Restrictions.isNull("dtExclusao"));
		
		size =  ((Long) criteria.setProjection(Projections.count("id")).uniqueResult());
		
		int result = size.intValue();
		
		if(result > 0) {
			return result;
		} else {
			return result = 0;
		}
	
    }
	
    public boolean checarPessoaLiberadas(){
    	
    	Integer id = checkUsuario.valid().getPessoa().getId();
    	
    	Pessoa pesAreaFuncao = pesService.findById(checkUsuario.valid().getPessoa().getId());
    	
    	if(pesAreaFuncao.getFuncao().getId() == 16 ||
    			pesAreaFuncao.getFuncao().getId() == 17 ||
    				pesAreaFuncao.getFuncao().getId() == 18 ) {
    		//liberarAcessoGestores = false;
    		return false;
    	} else {
    		//liberarAcessoGestores = true;
    		return true;	
    	}    	
    	
    }
    
	public HistoricoNegativo getHis() {
		return his;
	}

	public Integer getId() {
		return id;
	}

	public List<HistoricoNegativo> getLstHistNegativo() {
		return lstHistNegativo;
	}

	public void setHis(HistoricoNegativo his) {
		this.his = his;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setLstHistNegativo(List<HistoricoNegativo> lstHistNegativo) {
		this.lstHistNegativo = lstHistNegativo;
	}

	public Integer getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}



	public boolean isMostrarTipoRegistroV() {
		return mostrarTipoRegistroV;
	}

	public void setMostrarTipoRegistroV(boolean mostrarTipoRegistroV) {
		this.mostrarTipoRegistroV = mostrarTipoRegistroV;
	}

	public boolean isMostrarTipoRegistroM() {
		return mostrarTipoRegistroM;
	}

	public void setMostrarTipoRegistroM(boolean mostrarTipoRegistroM) {
		this.mostrarTipoRegistroM = mostrarTipoRegistroM;
	}

	public Integer getValQtdCaracteres() {
		return valQtdCaracteres;
	}

	public void setValQtdCaracteres(Integer valQtdCaracteres) {
		this.valQtdCaracteres = valQtdCaracteres;
	}

	public boolean isDisBtnMotoristaVeiculo() {
		return disBtnMotoristaVeiculo;
	}

	public boolean isDisBtnSalvarMotoristaVeiculo() {
		return disBtnSalvarMotoristaVeiculo;
	}

	public void setDisBtnMotoristaVeiculo(boolean disBtnMotoristaVeiculo) {
		this.disBtnMotoristaVeiculo = disBtnMotoristaVeiculo;
	}

	public void setDisBtnSalvarMotoristaVeiculo(boolean disBtnSalvarMotoristaVeiculo) {
		this.disBtnSalvarMotoristaVeiculo = disBtnSalvarMotoristaVeiculo;
	}

	public String getTxtHeaderDtTable() {
		return txtHeaderDtTable;
	}

	public void setTxtHeaderDtTable(String txtHeaderDtTable) {
		this.txtHeaderDtTable = txtHeaderDtTable;
	}

	public boolean isDcFinalizarOperacao() {
		return dcFinalizarOperacao;
	}

	public void setDcFinalizarOperacao(boolean dcFinalizarOperacao) {
		this.dcFinalizarOperacao = dcFinalizarOperacao;
	}

	public Date getPesqDtInicio() {
		return pesqDtInicio;
	}

	public Date getPesqDtFinal() {
		return pesqDtFinal;
	}

	public String getPesqMotorista() {
		return pesqMotorista;
	}

	public String getPesqCPF() {
		return pesqCPF;
	}

	public String getPesqPlaca() {
		return pesqPlaca;
	}

	public String getPesqStsStatus() {
		return pesqStsStatus;
	}

	public void setPesqDtInicio(Date pesqDtInicio) {
		this.pesqDtInicio = pesqDtInicio;
	}

	public void setPesqDtFinal(Date pesqDtFinal) {
		this.pesqDtFinal = pesqDtFinal;
	}

	public void setPesqMotorista(String pesqMotorista) {
		this.pesqMotorista = pesqMotorista;
	}

	public void setPesqCPF(String pesqCPF) {
		this.pesqCPF = pesqCPF;
	}

	public void setPesqPlaca(String pesqPlaca) {
		this.pesqPlaca = pesqPlaca;
	}

	public void setPesqStsStatus(String pesqStsStatus) {
		this.pesqStsStatus = pesqStsStatus;
	}

	public boolean isInvalidarBtnAcao() {
		return invalidarBtnAcao;
	}

	public void setInvalidarBtnAcao(boolean invalidarBtnAcao) {
		this.invalidarBtnAcao = invalidarBtnAcao;
	}

	public boolean isLiberarAcessoGestores() {
		return liberarAcessoGestores;
	}

	public void setLiberarAcessoGestores(boolean liberarAcessoGestores) {
		this.liberarAcessoGestores = liberarAcessoGestores;
	}

	
		
	
	
}
