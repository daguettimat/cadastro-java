package br.com.global5.infra.security;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.interceptor.InvocationContext;

import org.apache.deltaspike.security.api.authorization.Secures;

import br.com.global5.infra.exception.CustomException;

/**
 * Created by j r zielinski on 12/20/14.
 */
@SessionScoped
@Named("authorizer")
public class CustomAuthorizer implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3926377847137944031L;
	private Map<String, String> currentUser = new ConcurrentHashMap<>();


    @Secures
    @Admin
    public boolean doAdminCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        boolean allowed = currentUser.containsKey("user") && currentUser.get("user").equals("admin");
        if(!allowed){
            throw new CustomException("Access denied");
        }
        return allowed;
    }

    @Secures
    @Guest
    public boolean doGuestCheck(InvocationContext invocationContext, BeanManager manager) throws Exception {
        boolean allowed = currentUser.containsKey("user") && currentUser.get("user").equals("guest") || doAdminCheck(null, null);
        if(!allowed){
            throw new CustomException("Access denied");
        }
        return allowed;
    }

    public void login(String username) {
        currentUser.put("user", username);
        if(FacesContext.getCurrentInstance() != null){
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage("Logged in sucessfully as <b>"+username+"</b>"));
        }
    }

    public Map<String, String> getCurrentUser() {
        return currentUser;
    }
}
