package br.com.global5.manager.chamado.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.apache.deltaspike.core.api.scope.ViewAccessScoped;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;

import br.com.global5.infra.util.OperacoesArquivos;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.chamado.Chamado;
import br.com.global5.manager.chamado.ChamadoResposta;
import br.com.global5.manager.chamado.service.ChamadoRespostaService;
import br.com.global5.manager.chamado.service.ChamadoService;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.template.exception.BusinessException;

@Named
@ViewAccessScoped
public class ChamadoRespostaBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private ChamadoResposta chamR;
	private Chamado chamado;
	
	private Integer id;
	private Integer idChamado;
	
	private List<ChamadoResposta> listChamadoResp;
	
	private Usuario usuario;
	
	private String msgSuporteAoCliente = "";

	@Inject
	ChamadoRespostaService chamRService;
	
	@Inject
	ChamadoService chamService;
	
	@PostConstruct
	public void init(){
		
		clear();
		
	}

	public void clear(){
		
		usuario = checkUsuario.valid();		
		chamR = new ChamadoResposta();
//		chamR.setChamado(new Chamado());
//		id = null;
//		idChamado = null;
	}
	
	
	public void updateRegistro(Integer idChamado, String msgResposta){		
		
		Date dtInicioAtendimento = null;
		
		// Contagem de atendimentos ao chamado
		Number size = 0;
		Criteria criteria = chamRService.crud().getSession().createCriteria(ChamadoResposta.class);
		criteria.add(Restrictions.eq("chamado.id", idChamado));
		
		size = (Long) criteria.setProjection(Projections.count("id")).uniqueResult();
		
		int numAtendimento = size.intValue();
		
		
		chamR = new ChamadoResposta();
		
		if ( chamR.getId() == null ) {
			
			// Novo Registro de Suporte
			chamR.setChamado(findByIdChamado(idChamado, numAtendimento));					
			chamR.setDtMensagem(new Date());
			chamR.setUsuario(checkUsuario.valid());
			chamR.setUsuInterno(true);
			chamR.setMensagem(msgResposta);
			
			chamRService.crud().save(chamR);
			
			this.setMsgSuporteAoCliente("");
			
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmChamadoSuporte");
			
			 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso","Registro de atendimento ao cliente realizado com sucesso!"));
			
		} else {
			// Manutenção de registros
		}
		
		
		
	}

	 public Chamado findByIdChamado(Integer id, Integer numAtendimento){		 
	    	chamado = chamService.crud().get(id);
	    	
	    	if(numAtendimento == 1){
	    		chamado.setDtAtendimento(new Date());
	    		chamado.setUsuAtendimento(checkUsuario.valid());
	    		chamService.update(chamado);
	    	}
	    	
	    	idChamado = chamado.getId();
	        return chamado;
	    }	 
	 
	 
	 public void downLoadFileChamado(Integer id){
		
		 ChamadoResposta chamR = chamRService.crud().get(id);
		 
		 if( chamR.getId() != null){
		 String path = String.format("/var/www/chamados/" + chamR.getChamado().getId() + "/");
		 String fileName = chamR.getArquivoEnviado();
		 
		 OperacoesArquivos.downloadFile(fileName, path, "", FacesContext.getCurrentInstance());
		 
		 }
		 	else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Atenção", "Problema no download do arquivo. Informe ao suporte da TI!"));
				
				return;
		 		
		 }
			
			
	}
 
	 
	public ChamadoResposta getChamR() {
		return chamR;
	}

	public void setChamR(ChamadoResposta chamR) {
		this.chamR = chamR;
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

	public Integer getIdChamado() {
		return idChamado;
	}

	public void setIdChamado(Integer idChamado) {
		this.idChamado = idChamado;
	}

	public String getMsgSuporteAoCliente() {
		return msgSuporteAoCliente;
	}

	public void setMsgSuporteAoCliente(String msgSuporteAoCliente) {
		this.msgSuporteAoCliente = msgSuporteAoCliente;
	}

	public List<ChamadoResposta> getListChamadoResp() {
		return listChamadoResp;
	}

	public void setListChamadoResp(List<ChamadoResposta> listChamadoResp) {
		this.listChamadoResp = listChamadoResp;
	}	
	
	
	
}
