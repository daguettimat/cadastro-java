package br.com.global5.showcase.bean;

import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Created by j r zielinski on 23/01/17.
 */
@Model
public class PanelMB {

    public void onClose(CloseEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Panel Closed", "Closed panel id:'" + event.getComponent().getId() + "'");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onToggle(ToggleEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, event.getComponent().getId() + " toggled", "StatusPendencias:" + event.getVisibility().name());
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
