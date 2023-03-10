package br.com.global5.manager.bean.geral;


import br.com.global5.infra.util.AppUtils;
import br.com.global5.infra.util.checkUsuario;
import br.com.global5.manager.model.areas.AreaNivel;
import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.geral.UsuarioArea;
import br.com.global5.manager.model.geral.UsuarioCtr;
import br.com.global5.manager.model.permissao.Formulario;
import br.com.global5.manager.service.areas.AreaNivelService;
import br.com.global5.manager.service.areas.AreaService;
import br.com.global5.manager.service.geral.UsuarioService;
import br.com.global5.manager.service.permissao.FormularioService;


import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.ListUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import java.io.*;
import java.util.List;
import java.util.Properties;

/**
 * Created by j r zielinski on 04/02/17.
 */
@Named
@SessionScoped
public class LogonMB implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8356159542001076294L;

	private String login;

    private String password;

    private String newpasswd;

    private String chkpasswd;

    private boolean remember;

    private boolean loggedIn;

    private Usuario usuarioLogado;
    
    private boolean acessoSistemaCadastro = false;

    private Properties prop = new Properties();
    private InputStream input = null;

    private String ambiente;
    
    private List<UsuarioArea> listUsuArea;
    private List<UsuarioCtr>  listUsuCtr;
    
    
    //TESTE CONTROLE_ACESSO
    private String conteudoHtml = "";
    private String idProdutosDoContrato = "";
    private Integer codArea_anvloid = null;
    private Integer numArea_anvloid = null;
    
    // Disponibiliza acesso a tela index
    private boolean openCadastro = true;
    private boolean openVitimologia = true;
    
    @Inject
    AreaNivelService areaNivelService;
    
    @Inject
    UsuarioService uService;

    @Inject
    private AreaService areaService;
    
    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

    }

    public void trocarSenha() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("cpasswd.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "A troca de senhas n??o pode ser carregada. " +
                            "Informe ao suporte t??cnico.", null));
        }

    }

    public String cliente() {

        try {
            if (usuarioLogado.getPessoa().getFuncao().getArea().getRoot() == null) {
                return usuarioLogado.getNome() + "(" + usuarioLogado.getPessoa().getFuncao().getArea().getNome() + ")";
            } else {
                return usuarioLogado.getNome() + "(" + usuarioLogado.getPessoa().getFuncao().getArea().getRoot().getNome() + ")";
            }

        } catch (Exception e) {}

        return "";
    }

    public String doLogon() {
    	String permissao = "";
        //Verifica se o e-mail e senha existem e se o usuario pode logar
        Usuario usuarioFound = uService.isUsuarioReadyToLogin(login, password);
        if(usuarioFound != null){
        	
        	permissao = this.permissaoAcessoUsuario(usuarioFound);
        	
        }
        //Caso n??o tenha retornado nenhum usuario, ent??o mostramos um erro
        //e redirecionamos ele para a p??gina login.xhtml
        //para ele realiza-lo novamente
        if (usuarioFound == null){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "As informa????es de login, n??o s??o v??lidas, tente novamente!"));
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "login.xhtml?faces-redirect=true";
        } else if(usuarioFound != null && permissao == "concedida"){
            //caso tenha retornado um usuario, setamos a vari??vel loggedIn
            //como true e guardamos o usuario encontrado na vari??vel
            //usuarioLogado. Depois de tudo, mandamos o usu??rio
            //para a p??gina index.xhtml
        	
            loggedIn = true;
            usuarioLogado = usuarioFound;
            
            if(numArea_anvloid == 2 || numArea_anvloid == 3 ){
            	checarCtrClienteCad();
            	checarCtrClienteVit();
            } else {
            	openCadastro = true;
            	openVitimologia = true;
            }
            
			// Ajuste usuario_interno: Quando a area_nivel_interna estiver igual true o usuario ?? interno
			
			// pesquisa da area_nivel_interna em AreaNivel
			Criteria criteria = areaNivelService.crud().getSession().createCriteria(AreaNivel.class);
			criteria.add(Restrictions.eq("id", numArea_anvloid));
			criteria.add(Restrictions.eq("interna", true));
		
			int result = criteria.list().size();
			
			if (result > 0) {
				// fazemos o update no cadastro do usuario se caso n??o estiver com true seu cadastro
				AreaNivel areaNivel = (AreaNivel) criteria.list().get(0);
				
					if(areaNivel.isInterna() != usuarioLogado.isInterno()){
						usuarioLogado.setInterno(true);
						uService.crud().update(usuarioLogado);
					}
				
			}
		            
            this.gerarMenuHtml();
            //this.montarMenuHtml(usuarioFound);
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
           
            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("logonMB", this);
            session.setAttribute("ambiente", ambiente);
            return "index.xhtml?faces-redirect=true";
            
        } else if (usuarioFound != null && permissao == "negada"){
        	
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aten????o! As informa????es de login, n??o s??o v??lidas. Contate o nosso setor de Atendimento ao Cliente!", null));
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            //return "login.xhtml?faces-redirect=true";
        }
        
        return null;
		
    }
    
    public String checarCtrClienteVit(){
    	
		Integer idClient = null;
		
		if(numArea_anvloid == 3){
			//  Filial
			idClient = usuarioLogado.getPessoa().getFuncao().getArea().getRoot().getId();
		} else {
			// Demais cliente Matriz e Global5
			idClient = usuarioLogado.getPessoa().getFuncao().getArea().getId();
		}
		
        String SQL = "select row_number() over() as id, cp.conp_conoid as contrato" +
                "  from java.contrato c " +
                "       join java.contrato_produto cp " +
                "            on cp.conp_conoid = c.conoid " +
                "       join java.produto p " +
                "            on p.prodoid = cp.conp_prodoid " +
                " where c.con_areaoid = " + idClient +
                "   and c.con_dt_exclusao is null " +
                " 	and cp.conp_produto_ativo = true " + 
                "   and cp.conp_prodoid = 32 ";
        
        Query query = 
        		areaService.crud().getEntityManager().createNativeQuery(SQL);
    	
        try {

            Object valor = query.getResultList();
            
            int res = query.getResultList().size();  
                       
            if(numArea_anvloid == 2 || numArea_anvloid == 3 ){
	            
            	if(res > 0){
            		openVitimologia = true;
            		return "col-lg-3 col-xs-3";
            	} else {
	            	openVitimologia = false;
	            	return "col-lg-4 col-xs-4";            		
            	}
	            
            } 
            
        } catch (NoResultException nre) {
            
        }
	
	openVitimologia = true;
	return "col-lg-3 col-xs-3";
}
    
    // implementa????o
    /**
     *Method: call method listMenu and set at variable string conteudoHtml to menu application.   
     *
     *created: 2018-12-04 
     *
     *@author Francis.Bueno
     *
     */
    private void gerarMenuHtml(){
    	
    	this.setConteudoHtml(this.listMenu());
    	
    }
    
    /**
     * Method: called by method gerarMenuHtml for generation the menu of systems erp with base at access permission
     * 
     * created: 2018-12-04
     * 
     * @author Francis.Bueno
     * @return string 
     * 
     */
    public String listMenu(){
    	
    	StringBuilder html = new StringBuilder();
    	
    	EntityManager em = uService.crud().getEntityManager();
    	
    	String parameters = usuarioLogado.getPessoa().getFuncao().getId().toString(); 
    	String parametersFormulario = " and form_empoid is null ";
    	
	    String query = " select formoid as id, form_titulo as titulo, form_tag as tag, " +
                  " form_descricao as descricao, form_ordem_menu as ordemMenu, form_interna as areaInterna, " +
                  " form_area_matriz as areaMatriz,  form_area_filial as areaFilial, " +
                  " form_prodoid as idProduto, form_dt_criacao as dtCriacao, form_dt_exclusao as dtExclusao, " +
                  " form_usuoid_criacao as usuarioCriacao,form_usuoid_exclusao as usuarioExclusao,  form_url as urlMenu, " +
                  " form_nivel_pai as nivelPai , form_nivel as nivel  ,form_indice as indice, form_icone as icone, form_nivel_tipo as nivelTipo" +
                  " from formulario " +
                  " where formoid in (select aff_formoid from area_funcao_formulario where aff_afunoid = "  + parameters + " ) and " + 
                  " form_nivel_pai is null and form_dt_exclusao is null " +
                  	parametersFormulario +
                  " order by form_ordem_menu, form_indice" ;

	      List<Formulario> lMenu = em.createNativeQuery(query, "LstFormularioMapping").getResultList();
	      
	      int result = lMenu.size();
	    
	      if ( result > 0 ) {
	    	  
	    	  for( int i = 0; i < result ; i++){
	    		  //Nivel: S - sess??o, M - menu e P - p??gina
	    		  
	    		  // 1 Sess??o Livre - 
	    		  	//HOME ( ?? uma pagina pois t??m um link). Por??m deve ser o primeiro respeitando a estrutura do menu padr??o arquitetado
	    		  	if ( lMenu.get(i).getTag().equals("HOME") ) {

	    		  		html.append("<li class= \"\"" + ">");
	    		  		html.append("<a class=\"ui-link ui-widget\" href=\"/manager/index.xhtml\">");
	    		  		html.append("<i class=\"" + lMenu.get(i).getIcone() + "\" > "      );
	    		  		html.append("</i>");
	    		  		html.append("<span>" + lMenu.get(i).getTitulo() + "</span>"  );
	    		  		html.append("</a>");
	    		  		html.append("</li>");	    		  		
	    		  		
	    		  	}
	    		  
	    		  	//Administra????o (tipo menu) nota: html tag <li> n??o ser?? finalizada neste if
	    		  	if ( lMenu.get(i).getTag().equals("ADMIN") ) {
	    		  	
	    		  		//Menu
	    		  		html.append("<li class=\"treeview\" >");
	    		  		html.append("<a href=\"#\" >");
	    		  		html.append("<i class=\"" + lMenu.get(i).getIcone() + " \" >"  );
	    		  		html.append("</i>");
	    		  		html.append("<span>" + lMenu.get(i).getTitulo() + "</span>"  );	    		  		
	    		  		html.append("<span class=\"pull-right-container\">");
	    		  		html.append("<i class=\"fa fa-angle-left pull-right \" > ");
	    		  		html.append("</i>");
	    		  		html.append("</span>");
	    		  		html.append("</a>");
	    		  		
	    		  		//Paginas do Menu
	    		  		List<Formulario> lstPaginas = this.ListMenusFilhos(lMenu.get(i).getId());
	    		  		
	    		  		int resultF = lstPaginas.size();
	    		  		
	    		  		if ( resultF > 0 ) {
	    		  									 
	    		  			html.append("<ul class=\"treeview-menu menu-open\">");
	    		  			
	    		  			for ( int mi = 0 ; mi < resultF ; mi++) {
	    		  				
	    		  				html.append("<li> ");
	    		  				html.append("<a href=\"" + lstPaginas.get(mi).getUrlMenu() + "\">");
	    		  				html.append("<i class=\"" + lstPaginas.get(mi).getIcone() + " \" >");
	    		  				html.append("</i>");
	    		  				html.append("<span>" + lstPaginas.get(mi).getTitulo() + "</span>");	    		  				
	    		  				html.append("</a>");
	    		  				html.append("</li>");	    		  				
	    		  				
	    		  			}
	    		  			
	    		  			html.append("</ul>"); // final class treeview-menu
	    		  			
	    		  		}
	    		  		
	    		  		html.append("</li>");
	    		  		
	    		  	} // final Menu Administra????o
	    		  
	    		  	// S - sessao 
	    		  	if ( lMenu.get(i).getNivelTipo().equals("S") ) {
	    		  		
	    		  		//Header
	    		  		html.append("<li class=\"header\">" + lMenu.get(i).getTitulo() + "</li>");	    		  		
	    		  		
	    		  	}
	    		  	
	    		  	// Demais Menus e Paginas, excluindo ADMIN e HOME
	    		  	if ( lMenu.get(i).getNivelTipo().equals("M") && !lMenu.get(i).getTag().equals("ADMIN")) {
	    		  		
	    		  		//Menu	    		  			    		  		
	    		  		html.append("<li class=\"treeview\" >");
	    		  		html.append("<a href=\"#\" >");
	    		  		html.append("<i class=\"" + lMenu.get(i).getIcone() + " \" >"  );
	    		  		html.append("</i>");
	    		  		html.append("<span>" + lMenu.get(i).getTitulo() + "</span>"  );	    		  		
	    		  		html.append("<span class=\"pull-right-container\">");
	    		  		html.append("<i class=\"fa fa-angle-left pull-right \" > ");
	    		  		html.append("</i>");
	    		  		html.append("</span>");
	    		  		html.append("</a>");	    		  		    		  		
	    		  		
	    		  		//Paginas do Menu
	    		  		List<Formulario> lstPaginas = this.ListMenusFilhos(lMenu.get(i).getId());
	    		  		
	    		  		int resultF = lstPaginas.size();
	    		  		
	    		  		if ( resultF > 0 ) {
	    		  			
	    		  			html.append("<ul class=\"treeview-menu menu-open \">");
	    		  			
	    		  			for ( int mi = 0 ; mi < resultF ; mi++) {
	    		  					    		  				
	    		  				html.append("<li> ");
	    		  				html.append("<a class=\"ui-link ui-widget\" href=\"" + lstPaginas.get(mi).getUrlMenu() + "\">");
	    		  				html.append("<i class=\"" + lstPaginas.get(mi).getIcone() + " \" >");
	    		  				html.append("</i>");
	    		  				html.append("<span>" + lstPaginas.get(mi).getTitulo() + "</span>");	    		  				
	    		  				html.append("</a>");
	    		  				html.append("</li>");	    		  				
	    		  				
	    		  			}
	    		  			
	    		  			html.append("</ul>"); // final class treeview-menu
	    		  			
	    		  		}
	    		  		
	    		  		html.append("</li>");	    		  		
	    		  		
	    		  	}
	    		  
	    	  }
	    	  return html.toString();
	    	    
	      } else {
	    	  
	    	  return "<h1>.: PERMISSAO N??O ATRIBUIDA :.</h1>";
	      }
	      
    }
    
    
    /**
     * Method: called by method listMenu to implement at generation the sub menu of systems erp with base at access permission
     * 
     * created: 2018-12-04
     * 
     * @author Francis.Bueno
     * @return string 
     * 
     */
    public List<Formulario> ListMenusFilhos (Integer idNivelPai){
    	
    	EntityManager emF = uService.crud().getEntityManager();
    	
    	String parameters = usuarioLogado.getPessoa().getFuncao().getId().toString();
    	String parametersFormulario = " and form_empoid is null ";
    	
    	if ( !this.getIdProdutosDoContrato().equals("") ) {
    		
    		parameters  = parameters + " and form_prodoid is null or form_prodoid in ( " + this.getIdProdutosDoContrato() + ") "; 
    	}
    	
    	if ( this.getCodArea_anvloid() != null ) {
    		
    		//Transportadora / nivel 2 = Matriz Cliente
    		if ( this.getCodArea_anvloid() == 2 ) {
    			
    			parameters  = parameters + " and form_area_matriz = true " ;
    			
    		} else {
    		
    		// Unidade de Transportes / nivel 3 = filiais
    			if ( this.getCodArea_anvloid() == 3 ) {
    				parameters  = parameters + " and form_area_filial = true " ;
    			}
    		
    		}
    	}    			
    	
	      String query = " select formoid as id, form_titulo as titulo, form_tag as tag, " +
                  " form_descricao as descricao, form_ordem_menu as ordemMenu, form_interna as areaInterna, " +
                  " form_area_matriz as areaMatriz,  form_area_filial as areaFilial, " +
                  " form_prodoid as idProduto, form_dt_criacao as dtCriacao, form_dt_exclusao as dtExclusao, " +
                  " form_usuoid_criacao as usuarioCriacao,form_usuoid_exclusao as usuarioExclusao,  form_url as urlMenu, " +
                  " form_nivel_pai as nivelPai , form_nivel as nivel  ,form_indice as indice, form_icone as icone, form_nivel_tipo as nivelTipo" +
                  " from formulario " +
                  " where formoid in (select aff_formoid from area_funcao_formulario where aff_afunoid = "  + parameters + " ) " +
                  " and form_nivel_pai = " + idNivelPai + 
                  " and form_dt_exclusao is null " +
                  	parametersFormulario +
                  " order by form_ordem_menu, form_indice" ;
	      
	      int result = emF.createNativeQuery(query, "LstFormularioMapping").getResultList().size();
    	
	      if ( result > 0 ) {
	    	  // retorna uma lista com os itens de menus selecionados
	    	  return emF.createNativeQuery(query, "LstFormularioMapping").getResultList();
	    	  
	      } else {
	    	  // retorna uma lista null
	    	  return emF.createNativeQuery(query, "LstFormularioMapping").getResultList() ;
	      }
    	    	
    }    
    
  
    /**
     * Method:  verify the kind definition at your user by area and the validity status agreement the customer, 
     * case status of agreement is 645 (active) or 646 (cancel previous) then access will by allow 
     * created at: 2018/09/11
     * 
     * @author Francis.Bueno
     * @param usuarioFound receive usuario object for extract the key of pessoa
     * @return string (concedida) for access and (negada) for denied 
     */
    private String permissaoAcessoUsuario(Usuario usuarioFound){
    		
    	if(!usuarioFound.equals(null)){
    		
    		String parameters = "";
    		Integer numAreaoid, numArea_areaoid_pai = null;
    		String  alfUsu_Tipo = "";
    		
    		EntityManager em = uService.crud().getEntityManager();    		
    		
    		Integer userPessoid = usuarioFound.getPessoa().getId();
    	
    		parameters = " pesoid =  " + userPessoid + " and ";
    		
    		String query = "select row_number() over() as id, areaoid, area_areaoid_pai, area_anvloid " + 
    				" from area, area_funcao, pessoa " +
    				" where " + parameters + 
    				" pes_afunoid=afunoid and afun_areaoid=areaoid " ;
    		
    		listUsuArea = em.createNativeQuery(query, "UsuarioAreaMapping").getResultList();
    		
    		int sizeListUsuArea = listUsuArea.size();
    		
    		if(sizeListUsuArea == 1){
    			
    			String parametersNv2 = "";
    			String parametersNv3 = "";
    			
    			numAreaoid = listUsuArea.get(0).getAreaoid();
    			numArea_areaoid_pai = listUsuArea.get(0).getArea_areaoid_pai();
    			numArea_anvloid = listUsuArea.get(0).getArea_anvloid();
    			
    			// Continua com o processo
    			
    			this.setCodArea_anvloid(numArea_anvloid);
    			
    			//Caso nivel da area seja (2) = Transportadora
    			if(numArea_anvloid == 2){
    				
    				alfUsu_Tipo = "T";
    				
    				 EntityManager em2 = uService.crud().getEntityManager();
    				
    				 if(numAreaoid != null){
    					 
    					 parametersNv2 = " con_areaoid = " + numAreaoid + " and ";
    					 
    					 // Seleciona contratos ativos (645) e com aviso previos (646) no produto cadastro
    					 String query2 = "select row_number() over() as id, conoid, con_enumoid_produto_tipo as  produtoid from java.contrato where " + parametersNv2 + 
    						 " con_enumoid_status in (645, 646, 774) and con_enumoid_produto_tipo in (15,16)" ;
    					 
    					 listUsuCtr = em2.createNativeQuery(query2, "UsuarioCtrMapping").getResultList();
    					 int sizeListUsuCtr = listUsuCtr.size();
    					 
    					 if(sizeListUsuCtr != 0){
    						 // Retorna string para confirmar o acesso
    						 for( int i = 0; i < sizeListUsuCtr; i++) {
    							 
    							 if ( listUsuCtr.get(i).getProdutoid() == 15) {
    								 this.setAcessoSistemaCadastro(true);
    								 this.verificarProdutosDoContrato(listUsuCtr.get(i).getConoid());
    							 }
    							 
    						 }
    						 
    						 //this.verificarProdutosDoContrato(listUsuCtr.get(0).getConoid());
    						 
    						 return "concedida";
    					 } else if(sizeListUsuCtr == 0){
    						 // Retorna string para negar o acesso    						 
    						 return "negada";
    					 }
    					 
    				 } else {
    					 // Area da pessoa n??o encontrada. Retorna string para negar acesso
    					 return "negada";
    				 }
    				
    			} else 
    				//Caso nivel da area seja (3) = Unidade de Transportadora OU Filiais
    				if(numArea_anvloid == 3){
    				
        				alfUsu_Tipo = "F";
        				
       				 EntityManager em3 = uService.crud().getEntityManager();
       				
       				 if(numAreaoid != null){
       					 
       					 parametersNv3 = " con_areaoid = " + numArea_areaoid_pai + " and ";
       					 
       					 // Seleciona contratos ativos (645) e com aviso previos (646) no produto cadastro
       					 String query3 = "select row_number() over() as id, conoid, con_enumoid_produto_tipo as  produtoid from java.contrato where " + parametersNv3 + 
       						 " con_enumoid_status in (645, 646, 774 ) and con_enumoid_produto_tipo in (15, 16)" ;
       					 
       					 listUsuCtr = em3.createNativeQuery(query3, "UsuarioCtrMapping").getResultList();
       					 
       					 int sizeListUsuCtr = listUsuCtr.size();
       					 
       					 if(sizeListUsuCtr != 0){
       						 // Retorna string para confirmar o acesso
       					
    						 for( int i = 0; i < sizeListUsuCtr; i++) {
    							 
    							 if ( listUsuCtr.get(i).getProdutoid() == 15) {    								 
    								 this.setAcessoSistemaCadastro(true);
    								 this.verificarProdutosDoContrato(listUsuCtr.get(i).getConoid());
    							 }
    							 
    						 } 
       						 
       						//this.verificarProdutosDoContrato(listUsuCtr.get(0).getConoid());
       						 
       						 return "concedida";
       					 } else if(sizeListUsuCtr == 0){
       						 // Retorna string para negar o acesso    						 
       						 return "negada";
       					 }
       					 
       				 } else {
       					 // Area da pessoa n??o encontrada. Retorna string para negar acesso
       					 return "negada";
       				 }
    					
    			} else 
    				// Acesso ao Operacional Interno Global5
    				 {
    					//alfUsu_Tipo = "O";

    					 this.setAcessoSistemaCadastro(true);
    				
    					// Retorna string para confirmar o acesso
  						 return "concedida";
    				}
    			
    		}
    		
    	}
    		
    	return "negada";
    }
    
    
public String checarCtrClienteCad(){
    	  	
    		Integer idClient = null;

    		if(numArea_anvloid == 3){
    			//  Filial
    			idClient = usuarioLogado.getPessoa().getFuncao().getArea().getRoot().getId();
    		} else {
    			// Demais cliente Matriz e Global5
    			idClient = usuarioLogado.getPessoa().getFuncao().getArea().getId();
    		}

            String SQL = "select row_number() over() as id, cp.conp_conoid as contrato" +
                    "  from java.contrato c " +
                    "       join java.contrato_produto cp " +
                    "            on cp.conp_conoid = c.conoid " +
                    "       join java.produto p " +
                    "            on p.prodoid = cp.conp_prodoid " +
                    " where c.con_areaoid = " + idClient +
                    "   and c.con_dt_exclusao is null " +
                    " 	and cp.conp_produto_ativo = true " + 
                    "   and cp.conp_prodoid in (1,2,3) ";
            
            Query query = 
            		areaService.crud().getEntityManager().createNativeQuery(SQL);                   	
            try {
            	
                Object valor = query.getResultList();
                
                int res = query.getResultList().size(); 
                
                if(numArea_anvloid == 2 || numArea_anvloid == 3 ){
	                if (res > 0){
	                	openCadastro = true;
	                	//return "col-lg-3 col-xs-3";
	                	
	                	if(openVitimologia == true) {
	                		return "col-lg-3 col-xs-3";
	                	} else {
	                		return "col-lg-4 col-xs-4";
	                	}
	  
	                	
	                } else {
	                	openCadastro = false;
	                	return "col-lg-4 col-xs-4";
	                }
                } else {
                	openCadastro = true;
                	return "col-lg-3 col-xs-3";
                }
	                
	                
            } catch (NoResultException nre) {
                
            }
    	
    	openCadastro = true;
    	return "col-lg-3 col-xs-3";
    }
    

    //Nova Implementa????o - verifica os produtos do contrato
    public void verificarProdutosDoContrato(Integer idContrato){
    	
    	EntityManager em = uService.crud().getEntityManager();
    	
    	String SQL = " select cp.conp_prodoid " +
    	        	 "      from contrato c   " + 
    	        	 "       join contrato_produto cp " +   
    	        	 "        on cp.conp_conoid = c.conoid " +   
    	        	 "       join produto p   " + 
    	        	 "        on p.prodoid = cp.conp_prodoid  " +  
    	        	 "   where cp.conp_produto_ativo = true and cp.conp_conoid = :idContrato ";
    	Query query = em.createNativeQuery(SQL);
    	query.setParameter("idContrato", idContrato);
    	
    	int result = query.getResultList().size();
    	String codProdutoAtivoContrato = "";
    	
    	if ( result > 0 ) {
    		
    		Integer fatorSaida = result - 1 ;
    		
    		for ( int i = 1; i <= result ; i++  ){
    			
    			if ( i < result) {
    				
    				codProdutoAtivoContrato = codProdutoAtivoContrato + query.getResultList().get(i - 1).toString() + " , ";
    				
    			} else 
    		
    				if ( i == result ) {
    					
    				codProdutoAtivoContrato = codProdutoAtivoContrato + query.getResultList().get(i - 1).toString();
    				
    			}
    		}
    		
    		if (! codProdutoAtivoContrato.equals("") ) {
    			
    			this.setIdProdutosDoContrato(codProdutoAtivoContrato);
    			
    		}
    		
    	}
    	
    }
    
    public String doChangePasswd() {

        if( ! newpasswd.equals(chkpasswd) ) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso", "A nova senha e sua valida????o s??o diferentes!!!!"));
            return "cpasswd.xhtml?faces-redirect=true";

        }
        //Verifica se o e-mail e senha existem e se o usuario pode logar
        Usuario usuarioFound = uService.isUsuarioReadyToLogin(login, password, newpasswd);

        //Caso n??o tenha retornado nenhum usuario, ent??o mostramos um erro
        //e redirecionamos ele para a p??gina login.xhtml
        //para ele realiza-lo novamente
        if (usuarioFound == null){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "N??o foi poss??vel, realizar a troca de senha!"));
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "login.xhtml?faces-redirect=true";
        }

        return "index.xhtml?faces-redirect=true";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public UsuarioService getuService() {
        return uService;
    }

    public void setuService(UsuarioService uService) {
        this.uService = uService;
    }

    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getNewpasswd() {
        return newpasswd;
    }

    public void setNewpasswd(String newpasswd) {
        this.newpasswd = newpasswd;
    }

    public String getChkpasswd() {
        return chkpasswd;
    }

    public void setChkpasswd(String chkpasswd) {
        this.chkpasswd = chkpasswd;
    }

	public List<UsuarioArea> getListUsuArea() {
		return listUsuArea;
	}

	public void setListUsuArea(List<UsuarioArea> listUsuArea) {
		this.listUsuArea = listUsuArea;
	}

	public List<UsuarioCtr> getListUsuCtr() {
		return listUsuCtr;
	}

	public void setListUsuCtr(List<UsuarioCtr> listUsuCtr) {
		this.listUsuCtr = listUsuCtr;
	}

	public String getConteudoHtml() {
		return conteudoHtml;
	}

	public void setConteudoHtml(String conteudoHtml) {
		this.conteudoHtml = conteudoHtml;
	}

	public String getIdProdutosDoContrato() {
		return idProdutosDoContrato;
	}

	public void setIdProdutosDoContrato(String idProdutosDoContrato) {
		this.idProdutosDoContrato = idProdutosDoContrato;
	}

	public Integer getCodArea_anvloid() {
		return codArea_anvloid;
	}

	public void setCodArea_anvloid(Integer codArea_anvloid) {
		this.codArea_anvloid = codArea_anvloid;
	}

	public boolean isAcessoSistemaCadastro() {
		return acessoSistemaCadastro;
	}

	public void setAcessoSistemaCadastro(boolean acessoSistemaCadastro) {
		this.acessoSistemaCadastro = acessoSistemaCadastro;
	}

	public Integer getNumArea_anvloid() {
		return numArea_anvloid;
	}

	public void setNumArea_anvloid(Integer numArea_anvloid) {
		this.numArea_anvloid = numArea_anvloid;
	}

	public boolean isOpenCadastro() {
		return openCadastro;
	}

	public void setOpenCadastro(boolean openCadastro) {
		this.openCadastro = openCadastro;
	}

	public boolean isOpenVitimologia() {
		return openVitimologia;
	}

	public void setOpenVitimologia(boolean openVitimologia) {
		this.openVitimologia = openVitimologia;
	}	
	    
	
	
}
