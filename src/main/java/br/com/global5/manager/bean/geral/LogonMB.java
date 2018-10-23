package br.com.global5.manager.bean.geral;

import br.com.global5.manager.model.geral.Usuario;
import br.com.global5.manager.model.geral.UsuarioArea;
import br.com.global5.manager.model.geral.UsuarioCtr;
import br.com.global5.manager.service.geral.UsuarioService;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
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

    private Properties prop = new Properties();
    private InputStream input = null;

    private String ambiente;
    
    private List<UsuarioArea> listUsuArea;
    private List<UsuarioCtr>  listUsuCtr;

    @Inject
    UsuarioService uService;

    @PostConstruct
    public void init() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

//        // load a properties file
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//            input = new FileInputStream(classLoader.getResource("admin-config.properties").getFile());
//            prop.load(input);
//            // get the property value and print it out
//            ambiente = prop.getProperty("geral.ambiente").toUpperCase();
//            System.out.println(ambiente);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    public void trocarSenha() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("cpasswd.xhtml");
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "A troca de senhas não pode ser carregada. " +
                            "Informe ao suporte técnico.", null));
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
        //Caso não tenha retornado nenhum usuario, então mostramos um erro
        //e redirecionamos ele para a página login.xhtml
        //para ele realiza-lo novamente
        if (usuarioFound == null){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "As informações de login, não são válidas, tente novamente!"));
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            return "login.xhtml?faces-redirect=true";
        } else if(usuarioFound != null && permissao == "concedida"){
            //caso tenha retornado um usuario, setamos a variável loggedIn
            //como true e guardamos o usuario encontrado na variável
            //usuarioLogado. Depois de tudo, mandamos o usuário
            //para a página index.xhtml
            loggedIn = true;
            usuarioLogado = usuarioFound;
           //
            FacesContext facesContext = FacesContext.getCurrentInstance();

            HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
            session.setAttribute("logonMB", this);
            session.setAttribute("ambiente", ambiente);
            return "index.xhtml?faces-redirect=true";
        } else if (usuarioFound != null && permissao == "negada"){
        	
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Atenção! As informações de login, não são válidas. Contate o nosso setor de Atendimento ao Cliente!", null));
            FacesContext.getCurrentInstance().validationFailed();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().clear();
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            //return "login.xhtml?faces-redirect=true";
        }
        
        return null;
		
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
    public String permissaoAcessoUsuario(Usuario usuarioFound){
    	if(!usuarioFound.equals(null)){
    		
    		String parameters = "";
    		Integer numAreaoid, numArea_areaoid_pai, numArea_anvloid = null;
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
    			
    			//Caso nivel da area seja (2) = Transportadora
    			if(numArea_anvloid == 2){
    				
    				alfUsu_Tipo = "T";
    				
    				 EntityManager em2 = uService.crud().getEntityManager();
    				
    				 if(numAreaoid != null){
    					 
    					 parametersNv2 = " con_areaoid = " + numAreaoid + " and ";
    					 
    					 // Seleciona contratos ativos (645) e com aviso previos (646) no produto cadastro
    					 String query2 = "select row_number() over() as id, conoid from java.contrato where " + parametersNv2 + 
    						 " con_enumoid_status in (645, 646) and con_enumoid_produto_tipo = 15" ;
    					 
    					 listUsuCtr = em2.createNativeQuery(query2, "UsuarioCtrMapping").getResultList();
    					 int sizeListUsuCtr = listUsuCtr.size();
    					 
    					 if(sizeListUsuCtr != 0){
    						 // Retorna string para confirmar o acesso
    						 return "concedida";
    					 } else if(sizeListUsuCtr == 0){
    						 // Retorna string para negar o acesso    						 
    						 return "negada";
    					 }
    					 
    				 } else {
    					 // Area da pessoa não encontrada. Retorna string para negar acesso
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
       					 String query3 = "select row_number() over() as id, conoid from java.contrato where " + parametersNv3 + 
       						 " con_enumoid_status in (645, 646) and con_enumoid_produto_tipo = 15" ;
       					 
       					 listUsuCtr = em3.createNativeQuery(query3, "UsuarioCtrMapping").getResultList();
       					 
       					 int sizeListUsuCtr = listUsuCtr.size();
       					 
       					 if(sizeListUsuCtr != 0){
       						 // Retorna string para confirmar o acesso
       						 return "concedida";
       					 } else if(sizeListUsuCtr == 0){
       						 // Retorna string para negar o acesso    						 
       						 return "negada";
       					 }
       					 
       				 } else {
       					 // Area da pessoa não encontrada. Retorna string para negar acesso
       					 return "negada";
       				 }
    					
    			} else 
    				// Acesso ao Operacional Interno Global5
    				 {
    					//alfUsu_Tipo = "O";
    					// Retorna string para confirmar o acesso
  						 return "concedida";
    				}
    			
    		}
    		
    	}
    		
    	return "negada";
    }
    
    
    public String doChangePasswd() {

        if( ! newpasswd.equals(chkpasswd) ) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Aviso", "A nova senha e sua validação são diferentes!!!!"));
            return "cpasswd.xhtml?faces-redirect=true";

        }
        //Verifica se o e-mail e senha existem e se o usuario pode logar
        Usuario usuarioFound = uService.isUsuarioReadyToLogin(login, password, newpasswd);

        //Caso não tenha retornado nenhum usuario, então mostramos um erro
        //e redirecionamos ele para a página login.xhtml
        //para ele realiza-lo novamente
        if (usuarioFound == null){
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Não foi possível, realizar a troca de senha!"));
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
    
	
    
}
