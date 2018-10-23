
package br.com.global5.showcase.bean;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.TreeNode;

import br.com.global5.showcase.service.DocumentService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class IconMB implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -2076035549346113538L;

	private TreeNode root;
    
    @Inject
    private DocumentService service;
    
    @PostConstruct
    public void init() {
        root = service.createDocuments();
    }

    public void setService(DocumentService service) {
        this.service = service;
    }

    public TreeNode getRoot() {
        return root;
    }
}
