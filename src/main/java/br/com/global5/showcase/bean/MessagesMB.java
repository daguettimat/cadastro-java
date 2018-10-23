package br.com.global5.showcase.bean;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.inject.Named;
import java.io.Serializable;

/**
 * Created by j r zielinski on 14/01/17.
 */
@Named
@ViewScoped
public class MessagesMB implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5542391957005079135L;

	public void info() {
        Messages.create("Info").detail("AdminFaces info message.").add();
    }

    public void warn() {
        Messages.create("Warning!").warn().detail("AdminFaces Warning message.").add();
    }

    public void error() {
        Messages.create("Error!").error().detail("AdminFaces Error message.").add();
    }

    public void fatal() {
        Messages.create("Fatal!").fatal().detail("AdminFaces Fatal message.").add();
    }
}
