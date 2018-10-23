package br.com.global5.infra.converter;

import br.com.global5.infra.model.BaseEntity;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.util.Map;

/**
 * Created by zielinski on 16/05/17.
 */
@FacesConverter(value = "simpleEntityConverter")
public class SimpleEntityConverter implements Converter {

    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            return this.getAttributesFrom(component).get(value);
        }
        return null;
    }

    public String getAsString(FacesContext ctx, UIComponent component, Object value) {

        try {
            BaseEntity entity = (BaseEntity) value;
            // adiciona item como atributo do componente
            this.addAttribute(component, entity);

            Integer codigo = entity.getId();
            if (codigo != null) {
                return String.valueOf(codigo);
            } else {
                return null;
            }
        } catch( Exception e ) {
            return null;
        }


    }

    protected void addAttribute(UIComponent component, BaseEntity o) {
        String key = o.getId().toString(); // codigo da empresa como chave neste caso
        this.getAttributesFrom(component).put(key, o);
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }

}
