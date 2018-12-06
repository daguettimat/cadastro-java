package br.com.global5.infra.util;

import br.com.global5.manager.bean.geral.LogonMB;
import br.com.global5.manager.model.geral.Usuario;
import org.primefaces.context.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by zielinski on 27/06/17.
 */
public class checkUsuario {

    public static Usuario valid() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        try {
            LogonMB logonMB = (LogonMB) session.getAttribute("logonMB");
           
            if( logonMB == null  ) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
            } else {
            	
                return logonMB.getUsuarioLogado();

            }
        } catch (Exception e) {
            try {
                facesContext.getExternalContext().redirect("../login.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        return null;
    }

    public static boolean isOption(boolean internal) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        try {

            Usuario usuario = valid();
            if( usuario.isInterno() != internal ) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../../index.xhtml");
                RequestContext.getCurrentInstance().showMessageInDialog(
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Acesso Negado!",
                                "Seu perfil de usuário não possui acesso a esta opção"));
                return false;
            }

        } catch (Exception e) {
            try {
                facesContext.getExternalContext().redirect("../login.xhtml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;

        }

        return true;
    }



}
