package br.com.global5.infra.converter;

import br.com.global5.manager.model.cadastro.ReferenciaRespostas;
import sun.awt.image.ImageWatched;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.LinkedHashMap;

/**
 * Created by zielinski on 23/05/17.
 */
@FacesConverter(value = "respConverter")
public class RespostaConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {
        return s;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {

        try {
            LinkedHashMap<String, String> refp =  (LinkedHashMap) o;
            return String.valueOf(refp.get("Avaliacao"));

        } catch (Exception e) {
            return null;
        }

    }


}
