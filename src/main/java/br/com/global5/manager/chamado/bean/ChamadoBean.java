package br.com.global5.manager.chamado.bean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.*;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.Update;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.chamado.Chamado;
import br.com.global5.manager.chamado.ChamadoResposta;
import br.com.global5.manager.chamado.service.ChamadoRespostaService;
import br.com.global5.manager.chamado.service.ChamadoService;
import br.com.global5.manager.model.areas.Area;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class ChamadoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Chamado cham;
	
	private Integer id;
	private Usuario usuario;
	
	private List<Chamado> listChamados;
	private List<Chamado> filteredChamados;
	private LazyDataModel<Chamado> chamadoList;
	
	
	private ChamadoResposta chamR;
	
	private Integer idR;
	private List<ChamadoResposta> listChamadoResp;
	
	private String msgSuporteAoCliente = "";
	
	private boolean chamadoEncerrado = false;
	
	
	@Inject
	ChamadoService chamService;
	
	@Inject
	ChamadoRespostaService chamRService;
	
	@PostConstruct
	public void init(){
		
		clear();
		

	}
	
	public void clear(){
		
		usuario = checkUsuario.valid();
		
		cham = new Chamado();
		cham.setArea(new Area());
		
		chamR = new ChamadoResposta();
		id = null;
		this.setChamadoEncerrado(false);
		
	}
	
	public void updateSuporte(){
		//ChamadoResposta cr = new ChamadoResposta();
		
		EntityManager emChamado = chamService.crud().getEntityManager();
		
		emChamado.persist(cham);
		
		//chamR = new ChamadoResposta();
		
		if ( chamR.getId() == null ) {
			// Novo Registro de Suporte
			chamR.setChamado(new Chamado(id));		
			chamR.setMensagem(msgSuporteAoCliente);
			chamR.setDtMensagem(new Date());
			chamR.setUsuario(checkUsuario.valid());
			chamR.setUsuInterno(true);
			
			chamRService.crud().save(chamR);
			
			listarChamadoRespostas(id);
						
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmChamadoSuporte");
			//context.update("frmListaChamadoResposta:tbListResp");
			
		} else {
			// Manutenção de registros
		}
				
		
		
	}
	
	
	
	public void updateRegistro(Integer idChamado){		
		
		//chamR = new ChamadoResposta();
//		
//		if (idChamado != null){
//			Chamado chamadoP = findByIdChamado(idChamado);
//			//EntityManager em = usuarioService.crud().getEntityManager();
//			EntityManager em =  chamService.crud().getEntityManager();
//			em.persist(chamadoP);
//			
//		}
//		
		if ( chamR.getId() == null ) {
			
			// Novo Registro de Suporte
			chamR.setChamado(findByIdChamado(idChamado));					
			chamR.setDtMensagem(new Date());
			chamR.setUsuario(checkUsuario.valid());
			chamR.setUsuInterno(true);
			chamR.setMensagem(this.getMsgSuporteAoCliente());
			
			
			chamRService.crud().save(chamR);
			
			this.setMsgSuporteAoCliente("");
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmChamadoSuporte");
			context.update("frmListaChamadoResposta:tbListResp");
			
			
			
		} else {
			// Manutenção de registros
		}
		
		
		
	}

	 public Chamado findByIdChamado(Integer id){
	    	cham = chamService.crud().get(id);
	    	id = cham.getId();
	        return cham;
	    }

	 
	 public void finalizarChamado(){
		 
		 if (cham.getId() != null){
			 
			 if(cham.getDtAtendimento() != null) {
				 cham.setDtFinalizacao(new Date());
				 cham.setUsuFinalizacao(checkUsuario.valid());
				 chamService.update(cham);
				 
				 RequestContext context = RequestContext.getCurrentInstance();
				 context.update("frmChamadoSuporte");
				 
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso","Chamado foi fechado com sucesso!"));
			 } else {
				 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção","Registre um atendimento para finalizar o chamado!"));
			 }
			 
			 
		 }
	 }
	

	public void listarChamados(){
		
		Criteria criteria = chamService.crud().getSession().createCriteria(Chamado.class);
		
		int result = criteria.list().size();
		
		if ( result > 0 ){
			
			this.setListChamados(criteria.list());			
			RequestContext context = RequestContext.getCurrentInstance();
					
			context.update("frmListaChamados:tbListRec");
			
		} else {
			
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Nenhum registro encontrado conforme as especificações do filtro." ));
			//RequestContext context = RequestContext.getCurrentInstance();
			
			//context.update("frmListaRec:tbListRec");
			
		}
		
	}
	
	public void listarChamadoRespostas(Integer idCham){
		
		//findById(idCham);
		
		Criteria criteria = chamRService.crud().getSession().createCriteria(ChamadoResposta.class);
		
		if( id != null){
			criteria.add(Restrictions.eq("chamado.id", idCham ));
		}
				
		int result = criteria.list().size();
		
		if ( result > 0 ){
			
			this.setListChamadoResp(criteria.list());
			RequestContext context = RequestContext.getCurrentInstance();
			
			context.update("");
			
		} else {
			
			//FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Nenhum registro encontrado conforme as especificações do filtro." ));
			//RequestContext context = RequestContext.getCurrentInstance();
			
			//context.update("frmListaRec:tbListRec");
			
		}
		
	}
	
	public void refreshChamado(){
		if ( id != null){
			
			findById(id);
			
			Hibernate.initialize(cham.getUsuAtendimento());
			Hibernate.initialize(cham.getDtAtendimento());
			Hibernate.initialize(cham.getDtFinalizacao());
			
			listarChamadoRespostas(id);
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update(":frmChamadoSuporte:frmPainelChamado");
		}
	}
	
	public void showChamado(Integer id){
		
		this.id = id;
		
		findById(id);
		listarChamadoRespostas(id);
		
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("../chamados/chamados.xhtml?id=" + getId());
		} catch (Exception e) {
			 FacesContext.getCurrentInstance().addMessage(
	                    null,
	                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Aviso","Chamado " + getId()
	                            + " não pode ser carregado. Informe ao suporte técnico."));
		}
		
		
	}
	
    public void findById(Integer id){
        
    	if(id == null){
        
    		throw new BusinessException("O id é obrigatório");
    		
        }
        
    	cham = chamService.crud().get(id);
    	
    	if(cham.getDtFinalizacao() != null){
    		this.setChamadoEncerrado(true);
    	}

        if(cham == null){
        
        	throw new BusinessException("Registro não foi encontrado pelo id: " + id);
        	
        }
        
    }
    
	 public void btnBackListChamados() {
			try {
				clear();
				FacesContext.getCurrentInstance().getExternalContext().redirect("../chamados/chamados_list.xhtml");
			} catch (IOException e) {
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,"Lista de chamados não pode ser carregada. Informe ao suporte técnico.",null));
			}
	}
	 
	 
	 public Integer atendidos(){
	    	
 		Number size = 0;
 		
 		Criteria criteria = chamService.crud().getSession().createCriteria(Chamado.class);
 		criteria.add(Restrictions.isNotNull("dtAtendimento"));
 		criteria.add(Restrictions.isNotNull("dtFinalizacao"));
 		
 		size =  ((Long) criteria.setProjection(Projections.count("id")).uniqueResult());
 		
 		int result = size.intValue();
 		
 		if(result > 0) {
 			return result;
 		} else {
 			return result = 0;
 		}
 	
	 } 
	 
	 public Integer emAtendimento(){
	    	
    		Number size = 0;
    		
    		Criteria criteria = chamService.crud().getSession().createCriteria(Chamado.class);
    		criteria.add(Restrictions.isNotNull("dtAtendimento"));
    		criteria.add(Restrictions.isNull("dtFinalizacao"));
    		
    		size =  ((Long) criteria.setProjection(Projections.count("id")).uniqueResult());
    		
    		int result = size.intValue();
    		
    		if(result > 0) {
    			return result;
    		} else {
    			return result = 0;
    		}
    	
    } 

	public Integer naoAtendido(){
	    	
 		Number size = 0;
 		
 		Criteria criteria = chamService.crud().getSession().createCriteria(Chamado.class);
 		criteria.add(Restrictions.isNull("dtAtendimento"));
 		criteria.add(Restrictions.isNull("dtFinalizacao"));
 		
 		size =  ((Long) criteria.setProjection(Projections.count("id")).uniqueResult());
 		
 		int result = size.intValue();
 		
 		if(result > 0) {
 			return result;
 		} else {
 			return result = 0;
 		}
 	
	}  
	
	public List<Chamado> getListChamados() {
		listarChamados();
		return listChamados;
	}

	public void setListChamados(List<Chamado> listChamados) {
		this.listChamados = listChamados;
	}
	
	public List<Chamado> getFilteredChamados() {
		return filteredChamados;
	}

	public void setFilteredChamados(List<Chamado> filteredChamados) {
		this.filteredChamados = filteredChamados;
	}	
	
	public LazyDataModel<Chamado> getChamadoList() {
		return chamadoList;
	}

	public void setChamadoList(LazyDataModel<Chamado> chamadoList) {
		this.chamadoList = chamadoList;
	}

	public Chamado getCham() {
		return cham;
	}

	public void setCham(Chamado cham) {
		this.cham = cham;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChamadoResposta getChamR() {
		return chamR;
	}

	public void setChamR(ChamadoResposta chamR) {
		this.chamR = chamR;
	}

	public Integer getIdR() {
		return idR;
	}

	public void setIdR(Integer idR) {
		this.idR = idR;
	}

	public List<ChamadoResposta> getListChamadoResp() {
		return listChamadoResp;
	}

	public void setListChamadoResp(List<ChamadoResposta> listChamadoResp) {
		this.listChamadoResp = listChamadoResp;
	}

	public String getMsgSuporteAoCliente() {
		return msgSuporteAoCliente;
	}

	public void setMsgSuporteAoCliente(String msgSuporteAoCliente) {
		this.msgSuporteAoCliente = msgSuporteAoCliente;
	}

	public boolean isChamadoEncerrado() {
		return chamadoEncerrado;
	}

	public void setChamadoEncerrado(boolean chamadoEncerrado) {
		this.chamadoEncerrado = chamadoEncerrado;
	}

	
}
