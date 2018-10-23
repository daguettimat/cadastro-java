package br.com.global5.infra.converter;

import br.com.global5.infra.util.AppUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public class CPFConverter implements Converter {
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2)
            throws ConverterException {
        return arg2;
    }
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ConverterException {
        if(arg2 == null){
            return "";
        }
        return AppUtils.formataCpf(arg2.toString());
    }
}
