
package br.com.global5.showcase.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DualListModel;

import br.com.global5.showcase.model.Parentesco;
import br.com.global5.showcase.model.Theme;
import br.com.global5.showcase.model.TipoContato;
import br.com.global5.showcase.service.GeneralListService;
import br.com.global5.showcase.service.ThemeService;

@Named
@ViewScoped
public class PickListMB implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5037724471144154468L;

	@Inject
    private ThemeService service;
    
    @Inject
    private GeneralListService generalList;
    
    private DualListModel<String> cities;
    private DualListModel<Theme> themes;
    
    private String parentesco;
    private String tipoContato;
    
    private List<Parentesco> lstParentesco;
    private List<TipoContato> lstTipoContato;
     
    
    @PostConstruct
    public void init() {
        //Cities
        List<String> citiesSource = new ArrayList<String>();
        List<String> citiesTarget = new ArrayList<String>();
        
        citiesSource.add("San Francisco");
        citiesSource.add("London");
        citiesSource.add("Paris");
        citiesSource.add("Istanbul");
        citiesSource.add("Berlin");
        citiesSource.add("Barcelona");
        citiesSource.add("Rome");
        
        cities = new DualListModel<String>(citiesSource, citiesTarget);
        
        //Themes
        List<Theme> themesSource = service.getThemes().subList(0, 5);
        List<Theme> themesTarget = new ArrayList<Theme>();
        
        themes = new DualListModel<Theme>(themesSource, themesTarget);
        
        lstParentesco = generalList.getParentes();
        lstTipoContato = generalList.getTipo();
        
    }

    public DualListModel<String> getCities() {
        return cities;
    }

    public void setCities(DualListModel<String> cities) {
        this.cities = cities;
    }

    public ThemeService getService() {
        return service;
    }

    public void setService(ThemeService service) {
        this.service = service;
    }

    public DualListModel<Theme> getThemes() {
        return themes;
    }

    public void setThemes(DualListModel<Theme> themes) {
        this.themes = themes;
    }
    
    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append(((Theme) item).getName()).append("<br />");
        }
        
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
        
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }  
    
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
    
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
    
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }

	public GeneralListService getGeneralList() {
		return generalList;
	}

	public void setGeneralList(GeneralListService generalList) {
		this.generalList = generalList;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public List<Parentesco> getLstParentesco() {
		return lstParentesco;
	}

	public void setLstParentesco(List<Parentesco> lstParentesco) {
		this.lstParentesco = lstParentesco;
	}

	public List<TipoContato> getLstTipoContato() {
		return lstTipoContato;
	}

	public void setLstTipoContato(List<TipoContato> lstTipoContato) {
		this.lstTipoContato = lstTipoContato;
	}

	public String getTipoContato() {
		return tipoContato;
	}

	public void setTipoContato(String tipoContato) {
		this.tipoContato = tipoContato;
	}
}
